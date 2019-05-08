package org.whirlplatform.app.client;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ClosingEvent;
import com.google.gwt.user.client.Window.ClosingHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.whirlplatform.rpc.client.DataServiceAsync;
import org.whirlplatform.rpc.shared.SessionToken;

public class Whirl {

    // public static Logger logger = Logger.getLogger(Whirl.class.getName());

	private static boolean removeToken = true;

	static {
		// Ловим все не пойманные ошибки. Для отладки.
		// GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
		//
		// @Override
		// public void onUncaughtException(Throwable ex) {
		// logger.log(Level.WARNING, "Ex caught!", ex);
		// Window.alert(ex.getMessage() + ": "+ "\n" + ex.getStackTrace());
		// }
		// });

		// bugfix на Esc в Firefox
		// http://dev.vaadin.com/ticket/4200
		// http://code.google.com/p/google-web-toolkit/issues/detail?id=2193
		// Event.addNativePreviewHandler(new NativePreviewHandler() {
		// @Override
		// public void onPreviewNativeEvent(NativePreviewEvent e) {
		// if (e.getTypeInt() == Event.getTypeInt(KeyDownEvent.getType()
		// .getName())) {
		// NativeEvent nativeEvent = e.getNativeEvent();
		// if (nativeEvent.getKeyCode() == KeyCodes.KEY_ESCAPE) {
		// nativeEvent.preventDefault();
		// }
		// }
		// }
		// });

		final DataServiceAsync service = DataServiceAsync.Util.getDataService(new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(Void result) {
			}
		});
		ClientLoginUtils.initTouchTimer();

		Window.addWindowClosingHandler(new ClosingHandler() {
			@Override
			public void onWindowClosing(ClosingEvent event) {
				if (removeToken) {
					service.removeToken(SessionToken.get());
				}
			}
		});
	}

	public static void setRemoveToken(boolean remove) {
		removeToken = remove;
	}

}
