package org.whirlplatform.editor.client.presenter.compare;

import com.google.gwt.core.client.Callback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;
import com.mvp4g.client.view.ReverseViewInterface;
import com.sencha.gxt.widget.core.client.info.Info;
import java.util.Collection;
import java.util.List;
import org.whirlplatform.component.client.utils.InfoHelper;
import org.whirlplatform.editor.client.EditorEventBus;
import org.whirlplatform.editor.client.tree.AppTreePresenter;
import org.whirlplatform.editor.client.tree.ComparableAppTree;
import org.whirlplatform.editor.client.view.CompareApplicationsView;
import org.whirlplatform.editor.shared.EditorDataService;
import org.whirlplatform.editor.shared.OpenResult;
import org.whirlplatform.editor.shared.merge.ApplicationsDiff;
import org.whirlplatform.editor.shared.merge.ChangeUnit;
import org.whirlplatform.meta.shared.ApplicationStoreData;
import org.whirlplatform.meta.shared.Version;
import org.whirlplatform.meta.shared.editor.AbstractElement;
import org.whirlplatform.meta.shared.editor.ApplicationElement;
import org.whirlplatform.meta.shared.editor.RightType;

/**
 * Сравнение приложений
 */
@Presenter(view = CompareApplicationsView.class)
public class CompareApplicationsPresenter
    extends BasePresenter<CompareApplicationsPresenter.ICompareApplicationsView, EditorEventBus>
    implements AppTreePresenter {

    private ComparisonState currentState;
    private ComparableAppTree currentTree;
    private ApplicationElement leftApp;
    private ApplicationElement rightApp;
    private Version leftVersion;
    private Version rightVersion;
    private ApplicationsDiff appDiff;

    private void changeState(ComparisonState state) {
        if (this.currentState != state) {
            switch (state) {
                case Start:
                    currentState = state;
                    clearAll();
                    view.changeState(ComparisonState.Start);
                    break;
                case LeftLoaded:
                    if (currentState == ComparisonState.Start) {
                        currentState = state;
                        view.changeState(state);
                    } else if (currentState == ComparisonState.RightLoaded) {
                        view.changeState(ComparisonState.LeftLoaded);
                        changeState(ComparisonState.Initialised);
                    } else if (currentState == ComparisonState.LeftLoaded) {
                        view.changeState(ComparisonState.LeftLoaded);
                    } else {
                        changeState(ComparisonState.Initialised);
                    }
                    break;
                case RightLoaded:
                    if (currentState == ComparisonState.Start) {
                        currentState = state;
                        view.changeState(state);
                    } else if (currentState == ComparisonState.LeftLoaded) {
                        view.changeState(ComparisonState.RightLoaded);
                        changeState(ComparisonState.Initialised);
                    } else {
                        changeState(ComparisonState.Initialised);
                    }
                    break;
                case Initialised:
                    if (currentState != ComparisonState.Start) {
                        currentState = state;
                        view.changeState(state);
                    } else {
                        showWrongStateChange(state);
                    }
                    break;
                case Compared:
                    if (currentState == ComparisonState.Initialised) {
                        currentState = state;
                        view.changeState(state);
                    } else if (currentState == ComparisonState.Merged
                        || currentState == ComparisonState.ComparedAfterMerge) {
                        changeState(ComparisonState.ComparedAfterMerge);
                    } else {
                        showWrongStateChange(state);
                    }
                    break;
                case Merged:
                    if (currentState == ComparisonState.Compared
                        || currentState == ComparisonState.ComparedAfterMerge) {
                        currentState = state;
                        view.loadLeftApplication(leftApp, leftVersion);
                        view.setChanges(appDiff.getChanges());
                        view.changeState(state);
                    } else {
                        showWrongStateChange(state);
                    }
                    break;
                case ComparedAfterMerge:
                    if (currentState == ComparisonState.Merged) {
                        currentState = state;
                        view.changeState(state);
                    } else {
                        showWrongStateChange(state);
                    }
                    break;
                case Finish:
                    clearAll();
                    currentState = state;
                    view.changeState(state);
                    break;
                default:
                    break;
            }
        }
    }

    // Entry point
    public void onStartCompareApplications() {
        changeState(ComparisonState.Start);
    }

    public void onStartCompareApplication(ApplicationElement application, Version version) {
        onStartCompareApplications();
        onOpenLeftApplication(application, version);
    }

    public ComparableAppTree getCurrentTree() {
        return currentTree;
    }

    public void setCurrentTree(ComparableAppTree currentTree) {
        this.currentTree = currentTree;
    }

    private void clearAll() {
        currentTree = null;
        leftApp = null;
        rightApp = null;
        leftVersion = null;
        rightVersion = null;
        appDiff = null;
    }

    public void mergeApplications() {
        if (currentState != ComparisonState.Compared
            && currentState != ComparisonState.ComparedAfterMerge) {
            return;
        }
        if (appDiff.getChanges().size() == 0) {
            //TODO i18n
            InfoHelper.info("merge-impossible", "Merge impossible!", "No changes detected!");
            return;
        }
        List<ChangeUnit> changes = view.getCheckedChanges();
        if (changes.size() == 0) {
            //TODO i18n
            InfoHelper.info("merge-impossible", "Merge impossible!",
                "You should select at least one element.");
            return;
        }
        ApplicationsDiff diff = new ApplicationsDiff(changes, leftApp, rightApp);
        diff.setLeftStoreData(createAppStoreData(leftApp, leftVersion));
        diff.setRightStoreData(createAppStoreData(rightApp, rightVersion));
        EditorDataService.Util.getDataService()
            .merge(diff, new AsyncCallback<ApplicationElement>() {
                @Override
                public void onFailure(Throwable caught) {
                    appDiff = null;
                    changeState(ComparisonState.Initialised);
                    view.showException(caught);
                }

                @Override
                public void onSuccess(ApplicationElement result) {
                    leftApp = result;
                    changeState(ComparisonState.Merged);
                }
            });
    }

    private void showWrongStateChange(ComparisonState state) {
        //TODO i18n
        StringBuilder sb = new StringBuilder("Forbidden state change ");
        sb.append(currentState.toString()).append(" -> ").append(state.toString());
        InfoHelper.error("change-state", "Cnange state Error", sb.toString());
    }

    public void openLeftApplication() {
        eventBus.openApplicationCallback(new Callback<OpenResult, Throwable>() {
            @Override
            public void onSuccess(OpenResult result) {
                onOpenLeftApplication(result.getApplication(), result.getVersion());
            }

            @Override
            public void onFailure(Throwable reason) {
            }
        });
    }

    public void openRightApplication() {
        eventBus.openApplicationCallback(new Callback<OpenResult, Throwable>() {
            @Override
            public void onSuccess(OpenResult result) {
                onOpenRightApplication(result.getApplication(), result.getVersion());
            }

            @Override
            public void onFailure(Throwable reason) {
            }
        });
    }

    public void exit() {
        changeState(ComparisonState.Finish);
    }

    public void applyLeftApplication() {
        eventBus.loadApplication(leftApp, leftVersion);
        changeState(ComparisonState.Finish);
    }

    public void compareApplications() {
        if (!comparisonAllowed()) {
            return;
        }
        EditorDataService.Util.getDataService()
            .diff(leftApp, rightApp, new AsyncCallback<ApplicationsDiff>() {
                @Override
                public void onFailure(Throwable caught) {
                    appDiff = null;
                    view.showException(caught);
                }

                @Override
                public void onSuccess(ApplicationsDiff result) {
                    int numberOfChanges = result.getChanges().size();
                    appDiff = result;
                    view.setChanges(result.getChanges());
                    changeState(ComparisonState.Compared);
                    view.showComparisonResult(numberOfChanges);
                }
            });
    }

    private boolean comparisonAllowed() {
        return (currentState != ComparisonState.Start && currentState != ComparisonState.LeftLoaded
            && currentState != ComparisonState.RightLoaded);
    }

    private ApplicationStoreData createAppStoreData(final ApplicationElement app,
                                                    final Version version) {
        ApplicationStoreData result = new ApplicationStoreData(app.getId(), app.getName());
        result.setCode(app.getCode());
        result.setVersion(version);
        return result;
    }

    @Override
    public void riseRemoveElement(AbstractElement parent, AbstractElement element,
                                  boolean showDialog) {
        Info.display("Oops!", "riseRemoveElement - Illegal operation");
    }

    public void onOpenLeftApplication(ApplicationElement application, Version version) {
        leftApp = application;
        leftVersion = version;
        view.loadLeftApplication(application, version);
        changeState(ComparisonState.LeftLoaded);
    }

    public void onOpenRightApplication(ApplicationElement application, Version version) {
        rightApp = application;
        rightVersion = version;
        view.loadRightApplication(application, version);
        changeState(ComparisonState.RightLoaded);
    }

    @Override
    public void riseAddElement(AbstractElement parent, AbstractElement element) {
        Info.display("Oops!", "riseAddElement - Illegal operation");
    }

    @Override
    public void riseAddElementUI(AbstractElement parent, AbstractElement element) {
        currentTree.doAddElementUI(parent, element);
    }

    @Override
    public void riseRemoveElementUI(AbstractElement parent, AbstractElement element) {
        Info.display("Oops!", "riseRemoveElementUI - Illegal operation");
    }

    @Override
    public void riseEditRights(Collection<? extends AbstractElement> elements,
                               Collection<RightType> rightTypes) {
        Info.display("Oops!", "riseEditRights - Illegal operation");
    }

    @Override
    public void riseShowOpenApplicationsCallback(
        Callback<ApplicationStoreData, Throwable> callback) {
        Info.display("Oops!", "riseShowOpenApplicationsCallback - Illegal operation");
    }

    @Override
    public void riseOpenElement(AbstractElement element) {
        Info.display("Oops!", "riseOpenElement - Illegal operation");
    }

    @Override
    public void riseCloseOpenApplication() {
        Info.display("Oops!", "riseCloseOpenApplication - Illegal operation");
    }

    public enum ComparisonState {
        Start, LeftLoaded, RightLoaded, Initialised, Compared, Merged, ComparedAfterMerge, Finish
    }

    public interface ICompareApplicationsView
        extends IsWidget, ReverseViewInterface<CompareApplicationsPresenter> {
        void show();

        void showComparisonResult(int number);

        void clearAll();

        void changeState(ComparisonState state);

        List<ChangeUnit> getCheckedChanges();

        void loadLeftApplication(ApplicationElement application, Version version);

        void loadRightApplication(ApplicationElement application, Version version);

        void setChanges(List<ChangeUnit> changes);

        void showException(Throwable caught);
    }
}
