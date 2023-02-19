package org.whirlplatform.editor.client.view;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.tips.ToolTip;
import com.sencha.gxt.widget.core.client.tips.ToolTipConfig;

public class HelpDecorator {
    private HelpDecorator() {
    }

    public static void pinTips(Widget target, String type) {
        String propertyType = "api/" + type + ".html";
        ToolTipConfig tipConfig = new ToolTipConfig();

        ToolTipConfig.ToolTipRenderer renderer = data -> {
            StringBuilder html = new StringBuilder();
            String style = "<style type=\"text/css\">"
                    +" iframe {"
                    +" width: 100%;"
                    +" height: 65px;"
                    +" border:none;"
                    +" }"
                    +" </style>";
            html
                .append(style)
                .append("<iframe src=").append(propertyType)
                .append(" loading=\"lazy\"")
                .append(" scrolling=\"no\"")
                .append("></iframe>");
            return SafeHtmlUtils.fromSafeConstant(html.toString());
        };
        tipConfig.setRenderer(renderer);
        ToolTip toolTip = new ToolTip(target, tipConfig);
    }
}
