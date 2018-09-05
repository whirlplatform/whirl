package org.whirlplatform.editor.client.view.widget;

import org.whirlplatform.meta.shared.editor.ElementVisitor.VisitContext;

/**
 * Используется для создания отображаемого имени AbstractElement
 *
 * @author bedritckiy_mr
 */
public class DisplayNameVisitContext implements VisitContext {
    private String displayName = "";
    private String className = "";

    public DisplayNameVisitContext() {
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(final String displayName) {
        this.displayName = String.valueOf(displayName);
    }

    public void setDisplayName(final String first, final String second) {
        this.displayName = String.valueOf(first) + " : " + String.valueOf(second);
    }
}