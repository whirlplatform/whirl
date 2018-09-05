package org.whirlplatform.meta.shared.component;

import org.whirlplatform.meta.shared.data.DataValue;

import java.util.Comparator;

public class ModelChildComparator implements Comparator<ComponentModel> {

	public static final ModelChildComparator COMPARATOR = new ModelChildComparator();

	public ModelChildComparator() {
	}

	@Override
	public int compare(ComponentModel o1, ComponentModel o2) {
		DataValue val1 = o1.getValue(PropertyType.LayoutDataIndex.getCode());
		Integer index1 = parse(val1);
		DataValue val2 = o2.getValue(PropertyType.LayoutDataIndex.getCode());
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
