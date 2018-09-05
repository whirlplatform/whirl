package org.whirlplatform.editor.client.component;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.theme.base.client.field.TriggerFieldDefaultAppearance;
import org.whirlplatform.editor.client.component.ParameterEditorComponent.ParameterFieldAppearance;

public class ParameterFieldDefaultAppearance extends
        TriggerFieldDefaultAppearance implements ParameterFieldAppearance {

    public interface ParameterFieldResources extends TriggerFieldResources {

        @Source({"ValueBaseFieldTiny.gss", "TextFieldTiny.gss", "TriggerField.gss"})
        ParameterFieldStyle css();

        @Source("ParameterFieldTriggerArrow.png")
        ImageResource triggerArrow();

        @Source("ParameterFieldTriggerArrowOver.png")
        ImageResource triggerArrowOver();

        @Source("ParameterFieldTriggerArrowClick.png")
        ImageResource triggerArrowClick();

        @Source("ParameterFieldTriggerArrowFocus.png")
        ImageResource triggerArrowFocus();
    }

    public interface ParameterFieldStyle extends TriggerFieldStyle {
    }

    public ParameterFieldDefaultAppearance() {
        this(GWT.create(ParameterFieldResources.class));
    }

    public ParameterFieldDefaultAppearance(ParameterFieldResources resources) {
        super(resources);
    }
}
