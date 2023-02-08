package org.whirlplatform.component.client.ext;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.dom.CompositeElement;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.BaseEventPreview;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.data.shared.event.StoreDataChangeEvent;
import com.sencha.gxt.widget.core.client.event.CheckChangeEvent;
import com.sencha.gxt.widget.core.client.event.CheckChangeEvent.CheckChangeHandler;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.tree.Tree;
import java.util.ArrayList;
import java.util.List;
import org.whirlplatform.component.client.resource.ApplicationBundle;
import org.whirlplatform.meta.shared.data.RowModelData;

/**
 * Дерево для корректной работы с поиском. Обычное неправильно работало при CheckCascade = TRI
 */
public class XTree<M extends RowModelData, C> extends Tree<M, C>
    implements HasValueChangeHandlers<M> {

    private Command searchCommand;
    private TextField searchField;
    private Image searchButton;
    private BaseEventPreview eventPreview;
    private boolean filtered;

    public XTree(TreeStore<M> store, ValueProvider<? super M, C> valueProvider,
                 TreeAppearance appearance) {
        super(store, valueProvider, appearance);
    }

    public XTree(TreeStore<M> store, ValueProvider<? super M, C> valueProvider) {
        super(store, valueProvider);
    }

    private boolean onHideSearch(NativePreviewEvent pe) {
        XElement target = pe.getNativeEvent().getEventTarget().cast();
        if (searchField.getElement().isOrHasChild(target)) {
            return false;
        }
        hideSearch();
        return true;
    }

    private void hideSearch() {
        RootPanel.get().remove(searchField);
        eventPreview.remove();
    }

    private void showSearch() {
        eventPreview.add();
        RootPanel.get().add(searchField);
        searchField.getElement().makePositionable(true);
        searchField.setPosition(getAbsoluteLeft(), getAbsoluteTop());
        searchField.setWidth(XTree.this.getElement().getWidth(false)
            - 25 - searchButton.getElement().getOffsetWidth());
        searchField.focus();
    }

    /**
     * Добавление поиска к дереву. Если searchCommand == null, ничего не делает
     *
     * @param searchCommand
     */
    public void addSearch(Command searchCommand) {
        if (searchCommand == null) {
            return;
        }
        this.searchCommand = searchCommand;
        searchField = new TextField();
        FieldClearDecorator clear = new FieldClearDecorator(searchField, new Command() {
            @Override
            public void execute() {
                clearSearch();
            }
        });
        clear.setRightOffset(2);

        searchField.getElement().setZIndex(16777271);

        searchField.addKeyDownHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    search();
                }
            }
        });

        eventPreview = new BaseEventPreview() {
            @Override
            protected boolean onAutoHide(NativePreviewEvent pe) {
                return XTree.this.onHideSearch(pe);
            }
        };

        searchButton = new Image(ApplicationBundle.INSTANCE.magnifier());
        searchButton.getElement().getStyle().setCursor(Cursor.POINTER);
        Event.sinkEvents(searchButton.getElement(), Event.ONCLICK);
        Event.setEventListener(searchButton.getElement(), new EventListener() {
            @Override
            public void onBrowserEvent(Event event) {
                if (event.getTypeInt() == Event.ONCLICK) {
                    if (searchField.isVisible()) {
                        search();
                    } else {
                        showSearch();
                    }
                }
            }
        });
        // Чтобы по клику на иконке поле ввода не скывалось
        eventPreview.setIgnoreList(new CompositeElement(new Element[] {searchButton.getElement()}));

        this.addAttachHandler(new AttachEvent.Handler() {

            @Override
            public void onAttachOrDetach(AttachEvent event) {
                getElement().getParentElement().insertFirst(searchButton.getElement());
                searchButton.getElement().getStyle().setZIndex(1000);
                searchButton.getElement().getStyle().setPosition(Position.ABSOLUTE);
                searchButton.getElement().getStyle().setRight(20, Unit.PX);
                searchButton.getElement().getStyle().setTop(3, Unit.PX);
            }
        });
    }

    public boolean hasSearch() {
        return searchCommand != null;
    }

    public void clearSearch() {
        searchField.clear();
        if (filtered) {
            XTree.this.searchCommand.execute();
            hideSearch();
        }
    }

    private void search() {
        XTree.this.searchCommand.execute();
        hideSearch();
    }

    //    @Override
    //    protected void onTriCheckCascade(M model, com.sencha.gxt.widget.core.client.tree.Tree.CheckState checked) {
    //        if (checked == CheckState.CHECKED) {
    //
    //            List<M> children = store.getAllChildren(model);
    //            cascade = false;// TODO
    //            for (M child : children) {
    //                TreeNode<M> n = findNode(child);
    //                if (n != null) {
    //                    setChecked(child, checked);
    //                }
    //            }
    //
    //            M parent = store.getParent(model);
    //            while (parent != null) {
    //                boolean allChildrenChecked = true;
    //                for (M child : store.getAllChildren(parent)) {
    //                    TreeNode<M> n = findNode(child);
    //                    if (n != null) {
    //                        if (!isChecked(child)) {
    //                            allChildrenChecked = false;
    //                        }
    //                    }
    //                }
    //
    //                if (!allChildrenChecked || model.getLevelCount() > store.getChildCount(parent)) {
    //                    setChecked(parent, CheckState.PARTIAL);
    //                } else {
    //                    setChecked(parent, CheckState.CHECKED);
    //                }
    //
    //                parent = store.getParent(parent);
    //
    //            }
    //            cascade = true; //TODO
    //        } else if (checked == CheckState.UNCHECKED) {
    //            List<M> children = store.getAllChildren(model);
    //            cascade = false; //TODO
    //            for (M child : children) {
    //                setChecked(child, checked);
    //            }
    //
    //            M parent = store.getParent(model);
    //            while (parent != null) {
    //                boolean anyChildChecked = false;
    //                for (M child : store.getAllChildren(parent)) {
    //                    if (isChecked(child)) {
    //                        anyChildChecked = true;
    //                    }
    //                }
    //
    //                if (anyChildChecked) {
    //                    setChecked(parent, CheckState.PARTIAL);
    //                } else {
    //                    setChecked(parent, CheckState.UNCHECKED);
    //                }
    //
    //                parent = store.getParent(parent);
    //            }
    //
    //            cascade = true; //TODO
    //        }
    //    }

    public String getSearchText() {
        return searchField == null ? "" : searchField.getText();
    }

    public TextField getSearchField() {
        return searchField;
    }

    public Image getSearchButton() {
        return searchButton;
    }

    public boolean isFiltered() {
        return filtered;
    }

    public void setFiltered(boolean filtered) {
        this.filtered = filtered;
        if (filtered) {
            searchButton.setResource(ApplicationBundle.INSTANCE.magnifierExclamation());
        } else {
            searchButton.setResource(ApplicationBundle.INSTANCE.magnifier());
        }
    }

    public void setSingleSelectionCheckMode() {
        addCheckChangeHandler(new CheckChangeHandler<M>() {
            private final List<M> partialMarkedElements = new ArrayList<>();
            /**
             * Если выполняется сейчас, ничего не делаем. Иначе бесконечная
             * рекурсия
             */
            private boolean executionNow = false;

            // Снять метки PARTIAL c элементов дерева и очистить список хранящий
            // эти элементы.
            private void clearPartials() {
                if (!partialMarkedElements.isEmpty()) {
                    for (M marked : partialMarkedElements) {
                        setChecked(marked, CheckState.UNCHECKED);
                    }
                    partialMarkedElements.clear();
                }
            }

            // Снять CHECK со всех помеченных на дереве элементов.
            private void clearChecked() {
                if (!getCheckedSelection().isEmpty()) {
                    for (M item : getCheckedSelection()) {
                        setChecked(item, CheckState.UNCHECKED);
                    }
                }
            }

            // Пометить как PARTIAL родительские
            private void markParents(M currentItem) {
                TreeStore<M> treeStore = getStore();
                M parent = treeStore.getParent(currentItem);
                int count = 1000;
                while (parent != null && count > 0) {
                    partialMarkedElements.add(parent);
                    setChecked(parent, CheckState.PARTIAL);
                    parent = treeStore.getParent(parent);
                    count--;
                }
            }

            @Override
            public void onCheckChange(CheckChangeEvent<M> event) {
                if (executionNow) {
                    return;
                }
                executionNow = true;
                M currentItem = event.getItem();
                boolean isCheck = isChecked(currentItem);
                boolean containsItem = partialMarkedElements.contains(currentItem);
                clearChecked();
                clearPartials();
                if (isCheck || (!isCheck && containsItem)) {
                    setChecked(currentItem, CheckState.CHECKED);
                    markParents(currentItem);
                }
                executionNow = false;
            }
        });
    }

    @Override
    protected void onCheckClick(Event event,
                                com.sencha.gxt.widget.core.client.tree.Tree.TreeNode<M> node) {
        super.onCheckClick(event, node);
        List<M> checked = getCheckedSelection();
        ValueChangeEvent.fire(this, checked.size() == 0 ? null : checked.get(0));
    }

    /**
     * Событие вызывается один раз после завершения всех cascadeCheck
     */
    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<M> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    /**
     * Чтобы срабатывал changeEvent при разворачивании ветки с выбранным элементом и checkStyle ==
     * child
     */
    @Override
    protected void onDataChanged(StoreDataChangeEvent<M> event) {
        int size = getCheckedSelection().size();
        super.onDataChanged(event);
        List<M> checked = getCheckedSelection();
        if (checked.size() != size) {
            ValueChangeEvent.fire(this, checked.size() == 0 ? null : checked.get(0));
        }
    }
}
