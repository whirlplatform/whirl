package org.whirlplatform.editor.client.view.context;

import com.sencha.gxt.widget.core.client.button.TextButton;

/**
 * Декларация методов элемента управления Контекстная кнопка.
 * <p>
 * Использование как стандартной GXT кнопки с помощью метода asTextButton.
 * Предполагается что при реализации поведения и изменения состояния Контекстной
 * кнопки будут использованы только методы контекста. В идеале контекст - это
 * интерфейс.
 *
 * @param <C> - класс или интерфейс контекста
 * @author bedritckiy_mr
 * @see HasContext
 * @see HasUpdatableState
 */
public interface ContextTextButton<C> extends HasContext<C>, HasUpdatableState {

    /**
     * Возвращает GXT представление. Позволяет добавить Контекстную кнопку как
     * стандартный GXT элемент TextButton.
     *
     * @return объект типа GXT TextButton
     */

    TextButton asTextButton();
}
