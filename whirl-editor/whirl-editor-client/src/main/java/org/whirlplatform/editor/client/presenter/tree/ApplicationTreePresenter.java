package org.whirlplatform.editor.client.presenter.tree;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.ui.IsWidget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;
import com.sencha.gxt.dnd.core.client.DndDragMoveEvent.DndDragMoveHandler;
import org.whirlplatform.editor.client.EditorEventBus;
import org.whirlplatform.editor.client.tree.AppTree;
import org.whirlplatform.editor.client.tree.AppTreePresenter;
import org.whirlplatform.editor.client.view.ApplicationTreeView;
import org.whirlplatform.editor.shared.TreeState;
import org.whirlplatform.meta.shared.ApplicationStoreData;
import org.whirlplatform.meta.shared.Version;
import org.whirlplatform.meta.shared.editor.AbstractElement;
import org.whirlplatform.meta.shared.editor.ApplicationElement;
import org.whirlplatform.meta.shared.editor.ComponentElement;
import org.whirlplatform.meta.shared.editor.RightType;

import java.util.Collection;

@Presenter(view = ApplicationTreeView.class)
public class ApplicationTreePresenter extends
        BasePresenter<ApplicationTreePresenter.IApplicationTreeView, EditorEventBus> implements AppTreePresenter {

    public interface IApplicationTreeView extends IsWidget {

        void addDragMoveHandler(DndDragMoveHandler handler);

        void selectTreeElement(AbstractElement element);

        TreeState getState();

        void setState(TreeState state);

        AppTree getTree();

        void loadApplication(ApplicationElement application, Version version);
    }

    /**
     * Позволяет определить, элемент был выбран кликом по дереву или из
     * DesignPresenter
     */
    private boolean externalSelection;

    public ApplicationTreePresenter() {
        super();
    }

    public void onBuildApp() {
        eventBus.changeFirstLeftComponent(view);
    }

    /**
     * Строит полное дерево приложения
     *
     * @param application
     */
    public void onLoadApplication(ApplicationElement application, Version version) {
        view.loadApplication(application, version);
    }

    /**
     * Получить состояние приложения.
     *
     * @param callback
     */
    public void onGetApplicationState(Callback<TreeState, Throwable> callback) {
        callback.onSuccess(view.getState());
    }

    /**
     * Восстанавливает состояние приложения
     *
     * @param state
     */
    public void onRestoreApplicationState(TreeState state) {
        view.setState(state);
    }

    public void onSelectElement(final AbstractElement element) {
        if (element instanceof ComponentElement) {
            eventBus.selectComponentElement((ComponentElement) element);
            if (!externalSelection) {
                eventBus.selectComponentDesignElement((ComponentElement) element);
            } else {
                externalSelection = false;
            }
        } else {
            Scheduler.get().scheduleDeferred(() -> eventBus.changeSecondLeftComponent(null));
        }
    }

    public void onRemoveElementUI(AbstractElement parent, AbstractElement element) {
        view.getTree().doRemoveElementUI(parent, element);
    }

    public void onAddElementUI(AbstractElement parent, AbstractElement element) {
        view.getTree().doAddElementUI(parent, element);
    }

    public void openElement(AbstractElement element) {
        if (view.getTree().isReference(element)) {
            eventBus.viewElement(element);
        } else {
            eventBus.openElement(element);
        }
    }

    public void onSelectTreeElement(AbstractElement element) {
        externalSelection = true;
        view.selectTreeElement(element);
    }

    public void onDragDrop(AbstractElement dropTarget, Object dropData) {
        view.getTree().doDragDrop(dropTarget, dropData);
    }

    /*
     *
     */
    @Override
    public void riseAddElement(AbstractElement parent, AbstractElement element) {
        eventBus.addElement(parent, element);
    }

    @Override
    public void riseAddElementUI(AbstractElement parent, AbstractElement element) {
        eventBus.addElementUI(parent, element);
    }

    @Override
    public void riseRemoveElement(AbstractElement parent, AbstractElement element, boolean showDialog) {
        eventBus.removeElement(parent, element, showDialog);
    }

    @Override
    public void riseRemoveElementUI(AbstractElement parent, AbstractElement element) {
        eventBus.removeElementUI(parent, element);
    }

    @Override
    public void riseEditRights(Collection<? extends AbstractElement> elements, Collection<RightType> rightTypes) {
        eventBus.editRights(elements, rightTypes);
    }

    @Override
    public void riseOpenElement(AbstractElement element) {
        eventBus.openElement(element);
    }

    @Override
    public void riseCloseOpenApplication() {
        // eventBus.closeOpenApplication();
    }

    @Override
    public void riseShowOpenApplicationsCallback(Callback<ApplicationStoreData, Throwable> callback) {
        eventBus.showOpenApplicationsCallback(callback);
    }
}
