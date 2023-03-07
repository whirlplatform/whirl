package org.whirlplatform.editor.client.view;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.tips.ToolTip;
import com.sencha.gxt.widget.core.client.tips.ToolTipConfig;

public class TooltipGenerator extends ToolTip {

    private ToolTipConfig tipConfig;
    private PropertyEditorView.CompositeCell property;
    private PropertyEditorView propertyEditorView;

    public TooltipGenerator(Widget target) {
        super(target);
        if (target instanceof PropertyEditorView.CompositeCell) {
            property = (PropertyEditorView.CompositeCell) target;

        } else if (target instanceof PropertyEditorView) {
            propertyEditorView = (PropertyEditorView) target;
        }

        tipConfig = new ToolTipConfig();
        tipConfig.setRenderer(new ToolTipConfig.ToolTipRenderer<Object>() {
            @Override
            public SafeHtml renderToolTip(Object data) {
                String src = "https://www.google.com";
                StringBuilder html = new StringBuilder("No tip");

                html.append("<iframe src=").append(src).append(">My frame</iframe>");

                return SafeHtmlUtils.fromSafeConstant(html.toString());
            }
        });

    }

}
