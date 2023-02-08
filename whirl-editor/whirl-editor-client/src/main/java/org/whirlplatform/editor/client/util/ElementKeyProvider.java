package org.whirlplatform.editor.client.util;

import com.sencha.gxt.data.shared.ModelKeyProvider;
import org.whirlplatform.meta.shared.editor.AbstractElement;

public class ElementKeyProvider<T extends AbstractElement> implements
    ModelKeyProvider<T> {

    @Override
    public String getKey(T item) {
        return item.getId();
    }

}
