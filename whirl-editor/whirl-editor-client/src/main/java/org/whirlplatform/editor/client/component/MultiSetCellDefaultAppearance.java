package org.whirlplatform.editor.client.component;

import com.google.gwt.core.client.GWT;
import com.sencha.gxt.theme.base.client.field.TriggerFieldDefaultAppearance;
import org.whirlplatform.editor.client.component.MultiSetCell.MultiSetCellAppearance;

public class MultiSetCellDefaultAppearance extends TriggerFieldDefaultAppearance implements MultiSetCellAppearance {

    public interface MultiSetCellResources extends TriggerFieldResources {

        @Source({"MultiSetField.gss", "TextField.gss", "ValueBaseField.gss"})
        MultiSetCellStyle css();

    }

    public interface MultiSetCellTinyResources extends MultiSetCellResources {

        @Source({"MultiSetFieldTiny.gss", "TextFieldTiny.gss", "ValueBaseFieldTiny.gss"})
        MultiSetCellTinyStyle css();
    }

    public interface MultiSetCellStyle extends TriggerFieldStyle {

    }

    public interface MultiSetCellTinyStyle extends MultiSetCellStyle {
    }

    public MultiSetCellDefaultAppearance() {
        this(GWT.create(MultiSetCellResources.class));
    }

    public MultiSetCellDefaultAppearance(MultiSetCellResources resources) {
        super(resources);
    }
}
