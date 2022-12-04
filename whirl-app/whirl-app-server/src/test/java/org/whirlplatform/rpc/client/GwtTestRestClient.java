//package org.whirlplatform.rpc.client;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map.Entry;
//
//import org.fusesource.restygwt.client.Resource;
//import org.fusesource.restygwt.client.RestServiceProxy;
//
//import com.google.gwt.core.client.GWT;
//import com.google.gwt.junit.client.GWTTestCase;
//import com.google.gwt.user.client.rpc.AsyncCallback;
//
//import ApplicationData;
//import ClassLoadConfig;
//import ClientUser;
//import DataModifyConfig;
//import DataModifyConfig.DataModifyType;
//import EventMetadata;
//import EventResult;
//import FieldMetadata;
//import FileValue;
//import FilterValue;
//import LoadData;
//import SortType;
//import SortValue;
//import TableConfig;
//import TreeClassLoadConfig;
//import Version;
//import ComponentModel;
//import PropertyType;
//import DataType;
//import DataValue;
//import DataValueImpl;
//import EventParameter;
//import ListModelData;
//import RowListValue;
//import RowListValueImpl;
//import RowModelData;
//import RowValueImpl;
//import FormModel;
//import ClientRestException;
//import ListHolder;
//import org.whirlplatform.rpc.shared.ObjectProvider;
//import SessionToken;
//
//public class GwtTestRestClient extends GWTTestCase {
//
//    private final ObjectProvider provider = new ObjectProvider();
//
//    @Override
//    public String getModuleName() {
//        return "org.whirlplatform.rpc.RPCTest";
//    }
//
//    private Resource getResource() {
//        return new Resource(GWT.getModuleBaseURL() + "application/data");
//    }
//
//    public void testGetUser() {
//        delayTestFinish(10000);
//        SessionToken sessionToken = new SessionToken("sessionId", "tokenId");
//        boolean newSession = true;
//        String timeZoneKey = "ru";
//
//        DataServiceAsync service = DataServiceAsync.Util.getDataService(new AsyncCallback<ClientUser>() {
//            @Override
//            public void onFailure(Throwable caught) {
//                fail(caught.getMessage());
//            }
//
//            public void onSuccess(ClientUser result) {
//                ClientUser obj = provider.getClientUser();
//                assertEquals(obj.getLogin(), result.getLogin());
//                assertEquals(obj.getName(), result.getName());
//                finishTest();
//            };
//        });
//
//        ((RestServiceProxy) service).setResource(getResource());
//
//        service.getUser(sessionToken, newSession, timeZoneKey);
//    }
//
//    // public void testLogin() {
//    // // всё равно типы те же что и в getUser
//    // }
//
//    public void testGetApplication() {
//        delayTestFinish(10000);
//        SessionToken sessionToken = new SessionToken("sessionId", "tokenId");
//        DataServiceAsync service = DataServiceAsync.Util.getDataService(new AsyncCallback<ApplicationData>() {
//            @Override
//            public void onFailure(Throwable caught) {
//                fail(caught.getMessage());
//            }
//
//            public void onSuccess(ApplicationData result) {
//                ApplicationData obj = provider.getApplicationData();
//                assertEquals(obj.getApplicationCode(), result.getApplicationCode());
//                assertEquals(obj.getName(), result.getName());
//                assertEquals(obj.getCss().get(0), result.getCss().get(0));
//                assertEquals(obj.getCss().get(1), result.getCss().get(1));
//                assertEquals(obj.getScripts().get(0), result.getScripts().get(0));
//                assertEquals(obj.getNonCreateEvents().get(0).getId(), result.getNonCreateEvents().get(0).getId());
//                finishTest();
//            };
//        });
//        ((RestServiceProxy) service).setResource(getResource());
//
//        Version v = Version.create(1, 1, 1);
//        service.getApplication(sessionToken, "test_code", v, "ru");
//    }
//
//    public void testGetComponents() {
//        delayTestFinish(10000);
//        SessionToken sessionToken = new SessionToken("sessionId", "tokenId");
//        DataServiceAsync service = DataServiceAsync.Util.getDataService(new AsyncCallback<ComponentModel>() {
//            @Override
//            public void onFailure(Throwable caught) {
//                fail(caught.getMessage());
//            }
//
//            @Override
//            public void onSuccess(ComponentModel result) {
//                ComponentModel obj = provider.getComponentModel();
//                assertEquals(obj.getId(), result.getId());
//                assertEquals(obj.getValue(PropertyType.Code.getCode()), result.getValue(PropertyType.Code.getCode()));
//                assertEquals(obj.getType(), result.getType());
//                assertEquals(obj.getChildren().get(0).getId(), obj.getChildren().get(0).getId());
//                finishTest();
//            }
//        });
//        ((RestServiceProxy) service).setResource(getResource());
//
//        service.getComponents(sessionToken, "appId");
//    }
//
//    public void testGetTableConfig() {
//        delayTestFinish(10000);
//        SessionToken sessionToken = new SessionToken("sessionId", "tokenId");
//        DataServiceAsync service = DataServiceAsync.Util.getDataService(new AsyncCallback<TableConfig>() {
//            @Override
//            public void onFailure(Throwable caught) {
//                fail(caught.getMessage());
//            }
//
//            public void onSuccess(TableConfig result) {
//                TableConfig obj = provider.getTableConfig();
//                assertEquals(obj.getMetadata().getClassId(), result.getMetadata().getClassId());
//                FieldMetadata expField = obj.getMetadata().getFields().get(0);
//                assertEquals(result.getMetadata().getField(expField.getName()).getLabel(), expField.getLabel());
//                finishTest();
//            };
//        });
//        ((RestServiceProxy) service).setResource(getResource());
//
//        HashMap<String, DataValue> params = new HashMap<String, DataValue>();
//        params.put("param1", new DataValueImpl(DataType.BOOLEAN, true));
//        params.put("param2", new DataValueImpl(DataType.DATE, new Date()));
//        service.getTableConfig(sessionToken, "classId", "where 1=1", params);
//    }
//
//    public void testGetListClassData() {
//        delayTestFinish(10000);
//        SessionToken sessionToken = new SessionToken("sessionId", "tokenId");
//        DataServiceAsync service = DataServiceAsync.Util.getDataService(new AsyncCallback<LoadData<ListModelData>>() {
//            @Override
//            public void onFailure(Throwable caught) {
//                fail(caught.getMessage());
//            }
//
//            @Override
//            public void onSuccess(LoadData<ListModelData> result) {
//                LoadData<ListModelData> obj = provider.getLoadDataListModelData();
//                assertEquals(obj.getRows(), result.getRows());
//                ListModelData expModel = obj.get(1);
//                ListModelData actModel = result.get(1);
//                assertEquals(expModel.getId(), actModel.getId());
//                assertEquals(expModel.getLabel(), actModel.getLabel());
//                for (String s : expModel.getPropertyNames()) {
//                    assertEquals(expModel.getValue(s).getValue(), actModel.getValue(s).getValue());
//                }
//                finishTest();
//            }
//        });
//        ((RestServiceProxy) service).setResource(getResource());
//
//        ClassLoadConfig config = new ClassLoadConfig();
//        FieldMetadata field = new FieldMetadata("FieldName", DataType.STRING, "Название");
//        config.addFilter(new FilterValue(field));
//        SortValue sort = new SortValue();
//        sort.setOrder(SortType.DESC);
//        sort.setField(field);
//        config.addSort(sort);
//        service.getListClassData(sessionToken, provider.getClassMetadata(), config);
//    }
//
//    public void testGetTableClassData() {
//        delayTestFinish(10000);
//        SessionToken sessionToken = new SessionToken("sessionId", "tokenId");
//        DataServiceAsync service = DataServiceAsync.Util.getDataService(new AsyncCallback<LoadData<RowModelData>>() {
//            @Override
//            public void onFailure(Throwable caught) {
//                fail(caught.getMessage());
//            }
//
//            @Override
//            public void onSuccess(LoadData<RowModelData> result) {
//                LoadData<RowModelData> obj = provider.getLoadDataRowModelData();
//                assertEquals(obj.getRows(), result.getRows());
//                RowModelData expModel = obj.get(0);
//                RowModelData actModel = result.get(0);
//                assertEquals(expModel.getId(), actModel.getId());
//                for (String s : expModel.getPropertyNames()) {
//                    assertEquals(expModel.getValue(s).getValue(), actModel.getValue(s).getValue());
//                }
//                finishTest();
//            }
//        });
//
//        ((RestServiceProxy) service).setResource(getResource());
//
//        ClassLoadConfig config = new ClassLoadConfig();
//        FieldMetadata field = new FieldMetadata("FieldName", DataType.STRING, "Название");
//        config.addFilter(new FilterValue(field));
//        SortValue sort = new SortValue();
//        sort.setOrder(SortType.DESC);
//        sort.setField(field);
//        config.addSort(sort);
//        service.getTableClassData(sessionToken, provider.getClassMetadata(), config);
//    }
//
//    public void testGetTreeClassData() {
//        delayTestFinish(10000);
//        SessionToken sessionToken = new SessionToken("sessionId", "tokenId");
//        DataServiceAsync service = DataServiceAsync.Util.getDataService(new AsyncCallback<List<RowModelData>>() {
//            @Override
//            public void onFailure(Throwable caught) {
//                fail(caught.getMessage());
//            }
//
//            @Override
//            public void onSuccess(List<RowModelData> result) {
//                List<RowModelData> obj = Arrays.asList(provider.getRowModelData(), provider.getRowModelData());
//                RowModelData expModel = obj.get(0);
//                RowModelData actModel = result.get(0);
//                assertEquals(expModel.getId(), actModel.getId());
//                for (String s : expModel.getPropertyNames()) {
//                    assertEquals(expModel.getValue(s).getValue(), actModel.getValue(s).getValue());
//                }
//                finishTest();
//            }
//        });
//
//        ((RestServiceProxy) service).setResource(getResource());
//
//        TreeClassLoadConfig config = new TreeClassLoadConfig();
//        FieldMetadata field = new FieldMetadata("FieldName", DataType.STRING, "Название");
//        config.addFilter(new FilterValue(field));
//        SortValue sort = new SortValue();
//        sort.setOrder(SortType.DESC);
//        sort.setField(field);
//        config.addSort(sort);
//        service.getTreeClassData(sessionToken, provider.getClassMetadata(), config);
//    }
//
//    public void testInsert() {
//        delayTestFinish(10000);
//        SessionToken sessionToken = new SessionToken("sessionId", "tokenId");
//        DataServiceAsync service = DataServiceAsync.Util.getDataService(new AsyncCallback<RowModelData>() {
//            @Override
//            public void onFailure(Throwable caught) {
//                fail(caught.getMessage());
//            }
//
//            @Override
//            public void onSuccess(RowModelData result) {
//                RowModelData obj = provider.getRowModelData();
//                assertEquals(obj.getId(), result.getId());
//                for (String s : obj.getPropertyNames()) {
//                    assertEquals(obj.getValue(s).getValue(), result.getValue(s).getValue());
//                }
//                finishTest();
//            }
//        });
//        ((RestServiceProxy) service).setResource(getResource());
//
//        HashMap<String, DataValue> params = new HashMap<String, DataValue>();
//        params.put("param1", new DataValueImpl(DataType.BOOLEAN, true));
//        params.put("param2", new DataValueImpl(DataType.DATE, new Date()));
//        DataModifyConfig config = new DataModifyConfig(DataModifyType.INSERT,
//                Arrays.asList(provider.getRowModelData()), params);
//        service.insert(sessionToken, provider.getClassMetadata(), config);
//    }
//
//    public void testUpdate() {
//        delayTestFinish(10000);
//        SessionToken sessionToken = new SessionToken("sessionId", "tokenId");
//        DataServiceAsync service = DataServiceAsync.Util.getDataService(new AsyncCallback<RowModelData>() {
//            @Override
//            public void onFailure(Throwable caught) {
//                fail(caught.getMessage());
//            }
//
//            @Override
//            public void onSuccess(RowModelData result) {
//                RowModelData obj = provider.getRowModelData();
//                assertEquals(obj.getId(), result.getId());
//                for (String s : obj.getPropertyNames()) {
//                    assertEquals(obj.getValue(s).getValue(), result.getValue(s).getValue());
//                }
//                finishTest();
//            }
//        });
//        ((RestServiceProxy) service).setResource(getResource());
//
//        HashMap<String, DataValue> params = new HashMap<String, DataValue>();
//        params.put("param1", new DataValueImpl(DataType.BOOLEAN, true));
//        RowListValue rlv = new RowListValueImpl();
//        rlv.addRowValue(new RowValueImpl("elem_id"));
//        params.put("param2", rlv);
//        DataModifyConfig config = new DataModifyConfig(DataModifyType.UPDATE,
//                Arrays.asList(provider.getRowModelData()), params);
//        service.update(sessionToken, provider.getClassMetadata(), config);
//    }
//
//    public void testDelete() {
//        delayTestFinish(10000);
//        SessionToken sessionToken = new SessionToken("sessionId", "tokenId");
//        DataServiceAsync service = DataServiceAsync.Util.getDataService(new AsyncCallback<Void>() {
//            @Override
//            public void onFailure(Throwable caught) {
//                fail(caught.getMessage());
//            }
//
//            @Override
//            public void onSuccess(Void result) {
//                finishTest();
//            }
//        });
//        ((RestServiceProxy) service).setResource(getResource());
//
//        HashMap<String, DataValue> params = new HashMap<String, DataValue>();
//        params.put("param1", new DataValueImpl(DataType.BOOLEAN, true));
//        params.put("param2", new DataValueImpl(DataType.DATE, new Date()));
//        DataModifyConfig config = new DataModifyConfig(DataModifyType.INSERT,
//                Arrays.asList(provider.getRowModelData()), params);
//        service.delete(sessionToken, provider.getClassMetadata(), config);
//    }
//
//    public void testGetForm() {
//        delayTestFinish(10000);
//        SessionToken sessionToken = new SessionToken("sessionId", "tokenId");
//        DataServiceAsync service = DataServiceAsync.Util.getDataService(new AsyncCallback<FormModel>() {
//            @Override
//            public void onFailure(Throwable caught) {
//                fail(caught.getMessage());
//            }
//
//            public void onSuccess(FormModel result) {
//                FormModel obj = provider.getFormModel();
//                assertEquals(obj.getId(), result.getId());
//                // TODO: более детальное сравнение
//                finishTest();
//            }
//        });
//        ((RestServiceProxy) service).setResource(getResource());
//
//        ArrayList<DataValue> list = new ArrayList<DataValue>();
//        list.add(new DataValueImpl(DataType.DATE, new Date()));
//        list.add(new DataValueImpl(DataType.NUMBER, 15));
//        service.getForm(sessionToken, "formId", new ListHolder(list));
//    }
//
//    public void testGetNextEvent() {
//        delayTestFinish(10000);
//        SessionToken sessionToken = new SessionToken("sessionId", "tokenId");
//        DataServiceAsync service = DataServiceAsync.Util.getDataService(new AsyncCallback<EventMetadata>() {
//            @Override
//            public void onFailure(Throwable caught) {
//                fail(caught.getMessage());
//            }
//
//            @Override
//            public void onSuccess(EventMetadata result) {
//                EventMetadata obj = provider.getEventMetadata();
//                assertEquals(obj.getId(), result.getId());
//                finishTest();
//            }
//        });
//        ((RestServiceProxy) service).setResource(getResource());
//
//        service.getNextEvent(sessionToken, provider.getEventMetadata(), "nextEvent");
//    }
//
//    public void testExecuteServer() {
//        delayTestFinish(10000);
//        SessionToken sessionToken = new SessionToken("sessionId", "tokenId");
//        DataServiceAsync service = DataServiceAsync.Util.getDataService(new AsyncCallback<EventResult>() {
//            @Override
//            public void onFailure(Throwable caught) {
//                fail(caught.getMessage());
//            }
//
//            @Override
//            public void onSuccess(EventResult result) {
//                EventResult obj = provider.getEventResult();
//                assertEquals(obj.getNextEventCode(), result.getNextEventCode());
//                for (Entry<Integer, EventParameter> e : obj.getParametersMap().entrySet()) {
//                    if (e.getValue().getData().getType() == DataType.FILE) {
//                        FileValue objFile = e.getValue().getData().getValue();
//                        FileValue resultFile = result.getParametersMap().get(e.getKey()).getData().getValue();
//                        assertEquals(objFile.getName(), resultFile.getName());
//                        assertEquals(objFile.getSize(), resultFile.getSize());
//                        assertEquals(objFile.getTempId(), resultFile.getTempId());
//                        assertEquals(objFile.getInputStream(), resultFile.getInputStream());
//                    } else {
//                        assertEquals(e.getValue().getData().getValue(), result.getParametersMap().get(e.getKey())
//                                .getData().getValue());
//                    }
//                }
//                finishTest();
//            }
//        });
//        ((RestServiceProxy) service).setResource(getResource());
//
//        ArrayList<DataValue> list = new ArrayList<DataValue>();
//        list.add(new DataValueImpl(DataType.NUMBER, 123));
//        list.add(new DataValueImpl(DataType.STRING, ""));
//        service.executeServer(sessionToken, provider.getEventMetadata(), new ListHolder(list));
//    }
//
//    public void testGetReportFields() {
//        delayTestFinish(10000);
//        SessionToken sessionToken = new SessionToken("sessionId", "tokenId");
//        DataServiceAsync service = DataServiceAsync.Util.getDataService(new AsyncCallback<List<FieldMetadata>>() {
//            public void onFailure(Throwable caught) {
//                fail(caught.getMessage());
//            }
//
//            public void onSuccess(List<FieldMetadata> result) {
//                assertNotNull(result);
//                assertNotNull(result.get(0));
//                finishTest();
//            }
//        });
//        ((RestServiceProxy) service).setResource(getResource());
//
//        service.getReportFields(sessionToken, "classId");
//    }
//
//    public void testSaveReportValues() {
//        delayTestFinish(10000);
//        SessionToken sessionToken = new SessionToken("sessionId", "tokenId");
//        DataServiceAsync service = DataServiceAsync.Util.getDataService(new AsyncCallback<Void>() {
//            @Override
//            public void onFailure(Throwable caught) {
//                fail(caught.getMessage());
//            }
//
//            @Override
//            public void onSuccess(Void result) {
//                finishTest();
//            }
//        });
//        ((RestServiceProxy) service).setResource(getResource());
//
//        HashMap<String, DataValue> params = new HashMap<String, DataValue>();
//        params.put("param1", new DataValueImpl(DataType.BOOLEAN, true));
//        params.put("param2", new DataValueImpl(DataType.DATE, new Date()));
//        service.saveReportValues(sessionToken, "componentId", params, true);
//    }
//
//    public void testGetEvent() {
//        delayTestFinish(10000);
//        SessionToken sessionToken = new SessionToken("sessionId", "tokenId");
//        DataServiceAsync service = DataServiceAsync.Util.getDataService(new AsyncCallback<EventMetadata>() {
//            @Override
//            public void onFailure(Throwable caught) {
//                fail(caught.getMessage());
//            }
//
//            @Override
//            public void onSuccess(EventMetadata result) {
//                EventMetadata obj = provider.getEventMetadata();
//                assertEquals(obj.getId(), result.getId());
//
//                finishTest();
//            }
//        });
//        ((RestServiceProxy) service).setResource(getResource());
//
//        service.getEvent(sessionToken, "eventId");
//    }
//
//    public void testTouch() {
//        delayTestFinish(10000);
//        SessionToken sessionToken = new SessionToken("sessionId", "tokenId");
//        DataServiceAsync service = DataServiceAsync.Util.getDataService(new AsyncCallback<Boolean>() {
//            @Override
//            public void onFailure(Throwable caught) {
//                fail(caught.getMessage());
//            }
//
//            @Override
//            public void onSuccess(Boolean result) {
//                assertTrue(result);
//                
//                finishTest();
//            }
//        });
//        ((RestServiceProxy) service).setResource(getResource());
//
//        service.touch(sessionToken);
//    }
//
//    public void testRemoveToken() {
//        delayTestFinish(10000);
//        SessionToken sessionToken = new SessionToken("sessionId", "tokenId");
//        DataServiceAsync service = DataServiceAsync.Util.getDataService(new AsyncCallback<Void>() {
//            @Override
//            public void onFailure(Throwable caught) {
//                fail(caught.getMessage());
//            }
//
//            @Override
//            public void onSuccess(Void result) {
//                finishTest();
//            }
//        });
//        ((RestServiceProxy) service).setResource(getResource());
//
//        service.removeToken(sessionToken);
//    }
//
//    public void testCheckCaptchaCode() {
//        delayTestFinish(10000);
//        SessionToken sessionToken = new SessionToken("sessionId", "tokenId");
//        DataServiceAsync service = DataServiceAsync.Util.getDataService(new AsyncCallback<Boolean>() {
//            @Override
//            public void onFailure(Throwable caught) {
//                fail(caught.getMessage());
//            }
//
//            @Override
//            public void onSuccess(Boolean result) {
//                Boolean obj = provider.getBoolean();
//                assertEquals(obj, result);
//                finishTest();
//            }
//        });
//        ((RestServiceProxy) service).setResource(getResource());
//
//        service.checkCaptchaCode(sessionToken, "captcha_code", "component_id");
//    }
//
//    public void testGetFreeEvent() {
//        delayTestFinish(10000);
//        SessionToken sessionToken = new SessionToken("sessionId", "tokenId");
//        DataServiceAsync service = DataServiceAsync.Util.getDataService(new AsyncCallback<EventMetadata>() {
//            @Override
//            public void onFailure(Throwable caught) {
//                fail(caught.getMessage());
//            }
//
//            @Override
//            public void onSuccess(EventMetadata result) {
//                EventMetadata obj = provider.getEventMetadata();
//                assertEquals(obj.getId(), result.getId());
//                finishTest();
//            }
//        });
//        ((RestServiceProxy) service).setResource(getResource());
//
//        service.getFreeEvent(sessionToken, "event_code");
//    }
//
//    public void testSaveLoadConfig() {
//        delayTestFinish(10000);
//        SessionToken sessionToken = new SessionToken("sessionId", "tokenId");
//        DataServiceAsync service = DataServiceAsync.Util.getDataService(new AsyncCallback<String>() {
//            @Override
//            public void onFailure(Throwable caught) {
//                fail(caught.getMessage());
//            }
//
//            @Override
//            public void onSuccess(String result) {
//                String obj = provider.getString();
//                assertEquals(obj, result);
//                finishTest();
//            }
//        });
//        ((RestServiceProxy) service).setResource(getResource());
//
//        ClassLoadConfig config = new ClassLoadConfig();
//        FieldMetadata field = new FieldMetadata("FieldName", DataType.STRING, "Название");
//        config.addFilter(new FilterValue(field));
//        SortValue sort = new SortValue();
//        sort.setOrder(SortType.DESC);
//        sort.setField(field);
//        config.addSort(sort);
//        service.saveLoadConfig(sessionToken, config);
//    }
//
//    public void testGetApplicationCodeById() {
//        delayTestFinish(10000);
//        SessionToken sessionToken = new SessionToken("sessionId", "tokenId");
//        DataServiceAsync service = DataServiceAsync.Util.getDataService(new AsyncCallback<String>() {
//            @Override
//            public void onFailure(Throwable caught) {
//                fail(caught.getMessage());
//            }
//
//            @Override
//            public void onSuccess(String result) {
//                String obj = provider.getString();
//                assertEquals(obj, result);
//                finishTest();
//            }
//        });
//        ((RestServiceProxy) service).setResource(getResource());
//
//        service.getApplicationCodeById(sessionToken, "app_id");
//    }
//
//    public void testCheckUserAccess() {
//        delayTestFinish(10000);
//        SessionToken sessionToken = new SessionToken("sessionId", "tokenId");
//        DataServiceAsync service = DataServiceAsync.Util.getDataService(new AsyncCallback<Boolean>() {
//            @Override
//            public void onFailure(Throwable caught) {
//                fail(caught.getMessage());
//            }
//
//            @Override
//            public void onSuccess(Boolean result) {
//                Boolean obj = provider.getBoolean();
//                assertEquals(obj, result);
//                finishTest();
//            }
//        });
//        ((RestServiceProxy) service).setResource(getResource());
//
//        service.checkUserAccess(sessionToken, "app_code");
//    }
//
//    public void testException() {
//        delayTestFinish(10000);
//        SessionToken sessionToken = new SessionToken("sessionId", "tokenId");
//        DataServiceAsync service = DataServiceAsync.Util.getDataService(new AsyncCallback<ClientUser>() {
//            @Override
//            public void onFailure(Throwable caught) {
//                if (caught instanceof ClientRestException) {
//                    ClientRestException ex = (ClientRestException) caught;
//                    if (ex.isCustom()) {
//                        finishTest();
//                    }
//                }
//            }
//
//            @Override
//            public void onSuccess(ClientUser result) {
//                fail("Wrong user logged in!");
//            }
//        });
//        ((RestServiceProxy) service).setResource(getResource());
//
//        service.login(sessionToken, "error_user", "password");
//    }
//}
