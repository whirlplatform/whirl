package org.whirlplatform.editor.client.presenter;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;
import com.mvp4g.client.view.ReverseViewInterface;
import java.util.Collection;
import org.whirlplatform.component.client.utils.InfoHelper;
import org.whirlplatform.editor.client.EditorEventBus;
import org.whirlplatform.editor.client.util.HRefUtil;
import org.whirlplatform.editor.client.view.allapps.AllApplicationsView;
import org.whirlplatform.editor.shared.EditorDataService;
import org.whirlplatform.editor.shared.OpenResult;
import org.whirlplatform.meta.shared.ApplicationStoreData;
import org.whirlplatform.meta.shared.editor.ApplicationElement;

@Presenter(view = AllApplicationsView.class)
public class AllApplicationsPresenter
        extends BasePresenter<AllApplicationsPresenter.IAllApplicationsView, EditorEventBus> {

    private Callback<ApplicationStoreData, Throwable> getApplicationDataCallback;
    private Callback<OpenResult, Throwable> openApplicationCallback;

    public void fetchApplicationElement(final ApplicationStoreData data) {
        if (data != null) {
            EditorDataService.Util.getDataService()
                    .loadApplication(data, new AsyncCallback<ApplicationElement>() {
                        @Override
                        public void onSuccess(ApplicationElement result) {
                            getView().hide();
                            if (openApplicationCallback != null) {
                                openApplicationCallback.onSuccess(
                                        new OpenResult(result, data.getVersion()));
                                openApplicationCallback = null;
                            } else if (getApplicationDataCallback != null) {
                                getApplicationDataCallback.onSuccess(data);
                                getApplicationDataCallback = null;
                            } else {
                                getEventBus().loadApplication(result, data.getVersion());
                            }
                        }

                        @Override
                        public void onFailure(Throwable caught) {
                            view.showError(caught);
                        }
                    });
        }
    }

    public void onShowOpenApplications() {
        getView().show();
        loadApplicationsData();
    }

    public void onShowOpenApplicationsCallback(Callback<ApplicationStoreData, Throwable> callback) {
        this.getApplicationDataCallback = callback;
        onShowOpenApplications();
    }

    public void onOpenApplicationCallback(Callback<OpenResult, Throwable> callback) {
        openApplicationCallback = callback;
        onShowOpenApplications();
    }

    public void runPackageCreation(ApplicationStoreData data) {
        if (data != null) {
            EditorDataService.Util.getDataService().createPackage(data, new AsyncCallback<Void>() {
                @Override
                public void onSuccess(Void result) {
                    Window.open(GWT.getHostPageBaseURL() + "package", "_blank", "");
                }

                @Override
                public void onFailure(Throwable caught) {
                    InfoHelper.throwInfo("create-package", caught);
                }
            });
        }
    }

    public void runApplication(ApplicationStoreData data) {
        if (data != null) {
            HRefUtil.openNewApplicationTab(data);
        }
    }

    private void loadApplicationsData() {
        getView().showLoadDataProgress();
        EditorDataService.Util.getDataService()
                .loadApplicationList(new AsyncCallback<Collection<ApplicationStoreData>>() {
                    @Override
                    public void onSuccess(Collection<ApplicationStoreData> result) {
                        getView().loadApplications(result);
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        getView().showError(caught);
                    }
                });
    }

    public interface IAllApplicationsView
            extends IsWidget, ReverseViewInterface<AllApplicationsPresenter> {

        void loadApplications(Collection<ApplicationStoreData> data);

        void show();

        void hide();

        void setButtonsState();

        void showLoadDataProgress();

        void showError(Throwable caught);

        ApplicationStoreData getSelectedLeaf();

        void startApplicationEditing();

        void startApplicationRunning();

        void startApplicationPackaging();
    }
}
