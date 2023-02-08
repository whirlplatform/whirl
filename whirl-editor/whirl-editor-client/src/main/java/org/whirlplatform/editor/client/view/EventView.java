package org.whirlplatform.editor.client.view;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import com.sencha.gxt.widget.core.client.form.TextField;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import org.geomajas.codemirror.client.Config;
import org.geomajas.codemirror.client.widget.CodeMirrorPanel;
import org.whirlplatform.component.client.event.AttachEvent;
import org.whirlplatform.component.client.event.BlurEvent;
import org.whirlplatform.component.client.event.ChangeEvent;
import org.whirlplatform.component.client.event.ClickEvent;
import org.whirlplatform.component.client.event.CreateEvent;
import org.whirlplatform.component.client.event.DeleteEvent;
import org.whirlplatform.component.client.event.DetachEvent;
import org.whirlplatform.component.client.event.DoubleClickEvent;
import org.whirlplatform.component.client.event.FocusEvent;
import org.whirlplatform.component.client.event.HideEvent;
import org.whirlplatform.component.client.event.InsertEvent;
import org.whirlplatform.component.client.event.KeyPressEvent;
import org.whirlplatform.component.client.event.LoadEvent;
import org.whirlplatform.component.client.event.RefreshEvent;
import org.whirlplatform.component.client.event.RowDoubleClickEvent;
import org.whirlplatform.component.client.event.SelectEvent;
import org.whirlplatform.component.client.event.ShowEvent;
import org.whirlplatform.component.client.event.TimeEvent;
import org.whirlplatform.component.client.event.UpdateEvent;
import org.whirlplatform.editor.client.component.PropertyValueField;
import org.whirlplatform.editor.client.presenter.EventPresenter;
import org.whirlplatform.editor.client.presenter.EventPresenter.IEventView;
import org.whirlplatform.editor.client.util.ComponentComboBox;
import org.whirlplatform.editor.client.util.ComponentStore;
import org.whirlplatform.editor.client.util.DataSourceComboBox;
import org.whirlplatform.editor.client.util.DataSourceStore;
import org.whirlplatform.editor.shared.i18n.EditorMessage;
import org.whirlplatform.meta.shared.EventType;
import org.whirlplatform.meta.shared.editor.ComponentElement;
import org.whirlplatform.meta.shared.editor.LocaleElement;
import org.whirlplatform.meta.shared.editor.PropertyValue;
import org.whirlplatform.meta.shared.editor.db.DataSourceElement;

public class EventView extends ContentPanel implements IEventView {

    private EventPresenter presenter;

    private VerticalLayoutContainer container;

    private ComboBox<EventType> type;
    private FieldLabel typeLabel;
    private ComboBox<String> handlerType;
    private FieldLabel handlerTypeLabel;
    private TextField code;
    private FieldLabel codeLabel;
    private CheckBox named;
    private FieldLabel namedLabel;
    private CheckBox confirm;
    private FieldLabel confirmLabel;
    private PropertyValueField confirmText;
    private FieldLabel confirmTextLabel;
    private CheckBox wait;
    private FieldLabel waitLabel;
    private PropertyValueField waitText;
    private FieldLabel waitTextLabel;
    private TextField schema;
    private FieldLabel schemaLabel;
    private TextField function;
    private FieldLabel functionLabel;
    private FieldLabel sourceLabel;
    private ComboBox<ComponentElement> component;
    private FieldLabel componentLabel;
    private ComboBox<ComponentElement> targetComponent;
    private FieldLabel targetComponentLabel;
    private CodeMirrorPanel source;
    private ComboBox<DataSourceElement> dataSource;
    private FieldLabel dataSourceLabel;
    private CheckBox createNew;
    private FieldLabel createNewLabel;

    public EventView() {
        super();
    }

    @Override
    public void initUI() {
        setHeaderVisible(true);
        container = new VerticalLayoutContainer();
        container.setAdjustForScroll(true);
        container.setScrollMode(ScrollMode.AUTO);
        setWidget(container);
        initFields();
    }

    private ComboBox<EventType> initTypeField() {
        ListStore<EventType> store = new ListStore<EventType>(
            new ModelKeyProvider<EventType>() {
                @Override
                public String getKey(EventType item) {
                    return item.name();
                }
            });
        store.addAll(Arrays.stream(EventType.values())
            .filter(eventType -> !EventType.Java.equals(eventType))
            .collect(Collectors.toList()));

        ComboBox<EventType> result = new ComboBox<EventType>(store,
            new LabelProvider<EventType>() {
                @Override
                public String getLabel(EventType item) {
                    return item.name();
                }
            });
        result.setAllowBlank(false);
        result.setEditable(false);
        return result;
    }

    private ComboBox<String> initHandlerTypeField() {
        SimpleComboBox<String> result = new SimpleComboBox<String>(
            new LabelProvider<String>() {
                @Override
                public String getLabel(String item) {
                    int subStrEnd = item.indexOf("Handler");
                    return subStrEnd == -1 ? item : item.substring(0, subStrEnd);
                }
            });

        // TODO выделить в переиспользуемый список определяемый по типу
        // компонента
        result.add(AttachEvent.getType().toString());
        result.add(BlurEvent.getType().toString());
        result.add(ChangeEvent.getType().toString());
        result.add(ClickEvent.getType().toString());
        result.add(DoubleClickEvent.getType().toString());
        result.add(CreateEvent.getType().toString());
        result.add(DeleteEvent.getType().toString());
        result.add(DetachEvent.getType().toString());
        result.add(RowDoubleClickEvent.getType().toString());
        result.add(FocusEvent.getType().toString());
        result.add(HideEvent.getType().toString());
        result.add(InsertEvent.getType().toString());
        result.add(KeyPressEvent.getType().toString());
        result.add(LoadEvent.getType().toString());
        result.add(RefreshEvent.getType().toString());
        result.add(SelectEvent.getType().toString());
        result.add(ShowEvent.getType().toString());
        result.add(TimeEvent.getType().toString());
        result.add(UpdateEvent.getType().toString());
        return result;
    }

    private void initFields() {
        type = initTypeField();
        type.setAllowBlank(false);
        type.setTriggerAction(TriggerAction.ALL);
        type.addSelectionHandler(new SelectionHandler<EventType>() {

            @Override
            public void onSelection(SelectionEvent<EventType> event) {
                changeVisibility(event.getSelectedItem());
            }
        });
        typeLabel = new FieldLabel(type,
            EditorMessage.Util.MESSAGE.event_type());
        container.add(typeLabel, new VerticalLayoutData(1, -1, new Margins(10,
            10, 0, 10)));

        handlerType = initHandlerTypeField();
        handlerType.setAllowBlank(true);
        handlerType.setTriggerAction(TriggerAction.ALL);
        handlerType.setEditable(false);
        handlerTypeLabel = new FieldLabel(handlerType,
            EditorMessage.Util.MESSAGE.event_handler());
        container.add(handlerTypeLabel, new VerticalLayoutData(1, -1,
            new Margins(10, 10, 0, 10)));

        code = new TextField();
        codeLabel = new FieldLabel(code,
            EditorMessage.Util.MESSAGE.event_code());
        container.add(codeLabel, new VerticalLayoutData(1, -1, new Margins(10,
            10, 0, 10)));

        named = new CheckBox();
        named.setBoxLabel("");
        namedLabel = new FieldLabel(named,
            EditorMessage.Util.MESSAGE.event_single_parameter());
        container.add(namedLabel, new VerticalLayoutData(-1, -1, new Margins(10,
            10, 0, 10)));

        confirm = new CheckBox();
        confirm.setBoxLabel("");
        confirmLabel = new FieldLabel(confirm,
            EditorMessage.Util.MESSAGE.event_confirm());
        container.add(confirmLabel, new VerticalLayoutData(-1, -1, new Margins(
            10, 10, 0, 10)));

        confirmText = new PropertyValueField();
        confirmTextLabel = new FieldLabel(confirmText,
            EditorMessage.Util.MESSAGE.event_confirm_text());
        container.add(confirmTextLabel, new VerticalLayoutData(1, -1,
            new Margins(10, 10, 0, 10)));

        wait = new CheckBox();
        wait.setBoxLabel("");
        waitLabel = new FieldLabel(wait,
            EditorMessage.Util.MESSAGE.event_wait());
        container.add(waitLabel, new VerticalLayoutData(-1, -1, new Margins(10,
            10, 0, 10)));

        waitText = new PropertyValueField();
        waitTextLabel = new FieldLabel(waitText,
            EditorMessage.Util.MESSAGE.event_wait_text());
        container.add(waitTextLabel, new VerticalLayoutData(1, -1, new Margins(
            10, 10, 0, 10)));

        schema = new TextField();
        schemaLabel = new FieldLabel(schema,
            EditorMessage.Util.MESSAGE.event_schema());
        container.add(schemaLabel, new VerticalLayoutData(1, -1, new Margins(
            10, 10, 0, 10)));

        function = new TextField();
        functionLabel = new FieldLabel(function,
            EditorMessage.Util.MESSAGE.event_function());
        container.add(functionLabel, new VerticalLayoutData(1, -1, new Margins(
            10, 10, 0, 10)));

        component = new ComponentComboBox(new ComponentStore(getPresenter()
            .getEventBus(), false));
        component.setTriggerAction(TriggerAction.ALL);
        componentLabel = new FieldLabel(component,
            EditorMessage.Util.MESSAGE.event_component());
        container.add(componentLabel, new VerticalLayoutData(1, -1,
            new Margins(10, 10, 0, 10)));

        targetComponent = new ComponentComboBox(new ComponentStore(
            getPresenter().getEventBus(), true));
        targetComponent.setTriggerAction(TriggerAction.ALL);
        targetComponentLabel = new FieldLabel(targetComponent,
            EditorMessage.Util.MESSAGE.event_contaner());
        container.add(targetComponentLabel, new VerticalLayoutData(1, -1,
            new Margins(10, 10, 0, 10)));

        createNew = new CheckBox();
        createNew.setBoxLabel("");
        createNewLabel = new FieldLabel(createNew,
            EditorMessage.Util.MESSAGE.event_create_new());
        container.add(createNewLabel, new VerticalLayoutData(-1, -1,
            new Margins(10, 10, 0, 10)));

        dataSource = new DataSourceComboBox(new DataSourceStore(getPresenter()
            .getEventBus()));
        dataSource.setTriggerAction(TriggerAction.ALL);
        dataSourceLabel = new FieldLabel(dataSource,
            EditorMessage.Util.MESSAGE.event_datasource());
        container.add(dataSourceLabel, new VerticalLayoutData(1, -1,
            new Margins(10, 10, 0, 10)));

        source = new CodeMirrorPanel();
        sourceLabel = new FieldLabel(source,
            EditorMessage.Util.MESSAGE.event_source());
        container.add(sourceLabel, new VerticalLayoutData(1, 400, new Margins(
            10, 10, 0, 10)));
    }

    @Override
    public EventPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void setPresenter(EventPresenter presenter) {
        this.presenter = presenter;
    }

    private void hideAll() {
        schema.setVisible(false);
        schemaLabel.setVisible(false);
        function.setVisible(false);
        functionLabel.setVisible(false);
        source.setVisible(false);
        sourceLabel.setVisible(false);
        component.setVisible(false);
        componentLabel.setVisible(false);
        targetComponent.setVisible(false);
        targetComponentLabel.setVisible(false);
        dataSourceLabel.setVisible(false);
        createNewLabel.setVisible(false);
    }

    private void changeVisibility(EventType type) {
        hideAll();

        switch (type) {
            case DatabaseFunction:
                schema.setVisible(true);
                schemaLabel.setVisible(true);
                function.setVisible(true);
                functionLabel.setVisible(true);
                dataSourceLabel.setVisible(true);

                source.setVisible(true);
                source.setValue("-- database function");
                source.showEditor(Config.forSql());
                sourceLabel.setVisible(false);
                break;
            case Java:
                source.setVisible(true);
                source.setValue("//class goes here");
                source.showEditor(Config.forJava());
                sourceLabel.setVisible(true);
                break;
            case Component:
                component.setVisible(true);
                componentLabel.setVisible(true);
                targetComponent.setVisible(true);
                targetComponentLabel.setVisible(true);
                createNewLabel.setVisible(true);
                break;
            case JavaScript:
                source.setVisible(true);
                source.setValue("//Available variables: wctx - event context");
                source.showEditor(Config.forJavaScript());
                sourceLabel.setVisible(true);
                break;
            case DatabaseSQL:
                source.setVisible(true);
                source.showEditor(Config.forSql());
                sourceLabel.setVisible(true);
                break;
            default:
                throw new IllegalArgumentException("Unsupported type");
        }
        container.forceLayout();
    }

    @Override
    public String getCode() {
        return code.getValue();
    }

    @Override
    public void setCode(String code) {
        this.code.setValue(code);
    }

    @Override
    public EventType getType() {
        return type.getValue();
    }

    @Override
    public void setType(EventType type) {
        this.type.setValue(type);
        changeVisibility(type);
    }

    @Override
    public String getHandlerType() {
        return handlerType.getValue();
    }

    @Override
    public void setHandlerType(String handlerType) {
        this.handlerType.setValue(handlerType);
    }

    @Override
    public void setNamed(Boolean named) {
        this.named.setValue(named);
    }

    @Override
    public Boolean isNamed() {
        return named.getValue();
    }

    @Override
    public void setConfirm(Boolean confirm) {
        this.confirm.setValue(confirm);
    }

    @Override
    public Boolean isConfirm() {
        return confirm.getValue();
    }

    @Override
    public PropertyValue getConfirmText() {
        return confirmText.getPropertyValue();
    }

    @Override
    public void setConfirmText(PropertyValue text) {
        confirmText.setPropertyValue(text);
    }

    @Override
    public void setWait(Boolean wait) {
        this.wait.setValue(wait);
    }

    @Override
    public Boolean isWait() {
        return wait.getValue();
    }

    @Override
    public PropertyValue getWaitText() {
        return waitText.getPropertyValue();
    }

    @Override
    public void setWaitText(PropertyValue text) {
        waitText.setPropertyValue(text);
    }

    @Override
    public String getSchema() {
        return schema.getValue();
    }

    @Override
    public void setSchema(String schema) {
        this.schema.setValue(schema);
    }

    @Override
    public String getFunction() {
        return function.getValue();
    }

    @Override
    public void setFunction(String function) {
        this.function.setValue(function);
    }

    @Override
    public String getSource() {
        return source.getValue();
    }

    @Override
    public void setSource(String source) {
        if (source != null) {
            this.source.setValue(source);
        }
    }

    @Override
    public ComponentElement getComponent() {
        return component.getValue();
    }

    @Override
    public void setComponent(ComponentElement component) {
        this.component.setValue(component);
    }

    @Override
    public ComponentElement getTargetComponent() {
        return targetComponent.getValue();
    }

    @Override
    public void setTargetComponent(ComponentElement component) {
        targetComponent.setValue(component);
    }

    @Override
    public DataSourceElement getDataSource() {
        return dataSource.getValue();
    }

    @Override
    public void setDataSource(DataSourceElement element) {
        dataSource.setValue(element);
    }

    @Override
    public Boolean isCreateNewComponent() {
        return createNew.getValue();
    }

    @Override
    public void setCreateNewComponent(Boolean value) {
        createNew.setValue(value);
    }

    @Override
    public void clearValues() {
        code.clear();
        type.clear();
        named.clear();
        confirm.clear();
        confirmText.clear();
        wait.clear();
        waitText.clear();
        schema.clear();
        function.clear();
        source.setValue("");
        component.clear();
        targetComponent.clear();
        dataSource.clear();
        createNew.clear();
    }

    @Override
    public void setHeaderText(String text) {
        setHeading(text);
    }

    @Override
    public void setLocales(Collection<LocaleElement> locales,
                           LocaleElement defaultLocale) {
        confirmText.setLocales(defaultLocale, locales);
        waitText.setLocales(defaultLocale, locales);
    }
}
