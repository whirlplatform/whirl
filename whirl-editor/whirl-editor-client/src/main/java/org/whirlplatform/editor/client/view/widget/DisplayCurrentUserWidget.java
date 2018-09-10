package org.whirlplatform.editor.client.view.widget;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.widget.core.client.Status;
import org.whirlplatform.editor.client.image.ComponentBundle;
import org.whirlplatform.meta.shared.ClientUser;

/**
 * Отображает имя текущего пользователя
 *
 */
public class DisplayCurrentUserWidget extends Status {

    public DisplayCurrentUserWidget() {
        super();
        setIcon(ComponentBundle.INSTANCE.userSmall());
        update();
    }

    public void update() {
        setHTML(buildDisplayData(ClientUser.getCurrentUser()));
    }

    private SafeHtml buildDisplayData(final ClientUser clientUser) {
        final String name = (clientUser != null) ? clientUser.getName() : "?";
        SafeHtmlBuilder sb = new SafeHtmlBuilder();
        sb.appendHtmlConstant("<span style='color:blue'>");
        sb.appendEscaped(name);
        sb.appendHtmlConstant("</span>");
        return sb.toSafeHtml();
    }
}
