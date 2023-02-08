package org.whirlplatform.server.metadata.store;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;
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

    void saveApplicationDataFiles(String appCode, Version appVersion,
                                  Collection<FileElement> dataFiles)
        throws MetadataStoreException;

    List<FileElement> getApplicationDataFiles(String appCode, Version appVersion)
        throws MetadataStoreException;

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
     * Возвращает последнюю версию приложения с кодом appCode.
     *
     * @param appCode - код приложения
     * @return Самая старшая версия приложения или null.
     */
    Version getLastVersion(final String appCode);
}
