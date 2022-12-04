package org.whirlplatform.component.client;

public interface Validatable {

    /**
     * Получение информации о валидности
     *
     * @return boolean
     */
    boolean isValid();

    /**
     * Получение информации о валидности
     *
     * @param invalidate - boolean
     * @return boolean
     */
    boolean isValid(boolean invalidate);

    /**
     * Устанавливает компоненту сообщение о не валидности данных.
     *
     * @param msg - String
     */
    void markInvalid(String msg);

    /**
     * Очищает сообщение о не валидности.
     */
    void clearInvalid();

    /**
     * Получить информацию о свойстве "Обязателен для заполнения"
     *
     * @return boolean
     */
    boolean isRequired();

    /**
     * Установка свойства "Обязателен для заполнения"
     *
     * @param required - boolean
     */
    void setRequired(boolean required);
}
