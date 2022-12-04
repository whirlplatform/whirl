package org.whirlplatform.js.client;

import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.combo.ComboBoxBuilder;
import org.whirlplatform.meta.shared.data.DataValueImpl;
import org.whirlplatform.meta.shared.data.ListModelData;

public abstract class ComboBoxBuilderOverlay {

    @Deprecated
    public static ComboBoxBuilder create(ComboBoxBuilder instance) {
        instance.create();
        return instance;
    }

    public static boolean isEmpty(ComboBoxBuilder instance) {
        return instance.getValue() == null;
    }

    public static ComponentBuilder getParent(ComboBoxBuilder instance) {
        return instance.getParentBuilder();
    }

    /**
     * Установить текст всплывающей подсказки
     *
     * @param toolTip
     */
    public static void setToolTip(ComboBoxBuilder instance, String toolTip) {
        instance.getComponent().setToolTip(toolTip);
    }

    /**
     * Возвращает идентификатор элемента в DOM документа.
     */
    public abstract String getDomId();

    /**
     * Устанавливает идентификатор элемента в DOM документа.
     *
     * @param domId
     */
    public abstract void setDomId(String domId);

    public abstract String getCode();

    public abstract void setCode(String name);

    public abstract boolean isEnabled();

    public abstract void setEnabled(boolean enabled);

    public abstract void setHidden(boolean hidden);

    public abstract void isHidden();

    public abstract void setStyleName(String styleName);

    /**
     * Очистить текущее значение комбобокса. Значения, отображаемые в выпадающем списке остаются
     * доступными для выбора.
     */
    public abstract void clear();

    /**
     * Получить текущее значение комбобокса.
     *
     * @return {@link DataValueOverlay DataValue}
     */
////    public static DataValue getDataValue(ComboBoxBuilder instance) {
//        return instance.getFieldValue();
//    }

    /**
     * Установить текущее значение комбобокса
     *
     * @param value {@link DataValueOverlay DataValue}
     * @return RowListValue
     */
////    public static void setDataValue(ComboBoxBuilder instance, DataValue value) {
//        instance.setFieldValue(value);
//    }

    /**
     * Получить значение компонента в формате {@link ListModelDataOverlay ListModelData}.
     * (Аналогичный результат можно получить при помощи
     * comboboxBuilder.getDataValue().{@link DataValueOverlay#getListValue(DataValueImpl)
     * getListValue()} )
     *
     * @return {@link ListModelDataOverlay ListModelData}
     */
    public abstract ListModelData getValue();

    /**
     * Установить значение компонента в формате {@link ListModelDataOverlay ListModelData} .
     *
     * @param {@link ListModelDataOverlay model}
     */
    public abstract void setValue(ListModelData model);

    /**
     * Получить текст, отображаемый в области ввода комбобокса
     *
     * @return
     */
    public abstract String getText();

    public abstract boolean isRequired();

    /**
     * Сделать компонент обязательным для заполнения
     */
    public abstract void setRequired(boolean required);

    /**
     * Пометить комбобокс как невалидный и пометить компонент красным маркером с подсказкой message
     *
     * @param message - текст подсказки, появляющийся при наведении на восклицательный знак маркера
     */
    public abstract void markInvalid(String message);

    /**
     * Очистить информацию о невалидности компонента. Убрать все светящиеся красные подсказки об
     * ошибках с компонента.
     */
    public abstract void clearInvalid();

    /**
     * Установить фокус пользовательского ввода на комбобокс.
     */
    public abstract void focus();

    /**
     * Проверяет валидность заполнения поля
     *
     * @param invalidate - отображать информацию о невалидном значении
     * @return boolean
     */
    public abstract boolean isValid(boolean invalidate);
}
