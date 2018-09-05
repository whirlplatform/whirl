package org.whirlplatform.editor.shared.merge;

import org.whirlplatform.meta.shared.ApplicationStoreData;
import org.whirlplatform.meta.shared.editor.ApplicationElement;

import java.io.Serializable;
import java.util.List;

public class ApplicationsDiff implements Serializable {

    private static final long serialVersionUID = 4075305261674438797L;

    private List<ChangeUnit> changes;

    private ApplicationStoreData leftStoreData;
    private ApplicationElement left;

    private ApplicationStoreData rightStoreData;
    private ApplicationElement right;

    public ApplicationsDiff() {
    }

    public ApplicationsDiff(List<ChangeUnit> changes, ApplicationElement left, ApplicationElement right) {
        this.changes = changes;
        this.left = left;
        this.right = right;
    }

    public List<ChangeUnit> getChanges() {
        return changes;
    }

    public void setLeftStoreData(ApplicationStoreData leftStoreData) {
        this.leftStoreData = leftStoreData;
    }

    public ApplicationStoreData getLeftStoreData() {
        return leftStoreData;
    }

    public ApplicationElement getLeft() {
        return left;
    }

    public void setRightStoreData(ApplicationStoreData rightStoreData) {
        this.rightStoreData = rightStoreData;
    }

    public ApplicationStoreData getRightStoreData() {
        return rightStoreData;
    }

    public ApplicationElement getRight() {
        return right;
    }

}
