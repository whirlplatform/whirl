package org.whirlplatform.editor.client.util;

import java.util.Comparator;
import org.whirlplatform.meta.shared.editor.AbstractElement;
import org.whirlplatform.meta.shared.editor.ComponentElement;
import org.whirlplatform.meta.shared.editor.db.AbstractTableElement;

public class TreeElementNamesComparator implements Comparator<AbstractElement> {
    /**
     * Сортирует компоненты и таблицы по названиям.
     */

    @Override
    public int compare(AbstractElement first, AbstractElement second) {
        if ((first instanceof ComponentElement && second instanceof ComponentElement) ||
                (first instanceof AbstractTableElement && second instanceof AbstractTableElement)) {
            return first.getName().compareTo(second.getName());
        } else {
            return 0;
        }
    }

}
