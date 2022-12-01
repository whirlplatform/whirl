package org.whirlplatform.integration.db;

import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.empire.db.DBCmdType;
import org.apache.empire.db.DBDatabaseDriver;
import org.apache.empire.db.DBSQLScript;
import org.apache.empire.db.DBTable;
import org.apache.empire.db.DBView;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;

public final class DBInitUtil {

    public static final DBConfig CONFIG = initConfig();

    private DBInitUtil() {

    }

    private static DBConfig initConfig() {
        DBConfig config = new DBConfig();
        config.init("dbconfig.xml", true);
        System.out.println("Init config : ");
        System.out.println(config);
        return config;
    }

    public static void prepareDB() {
        try {
            Class.forName(CONFIG.getJdbcClass());
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        TestTableSet db = TestTableSet.get();
        try (Connection connection = DriverManager.getConnection(CONFIG.getJdbcURL(),
                CONFIG.getJdbcUser(),
                CONFIG.getJdbcPwd())) {
            String schemaName = CONFIG.getSchemaName().toUpperCase();
            db.setSchema(schemaName);
            assertTrue(!connection.isClosed());
            DBDatabaseDriver driver = null;
            try {
                driver = (DBDatabaseDriver) Class.forName(CONFIG.getEmpireDBDriverClass())
                        .newInstance();
            } catch (ClassNotFoundException e) {
                System.out.println(e.getMessage());
            }
            db.open(driver, connection);
            createTables(db, connection);
            IDataSet dataSet = loadDataSet();
            assertTrue(dataSet != null);
            IDatabaseConnection iDatabaseConnection =
                    new DatabaseConnection(connection, schemaName);
            DatabaseOperation.CLEAN_INSERT.execute(iDatabaseConnection, dataSet);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (InstantiationException e) {
            System.out.println(e.getMessage());
        } catch (IllegalAccessException e) {
            System.out.println(e.getMessage());
        } catch (DatabaseUnitException e) {
            System.out.println(e.getMessage());
        }
    }

    private static boolean isTableExist(DBTable table, String schema, Connection conn) {
        DatabaseMetaData dbm;
        try {
            dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null, schema, table.getName(), null);
            return tables.next();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    private static void createTables(TestTableSet db, Connection conn) {
        System.out.println("Create tables");
        DBSQLScript dbsqlScript = new DBSQLScript();
        db.getCreateDDLScript(db.getDriver(), dbsqlScript);
        System.out.println(dbsqlScript);
        dbsqlScript.executeAll(db.getDriver(), conn, false);
        db.commit(conn);
    }

    public static void cleanDB(TestTableSet db) {
        try (Connection connection = DriverManager.getConnection(CONFIG.getJdbcURL(),
                CONFIG.getJdbcUser(),
                CONFIG.getJdbcPwd())) {
            DBSQLScript script = new DBSQLScript();
            DBDatabaseDriver driver = db.getDriver();
            for (DBView view : db.getViews()) {
                driver.getDDLScript(DBCmdType.DROP, view, script);
            }
            for (DBTable table : db.getTables()) {
                driver.getDDLScript(DBCmdType.DROP, table, script);
            }

            for (int i = 0; i < script.getCount(); i++) {
                String stmt = script.getStmt(i);
                stmt = stmt.replaceAll("\"", "");
                script.replaceStmt(i, stmt);
            }
            System.out.println(script);
            // Бага в empire-db:
            // https://issues.apache.org/jira/browse/EMPIREDB-265
            script.executeAll(driver, connection, false);
            connection.commit();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static IDataSet loadDataSet() {
        System.out.println("Load dataSet");
        try {
            FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
            // IDataSet dataSet = builder.build(new
            // FileInputStream("src/test/resources/dataset/db_dataset.xml"));
            IDataSet dataSet = builder
                    .build(DBInitUtil.class.getClassLoader()
                            .getResourceAsStream("dataset/db_dataset.xml"));
            return dataSet;
        } catch (DataSetException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

}
