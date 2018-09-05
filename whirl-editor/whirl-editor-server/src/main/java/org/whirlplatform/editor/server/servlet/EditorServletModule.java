package org.whirlplatform.editor.server.servlet;

import com.google.inject.servlet.ServletModule;
import org.whirlplatform.editor.server.EditorDataServiceImpl;
import org.whirlplatform.editor.server.i18n.EditorI18NFilter;

public class EditorServletModule extends ServletModule {

    @Override
    protected void configureServlets() {
        filter("/*").through(EditorI18NFilter.class);
        // Сервлет /editor/data
        serve("/editor/data").with(EditorDataServiceImpl.class);
        // Сервлет /editor/export
        serve("/export").with(ExportServlet.class);
        // Сервлет /editor/import
        serve("/import").with(ImportServlet.class);
        // Сервлет /editor/resource
        serve("/resource").with(ResourceServlet.class);
        // Сервлет /editor/file
        serve("/file").with(FileServlet.class);
        // Сервлет /editor/package
        serve("/package").with(PackageServlet.class);
        serve("/download").with(DownloadServlet.class);
    }

}
