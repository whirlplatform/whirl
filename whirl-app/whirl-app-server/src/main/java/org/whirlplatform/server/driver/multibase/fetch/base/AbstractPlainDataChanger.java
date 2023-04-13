package org.whirlplatform.server.driver.multibase.fetch.base;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.empire.data.DataType;
import org.apache.empire.db.DBBlobData;
import org.apache.empire.db.DBColumn;
import org.apache.empire.db.DBCommand;
import org.apache.empire.db.DBDatabase;
import org.apache.empire.db.DBReader;
import org.apache.empire.db.DBRecord;
import org.apache.empire.db.DBTable;
import org.apache.empire.db.DBTableColumn;
import org.apache.empire.db.expr.compare.DBCompareExpr;
import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.DataModifyConfig;
import org.whirlplatform.meta.shared.FileValue;
import org.whirlplatform.meta.shared.data.ListModelData;
import org.whirlplatform.meta.shared.data.RowModelData;
import org.whirlplatform.meta.shared.data.RowModelDataImpl;
import org.whirlplatform.meta.shared.editor.db.PlainTableElement;
import org.whirlplatform.meta.shared.editor.db.TableColumnElement;
import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.driver.multibase.fetch.AbstractMultiFetcher;
import org.whirlplatform.server.driver.multibase.fetch.DataChanger;
import org.whirlplatform.server.driver.multibase.fetch.DataSourceDriver;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;
import org.whirlplatform.server.utils.TypesUtil;

public abstract class AbstractPlainDataChanger extends AbstractMultiFetcher
    implements DataChanger<PlainTableElement> {

    private static Logger _log = LoggerFactory.getLogger(AbstractPlainDataChanger.class);

    public AbstractPlainDataChanger(ConnectionWrapper connectionWrapper, DataSourceDriver factory) {
        super(connectionWrapper, factory);
    }

    protected abstract String getNextId();

    @Override
    public RowModelData insert(ClassMetadata metadata, DataModifyConfig config,
                               PlainTableElement table) {
        // TODO: Нужна загрузка значений по умолчанию?

        RowModelData model = config.getModels().get(0);

        String id;
        if (model.getId() == null || model.getId().startsWith("temp")) {
            id = getNextId();
        } else {
            id = model.getId();
        }
        DBDatabase database = createAndOpenDatabase(table.getSchema().getSchemaName());
        DBTable dbTable = new DBTable(table.getTableName(), database);

        for (TableColumnElement c : table.getColumns()) {
            if (c.getType() == org.whirlplatform.meta.shared.data.DataType.FILE) {
                dbTable.addColumn(c.getColumnName(), DataType.BLOB, 0, c.isNotNull());
                dbTable.addColumn(c.getLabelExpression(), DataType.TEXT,
                    c.getSize() == null ? 0 : c.getSize(), c.isNotNull());
            } else {
                org.whirlplatform.meta.shared.data.DataType dataType =
                    (c.getListTable()) == null ? null
                        : getDataSourceDriver().createDataFetcher(c.getListTable())
                        .getIdColumnType(table);
                Integer dataSize = (c.getSize() == null) ? 0 : c.getSize();
                DataType empireType = TypesUtil.toEmpireType(c.getType(), dataType);
                dbTable.addColumn(c.getColumnName(), empireType, dataSize, c.isNotNull());
            }
        }

        DBRecord record = new DBRecord();
        record.create(dbTable);

        // т.к. размер поля по умолчанию - 0, и возникает ошибка при попытке
        // записать данные
        // TODO: По умолчанию ставить размер поля > 0 ?
        record.setValidateFieldValues(false);

        record.setValue(dbTable.getColumn(table.getIdColumn().getColumnName()), id);
        for (String f : model.getPropertyNames()) {
            TableColumnElement c = table.getColumn(f);

            if (c.getType() == org.whirlplatform.meta.shared.data.DataType.FILE) {
                FileValue fileValue = model.get(f);

                // Get rid of the 'fakepath'
                String fname = fileValue.getName();
                Integer index = fname.indexOf("fakepath");
                fname = fname.substring(index+9, fname.length());
                fileValue.setName(fname);

                DBBlobData blob = new DBBlobData((InputStream) fileValue.getInputStream(),
                    (int) fileValue.getSize());
                record.setValue(dbTable.getColumn(f), blob);
                record.setValue(dbTable.getColumn(c.getLabelExpression()),
                    ((FileValue) model.get(f)).getName());
            } else if (c.getType() == org.whirlplatform.meta.shared.data.DataType.LIST) {
                record.setValue(dbTable.getColumn(f),
                    model.get(f) == null ? null : ((ListModelData) model.get(f)).getId());
            } else {
                record.setValue(dbTable.getColumn(f), model.get(f));
            }
        }
        record.update(getConnection());
        record.close();

        // TODO read from database
        return model;
    }

    @Override
    public RowModelData update(ClassMetadata metadata, DataModifyConfig config,
                               PlainTableElement table) {
        RowModelData model = config.getModels().get(0);
        RowModelData oldValues = new RowModelDataImpl();
        List<DBColumn> columns = new ArrayList<DBColumn>();
        DBDatabase database = createAndOpenDatabase(table.getSchema().getSchemaName());
        DBTable dbTable = new DBTable(table.getTableName(), database);

        TableColumnElement idColumn = table.getIdColumn();
        DBTableColumn dbIdColumn = dbTable.addColumn(idColumn.getColumnName(),
            TypesUtil.toEmpireType(idColumn.getType(),
                (idColumn.getListTable() == null) ? null
                    : getDataSourceDriver().createDataFetcher(idColumn.getListTable())
                    .getIdColumnType(table)),
            idColumn.getSize() == null ? 0 : idColumn.getSize(), idColumn.isNotNull());

        DBCommand selectCmd = database.createCommand();
        DBCommand updateCmd = database.createCommand();
        for (String f : model.getPropertyNames()) {
            if (!model.hasChanged(f)) {
                continue;
            }
            TableColumnElement c = table.getColumn(f);

            if (c.getType() == org.whirlplatform.meta.shared.data.DataType.FILE) {
                FileValue fileValue = model.get(f);
                DBTableColumn dbColumn = dbTable.addColumn(f, DataType.BLOB, 0, c.isNotNull());
                DBBlobData blob = new DBBlobData((InputStream) fileValue.getInputStream(),
                    (int) fileValue.getSize());
                updateCmd.set(dbColumn.to(blob));
                DBTableColumn fileNameColumn =
                    dbTable.addColumn(c.getLabelExpression(), DataType.TEXT,
                        c.getSize() == null ? 0 : c.getSize(), c.isNotNull());
                selectCmd.select(fileNameColumn);
                updateCmd.set(fileNameColumn.to(((FileValue) model.get(f)).getName()));
            } else {
                DBTableColumn dbColumn = dbTable.addColumn(f,
                    TypesUtil.toEmpireType(c.getType(),
                        c.getListTable() == null ? null
                            : getDataSourceDriver().createDataFetcher(c.getListTable())
                            .getIdColumnType(table)),
                    c.getSize() == null ? 0 : c.getSize(), c.isNotNull());
                columns.add(dbColumn);
                selectCmd.select(dbColumn);
                if (c.getType() == org.whirlplatform.meta.shared.data.DataType.LIST) {
                    ListModelData v = model.get(f);
                    updateCmd.set(dbColumn.to(v == null ? null : v.getId()));
                } else {
                    updateCmd.set(dbColumn.to(model.get(f)));
                }
            }
        }
        if (!selectCmd.isValid()) {
            // TODO читать из базы
            return model; // Нет измененных данных
        }
        selectCmd.where(dbIdColumn.is(model.getId()));
        updateCmd.where(dbIdColumn.is(model.getId()));

        DBReader reader = new DBReader();
        reader.open(selectCmd, getConnection());
        if (reader.moveNext()) {
            for (DBColumn c : columns) {
                oldValues.set(c.getName(), reader.getValue(c));
            }
        }

        int rows = database.executeUpdate(updateCmd, getConnection());
        // TODO read from database
        return model;
    }

    @Override
    public void delete(ClassMetadata metadata, DataModifyConfig config, PlainTableElement table) {
        List<RowModelData> models = config.getModels();
        DBDatabase database = createAndOpenDatabase(table.getSchema().getSchemaName());
        DBTable dbTable = new DBTable(table.getTableName(), database);

        TableColumnElement idColumn = table.getIdColumn();
        DBTableColumn dbIdColumn = dbTable.addColumn(idColumn.getColumnName(),
            TypesUtil.toEmpireType(idColumn.getType(),
                idColumn.getListTable() == null ? null
                    : getDataSourceDriver().createDataFetcher(idColumn.getListTable())
                    .getIdColumnType(table)),
            idColumn.getSize() == null ? 0 : idColumn.getSize(), idColumn.isNotNull());

        DBCommand cmd = database.createCommand();

        DBCompareExpr orExpr = null;
        for (RowModelData m : models) {
            if (!m.isDeletable()) {
                continue;
            }
            if (orExpr == null) {
                orExpr = dbIdColumn.is(m.getId());
            } else {
                orExpr = orExpr.or(dbIdColumn.is(m.getId()));
            }
        }
        cmd.where(orExpr);
        // Если deleteColumn задан, то вместо удаления строки ставим в поле true
        TableColumnElement deleteColumnEl = table.getDeleteColumn();
        if (deleteColumnEl != null) {
            DBTableColumn deleteColumn =
                dbTable.addColumn(deleteColumnEl.getColumnName(), DataType.BOOL,
                    deleteColumnEl.getSize() == null ? 0 : deleteColumnEl.getSize(),
                    deleteColumnEl.isNotNull());
            cmd.set(deleteColumn.to(true));
            database.executeUpdate(cmd, getConnection());
        } else {
            database.executeDelete(dbTable, cmd, getConnection());
        }
    }
}
