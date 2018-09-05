package org.whirlplatform.js.client;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportInstanceMethod;
import org.timepedia.exporter.client.ExportOverlay;
import org.timepedia.exporter.client.ExportPackage;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.grid.EditGridBuilder;

@Export("EditGrid")
@ExportPackage("Whirl")
public abstract class EditGridBuilderOverlay implements ExportOverlay<EditGridBuilder> {

    /**
     * Инициализация грида
     *
     * @param instance - EditGridBuilder
     * @return EditGridBuilder
     * @deprecated new EditGridBuilder()
     */
    @ExportInstanceMethod
    @Deprecated
    public static EditGridBuilder create(EditGridBuilder instance) {
        instance.create();
        return instance;
    }

    /**
     * Устанавливает идентификатор элемента в DOM документа.
     *
     * @param domId
     */
    @Export
    public abstract void setDomId(String domId);

    /**
     * Возвращает идентификатор элемента в DOM документа.
     *
     */
    @Export
    public abstract String getDomId();

    /**
     * Установка кода гриду
     *
     * @param name - Strinng, код
     */
    @Export
    public abstract void setCode(String name);

    /**
     * Получение кода у грида
     *
     * @return String
     */
    @Export
    public abstract String getCode();

    /**
     * Установка активности грида
     *
     * @param enabled - boolean
     */
    @Export
    public abstract void setEnabled(boolean enabled);

    /**
     * Получение информации об активности грида
     *
     * @return boolean
     */
    @Export
    public abstract boolean isEnabled();

    /**
     * Установка скрытости грида
     *
     * @param hidden - boolean
     */
    @Export
    public abstract void setHidden(boolean hidden);

    /**
     * Получение информации об скрытости грида
     *
     * @return boolean
     */
    @Export
    public abstract boolean isHidden();

    /**
     * Изменить css атрибут class компонента
     *
     * @param styleName - String
     */
    @Export
    public abstract void setStyleName(String styleName);

    /**
     * Перечитать данные для грида и обновить компонент
     */
    @Export
    public abstract void load();

    /**
     * @param parameters
     * @deprecated loadWithParameters
     */
//    @Deprecated
//    @ExportInstanceMethod
//    public static void loadParameters(EditGridBuilder instance, DataValue[] parameters) {
//        instance.load(Arrays.asList(parameters));
//    }


//    @ExportInstanceMethod
//    public static void loadWithParameters(EditGridBuilder instance, DataValue[] parameters) {
//        instance.load(Arrays.asList(parameters));
//    }

    /**
     * Получение родителя грида
     *
     * @param instance - EditGridBuilder
     * @return ComponentBuilder
     */
    @ExportInstanceMethod
    public static ComponentBuilder getParent(EditGridBuilder instance) {
        return instance.getParentBuilder();
    }

    /**
     * Получить значения всех строк грида
     *
     * @return Список значений
     * {@link RowModelDataOverlay
     * RowModelData}[]
     */
//    @ExportInstanceMethod
//    public static RowModelData[] getAllItems(EditGridBuilder instance) {
//        return instance.getAllItems().toArray(new RowModelData[0]);
//    }

    /**
     * Получить строку грида по идентификатору записи
     *
     * @param id
     * @return Значение {@link RowModelDataOverlay
     * RowModelData}
     */
//    @ExportInstanceMethod
//    public abstract RowModelData getItemById(String id);

    /**
     * Получить информацию о строках грида: идентификатор, selected, checked,
     * expanded
     * <p>
     * Значений в них не хранится.
     *
     * @return {@link RowListValueOverlay
     * RowListValue}
     */
//    @ExportInstanceMethod
//    public static RowListValue getDataValue(EditGridBuilder instance) {
//        return instance.getFieldValue();
//    }

    /**
     * Установить значения грида
     *
     * @return {@link RowListValueOverlay
     * RowListValue}
     * @deprecated EditGridBuilder.setDataValue
     */
//    @ExportInstanceMethod
//    @Deprecated
//    public static void setValue(EditGridBuilder instance, RowListValue value) {
//        instance.setFieldValue(value);
//    }

    /**
     * Установить значения грида при помощи
     * {@link RowListValueOverlay RowListValue}
     *
     * @return RowListValue
     */
//    @ExportInstanceMethod
//    public static void setDataValue(EditGridBuilder instance, RowListValue value) {
//        instance.setFieldValue(value);
//    }

    /**
     * Установить фокус на грид
     */
    @Export
    public abstract void focus();

    /**
     * Проверяет валидность заполнения поля
     *
     * @param invalidate - отображать информацию о невалидном значении
     * @return boolean
     */
    @Export
    public abstract boolean isValid(boolean invalidate);

    /**
     * Очищает фильтр грида.
     */
//    @Export
//    public abstract void clearFilter();

}
