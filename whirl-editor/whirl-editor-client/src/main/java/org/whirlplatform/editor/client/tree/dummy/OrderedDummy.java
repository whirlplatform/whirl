package org.whirlplatform.editor.client.tree.dummy;

/**
 * Для сортировки и сравнения Dummy элементов относительно друг друга. Позволяет зафиксировать
 * определенный порядок названий разделов в корне дерева приложения
 */
public interface OrderedDummy {

    /**
     * @return Порядковый номер заголовка
     */
    int getIndex();
}
