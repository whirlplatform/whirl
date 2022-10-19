package org.whirlplatform.editor.client.presenter;

import com.google.gwt.core.client.Callback;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;
import com.mvp4g.client.view.ReverseViewInterface;
import com.sencha.gxt.cell.core.client.SimpleSafeHtmlCell;
import com.sencha.gxt.core.client.Style;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.loader.ListStoreBinding;
import com.sencha.gxt.data.shared.loader.Loader;
import com.sencha.gxt.widget.core.client.ListView;
import org.whirlplatform.editor.client.EditorEventBus;
import org.whirlplatform.editor.client.view.AppShowIconsView;
import org.whirlplatform.editor.shared.EditorDataService;
import org.whirlplatform.editor.shared.metadata.ApplicationBasicInfo;
import org.whirlplatform.meta.shared.ApplicationStoreData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Presenter(view = AppShowIconsView.class)
public class AppShowIconsPresenter extends BasePresenter<AppShowIconsPresenter.IAppShowIconsView, EditorEventBus> {

    public interface IAppShowIconsView extends IsWidget, ReverseViewInterface<AppShowIconsPresenter> {

    }

    private Callback<String, Throwable> resultCallback;

    private List<String> list = new ArrayList<>();

    private AppShowIconsView IconsView;
    private ListStore<String> store;

    private ListView<String, String> viewList;

    private RpcProxy<Void, List<String>> proxy;


    public void onGetIcons() {
        loadIcons();
    }

    public AppShowIconsPresenter() {
    }

    public AppShowIconsPresenter(AppShowIconsView iconsView) {
        this.IconsView = iconsView;
        this.store = new ListStore<>(String::toString);
        this.proxy = new RpcProxy<Void, List<String>>() {
            @Override
            public void load(Void loadConfig, AsyncCallback<List<String>> callback) {
                EditorDataService.Util.getDataService().getIcons(callback);
            }
        };
//        onGetIcons();
    }

    public void loadIcons() {
        EditorDataService.Util.getDataService().getIcons(new AsyncCallback<List<String>>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(List<String> result) {
                list = result;

            }
        });

    }

    public RpcProxy<Void, List<String>> getProxy() {
//        this.proxy = new RpcProxy<Void, List<String>>() {
//            @Override
//            public void load(Void loadConfig, AsyncCallback<List<String>> callback) {
//                EditorDataService.Util.getDataService().getIcons(callback);
//            }
//        };
        return proxy;
    }

    public ListStore<String> getStore() {
//        this.store = new ListStore<>(String::toString);
        return store;
    }

    public AppShowIconsView getIconsView() {
        return IconsView;
    }

    public void setIconsView(AppShowIconsView iconsView) {
        this.IconsView = iconsView;
    }
}
