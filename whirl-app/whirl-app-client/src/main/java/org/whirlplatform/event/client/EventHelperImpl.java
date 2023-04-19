package org.whirlplatform.event.client;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.container.ResizeContainer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import org.whirlplatform.component.client.BuilderManager;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.Containable;
import org.whirlplatform.component.client.ContainerHelper;
import org.whirlplatform.component.client.ListParameter;
import org.whirlplatform.component.client.Parameter;
import org.whirlplatform.component.client.Prepareable;
import org.whirlplatform.component.client.Validatable;
import org.whirlplatform.component.client.event.AttachEvent;
import org.whirlplatform.component.client.event.BlurEvent;
import org.whirlplatform.component.client.event.ChangeEvent;
import org.whirlplatform.component.client.event.ClickEvent;
import org.whirlplatform.component.client.event.CreateEvent;
import org.whirlplatform.component.client.event.DeleteEvent;
import org.whirlplatform.component.client.event.DetachEvent;
import org.whirlplatform.component.client.event.DoubleClickEvent;
import org.whirlplatform.component.client.event.EventCallbackResult;
import org.whirlplatform.component.client.event.EventHelper;
import org.whirlplatform.component.client.event.FocusEvent;
import org.whirlplatform.component.client.event.HideEvent;
import org.whirlplatform.component.client.event.InsertEvent;
import org.whirlplatform.component.client.event.JavaScriptContext;
import org.whirlplatform.component.client.event.KeyPressEvent;
import org.whirlplatform.component.client.event.LoadEvent;
import org.whirlplatform.component.client.event.RefreshEvent;
import org.whirlplatform.component.client.event.RowDoubleClickEvent;
import org.whirlplatform.component.client.event.SelectEvent;
import org.whirlplatform.component.client.event.ShowEvent;
import org.whirlplatform.component.client.event.TimeEvent;
import org.whirlplatform.component.client.event.UpdateEvent;
import org.whirlplatform.component.client.report.ReportBuilder;
import org.whirlplatform.component.client.utils.InfoHelper;
import org.whirlplatform.component.client.utils.ProgressHelper;
import org.whirlplatform.component.client.window.WindowBuilder;
import org.whirlplatform.meta.shared.EventMetadata;
import org.whirlplatform.meta.shared.EventResult;
import org.whirlplatform.meta.shared.EventType;
import org.whirlplatform.meta.shared.JavaScriptEventResult;
import org.whirlplatform.meta.shared.component.ComponentModel;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.data.DataValueImpl;
import org.whirlplatform.meta.shared.data.EventParameter;
import org.whirlplatform.meta.shared.data.ParameterType;
import org.whirlplatform.meta.shared.i18n.AppMessage;
import org.whirlplatform.rpc.client.DataServiceAsync;
import org.whirlplatform.rpc.shared.ListHolder;
import org.whirlplatform.rpc.shared.SessionToken;
import org.whirlplatform.storage.client.StorageHelper;

public class EventHelperImpl implements EventHelper {

    private static final Logger LOGGER = Logger.getLogger(EventHelperImpl.class.getName());

    private final EventMetadata metadata;
    private final Command startHandler = new Command() {
        @Override
        public void execute() {
            if (metadata.isWait()) {
                ProgressHelper.show(metadata.getWaitText());
            }
        }
    };
    private final Command completeHandler = new Command() {
        @Override
        public void execute() {
            ProgressHelper.hide();
        }
    };
    private AsyncCallback<EventCallbackResult> afterEventCallback;

    EventHelperImpl(EventMetadata metadata) {
        this.metadata = metadata;
    }

    @Override
    public void onEvent(Object source) {
        ComponentBuilder builder = null;
        if (source instanceof ComponentBuilder) {
            builder = (ComponentBuilder) source;
        }
        onEvent(builder);
    }

    private void onEvent(final ComponentBuilder source) {
        if (metadata.isConfirm()) {
            String confirmText = metadata.getConfirmText();
            Dialog dialog = new Dialog();
            dialog.setModal(true);
            dialog.setHeading(AppMessage.Util.MESSAGE.confirm());
            dialog.setWidget(new HTML(confirmText));
            dialog.setHideOnButtonClick(true);
            dialog.setPredefinedButtons(PredefinedButton.YES, PredefinedButton.NO);
            dialog.getButton(PredefinedButton.YES)
                .addSelectHandler(event -> scheduleEventExecute(source));
            dialog.show();
        } else {
            scheduleEventExecute(source);
        }
    }

    private void scheduleEventExecute(final ComponentBuilder source) {
        startHandler.execute();
        // final ScheduledCommand eventCmd = new ScheduledCommand() {
        // @Override
        // public void execute() {
        // eventExecute(source);
        // }
        // };
        // Scheduler.get().scheduleDeferred(eventCmd);
        Scheduler.get().scheduleDeferred(() -> eventExecute(source));
    }

    private void eventExecute(final ComponentBuilder source) {
        Callback<List<DataValue>, Throwable> callback = null;
        if (metadata.getType().isServer()) {
            callback = new Callback<List<DataValue>, Throwable>() {

                @Override
                public void onFailure(Throwable reason) {
                    completeHandler.execute();
                }

                @Override
                public void onSuccess(List<DataValue> result) {
                    serverExecute(source, result);
                }
            };

        } else {
            callback = new Callback<List<DataValue>, Throwable>() {

                @Override
                public void onFailure(Throwable reason) {
                    completeHandler.execute();
                }

                @Override
                public void onSuccess(List<DataValue> result) {
                    clientExecute(source, result);
                }
            };
        }
        if (callback != null) {
            prepareValidateGetParameters(metadata.getParametersList(), callback);
        } else {
            completeHandler.execute();
        }
    }

    private void serverExecute(final ComponentBuilder source, final List<DataValue> parameters) {
        DataServiceAsync.Util.getDataService(new AsyncCallback<EventResult>() {

            @Override
            public void onFailure(Throwable caught) {
                InfoHelper.throwInfo(metadata.getId(), caught);
                completeHandler.execute();
            }

            @Override
            public void onSuccess(final EventResult result) {
                onResult(source, result);
            }
        }).executeServer(SessionToken.get(), metadata, new ListHolder<DataValue>(parameters));
    }

    private void onResult(final ComponentBuilder source, final EventResult result) {
        if (result == null) {
            completeHandler.execute();
            return;
        }

        if (result.getMessage() != null && !result.getMessage().isEmpty()) {
            if ("ERROR".equalsIgnoreCase(result.getMessageType())) {
                InfoHelper.error(metadata.getId(), result.getTitle(), result.getMessage());
            } else if ("WARN".equalsIgnoreCase(result.getMessageType())) {
                InfoHelper.warning(metadata.getId(), result.getTitle(), result.getMessage());
            } else {
                InfoHelper.info(metadata.getId(), result.getTitle(), result.getMessage());
            }
        }
        Command nextEventCommand = new Command() {

            @Override
            public void execute() {
                completeHandler.execute();
                EventHelperImpl helper = new EventHelperImpl(result.getNextEvent());
                helper.onEvent(source);
            }
        };

        // Для обратного вызова после завершения цепочки событий
        AsyncCallback<Void> callback = new AsyncCallback<Void>() {

            @Override
            public void onSuccess(Void res) {
                if (afterEventCallback != null) {
                    EventCallbackResult callbackResult = new EventCallbackResult();
                    List<EventParameter> list = new ArrayList<>(result.getParametersMap().values());
                    Map<EventParameter, ComponentBuilder> components =
                        new HashMap<EventParameter, ComponentBuilder>();
                    findComponents(list, components, null, null, null);

                    callbackResult.setParameters(convertParameters(list, components));
                    callbackResult.setRawValue(result.getRawValue());
                    afterEventCallback.onSuccess(callbackResult);
                }
            }

            @Override
            public void onFailure(Throwable caught) {
                if (afterEventCallback != null) {
                    afterEventCallback.onFailure(caught);
                }
            }
        };
        prepairEventResult(result, nextEventCommand, callback);
    }

    public void prepairEventResult(final EventResult eventResult, final Command command,
                                   final AsyncCallback<Void> callback) {
        // TODO афигеть как тут все запутано
        if (!eventResult.isReady() && eventResult.getNextEventCode() != null) {
            // если есть следующее событие и оно еще не подгружено
            DataServiceAsync.Util.getDataService(new AsyncCallback<EventMetadata>() {

                @Override
                public void onFailure(Throwable caught) {
                    callback.onFailure(caught);
                    InfoHelper.throwInfo(metadata.getId(), caught);
                    completeHandler.execute();
                }

                @Override
                public void onSuccess(EventMetadata result) {
                    onNextEventReceived(result, eventResult, command, callback);
                }

            }).getNextEvent(SessionToken.get(), metadata, eventResult.getNextEventCode());
        } else if (eventResult.isReady()) {
            // если есть следующее событие и оно уже подгружено
            onNextEventReceived(eventResult.getNextEvent(), eventResult, command, callback);
        } else {
            if (!eventResult.isReady()) {
                callback.onSuccess(null);
            }
            if (eventResult.isReady()) {
                command.execute();
            } else {
                completeHandler.execute();
            }
        }
    }

    private void onNextEventReceived(EventMetadata nextEvent, EventResult eventResult,
                                     Command command,
                                     AsyncCallback<Void> callback) {
        if (nextEvent == null) {
            if (!eventResult.isReady()) {
                callback.onSuccess(null);
            }
            if (eventResult.isReady()) {
                command.execute();
            } else {
                completeHandler.execute();
            }
            return;
        }
        eventResult.setNextEvent(nextEvent);
        for (Entry<Integer, EventParameter> e : eventResult.getParametersMap().entrySet()) {
            eventResult.getNextEvent().setParameter(e.getKey(), e.getValue());
        }
        if (eventResult.isReady()) {
            command.execute();
        } else {
            completeHandler.execute();
        }
    }

    private void clientExecute(ComponentBuilder source, List<DataValue> parameters) {
        if (metadata.getType() == EventType.Component) {
            componentExecute(source, parameters);
        } else if (metadata.getType() == EventType.JavaScript) {
            String script = metadata.getSource();
            String functionName = "function_" + metadata.getId().replace("-", "");
            script = "window." + functionName + " = function (wctx) {\n" + script + "\n}";
            attachScript(functionName, script);
            onResult(source, javaScriptExecute(functionName, source, parameters));
        }
    }

    private void componentExecute(final ComponentBuilder source, final List<DataValue> parameters) {
        DataServiceAsync.Util.getDataService(new AsyncCallback<ComponentModel>() {

            @Override
            public void onFailure(Throwable caught) {
                InfoHelper.throwInfo(metadata.getId(), caught);
                completeHandler.execute();
            }

            @Override
            public void onSuccess(ComponentModel result) {
                ComponentBuilder c = ContainerHelper.buildComponent(result, null, parameters);
                if (c != null) {
                    componentExecute(source, c, parameters);
                }
                completeHandler.execute();
            }
        }).getComponents(SessionToken.get(), metadata.getComponentId());
    }

    private void componentExecute(ComponentBuilder source, final ComponentBuilder component,
                                  List<DataValue> parameters) {
        if (ComponentType.ReportType == component.getType()
            && !((ReportBuilder) component).isShowReportParams()) {
            return;
        }

        if (!metadata.isCreateNew()) {
            ComponentBuilder old = BuilderManager.findBuilder(component.getId(), false);

            if (old != null) {
                if (old.getType() == ComponentType.WindowType) {
                    ((WindowBuilder) old).hide();
                } else {
                    // else if (old.getParentBuilder().getType() == ComponentType.WindowType) {
                    //      if (WindowManager.get().isMinimized((Window) old.getParentBuilder().getComponent())) {
                    //             WindowManager.get().removeWindow((Window) old.getParentBuilder().getComponent());
                    //      } else {
                    //     ((WindowBuilder) old.getParentBuilder()).hide();
                    //        }
                    //       }
                    old.removeFromParent();
                }
            }
        }

        ComponentBuilder targetComponent =
            BuilderManager.findBuilder(metadata.getTargetComponentId(), false);
        if (targetComponent instanceof Containable) {
            ((Containable) targetComponent).addChild(component);
        } else if (component.getType() == ComponentType.WindowType) {
            // если сам компонент окно, то просто показываем
            ((WindowBuilder) component).show();
            return;
        }

        //
        //            // строим новое окно
        //            WindowBuilder window = new WindowBuilder();
        //            window.setTitle(component.getTitle());
        //            window.setClosable(true);
        //            window.setMinimizable(true);
        //            window.setMaximizable(true);
        //            window.addChild(component);
        //
        //            DataValue width = component.getProperties().get(PropertyType.Width.getCode());
        //            DataValue height = component.getProperties().get(PropertyType.Height.getCode());
        //            if (width == null || width.getDouble() == null) {
        //                window.setWidth(800);
        //            } else {
        //                window.setWidth(width.getDouble().intValue());
        //            }
        //            if (height == null || height.getDouble() == null) {
        //                window.setHeight(500);
        //            } else {
        //                window.setHeight(height.getDouble().intValue());
        //            }
        //            window.show();
        //            window.center();
        //
        //            targetComponent = window;
        //        }
        if (targetComponent == null) {
            LOGGER.warning("Target component not found: " + metadata.getTargetComponentId());
        } else if (targetComponent.getComponent() instanceof ResizeContainer) {
            ((ResizeContainer) targetComponent.getComponent()).forceLayout();
        }
    }

    // Реализация вызова событий
    private JavaScriptEventResult javaScriptExecute(String function, ComponentBuilder source,
                                                    List<DataValue> parameters) {
        try {
            JavaScriptContext context = new JavaScriptContext(source, parameters);
            javaScriptExecute(function, context);
            JavaScriptEventResult result = new JavaScriptEventResult();
            result.setNextEventCode(context.getNextEvent());
            result.setTitle(context.getTitle());
            result.setMessageType(context.getMessageType());
            result.setMessage(context.getMessage());
            return result;
        } catch (Exception e) {
            InfoHelper.throwInfo(metadata.getId(), e);
            completeHandler.execute();
            throw e;
        }
    }

    private native void javaScriptExecute(String func, JavaScriptContext context) /*-{
        return $wnd[func](context);
    }-*/;

    private native boolean checkFunctionExists(String func) /*-{
        return $wnd[func] != undefined;
    }-*/;

    private void attachScript(String functionName, String script) {
        if (!checkFunctionExists(functionName)) {
            ScriptInjector.fromString(script).setWindow(ScriptInjector.TOP_WINDOW)
                .setRemoveTag(false).inject();
        }
    }

    private void findComponents(List<EventParameter> parameters,
                                Map<EventParameter, ComponentBuilder> components,
                                Map<EventParameter, Parameter<?>> componentParameters,
                                Map<EventParameter, Validatable> componentValidatables,
                                Map<EventParameter, Prepareable> componentPreparables) {
        for (EventParameter p : parameters) {
            ComponentBuilder c = null;
            if (p.getType() == ParameterType.COMPONENTCODE) {
                c = BuilderManager.findBuilder(p.getComponentCode(), true);
            } else if (p.getType() == ParameterType.COMPONENT) {
                c = BuilderManager.findBuilder(p.getComponentId(), false);
            }
            if (c != null) {
                initByComponentBuilder(p, c);
                if (components != null) {
                    components.put(p, c);
                }

                if (componentParameters != null && c instanceof Parameter) {
                    componentParameters.put(p, (Parameter<?>) c);
                }
                if (componentValidatables != null && c instanceof Validatable) {
                    componentValidatables.put(p, (Validatable) c);
                }
                if (componentPreparables != null && c instanceof Prepareable) {
                    componentPreparables.put(p, (Prepareable) c);
                }
            }
        }
    }

    private void initByComponentBuilder(final EventParameter parameter,
                                        final ComponentBuilder component) {
        if (component != null) {
            if (parameter.getType() == ParameterType.COMPONENT) {
                parameter.setComponentId(component.getId());
            }
            if (parameter.getType() == ParameterType.COMPONENTCODE) {
                parameter.setComponentCode(component.getCode());
            }
        } else {
            parameter.setComponentId(null);
            parameter.setComponentCode(null);
        }
    }

    private void prepareValidateGetParameters(final List<EventParameter> parameters,
                                              final Callback<List<DataValue>, Throwable> callback) {
        final Map<EventParameter, ComponentBuilder> components = new HashMap<>();
        final Map<EventParameter, Parameter<?>> componentParameters = new HashMap<>();
        final Map<EventParameter, Validatable> componentValidatables = new HashMap<>();
        final Map<EventParameter, Prepareable> componentPreparables = new HashMap<>();
        findComponents(parameters, components, componentParameters, componentValidatables,
            componentPreparables);

        // сначала все проверяем
        boolean valid = true;
        for (Validatable v : componentValidatables.values()) {
            if (!v.isValid(true)) {
                valid = false;
            }
        }
        if (!valid) {
            callback.onFailure(new Throwable("Invalid parameters"));
            return;
        }

        // объявляем собирание параметров
        final Command readyCommand = new Command() {
            @Override
            public void execute() {
                List<DataValue> params = convertParameters(parameters, components);
                callback.onSuccess(params);
            }
        };

        // теперь подготавливаем параметры если есть что готовить
        if (!componentPreparables.isEmpty()) {
            Command prepareCallback = new Command() {
                @Override
                public void execute() {
                    for (Prepareable p : componentPreparables.values()) {
                        if (!p.isReady()) {
                            return;
                        }
                    }
                    readyCommand.execute();
                }
            };

            for (Prepareable p : componentPreparables.values()) {
                p.prepair(prepareCallback);
            }
        } else {
            readyCommand.execute();
        }
    }

    private List<DataValue> convertParameters(Collection<EventParameter> parameters,
                                              Map<EventParameter, ComponentBuilder> components) {
        List<DataValue> result = new ArrayList<DataValue>();
        for (EventParameter v : parameters) {
            DataValue data;
            boolean isCompParam = v.getType() == ParameterType.COMPONENTCODE
                || v.getType() == ParameterType.COMPONENT;
            if (isCompParam /*TODO && components.get(v) != null - определится зачем это условие*/) {
                data = getDataValue(components.get(v));
            } else if (v.getType() == ParameterType.STORAGE) {
                data = StorageHelper.findStorageValue(v.getStorageCode());
            } else {
                data = v.getData();
            }
            // TODO Пересмотреть логику работы сбора параметров
            // StorageHelper.findStorageValue должен возвращать null если
            // компонент не найден
            // getDataValue должен возвращать null если компонент не найден
            // выводить в лог(консоль браузера) информацию о не найденном
            // компоненте и создавать пустой здесь и в других местах где нужно
            // реализовать специфическую логику
            if (data != null) {
                data.setCode(v.getCode());
            }
            result.add(data);
        }
        return result;
    }

    private DataValue getDataValue(ComponentBuilder builder) {
        DataValue result;
        if (builder instanceof ListParameter) {
            result = ((ListParameter<?>) builder).getFieldValue();
        } else if (builder instanceof Parameter) {
            result = ((Parameter<?>) builder).getFieldValue();
        } else {
            result = new DataValueImpl(DataType.STRING);
        }
        if (builder != null) {
            result.setCode(builder.getCode());
        }
        return result;
    }

    @Override
    public void onAttach(AttachEvent event) {
        onEvent(event.getSource());
    }

    @Override
    public void onTime(TimeEvent event) {
        onEvent(event.getSource());
    }

    @Override
    public void onShow(ShowEvent event) {
        onEvent(event.getSource());
    }

    @Override
    public void onSelect(SelectEvent event) {
        onEvent(event.getSource());
    }

    @Override
    public void onLoad(LoadEvent event) {
        onEvent(event.getSource());
    }

    @Override
    public void onUpdate(UpdateEvent event) {
        onEvent(event.getSource());
    }

    @Override
    public void onInsert(InsertEvent event) {
        onEvent(event.getSource());
    }

    @Override
    public void onDoubleClick(DoubleClickEvent event) {
        onEvent(event.getSource());
    }

    @Override
    public void onDetach(DetachEvent event) {
        onEvent(event.getSource());
    }

    @Override
    public void onDelete(DeleteEvent event) {
        onEvent(event.getSource());
    }

    @Override
    public void onCreate(CreateEvent event) {
        onEvent(event.getSource());
    }

    @Override
    public void onClick(ClickEvent event) {
        onEvent(event.getSource());
    }

    @Override
    public void onChange(ChangeEvent event) {
        onEvent(event.getSource());
    }

    @Override
    public void onKeyPress(KeyPressEvent event) {
        onEvent(event.getSource());
    }

    @Override
    public void onBlur(BlurEvent event) {
        onEvent(event.getSource());
    }

    @Override
    public void onFocus(FocusEvent event) {
        onEvent(event.getSource());
    }

    @Override
    public void onHide(HideEvent event) {
        onEvent(event.getSource());
    }

    @Override
    public void setAfterEvent(AsyncCallback<EventCallbackResult> callback) {
        this.afterEventCallback = callback;
    }

    @Override
    public void onRefresh(RefreshEvent event) {
        onEvent(event.getSource());
    }

    @Override
    public void onRowDoubleClick(RowDoubleClickEvent event) {
        onEvent(event.getSource());
    }
}
