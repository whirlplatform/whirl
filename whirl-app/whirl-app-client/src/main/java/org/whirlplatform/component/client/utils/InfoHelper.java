package org.whirlplatform.component.client.utils;


import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.rpc.StatusCodeException;
import com.sencha.gxt.widget.core.client.box.MessageBox;
import com.sencha.gxt.widget.core.client.info.DefaultInfoConfig;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.info.InfoConfig;
import org.whirlplatform.component.client.window.dialog.DialogManager;
import org.whirlplatform.meta.shared.i18n.AppMessage;
import org.whirlplatform.rpc.shared.ClientRestException;
import org.whirlplatform.rpc.shared.ExceptionData;

public class InfoHelper {

    private static MessageBox create(String dialogId, String title, String message) {
        return DialogManager.createMessage(dialogId, title, message);
    }

    public static void info(String dialogId, String title, String message) {
        dialogId = dialogId + "-info";
        MessageBox window = create(dialogId, title, message);
        window.setIcon(MessageBox.ICONS.info());
        window.show();
    }

    public static void warning(String dialogId, String title, String message) {
        dialogId = dialogId + "-warning";
        MessageBox window = create(dialogId, title, message);
        window.setIcon(MessageBox.ICONS.warning());
        window.show();
    }

    public static void error(String dialogId, String title, String message) {
        dialogId = dialogId + "-error";
        MessageBox window = create(dialogId, title, message);
        window.setIcon(MessageBox.ICONS.error());
        window.show();
    }

    public static void display(String title, String message) {
        InfoConfig config = new DefaultInfoConfig(title == null ? "" : title, message);
        Info.display(config);
    }

    private static void displayReloadPage(String dialogId, String title, String message) {
        SafeHtmlBuilder builder = new SafeHtmlBuilder();
        builder.appendHtmlConstant(message)
                .appendHtmlConstant(
                        "<br/><a href=\"#\" onclick=\"window.location.reload(true);\" style=\"color:#7777FF\">")
                .appendHtmlConstant(AppMessage.Util.MESSAGE.reloadPage())
                .appendHtmlConstant("</a>");
        SafeHtml titleHtml = SafeHtmlUtils.fromString(title == null ? "" : title);
        // InfoConfig config = new DefaultInfoConfig(titleHtml,
        // builder.toSafeHtml());
        // config.setDisplay(20000);
        // Info.display(config);
        MessageBox window = create(dialogId, title, builder.toSafeHtml().asString());
        window.setIcon(MessageBox.ICONS.warning());
        window.show();
    }

    public static void throwInfo(String dialogId, Throwable exception) {
        dialogId = dialogId + "-throw";
        if (exception instanceof StatusCodeException) {
            StatusCodeException sex = (StatusCodeException) exception;
            if (sex.getStatusCode() == 0) {
                error(dialogId, AppMessage.Util.MESSAGE.alert(),
                        AppMessage.Util.MESSAGE.errorServerConnection());
            } else {
                error(dialogId, AppMessage.Util.MESSAGE.alert(), exception.getMessage());
            }
        } else if (exception instanceof ClientRestException &&
                ((ClientRestException) exception).getData() != null) {
            ExceptionData rpc = ((ClientRestException) exception).getData();
            // сессиия истекла
            if (rpc.isSessionExpired()) {
                displayReloadPage(dialogId, AppMessage.Util.MESSAGE.alert(),
                        exception.getMessage());
            } else if (rpc.getType() == ExceptionData.ExceptionType.SIMPLE) {
                error(dialogId, AppMessage.Util.MESSAGE.alert(), exception.getMessage());
            } else {
                info(dialogId, AppMessage.Util.MESSAGE.info(), exception.getMessage());
            }
        } else {
            error(dialogId, AppMessage.Util.MESSAGE.alert(), exception.getMessage());
        }
    }

}
