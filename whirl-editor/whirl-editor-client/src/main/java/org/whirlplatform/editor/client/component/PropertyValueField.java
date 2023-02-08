package org.whirlplatform.editor.client.component;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.whirlplatform.editor.client.component.MultiSetCellDefaultAppearance.MultiSetCellResources;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.data.DataValueImpl;
import org.whirlplatform.meta.shared.editor.LocaleElement;
import org.whirlplatform.meta.shared.editor.PropertyValue;

public class PropertyValueField extends MultiSetField<LocaleElement> {

    private LocaleElement defaultLocale;
    private Set<LocaleElement> locales = new HashSet<LocaleElement>();

    public PropertyValueField() {
        super(new HashSet<LocaleElement>());
    }

    public PropertyValueField(MultiSetCellResources resources) {
        super(new HashSet<LocaleElement>(), resources);
    }

    public void setLocales(LocaleElement defaultLocale, Collection<LocaleElement> locales) {
        this.defaultLocale = defaultLocale;
        this.locales.clear();
        this.locales.addAll(locales);
        Set<LocaleElement> all = new HashSet<LocaleElement>();
        all.add(defaultLocale);
        all.addAll(locales);
        setKeys(all);
        setHideTrigger(this.locales.isEmpty());
        redrawCell();
    }

    public PropertyValue getPropertyValue() {
        PropertyValue value =
            new PropertyValue(DataType.STRING, defaultLocale, getValue(defaultLocale));
        for (LocaleElement locale : locales) {
            value.setValue(locale, new DataValueImpl(DataType.STRING, getValue(locale)));
        }
        return value;
    }

    public void setPropertyValue(PropertyValue value) {
        setValue(defaultLocale, value.getValue(defaultLocale).getString());
        for (LocaleElement locale : locales) {
            setValue(locale, value.getValue(locale).getString());
        }
        setCurrentKey(defaultLocale);
    }
}
