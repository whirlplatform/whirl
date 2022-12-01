package org.whirlplatform.server.form;

import com.floreysoft.jmte.Engine;
import java.util.Map;

public class TemplateProcessor {

    private static TemplateProcessor _instance;
    private Engine engine;

    private TemplateProcessor() {
        engine = new Engine();
    }

    public static TemplateProcessor get() {
        if (_instance == null) {
            _instance = new TemplateProcessor();
        }
        return _instance;
    }

    @SuppressWarnings("unchecked")
    public String replace(String template, Map<String, ?> model) {
        return engine.transform(template, (Map<String, Object>) model);
    }
}
