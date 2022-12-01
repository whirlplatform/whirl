package org.whirlplatform.editor.server.templates;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.whirlplatform.editor.shared.RPCException;
import org.whirlplatform.meta.shared.editor.ComponentElement;
import org.whirlplatform.meta.shared.editor.EventElement;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;
import org.whirlplatform.server.metadata.store.XMLApplicationImporter;

public class FileTemplateImporter implements TemplateImporter {

    private Logger _log = LoggerFactory.getLogger(FileTemplateImporter.class);

    protected FileTemplateImporter() {

    }

    @Override
    public List<EventElement> loadEventTemplates(Path path) throws RPCException {
        XMLApplicationImporter importer = new XMLApplicationImporter(false);
        List<EventElement> result = new ArrayList<>();
        List<String> tmplXml = readTemplatesAsStrings(path);
        try {
            for (String xml : tmplXml) {
                Element el = readFromTemplate(xml);
                Element eventEl = el.element("event");
                EventElement event = importer.buildEventFromTemplate(eventEl);
                result.add(event);
            }
            return result;
        } catch (DocumentException e) {
            _log.error(e);
            throw new RPCException(e.getMessage());
        }
    }

    @Override
    public List<ComponentElement> loadComponentTemplates(Path path) throws RPCException {
        XMLApplicationImporter importer = new XMLApplicationImporter(false);
        List<ComponentElement> result = new ArrayList<>();
        List<String> tmplXml = readTemplatesAsStrings(path);
        try {
            for (String xml : tmplXml) {
                Element el = readFromTemplate(xml);
                Element compEl = el.element("component");
                ComponentElement component = importer.buildComponentFromTemplate(compEl);
                result.add(component);
            }
            return result;
        } catch (DocumentException e) {
            _log.error(e);
            throw new RPCException(e.getMessage());
        }
    }

    private Element readFromTemplate(String xml) {
        Element result = null;
        try (InputStream stream = new ByteArrayInputStream(xml.getBytes("UTF-8"))) {
            SAXReader reader = new SAXReader();
            Document document = reader.read(stream);
            result = document.getRootElement();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            _log.error(e);
        }
        return result;
    }

    private List<String> readTemplatesAsStrings(Path pathToFolder) {
        List<String> result = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(pathToFolder,
                "*" + FileSystemTemplateStore.EXT)) {
            for (Path path : stream) {
                String xmlFile = new String(Files.readAllBytes(path), Charset.forName("UTF-8"));
                if (!xmlFile.isEmpty()) {
                    result.add(xmlFile);
                }
            }
        } catch (IOException e) {
            _log.error(e);
        }
        return result;
    }

}
