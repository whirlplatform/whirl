package org.whirlplatform.editor.server.templates;

import com.google.inject.Inject;
import org.whirlplatform.editor.shared.RPCException;
import org.whirlplatform.editor.shared.templates.BaseTemplate;
import org.whirlplatform.meta.shared.editor.AbstractElement;
import org.whirlplatform.server.config.Configuration;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class FileSystemTemplateStore implements TemplateStore {

    protected static final String EXT = ".wrt";
    private static final String TEMPLATES_PATH = "templates";
    private Logger _log = LoggerFactory.getLogger(FileSystemTemplateStore.class);
    private TemplateExporter exporter;
    private TemplateImporter importer;

    private Path pathToTemplatesFolder;
    private FileSystem fileSystem;

    @Inject
    public FileSystemTemplateStore(Configuration configuration, FileSystem fileSystem) {
        this.fileSystem = fileSystem;
        this.pathToTemplatesFolder = initPathToTemplFolder(configuration);
        this.importer = new FileTemplateImporter();
        this.exporter = new FileTemplateExporter();
    }

    private Path initPathToTemplFolder(Configuration config) {
        String contPath = config.lookup("Whirl/work-path");
        return fileSystem.getPath(contPath).resolve(TEMPLATES_PATH);
    }

    private Path getPathToFolder(BaseTemplate.Type type) {
        return pathToTemplatesFolder.resolve(type.getPath());
    }

    Path getPathToFile(BaseTemplate.Type type, String fileName) {
        return getPathToFolder(type).resolve(fileName + EXT);
    }

    @Override
    public String saveTemplate(BaseTemplate template) throws RPCException {
        try {
            Path pathToDir = getPathToFolder(template.getType());
            if (!Files.exists(pathToDir)) {
                Files.createDirectories(pathToDir);
            }
            Path pathToFile = getPathToFile(template.getType(), template.getName());
            if (!Files.exists(pathToFile)) {
                Files.createFile(pathToFile);
            } else {
                int count = 2;
                String fileNum = "";
                while (Files.exists(pathToFile)) {
                    String path = pathToFile.toString();
                    fileNum = "(" + count + ")";
                    ;
                    if (count > 2) {
                        path = path.replaceFirst("\\(\\d*\\)\\" + EXT, fileNum + EXT);
                    } else {
                        path = path.replace(EXT, fileNum + EXT);
                    }
                    pathToFile = pathToFile.resolve(path);
                    count++;
                }
                template.setName(template.getName().concat(fileNum));
                Files.createFile(pathToFile);
            }
            exporter.saveTemplate(template, pathToFile);
            return template.getName();
        } catch (IOException e) {
            _log.error(e);
            throw new RPCException(e.getMessage());
        }
    }

    @Override
    public List<BaseTemplate> loadEventTemplates() throws RPCException {
        return importer.loadEventTemplates(getPathToFolder(BaseTemplate.Type.EVENT_TEMPLATE))
                .stream().map(this::asBaseTemplate).collect(Collectors.toList());
    }

    @Override
    public List<BaseTemplate> loadComponentTemplates() throws RPCException {
        return importer.loadComponentTemplates(getPathToFolder(BaseTemplate.Type.COMPONENT_TEMPLATE))
                .stream().map(this::asBaseTemplate).collect(Collectors.toList());
    }

    private BaseTemplate asBaseTemplate(AbstractElement element) {
        return new BaseTemplate(element, true);
    }

    @Override
    public void deleteTemplate(BaseTemplate template) throws RPCException {
        Path path = getPathToFile(template.getType(), template.getName());
        try {
            Files.delete(path);
        } catch (IOException e) {
            _log.error(e);
            throw new RPCException(e.getMessage());
        }
    }
}
