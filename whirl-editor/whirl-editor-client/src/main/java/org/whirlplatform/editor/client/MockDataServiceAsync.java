package org.whirlplatform.editor.client;

import com.google.gwt.user.client.Random;
import org.whirlplatform.meta.shared.*;
import org.whirlplatform.meta.shared.component.ComponentModel;
import org.whirlplatform.meta.shared.data.*;
import org.whirlplatform.meta.shared.form.FormModel;
import org.whirlplatform.rpc.shared.DataService;
import org.whirlplatform.rpc.shared.ListHolder;
import org.whirlplatform.rpc.shared.SessionToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MockDataServiceAsync implements DataService {

	public MockDataServiceAsync() {
	}

	@Override
	public ClientUser getUser(SessionToken token, boolean newSession, String timeZoneKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientUser login(SessionToken token, String login, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean logout(SessionToken token) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ApplicationData getApplication(SessionToken token, String applictionCode, Version version, String locale) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ComponentModel getComponents(SessionToken token, String appId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TableConfig getTableConfig(SessionToken token, String classId, String whereSql,
			Map<String, DataValue> params) {
		TableConfig config = new TableConfig();
		ClassMetadata metadata = new ClassMetadata(classId);
		metadata.setTitle("Prototype table");
		metadata.setViewable(true);
		metadata.setInsertable(true);
		metadata.setUpdatable(true);
		metadata.setDeletable(true);

		List<DataType> types = Arrays.asList(DataType.STRING, DataType.BOOLEAN, DataType.DATE, DataType.NUMBER);
		for (int i = 0; i < 15; i++) {
			FieldMetadata field = new FieldMetadata("COLUMN_" + i, types.get(Random.nextInt(types.size())),
					"Column " + i);
			field.setWidth(150);
			field.setView(true);
			field.setEdit(Random.nextBoolean());
			metadata.addField(field);
		}
		config.setMetadata(metadata);
		return config;
	}

	@Override
	public LoadData<ListModelData> getListClassData(SessionToken token, ClassMetadata metadata,
			ClassLoadConfig loadConfig) {
		List<ListModelData> data = new ArrayList<>();
		for (int i = 0; i < 200; i++) {
			ListModelData item = new ListModelDataImpl();
			item.setId(String.valueOf(i));
			item.setLabel("Item " + i);
			data.add(item);
		}
		return new LoadData<>(data);
	}

	@Override
	public LoadData<RowModelData> getTableClassData(SessionToken token, ClassMetadata metadata,
			ClassLoadConfig loadConfig) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RowModelData> getTreeClassData(SessionToken token, ClassMetadata metadata,
			TreeClassLoadConfig loadConfig) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RowModelData insert(SessionToken token, ClassMetadata metadata, DataModifyConfig config) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RowModelData update(SessionToken token, ClassMetadata metadata, DataModifyConfig config) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(SessionToken token, ClassMetadata metadata, DataModifyConfig config) {
		// TODO Auto-generated method stub

	}

	@Override
	public FormModel getForm(SessionToken token, String formId, ListHolder<DataValue> parameters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EventMetadata getNextEvent(SessionToken token, EventMetadata event, String nextEventCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EventResult executeServer(SessionToken token, EventMetadata event, ListHolder<DataValue> parameters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FieldMetadata> getReportFields(SessionToken token, String classId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveReportValues(SessionToken token, String componentId, Map<String, DataValue> values) {
		// TODO Auto-generated method stub

	}

	@Override
	public EventMetadata getEvent(SessionToken token, String eventId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean touch(SessionToken token) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeToken(SessionToken token) {
		// TODO Auto-generated method stub

	}

	@Override
	public Boolean checkCaptchaCode(SessionToken token, String captchaCode, String componentId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EventMetadata getFreeEvent(SessionToken token, String eventCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String saveLoadConfig(SessionToken token, ClassLoadConfig loadConfig) {
		// TODO Auto-generated method stub
		return null;
	}

}
