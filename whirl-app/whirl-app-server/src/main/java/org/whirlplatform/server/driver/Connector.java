package org.whirlplatform.server.driver;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import org.whirlplatform.meta.shared.ApplicationData;
import org.whirlplatform.meta.shared.ClassLoadConfig;
import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.DataModifyConfig;
import org.whirlplatform.meta.shared.EventMetadata;
import org.whirlplatform.meta.shared.EventResult;
import org.whirlplatform.meta.shared.FieldMetadata;
import org.whirlplatform.meta.shared.FileValue;
import org.whirlplatform.meta.shared.LoadData;
import org.whirlplatform.meta.shared.TreeClassLoadConfig;
import org.whirlplatform.meta.shared.Version;
import org.whirlplatform.meta.shared.component.ComponentModel;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.data.ListModelData;
import org.whirlplatform.meta.shared.data.RowModelData;
import org.whirlplatform.meta.shared.data.TreeModelData;
import org.whirlplatform.meta.shared.editor.PropertyValue;
import org.whirlplatform.meta.shared.editor.db.AbstractTableElement;
import org.whirlplatform.meta.shared.form.FormModel;
import org.whirlplatform.server.form.FormElementWrapper;
import org.whirlplatform.server.login.ApplicationUser;

public interface Connector {

    ApplicationData getApplication(String applicationCode, Version version);

    ApplicationData getApplication(String applicationCode, Version version, ApplicationUser user);

    ComponentModel getComponents(String componentId, List<DataValue> params, ApplicationUser user);

    /**
     * Формирует метаданные таблицы базы данных
     */
    ClassMetadata getClassMetadata(String classId, Map<String, DataValue> params,
                                   ApplicationUser user);

    /**
     * Собирает в БД данные для формирования строк списка
     */
    LoadData<ListModelData> getListClassData(String dataSourceId, ClassLoadConfig loadConfig,
                                             ApplicationUser user);

    /**
     * Собирает в БД данные для формирования строк грида
     */
    LoadData<RowModelData> getTableClassData(ClassMetadata metadata, ClassLoadConfig loadConfig,
                                             ApplicationUser user);

    /**
     * Собирает в БД данные для формирования строк дерева
     */
    List<TreeModelData> getTreeClassData(String dataSourceId, TreeClassLoadConfig loadConfig,
                                         ApplicationUser user);

    /**
     * Вставляет записи в таблицу
     */
    RowModelData insert(ClassMetadata metadata, DataModifyConfig config, ApplicationUser user);

    /**
     * Обновляет записи в таблице
     */
    RowModelData update(ClassMetadata metadata, DataModifyConfig config, ApplicationUser user);

    /**
     * Удаляет записи в таблице
     */
    void delete(ClassMetadata metadata, DataModifyConfig config, ApplicationUser user);

    FormModel getForm(String formId, List<DataValue> params, ApplicationUser user);

    FormElementWrapper getFormRepresent(String formId, List<DataValue> params,
                                        ApplicationUser user);

    void exportXLS(ClassMetadata metadata, boolean columnHeader, boolean xlsx,
                   ClassLoadConfig loadConfig,
                   OutputStream output, ApplicationUser user);

    void exportCSV(ClassMetadata metadata, boolean columnHeader, ClassLoadConfig loadConfig,
                   OutputStream output,
                   ApplicationUser user);

    List<FieldMetadata> getReportFields(String reportId, ApplicationUser user);

    Map<PropertyType, PropertyValue> getReportProperties(String codeOrId, boolean isCode,
                                                         ApplicationUser user);

    /**
     * Выполняет заданную в событии процедуру в базе данных
     */
    EventResult executeDBFunction(EventMetadata event, List<DataValue> params,
                                  ApplicationUser user);

    /**
     * Выполняет заданный в собитии запрос в БД.
     */
    EventResult executeSQL(EventMetadata event, List<DataValue> params, ApplicationUser user);

    /**
     * Ищет событие в приложении пользователя по коду или id
     */
    EventMetadata getEvent(String eventCodeOrId, boolean usingCode, ApplicationUser user);

    /**
     * Ищет событие приложения пользователя по коду
     */
    EventMetadata getFreeEvent(String eventCode, ApplicationUser user);

    /**
     * Находит в цепочке выполняемых событий следующее за parentEvent событие по его коду
     */
    EventMetadata getNextEvent(EventMetadata parentEvent, String nextEventCode,
                               ApplicationUser user);


    /**
     * Извлекает файл из базы данных
     */
    FileValue downloadFileFromTable(String tableId, String column, String rowId,
                                    ApplicationUser user);

    /**
     * Поиск табличного элемента по id текущем приложении пользователя
     */
    AbstractTableElement findTableElement(String tableId, ApplicationUser user);

}
