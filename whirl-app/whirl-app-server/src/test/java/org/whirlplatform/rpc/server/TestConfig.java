//package org.whirlplatform.rpc.server;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//
//import java.util.Arrays;
//import java.util.Map;
//
//import org.glassfish.jersey.server.ResourceConfig;
//import org.mockito.Mockito;
//import org.mockito.invocation.InvocationOnMock;
//import org.mockito.stubbing.Answer;
//
//import ClassLoadConfig;
//import ClassMetadata;
//import DataModifyConfig;
//import FieldMetadata;
//import TreeClassLoadConfig;
//import Message;
//import DataType;
//import CustomException;
//import org.whirlplatform.rpc.shared.ObjectProvider;
//import SessionToken;
//
//public class TestConfig extends ResourceConfig {
//
//	private ObjectProvider provider = new ObjectProvider();
//
//	public TestConfig() {
//		register(prepareService());
//		register(JsonParamConverterProvider.class);
//		register(RestExceptionMapper.class);
//	}
//
//	private DataServiceImpl prepareService() {
//		// Для большинства методов подойдет поведение по умолчанию
//		DataServiceImpl service = Mockito.mock(DataServiceImpl.class, new Answer() {
//			@Override
//			public Object answer(InvocationOnMock invocation) throws Throwable {
//				return provider.getObject(invocation.getMethod().getReturnType());
//			}
//		});
//
//		// Для остальных ответ настраиваем отдельно
//		when(service.getListClassData(any(SessionToken.class), any(ClassMetadata.class), any(ClassLoadConfig.class)))
//				.thenReturn(provider.getLoadDataListModelData());
//		when(service.getTableClassData(any(SessionToken.class), any(ClassMetadata.class), any(ClassLoadConfig.class)))
//				.thenReturn(provider.getLoadDataRowModelData());
//		when(
//				service.getTreeClassData(any(SessionToken.class), any(ClassMetadata.class),
//						any(TreeClassLoadConfig.class))).thenReturn(
//				Arrays.asList(provider.getRowModelData(), provider.getRowModelData()));
//		Mockito.doNothing().when(service)
//				.delete(any(SessionToken.class), any(ClassMetadata.class), any(DataModifyConfig.class));
//		when(service.getReportFields(any(SessionToken.class), any(String.class))).thenReturn(
//				Arrays.asList(new FieldMetadata("FieldName", DataType.STRING, "Название"), new FieldMetadata(
//						"FieldName2", DataType.STRING, "Название2")));
//		Mockito.doNothing().when(service)
//				.saveReportValues(any(SessionToken.class), any(String.class), any(Map.class), any(Boolean.class));
//		when(service.touch(any(SessionToken.class))).thenReturn(
//				Arrays.asList(provider.getMessage(), provider.getMessage()));
//		Mockito.doNothing().when(service).setMessageReceived(any(SessionToken.class), any(Message.class));
//		Mockito.doNothing().when(service).removeToken(any(SessionToken.class));
//		when(service.login(any(SessionToken.class), Mockito.eq("error_user"), any(String.class))).thenThrow(
//				new CustomException("login exception"));
//		return service;
//	}
//}
