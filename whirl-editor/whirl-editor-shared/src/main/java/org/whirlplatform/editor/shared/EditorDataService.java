package org.whirlplatform.editor.shared;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import org.whirlplatform.editor.shared.merge.ApplicationsDiff;
import org.whirlplatform.editor.shared.metadata.ApplicationBasicInfo;
import org.whirlplatform.editor.shared.templates.BaseTemplate;
import org.whirlplatform.meta.shared.ApplicationStoreData;
import org.whirlplatform.meta.shared.ClientUser;
import org.whirlplatform.meta.shared.Version;
import org.whirlplatform.meta.shared.data.RowModelData;
import org.whirlplatform.meta.shared.editor.AbstractElement;
import org.whirlplatform.meta.shared.editor.ApplicationElement;
import org.whirlplatform.meta.shared.editor.db.DataSourceElement;
import org.whirlplatform.meta.shared.editor.db.PlainTableElement;
import org.whirlplatform.meta.shared.editor.db.SchemaElement;

import java.util.Collection;
import java.util.List;

@RemoteServiceRelativePath("data")
public interface EditorDataService extends RemoteService {

    class Util {
        private static EditorDataServiceAsync dataService;

        public static EditorDataServiceAsync getDataService() {
            if (dataService == null) {
                dataService = GWT.create(EditorDataService.class);
            }
            return dataService;
        }
    }

    ApplicationElement newApplication(ApplicationBasicInfo appInfo) throws RPCException;

    Collection<ApplicationStoreData> loadApplicationList() throws RPCException;

    ApplicationElement loadApplication(ApplicationStoreData applicationData) throws RPCException;

    SaveResult saveApplication(SaveData saveData) throws RPCException;

    SaveResult saveApplicationAs(SaveData saveData, Version oldVersion) throws RPCException;

    SaveResult syncServerApplication(ApplicationElement application, Version version) throws RPCException;

    SaveResult loadServerApplication() throws RPCException;

    <T extends AbstractElement> T newElement(AbstractElement parent, AbstractElement element) throws RPCException;

    Collection<RowModelData> getTableImportList(DataSourceElement datasource, SchemaElement schema) throws RPCException;

    Collection<PlainTableElement> importTables(DataSourceElement datasource, SchemaElement schema,
                                               Collection<RowModelData> models) throws RPCException;

    ClientUser login(String login, String password) throws RPCException;

    ClientUser getUser() throws RPCException;

    void createPackage(ApplicationStoreData data) throws RPCException;

    ApplicationsDiff diff(ApplicationStoreData left, ApplicationStoreData right) throws RPCException;

    ApplicationsDiff diff(ApplicationElement left, ApplicationElement right) throws RPCException;

    ApplicationElement merge(ApplicationsDiff diff) throws RPCException;

    String saveTemplate(BaseTemplate template) throws RPCException;

    List<BaseTemplate> loadEventTemplates() throws RPCException;

    List<BaseTemplate> loadComponentTemplates() throws RPCException;

    void deleteTemplate(BaseTemplate template) throws RPCException;

}
