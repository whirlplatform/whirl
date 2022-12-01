package org.whirlplatform.component.client.tree;

import com.sencha.gxt.widget.core.client.tree.Tree.CheckCascade;

public class CheckStyleHelper {

    private static final String NONE = "NONE";
    private static final String PARENTS = "PARENTS";
    private static final String CHILDREN = "CHILDREN";
    private static final String TRI = "TRI";

    public static CheckCascade parseTreePanelCheckStyle(String value) {
        if (NONE.equalsIgnoreCase(value)) {
            return CheckCascade.NONE;
        }
        if (PARENTS.equalsIgnoreCase(value)) {
            return CheckCascade.PARENTS;
        }
        if (TRI.equalsIgnoreCase(value)) {
            return CheckCascade.TRI;
        }
        return CheckCascade.CHILDREN;
    }

    public static CheckCascade parseTreeGridCheckStyle(String value) {
        if (NONE.equalsIgnoreCase(value)) {
            return CheckCascade.NONE;
        }
        if (PARENTS.equalsIgnoreCase(value)) {
            return CheckCascade.PARENTS;
        }
        if (TRI.equalsIgnoreCase(value)) {
            return CheckCascade.TRI;
        }
        return CheckCascade.CHILDREN;
    }

}
