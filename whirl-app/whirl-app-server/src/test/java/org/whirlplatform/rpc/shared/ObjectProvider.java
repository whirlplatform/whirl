//package org.whirlplatform.rpc.shared;
//
//import java.util.Arrays;
//
//import ClickEvent;
//import ApplicationData;
//import ClassMetadata;
//import ClientUser;
//import EventMetadata;
//import EventResult;
//import EventType;
//import FieldMetadata;
//import FileValue;
//import JavaEventResult;
//import LoadData;
//import TableConfig;
//import ComponentModel;
//import ComponentType;
//import Message;
//import PropertyType;
//import DataType;
//import DataValueImpl;
//import EventParameterImpl;
//import ListModelData;
//import ListModelDataImpl;
//import ParameterType;
//import RowModelData;
//import RowModelDataImpl;
//import FormCellModel;
//import FormColumnModel;
//import FormModel;
//import FormRowModel;
//
//public class ObjectProvider {
//
//	// Получение тестового объекта нужного класса
//	@SuppressWarnings("unchecked")
//	public <T> T getObject(Class<T> cl) {
//		if (ClientUser.class == cl) {
//			return (T) getClientUser();
//		} else if (String.class == cl) {
//			return (T) getString();
//		} else if (ApplicationData.class == cl) {
//			return (T) getApplicationData();
//		} else if (ComponentModel.class == cl) {
//			return (T) getComponentModel();
//		} else if (TableConfig.class == cl) {
//			return (T) getTableConfig();
//		} else if (RowModelData.class == cl) {
//			return (T) getRowModelData();
//		} else if (ListModelData.class == cl) {
//			return (T) getListModelData();
//		} else if (FormModel.class == cl) {
//			return (T) getFormModel();
//		} else if (EventMetadata.class == cl) {
//			return (T) getEventMetadata();
//		} else if (EventResult.class == cl) {
//			return (T) getEventResult();
//		} else if (Boolean.class == cl) {
//			return (T) getBoolean();
//		}
//		
//		return null;
//	}
//
//	public ClientUser getClientUser() {
//		ClientUser u = new ClientUser();
//		u.setLogin("test_user");
//		u.setName("Иванов Иван Иванович");
//		return u;
//	}
//	
//	public String getString() {
//		return "TestString\n Тестовая строка";
//	}
//	
//	public ApplicationData getApplicationData() {
//		ApplicationData data = new ApplicationData();
//		data.setApplicationCode("app_code");
//		data.setName("Название приложения");
//		data.addCss("p.one {border-style: solid;border-color: #0000ff;}");
//		data.addCss("p.two {border-style: solid;border-color: #ffffff;}");
//		data.addScript("/script.js");
//		data.addEvent(ClickEvent.getType().toString(), getEventMetadata());
//		return data;
//	}
//	
//	public EventMetadata getEventMetadata() {
//		EventMetadata event = new EventMetadata(EventType.JavaScript);
//		event.setCode("event_code");
//		event.setId("1234567890");
//		event.setSource("fnc(param){\n  alert(\"test\");\n}");
//		return event;
//	}
//	
//	public ComponentModel getComponentModel() {
//		ComponentModel model = new ComponentModel(ComponentType.SimpleContainerType);
//		model.setId("0987654321");
//		model.setValue(PropertyType.Code.getCode(), new DataValueImpl(DataType.STRING, "component_code"));
//		ComponentModel child = new ComponentModel(ComponentType.ButtonType);
//		child.setId("56363577");
//		child.setValue(PropertyType.Code.getCode(), new DataValueImpl(DataType.STRING, "inner_component_code"));
//		model.addChild(child);
//		return model;
//	}
//	
//	public TableConfig getTableConfig() {
//		TableConfig config = new TableConfig();
//		config.setMetadata(getClassMetadata());
//		return config;
//	}
//	
//	public ClassMetadata getClassMetadata() {
//		ClassMetadata meta = new ClassMetadata("12345");
//		meta.addField(new FieldMetadata("FieldName", DataType.STRING, "Название"));
//		meta.addField(new FieldMetadata("FieldName2", DataType.STRING, "Название2"));
//		return meta;
//	}
//	
//	public LoadData<ListModelData> getLoadDataListModelData() {
//		LoadData<ListModelData> data = new LoadData<>(Arrays.asList(getListModelData(), getListModelData()));
//		data.setRows(2);
//		return data;
//	}
//	
//	public LoadData<RowModelData> getLoadDataRowModelData() {
//		LoadData<RowModelData> data = new LoadData<>(Arrays.asList(getRowModelData(), getRowModelData(), getRowModelData()));
//		data.setRows(3);
//		return data;
//	}
//	
//	public RowModelData getRowModelData() {
//		RowModelData model = new RowModelDataImpl();
//		model.setId("3467878");
//		model.setEditable(true);
//		model.setValue("Property 1", new DataValueImpl(DataType.STRING, "Value1"));
//		model.setValue("Property 2", new DataValueImpl(DataType.STRING, "Value2"));
//		model.setStyle("Property 1", "background-color:red");
//		return model;
//	}
//	
//	public ListModelData getListModelData() {
//		ListModelData model = new ListModelDataImpl();
//		model.setId("1244556");
//		model.setLabel("Тестовый текст");
//		model.setEditable(true);
//		model.setValue("Property 1", new DataValueImpl(DataType.STRING, "Value1"));
//		model.setValue("Property 2", new DataValueImpl(DataType.STRING, "Value2"));
//		model.setStyle("Property 1", "background-color:red");
//		return model;
//	}
//	
//	public FormModel getFormModel() {
//		FormModel model = new FormModel("1235455");
//		FormRowModel row = new FormRowModel(0);
//		FormColumnModel column = new FormColumnModel(0);
//		FormCellModel cell = new FormCellModel("123566");
//		cell.setRow(row);
//		cell.setColumn(column);
//		cell.setComponent(getComponentModel());
//		row.addCell(cell);
//		model.addColumn(column);
//		model.addRow(row);
//		return model;
//	}
//	
//	public EventResult getEventResult() {
//		EventResult result = new JavaEventResult();
//		result.setNextEventCode("nextEventCode");
//		EventParameterImpl param = new EventParameterImpl(ParameterType.DATAVALUE);
//		FileValue file = new FileValue();
//		file.setName("file name");
//		file.setSaveName(true);
//		file.setSize(234);
//		file.setTempId("temp_id");
//		param.setData(new DataValueImpl(DataType.FILE, file));
//		result.addParameter(param);
//		result.setMessage("Тестовое сообщение");
//		result.setTitle("Title");
//		return result;
//	}
//	
//	public Message getMessage() {
//		Message msg = new Message();
//		msg.setId("1235512");
//		msg.setMessage("Текст сообщения");
//		msg.setUserId("1312351");
//		return msg;
//	}
//	
//	public Boolean getBoolean() {
//		return Boolean.TRUE;
//	}
//}
