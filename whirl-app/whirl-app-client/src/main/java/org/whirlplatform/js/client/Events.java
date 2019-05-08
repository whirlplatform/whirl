package org.whirlplatform.js.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import org.whirlplatform.component.client.event.EventCallbackResult;
import org.whirlplatform.component.client.event.EventHelper;
import org.whirlplatform.component.client.event.EventManager;
import org.whirlplatform.meta.shared.EventMetadata;
import org.whirlplatform.meta.shared.data.EventParameterImpl;
import org.whirlplatform.rpc.client.DataServiceAsync;
import org.whirlplatform.rpc.shared.SessionToken;

public class Events {

    public interface EventClosure {

        void success(EventCallbackResult result);

        void fail(String err);

    }

    public static void execute(final String eventCode,
                               final EventParameterImpl[] params, final EventClosure successFunction,
                               final EventClosure failFunction) {
        DataServiceAsync.Util.getDataService(new AsyncCallback<EventMetadata>() {

            @Override
            public void onFailure(Throwable caught) {
                failFunction.fail(caught.getMessage());
            }

            @Override
            public void onSuccess(EventMetadata result) {
                if (result == null) {
                    failFunction.fail("Event not found: "
                            + eventCode);
                    return;
                }
                for (EventParameterImpl param : params) {
                    result.addParameter(param);
                }
                EventHelper helper = EventManager.Util.get().wrapEvent(result);
                helper.setAfterEvent(new AsyncCallback<EventCallbackResult>() {

                    @Override
                    public void onSuccess(EventCallbackResult result) {
                        successFunction.success(result);
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        failFunction.fail(caught.getMessage());
                    }
                });
                helper.onEvent(null);
            }

        }).getFreeEvent(SessionToken.get(), eventCode);
    }
}
