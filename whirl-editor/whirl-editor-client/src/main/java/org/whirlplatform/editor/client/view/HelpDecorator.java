package org.whirlplatform.editor.client.view;

import com.google.gwt.http.client.*;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.widget.core.client.tips.ToolTip;
import com.sencha.gxt.widget.core.client.tips.ToolTipConfig;
import org.whirlplatform.editor.client.view.toolbar.ToolBarView;

import java.util.ArrayList;

import static com.google.gwt.core.client.GWT.getHostPageBaseURL;

public class HelpDecorator {
    static ArrayList<ToolTip> listOfToolTips = new ArrayList<>();
    static ArrayList<Widget> listOfTargetElements = new ArrayList<>();

    public static void pinTips(Widget target, String type) {
        String elementType = "api/" + type.replaceFirst(":","") + ".html";
        RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, getHostPageBaseURL() + elementType);
        builder.setCallback(new RequestCallback() {
            @Override
            public void onResponseReceived(Request request, Response response) {
                String htm = "<div style=\"width: 300px;\">"+response.getText()+ "</div>";
                ToolTipConfig tipConfig = new ToolTipConfig();
                tipConfig.setBody(SafeHtmlUtils.fromSafeConstant(htm));
                ToolTip toolTip = new ToolTip(target, tipConfig);

                listOfToolTips.add(toolTip);
                listOfTargetElements.add(target);

                if(ToolBarView.toggleButton.getValue()) {
                    toolTip.enable();
                    XElement element = (XElement) target.getElement();
                    element.applyStyles("color: green;");
                } else {
                    toolTip.disable();
                    XElement element = (XElement) target.getElement();
                    element.applyStyles("color: black;");
                }
            }
            @Override
            public void onError(Request request, Throwable exception) {
            }
        });
        try {
            builder.send();
        } catch (RequestException e) {
            throw new RuntimeException(e);
        }
    }

    public static void disableTips() {
        for(ToolTip toolTip: listOfToolTips) {
            toolTip.disable();
        }
        for(Widget widget: listOfTargetElements) {
            XElement element = (XElement) widget.getElement();
            element.applyStyles("color: black;");
        }
    }

    public static void enableTips() {
        for(ToolTip toolTip: listOfToolTips) {
            toolTip.enable();
        }
        for(Widget widget: listOfTargetElements) {
            XElement element = (XElement) widget.getElement();
            element.applyStyles("color: green;");
        }
    }
}
