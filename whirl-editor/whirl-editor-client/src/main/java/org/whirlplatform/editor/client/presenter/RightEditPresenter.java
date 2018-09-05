package org.whirlplatform.editor.client.presenter;

import com.google.gwt.core.client.Callback;
import com.google.gwt.user.client.ui.IsWidget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import org.whirlplatform.editor.client.EditorEventBus;
import org.whirlplatform.editor.client.view.RightEditView;
import org.whirlplatform.editor.shared.i18n.EditorMessage;
import org.whirlplatform.meta.shared.editor.AbstractElement;
import org.whirlplatform.meta.shared.editor.GroupElement;
import org.whirlplatform.meta.shared.editor.RightCollectionElement;
import org.whirlplatform.meta.shared.editor.RightType;

import java.util.Collection;
import java.util.Map;

@Presenter(view = RightEditView.class)
public class RightEditPresenter extends
        BasePresenter<RightEditPresenter.IRightEditView, EditorEventBus> {

    public interface IRightEditView extends IsWidget {

        void initialize(Collection<? extends AbstractElement> elements,
                        Collection<GroupElement> groups, Collection<RightType> types);

        void setRight(AbstractElement element, RightCollectionElement right);

        Map<AbstractElement, RightCollectionElement> getRights();

    }

    private Window w = new Window();

    public RightEditPresenter() {
        initUI();
    }

    private void initUI() {
        w.setWidth(900);
        w.setHeight(600);
        w.setModal(true);

        TextButton save = new TextButton(EditorMessage.Util.MESSAGE.apply());
        save.addSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                saveElementRights();
                w.hide();
            }
        });
        w.addButton(save);

        TextButton close = new TextButton(EditorMessage.Util.MESSAGE.close());
        close.addSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                w.hide();
            }
        });
        w.addButton(close);
    }

    private void saveElementRights() {
        Map<AbstractElement, RightCollectionElement> all = view.getRights();
        if (all != null) {
            for (AbstractElement e : all.keySet()) {
                eventBus.setElementRights(e, all.get(e));
            }
        }
    }

    public void onEditRights(final Collection<AbstractElement> elements,
                             final Collection<RightType> rightTypes) {
        eventBus.getAvailableGroups(new Callback<Collection<GroupElement>, Throwable>() {
            @Override
            public void onFailure(Throwable reason) {
            }

            @Override
            public void onSuccess(Collection<GroupElement> result) {
                view.initialize(elements, result, rightTypes);
                loadRights(elements);
            }
        });
    }

    private void loadRights(Collection<AbstractElement> elements) {
        eventBus.getElementRights(elements,
                new Callback<Collection<RightCollectionElement>, Throwable>() {

                    @Override
                    public void onFailure(Throwable reason) {
                    }

                    @Override
                    public void onSuccess(
                            Collection<RightCollectionElement> result) {
                        onRightsLoaded(result);
                    }

                });
    }

    private void onRightsLoaded(Collection<RightCollectionElement> rights) {
        for (RightCollectionElement collection : rights) {
            view.setRight(collection.getElement(), collection);
        }
        VerticalLayoutContainer container = new VerticalLayoutContainer();
        container.setScrollMode(ScrollMode.ALWAYS);
        container.add(view.asWidget());
        w.setWidget(container);
        w.show();
    }

}
