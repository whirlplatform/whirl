package org.whirlplatform.editor.client.util;

import com.google.gwt.core.client.Callback;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.loader.DataProxy;
import com.sencha.gxt.data.shared.loader.LoadEvent;
import com.sencha.gxt.data.shared.loader.LoadHandler;
import com.sencha.gxt.data.shared.loader.Loader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import org.whirlplatform.editor.client.ApplicationDataProvider;
import org.whirlplatform.editor.shared.i18n.EditorMessage;
import org.whirlplatform.meta.shared.editor.db.DataSourceElement;

public class DataSourceStore extends ListStore<DataSourceElement> {

    private static final Comparator<DataSourceElement> comparator =
        new Comparator<DataSourceElement>() {

            @Override
            public int compare(DataSourceElement o1, DataSourceElement o2) {
                if (o1.getId().isEmpty()) {
                    return -1;
                } else if (o2.getId().isEmpty()) {
                    return 1;
                }
                return o1.getName().compareTo(o2.getName());
            }
        };
    private DataProxy<String, List<DataSourceElement>> proxy;
    private Loader<String, List<DataSourceElement>> loader;

    public DataSourceStore(
        ApplicationDataProvider provider) {
        super(new ElementKeyProvider<DataSourceElement>());

        proxy = new DataSourceProxy(provider);
        loader = new Loader<String, List<DataSourceElement>>(proxy);

        loader.addLoadHandler(new LoadHandler<String, List<DataSourceElement>>() {
            @Override
            public void onLoad(LoadEvent<String, List<DataSourceElement>> event) {
                DataSourceStore.this.replaceAll(event.getLoadResult());
                DataSourceElement empty = new DataSourceElement(null, null);
                empty.setId("");
                empty.setName(EditorMessage.Util.MESSAGE.property_no_data());
                DataSourceStore.this.add(empty);
            }
        });
    }

    public Loader<String, List<DataSourceElement>> getLoader() {
        return loader;
    }

    private class DataSourceProxy implements DataProxy<String, List<DataSourceElement>> {
        private ApplicationDataProvider provider;

        private DataSourceProxy(ApplicationDataProvider provider) {
            this.provider = provider;
            addSortInfo(new StoreSortInfo<DataSourceElement>(comparator, SortDir.ASC));
        }

        @Override
        public void load(final String loadConfig,
                         final Callback<List<DataSourceElement>, Throwable> callback) {

            provider.getDataSources(new Callback<Collection<DataSourceElement>, Throwable>() {

                @Override
                public void onFailure(Throwable reason) {
                    callback.onFailure(reason);
                }

                @Override
                public void onSuccess(Collection<DataSourceElement> result) {
                    List<DataSourceElement> list = new ArrayList<DataSourceElement>();
                    if (loadConfig == null || loadConfig.isEmpty()) {
                        list.addAll(result);
                    } else {
                        for (DataSourceElement element : result) {
                            String query = loadConfig.toLowerCase();
                            boolean isName = element.getName() != null
                                && element.getName().toLowerCase()
                                .contains(query);
                            boolean isDataSource = (element.getDatabaseName() != null && element
                                .getDatabaseName().toLowerCase()
                                .contains(query));
                            if (isName || isDataSource) {
                                list.add(element);
                            }
                        }
                    }
                    callback.onSuccess(list);
                }

            });
        }
    }
}
