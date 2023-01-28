package org.whirlplatform.editor.client.tree.toolbar;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.widget.core.client.Status;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.toolbar.FillToolItem;
import com.sencha.gxt.widget.core.client.toolbar.SeparatorToolItem;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;
import org.whirlplatform.editor.client.image.ComponentBundle;
import org.whirlplatform.editor.client.tree.AppTree;
import org.whirlplatform.editor.client.tree.menu.AppTreeMenu;

/**
 * Базовая реализация панели инструментов дерева приложения. Состоит из статусной строки с именем и
 * версией загруженного приложения Статус располагается слева, кнопки, при добавлении их в
 * реализациях класса, будут добавляться справа. Статусная строка форматируется различным образом в
 * зависимости от того были ли приложение в дереве отредактировано или нет. Изменение форматирование
 * производится в этом случае вызовом метода setChanged
 *
 * @param <T> - наследник AppTree
 */
public abstract class AbstractAppTreeToolBar<T extends AppTree> extends ToolBar {
    private static final ImageResource CONTEXT_ICON = ComponentBundle.INSTANCE.context();
    private static final int H_SPACING = 5;
    private final Status status;
    private final TextButton contextButton;
    protected T appTree;
    private AppTreeMenu<T> contextButtonMenu;

    public AbstractAppTreeToolBar() {
        super();
        setSpacing(0);
        setHorizontalSpacing(H_SPACING);
        status = new Status();
        status.setText("  ");
        add(status);
        add(new FillToolItem());
        contextButton = new TextButton("", CONTEXT_ICON);
    }

    /**
     * Устанавливает ссылку на дерево приложения
     *
     * @param appTree - Дерево приложения
     */
    public void setAppTree(final T appTree) {
        this.appTree = appTree;
        setVisible(true);
        setLayoutData(new VerticalLayoutData(1, -1));
        status.setHTML(buildStatus(false));
        contextButtonMenu.setAppTree(appTree);
        forceLayout();
    }

    /**
     * Устанавливает меню дерева приложений для контекстной кнопки
     *
     * @param menu - меню дерева приложений
     */
    public void setContextButtonMenu(AppTreeMenu<T> menu) {
        contextButtonMenu = menu;
        contextButton.setMenu(contextButtonMenu.asMenu());
    }

    /**
     * Устанавливает досупность кнопки меню
     */
    public void setContextButtonEnabled(boolean enabled) {
        contextButton.setEnabled(enabled);
    }

    /**
     * Добавляет разделитель
     */
    public void addSeparator() {
        add(new SeparatorToolItem());
    }

    /**
     * Добавляет контекстную кнопку в панель инструментов
     */
    public void addContextButton() {
        add(contextButton);
    }

    /**
     * Форматирует заголовок в зависимости от состояния приложения
     */
    public void setStatusAsChanged(boolean applicationChanged) {
        status.setHTML(buildStatus(applicationChanged));
    }

    /**
     * Очищает статусную строку с именем и версией приложения
     */
    public void clearStatus() {
        status.setText(" ");
    }

    /**
     * Строит заголовок приложения
     *
     * @param applicationChanged - признак того что в приложение вносились изменения
     * @return Форматированный заголовок приложения
     */
    private SafeHtml buildStatus(boolean applicationChanged) {
        SafeHtmlBuilder builder = new SafeHtmlBuilder();
        if (applicationChanged) {
            builder.appendHtmlConstant("<span style='color:blue'>");
        }
        builder.appendHtmlConstant("<b>");
        builder.appendEscaped(buildStatusText(applicationChanged));
        builder.appendHtmlConstant("</b>");
        if (!applicationChanged) {
            builder.appendHtmlConstant("</span>");
        }
        return builder.toSafeHtml();
    }

    private String buildStatusText(boolean applicationChanged) {
        StringBuilder sb = new StringBuilder(getAppCode());
        String version = getAppVersion();
        if (!"".equals(version)) {
            sb.append(" [").append(version).append("]");
        }
        if (applicationChanged) {
            sb.append("*");
        }
        return sb.toString();
    }

    private String getAppCode() {
        return (appTree != null && appTree.getApplication() != null)
            ? appTree.getApplication().getCode() : "";
    }

    private String getAppVersion() {
        return (appTree != null && appTree.getVersion() != null) ? appTree.getVersion().toString() :
                "";
    }
}
