package org.whirlplatform.editor.client.view;

import com.google.gwt.user.client.Event;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer.HBoxLayoutAlign;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer.VBoxLayoutAlign;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent.DialogHideHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.info.Info;
import java.util.ArrayList;
import java.util.List;
import org.whirlplatform.component.client.utils.InfoHelper;
import org.whirlplatform.component.client.utils.ProgressHelper;
import org.whirlplatform.editor.client.presenter.compare.CompareApplicationsPresenter;
import org.whirlplatform.editor.client.presenter.compare.CompareApplicationsPresenter.ComparisonState;
import org.whirlplatform.editor.client.presenter.compare.CompareApplicationsPresenter.ICompareApplicationsView;
import org.whirlplatform.editor.client.tree.ComparableAppTree;
import org.whirlplatform.editor.client.tree.ComparableLeftAppTreeWidget;
import org.whirlplatform.editor.client.tree.ComparableRightAppTreeWidget;
import org.whirlplatform.editor.client.tree.toolbar.ComparableAppTreeToolBar;
import org.whirlplatform.editor.client.view.widget.WidgetUtil;
import org.whirlplatform.editor.shared.i18n.EditorMessage;
import org.whirlplatform.editor.shared.merge.ChangeUnit;
import org.whirlplatform.meta.shared.Version;
import org.whirlplatform.meta.shared.editor.AbstractElement;
import org.whirlplatform.meta.shared.editor.ApplicationElement;

/**
 * Сравнение приложений
 */
public class CompareApplicationsView extends Window implements ICompareApplicationsView {

    private static final String MERGING = EditorMessage.Util.MESSAGE.action_merge_selection();
    private static final String COMPARING =
        EditorMessage.Util.MESSAGE.action_compare_applications();
    private static final String EXIT_BUTTON = EditorMessage.Util.MESSAGE.exit();
    private static final String APPLY_BUTTON = EditorMessage.Util.MESSAGE.apply();
    private static final String APPLY_TITLE = EditorMessage.Util.MESSAGE.compare_apps_apply_title();
    private static final String APPLY_TEXT = EditorMessage.Util.MESSAGE.compare_apps_apply_text();
    private static final String MERGE_BUTTON = EditorMessage.Util.MESSAGE.merge();
    private static final String WINDOW_TITLE = EditorMessage.Util.MESSAGE.compare_apps_title();
    private static final int WINDOW_WIDTH = 1024;
    private static final int WINDOW_HEIGHT = WidgetUtil.MAX_WINDOW_HEIGHT;
    private static final int GAP_BETWEEN_TREES = 40;
    private static final int TREE_WIDTH = (WINDOW_WIDTH - GAP_BETWEEN_TREES) / 2;
    private final TextButton mergeButton;
    private final TextButton applyButton;
    private CompareApplicationsPresenter presenter;
    private ComparableAppTree leftTree;
    private ComparableAppTree rightTree;
    private AppTreeContainer leftContainer;
    private AppTreeContainer rightContainer;

    public CompareApplicationsView() {
        super();
        setHeading(WINDOW_TITLE);
        setWidth(WINDOW_WIDTH);
        setHeight(WINDOW_HEIGHT);
        this.setClosable(false);
        HBoxLayoutContainer mainContainer = new HBoxLayoutContainer();
        mainContainer.setHBoxLayoutAlign(HBoxLayoutAlign.STRETCH);
        leftTree = new ComparableLeftAppTreeWidget() {
            @Override
            protected void onCheckClick(Event event, TreeNode<AbstractElement> node) {
                super.onCheckClick(event, node);
                changeMergeButtonState();
            }
        };
        leftContainer = new AppTreeContainer(leftTree);
        leftContainer.setWidth(TREE_WIDTH);
        leftContainer.setOpenApplicationHandler(createOpenLeftHandler());
        leftContainer.setAdjustForFlexRemainder(true);
        mainContainer.add(leftContainer, WidgetUtil.noStretchLayout());

        VBoxLayoutContainer middle = new VBoxLayoutContainer();
        middle.setVBoxLayoutAlign(VBoxLayoutAlign.CENTER);
        middle.setWidth(GAP_BETWEEN_TREES);
        mainContainer.add(middle, WidgetUtil.flexLayout());

        rightTree = new ComparableRightAppTreeWidget() {
            @Override
            protected void onCheckClick(Event event, TreeNode<AbstractElement> node) {
                super.onCheckClick(event, node);
                changeMergeButtonState();
            }
        };
        rightContainer = new AppTreeContainer(rightTree);
        rightContainer.setWidth(TREE_WIDTH);
        rightContainer.setOpenApplicationHandler(createOpenRightHandler());
        mainContainer.add(rightContainer, WidgetUtil.noStretchLayout());

        this.add(mainContainer);
        mergeButton = new TextButton(MERGE_BUTTON, createMergeButtonHandler());
        applyButton = new TextButton(APPLY_BUTTON, createApplyButtonHandler());
        this.addButton(applyButton);
        this.addButton(mergeButton);
        this.addButton(new TextButton(EXIT_BUTTON, createExitButtonHandler()));
    }

    @Override
    public void loadLeftApplication(ApplicationElement application, Version version) {
        presenter.setCurrentTree(null);
        presenter.setCurrentTree(leftTree);
        // ждем инициализации текущего дерева в презентере
        while (presenter.getCurrentTree() != leftTree) {
        }
        leftTree.loadApplication(application, version, presenter);
        leftContainer.updateTitle(false);
    }

    @Override
    public void loadRightApplication(ApplicationElement application, Version version) {
        presenter.setCurrentTree(null);
        presenter.setCurrentTree(rightTree);
        while (presenter.getCurrentTree() != rightTree) {
        }
        rightTree.loadApplication(application, version, presenter);
        rightContainer.updateTitle(false);
    }

    @Override
    public void setChanges(List<ChangeUnit> changes) {
        leftTree.setChanges(changes);
        rightTree.setChanges(changes);
    }

    private SelectHandler createApplyButtonHandler() {
        return new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                ConfirmMessageBox confirm = new ConfirmMessageBox(APPLY_TITLE, APPLY_TEXT);
                confirm.addDialogHideHandler(new DialogHideHandler() {
                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton() == PredefinedButton.YES) {
                            presenter.applyLeftApplication();
                        }
                    }
                });
                confirm.show();
            }
        };
    }

    private SelectHandler createExitButtonHandler() {
        return new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                clearAll();
                getPresenter().exit();
            }
        };
    }

    private SelectHandler createMergeButtonHandler() {
        return new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                ProgressHelper.show(MERGING);
                presenter.mergeApplications();
            }
        };
    }

    private SelectHandler createOpenLeftHandler() {
        return new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                presenter.openLeftApplication();
            }
        };
    }

    private SelectHandler createOpenRightHandler() {
        return new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                presenter.openRightApplication();
            }
        };
    }

    @Override
    public CompareApplicationsPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void setPresenter(CompareApplicationsPresenter presenter) {
        this.presenter = presenter;
    }

    private void changeMergeButtonState() {
        if (leftTree.getCheckedSelection().size() > 0
            || rightTree.getCheckedSelection().size() > 0) {
            mergeButton.setEnabled(true);
        } else {
            mergeButton.setEnabled(false);
        }
    }

    @Override
    public void changeState(ComparisonState state) {
        mergeButton.setEnabled(false);
        switch (state) {
            case Start:
                applyButton.setEnabled(false);
                clearAll();
                show();
                break;
            case LeftLoaded:
                applyButton.setEnabled(false);
                leftContainer.updateTitle(false);
                break;
            case RightLoaded:
                applyButton.setEnabled(false);
                rightContainer.updateTitle(false);
                break;
            case Initialised:
                ProgressHelper.show(COMPARING);
                getPresenter().compareApplications();
                break;
            case Compared:
                ProgressHelper.hide();
                applyButton.setEnabled(false);
                break;
            case Merged:
                ProgressHelper.hide();
                leftContainer.updateTitle(true);
                applyButton.setEnabled(true);
                ProgressHelper.show(COMPARING);
                getPresenter().compareApplications();
                break;
            case ComparedAfterMerge:
                applyButton.setEnabled(true);
                break;
            case Finish:
                hide();
                break;
            default:
                break;
        }
    }

    @Override
    public void clearAll() {
        leftTree.clearAll();
        rightTree.clearAll();
        leftContainer.clearTitle();
        rightContainer.clearTitle();
    }

    @Override
    public void showComparisonResult(int number) {
        ProgressHelper.hide();
        Info.display(WINDOW_TITLE, EditorMessage.Util.MESSAGE.compare_apps_compare_result(number));
    }

    @Override
    public List<ChangeUnit> getCheckedChanges() {
        List<ChangeUnit> result = new ArrayList<>();
        result.addAll(leftTree.getCheckedChangeUnits());
        result.addAll(rightTree.getCheckedChangeUnits());
        return result;
    }

    @Override
    public void showException(Throwable caught) {
        ProgressHelper.hide();
        InfoHelper.throwInfo("show-error", caught);
    }

    class AppTreeContainer extends VBoxLayoutContainer {
        private final ComparableAppTree appTree;
        private ComparableAppTreeToolBar toolbar;

        AppTreeContainer(final ComparableAppTree appTree) {
            super();
            this.appTree = appTree;
            this.toolbar = new ComparableAppTreeToolBar();
            this.toolbar.setAppTree(appTree);
            this.setVBoxLayoutAlign(VBoxLayoutAlign.STRETCH);
            this.add(toolbar, WidgetUtil.noStretchLayout());
            this.add(appTree.asWidget(), WidgetUtil.flexLayout());
        }

        public void setOpenApplicationHandler(SelectHandler handler) {
            toolbar.addOpenButtonHandler(handler);
        }

        public ComparableAppTree getTree() {
            return appTree;
        }

        public void updateTitle(boolean applicationChanged) {
            toolbar.setStatusAsChanged(applicationChanged);
        }

        public void clearTitle() {
            toolbar.clearStatus();
        }
    }
}
