package org.whirlplatform.meta.shared;

import java.io.Serializable;

public enum FilterType implements Serializable {
    NO_FILTER, EQUALS, CONTAINS, NOT_CONTAINS, GREATER, LOWER, BETWEEN, EMPTY, NOT_EMPTY,
    START_WITH, END_WITH, REVERSE
}
