package org.whirlplatform.editor.client.view.context;

/**
 * Декларирует что поведение и состояние элемента имплементирующего данный
 * интерфейс зависит от некоторого контекста и/или элемент использует методы
 * контекста в своих целях. В идеальном случае контекст - тоже интерфейс.
 *
 * @param <C> - класс контекста
 * @author bedritckiy_mr
 */
public interface HasContext<C> {

    /**
     * Устанавливает контекст
     *
     * @param context - экземпляр контекстного класса
     */
    void setContext(C context);

    /**
     * Возвращает контекст
     *
     * @return контекст элемента
     */
    C getContext();
}
