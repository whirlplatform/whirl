package org.whirlplatform.server.driver.db;

import org.apache.empire.db.DBDatabase;
import org.whirlplatform.server.driver.db.table.TableWhirlUserApplications;
import org.whirlplatform.server.driver.db.table.TableWhirlUserGroups;
import org.whirlplatform.server.driver.db.table.TableWhirlUsers;

// org.whirlplatform.server.driver.db.table vs org.whirlplatform.server.driver.db 

@SuppressWarnings("checkstyle:all")
public class MetadataDatabase extends DBDatabase {

    private static final long serialVersionUID = 1L;
    private static MetadataDatabase instance;
    public final TableWhirlUsers WHIRL_USERS = new TableWhirlUsers(this);
    public final TableWhirlUserApplications WHIRL_USER_APPLICATIONS =
        new TableWhirlUserApplications(
            this);
    public final TableWhirlUserGroups WHIRL_USER_GROUPS = new TableWhirlUserGroups(
        this);

    /**
     * Default constructor for the MetadataDatabase.
     */
    private MetadataDatabase() {

        // Define foreign key relations

        // foreign key relations done
    }

    /**
     * Returns the instance of the database.
     *
     * @return
     */
    public static MetadataDatabase get() {
        if (instance == null) {
            instance = new MetadataDatabase();
        }
        return instance;
    }

}
