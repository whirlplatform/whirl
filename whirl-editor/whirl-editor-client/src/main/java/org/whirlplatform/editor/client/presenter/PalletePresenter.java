package org.whirlplatform.editor.client.presenter;

import com.google.gwt.user.client.ui.IsWidget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;
import com.sencha.gxt.dnd.core.client.DragSource;
import com.sencha.gxt.widget.core.client.Component;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.whirlplatform.editor.client.EditorEventBus;
import org.whirlplatform.editor.client.ToggleButtonGenerateDocs;
import org.whirlplatform.editor.client.main.ComponentPreferences;
import org.whirlplatform.editor.client.view.HelpDecorator;
import org.whirlplatform.editor.client.view.PalleteView;
import org.whirlplatform.editor.client.view.PropertyEditorView;
import org.whirlplatform.editor.client.view.toolbar.ToolBarView;
import org.whirlplatform.meta.shared.component.ComponentType;


@Presenter(view = PalleteView.class)
public class PalletePresenter extends BasePresenter<PalletePresenter.IPalleteView, EditorEventBus> {

    public PalletePresenter() {
        super();
    }

    @Override
    public void bind() {
        List<ComponentType> types = Arrays.stream(ComponentType.values())
            .filter(v -> !ComponentPreferences.PALETTE_EXCLUSIONS.contains(v))
            .collect(Collectors.toList());
        for (ComponentType t : types) {
            Component c = view.addComponentType(t);
            initDND(c, t);
        }
        ToggleButtonGenerateDocs.disableEnableTips(ToolBarView.toggleButton.getValue());
    }

    private void initDND(Component component, ComponentType type) {
        DragSource source = new DragSource(component);
        source.setData(type);
    }

    public void onBuildApp() {
        eventBus.addFirstRightComponent(view);
    }

    public interface IPalleteView extends IsWidget {

        Component addComponentType(ComponentType type);

    }

}
