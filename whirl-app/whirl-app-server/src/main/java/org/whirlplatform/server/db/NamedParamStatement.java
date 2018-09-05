package org.whirlplatform.server.db;

import org.apache.empire.db.DBDatabaseDriver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public class NamedParamStatement extends NamedParamResolver {

	PreparedStatement stmt;

	public NamedParamStatement(DBDatabaseDriver driver, Connection conn, String sql) throws SQLException {
		super(driver, sql);
		stmt = conn.prepareStatement(getResultSql());
	}

	public PreparedStatement getStatement() {
		return stmt;
	}

	public Set<String> getParameterNames() {
		return paramMap.keySet();
	}

	public void setObject(String name, Object value) throws SQLException {
		List<Integer> indList = paramMap.get(name.toUpperCase());
		if (indList != null)
			for (Integer i : indList) {
				stmt.setObject(i, value);
			}
	}

	public void setString(String name, String value) throws SQLException {
		List<Integer> indList = paramMap.get(name.toUpperCase());
		if (indList != null) {
			for (Integer i : indList) {
				stmt.setString(i, value);
			}
		}
	}

	public void setInt(String name, Integer value) throws SQLException {
		List<Integer> indList = paramMap.get(name.toUpperCase());
		if (indList != null)
			for (Integer i : indList) {
				stmt.setInt(i, value);
			}
	}

}
