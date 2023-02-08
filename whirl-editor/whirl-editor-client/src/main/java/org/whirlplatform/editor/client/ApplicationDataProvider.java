package org.whirlplatform.editor.client;

import com.google.gwt.core.client.Callback;
import java.util.Collection;
import org.whirlplatform.meta.shared.editor.ComponentElement;
import org.whirlplatform.meta.shared.editor.db.AbstractTableElement;
import org.whirlplatform.meta.shared.editor.db.DataSourceElement;
import org.whirlplatform.meta.shared.editor.db.PlainTableElement;
import org.whirlplatform.meta.shared.editor.db.TableColumnElement;

public interface ApplicationDataProvider {

    void getAvailableComponents(
        Callback<Collection<ComponentElement>, Throwable> callback);

    void getAvailableTables(
        Callback<Collection<AbstractTableElement>, Throwable> callback);

    void getAvailableColumns(PlainTableElement table,
                             Callback<Collection<TableColumnElement>, Throwable> callback);

    void getDataSources(
        Callback<Collection<DataSourceElement>, Throwable> callback);

}
