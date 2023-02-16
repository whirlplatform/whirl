package org.whirlplatform.server.db;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;
import org.apache.empire.db.DBDatabaseDriver;
import org.whirlplatform.server.driver.multibase.fetch.HasDataSourceDriver;
import org.whirlplatform.server.login.ApplicationUser;

public abstract class ConnectionWrapper implements Connection, HasDataSourceDriver {

    private String alias;
    private Connection conn;
    private ApplicationUser user;

    public ConnectionWrapper(String alias, Connection connection,
                             ApplicationUser user) {
        this.alias = alias;
        this.conn = connection;
        this.user = user;
    }

    public ConnectionWrapper() {

    }

    public abstract DBDatabaseDriver getDatabaseDriver();


    public String getAlias() {
        return alias;
    }

    // wrapping Connection.class methods

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return conn.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return conn.isWrapperFor(iface);
    }

    @Override
    public Statement createStatement() throws SQLException {
        return conn.createStatement();
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return conn.prepareStatement(sql);
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        return conn.prepareCall(sql);
    }

    @Override
    public String nativeSQL(String sql) throws SQLException {
        return conn.nativeSQL(sql);
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        return conn.getAutoCommit();
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        conn.setAutoCommit(autoCommit);
    }

    @Override
    public void commit() throws SQLException {
        conn.commit();
    }

    @Override
    public void rollback() throws SQLException {
        conn.rollback();
    }

    @Override
    public void close() throws SQLException {
        conn.close();
    }

    @Override
    public boolean isClosed() throws SQLException {
        return conn.isClosed();
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        return conn.getMetaData();
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        return conn.isReadOnly();
    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {
        conn.setReadOnly(readOnly);
    }

    @Override
    public String getCatalog() throws SQLException {
        return conn.getCatalog();
    }

    @Override
    public void setCatalog(String catalog) throws SQLException {
        conn.setCatalog(catalog);
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        return conn.getTransactionIsolation();
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException {
        conn.setTransactionIsolation(level);
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return conn.getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        conn.clearWarnings();
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency)
        throws SQLException {
        return conn.createStatement();
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType,
                                              int resultSetConcurrency) throws SQLException {
        return conn.prepareStatement(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType,
                                         int resultSetConcurrency) throws SQLException {
        return conn.prepareCall(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return conn.getTypeMap();
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        conn.setTypeMap(map);
    }

    @Override
    public int getHoldability() throws SQLException {
        return conn.getHoldability();
    }

    @Override
    public void setHoldability(int holdability) throws SQLException {
        conn.setHoldability(holdability);
    }

    @Override
    public Savepoint setSavepoint() {
        return setSavepoint();
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        return conn.setSavepoint(name);
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        conn.rollback(savepoint);
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        conn.releaseSavepoint(savepoint);
    }

    @Override
    public Statement createStatement(int resultSetType,
                                     int resultSetConcurrency, int resultSetHoldability)
        throws SQLException {
        return conn.createStatement(resultSetType, resultSetConcurrency,
            resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType,
                                              int resultSetConcurrency, int resultSetHoldability)
        throws SQLException {
        return conn.prepareStatement(sql, resultSetType, resultSetConcurrency,
            resultSetHoldability);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType,
                                         int resultSetConcurrency, int resultSetHoldability)
        throws SQLException {
        return conn.prepareCall(sql, resultSetType, resultSetConcurrency,
            resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
        throws SQLException {
        return conn.prepareStatement(sql, autoGeneratedKeys);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
        throws SQLException {
        return conn.prepareStatement(sql, columnIndexes);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) {
        return prepareStatement(sql, columnNames);
    }

    @Override
    public Clob createClob() throws SQLException {
        return conn.createClob();
    }

    @Override
    public Blob createBlob() throws SQLException {
        return conn.createBlob();
    }

    @Override
    public NClob createNClob() throws SQLException {
        return conn.createNClob();
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        return conn.createSQLXML();
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
        return conn.isValid(timeout);
    }

    @Override
    public void setClientInfo(String name, String value)
        throws SQLClientInfoException {
        conn.setClientInfo(name, value);
    }

    @Override
    public String getClientInfo(String name) throws SQLException {
        return conn.getClientInfo(name);
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        return conn.getClientInfo();
    }

    @Override
    public void setClientInfo(Properties properties)
        throws SQLClientInfoException {
        conn.setClientInfo(properties);
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements)
        throws SQLException {
        return conn.createArrayOf(typeName, elements);
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes)
        throws SQLException {
        return conn.createStruct(typeName, attributes);
    }

    @Override
    public String getSchema() throws SQLException {
        return conn.getSchema();
    }

    @Override
    public void setSchema(String schema) throws SQLException {
        conn.setSchema(schema);
    }

    @Override
    public void abort(Executor executor) throws SQLException {
        conn.abort(executor);
    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds)
        throws SQLException {
        conn.setNetworkTimeout(executor, milliseconds);
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        return conn.getNetworkTimeout();
    }

    public ApplicationUser getUser() {
        return user;
    }

}
