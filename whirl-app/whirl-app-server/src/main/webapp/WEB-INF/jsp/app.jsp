<%@page import="org.whirlplatform.server.config.Configuration"%>
<%@page import="org.whirlplatform.rpc.shared.CustomException"%>
<%@page import="org.apache.commons.lang.LocaleUtils"%>
<%@page import="org.whirlplatform.server.login.AccountAuthenticator"%>
<%@page import="org.whirlplatform.server.login.LoginData"%>
<%@page import="org.whirlplatform.server.login.impl.GuestAccountAuthenticator"%>
<%@page import="org.whirlplatform.meta.shared.AppConstant"%>
<%@page import="org.whirlplatform.rpc.shared.SessionToken"%>
<%@page import="org.whirlplatform.meta.shared.ApplicationData"%>
<%@page import="org.whirlplatform.server.login.ApplicationUser"%>
<%@page import="org.whirlplatform.server.session.SessionManager"%>
<%@page import="org.whirlplatform.server.i18n.I18NMessage"%>
<%@page import="java.util.Random"%>
<%@page import="java.util.Locale"%>
<%@page import="org.apache.commons.lang.LocaleUtils"%>
<%@page import="org.whirlplatform.server.driver.Connector"%>
<%@page import="org.whirlplatform.meta.shared.Version"%>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="org.whirlplatform.meta.shared.Theme" %>
<%@ page import="org.checkerframework.checker.nullness.Opt" %>
<%@ page import="java.util.Optional" %>

<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
    Locale l = I18NMessage.getRequestLocale();
    String locale = request.getParameter("locale");
    if (locale != null && !locale.isEmpty()) {
        l = LocaleUtils.toLocale(locale);
    }
    l = new Locale(l.getLanguage(), l.getCountry());
    locale = l.toString();

    //сообщение спецефичные для настроек сервера
    String title = I18NMessage.getSpecifiedMessage("index_title", l);
    if (title == null) {
        title = "Whirl Platform";
    }
    String header = I18NMessage.getSpecifiedMessage("index_header", l);
    if (header == null) {
        header = "<div style=\"font-weight: bold; font-size: 20px;\">Whirl Platform</div>";
    }
    String footer = I18NMessage.getSpecifiedMessage("index_footer", l);
    if (footer == null) {
        footer = "Whirl Platform";
    }
    
    Configuration configuration = (Configuration) request.getAttribute("configuration");
    String sessionTimeout = configuration.lookup("Whirl/sessiontimeout") == null?"500":String.valueOf(configuration.<Integer> lookup("Whirl/sessiontimeout"));

    String theme = Theme.BLUE.getPath();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">

<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<meta name="gwt:property" content="locale=<%=locale%>">
<meta http-equiv="X-UA-Compatible" content="IE=9">
<title><%=title%></title>
<link type="text/css" rel="stylesheet" href="css/application.css">
<script type="text/javascript" src="cke/ckeditor.js"></script>

<%
    String tokenId = request.getParameter(AppConstant.TOKEN);
    if (tokenId != null) {
        SessionToken token = new SessionToken(session.getId(), tokenId);
        ApplicationUser user;
        try {
            user = SessionManager.get(request.getSession()).getUser(token);
        } catch (CustomException e) {
            // если нет пользователя в сессии, то создаем гостевого
            AccountAuthenticator authenticator = new GuestAccountAuthenticator();
            Random rnd = new Random();
            //TODO идентификаторы будут пересекаться с реальными пользователями
            LoginData data = new LoginData(String.valueOf(rnd.nextLong()), null);
            data.setIp(request.getRemoteAddr());
            user = authenticator.login(data);
            user.setLocale(l);

            SessionManager manager = SessionManager.get(request.getSession());
            manager.unregisterToken(token);
            manager.unregisterUser(token);
            manager.registerToken(token);
            manager.registerUser(token, user);

            //SessionManager.get(getServletContext()).touch(token);
            SessionManager.get(request.getSession()).touch(token);
        }

        Connector connector = (Connector) request.getAttribute("connector");
        String applicationCode = request.getParameter("application");
        String branchParam = request.getParameter("branch");
        String versionParam = request.getParameter("version");
        if (!StringUtils.isEmpty(applicationCode)) {
            try {
                Version version = null;
                if (branchParam != null && !"".equals(branchParam)) {
                    version = Version.create(branchParam);
                } else if (versionParam != null && !"".equals(versionParam)) {
                    version = Version.parseVersion(versionParam);
                }
                ApplicationData app = connector.getApplication(applicationCode, version, user);
                // приложение существует и не заблокировано и (гостевое или не гостевое и пользователь авторизован
                if (app != null && !app.isBlocked() && (app.isGuest() || (!app.isGuest() && !user.isGuest()))) {
                    theme = Optional.ofNullable(app.getTheme()).map(Theme::getPath).orElse(theme);

                    // javascript
                    for (String script : app.getScripts()) {
%>
<script type="text/javascript"
    src="resource?action=download&code=<%=applicationCode%>&path=javascript&fileName=<%=script%>"></script>
<%
                    }

                    // css
                    for (String css : app.getCss()) {
%>
<link type="text/css" rel="stylesheet"
    href="resource?action=download&code=<%=applicationCode%>&path=css&fileName=<%=css%>">
<%
                    }
                    if (app.getHeaderHtml() != null && !app.getHeaderHtml().isEmpty()) {
%><%=app.getHeaderHtml()%>
<%
                    }
                }
            } catch (CustomException e) {
            }
        }
    }
%>

<style type="text/css" media="print">
.xs-no-print {
    display: none;
}
</style>
<style type="text/css">
* {
    margin: 0px;
    padding: 0px;
}

#loading {
    position: absolute;
    left: 45%;
    top: 40%;
    margin-left: -45px;
    padding: 2px;
    z-index: 20001;
    height: auto;
    border: 1px solid #ccc;
}

#loading a {
    color: #225588;
}

#loading .loading-indicator {
    background: white;
    color: #444;
    font: bold 13px tahoma, arial, helvetica;
    padding: 10px;
    margin: 0;
    height: auto;
}

#loading .loading-indicator img {
    margin-right: 8px;
    float: left;
    vertical-align: top;
}

#loading-msg {
    font: normal 10px arial, tahoma, sans-serif;
}
</style>

</head>

<body>
    <input id="firefox-bug-654072" style="display: none;" disabled="true" />

    <noscript>
        <H3><%=I18NMessage.getMessage(l).page_noJavaScript()%></H3>
    </noscript>

    <div id="session-timeout" style="display: none;"><%=sessionTimeout%></div>

    <iframe src="javascript:''" id="__gwt_historyFrame" tabIndex='-1'
        style="position: absolute; width: 0; height: 0; border: 0"></iframe>

    <div id="loading">
        <div class="loading-indicator">
            <table>
                <tr>
                    <td><img src="image/ajax-loader.gif"
                        style="margin-right: 8px; float: left; vertical-align: top;" /></td>
                    <td><%=I18NMessage.getMessage(l).page_loading()%></td>
                </tr>
            </table>
        </div>
    </div>

    <div id="div_login_form" class="xs-login-form" style="display: none;">
        <form id="login_form" name="login_form" method="post" action="app"
            onSubmit="login(); return false;">
            <table>
                <tr>
                    <td class="xs-left"><%=I18NMessage.getMessage(l).login_login()%></td>
                    <td class="xs-right"><input id="login-field"
                        name="login-field" type="text" class="xs-input" tabindex="1" /></td>
                </tr>
                <tr>
                    <td class="xs-left"><%=I18NMessage.getMessage(l).login_password()%></td>
                    <td class="xs-right"><input id="pwd-field" name="pwd-field"
                        type="password" class="xs-input" tabindex="2" /></td>
                </tr>
                <tr>
                    <td class="xs-left"></td>
                    <td class="xs-right"><input id="submit-btn" name="submit-btn"
                        type="submit"
                        value="<%=I18NMessage.getMessage(l).login_button()%>" tabindex="3"
                        style="width: 60px;" /></td>
                </tr>
            </table>
        </form>
    </div>
    <div id="login-header-template" style="display: none;"><%=header%></div>
    <div id="login-footer-template" style="display: none;"><%=footer%></div>
    <script type="text/javascript">
        document.getElementById('loading-msg').innerHTML = '<%=I18NMessage.getMessage(l).page_loadingInnerHTML()%>';
    </script>
    <script type="text/javascript" src="application<%=theme%>/application<%=theme%>.nocache.js"></script>
</body>
</html>
