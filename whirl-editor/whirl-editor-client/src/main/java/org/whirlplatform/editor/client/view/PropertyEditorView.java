package org.whirlplatform.editor.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.dom.DomQuery;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.StringLabelProvider;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.AccordionLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.FocusEvent;
import com.sencha.gxt.widget.core.client.event.FocusEvent.FocusHandler;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.DateField;
import com.sencha.gxt.widget.core.client.form.DateTimePropertyEditor;
import com.sencha.gxt.widget.core.client.form.Field;
import com.sencha.gxt.widget.core.client.form.NumberField;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor.DoublePropertyEditor;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.whirlplatform.component.client.event.ClickEvent;
import org.whirlplatform.component.client.event.ClickEvent.ClickHandler;
import org.whirlplatform.component.client.event.ClickEvent.HasClickHandlers;
import org.whirlplatform.editor.client.component.LayoutComponent;
import org.whirlplatform.editor.client.component.MultiSetCellDefaultAppearance.MultiSetCellTinyResources;
import org.whirlplatform.editor.client.component.MultiSetField;
import org.whirlplatform.editor.client.component.ParameterEditorComponent;
import org.whirlplatform.editor.client.component.PropertyValueField;
import org.whirlplatform.editor.client.image.EditorBundle;
import org.whirlplatform.editor.client.meta.EditListModelDataImpl;
import org.whirlplatform.editor.client.presenter.PropertyEditorPresenter;
import org.whirlplatform.editor.client.presenter.PropertyEditorPresenter.IPropertyEditorView;
import org.whirlplatform.editor.client.util.ComponentStore;
import org.whirlplatform.editor.client.util.ElementLabelProvider;
import org.whirlplatform.editor.client.util.PropertiesLists;
import org.whirlplatform.editor.client.util.TableComboBox;
import org.whirlplatform.editor.client.util.TableStore;
import org.whirlplatform.editor.shared.i18n.EditorMessage;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.data.ListModelData;
import org.whirlplatform.meta.shared.editor.AbstractElement;
import org.whirlplatform.meta.shared.editor.ApplicationElement;
import org.whirlplatform.meta.shared.editor.FileElement;
import org.whirlplatform.meta.shared.editor.LocaleElement;
import org.whirlplatform.meta.shared.editor.PropertyValue;

public class PropertyEditorView extends AccordionLayoutContainer implements IPropertyEditorView {

    private PropertyEditorPresenter presenter;

    private VerticalLayoutContainer propertiesContainer;

    private LayoutComponent layoutPanel;

    private Collection<LocaleElement> locales;
    private LocaleElement defaultLocale;

    private Map<PropertyType, CompositeCell> cells = new HashMap<PropertyType, CompositeCell>();

    private Field<?> focusedField;

    private AccordionLayoutAppearance appearance = GWT.create(AccordionLayoutAppearance.class);

    public PropertyEditorView() {
        super();
        initUI();
    }

    public void initPropertiesPanel() {
        propertiesContainer = new VerticalLayoutContainer();
        propertiesContainer.setScrollMode(ScrollMode.ALWAYS);
        propertiesContainer.setAdjustForScroll(true);
        propertiesContainer.getElement().getStyle().setBackgroundColor("#FFFFFF");
        propertiesContainer.getElement().getStyle().setFontSize(12, Unit.PX);

        ContentPanel panel = new ContentPanel(appearance);
        panel.setHeading(EditorMessage.Util.MESSAGE.property_parameters());
        panel.setWidget(propertiesContainer);
        add(panel);
        setActiveWidget(panel);
    }

    public void initLayoutPanel() {
        ContentPanel panel = new ContentPanel(appearance);
        panel.setHeading(EditorMessage.Util.MESSAGE.property_maket());
        layoutPanel = new LayoutComponent();
        panel.setWidget(layoutPanel);
        add(panel);

        layoutPanel.addValueChangeHandler(new ValueChangeHandler<PropertyType>() {

            @Override
            public void onValueChange(ValueChangeEvent<PropertyType> event) {
                PropertyValue value = layoutPanel.getProperty(event.getValue());
                presenter.onChangeComponentProperty(event.getValue(), defaultLocale, false,
                    value.getValue(defaultLocale).getObject());
            }
        });
    }

    private void initUI() {
        setExpandMode(ExpandMode.SINGLE_FILL);
        initPropertiesPanel();
        initLayoutPanel();
    }

    private void initLocales() {
        ApplicationElement application = presenter.getApplication();
        locales = new ArrayList<LocaleElement>(application.getLocales());
        defaultLocale = application.getDefaultLocale();
    }

    private void rebuild(Map<PropertyType, PropertyValue> properties) {
        propertiesContainer.clear();
        layoutPanel.clearFields();
        initLocales();
        Margins margins = new Margins(0, 2, 0, 2);
        VerticalLayoutData vData = new VerticalLayoutData(1, 20, margins);

        for (Entry<PropertyType, PropertyValue> prop : properties.entrySet()) {
            switch (prop.getKey()) {
                case LayoutDataMarginTop:
                case LayoutDataMarginRight:
                case LayoutDataMarginBottom:
                case LayoutDataMarginLeft:
                case LayoutDataWidth:
                case LayoutDataHeight:
                case Width:
                case Height:
                    layoutPanel.setProperty(prop.getKey(), prop.getValue());
                    break;
                default:
                    HorizontalLayoutContainer hCon = new HorizontalLayoutContainer();
                    hCon.getElement().appendChild(createHRElement());
                    propertiesContainer.add(hCon);

                    PropertyType propertyType = prop.getKey();
                    PropertyValue propertyValue = prop.getValue();
                    if (propertyValue == null) {
                        propertyValue = new PropertyValue(propertyType.getType());
                    }

                    CompositeCell cell = cells.get(propertyType);
                    if (cell == null) {
                        cell = new CompositeCell(propertyType);
                        cells.put(propertyType, cell);
                    } else {
                        cell.clearField();
                    }
                    cell.setValue(propertyValue);
                    propertiesContainer.add(cell, vData);
            }
        }
    }

    private Element createHRElement() {
        Element hr = Document.get().createHRElement();
        hr.getStyle().setColor("#DFE8F6");
        hr.getStyle().setMarginTop(1, Unit.PX);
        hr.getStyle().setMarginBottom(1, Unit.PX);
        return hr;
    }

    public Map<PropertyType, PropertyValue> getProperties() {
        Map<PropertyType, PropertyValue> result = new HashMap<PropertyType, PropertyValue>();
        for (Widget w : getChildren()) {
            if (w instanceof CompositeCell) {
                CompositeCell cell = (CompositeCell) w;
                result.put(cell.propertyType, cell.getValue());
            }
        }
        result.putAll(layoutPanel.getProperties());
        return result;
    }

    @Override
    public void setComponentProperties(ComponentType type,
                                       Map<PropertyType, PropertyValue> properties) {
        rebuild(properties);
    }

    @Override
    public PropertyEditorPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void setPresenter(PropertyEditorPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void finishEditing() {
        if (focusedField != null) {
            focusedField.finishEditing();
        }
    }

    private class ToggleWidget extends Widget implements HasClickHandlers {

        private Element img;

        private ToggleWidget(Widget widget) {
            img = DOM.createImg();
            img.setAttribute("src", EditorBundle.INSTANCE.greenToggle().getSafeUri().asString());
            img.setAttribute("align", "right");
            img.getStyle().setCursor(Cursor.POINTER);
            img.getStyle().setPosition(Position.ABSOLUTE);
            img.getStyle().setTop(0, Unit.PX);
            // label / widget = 50% / 50%
            img.getStyle().setLeft(50, Unit.PCT);
            widget.getElement().appendChild(img);
            setElement(img);
            sinkEvents(Event.ONCLICK);
            onAttach();
        }

        private void setReplaceable(boolean replaceable) {
            if (replaceable) {
                img.setAttribute("src", EditorBundle.INSTANCE.redToggle().getSafeUri().asString());
            } else {
                img.setAttribute("src",
                    EditorBundle.INSTANCE.greenToggle().getSafeUri().asString());
            }
        }

        @Override
        public void onBrowserEvent(Event event) {
            if ("click".equals(event.getType())) {
                fireEvent(new ClickEvent());
            }
            super.onBrowserEvent(event);
        }

        @Override
        public HandlerRegistration addClickHandler(ClickHandler handler) {
            return addHandler(handler, ClickEvent.getType());
        }

    }

    public class CompositeCell extends HorizontalLayoutContainer {

        private final DateTimeFormat format = DateTimeFormat.getFormat("dd.MM.yyyy HH:mm:ss.SSS");
        private PropertyValueField textField;
        private Field<?> typeField;
        private ToggleWidget toggle;
        private PropertyType propertyType;
        private PropertyValue propertyValue;

        public CompositeCell(PropertyType type) {
            propertyType = type;
            toggle = new ToggleWidget(this);
            createFields();

            HelpDecorator.pinTips(this, "propertytype/" + type.getCode().toLowerCase());
        }

        public void clearField() {
            textField.clear();
            typeField.clear();
        }

        public PropertyValue getValue() {
            return propertyValue;
        }

        @SuppressWarnings("unchecked")
        public void setValue(PropertyValue value) {
            this.propertyValue = value;
            DataValue dataValue = value.getValue(value.getDefaultLocale());

            if (value.isReplaceable()) {
                if (PropertyType.Parameters == propertyType) {
                    ((ParameterEditorComponent) typeField).setValue(dataValue.getString());
                } else {
                    for (LocaleElement locale : value.getLocales()) {
                        textField.setValue(locale, value.getValue(locale).getString());
                    }

                    LocaleElement key =
                        value.getDefaultLocale() != null ? value.getDefaultLocale() :
                            textField.getCurrentKey();
                    restoreValue(textField, key);
                }
                rebuild(true);
                return;
            }

            if (PropertyType.Parameters == propertyType) {
                ((ParameterEditorComponent) typeField).setValue(dataValue.getString());
            } else if (DataType.BOOLEAN == dataValue.getType()) {
                ((CheckBox) typeField).setValue(dataValue.getBoolean());
            } else if (DataType.DATE.equals(dataValue.getType())) {
                ((DateField) typeField).setValue(dataValue.getDate());
            } else if (DataType.FILE.equals(dataValue.getType())) {
                // У нас не может быть свойства типа файл
            } else if (DataType.LIST.equals(dataValue.getType())
                && dataValue.getListModelData() != null) {
                ListModelData l = dataValue.getListModelData();
                AbstractElement e = ((ComboBox<AbstractElement>) typeField).getStore()
                    .findModelWithKey(l.getId());
                ((ComboBox<AbstractElement>) typeField).setValue(e);
                ((ComboBox<AbstractElement>) typeField).setText(l.getLabel());
            } else if (DataType.NUMBER.equals(dataValue.getType())) {
                ((NumberField<Double>) typeField).setValue(dataValue.getDouble());
            } else if (DataType.STRING.equals(dataValue.getType())) {
                if (typeField instanceof SimpleComboBox) {
                    ((SimpleComboBox<String>) typeField).setValue(dataValue.getString());
                } else {
                    for (LocaleElement locale : value.getLocales()) {
                        ((MultiSetField<LocaleElement>) typeField).setValue(locale,
                            value.getValue(locale).getString());
                    }

                    LocaleElement key =
                        value.getDefaultLocale() != null ? value.getDefaultLocale() :
                            ((MultiSetField<LocaleElement>) typeField).getCurrentKey();
                    restoreValue((MultiSetField<LocaleElement>) typeField, key);
                }
            } else {
                // Что если мы не определили тип?
            }
            rebuild(false);
        }

        private <T> void restoreValue(MultiSetField<T> field, T key) {
            field.setCurrentKey(key);
            field.redrawCell();
        }

        /**
         * Создание чекбокса
         *
         * @return
         */
        private Field<Boolean> createBooleanField() {
            CheckBox field = new CheckBox();
            return field;
        }

        private NumberField<Double> createDoubleField() {
            NumberField<Double> field = new NumberField<Double>(new DoublePropertyEditor());
            return field;
        }

        /**
         * Создание текстового поля
         *
         * @return
         */
        private PropertyValueField createStringField() {
            PropertyValueField field =
                new PropertyValueField(GWT.create(MultiSetCellTinyResources.class));
            if (propertyType.isSimple()) {
                field.setLocales(defaultLocale, Collections.emptyList());
            } else {
                field.setLocales(defaultLocale, locales);
            }

            field.setCurrentKey(defaultLocale);
            return field;
        }

        /**
         * Создание поля с типом дата
         *
         * @return
         */
        private Field<Date> createDateField() {
            DateField field = new DateField(new DateTimePropertyEditor(format));
            return field;
        }

        private Field<? extends AbstractElement> createListField() {
            Field<? extends AbstractElement> field = null;
            if (propertyType == PropertyType.DataSource) {
                field = new TableComboBox(new TableStore(presenter.getEventBus()));
            } else if (propertyType == PropertyType.ImageUrl) {
                ListStore<AbstractElement> store = new ListStore<AbstractElement>(
                    new ModelKeyProvider<AbstractElement>() {

                        @Override
                        public String getKey(AbstractElement item) {
                            return item.getId();
                        }

                    });
                store.addAll(presenter.getApplication().getImageFiles());
                FileElement img = new FileElement();
                img.setId("");
                img.setName(EditorMessage.Util.MESSAGE.property_no_data());
                field = new ComboBox<AbstractElement>(store,
                    new ElementLabelProvider<AbstractElement>());
                ((ComboBox<? extends AbstractElement>) field).setTriggerAction(TriggerAction.ALL);
            }
            return field;
        }

        private SimpleComboBox<String> createPropertyListField() {
            SimpleComboBox<String> field = new SimpleComboBox<String>(new StringLabelProvider());
            field.setForceSelection(true);
            field.setTriggerAction(TriggerAction.ALL);
            field.add(PropertiesLists.getList(propertyType));
            return field;
        }

        private Field<String> createParametersField() {
            return new ParameterEditorComponent(
                new ComponentStore(getPresenter().getEventBus(), false));
        }

        /**
         * Создание всех полей
         */
        @SuppressWarnings({"rawtypes", "unchecked"})
        private void createFields() {
            // create label field
            Label label = new Label(propertyType.getCode());
            add(label, new HorizontalLayoutData(0.5, -1, new Margins(3, 0, 0, 0)));

            // create text field
            textField = createStringField();

            // create typed field
            DataType dataType = propertyType.getType();

            if (propertyType == PropertyType.Parameters) {
                typeField = createParametersField();
            } else if (dataType == DataType.BOOLEAN) {
                typeField = createBooleanField();
                textField.setValue(String.valueOf(false));
            } else if (dataType == DataType.DATE) {
                typeField = createDateField();
            } else if (dataType == DataType.NUMBER) {
                typeField = createDoubleField();
            } else if (dataType == DataType.LIST) {
                typeField = createListField();
            } else if (PropertiesLists.containsList(propertyType)) {
                typeField = createPropertyListField();
            }
            if (typeField == null) {
                typeField = createStringField();
            }

            if (typeField instanceof MultiSetField) {
                MultiSetField localeField = (MultiSetField) typeField;
                restoreValue(localeField, defaultLocale);
            }

            if (typeField instanceof ComboBox) {
                ((ComboBox) typeField).addSelectionHandler(new SelectionHandler<Object>() {

                    @Override
                    public void onSelection(SelectionEvent<Object> event) {
                        Object value = event.getSelectedItem();
                        LocaleElement l = defaultLocale;
                        if (value instanceof AbstractElement) {
                            if (((AbstractElement) value).getId().isEmpty()) {
                                value = null;
                            } else {
                                EditListModelDataImpl m = new EditListModelDataImpl();
                                m.setId(((AbstractElement) value).getId());
                                m.setLabel(((AbstractElement) value).getName());
                                m.setElement((AbstractElement) value);
                                value = m;
                            }
                        }
                        presenter.onChangeComponentProperty(propertyType, l, false, value);
                    }
                });
            } else {
                typeField.addValueChangeHandler(new ValueChangeHandler() {

                    @Override
                    public void onValueChange(ValueChangeEvent event) {
                        LocaleElement l = defaultLocale;
                        Object value = event.getValue();
                        if (typeField instanceof MultiSetField) {
                            l = ((MultiSetField<LocaleElement>) typeField).getCurrentKey();
                        }
                        // presenter.onChangeComponentProperty(propertyType, l,
                        // false, value);
                        presenter.onChangeComponentProperty(propertyType, l,
                            propertyValue.isReplaceable(), value);
                    }

                });
                typeField.addFocusHandler(new FocusHandler() {
                    @Override
                    public void onFocus(FocusEvent event) {
                        focusedField = typeField;
                    }
                });
            }

            restoreValue(textField, defaultLocale);
            textField.addValueChangeHandler(new ValueChangeHandler<String>() {

                @Override
                public void onValueChange(ValueChangeEvent<String> event) {
                    presenter
                        .onChangeComponentProperty(propertyType, textField.getCurrentKey(),
                            true, event.getValue());
                }
            });
            textField.addFocusHandler(new FocusHandler() {
                @Override
                public void onFocus(FocusEvent event) {
                    focusedField = textField;
                }
            });

            // уменьшаем элементы
            Element input;
            input = DomQuery.selectNode("input", typeField.getElement());
            input.getStyle().setHeight(15, Unit.PX);
        }

        private boolean changeReplaceable() {
            boolean r = propertyValue.isReplaceable();
            if (PropertyType.Parameters == propertyType) {
                Map<LocaleElement, DataValue> tmp = new HashMap<LocaleElement, DataValue>();
                for (LocaleElement loc : propertyValue.getLocales()) {
                    tmp.put(loc, propertyValue.getValue(loc));
                }
                propertyValue.setReplaceable(!r);
                for (LocaleElement loc : tmp.keySet()) {
                    propertyValue.setValue(loc, tmp.get(loc));
                }
            } else {
                propertyValue.setReplaceable(!r);
            }
            rebuild(propertyValue.isReplaceable());
            return propertyValue.isReplaceable();
        }

        /**
         * Перестраиваем элементы
         *
         * @param replaceable
         */
        private void rebuild(boolean replaceable) {
            if (PropertyType.Parameters == propertyType) {
                if (this.getWidgetIndex(typeField) == -1) {
                    add(typeField, new HorizontalLayoutData(0.5, -1));
                }
            } else if (replaceable) {
                remove(typeField);
                add(textField, new HorizontalLayoutData(0.5, -1));
            } else {
                remove(textField);
                add(typeField, new HorizontalLayoutData(0.5, -1));
            }
            toggle.setReplaceable(replaceable);
            PropertyEditorView.this.forceLayout();
        }

    }
}
