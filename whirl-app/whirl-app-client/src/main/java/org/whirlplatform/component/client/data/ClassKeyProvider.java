package org.whirlplatform.component.client.data;

import com.sencha.gxt.data.shared.ModelKeyProvider;
import org.whirlplatform.meta.shared.data.RowModelData;

public class ClassKeyProvider implements ModelKeyProvider<RowModelData> {

    @Override
    public String getKey(RowModelData item) {
        return item.getId();
    }
}
