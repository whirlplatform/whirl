package org.whirlplatform.server.expimp;

import static org.whirlplatform.server.global.SrvConstant.LABEL_EXPRESSION_NAME;

import au.com.bytecode.opencsv.CSVWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.apache.empire.db.DBReader;
import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.FieldMetadata;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.server.utils.ServerGetter;

public class CSVExporter extends Exporter {

    private ClassMetadata metadata;

    private DBReader reader;

    private boolean columnHeader;

    private CSVWriter writer;

    public CSVExporter(ClassMetadata metadata, DBReader reader) {
        this.metadata = metadata;
        this.reader = reader;
    }

    public void setColumnHeader(boolean columnHeader) {
        this.columnHeader = columnHeader;
    }

    public void write(OutputStream stream) throws IOException {
        writer = new CSVWriter(new OutputStreamWriter(stream, StandardCharsets.UTF_8), ';');

        // первая строка это названия колонок
        List<String> header = new ArrayList<String>();
        for (FieldMetadata f : metadata.getFields()) {
            if (!f.isView()) {
                continue;
            }
            if (columnHeader) {
                header.add(f.getName());
            } else {
                header.add(f.getRawLabel());
            }
        }

        writer.writeNext(header.toArray(new String[0]));

        while (reader.moveNext()) {
            List<String> line = new ArrayList<String>();
            for (FieldMetadata f : metadata.getFields()) {
                if (!f.isView()) {
                    continue;
                }
                String result = (String) getColumnValue(f, reader);
                line.add(result);
            }
            writer.writeNext(line.toArray(new String[0]));
            writer.flush();

            Thread.yield();
        }
    }

    @Override
    protected Object getColumnValue(FieldMetadata field,
                                    DBReader reader) {
        String result;
        if (DataType.LIST == field.getType() || DataType.FILE == field.getType()) {
            result =
                    reader.getString(reader.getFieldIndex(field.getName() + LABEL_EXPRESSION_NAME));
        } else {
            result = ServerGetter.getResultSetValue(reader,
                    reader.getFieldIndex(field.getName()));
        }
        return result;
    }
}
