package org.whirlplatform.meta.shared.editor;

import java.util.Comparator;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataValue;

public class ElementChildComparator implements Comparator<ComponentElement> {

    public static final ElementChildComparator COMPARATOR = new ElementChildComparator();

    private ElementChildComparator() {
    }

    @Override
    public int compare(ComponentElement o1, ComponentElement o2) {
        DataValue val1 = o1.getProperty(PropertyType.LayoutDataIndex)
                .getValue(o1.getProperty(PropertyType.LayoutDataIndex).getDefaultLocale());
        Integer index1 = parse(val1);
        DataValue val2 = o2.getProperty(PropertyType.LayoutDataIndex)
                .getValue(o2.getProperty(PropertyType.LayoutDataIndex).getDefaultLocale());
        Integer index2 = parse(val2);
        return index1 - index2;
    }

    private Integer parse(DataValue value) {
        try {
            return value.getDouble().intValue();
        } catch (Exception e) {
            return 3000;
        }
    }
}
