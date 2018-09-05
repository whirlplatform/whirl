package org.whirlplatform.editor.server.merge;

import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;
import com.google.gwt.thirdparty.guava.common.reflect.Reflection;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Change;
import org.javers.core.diff.Diff;
import org.javers.core.diff.changetype.PropertyChange;
import org.javers.core.diff.changetype.ReferenceChange;
import org.javers.core.diff.changetype.ValueChange;
import org.javers.core.diff.changetype.container.CollectionChange;
import org.javers.core.diff.changetype.container.SetChange;
import org.javers.core.diff.changetype.container.ValueAdded;
import org.javers.core.diff.changetype.container.ValueRemoved;
import org.javers.core.diff.changetype.map.*;
import org.javers.core.metamodel.clazz.EntityDefinition;
import org.javers.core.metamodel.object.GlobalId;
import org.javers.core.metamodel.object.InstanceId;
import org.whirlplatform.editor.shared.merge.*;
import org.whirlplatform.editor.shared.visitor.SearchGraphVisitor;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.editor.*;
import org.whirlplatform.meta.shared.editor.db.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

public class ReflectionJaversDiffer implements Differ, ElementVisitor<ReflectionJaversDiffer.ChangeDifferContext> {

    class ChangeDifferContext implements ElementVisitor.VisitContext {

        private ApplicationElement left;
        private ApplicationElement right;
        private List<ChangeUnit> result;
        private Change change;
        private List<Change> allChanges;
        private Set<AbstractElement> excluded;
        // private Map<AbstractElement, ChangeUnit> rightChanges = new
        // HashMap<>();

        public ChangeDifferContext(ApplicationElement left, ApplicationElement right, List<ChangeUnit> result,
                                   Change change, List<Change> allChanges, Set<AbstractElement> excluded) {
            this.left = left;
            this.right = right;
            this.result = result;
            this.change = change;
            this.allChanges = allChanges;
            this.excluded = excluded;
        }

        public ApplicationElement getLeft() {
            return left;
        }

        public ApplicationElement getRight() {
            return right;
        }

        public void addResult(ChangeUnit changeUnit) {
            result.add(changeUnit);
        }

        public List<ChangeUnit> getResult() {
            return Collections.unmodifiableList(result);
        }

        public Change getChange() {
            return change;
        }

        public Change findChangeByObject(Object object) {
            for (Change c : allChanges) {
                if (c.getAffectedObject().get() == object) {
                    return change;
                }
            }
            return null;
        }

        public boolean isPropertyChange() {
            return change instanceof PropertyChange;
        }

        public PropertyChange getPropertyChange() {
            return isPropertyChange() ? (PropertyChange) change : null;
        }

        public String getProperty() {
            if (isPropertyChange()) {
                return ((PropertyChange) change).getPropertyName();
            }
            return null;
        }

        public boolean isMapChange() {
            return change instanceof MapChange;
        }

        public MapChange getMapChange() {
            return isMapChange() ? (MapChange) change : null;
        }

        public boolean isSetChange() {
            return change instanceof SetChange;
        }

        public SetChange getSetChange() {
            return isSetChange() ? (SetChange) change : null;
        }

        public boolean isCollectionChange() {
            return change instanceof CollectionChange;
        }

        public CollectionChange getCollectionChange() {
            return isCollectionChange() ? (CollectionChange) change : null;
        }

        public boolean isReferenceChange() {
            return change instanceof ReferenceChange;
        }

        public ReferenceChange getReferenceChange() {
            return isReferenceChange() ? (ReferenceChange) change : null;
        }

        public boolean isValueChange() {
            return change instanceof ValueChange;
        }

        public ValueChange getValueChange() {
            return isValueChange() ? (ValueChange) change : null;
        }

        @SuppressWarnings("unchecked")
        public <T> T getObject() {
            return (T) change.getAffectedObject().get();
        }

        public List<Change> getAllChanges() {
            return allChanges;
        }

        @SuppressWarnings("unchecked")
        public <T> T findObject(Object object) {
            if (!(object instanceof GlobalId)) {
                return (T) object;
            }
            for (Change c : allChanges) {
                if (c.getAffectedGlobalId().equals(object)) {
                    return (T) c.getAffectedObject().get();
                }
            }
            return null;
        }

        public void exclude(AbstractElement element) {
            excluded.add(element);
        }

        public void exclude(Collection<AbstractElement> elements) {
            excluded.addAll(elements);
        }

        // public Map<AbstractElement, ChangeUnit> getRightChanges() {
        // return rightChanges;
        // }

        public void addRightChange(AbstractElement target, ChangeUnit rightChange) {

        }
    }

    private static final Set<Class<?>> ENTITIES = new HashSet<>();
    private static final Set<Class<?>> VALUES = new HashSet<>();
    private static final Set<Class<?>> VALUE_OBJECTS = new HashSet<>();

    static {
        collectAllSubclasses(ENTITIES, AbstractElement.class);
        collectAllSubclasses(ENTITIES, CellRowCol.class);

        collectAllSubclasses(VALUES, DataValue.class);
        collectAllSubclasses(VALUES, PropertyValue.class);
        collectAllSubclasses(VALUES, RightElement.class);
        collectAllSubclasses(VALUES, AbstractCondition.class);
        collectAllSubclasses(VALUES, RightCollectionElement.class);
    }

    private static void collectAllSubclasses(Set<Class<?>> collection, Class<?> superclass) {
        try {
            Set<ClassInfo> classes = ClassPath.from(superclass.getClassLoader())
                    .getTopLevelClassesRecursive(Reflection.getPackageName(superclass));
            for (ClassInfo info : classes) {
                Class<?> base = info.load();
                if (base.isInterface() || base.isEnum()) {
                    continue;
                }
                Class<?> sup = base;
                while (sup != null) {
                    if (sup == superclass) {
                        collection.add(base);
                        break;
                    }
                    sup = sup.getSuperclass();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Object mutex = new Object();
    private Javers javers;
    private SearchGraphVisitor search = new SearchGraphVisitor();
    private ChildCollectorVisitor collector = new ChildCollectorVisitor();

    public ReflectionJaversDiffer() {
    }

    private void init() {
        if (javers == null) {
            synchronized (mutex) {
                if (javers == null) {
                    JaversBuilder builder = JaversBuilder.javers();
                    for (Class<?> c : ENTITIES) {
                        builder.registerEntity(new EntityDefinition(c, "id"));
                    }

                    for (Class<?> c : VALUES) {
                        builder.registerValue(c);
                    }

                    for (Class<?> c : VALUE_OBJECTS) {
                        builder.registerValueObject(c);
                    }

                    builder.withPrettyPrint(true);
                    javers = builder.build();
                }
            }
        }
    }

    @Override
    public ApplicationsDiff diff(ApplicationElement left, ApplicationElement right) throws DiffException {
        init();
        List<ChangeUnit> result = new ArrayList<>();
        Set<AbstractElement> excluded = new HashSet<>();

        Diff diff = javers.compare(left, right);

        // сначала собираем все изменения
        for (Change c : diff.getChanges()) {
            Object o = c.getAffectedObject().get();
            if (o instanceof AbstractElement) {
                ChangeDifferContext context = new ChangeDifferContext(left, right, result, c, diff.getChanges(),
                        excluded);
                ((AbstractElement) o).accept(context, this);
            }
        }

        // затем выкидываем все ненужные и перемещаем изменения по правам в
        // соотвествующие изменения объектов
        List<ChangeUnit> data = new ArrayList<>();
        int i = 0;
        for (ChangeUnit unit : result) {
            if (!excluded.contains(unit.getTarget())) {
                unit.setId(String.valueOf(i));
                data.add(unit);
                i++;
            }
        }
        return new ApplicationsDiff(data, left, right);
    }

    @Override
    public void visit(ChangeDifferContext ctx, AbstractElement element) {
        for (ChangeUnit c : changes(ctx, element)) {
            ctx.addResult(c);
        }
    }

    private List<ChangeUnit> changes(ChangeDifferContext ctx, AbstractElement element) {
        List<ChangeUnit> result = new ArrayList<>();
        if (ctx.isValueChange()) {
            ValueChange value = ctx.getValueChange();
            result.add(new ChangeUnit(ChangeType.Change, element, value.getPropertyName(), null,
                    ctx.<Serializable>findObject(value.getRight()), ctx.findObject(value.getLeft())));
        } else if (ctx.isReferenceChange()) {
            ReferenceChange reference = ctx.getReferenceChange();
            result.add(new ChangeUnit(ChangeType.Change, element, reference.getPropertyName(), null,
                    !reference.getRightObject().isPresent() ? null : reference.getRightObject().get(),
                    !reference.getLeftObject().isPresent() ? null : (Serializable) reference.getLeftObject().get()));
        } else if (ctx.isCollectionChange()) {
            CollectionChange collection = ctx.getCollectionChange();
            for (ValueAdded e : collection.getValueAddedChanges()) {
                result.add(new ChangeUnit(ChangeType.Add, element, collection.getPropertyName(), e.getIndex(),
                        ctx.<Serializable>findObject(e.getValue())));
            }
            for (ValueRemoved e : collection.getValueRemovedChanges()) {
                result.add(new ChangeUnit(ChangeType.Remove, element, collection.getPropertyName(), e.getIndex(), null,
                        ctx.findObject(e.getValue())));
            }
        } else if (ctx.isMapChange()) {
            MapChange map = ctx.getMapChange();
            for (EntryAdded e : map.getEntryAddedChanges()) {
                result.add(new ChangeUnit(ChangeType.Add, element, map.getPropertyName(),
                        ctx.<Serializable>findObject(e.getKey()), ctx.<Serializable>findObject(e.getValue())));
            }
            for (EntryRemoved e : map.getEntryRemovedChanges()) {
                result.add(new ChangeUnit(ChangeType.Remove, element, map.getPropertyName(),
                        ctx.<Serializable>findObject(e.getKey()), ctx.<Serializable>findObject(e.getValue())));
            }
            for (EntryValueChange e : map.getEntryValueChanges()) {
                result.add(new ChangeUnit(ChangeType.Change, element, map.getPropertyName(),
                        ctx.<Serializable>findObject(e.getKey()), ctx.<Serializable>findObject(e.getRightValue()),
                        ctx.findObject(e.getLeftValue())));
            }
        }
        return result;
    }

    @Override
    public void visit(ChangeDifferContext ctx, ApplicationElement element) {
        if (ctx.isMapChange() && ("tableRights".equals(ctx.getProperty())
                || "tableColumnRights".equals(ctx.getProperty()) || "eventRights".equals(ctx.getProperty()))) {
            MapChange map = ctx.getMapChange();
            for (EntryAdded e : map.getEntryAddedChanges()) {
                AbstractElement key = search.search(element, (String) ((InstanceId) e.getKey()).getCdoId());
                RightCollectionElement value = (RightCollectionElement) e.getValue();
                ctx.addResult(new ChangeUnit(ChangeType.Add, element, map.getPropertyName(), key, value));
            }
            for (EntryRemoved e : map.getEntryRemovedChanges()) {
                ctx.addResult(new ChangeUnit(ChangeType.Remove, element, map.getPropertyName(),
                        search.search(element, (String) ((InstanceId) e.getKey()).getCdoId()),
                        ctx.<Serializable>findObject(e.getValue())));
            }
            for (EntryValueChange e : map.getEntryValueChanges()) {
                ctx.addResult(new ChangeUnit(ChangeType.Change, element, map.getPropertyName(),
                        search.search(element, (String) ((InstanceId) e.getKey()).getCdoId()),
                        ctx.<Serializable>findObject(e.getRightValue()),
                        ctx.findObject(e.getLeftValue())));
            }
            return;
        }
        if (!"version".equals(ctx.getProperty())) {
            visit(ctx, (AbstractElement) element);
        }
    }

    @Override
    public void visit(ChangeDifferContext ctx, CellElement element) {
        // TODO
        visit(ctx, (AbstractElement) element);
    }

    @Override
    public void visit(ChangeDifferContext ctx, CellRangeElement element) {
        // TODO
        visit(ctx, (AbstractElement) element);
    }

    @Override
    public void visit(ChangeDifferContext ctx, ColumnElement element) {
        // TODO
        visit(ctx, (AbstractElement) element);
    }

    @Override
    public void visit(ChangeDifferContext ctx, ComponentElement element) {
        visit(ctx, (AbstractElement) element);
    }

    @Override
    public void visit(ChangeDifferContext ctx, EventElement element) {
        visit(ctx, (AbstractElement) element);
    }

    @Override
    public void visit(ChangeDifferContext ctx, EventParameterElement element) {
        visit(ctx, (AbstractElement) element);
    }

    @Override
    public void visit(ChangeDifferContext ctx, FileElement element) {
        visit(ctx, (AbstractElement) element);
    }

    @Override
    public void visit(ChangeDifferContext ctx, FormElement element) {
        boolean stop = false;
        if (ctx.isPropertyChange()) {
            // TODO сделаь универсально
            if ("values".equals(ctx.getProperty()) && ctx.isMapChange()) {
                stop = stop || parseFormPropertyChange(ctx, element);
            }
        }
        if (!stop) {
            visit(ctx, (ComponentElement) element);
        }
    }

    public boolean parseFormPropertyChange(ChangeDifferContext ctx, FormElement element) {
        boolean stop = false;
        MapChange c = ctx.getMapChange();
        for (EntryChange ec : c.getEntryChanges()) {
            ChangeUnit change = null;
            PropertyType key = (PropertyType) ec.getKey();
            if (ec instanceof EntryValueChange && (key == PropertyType.Rows || key == PropertyType.Columns)) {
                change = new ChangeUnit(ChangeType.Change, element.getParent(), "children", null, element,
                        search.search(ctx.getLeft(), element.getId()));
                ctx.exclude(element);
                ctx.exclude(collector.collect(element));
                stop = true;
            }
            if (change != null) {
                ctx.addResult(change);
            }
        }
        return stop;
    }

    @Override
    public void visit(ChangeDifferContext ctx, GroupElement element) {
        visit(ctx, (AbstractElement) element);
    }

    @Override
    public void visit(ChangeDifferContext ctx, ReportElement element) {
        visit(ctx, (ComponentElement) element);
    }

    @Override
    public void visit(ChangeDifferContext ctx, RequestElement element) {
        visit(ctx, (CellRangeElement) element);
    }

    @Override
    public void visit(ChangeDifferContext ctx, RightCollectionElement element) {
        visit(ctx, (AbstractElement) element);
    }

    /*
     * Все плохо с структурами типа Map<Key, Set<Value>> поэтому обрабатываем
     * вручную.
     */
    // private List<ChangeUnit> parseRightCollectionGroupRights(ChangeDifferContext
    // ctx, RightCollectionElement element) {
    // List<ChangeUnit> result = new ArrayList<>();
    // if (ctx.isMapChange()) {
    // MapChange map = ctx.getMapChange();
    // for (EntryChange e : map.getEntryChanges()) {
    // if (e instanceof EntryValueChange && ((EntryValueChange) e).getLeftValue()
    // instanceof Collection) {
    // Diff d = javers.compareCollections((Collection<RightElement>)
    // ((EntryValueChange) e).getLeftValue(),
    // (Collection<RightElement>) ((EntryValueChange) e).getRightValue(),
    // RightElement.class);
    // if (d.hasChanges()) {
    // result.add(new ChangeUnit(ChangeType.Change, element, map.getPropertyName(),
    // ctx.<Serializable>findObject(e.getKey()),
    // ctx.<Serializable>findObject(((EntryValueChange) e).getRightValue()),
    // ctx.<Serializable>findObject(((EntryValueChange) e).getLeftValue())));
    // }
    // } else if (e instanceof EntryAdded) {
    // result.add(new ChangeUnit(ChangeType.Add, element, map.getPropertyName(),
    // ctx.<Serializable>findObject(e.getKey()),
    // ctx.<Serializable>findObject(((EntryAdded) e).getValue())));
    // } else if (e instanceof EntryRemoved) {
    // result.add(new ChangeUnit(ChangeType.Remove, element, map.getPropertyName(),
    // ctx.<Serializable>findObject(e.getKey()),
    // ctx.<Serializable>findObject(((EntryRemoved) e).getValue())));
    // }
    // }
    // }
    // return result;
    // }

    @Override
    public void visit(ChangeDifferContext ctx, RowElement element) {
        visit(ctx, (AbstractElement) element);
    }

    @Override
    public void visit(ChangeDifferContext ctx, DataSourceElement element) {
        visit(ctx, (AbstractElement) element);
    }

    @Override
    public void visit(ChangeDifferContext ctx, SchemaElement element) {
        visit(ctx, (AbstractElement) element);
    }

    @Override
    public void visit(ChangeDifferContext ctx, TableColumnElement element) {
        visit(ctx, (AbstractElement) element);
    }

    @Override
    public void visit(ChangeDifferContext ctx, AbstractTableElement element) {
        visit(ctx, (AbstractElement) element);
    }

    @Override
    public void visit(ChangeDifferContext ctx, ViewElement element) {
        visit(ctx, (SourceElement) element);
    }

    @Override
    public void visit(ChangeDifferContext ctx, DatabaseTableElement element) {
        visit(ctx, (AbstractTableElement) element);
    }

    @Override
    public void visit(ChangeDifferContext ctx, PlainTableElement element) {
        visit(ctx, (DatabaseTableElement) element);
    }

    @Override
    public void visit(ChangeDifferContext ctx, DynamicTableElement element) {
        visit(ctx, (DatabaseTableElement) element);
    }

    @Override
    public void visit(ChangeDifferContext ctx, ContextMenuItemElement element) {
        visit(ctx, (AbstractElement) element);
    }

}
