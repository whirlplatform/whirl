package org.whirlplatform.meta.shared.editor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.whirlplatform.meta.shared.Theme;
import org.whirlplatform.meta.shared.component.RandomUUID;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.editor.db.AbstractTableElement;
import org.whirlplatform.meta.shared.editor.db.DataSourceElement;
import org.whirlplatform.meta.shared.editor.db.PlainTableElement;
import org.whirlplatform.meta.shared.editor.db.SchemaElement;
import org.whirlplatform.meta.shared.editor.db.TableColumnElement;

@SuppressWarnings("serial")
public class ApplicationElement extends AbstractElement {

    private Map<String, String> parameters = new HashMap<String, String>();

    private ComponentElement rootComponent;

    private LocaleElement defaultLocale;

    private Set<LocaleElement> locales = new HashSet<LocaleElement>();

    private Set<ComponentElement> freeComponents = new HashSet<ComponentElement>();

    private Set<ApplicationElement> references = new HashSet<ApplicationElement>();

    private Set<DataSourceElement> dataSources = new HashSet<DataSourceElement>();

    private PropertyValue title = new PropertyValue(DataType.STRING);
    private String code;
    private String version;
    private boolean enabled;
    private boolean guest;
    private String htmlHeader;

    private Set<EventElement> events = new HashSet<EventElement>();

    private Set<FileElement> javaScriptFiles = new HashSet<FileElement>();
    private Set<FileElement> cssFiles = new HashSet<FileElement>();
    private Set<FileElement> javaFiles = new HashSet<FileElement>();
    private Set<FileElement> imageFiles = new HashSet<FileElement>();
    private FileElement staticFile;

    private Set<GroupElement> groups = new HashSet<GroupElement>();

    private Map<AbstractTableElement, RightCollectionElement> tableRights =
        new HashMap<AbstractTableElement, RightCollectionElement>();
    private Map<TableColumnElement, RightCollectionElement> tableColumnRights =
        new HashMap<TableColumnElement, RightCollectionElement>();
    private Map<EventElement, RightCollectionElement> eventRights =
        new HashMap<EventElement, RightCollectionElement>();

    private boolean available = true;

    private Theme theme;

    public ApplicationElement() {
    }

    public void setParameter(String name, String value) {
        parameters.put(name, value);
    }

    public String getParameter(String name) {
        return parameters.get(name);
    }

    public void removeParameter(String name) {
        parameters.remove(name);
    }

    public void changeParameters(Map<String, String> parameters) {
        this.parameters.clear();
        this.parameters.putAll(parameters);
    }

    public Map<String, String> getParameters() {
        return Collections.unmodifiableMap(parameters);
    }

    public ComponentElement getRootComponent() {
        return rootComponent;
    }

    public void setRootComponent(ComponentElement rootComponent) {
        this.rootComponent = rootComponent;
        if (rootComponent != null) {
            rootComponent.setParent(null);
        }
    }

    public Collection<ComponentElement> getFreeComponents() {
        return Collections.unmodifiableSet(freeComponents);
    }

    public void addFreeComponent(ComponentElement component) {
        freeComponents.add(component);
        if (component != null) {
            component.setParent(null);
        }
    }

    public void addFreeComponents(Collection<ComponentElement> components) {
        for (ComponentElement c : components) {
            addFreeComponent(c);
        }
    }

    public void removeFreeComponent(ComponentElement component) {
        freeComponents.remove(component);
    }

    public PropertyValue getTitle() {
        return title;
    }

    public void setTitle(PropertyValue title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Версия определена только для зависимостей.
     *
     * @return version
     */
    public String getVersion() {
        return version;
    }

    /**
     * Версия определяется только для зависимостей.
     *
     * @param version
     */
    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isGuest() {
        return guest;
    }

    public void setGuest(boolean guest) {
        this.guest = guest;
    }

    public String getHtmlHeader() {
        return htmlHeader;
    }

    public void setHtmlHeader(String htmlHeader) {
        this.htmlHeader = htmlHeader;
    }

    public void addDataSource(DataSourceElement dataSource) {
        dataSources.add(dataSource);
        dataSource.setApplication(this);
    }

    public void removeDataSource(DataSourceElement dataSource) {
        dataSources.remove(dataSource);
        dataSource.setApplication(null);

        for (SchemaElement s : dataSource.getSchemas()) {
            for (AbstractTableElement t : s.getTables()) {
                removeTableColumnRights(t);
                removeTableRights(t);
            }
        }
    }

    public Collection<DataSourceElement> getDataSources() {
        return Collections.unmodifiableSet(dataSources);
    }

    public DataSourceElement getDataSource(String alias) {
        if (alias == null || alias.isEmpty()) {
            throw new IllegalArgumentException("alias is null");
        }
        for (DataSourceElement d : dataSources) {
            if (alias.equals(d.getAlias())) {
                return d;
            }
        }
        return null;
    }

    public Collection<ComponentElement> getApplicationComponents() {
        Set<ComponentElement> result = new HashSet<ComponentElement>();
        if (rootComponent != null) {
            result.add(rootComponent);
            result.addAll(getAllSubComponents(rootComponent));
        }
        for (ComponentElement c : freeComponents) {
            result.add(c);
            result.addAll(getAllSubComponents(c));
        }
        return Collections.unmodifiableSet(result);
    }

    private Collection<ComponentElement> getAllSubComponents(ComponentElement component) {
        Set<ComponentElement> result = new HashSet<ComponentElement>();
        for (ComponentElement c : component.getChildren()) {
            result.add(c);
            result.addAll(getAllSubComponents(c));
        }
        return result;
    }

    public Collection<ComponentElement> getAvailableComponents() {
        Set<ComponentElement> result = new HashSet<ComponentElement>();
        result.addAll(getApplicationComponents());

        // TODO: Реализовать компоненты подключенных приложений
        // for (ApplicationElement a : references) {
        // result.addAll(a.getApplicationComponents());
        // }
        return Collections.unmodifiableSet(result);
    }

    public Collection<AbstractTableElement> getApplicationTables() {
        Set<AbstractTableElement> result = new HashSet<AbstractTableElement>();
        for (DataSourceElement d : dataSources) {
            for (SchemaElement s : d.getSchemas()) {
                result.addAll(s.getTables());
            }
        }
        return result;
    }

    public Collection<AbstractTableElement> getTablesWithClones() {
        Set<AbstractTableElement> result = new HashSet<AbstractTableElement>();
        for (DataSourceElement d : dataSources) {
            for (SchemaElement s : d.getSchemas()) {
                for (AbstractTableElement t : s.getTables()) {
                    result.add(t);
                    if (t instanceof PlainTableElement) {
                        result.addAll(((PlainTableElement) t).getClones());
                    }
                }
            }
        }
        return result;
    }

    public Collection<AbstractTableElement> getAvailableTables() {
        Set<AbstractTableElement> result = new HashSet<AbstractTableElement>();
        result.addAll(getTablesWithClones());

        for (ApplicationElement a : references) {
            result.addAll(a.getTablesWithClones());
        }
        return result;
    }

    public void addFreeEvent(EventElement event) {
        events.add(event);
        event.setParentApplication(this);
    }

    public void removeFreeEvent(EventElement event) {
        events.remove(event);
        removeEventRights(event);
    }

    public Collection<EventElement> getFreeEvents() {
        return Collections.unmodifiableSet(events);
    }

    public void addLocale(LocaleElement locale) {
        locales.add(locale);
    }

    public void removeLocale(LocaleElement locale) {
        locales.remove(locale);
    }

    public Collection<LocaleElement> getLocales() {
        return Collections.unmodifiableSet(locales);
    }

    public void changeLocales(Collection<LocaleElement> locales) {
        this.locales.clear();
        this.locales.addAll(locales);
    }

    public void replaceJavaScriptFiles(Collection<FileElement> files) {
        javaScriptFiles.clear();
        javaScriptFiles.addAll(files);
    }

    public void addJavaScriptFile(FileElement file) {
        javaScriptFiles.add(file);
    }

    public Collection<FileElement> getJavaScriptFiles() {
        return Collections.unmodifiableSet(javaScriptFiles);
    }

    public void replaceCssFiles(Collection<FileElement> files) {
        cssFiles.clear();
        cssFiles.addAll(files);
    }

    public void addCssFile(FileElement file) {
        cssFiles.add(file);
    }

    public Collection<FileElement> getCssFiles() {
        return Collections.unmodifiableSet(cssFiles);
    }

    public void replaceJavaFiles(Collection<FileElement> files) {
        javaFiles.clear();
        javaFiles.addAll(files);
    }

    public void addJavaFile(FileElement file) {
        javaFiles.add(file);
    }

    public Collection<FileElement> getJavaFiles() {
        return Collections.unmodifiableSet(javaFiles);
    }

    public void replaceImageFiles(Collection<FileElement> files) {
        imageFiles.clear();
        imageFiles.addAll(files);
    }

    public void addImageFile(FileElement file) {
        imageFiles.add(file);
    }

    public Collection<FileElement> getImageFiles() {
        return Collections.unmodifiableSet(imageFiles);
    }

    public FileElement getStaticFile() {
        return staticFile;
    }

    public void setStaticFile(FileElement file) {
        staticFile = file;
    }

    public LocaleElement getDefaultLocale() {
        return defaultLocale;
    }

    public void setDefaultLocale(LocaleElement locale) {
        this.defaultLocale = locale;
    }

    public boolean hasLocale(LocaleElement locale) {
        return defaultLocale.equals(locale) || locales.contains(locale);
    }

    public void addGroup(GroupElement group) {
        groups.add(group);
    }

    public void removeGroup(GroupElement group) {
        groups.remove(group);
    }

    public boolean hasGroups() {
        return !groups.isEmpty();
    }

    public boolean hasGroup(String group) {
        if (group == null || group.isEmpty()) {
            return false;
        }
        return groups.stream().anyMatch(g -> group.equals(g.getGroupName()));
    }

    public void changeGroups(Collection<GroupElement> groups) {
        // удаление групп
        Iterator<GroupElement> iter = this.groups.iterator();
        List<GroupElement> tempList = new ArrayList<>();
        while (iter.hasNext()) {
            GroupElement g = iter.next();
            if (!groups.contains(g)) {
                tempList.add(g);
                // removeGroup(g);
            }
        }
        this.groups.removeAll(tempList);
        // добавление групп
        for (GroupElement newGroup : groups) {
            if (!this.groups.contains(newGroup)) {
                addGroup(newGroup);
            }
        }
    }

    public Collection<GroupElement> getGroups() {
        return Collections.unmodifiableSet(groups);
    }

    public GroupElement getGroup(String name) {
        if (name == null) {
            return null;
        }
        for (GroupElement g : groups) {
            if (name.equals(g.getGroupName())) {
                return g;
            }
        }
        return null;
    }

    private RightCollectionElement getTableRightCollection(AbstractTableElement table) {
        // TODO по идее тут нужен ReentrantReadWriteLock, но gwt его не
        // поддержывает надо дописывать его эмуляцию
        RightCollectionElement collect = null;
        synchronized (tableRights) {
            for (AbstractTableElement element : tableRights.keySet()) {
                if (element.getId() == table.getId()) {
                    collect = tableRights.get(element);
                    break;
                }
            }
            if (collect == null) {
                collect = new RightCollectionElement(table);
                collect.setId(RandomUUID.uuid());
                tableRights.put(table, collect);
            }
            return collect;
        }
    }

    public void setTableRightCollection(AbstractTableElement table,
                                        RightCollectionElement collection) {
        removeTableRights(table);
        for (RightElement r : collection.getApplicationRights()) {
            addApplicationTableRight(table, r);
        }
        for (GroupElement g : groups) {
            for (RightElement r : collection.getGroupRights(g)) {
                addGroupTableRight(table, g, r);
            }
        }
    }

    public void addApplicationTableRight(AbstractTableElement table, RightElement right) {
        getTableRightCollection(table).addApplicationRight(right);
    }

    public void addGroupTableRight(AbstractTableElement table, GroupElement group,
                                   RightElement right) {
        getTableRightCollection(table).addGroupRight(group, right);
    }

    public void removeApplicationTableRight(AbstractTableElement table, RightElement right) {
        getTableRightCollection(table).removeApplicationRight(right);
    }

    public void removeGroupTableRight(AbstractTableElement table, GroupElement group,
                                      RightElement right) {
        getTableRightCollection(table).removeGroupRight(group, right);
    }

    public void removeGroupTableRights(AbstractTableElement table, GroupElement group) {
        getTableRightCollection(table).removeGroupRights(group);
    }

    public RightCollectionElement getTableRights(AbstractTableElement table) {
        return getTableRightCollection(table);
    }

    public void removeTableRights(AbstractTableElement table) {
        synchronized (tableRights) {
            tableRights.remove(table);
        }
    }

    public Collection<RightCollectionElement> getAllTableRights() {
        synchronized (tableRights) {
            return Collections.unmodifiableCollection(tableRights.values());
        }
    }

    private RightCollectionElement getTableColumnRightCollection(TableColumnElement column) {
        RightCollectionElement collect = null;
        synchronized (tableColumnRights) {
            for (TableColumnElement element : tableColumnRights.keySet()) {
                if (element.getId() == column.getId()) {
                    collect = tableColumnRights.get(element);
                    break;
                }
            }
            if (collect == null) {
                collect = new RightCollectionElement(column);
                collect.setId(RandomUUID.uuid());
                tableColumnRights.put(column, collect);
            }
            return collect;
        }
    }

    public void setTableColumnRightCollection(TableColumnElement column,
                                              RightCollectionElement collection) {
        removeTableColumnRights(column);
        for (RightElement r : collection.getApplicationRights()) {
            addApplicationTableColumnRight(column, r);
        }
        for (GroupElement g : groups) {
            for (RightElement r : collection.getGroupRights(g)) {
                addGroupTableColumnRight(column, g, r);
            }
        }
    }

    public void addApplicationTableColumnRight(TableColumnElement column, RightElement right) {
        getTableColumnRightCollection(column).addApplicationRight(right);
    }

    public void addGroupTableColumnRight(TableColumnElement column, GroupElement group,
                                         RightElement right) {
        getTableColumnRightCollection(column).addGroupRight(group, right);
    }

    public void removeApplicationTableColumnRight(TableColumnElement column, RightElement right) {
        getTableColumnRightCollection(column).removeApplicationRight(right);
    }

    public void removeGroupTableColumnRight(TableColumnElement column, GroupElement group,
                                            RightElement right) {
        getTableColumnRightCollection(column).removeGroupRight(group, right);
    }

    public void removeGroupTableColumnRights(TableColumnElement column, GroupElement group) {
        getTableColumnRightCollection(column).removeGroupRights(group);
    }

    public RightCollectionElement getTableColumnRights(TableColumnElement column) {
        return getTableColumnRightCollection(column);
    }

    public void removeTableColumnRights(TableColumnElement column) {
        synchronized (tableColumnRights) {
            tableColumnRights.remove(column);
        }
    }

    public void removeTableColumnRights(AbstractTableElement table) {
        synchronized (tableColumnRights) {
            Iterator<Entry<TableColumnElement, RightCollectionElement>> iter =
                tableColumnRights.entrySet().iterator();
            while (iter.hasNext()) {
                Entry<TableColumnElement, RightCollectionElement> e = iter.next();
                if (table == e.getKey().getTable()) {
                    iter.remove();
                }
            }
        }
    }

    public Collection<RightCollectionElement> getAllTableColumnRights() {
        synchronized (tableColumnRights) {
            return Collections.unmodifiableSet(new HashSet<>(tableColumnRights.values()));
        }
    }

    private RightCollectionElement getEventRightCollection(EventElement event) {
        RightCollectionElement collect = null;
        synchronized (eventRights) {
            for (EventElement element : eventRights.keySet()) {
                if (element.getId() == event.getId()) {
                    collect = eventRights.get(element);
                    break;
                }
            }
            if (collect == null) {
                collect = new RightCollectionElement(event);
                collect.setId(RandomUUID.uuid());
                eventRights.put(event, collect);
            }
            return collect;
        }
    }

    public void setEventRightCollection(EventElement event, RightCollectionElement collection) {
        removeEventRights(event);
        for (RightElement r : collection.getApplicationRights()) {
            addApplicationEventRight(event, r);
        }
        for (GroupElement g : groups) {
            for (RightElement r : collection.getGroupRights(g)) {
                addGroupEventRight(event, g, r);
            }
        }
    }

    public void addApplicationEventRight(EventElement event, RightElement right) {
        getEventRightCollection(event).addApplicationRight(right);
    }

    public void addGroupEventRight(EventElement event, GroupElement group, RightElement right) {
        getEventRightCollection(event).addGroupRight(group, right);
    }

    public void removeApplicationEventRight(EventElement event, RightElement right) {
        getEventRightCollection(event).removeApplicationRight(right);
    }

    public void removeGroupEventRight(EventElement event, GroupElement group, RightElement right) {
        getEventRightCollection(event).removeGroupRight(group, right);
    }

    public void removeGroupEventRights(EventElement event, GroupElement group) {
        getEventRightCollection(event).removeGroupRights(group);
    }

    public RightCollectionElement getEventRights(EventElement event) {
        return getEventRightCollection(event);
    }

    public void removeEventRights(EventElement event) {
        eventRights.remove(event);
    }

    public Collection<RightCollectionElement> getAllEventRights() {
        synchronized (eventRights) {
            return Collections.unmodifiableCollection(eventRights.values());
        }
    }

    public void addReference(ApplicationElement ref) {
        references.add(ref);
    }

    public Collection<ApplicationElement> getReferences() {
        return Collections.unmodifiableCollection(references);
    }

    public void removeReference(ApplicationElement element) {
        references.remove(element);
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    public AbstractTableElement findTableElementById(final String tableId) {
        for (AbstractTableElement table : this.getAvailableTables()) {
            if (table.getId().equals(tableId)) {
                return table;
            }
        }
        return null;
    }

    public EventElement findFreeEventElementByIdOrCode(final String eventIdOrCode, boolean byCode) {
        EventElement result = getEventFromList(this.getFreeEvents(), eventIdOrCode, null, byCode);
        return result;
    }

    public EventElement findNextEventElement(final String parentEventId,
                                             final String nextEventCode) {
        EventElement result = null;
        for (ComponentElement component : this.getAvailableComponents()) {
            result = getEventFromList(component.getEvents(), nextEventCode, parentEventId, true);
            if (result != null) {
                break;
            }
        }
        return result;
    }

    public EventElement findEventElementById(final String eventId) {
        EventElement result = findFreeEventElementByIdOrCode(eventId, false);
        if (result == null) {
            for (ComponentElement comp : this.getAvailableComponents()) {
                result = getEventFromList(comp.getEvents(), eventId, null, false);
                if (result != null) {
                    break;
                }
            }
        }
        return result;
    }

    private EventElement getEventFromList(Collection<EventElement> events, String codeOrId,
                                          String parentId,
                                          boolean code) {
        if (events != null) {
            for (EventElement event : events) {
                boolean equalsCodeOrId = (code && codeOrId.equals(event.getCode())
                    || codeOrId.equals(event.getId()));
                boolean equalsParentId =
                    (parentId == null || parentId.equals(event.getParentEventId()));
                if (equalsCodeOrId && equalsParentId) {
                    return event;
                }
                EventElement subEvent =
                    getEventFromList(event.getSubEvents(), codeOrId, parentId, code);
                if (subEvent != null) {
                    return subEvent;
                }
            }
        }
        return null;
    }

    @Override
    public <T extends ElementVisitor.VisitContext> void accept(T ctx, ElementVisitor<T> visitor) {
        visitor.visit(ctx, this);
    }

}
