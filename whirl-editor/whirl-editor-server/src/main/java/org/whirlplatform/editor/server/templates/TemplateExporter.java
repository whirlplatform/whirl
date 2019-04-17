package org.whirlplatform.editor.server.templates;

import org.whirlplatform.editor.shared.RPCException;
import org.whirlplatform.editor.shared.templates.BaseTemplate;

import java.nio.file.Path;

public interface TemplateExporter {

    public void saveTemplate(BaseTemplate template, Path path) throws RPCException;

}
