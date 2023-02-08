package org.whirlplatform.editor.client.presenter;

import com.google.gwt.core.client.Callback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;
import com.mvp4g.client.view.ReverseViewInterface;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.whirlplatform.editor.client.EditorEventBus;
import org.whirlplatform.editor.client.view.AppBasicInfoView;
import org.whirlplatform.editor.shared.EditorDataService;
import org.whirlplatform.editor.shared.metadata.ApplicationBasicInfo;
import org.whirlplatform.meta.shared.ApplicationStoreData;
import org.whirlplatform.meta.shared.Version;

/**
 * Ввод данных приложения
 */
@Presenter(view = AppBasicInfoView.class)
public class AppBasicInfoPresenter
    extends BasePresenter<AppBasicInfoPresenter.IAppBasicInfoView, EditorEventBus> {

    private Callback<ApplicationBasicInfo, Throwable> resultCallback;
    private Map<String, Set<Version>> appsMap = new HashMap<>();

    public void onGetAppInfoForNew(Callback<ApplicationBasicInfo, Throwable> callback) {
        loadApplications();
        resultCallback = callback;
        view.showAsNew();
    }

    public void onGetAppInfoForSaveAs(ApplicationBasicInfo info,
                                      Callback<ApplicationBasicInfo, Throwable> callback) {
        loadApplications();
        resultCallback = callback;
        view.showAsSaveAs(info);
    }

    public void returnResult(ApplicationBasicInfo appInfo) {
        resultCallback.onSuccess(appInfo);
    }

    public void returnFailure() {
        resultCallback.onFailure(null);
    }

    public boolean exists(final String applicationCode, final Version version) {
        if (applicationCode == null || version == null) {
            return false;
        }
        if (!appsMap.containsKey(applicationCode)) {
            return false;
        }
        return appsMap.get(applicationCode).contains(version);
    }

    private void loadApplications() {
        EditorDataService.Util.getDataService()
            .loadApplicationList(new AsyncCallback<Collection<ApplicationStoreData>>() {
                @Override
                public void onFailure(Throwable caught) {
                    view.showError(caught);
                }

                @Override
                public void onSuccess(Collection<ApplicationStoreData> result) {
                    initAppsMap(result);
                }
            });
    }

    protected void initAppsMap(Collection<ApplicationStoreData> result) {
        for (final ApplicationStoreData data : result) {
            String code = data.getCode();
            if (!appsMap.containsKey(code)) {
                appsMap.put(code, new HashSet<Version>());
            }
            appsMap.get(code).add(data.getVersion());
        }
    }

    public interface IAppBasicInfoView
        extends IsWidget, ReverseViewInterface<AppBasicInfoPresenter> {
        void showAsNew();

        void showAsSaveAs(ApplicationBasicInfo appInfo);

        void showError(Throwable caught);
    }
}