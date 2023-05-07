package org.whirlplatform.server.metadata.store.simple;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Named;
import javax.inject.Singleton;
import org.apache.commons.io.IOUtils;
import org.whirlplatform.meta.shared.ApplicationStoreData;
import org.whirlplatform.meta.shared.Version;
import org.whirlplatform.meta.shared.editor.ApplicationElement;
import org.whirlplatform.meta.shared.version.VersionUtil;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;
import org.whirlplatform.server.login.ApplicationUser;
import org.whirlplatform.server.metadata.store.AbstractMetadataStore;
import org.whirlplatform.server.metadata.store.MetadataStoreException;

/**
 * Хранит приложения в ресурсной папке. Код приложения - имя файла без расширения.
 */
@Singleton
@Named("SimpleMetadataStore")
public class SimpleMetadataStore extends AbstractMetadataStore {
    private static final Logger log = LoggerFactory.getLogger(SimpleMetadataStore.class);

    @Override
    public ApplicationElement loadApplication(String code, Version version,
                                              boolean ignoreReferences)
        throws MetadataStoreException {
        if (version != null) {
            log.warn(String.format("The version of '%s' should be null", code));
        }
        ApplicationElement result = null;
        final String fileName = String.format("%s.xml", code);
        try {
            final String xml =
                IOUtils.toString(this.getClass().getResourceAsStream(fileName), "UTF-8");
            result = deserialize(xml, ignoreReferences);
            log.info(
                String.format("Successfully deserialized the application file '%s'", fileName));
        } catch (IOException e) {
            final String message = String.format("Error loading application file '%s'", fileName);
            log.error(message, e);
            throw new MetadataStoreException(message, e);
        }
        return result;
    }

    @Override
    public void saveApplication(ApplicationElement application, Version version,
                                ApplicationUser user) {
    }

    @Override
    public List<ApplicationStoreData> all() throws MetadataStoreException {
        List<ApplicationStoreData> result = new ArrayList<>();
        Path applicationsRoot = resolveApplicationPath(null, null);
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(applicationsRoot)) {
            applicationsRoot = resolveApplicationPath(null, null);
            for (Path file : stream) {
                if (file.toString().endsWith(".xml")) {
                    ApplicationStoreData data = createApplicationStoreData(file);
                    result.add(data);
                }
            }
        } catch (IOException e) {
            final String message =
                String.format("Error loading application list from %s", applicationsRoot);
            log.error(message, e);
            throw new MetadataStoreException(message, e);
        }
        return result;
    }

    @Override
    protected Path resolveApplicationPath(String appCode, Version appVersion)
        throws MetadataStoreException {
        final String resourcePath =
            getClass().getPackage().toString().replace("package ", "").replace(".", "/");
        URL resourceUrl = getClass().getResource("/" + resourcePath);
        File resourceDir;
        try {
            resourceDir = new File(resourceUrl.toURI());
        } catch (URISyntaxException e) {
            final String message = "Wrong URI syntax";
            log.error(message, e);
            throw new MetadataStoreException(message, e);
        }
        Path result = Paths.get(resourceDir.getAbsolutePath());
        log.info(String.format("The applications root resolved to '%s'", result));
        return result;
    }

    private ApplicationStoreData createApplicationStoreData(final Path appXmlFile)
        throws IOException {
        final String content = new String(Files.readAllBytes(appXmlFile), StandardCharsets.UTF_8);
        ApplicationStoreData result = assembleApplicationStoreData(content);
        final String fileName = appXmlFile.getFileName().toString();
        result.setId(fileName);
        final String code = fileName.replace(".xml", "");
        result.setCode(code);
        result.setVersion(VersionUtil.createVersion("0.0.1"));
        return result;
    }

    @Override
    public void packageToZip(String appCode, Version appVersion, OutputStream out)
        throws MetadataStoreException, IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Version getLastVersion(String appCode) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, String> getAllowedApplications() {
        return null;
    }
}
