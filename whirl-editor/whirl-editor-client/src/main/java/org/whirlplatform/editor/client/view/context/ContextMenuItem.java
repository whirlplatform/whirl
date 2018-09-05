package org.whirlplatform.editor.client.view.context;

import com.sencha.gxt.widget.core.client.menu.MenuItem;

/**
 * Декларация методов элемента управления Контекстный пункт меню.
 * <p>
 * Может быть добавлена в Контекстное меню непосредственно или в стандартное GXT
 * меню через метод asMenuItem. Предполагается что при реализации поведения и
 * изменения состояния Контекстного пункта меню будут использованы только методы
 * контекста. В идеале контекст - это интерфейс.
 *
 * @param <C> - контекст
 * @author bedritckiy_mr
 * @see HasContext
 * @see HasUpdatableState
 * @see ContextMenu
 */
public interface ContextMenuItem<C> extends HasContext<C>, HasUpdatableState {

    /**
     * Возвращает GXT представление. Позволяет добавить Контекстное меню в
     * стандартное GXT меню.
     *
     * @return объект типа GXT MenuItem
     */
    MenuItem asMenuItem();
}
