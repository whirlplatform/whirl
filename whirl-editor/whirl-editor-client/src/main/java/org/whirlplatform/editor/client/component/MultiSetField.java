package org.whirlplatform.editor.client.component;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.sencha.gxt.widget.core.client.event.BlurEvent;
import com.sencha.gxt.widget.core.client.event.BlurEvent.BlurHandler;
import com.sencha.gxt.widget.core.client.form.TriggerField;
import com.sencha.gxt.widget.core.client.menu.Item;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.whirlplatform.editor.client.component.MultiSetCellDefaultAppearance.MultiSetCellResources;

public class MultiSetField<T> extends TriggerField<String> {

    private T oldKey;
    private Map<T, String> values = new HashMap<T, String>();

    public MultiSetField(Collection<T> keys) {
        this(keys, new MultiSetCell<T>());
    }

    public MultiSetField(Collection<T> keys, MultiSetCell<T> cell) {
        super(cell);
        setKeys(keys);
        initTrigger();
    }

    public MultiSetField(Collection<T> keys, MultiSetCellResources resources) {
        this(keys, new MultiSetCell<T>(new MultiSetCellDefaultAppearance(
                resources)));
    }

    @SuppressWarnings("unchecked")
    @Override
    public MultiSetCell<T> getCell() {
        return (MultiSetCell<T>) super.getCell();
    }

    private void initTrigger() {
        addSelectionHandler(new SelectionHandler<Item>() {
            @Override
            public void onSelection(SelectionEvent<Item> event) {
                T k = event.getSelectedItem().getData("key");
                setNewKey(k);
                focus();
            }
        });
        addBlurHandler(new BlurHandler() {

            @Override
            public void onBlur(BlurEvent event) {
                saveOldValue(null);
            }
        });
    }

    private void setNewKey(T key) {
        saveOldValue(key);
        restoreValue(key);
        oldKey = key;
    }

    private void saveOldValue(T key) {
        if (oldKey != null && !oldKey.equals(key)) {
            values.put(oldKey, this.getText());
        }
    }

    private void restoreValue(T key) {
        super.setValue(values.get(key), false, false);
        setText(values.get(key));
    }

    public void setKeys(Collection<T> keys) {
        getCell().setKeys(keys);
    }

    public HandlerRegistration addSelectionHandler(
            SelectionHandler<Item> handler) {
        return getCell().addSelectionHandler(handler);
    }

    public T getCurrentKey() {
        return getCell().getCurrentKey();
    }

    public void setCurrentKey(T key) {
        if (getCell().getKeys().contains(key)) {
            setNewKey(key);
            getCell().setCurrentKey(key);
        }
    }

    public String getValue(T key) {
        return values.get(key);
    }

    public void setValue(T key, String value) {
        values.put(key, value);
    }

    public void redrawCell() {
        getCell().redraw(getElement());
    }

    @Override
    public void clear() {
        values.clear();
        oldKey = null;
        super.clear();
    }
}
