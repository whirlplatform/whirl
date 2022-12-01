package org.whirlplatform.editor.client.tree;

import com.google.gwt.core.client.Callback;
import java.util.List;
import java.util.Map;
import org.whirlplatform.editor.client.tree.cell.ComparableLeftTreeView;
import org.whirlplatform.editor.shared.merge.ChangeUnit;

public class ComparableLeftAppTreeWidget extends ComparableAppTreeWidget {

    public ComparableLeftAppTreeWidget() {
        super();
        setView(new ComparableLeftTreeView());
    }

    @Override
    protected void showDiff(List<ChangeUnit> changes,
                            Callback<Map<String, Boolean>, Throwable> callback) {
        showDiff(changes, true, callback);
    }
}
