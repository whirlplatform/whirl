package org.whirlplatform.editor.client.presenter;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.core.client.util.Padding;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.info.Info;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.whirlplatform.component.client.utils.InfoHelper;
import org.whirlplatform.component.client.utils.ProgressHelper;
import org.whirlplatform.editor.client.EditorEventBus;
import org.whirlplatform.editor.client.meta.NewComponentElement;
import org.whirlplatform.editor.client.meta.NewContextMenuItemElement;
import org.whirlplatform.editor.client.meta.NewDataSourceElement;
import org.whirlplatform.editor.client.meta.NewDynamicTableElement;
import org.whirlplatform.editor.client.meta.NewEventElement;
import org.whirlplatform.editor.client.meta.NewEventParameterElement;
import org.whirlplatform.editor.client.meta.NewPropertyElement;
import org.whirlplatform.editor.client.meta.NewSchemaElement;
import org.whirlplatform.editor.client.meta.NewTableElement;
import org.whirlplatform.editor.client.meta.NullFreeComponentElement;
import org.whirlplatform.editor.client.meta.NullRootComponentElement;
import org.whirlplatform.editor.client.util.EditorHelper;
import org.whirlplatform.editor.shared.EditorDataService;
import org.whirlplatform.editor.shared.EditorDataServiceAsync;
import org.whirlplatform.editor.shared.RPCException;
import org.whirlplatform.editor.shared.SaveData;
import org.whirlplatform.editor.shared.SaveResult;
import org.whirlplatform.editor.shared.TreeState;
import org.whirlplatform.editor.shared.i18n.EditorMessage;
import org.whirlplatform.editor.shared.metadata.ApplicationBasicInfo;
import org.whirlplatform.meta.shared.Version;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.data.DataValueImpl;
import org.whirlplatform.meta.shared.editor.AbstractElement;
import org.whirlplatform.meta.shared.editor.ApplicationElement;
import org.whirlplatform.meta.shared.editor.ComponentElement;
import org.whirlplatform.meta.shared.editor.ContextMenuItemElement;
import org.whirlplatform.meta.shared.editor.EventElement;
import org.whirlplatform.meta.shared.editor.EventParameterElement;
import org.whirlplatform.meta.shared.editor.FileElement;
import org.whirlplatform.meta.shared.editor.GroupElement;
import org.whirlplatform.meta.shared.editor.LocaleElement;
import org.whirlplatform.meta.shared.editor.PropertyValue;
import org.whirlplatform.meta.shared.editor.ReportElement;
import org.whirlplatform.meta.shared.editor.RightCollectionElement;
import org.whirlplatform.meta.shared.editor.db.AbstractTableElement;
import org.whirlplatform.meta.shared.editor.db.DataSourceElement;
import org.whirlplatform.meta.shared.editor.db.DatabaseTableElement;
import org.whirlplatform.meta.shared.editor.db.DynamicTableElement;
import org.whirlplatform.meta.shared.editor.db.PlainTableElement;
import org.whirlplatform.meta.shared.editor.db.SchemaElement;
import org.whirlplatform.meta.shared.editor.db.TableColumnElement;
import org.whirlplatform.meta.shared.i18n.AppMessage;
import org.whirlplatform.meta.shared.version.VersionUtil;

@EventHandler
public class ElementEventHandler extends BaseEventHandler<EditorEventBus> {

    private ApplicationElement currentApplication;
    private Version currentVersion;

    public int getCount(String type) {
        int count = 1;
        Collection<ComponentElement> components = currentApplication.getAvailableComponents();
        for (ComponentElement comp : components) {
            if (comp.getType().getType().equalsIgnoreCase(type)) {
                count++;
            }
        }
        return count;
    }

    public void onNewApplication(final ApplicationBasicInfo appInfo) {
        EditorDataService.Util.getDataService()
            .newApplication(appInfo, new AsyncCallback<ApplicationElement>() {
                @Override
                public void onFailure(Throwable caught) {
                    InfoHelper.throwInfo("on-new-application", caught);
                }

                @Override
                public void onSuccess(ApplicationElement result) {
                    eventBus.loadApplication(result, appInfo.getVersion());
                }
            });
    }

    public void onLoadApplication(ApplicationElement application, Version version) {
        this.currentApplication = application;
        this.currentVersion = version;
    }

    /**
     * Добавление подчиненного элемента. Абсолютно все новые элементы должны добавляться через этот
     * метод.
     *
     * @param parent  элемент-родитель
     * @param element добавляемый элемент
     */
    public void onAddElement(final AbstractElement parent, final AbstractElement element) {

        // TODO очень много повторяющегося кода - пересмотреть/переделать
        if (parent instanceof NullRootComponentElement && element instanceof NewComponentElement) {
            // Перетаскивание компонента в корень компонентов
            onAddElementCallback(parent, element, new Callback<ComponentElement, Throwable>() {
                @Override
                public void onFailure(Throwable reason) {
                    InfoHelper.throwInfo("on-add-element", reason);
                }

                @Override
                public void onSuccess(ComponentElement result) {
                    if (currentApplication.getRootComponent() != null) {
                        onAddFreeComponent(currentApplication.getRootComponent());
                    }
                    result.setName(result.getName() + " " + getCount(result.getType().getType()));
                    currentApplication.setRootComponent(result);
                    fireAddElementUi(parent, result);
                }
            });
        } else if (parent instanceof NullFreeComponentElement
            && element instanceof NewComponentElement) {
            // Перетаскивание компонента в корень свободных компонентов
            onAddElementCallback(parent, element, new Callback<ComponentElement, Throwable>() {
                @Override
                public void onFailure(Throwable reason) {
                    InfoHelper.throwInfo("on-add-element", reason);
                }

                @Override
                public void onSuccess(ComponentElement result) {
                    result.setName(result.getName() + " " + getCount(result.getType().getType()));
                    onAddFreeComponent(result);
                    eventBus.addElementUI(new NullFreeComponentElement(), result);
                    fireAddElementUi(parent, result);
                }
            });
        } else if (parent instanceof ComponentElement
            &&
            (element instanceof ComponentElement || element instanceof NewComponentElement)) {
            // подчиненный компонент в уже существующем
            final ComponentElement added;
            LocaleElement l = currentApplication.getDefaultLocale();
            if (element instanceof NewComponentElement) {
                added = EditorHelper.newComponentElement(((NewComponentElement) element).getType(),
                    currentApplication.getDefaultLocale());
            } else {
                added = (ComponentElement) element;
            }
            int count = getCount(added.getType().getType());
            if (!currentApplication.getApplicationComponents().contains(element)) {
                added.setName(added.getName() + " " + count);
            }
            //            PropertyValue propValue = null;
            //            PropertyType propertyType = null;
            //            switch (added.getType()) {
            //                case ButtonType:
            //                case LabelType:
            //                case HtmlType:
            //                    propertyType = PropertyType.Html;
            //                    propValue = added.getProperty(propertyType);
            //                    break;
            //                case TabItemType:
            //                    propertyType = PropertyType.Title;
            //                    propValue = added.getProperty(propertyType);
            //                    break;
            //            }
            //            if (propValue != null) {
            //                String val = null;
            //                DataValue dataValue = propValue.getValue(l);
            //                if (dataValue != null && (val = dataValue.getString()) != null) {
            //                    val = val + " " + count;
            //                }
            //                added.setProperty(propertyType, new PropertyValue(DataType.STRING, l, val));
            //            }
            PropertyValue hAlign = added.getProperty(PropertyType.LayoutDataHorizontalAlign);
            if (hAlign.getValue(l) == null
                || (hAlign.getValue(l).getType() == DataType.STRING
                && hAlign.getValue(l).toString().isEmpty())) {
                added.setProperty(PropertyType.LayoutDataHorizontalAlign,
                    new PropertyValue(DataType.STRING, l, "Center"));
            }
            PropertyValue vAlign = added.getProperty(PropertyType.LayoutDataVerticalAlign);
            if (vAlign.getValue(l) == null
                || vAlign.getValue(l).getType() == DataType.STRING
                && vAlign.getValue(l).toString().isEmpty()) {
                added.setProperty(PropertyType.LayoutDataVerticalAlign,
                    new PropertyValue(DataType.STRING, l, "Middle"));
            }
            onAddChildComponent((ComponentElement) parent, added);
            onSyncServerApplication();
            eventBus.selectTreeElement(added);
            com.google.gwt.core.client.Scheduler.get().scheduleDeferred(new Command() {

                @Override
                public void execute() {
                    eventBus.selectComponentDesignElement(added);

                }
            });
        } else if (parent instanceof NullFreeComponentElement
            && element instanceof ComponentElement) {
            // перенос существующего компонента в свободные
            onAddFreeComponent((ComponentElement) element);
            onSyncServerApplication();
        } else if (parent instanceof NullRootComponentElement
            && element instanceof ComponentElement) {
            // перенос существующего компонента в корневой
            onAddFreeComponent(currentApplication.getRootComponent());
            if (currentApplication.getFreeComponents().contains(element)) {
                currentApplication.removeFreeComponent((ComponentElement) element);
            } else {
                removeFromContainer((ComponentElement) element);
            }
            currentApplication.setRootComponent((ComponentElement) element);
            eventBus.addElementUI(currentApplication, element);
            onSyncServerApplication();
        } else if (parent instanceof ComponentElement && element instanceof NewEventElement) {
            // добавление нового события компоненту
            onAddElementCallback(parent, element, new Callback<AbstractElement, Throwable>() {
                @Override
                public void onFailure(Throwable reason) {
                    InfoHelper.throwInfo("on-add-element", reason);
                }

                @Override
                public void onSuccess(AbstractElement result) {
                    ((ComponentElement) parent).addEvent((EventElement) result);
                    fireAddElementUi(parent, result);
                }
            });
        } else if (parent instanceof ApplicationElement && element instanceof NewEventElement) {
            // добавление нового события компоненту
            onAddElementCallback(parent, element, new Callback<AbstractElement, Throwable>() {
                @Override
                public void onFailure(Throwable reason) {
                    InfoHelper.throwInfo("on-add-element", reason);
                }

                @Override
                public void onSuccess(AbstractElement result) {
                    ((ApplicationElement) parent).addFreeEvent((EventElement) result);
                    fireAddElementUi(parent, result);
                }
            });
        } else if (parent instanceof EventElement && element instanceof NewEventParameterElement) {
            // добавление нового параметра событию
            onAddElementCallback(parent, element, new Callback<AbstractElement, Throwable>() {
                @Override
                public void onFailure(Throwable reason) {
                    InfoHelper.throwInfo("on-add-element", reason);
                }

                @Override
                public void onSuccess(AbstractElement result) {
                    // Устанавливать компонент на серверной части не
                    // получится, т.к.
                    // получается копия компонента со всеми связями ->
                    // размер приложения увеличивается почти вдвое
                    EventParameterElement param = (EventParameterElement) result;
                    if (param.getComponentId() != null) {
                        for (ComponentElement c : currentApplication.getAvailableComponents()) {
                            if (param.getComponentId().equals(c.getId())) {
                                param.setComponent(c);
                                break;
                            }
                        }
                    }
                    ((EventElement) parent).addParameter(param);
                    fireAddElementUi(parent, param);
                }
            });
        } else if (parent instanceof ApplicationElement
            && element instanceof NewDataSourceElement) {
            onAddElementCallback(parent, element, new Callback<AbstractElement, Throwable>() {
                @Override
                public void onFailure(Throwable reason) {
                    InfoHelper.throwInfo("on-add-element", reason);
                }

                @Override
                public void onSuccess(AbstractElement result) {
                    ((ApplicationElement) parent).addDataSource((DataSourceElement) result);
                    fireAddElementUi(parent, result);
                }
            });
        } else if (parent instanceof DataSourceElement && element instanceof NewSchemaElement) {
            // созадем новую схему
            onAddElementCallback(parent, element, new Callback<AbstractElement, Throwable>() {
                @Override
                public void onFailure(Throwable reason) {
                    InfoHelper.throwInfo("on-add-element", reason);
                }

                @Override
                public void onSuccess(AbstractElement result) {
                    ((DataSourceElement) parent).addSchema((SchemaElement) result);
                    fireAddElementUi(parent, result);
                }
            });
        } else if (parent instanceof SchemaElement && element instanceof NewTableElement) {
            // содаем таблицу
            onAddElementCallback(parent, element, new Callback<AbstractElement, Throwable>() {
                @Override
                public void onFailure(Throwable reason) {
                    InfoHelper.throwInfo("on-add-element", reason);
                }

                @Override
                public void onSuccess(AbstractElement result) {
                    ((SchemaElement) parent).addTable((PlainTableElement) result);
                    fireAddElementUi(parent, result);
                }
            });
        } else if (parent instanceof SchemaElement && element instanceof NewDynamicTableElement) {
            // содаем динамическую таблицу
            onAddElementCallback(parent, element, new Callback<AbstractElement, Throwable>() {
                @Override
                public void onFailure(Throwable reason) {
                    InfoHelper.throwInfo("on-add-element", reason);
                }

                @Override
                public void onSuccess(AbstractElement result) {
                    ((SchemaElement) parent).addTable((DynamicTableElement) result);
                    fireAddElementUi(parent, result);
                }
            });
        } else if (parent instanceof ComponentElement && element instanceof NewPropertyElement) {
            ComponentElement component = (ComponentElement) parent;
            NewPropertyElement prop = (NewPropertyElement) element;
            LocaleElement l = prop.getLocale() == null ? currentApplication.getDefaultLocale() :
                prop.getLocale();
            if (prop.getValue() == null) {
                component.removeProperty(prop.getType());
            } else {
                PropertyValue property;
                if (component.hasProperty(prop.getType())) {
                    property = component.getProperty(prop.getType());
                } else {
                    property = new PropertyValue(prop.getType().getType(), l);
                }
                boolean replaceable = prop.isReplaceable();
                property.setReplaceable(replaceable);
                DataValueImpl v;
                if (replaceable) {
                    v = new DataValueImpl(DataType.STRING);
                } else {
                    v = new DataValueImpl(prop.getType().getType());
                }
                v.setValue(prop.getValue());
                property.setValue(l, v);

                if (PropertyType.Rows.equals(prop.getType())) {
                    eventBus.syncRowsNumber(property.getValue(l).getDouble().intValue());
                } else if (PropertyType.Columns.equals(prop.getType())) {
                    eventBus.syncColumnsNumber(property.getValue(l).getInteger());
                }
                component.setProperty(prop.getType(), property);

            }
        } else if (parent instanceof ReportElement && element instanceof NewComponentElement
            &&
            ComponentType.FormBuilderType.equals(((NewComponentElement) element).getType())) {
            onAddElementCallback(parent, element, new Callback<ComponentElement, Throwable>() {
                @Override
                public void onFailure(Throwable reason) {
                    InfoHelper.throwInfo("on-add-element", reason);
                }

                @Override
                public void onSuccess(ComponentElement result) {
                    onAddChildComponent((ComponentElement) parent, result);
                    onSyncServerApplication();
                }
            });
        } else if (parent instanceof SchemaElement && element instanceof DatabaseTableElement) {
            SchemaElement schema = (SchemaElement) parent;
            schema.addTable((DatabaseTableElement) element);
            fireAddElementUi(parent, element);
            onSyncServerApplication();
        } else if (parent instanceof EventElement && element instanceof NewEventElement) {
            // Добавление подчиненного события
            onAddElementCallback(parent, element, new Callback<AbstractElement, Throwable>() {
                @Override
                public void onFailure(Throwable reason) {
                    InfoHelper.throwInfo("on-add-element", reason);
                }

                @Override
                public void onSuccess(AbstractElement result) {
                    ((EventElement) parent).addSubEvent((EventElement) result);
                    fireAddElementUi(parent, result);
                }
            });
        } else if (parent instanceof PlainTableElement && element instanceof NewTableElement) {
            // содаем клон таблицы
            onAddElementCallback(parent, element, new Callback<AbstractElement, Throwable>() {
                @Override
                public void onFailure(Throwable reason) {
                    InfoHelper.throwInfo("on-add-element", reason);
                }

                @Override
                public void onSuccess(AbstractElement result) {
                    ((PlainTableElement) parent).addClone((PlainTableElement) result);
                    fireAddElementUi(parent, result);
                }
            });
        } else if (parent instanceof ApplicationElement && element instanceof ApplicationElement) {
            if (parent.getId() != element.getId()) {
                ApplicationElement existingReference = null;
                for (ApplicationElement ref : ((ApplicationElement) parent).getReferences()) {
                    if (ref.getId() == element.getId()) {
                        existingReference = ref;
                        break;
                    }
                }
                if (existingReference == null) {
                    ApplicationElement parentApp = (ApplicationElement) parent;
                    parentApp.addReference((ApplicationElement) element);
                    // fireAddElementUi(parent, element);
                    eventBus.addElementUI(parentApp, element);
                    onSyncServerApplication();
                    eventBus.selectTreeElement(element);
                }
            } else {
                InfoHelper.info("on-add-element", EditorMessage.Util.MESSAGE.warn(),
                    EditorMessage.Util.MESSAGE.error_application_is_main());
            }
        } else if (parent instanceof ComponentElement && element instanceof EventElement) {
            ComponentElement comp = (ComponentElement) parent;
            EventElement event = (EventElement) element;
            event.removeFromParent();
            comp.addEvent(event);
            fireAddElementUi(comp, event);
        } else if (parent instanceof EventElement && element instanceof EventElement) {
            EventElement event = (EventElement) element;
            EventElement parentEvent = (EventElement) parent;
            event.removeFromParent();
            parentEvent.addSubEvent(event);
            fireAddElementUi(parentEvent, event);
        } else if (parent instanceof ApplicationElement && element instanceof EventElement) {
            ApplicationElement app = (ApplicationElement) parent;
            EventElement event = (EventElement) element;
            event.removeFromParent();
            app.addFreeEvent(event);
            fireAddElementUi(app, event);
        } else if (parent instanceof ComponentElement
            && element instanceof NewContextMenuItemElement) {
            onAddElementCallback(parent, element,
                new Callback<ContextMenuItemElement, Throwable>() {
                    @Override
                    public void onFailure(Throwable reason) {
                        InfoHelper.throwInfo("on-add-element", reason);
                    }

                    @Override
                    public void onSuccess(ContextMenuItemElement result) {
                        ((ComponentElement) parent).addContextMenuItem(result);
                        fireAddElementUi(parent, result);
                    }
                });
        } else if (parent instanceof ComponentElement
            && element instanceof ContextMenuItemElement) {
            ComponentElement comp = (ComponentElement) parent;
            ContextMenuItemElement item = (ContextMenuItemElement) element;
            comp.removeContextMenuItem(item);
            comp.addContextMenuItem(item);
            fireAddElementUi(comp, item);
        } else if (parent instanceof ContextMenuItemElement && element instanceof NewEventElement) {
            onAddElementCallback(parent, element, new Callback<EventElement, Throwable>() {
                @Override
                public void onFailure(Throwable reason) {
                    InfoHelper.throwInfo("on-add-element", reason);
                }

                @Override
                public void onSuccess(EventElement result) {
                    ((ContextMenuItemElement) parent).addEvent(result);
                    fireAddElementUi(parent, result);
                }
            });
        } else if (parent instanceof ContextMenuItemElement && element instanceof EventElement) {
            ContextMenuItemElement comp = (ContextMenuItemElement) parent;
            EventElement event = (EventElement) element;
            event.removeFromParent();
            comp.addEvent(event);
            fireAddElementUi(comp, event);
        } else if (parent instanceof ContextMenuItemElement
            && element instanceof NewContextMenuItemElement) {
            onAddElementCallback(parent, element,
                new Callback<ContextMenuItemElement, Throwable>() {
                    @Override
                    public void onFailure(Throwable reason) {
                        InfoHelper.throwInfo("on-add-element", reason);
                    }

                    @Override
                    public void onSuccess(ContextMenuItemElement result) {
                        ((ContextMenuItemElement) parent).addChild(result);
                        fireAddElementUi(parent, result);
                    }
                });
        } else if (parent instanceof ContextMenuItemElement
            && element instanceof ContextMenuItemElement) {
            ContextMenuItemElement item = (ContextMenuItemElement) parent;
            ContextMenuItemElement childItem = (ContextMenuItemElement) element;
            item.removeChild(childItem);
            item.addChild(childItem);
            fireAddElementUi(item, childItem);
        }
    }

    private void fireAddElementUi(AbstractElement parent, AbstractElement addedElement) {
        eventBus.addElementUI(parent, addedElement);
        eventBus.selectTreeElement(addedElement);
        if (addedElement instanceof ComponentElement
            || (addedElement instanceof EventParameterElement
            && ((EventParameterElement) addedElement).getComponentId() != null)) {
            // skipped
        } else {
            eventBus.openElement(addedElement);
        }
        onSyncServerApplication();
    }

    /**
     * Добавление подчиненного элемента. Абсолютно все новые элементы должны добавляться через этот
     * метод.
     *
     * @param parent   элемент-родитель
     * @param element  добавляемый элемент
     * @param callback результат добавления/создания элемента
     */
    public <T extends AbstractElement> void onAddElementCallback(AbstractElement parent,
                                                                 AbstractElement element,
                                                                 final Callback<T, Throwable> callback) {
        EditorDataService.Util.getDataService().newElement(parent, element, new AsyncCallback<T>() {
            @Override
            public void onSuccess(T result) {
                callback.onSuccess(result);
            }

            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }
        });
    }

    /**
     * Удаляет из родителя компонент. Абсолютно все процедуры удаления любых компонентов должны
     * происходить через этот метод.
     *
     * @param parent  компонент-родитель
     * @param element удаляемый компонент
     */
    public void onRemoveElement(final AbstractElement parent, final AbstractElement element,
                                boolean showDialog) {
        if (!showDialog) {
            removeElement(parent, element);
            return;
        }
        final Dialog dialog = new Dialog();
        dialog.setHeading(EditorMessage.Util.MESSAGE.warn_element_deletion());
        HTML text = new HTML(EditorMessage.Util.MESSAGE.warn_is_delete(element.getName()));
        text.setLayoutData(new MarginData(new Margins(5)));
        dialog.setWidget(text);
        dialog.setPredefinedButtons(PredefinedButton.YES, PredefinedButton.NO);
        dialog.setLayoutData(new Padding(20));
        dialog.setModal(true);
        dialog.setResizable(false);
        TextButton yesButton = dialog.getButton(PredefinedButton.YES);
        yesButton.addSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                dialog.hide();
                removeElement(parent, element);
            }
        });
        TextButton noButton = dialog.getButton(PredefinedButton.NO);
        noButton.addSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                dialog.hide();
            }
        });
        dialog.show();
    }

    private void removeElement(AbstractElement parent, AbstractElement element) {
        AbstractElement removedElement = null;
        if (parent instanceof NullRootComponentElement && element instanceof ComponentElement
            && currentApplication.getRootComponent() == element) {
            // удаляем корневой компонент
            currentApplication.setRootComponent(null);
            onAddFreeComponent((ComponentElement) element);
            removedElement = element;
        } else if (parent instanceof NullFreeComponentElement && element instanceof ComponentElement
            && currentApplication.getFreeComponents().contains(element)) {
            // удаляем свободный компонент
            currentApplication.removeFreeComponent((ComponentElement) element);
            removedElement = element;
        } else if (parent instanceof ComponentElement && element instanceof ComponentElement
            && ((ComponentElement) parent).getChildren().contains(element)) {
            // удаляем подчиненный компонент
            // TODO пересчитываем индексы
            onRemoveChildComponent((ComponentElement) element);
            ((ComponentElement) element).removeFromParent();
            removedElement = element;
        } else if (parent instanceof EventElement && element instanceof EventElement
            && ((EventElement) parent).getSubEvents().contains(element)) {
            // удаляем подчиненное событие
            ((EventElement) parent).removeSubEvent((EventElement) element);
            removedElement = element;
        } else if (parent instanceof EventElement && element instanceof EventParameterElement
            && ((EventElement) parent).getParameters().contains(element)) {
            // удаляем параметр события
            ((EventElement) parent).removeParameter((EventParameterElement) element);
            removedElement = element;
        } else if (parent instanceof ComponentElement && element instanceof EventElement
            && ((ComponentElement) parent).getEvents().contains(element)) {
            // удаляем событие
            ((ComponentElement) parent).removeEvent((EventElement) element);
            removedElement = element;
        } else if (parent instanceof ApplicationElement && element instanceof EventElement
            && ((ApplicationElement) parent).getFreeEvents().contains(element)) {
            // удаляем событие приложения
            ((ApplicationElement) parent).removeFreeEvent((EventElement) element);
            removedElement = element;
        } else if (parent instanceof ApplicationElement && element instanceof DataSourceElement
            && ((ApplicationElement) parent).getDataSources().contains(element)) {
            // удаляем источник данных
            ((ApplicationElement) parent).removeDataSource((DataSourceElement) element);
            removedElement = element;
        } else if (parent instanceof DataSourceElement && element instanceof SchemaElement) {
            ((DataSourceElement) parent).removeSchema((SchemaElement) element);
            removedElement = element;
        } else if (parent instanceof SchemaElement && element instanceof DatabaseTableElement) {
            ((SchemaElement) parent).removeTable((DatabaseTableElement) element);
            removedElement = element;
        } else if (parent instanceof PlainTableElement && element instanceof PlainTableElement) {
            ((PlainTableElement) parent).removeClone((PlainTableElement) element);
            removedElement = element;
        } else if (parent instanceof ApplicationElement && element instanceof ApplicationElement) {
            ((ApplicationElement) parent).removeReference((ApplicationElement) element);
            removedElement = element;
        } else if (parent instanceof ComponentElement
            && element instanceof ContextMenuItemElement) {
            ((ComponentElement) parent).removeContextMenuItem((ContextMenuItemElement) element);
        } else if (parent instanceof ContextMenuItemElement
            && element instanceof ContextMenuItemElement) {
            ((ContextMenuItemElement) parent).removeChild((ContextMenuItemElement) element);
        }

        if (removedElement != null) {
            eventBus.removeElementUI(parent, removedElement);
        }
        onSyncServerApplication();
    }

    // компоненты

    private void onRemoveChildComponent(ComponentElement child) {
        ComponentElement parent = child.getParent();
        if (parent.getType() == ComponentType.HorizontalContainerType
            || parent.getType() == ComponentType.VerticalContainerType
            || parent.getType() == ComponentType.HBoxContainerType
            || parent.getType() == ComponentType.VBoxContainerType
            || parent.getType() == ComponentType.TreeMenuType
            || parent.getType() == ComponentType.HorizontalMenuType
            || parent.getType() == ComponentType.HorizontalMenuItemType
            || parent.getType() == ComponentType.TabPanelType) {

            Double tmpIndex = child.getProperty(PropertyType.LayoutDataIndex)
                .getValue(currentApplication.getDefaultLocale()).getDouble();

            // если индекс нулевой, то просто выходим
            if (tmpIndex == null || tmpIndex < 0) {
                return;
            }
            int childIndex = tmpIndex.intValue();
            List<ComponentElement> items = new ArrayList<ComponentElement>(parent.getChildren());

            for (int i = childIndex; i < items.size() - 1; i++) {
                eventBus.addElement(items.get(i + 1),
                    new NewPropertyElement(PropertyType.LayoutDataIndex,
                        currentApplication.getDefaultLocale(), i));
            }
        }
    }

    /**
     * Добавляет свободный компонент приложения.
     *
     * @param component
     */
    private void onAddFreeComponent(ComponentElement component) {
        if (component == currentApplication.getRootComponent()) {
            currentApplication.setRootComponent(null);
        } else {
            removeFromContainer(component);
        }
        if (component != null) {
            currentApplication.addFreeComponent(component);
            eventBus.addElementUI(new NullFreeComponentElement(), component);
        }
    }

    /**
     * Добавляет подчиненных компонент.
     *
     * @param parent
     * @param child
     */
    private void onAddChildComponent(ComponentElement parent, ComponentElement child) {
        if (parent.getType().isContainer() || ComponentType.ReportType.equals(parent.getType())) {

            if (parent.getType() == ComponentType.BorderContainerType) {
                Iterator<ComponentElement> iter = parent.getChildren().iterator();
                while (iter.hasNext()) {
                    ComponentElement e = iter.next();
                    String location = e.getProperty(PropertyType.LayoutDataLocation)
                        .getValue(currentApplication.getDefaultLocale()).getString();
                    if (location == null
                        || location.equals(child.getProperty(PropertyType.LayoutDataLocation)
                        .getValue(currentApplication.getDefaultLocale()).getString())) {

                        eventBus.removeElementUI(parent, e);
                        parent.removeChild(e);
                        onAddFreeComponent(e);
                    }
                }
            } else if (parent.getType() == ComponentType.HorizontalContainerType
                || parent.getType() == ComponentType.VerticalContainerType
                || parent.getType() == ComponentType.HBoxContainerType
                || parent.getType() == ComponentType.VBoxContainerType
                || parent.getType() == ComponentType.TreeMenuType
                || parent.getType() == ComponentType.HorizontalMenuType
                || parent.getType() == ComponentType.HorizontalMenuItemType
                || parent.getType() == ComponentType.TabPanelType) {
                Iterator<ComponentElement> iter = parent.getChildren().iterator();

                Double tmpIndex = child.getProperty(PropertyType.LayoutDataIndex)
                    .getValue(currentApplication.getDefaultLocale()).getDouble();

                int childIndex;
                if (tmpIndex == null) {
                    childIndex = parent.getChildren().size();
                    eventBus.addElement(child, new NewPropertyElement(PropertyType.LayoutDataIndex,
                        currentApplication.getDefaultLocale(), childIndex));
                } else {
                    childIndex = tmpIndex.intValue();
                }

                if (childIndex < 0) {
                    childIndex = 0;
                }
                int index = 0;
                while (iter.hasNext()) {
                    ComponentElement e = iter.next();
                    if (e.equals(child)) {
                        continue;
                    }
                    if (index == childIndex) {
                        index++;
                    }
                    eventBus.addElement(e, new NewPropertyElement(PropertyType.LayoutDataIndex,
                        currentApplication.getDefaultLocale(), index));
                    index++;
                }
                if (index < childIndex) {
                    eventBus.addElement(child, new NewPropertyElement(PropertyType.LayoutDataIndex,
                        currentApplication.getDefaultLocale(), index));
                }
            } else if (parent.getType() == ComponentType.FormBuilderType) {
                Iterator<ComponentElement> iter = parent.getChildren().iterator();
                long childRow = Math.round(child.getProperty(PropertyType.LayoutDataFormRow)
                    .getValue(currentApplication.getDefaultLocale()).getDouble());
                long childCol = Math.round(child.getProperty(PropertyType.LayoutDataFormColumn)
                    .getValue(currentApplication.getDefaultLocale()).getDouble());
                while (iter.hasNext()) {
                    ComponentElement e = iter.next();
                    boolean rowEq =
                        childRow == Math.round(e.getProperty(PropertyType.LayoutDataFormRow)
                            .getValue(currentApplication.getDefaultLocale()).getDouble());
                    boolean colEq =
                        childCol == Math.round(e.getProperty(PropertyType.LayoutDataFormColumn)
                            .getValue(currentApplication.getDefaultLocale()).getDouble());
                    if (rowEq && colEq) {

                        eventBus.removeElement(parent, e, false);
                        parent.removeChild(e);
                        onAddFreeComponent(e);
                    }
                }
            } else if (parent.getType() == ComponentType.HorizontalMenuType
                || parent.getType() == ComponentType.TreeMenuType
                || parent.getType() == ComponentType.HorizontalMenuItemType) {
                // Перенести установку LayoutData в одно место!
            } else {
                Iterator<ComponentElement> iter = parent.getChildren().iterator();
                // Нужен цикл? По идее дочерний элемент может быть только один
                while (iter.hasNext()) {
                    ComponentElement e = iter.next();

                    eventBus.removeElementUI(parent, e);
                    parent.removeChild(e);
                    onAddFreeComponent(e);
                    // eventBus.addElementUI(new NullFreeComponentElement(), e);
                }
            }
            if (currentApplication.getFreeComponents().contains(child)) {
                currentApplication.removeFreeComponent(child);
            }
            if (currentApplication.getRootComponent() == child) {
                currentApplication.setRootComponent(null);
            }

            parent.addChild(child);
            eventBus.addElementUI(parent, child);
        }
    }

    // компоненты
    public void onSaveApplication() {
        ProgressHelper.show(EditorMessage.Util.MESSAGE.action_save_application());
        eventBus.getApplicationState(new Callback<TreeState, Throwable>() {
            @Override
            public void onSuccess(TreeState result) {
                SaveData saveData = new SaveData();
                saveData.setApplication(currentApplication);
                saveData.setState(result);
                saveData.setVersion(currentVersion);
                saveApplication(saveData);
            }

            @Override
            public void onFailure(Throwable reason) {
                ProgressHelper.hide();
            }
        });
    }

    public void onSaveApplicationAs(Version version) {
        if (version == null) {
            return;
        }
        ProgressHelper.show(EditorMessage.Util.MESSAGE.action_save_application());
        final Version newVersion = version;
        final Version oldVersion = currentVersion;
        eventBus.getApplicationState(new Callback<TreeState, Throwable>() {
            @Override
            public void onSuccess(TreeState result) {
                SaveData saveData = new SaveData();
                saveData.setApplication(currentApplication);
                saveData.setState(result);
                saveData.setVersion(newVersion);
                saveApplicationAs(saveData, oldVersion);
            }

            @Override
            public void onFailure(Throwable reason) {
                ProgressHelper.hide();
            }
        });
    }

    private void saveApplication(SaveData saveData) {
        EditorDataService.Util.getDataService()
            .saveApplication(saveData, new AsyncCallback<SaveResult>() {
                @Override
                public void onSuccess(SaveResult result) {
                    // Или в цикле перебрать все компоненты и поставить им
                    // новый id?
                    currentApplication = result.getApplication();
                    currentVersion = result.getVersion();
                    eventBus.loadApplication(currentApplication, currentVersion);
                    eventBus.restoreApplicationState(result.getState());
                    ProgressHelper.hide();
                    Info.display(AppMessage.Util.MESSAGE.success(),
                        EditorMessage.Util.MESSAGE.info_application_saved());
                }

                @Override
                public void onFailure(Throwable caught) {
                    ProgressHelper.hide();
                    if (caught instanceof RPCException
                        && ((RPCException) caught).isSessionExpired()) {
                        eventBus.showLoginPanel();
                        InfoHelper.error("save-application", EditorMessage.Util.MESSAGE.error(),
                            caught.getMessage());
                    } else {
                        InfoHelper.throwInfo("save-application", caught);
                    }
                }
            });
    }

    private void saveApplicationAs(SaveData saveData, Version oldVersion) {
        EditorDataService.Util.getDataService().saveApplicationAs(saveData, oldVersion,
            new AsyncCallback<SaveResult>() {
                @Override
                public void onSuccess(SaveResult result) {
                    currentApplication = result.getApplication();
                    currentVersion = result.getVersion();
                    eventBus.loadApplication(currentApplication, currentVersion);
                    eventBus.restoreApplicationState(result.getState());
                    ProgressHelper.hide();
                    Info.display(AppMessage.Util.MESSAGE.success(),
                        EditorMessage.Util.MESSAGE.info_application_saved());
                }

                @Override
                public void onFailure(Throwable caught) {
                    ProgressHelper.hide();
                    if (caught instanceof RPCException
                        && ((RPCException) caught).isSessionExpired()) {
                        eventBus.showLoginPanel();
                        InfoHelper.error("save-application-as",
                            EditorMessage.Util.MESSAGE.error(),
                            caught.getMessage());
                    } else {
                        InfoHelper.throwInfo("save-application-as", caught);
                    }
                }
            });
    }

    // Удаление из контейнера и перестроение индексов компонентов в
    // горизонтальном/вертикальном контейнере
    private void removeFromContainer(ComponentElement component) {
        ComponentElement parent = component.getParent();
        if (parent != null) {
            component.removeFromParent();

            if (parent.getType() == ComponentType.HorizontalContainerType
                || parent.getType() == ComponentType.VerticalContainerType) {
                int index = component.getProperty(PropertyType.LayoutDataIndex)
                    .getValue(currentApplication.getDefaultLocale()).getInteger();
                for (ComponentElement child : component.getParent().getChildren()) {
                    int childIndex = child.getProperty(PropertyType.LayoutDataIndex)
                        .getValue(currentApplication.getDefaultLocale()).getInteger();
                    if (childIndex > index) {
                        eventBus.addElement(child,
                            new NewPropertyElement(PropertyType.LayoutDataIndex,
                                currentApplication.getDefaultLocale(), --childIndex));
                    }
                }
            }
        }
    }

    public void onGetAvailableComponents(
        Callback<Collection<ComponentElement>, Throwable> callback) {
        callback.onSuccess(currentApplication.getAvailableComponents());
    }

    public void onGetAvailableTables(
        Callback<Collection<AbstractTableElement>, Throwable> callback) {
        callback.onSuccess(currentApplication.getAvailableTables());
    }

    public void onGetAvailableColumns(PlainTableElement table,
                                      Callback<Collection<TableColumnElement>, Throwable> callback) {
        callback.onSuccess(table.getColumns());
    }

    public void onGetAvailableGroups(Callback<Collection<GroupElement>, Throwable> callback) {
        callback.onSuccess(currentApplication.getGroups());
    }

    public <T extends AbstractElement> void onGetElementRights(
        Collection<T> elements,
        Callback<Collection<RightCollectionElement>,
            Throwable> callback) {
        Set<RightCollectionElement> result = new HashSet<RightCollectionElement>();
        for (T e : elements) {
            if (e instanceof AbstractTableElement) {
                result.add(currentApplication.getTableRights((AbstractTableElement) e));
                // if (result.isEmpty()) {
                // for (ApplicationElement ref :
                // currentApplication.getReferences()) {
                // result.add(ref.getTableRights((TableElement) e));
                // }
                // }
            } else if (e instanceof TableColumnElement) {
                TableColumnElement c = (TableColumnElement) e;
                result.add(currentApplication.getTableColumnRights(c));
                // if (result.isEmpty()) {
                // for (ApplicationElement ref :
                // currentApplication.getReferences()) {
                // result.add(ref.getTableColumnRights(c.getTable(), c));
                // }
                // }
            } else if (e instanceof EventElement) {
                result.add(currentApplication.getEventRights((EventElement) e));
            }
        }
        callback.onSuccess(result);
    }

    public void onSetElementRights(AbstractElement element, RightCollectionElement rights) {
        if (element instanceof AbstractTableElement) {
            currentApplication.setTableRightCollection((AbstractTableElement) element, rights);
        } else if (element instanceof TableColumnElement) {
            TableColumnElement column = (TableColumnElement) element;
            currentApplication.setTableColumnRightCollection(column, rights);
        } else if (element instanceof EventElement) {
            currentApplication.setEventRightCollection((EventElement) element, rights);
        }
    }

    public void onSaveApplicationAsXML() {
        if (currentApplication == null) {
            return;
        }
        EditorDataServiceAsync service = EditorDataService.Util.getDataService();
        service.syncServerApplication(currentApplication, currentVersion,
            new AsyncCallback<SaveResult>() {

                @Override
                public void onFailure(Throwable caught) {
                    InfoHelper.throwInfo("save-application-as-xml", caught);
                }

                @Override
                public void onSuccess(SaveResult result) {
                    Window.open(GWT.getHostPageBaseURL() + "export", "_blank", null);
                }

            });
    }

    public void onLoadApplicationFromXML() {
        EditorDataServiceAsync service = EditorDataService.Util.getDataService();
        service.loadServerApplication(new AsyncCallback<SaveResult>() {

            @Override
            public void onFailure(Throwable caught) {
                InfoHelper.throwInfo("load-server-application", caught);
            }

            @Override
            public void onSuccess(SaveResult result) {
                eventBus.loadApplication(result.getApplication(),
                    VersionUtil.createVersion("xml-import"));
            }
        });
    }

    public void onSyncServerApplication() {
        EditorDataService.Util.getDataService()
            .syncServerApplication(currentApplication, currentVersion,
                new AsyncCallback<SaveResult>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        InfoHelper.throwInfo("sync-server-application", caught);
                    }

                    @Override
                    public void onSuccess(SaveResult result) {
                        syncFiles(currentApplication.getJavaScriptFiles(),
                            result.getApplication().getJavaScriptFiles());
                        syncFiles(currentApplication.getCssFiles(),
                            result.getApplication().getCssFiles());
                        syncFiles(currentApplication.getJavaFiles(),
                            result.getApplication().getJavaFiles());
                        syncFiles(currentApplication.getImageFiles(),
                            result.getApplication().getImageFiles());
                    }
                });
    }

    private void syncFiles(Collection<FileElement> destFiles, Collection<FileElement> srcFiles) {
        for (FileElement dstFile : destFiles) {
            for (FileElement srcFile : srcFiles) {
                if (dstFile.getId().equals(srcFile.getId())) {
                    dstFile.setChecksum(srcFile.getChecksum());
                }
            }
        }
    }

    public void onGetApplication(Callback<ApplicationElement, Throwable> callback) {
        callback.onSuccess(currentApplication);
    }

    public void onGetApplicationVersion(Callback<Version, Throwable> callback) {
        callback.onSuccess(currentVersion);
    }

    public void onGetDataSources(Callback<Collection<DataSourceElement>, Throwable> callback) {
        callback.onSuccess(currentApplication.getDataSources());
    }
}