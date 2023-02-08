package org.whirlplatform.component.client.data;

import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.loader.LoadEvent;
import com.sencha.gxt.data.shared.loader.LoadExceptionEvent;
import com.sencha.gxt.data.shared.loader.LoadExceptionEvent.LoadExceptionHandler;
import com.sencha.gxt.data.shared.loader.LoadHandler;
import com.sencha.gxt.data.shared.loader.Loader;
import java.util.Objects;
import org.whirlplatform.component.client.utils.InfoHelper;
import org.whirlplatform.meta.shared.ClassLoadConfig;
import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.LoadData;
import org.whirlplatform.meta.shared.data.RowModelData;

//TODO перенести в component/client
public class ClassStore<T extends RowModelData, C extends ClassLoadConfig>
    extends ListStore<T> {

    protected ClassMetadata metadata;

    protected RpcProxy<?, LoadData<T>> proxy;

    protected Loader<C, LoadData<T>> loader;

    protected ClassStore() {
        super(new ClassKeyProvider());
    }

    public ClassStore(ClassMetadata metadata, RpcProxy<C, LoadData<T>> proxy) {
        this();
        this.metadata = metadata;

        this.proxy = proxy;
        loader = new Loader<C, LoadData<T>>(proxy);
        loader.addLoadHandler(new LoadHandler<C, LoadData<T>>() {

            @Override
            public void onLoad(LoadEvent<C, LoadData<T>> event) {
                ClassStore.this.replaceAll(event.getLoadResult().getData());
            }
        });
        loader.addLoadExceptionHandler(new LoadExceptionHandler<C>() {
            @Override
            public void onLoadException(LoadExceptionEvent<C> event) {
                InfoHelper.throwInfo("loader-exception", event.getException());
            }
        });
    }

    public Loader<C, LoadData<T>> getLoader() {
        return loader;
    }


    @Override
    public T findModelWithKey(String key) {
        for (int i = 0; i < size(); i++) {
            String findedKey = getKeyProvider().getKey(get(i));
            // TODO ошибка в реализации GXT хорошо бы оформить у них
            if (Objects.equals(findedKey, key)) {
                return get(i);
            }
        }
        return null;
    }

}
