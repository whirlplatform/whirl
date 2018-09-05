package org.whirlplatform.editor.client.view.widget;

import com.sencha.gxt.core.client.ValueProvider;

public abstract class ReadOnlyValueProvider<T> implements ValueProvider<T, String> {
    private String path;

    public ReadOnlyValueProvider() {
    }

    public ReadOnlyValueProvider(final String path) {
        this.path = path;
    }

    @Override
    public void setValue(T object, String value) {
    }

    @Override
    public String getPath() {
        return path;
    }
}