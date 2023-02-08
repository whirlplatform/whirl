package org.whirlplatform.server.form;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import org.whirlplatform.meta.shared.component.ComponentModel;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.data.DataValueImpl;
import org.whirlplatform.meta.shared.form.FormCellModel;
import org.whirlplatform.meta.shared.form.FormColumnModel;
import org.whirlplatform.meta.shared.form.FormModel;
import org.whirlplatform.meta.shared.form.FormRowModel;
import org.whirlplatform.server.db.ConnectException;
import org.whirlplatform.server.db.ConnectionProvider;
import org.whirlplatform.server.login.ApplicationUser;

public class ClientFormWriter extends FormWriter {

    private FormModel finalForm;

    public ClientFormWriter(ConnectionProvider connectionProvider, FormElementWrapper form,
                            Collection<DataValue> startParams, ApplicationUser user) {
        super(connectionProvider, form, startParams, user);
        finalForm = new FormModel(form.getId());
    }

    public void write(OutputStream stream) throws IOException, SQLException, ConnectException {
        startParams = processStartParams(startParams);

        prepareForm();

        build();
        finalForm.setHeight(form.getFinalHeight());
        finalForm.setWidth(form.getWidth());
        finalForm.setRowCount(form.getFinalRows());
        finalForm.setColumnCount(form.getFinalColumns());
    }

    public void close() {
    }

    protected void writeRow(RowElementWrapper row) {
        FormRowModel newRow = new FormRowModel(row.getFinalRow());
        newRow.setHeight(row.getHeight());
        for (CellElementWrapper c : row.getCells()) {
            FormCellModel m = toFormCellModel(c, newRow);

            m.setRow(newRow);
            newRow.addCell(m);
        }
        finalForm.addRow(newRow);
        form.addFinalRow(row);
    }

    @Override
    protected String changeParameter(String property, String value, Map<String, String> params) {
        if (PropertyType.WhereSql.equals(property)) {
            return value;
        }
        return super.changeParameter(property, value, params);
    }

    private FormCellModel toFormCellModel(CellElementWrapper cell, FormRowModel row) {
        FormCellModel model = new FormCellModel(cell.getId());
        model.setRow(row);
        model.setRowSpan(cell.getRowSpan());
        model.setColSpan(cell.getColSpan());
        model.setBorderTop(cell.getBorderTop(), cell.getBorderTopColor());
        model.setBorderRight(cell.getBorderRight(), cell.getBorderRightColor());
        model.setBorderBottom(cell.getBorderBottom(), cell.getBorderBottomColor());
        model.setBorderLeft(cell.getBorderLeft(), cell.getBorderLeftColor());
        model.setColor(cell.getBackgroundColor());
        if (cell.getComponent() != null) {
            ComponentModel cm = cell.getComponent();
            cm.setValue(PropertyType.LayoutDataFormRow.getCode(),
                new DataValueImpl(DataType.NUMBER, cell.getRow().getFinalRow()));
            cm.setValue(PropertyType.LayoutDataFormColumn.getCode(),
                new DataValueImpl(DataType.NUMBER, cell.getColumn().getFinalCol()));
        }
        model.setComponent(cell.getComponent());

        ColumnElementWrapper column = cell.getColumn();
        FormColumnModel colModel = finalForm.getColumn(column.getFinalCol());
        if (colModel == null) {
            colModel = new FormColumnModel(column.getFinalCol());
            colModel.setWidth(column.getWidth());
            finalForm.addColumn(colModel);
        }
        model.setColumn(colModel);
        return model;
    }

    @Override
    protected int nextRow() {
        return form.getFinalRows();
    }

    public FormModel getFormModel() {
        return finalForm;
    }

}
