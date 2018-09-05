package org.whirlplatform.editor.client.component;

import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.sencha.gxt.cell.core.client.form.FieldCell;
import com.sencha.gxt.core.client.dom.XElement;

public class ThreeStateCheckCell extends FieldCell<CheckState> {

    public interface ThreeStateCheckAppearance extends FieldAppearance {

        void render(SafeHtmlBuilder sb, CheckState value);

    }

    public interface ThreeStateCheckResources extends ClientBundle {

        @Source("checked.gif")
        ImageResource checked();

        @Source("partialChecked.gif")
        ImageResource partialChecked();

        @Source("unchecked.gif")
        ImageResource unchecked();

    }

    public static class DefaultTreeStateCheckAppearance implements
            ThreeStateCheckAppearance {

        private ThreeStateCheckResources resources;

        public DefaultTreeStateCheckAppearance(
                ThreeStateCheckResources resources) {
            this.resources = resources;
        }

        @Override
        public void onEmpty(Element parent, boolean empty) {
        }

        @Override
        public void onFocus(Element parent, boolean focus) {
            Element child = parent.getFirstChildElement();
            if (focus) {
                child.focus();
            } else {
                child.blur();
            }
        }

        @Override
        public void onValid(Element parent, boolean valid) {
        }

        @Override
        public void setReadOnly(Element parent, boolean readonly) {
        }

        @Override
        public void render(SafeHtmlBuilder sb, CheckState value) {
            ImageResource image;
            if (value == CheckState.CHECKED) {
                image = resources.checked();
            } else if (value == CheckState.PARTIAL) {
                image = resources.partialChecked();
            } else {
                image = resources.unchecked();
            }
            sb.append(SafeHtmlUtils.fromTrustedString("<img src=\""
                    + image.getSafeUri().asString() + "\" width=\""
                    + image.getWidth() + "px;\" + height=\""
                    + image.getHeight() + "px;\"></img>"));
        }

    }

    private ThreeStateCheckAppearance appearance;

    public ThreeStateCheckCell() {
        this(
                new DefaultTreeStateCheckAppearance(
                        GWT.create(ThreeStateCheckResources.class)));
    }

    public ThreeStateCheckCell(ThreeStateCheckAppearance appearance) {
        super(appearance);
        this.appearance = appearance;
    }

    @Override
    public void onEmpty(XElement parent, boolean empty) {

    }

    @Override
    public void render(com.google.gwt.cell.client.Cell.Context context,
                       CheckState value, SafeHtmlBuilder sb) {
        this.appearance.render(sb, value);
    }

    @Override
    public void onBrowserEvent(com.google.gwt.cell.client.Cell.Context context,
                               Element parent, CheckState value, NativeEvent event,
                               ValueUpdater<CheckState> valueUpdater) {
        super.onBrowserEvent(context, parent, value, event, valueUpdater);

        if ("click".equals(event.getType())
                || ("keypress".equals(event.getType()) && event.getKeyCode() == KeyCodes.KEY_ENTER)) {
            CheckState v = nextState(value);
            valueUpdater.update(v);
            setValue(context, parent, v);
        }
    }

    private CheckState nextState(CheckState value) {
        if (value == CheckState.PARTIAL) {
            return CheckState.CHECKED;
        } else if (value == CheckState.CHECKED) {
            return CheckState.UNCHECKED;
        } else if (value == null || value == CheckState.UNCHECKED) {
            return CheckState.PARTIAL;
        } else {
            return CheckState.UNCHECKED;
        }

    }

}
