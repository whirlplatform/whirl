package org.whirlplatform.component.client.form;

import java.util.Comparator;
import org.whirlplatform.meta.shared.form.FormRowModel;

public class RowComparator implements Comparator<FormRowModel> {

    @Override
    public int compare(FormRowModel o1, FormRowModel o2) {
        int row1 = o1.getRow();
        int row2 = o2.getRow();
        if (row1 < row2) {
            return -1;
        } else if (row1 > row2) {
            return 1;
        }
        return 0;
    }

}