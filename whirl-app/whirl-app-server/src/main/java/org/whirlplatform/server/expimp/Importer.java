package org.whirlplatform.server.expimp;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.DataModifyConfig;
import org.whirlplatform.meta.shared.DataModifyConfig.DataModifyType;
import org.whirlplatform.meta.shared.FieldMetadata;
import org.whirlplatform.meta.shared.data.RowModelData;
import org.whirlplatform.meta.shared.data.RowModelDataImpl;
import org.whirlplatform.server.driver.Connector;
import org.whirlplatform.server.login.ApplicationUser;

public abstract class Importer {

    protected Connector connector;
    protected ApplicationUser user;
    protected ClassMetadata metadata;
    protected boolean importError = false;

    public abstract void importFromStream(InputStream input) throws IOException;

    protected void insert(Map<FieldMetadata, String> line) {
        if (line == null || line.size() < 1) {
            return;
        }

        RowModelData model = new RowModelDataImpl();
        for (Entry<FieldMetadata, String> f : line.entrySet()) {
            model.set(f.getKey().getName(), f.getValue());
        }
        DataModifyConfig config =
                new DataModifyConfig(DataModifyType.INSERT, Arrays.asList(model), null);
        connector.insert(metadata, config, user);
    }

    public boolean isImportError() {
        return importError;
    }
}
