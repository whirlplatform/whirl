package org.whirlplatform.editor.server;

import org.whirlplatform.editor.shared.RPCException;
import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.driver.db.MetadataDatabase;
import org.whirlplatform.server.login.ApplicationUser;

public interface EditorDatabaseConnector {

    ConnectionWrapper aliasConnection(String alias, ApplicationUser user) throws RPCException;

    ConnectionWrapper metadataConnection(ApplicationUser user) throws RPCException;

    ConnectionWrapper metadataConnection() throws RPCException;

    MetadataDatabase openMetadataDatabase() throws RPCException;

    String getNextDfobj(ApplicationUser user) throws RPCException;

    String getNextId();
}
