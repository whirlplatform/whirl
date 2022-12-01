package org.whirlplatform.component.client.check;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.sencha.gxt.core.client.Style.Orientation;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.dom.HasScrollSupport;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.shared.event.GroupingHandlerRegistration;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.data.shared.event.StoreAddEvent;
import com.sencha.gxt.data.shared.event.StoreClearEvent;
import com.sencha.gxt.data.shared.event.StoreDataChangeEvent;
import com.sencha.gxt.data.shared.event.StoreFilterEvent;
import com.sencha.gxt.data.shared.event.StoreHandlers;
import com.sencha.gxt.data.shared.event.StoreRecordChangeEvent;
import com.sencha.gxt.data.shared.event.StoreRemoveEvent;
import com.sencha.gxt.data.shared.event.StoreSortEvent;
import com.sencha.gxt.data.shared.event.StoreUpdateEvent;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.InsertResizeContainer;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.CheckChangedEvent;
import com.sencha.gxt.widget.core.client.event.CheckChangedEvent.CheckChangedHandler;
import com.sencha.gxt.widget.core.client.event.CheckChangedEvent.HasCheckChangedHandlers;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.whirlplatform.meta.shared.data.ListModelData;
import org.whirlplatform.meta.shared.data.RowModelData;

public class CheckBoxList extends SimpleContainer implements
        HasCheckChangedHandlers<ListModelData> {

    private GroupingHandlerRegistration groupingRegistration;
    private InsertResizeContainer list;
    private LabelProvider<ListModelData> labelProvider;
    private ValueProvider<RowModelData, Boolean> valueProvider;
    private Map<RowModelData, CheckBox> modelCheck = new HashMap<RowModelData, CheckBox>();
    private Map<CheckBox, HandlerRegistration> checkHandler =
            new HashMap<CheckBox, HandlerRegistration>();
    private Store<ListModelData> store;
    private CheckSelectionModel selectionModel = new CheckSelectionModel();
    private StoreHandlers<ListModelData> handlers = new StoreHandlers<ListModelData>() {

        @Override
        public void onAdd(StoreAddEvent<ListModelData> event) {
            onAddItems(event.getItems(), event.getIndex());
        }

        @Override
        public void onRemove(StoreRemoveEvent<ListModelData> event) {
            onRemoveItem(event.getItem());
        }

        @Override
        public void onFilter(StoreFilterEvent<ListModelData> event) {
            // not filtered
        }

        @Override
        public void onClear(StoreClearEvent<ListModelData> event) {
            onClearItems();
        }

        @Override
        public void onUpdate(StoreUpdateEvent<ListModelData> event) {
        }

        @Override
        public void onDataChange(StoreDataChangeEvent<ListModelData> event) {
            onClearItems();
            onAddItems(store.getAll(), 0);
        }

        @Override
        public void onRecordChange(StoreRecordChangeEvent<ListModelData> event) {
        }

        @Override
        public void onSort(StoreSortEvent<ListModelData> event) {
        }

    };

    public CheckBoxList(Orientation orientation,
                        LabelProvider<ListModelData> labelProvider,
                        ValueProvider<RowModelData, Boolean> valueProvider) {
        super();
        this.labelProvider = labelProvider;
        this.valueProvider = valueProvider;
        if (orientation == Orientation.HORIZONTAL) {
            list = new HorizontalLayoutContainer();
        } else {
            list = new VerticalLayoutContainer();
        }
        ((HasScrollSupport) list).getScrollSupport().setScrollMode(
                ScrollMode.AUTO);
        setWidget(list);
    }

    protected CheckBox initCheckBox() {
        return new CheckBox();
    }

    private void onAddItems(List<ListModelData> items, int index) {
        int count = 0;
        for (final ListModelData m : items) {
            CheckBox check = initCheckBox();
            check.setBoxLabel(labelProvider.getLabel(m));
            list.insert(check, index + count);
            count = count + 1;
            Boolean value = valueProvider.getValue(m);
            if (value != null) {
                selectionModel.select(m, value);
            }

            HandlerRegistration handler = check
                    .addChangeHandler(new ChangeHandler() {

                        @Override
                        public void onChange(ChangeEvent event) {
                            fireEvent(new CheckChangedEvent<RowModelData>(
                                    Collections.singletonList(m)));
                        }
                    });

            checkHandler.put(check, handler);
            modelCheck.put(m, check);
        }
    }

    private void onRemoveItem(RowModelData item) {
        CheckBox check = modelCheck.remove(item);
        list.remove(check);
        checkHandler.remove(check).removeHandler();
    }

    private void onClearItems() {
        list.clear();
        modelCheck.clear();
        for (CheckBox box : checkHandler.keySet()) {
            checkHandler.remove(box).removeHandler();
        }
    }

    @Override
    public HandlerRegistration addCheckChangedHandler(
            CheckChangedHandler<ListModelData> handler) {
        return addHandler(handler, CheckChangedEvent.getType());
    }

    public void setChecked(RowModelData model, boolean check) {
        setChecked(model, check, true);
    }

    public void setChecked(RowModelData model, boolean check, boolean fireEvents) {
        CheckBox checkBox = modelCheck.get(model);
        if (checkBox != null) {
            checkBox.setValue(check, fireEvents);
        }
    }

    public Store<ListModelData> getStore() {
        return store;
    }

    public void setStore(Store<ListModelData> store) {
        if (groupingRegistration != null) {
            groupingRegistration.removeHandler();
            groupingRegistration = null;
        }
        if (this.store != null) {
            onClearItems();
        }
        this.store = store;
        if (store != null) {
            onAddItems(store.getAll(), 0);
            groupingRegistration = new GroupingHandlerRegistration();
            groupingRegistration.add(store.addStoreAddHandler(handlers));
            groupingRegistration.add(store.addStoreRemoveHandler(handlers));
            groupingRegistration.add(store.addStoreClearHandler(handlers));
            groupingRegistration.add(store.addStoreDataChangeHandler(handlers));
        }
        selectionModel.bindCheckList(this);
    }

    public CheckSelectionModel getSelectionModel() {
        return selectionModel;
    }

}
