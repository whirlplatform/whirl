package org.whirlplatform.server.login.impl;

import org.whirlplatform.server.login.AccountAuthenticator;
import org.whirlplatform.server.login.ApplicationUser;
import org.whirlplatform.server.login.LoginData;

/**
 * Авторизация гостевых пользователей. Все пользователи изначально являются
 * гостевыми.
 *
 */
public class GuestAccountAuthenticator implements AccountAuthenticator {

    @Override
    public ApplicationUser login(LoginData login) {
        ApplicationUser user = new ApplicationUser();
        String userId = login.getLogin();
        user.setId(userId);
        user.setLogin(userId);
        user.setName(userId);
        user.setGuest(true);
        return user;
    }

    @Override
    public void logout(ApplicationUser user) {
        // TODO Auto-generated method stub

    }

}
