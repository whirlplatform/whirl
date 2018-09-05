//package org.whirlplatform.server.servlet;
//
//import java.io.IOException;
//import java.io.OutputStream;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import AppConstant;
//import EventResult;
//import org.whirlplatform.rpc.client.SessionToken;
//import org.whirlplatform.rpc.shared.RPCException;
//import org.whirlplatform.server.driver.Connector;
//import org.whirlplatform.server.log.Logger;
//import org.whirlplatform.server.log.LoggerFactory;
//import org.whirlplatform.server.login.AccountAuthenticator;
//import org.whirlplatform.server.login.ApplicationUser;
//import org.whirlplatform.server.session.SessionManager;
//
//import com.google.inject.Inject;
//import com.google.inject.Singleton;
//
///**
// * Сервлет, выполняющий функцию PL/SQL, гостевое имя которой передается в
// * качестве значения параметра do, а параметры функции передаются в параметрах
// * p1, p2 и т.д. Функция должна возращать строку xml, которую можно формировать
// * например с помощью f_get_method_result. Может быть вариант с предварительной
// * авторизацией, например:
// * http://localhost:8888/FunctionServlet?app=test&do=link&p1=pfuser
// * 
// * @author Влад
// */
//@Singleton
//public class FunctionServlet extends HttpServlet {
//
//	private static final long serialVersionUID = 8799638218415051114L;
//
//	private Logger _log = LoggerFactory.getLogger(FunctionServlet.class);
//
//	private static String stHtmlTemplate = "<html><head><title>Результат запроса</title>"
//			+ "<meta http-equiv='Content-Type' content='text/html;charset=utf-8' ></head><body>";
//	private static String endHtmlTemplate = "</body></html>";
//
//	private final Connector connector;
//	private final AccountAuthenticator authenticator;
//
//	@Inject
//	public FunctionServlet(Connector connector,
//			AccountAuthenticator authenticator) {
//		this.connector = connector;
//		this.authenticator = authenticator;
//	}
//
//	@Override
//	protected void doGet(HttpServletRequest request,
//			HttpServletResponse response) throws ServletException, IOException {
//		doFunction(request, response);
//	}
//
//	@Override
//	protected void doPost(HttpServletRequest request,
//			HttpServletResponse response) throws ServletException, IOException {
//		doFunction(request, response);
//	}
//
//	protected void doFunction(HttpServletRequest request,
//			HttpServletResponse response) throws ServletException, IOException {
//		String resultStr = "<center>Внимание, выполняемое Вами действие недоступно!<br/><b>Обратитесь к администратору.</b></center>";
//
//		String tokenId = request.getParameter(AppConstant.TOKEN_ID);
//		SessionToken token = new SessionToken(request.getSession().getId(),
//				tokenId);
//
//		ApplicationUser user = null;
//		try {
//			//user = SessionManager.get(request.getSession().getServletContext())
//			user = SessionManager.get(request.getSession())
//					.getUser(token);
//		} catch (RPCException e1) {
//		}
////TODO РіРѕСЃС‚РµРІС‹Рµ РїСЂРёР»РѕР¶РµРЅРёСЏ Р·Р°РєСЂС‹С‚С‹
////		String guestUrl = request.getParameter(AppConstant.GUEST_URL);
////		if (user == null && guestUrl != null) {
////			try {
////				user = authenticator.loginGuest(token.getSessionId(), guestUrl);
////			} catch (LoginException e) {
////				_log.error(e);
////			}
////		}
//		user.setLocale(request.getLocale());
//
//		_log.info("SERVLET FUNCTION: " + request.getMethod() + " params = ["
//				+ request.getParameterMap() + "]");
//
//		EventResult result = null;
//
//		try {
//			String doGuestName = request.getParameter("do");
////			TODO РіРѕСЃС‚РµРІС‹Рµ РїСЂРёР»РѕР¶РµРЅРёСЏ Р·Р°РєСЂС‹С‚С‹
////			if (user != null && doGuestName != null) {
////				EventMetadata event = connector.getEventByGuestName(
////						doGuestName, null,
////						request.getParameter(AppConstant.GUEST_URL), user);
////
////				List<DataValue> params = new ArrayList<DataValue>();
////
////				for (int i = 1; request.getParameter("p" + i) != null; i++) {
////					DataValue value = new DataValueImpl(DataType.STRING);
////					value.setValue(request.getParameter("p" + i));
////					params.add(value);
////				}
////
////				if (params.size() == 0) {
////					for (EventParameter param : event.getParameters()) {
////						params.add(param.getData());
////					}
////				}
////
////				result = connector.executeDatabase(event, params, user);
////			}
//		} catch (Exception e) {
//			_log.error(e);
//		} finally {
//			if (result != null) {
//				String message = result.getMessage();
//				if (message == null) {
//					message = "Событие выполнено!!";
//				}
//				OutputStream out = response.getOutputStream();
//				response.setContentType("text/html");
//				out.write((stHtmlTemplate + message + endHtmlTemplate)
//						.getBytes());
//				out.flush();
//			} else {
//				OutputStream out = response.getOutputStream();
//				response.setContentType("text/html");
//				out.write((stHtmlTemplate + resultStr + endHtmlTemplate)
//						.getBytes());
//				out.flush();
//			}
//		}
//	}
//
//}
