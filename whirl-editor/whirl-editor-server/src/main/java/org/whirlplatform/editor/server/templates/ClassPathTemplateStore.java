package org.whirlplatform.editor.server.templates;

import com.google.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.whirlplatform.editor.shared.RPCException;
import org.whirlplatform.editor.shared.templates.BaseTemplate;
import org.whirlplatform.meta.shared.editor.EventElement;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;
import org.whirlplatform.server.metadata.store.XMLApplicationImporter;

public class ClassPathTemplateStore implements TemplateStore {

    private final Set<String> eventTemplates;
    private Logger _log = LoggerFactory.getLogger(ClassPathTemplateStore.class);
    private String PACKAGE_EVENTS = "event";
    private Reflections reflections;

    @Inject
    public ClassPathTemplateStore() {
        reflections =
                new Reflections(TemplateStore.class.getPackage().getName() + "." + PACKAGE_EVENTS,
                        new ResourcesScanner());
        eventTemplates = reflections.getResources(Pattern.compile(".*\\.wrt"));
    }


    @Override
    public String saveTemplate(BaseTemplate template) throws RPCException {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<BaseTemplate> loadEventTemplates() throws RPCException {
        List<BaseTemplate> result = new ArrayList<>();
        for (String file : eventTemplates) {
            EventElement event = readEventFromTemplate(file);
            if (event != null) {
                result.add(new BaseTemplate(event, false));
            }
        }
        return result;
    }

    private EventElement readEventFromTemplate(String file) {
        try {
            try (InputStream in = TemplateStore.class.getClassLoader().getResourceAsStream(file)) {
                SAXReader reader = new SAXReader();
                Document document = reader.read(in);
                Element el = document.getRootElement();
                Element eventEl = el.element("event");
                XMLApplicationImporter importer = new XMLApplicationImporter(true);
                return importer.buildEventFromTemplate(eventEl);
            }
        } catch (IOException | DocumentException e) {
            _log.warn("Template load error", e);
            return null;
        }
    }

    @Override
    public List<BaseTemplate> loadComponentTemplates() throws RPCException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteTemplate(BaseTemplate template) throws RPCException {
        throw new UnsupportedOperationException();
    }
}
