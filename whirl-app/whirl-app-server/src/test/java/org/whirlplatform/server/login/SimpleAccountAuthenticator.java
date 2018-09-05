package org.whirlplatform.server.login;

import org.whirlplatform.rpc.shared.CustomException;

import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
@Named("SimpleAccountAuthenticator")
public class SimpleAccountAuthenticator implements AccountAuthenticator {

    @Override
    public ApplicationUser login(LoginData login) throws CustomException {
        ApplicationUser user = new ApplicationUser();
        user.setId("-678755146004");
        user.setLogin("arquillian");
        user.setName("arquillian");
        user.addGroup("new_group4444");
        user.setGuest(false);
        return user;
    }

    @Override
    public void logout(ApplicationUser user) {
    }
}
