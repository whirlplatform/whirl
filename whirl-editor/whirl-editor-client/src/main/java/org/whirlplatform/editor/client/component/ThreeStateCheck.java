package org.whirlplatform.editor.client.component;

import com.sencha.gxt.widget.core.client.form.Field;

public class ThreeStateCheck extends Field<CheckState> {

    public ThreeStateCheck() {
        super(new ThreeStateCheckCell());
    }

    public ThreeStateCheck(ThreeStateCheckCell cell) {
        super(cell);
    }

}
