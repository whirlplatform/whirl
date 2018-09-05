package org.whirlplatform.meta.shared.editor.condition;

import org.whirlplatform.meta.shared.editor.BooleanCondition;
import org.whirlplatform.meta.shared.editor.SQLCondition;

import java.io.Serializable;

public interface ConditionSolver extends Serializable {
	
	void solve(BooleanCondition condition);
	
	void solve(SQLCondition condition);
	
	boolean allowed();
	
}
