package org.whirlplatform.server.login.impl;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import javax.inject.Inject;
import org.apache.empire.db.DBCmpType;
import org.apache.empire.db.DBColumnExpr;
import org.apache.empire.db.DBCommand;
import org.apache.empire.db.DBReader;
import org.apache.empire.exceptions.EmpireException;
import org.mindrot.jbcrypt.BCrypt;
import org.whirlplatform.rpc.shared.CustomException;
import org.whirlplatform.server.db.ConnectException;
import org.whirlplatform.server.db.ConnectionProvider;
import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.driver.db.MetadataDatabase;
import org.whirlplatform.server.global.SrvConstant;
import org.whirlplatform.server.i18n.I18NMessage;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;
import org.whirlplatform.server.login.AccountAuthenticator;
import org.whirlplatform.server.login.ApplicationUser;
import org.whirlplatform.server.login.LoginData;
import org.whirlplatform.server.login.LoginException;

public class DBAccountAuthenticator implements AccountAuthenticator {

    private static Logger _log = LoggerFactory.getLogger(DBAccountAuthenticator.class);

    private ConnectionProvider connectionProvider;

    @Inject
    public DBAccountAuthenticator(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    private MetadataDatabase getMetadataDatabase(ConnectionWrapper connection) {
        MetadataDatabase db = MetadataDatabase.get();
        if (!db.isOpen()) {
            db.open(connection.getDatabaseDriver(), connection);
        }
        return db;
    }

    @Override
    public ApplicationUser login(LoginData login) throws LoginException, CustomException {
        try (ConnectionWrapper connection = connectionProvider.getConnection(
                SrvConstant.DEFAULT_CONNECTION)) {
            MetadataDatabase db = getMetadataDatabase(connection);

            DBCommand command = db.createCommand();
            DBColumnExpr loginCol = db.WHIRL_USERS.LOGIN.lower().as("LOGIN");
            command.select(db.WHIRL_USERS.ID, loginCol, db.WHIRL_USERS.PASSWORD_HASH,
                    db.WHIRL_USERS.NAME,
                    db.WHIRL_USERS.EMAIL);
            command.where(db.WHIRL_USERS.LOGIN.lower().is(login.getLogin().toLowerCase()));
            command.where(db.WHIRL_USERS.DELETED.cmp(DBCmpType.NULL, null));

            DBReader reader = new DBReader();
            reader.open(command, connection);

            ApplicationUser user = null;
            if (reader.moveNext() && login.getLogin().equalsIgnoreCase(reader.getString(loginCol))
                    && BCrypt.checkpw(login.getPassword(),
                    reader.getString(db.WHIRL_USERS.PASSWORD_HASH))) {
                user = new ApplicationUser();
                String userId = reader.getString(db.WHIRL_USERS.ID);
                user.setId(reader.getString(db.WHIRL_USERS.ID));
                user.setLogin(reader.getString(loginCol));
                user.setName(reader.getString(db.WHIRL_USERS.NAME));
                user.addGroups(getPersistentGroups(userId, connection));
                user.addAllowedApps(getAllowedApps(userId, connection));
            }
            reader.close();

            if (user == null) {
                throw new LoginException(
                        I18NMessage.getMessage(I18NMessage.getRequestLocale()).login_error());
            }

            return user;
        } catch (SQLException | ConnectException | EmpireException e) {
            _log.error(e);
            throw new LoginException(e);
        }
    }

    private Set<String> getPersistentGroups(String userId, ConnectionWrapper connection) {
        MetadataDatabase db = getMetadataDatabase(connection);
        Set<String> result = new HashSet<String>();
        DBCommand command = db.createCommand();
        command.select(db.WHIRL_USER_GROUPS.GROUP_CODE);
        command.where(db.WHIRL_USER_GROUPS.R_WHIRL_USERS.is(userId)
                .and(db.WHIRL_USER_GROUPS.DELETED.cmp(DBCmpType.NULL, null)));

        DBReader reader = new DBReader();
        reader.open(command, connection);
        while (reader.moveNext()) {
            result.add(reader.getString(db.WHIRL_USER_GROUPS.GROUP_CODE));
        }
        reader.close();
        return result;
    }

    private Set<String> getAllowedApps(String userId, ConnectionWrapper connection) {
        MetadataDatabase db = getMetadataDatabase(connection);
        Set<String> result = new HashSet<String>();
        DBCommand command = db.createCommand();
        command.select(db.WHIRL_USER_APPLICATIONS.APPLICATION_CODE);
        command.where(db.WHIRL_USER_GROUPS.R_WHIRL_USERS.is(userId)
                .and(db.WHIRL_USER_GROUPS.DELETED.cmp(DBCmpType.NULL, null)));

        DBReader reader = new DBReader();
        reader.open(command, connection);
        while (reader.moveNext()) {
            result.add(reader.getString(db.WHIRL_USER_APPLICATIONS.APPLICATION_CODE));
        }
        reader.close();
        return result;
    }

    @Override
    public void logout(ApplicationUser user) {
        // TODO Auto-generated method stub

    }

}
