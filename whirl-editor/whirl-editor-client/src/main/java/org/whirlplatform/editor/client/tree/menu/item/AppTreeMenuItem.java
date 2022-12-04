package org.whirlplatform.editor.client.tree.menu.item;

import org.whirlplatform.editor.client.tree.AppTree;

/**
 * Пункт меню для работы с деревом приложения
 *
 * @param <T>
 */
public interface AppTreeMenuItem<T extends AppTree> {

    /**
     * Обновляет свое состояние - делает видимым, доступным и т.п. Как правило при реализации этого
     * метода требуется ссылка на дерево приложения
     */
    void updateState();

    /**
     * Устанавливает ссылку на дерево приложения
     *
     * @param appTree - Дерево приложения
     */
    void setAppTree(T appTree);
}
