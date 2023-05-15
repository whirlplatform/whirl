package org.whirlplatform.server.metadata.store;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import org.whirlplatform.meta.shared.ApplicationStoreData;
import org.whirlplatform.meta.shared.Version;
import org.whirlplatform.meta.shared.editor.ApplicationElement;
import org.whirlplatform.meta.shared.editor.FileElement;
import org.whirlplatform.meta.shared.editor.FileElementCategory;
import org.whirlplatform.server.login.ApplicationUser;

public interface MetadataStore {

    ApplicationElement loadApplication(String code, Version version) throws MetadataStoreException;

    ApplicationElement loadApplication(String code, Version version, boolean ignoreReferences)
        throws MetadataStoreException;

    void saveApplication(ApplicationElement application, Version version, ApplicationUser user)
        throws MetadataStoreException;

    String serialize(ApplicationElement application) throws MetadataStoreException;

    ApplicationElement deserialize(String data) throws MetadataStoreException;

    List<ApplicationStoreData> all() throws MetadataStoreException;

    InputStream getApplicationFileInputStream(String appCode, Version appVersion,
                                              FileElementCategory category,
                                              String fileName) throws MetadataStoreException;

    void saveApplicationAs(ApplicationElement application, Version oldVersion, Version newVersion,
                           ApplicationUser user)
        throws MetadataStoreException;

    void copyFileElements(ApplicationStoreData source, ApplicationStoreData destination,
                          List<FileElement> toCopy)
        throws MetadataStoreException;

    /**
     * Запаковывает все содержимое директории приложения в zip-файл.
     */
    void packageToZip(String appCode, Version appVersion, OutputStream out) throws MetadataStoreException, IOException;

    /**
     * Возвращает последнюю версию приложения с кодом appCode.
     *
     * @param appCode - код приложения
     * @return Самая старшая версия приложения или null.
     */
    Version getLastVersion(final String appCode);

    Map<String, String> getAllowedApplications();
}
