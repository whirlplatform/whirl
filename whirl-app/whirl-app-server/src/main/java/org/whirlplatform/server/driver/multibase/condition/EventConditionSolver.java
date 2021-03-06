package org.whirlplatform.server.driver.multibase.condition;

import org.apache.empire.data.DataType;
import org.apache.empire.db.DBDatabaseDriver;
import org.apache.empire.db.derby.DBDatabaseDriverDerby;
import org.apache.empire.db.oracle.DBDatabaseDriverOracle;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.data.DataValueImpl;
import org.whirlplatform.meta.shared.editor.*;
import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.db.NamedParamResolver;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;
import org.whirlplatform.server.login.ApplicationUser;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@SuppressWarnings("serial")
public class EventConditionSolver extends AbstractConditionSolver {

	private static Logger _log = LoggerFactory.getLogger(EventConditionSolver.class);

	private EventElement event;
	private ApplicationElement application;
	private ApplicationUser user;
	private Collection<DataValue> params;
	private ConnectionWrapper connection;
	private boolean allowed = false;

	private List<SQLCondition> sqlConditions = new ArrayList<>();
	private List<String> sqlColumnEventAccess = new ArrayList<>();

	public EventConditionSolver(EventElement event, ApplicationElement application, Collection<DataValue> params,
			ApplicationUser user, ConnectionWrapper connection) {
		this.connection = connection;
		this.event = event;
		this.application = application;
		this.params = params;
		this.user = user;
	}

	public boolean needNext() {
		return !allowed;
	}

	@Override
	public void solve(BooleanCondition condition) {
		allowed = allowed || condition.getValue();
	}

	@Override
	public void solve(SQLCondition condition) {
		sqlConditions.add(condition);
	}

	@Override
	public boolean allowed() {
		// собираем все права
		Set<RightElement> rights = new HashSet<RightElement>();
		RightCollectionElement right = application.getEventRights(event);
		for (String g : user.getGroups()) {
			rights.addAll(right.getGroupRights(application.getGroup(g)));
		}
		if (rights.isEmpty()) {
			rights.addAll(right.getApplicationRights());
		}
		checkAllowed(rights, RightType.EXECUTE);

		// если права еще нет, то будем проверять вычисляемые
		if (!allowed) {
			try {
				checkSqlConditions();
			} catch (SQLException e) {
				_log.error(e);
			}
		}
		return allowed;
	}

	private void checkSqlConditions() throws SQLException {
		if (sqlConditions.isEmpty()) {
			return;
		}

		List<String> subColumns = new ArrayList<String>();
		List<String> subQueries = new ArrayList<String>();

		sqlColumnEventAccess.addAll(createConditionSubQueries("EX_", "EVENT_", sqlConditions, subColumns, subQueries));

		// если нет ни одного условия, хотя они должны быть так как мы уже
		// проверили вначале?
		if (subQueries.size() == 0) {
			return;
		}

		String query = makeQuery(subQueries);
		_log.info(query);

		try (ResultSet rs = connection.getDatabaseDriver().executeQuery(query, null, false, connection)) {
			if (rs.next()) {
				// достаем вычисленные значения
				for (String col : sqlColumnEventAccess) {
					allowed = allowed || getSubQueryValue(rs, subColumns.indexOf(col) + 1);
				}
			}
		}
	}

	private boolean getSubQueryValue(ResultSet rs, int colIndex) throws SQLException {
		DBDatabaseDriver driver = connection.getDatabaseDriver();
		Boolean result = (Boolean) driver.getResultValue(rs, colIndex, DataType.BOOL);
		if (result == null) {
			return false;
		}
		return result;
	}

	private String makeQuery(Collection<String> subQueries) {
		DBDatabaseDriver driver = connection.getDatabaseDriver();

		StringBuilder result = new StringBuilder();
		result.append("select ");

		for (String q : subQueries) {
			result.append(q + " ,");
		}
		result.deleteCharAt(result.length() - 1);

		if (driver instanceof DBDatabaseDriverOracle) {
			result.append("from DUAL");
		} else if (driver instanceof DBDatabaseDriverDerby) {
			result.append("from SYSIBM.SYSDUMMY1");
		}
		return result.toString();
	}

	private Collection<String> createConditionSubQueries(String columnPrefix, String objectName,
			Collection<SQLCondition> list, List<String> subColumns, List<String> subQueries) {
		Set<String> result = new HashSet<String>();
		DBDatabaseDriver driver = connection.getDatabaseDriver();
		int index = 0;
		for (SQLCondition condition : list) {
			String columnName = columnPrefix + objectName + index;
			String resolvedValue = resolveValue(driver, condition.getValue(), objectName);
			String q = "(" + resolvedValue + ")" + driver.getSQLPhrase(DBDatabaseDriver.SQL_RENAME_COLUMN) + columnName;
			subColumns.add(columnName);
			subQueries.add(q);
			result.add(columnName);
			index++;
		}
		return result;
	}

	protected String resolveValue(DBDatabaseDriver driver, String value, String objectName) {
		if (value == null) {
			return null;
		}
		NamedParamResolver resolver = new NamedParamResolver(driver, value, processParams(params, objectName));
		return resolver.getResultSql();
	}

	protected Map<String, DataValue> processParams(Collection<DataValue> paramMap, String eventCode) {
		ApplicationUser user = connection.getUser();
		Map<String, DataValue> result = new HashMap<String, DataValue>();

        DataValue data = new DataValueImpl(org.whirlplatform.meta.shared.data.DataType.STRING);
		data.setCode("PFUSER");
		data.setValue(user.getId());
		result.put(data.getCode(), data);

        data = new DataValueImpl(org.whirlplatform.meta.shared.data.DataType.STRING);
		data.setCode("PFIP");
		data.setValue(user.getIp());
		result.put(data.getCode(), data);

        data = new DataValueImpl(org.whirlplatform.meta.shared.data.DataType.STRING);
		data.setCode("PFROLE");
		data.setValue(user.getApplication().getId());
		result.put(data.getCode(), data);

        data = new DataValueImpl(org.whirlplatform.meta.shared.data.DataType.STRING);
		data.setCode("EVENT_CODE");
		data.setValue(eventCode);
		result.put(data.getCode(), data);

		if (paramMap == null) {
			return result;
		}
		for (DataValue entry : paramMap) {
			result.put(entry.getCode(), entry);
		}

		return result;
	}

}
