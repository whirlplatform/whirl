package org.whirlplatform.js.client;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportConstructor;
import org.timepedia.exporter.client.ExportOverlay;
import org.whirlplatform.meta.shared.data.RowValueImpl;

/**
 * Используется в качестве значений объекта спискового типа {RowListValueOverlay RowListValue}.
 * Содержит значения checked, expanded, selected, id.
 * Применяется для организации групп чекбоков, радиокнопок, списков.
 */
//@Export("RowValue")
//@ExportPackage("Whirl")
public abstract class RowValueOverlay implements ExportOverlay<RowValueImpl> {

    /**
     * Конструктор позволяет только создать объект и назначить ему единтификатор.
     * Служебные поля вроде checked, selected следует задавать соответствующими методами.
     *
     * @param id Значение элемента списка
     */
    @ExportConstructor
    public static RowValueImpl constructor(String id) {
        return new RowValueImpl(id);
    }

    @Export
    public abstract String getId();

    @Export
    public abstract boolean isSelected();

    @Export
    public abstract void setSelected(boolean selected);

    @Export
    public abstract boolean isChecked();

    @Export
    public abstract void setChecked(boolean checked);

    @Export
    public abstract boolean isExpanded();

    @Export
    public abstract void setExpanded(boolean expanded);

    @Export
    public abstract void setLabel(String label);

}
