package org.whirlplatform.js.client;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportInstanceMethod;
import org.timepedia.exporter.client.ExportOverlay;
import org.timepedia.exporter.client.ExportPackage;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.combo.ComboBoxBuilder;
import org.whirlplatform.meta.shared.data.DataValueImpl;
import org.whirlplatform.meta.shared.data.ListModelData;

@Export("ComboBox")
@ExportPackage("Whirl")
public abstract class ComboBoxBuilderOverlay implements
        ExportOverlay<ComboBoxBuilder> {

    @ExportInstanceMethod
    @Deprecated
    public static ComboBoxBuilder create(ComboBoxBuilder instance) {
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

    @Export
    public abstract void setCode(String name);

    @Export
    public abstract String getCode();

    @Export
    public abstract void setEnabled(boolean enabled);

    @Export
    public abstract boolean isEnabled();

    @Export
    public abstract void setHidden(boolean hidden);

    @Export
    public abstract void isHidden();

    @Export
    public abstract void setStyleName(String styleName);


    /**
     * Очистить текущее значение комбобокса. Значения, отображаемые в выпадающем списке остаются доступными для выбора.
     */
    @Export
    public abstract void clear();

    @ExportInstanceMethod
    public static boolean isEmpty(ComboBoxBuilder instance) {
        return instance.getValue() == null;
    }

    /**
     * Получить значение компонента в формате {@link ListModelDataOverlay ListModelData}.
     * (Аналогичный результат можно получить при помощи comboboxBuilder.getDataValue().{@link DataValueOverlay#getListValue(DataValueImpl) getListValue()} )
     *
     * @return {@link ListModelDataOverlay ListModelData}
     */
    @Export
    public abstract ListModelData getValue();

    /**
     * Установить значение компонента в формате {@link ListModelDataOverlay ListModelData} .
     *
     * @param {@link ListModelDataOverlay model}
     */
    @Export
    public abstract void setValue(ListModelData model);

    /**
     * Получить текущее значение комбобокса.
     *
     * @return {@link DataValueOverlay DataValue}
     */
//    @ExportInstanceMethod
//    public static DataValue getDataValue(ComboBoxBuilder instance) {
//        return instance.getFieldValue();
//    }

    /**
     * Установить текущее значение комбобокса
     *
     * @param value {@link DataValueOverlay DataValue}
     * @return RowListValue
     */
//    @ExportInstanceMethod
//    public static void setDataValue(ComboBoxBuilder instance, DataValue value) {
//        instance.setFieldValue(value);
//    }

    /**
     * Получить текст, отображаемый в области ввода комбобокса
     *
     * @return
     */
    @Export
    public abstract String getText();

    /**
     * Сделать компонент обязательным для заполнения
     */
    @Export
    public abstract void setRequired(boolean required);

    @Export
    public abstract boolean isRequired();

    @ExportInstanceMethod
    public static ComponentBuilder getParent(ComboBoxBuilder instance) {
        return instance.getParentBuilder();
    }

    /**
     * Пометить комбобокс как невалидный и пометить компонент красным маркером с подсказкой message
     *
     * @param message - текст подсказки, появляющийся при наведении на восклицательный знак маркера
     */
    @Export
    public abstract void markInvalid(String message);

    /**
     * Очистить информацию о невалидности компонента. Убрать все светящиеся красные подсказки об ошибках с компонента.
     */
    @Export
    public abstract void clearInvalid();

    /**
     * Установить фокус пользовательского ввода на комбобокс.
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
     * Установить текст всплывающей подсказки
     *
     * @param toolTip
     */
    @ExportInstanceMethod
    public static void setToolTip(ComboBoxBuilder instance, String toolTip) {
        instance.getComponent().setToolTip(toolTip);
    }
}
