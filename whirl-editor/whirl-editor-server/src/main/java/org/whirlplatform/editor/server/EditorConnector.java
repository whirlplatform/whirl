package org.whirlplatform.editor.server;

import java.util.Collection;
import java.util.List;
import org.whirlplatform.editor.shared.RPCException;
import org.whirlplatform.editor.shared.merge.ApplicationsDiff;
import org.whirlplatform.editor.shared.metadata.ApplicationBasicInfo;
import org.whirlplatform.editor.shared.templates.BaseTemplate;
import org.whirlplatform.meta.shared.ApplicationStoreData;
import org.whirlplatform.meta.shared.Version;
import org.whirlplatform.meta.shared.data.RowModelData;
import org.whirlplatform.meta.shared.editor.ApplicationElement;
import org.whirlplatform.meta.shared.editor.db.DataSourceElement;
import org.whirlplatform.meta.shared.editor.db.PlainTableElement;
import org.whirlplatform.meta.shared.editor.db.SchemaElement;
import org.whirlplatform.server.login.ApplicationUser;

public interface EditorConnector {

    ApplicationElement newApplication(ApplicationBasicInfo appInfo, ApplicationUser user)
            throws RPCException;

    boolean isEditorAllowed(String appCode, String userId) throws RPCException;

    Collection<ApplicationStoreData> getApplicationList(ApplicationUser user) throws RPCException;

    ApplicationElement loadApplication(ApplicationStoreData applicationData, ApplicationUser user)
            throws RPCException;

    void saveApplication(ApplicationElement application, Version version, ApplicationUser user)
            throws RPCException;

    Collection<RowModelData> getTableImportList(DataSourceElement datasource, SchemaElement schema,
                                                ApplicationUser user) throws RPCException;

    Collection<PlainTableElement> importTables(DataSourceElement datasource, SchemaElement schema,
                                               Collection<RowModelData> models,
                                               ApplicationUser user, ApplicationElement application)
            throws RPCException;

    ApplicationsDiff diff(ApplicationStoreData left, ApplicationStoreData target)
            throws RPCException;

    ApplicationsDiff diff(ApplicationElement left, ApplicationElement target) throws RPCException;

    ApplicationElement merge(ApplicationsDiff diff) throws RPCException;

    void saveApplicationAs(ApplicationElement application, Version oldVersion, Version newVersion,
                           ApplicationUser user)
            throws RPCException;

    String getNextId();

    String saveTemplate(BaseTemplate template) throws RPCException;

    List<BaseTemplate> loadEventTemplates() throws RPCException;

    List<BaseTemplate> loadComponentTemplates() throws RPCException;

    void deleteTemplate(BaseTemplate template) throws RPCException;
}
