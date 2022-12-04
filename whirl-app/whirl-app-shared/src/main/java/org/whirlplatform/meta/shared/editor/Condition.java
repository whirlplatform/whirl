package org.whirlplatform.meta.shared.editor;

import org.whirlplatform.meta.shared.editor.condition.ConditionSolver;

public interface Condition {

    void accept(ConditionSolver solver);

}
