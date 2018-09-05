package org.whirlplatform.meta.shared.editor;

import java.util.Comparator;

public class ParametersComparator implements Comparator<EventParameterElement> {

    public static final ParametersComparator COMPARATOR = new ParametersComparator();

    private ParametersComparator() {
    }

    @Override
    public int compare(EventParameterElement arg1, EventParameterElement arg2) {
        return arg1.getIndex() - arg2.getIndex();
    }
}
