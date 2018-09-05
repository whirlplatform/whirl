package org.whirlplatform.editor.client.meta;

import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.editor.ElementVisitor;
import org.whirlplatform.meta.shared.editor.ElementVisitor.VisitContext;
import org.whirlplatform.meta.shared.editor.LocaleElement;

@SuppressWarnings("serial")
public class NewPropertyElement extends NewElement {

    private PropertyType type;
    private LocaleElement locale;
    private Object value;
    private boolean replaceable;

    public NewPropertyElement(PropertyType type, Object value) {
        this.type = type;
        this.value = value;
    }

    public NewPropertyElement(PropertyType type, LocaleElement locale,
                              Object value) {
        this(type, value);
        this.locale = locale;
    }

    public NewPropertyElement(PropertyType type, LocaleElement locale,
                              boolean replaceable, Object value) {
        this(type, value);
        this.locale = locale;
        this.replaceable = replaceable;
    }

    public PropertyType getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    public LocaleElement getLocale() {
        return locale;
    }

    public boolean isReplaceable() {
        return replaceable;
    }

    @Override
    public void accept(VisitContext ctx, ElementVisitor visitor) {

    }

}