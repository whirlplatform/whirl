package org.whirlplatform.editor.client.image;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface PropertyFormIcon extends ClientBundle {

    @Source("PropertyForm.gss")
    PropertyFormResource style();

    interface PropertyFormResource extends CssResource {

        String tableInsert();

        String tableDelete();

    }
}
