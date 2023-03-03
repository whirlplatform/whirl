package org.whirlplatform.editor.client.view;

import com.google.gwt.http.client.*;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.tips.ToolTip;
import com.sencha.gxt.widget.core.client.tips.ToolTipConfig;

import java.util.ArrayList;

import static com.google.gwt.core.client.GWT.getHostPageBaseURL;

public class HelpDecorator {

    private HelpDecorator() {
    }

    public static ArrayList<ToolTip> listOfToolTips = new ArrayList<>();

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
    }

    public static void enableTips() {
        for(ToolTip toolTip: listOfToolTips) {
            toolTip.enable();
        }
    }
}
