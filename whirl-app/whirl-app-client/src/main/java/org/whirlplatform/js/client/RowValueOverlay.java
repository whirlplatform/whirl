package org.whirlplatform.js.client;

import org.whirlplatform.meta.shared.data.RowValueImpl;

/**
 * Используется в качестве значений объекта спискового типа {RowListValueOverlay RowListValue}.
 * Содержит значения checked, expanded, selected, id. Применяется для организации групп чекбоков,
 * радиокнопок, списков.
 */
public abstract class RowValueOverlay {

    /**
     * Конструктор позволяет только создать объект и назначить ему единтификатор. Служебные поля
     * вроде checked, selected следует задавать соответствующими методами.
     *
     * @param id Значение элемента списка
     */
    public static RowValueImpl constructor(String id) {
        return new RowValueImpl(id);
    }

    public abstract String getId();

    public abstract boolean isSelected();

    public abstract void setSelected(boolean selected);

    public abstract boolean isChecked();

    public abstract void setChecked(boolean checked);

    public abstract boolean isExpanded();

    public abstract void setExpanded(boolean expanded);

    public abstract void setLabel(String label);

}
