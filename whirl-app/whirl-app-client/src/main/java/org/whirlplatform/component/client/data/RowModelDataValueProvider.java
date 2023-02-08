package org.whirlplatform.component.client.data;

import com.sencha.gxt.core.client.ValueProvider;
import org.whirlplatform.meta.shared.data.RowModelData;

public class RowModelDataValueProvider<V> implements ValueProvider<RowModelData, V> {
    private final String property;

    public RowModelDataValueProvider(String property) {
        this.property = property;
    }

    @Override
    public V getValue(RowModelData object) {
        return object.get(property);
    }

    @Override
    public void setValue(RowModelData object, V value) {
        object.set(property, value);
    }

    @Override
    public String getPath() {
        return property;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RowModelDataValueProvider) {
            return property
                .equals(((RowModelDataValueProvider<?>) obj).property);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return property.hashCode();
    }
}
