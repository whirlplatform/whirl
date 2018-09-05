package org.whirlplatform.server.metadata.store;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.apache.commons.codec.binary.Base64;
import org.apache.empire.commons.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.whirlplatform.meta.shared.*;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.*;
import org.whirlplatform.meta.shared.editor.*;
import org.whirlplatform.meta.shared.editor.FileElement.InputStreamProvider;
import org.whirlplatform.meta.shared.editor.db.*;
import org.whirlplatform.meta.shared.editor.db.TableColumnElement.Order;
import org.whirlplatform.meta.shared.editor.db.TableColumnElement.ViewFormat;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class XMLApplicationImporter {

	private ApplicationElement application;

	private Multimap<String, AbstractElement> map = HashMultimap.create();
	private List<Runnable> finalizations = new ArrayList<>();

	private Map<EventElement, String> tableEvents = new HashMap<EventElement, String>(); // TODO
																							// как
																							// используется?

	boolean ignoreReferences = false;

	public XMLApplicationImporter(boolean ignoreReferences) {
		this.ignoreReferences = ignoreReferences;
	}

	void putMap(AbstractElement element) {
		map.put(element.getId(), element);
	}

	@SuppressWarnings("unchecked")
	<T extends AbstractElement> T map(String id, Class<T> clazz) {
		for (AbstractElement e : map.get(id)) {
			if (clazz.isAssignableFrom(e.getClass())) {
				return (T) e;
			}
		}
		return null;
	}

	private void finalization(Runnable runnable) {
		finalizations.add(runnable);
	}

	@SuppressWarnings("unchecked")
	ApplicationElement buildApplication(InputStream stream, MetadataStore loader) throws DocumentException {
		SAXReader reader = new SAXReader();
		Document document = reader.read(stream);
		Element appEl = document.getRootElement();
		if (!"application".equals(appEl.getName())) {
			throw new DocumentException("Not propper application data.");
		}
		int metaversion = -1;
		try {
			metaversion = Integer.parseInt(appEl.attributeValue("metaversion"));
		} catch (NumberFormatException e) {
		}

		if (MetadataSerializer.CURRENT_VERSION != metaversion) {
			throw new DocumentException(
					"Metadata version must be " + MetadataSerializer.CURRENT_VERSION + ". Got: " + metaversion);
		}

		application = new ApplicationElement();

		application.setId(appEl.attributeValue("id"));
		application.setName(appEl.attributeValue("name"));

		// локали
		application.setDefaultLocale(buildLocale(appEl.element("defaultLocale")));
		Element localesEl = appEl.element("locales");
		if (localesEl != null) {
			for (Element localeEl : (List<Element>) localesEl.elements("locale")) {
				application.addLocale(buildLocale(localeEl));
			}
		}

		// источники данных
		Element datasourcesEl = appEl.element("datasources");
		if (datasourcesEl != null) {
			for (Element datasourceEl : (List<Element>) datasourcesEl.elements("datasource")) {
				DataSourceElement dataSource = buildDataSource(datasourceEl);
				application.addDataSource(dataSource);
				putMap(dataSource);
			}
		}

		// свойства приложения
		Element props = appEl.element("properties");
		for (Element prop : (List<Element>) props.elements()) {
			String name = prop.getName();
			if ("title".equalsIgnoreCase(name)) {
				PropertyValue title = buildPropertyValue(DataType.STRING, prop);
				application.setTitle(title);
			} else if ("code".equalsIgnoreCase(name)) {
				application.setCode(prop.getText());
			} else if ("version".equalsIgnoreCase(name)) {
				application.setVersion(prop.getText());
			} else if ("enabled".equalsIgnoreCase(name)) {
				application.setEnabled(Boolean.valueOf(prop.getText()));
			} else if ("guest".equalsIgnoreCase(name)) {
				application.setGuest(Boolean.valueOf(prop.getText()));
			} else if ("htmlHeader".equalsIgnoreCase(name)) {
				application.setHtmlHeader(prop.getText());
			}
		}

		// Зависимости
		if (!ignoreReferences) {
			Element referencesEl = appEl.element("references");
			if (referencesEl != null) {
				for (Element referenceEl : (List<Element>) referencesEl.elements()) {
					ApplicationElement reference = null;
					if (loader != null) {
						reference = buildReference(referenceEl, loader);
					}
					if (reference == null) {
						reference = new ApplicationElement();
						reference.setAvailable(false);
						reference.setId(referenceEl.attributeValue("id"));
						reference.setCode(referenceEl.attributeValue("code"));
						reference.setName(referenceEl.attributeValue("name"));
						// reference.setCode(code); // ??
					}
					application.addReference(reference);
				}
			}
		}

		// дерево компонентов
		Element componentsEl = appEl.element("components");
		if (componentsEl != null) {
            Element compEl = componentsEl.element("component");
			application.setRootComponent(buildComponent(compEl));
		}

		// свободные компоненты
		Element freeComponentsEl = appEl.element("freeComponents");
		if (freeComponentsEl != null) {
			for (Element freeEl : (List<Element>) freeComponentsEl.elements("component")) {
				ComponentElement c = buildComponent(freeEl);
				if (c != null) {
					application.addFreeComponent(c);
				}
			}
		}

		// свободные/начальные события приложения
		Element eventsEl = appEl.element("events");
		if (eventsEl != null) {
			for (Element eventEl : (List<Element>) eventsEl.elements("event")) {
				application.addFreeEvent(buildEvent(eventEl, null));
			}
		}

		// файлы javascript
		Element javascriptFilesEl = appEl.element("javascriptFiles");
		if (javascriptFilesEl != null) {
			for (Element fileEl : (List<Element>) javascriptFilesEl.elements("file")) {
				application.addJavaScriptFile(buildFile(fileEl));
			}
		}

		// файлы css
		Element cssFilesEl = appEl.element("cssFiles");
		if (cssFilesEl != null) {
			for (Element fileEl : (List<Element>) cssFilesEl.elements("file")) {
				application.addCssFile(buildFile(fileEl));
			}
		}

		// файлы java
		Element javaFilesEl = appEl.element("javaFiles");
		if (javaFilesEl != null) {
			for (Element fileEl : (List<Element>) javaFilesEl.elements("file")) {
				application.addJavaFile(buildFile(fileEl));
			}
		}

		// файлы image
		Element imageFilesEl = appEl.element("imageFiles");
		if (imageFilesEl != null) {
			for (Element fileEl : (List<Element>) imageFilesEl.elements("file")) {
				application.addImageFile(buildFile(fileEl));
			}
		}

		// static файл
		Element staticFileEl = appEl.element("staticFile");
		if (staticFileEl != null) {
			Element fileEl = staticFileEl.element("file");
			if (fileEl != null) {
				application.setStaticFile(buildFile(fileEl));
			}
		}

		// группы
		Element groupsEl = appEl.element("groups");
		if (groupsEl != null) {
			for (Element groupEl : (List<Element>) groupsEl.elements("group")) {
				application.addGroup(buildGroup(groupEl));
			}
		}

		// права
		if (!ignoreReferences) {
			Element rightsEl = appEl.element("rights");
			if (rightsEl != null) {

				Element tableRightsEl = rightsEl.element("tableRights");
				if (tableRightsEl != null) {
					for (Element collectionEl : (List<Element>) tableRightsEl.elements("collection")) {
						RightCollectionElement collection = buildTableRightCollection(collectionEl);
						if (collection == null) {
							continue;
						}
						AbstractTableElement table = (AbstractTableElement) collection.getElement();
						if (table != null) {
							application.setTableRightCollection(table, collection);
						}
					}
				}

				Element tableColumnRightsEl = rightsEl.element("tableColumnRights");
				if (tableColumnRightsEl != null) {
					for (Element collectionEl : (List<Element>) tableColumnRightsEl.elements("collection")) {
						RightCollectionElement collection = buildTableColumnRightCollection(collectionEl);
						if (collection == null) {
							continue;
						}
						TableColumnElement column = (TableColumnElement) collection.getElement();
						if (column != null) {
							application.setTableColumnRightCollection(column, collection);
						}
					}
				}

				Element eventRightsEl = rightsEl.element("eventRights");
				if (eventRightsEl != null) {
					for (Element collectionEl : (List<Element>) eventRightsEl.elements("collection")) {
						RightCollectionElement collection = buildEventRightCollection(collectionEl);
						if (collection == null) {
							continue;
						}
						application.setEventRightCollection((EventElement) collection.getElement(), collection);
					}
				}
			}
		}

		for (Runnable run : finalizations) {
			run.run();
		}

		return application;
	}

	private RightCollectionElement buildTableRightCollection(Element collectionEl) {
		String tableId = collectionEl.attributeValue("tableId");
		AbstractTableElement table = map(tableId, AbstractTableElement.class);
		if (table == null) {
			return null;
		}
		RightCollectionElement collection = new RightCollectionElement(table);
		String id = collectionEl.attributeValue("id");
		id = id == null || id.isEmpty() ? "temp" + tableId : id;
		collection.setId(id);
		buildRightCollection(collection, collectionEl);
		return collection;
	}

	private RightCollectionElement buildTableColumnRightCollection(Element collectionEl) {
		String columnId = collectionEl.attributeValue("tableColumnId");
		TableColumnElement column = map(columnId, TableColumnElement.class);
		if (column == null) {
			return null;
		}
		RightCollectionElement collection = new RightCollectionElement(column);
		String id = collectionEl.attributeValue("id");
		id = id == null || id.isEmpty() ? "temp" + columnId : id;
		collection.setId(id);
		buildRightCollection(collection, collectionEl);
		return collection;
	}

	private RightCollectionElement buildEventRightCollection(Element collectionEl) {
		String eventId = collectionEl.attributeValue("eventId");
		EventElement event = map(eventId, EventElement.class);
		if (event == null) {
			return null;
		}
		RightCollectionElement collection = new RightCollectionElement(event);
		String id = collectionEl.attributeValue("id");
		id = id == null || id.isEmpty() ? "temp" + eventId : id;
		collection.setId(id);
		buildRightCollection(collection, collectionEl);
		return collection;
	}

	@SuppressWarnings("unchecked")
	private void buildRightCollection(RightCollectionElement collection, Element collectionEl) {
		Element applicationEl = collectionEl.element("application");
		for (Element rightEl : (List<Element>) applicationEl.elements("right")) {
			collection.addApplicationRight(buildRight(rightEl));
		}

		Element groupsEl = collectionEl.element("groups");
		for (Element groupEl : (List<Element>) groupsEl.elements("group")) {
			String groupId = groupEl.attributeValue("id");
			GroupElement group = map(groupId, GroupElement.class);
			for (Element rightEl : (List<Element>) groupEl.elements("right")) {
				collection.addGroupRight(group, buildRight(rightEl));
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private RightElement buildRight(Element rightEl) {
		String type = rightEl.attributeValue("type");
		RightElement result = new RightElement(RightType.valueOf(type));

		Element conditionEl = rightEl.element("condition");
		if (conditionEl != null) {
			AbstractCondition condition = null;
			String condType = conditionEl.attributeValue("type");
			if ("boolean".equals(condType)) {
				condition = new BooleanCondition();
				Element valueEl = conditionEl.element("value");
				if (valueEl != null) {
					condition.setValue(Boolean.valueOf(valueEl.getText()));
				}
			} else if ("plain".equals(condType)) {
				condition = new SQLCondition();
				Element valueEl = conditionEl.element("value");
				if (valueEl != null) {
					condition.setValue(valueEl.getText());
				}
			}
			result.setCondition(condition);
		}
		return result;
	}

	private GroupElement buildGroup(Element groupEl) {
		GroupElement result = new GroupElement();
		result.setId(groupEl.attributeValue("id"));
		result.setName(groupEl.attributeValue("name"));
		result.setGroupName(groupEl.attributeValue("groupName"));

		putMap(result);
		return result;
	}

	private LocaleElement buildLocale(Element localeEl) {
		LocaleElement result = new LocaleElement();
		result.setLanguage(localeEl.attributeValue("language"));
		result.setCountry(localeEl.attributeValue("country"));
		return result;
	}

	@SuppressWarnings("unchecked")
	private DataSourceElement buildDataSource(Element dataSourceEl) throws DocumentException {
		String alias = dataSourceEl.elementText("alias");
		String databaseName = dataSourceEl.elementText("databaseName");

		DataSourceElement result = new DataSourceElement(alias, databaseName);
		result.setId(dataSourceEl.attributeValue("id"));
		result.setName(dataSourceEl.attributeValue("name"));

		Element schemasEl = dataSourceEl.element("schemas");
		if (schemasEl != null) {
			for (Element schemaEl : (List<Element>) schemasEl.elements("schema")) {
				result.addSchema(buildSchema(schemaEl));
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	private SchemaElement buildSchema(Element schemaEl) throws DocumentException {
		SchemaElement result = new SchemaElement();
		result.setId(schemaEl.attributeValue("id"));
		result.setName(schemaEl.attributeValue("name"));
		result.setSchemaName(schemaEl.elementText("schemaName"));

		Element tablesEl = schemaEl.element("tables");
		if (tablesEl != null) {
			for (Element tableEl : (List<Element>) tablesEl.elements("table")) {
				result.addTable(buildSimpleTable(tableEl));
			}
			for (Element tableEl : (List<Element>) tablesEl.elements("dynamicTable")) {
				result.addTable(buildDynamicTable(tableEl));
			}
		}
		return result;
	}

	private DynamicTableElement buildDynamicTable(Element tableEl) throws DocumentException {
		DynamicTableElement result = new DynamicTableElement();
		result.setId(tableEl.attributeValue("id"));
		result.setName(tableEl.attributeValue("name"));

		result.setCode(tableEl.elementText("code"));
		result.setEmptyRow(Boolean.valueOf(tableEl.elementText("emptyRow")));
		result.setTitle(buildPropertyValue(DataType.STRING, tableEl.element("title")));
		result.setMetadataFunction(tableEl.elementText("metadataFunction"));
		result.setDataFunction(tableEl.elementText("dataFunction"));
		result.setInsertFunction(tableEl.elementText("insertFunction"));
		result.setUpdateFunction(tableEl.elementText("updateFunction"));
		result.setDeleteFunction(tableEl.elementText("deleteFunction"));

		return result;
	}

	private PlainTableElement buildSimpleTable(Element tableEl) throws DocumentException {
		PlainTableElement result = new PlainTableElement();
		result.setId(tableEl.attributeValue("id"));
		result.setName(tableEl.attributeValue("name"));

		if (tableEl.element("code") != null) {
			result.setCode(tableEl.elementText("code"));
		}

		Element titleEl = tableEl.element("title");
		PropertyValue title = new PropertyValue(DataType.STRING, application.getDefaultLocale());
		for (Element valueEl : (List<Element>) titleEl.element("locales").elements()) {
			LocaleElement locale = LocaleElement.valueOf(valueEl.attributeValue("locale"));
			DataValue value = new DataValueImpl(DataType.STRING);
			value.setValue(valueEl.getText());
			title.setValue(locale, value);
		}
		result.setTitle(title);

		result.setTableName(tableEl.elementText("tableName"));

		result.setEmptyRow(Boolean.parseBoolean(tableEl.elementText("emptyRow")));
		result.setSimple(Boolean.parseBoolean(tableEl.elementText("simple")));

		if (tableEl.element("view") != null) {
			result.setView(buildView(tableEl.element("view")));
		}
		if (tableEl.element("list") != null) {
			result.setList(buildView(tableEl.element("list")));
		}

		Element columnsEl = tableEl.element("columns");
		if (columnsEl != null) {
			for (Element columnEl : (List<Element>) columnsEl.elements("column")) {
				result.addColumn(buildColumn(columnEl));
			}
		}

		String idColumnId = tableEl.elementText("idColumnId");
		String deleteColumnId = tableEl.elementText("deleteColumnId");
		for (TableColumnElement c : result.getColumns()) {
			if (idColumnId != null && idColumnId.equals(c.getId())) {
				result.setIdColumn(c);
			} else if (deleteColumnId != null && deleteColumnId.equals(c.getId())) {
				result.setDeleteColumn(c);
			}
		}

		putMap(result);

		Element clonesEl = tableEl.element("clones");
		if (clonesEl != null) {
			for (Element cloneEl : (List<Element>) clonesEl.elements("table")) {
				result.addClone(buildSimpleTable(cloneEl));
			}
		}

		return result;
	}

	private ViewElement buildView(Element viewEl) {
		ViewElement result = new ViewElement();
		result.setId(viewEl.attributeValue("id"));
		result.setName(viewEl.attributeValue("name"));
		result.setViewName(viewEl.elementText("viewName"));
		result.setSource(viewEl.elementText("source"));
		return result;
	}

	private TableColumnElement buildColumn(Element columnEl) throws DocumentException {
		final TableColumnElement result = new TableColumnElement();
		result.setId(columnEl.attributeValue("id"));
		result.setName(columnEl.attributeValue("name"));

		result.setIndex(parseInt(columnEl.elementText("index"), 0));

		Element titleEl = columnEl.element("title");
		PropertyValue title = buildPropertyValue(DataType.STRING, titleEl);
		result.setTitle(title);

		result.setColumnName(columnEl.elementText("columnName"));
		result.setType(DataType.valueOf(columnEl.elementText("type")));
		result.setWidth(parseInt(columnEl.elementText("width"), null));
		result.setHeight(parseInt(columnEl.elementText("height"), null));
		result.setSize(parseInt(columnEl.elementText("size"), null));
		result.setScale(parseInt(columnEl.elementText("scale"), null));
		result.setDefaultValue(columnEl.elementText("defaultValue"));
		result.setNotNull(parseBoolean(columnEl.elementText("notNull"), false));
		result.setListTitle(parseBoolean(columnEl.elementText("listTitle"), false));
		final String listTable = columnEl.elementText("listTable");
		if (listTable != null && !listTable.isEmpty()) {
			finalization(new Runnable() {
				@Override
				public void run() {
                    result.setListTable(map(listTable, AbstractTableElement.class));
				}
			});
		}
		result.setHidden(parseBoolean(columnEl.elementText("hidden"), true));
		result.setDataFormat(columnEl.elementText("dataFormat"));
		result.setColor(columnEl.elementText("color"));
		result.setRegex(columnEl.elementText("regex"));

		Element regexMessageEl = columnEl.element("regexMessage");
		if (regexMessageEl != null) {
			PropertyValue regexMessage = buildPropertyValue(DataType.STRING, regexMessageEl);
			result.setRegexMessage(regexMessage);
		}

		result.setFunction(columnEl.elementText("function"));

		result.setFilter(parseBoolean(columnEl.elementText("filter"), true));

		result.setDefaultOrder(parseBoolean(columnEl.elementText("defaultOrder"), false));
		if (columnEl.elementText("order") != null) {
			result.setOrder(Order.valueOf(columnEl.elementText("order")));
		}

		Element viewFormatEl = columnEl.element("viewFormat");
		if (viewFormatEl != null) {
			result.setViewFormat(ViewFormat.valueOf(viewFormatEl.getText()));
		}

		Element configColumnEl = columnEl.element("configColumn");
		if (configColumnEl != null) {
			result.setConfigColumn(configColumnEl.getText());
		}

		putMap(result);
		return result;
	}

	@SuppressWarnings("unchecked")
	/* package */ ComponentElement buildComponent(Element compEl) throws DocumentException {
		ComponentType type = ComponentType.parse(compEl.attributeValue("type"));
		if (type == null) {
			return null;
		}

		ComponentElement comp;

		if (ComponentType.FormBuilderType.equals(type)) {
			comp = buildForm(compEl);
		} else if (ComponentType.ReportType.equals(type)) {
			comp = buildReport(compEl);
		} else {
			comp = new ComponentElement(type);
		}

		comp.setId(compEl.attributeValue("id"));
		comp.setName(compEl.attributeValue("name"));

		Element propsEl = compEl.element("properties");
		// for (Element propsEl : (List<Element>) propertiesEl.elements()) {
		if (propsEl != null) {
			for (Element propEl : (List<Element>) propsEl.elements("property")) {
				PropertyType t = PropertyType.parse(propEl.attributeValue("name"), type);
				if (t == null) {
					continue;
				}
				PropertyValue pv = buildPropertyValue(t.getType(), propEl);
				comp.setProperty(t, pv);
			}
		}
		// }

		Element eventsEl = compEl.element("events");
		if (eventsEl != null) {
			for (Element eventEl : (List<Element>) eventsEl.elements()) {
				comp.addEvent(buildEvent(eventEl, null));
			}
		}

		Element menuItemsEl = compEl.element("contextMenuItems");
		if (menuItemsEl != null) {
			for (Element itemEl : (List<Element>) menuItemsEl.elements()) {
				comp.addContextMenuItem(buildContextMenuItem(itemEl));
			}
		}

		Element childrenEl = compEl.element("children");
		if (childrenEl != null) {
			for (Element childCompEl : (List<Element>) childrenEl.elements()) {
				ComponentElement c = buildComponent(childCompEl);
				if (c != null) {
					comp.addChild(c);
				}
			}
		}
		putMap(comp);

		return comp;
	}

	private ContextMenuItemElement buildContextMenuItem(Element itemEl) throws DocumentException {
		ContextMenuItemElement item = new ContextMenuItemElement();
		item.setId(itemEl.attributeValue("id"));
		item.setName(itemEl.attributeValue("name"));
		item.setIndex(parseInt(itemEl.attributeValue("index"), 0));

		Element labelEl = itemEl.element("label");
		if (labelEl != null) {
			item.setLabel(buildPropertyValue(DataType.STRING, labelEl));
		}

		item.setImageUrl(itemEl.elementText("imageUrl"));

		Element eventsEl = itemEl.element("events");
		if (eventsEl != null) {
			for (Element childEventEl : (List<Element>) eventsEl.elements()) {
				item.addEvent(buildEvent(childEventEl, null));
			}
		}

		Element childrenEl = itemEl.element("children");
		if (childrenEl != null) {
			for (Element childEl : (List<Element>) childrenEl.elements()) {
				item.addChild(buildContextMenuItem(childEl));
			}
		}
		return item;
	}

	@SuppressWarnings("unchecked")
	private PropertyValue buildPropertyValue(DataType type, Element propertyEl) throws DocumentException {

		Element defaulLocaleEl = propertyEl.element("defaultLocale");
		LocaleElement defaultLocale = parseLocale(defaulLocaleEl.attributeValue("locale"));

		PropertyValue result = new PropertyValue(type, defaultLocale);
		result.setReplaceable(Boolean.valueOf(propertyEl.attributeValue("replace")));

		Element localesEl = propertyEl.element("locales");
		if (localesEl != null) {
			for (Element localeEl : (List<Element>) localesEl.elements("value")) {
				LocaleElement locale = parseLocale(localeEl.attributeValue("locale"));
				result.setValue(locale, buildDataValue(localeEl));
			}
		}
		return result;
	}

	private LocaleElement parseLocale(String locale) {
		if (locale == null || locale.isEmpty()) {
			return application.getDefaultLocale();
		}
		return LocaleElement.valueOf(locale);
	}

	private DataValue buildDataValue(Element dataValueEl) throws DocumentException {
		DataType t = DataType.valueOf(dataValueEl.attributeValue("type"));
		String value;
		String label = null;
		if (DataType.LIST == t) {
			value = dataValueEl.elementText("id");
			label = dataValueEl.elementText("label");
		} else {
			value = dataValueEl.getText();
		}
		DataValue result = parseStringValue(t, value, label);
		return result;
	}

	@SuppressWarnings("unchecked")
	private EventElement buildEvent(Element eventEl, String parentId) throws DocumentException {
		EventType type = EventType.valueOf(eventEl.attributeValue("type"));
		final EventElement event = new EventElement();
		event.setType(type);

		String id = eventEl.attributeValue("id");
		event.setId(id);
		event.setName(eventEl.attributeValue("name"));

		final Element propertiesEl = eventEl.element("properties");

		if (propertiesEl.element("dataSourceId") != null) {
			finalization(new Runnable() {
				@Override
				public void run() {
					event.setDataSource(
                            map(propertiesEl.elementText("dataSourceId"), DataSourceElement.class));
				}
			});

		}

		event.setSchema(propertiesEl.elementText("schema"));
		event.setSource(propertiesEl.elementText("source"));
		event.setFunction(propertiesEl.elementText("function"));
		event.setNamed(parseBoolean(propertiesEl.elementText("isNamed"), false));
		event.setConfirm(parseBoolean(propertiesEl.elementText("isConfirm"), false));
		event.setWait(parseBoolean(propertiesEl.elementText("isWait"), false));
		event.setHandlerType(propertiesEl.elementText("handlerType"));
		event.setCode(propertiesEl.elementText("code"));
		event.setCreateNew(parseBoolean(propertiesEl.elementText("createNew"), false));

		Element waitTextEl = propertiesEl.element("waitText");
		PropertyValue waitText = buildPropertyValue(DataType.STRING, waitTextEl);
		event.setWaitText(waitText);

		Element confirmTextEl = propertiesEl.element("confirmText");
		PropertyValue confirmText = buildPropertyValue(DataType.STRING, confirmTextEl);
		event.setConfirmText(confirmText);

		if (propertiesEl.element("componentId") != null) {
			finalization(new Runnable() {
				@Override
				public void run() {
					event.setComponent(
                            map(propertiesEl.elementText("componentId"), ComponentElement.class));
				}
			});
		}
		if (propertiesEl.element("targetComponentId") != null) {
			finalization(new Runnable() {
				@Override
				public void run() {
                    event.setTargetComponent(map(propertiesEl.elementText("targetComponentId"),
							ComponentElement.class));
				}
			});
		}

		if (propertiesEl.element("tableId") != null) {
			tableEvents.put(event, propertiesEl.elementText("tableId"));
		}
		if (parentId != null) {
			event.setParentEventId(parentId);
		}

		Element parametersEl = eventEl.element("parameters");
		if (parametersEl != null) {
			for (Element paramEl : (List<Element>) parametersEl.elements()) {
				ParameterType EventParamType = ParameterType.valueOf(paramEl.attributeValue("type"));
				final EventParameterElement param = new EventParameterElement(EventParamType);

				param.setId(paramEl.attributeValue("id"));
				param.setIndex(parseInt(paramEl.attributeValue("index"), 0));
				param.setCode(paramEl.attributeValue("code"));
				param.setName(paramEl.attributeValue("name"));
				param.setStorageCode(paramEl.attributeValue("storageCode"));
				param.setComponentCode(paramEl.attributeValue("componentName"));
				final String compId = paramEl.attributeValue("componentId");
				param.setComponentId(compId);
				if (compId != null) {
					finalization(new Runnable() {
						@Override
						public void run() {
                            param.setComponent(map(compId, ComponentElement.class));
						}
					});
				}

				String dataTypeStr = paramEl.attributeValue("dataType");
				if (dataTypeStr != null) {
					DataType dataType = DataType.valueOf(dataTypeStr);
					param.setValue(parseStringValue(dataType, paramEl.getText(), paramEl.attributeValue("label")));
				}
				event.addParameter(param);
			}
		}
		Element subEvents = eventEl.element("events");
		if (subEvents != null) {
			for (Element childEventEl : (List<Element>) subEvents.elements()) {
				event.addSubEvent(buildEvent(childEventEl, id));
			}
		}
		putMap(event);
		return event;
	}

	@SuppressWarnings("unchecked")
	private ComponentElement buildForm(Element formEl) throws DocumentException {
		FormElement form = new FormElement();
		List<RowElement> rowsHeight = new ArrayList<RowElement>();

		Element rowsEl = formEl.element("rowsHeight");
		if (rowsEl != null) {
			for (Element heightEl : (List<Element>) rowsEl.elements()) {
				RowElement model = new RowElement();
				model.setId(heightEl.attributeValue("id"));
				model.setRow(Integer.valueOf(heightEl.attributeValue("row"))); // не
																				// может
																				// быть
																				// null
				model.setHeight(parseDouble(heightEl.getText(), null));
				rowsHeight.add(model);
			}
		}
		form.setRowsHeight(rowsHeight);

		List<ColumnElement> colsWidth = new ArrayList<ColumnElement>();
		Element columnsEl = formEl.element("columnsWidth");
		if (columnsEl != null) {
			for (Element widthEl : (List<Element>) columnsEl.elements()) {
				ColumnElement model = new ColumnElement();
				model.setId(widthEl.attributeValue("id"));
				model.setColumn(Integer.valueOf(widthEl.attributeValue("column"))); // не
																					// может
																					// быть
																					// null
				model.setWidth(parseDouble(widthEl.getText(), null));
				colsWidth.add(model);
			}
		}
		form.setColumnsWidth(colsWidth);

		Element cellsEl = formEl.element("cells");
		if (cellsEl != null) {
			for (Element cellEl : (List<Element>) cellsEl.elements()) {
				CellElement model = new CellElement(cellEl.attributeValue("id"));
				int row = Integer.valueOf(cellEl.attributeValue("row")); // не
																			// может
																			// быть
																			// null
				int column = Integer.valueOf(cellEl.attributeValue("column"));
				model.setRowSpan(parseInt(cellEl.attributeValue("rowspan"), 1));
				model.setColSpan(parseInt(cellEl.attributeValue("colspan"), 1));
				model.setBorderTop(parseInt(cellEl.attributeValue("borderTop"), 0));
				model.setBorderRight(parseInt(cellEl.attributeValue("borderRight"), 0));
				model.setBorderBottom(parseInt(cellEl.attributeValue("borderBottom"), 0));
				model.setBorderLeft(parseInt(cellEl.attributeValue("borderLeft"), 0));
				model.setBorderTopColor(cellEl.attributeValue("borderTopColor"));
				model.setBorderRightColor(cellEl.attributeValue("borderRightColor"));
				model.setBorderBottomColor(cellEl.attributeValue("borderBottomColor"));
				model.setBorderLeftColor(cellEl.attributeValue("borderLeftColor"));
				model.setBackgroundColor(cellEl.attributeValue("backgroundColor"));
				form.addCellElement(row, column, model);
			}
		}

		List<RequestElement> requests = new ArrayList<RequestElement>();
		Element requestsEl = formEl.element("requests");
		if (requestsEl != null) {
			for (final Element requestEl : (List<Element>) requestsEl.elements()) {
				final RequestElement model = new RequestElement();
				model.setId(requestEl.attributeValue("id"));
				model.setTop(Integer.valueOf(requestEl.attributeValue("top"))); // не
																				// может
																				// быть
																				// null
				model.setBottom(Integer.valueOf(requestEl.attributeValue("bottom"))); // не
																						// может
																						// быть
																						// null
				model.setEmptyText(buildPropertyValue(DataType.STRING, requestEl.element("emptyText")));
				model.setSql(requestEl.elementText("query"));

				if (requestEl.element("dataSourceId") != null) {
					finalization(new Runnable() {
						@Override
						public void run() {
                            model.setDataSource(map(requestEl.elementText("dataSourceId"),
									DataSourceElement.class));
						}
					});

				}
				requests.add(model);
			}
		}
		form.setRowRequests(requests);

		// List<CellGroupElement> groups = new ArrayList<CellGroupElement>();
		// Element groupsEl = formEl.element("cellGroups");
		// if (groupsEl != null) {
		// for (Element groupEl : (List<Element>) groupsEl.elements()) {
		// int top = Integer.valueOf(groupEl.attributeValue("top")); // не
		// // может
		// // быть
		// // null
		// int right = Integer.valueOf(groupEl.attributeValue("right")); // не
		// // может
		// // быть
		// // null
		// int bottom = Integer.valueOf(groupEl.attributeValue("bottom")); // не
		// // может
		// // быть
		// // null
		// int left = Integer.valueOf(groupEl.attributeValue("left")); // не
		// // может
		// // быть
		// // null
		// CellGroupElement model = new CellGroupElement(top, right,
		// bottom, left);
		// model.setId(groupEl.attributeValue("id"));
		// model.setTitle(buildPropertyValue(DataType.STRING, groupEl));
		// groups.add(model);
		// }
		// }
		// form.setCellGroups(groups);
		return form;
	}

	@SuppressWarnings("unchecked")
	private ComponentElement buildReport(Element reportEl) {
		ReportElement report = new ReportElement();
		List<FieldMetadata> fields = new ArrayList<FieldMetadata>();

		Element fieldsEl = reportEl.element("fields");
		for (Element fieldEl : (List<Element>) fieldsEl.elements()) {
			FieldMetadata fd = new FieldMetadata();
			fd.setId(fieldEl.attributeValue("id"));
			fd.setName(fieldEl.attributeValue("name"));
			fd.setLabel(fieldEl.attributeValue("label"));

			DataType type = DataType.valueOf(fieldEl.attributeValue("type"));

			fd.setType(type);
			fd.setEdit(true);
			fd.setView(true);
			fd.setRequired("T".equalsIgnoreCase(fieldEl.attributeValue("required")));

			if (DataType.LIST.equals(type)) {
				fd.setClassId(fieldEl.attributeValue("classid"));
				if (!StringUtils.isEmpty(fieldEl.attributeValue("listViewType"))) {
					fd.setListViewType(ListViewType.valueOf(fieldEl.attributeValue("listViewType")));
				}
			}
			fields.add(fd);
		}
		report.setFields(fields);
		return report;
	}

	public FileElement buildFile(final Element fileEl) {
		FileElement file = new FileElement();
		file.setId(fileEl.attributeValue("id"));
		file.setName(fileEl.attributeValue("name"));
		file.setFileName(fileEl.elementText("file"));
		file.setContentType(fileEl.elementText("type"));
		if (fileEl.element("content") != null) {
			file.setInputStreamProvider(new InputStreamProvider() {
				@Override
                public Object get() {
					return fromBase64(fileEl.elementText("content"));
				}

				@Override
				public String path() {
					// TODO Auto-generated method stub
					return null;
				}

			});
			file.setChecksum(Long.parseLong(fileEl.elementText("checksum")));
		}
		return file;
	}

	private InputStream fromBase64(String data) {
		InputStream byteStream = new ByteArrayInputStream(Base64.decodeBase64(data.getBytes()));
		return new BufferedInputStream(byteStream);
	}

	public ApplicationElement buildApplicationFromString(String xml, MetadataStore loader)
			throws UnsupportedEncodingException, DocumentException {
		InputStream stream = new ByteArrayInputStream(xml.getBytes("utf-8"));
		return buildApplication(stream, loader);
	}

	private DataValue parseStringValue(DataType type, String value, String label) throws DocumentException {
		DataValue result = new DataValueImpl(type);
		if (DataType.BOOLEAN == type) {
			result.setValue(Boolean.valueOf(value));
		} else if (DataType.DATE == type) {
			try {
				Date date = new SimpleDateFormat(AppConstant.DATE_FORMAT_LONGEST).parse(value);
				result.setValue(date);
			} catch (ParseException e) {
				throw new DocumentException("Wrong date format - " + value + e.getMessage());
			}
		} else if (DataType.NUMBER == type) {
			try {
				result.setValue(Double.valueOf(value));
			} catch (NumberFormatException e) {
				throw new DocumentException("Wrong number format - " + value, e);
			}
		} else if (DataType.LIST == type) {
			ListModelData listValue = new ListModelDataImpl();
			listValue.setId(value);
			listValue.setLabel(label);
			result.setValue(listValue);
		} else if (DataType.STRING == type) {
			result.setValue(value);
		} else if (DataType.FILE == type) {
			FileValue fileValue = new FileValue();
			fileValue.setName(value);
			result.setValue(fileValue);
		}
		return result;
	}

	private Integer parseInt(String value, Integer defaultValue) {
		try {
			return Integer.valueOf(value);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	private Double parseDouble(String value, Double defaultValue) {
		try {
			return Double.valueOf(value);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	private Boolean parseBoolean(String value, Boolean defaultValue) {
		if (value == null) {
			return defaultValue;
		}
		return Boolean.valueOf(value);
	}

    private ApplicationElement buildReference(Element referenceEl, MetadataStore loader) {
		String code = referenceEl.attributeValue("code");
		String version = referenceEl.attributeValue("version");
		try {

			ApplicationElement reference = loader.loadApplication(code,
					version == null ? null : Version.parseVersion(version), true);
			if (reference == null) {
				return null;
			}
			for (AbstractTableElement t : reference.getAvailableTables()) {
				if (t instanceof PlainTableElement) {
					PlainTableElement p = (PlainTableElement) t;
					putMap(p);
					for (TableColumnElement c : p.getColumns()) {
						putMap(c);
					}
				}
			}
			return reference;
		} catch (MetadataStoreException e) {
			return null;
		}

	}

	public EventElement buildEventFromTemplate(Element templEl) throws DocumentException {
		return buildEvent(templEl, null);
	}

	public ComponentElement buildComponentFromTemplate(Element compEl) throws DocumentException {
		return buildComponent(compEl);
	}

}
