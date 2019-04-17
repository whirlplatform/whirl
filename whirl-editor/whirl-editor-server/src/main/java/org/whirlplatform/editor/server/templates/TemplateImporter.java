package org.whirlplatform.editor.server.templates;

import org.whirlplatform.editor.shared.RPCException;
import org.whirlplatform.meta.shared.editor.ComponentElement;
import org.whirlplatform.meta.shared.editor.EventElement;

import java.nio.file.Path;
import java.util.List;

public interface TemplateImporter {

    public List<EventElement> loadEventTemplates(Path path) throws RPCException;

    public List<ComponentElement> loadComponentTemplates(Path path) throws RPCException;

}
