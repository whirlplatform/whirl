package org.whirlplatform.editor.client.presenter;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;
import com.mvp4g.client.view.ReverseViewInterface;
import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.ListStore;
import java.util.List;
import org.whirlplatform.editor.client.EditorEventBus;
import org.whirlplatform.editor.client.view.AppShowIconsView;
import org.whirlplatform.editor.shared.EditorDataService;

@Presenter(view = AppShowIconsView.class)
public class AppShowIconsPresenter
        extends BasePresenter<AppShowIconsPresenter.IAppShowIconsView, EditorEventBus> {

    private IAppShowIconsView iconsView;
    private ListStore<String> store;
    private RpcProxy<Void, List<String>> proxy;

    public AppShowIconsPresenter() {
    }

    public AppShowIconsPresenter(IAppShowIconsView iconsView) {
        this.iconsView = iconsView;
        this.store = new ListStore<>(String::toString);
        this.proxy = new RpcProxy<Void, List<String>>() {
            @Override
            public void load(Void loadConfig, AsyncCallback<List<String>> callback) {
                EditorDataService.Util.getDataService().getIcons(callback);
            }
        };
    }

    public void onShowIconsPanel() {
        getView().show();
    }

    public RpcProxy<Void, List<String>> getProxy() {
        return proxy;
    }

    public ListStore<String> getStore() {
        return store;
    }

    public interface IAppShowIconsView
            extends IsWidget, ReverseViewInterface<AppShowIconsPresenter> {
        void show();
    }
}
