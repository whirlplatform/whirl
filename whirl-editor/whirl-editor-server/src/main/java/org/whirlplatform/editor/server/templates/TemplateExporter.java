package org.whirlplatform.editor.server.templates;

import java.nio.file.Path;
import org.whirlplatform.editor.shared.RPCException;
import org.whirlplatform.editor.shared.templates.BaseTemplate;

public interface TemplateExporter {

    public void saveTemplate(BaseTemplate template, Path path) throws RPCException;

}
