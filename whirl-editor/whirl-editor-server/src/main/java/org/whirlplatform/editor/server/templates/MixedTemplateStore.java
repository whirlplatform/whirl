package org.whirlplatform.editor.server.templates;

import org.whirlplatform.editor.shared.RPCException;
import org.whirlplatform.editor.shared.templates.BaseTemplate;
import org.whirlplatform.server.config.Configuration;

import javax.inject.Inject;
import java.nio.file.FileSystem;
import java.util.List;

public class MixedTemplateStore implements TemplateStore {

    private final ClassPathTemplateStore classPathStore = new ClassPathTemplateStore();
    private final FileSystemTemplateStore fileSystemTemplateStore;

    @Inject
    public MixedTemplateStore(Configuration configuration, FileSystem fileSystem) {
        fileSystemTemplateStore = new FileSystemTemplateStore(configuration, fileSystem);
    }

    @Override
    public String saveTemplate(BaseTemplate template) throws RPCException {
        return fileSystemTemplateStore.saveTemplate(template);
    }

    @Override
    public List<BaseTemplate> loadEventTemplates() throws RPCException {
        List<BaseTemplate> result = classPathStore.loadEventTemplates();
        result.addAll(fileSystemTemplateStore.loadEventTemplates());
        return result;
    }

    @Override
    public List<BaseTemplate> loadComponentTemplates() throws RPCException {
        List<BaseTemplate> result = classPathStore.loadComponentTemplates();
        result.addAll(fileSystemTemplateStore.loadComponentTemplates());
        return result;
    }

    @Override
    public void deleteTemplate(BaseTemplate template) throws RPCException {
        fileSystemTemplateStore.deleteTemplate(template);
    }
}
