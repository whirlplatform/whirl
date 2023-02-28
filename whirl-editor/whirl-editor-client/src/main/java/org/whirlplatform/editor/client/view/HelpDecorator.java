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
        String propertyType = "api/"+type+".html";
        ToolTipConfig tipConfig = new ToolTipConfig();

        ToolTipConfig.ToolTipRenderer renderer = new ToolTipConfig.ToolTipRenderer() {
            @Override
            public SafeHtml renderToolTip(Object data) {
                StringBuilder html = new StringBuilder();

                String width = "\"100%\"";
                String height = "\"100%\"";
                String style = "border:3px solid black;";

                html.append("<iframe src=").append(propertyType)
                    .append(" width=").append(width)
                    .append(" height=").append(height)
                    .append("style=").append(style)
                    .append(" seamless")
                    .append(" allowfullscreen=\"\" loading=\"lazy\" referrerpolicy=\"no-referrer-when-downgrade\"")
                    .append(" frameborder=\"0\" scrolling=\"no\"")
                    .append(" align=\"absmiddle\"")
                    .append("></iframe>");
                return SafeHtmlUtils.fromSafeConstant(html.toString());
            }
        };
        tipConfig.setRenderer(renderer);
        ToolTip toolTip = new ToolTip(target, tipConfig);
    }
}
