package org.whirlplatform.editor.client.view;

import com.google.gwt.http.client.*;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.widget.core.client.tips.ToolTip;
import com.sencha.gxt.widget.core.client.tips.ToolTipConfig;

import java.util.ArrayList;

import static com.google.gwt.core.client.GWT.getHostPageBaseURL;

public class HelpDecorator {

    private HelpDecorator() {
    }

    public static ArrayList<ToolTip> listOfToolTips = new ArrayList<>();

    public static ArrayList<Widget> listOfTargetElements = new ArrayList<>();

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

                //


                if(type.substring(0, 9) == "component") {
                    toolTip.setTitle("component");
                } else if(type.substring(0, 8) == "property") {
                    toolTip.setTitle("property");
                } else {
                    //
                }

                listOfTargetElements.add(target);
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

    public static void disableTip(String type) {
        for(ToolTip toolTip: listOfToolTips) {
            toolTip.disable();
//            if(type == toolTip.getTitle()) {
//                toolTip.disable();
//            }
        }
//        for(int i = 0 ; i < listOfTargetElements.size(); ++i) {
//            if(listOfTargetElements.get(i).getClass() == currentElement.getClass()) {
//                listOfToolTips.get(i).disable();
//            }
//        }
        for(Widget widget: listOfTargetElements) {
            XElement element = (XElement) widget.getElement();
            element.applyStyles("border: none;");
            //element.applyStyles("color: black;");
        }
    }

    public static void enableTip(String type) {
        for(ToolTip toolTip: listOfToolTips) {
            toolTip.enable();
//            if(type == toolTip.getTitle()) {
//                toolTip.enable();
//            }
        }
        for(Widget widget: listOfTargetElements) {
            XElement element = (XElement) widget.getElement();
            //element.applyStyles("border: 2px solid green; position: relative;");
            element.applyStyles("border-bottom: 2px solid green; position: relative;");
            //element.applyStyles("border-bottom: 2px dashed green; position: relative;");
            //element.applyStyles("color: green;");
            //element.applyStyles("background-image: linear-gradient(to right, #25aae1, #40e495, #30dd8a, #2bb673);");
        }
        //        for(int i = 0 ; i < listOfToolTips.size(); ++i) {
//            ToolTip toolTip = listOfToolTips.get(i);
//            Widget element = listOfTargetElements.get(i);
//
//            if(currentElement.getClass() == element.getClass()) {
//                toolTip.enable();
//            }
//        }
    }

    public static void clearTips(){
        //listOfToolTips.clear();
        //listOfTargetElements.clear();
    }
}
