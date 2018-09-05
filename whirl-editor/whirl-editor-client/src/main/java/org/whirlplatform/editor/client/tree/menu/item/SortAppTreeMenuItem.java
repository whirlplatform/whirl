package org.whirlplatform.editor.client.tree.menu.item;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.core.client.Style.Side;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.menu.CheckMenuItem;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import com.sencha.gxt.widget.core.client.tips.ToolTipConfig;
import org.whirlplatform.editor.client.image.ComponentBundle;
import org.whirlplatform.editor.client.tree.AppTree;
import org.whirlplatform.editor.client.util.ApplicationTreeComparator;
import org.whirlplatform.editor.client.util.TreeElementNamesComparator;
import org.whirlplatform.editor.shared.TreeState;
import org.whirlplatform.editor.shared.i18n.EditorMessage;
import org.whirlplatform.meta.shared.editor.AbstractElement;

import java.util.List;

public class SortAppTreeMenuItem<T extends AppTree> extends MenuItem {

    private final static String TITLE = EditorMessage.Util.MESSAGE.sort_title();
    private final static ImageResource ICON = ComponentBundle.INSTANCE.context();
    private final static String RADIO_GROUP = "SORT";

    private final static String ASC = EditorMessage.Util.MESSAGE.sort_asc();
    private final static String DESC = EditorMessage.Util.MESSAGE.sort_desc();
    private final static String DEFAULT = EditorMessage.Util.MESSAGE.sort_default();

    private T appTree;
    private TreeStore<AbstractElement> store;
    CheckMenuItem defaultItem;
    CheckMenuItem ascItem;
    CheckMenuItem descItem;

    public SortAppTreeMenuItem(T appTree) {
        this.appTree = appTree;
        this.setText(TITLE);
        this.setIcon(ICON);
        initSortItem();
        initStore();
    }

    private void initSortItem() {
        defaultItem = new CheckMenuItem(DEFAULT);
        ascItem = new CheckMenuItem(ASC);
        descItem = new CheckMenuItem(DESC);

        defaultItem.setGroup(RADIO_GROUP);
        ascItem.setGroup(RADIO_GROUP);
        descItem.setGroup(RADIO_GROUP);
        defaultItem.setChecked(true);

        ToolTipConfig defaultTip = new ToolTipConfig();
        defaultTip.setBody(EditorMessage.Util.MESSAGE.sort_default_tooltip());
        defaultTip.setAnchor(Side.LEFT);
        defaultItem.setToolTipConfig(defaultTip);

        ToolTipConfig ascTip = new ToolTipConfig();
        ascTip.setBody(EditorMessage.Util.MESSAGE.sort_asc_tooltip());
        ascTip.setAnchor(Side.LEFT);
        ascItem.setToolTipConfig(ascTip);

        ToolTipConfig descTip = new ToolTipConfig();
        descTip.setBody(EditorMessage.Util.MESSAGE.sort_desc_tooltip());
        descTip.setAnchor(Side.LEFT);
        descItem.setToolTipConfig(descTip);

        Menu menu = new Menu();
        menu.add(defaultItem);
        menu.add(ascItem);
        menu.add(descItem);
        menu.addSelectionHandler(createSelectionHandler());
        this.setSubMenu(menu);
        this.setEnabled(false);
    }

    private void initStore() {
        store = appTree.getStore();
    }

    protected SelectionHandler<Item> createSelectionHandler() {
        return new SelectionHandler<Item>() {
            @Override
            public void onSelection(SelectionEvent<Item> event) {
                MenuItem currentItem = (MenuItem) event.getSelectedItem();
                if (currentItem.equals(this)) {
                    return; // Клик по корню меню сортировки.
                }
                store.clearSortInfo();
                TreeState state = appTree.getState();
                String message = "";
                List<StoreSortInfo<AbstractElement>> sortInfo = store.getSortInfo();
                if (ascItem.equals(currentItem)) {
                    sortInfo.add(new StoreSortInfo<>(new TreeElementNamesComparator(), SortDir.ASC));
                    message = EditorMessage.Util.MESSAGE.sort_display_asc();
                } else if (descItem.equals(currentItem)) {
                    sortInfo.add(new StoreSortInfo<>(new TreeElementNamesComparator(), SortDir.DESC));
                    message = EditorMessage.Util.MESSAGE.sort_display_desc();
                } else if (defaultItem.equals(currentItem)) {
                    message = EditorMessage.Util.MESSAGE.sort_display_default();
                }
                sortInfo.add(new StoreSortInfo<>(new ApplicationTreeComparator(), SortDir.ASC));
                store.applySort(false);
                appTree.setState(state);
                Info.display(EditorMessage.Util.MESSAGE.sort_display_title(), message);
            }
        };
    }
}
