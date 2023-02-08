package org.whirlplatform.component.client.data;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.client.loader.RpcProxy;
import java.util.List;
import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.TreeClassLoadConfig;
import org.whirlplatform.meta.shared.data.RowModelData;
import org.whirlplatform.rpc.client.DataServiceAsync;
import org.whirlplatform.rpc.shared.SessionToken;

public class TreeClassProxy extends
    RpcProxy<TreeClassLoadConfig, List<RowModelData>> {

    private final ClassMetadata metadata;

    public TreeClassProxy(ClassMetadata metadata) {
        this.metadata = metadata;
    }

    @Override
    public void load(final TreeClassLoadConfig loadConfig,
                     final AsyncCallback<List<RowModelData>> callback) {
        Scheduler.get().scheduleDeferred(new ScheduledCommand() {
            @Override
            public void execute() {
                DataServiceAsync.Util.getDataService(callback)
                    .getTreeClassData(SessionToken.get(), metadata,
                        loadConfig);
            }
        });
    }

}
