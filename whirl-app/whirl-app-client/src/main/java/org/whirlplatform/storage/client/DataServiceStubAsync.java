package org.whirlplatform.storage.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.data.ListModelData;
import org.whirlplatform.meta.shared.data.RowListValue;
import org.whirlplatform.meta.shared.data.RowModelData;

public interface DataServiceStubAsync {

    void dataValueStub(AsyncCallback<DataValue> callback);

    void rowListValueStub(AsyncCallback<RowListValue> callback);

    void listModelDataSub(AsyncCallback<ListModelData> callback);

    void rowModelDataSub(AsyncCallback<RowModelData> callback);
}