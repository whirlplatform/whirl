package org.whirlplatform.editor.client.presenter.compare;

import org.whirlplatform.editor.shared.merge.ChangeUnit;

import java.util.ArrayList;
import java.util.List;

/**
 * @author bedritckiy_mr
 */
public class ChangesSorterEntry {
    private final List<ChangeUnit> changeUnits;
    private ElementChangeState changeState;

    public ChangesSorterEntry() {
        changeUnits = new ArrayList<>();
        changeState = ElementChangeState.NONE;
    }

    public List<ChangeUnit> getChangeUnits() {
        return changeUnits;
    }

    public void addChangeUnit(ChangeUnit changeUnit) {
        if (changeUnit != null) {
            this.changeUnits.add(changeUnit);
        }
    }

    public int numberOfChanges() {
        return changeUnits.size();
    }

    public ElementChangeState getChangeState() {
        return changeState;
    }

    public void setChangeState(ElementChangeState changeState) {
        this.changeState = changeState;
    }
}
