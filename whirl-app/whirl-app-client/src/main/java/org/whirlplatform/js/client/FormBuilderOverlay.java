package org.whirlplatform.js.client;

import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.form.FormBuilder;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.data.DataValueImpl;

/**
 * Работа с формами на стороне клиента
 */
public abstract class FormBuilderOverlay {

    public static FormBuilder create(FormBuilder instance) {
        instance.create();
        return instance;
    }

    /**
     * Установить дочерний компонент в ячейку формы, используя набор параметров ячейки формы data
     *
     * @param row     - строка, куда происходит добавление компонента
     * @param column  - столбец, куда происходит добавление компонента
     * @param builder - билдер добавляемого компонента
     */
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
    public static void clear(FormBuilder instance) {
        instance.clearContainer();
    }

    /**
     * Удалить конкретный компонент с формы
     *
     * @param builder
     */
    public static void remove(FormBuilder instance, ComponentBuilder builder) {
        instance.removeChild(builder);
    }

    public static ComponentBuilder getParent(FormBuilder instance) {
        return instance.getParentBuilder();
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

    /**
     * Перезагрузить форму, используя параметры.
     * Эти параметры будут использоваться для выполнения SQL-запросов в свойствах вроде whereSQL формы.
     *
     * @param parameters список параметров  {@link DataValueOverlay DataValue}
     */
    //    public static void loadParameters(FormBuilder instance,
    //                                      DataValue[] parameters) {
    //        instance.load(Arrays.asList(parameters));
    //    }

    //    public abstract void refresh();

    /**
     * Перезагрузить компоненты формы, зависящие от выполнения запросов SQL на форме.
     *
     * @param parameters - массив параметров для выполнения запросов на форме
     */
    //    public static void refreshParameters(FormBuilder instance,
    //                                         DataValue[] parameters) {
    //        instance.refresh(Arrays.asList(parameters));
    //    }
    public abstract boolean isHidden();

    public abstract void setHidden(boolean hidden);

    public abstract void setStyleName(String styleName);

    /**
     * Перезагрузить форму, обновить содержимое
     */
    public abstract void load();

    public abstract ComponentBuilder[] getChildren();

    public abstract void forceLayout();

    public abstract ComponentBuilder getChild(int row, int column);

    public abstract int getChildrenCount();

    public abstract void focus();

    public abstract void mask();

    public abstract void mask(String message);

    public abstract void unmask();
}
