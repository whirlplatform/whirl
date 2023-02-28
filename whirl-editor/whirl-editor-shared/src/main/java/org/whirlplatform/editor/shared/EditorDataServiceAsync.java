package org.whirlplatform.editor.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.Collection;
import java.util.List;
import org.whirlplatform.editor.shared.merge.ApplicationsDiff;
import org.whirlplatform.editor.shared.metadata.ApplicationBasicInfo;
import org.whirlplatform.editor.shared.templates.BaseTemplate;
import org.whirlplatform.meta.shared.ApplicationStoreData;
import org.whirlplatform.meta.shared.ClientUser;
import org.whirlplatform.meta.shared.Version;
import org.whirlplatform.meta.shared.component.TipPropertyType;
import org.whirlplatform.meta.shared.data.RowModelData;
import org.whirlplatform.meta.shared.editor.AbstractElement;
import org.whirlplatform.meta.shared.editor.ApplicationElement;
import org.whirlplatform.meta.shared.editor.db.DataSourceElement;
import org.whirlplatform.meta.shared.editor.db.PlainTableElement;
import org.whirlplatform.meta.shared.editor.db.SchemaElement;

public interface EditorDataServiceAsync {

    void newApplication(ApplicationBasicInfo appInfo, AsyncCallback<ApplicationElement> callback);

    void loadApplicationList(AsyncCallback<Collection<ApplicationStoreData>> callback);

    void loadApplication(ApplicationStoreData applicationData,
                         AsyncCallback<ApplicationElement> callback);

    void saveApplication(SaveData saveData, AsyncCallback<SaveResult> callback);

    void saveApplicationAs(SaveData saveData, Version oldVersion,
                           AsyncCallback<SaveResult> callback);

    void syncServerApplication(ApplicationElement application, Version version,
                               AsyncCallback<SaveResult> callback);

    void loadServerApplication(AsyncCallback<SaveResult> callback);

    void newElement(AbstractElement parent, AbstractElement element,
                    AsyncCallback<? extends AbstractElement> callback);

    void getTableImportList(DataSourceElement datasource, SchemaElement schema,
                            AsyncCallback<Collection<RowModelData>> callback);

    void importTables(DataSourceElement datasource, SchemaElement schema,
                      Collection<RowModelData> models,
                      AsyncCallback<Collection<PlainTableElement>> callback);

    void login(String login, String password, AsyncCallback<ClientUser> callback);

    void getUser(AsyncCallback<ClientUser> callback);

    void createPackage(ApplicationStoreData data, AsyncCallback<Void> callback);

    void diff(ApplicationStoreData left, ApplicationStoreData right,
              AsyncCallback<ApplicationsDiff> callback);

    void diff(ApplicationElement left, ApplicationElement right,
              AsyncCallback<ApplicationsDiff> callback);

    void merge(ApplicationsDiff diff, AsyncCallback<ApplicationElement> callback);

    void saveTemplate(BaseTemplate template, AsyncCallback<String> callback);

    void loadEventTemplates(AsyncCallback<List<BaseTemplate>> callback);

    void loadComponentTemplates(AsyncCallback<List<BaseTemplate>> callback);

    void deleteTemplate(BaseTemplate template, AsyncCallback<Void> callback);

    void getIcons(AsyncCallback<List<String>> callback);

}
