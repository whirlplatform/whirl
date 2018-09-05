package org.whirlplatform.editor.client.view.allapps;

import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.CellDoubleClickEvent;
import com.sencha.gxt.widget.core.client.event.CellDoubleClickEvent.CellDoubleClickHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;
import com.sencha.gxt.widget.core.client.toolbar.FillToolItem;
import org.whirlplatform.component.client.utils.InfoHelper;
import org.whirlplatform.component.client.utils.ProgressHelper;
import org.whirlplatform.editor.client.image.ComponentBundle;
import org.whirlplatform.editor.client.presenter.AllApplicationsPresenter;
import org.whirlplatform.editor.client.presenter.AllApplicationsPresenter.IAllApplicationsView;
import org.whirlplatform.editor.client.view.widget.WidgetUtil;
import org.whirlplatform.editor.shared.i18n.EditorMessage;
import org.whirlplatform.meta.shared.ApplicationStoreData;

import java.util.Collection;

/**
 * Отображение перечня доступных приложений
 */
public class AllApplicationsView extends Window implements IAllApplicationsView {
    // Operations
    private final static String LOADING = EditorMessage.Util.MESSAGE.action_load_application();
    private static final String RETRIEVING_APPS = EditorMessage.Util.MESSAGE.all_applications_retrieving_apps();
    // Button titles
    private static final String REMOVE = EditorMessage.Util.MESSAGE.remove();
    private static final String PACKAGE_TITLE = EditorMessage.Util.MESSAGE.toolbar_package();
    private static final String LOAD_TITLE = EditorMessage.Util.MESSAGE.load();
    private static final String CLOSE_TITLE = EditorMessage.Util.MESSAGE.all_applications_close();
    private static final String RUN_TITLE = EditorMessage.Util.MESSAGE.run();
    // The main window
    private final static int WINDOW_WIDTH = ApplicationsTreeGrid.estimatedWidth() + 20;
    private final static int WINDOW_HEIGHT = WidgetUtil.MAX_WINDOW_HEIGHT;
    private final static String WINDOW_HEADER = EditorMessage.Util.MESSAGE.all_applications_header();


    private AllApplicationsPresenter presenter;
    private ApplicationsTreeGrid tree;

    private TextButton removeButton;
    private TextButton packageButton;
    private TextButton loadButton;
    private TextButton closeButton;
    private TextButton runButton;

    private AllApplicationsContextMenu contextMenu;

    public AllApplicationsView() {
        getHeader().setText(WINDOW_HEADER);
        getHeader().setIcon(ComponentBundle.INSTANCE.open());
        setWidth(WINDOW_WIDTH);
        setHeight(WINDOW_HEIGHT);
        tree = new ApplicationsTreeGrid();
        tree.getSelectionModel().addSelectionChangedHandler(new SelectionChangedHandler<ApplicationStoreData>() {
            @Override
            public void onSelectionChanged(SelectionChangedEvent<ApplicationStoreData> event) {
                setButtonsState();
            }
        });
        tree.addCellDoubleClickHandler(new CellDoubleClickHandler() {
            @Override
            public void onCellClick(CellDoubleClickEvent event) {
                startApplicationEditing();
            }
        });
        contextMenu = new AllApplicationsContextMenu(this);
        tree.setContextMenu(contextMenu.asMenu());
        initButtons();
        initPanel();
    }

    @Override
    public void hide() {
        ProgressHelper.hide();
        super.hide();
    }

    @Override
    public void setPresenter(AllApplicationsPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public AllApplicationsPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void loadApplications(Collection<ApplicationStoreData> data) {
        tree.loadData(data);
        ProgressHelper.hide();
    }

    @Override
    public void setButtonsState() {
        ApplicationStoreData selected = tree.getSelectedLeaf();
        if (selected == null) {
            removeButton.disable();
            loadButton.disable();
            packageButton.disable();
            runButton.disable();
        } else {
            removeButton.enable();
            loadButton.enable();
            packageButton.enable();
            runButton.enable();
        }
    }

    @Override
    public void showError(Throwable caught) {
        ProgressHelper.hide();
        InfoHelper.throwInfo("all-applications-view", caught);
    }

    @Override
    public void showLoadDataProgress() {
        ProgressHelper.hide();
        ProgressHelper.show(RETRIEVING_APPS);
    }

    private void initButtons() {
        removeButton = new TextButton(REMOVE, new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                //TODO remove
            }
        });
        packageButton = new TextButton(PACKAGE_TITLE, new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                startApplicationPackaging();
            }
        });
        packageButton.setIcon(ComponentBundle.INSTANCE.pack());
        loadButton = new TextButton(LOAD_TITLE, new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                startApplicationEditing();
            }
        });
        loadButton.setIcon(ComponentBundle.INSTANCE.load());
        closeButton = new TextButton(CLOSE_TITLE, new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                ProgressHelper.hide();
                tree.clear();
                hide();
            }
        });
        runButton = new TextButton(RUN_TITLE, new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                startApplicationRunning();
            }
        });
        runButton.setIcon(ComponentBundle.INSTANCE.run());
    }

    @Override
    public void startApplicationEditing() {
        ApplicationStoreData selected = tree.getSelectedLeaf();
        if (selected != null) {
            ProgressHelper.show(LOADING);
            getPresenter().fetchApplicationElement(selected);
        }
    }

    @Override
    public void startApplicationRunning() {
        ApplicationStoreData selected = tree.getSelectedLeaf();
        getPresenter().runApplication(selected);
    }

    @Override
    public void startApplicationPackaging() {
        ApplicationStoreData selected = tree.getSelectedLeaf();
        getPresenter().runPackageCreation(selected);
    }

    private void initPanel() {
        VerticalLayoutContainer container = new VerticalLayoutContainer();
        container.add(tree, new VerticalLayoutData(1, 1));
        setWidget(container);
        addButton(runButton);
        addButton(packageButton);
        addButton(loadButton);
        addButton(new FillToolItem());
        addButton(closeButton);
        setButtonsState();
    }

    @Override
    public ApplicationStoreData getSelectedLeaf() {
        return tree.getSelectedLeaf();
    }

    public ApplicationStoreData getSelectedFolder() {
        return tree.getSelectedFolder();
    }

    public void expandSelectedFolder() {
        tree.expandSelectedFolder();
    }

    public void collapseSelectedFolder() {
        tree.collapseSelectedFolder();
    }
}
