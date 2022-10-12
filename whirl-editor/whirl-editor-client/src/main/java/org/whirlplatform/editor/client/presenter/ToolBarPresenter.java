package org.whirlplatform.editor.client.presenter;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;
import com.mvp4g.client.view.ReverseViewInterface;
import org.whirlplatform.component.client.utils.InfoHelper;
import org.whirlplatform.editor.client.EditorEventBus;
import org.whirlplatform.editor.client.util.HRefUtil;
import org.whirlplatform.editor.client.view.toolbar.ToolBarView;
import org.whirlplatform.editor.shared.EditorDataService;
import org.whirlplatform.editor.shared.metadata.ApplicationBasicInfo;
import org.whirlplatform.meta.shared.ApplicationStoreData;
import org.whirlplatform.meta.shared.Version;
import org.whirlplatform.meta.shared.editor.ApplicationElement;

import java.util.List;

/**
 * Основная панель инструментов
 */
@Presenter(view = ToolBarView.class)
public class ToolBarPresenter extends BasePresenter<ToolBarPresenter.IToolBarView, EditorEventBus> {

    public interface IToolBarView extends IsWidget, ReverseViewInterface<ToolBarPresenter> {

        void updateButtonState();

        void showNoAppLoadedWarning();

        void buildUi();
    }

    private ApplicationElement currentApplication;
    private Version currentVersion;

    public void onBuildApp() {
        view.buildUi();
        eventBus.changeTopComponent(view);
    }

    public void onLoadApplication(ApplicationElement application, Version version) {
        currentApplication = application;
        currentVersion = version;
        view.updateButtonState();
    }

    public void showOpenApplications() {
        eventBus.showOpenApplications();
        view.updateButtonState();
    }

    public void saveApplication() {
        eventBus.saveApplication();
    }

    public void saveApplicationAsXml() {
        eventBus.saveApplicationAsXML();
    }

    public boolean isApplicationPresent() {
        return currentApplication != null;
    }

    public void saveApplicationAs() {
        if (currentApplication == null) {
            view.showNoAppLoadedWarning();
            return;
        }
        ApplicationBasicInfo appInfo = new ApplicationBasicInfo(currentApplication, currentVersion);
        eventBus.getAppInfoForSaveAs(appInfo, new Callback<ApplicationBasicInfo, Throwable>() {
            @Override
            public void onFailure(Throwable reason) {
            }

            @Override
            public void onSuccess(ApplicationBasicInfo result) {
                eventBus.saveApplicationAs(result.getVersion());
            }
        });
    }

    public void startCompareApplications() {
        if (currentApplication != null && currentVersion != null) {
            eventBus.startCompareApplication(currentApplication, currentVersion);
        } else {
            eventBus.startCompareApplications();
        }
    }

    public void loadApplicationFromXML() {
        eventBus.loadApplicationFromXML();
    }

    public void createNewApplication() {
        eventBus.getAppInfoForNew(new Callback<ApplicationBasicInfo, Throwable>() {
            @Override
            public void onFailure(Throwable reason) {
            }

            @Override
            public void onSuccess(ApplicationBasicInfo result) {
                eventBus.newApplication(result);
            }
        });
    }

    public void runPackageCreation() {
        final ApplicationStoreData data = new ApplicationStoreData(currentApplication, currentVersion);
        EditorDataService.Util.getDataService().createPackage(data, new AsyncCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                Window.open(GWT.getHostPageBaseURL() + "package", "_blank", "");
            }

            @Override
            public void onFailure(Throwable caught) {
                InfoHelper.throwInfo("toolbar-run-package", caught);
            }
        });
    }

    public void runApplication() {
        ApplicationStoreData data = new ApplicationStoreData(currentApplication, currentVersion);
        HRefUtil.openNewApplicationTab(data);
    }

    public void getIcons() {
        EditorDataService.Util.getDataService().getIcons(new AsyncCallback<List<String>>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(List<String> result) {
                System.out.println(result.toString());
                System.out.println("Result OK");

            }
        });
    }
}
