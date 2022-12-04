package org.whirlplatform.editor.client.util;

import com.sencha.gxt.data.shared.LabelProvider;
import org.whirlplatform.meta.shared.editor.AbstractElement;

public class ElementLabelProvider<T extends AbstractElement> implements
        LabelProvider<T> {

    @Override
    public String getLabel(T item) {
        return item.getName();
    }

}
