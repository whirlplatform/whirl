package org.whirlplatform.js.client;

import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.base.UploadFieldBuilder;

public abstract class UploadFieldBuilderOverlay {

    public static UploadFieldBuilder create(UploadFieldBuilder instance) {
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

    public abstract void setCode(String name);

    public abstract String getCode();

    public abstract void setEnabled(boolean enabled);

    public abstract boolean isEnabled();

    public abstract void setHidden(boolean hidden);

    public abstract void isHidden();

    public abstract void setStyleName(String styleName);

    public static boolean isEmpty(UploadFieldBuilder instance) {
        return instance.getValue() == null;
    }

    public abstract void setValue(String value);

    public abstract String getValue();

    public abstract void setRequired(boolean required);

    public abstract boolean isRequired();

    public abstract void clear();

    public static ComponentBuilder getParent(UploadFieldBuilder instance) {
        return instance.getParentBuilder();
    }

    public abstract void focus();

    /**
     * Проверяет валидность заполнения поля
     *
     * @param invalidate - отображать информацию о невалидном значении
     * @return boolean
     */
    public abstract boolean isValid(boolean invalidate);

    public abstract String getFileName();

    public abstract void markInvalid(String message);

    abstract void clearInvalid();
}
