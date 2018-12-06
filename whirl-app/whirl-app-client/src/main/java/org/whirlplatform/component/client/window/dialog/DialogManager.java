package org.whirlplatform.component.client.window.dialog;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.HTML;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.box.MessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import org.whirlplatform.component.client.selenium.Locator;
import org.whirlplatform.component.client.selenium.LocatorAware;
import org.whirlplatform.component.client.utils.DataUtils;
import org.whirlplatform.component.client.utils.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DialogManager {

    private static Map<Dialog, String> dialogs = new HashMap<>();

    public static Dialog createDialog(String dialogId, String headingText, String text,
                                      Pair<Dialog.PredefinedButton, SelectEvent.SelectHandler>... buttons) {
        Dialog dialog = new Dialog();
        dialog.setModal(true);
        dialog.setHeading(headingText);
        dialog.setWidget(new HTML(text));
        dialog.setHideOnButtonClick(true);
        if (buttons != null) {
            dialog.setPredefinedButtons(Pair.asArrayOfFirst(buttons, new PredefinedButton[0]));
            for (Pair<Dialog.PredefinedButton, SelectEvent.SelectHandler> p : buttons) {
                if (p.getFirst() != null && p.getSecond() != null) {
                    dialog.getButton(p.getFirst()).addSelectHandler(p.getSecond());
                }
            }
        }
        addHandler(dialogId, dialog);
        return dialog;
    }

    private static void addHandler(String dialogId, final Dialog dialog) {
        dialog.addHideHandler(new HideHandler() {

            @Override
            public void onHide(HideEvent event) {
                dialogs.remove(dialog);
            }
        });
        dialogs.put(dialog, dialogId);
    }

    public static MessageBox createMessage(String dialogId, String title, String message) {
        AlertMessageBox window = new AlertMessageBox(DataUtils.notNull(title, ""), DataUtils.notNull(message, ""));
        window.setWidth(500);
        window.setPredefinedButtons(PredefinedButton.OK);
        addHandler(dialogId, window);
        return window;
    }

    private static class LocatorParams {

        private static String TYPE_DIALOG = "Dialog";

        private static String PARAMETER_ID = "id";

        private static String TYPE_OK_BUTTON = "OkButton";
        private static String TYPE_CANCEL_BUTTON = "CancelButton";
        private static String TYPE_CLOSE_BUTTON = "CloseButton";
        private static String TYPE_YES_BUTTON = "YesButton";
        private static String TYPE_NO_BUTTON = "NoButton";

    }

    public static List<LocatorAware> getDialogLocators() {
        List<LocatorAware> result = new ArrayList<LocatorAware>();
        for (final Dialog d : dialogs.keySet()) {
            result.add(new LocatorAware() {

                @Override
                public Locator getLocatorByElement(Element element) {
                    if (!d.getElement().isOrHasChild(element)) {
                        return null;
                    }
                    Locator locator = new Locator(LocatorParams.TYPE_DIALOG);
                    locator.setParameter(LocatorParams.PARAMETER_ID, dialogs.get(d));

                    for (PredefinedButton bt : PredefinedButton.values()) {
                        TextButton b = d.getButton(bt);
                        if (b != null && b.getElement().isOrHasChild(element)) {
                            switch (bt) {
                                case OK:
                                    locator.setPart(new Locator(LocatorParams.TYPE_OK_BUTTON));
                                    break;
                                case CANCEL:
                                    locator.setPart(new Locator(LocatorParams.TYPE_CANCEL_BUTTON));
                                    break;
                                case CLOSE:
                                    locator.setPart(new Locator(LocatorParams.TYPE_CLOSE_BUTTON));
                                    break;
                                case YES:
                                    locator.setPart(new Locator(LocatorParams.TYPE_YES_BUTTON));
                                    break;
                                case NO:
                                    locator.setPart(new Locator(LocatorParams.TYPE_NO_BUTTON));
                                    break;
                            }
                        }
                    }
                    return locator;
                }

                @Override
                public Element getElementByLocator(Locator locator) {
                    if (!locator.hasParameter(LocatorParams.PARAMETER_ID)
                            || !locator.getParameter(LocatorParams.PARAMETER_ID).equals(dialogs.get(d))) {
                        return null;
                    }
                    Locator part = locator.getPart();
                    if (part != null) {
                        PredefinedButton bt = null;
                        if (LocatorParams.TYPE_OK_BUTTON.equals(part.getType())) {
                            bt = PredefinedButton.OK;
                        } else if (LocatorParams.TYPE_CANCEL_BUTTON.equals(part.getType())) {
                            bt = PredefinedButton.CANCEL;
                        } else if (LocatorParams.TYPE_CLOSE_BUTTON.equals(part.getType())) {
                            bt = PredefinedButton.CLOSE;
                        } else if (LocatorParams.TYPE_YES_BUTTON.equals(part.getType())) {
                            bt = PredefinedButton.YES;
                        } else if (LocatorParams.TYPE_NO_BUTTON.equals(part.getType())) {
                            bt = PredefinedButton.NO;
                        }
                        if (bt != null && d.getButton(bt) != null) {
                            TextButton b = d.getButton(bt);
                            return b.getCell().getFocusElement(b.getElement());
                        }
                    }
                    return null;
                }

                @Override
                public void fillLocatorDefaults(Locator locator, Element element) {

                }
            });
        }
        return result;
    }

}
