package org.whirlplatform.editor.client.view;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.event.FocusEvent;
import com.sencha.gxt.widget.core.client.tips.ToolTip;
import com.sencha.gxt.widget.core.client.tips.ToolTipConfig;

import java.util.ArrayList;

public class HelpDecorator {

    private HelpDecorator() {
    }

    public static ArrayList<ToolTip> listOfToolTips = new ArrayList<>();

    public static ArrayList<ToolTip> listOfComponentToolTips = new ArrayList<>();
    public static ArrayList<ToolTip> listOfPropertyToolTips = new ArrayList<>();

    public static void pinPropertyTips(Widget target, String type) {
        String propertyType = "api/propertytype/" + type + ".html";

        ToolTipConfig tipConfig = new ToolTipConfig();
        ToolTipConfig.ToolTipRenderer renderer = new ToolTipConfig.ToolTipRenderer() {
            @Override
            public SafeHtml renderToolTip(Object data) {
                StringBuilder html = new StringBuilder();
                String style = "<style type=\"text/css\">"
                        + " iframe {"
                        + " width: 100%;"
                        + " height: 75px;"
                        + " border:none;"
                        + " }"
                        + " </style>";
                html
                        .append(style)
                        .append("<iframe src=").append(propertyType)
                        .append(" loading=\"lazy\"")
                        .append(" scrolling=\"no\"")
                        .append("></iframe>");
                return SafeHtmlUtils.fromSafeConstant(html.toString());
            }
        };

        tipConfig.setRenderer(renderer);
        ToolTip toolTip = new ToolTip(target, tipConfig);
        listOfToolTips.add(toolTip);
    }

    public static void pinComponentTips(Widget target, String type) {
        String componentType = "api/componenttype/" + type + ".html";

        ToolTipConfig tipConfig = new ToolTipConfig();
        ToolTipConfig.ToolTipRenderer renderer = new ToolTipConfig.ToolTipRenderer() {
            @Override
            public SafeHtml renderToolTip(Object data) {
                StringBuilder html = new StringBuilder();
                String style = "<style type=\"text/css\">"
                        + " iframe {"
                        + " width: 100%;"
                        + " height: 120px;"
                        + " border:none;"
                        + " }"
                        + " </style>";
                html
                        .append(style)
                        .append("<iframe src=").append(componentType)
                        .append(" loading=\"lazy\"")
                        .append(" scrolling=\"no\"")
                        .append("></iframe>");
                return SafeHtmlUtils.fromSafeConstant(html.toString());
            }
        };

        tipConfig.setRenderer(renderer);
        ToolTip toolTip = new ToolTip(target, tipConfig);
        listOfToolTips.add(toolTip);
    }

    public static void disableTips() {
        for(ToolTip toolTip: listOfToolTips) {
            toolTip.disable();
        }
    }

    public static void enableTips() {
        for(ToolTip toolTip: listOfToolTips) {
            toolTip.enable();
        }
    }
}
