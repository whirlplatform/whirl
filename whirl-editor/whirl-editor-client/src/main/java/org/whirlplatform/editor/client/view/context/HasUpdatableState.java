package org.whirlplatform.editor.client.view.context;

/**
 * Декларирует что состояние элемента имплементирующего данный интерфейс может
 * изменятся с помощью вызова метода updateState
 *
 * @author bedritckiy_mr
 */
public interface HasUpdatableState {

    /**
     * Обновляет состояние элемента - например делает его enabled или disabled.
     */
    void updateState();
}
