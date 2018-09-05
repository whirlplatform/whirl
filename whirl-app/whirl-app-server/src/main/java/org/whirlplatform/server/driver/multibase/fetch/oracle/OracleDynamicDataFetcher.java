package org.whirlplatform.server.driver.multibase.fetch.oracle;

import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.i18n.shared.DefaultDateTimeFormatInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.empire.db.DBReader;
import org.whirlplatform.meta.shared.*;
import org.whirlplatform.meta.shared.data.*;
import org.whirlplatform.meta.shared.editor.*;
import org.whirlplatform.meta.shared.editor.db.DynamicTableElement;
import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.driver.multibase.fetch.DataFetcher;
import org.whirlplatform.server.driver.multibase.fetch.DataSourceDriver;
import org.whirlplatform.server.driver.multibase.fetch.base.AbstractDynamicDataFetcher;
import org.whirlplatform.server.global.SrvConstant;
import org.whirlplatform.server.login.ApplicationUser;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class OracleDynamicDataFetcher extends AbstractDynamicDataFetcher implements DataFetcher<DynamicTableElement> {

	public OracleDynamicDataFetcher(ConnectionWrapper connection, DataSourceDriver factory) {
		super(connection, factory);
	}

	@Override
	public DataType getIdColumnType(DynamicTableElement table) {
		return null;
	}

	@Override
	public DBReader getTableReader(ClassMetadata metadata, DynamicTableElement table, ClassLoadConfig loadConfig) {
		return null;
	}

	protected void setModelValue(RowModelData model, FieldMetadata field, ResultSet resultSet) throws SQLException {
		if (!StringUtils.isEmpty(field.getViewFormat())) {
			String value = null;
			String objValue = null;
			Map<String, String> formattedMap;
			// Для списков стиль задается в поле ..dfname
            if (field.getType() == DataType.LIST) {
				// DBColumnExpr labelCol = temp.dbTable.getColumn(field
				// .getName() + SrvConstant.COLUMN_LIST_POSTFIX);
				formattedMap = fromUrlEncoded(resultSet.getString(field.getName() + SrvConstant.COLUMN_LIST_POSTFIX));
				value = formattedMap.get(SrvConstant.VALUE);
				objValue = resultSet.getString(field.getName());
			} else {
				formattedMap = fromUrlEncoded(resultSet.getString(field.getName()));
				value = formattedMap.get(SrvConstant.VALUE);
			}
			model.setStyle(field.getName(), formattedMap.get(SrvConstant.STYLE));
            if (field.getType() == DataType.DATE && value != null) {
				DateTimeFormat fmt = new DateTimeFormat("dd.MM.yyyy hh:mm:ss", new DefaultDateTimeFormatInfo()) {
				};
				model.set(field.getName(), fmt.parse(value));
			} else {
				model.set(field.getName(), convertValueFromString(value, objValue, field.getType()));
			}
		} else {
			if (field.getType() != null) {
				switch (field.getType()) {
				case STRING:
					model.set(field.getName(), resultSet.getString(field.getName()));
					break;
				case NUMBER:
					// getDouble преобразует null в 0
					model.set(field.getName(), resultSet.getObject(field.getName()));
					break;
				case DATE:
					model.set(field.getName(), resultSet.getTimestamp(field.getName()));
					break;
				case BOOLEAN:
					final String stringBool = resultSet.getString(field.getName());
					if("T".equalsIgnoreCase(stringBool)) {
						model.set(field.getName(), true);
					} else {
						model.set(field.getName(), false);
					}
					break;
				case LIST:
					ListModelData listValue = new ListModelDataImpl();
					listValue.setLabel(resultSet.getString(field.getName() + SrvConstant.COLUMN_LIST_POSTFIX));
					listValue.setId(resultSet.getString(field.getName()));
					model.set(field.getName(), listValue);
					break;
				case FILE:
					FileValue fileValue = new FileValue();
					fileValue.setName(resultSet.getString(field.getName() + SrvConstant.COLUMN_FILE_POSTFIX));
					model.set(field.getName(), fileValue);
					break;
				default:
					break;
				}
			}
		}
	}

	protected String getFiltersString(ClassLoadConfig config) {
		StringBuilder builder = new StringBuilder();

		// собираем фильтры
		for (FilterValue filter : config.getFilters()) {
			// DBColumnExpr col = temp.dbTable.getColumn(filter.getMetadata()
			// .getName());
			// Object firstValue;
			// if (filter.getMetadata().getType() ==
            // DataType.LIST
			// && filter.getFirstValue() != null) {
			// firstValue = ((ListModelData) filter.getFirstValue()).getId();
			// } else {
			// firstValue = filter.getFirstValue();
			// }
			String expr = null;
			DataValue v = new DataValueImpl(filter.getMetadata().getType());
			v.setValue(filter.getFirstValue());
			switch (filter.getType()) {
			case NO_FILTER:
				break;
			case EQUALS:
				expr = filter.getMetadata().getName() + "=" + escapeParameter(v);
				break;
			case CONTAINS:
				expr = "upper(" + filter.getMetadata().getName() + ") like upper('%'||" + escapeParameter(v) + "||'%')";
				break;
			case NOT_CONTAINS:
				expr = "upper(" + filter.getMetadata().getName() + ") not like upper('%'||" + escapeParameter(v)
						+ "||'%')";
				break;
			case EMPTY:
				expr = filter.getMetadata().getName() + " is null";
				break;
			case NOT_EMPTY:
				expr = filter.getMetadata().getName() + " is not null";
				break;
			case START_WITH:
				expr = "upper(" + filter.getMetadata().getName() + ") like upper(" + escapeParameter(v) + "||'%')";
				break;
			case END_WITH:
				expr = "upper(" + filter.getMetadata().getName() + ") like upper('%'||" + escapeParameter(v) + ")";
				break;
			case LOWER:
				expr = filter.getMetadata().getName() + "<=" + escapeParameter(v);
				break;
			case GREATER:
				expr = filter.getMetadata().getName() + ">=" + escapeParameter(v);
				break;
			case BETWEEN:
				DataValue v2 = new DataValueImpl(filter.getMetadata().getType());
				v2.setValue(filter.getSecondValue());
				expr = filter.getMetadata().getName() + " between " + escapeParameter(v) + " and "
						+ escapeParameter(v2);
				break;
			// Он вообще используется?
			// case REVERSE:
			// expr = createReverse(filter.getMetadata(), firstValue, temp);
			// break;
			case REVERSE:
				break;
			default:
				break;
			}
			if (expr != null) {
				builder.append(expr).append(" and ");
			}
		}
		String result;
		int lastIndex = builder.lastIndexOf(" and ");
		if (lastIndex != -1) {
			result = builder.substring(0, lastIndex);
		} else {
			result = "";
		}
		return result;
	}

	protected String getSortsString(ClassLoadConfig config, ClassMetadata metadata) {
		StringBuilder builder = new StringBuilder();
		if (!config.getSorts().isEmpty()) {
			for (SortValue s : config.getSorts()) {
				// Если тип поля - список, сортировать по строке
                if (DataType.LIST == s.getField().getType()) {
					builder.append(s.getField().getName() + "DFNAME");
				} else {
					builder.append(s.getField().getName());
				}
				if (s.getOrder() == SortType.DESC) {
					builder.append(" DESC");
				}
				builder.append(",");
			}
		}
		builder.append(metadata.getIdField().getName());
		return builder.toString();
	}

	protected String getRightsString(DynamicTableElement table, ApplicationUser user) {
		StringBuilder builder = new StringBuilder();

		RightCollectionElement rightCollection = user.getApplication().getTableRights(table);
		// Добавление ограничений по правам приложения
		for (RightElement right : rightCollection.getApplicationRights()) {
			if (RightType.RESTRICT == right.getType() && right.getCondition() instanceof SQLCondition) {
				String value = (String) right.getCondition().getValue();
				if (value != null && !value.isEmpty()) {
					// String resolvedValue = resolveValue(
					// connection.getDatabaseDriver(), value,
					// processParams(config));
					// DBColumnExpr expr = db.getValueExpr("(" + resolvedValue
					// + ")", DataType.UNKNOWN);
					// result.add(new DBCompareColExpr(expr, DBCmpType.NONE,
					// " "));
					builder.append("(").append(value).append(")").append(" and ");
				}
			}
		}

		// Добавление ограничений по правам пользователя
		for (String groupName : user.getGroups()) {
			GroupElement group = user.getApplication().getGroup(groupName);
			for (RightElement right : rightCollection.getGroupRights(group)) {
				if (RightType.RESTRICT == right.getType() && right.getCondition() instanceof SQLCondition) {
					String value = (String) right.getCondition().getValue();
					if (value != null && !value.isEmpty()) {
						// String resolvedValue =
						// resolveValue(connection.getDatabaseDriver(), value,
						// processParamsSelect(config));
						// DBColumnExpr expr = db.getValueExpr("(" +
						// resolvedValue
						// + ")", DataType.UNKNOWN);
						// result.add(new DBCompareColExpr(expr, DBCmpType.NONE,
						// " "));
						builder.append("(").append(value).append(")").append(" and ");
					}
				}
			}
		}

		return builder.toString();
	}

	private String escapeParameter(DataValue parameter) {
		if (parameter == null) {
			return null;
		}
		String result;
		// // Если параметр - список (например из дерева)
		// if (parameter instanceof RowListValue) {
		// StringBuilder builder = new StringBuilder();
		// for (RowValue row : ((RowListValue) parameter).getRowList()) {
		// if ((!row.isChecked() && ((RowListValue) parameter)
		// .isCheckable())
		// || (!row.isSelected() && !((RowListValue) parameter)
		// .isCheckable())) {
		// continue;
		// }
		// String id = row.getId();
		// if (NumberUtils.isNumber(id)) {
		// id = connection.getDatabaseDriver().getValueString(id,
		// org.apache.empire.data.DataType.FLOAT);
		// } else {
		// id = connection.getDatabaseDriver().getValueString(id,
		// org.apache.empire.data.DataType.TEXT);
		// }
		// builder.append(id);
		// builder.append(",");
		// }
		// if (builder.length() > 0) {
		// result = builder.substring(0, builder.length() - 1);
		// } else {
		// result = null;
		// }
		// return result;
		// }

		ConnectionWrapper connectionWrapper = getConnection();
		switch (parameter.getType()) {
		case BOOLEAN:
			result = connectionWrapper.getDatabaseDriver().getValueString(parameter.getValue(),
					org.apache.empire.data.DataType.BOOL);
			break;
		case DATE:
			result = connectionWrapper.getDatabaseDriver().getValueString(parameter.getValue(),
					org.apache.empire.data.DataType.DATETIME);
			break;
		case NUMBER:
			result = connectionWrapper.getDatabaseDriver().getValueString(parameter.getValue(),
					org.apache.empire.data.DataType.FLOAT);
			break;
		case STRING:
			result = connectionWrapper.getDatabaseDriver().getValueString(parameter.getValue(),
					org.apache.empire.data.DataType.TEXT);
			break;
		case LIST:
			if (parameter.getValue() == null || !(parameter.getValue() instanceof ListModelData)) {
				return null;
			}
			ListModelData list = parameter.getValue();
			String id = list.getId();
			if (NumberUtils.isNumber(id)) {
				id = connectionWrapper.getDatabaseDriver().getValueString(id, org.apache.empire.data.DataType.FLOAT);

			} else {
				id = connectionWrapper.getDatabaseDriver().getValueString(id, org.apache.empire.data.DataType.TEXT);
			}
			result = id;
			break;
		default:
			result = connectionWrapper.getDatabaseDriver().getValueString(parameter.getValue(),
					org.apache.empire.data.DataType.UNKNOWN);
			break;
		}
		return result;
	}
}
