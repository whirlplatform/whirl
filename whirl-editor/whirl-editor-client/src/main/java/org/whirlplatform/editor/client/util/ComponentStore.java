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
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.editor.ComponentElement;

public class ComponentStore extends ListStore<ComponentElement> {

    private static final Comparator<ComponentElement> comparator =
            new Comparator<ComponentElement>() {

                @Override
                public int compare(ComponentElement o1, ComponentElement o2) {
                    if (o1.getId().isEmpty()) {
                        return -1;
                    } else if (o2.getId().isEmpty()) {
                        return 1;
                    }
                    return o1.getName().compareTo(o2.getName());
                }

            };
    private DataProxy<String, List<ComponentElement>> proxy;
    private Loader<String, List<ComponentElement>> loader;

    /**
     * По умолчанию выгружает все существующие в приложении компоненты.
     * <br> Второй параметр позволяет загрузить только компоненты являющиеся контейнерами.
     *
     * @param provider
     * @param containersOnly Если <b>true</b> - будут загружены только компоненты контейнеры.
     */
    public ComponentStore(ApplicationDataProvider provider, boolean containersOnly) {
        super(new ElementKeyProvider<ComponentElement>());

        proxy = new ComponentProxy(provider, containersOnly);
        loader = new Loader<String, List<ComponentElement>>(proxy);

        loader.addLoadHandler(new LoadHandler<String, List<ComponentElement>>() {
            @Override
            public void onLoad(LoadEvent<String, List<ComponentElement>> event) {
                ComponentStore.this.replaceAll(event.getLoadResult());
                ComponentElement empty = new ComponentElement();
                empty.setId("");
                empty.setName(EditorMessage.Util.MESSAGE.property_no_data());
                ComponentStore.this.add(empty);
            }
        });
    }

    public void setContainersOnly(boolean containersOnly) {
        ((ComponentProxy) proxy).setContainersOnly(containersOnly);
    }

    public Loader<String, List<ComponentElement>> getLoader() {
        return loader;
    }

    private class ComponentProxy implements
            DataProxy<String, List<ComponentElement>> {

        private ApplicationDataProvider provider;
        private boolean containersOnly = false;

        private ComponentProxy(ApplicationDataProvider provider, boolean containersOnly) {
            this.provider = provider;
            this.containersOnly = containersOnly;
            addSortInfo(new StoreSortInfo<ComponentElement>(comparator,
                    SortDir.ASC));
        }

        public void setContainersOnly(boolean containersOnly) {
            this.containersOnly = containersOnly;
        }

        @Override
        public void load(final String loadConfig,
                         final Callback<List<ComponentElement>, Throwable> callback) {
            provider.getAvailableComponents(
                    new Callback<Collection<ComponentElement>, Throwable>() {
                        @Override
                        public void onFailure(Throwable reason) {
                            callback.onFailure(reason);
                        }

                        @Override
                        public void onSuccess(Collection<ComponentElement> result) {
                            List<ComponentElement> list = new ArrayList<ComponentElement>();
                            if (loadConfig == null || loadConfig.isEmpty()) {
                                if (containersOnly) {
                                    for (ComponentElement element : result) {
                                        if (element.getType().isContainer()) {
                                            list.add(element);
                                        }
                                    }
                                } else {
                                    list.addAll(result);
                                }
                            } else {
                                for (ComponentElement element : result) {
                                    String query = loadConfig.toLowerCase();
                                    boolean isName = element.getName() != null
                                            && element.getName().toLowerCase().contains(query);
                                    String val =
                                            element.getProperty(PropertyType.Code).getDefaultValue()
                                                    .getString();
                                    boolean isCode = val != null
                                        && (String.valueOf(val).toLowerCase().contains(query));
                                    if (isName || isCode) {
                                        if (containersOnly) {
                                            if (element.getType().isContainer()) {
                                                list.add(element);
                                            }
                                        } else {
                                            list.add(element);
                                        }
                                    }
                                }
                            }
                            callback.onSuccess(list);
                        }
                    });
        }
    }

}
