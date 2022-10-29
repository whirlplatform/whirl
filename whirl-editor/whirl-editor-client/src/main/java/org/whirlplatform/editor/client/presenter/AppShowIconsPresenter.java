package org.whirlplatform.editor.client.presenter;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;
import com.mvp4g.client.view.ReverseViewInterface;
import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.ListStore;
import org.whirlplatform.editor.client.EditorEventBus;
import org.whirlplatform.editor.client.view.AppShowIconsView;
import org.whirlplatform.editor.shared.EditorDataService;


import java.util.List;
import java.util.logging.Logger;

@Presenter(view = AppShowIconsView.class)
public class AppShowIconsPresenter extends BasePresenter<AppShowIconsPresenter.IAppShowIconsView, EditorEventBus> {

    public interface IAppShowIconsView extends IsWidget, ReverseViewInterface<AppShowIconsPresenter> {
        void show();
    }

    private IAppShowIconsView IconsView;
    private ListStore<String> store;
    private RpcProxy<Void, List<String>> proxy;

    public void onShowIconsPanel() {
        getView().show();
    }

    public AppShowIconsPresenter() {
    }

    public AppShowIconsPresenter(IAppShowIconsView iconsView) {
        this.IconsView = iconsView;
        this.store = new ListStore<>(String::toString);
        this.proxy = new RpcProxy<Void, List<String>>() {
            @Override
            public void load(Void loadConfig, AsyncCallback<List<String>> callback) {
                EditorDataService.Util.getDataService().getIcons(callback);
            }
        };
    }

    public RpcProxy<Void, List<String>> getProxy() {
        return proxy;
    }

    public ListStore<String> getStore() {
        return store;
    }
}