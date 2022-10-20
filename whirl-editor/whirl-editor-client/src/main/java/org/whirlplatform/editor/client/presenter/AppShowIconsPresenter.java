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

    private final static Logger logger = Logger.getLogger(AppShowIconsPresenter.class.getName());

    private IAppShowIconsView IconsView;
    private ListStore<String> store;
    private RpcProxy<Void, List<String>> proxy;


    public void onShowIconsPanel() {
        loadIcons();
    }

    public AppShowIconsPresenter() {
    }

    public AppShowIconsPresenter(IAppShowIconsView iconsView) {
        this.IconsView = iconsView;
        this.store = new ListStore<>(String::toString);
        this.proxy = new RpcProxy<Void, List<String>>() {
            @Override
            public void load(Void loadConfig, AsyncCallback<List<String>> callback) {
                EditorDataService.Util.getDataService().showIconsPanel(callback);
            }
        };
    }

    public void loadIcons() {
//        if (store.size()==0){
        if (store == null) {
            logger.info("store = null");
            EditorDataService.Util.getDataService().showIconsPanel(new AsyncCallback<List<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                }
                @Override
                public void onSuccess(List<String> result) {
                    logger.info("RESULT FROM AppShowIconsPresenter");
                    store.replaceAll(result);


                }
            });
        }
        logger.info("store not null");
        getView().show();

    }

    public RpcProxy<Void, List<String>> getProxy() {
        return proxy;
    }

    public ListStore<String> getStore() {
        return store;
    }

    public IAppShowIconsView getIconsView() {
        return IconsView;
    }

    public void setIconsView(AppShowIconsView iconsView) {
        this.IconsView = iconsView;
    }
}
