package org.whirlplatform.rpc.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.Date;
import org.fusesource.restygwt.client.DirectRestService;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.REST;
import org.whirlplatform.rpc.shared.ClientRestException;
import org.whirlplatform.rpc.shared.DataService;
import org.whirlplatform.rpc.shared.ExceptionData;
import org.whirlplatform.rpc.shared.ExceptionSerializer;

/**
 * Передаваемые параметры типа List должны быть обернуты в ListHolder
 */
public interface DataServiceAsync extends DataService, DirectRestService {

    class Util {

        private static Date lastCall = new Date();

        // Нельзя переиспользовать один и тот же DataServiceAsync, т.к. в
        // экземпляре хранится callback.
        // Если GWT.create(DataServiceAsync.class) слишком затратная операция,
        // можно переделать на пул?
        public static <T> DataServiceAsync getDataService(final AsyncCallback<T> callback) {
            lastCall = new Date();
            DataServiceAsync dataService = GWT.create(DataServiceAsync.class);
            MethodCallback<T> mc = new MethodCallback<T>() {
                @Override
                public void onFailure(Method method, Throwable exception) {
                    ExceptionData data = null;
                    try {
                        data = ExceptionSerializer.Util.get()
                            .decode(method.getResponse().getText());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    exception.printStackTrace();
                    callback.onFailure(new ClientRestException(exception, data));
                }

                @Override
                public void onSuccess(Method method, T response) {
                    callback.onSuccess(response);
                }
            };

            return REST.withCallback(mc).call(dataService);
        }

        public static Date lastCallDate() {
            return lastCall;
        }
    }
}
