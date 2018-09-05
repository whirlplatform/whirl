package org.whirlplatform.editor.shared.merge;

import org.whirlplatform.meta.shared.editor.ApplicationElement;

import java.util.List;

public interface Merger {

    void merge(ApplicationElement left, List<ChangeUnit> changes) throws MergeException;

}
