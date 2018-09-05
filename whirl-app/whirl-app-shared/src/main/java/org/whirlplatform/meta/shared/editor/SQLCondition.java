package org.whirlplatform.meta.shared.editor;

import org.whirlplatform.meta.shared.editor.condition.ConditionSolver;

@SuppressWarnings("serial")
public class SQLCondition extends AbstractCondition<String> {

    public SQLCondition() {
    }

    @Override
    public void accept(ConditionSolver solver) {
        solver.solve(this);
    }

    @Override
    public SQLCondition clone() {
        SQLCondition result = new SQLCondition();
        result.setValue(super.getValue());
        return result;
    }
}
