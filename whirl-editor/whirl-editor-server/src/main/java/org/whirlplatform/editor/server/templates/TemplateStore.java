package org.whirlplatform.editor.server.templates;

import org.whirlplatform.editor.shared.RPCException;
import org.whirlplatform.editor.shared.templates.BaseTemplate;

import java.util.List;

public interface TemplateStore {

    public String saveTemplate(BaseTemplate template) throws RPCException;

    public List<BaseTemplate> loadEventTemplates() throws RPCException;

    public List<BaseTemplate> loadComponentTemplates() throws RPCException;

    public void deleteTemplate(BaseTemplate template) throws RPCException;

}
