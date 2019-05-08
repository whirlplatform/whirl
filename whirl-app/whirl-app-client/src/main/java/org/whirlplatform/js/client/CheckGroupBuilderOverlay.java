package org.whirlplatform.js.client;

import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.check.CheckGroupBuilder;

/**
 * Группа чекбоксов. Чекбоксы назначаются в редакторе форм путём привязки DataSource.
 * <p>
 * Пример использования:
 * <pre>
 * var c = Whirl.Components.findByCode('cg1'); //CheckGroup
 * var rlv = c.getFieldValue(); //{@link RowListValueOverlay RowListValue}
 * var values = rlv.getValues(); // {@link RowValueOverlay RowValue}[]
 *
 * for(var o in values){
 * 	  var e = values[o]; //{@link RowValueOverlay RowValue}
 * 	  console.log(e.getId()+" c:"+e.isChecked()+" s:"+e.isSelected());
 *    }
 *
 * </pre>
 * </p>
 */
public abstract class CheckGroupBuilderOverlay {

    /**
     * Инициализация CheckGroup
     *
     * @param instance - CheckGroupBuilder
     * @return CheckGroupBuilder
     */
    public static CheckGroupBuilder create(CheckGroupBuilder instance) {
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
     * Установка кода CheckGroup
     *
     * @param name - String, код
     */
    public abstract void setCode(String name);

    /**
     * Получение кода CheckGroup
     *
     * @return String
     */
    public abstract String getCode();

    /**
     * Установка активности CheckGroup
     *
     * @param enabled - boolean
     */
    public abstract void setEnabled(boolean enabled);

    /**
     * Получение информации об активности CheckGroup
     *
     * @return boolean
     */
    public abstract boolean isEnabled();

    /**
     * Установка скрытности CheckGroup
     *
     * @param hidden - boolean
     */
    public abstract void setHidden(boolean hidden);

    /**
     * Получение информации о скрытности CheckGroup
     *
     * @return boolean
     */
    public abstract boolean isHidden();

    /**
     * Установка стиля CheckGroup
     *
     * @param styleName - String, название стиля
     */
    public abstract void setStyleName(String styleName);

    /**
     * Получение родительского компонента CheckGroup
     *
     * @param instance - CheckGroupBuilder
     * @return ComponentBuilder, компонент
     */
    public static ComponentBuilder getParent(CheckGroupBuilder instance) {
        return instance.getParentBuilder();
    }

    /**
     * Установить фокус на CheckGroup
     */
    public abstract void focus();

    /**
     * Возвращает список элементов группы ({@link RowListValueOverlay RowListValue}),
     *
     * @return объект типа {@link RowListValueOverlay RowListValue}
     */
////    public abstract RowListValue getFieldValue();
}
