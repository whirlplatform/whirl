package org.whirlplatform.component.client;

import com.sencha.gxt.widget.core.client.Component;
import java.util.List;
import org.whirlplatform.meta.shared.data.DataValue;

public interface HasCreateParameters {

    Component create(List<DataValue> parameters);

}
