package org.whirlplatform.server.metadata.store;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.whirlplatform.meta.shared.AppConstant;
import org.whirlplatform.meta.shared.FieldMetadata;
import org.whirlplatform.meta.shared.ListViewType;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.data.ListModelData;
import org.whirlplatform.meta.shared.editor.AbstractCondition;
import org.whirlplatform.meta.shared.editor.ApplicationElement;
import org.whirlplatform.meta.shared.editor.BooleanCondition;
import org.whirlplatform.meta.shared.editor.CellElement;
import org.whirlplatform.meta.shared.editor.CellRowCol;
import org.whirlplatform.meta.shared.editor.ColumnElement;
import org.whirlplatform.meta.shared.editor.ComponentElement;
import org.whirlplatform.meta.shared.editor.ContextMenuItemElement;
import org.whirlplatform.meta.shared.editor.EventElement;
import org.whirlplatform.meta.shared.editor.EventParameterElement;
import org.whirlplatform.meta.shared.editor.FileElement;
import org.whirlplatform.meta.shared.editor.FormElement;
import org.whirlplatform.meta.shared.editor.GroupElement;
import org.whirlplatform.meta.shared.editor.LocaleElement;
import org.whirlplatform.meta.shared.editor.PropertyValue;
import org.whirlplatform.meta.shared.editor.ReportElement;
import org.whirlplatform.meta.shared.editor.RequestElement;
import org.whirlplatform.meta.shared.editor.RightCollectionElement;
import org.whirlplatform.meta.shared.editor.RightElement;
import org.whirlplatform.meta.shared.editor.RowElement;
import org.whirlplatform.meta.shared.editor.SQLCondition;
import org.whirlplatform.meta.shared.editor.db.AbstractTableElement;
import org.whirlplatform.meta.shared.editor.db.DataSourceElement;
import org.whirlplatform.meta.shared.editor.db.DynamicTableElement;
import org.whirlplatform.meta.shared.editor.db.PlainTableElement;
import org.whirlplatform.meta.shared.editor.db.SchemaElement;
import org.whirlplatform.meta.shared.editor.db.TableColumnElement;
import org.whirlplatform.meta.shared.editor.db.ViewElement;

//import org.whirlplatform.meta.shared.editor.CellGroupElement;

public class XMLApplicationExporter {

    private ApplicationElement application;
    private boolean saveFileStream = false;
    private boolean writeTemplate = false;

    public XMLApplicationExporter() {
    }

    XMLApplicationExporter(ApplicationElement application) {
        this.application = application;
    }

    public XMLApplicationExporter(boolean writeTemplate) {
        this.writeTemplate = true;
    }

    public void setSaveFileStream(boolean saveFileStream) {
        this.saveFileStream = saveFileStream;
    }

    public Document export() throws IOException {
        Document document = DocumentFactory.getInstance().createDocument("UTF-8");
        Element applicationEl = document.addElement("application");
        applicationEl.addAttribute("id", application.getId());
        applicationEl.addAttribute("name", application.getName());
        applicationEl.addAttribute("metaversion",
            String.valueOf(MetadataSerializer.CURRENT_VERSION));

        Element propertiesEl = applicationEl.addElement("properties");

        Element property = null;

        property = propertiesEl.addElement("title");
        writePropertyValue(property, application.getTitle());

        property = propertiesEl.addElement("code");
        property.addCDATA(application.getCode());

        property = propertiesEl.addElement("enabled");
        setElementText(property, String.valueOf(application.isEnabled()));

        property = propertiesEl.addElement("guest");
        setElementText(property, String.valueOf(application.isGuest()));

        property = propertiesEl.addElement("htmlHeader");
        property.addCDATA(application.getHtmlHeader());

        Element defaultLocaleEl = applicationEl.addElement("defaultLocale");
        writeLocale(defaultLocaleEl, application.getDefaultLocale());

        Element localesEl = applicationEl.addElement("locales");
        for (LocaleElement locale : application.getLocales()) {
            Element localeEl = localesEl.addElement("locale");
            writeLocale(localeEl, locale);
        }

        Element referencesEl = applicationEl.addElement("references");
        for (ApplicationElement ref : application.getReferences()) {
            Element referenceEl = referencesEl.addElement("reference");
            referenceEl.addAttribute("id", ref.getId());
            referenceEl.addAttribute("code", ref.getCode());
            referenceEl.addAttribute("version", ref.getVersion());
            referenceEl.addAttribute("name", ref.getName());
        }

        ComponentElement rootComponent = application.getRootComponent();
        if (rootComponent != null) {
            Element componentsEl = applicationEl.addElement("components");
            writeComponent(componentsEl, rootComponent);
        }

        Collection<ComponentElement> freeComponents = application.getFreeComponents();
        if (freeComponents.size() > 0) {
            Element freeComponentsEl = applicationEl.addElement("freeComponents");
            for (ComponentElement comp : freeComponents) {
                writeComponent(freeComponentsEl, comp);
            }
        }

        if (application.getFreeEvents().size() > 0) {
            Element eventsEl = applicationEl.addElement("events");
            for (EventElement e : application.getFreeEvents()) {
                writeEvent(eventsEl, e);
            }
        }

        // файлы JavaScript
        if (application.getJavaScriptFiles().size() > 0) {
            Element javaScriptEl = applicationEl.addElement("javascriptFiles");
            for (FileElement f : application.getJavaScriptFiles()) {
                writeFile(javaScriptEl, f);
            }
        }
        // файлы CSS
        if (application.getCssFiles().size() > 0) {
            Element cssEl = applicationEl.addElement("cssFiles");
            for (FileElement f : application.getCssFiles()) {
                writeFile(cssEl, f);
            }
        }
        // файлы Java
        if (application.getJavaFiles().size() > 0) {
            Element javaEl = applicationEl.addElement("javaFiles");
            for (FileElement f : application.getJavaFiles()) {
                writeFile(javaEl, f);
            }
        }
        // файлы Images
        if (application.getImageFiles().size() > 0) {
            Element imageEl = applicationEl.addElement("imageFiles");
            for (FileElement f : application.getImageFiles()) {
                writeFile(imageEl, f);
            }
        }
        // static файл
        if (application.getStaticFile() != null) {
            Element staticEl = applicationEl.addElement("staticFile");
            writeFile(staticEl, application.getStaticFile());
        }

        Element datasourcesEl = applicationEl.addElement("datasources");
        for (DataSourceElement d : application.getDataSources()) {
            writeDataSource(datasourcesEl, d, "datasource");
        }

        Element groupsEl = applicationEl.addElement("groups");
        for (GroupElement g : application.getGroups()) {
            writeGroup(groupsEl, g);
        }

        // права
        Element rightsEl = applicationEl.addElement("rights");

        Element tableRightsEl = rightsEl.addElement("tableRights");
        for (RightCollectionElement c : application.getAllTableRights()) {
            writeCollectionRights(tableRightsEl, c, "collection", "tableId");
        }

        Element tableColumnRightsEl = rightsEl.addElement("tableColumnRights");
        for (RightCollectionElement c : application.getAllTableColumnRights()) {
            writeCollectionRights(tableColumnRightsEl, c, "collection", "tableColumnId");
        }

        Element eventRightsEl = rightsEl.addElement("eventRights");
        for (RightCollectionElement c : application.getAllEventRights()) {
            writeCollectionRights(eventRightsEl, c, "collection", "eventId");
        }

        document.setXMLEncoding("UTF-8");
        return document;
    }

    private void writeCollectionRights(Element parentEl, RightCollectionElement collection,
                                       String collectionTag,
                                       String idTag) {
        // Второе условие - временное. Нужно сделать чтобы при удалении элемента
        // удалялись и его права(и права всех дочерних удаленных элементов)
        if (collection.isEmpty() || collection.getElement() == null) {
            return;
        }

        Element collectionEl = parentEl.addElement(collectionTag);
        collectionEl.addAttribute("id", collection.getId());
        collectionEl.addAttribute(idTag, collection.getElement().getId());

        Element applicationEl = collectionEl.addElement("application");
        for (RightElement r : collection.getApplicationRights()) {
            writeRight(applicationEl, r);
        }

        Element groupsEl = collectionEl.addElement("groups");
        for (GroupElement g : application.getGroups()) {
            Element groupEl = groupsEl.addElement("group");
            groupEl.addAttribute("id", g.getId());
            for (RightElement r : collection.getGroupRights(g)) {
                writeRight(groupEl, r);
            }
        }
    }

    private void writeRight(Element parentEl, RightElement right) {
        Element rightEl = parentEl.addElement("right");
        rightEl.addAttribute("type", right.getType().name());
        AbstractCondition<?> condition = right.getCondition();

        Element conditionEl = rightEl.addElement("condition");
        if (condition instanceof BooleanCondition) {
            conditionEl.addAttribute("type", "boolean");

            Element valueEl = conditionEl.addElement("value");
            valueEl.setText(String.valueOf(((BooleanCondition) condition).getValue()));
        } else if (condition instanceof SQLCondition) {
            conditionEl.addAttribute("type", "plain");

            Element valueEl = conditionEl.addElement("value");
            valueEl.addCDATA(((SQLCondition) condition).getValue());
        }
    }

    private void writeGroup(Element groupsEl, GroupElement group) {
        Element groupEl = groupsEl.addElement("group");
        groupEl.addAttribute("id", group.getId());
        groupEl.addAttribute("name", group.getName());
        groupEl.addAttribute("groupName", group.getGroupName());
    }

    private void writeLocale(Element localeEl, LocaleElement locale) {
        localeEl.addAttribute("language", locale.getLanguage());
        localeEl.addAttribute("country", locale.getCountry());
    }

    public void writeComponent(Element parentElement, ComponentElement comp) {
        if (comp.getType() == null) {
            return;
        }

        Element compEl = parentElement.addElement("component");
        compEl.addAttribute("id", comp.getId());
        compEl.addAttribute("name", comp.getName());
        compEl.addAttribute("type", comp.getType().getType());

        Map<PropertyType, PropertyValue> properties = comp.getProperties();
        if (properties.size() > 0) {
            Element propertiesEl = compEl.addElement("properties");
            for (PropertyType type : properties.keySet()) {
                PropertyValue value = properties.get(type);
                Element propertyEl = propertiesEl.addElement("property");
                propertyEl.addAttribute("name", type.getCode());
                propertyEl.addAttribute("type", type.getType().name());
                if (writeTemplate) {
                    writeTemplatePropertyValue(propertyEl, value);
                } else {
                    writePropertyValue(propertyEl, value);
                }
            }
        }

        if (comp instanceof FormElement) {
            writeForm(compEl, (FormElement) comp);
        } else if (comp instanceof ReportElement) {
            writeReport(compEl, (ReportElement) comp);
        }

        Collection<EventElement> events = comp.getEvents();
        if (events.size() > 0) {
            Element eventsEl = compEl.addElement("events");
            for (EventElement event : events) {
                writeEvent(eventsEl, event);
            }
        }

        Collection<ContextMenuItemElement> menuItems = comp.getContextMenuItems();
        if (menuItems.size() > 0) {
            Element menuItemsEl = compEl.addElement("contextMenuItems");
            for (ContextMenuItemElement item : menuItems) {
                writeContextMenuItem(menuItemsEl, item);
            }
        }

        Collection<ComponentElement> children = comp.getChildren();
        if (children.size() > 0) {
            Element childrenEl = compEl.addElement("children");
            for (ComponentElement child : children) {
                writeComponent(childrenEl, child);
            }
        }
    }

    private void writeContextMenuItem(Element parentEl, ContextMenuItemElement item) {
        Element itemEl = parentEl.addElement("item");
        itemEl.addAttribute("id", item.getId());
        itemEl.addAttribute("name", item.getName());
        itemEl.addAttribute("index", String.valueOf(item.getIndex()));

        Element label = itemEl.addElement("label");
        if (writeTemplate) {
            writeTemplatePropertyValue(label, item.getLabel());
        } else {
            writePropertyValue(label, item.getLabel());
        }

        Element imageUrl = itemEl.addElement("imageUrl");
        setElementText(imageUrl, item.getImageUrl());

        Collection<EventElement> events = item.getEvents();
        if (events.size() > 0) {
            Element eventsEl = itemEl.addElement("events");
            for (EventElement event : events) {
                writeEvent(eventsEl, event);
            }
        }

        Collection<ContextMenuItemElement> children = item.getChildren();
        if (children.size() > 0) {
            Element childrenEl = itemEl.addElement("children");
            for (ContextMenuItemElement e : children) {
                writeContextMenuItem(childrenEl, e);
            }
        }
    }

    private void writeTemplatePropertyValue(Element propertyEl, PropertyValue value) {
        propertyEl.addAttribute("replace", String.valueOf(value.isReplaceable()));
        //Локаль заглушка
        LocaleElement stubLocale = new LocaleElement("RU_TMPL", "TEMPLATE");
        Element defaultLocaleEl = propertyEl.addElement("defaultLocale");
        defaultLocaleEl.addAttribute("locale", stubLocale.asString());
        Element localesEl = propertyEl.addElement("locales");
        DataValue v = value.getValue(stubLocale);
        if (v.getObject() != null) {
            Element valueEl = localesEl.addElement("value");
            valueEl.addAttribute("locale", stubLocale.asString());
            writeDataValue(valueEl, v);
        }
    }

    private void writePropertyValue(Element propertyEl, PropertyValue value) {
        propertyEl.addAttribute("replace", String.valueOf(value.isReplaceable()));

        LocaleElement defLocale = value.getDefaultLocale();
        if (defLocale == null) {
            defLocale = application.getDefaultLocale();
        }

        Element defaultLocaleEl = propertyEl.addElement("defaultLocale");
        defaultLocaleEl.addAttribute("locale", defLocale.asString());

        Element localesEl = propertyEl.addElement("locales");
        for (LocaleElement locale : value.getLocales()) {
            DataValue v = value.getValue(locale);
            if (application.hasLocale(locale)) {
                if (v.getObject() == null) {
                    continue;
                }
                Element valueEl = localesEl.addElement("value");
                valueEl.addAttribute("locale", locale.asString());
                writeDataValue(valueEl, v);
            }
        }
    }

    private void writeDataValue(Element dataValueEl, DataValue value) {
        dataValueEl.addAttribute("type", value.getType().name());
        if (DataType.STRING == value.getType()) {
            dataValueEl.addCDATA(value.getString());
        } else if (DataType.LIST == value.getType()) {
            Element idEl = dataValueEl.addElement("id");
            Element labelEl = dataValueEl.addElement("label");
            ListModelData model = value.getListModelData();
            idEl.setText(model.getId());
            labelEl.addCDATA(model.getLabel());
        } else {
            String valueStr = valueAsString(value.getObject());
            if (valueStr != null) {
                dataValueEl.setText(valueStr);
            }
        }
    }

    private String valueAsString(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Date) {
            // return DateTimeFormat.getFormat("dd.MM.yyyy HH:mm:ss").format(
            // (Date) value);
            return new SimpleDateFormat(AppConstant.DATE_FORMAT_LONGEST).format(value);
        } else {
            return String.valueOf(value);
        }
    }

    private void writeForm(Element parentElement, FormElement comp) {
        List<RowElement> rowsHeight = comp.getRowsHeight();
        if (rowsHeight.size() > 0) {
            Element rowsHeightEl = parentElement.addElement("rowsHeight");
            for (RowElement model : rowsHeight) {
                Element rowEl = rowsHeightEl.addElement("height");
                rowEl.addAttribute("id", model.getId());
                rowEl.addAttribute("row", String.valueOf(model.getRow()));
                if (model.getHeight() != null) {
                    rowEl.addText(String.valueOf(model.getHeight()));
                }
            }
        }

        List<ColumnElement> columnsWidth = comp.getColumnsWidth();
        if (columnsWidth.size() > 0) {
            Element columnsWidthEl = parentElement.addElement("columnsWidth");
            for (ColumnElement model : columnsWidth) {
                Element columnEl = columnsWidthEl.addElement("width");
                columnEl.addAttribute("id", model.getId());
                columnEl.addAttribute("column", String.valueOf(model.getColumn()));
                if (model.getWidth() != null) {
                    columnEl.addText(String.valueOf(model.getWidth()));
                }
            }
        }

        Map<CellRowCol, CellElement> cells = comp.getCells();
        if (cells.size() > 0) {
            Element cellsEl = parentElement.addElement("cells");
            for (Entry<CellRowCol, CellElement> entry : cells.entrySet()) {
                Element cellEl = cellsEl.addElement("item");

                CellRowCol cell = entry.getKey();
                CellElement model = entry.getValue();

                cellEl.addAttribute("id", model.getId());
                cellEl.addAttribute("row", String.valueOf(cell.getRow()));
                cellEl.addAttribute("column", String.valueOf(cell.getCol()));
                cellEl.addAttribute("rowspan", String.valueOf(model.getRowSpan()));
                cellEl.addAttribute("colspan", String.valueOf(model.getColSpan()));
                if (model.getBorderTop() != 0) {
                    cellEl.addAttribute("borderTop", String.valueOf(model.getBorderTop()));
                    cellEl.addAttribute("borderTopColor", model.getBorderTopColor());
                }
                if (model.getBorderRight() != 0) {
                    cellEl.addAttribute("borderRight", String.valueOf(model.getBorderRight()));
                    cellEl.addAttribute("borderRightColor", model.getBorderRightColor());
                }
                if (model.getBorderBottom() != 0) {
                    cellEl.addAttribute("borderBottom", String.valueOf(model.getBorderBottom()));
                    cellEl.addAttribute("borderBottomColor", model.getBorderBottomColor());
                }
                if (model.getBorderLeft() != 0) {
                    cellEl.addAttribute("borderLeft", String.valueOf(model.getBorderLeft()));
                    cellEl.addAttribute("borderLeftColor", model.getBorderLeftColor());
                }
                cellEl.addAttribute("backgroundColor", model.getBackgroundColor());
            }
        }

        List<RequestElement> requests = comp.getRowRequests();
        if (requests.size() > 0) {
            Element requestsEl = parentElement.addElement("requests");
            for (RequestElement model : requests) {
                Element requestEl = requestsEl.addElement("request");
                requestEl.addAttribute("id", model.getId());
                requestEl.addAttribute("top", String.valueOf(model.getTop()));
                requestEl.addAttribute("bottom", String.valueOf(model.getBottom()));

                Element textEl = requestEl.addElement("emptyText");
                if (writeTemplate) {
                    writeTemplatePropertyValue(textEl, model.getEmptyText());
                } else {
                    writePropertyValue(textEl, model.getEmptyText());
                }


                Element queryEl = requestEl.addElement("query");
                queryEl.addCDATA(model.getSql());

                if (model.getDataSource() != null) {
                    Element dataSourceEl = requestEl.addElement("dataSourceId");
                    setElementText(dataSourceEl, model.getDataSource().getId());
                }
            }
        }

        // List<CellGroupElement> cellGroups = ((FormElement) comp)
        // .getCellGroups();
        // if (cellGroups.size() > 0) {
        // Element cellGroupsEl = parentElement.addElement("cellGroups");
        // for (CellGroupElement model : cellGroups) {
        // Element cellGroupEl = cellGroupsEl.addElement("group");
        // cellGroupEl.addAttribute("id", model.getId());
        // cellGroupEl.addAttribute("top", String.valueOf(model.getTop()));
        // cellGroupEl.addAttribute("right",
        // String.valueOf(model.getRight()));
        // cellGroupEl.addAttribute("bottom",
        // String.valueOf(model.getBottom()));
        // cellGroupEl.addAttribute("left",
        // String.valueOf(model.getLeft()));
        // writePropertyValue(cellGroupEl, model.getTitle());
        // }
        // }
    }

    private void writeEvent(Element parentElement, EventElement event) {
        Element eventEl = parentElement.addElement("event");
        eventEl.addAttribute("id", event.getId());
        eventEl.addAttribute("name", event.getName());
        eventEl.addAttribute("type", event.getType().name());

        Element propertiesEl = eventEl.addElement("properties");
        Element propertyEl;

        if (event.getDataSource() != null) {
            propertyEl = propertiesEl.addElement("dataSourceId");
            setElementText(propertyEl, event.getDataSource().getId());
        }

        propertyEl = propertiesEl.addElement("schema");
        setElementText(propertyEl, event.getSchema());

        if (event.getCode() != null) {
            propertyEl = propertiesEl.addElement("code");
            setElementText(propertyEl, event.getCode());
        }

        propertyEl = propertiesEl.addElement("function");
        setElementText(propertyEl, event.getFunction());

        propertyEl = propertiesEl.addElement("source");
        propertyEl.addCDATA(event.getSource());

        if (event.getComponent() != null) {
            propertyEl = propertiesEl.addElement("componentId");
            setElementText(propertyEl, event.getComponent().getId());
        }
        if (event.getTargetComponent() != null) {
            propertyEl = propertiesEl.addElement("targetComponentId");
            setElementText(propertyEl, event.getTargetComponent().getId());
        }

        propertyEl = propertiesEl.addElement("isNamed");
        setElementText(propertyEl, String.valueOf(event.isNamed()));

        propertyEl = propertiesEl.addElement("isConfirm");
        setElementText(propertyEl, String.valueOf(event.isConfirm()));

        propertyEl = propertiesEl.addElement("confirmText");
        if (writeTemplate) {
            writeTemplatePropertyValue(propertyEl, event.getConfirmText());
        } else {
            writePropertyValue(propertyEl, event.getConfirmText());
        }


        propertyEl = propertiesEl.addElement("isWait");
        setElementText(propertyEl, String.valueOf(event.isWait()));

        propertyEl = propertiesEl.addElement("waitText");
        PropertyValue waitText = event.getWaitText();
        if (writeTemplate) {
            writeTemplatePropertyValue(propertyEl, waitText);
        } else {
            writePropertyValue(propertyEl, waitText);
        }

        propertyEl = propertiesEl.addElement("handlerType");
        setElementText(propertyEl, event.getHandlerType());

        propertyEl = propertiesEl.addElement("createNew");
        setElementText(propertyEl, String.valueOf(event.isCreateNew()));

        Collection<EventParameterElement> parameters = event.getParameters();
        if (parameters.size() > 0) {
            Element parametersEl = eventEl.addElement("parameters");
            for (EventParameterElement param : parameters) {
                DataType type = null;
                Object value = null;
                if (param.getValue() != null) {
                    type = param.getValue().getType();
                    value = param.getValue().getObject();
                }

                Element parameter = parametersEl.addElement("parameter");
                parameter.addAttribute("id", param.getId());
                parameter.addAttribute("index", String.valueOf(param.getIndex()));
                parameter.addAttribute("code", param.getCode());
                parameter.addAttribute("name", param.getName());
                parameter.addAttribute("type", param.getType().toString());
                parameter.addAttribute("componentId",
                    param.getComponent() == null ? param.getComponentId() :
                        param.getComponent().getId());
                parameter.addAttribute("componentName", param.getComponentCode());
                parameter.addAttribute("storageCode", param.getStorageCode());
                if (type != null) {
                    parameter.addAttribute("dataType", type.toString());
                }
                if (DataType.LIST.equals(type)) {
                    parameter.addAttribute("label", ((ListModelData) value).getLabel());
                }
                if (DataType.STRING.equals(type)) {
                    parameter.addCDATA(stringifyForSql(value, type));
                } else {
                    parameter.addText(stringifyForSql(value, type));
                }
            }
        }

        List<EventElement> subEvents = event.getSubEvents();
        if (subEvents.size() > 0) {
            Element subEventsEl = eventEl.addElement("events");
            for (EventElement subEvent : subEvents) {
                writeEvent(subEventsEl, subEvent);
            }
        }
    }

    private void writeReport(Element parentElement, ReportElement report) {
        Element fieldsEl = parentElement.addElement("fields");
        List<FieldMetadata> fields = report.getFields();
        if (fields != null) {
            for (FieldMetadata meta : fields) {
                Element field = fieldsEl.addElement("field");

                DataType type = meta.getType();
                field.addAttribute("id", meta.getId());
                field.addAttribute("name", meta.getName());
                field.addAttribute("label", meta.getRawLabel());
                field.addAttribute("type", type.toString());
                field.addAttribute("required", String.valueOf(meta.isRequired()));
                ListViewType viewType = meta.getListViewType();
                field.addAttribute("listViewType", viewType == null ? "" : viewType.toString());
                if (DataType.LIST.equals(type)) {
                    field.addAttribute("classid", meta.getClassId());
                }
            }
        }
    }

    private void writeFile(Element parentElement, FileElement file) throws IOException {
        Element fileEl = parentElement.addElement("file");
        fileEl.addAttribute("id", file.getId());
        fileEl.addAttribute("name", file.getName());

        Element propertyEl = fileEl.addElement("file");
        setElementText(propertyEl, file.getFileName());

        propertyEl = fileEl.addElement("type");
        setElementText(propertyEl, file.getContentType());

        propertyEl = fileEl.addElement("checksum");
        setElementText(propertyEl, String.valueOf(file.getChecksum()));

        if (saveFileStream) {
            propertyEl = fileEl.addElement("content");
            try (InputStream stream = (InputStream) file.getInputStream()) {
                if (stream != null) {
                    propertyEl.addCDATA(asBase64String(stream));
                }
            }
        }
    }

    private String asBase64String(InputStream input) throws IOException {
        return new String(Base64.encodeBase64(IOUtils.toByteArray(input)));
    }

    private void writeDataSource(Element parentElement, DataSourceElement dataSource, String tag) {
        Element dataSourceEl = parentElement.addElement(tag);
        dataSourceEl.addAttribute("id", dataSource.getId());
        dataSourceEl.addAttribute("name", dataSource.getName());

        Element propertyEl = dataSourceEl.addElement("alias");
        propertyEl.addText(dataSource.getAlias());

        propertyEl = dataSourceEl.addElement("databaseName");
        propertyEl.addText(dataSource.getDatabaseName());

        Element schemasEl = dataSourceEl.addElement("schemas");
        for (SchemaElement s : dataSource.getSchemas()) {
            writeSchema(schemasEl, s);
        }
    }

    private void writeSchema(Element parentElement, SchemaElement schema) {
        Element schemaEl = parentElement.addElement("schema");
        schemaEl.addAttribute("id", schema.getId());
        schemaEl.addAttribute("name", schema.getName());

        Element propertyEl = schemaEl.addElement("schemaName");
        setElementText(propertyEl, schema.getSchemaName());

        Element tablesEl = schemaEl.addElement("tables");
        for (AbstractTableElement t : schema.getTables()) {
            if (t instanceof PlainTableElement) {
                writeSimpleTable(tablesEl, (PlainTableElement) t);
            } else if (t instanceof DynamicTableElement) {
                writeDynamicTable(tablesEl, (DynamicTableElement) t);
            }
        }
    }

    private void writeDynamicTable(Element parentElement, DynamicTableElement table) {
        Element tableEl = parentElement.addElement("dynamicTable");
        tableEl.addAttribute("id", table.getId());
        tableEl.addAttribute("name", table.getName());

        Element propertyEl = tableEl.addElement("title");
        PropertyValue title = table.getTitle();
        writePropertyValue(propertyEl, title);

        propertyEl = tableEl.addElement("emptyRow");
        setElementText(propertyEl, String.valueOf(table.isEmptyRow()));

        if (table.getCode() != null && !table.getCode().isEmpty()) {
            propertyEl = tableEl.addElement("code");
            setElementText(propertyEl, table.getCode());
        }

        if (table.getMetadataFunction() != null && !table.getMetadataFunction().isEmpty()) {
            propertyEl = tableEl.addElement("metadataFunction");
            setElementText(propertyEl, table.getMetadataFunction());
        }

        if (table.getDataFunction() != null && !table.getDataFunction().isEmpty()) {
            propertyEl = tableEl.addElement("dataFunction");
            setElementText(propertyEl, table.getDataFunction());
        }

        if (table.getInsertFunction() != null && !table.getInsertFunction().isEmpty()) {
            propertyEl = tableEl.addElement("insertFunction");
            setElementText(propertyEl, table.getInsertFunction());
        }

        if (table.getUpdateFunction() != null && !table.getUpdateFunction().isEmpty()) {
            propertyEl = tableEl.addElement("updateFunction");
            setElementText(propertyEl, table.getUpdateFunction());
        }

        if (table.getDeleteFunction() != null && !table.getDeleteFunction().isEmpty()) {
            propertyEl = tableEl.addElement("deleteFunction");
            setElementText(propertyEl, table.getDeleteFunction());
        }
    }

    private void writeSimpleTable(Element parentElement, PlainTableElement table) {
        Element tableEl = parentElement.addElement("table");
        tableEl.addAttribute("id", table.getId());
        tableEl.addAttribute("name", table.getName());

        Element propertyEl = tableEl.addElement("title");
        PropertyValue title = table.getTitle();
        writePropertyValue(propertyEl, title);

        propertyEl = tableEl.addElement("emptyRow");
        setElementText(propertyEl, String.valueOf(table.isEmptyRow()));

        if (table.getTableName() != null && !table.getTableName().isEmpty()) {
            propertyEl = tableEl.addElement("tableName");
            setElementText(propertyEl, table.getTableName());
        }

        if (table.getIdColumn() != null) {
            propertyEl = tableEl.addElement("idColumnId");
            setElementText(propertyEl, table.getIdColumn().getId());
        }
        if (table.getDeleteColumn() != null) {
            propertyEl = tableEl.addElement("deleteColumnId");
            setElementText(propertyEl, table.getDeleteColumn().getId());
        }

        if (table.getCode() != null && !table.getCode().isEmpty()) {
            propertyEl = tableEl.addElement("code");
            setElementText(propertyEl, table.getCode());
        }

        Element columnsEl = tableEl.addElement("columns");
        for (TableColumnElement c : table.getColumns()) {
            writeColumn(columnsEl, c);
        }

        if (table.getView() != null) {
            writeView(tableEl, table.getView(), "view");
        }

        if (!table.isClone() && !table.getClones().isEmpty()) {
            Element clonesEl = tableEl.addElement("clones");
            for (PlainTableElement t : table.getClones()) {
                writeSimpleTable(clonesEl, t);
            }
        }
    }

    private void writeColumn(Element parentElement, TableColumnElement column) {
        Element columnEl = parentElement.addElement("column");
        columnEl.addAttribute("id", column.getId());
        columnEl.addAttribute("name", column.getName());

        Element parameterEl = columnEl.addElement("index");
        setElementText(parameterEl, String.valueOf(column.getIndex()));

        parameterEl = columnEl.addElement("title");
        PropertyValue title = column.getTitle();
        writePropertyValue(parameterEl, title);

        parameterEl = columnEl.addElement("columnName");
        setElementText(parameterEl, column.getColumnName());

        parameterEl = columnEl.addElement("type");
        parameterEl.addCDATA(column.getType().name());

        parameterEl = columnEl.addElement("width");
        setElementText(parameterEl, String.valueOf(column.getWidth()));

        parameterEl = columnEl.addElement("height");
        setElementText(parameterEl, String.valueOf(column.getHeight()));

        parameterEl = columnEl.addElement("size");
        setElementText(parameterEl, String.valueOf(column.getSize()));

        parameterEl = columnEl.addElement("scale");
        setElementText(parameterEl, String.valueOf(column.getScale()));

        parameterEl = columnEl.addElement("defaultValue");
        parameterEl.addCDATA(column.getDefaultValue());

        parameterEl = columnEl.addElement("notNull");
        setElementText(parameterEl, String.valueOf(column.isNotNull()));

        parameterEl = columnEl.addElement("listTitle");
        setElementText(parameterEl, String.valueOf(column.isListTitle()));

        parameterEl = columnEl.addElement("hidden");
        setElementText(parameterEl, String.valueOf(column.isHidden()));

        parameterEl = columnEl.addElement("dataFormat");
        setElementText(parameterEl, column.getDataFormat());

        parameterEl = columnEl.addElement("color");
        setElementText(parameterEl, column.getColor());

        parameterEl = columnEl.addElement("regex");
        parameterEl.addCDATA(column.getRegex());

        parameterEl = columnEl.addElement("regexMessage");
        PropertyValue regexMessage = column.getRegexMessage();
        writePropertyValue(parameterEl, regexMessage);

        parameterEl = columnEl.addElement("function");
        parameterEl.addCDATA(column.getFunction());

        parameterEl = columnEl.addElement("filter");
        setElementText(parameterEl, String.valueOf(column.isFilter()));

        parameterEl = columnEl.addElement("defaultOrder");
        setElementText(parameterEl, String.valueOf(column.isDefaultOrder()));

        if (column.getOrder() != null) {
            parameterEl = columnEl.addElement("order");
            setElementText(parameterEl, column.getOrder().name());
        }

        if (column.getListTable() != null) {
            parameterEl = columnEl.addElement("listTable");
            parameterEl.addCDATA(column.getListTable().getId());
        }

        if (column.getConfigColumn() != null) {
            parameterEl = columnEl.addElement("configColumn");
            setElementText(parameterEl, column.getConfigColumn());
        }

        if (column.getLabelExpression() != null) {
            parameterEl = columnEl.addElement("labelExpression");
            setElementText(parameterEl, column.getLabelExpression());
        }
    }

    private void writeView(Element parentElement, ViewElement view, String tag) {
        Element viewEl = parentElement.addElement(tag);
        viewEl.addAttribute("id", view.getId());
        viewEl.addAttribute("name", view.getName());

        Element propertyEl = viewEl.addElement("viewName");
        setElementText(propertyEl, view.getViewName());

        propertyEl = viewEl.addElement("source");
        propertyEl.addCDATA(view.getSource());
    }

    private String stringifyForSql(Object value, DataType type) {
        if (value == null) {
            return "";
        }
        String result;
        if (type == DataType.BOOLEAN && value instanceof Boolean) {
            result = String.valueOf(value);
        } else if (type == DataType.DATE && value instanceof Date) {
            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            result = format.format(value);
        } else if (type == DataType.NUMBER && value instanceof Number) {
            result = value == null ? null : String.valueOf(value);
        } else if (type == DataType.LIST && value instanceof ListModelData) {
            result = ((ListModelData) value).getId();
        } else {
            result = value.toString();
        }
        return result;
    }

    public String exportAsString() throws IOException {
        OutputFormat format = OutputFormat.createPrettyPrint();
        StringWriter string = new StringWriter();
        XMLWriter writer = new XMLWriter(string, format);
        writer.write(export());
        writer.close();
        return string.toString();
    }

    private void setElementText(Element element, String value) {
        if (value != null) {
            element.setText(value);
        }
    }

    public void writeComponentTemplate(Element templateRoot, ComponentElement template) {
        writeComponent(templateRoot, template);
    }

    public void writeEventTemplate(Element templateRoot, EventElement template) {
        writeEvent(templateRoot, template);
    }
}
