package org.whirlplatform.server.expimp;

import org.apache.empire.db.DBReader;
import org.whirlplatform.meta.shared.FieldMetadata;
import org.whirlplatform.meta.shared.data.DataType;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;

import static org.whirlplatform.server.global.SrvConstant.LABEL_EXPRESSION_NAME;

public abstract class Exporter {

    public abstract void write(OutputStream stream) throws SQLException,
            IOException;

    protected Object getColumnValue(FieldMetadata field, DBReader reader) {
        Object object;
    
        if (DataType.LIST == field.getType() || DataType.FILE == field.getType()) {
            object = reader.getString(reader.getFieldIndex(field.getName() + LABEL_EXPRESSION_NAME));
        } else if (DataType.DATE == field.getType()) {
            object = reader.getDateTime(reader.getFieldIndex(field.getName()));
        } else {
            object = reader.getValue(reader.getFieldIndex(field.getName()));
        }

        return object;
    }

}
