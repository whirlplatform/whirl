package org.whirlplatform.server.metadata;

import org.whirlplatform.server.login.ApplicationUser;

import java.util.List;

public interface MetadataProvider {

    List<String> getUserApplications(ApplicationUser user);

    List<String> getUserGroups(ApplicationUser user);

    void createDatabaseStructure();
}
