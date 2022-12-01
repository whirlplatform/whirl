package org.whirlplatform.editor.shared.merge;

import java.util.List;
import org.whirlplatform.meta.shared.editor.ApplicationElement;

public interface Merger {

    void merge(ApplicationElement left, List<ChangeUnit> changes) throws MergeException;

}
