package org.whirlplatform.server.metadata;

import java.util.List;
import org.whirlplatform.server.login.ApplicationUser;

public interface MetadataProvider {

    List<String> getUserApplications(ApplicationUser user);

    List<String> getUserGroups(ApplicationUser user);

    void createDatabaseStructure();
}
