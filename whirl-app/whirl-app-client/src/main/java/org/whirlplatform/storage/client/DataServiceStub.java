package org.whirlplatform.storage.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.data.ListModelData;
import org.whirlplatform.meta.shared.data.RowListValue;
import org.whirlplatform.meta.shared.data.RowModelData;

@RemoteServiceRelativePath("fake")
public interface DataServiceStub extends RemoteService {

    DataValue dataValueStub();

    RowListValue rowListValueStub();

    ListModelData listModelDataSub();

    RowModelData rowModelDataSub();

}