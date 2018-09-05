package org.whirlplatform.js.client;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportInstanceMethod;
import org.timepedia.exporter.client.ExportOverlay;
import org.timepedia.exporter.client.ExportPackage;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.form.FormBuilder;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.data.DataValueImpl;

/**
 * Работа с формами на стороне клиента
 */
@Export("Form")
@ExportPackage("Whirl")
public abstract class FormBuilderOverlay implements ExportOverlay<FormBuilder> {

    @ExportInstanceMethod
    public static FormBuilder create(FormBuilder instance) {
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
    public abstract boolean isHidden();

    @Export
    public abstract void setStyleName(String styleName);

    /**
     * Перезагрузить форму, обновить содержимое
     */
    @Export
    public abstract void load();

    /**
     * Перезагрузить форму, используя параметры.
     * Эти параметры будут использоваться для выполнения SQL-запросов в свойствах вроде whereSQL формы.
     *
     * @param parameters список параметров  {@link DataValueOverlay DataValue}
     */
//    @ExportInstanceMethod
//    public static void loadParameters(FormBuilder instance,
//                                      DataValue[] parameters) {
//        instance.load(Arrays.asList(parameters));
//    }

//    @Export
//    public abstract void refresh();

    /**
     * Перезагрузить компоненты формы, зависящие от выполнения запросов SQL на форме.
     *
     * @param parameters - массив параметров для выполнения запросов на форме
     */
//    @ExportInstanceMethod
//    public static void refreshParameters(FormBuilder instance,
//                                         DataValue[] parameters) {
//        instance.refresh(Arrays.asList(parameters));
//    }

    /**
     * Установить дочерний компонент в ячейку формы, используя набор параметров ячейки формы data
     *
     * @param row     - строка, куда происходит добавление компонента
     * @param column  - столбец, куда происходит добавление компонента
     * @param builder - билдер добавляемого компонента
     */
    @ExportInstanceMethod
    public static void setChild(FormBuilder instance, int row, int column,
                                ComponentBuilder builder) {
        builder.setProperty(PropertyType.LayoutDataFormRow.getCode(),
                new DataValueImpl(DataType.NUMBER, row));
        builder.setProperty(PropertyType.LayoutDataFormColumn.getCode(),
                new DataValueImpl(DataType.NUMBER, column));
        instance.addChild(builder);
    }

    /**
     * Очистить форму от всех компонентов.
     */
    @ExportInstanceMethod
    public static void clear(FormBuilder instance) {
        instance.clearContainer();
    }

    /**
     * Удалить конкретный компонент с формы
     *
     * @param builder
     */
    @ExportInstanceMethod
    public static void remove(FormBuilder instance, ComponentBuilder builder) {
        instance.removeChild(builder);
    }

    @Export
    public abstract ComponentBuilder[] getChildren();

    @Export
    public abstract void forceLayout();

    @Export
    public abstract ComponentBuilder getChild(int row, int column);

    @ExportInstanceMethod
    public static ComponentBuilder getParent(FormBuilder instance) {
        return instance.getParentBuilder();
    }

    @Export
    public abstract int getChildrenCount();

    @Export
    public abstract void focus();

    @Export
    public abstract void mask();

    @Export
    public abstract void mask(String message);

    @Export
    public abstract void unmask();
}
