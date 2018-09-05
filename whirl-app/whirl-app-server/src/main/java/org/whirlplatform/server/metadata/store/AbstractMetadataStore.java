package org.whirlplatform.server.metadata.store;

import org.apache.commons.lang.StringEscapeUtils;
import org.dom4j.DocumentException;
import org.whirlplatform.meta.shared.ApplicationStoreData;
import org.whirlplatform.meta.shared.Version;
import org.whirlplatform.meta.shared.editor.ApplicationElement;
import org.whirlplatform.meta.shared.editor.FileElement;
import org.whirlplatform.meta.shared.editor.FileElementCategory;
import org.whirlplatform.server.login.ApplicationUser;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLFilter;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import javax.xml.transform.Result;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class AbstractMetadataStore implements MetadataStore {

    private static final List<Class<? extends XMLFilter>> CONVERTERS = new ArrayList<>();

    static {

    }

    @Override
    public ApplicationElement loadApplication(String code, Version version) throws MetadataStoreException {
        ApplicationElement application = loadApplication(code, version, false);
        if (version != null) {
            application.setVersion(version.toString());
        }
        return application;
    }

    @Override
    public String serialize(ApplicationElement application) throws MetadataStoreException {
        try {
            XMLApplicationExporter exporter = new XMLApplicationExporter(application);
            return exporter.exportAsString();
        } catch (IOException e) {
            throw new MetadataStoreException(e);
        }
    }

    @Override
    public ApplicationElement deserialize(String data) throws MetadataStoreException {
        return deserialize(data, false);
    }

    protected ApplicationElement deserialize(String data, boolean ignoreReferences) throws MetadataStoreException {
        // применяем все конвертеры
        String finalData = applyConverters(data);
        try {
            XMLApplicationImporter importer = new XMLApplicationImporter(ignoreReferences);
            return importer.buildApplicationFromString(finalData, this);
        } catch (UnsupportedEncodingException | DocumentException e) {
            throw new MetadataStoreException(e);
        }
    }

    private int getMetaVersion(String data) {
        int metaversion = 0;
        final String value = extractString(data, "metaversion=\"", "\"");
        if (!"".equals(value)) {
            metaversion = Integer.parseInt(value);
        }
        return metaversion;
    }

    protected ApplicationStoreData assembleApplicationStoreData(final String data) {
        ApplicationStoreData result = new ApplicationStoreData();
        String name = extractString(data, "name=\"", "\"");
        name = StringEscapeUtils.unescapeXml(name);
        result.setName(name);
        result.setCode(extractString(data, "<code><![CDATA[", "]]></code>"));
        return result;
    }

    private String extractString(final String source, final String startPattern, final String endPattern) {
        String result = "";
        int start = source.indexOf(startPattern);
        if (start > -1) {
            start = start + startPattern.length();
            int end = source.indexOf(endPattern, start + 1);
            if (end > -1) {
                result = source.substring(start, end);
            }
        }
        return result;
    }

    private String applyConverters(String data) throws MetadataStoreException {
        if (CONVERTERS.size() == 0) {
            return data;
        }
        try {
            XMLReader reader = XMLReaderFactory.createXMLReader();
            int startIndex = getMetaVersion(data);
            for (int i = startIndex; i < CONVERTERS.size(); i++) {
                Class<? extends XMLFilter> filter = CONVERTERS.get(i);
                XMLFilter f = filter.getConstructor(String.class).newInstance(data);
                f.setParent(reader);
                reader = f;
            }
            SAXSource source = new SAXSource(reader, new InputSource(new StringReader(data)));
            StringWriter writer = new StringWriter();
            Result result = new StreamResult(writer);
            TransformerFactory.newInstance().newTransformer().transform(source, result);
            return writer.toString();
        } catch (SAXException | TransformerException | TransformerFactoryConfigurationError | InstantiationException
                | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
                | SecurityException e) {
            throw new MetadataStoreException("Metadata converter applying error", e);
        }
    }

    @Override
    public void saveApplicationDataFiles(String appCode, Version appVersion, Collection<FileElement> dataFiles)
            throws MetadataStoreException {
        Path applicationPath;
        try {
            applicationPath = resolveApplicationPath(appCode, appVersion);
            ApplicationFilesUtil.saveApplicationDataFiles(dataFiles, applicationPath);
        } catch (IOException e) {
            throw new MetadataStoreException(e);
        }
    }

    @Override
    public List<FileElement> getApplicationDataFiles(String appCode, Version appVersion) throws MetadataStoreException {
        Path applicationPath;
        try {
            applicationPath = resolveApplicationPath(appCode, appVersion);
            return ApplicationFilesUtil.getApplicationDataFiles(applicationPath);
        } catch (IOException e) {
            throw new MetadataStoreException(e);
        }
    }

    @Override
    public InputStream getApplicationFileInputStream(String appCode, Version appVersion, FileElementCategory category,
                                                     String fileName) throws MetadataStoreException {
        try {
            Path applicationPath = resolveApplicationPath(appCode, appVersion);
            InputStream result = ApplicationFilesUtil.getInputStream(applicationPath, category, fileName);
            return result;
        } catch (IOException e) {
            throw new MetadataStoreException(e);
        }
    }

    @Override
    public void saveApplicationAs(ApplicationElement application, Version oldVersion, Version newVersion,
                                  ApplicationUser user) throws MetadataStoreException {
        Path oldAppPath;
        Path newAppPath;
        try {
            oldAppPath = resolveApplicationPath(application.getCode(), oldVersion);
            newAppPath = resolveApplicationPath(application.getCode(), newVersion);
            ApplicationFilesUtil.saveApplicationFilesAs(oldAppPath, newAppPath, application);
        } catch (IOException e) {
        }
        saveApplication(application, newVersion, user);
    }

    @Override
    public void copyFileElements(ApplicationStoreData source, ApplicationStoreData destination,
                                 List<FileElement> toCopy) throws MetadataStoreException {
        try {
            final Path srcAppPath = resolveApplicationPath(source.getCode(), source.getVersion());
            final Path dstAppPath = resolveApplicationPath(destination.getCode(), destination.getVersion());
            for (FileElement srcFile : toCopy) {
                ApplicationFilesUtil.copyApplicationFile(srcAppPath, srcFile, dstAppPath);
            }
        } catch (IOException e) {
            throw new MetadataStoreException("Error copying", e);
        }
    }

    protected abstract Path resolveApplicationPath(String appCode, Version appVersion)
            throws MetadataStoreException, IOException;
}
