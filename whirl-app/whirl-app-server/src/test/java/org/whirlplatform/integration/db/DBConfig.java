package org.whirlplatform.integration.db;

import org.apache.empire.xml.XMLConfiguration;

public class DBConfig extends XMLConfiguration {

    private String databaseProvider = "postgres";
    private String jdbcClass;
    private String jdbcURL;
    private String jdbcUser;
    private String jdbcPwd;
    private String schemaName;
    private String empireDBDriverClass;

    public void init(String filename, boolean fromResource) {
        super.init(filename, fromResource);
        readProperties(this, "properties");
        readProperties(this, "properties-" + databaseProvider);
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("-----------------------------------------------------" + '\n');
        str.append("databaseProvide  " + '\t' + databaseProvider + '\n');
        str.append("jdbcClass  " + '\t' + '\t' + jdbcClass + '\n');
        str.append("jdbcURL  " + '\t' + '\t' + jdbcURL + '\n');
        str.append("jdbcUser  " + '\t' + '\t' + jdbcUser + '\n');
        str.append("jdbcPwd  " + '\t' + '\t' + jdbcPwd + '\n');
        str.append("schemaName  " + '\t' + '\t' + schemaName + '\n');
        str.append("empireDBDriverClass  " + '\t' + empireDBDriverClass + '\n');
        str.append("--------------------------------------------------------" + '\n');
        return str.toString();
    }

    public String getDatabaseProvider() {
        return databaseProvider;
    }

    public void setDatabaseProvider(String databaseProvider) {
        this.databaseProvider = databaseProvider;
    }

    public String getJdbcClass() {
        return jdbcClass;
    }

    public void setJdbcClass(String jdbcClass) {
        this.jdbcClass = jdbcClass;
    }

    public String getJdbcPwd() {
        return jdbcPwd;
    }

    public void setJdbcPwd(String jdbcPwd) {
        this.jdbcPwd = jdbcPwd;
    }

    public String getJdbcURL() {
        return jdbcURL;
    }

    public void setJdbcURL(String jdbcURL) {
        this.jdbcURL = jdbcURL;
    }

    public String getJdbcUser() {
        return jdbcUser;
    }

    public void setJdbcUser(String jdbcUser) {
        this.jdbcUser = jdbcUser;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getEmpireDBDriverClass() {
        return empireDBDriverClass;
    }

    public void setEmpireDBDriverClass(String dBDriverClass) {
        empireDBDriverClass = dBDriverClass;
    }
}
