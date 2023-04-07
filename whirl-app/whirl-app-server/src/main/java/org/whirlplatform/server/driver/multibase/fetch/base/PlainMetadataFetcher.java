package org.whirlplatform.server.driver.multibase.fetch.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.FieldMetadata;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.editor.LocaleElement;
import org.whirlplatform.meta.shared.editor.db.PlainTableElement;
import org.whirlplatform.meta.shared.editor.db.TableColumnElement;
import org.whirlplatform.rpc.shared.CustomException;
import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.driver.multibase.condition.TableConditionSolver;
import org.whirlplatform.server.driver.multibase.fetch.AbstractFetcher;
import org.whirlplatform.server.driver.multibase.fetch.MetadataFetcher;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;
import org.whirlplatform.server.login.ApplicationUser;

public class PlainMetadataFetcher extends AbstractFetcher
    implements MetadataFetcher<PlainTableElement> {
    protected static Logger _log = LoggerFactory.getLogger(PlainMetadataFetcher.class);

    public PlainMetadataFetcher(ConnectionWrapper connectionWrapper) {
        super(connectionWrapper);
    }

    @Override
    public ClassMetadata getClassMetadata(PlainTableElement table, List<DataValue> params) {
        if (table == null) {
            final String message = "The PlainTableElement is null";
            _log.warn(message);
            throw new CustomException(message);
        }
        ApplicationUser user = getUser();
        ClassMetadata result = new ClassMetadata(table.getId());
        result.setTitle(table.getTitle().getValue(user.getLocaleElement()).getString());

        TableConditionSolver tableCondition;
        tableCondition = new TableConditionSolver(table, user.getApplication(), params, user,
            getConnection());
        tableCondition.allowed();

        result.setViewable(tableCondition.isViewable());
        result.setInsertable(tableCondition.isInsertable());
        result.setUpdatable(tableCondition.isUpdateable());
        result.setDeletable(tableCondition.isDeletable());

        Comparator<TableColumnElement> comparator = new Comparator<TableColumnElement>() {
            @Override
            public int compare(TableColumnElement o1, TableColumnElement o2) {
                return o1.getIndex() - o2.getIndex();
            }
        };
        ArrayList<TableColumnElement> columns =
            new ArrayList<TableColumnElement>(table.getColumns());

        Collections.sort(columns, comparator);
        LocaleElement userLocale = user.getLocaleElement();
        for (TableColumnElement columnElement : columns) {
            FieldMetadata fieldMetadata = new FieldMetadata();
            fieldMetadata.setId(columnElement.getId());
            fieldMetadata.setName(columnElement.getColumnName());
            fieldMetadata.setLabel(columnElement.getTitle().getValue(userLocale).getString());
            fieldMetadata.setView(tableCondition.isViewable(columnElement));
            fieldMetadata.setEdit(tableCondition.isEditable(columnElement));
            if (columnElement.getSize() != null) {
                fieldMetadata.setLength(columnElement.getSize());
            }
            fieldMetadata.setType(columnElement.getType());
            if (columnElement.getType() == DataType.LIST && columnElement.getListTable() != null) {
                fieldMetadata.setClassId(columnElement.getListTable().getId());
            }
            if (columnElement.getWidth() != null) {
                fieldMetadata.setWidth(columnElement.getWidth());
            }
            if (columnElement.getHeight() != null) {
                fieldMetadata.setHeight(columnElement.getHeight());
            }
            fieldMetadata.setFilter(columnElement.isFilter());
            fieldMetadata.setRequired(columnElement.isNotNull());
            fieldMetadata.setDataFormat(columnElement.getDataFormat());
            fieldMetadata.setRegEx(columnElement.getRegex());
            fieldMetadata.setRegExError(
                columnElement.getRegexMessage().getValue(userLocale).getString());
            fieldMetadata.setHidden(columnElement.isHidden());
            // TODO field.setEvent();
            fieldMetadata.setLabelExpression(columnElement.getLabelExpression());

            if (table.getIdColumn() == columnElement) {
                result.setIdField(fieldMetadata);
            }
            result.addField(fieldMetadata);
        }
        return result;
    }
}
