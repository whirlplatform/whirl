package org.whirlplatform.editor.client.view;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.ModalPanel;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FormPanel;
import com.sencha.gxt.widget.core.client.form.PasswordField;
import com.sencha.gxt.widget.core.client.form.TextField;
import org.whirlplatform.editor.client.presenter.LoginPresenter;
import org.whirlplatform.editor.client.util.EditorHelper;
import org.whirlplatform.editor.client.util.EditorHelper.ComponentCommand;
import org.whirlplatform.editor.shared.i18n.EditorMessage;

public class LoginView extends Window implements LoginPresenter.ILoginView {

    LoginPresenter presenter;

    TextField login = new TextField();
    PasswordField password = new PasswordField();
    TextButton submit = new TextButton(
            EditorMessage.Util.MESSAGE.login_submit());
    ModalPanel modal = ModalPanel.pop();

    @Override
    public void setPresenter(LoginPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public LoginPresenter getPresenter() {
        return presenter;
    }

    public LoginView() {

        setWidth(400);
        setClosable(false);
        setBlinkModal(true);
        setResizable(false);
        setDraggable(false);
        getHeader().setText(
                EditorMessage.Util.MESSAGE.login_fill_login_password());
        final FormPanel form = new FormPanel();
        form.setAction("javascript:;");
        VerticalLayoutContainer container = new VerticalLayoutContainer();
        container.setHeight(70);
        EditorHelper.attachSubmitHandler(new ComponentCommand() {

            @Override
            public void execute() {
                form.submit();
                submit.setEnabled(false);
                presenter.login(login.getText(), password.getText());
            }
        }, submit, login, password);

        FieldLabel loginField = new FieldLabel(login,
                EditorMessage.Util.MESSAGE.login_login());
        FieldLabel passwordField = new FieldLabel(password,
                EditorMessage.Util.MESSAGE.login_password());

        container
                .add(loginField, new VerticalLayoutData(1, -1, new Margins(5)));
        container.add(passwordField, new VerticalLayoutData(1, -1, new Margins(
                5)));
        addButton(submit);
        setButtonAlign(BoxLayoutPack.END);

        form.add(container);
        add(form);
    }

    @Override
    public void setButtonEnabled(boolean enabled) {
        submit.setEnabled(enabled);
    }

    @Override
    public void clear() {
        login.clear();
        password.clear();
        submit.setEnabled(true);
    }

    @Override
    public void show() {
        super.show();
        modal.show(this);
        forceLayout();

        Scheduler.get().scheduleDeferred(new ScheduledCommand() {
            @Override
            public void execute() {
                login.focus();
            }
        });
        setFocus(login);
    }

    private static void setFocus(final TextField element) {
        Scheduler.get().scheduleDeferred(new ScheduledCommand() {
            @Override
            public void execute() {
                element.focus();
            }
        });
    }


    @Override
    public void hide() {
        modal.hide();
        super.hide();
    }
}
