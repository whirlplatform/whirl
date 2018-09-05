package org.whirlplatform.component.client.form;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.RootPanel;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import org.whirlplatform.component.client.selenium.Locator;
import org.whirlplatform.component.client.selenium.LocatorAware;
import org.whirlplatform.meta.shared.FieldMetadata;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.i18n.AppMessage;

import java.util.List;

public class FieldFormWindow extends Window implements LocatorAware {

    private static final double MAX_HEIGHT_PERCENT = 0.85;
    private FieldFormPanel fieldPanel;
    private String code;
    private TextButton saveBtn = new TextButton(AppMessage.Util.MESSAGE.save());
    private TextButton closeBtn = new TextButton(AppMessage.Util.MESSAGE.close());

    public FieldFormWindow(List<FieldMetadata> fields, boolean viewOnly, String code) {
        super();
        setCode(code);
        fieldPanel = new FieldFormPanel(fields, viewOnly);
        setClosable(false);
        setOnEsc(true);
        setBodyBorder(true);
        setWidget(fieldPanel);
        setWidth(750);
        if (!viewOnly) {
            getButtonBar().add(saveBtn);
        }
        getButtonBar().add(closeBtn);
        closeBtn.addSelectHandler(new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                hide();
            }
        });
        int height = getHeader().getOffsetHeight() + fieldPanel.getOffsetHeight() + getButtonBar().getOffsetHeight();
        int maxHeight = (int) Math.round(RootPanel.get().getOffsetHeight() * MAX_HEIGHT_PERCENT);
        height = (maxHeight < height) ? height : maxHeight;
        setHeight(height);
        setModal(true); // Т.к. если окно не модальное, всё разблокируется
    }

    public void setValue(FieldMetadata field, DataValue value) {
        fieldPanel.setValue(field, value);
    }

    public DataValue getValue(FieldMetadata field) {
        return fieldPanel.getValue(field);
    }

    public boolean isChanged(FieldMetadata field) {
        return fieldPanel.isChanged(field);
    }

    public boolean isValid() {
        return fieldPanel.isValid();
    }

    public HandlerRegistration addSaveHandler(SelectHandler handler) {
        return saveBtn.addSelectHandler(handler);
    }

    public HandlerRegistration addCloseHandler(SelectHandler handler) {
        return closeBtn.addSelectHandler(handler);
    }

    public void setUploadCommand(Command command) {
        fieldPanel.setUploadCommand(command);
    }

    public boolean checkUpload() {
        return fieldPanel.checkUpload();
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    // Selenium

    static class LocatorParams {
        public static String PARAMETER_CODE = "code";
        public static String TYPE_FIELD_FORM = "FieldForm";
        public static String TYPE_SAVE_BUTTON = "SaveButton";
        public static String TYPE_CLOSE_BUTTON = "CloseButton";
    }

    @Override
    public Locator getLocatorByElement(Element element) {
        Locator result = null;
        if (getElement().isOrHasChild(element)) {
            result = new Locator(LocatorParams.TYPE_FIELD_FORM);
            fillLocatorDefaults(result, element);
            Locator part = null;
            if (saveBtn.getElement().isOrHasChild(element)) {
                part = new Locator(LocatorParams.TYPE_SAVE_BUTTON);
            } else if (closeBtn.getElement().isOrHasChild(element)) {
                part = new Locator(LocatorParams.TYPE_CLOSE_BUTTON);
            } else {
                part = fieldPanel.getLocatorByElement(element);
            }
            result.setPart(part);
        }
        return result;
    }

    @Override
    public void fillLocatorDefaults(Locator locator, Element element) {
        locator.setParameter(LocatorParams.PARAMETER_CODE, getCode());
    }

    @Override
    public Element getElementByLocator(Locator locator) {
        Element result = null;
        if (isAcceptable(locator)) {
            Locator part = locator.getPart();
            if (part.typeEquals(LocatorParams.TYPE_SAVE_BUTTON) && saveBtn != null) {
                result = getTextButtonElement(saveBtn);
            } else if (part.typeEquals(LocatorParams.TYPE_CLOSE_BUTTON) && closeBtn != null) {
                result = getTextButtonElement(closeBtn);
            } else {
                result = fieldPanel.getElementByLocator(part);
            }
            if (result == null) {
                result = this.getElement();
            }
        }
        return result;
    }

    private boolean isAcceptable(final Locator locator) {
        if (locator == null || locator.getPart() == null || !locator.hasParameter(LocatorParams.PARAMETER_CODE)) {
            return false;
        }
        boolean typeIsOk = locator.typeEquals(LocatorParams.TYPE_FIELD_FORM);
        boolean codeIsOk = getCode().equals(locator.getParameter(LocatorParams.PARAMETER_CODE));
        return typeIsOk && codeIsOk;
    }

    private XElement getTextButtonElement(final TextButton button) {
        return (button != null) ? button.getCell().getFocusElement(button.getElement()) : null;
    }
}
