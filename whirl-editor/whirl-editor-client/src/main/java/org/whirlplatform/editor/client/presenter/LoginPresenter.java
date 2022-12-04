package org.whirlplatform.editor.client.presenter;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;
import com.mvp4g.client.view.ReverseViewInterface;
import com.sencha.gxt.widget.core.client.Window;
import org.whirlplatform.component.client.utils.InfoHelper;
import org.whirlplatform.editor.client.EditorEventBus;
import org.whirlplatform.editor.client.view.LoginView;
import org.whirlplatform.editor.shared.EditorDataService;
import org.whirlplatform.meta.shared.ClientUser;

@Presenter(view = LoginView.class)
public class LoginPresenter extends
        BasePresenter<LoginPresenter.ILoginView, EditorEventBus> {
    public void onStart() {
        EditorDataService.Util.getDataService().getUser(
                new AsyncCallback<ClientUser>() {

                    @Override
                    public void onSuccess(ClientUser user) {
                        if (user != null) {
                            ClientUser.setCurrentUser(user);
                            eventBus.initUi();
                            eventBus.buildApp();
                        } else {
                            onShowLoginPanel();
                        }
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        onShowLoginPanel();
                        InfoHelper.throwInfo("get-user", caught);
                    }
                });
    }

    public void login(String login, String password) {
        EditorDataService.Util.getDataService().login(login, password,
                new AsyncCallback<ClientUser>() {

                    @Override
                    public void onSuccess(ClientUser result) {
                        view.setButtonEnabled(true);
                        ((Window) view).hide();
                        // Если логин вводится первый раз (приложение еще не
                        // построено)
                        if (ClientUser.getCurrentUser() == null) {
                            ClientUser.setCurrentUser(result);
                            eventBus.initUi();
                            eventBus.buildApp();
                        }
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        view.setButtonEnabled(true);
                        InfoHelper.throwInfo("login", caught);
                    }
                });
    }

    public void onShowLoginPanel() {
        view.clear();
        ((Window) view).show();
    }

    public interface ILoginView extends IsWidget,
            ReverseViewInterface<LoginPresenter> {
        void setButtonEnabled(boolean enabled);

        void clear();
    }
}
