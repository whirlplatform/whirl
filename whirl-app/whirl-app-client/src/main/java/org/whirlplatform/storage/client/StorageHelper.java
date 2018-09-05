package org.whirlplatform.storage.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.SerializationException;
import com.seanchenxi.gwt.storage.client.StorageExt;
import com.seanchenxi.gwt.storage.client.StorageKeyFactory;
import com.seanchenxi.gwt.storage.client.StorageQuotaExceededException;
import com.seanchenxi.gwt.storage.client.serializer.StorageSerializer;
import com.sencha.gxt.core.client.util.Util;
import org.whirlplatform.component.client.BuilderManager;
import org.whirlplatform.component.client.state.StateScope;
import org.whirlplatform.meta.shared.ApplicationData;
import org.whirlplatform.meta.shared.ClientUser;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.data.DataValueImpl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class StorageHelper {

	/*
	 * TODO это фейковый сервис для создания сериализаторова GWT-RPC	
	 */
	@SuppressWarnings("unused")
	private static DataServiceStubAsync dataService = GWT.create(DataServiceStub.class);

    private static final String KEY_PREFIX = "whirl";

	public interface StorageWrapper<T> {

		boolean put(String code, T value);

		T get(String code);

		void remove(String code);

		void clear();

		StateScope getScope();

	}

	private static class StorageExtWrapper<T extends Serializable> implements
			StorageWrapper<T> {

		private StorageExt storage;
		private StateScope scope;

		public StorageExtWrapper(StorageExt storage, StateScope scope) {
			this.storage = storage;
			this.scope = scope;
		}

		@Override
		public boolean put(String code, T value) {
			assert !Util.isEmptyString(code) : "Code can not be empty to save in store";
			try {
				storage.put(StorageKeyFactory.serializableKey(getKey(code)),
						value);
				return true;
			} catch (StorageQuotaExceededException | SerializationException e) {
				return false;
			}
		}

		@SuppressWarnings("unchecked")
		@Override
		public T get(String code) {
			try {
				return (T) storage.get(StorageKeyFactory
						.serializableKey(getKey(code)));
			} catch (Exception e) {
				return null;
			}
		}

		@Override
		public void remove(String code) {
			storage.remove(StorageKeyFactory.serializableKey(getKey(code)));
		}

		@Override
		public void clear() {
			String prefix = getKey("");
			for (int i = 0; storage.size() < i; i++) {
				String code = storage.key(i);
				if (code.startsWith(prefix)) {
					storage.remove(StorageKeyFactory
							.serializableKey(getKey(code)));
				}
			}
		}

		@Override
		public StateScope getScope() {
			return scope;
		}

	}

	private static class MemoryStorageWrapper<T extends Serializable>
			implements StorageWrapper<T> {

		private static final StorageSerializer TYPE_SERIALIZER = GWT
				.create(StorageSerializer.class);
		private static Map<String, String> store = new HashMap<String, String>();

		@Override
		public boolean put(String code, T value) {
			assert !Util.isEmptyString(code) : "Code can not be empty to save in store";
			try {
				String v = TYPE_SERIALIZER.serialize(Serializable.class, value);
				store.put(getKey(code), v);
				return true;
			} catch (Exception e) {
				return false;
			}
		}

		@Override
		public T get(String code) {
			try {
				String v = store.get(getKey(code));
				return TYPE_SERIALIZER.deserialize(Serializable.class, v);
			} catch (Exception e) {
				return null;
			}
		}

		@Override
		public void remove(String code) {
			store.remove(getKey(code));
		}

		@Override
		public void clear() {
			String prefix = getKey("");
			Iterator<String> iter = store.keySet().iterator();
			while (iter.hasNext()) {
				String code = iter.next();
				if (code.startsWith(prefix)) {
					iter.remove();
				}
			}
		}

		@Override
		public StateScope getScope() {
			return StateScope.MEMORY;
		}

	}

	private static String getKey(String code) {
		ApplicationData application = BuilderManager.getApplicationData();
		assert application != null : "Application should be loaded to use StorageHelper";
		ClientUser user = ClientUser.getCurrentUser();
		assert user != null : "You should be authorized to use StorageHelper";
		return "/" + KEY_PREFIX + "/" + application.getApplicationCode() + "/"
				+ user.getLogin() + "/" + code;
	}

	public static <T extends Serializable> StorageWrapper<T> local() {
		StorageExt storage = StorageExt.getLocalStorage();
		return new StorageExtWrapper<T>(storage, StateScope.LOCAL);
	}

	public static <T extends Serializable> StorageWrapper<T> session() {
		StorageExt storage = StorageExt.getSessionStorage();
		return new StorageExtWrapper<T>(storage, StateScope.LOCAL);
	}

	public static <T extends Serializable> StorageWrapper<T> memory() {
		return new MemoryStorageWrapper<T>();
	}
	
	public static DataValue findStorageValue(String code) {
		DataValue data = StorageHelper.<DataValue> memory().get(code);
		if (data == null) {
			data = StorageHelper.<DataValue> session().get(code);
		}
		if (data == null) {
			data = StorageHelper.<DataValue> local().get(code);
		}
		if (data == null) {
			data = new DataValueImpl(DataType.STRING);
		}
		return data;
	}
}
