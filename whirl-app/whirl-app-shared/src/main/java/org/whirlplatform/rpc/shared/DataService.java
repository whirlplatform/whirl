package org.whirlplatform.rpc.shared;

import org.whirlplatform.meta.shared.*;
import org.whirlplatform.meta.shared.component.ComponentModel;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.data.ListModelData;
import org.whirlplatform.meta.shared.data.RowModelData;
import org.whirlplatform.meta.shared.form.FormModel;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

/**
 * Передаваемые параметры типа List должны быть обернуты в ListHolder
 */
@Path("/data")
public interface DataService {

    @POST
    @Path("/getUser")
    ClientUser getUser(@FormParam("token") SessionToken token, @FormParam("newSession") boolean newSession,
                       @FormParam("timeZoneKey") String timeZoneKey);

    @POST
    @Path("/login")
    ClientUser login(@FormParam("token") SessionToken token, @FormParam("login") String login,
                     @FormParam("password") String password);

    /**
     * Выход из приложения
     */
    @POST
    @Path("/logout")
    String logout(@FormParam("token") SessionToken token);

    @POST
    @Path("/getApplication")
    ApplicationData getApplication(@FormParam("token") SessionToken token,
                                   @FormParam("applicationCode") String applictionCode, @FormParam("version") Version version,
                                   @FormParam("locale") String locale);

    @POST
    @Path("/getComponents")
    ComponentModel getComponents(@FormParam("token") SessionToken token, @FormParam("appId") String appId);

    @POST
    @Path("/getTableConfig")
    TableConfig getTableConfig(@FormParam("token") SessionToken token, @FormParam("classId") String classId,
                               @FormParam("whereSql") String whereSql, @FormParam("params") Map<String, DataValue> params);

    @POST
    @Path("/getListClassData")
    LoadData<ListModelData> getListClassData(@FormParam("token") SessionToken token,
                                             @FormParam("metadata") ClassMetadata metadata, @FormParam("loadConfig") ClassLoadConfig loadConfig);

    @POST
    @Path("/getTableClassData")
    LoadData<RowModelData> getTableClassData(@FormParam("token") SessionToken token,
                                             @FormParam("metadata") ClassMetadata metadata, @FormParam("loadConfig") ClassLoadConfig loadConfig);

    @POST
    @Path("/getTreeClassData")
    List<RowModelData> getTreeClassData(@FormParam("token") SessionToken token,
                                        @FormParam("metadata") ClassMetadata metadata, @FormParam("loadConfig") TreeClassLoadConfig loadConfig);

    @POST
    @Path("/insert")
    RowModelData insert(@FormParam("token") SessionToken token, @FormParam("metadata") ClassMetadata metadata,
                        @FormParam("config") DataModifyConfig config);

    @POST
    @Path("/update")
    RowModelData update(@FormParam("token") SessionToken token, @FormParam("metadata") ClassMetadata metadata,
                        @FormParam("config") DataModifyConfig config);

    @POST
    @Path("/delete")
    void delete(@FormParam("token") SessionToken token, @FormParam("metadata") ClassMetadata metadata,
                @FormParam("config") DataModifyConfig config);

    @POST
    @Path("getForm")
    FormModel getForm(@FormParam("token") SessionToken token, @FormParam("formId") String formId,
                      @FormParam("parameters") ListHolder<DataValue> parameters);

    @POST
    @Path("getNextEvent")
    EventMetadata getNextEvent(@FormParam("token") SessionToken token, @FormParam("event") EventMetadata event,
                               @FormParam("nextEventCode") String nextEventCode);

    @POST
    @Path("executeServer")
    EventResult executeServer(@FormParam("token") SessionToken token, @FormParam("event") EventMetadata event,
                              @FormParam("parameters") ListHolder<DataValue> parameters);

    @POST
    @Path("getReportFields")
    List<FieldMetadata> getReportFields(@FormParam("token") SessionToken token, @FormParam("classId") String classId);

    @POST
    @Path("saveReportValues")
    void saveReportValues(@FormParam("token") SessionToken token, @FormParam("componentId") String componentId,
                          @FormParam("values") Map<String, DataValue> values);

    @POST
    @Path("getEvent")
    EventMetadata getEvent(@FormParam("token") SessionToken token, @FormParam("eventId") String eventId);

    @POST
    @Path("touch")
    Boolean touch(@FormParam("token") SessionToken token);

    @POST
    @Path("removeToken")
    void removeToken(@FormParam("token") SessionToken token);

    @POST
    @Path("checkCaptchaCode")
    Boolean checkCaptchaCode(@FormParam("token") SessionToken token, @FormParam("captchaCode") String captchaCode,
                             @FormParam("componentId") String componentId);

    @POST
    @Path("getFreeEvent")
    EventMetadata getFreeEvent(@FormParam("token") SessionToken token, @FormParam("eventCode") String eventCode);

    @POST
    @Path("saveLoadConfig")
    @Produces(MediaType.TEXT_PLAIN)
    String saveLoadConfig(@FormParam("token") SessionToken token, @FormParam("loadConfig") ClassLoadConfig loadConfig);

}
