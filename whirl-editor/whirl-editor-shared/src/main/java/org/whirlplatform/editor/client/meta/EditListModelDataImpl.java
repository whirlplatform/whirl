package org.whirlplatform.editor.client.meta;

import com.fasterxml.jackson.annotation.JsonIgnoreType;
import org.whirlplatform.meta.shared.data.ListModelDataImpl;
import org.whirlplatform.meta.shared.editor.AbstractElement;

@SuppressWarnings("serial")
@JsonIgnoreType
public class EditListModelDataImpl extends ListModelDataImpl {

    private AbstractElement element;

    public EditListModelDataImpl() {
    }

    public AbstractElement getElement() {
        return element;
    }

    public void setElement(AbstractElement element) {
        this.element = element;
    }

}
