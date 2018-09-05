package org.whirlplatform.meta.shared.editor;

import org.whirlplatform.meta.shared.editor.condition.ConditionSolver;

@SuppressWarnings("serial")
public class BooleanCondition extends AbstractCondition<Boolean> {

    public BooleanCondition() {
    }

    @Override
    public void accept(ConditionSolver solver) {
        solver.solve(this);
    }

    @Override
    public BooleanCondition clone() {
        BooleanCondition result = new BooleanCondition();
        result.setValue(super.getValue());
        return result;
    }
}
