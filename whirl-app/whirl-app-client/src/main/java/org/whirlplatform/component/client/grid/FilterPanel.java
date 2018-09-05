package org.whirlplatform.component.client.grid;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.TriggerFieldCell.TriggerFieldAppearance;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.Util;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.theme.base.client.field.TriggerFieldDefaultAppearance;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.BeforeQueryEvent;
import com.sencha.gxt.widget.core.client.event.BeforeQueryEvent.BeforeQueryHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.event.ShowEvent;
import com.sencha.gxt.widget.core.client.event.ShowEvent.ShowHandler;
import com.sencha.gxt.widget.core.client.form.*;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor.DoublePropertyEditor;
import org.whirlplatform.component.client.combo.ComboBoxBuilder;
import org.whirlplatform.component.client.event.SearchEvent;
import org.whirlplatform.component.client.selenium.Locator;
import org.whirlplatform.component.client.selenium.LocatorAware;
import org.whirlplatform.meta.shared.*;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.data.DataValueImpl;
import org.whirlplatform.meta.shared.data.ListModelData;
import org.whirlplatform.meta.shared.data.ListModelDataImpl;
import org.whirlplatform.meta.shared.i18n.AppMessage;

import java.util.*;

public class FilterPanel extends VerticalLayoutContainer implements ResizeHandler, SearchEvent.HasSearchHandlers, LocatorAware {

    private ClassMetadata metadata;

    private FlexTable panel = new FlexTable();
    private Map<FieldMetadata, FilterSet> fields = new HashMap<FieldMetadata, FilterSet>();

    private Window window;

    private TextButton searchBtn = new TextButton(AppMessage.Util.MESSAGE.filter());
    private TextButton clearBtn = new TextButton(AppMessage.Util.MESSAGE.clear());
    private TextButton closeBtn = new TextButton(AppMessage.Util.MESSAGE.close());

    public FilterPanel(ClassMetadata metadata) {
        super();
        this.metadata = metadata;
        initHeader();
        initFields();

        panel.setBorderWidth(1);
        panel.setCellPadding(2);
        addResizeHandler(this);
        add(panel, new VerticalLayoutData(1, -1));
        setScrollMode(ScrollMode.AUTOY);
        setAdjustForScroll(true);

        searchBtn.addSelectHandler(new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                onSearch();
            }
        });

        clearBtn.addSelectHandler(new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                onClear();
            }
        });

        closeBtn.addSelectHandler(new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                window.hide();
            }
        });
    }

    private void initHeader() {
        panel.setWidget(0, 0, new HTML(AppMessage.Util.MESSAGE.filter_name()));
        panel.setWidget(0, 1, new HTML(AppMessage.Util.MESSAGE.filter_condition()));
        panel.setWidget(0, 2, new HTML(AppMessage.Util.MESSAGE.filter_value1()));
        panel.setWidget(0, 3, new HTML(AppMessage.Util.MESSAGE.filter_value2()));
    }

    private void initFields() {
        int row = 1;
        for (FieldMetadata f : metadata.getFields()) {
            if (!f.isView() || !f.isFilter()) {
                continue;
            }
            FilterSet set = new FilterSet(f);
            panel.setWidget(row, 0, set.getLabelWidget());
            panel.setWidget(row, 1, set.getConditionWidget());

            if (DataType.DATE == f.getType() || DataType.NUMBER == f.getType()) {
                panel.setWidget(row, 3, set.getSecondValueWidget());
            } else {
                panel.getFlexCellFormatter().setColSpan(row, 2, 2);
            }

            panel.setWidget(row, 2, set.getFirstValueWidget());

            fields.put(f, set);
            row = row + 1;
        }
    }

    public void syncToLoadConfig(ClassLoadConfig loadConfig) {
        for (FilterValue f : getFilters()) {
            Object first = f.getFirstValue();
            Object second = f.getSecondValue();
            if (FilterType.NO_FILTER != f.getType() && (first != null || second != null
                    || FilterType.EMPTY == f.getType() || FilterType.NOT_EMPTY == f.getType())) {
                loadConfig.addFilter(f);
            }
        }
    }

    public void setFilters(List<FilterValue> filters) {
        for (FilterValue f : filters) {
            FilterSet set = fields.get(f.getMetadata());
            if (set != null) {
                set.syncFrom(f);
            }
        }
    }

    @Override
    public void onResize(ResizeEvent event) {
        // panel.setWidth(event.getWidth() - 6 + "px");
        int width = panel.getOffsetWidth();
        if (event.getWidth() > 1) {
            width = event.getWidth();
            panel.setWidth(width + "px");
        }
        int valuesWidth = width - 300 - 20;
        for (int row = 0; row < panel.getRowCount(); row++) {
            panel.getWidget(row, 0).setWidth("150px");
            panel.getFlexCellFormatter().setWidth(row, 0, "150px");

            panel.getWidget(row, 1).setWidth("150px");
            panel.getFlexCellFormatter().setWidth(row, 1, "150px");
            if (panel.getFlexCellFormatter().getColSpan(row, 2) == 2) {
                panel.getWidget(row, 2).setWidth(valuesWidth + 4 + "px");
                panel.getFlexCellFormatter().setWidth(row, 2, valuesWidth + "px");
            } else {
                panel.getWidget(row, 2).setWidth(valuesWidth / 2 + "px");
                panel.getFlexCellFormatter().setWidth(row, 2, valuesWidth / 2 + "px");

                panel.getWidget(row, 3).setWidth(valuesWidth / 2 + "px");
                panel.getFlexCellFormatter().setWidth(row, 3, valuesWidth / 2 + "px");
            }
        }
    }

    public void show() {
        window = new Window();
        window.setMinimizable(false);
        window.setMaximizable(true);
        window.setWidget(this);

        window.addShowHandler(new ShowHandler() {
            @Override
            public void onShow(ShowEvent event) {
                int height = com.google.gwt.user.client.Window.getClientHeight();
                if (window.getOffsetHeight() > height) {
                    window.setHeight(height);
                }
            }
        });

        window.addButton(searchBtn);

        window.addButton(clearBtn);

        window.addButton(closeBtn);

        window.setModal(true); // Т.к. если окно не модальное, всё
        // разблокируется
        window.show();
    }

    private List<FilterValue> getFilters() {
        List<FilterValue> result = new ArrayList<FilterValue>(fields.size());
        for (FieldMetadata f : fields.keySet()) {
            FilterValue filter = new FilterValue(f);
            fields.get(f).syncTo(filter);
            result.add(filter);
        }
        return result;
    }

    protected Collection<FilterSet> getFilterSets() {
        return fields.values();
    }

    private void onSearch() {
        boolean valid = true;
        for (FilterSet s : fields.values()) {
            valid = s.validate() && valid;
        }
        if (!valid) {
            return;
        }
        fireEvent(new SearchEvent());
    }

    private void onClear() {
        clearFilter();
        onSearch();
    }

    void clearFilter() {
        for (FilterSet s : fields.values()) {
            s.clear();
        }
    }

    private class FilterSet implements LocatorAware {

        private DataType type;
        private FieldMetadata fieldData;

        private HTML label;

        private Widget condition;

        private Widget firstValue;

        private Widget secondValue;

        public FilterSet(FieldMetadata fieldData) {
            this.fieldData = fieldData;
            this.type = fieldData.getType();
            condition = getConditionWidget();
        }

        public HTML getLabelWidget() {
            if (label == null) {
                label = new HTML(fieldData.getLabel());
            }
            return label;
        }

        public Widget getConditionWidget() {
            if (condition == null) {
                if (DataType.DATE == type || DataType.FILE == type || DataType.NUMBER == type
                        || DataType.STRING == type) {
                    condition = createConditionCombo();
                } else {
                    condition = new CheckBox();
                }
            }
            return condition;
        }

        @SuppressWarnings({"unchecked"})
        public void setCondition(FilterType value) {
            if (DataType.DATE == type || DataType.FILE == type || DataType.NUMBER == type || DataType.STRING == type) {
                ComboBox<FilterType> combo = (ComboBox<FilterType>) condition;
                combo.setValue(value);
            } else {
                CheckBox check = (CheckBox) condition;
                if (FilterType.EQUALS.equals(value)) {
                    check.setValue(true);
                } else {
                    check.setValue(false);
                }
            }
        }

        @SuppressWarnings("unchecked")
        public FilterType getCondition() {
            if (DataType.DATE.equals(type) || DataType.FILE == (type) || DataType.NUMBER == type
                    || DataType.STRING == type) {
                ComboBox<FilterType> combo = (ComboBox<FilterType>) condition;
                return combo.getValue();
            } else {
                CheckBox check = (CheckBox) condition;
                if (check.getValue() != null && check.getValue()) {
                    return FilterType.EQUALS;
                } else {
                    return FilterType.NO_FILTER;
                }
            }
        }

        public Widget getFirstValueWidget() {
            if (firstValue == null) {
                firstValue = getValueWidget();
            }
            return firstValue;
        }

        public Widget getValueWidget() {
            Widget w;
            if (DataType.DATE == type) {
                w = new DateField();
                DateTimePropertyEditor editor = new DateTimePropertyEditor("dd.MM.yyyy HH:mm:ss");
                ((DateField) w).setPropertyEditor(editor);
            } else if (DataType.NUMBER == type) {
                w = new NumberField<Double>(new DoublePropertyEditor());
            } else if (DataType.BOOLEAN == type) {
                w = new CheckBox();
            } else if (DataType.LIST == type) {
                // w = new SimpleComboBox<String>(
                // new StringLabelProvider<String>());
                ComboBoxBuilder<?> comboBuilder = new ComboBoxBuilder<>();
                ListModelData sourse = new ListModelDataImpl();
                sourse.setId(fieldData.getClassId());
                comboBuilder.setProperty("DataSource", new DataValueImpl(DataType.LIST, sourse));
                comboBuilder.setProperty(PropertyType.ReloadStructure.getCode(),
                        new DataValueImpl(DataType.BOOLEAN, true));
                w = comboBuilder.create();
            } else {
                w = new TextField();
            }
            return w;
        }

        public Widget getSecondValueWidget() {
            if (secondValue == null) {
                secondValue = getValueWidget();
            }
            return secondValue;
        }

        private ComboBox<FilterType> createConditionCombo() {
            SimpleComboBox<FilterType> combo = new SimpleComboBox<FilterType>(new LabelProvider<FilterType>() {

                @Override
                public String getLabel(FilterType item) {
                    return FilterTypeUtil.getLabel(item);
                }

            });
            combo.addBeforeQueryHandler(new BeforeQueryHandler<FilterType>() {

                @Override
                public void onBeforeQuery(BeforeQueryEvent<FilterType> event) {
                    event.getQuery();
                    event.setQuery("");
                }

            });
            combo.add(FilterType.NO_FILTER);
            combo.add(FilterType.EQUALS);
            combo.add(FilterType.EMPTY);
            combo.add(FilterType.NOT_EMPTY);
            if (DataType.STRING == type || DataType.FILE == type) {
                combo.add(FilterType.CONTAINS);
                combo.add(FilterType.NOT_CONTAINS);
                combo.add(FilterType.START_WITH);
                combo.add(FilterType.END_WITH);
            }
            if (DataType.NUMBER == type || DataType.DATE == type) {
                combo.add(FilterType.GREATER);
                combo.add(FilterType.LOWER);
                combo.add(FilterType.BETWEEN);
            }
            combo.setValue(FilterType.NO_FILTER);
            combo.setEditable(false);

            return combo;
        }

        @SuppressWarnings("unchecked")
        private void setValue(Widget field, Object value) {
            if (field == null) {
                return;
            }
            if (DataType.BOOLEAN == type) {
                ((CheckBox) field).setValue((Boolean) value);
            } else if (DataType.DATE == type) {
                ((DateField) field).setValue((Date) value);
            } else if (DataType.NUMBER == type) {
                ((NumberField<Double>) field).setValue((Double) value);
            } else if (DataType.LIST == type) {
                ((ComboBox<ListModelData>) field).setValue(((ListModelData) value));
            } else {
                ((TextField) field).setValue((String) value);
            }
        }

        public void setFirst(Object value) {
            setValue(firstValue, value);
        }

        @SuppressWarnings("rawtypes")
        public Object getFirst() {
            if (firstValue instanceof CheckBox) {
                return ((CheckBox) firstValue).getValue();
            } else {
                return ((ValueBaseField) firstValue).getValue();
            }
        }

        @SuppressWarnings("rawtypes")
        public Object getSecond() {
            if (secondValue == null) {
                return null;
            }
            return ((ValueBaseField) secondValue).getValue();
        }

        public void setSecond(Object value) {
            setValue(secondValue, value);
        }

        public void syncFrom(FilterValue value) {
            setCondition(value.getType());
            setFirst(value.getFirstValue());
            setSecond(value.getSecondValue());
        }

        public void syncTo(FilterValue value) {
            value.setType(getCondition());
            value.setFirstValue(getFirst());
            value.setSecondValue(getSecond());
        }

        @SuppressWarnings({"rawtypes", "unchecked"})
        public void clear() {
            if (condition instanceof ComboBox) {
                ((ComboBox<FilterType>) condition).setValue(FilterType.NO_FILTER);
            } else if (condition instanceof IsField) {
                ((IsField) condition).clear();
            }

            if (firstValue != null && firstValue instanceof IsField) {
                ((IsField) firstValue).clear();
            }
            if (secondValue != null && secondValue instanceof IsField) {
                ((IsField) secondValue).clear();
            }
        }

        private boolean isEmpty(Field<?> field) {
            Object value = field.getValue();
            if (value instanceof String) {
                return Util.isEmptyString((String) value);
            }
            return value == null;
        }

        @SuppressWarnings("unchecked")
        public boolean validate() {
            if (condition instanceof ComboBox) {
                FilterType type = ((ComboBox<FilterType>) condition).getValue();
                switch (type) {
                    case EQUALS:
                    case GREATER:
                    case LOWER:
                    case START_WITH:
                    case END_WITH:
                    case CONTAINS:
                    case NOT_CONTAINS:
                    case REVERSE:
                        if (firstValue instanceof Field && isEmpty((Field<?>) firstValue)) {
                            ((Field<?>) firstValue).markInvalid(AppMessage.Util.MESSAGE.alert_notAllRequiredFieldsIsFill());
                            return false;
                        }
                        break;
                    case BETWEEN:
                        boolean valid = true;
                        if (firstValue instanceof Field && isEmpty((Field<?>) firstValue)) {
                            ((Field<?>) firstValue).markInvalid(AppMessage.Util.MESSAGE.alert_notAllRequiredFieldsIsFill());
                            valid = false;
                        }
                        if (secondValue instanceof Field && isEmpty((Field<?>) secondValue)) {
                            ((Field<?>) secondValue)
                                    .markInvalid(AppMessage.Util.MESSAGE.alert_notAllRequiredFieldsIsFill());
                            valid = false;
                        }
                        if (!valid) {
                            return false;
                        }
                        break;
                    default:
                        break;
                }
            }
            if (firstValue instanceof Field) {
                ((Field<?>) firstValue).clearInvalid();
            }
            return true;
        }

        public Locator getLocatorByElement(Element element) {
            boolean isChild = false;
            boolean isComboItem = false;
            if (label.getElement().isOrHasChild(element))
                return createFilterLocator(label, element, true, false);
            List<Widget> existingWidgets = new ArrayList<>();
            existingWidgets.add(condition);
            existingWidgets.add(firstValue);
            if (secondValue != null) {
                existingWidgets.add(secondValue);
            }
            for (Widget widget : existingWidgets) {
                isChild = widget.getElement().isOrHasChild(element);
                isComboItem = isComboBoxItemEl(widget, element);
                if (isChild || isComboItem) {
                    return createFilterLocator(widget, element, isChild, isComboItem);
                }
            }
            return null;
        }

        private Locator createFilterLocator(Widget widget, Element element, boolean isChild, boolean isCombo) {
            Locator filterPart = new Locator(LocatorParams.TYPE_FILTER);
            String mdId = fieldData.getId();
            if (!Util.isEmptyString(mdId))
                filterPart.setParameter(LocatorParams.META_DATA_ID, mdId);
            String mdName = fieldData.getName();
            if (!Util.isEmptyString(mdName))
                filterPart.setParameter(LocatorParams.META_DATA_NAME, mdName);
            Locator widgetPart = null;
            if (label == widget) {
                widgetPart = new Locator(LocatorParams.TYPE_LABEL);
            } else if (condition == widget) {
                widgetPart = new Locator(LocatorParams.TYPE_CONDITION);
            } else if (firstValue == widget) {
                widgetPart = new Locator(LocatorParams.TYPE_FIRST_VAL);
            } else if (secondValue == widget) {
                widgetPart = new Locator(LocatorParams.TYPE_SECOND_VAL);
            }
            if (widgetPart != null) {
                Locator concreteWidgetPart = null;
                if (isChild) {
                    if (widget instanceof DateField) {
                        concreteWidgetPart = getDateFieldLocator((DateField) widget, element);
                    } else if (widget instanceof ComboBox<?>) {
                        concreteWidgetPart = getComboBoxLocator((ComboBox<?>) widget, element);
                    } else if (widget instanceof NumberField<?>) {
                        concreteWidgetPart = new Locator(LocatorParams.TYPE_NUM_FIELD);
                        concreteWidgetPart.setPart(new Locator(LocatorParams.INPUT));
                    } else if (widget instanceof TextField) {
                        concreteWidgetPart = new Locator(LocatorParams.TYPE_TEXT_FIELD);
                        concreteWidgetPart.setPart(new Locator(LocatorParams.INPUT));
                    } else if (widget instanceof HTML) {
                        concreteWidgetPart = new Locator(LocatorParams.TYPE_HTML);
                        concreteWidgetPart.setPart(new Locator(LocatorParams.TEXT));
                    } else if (widget instanceof CheckBox) {
                        concreteWidgetPart = new Locator(LocatorParams.TYPE_CHECK);
                        concreteWidgetPart.setPart(new Locator(LocatorParams.INPUT));
                    }
                } else if (isCombo) {
                    concreteWidgetPart = getComboBoxLocator((ComboBox<?>) widget, element);
                }
                widgetPart.setPart(concreteWidgetPart);
            }
            filterPart.setPart(widgetPart);
            return filterPart;

        }

        private Locator getComboBoxLocator(ComboBox<?> box, Element element) {
            Locator comboBoxPart = new Locator(LocatorParams.TYPE_COMBO);
            Locator concretePart = null;
            if (box.getCell().getInputElement(box.getElement()).isOrHasChild(element)) {
                concretePart = new Locator(LocatorParams.INPUT);
            } else if (box.getCell().getAppearance().triggerIsOrHasChild(box.getElement(), element)) {
                concretePart = new Locator(LocatorParams.TRIGGER);
            } else {
                concretePart = new Locator(LocatorParams.ITEM);
                int ind = box.getListView().indexOf(element);
                String value = box.getStore().get(ind).toString();
                concretePart.setParameter(LocatorParams.INDEX, String.valueOf(ind));
                concretePart.setParameter(LocatorParams.VALUE, value);
            }
            comboBoxPart.setPart(concretePart);
            return comboBoxPart;
        }

        private Locator getDateFieldLocator(DateField dateField, Element element) {
            Locator dfPart = new Locator(LocatorParams.TYPE_DATE_FIELD);
            Locator concretePart = null;
            if (dateField.getCell().getInputElement(dateField.getElement()).isOrHasChild(element)) {
                concretePart = new Locator(LocatorParams.INPUT);
            } else if (dateField.getCell().getAppearance().triggerIsOrHasChild(dateField.getElement(), element)) {
                concretePart = new Locator(LocatorParams.TRIGGER);
            } else {
                // TODO: Разобраться с получением конкретного элемента в пикере.
                concretePart = new Locator(LocatorParams.ITEM);
            }
            dfPart.setPart(concretePart);
            return dfPart;
        }

        /**
         * Проверяет является ли виджет комбо - боксом, а элемент частью его вьюхи.
         *
         * @param potentialComboBox
         * @param element
         * @return
         */
        private boolean isComboBoxItemEl(Widget potentialComboBox, Element element) {
            boolean result = false;
            if (potentialComboBox instanceof ComboBox<?>) {
                ComboBox<?> comboBox = (ComboBox<?>) potentialComboBox;
                ListView<?, ?> view = comboBox.getListView();
                List<Element> elements = view.getElements();
                for (Element el : elements) {
                    if (el.isOrHasChild(element)) {
                        result = true;
                        break;
                    }
                }
            }
            return result;
        }

        /**
         * Проверяет есть ли в панели фильтрации комбо - боксы и является ли элемент
         * частью одного из них.
         *
         * @param element
         * @return
         */
        protected boolean isComboBoxItemElement(Element element) {
            boolean isItem = false;
            if (condition != null) {
                isItem = isComboBoxItemEl(condition, element);
                if (isItem)
                    return isItem;
            }
            if (firstValue != null) {
                isItem = isComboBoxItemEl(firstValue, element);
                if (isItem)
                    return isItem;
            }
            if (secondValue != null) {
                isItem = isComboBoxItemEl(secondValue, element);
                if (isItem)
                    return isItem;
            }
            return isItem;
        }

        @Override
        public void fillLocatorDefaults(Locator locator, Element element) {
            // TODO Auto-generated method stub

        }

        @Override
        public Element getElementByLocator(Locator locator) {
            String fieldMDID = locator.getParameter(LocatorParams.META_DATA_ID);
            if (!Util.isEmptyString(fieldMDID)) {
                if (fieldMDID.equals(fieldData.getId())) {
                    Locator part = locator.getPart();
                    if (part != null) {
                        return getFilterElement(part);
                    }
                }
            }
            String fieldMDName = locator.getParameter(LocatorParams.META_DATA_NAME);
            if (!Util.isEmptyString(fieldMDName)) {
                if (fieldMDName.equals(fieldData.getName())) {
                    Locator part = locator.getPart();
                    if (part != null) {
                        return getFilterElement(part);
                    }
                }
            }
            return null;
        }

        private Element getFilterElement(Locator locator) {
            if (LocatorParams.TYPE_LABEL.equals(locator.getType()) && label != null) {
                return label.getElement();
            } else if (LocatorParams.TYPE_CONDITION.equals(locator.getType()) && condition != null) {
                Locator condPart = locator.getPart();
                if (condPart != null) {
                    return getSpecificElement(condPart, condition);
                }
            } else if (LocatorParams.TYPE_FIRST_VAL.equals(locator.getType())) {
                Locator firstValPart = locator.getPart();
                if (firstValPart != null) {
                    return getSpecificElement(firstValPart, firstValue);
                }
            } else if (LocatorParams.TYPE_SECOND_VAL.equals(locator.getType())) {
                Locator secondValPart = locator.getPart();
                if (secondValPart != null) {
                    return getSpecificElement(secondValPart, secondValue);
                }
            }
            return null;
        }

        private Element getSpecificElement(Locator locator, Widget widget) {
            boolean isTextField = LocatorParams.TYPE_TEXT_FIELD.equals(locator.getType());
            boolean isNumberField = LocatorParams.TYPE_NUM_FIELD.equals(locator.getType());
            if (isTextField || isNumberField) {
                if (widget instanceof ValueBaseField<?>) {
                    return ((ValueBaseField<?>) widget).getCell().getInputElement(widget.getElement());
                }
            } else if (LocatorParams.TYPE_CHECK.equals(locator.getType())) {
                if (widget instanceof CheckBox) {
                    return ((CheckBox) widget).getCell().getInputElement(widget.getElement());
                }
            } else if (LocatorParams.TYPE_COMBO.equals(locator.getType())) {
                return getComboBoxSpecificElement(locator, widget);
            } else if (LocatorParams.TYPE_DATE_FIELD.equals(locator.getType())) {
                return getDateFieldSpecificElement(locator, widget);
            }
            return null;
        }
    }

    private Element getComboBoxSpecificElement(Locator locator, Widget widget) {
        if (widget instanceof ComboBox<?>) {
            Locator comboPart = locator.getPart();
            if (comboPart == null) {
                return null;
            }
            ComboBox<?> comboBox = (ComboBox<?>) widget;
            if (LocatorParams.INPUT.equals(comboPart.getType())) {
                return comboBox.getCell().getInputElement(comboBox.getElement());
            } else if (LocatorParams.TRIGGER.equals(comboPart.getType())) {
                TriggerFieldAppearance appearance = comboBox.getCell().getAppearance();
                if (appearance instanceof TriggerFieldDefaultAppearance) {
                    TriggerFieldDefaultAppearance defApp = (TriggerFieldDefaultAppearance) appearance;
                    return comboBox.getElement().selectNode("." + defApp.getStyle().trigger());// TODO
                }
            } else if (LocatorParams.ITEM.equals(comboPart.getType()) && comboBox.isExpanded()) {
                String indexParam = comboPart.getParameter(LocatorParams.INDEX);
                if (!Util.isEmptyString(indexParam)) {
                    int index = Integer.valueOf(indexParam);
                    return comboBox.getListView().getElement(index);
                }
                String innerValParam = comboPart.getParameter(LocatorParams.VALUE);
                if (!Util.isEmptyString(innerValParam)) {
                    List<Element> elements = comboBox.getListView().getElements();
                    for (Element el : elements) {
                        int i = comboBox.getListView().indexOf(el);
                        if (innerValParam.equals(comboBox.getStore().get(i).toString())) {
                            return el;
                        }
                    }
                }
            }
        }
        return null;
    }

    private Element getDateFieldSpecificElement(Locator locator, Widget widget) {
        if (widget instanceof DateField) {
            Locator dateFieldPart = locator.getPart();
            if (dateFieldPart == null) {
                return null;
            }
            DateField dateField = (DateField) widget;
            if (LocatorParams.INPUT.equals(dateFieldPart.getType())) {
                return dateField.getCell().getInputElement(dateField.getElement());
            } else if (LocatorParams.TRIGGER.equals(dateFieldPart.getType())) {
                TriggerFieldAppearance appearance = dateField.getCell().getAppearance();
                if (appearance instanceof TriggerFieldDefaultAppearance) {
                    TriggerFieldDefaultAppearance defApp = (TriggerFieldDefaultAppearance) appearance;
                    return dateField.getElement().selectNode("." + defApp.getStyle().trigger());// TODO
                }
            } else if (LocatorParams.ITEM.equals(dateFieldPart.getType())) {
                // TODO Реализовать получение элементов DatePicker - a.
                return null;
            }
        }
        return null;
    }

    @Override
    public HandlerRegistration addSearchHandler(SearchEvent.SearchHandler handler) {
        return addHandler(handler, SearchEvent.getType());
    }

    public String getFilterString() {
        List<FilterValue> filters = getFilters();
        if (filters == null || filters.isEmpty()) {
            return null;
        }

        StringBuilder builder = new StringBuilder();
        for (FilterValue v : filters) {
            if (FilterType.NO_FILTER == v.getType()) {
                continue;
            }
            builder.append(" ").append(v.getMetadata().getRawLabel()).append(" ");
            builder.append(FilterTypeUtil.getLabel(v.getType()));
            if (FilterType.EMPTY != v.getType() && FilterType.NOT_EMPTY != v.getType()) {
                builder.append(" '");
                if (DataType.LIST == v.getMetadata().getType()) {
                    builder.append((v.<ListModelData>getFirstValue()).getLabel());
                } else if (DataType.BOOLEAN == v.getMetadata().getType()) {
                    builder.append(
                            v.<Boolean>getFirstValue() ? AppMessage.Util.MESSAGE.yes() : AppMessage.Util.MESSAGE.no());
                } else if (DataType.DATE == v.getMetadata().getType()) {
                    builder.append(DateTimeFormat.getFormat("dd.MM.yyyy hh:mm:ss").format(v.getFirstValue()));
                } else {
                    builder.append(v.<Object>getFirstValue());
                }
                builder.append("'");
            }
            builder.append(",");
            // if (FilterType.BETWEEN == v.getType()) {
            // builder.append("");
            // }
        }
        String res = null;
        if (builder.length() > 0) {
            res = AppMessage.Util.MESSAGE.filter_filter() + " :" + builder.substring(0, builder.length() - 1);
        }
        return res;
    }

    static class LocatorParams {
        public static String TYPE_FILTER_PANEL = "FilterPanel";
        public static String TYPE_SEARCH_BUTTON = "SearchButton";
        public static String TYPE_CLOSE_BUTTON = "CloseButton";
        public static String TYPE_CLEAR_BUTTON = "ClearButton";

        public static String TYPE_NUM_FIELD = "NumField";
        public static String TYPE_TEXT_FIELD = "TextField";
        public static String TYPE_DATE_FIELD = "DateField";
        public static String TYPE_COMBO = "ComboBox";
        public static String TYPE_HTML = "Html";
        public static String TYPE_CHECK = "CheckBox";

        public static String TYPE_FILTER = "Filter";
        public static String TYPE_LABEL = "Label";
        public static String TYPE_CONDITION = "Condition";
        public static String TYPE_FIRST_VAL = "FirstVal";
        public static String TYPE_SECOND_VAL = "SecondVal";
        public static String TYPE_HEADER = "Header";

        public static String VALUE = "value";
        public static String INPUT = "Input";
        public static String TRIGGER = "Trigger";
        public static String ITEM = "Item";
        public static String TEXT = "Text";
        public static String INDEX = "index";
        public static String META_DATA_ID = "mdid";
        public static String META_DATA_NAME = "mdname";
        public static String COLUMN = "col";
        public static String ROW = "row";

    }

    // Поиск элементов в панели фильтрации. Поиск от окна панели к конкретному
    // элементу.
    @Override
    public Locator getLocatorByElement(Element element) {
        Locator result = new Locator(LocatorParams.TYPE_FILTER_PANEL);
        Locator part = null;
        Locator headerPart = null;
        if (searchBtn.getElement().isOrHasChild(element)) {
            part = new Locator(LocatorParams.TYPE_SEARCH_BUTTON);
        } else if (clearBtn.getElement().isOrHasChild(element)) {
            part = new Locator(LocatorParams.TYPE_CLEAR_BUTTON);
        } else if (closeBtn.getElement().isOrHasChild(element)) {
            part = new Locator(LocatorParams.TYPE_CLOSE_BUTTON);
        } else if ((headerPart = searchFromHeaders(element)) != null) {
            part = headerPart;
        } else {
            part = searchFromFilters(element);
        }
        result.setPart(part);
        return result;
    }

    private Locator searchFromHeaders(Element element) {
        Locator headerLocator = null;
        for (int i = 0; i < panel.getCellCount(0); i++) {
            Widget widget = panel.getWidget(0, i);
            if (widget.getElement().isOrHasChild(element)) {
                headerLocator = new Locator(LocatorParams.TYPE_HEADER);
                headerLocator.setParameter(LocatorParams.COLUMN, String.valueOf(i));
                headerLocator.setParameter(LocatorParams.TEXT, ((HTML) widget).getText());
                break;
            }
        }
        return headerLocator;
    }

    private Locator searchFromFilters(Element element) {
        Locator filterPart = null;
        for (FilterSet set : fields.values()) {
            filterPart = set.getLocatorByElement(element);
            if (filterPart != null)
                return filterPart;
        }
        return filterPart;
    }

    public Widget getWindow() {
        return window;
    }

    protected boolean isWindowElement(Element element) {
        return window != null && window.getElement().isOrHasChild(element);
    }

    protected boolean isComboBoxItemElement(Element element) {
        boolean res = false;
        for (FilterSet set : fields.values()) {
            res = set.isComboBoxItemElement(element);
            if (res)
                break;
        }
        return res;
    }

    @Override
    public void fillLocatorDefaults(Locator locator, Element element) {
    }

    @Override
    public Element getElementByLocator(Locator locator) {
        Locator part = locator.getPart();
        if (part == null) {
            return window == null ? null : window.getElement();
        }
        if (LocatorParams.TYPE_SEARCH_BUTTON.equals(part.getType())) {
            return getTextButtonElement(searchBtn);
        } else if (LocatorParams.TYPE_CLEAR_BUTTON.equals(part.getType())) {
            return getTextButtonElement(clearBtn);
        } else if (LocatorParams.TYPE_CLOSE_BUTTON.equals(part.getType())) {
            return getTextButtonElement(closeBtn);
        } else if (LocatorParams.TYPE_HEADER.equals(part.getType())) {
            return getHeaderElement(part);
        } else if (LocatorParams.TYPE_FILTER.equals(part.getType())) {
            return getFilterWidgetElement(part);
        }
        return null;
    }

    private Element getFilterWidgetElement(Locator part) {
        Element result = null;
        for (FilterSet set : fields.values()) {
            result = set.getElementByLocator(part);
            if (result != null) {
                return result;
            }
        }
        return result;
    }

    private Element getHeaderElement(Locator part) {
        String colParam = part.getParameter(LocatorParams.COLUMN);
        if (!Util.isEmptyString(colParam)) {
            int colInd = Integer.valueOf(colParam);
            return panel.getWidget(0, colInd).getElement();
        }
        String colText = part.getParameter(LocatorParams.TEXT);
        if (!Util.isEmptyString(colText)) {
            for (int col = 0; col < panel.getCellCount(0); col++) {
                HTML panelHeader = (HTML) panel.getWidget(0, col);
                if (colText.equals(panelHeader.getText())) {
                    return panelHeader.getElement();
                }
            }
        }
        return null;
    }

    private XElement getTextButtonElement(final TextButton button) {
        return (button != null) ? button.getCell().getFocusElement(button.getElement()) : null;
    }
}
