package org.whirlplatform.component.client.data;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.client.loader.RpcProxy;
import org.whirlplatform.meta.shared.ClassLoadConfig;
import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.LoadData;
import org.whirlplatform.meta.shared.data.ListModelData;
import org.whirlplatform.rpc.client.DataServiceAsync;
import org.whirlplatform.rpc.shared.SessionToken;

public class ListClassProxy extends
    RpcProxy<ClassLoadConfig, LoadData<ListModelData>> {

    private final ClassMetadata metadata;

    public ListClassProxy(ClassMetadata metadata) {
        this.metadata = metadata;
    }

    @Override
    public void load(final ClassLoadConfig loadConfig,
                     final AsyncCallback<LoadData<ListModelData>> callback) {
        Scheduler.get().scheduleDeferred(new ScheduledCommand() {
            @Override
            public void execute() {
                DataServiceAsync.Util.getDataService(callback)
                    .getListClassData(SessionToken.get(), metadata,
                        loadConfig);
            }
        });

    }

}
