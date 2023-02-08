package org.whirlplatform.component.client.ext;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.dom.DomQuery;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.data.shared.loader.Loader;
import com.sencha.gxt.data.shared.loader.TreeLoader;
import com.sencha.gxt.widget.core.client.ComponentHelper;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.event.CheckChangeEvent;
import com.sencha.gxt.widget.core.client.event.CheckChangeEvent.CheckChangeHandler;
import com.sencha.gxt.widget.core.client.event.TriggerClickEvent;
import com.sencha.gxt.widget.core.client.event.TriggerClickEvent.TriggerClickHandler;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.tree.Tree;
import com.sencha.gxt.widget.core.client.tree.Tree.CheckCascade;
import com.sencha.gxt.widget.core.client.tree.Tree.CheckState;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.whirlplatform.meta.shared.data.ListModelData;
import org.whirlplatform.meta.shared.data.ListModelDataImpl;

public class TreeComboBox extends ComboBox<ListModelData> {

    private final TreeStore<ListModelData> treeStore;
    private final List<ListModelData> checkedItems;
    private final TreeLoader<ListModelData> loader;
    Element innerElement;
    private CountElement ce;
    private XTree<ListModelData, String> tree;
    private Composite composite;

    public TreeComboBox(ComboBoxCell<ListModelData> cell, Cell<String> treeCell,
                        TreeStore<ListModelData> store,
                        TreeLoader<ListModelData> loader) {
        super(cell);
        treeStore = store;
        this.loader = loader;
        checkedItems = new ArrayList<ListModelData>();
        ListModelData md = new ListModelDataImpl();
        md.setLabel("");
        cell.getStore().add(md);
        this.setTriggerAction(TriggerAction.ALL);
        createTreePanel(treeCell);
        bind();
    }

    private void createTreePanel(Cell<String> cell) {
        tree = new XTree<ListModelData, String>(treeStore, new TreeValueProvider());
        tree.setCheckable(true);
        tree.setLoader((TreeLoader<ListModelData>) getLoader());
        tree.setCell(cell);
        tree.addCheckChangeHandler(new CheckChangeHandler<ListModelData>() {
            @Override
            public void onCheckChange(CheckChangeEvent<ListModelData> event) {
                if (event.getChecked() == CheckState.CHECKED) {
                    if (!checkedItems.contains(event.getItem())) {
                        checkedItems.add(event.getItem());
                    }
                } else {
                    checkedItems.remove(event.getItem());
                }
            }
        });
        tree.addValueChangeHandler(new ValueChangeHandler<ListModelData>() {
            @Override
            public void onValueChange(ValueChangeEvent<ListModelData> event) {
                updateText();
            }
        });
        composite = new Composite() {
            {
                initWidget(tree);
            }
        };
    }

    private void bind() {
        addTriggerClickHandler(new TriggerClickHandler() {

            @Override
            public void onTriggerClick(TriggerClickEvent event) {
                if (!isExpanded()) {
                    setText("");
                    tree.mask();
                    getLoader().load();
                }
            }
        });

        addBeforeSelectionHandler(new BeforeSelectionHandler<ListModelData>() {

            @Override
            public void onBeforeSelection(BeforeSelectionEvent<ListModelData> event) {
                event.cancel();
            }
        });
    }

    private void disableCheckListener() {
        tree.disableEvents();
    }

    private void enableCheckListener() {
        tree.enableEvents();
    }

    public List<ListModelData> getSelection() {
        return Collections.unmodifiableList(new ArrayList<ListModelData>(checkedItems));
    }

    public void setSelection(List<ListModelData> items) {
        int count = checkedItems.size();
        checkedItems.clear();
        if (items != null) {
            for (final ListModelData d : items) {
                if (d.getId() != null) {
                    checkedItems.add(d);
                }
            }
        }
        updateText();
        if (checkedItems.size() > 0 || count > 0) {
            ValueChangeEvent.fire(tree, checkedItems.size() == 0 ? null : checkedItems.get(0));
        }
    }

    public ListModelData getSelectedItem() {
        return tree.getSelectionModel().getSelectedItem();
    }

    public void setChecked(ListModelData item, CheckState checked, boolean fireEvent) {
        if (checkedItems.contains(item)) {
            if (CheckState.UNCHECKED.equals(checked) || CheckState.PARTIAL.equals(checked)) {
                checkedItems.remove(item);
                if (!fireEvent) {
                    disableCheckListener();
                }
                tree.setChecked(item, checked);
                if (!fireEvent) {
                    enableCheckListener();
                }
                updateText();
                return;
            } else {
                return;
            }
        }

        if (CheckState.CHECKED.equals(checked)) {
            checkedItems.add(item);
            if (!fireEvent) {
                disableCheckListener();
            }
            tree.setChecked(item, checked);
            if (!fireEvent) {
                enableCheckListener();
            }
            updateText();
        }
    }

    public void setCheckStyle(CheckCascade style) {
        tree.setCheckStyle(style);
    }

    public void onStoreLoad() {
        if (!isExpanded()) {
            expand();
        }
        if (!composite.isAttached()) {
            ComponentHelper.setParent(this, composite);
        }
        XElement element = getListView().getElement();

        if (innerElement == null) {
            SafeHtmlBuilder html = new SafeHtmlBuilder();
            html.appendHtmlConstant("<div style=\"height: 200px\">");
            html.appendHtmlConstant("</div>");
            innerElement = element.createChild(html.toSafeHtml());
        }
        innerElement.appendChild(composite.getElement());
        onTreeDataChanged();
        tree.unmask();
        // Т.к. фокус сбрасывается, и комбобокс закрывается
        focus();
    }

    private void onTreeDataChanged() {
        disableCheckListener();

        for (ListModelData model : treeStore.getAll()) {
            if (checkedItems.contains(model)) {
                tree.setChecked(model, CheckState.CHECKED);
                tree.refresh(model);
            }
        }
        updateText();

        enableCheckListener();
    }

    @Override
    protected void onAfterFirstAttach() {
        super.onAfterFirstAttach();
        ce = new CountElement(DomQuery.selectNode("input", getElement()));
        updateText();
    }

    protected void updateText() {
        if (ce != null) {
            ce.setCount(checkedItems.size());
        }
    }

    @Override
    public ListModelData getValue() {
        return checkedItems.size() > 0 ? checkedItems.get(0) : null;
    }

    @Override
    public void setValue(ListModelData value) {
        // Устанавливается через setChecked
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void clear() {
        int size = checkedItems.size();
        checkedItems.clear();
        disableCheckListener();
        tree.setCheckedSelection(null);
        enableCheckListener();
        updateText();
        if (size > 0) {
            ValueChangeEvent.fire(tree, null);
        }
    }

    public void setSelectionMode(SelectionMode mode) {
        tree.getSelectionModel().setSelectionMode(mode);
    }

    @Override
    public Loader<ListModelData, List<ListModelData>> getLoader() {
        return loader;
    }

    public Tree<ListModelData, String> getTree() {
        return tree;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<ListModelData> handler) {
        return tree.addValueChangeHandler(handler);
    }

    public void setSingleSelectionCheckMode() {
        tree.setSingleSelectionCheckMode();
    }

    private class TreeValueProvider implements ValueProvider<ListModelData, String> {

        @Override
        public String getValue(ListModelData object) {
            return object.getLabel();
        }

        @Override
        public void setValue(ListModelData object, String value) {
        }

        @Override
        public String getPath() {
            return null;
        }
    }
}
