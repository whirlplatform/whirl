package org.whirlplatform.meta.shared;

import java.io.Serializable;

public enum SortType implements Serializable {

	DESC("desc"), ASC("asc");

	private String type;

    SortType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return type;
	}

	public SortType parse(String type) {
		if (DESC.type.equalsIgnoreCase(type)) {
			return DESC;
		}
		if (ASC.type.equalsIgnoreCase(type)) {
			return ASC;
		}
		return null;
	}

}
