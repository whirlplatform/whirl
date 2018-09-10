package org.whirlplatform.editor.client.view.widget;

import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.grid.CheckBoxSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import org.whirlplatform.editor.client.tree.visitor.VisitableTreeElement;
import org.whirlplatform.editor.shared.i18n.EditorMessage;
import org.whirlplatform.editor.shared.merge.ChangeUnit;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.editor.*;

import java.util.*;

/**
 * Простой контейнер для отображения изменений при сравнении приложений
 *
 */
public class ChangeUnitsContainer extends SimpleContainer {
    private static final String ADDED = EditorMessage.Util.MESSAGE.compare_apps_added();
    private static final String REMOVED = EditorMessage.Util.MESSAGE.compare_apps_removed();
    private static final String CHANGED = EditorMessage.Util.MESSAGE.compare_apps_changed();
    private static final String NESTED = EditorMessage.Util.MESSAGE.compare_apps_nested();
    private static final String VALUE = EditorMessage.Util.MESSAGE.compare_apps_value();
    private static final String TYPE = EditorMessage.Util.MESSAGE.compare_apps_type();
    private static final String TARGET = EditorMessage.Util.MESSAGE.compare_apps_target();
    private static final int VALUE_WIDTH = 300;
    private static final int TYPE_WIDTH = 80;
    private static final int TARGET_WIDTH = 300;
    private static final int NESTED_WIDTH = 60;

    final private Grid<ChangeUnit> grid;
    final private CheckBoxSelectionModel<ChangeUnit> selectionModel;
    final private List<ChangeUnit> undoSelected;
    final private List<ChangeUnit> undoDeselected;
    final private DisplayNameVisitor displayNameVisitor;
    private boolean locked = true;

    public ChangeUnitsContainer() {
        super();
        undoSelected = new ArrayList<>();
        undoDeselected = new ArrayList<>();
        displayNameVisitor = new DisplayNameVisitor();
        IdentityValueProvider<ChangeUnit> identity = new IdentityValueProvider<>("approved");
        selectionModel = new CheckBoxSelectionModel<>(identity);
        selectionModel.setSelectionMode(SelectionMode.MULTI);
        grid = new Grid<ChangeUnit>(new ListStore<>(createModelKeyProvider()), createColumnModel());
        grid.setSelectionModel(selectionModel);
        setWidget(grid);
    }

    public ListStore<ChangeUnit> getStore() {
        return grid.getStore();
    }

    public void loadData(Collection<? extends ChangeUnit> changeUnits) {
        turnLocked(false);
        grid.getStore().clear();
        grid.getStore().addAll(changeUnits);
        undoSelected.clear();
        undoDeselected.clear();
        for (ChangeUnit unit : changeUnits) {
            if (unit.isApproved()) {
                undoSelected.add(unit);
                grid.getSelectionModel().select(unit, true);
            } else {
                undoDeselected.add(unit);
                grid.getSelectionModel().deselect(unit);
            }
        }
        turnLocked(locked);
    }

    public void undoSelection() {
        grid.getSelectionModel().select(undoSelected, true);
        grid.getSelectionModel().deselect(undoDeselected);
    }

    public Map<String, Boolean> getSelectionMap() {
        Map<String, Boolean> result = new HashMap<String, Boolean>();
        for (ChangeUnit unit : grid.getStore().getAll()) {
            result.put(unit.getId(), false);
        }
        for (ChangeUnit unit : grid.getSelectionModel().getSelection()) {
            result.put(unit.getId(), true);
        }
        return result;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
        turnLocked(locked);
    }

    private void turnLocked(boolean locked) {
        grid.getSelectionModel().setLocked(locked);
    }

    private ModelKeyProvider<ChangeUnit> createModelKeyProvider() {
        return new ModelKeyProvider<ChangeUnit>() {
            @Override
            public String getKey(ChangeUnit item) {
                return item.getId();
            }
        };
    }

    private ColumnModel<ChangeUnit> createColumnModel() {
        List<ColumnConfig<ChangeUnit, ?>> columnsList = new ArrayList<>();
        columnsList.add(selectionModel.getColumn());
        columnsList.add(new ColumnConfig<>(createTargetValueProvider(), TARGET_WIDTH, TARGET));
        columnsList.add(new ColumnConfig<>(createTypeValueProvider(), TYPE_WIDTH, TYPE));
        columnsList.add(new ColumnConfig<>(createValueValueProvider(), VALUE_WIDTH, VALUE));
        columnsList.add(new ColumnConfig<>(createNestedValueProvider(), NESTED_WIDTH, NESTED));
        return new ColumnModel<ChangeUnit>(columnsList);
    }

    private ValueProvider<ChangeUnit, String> createValueValueProvider() {
        return new ReadOnlyValueProvider<ChangeUnit>("value") {
            @Override
            public String getValue(ChangeUnit object) {
                if (object == null) {
                    return "";
                }
                Object valueObject = (object.getRightValue() == null) ? object.getLeftValue() : object.getRightValue();
                return decipher(valueObject);
            }
        };
    }

    private ValueProvider<ChangeUnit, String> createTypeValueProvider() {
        return new ReadOnlyValueProvider<ChangeUnit>("type") {
            @Override
            public String getValue(ChangeUnit object) {
                if (object != null) {
                    switch (object.getType()) {
                        case Add:
                            return ADDED;
                        case Change:
                            return CHANGED;
                        case Remove:
                            return REMOVED;
                        default:
                            break;
                    }
                }
                return "";
            }
        };
    }

    private ValueProvider<ChangeUnit, String> createTargetValueProvider() {
        return new ReadOnlyValueProvider<ChangeUnit>("target") {
            @Override
            public String getValue(ChangeUnit object) {
                return (object == null) ? "" : decipher(object.getTarget());
            }
        };
    }

    private ValueProvider<ChangeUnit, String> createNestedValueProvider() {
        return new ReadOnlyValueProvider<ChangeUnit>("nested") {
            @Override
            public String getValue(ChangeUnit object) {
                return (object == null) ? "" : String.valueOf(object.getNestedChanges().size());
            }
        };
    }

    @SuppressWarnings("rawtypes")
    private String decipher(final Object object) {
        if (object == null) {
            return "";
        }
        if (object instanceof AbstractElement) {
            return decipherAbstractElement((AbstractElement) object);
        }
        if (object instanceof CellRowCol) {
            return decipherCellRowCol((CellRowCol) object);
        }
        if (object instanceof DataValue) {
            return decipherDataValue((DataValue) object);
        }
        if (object instanceof PropertyValue) {
            return decipherPropertyValue((PropertyValue) object);
        }
        if (object instanceof RightElement) {
            return decipherRightElement((RightElement) object);
        }
        if (object instanceof AbstractCondition) {
            return decipherAbstractCondition((AbstractCondition) object);
        }
        return "";
    }

    @SuppressWarnings("rawtypes")
    private String decipherAbstractCondition(final AbstractCondition object) {
        return buildDisplayName("Condition", object.getValue().toString(), "");
    }

    private String decipherRightElement(final RightElement object) {
        return buildDisplayName("Right", object.getType().toString(), object.getCondition().getValue().toString());
    }

    private String decipherPropertyValue(final PropertyValue object) {
        return buildDisplayName("PropertyValue", object.getDefaultValue().toString(),
                object.getDefaultValue().getType().toString());
    }

    private String decipherDataValue(final DataValue object) {
        return buildDisplayName("DataValue", object.toString(), object.getType().toString());
    }

    private String decipherCellRowCol(final CellRowCol object) {
        return buildDisplayName("CellRowCol", object.getId(), "");
    }

    private String decipherAbstractElement(final AbstractElement element) {
        if (element == null) {
            return "";
        }
        DisplayNameVisitContext ctx = visit(element);
        return buildDisplayName(ctx);
    }

    private DisplayNameVisitContext visit(final AbstractElement element) {
        DisplayNameVisitContext ctx = new DisplayNameVisitContext();
        if (element instanceof VisitableTreeElement) {
            ((VisitableTreeElement) element).accept(ctx, displayNameVisitor);
        } else {
            element.accept(ctx, displayNameVisitor);
        }
        return ctx;
    }

    private String buildDisplayName(DisplayNameVisitContext ctx) {
        return buildDisplayName(ctx.getClassName(), ctx.getDisplayName(), "");
    }

    private String buildDisplayName(final String className, final String first, final String second) {
        StringBuilder sb = new StringBuilder(className);
        if (first != null && !"".equals(first)) {
            sb.append(" (").append(first);
            if (second != null && !"".equals(second)) {
                sb.append(" : ").append(second);
            }
            sb.append(")");
        }
        return sb.toString();
    }
}
