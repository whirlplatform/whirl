package org.whirlplatform.component.client.data;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.client.loader.RpcProxy;
import org.whirlplatform.meta.shared.ClassLoadConfig;
import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.LoadData;
import org.whirlplatform.meta.shared.data.RowModelData;
import org.whirlplatform.rpc.client.DataServiceAsync;
import org.whirlplatform.rpc.shared.SessionToken;

public class TableClassProxy extends
    RpcProxy<ClassLoadConfig, LoadData<RowModelData>> {

    private final ClassMetadata metadata;

    public TableClassProxy(ClassMetadata metadata) {
        this.metadata = metadata;
    }

    @Override
    public void load(final ClassLoadConfig loadConfig,
                     final AsyncCallback<LoadData<RowModelData>> callback) {
        Scheduler.get().scheduleDeferred(new ScheduledCommand() {
            @Override
            public void execute() {
                DataServiceAsync.Util.getDataService(callback)
                    .getTableClassData(SessionToken.get(), metadata,
                        loadConfig);
            }
        });
    }
}