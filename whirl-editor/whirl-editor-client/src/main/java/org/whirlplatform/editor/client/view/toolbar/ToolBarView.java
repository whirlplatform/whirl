package org.whirlplatform.editor.client.view.toolbar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.button.ToggleButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.event.SubmitCompleteEvent;
import com.sencha.gxt.widget.core.client.event.SubmitCompleteEvent.SubmitCompleteHandler;
import com.sencha.gxt.widget.core.client.form.FileUploadField;
import com.sencha.gxt.widget.core.client.form.FormPanel;
import com.sencha.gxt.widget.core.client.form.FormPanel.Encoding;
import com.sencha.gxt.widget.core.client.form.FormPanel.Method;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import com.sencha.gxt.widget.core.client.menu.SeparatorMenuItem;
import com.sencha.gxt.widget.core.client.toolbar.FillToolItem;
import com.sencha.gxt.widget.core.client.toolbar.LabelToolItem;
import com.sencha.gxt.widget.core.client.toolbar.SeparatorToolItem;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;
import org.whirlplatform.component.client.utils.InfoHelper;
import org.whirlplatform.editor.client.image.ComponentBundle;
import org.whirlplatform.editor.client.presenter.ToolBarPresenter;
import org.whirlplatform.editor.client.presenter.ToolBarPresenter.IToolBarView;
import org.whirlplatform.editor.client.view.HelpDecorator;
import org.whirlplatform.editor.client.view.context.ContextTextButton;
import org.whirlplatform.editor.client.view.widget.DisplayCurrentUserWidget;
import org.whirlplatform.editor.shared.i18n.EditorMessage;


/**
 * Основная панель инструментов.
 */
public class ToolBarView extends ToolBar implements IToolBarView {
    private static final int SPACING = 5;
    private static final int IMPORT_HEIGHT = 150;
    private static final int IMPORT_WIDTH = 400;
    private static final String IMPORT_BUTTON = EditorMessage.Util.MESSAGE.load();
    private static final String IMPORT_TITLE = EditorMessage.Util.MESSAGE.toolbar_load_xml();
    private static final String IMPORT_CLOSE = EditorMessage.Util.MESSAGE.close();

    private static final String ERROR = EditorMessage.Util.MESSAGE.error();
    private static final String ERROR_IMPORT =
        EditorMessage.Util.MESSAGE.error_import_application();
    private static final String WARN = EditorMessage.Util.MESSAGE.warn();
    private static final String WARN_NO_CURR_APP = "No applications loaded!";

    private ToolBarPresenter presenter;
    private ContextTextButton<ToolBarPresenter> newButton;
    private ContextTextButton<ToolBarPresenter> openButton;
    private ContextTextButton<ToolBarView> openXmlButton;
    private ContextTextButton<ToolBarPresenter> saveButton;
    private ContextTextButton<ToolBarPresenter> saveAsButton;
    private ContextTextButton<ToolBarPresenter> saveXmlButton;
    private ContextTextButton<ToolBarPresenter> packageButton;
    private ContextTextButton<ToolBarPresenter> compareButton;
    private Window importWindow;
    private FormPanel importForm;

    public void buildUi() {
        setHorizontalSpacing(SPACING);
        setLayoutData(new VerticalLayoutData(1, -1));
        newButton = new ToolbarNewButton(getPresenter());
        openButton = new ToolbarOpenButton(getPresenter());
        openXmlButton = new ToolbarOpenXmlButton(this);
        saveButton = new ToolbarSaveButton(getPresenter());
        saveAsButton = new ToolbarSaveAsButton(getPresenter());
        saveXmlButton = new ToolbarSaveXmlButton(getPresenter());
        packageButton = new ToolbarPackageButton(getPresenter());
        compareButton = new ToolbarCompareButton(getPresenter());
        add(newButton.asTextButton());
        add(openButton.asTextButton());
        //        add(openXmlButton.asTextButton());
        add(new SeparatorToolItem());
        add(saveButton.asTextButton());
        add(saveAsButton.asTextButton());
        //        add(saveXmlButton.asTextButton());
        //        add(packageButton.asTextButton());
        //        add(compareButton.asTextButton());
        add(new SeparatorToolItem());
        add(new ToolBarShowIconsButton(getPresenter()));
        add(createHelpMenu());
        add(createHelpToggleButton());
        add(new FillToolItem());
        add(new DisplayCurrentUserWidget());
        add(new LabelToolItem(SafeHtmlUtils.fromTrustedString("&nbsp;")));
        updateButtonState();
    }

    public TextButton createHelpMenu() {
        TextButton button = new TextButton();
        MenuItem js = new MenuItem("JavaScript API");
        js.setIcon(ComponentBundle.INSTANCE.helpJS());
        MenuItem db = new MenuItem("Database API");
        db.setIcon(ComponentBundle.INSTANCE.helpDB());
        js.addSelectionHandler(event -> com.google.gwt.user.client.Window.open("api/js/jsdoc", "_blank", ""));
        db.addSelectionHandler(event -> com.google.gwt.user.client.Window.open("api/db/dbdoc", "_blank", ""));
        Menu m = new Menu();
        m.add(js);
        m.add(new SeparatorMenuItem());
        m.add(db);

        button.setMenu(m);
        button.setIcon(ComponentBundle.INSTANCE.helpApi());
        button.setToolTip(EditorMessage.Util.MESSAGE.help_js_api());

        return button;
    }

    public static ToggleButton toggleButton = new ToggleButton();

    private ToggleButton createHelpToggleButton() {
        toggleButton = new ToggleButton();
        toggleButton.setIcon(ComponentBundle.INSTANCE.helpTips());
        toggleButton.addSelectHandler(event -> disableEnableTips(toggleButton.getValue()));
        return toggleButton;
    }

    public static void disableEnableTips(boolean isDepressed) {

        if (isDepressed) {
            HelpDecorator.enableTips();
        } else {
            HelpDecorator.disableTips();
        }
    }

    private FormPanel createImportForm() {
        if (importForm == null) {
            importForm = new FormPanel();
            importForm.setEncoding(Encoding.MULTIPART);
            importForm.setMethod(Method.POST);
            importForm.setAction(GWT.getHostPageBaseURL() + "import");
            importForm.addSubmitCompleteHandler(new SubmitCompleteHandler() {
                @Override
                public void onSubmitComplete(SubmitCompleteEvent event) {
                    if (event.getResults().contains("OK")) {
                        closeImportWindow();
                        presenter.loadApplicationFromXML();
                    } else {
                        InfoHelper.error("toolbar-view-import-form", ERROR, ERROR_IMPORT);
                    }
                }
            });
        }
        return importForm;
    }

    private void closeImportWindow() {
        if (importWindow != null) {
            importWindow.hide();
        }
    }

    public void showImportWindow() {
        final FormPanel form = createImportForm();
        final FileUploadField field = new FileUploadField();
        field.setName("file");
        importWindow = new Window();
        importWindow.getHeader().setText(IMPORT_TITLE);
        importWindow.getHeader().setIcon(ComponentBundle.INSTANCE.openXml());
        importWindow.setResizable(false);
        importWindow.setHeight(IMPORT_HEIGHT);
        importWindow.setWidth(IMPORT_WIDTH);
        TextButton importButton = new TextButton(IMPORT_BUTTON);
        importButton.addSelectHandler(new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                if (field.getValue() != null && !"".equals(field.getValue())) {
                    form.submit();
                }
            }
        });
        importButton.setIcon(ComponentBundle.INSTANCE.load());
        TextButton closeButton = new TextButton(IMPORT_CLOSE);
        closeButton.addSelectHandler(new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                importWindow.hide();
            }
        });
        closeButton.setIcon(ComponentBundle.INSTANCE.cancel());
        importWindow.setWidget(form);
        form.setWidget(field);
        importWindow.addButton(importButton);
        importWindow.addButton(closeButton);
        importWindow.show();
    }

    @Override
    public ToolBarPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void setPresenter(ToolBarPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void updateButtonState() {
        saveAsButton.updateState();
        saveXmlButton.updateState();
        saveButton.updateState();
        packageButton.updateState();
    }

    @Override
    public void showNoAppLoadedWarning() {
        InfoHelper.warning("toolbar-view-show-no-app-loaded", WARN, WARN_NO_CURR_APP);
    }
}
