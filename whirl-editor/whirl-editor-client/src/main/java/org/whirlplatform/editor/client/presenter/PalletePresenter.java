package org.whirlplatform.editor.client.presenter;

import com.google.gwt.user.client.ui.IsWidget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;
import com.sencha.gxt.dnd.core.client.DragSource;
import com.sencha.gxt.widget.core.client.Component;
import org.whirlplatform.editor.client.EditorEventBus;
import org.whirlplatform.editor.client.view.PalleteView;
import org.whirlplatform.meta.shared.component.ComponentType;

@Presenter(view = PalleteView.class)
public class PalletePresenter extends BasePresenter<PalletePresenter.IPalleteView, EditorEventBus> {

    public interface IPalleteView extends IsWidget {

        Component addComponentType(ComponentType type);

    }

    public PalletePresenter() {
        super();
    }

    @Override
    public void bind() {
        for (ComponentType t : ComponentType.values()) {
            Component c = view.addComponentType(t);
            initDND(c, t);
        }
    }

    private void initDND(Component component, ComponentType type) {
        DragSource source = new DragSource(component);
        source.setData(type);
    }

    public void onBuildApp() {
        eventBus.addFirstRightComponent(view);
    }

}
