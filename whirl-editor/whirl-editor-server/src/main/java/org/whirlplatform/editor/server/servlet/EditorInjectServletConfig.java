package org.whirlplatform.editor.server.servlet;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.whirlplatform.server.servlet.InjectServletConfig;

public class EditorInjectServletConfig extends InjectServletConfig {

    @Override
    protected Injector getInjector() {
        return Guice.createInjector(new EditorCoreModule(),
                new EditorServletModule());
    }

}
