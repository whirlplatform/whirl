package org.geomajas.codemirror.client.widget;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextArea;
import org.geomajas.codemirror.client.CodeMirrorWrapper;
import org.geomajas.codemirror.client.Config;

public class CodeMirrorPanel extends SimplePanel {

    private CodeMirrorWrapper editor;
    private TextArea textArea;

    /**
     * Create an new codemirrorpanel with the default configuration.
     */
    public CodeMirrorPanel() {
        super();
        textArea = new TextArea();
        setWidget(textArea);
    }

    /**
     * Get the internal CodemirrorWrapper object.
     *
     * @return editor
     */
    public CodeMirrorWrapper getEditor() {
        return editor;
    }

    /**
     * Set the initial data to be displayed in the codemirror editor window.
     *
     * @param initialData
     */
    private void setInitialData(String initialData) {
        textArea.setValue(initialData);
    }

    public String getValue() {
        if (editor == null) {
            return textArea.getValue();
        } else {
            return editor.getContent();
        }
    }

    public void setValue(String value) {
        if (editor != null) {
            editor.setContent(value);
        }
        setInitialData(value);
    }

    public void showEditor(Config config) {
        if (getElement().getChildCount() > 1) {
            getElement().getChild(1).removeFromParent();
        }
        editor = CodeMirrorWrapper.fromTextArea(textArea,
                config == null ? Config.getDefault() : config);
        refreshCodeMirror();
    }

    private void refreshCodeMirror() {
        Scheduler.get().scheduleDeferred(new Command() {
            public void execute() {
                editor.refresh();
            }
        });
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        Style s = getElement().getStyle();
        s.setBorderWidth(1, Unit.PX);
        s.setBorderStyle(BorderStyle.SOLID);
        s.setBorderColor("#DDDDDD");
        s.setBackgroundColor("#FFFFFF");
    }
}
