package org.whirlplatform.server.driver.multibase.fetch.base;

import org.apache.empire.db.DBColumnExpr;
import org.whirlplatform.meta.shared.ClassLoadConfig;
import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.TreeClassLoadConfig;
import org.whirlplatform.meta.shared.editor.db.PlainTableElement;
import org.whirlplatform.meta.shared.editor.db.TableColumnElement;
import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.driver.multibase.fetch.DataSourceDriver;

public class PlainTreeFetcherHelper extends PlainTableFetcherHelper {
    public DBColumnExpr dbIsSelectExpression;
    public DBColumnExpr dbLeafExpression;
    public DBColumnExpr dbStateExpression;
    public DBColumnExpr dbCheckExpression;
    public DBColumnExpr dbNameExpression;
    public DBColumnExpr dbParentColumn;
    public DBColumnExpr imageColumn;
    public TableColumnElement nameExpressionColumn;

    public PlainTreeFetcherHelper(ConnectionWrapper connectionWrapper, DataSourceDriver factory) {
        super(connectionWrapper, factory);
    }

    @Override
    public void prepare(ClassMetadata metadata, PlainTableElement table, ClassLoadConfig loadConfig) {
        prepare(metadata, table, loadConfig, true);
        if (loadConfig instanceof TreeClassLoadConfig) {
            nameExpressionColumn = table.getColumn(((TreeClassLoadConfig) loadConfig).getNameExpression());
        }
    }

    /*
     * Getters and setters
     */

    public DBColumnExpr getDbIsSelectExpression() {
        return dbIsSelectExpression;
    }

    public void setDbIsSelectExpression(DBColumnExpr dbIsSelectExpression) {
        this.dbIsSelectExpression = dbIsSelectExpression;
    }

    public DBColumnExpr getDbLeafExpression() {
        return dbLeafExpression;
    }

    public void setDbLeafExpression(DBColumnExpr dbLeafExpression) {
        this.dbLeafExpression = dbLeafExpression;
    }

    public DBColumnExpr getDbStateExpression() {
        return dbStateExpression;
    }

    public void setDbStateExpression(DBColumnExpr dbStateExpression) {
        this.dbStateExpression = dbStateExpression;
    }

    public DBColumnExpr getDbCheckExpression() {
        return dbCheckExpression;
    }

    public void setDbCheckExpression(DBColumnExpr dbCheckExpression) {
        this.dbCheckExpression = dbCheckExpression;
    }

    public DBColumnExpr getDbNameExpression() {
        return dbNameExpression;
    }

    public void setDbNameExpression(DBColumnExpr dbNameExpression) {
        this.dbNameExpression = dbNameExpression;
    }

    public DBColumnExpr getDbParentColumn() {
        return dbParentColumn;
    }

    public void setDbParentColumn(DBColumnExpr dbParentColumn) {
        this.dbParentColumn = dbParentColumn;
    }

    public DBColumnExpr getImageColumn() {
        return imageColumn;
    }

    public void setImageColumn(DBColumnExpr imageColumn) {
        this.imageColumn = imageColumn;
    }

    public TableColumnElement getNameExpressionColumn() {
        return nameExpressionColumn;
    }

    public void setNameExpressionColumn(TableColumnElement nameExpressionColumn) {
        this.nameExpressionColumn = nameExpressionColumn;
    }
}
