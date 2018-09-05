package org.whirlplatform.server.login;

import org.whirlplatform.rpc.shared.CustomException;

public interface AccountAuthenticator {

	ApplicationUser login(LoginData login) throws LoginException, CustomException;

	void logout(ApplicationUser user) throws LoginException;

}