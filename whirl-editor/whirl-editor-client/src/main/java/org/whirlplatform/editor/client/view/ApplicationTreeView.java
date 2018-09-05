package org.whirlplatform.editor.client.view;

import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.mvp4g.client.view.ReverseViewInterface;
import com.sencha.gxt.dnd.core.client.DndDragMoveEvent;
import com.sencha.gxt.dnd.core.client.DndDragMoveEvent.DndDragMoveHandler;
import com.sencha.gxt.dnd.core.client.DndDragStartEvent;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.dnd.core.client.TreeDragSource;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.tree.Tree;
import org.whirlplatform.editor.client.dnd.ApplicationTreeDropTarget;
import org.whirlplatform.editor.client.presenter.tree.ApplicationTreePresenter;
import org.whirlplatform.editor.client.presenter.tree.ApplicationTreePresenter.IApplicationTreeView;
import org.whirlplatform.editor.client.tree.AppTree;
import org.whirlplatform.editor.client.tree.AppTreeWidget;
import org.whirlplatform.editor.client.tree.toolbar.AppTreeToolBarWithSort;
import org.whirlplatform.editor.client.view.widget.WidgetUtil;
import org.whirlplatform.editor.shared.TreeState;
import org.whirlplatform.meta.shared.Version;
import org.whirlplatform.meta.shared.editor.*;

public class ApplicationTreeView extends VBoxLayoutContainer implements HasSelectionHandlers<AbstractElement>,
        IApplicationTreeView, ReverseViewInterface<ApplicationTreePresenter> {

    private ApplicationTreePresenter presenter;
    private AppTree tree;
    private AppTreeToolBarWithSort toolbar;

    private ApplicationTreeDropTarget dropTarget;

    public ApplicationTreeView() {
        super();
        tree = new AppTreeWidget() {
            @Override
            public void doOpenElement(AbstractElement element) {
                if (!(element instanceof ComponentElement)
                        || (element instanceof ComponentElement && ((ComponentElement) element).getType().isContainer())) {
                    //Если контейнер, то можно открыть для редактирования. Иначе - редактировать только свойства.
                    presenter.openElement(tree.getSelectedElement());
                }
            }
        };
        toolbar = new AppTreeToolBarWithSort(tree);
        initUi();
        initDND();
        initListeners();
    }

    private void initUi() {
        this.setVBoxLayoutAlign(VBoxLayoutAlign.STRETCH);
        this.add(toolbar, WidgetUtil.noStretchLayout());
        this.add(tree.asWidget(), WidgetUtil.flexLayout());
    }

    @SuppressWarnings("unchecked")
    private void initDND() {
        dropTarget = new ApplicationTreeDropTarget((Tree<AbstractElement, ?>) tree) {

            @Override
            protected void onDragDrop(DndDropEvent event) {
                presenter.onDragDrop(getDropTargetElement(event), event.getData());
                super.onDragDrop(event);
            }

            @Override
            protected void onDragMove(DndDragMoveEvent event) {
                event.getStatusProxy().setStatus(tree.canDragDrop(getDropTargetElement(event), event.getData()));
            }
        };
        dropTarget.setAllowSelfAsSource(true);

        @SuppressWarnings("unused")
        TreeDragSource<AbstractElement> source = new TreeDragSource<AbstractElement>((Tree<AbstractElement, ?>) tree) {
            @Override
            protected void onDragDrop(DndDropEvent event) {
            }

            @Override
            protected void onDragStart(DndDragStartEvent event) {
                super.onDragStart(event);
                AbstractElement selectedItem = tree.getSelectedElement();
                if (selectedItem instanceof ComponentElement || selectedItem instanceof EventParameterElement
                        || selectedItem instanceof EventElement || selectedItem instanceof ContextMenuItemElement) {
                    event.setData(tree.getSelectedElement());
                } else {
                    event.setCancelled(true);
                }

            }
        };
    }

    @SuppressWarnings("unchecked")
    private void initListeners() {
        ((Tree<AbstractElement, String>) tree).getSelectionModel()
                .addSelectionHandler(new SelectionHandler<AbstractElement>() {
                    @Override
                    public void onSelection(SelectionEvent<AbstractElement> event) {
                        AbstractElement element = event.getSelectedItem();
                        presenter.onSelectElement(element);
                    }
                });
    }

    @Override
    public HandlerRegistration addSelectionHandler(SelectionHandler<AbstractElement> handler) {
        return addHandler(handler, SelectionEvent.getType());
    }

    @Override
    public void addDragMoveHandler(DndDragMoveHandler handler) {
        dropTarget.addDragMoveHandler(handler);
    }

    @Override
    public void setPresenter(ApplicationTreePresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public ApplicationTreePresenter getPresenter() {
        return presenter;
    }

    @Override
    public void selectTreeElement(AbstractElement element) {
        tree.selectElement(element);
    }

    @Override
    public TreeState getState() {
        return tree.getState();
    }

    @Override
    public void setState(TreeState state) {
        tree.setState(state);
    }

    @Override
    public AppTree getTree() {
        return tree;
    }

    @Override
    public void loadApplication(ApplicationElement application, Version version) {
        tree.loadApplication(application, version, presenter);
        toolbar.setSortItemEnabled(true);
        toolbar.setAppTree(tree);
        forceLayout();
    }
}
