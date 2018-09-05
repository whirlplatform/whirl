package org.whirlplatform.integration.db;

import org.apache.commons.lang3.StringUtils;
import org.whirlplatform.integration.grid.GridTestRowModel;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GridDataExtractor {
    private final String DATE_FORMAT = "dd.MM.yyyy HH:mm:ss";
    private String queryString;
    private Map<Integer, GridTestRowModel> rows;
    private List<String> colNames;
    private List<String> colTypes;
    private DBConfig config;

    @Deprecated
    public GridDataExtractor() {
        rows = new HashMap<>();
        colNames = new ArrayList<>();
        colTypes = new ArrayList<>();
        initConfig();
    }

    public GridDataExtractor(DBConfig config) {
        rows = new HashMap<>();
        colNames = new ArrayList<>();
        colTypes = new ArrayList<>();
        this.config = config;
    }

    public Map<Integer, GridTestRowModel> extractDbData(final String query) {
        clearData();
        setQueryString(query);
        return extractDbData();
    }

    private Map<Integer, GridTestRowModel> extractDbData() {
        try (Connection connection = DriverManager.getConnection(config.getJdbcURL(), config.getJdbcUser(),
                config.getJdbcPwd())) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(queryString);
            ResultSetMetaData metaData = resultSet.getMetaData();
            int colsCount = metaData.getColumnCount();
            for (int i = 1; i <= colsCount; i++) {
                colNames.add(metaData.getColumnName(i));
                colTypes.add(metaData.getColumnTypeName(i));
            }
            DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
            while (resultSet.next()) {
                GridTestRowModel rowModel = new GridTestRowModel();
                for (int i = 1; i <= colsCount; i++) {
                    if (i == 1) {
                        rowModel.setId(resultSet.getInt(i));
                    }
                    String resultValue = "";
                    final String typeName = colTypes.get(i - 1);
                    if ("DATE".equals(typeName) || "TIMESTAMP".equals(typeName)) {
                        if (resultSet.getTimestamp(i) != null) {
                            resultValue = dateFormat.format(resultSet.getTimestamp(i));
                        }
                    } else {
                        String value = resultSet.getString(i);
                        if (value != null) {
                            resultValue = ("NUMBER".equals(typeName)) ? value.replace(".", ",") : value;
                        }
                    }
                    rowModel.addValue(metaData.getColumnName(i), resultValue);
                }
                rows.put(rowModel.getId(), rowModel);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return rows;
    }

    public void execute(final String query) {
        try (Connection connection = DriverManager.getConnection(config.getJdbcURL(), config.getJdbcUser(),
                config.getJdbcPwd())) {
            Statement statement = connection.createStatement();
            statement.execute(query);
        } catch (SQLException e) {
            System.out.printf("Execute query error: %s", e.getMessage());
        }
    }

    private void initConfig() {
        config = new DBConfig();
        config.init("dbconfig.xml", true);
        System.out.println("Init config : ");
        System.out.println(config);
    }

    private void clearData() {
        rows.clear();
        colNames.clear();
        colTypes.clear();
    }

    public Map<Integer, GridTestRowModel> getRowModels() {
        return rows;
    }

    public List<String> getColumnNames() {
        return colNames;
    }

    public List<String> getColumnTypeNames() {
        return colTypes;
    }

    public void setQueryString(final String queryString) {
        if (StringUtils.isEmpty(queryString)) {
            throw new IllegalArgumentException("The query string should not be empty");
        }
        this.queryString = queryString;
    }

    public boolean containsId(Integer id) {
        return rows.containsKey(id);
    }

    public GridTestRowModel getRowModel(Integer id) {
        return rows.get(id);
    }
}
