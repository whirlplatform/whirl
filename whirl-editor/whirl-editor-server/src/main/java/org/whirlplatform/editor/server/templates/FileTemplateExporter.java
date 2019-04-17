package org.whirlplatform.editor.server.templates;

import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.whirlplatform.editor.shared.RPCException;
import org.whirlplatform.editor.shared.templates.BaseTemplate;
import org.whirlplatform.meta.shared.editor.ComponentElement;
import org.whirlplatform.meta.shared.editor.EventElement;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;
import org.whirlplatform.server.metadata.store.XMLApplicationExporter;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FileTemplateExporter implements TemplateExporter {

    private Logger _log = LoggerFactory.getLogger(FileTemplateExporter.class);

    protected FileTemplateExporter() {
    }

    private String transformToDocument(BaseTemplate template) throws IOException {
        XMLApplicationExporter exporter = new XMLApplicationExporter(true);
        Document templateFile = DocumentFactory.getInstance().createDocument("UTF-8");
        Element root = templateFile.addElement("ROOT");
        root.addAttribute("type", template.getType().toString());
        root.addAttribute("name", template.getName());
        if (BaseTemplate.Type.COMPONENT_TEMPLATE == template.getType()) {
            exporter.writeComponentTemplate(root, (ComponentElement) template.getElement());
        } else if (BaseTemplate.Type.EVENT_TEMPLATE == template.getType()) {

            exporter.writeEventTemplate(root, (EventElement) template.getElement());
        }
        OutputFormat format = OutputFormat.createPrettyPrint();
        StringWriter string = new StringWriter();
        XMLWriter writer = new XMLWriter(string, format);
        writer.write(templateFile);
        writer.flush();
        writer.close();
        return string.toString();
    }

    @Override
    public void saveTemplate(BaseTemplate template, Path path) throws RPCException {
        try {
            String doc = transformToDocument(template);
            Files.write(path, doc.getBytes("UTF-8"), StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            _log.error(e);
            throw new RPCException(e.getMessage());
        }
    }

}
