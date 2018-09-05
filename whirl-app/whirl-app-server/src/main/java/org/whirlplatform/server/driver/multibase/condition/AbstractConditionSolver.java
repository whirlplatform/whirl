package org.whirlplatform.server.driver.multibase.condition;

import org.whirlplatform.meta.shared.editor.BooleanCondition;
import org.whirlplatform.meta.shared.editor.RightElement;
import org.whirlplatform.meta.shared.editor.RightType;
import org.whirlplatform.meta.shared.editor.condition.ConditionSolver;

import java.util.Collection;

@SuppressWarnings("serial")
public abstract class AbstractConditionSolver implements ConditionSolver {

    protected boolean allowed = false;

    /*
     * По-умолчанию если право хоть где-то дано, то оно действующее.
     */
    protected boolean needNext() {
        return !allowed;
    }

    @Override
    public void solve(BooleanCondition condition) {
        allowed = allowed || condition.getValue();
    }

    protected void checkAllowed(Collection<RightElement> rights, RightType type) {
        for (RightElement r : rights) {
            if (r.getType() != type) {
                continue;
            }
            r.getCondition().accept(this);
            if (!needNext()) {
                // если право дано хоть где-то, то оно действующее
                break;
            }
        }
    }

}
