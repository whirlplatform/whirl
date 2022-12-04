package org.whirlplatform.editor.client.view.context;

import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

/**
 * Базовая реализация Контекстной кнопки. Предполагается что при наследовании будут реализованы:
 * <li>метод описывающий поведение при нажатии</li>
 * <li>метод изменяющий состояние</li>
 * <li>методы внешнего представления - источник для иконки, текста, всплывающей
 * подсказки</li>
 * <p/>
 *
 * @param <C> - класс или интерфейс контекста
 */
public abstract class AbstractContextTextButton<C> extends TextButton
        implements ContextTextButton<C> {
    private C context;

    public AbstractContextTextButton() {
        final String title = createButtonTitle();
        if (title != null) {
            this.setText(title);
        }
        final ImageResource icon = selectButtonIcon();
        if (icon != null) {
            this.setIcon(icon);
        }
        final String tooltip = createToolTip();
        if (tooltip != null) {
            this.setToolTip(tooltip);
        }
        final SelectHandler handler = createSelectHandler();
        if (handler != null) {
            this.addSelectHandler(handler);
        }
    }

    public AbstractContextTextButton(final C context) {
        this();
        setContext(context);
    }

    @Override
    public C getContext() {
        return context;
    }

    @Override
    public void setContext(final C context) {
        this.context = context;
    }

    @Override
    public TextButton asTextButton() {
        return this;
    }

    protected abstract SelectHandler createSelectHandler();

    protected abstract String createButtonTitle();

    protected abstract String createToolTip();

    protected abstract ImageResource selectButtonIcon();
}
