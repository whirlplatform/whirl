package org.whirlplatform.editor.shared.visitor;

import org.whirlplatform.editor.shared.visitor.CloneVisitor.CopyContext;
import org.whirlplatform.meta.shared.FieldMetadata;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.component.RandomUUID;
import org.whirlplatform.meta.shared.editor.*;
import org.whirlplatform.meta.shared.editor.db.*;

import javax.annotation.concurrent.NotThreadSafe;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @param <T>
 */
@NotThreadSafe
public class CloneVisitor<T extends AbstractElement> extends GraphVisitor<CopyContext> {

    /**
     * Контекст копирвоания
     *
     */
    static class CopyContext implements ElementVisitor.VisitContext {

        /**
         * Означает, что создается корень копии.
         */
        boolean root = false;

        /**
         * Копия вышелжещого элемента-родителя. Если создается кортневая копия, то
         * родитель будет пустым.
         */
        AbstractElement parentCopy;

        AbstractElement parentSource;

        CopyContext() {
            this(false);
        }

        CopyContext(boolean root) {
            this.root = root;
        }

        CopyContext(AbstractElement parentCopy, AbstractElement parentSource) {
            this(false);
            this.parentCopy = parentCopy;
            this.parentSource = parentSource;
        }

    }

    private T element;
    private T copy;
    private boolean copyId = false;
    private boolean userReferences = false;

    private Map<AbstractElement, AbstractElement> map = new IdentityHashMap<>();
    private List<Runnable> finalizations = new ArrayList<>();

    /**
     * @param element element to copy
     */
    public CloneVisitor(T element, boolean copyId, boolean userReferences) {
        this.element = element;
        this.copyId = copyId;
        this.userReferences = userReferences;
    }

    public T copy() {
        synchronized (this) {
            copy = null;
            map.clear();
            finalizations.clear();

            visit(new CopyContext(true), element);

            for (Runnable run : finalizations) {
                run.run();
            }

            return copy;
        }
    }

    private AbstractElement mapped(AbstractElement element) {
        return userReferences && !map.containsKey(element) ? element : map.get(element);
    }

    private void finalization(Runnable runnable) {
        finalizations.add(runnable);
    }

    @Override
    public void visit(CopyContext ctx, AbstractElement element) {
        if (element instanceof ReportElement) {
            visit(ctx, (ReportElement) element);
        } else if (element instanceof FormElement) {
            visit(ctx, (FormElement) element);
        } else if (element instanceof ComponentElement) {
            visit(ctx, (ComponentElement) element);
        } else if (element instanceof PlainTableElement) {
            visit(ctx, (PlainTableElement) element);
        } else if (element instanceof TableColumnElement) {
            visit(ctx, (TableColumnElement) element);
        } else if (element instanceof EventElement) {
            visit(ctx, (EventElement) element);
        } else if (element instanceof EventParameterElement) {
            visit(ctx, (EventParameterElement) element);
        } else if (element instanceof ContextMenuItemElement) {
            visit(ctx, (ContextMenuItemElement) element);
        }

    }

    private void copyAbstractElementProperties(CopyContext ctx, AbstractElement src, AbstractElement dest) {
        if (copyId) {
            dest.setId(src.getId());
        } else {
            dest.setId(RandomUUID.uuid());
        }
        dest.setName(src.getName());
    }

    @Override
    public void visit(CopyContext ctx, ApplicationElement element) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(CopyContext ctx, CellElement element) {
        CellElement dest = new CellElement();
        if (ctx.root) {
            copy = (T) dest;
        }
        copyCellElementProperties(ctx, element, dest);
        super.visit(new CopyContext(dest, element), element);
        map.put(element, dest);
    }

    private void copyCellElementProperties(CopyContext ctx, CellElement src, CellElement dest) {
        copyAbstractElementProperties(ctx, src, dest);
        // private int colSpan = 1;
        dest.setColSpan(src.getColSpan());
        // private int rowSpan = 1;
        dest.setRowSpan(src.getRowSpan());
        // private int borderTop;
        dest.setBorderTop(src.getBorderTop());
        // private int borderRight;
        dest.setBorderRight(src.getBorderRight());
        // private int borderBottom;
        dest.setBorderBottom(src.getBorderBottom());
        // private int borderLeft;
        dest.setBorderLeft(src.getBorderLeft());
        // private String borderTopColor;
        dest.setBorderTopColor(src.getBorderTopColor());
        // private String borderRightColor;
        dest.setBorderRightColor(src.getBorderRightColor());
        // private String borderBottomColor;
        dest.setBorderBottomColor(src.getBorderBottomColor());
        // private String borderLeftColor;
        dest.setBorderLeftColor(src.getBorderLeftColor());
        // private String backgroundColor;
        dest.setBackgroundColor(src.getBackgroundColor());

        if (ctx.parentCopy instanceof FormElement) {
            for (Entry<CellRowCol, CellElement> e : ((FormElement) ctx.parentSource).getCells().entrySet()) {
                if (e.getValue().getId().equals(src.getId())) {
                    ((FormElement) ctx.parentCopy).addCellElement(e.getKey().getRow(), e.getKey().getCol(), dest);
                    break;
                }
            }
        }
    }

    @Override
    public void visit(CopyContext ctx, CellRangeElement element) {
        // TODO Auto-generated method stub

    }

    private void copyCellRangeElementProperties(CopyContext ctx, CellRangeElement src, CellRangeElement dest) {
        copyAbstractElementProperties(ctx, src, dest);
        // private int top;
        dest.setTop(src.getTop());
        // private int right;
        dest.setRight(src.getRight());
        // private int bottom;
        dest.setBottom(src.getBottom());
        // private int left;
        dest.setLeft(src.getLeft());
    }

    @Override
    public void visit(CopyContext ctx, ColumnElement element) {
        ColumnElement dest = new ColumnElement();
        if (ctx.root) {
            copy = (T) dest;
        }
        copyColumnElementProperties(ctx, element, dest);
        super.visit(new CopyContext(dest, element), element);
        map.put(element, dest);
    }

    private void copyColumnElementProperties(CopyContext ctx, ColumnElement src, ColumnElement dest) {
        copyAbstractElementProperties(ctx, src, dest);
        // private int col;
        dest.setColumn(src.getColumn());
        // private Double width;
        dest.setWidth(src.getWidth());

        if (ctx.parentCopy instanceof FormElement) {
            ((FormElement) ctx.parentCopy).getColumnsWidth().add(dest);
        }
    }

    @Override
    public void visit(CopyContext ctx, ComponentElement element) {
        ComponentElement dest = new ComponentElement();
        if (ctx.root) {
            copy = (T) dest;
        }
        copyComponentElementProperties(ctx, element, dest);
        super.visit(new CopyContext(dest, element), element);
        map.put(element, dest);
    }

    private void copyComponentElementProperties(CopyContext ctx, ComponentElement src, ComponentElement dest) {
        copyAbstractElementProperties(ctx, src, dest);
        // private ComponentType type;
        dest.setType(src.getType());
        // private Map<PropertyType, PropertyValue> values = new
        // HashMap<PropertyType, PropertyValue>();
        for (Entry<PropertyType, PropertyValue> e : src.getProperties().entrySet()) {
            dest.setProperty(e.getKey(), e.getValue() == null ? null : e.getValue().clone());
        }
        // private ComponentElement parent;
        if (ctx.parentCopy instanceof ComponentElement) {
            ((ComponentElement) ctx.parentCopy).addChild(dest);
        }
        // private Set<ComponentElement> children = new
        // HashSet<ComponentElement>();
        //
        // private Set<EventElement> events = new HashSet<EventElement>();
    }

    @Override
    public void visit(CopyContext ctx, ContextMenuItemElement element) {
        ContextMenuItemElement dest = new ContextMenuItemElement();
        if (ctx.root) {
            copy = (T) dest;
        }
        copyContextMenuItemElementProperties(ctx, element, dest);
        super.visit(new CopyContext(dest, element), element);
        map.put(element, dest);
    }

    private void copyContextMenuItemElementProperties(CopyContext ctx, ContextMenuItemElement src,
                                                      ContextMenuItemElement dest) {
        copyAbstractElementProperties(ctx, src, dest);
        // private int index = -1;
        dest.setIndex(src.getIndex());
        // private PropertyValue label = new PropertyValue(DataType.STRING);
        dest.setLabel(src.getLabel().clone());
        // private String imageUrl;
        dest.setImageUrl(src.getImageUrl());

        // private ComponentElement parentComponent;
        if (ctx.parentCopy instanceof ComponentElement) {
            ((ComponentElement) ctx.parentCopy).addContextMenuItem(dest);
        }

        if (ctx.parentCopy instanceof ContextMenuItemElement) {
            ((ContextMenuItemElement) ctx.parentCopy).addChild(dest);
        }
    }

    @Override
    public void visit(CopyContext ctx, EventElement element) {
        EventElement dest = new EventElement();
        if (ctx.root) {
            copy = (T) dest;
        }
        copyEventElementProperties(ctx, element, dest);
        super.visit(new CopyContext(dest, element), element);
        map.put(element, dest);
    }

    private void copyEventElementProperties(CopyContext ctx, final EventElement src, final EventElement dest) {
        copyAbstractElementProperties(ctx, src, dest);
        // private EventType type;
        dest.setType(src.getType());
        // private String code;
        dest.setCode(src.getCode());
        // private String handlerType;
        dest.setHandlerType(src.getHandlerType());
        //
        // TODO private DataSourceElement dataSource;
        finalization(new Runnable() {
            @Override
            public void run() {
                dest.setDataSource((DataSourceElement) mapped(src.getDataSource()));
            }
        });

        // private String schema;
        dest.setSchema(src.getSchema());
        // private String function;
        dest.setFunction(src.getFunction());
        // private String source;
        dest.setSource(src.getSource());
        //
        // TODO private ComponentElement component;
        finalization(new Runnable() {
            @Override
            public void run() {
                dest.setComponent((ComponentElement) mapped(src.getComponent()));
            }

        });

        // TODO private ComponentElement targetComponent;
        finalization(new Runnable() {
            @Override
            public void run() {
                dest.setTargetComponent((ComponentElement) mapped(src.getTargetComponent()));
            }

        });

        // private boolean confirm = false;
        dest.setConfirm(src.isConfirm());
        // private PropertyValue confirmText = new
        // PropertyValue(DataType.STRING);
        if (src.getConfirmText() != null) {
            dest.setConfirmText(src.getConfirmText().clone());
        }
        // private boolean wait = false;
        dest.setWait(src.isWait());
        // private PropertyValue waitText = new PropertyValue(DataType.STRING);
        if (src.getWaitText() != null) {
            dest.setWaitText(src.getWaitText().clone());
        }
        // private boolean named = false;
        dest.setNamed(src.isNamed());
        // private boolean createNew = false;
        dest.setCreateNew(src.isCreateNew());

        if (ctx.parentCopy instanceof EventElement) {
            ((EventElement) ctx.parentCopy).addSubEvent(dest);
        }
        if (ctx.parentCopy instanceof ComponentElement) {
            ((ComponentElement) ctx.parentCopy).addEvent(dest);
        }
        if (ctx.parentCopy instanceof ContextMenuItemElement) {
            ((ContextMenuItemElement) ctx.parentCopy).addEvent(dest);
        }
    }

    @Override
    public void visit(CopyContext ctx, EventParameterElement element) {
        EventParameterElement dest = new EventParameterElement();
        if (ctx.root) {
            copy = (T) dest;
        }
        copyEventParameterElementProperties(ctx, element, dest);
        super.visit(new CopyContext(dest, element), element);
        map.put(element, dest);
    }

    private void copyEventParameterElementProperties(CopyContext ctx, EventParameterElement src,
                                                     EventParameterElement dest) {
        copyAbstractElementProperties(ctx, src, dest);
        // private int index = -1;
        dest.setIndex(src.getIndex());
        // private String code;
        dest.setCode(src.getCode());
        // private ParameterType type;
        dest.setType(src.getType());
        // private String componentId;
        dest.setComponentId(src.getComponentId());
        // private String componentCode;
        dest.setComponentCode(src.getComponentCode());
        // private String storageCode;
        dest.setStorageCode(src.getStorageCode());
        // private DataValue data;
        if (src.getValue() != null) {
            dest.setValue(src.getValue().clone());
        }

        if (ctx.parentCopy instanceof EventElement) {
            ((EventElement) ctx.parentCopy).addParameter(dest);
        }
    }

    @Override
    public void visit(CopyContext ctx, FileElement element) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(CopyContext ctx, FormElement element) {
        FormElement dest = new FormElement();
        if (ctx.root) {
            copy = (T) dest;
        }
        copyFormElementProperties(ctx, element, dest);
        super.visit(new CopyContext(dest, element), element);
        map.put(element, dest);
    }

    private void copyFormElementProperties(CopyContext ctx, FormElement src, FormElement dest) {
        copyComponentElementProperties(ctx, src, dest);

        // private List<RowElement> rowsHeight = new ArrayList<RowElement>();
        // private List<ColumnElement> columnsWidth = new
        // ArrayList<ColumnElement>();
        // private List<RequestElement> rowRequests = new
        // ArrayList<RequestElement>();
        // private Map<CellRowCol, CellElement> cells = new HashMap<CellRowCol,
        // CellElement>();

    }

    @Override
    public void visit(CopyContext ctx, GroupElement element) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(CopyContext ctx, ReportElement element) {
        ReportElement dest = new ReportElement();
        if (ctx.root) {
            copy = (T) dest;
        }
        copyReportElementProperties(ctx, element, dest);
        super.visit(new CopyContext(dest, element), element);
        map.put(element, dest);
    }

    private void copyReportElementProperties(CopyContext ctx, ReportElement src, ReportElement dest) {
        copyComponentElementProperties(ctx, src, dest);
        for (FieldMetadata f : src.getFields()) {
            dest.addField(f.clone());
        }
    }

    @Override
    public void visit(CopyContext ctx, RequestElement element) {
        RequestElement dest = new RequestElement();
        if (ctx.root) {
            copy = (T) dest;
        }
        copyRequestElementProperties(ctx, element, dest);
        super.visit(new CopyContext(dest, element), element);
        map.put(element, dest);
    }

    private void copyRequestElementProperties(CopyContext ctx, final RequestElement src, final RequestElement dest) {
        copyCellRangeElementProperties(ctx, src, dest);
        // private DataSourceElement datasource;
        finalization(new Runnable() {

            @Override
            public void run() {
                dest.setDataSource((DataSourceElement) mapped(src.getDataSource()));
            }

        });
        // private String sql;
        dest.setSql(src.getSql());
        // private PropertyValue emptyText = new PropertyValue(DataType.STRING);
        if (src.getEmptyText() != null) {
            dest.setEmptyText(src.getEmptyText().clone());
        }
    }

    @Override
    public void visit(CopyContext ctx, RightCollectionElement element) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(CopyContext ctx, RowElement element) {
        RowElement dest = new RowElement();
        if (ctx.root) {
            copy = (T) dest;
        }
        copyRowElementProperties(ctx, element, dest);
        super.visit(new CopyContext(dest, element), element);
        map.put(element, dest);
    }

    private void copyRowElementProperties(CopyContext ctx, RowElement src, RowElement dest) {
        copyAbstractElementProperties(ctx, src, dest);
        // private int row;
        dest.setRow(src.getRow());
        // private Double height;
        dest.setHeight(src.getHeight());

        if (ctx.parentCopy instanceof FormElement) {
            ((FormElement) ctx.parentCopy).getRowsHeight().add(dest);
        }
    }

    // DB
    @Override
    public void visit(CopyContext ctx, DataSourceElement element) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(CopyContext ctx, SchemaElement element) {
        // TODO Auto-generated method stub

    }

    @SuppressWarnings("unchecked")
    @Override
    public void visit(CopyContext ctx, TableColumnElement element) {
        TableColumnElement dest = new TableColumnElement();
        if (ctx.root) {
            copy = (T) dest;
        }
        copyTableColumnElementProperties(ctx, element, dest);
        super.visit(new CopyContext(dest, element), element);
        map.put(element, dest);
    }

    private void copyTableColumnElementProperties(CopyContext ctx, final TableColumnElement src,
                                                  final TableColumnElement dest) {
        copyAbstractElementProperties(ctx, src, dest);
        // private int index;
        dest.setIndex(src.getIndex());
        // private DataType type;
        dest.setType(src.getType());
        // private PropertyValue title = new PropertyValue(DataType.STRING);
        if (src.getTitle() != null) {
            dest.setTitle(src.getTitle().clone());
        }
        // private String columnName;
        dest.setColumnName(src.getColumnName());
        // private Integer width;
        dest.setWidth(src.getWidth());
        // private Integer height;
        dest.setHeight(src.getHeight());
        // private Integer size;
        dest.setSize(src.getSize());
        // private Integer scale;
        dest.setScale(src.getScale());
        // private String defaultValue;
        dest.setDefaultValue(src.getDefaultValue());
        // private boolean notNull = false;
        dest.setNotNull(src.isNotNull());
        // private boolean listTitle = false;
        dest.setListTitle(src.isListTitle());
        // private boolean hidden = false;
        dest.setHidden(src.isHidden());
        // private boolean filter = true;
        dest.setFilter(src.isFilter());
        // private boolean defaultOrder;
        dest.setDefaultOrder(src.isDefaultOrder());
        // private Order order;
        dest.setOrder(src.getOrder());
        // private ViewFormat viewFormat;
        dest.setViewFormat(src.getViewFormat());
        // TODO private EventMetadata event;
        // private String dataFormat;
        dest.setDataFormat(src.getDataFormat());
        // private AbstractTableElement listTable;
        if (src.getListTable() != null) {
            finalization(new Runnable() {
                @Override
                public void run() {
                    dest.setListTable((AbstractTableElement) mapped(src.getListTable()));
                }
            });
        }
        // private String function;
        dest.setFunction(src.getFunction());
        // private String regex;
        dest.setRegex(src.getRegex());
        // private PropertyValue regexMessage = new
        if (src.getRegexMessage() != null) {
            dest.setRegexMessage(src.getRegexMessage().clone());
        }
        dest.setConfigColumn(src.getConfigColumn());
        dest.setColor(src.getColor());

        // PropertyValue(DataType.STRING);
        if (ctx.parentCopy instanceof PlainTableElement) {
            ((PlainTableElement) ctx.parentCopy).addColumn(dest);
        }
    }

    @Override
    public void visit(CopyContext ctx, AbstractTableElement element) {
        // TODO Auto-generated method stub

    }

    private void copyAbstractTableElementProperties(CopyContext ctx, AbstractTableElement src,
                                                    AbstractTableElement dest) {
        copyAbstractElementProperties(ctx, src, dest);
        if (src.getTitle() != null) {
            dest.setTitle(src.getTitle().clone());
        }
        dest.setCode(src.getCode());
        dest.setEmptyRow(src.isEmptyRow());
    }

    @Override
    public void visit(CopyContext ctx, ViewElement element) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(CopyContext ctx, DatabaseTableElement element) {
        // TODO Auto-generated method stub

    }

    private void copyDatabaseTableElementProperties(CopyContext ctx, DatabaseTableElement src,
                                                    DatabaseTableElement dest) {
        copyAbstractTableElementProperties(ctx, src, dest);
        if (ctx.parentCopy instanceof SchemaElement) {
            ((SchemaElement) ctx.parentCopy).addTable(dest);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void visit(CopyContext ctx, PlainTableElement element) {
        PlainTableElement dest = new PlainTableElement();
        if (ctx.root) {
            copy = (T) dest;

        }
        copyPlainTableElementProperties(ctx, element, dest);
        super.visit(new CopyContext(dest, element), element);
        map.put(element, dest);
    }

    private void copyPlainTableElementProperties(CopyContext ctx, final PlainTableElement src,
                                                 final PlainTableElement dest) {
        copyDatabaseTableElementProperties(ctx, src, dest);
        dest.setTableName(src.getTableName());
        if (src.getIdColumn() != null) {
            finalizations.add(new Runnable() {
                @Override
                public void run() {
                    dest.setIdColumn((TableColumnElement) mapped(src.getIdColumn()));
                }
            });
        }
        if (src.getDeleteColumn() != null) {
            finalizations.add(new Runnable() {
                @Override
                public void run() {
                    dest.setDeleteColumn((TableColumnElement) mapped(src.getDeleteColumn()));
                }
            });
        }
    }

    @Override
    public void visit(CopyContext ctx, DynamicTableElement element) {
        DynamicTableElement dest = new DynamicTableElement();
        if (ctx.root) {
            copy = (T) dest;

        }
        copyDynamicTableElementProperties(ctx, element, dest);
        super.visit(new CopyContext(dest, element), element);
        map.put(element, dest);
    }

    private void copyDynamicTableElementProperties(CopyContext ctx, final DynamicTableElement src,
                                                   final DynamicTableElement dest) {
        copyDatabaseTableElementProperties(ctx, src, dest);
        // String metadataFunction;
        dest.setMetadataFunction(src.getMetadataFunction());
        // String dataFunction;
        dest.setDataFunction(src.getDataFunction());
        // String updateFunction;
        dest.setUpdateFunction(src.getUpdateFunction());
        // String deleteFunction;
        dest.setDeleteFunction(src.getDeleteFunction());
        // String insertFunction;
        dest.setInsertFunction(src.getInsertFunction());
    }

}
