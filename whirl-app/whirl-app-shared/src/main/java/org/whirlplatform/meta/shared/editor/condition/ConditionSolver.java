package org.whirlplatform.meta.shared.editor.condition;

import java.io.Serializable;
import org.whirlplatform.meta.shared.editor.BooleanCondition;
import org.whirlplatform.meta.shared.editor.SQLCondition;

public interface ConditionSolver extends Serializable {

    void solve(BooleanCondition condition);

    void solve(SQLCondition condition);

    boolean allowed();

}
