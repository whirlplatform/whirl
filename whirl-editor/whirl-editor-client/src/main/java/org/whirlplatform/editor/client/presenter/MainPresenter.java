package org.whirlplatform.editor.client.presenter;

import com.google.gwt.user.client.ui.IsWidget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import org.whirlplatform.editor.client.EditorEventBus;
import org.whirlplatform.editor.client.view.MainView;
import org.whirlplatform.meta.shared.Version;
import org.whirlplatform.meta.shared.editor.ApplicationElement;

@Presenter(view = MainView.class)
public class MainPresenter extends BasePresenter<MainPresenter.IMainView, EditorEventBus> {

    public void onChangeFirstLeftComponent(IsWidget component) {
        getView().setFirstLeftComponent(component);
    }

    public MainPresenter() {
        super();
    }

    public void onCloseElementView() {
        onChangeCenterComponent(null);
    }

    public void onOpenElementView(IsWidget component) {
        if (component instanceof ElementPresenter) {
            onChangeSecondLeftComponent(null);
            onChangeSecondRightComponent(null);
        }
        onChangeCenterComponent(component);
    }

    public void onLoadApplication(ApplicationElement application, Version version) {
        onChangeCenterComponent(null);
        onChangeSecondLeftComponent(null);
        onChangeSecondRightComponent(null);
    }

    public void onChangeTopComponent(IsWidget component) {
        getView().setTopComponent(component);
    }

    public interface IMainView {

        void setTopComponent(IsWidget component);

        void setFirstLeftComponent(IsWidget component);

        void setSecondLeftComponent(IsWidget component);

        void addFirstRightComponent(IsWidget component);

        void addSecondRightComponent(IsWidget comonent);

        void addThirdRightComponent(IsWidget comonent);

        void setSecondRightComponent(IsWidget component);

        void setCenterComponent(IsWidget component);

        void initUi();
    }

    public void onChangeSecondLeftComponent(IsWidget component) {
        getView().setSecondLeftComponent(component);
    }

    // Добавление вьюх на таб-панель справа.
    public void onAddFirstRightComponent(IsWidget component) {
        getView().addFirstRightComponent(component);
    }

    public void onAddSecondRightComponent(IsWidget component) {
        getView().addSecondRightComponent(component);
    }

    public void onAddThirdRightComponent(IsWidget component) {
        getView().addThirdRightComponent(component);
    }

    public void onChangeSecondRightComponent(IsWidget component) {
        getView().setSecondRightComponent(component);
    }

    public void onChangeCenterComponent(IsWidget component) {
        getView().setCenterComponent(component);
    }

    public void onInitUi() {
        view.initUi();
        view.setCenterComponent(new SimpleContainer());
    }

    public void onInitHistory() {
    }
}
