package org.whirlplatform.js.client;

import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.grid.EditGridBuilder;

public abstract class EditGridBuilderOverlay {

    /**
     * Инициализация грида
     *
     * @param instance - EditGridBuilder
     * @return EditGridBuilder
     * @deprecated new EditGridBuilder()
     */
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
    public abstract void setDomId(String domId);

    /**
     * Возвращает идентификатор элемента в DOM документа.
     *
     */
    public abstract String getDomId();

    /**
     * Установка кода гриду
     *
     * @param name - Strinng, код
     */
    public abstract void setCode(String name);

    /**
     * Получение кода у грида
     *
     * @return String
     */
    public abstract String getCode();

    /**
     * Установка активности грида
     *
     * @param enabled - boolean
     */
    public abstract void setEnabled(boolean enabled);

    /**
     * Получение информации об активности грида
     *
     * @return boolean
     */
    public abstract boolean isEnabled();

    /**
     * Установка скрытости грида
     *
     * @param hidden - boolean
     */
    public abstract void setHidden(boolean hidden);

    /**
     * Получение информации об скрытости грида
     *
     * @return boolean
     */
    public abstract boolean isHidden();

    /**
     * Изменить css атрибут class компонента
     *
     * @param styleName - String
     */
    public abstract void setStyleName(String styleName);

    /**
     * Перечитать данные для грида и обновить компонент
     */
    public abstract void load();

    /**
     * @param parameters
     * @deprecated loadWithParameters
     */
//    @Deprecated
////    public static void loadParameters(EditGridBuilder instance, DataValue[] parameters) {
//        instance.load(Arrays.asList(parameters));
//    }


////    public static void loadWithParameters(EditGridBuilder instance, DataValue[] parameters) {
//        instance.load(Arrays.asList(parameters));
//    }

    /**
     * Получение родителя грида
     *
     * @param instance - EditGridBuilder
     * @return ComponentBuilder
     */
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
////    public static RowModelData[] getAllItems(EditGridBuilder instance) {
//        return instance.getAllItems().toArray(new RowModelData[0]);
//    }

    /**
     * Получить строку грида по идентификатору записи
     *
     * @param id
     * @return Значение {@link RowModelDataOverlay
     * RowModelData}
     */
////    public abstract RowModelData getItemById(String id);

    /**
     * Получить информацию о строках грида: идентификатор, selected, checked,
     * expanded
     * <p>
     * Значений в них не хранится.
     *
     * @return {@link RowListValueOverlay
     * RowListValue}
     */
////    public static RowListValue getDataValue(EditGridBuilder instance) {
//        return instance.getFieldValue();
//    }

    /**
     * Установить значения грида
     *
     * @return {@link RowListValueOverlay
     * RowListValue}
     * @deprecated EditGridBuilder.setDataValue
     */
////    @Deprecated
//    public static void setValue(EditGridBuilder instance, RowListValue value) {
//        instance.setFieldValue(value);
//    }

    /**
     * Установить значения грида при помощи
     * {@link RowListValueOverlay RowListValue}
     *
     * @return RowListValue
     */
////    public static void setDataValue(EditGridBuilder instance, RowListValue value) {
//        instance.setFieldValue(value);
//    }

    /**
     * Установить фокус на грид
     */
    public abstract void focus();

    /**
     * Проверяет валидность заполнения поля
     *
     * @param invalidate - отображать информацию о невалидном значении
     * @return boolean
     */
    public abstract boolean isValid(boolean invalidate);

    /**
     * Очищает фильтр грида.
     */
////    public abstract void clearFilter();

}
