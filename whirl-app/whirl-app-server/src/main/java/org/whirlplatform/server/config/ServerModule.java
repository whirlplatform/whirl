package org.whirlplatform.server.config;

import com.google.inject.Scopes;
import com.google.inject.servlet.ServletModule;
import org.glassfish.jersey.servlet.ServletContainer;
import org.whirlplatform.rpc.server.DataServiceImpl;
import org.whirlplatform.rpc.server.JsonParamConverterProvider;
import org.whirlplatform.rpc.server.RestExceptionMapper;
import org.whirlplatform.server.cache.CacheFilter;
import org.whirlplatform.server.form.captcha.CaptchaImgServlet;
import org.whirlplatform.server.i18n.I18NFilter;
import org.whirlplatform.server.scheduler.MainInitializerServlet;
import org.whirlplatform.server.servlet.*;

import java.util.HashMap;

public class ServerModule extends ServletModule {

    @Override
    protected void configureServlets() {
        // фильтр кеширования запросов
        filter("/application", "/application/application.nocache.js")
                .through(CacheFilter.class);
        // фильтр для локалей
        filter("/*").through(I18NFilter.class);

        // Основная страница
        serve("/uniapp").with(MainController.class);
        // Сервлет RPC
//		serve("/application/data").with(DataServiceImpl.class);
        // Регистрация REST приложения
        HashMap<String, String> servletParams = new HashMap<>();
        servletParams.put("javax.ws.rs.Application", RestApplication.class.getName());
        bind(ServletContainer.class).in(Scopes.SINGLETON);
        serve("/application/data/*").with(ServletContainer.class, servletParams);

//		serve("/application").with(HttpServletDispatcher.class, servletParams);
//		bind(HttpServletDispatcher.class).in(Scopes.SINGLETON);
////		bind(JacksonJsonProvider.class);
        bind(DataServiceImpl.class);
        bind(JsonParamConverterProvider.class);
        bind(RestExceptionMapper.class);

        // Сервлет экспорта
        serve("/export").with(ExportServlet.class);
        // Сервлет импорта
        serve("/import").with(ImportServlet.class);
        // Сервлет отчетов
        serve("/report").with(ReportServlet.class);
        // Сервлет капчи
        serve("/captcha.png").with(CaptchaImgServlet.class);
        // TODO удалить Сервлет функций
        // serve("/function").with(FunctionServlet.class);
        // Сервлет ресурсы
        serve("/resource").with(ResourceServlet.class);
        // Сервлет файлы для таблиц
        serve("/file").with(FileServlet.class);
        // Сервлет вызова сервисов
//		serveRegex("/service/([a-z]+)/([a-z]+)$").with(RewriteRunner.class);
        // Сервлет для статики
        serveRegex("/static/.+$").with(StaticResourceServlet.class);
        //Jobs
        serve("/job-initializer").with(MainInitializerServlet.class);
    }

}
