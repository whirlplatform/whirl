package org.whirlplatform.component.client.grid;

import org.whirlplatform.meta.shared.FilterType;
import org.whirlplatform.meta.shared.i18n.AppMessage;

class FilterTypeUtil {

    public static String getLabel(final FilterType item) {
        if (FilterType.NO_FILTER == item) {
            return AppMessage.Util.MESSAGE.filter_condition_no_filter();
        } else if (FilterType.BETWEEN == item) {
            return AppMessage.Util.MESSAGE.filter_condition_between();
        } else if (FilterType.CONTAINS == item) {
            return AppMessage.Util.MESSAGE.filter_condition_contains();
        } else if (FilterType.EMPTY == item) {
            return AppMessage.Util.MESSAGE.filter_condition_empty();
        } else if (FilterType.END_WITH == item) {
            return AppMessage.Util.MESSAGE.filter_condition_end_with();
        } else if (FilterType.EQUALS == (item)) {
            return AppMessage.Util.MESSAGE.filter_condition_equals();
        } else if (FilterType.GREATER == item) {
            return AppMessage.Util.MESSAGE.filter_condition_greater();
        } else if (FilterType.LOWER == item) {
            return AppMessage.Util.MESSAGE.filter_condition_lower();
        } else if (FilterType.NOT_CONTAINS.equals(item)) {
            return AppMessage.Util.MESSAGE.filter_condition_not_contains();
        } else if (FilterType.NOT_EMPTY.equals(item)) {
            return AppMessage.Util.MESSAGE.filter_condition_not_empty();
        } else if (FilterType.REVERSE.equals(item)) {
            return AppMessage.Util.MESSAGE.filter_condition_reverse();
        } else if (FilterType.START_WITH.equals(item)) {
            return AppMessage.Util.MESSAGE.filter_condition_start_with();
        }
        return null;
    }
}
