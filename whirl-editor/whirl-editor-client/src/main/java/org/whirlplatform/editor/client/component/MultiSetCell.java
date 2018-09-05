package org.whirlplatform.editor.client.component;

import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.sencha.gxt.cell.core.client.form.TriggerFieldCell;
import com.sencha.gxt.core.client.Style.Anchor;
import com.sencha.gxt.core.client.Style.AnchorAlignment;
import com.sencha.gxt.core.client.dom.DomQuery;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;

import java.util.Collection;

public class MultiSetCell<T> extends TriggerFieldCell<String> {

    public interface MultiSetCellAppearance extends TriggerFieldAppearance {
    }

    private Menu menu;
    private XElement trigger;
    private T currentKey;
    private Collection<T> keys;
    private AnchorAlignment alignment = new AnchorAlignment(Anchor.TOP_LEFT);

    public MultiSetCell() {
        this(
                GWT.<MultiSetCellDefaultAppearance>create(MultiSetCellDefaultAppearance.class));
    }

    public MultiSetCell(MultiSetCellAppearance appearance) {
        super(appearance);
        createMenu();
    }

    private void createMenu() {
        menu = new Menu();
        menu.addSelectionHandler(new SelectionHandler<Item>() {
            @Override
            public void onSelection(SelectionEvent<Item> event) {
                MenuItem item = (MenuItem) event.getSelectedItem();
                T key = item.getData("key");
                setTriggerText(key.toString());
                currentKey = key;
            }
        });
    }

    protected void setTriggerText(String text) {
        if (trigger != null) {
            trigger.setInnerText(text);
        }
    }

    private void refreshMenu() {
        menu.clear();
        for (T key : keys) {
            Item item = new MenuItem(key.toString());
            item.setData("key", key);
            menu.add(item);
        }
    }

    public XElement getTriggerElement() {
        return trigger;
    }

    public void setTriggerElement(XElement trigger) {
        this.trigger = trigger;
    }

    public T getCurrentKey() {
        return currentKey;
    }

    public void setCurrentKey(T key) {
        currentKey = key;
        if (key != null) {
            setTriggerText(key.toString());
        }
    }

    public Collection<T> getKeys() {
        return keys;
    }

    public void setKeys(Collection<T> keys) {
        this.keys = keys;
        refreshMenu();
    }

    public Menu getMenu() {
        return menu;
    }

    protected void redraw(XElement parent) {
        trigger = (XElement) DomQuery.selectNode("td/div", parent);
        if (currentKey != null) {
            setTriggerText(currentKey.toString());
        }
    }

    @Override
    protected void onTriggerClick(Context context, XElement parent,
                                  NativeEvent event, String value, ValueUpdater<String> updater) {
        if (menu.isVisible()) {
            menu.hide();
        } else {
            trigger = (XElement) DomQuery.selectNode("td/div", parent);
            menu.show(trigger, alignment);
        }
    }

    public HandlerRegistration addSelectionHandler(
            SelectionHandler<Item> handler) {
        return menu.addSelectionHandler(handler);
    }

    @Override
    public void setValue(com.google.gwt.cell.client.Cell.Context context,
                         Element parent, String value) {
        super.setValue(context, parent, value);
    }
}
