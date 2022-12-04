package org.whirlplatform.component.client.grid;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.data.shared.event.StoreUpdateEvent;
import com.sencha.gxt.data.shared.event.StoreUpdateEvent.StoreUpdateHandler;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.whirlplatform.component.client.ParameterHelper;
import org.whirlplatform.component.client.data.ClassStore;
import org.whirlplatform.component.client.event.DeleteEvent;
import org.whirlplatform.component.client.event.InsertEvent;
import org.whirlplatform.component.client.event.LoadEvent;
import org.whirlplatform.component.client.event.UpdateEvent;
import org.whirlplatform.component.client.utils.InfoHelper;
import org.whirlplatform.meta.shared.ClassLoadConfig;
import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.DataModifyConfig;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.data.RowModelData;
import org.whirlplatform.meta.shared.i18n.AppMessage;
import org.whirlplatform.rpc.client.DataServiceAsync;
import org.whirlplatform.rpc.shared.SessionToken;

/**
 * Класс по работе с таблицей. Все изменения данных на таблицах должны производиться через него. Все
 * компоненты редактирующие данные должны делать это через создание стора и использовать этот
 * класс.
 */
//TODO перенести в component/client
public class ClassAction implements HasHandlers, LoadEvent.HasLoadHandlers,
        InsertEvent.HasInsertHandlers, UpdateEvent.HasUpdateHandlers,
        DeleteEvent.HasDeleteHandlers {

    private ClassMetadata metadata;

    private ClassStore<RowModelData, ClassLoadConfig> store;

    private HandlerManager handlerManager;

    private StoreUpdateHandler<RowModelData> storeUpdateHandler;
    private HandlerRegistration updateHandlerRegistration;

    private ParameterHelper paramHelper;

    public ClassAction(ClassMetadata metadata,
                       ClassStore<RowModelData, ClassLoadConfig> store,
                       ParameterHelper paramHelper) {
        assert metadata != null;
        this.metadata = metadata;
        this.store = store;
        this.paramHelper = paramHelper;
        initActionListeners();
    }

    public ClassAction(ClassMetadata metadata) {
        this(metadata, null, null);
    }

    /**
     * Если указан стор, то подвязываем к нему слушателей на insert, update, delete.
     */
    private void initActionListeners() {
        // if (store != null && false) {
        // store.addStoreAddHandler(new StoreAddHandler<RowModelData>() {
        //
        // @Override
        // public void onAdd(StoreAddEvent<RowModelData> event) {
        // for (RowModelData m : event.getItems()) {
        // insert(m, false);
        // }
        // }
        // });
        // store.addStoreRemoveHandler(new StoreRemoveHandler<RowModelData>() {
        //
        // @Override
        // public void onRemove(StoreRemoveEvent<RowModelData> event) {
        // delete(Collections.singletonList(event.getItem()), false);
        // }
        //
        // });
        // store.addStoreUpdateHandler(new StoreUpdateHandler<RowModelData>() {
        //
        // @Override
        // public void onUpdate(StoreUpdateEvent<RowModelData> event) {
        // for (RowModelData m : event.getItems()) {
        // update(m, false);
        // }
        // }
        //
        // });
        // }

        storeUpdateHandler = new StoreUpdateHandler<RowModelData>() {

            @Override
            public void onUpdate(StoreUpdateEvent<RowModelData> event) {
                for (RowModelData m : event.getItems()) {
                    update(m, false);
                }
            }
        };

        // UpdateHandler нужен, т.к. при inline редактировании не будут
        // обновляться данные в БД
        updateHandlerRegistration = store
                .addStoreUpdateHandler(storeUpdateHandler);
    }

    public Store<RowModelData> getStore() {
        return store;
    }

    private void check(RowModelData model) {
        for (String property : model.getPropertyNames()) {
            if (!metadata.hasField(property)) {
                String message = AppMessage.Util.MESSAGE.noTableField(property);
                InfoHelper.display(null, message);
                throw new RuntimeException(message);
            }
        }
    }

    public void insert(final RowModelData model, final boolean notifyStore) {
        if (!metadata.isInsertable()) {
            String message = AppMessage.Util.MESSAGE.notInsertableTable();
            InfoHelper.display(null, message);
            throw new RuntimeException(message);
        }
        check(model);

        AsyncCallback<RowModelData> callback = new AsyncCallback<RowModelData>() {

            @Override
            public void onFailure(Throwable caught) {
                InfoHelper.throwInfo("insert", caught);
            }

            @Override
            public void onSuccess(RowModelData result) {
                model.setId(result.getId());
                if (notifyStore) {
                    insertStore(model);
                }
                InfoHelper.display(null,
                        AppMessage.Util.MESSAGE.successSqlExecuted());
                fireEvent(new InsertEvent());
            }

        };
        DataModifyConfig config = new DataModifyConfig(DataModifyConfig.DataModifyType.INSERT,
                Arrays.asList(model), getParameters());
        DataServiceAsync.Util.getDataService(callback).insert(SessionToken.get(), metadata, config);
    }

    private void insertStore(RowModelData model) {
        if (store != null) {
            store.add(0, model);
        }
    }

    public void update(final RowModelData model, final boolean notifyStore) {
        if (!metadata.isUpdatable()) {
            InfoHelper
                    .display(null, AppMessage.Util.MESSAGE.notEditableTable());
            throw new UnsupportedOperationException(
                    AppMessage.Util.MESSAGE.notEditableTable());
        }
        check(model);

        AsyncCallback<RowModelData> callback = new AsyncCallback<RowModelData>() {

            @Override
            public void onFailure(Throwable caught) {
                InfoHelper.throwInfo("update", caught);
            }

            @Override
            public void onSuccess(RowModelData result) {
                if (notifyStore) {
                    updateStore(model);
                }
                model.setUnchanged();
                InfoHelper.display(null,
                        AppMessage.Util.MESSAGE.successSqlExecuted());
                fireEvent(new UpdateEvent());
            }

        };

        DataModifyConfig config = new DataModifyConfig(DataModifyConfig.DataModifyType.UPDATE,
                Arrays.asList(model), getParameters());
        DataServiceAsync.Util.getDataService(callback).update(SessionToken.get(), metadata,
                config);
    }

    private void updateStore(RowModelData model) {
        if (store != null) {
            // Отключение и затем подключение обработчика
            updateHandlerRegistration.removeHandler();
            store.update(model);
            store.addStoreUpdateHandler(storeUpdateHandler);
        }
    }

    public void delete(final List<RowModelData> models,
                       final boolean notifyStore) {
        if (!metadata.isDeletable()) {
            InfoHelper.display(null,
                    AppMessage.Util.MESSAGE.notDeletableTable());
            throw new UnsupportedOperationException(
                    AppMessage.Util.MESSAGE.notDeletableTable());
        }
        if (models == null) {
            throw new IllegalArgumentException("Models value can not be null");
        }
        if (models.size() == 0) {
            throw new IllegalArgumentException("Models can not be empty");
        }

        AsyncCallback<Void> callback = new AsyncCallback<Void>() {

            @Override
            public void onFailure(Throwable caught) {
                InfoHelper.throwInfo("delete", caught);
            }

            @Override
            public void onSuccess(Void result) {
                if (notifyStore) {
                    deleteStore(models);
                }
                InfoHelper.display(null,
                        AppMessage.Util.MESSAGE.successSqlExecuted());
                fireEvent(new DeleteEvent());
            }

        };

        DataModifyConfig config = new DataModifyConfig(DataModifyConfig.DataModifyType.DELETE,
                models, getParameters());
        DataServiceAsync.Util.getDataService(callback).delete(SessionToken.get(), metadata,
                config);
    }

    private void deleteStore(List<RowModelData> models) {
        if (store != null) {
            for (RowModelData m : models) {
                store.remove(m);
            }
        }
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        ensureHandler().fireEvent(event);
    }

    protected HandlerManager ensureHandler() {
        if (handlerManager == null) {
            handlerManager = new HandlerManager(this);
        }
        return handlerManager;
    }

    @Override
    public HandlerRegistration addDeleteHandler(DeleteEvent.DeleteHandler handler) {
        return ensureHandler().addHandler(DeleteEvent.getType(), handler);
    }

    @Override
    public HandlerRegistration addUpdateHandler(UpdateEvent.UpdateHandler handler) {
        return ensureHandler().addHandler(UpdateEvent.getType(), handler);
    }

    @Override
    public HandlerRegistration addInsertHandler(InsertEvent.InsertHandler handler) {
        return ensureHandler().addHandler(InsertEvent.getType(), handler);
    }

    @Override
    public HandlerRegistration addLoadHandler(LoadEvent.LoadHandler handler) {
        return ensureHandler().addHandler(LoadEvent.getType(), handler);
    }

    private Map<String, DataValue> getParameters() {
        return paramHelper == null ? new HashMap<String, DataValue>()
                : paramHelper.getValues();
    }
}
