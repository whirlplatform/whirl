package org.whirlplatform.editor.client.view;

import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;
import org.whirlplatform.component.client.utils.InfoHelper;
import org.whirlplatform.component.client.utils.ProgressHelper;
import org.whirlplatform.editor.client.presenter.AppBasicInfoPresenter;
import org.whirlplatform.editor.client.presenter.AppBasicInfoPresenter.IAppBasicInfoView;
import org.whirlplatform.editor.client.validator.ValidatorUtil;
import org.whirlplatform.editor.client.view.widget.LocaleField;
import org.whirlplatform.editor.client.view.widget.VersionField;
import org.whirlplatform.editor.client.view.widget.WidgetUtil;
import org.whirlplatform.editor.shared.i18n.EditorMessage;
import org.whirlplatform.editor.shared.metadata.ApplicationBasicInfo;
import org.whirlplatform.meta.shared.Version;
import org.whirlplatform.meta.shared.editor.LocaleElement;

/**
 * Ввод данных приложения
 */
public class AppBasicInfoView extends Window implements IAppBasicInfoView {
    protected static final int MIN_WIDTH = 400;
    protected static final int MIN_HEIGHT = 280;
    private static final String OK = "OK";
    private static final String CLOSE = EditorMessage.Util.MESSAGE.close();
    private static final String WARN = EditorMessage.Util.MESSAGE.warn();
    private static final String WARN_MESSAGE =
            EditorMessage.Util.MESSAGE.warn_input_data_is_not_valid();
    private static final String WARN_EXISTS = EditorMessage.Util.MESSAGE.new_application_exists();
    private static final String NAME = EditorMessage.Util.MESSAGE.new_application_name();
    private static final String CODE = EditorMessage.Util.MESSAGE.new_application_code();
    private static final String TITLE = EditorMessage.Util.MESSAGE.new_application_title();
    //    private static final String URL = EditorMessage.Util.MESSAGE.new_application_guest();
    private static final String LOCALE = EditorMessage.Util.MESSAGE.new_application_locale();
    private static final String VERSION = EditorMessage.Util.MESSAGE.new_application_version();
    private static final String TITLE_NEW_APP =
            EditorMessage.Util.MESSAGE.toolbar_creating_application();
    private static final String TITLE_SAVE_AS = EditorMessage.Util.MESSAGE.save_as();
    private AppBasicInfoPresenter presenter;
    private TextField name;
    private TextField title;
    private TextField code;
    //    private TextField url;
    private LocaleField locale;
    private VersionField version;

    public AppBasicInfoView() {
        this.setResizable(true);
        this.setModal(true);
        this.setMinWidth(MIN_WIDTH);
        this.setMinHeight(MIN_HEIGHT);
        TextButton okButton = new TextButton(OK);
        okButton.addSelectHandler(new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                if (!hasValidationErrors()) {
                    if (presenter.exists(code.getValue(), version.getValue())) {
                        InfoHelper.warning("application-exists", WARN, WARN_EXISTS);
                    } else {
                        presenter.returnResult(createInfo());
                        clearFields();
                        hide();
                    }
                } else {
                    InfoHelper.warning("input-data-invalid", WARN, WARN_MESSAGE);
                }
            }
        });
        TextButton closeButton = new TextButton(CLOSE);
        closeButton.addSelectHandler(new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                clearFields();
                hide();
                presenter.returnFailure();
            }
        });
        addButton(okButton);
        addButton(closeButton);
    }

    private Widget createEditor() {
        VerticalLayoutContainer container = new VerticalLayoutContainer();
        name = WidgetUtil.createTextField(false);
//        name.addValidator(ValidatorUtil.createNameValidator());
        code = WidgetUtil.createTextField(false);
        code.addValidator(ValidatorUtil.createCodeValidator());
        title = WidgetUtil.createTextField(false);
//        url = WidgetUtil.createTextField(true);
        locale = new LocaleField();
        version = new VersionField();
        container.add(new FieldLabel(name, NAME),
                new VerticalLayoutData(1, -1, new Margins(5, 5, 0, 5)));
        container.add(new FieldLabel(code, CODE),
                new VerticalLayoutData(1, -1, new Margins(0, 5, 0, 5)));
        container.add(new FieldLabel(title, TITLE),
                new VerticalLayoutData(1, -1, new Margins(0, 5, 0, 5)));
//        container.add(new FieldLabel(url, URL), new VerticalLayoutData(1, -1, new Margins(0, 5, 0, 5)));
        container.add(new FieldLabel(locale, LOCALE),
                new VerticalLayoutData(1, -1, new Margins(0, 5, 0, 5)));
        container.add(new FieldLabel(version, VERSION),
                new VerticalLayoutData(1, -1, new Margins(10, 5, 0, 5)));
        container.setBorders(false);
        return container;
    }

    private boolean hasValidationErrors() {
        return !(name.validate() && /*url.validate() &&*/ code.validate() && title.validate()
                && version.isValid() & locale.isValid());
    }

    private ApplicationBasicInfo createInfo() {
        ApplicationBasicInfo appInfo = new ApplicationBasicInfo();
        appInfo.setCode(code.getValue());
        appInfo.setName(name.getValue());
        appInfo.setTitle(title.getValue());
//        appInfo.setUrl(url.getValue());
        appInfo.setLocale(locale.getValue());
        appInfo.setVersion(version.getValue());
        return appInfo;
    }

    @Override
    public AppBasicInfoPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void setPresenter(AppBasicInfoPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showError(Throwable caught) {
        ProgressHelper.hide();
        InfoHelper.throwInfo("show-error", caught);
    }

    @Override
    public void showAsNew() {
        setWidget(createEditor());
        setWindowTitle(TITLE_NEW_APP);
        setKeyFieldsEnabled(true);
        clearFields();
        show();
    }

    @Override
    public void showAsSaveAs(ApplicationBasicInfo appInfo) {
        setWidget(createEditor());
        setWindowTitle(TITLE_SAVE_AS);
        setKeyFieldsEnabled(false);
        if (appInfo != null) {
            code.setValue(appInfo.getCode());
            name.setValue(appInfo.getName());
            title.setValue(appInfo.getTitle());
//            url.setValue(appInfo.getUrl());
            locale.setValue(appInfo.getLocale());
            version.setValue(appInfo.getVersion());
        } else {
            clearFields();
        }
        show();
    }

    private void setWindowTitle(String title) {
        this.getHeader().setText(title);
    }

    private void setKeyFieldsEnabled(boolean value) {
        title.setEnabled(value);
        code.setEnabled(value);
        name.setEnabled(value);
        locale.setEnabled(value);
//        url.setEnabled(value);
    }

    public void clearFields() {
        code.setValue("");
        name.setValue("");
        title.setValue("");
//        url.setValue("");
        locale.setValue(new LocaleElement("ru", ""));
        version.setValue(Version.create(0, 0, 1));
    }
}
