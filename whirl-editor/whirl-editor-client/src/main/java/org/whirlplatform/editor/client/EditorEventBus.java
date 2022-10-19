package org.whirlplatform.editor.client;

import com.google.gwt.core.client.Callback;
import com.google.gwt.user.client.ui.IsWidget;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.annotation.Start;
import com.mvp4g.client.event.EventBus;
import org.whirlplatform.editor.client.presenter.*;
import org.whirlplatform.editor.client.presenter.compare.CompareApplicationsPresenter;
import org.whirlplatform.editor.client.presenter.tree.ApplicationTreePresenter;
import org.whirlplatform.editor.client.view.AppShowIconsView;
import org.whirlplatform.editor.shared.OpenResult;
import org.whirlplatform.editor.shared.TreeState;
import org.whirlplatform.editor.shared.metadata.ApplicationBasicInfo;
import org.whirlplatform.meta.shared.ApplicationStoreData;
import org.whirlplatform.meta.shared.Version;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.editor.*;
import org.whirlplatform.meta.shared.editor.db.AbstractTableElement;
import org.whirlplatform.meta.shared.editor.db.DataSourceElement;
import org.whirlplatform.meta.shared.editor.db.PlainTableElement;
import org.whirlplatform.meta.shared.editor.db.TableColumnElement;

import java.util.Collection;
import java.util.List;

@Events(startPresenter = MainPresenter.class)
public interface EditorEventBus extends EventBus, ApplicationDataProvider {

    @Start
    @Event(handlers = LoginPresenter.class)
    void start();

    @Event(handlers = MainPresenter.class)
    void initUi();

    @Event(handlers = {ApplicationTreePresenter.class, ToolBarPresenter.class, PalletePresenter.class,
            ApplicationPresenter.class, DesignPresenter.class, EventTemplatesPresenter.class})
    void buildApp();

    @Event(handlers = LoginPresenter.class)
    void showLoginPanel();

    // интерфейс редактора
    @Event(handlers = MainPresenter.class)
    void changeTopComponent(IsWidget component);

    @Event(handlers = MainPresenter.class)
    void changeFirstLeftComponent(IsWidget component);

    @Event(handlers = MainPresenter.class)
    void changeSecondLeftComponent(IsWidget component);

    @Event(handlers = MainPresenter.class)
    void addFirstRightComponent(IsWidget component);

    @Event(handlers = MainPresenter.class)
    void addSecondRightComponent(IsWidget component);

    @Event(handlers = MainPresenter.class)
    void addThirdRightComponent(IsWidget component);

    @Event(handlers = MainPresenter.class)
    void changeSecondRightComponent(IsWidget component);

    @Event(handlers = MainPresenter.class)
    void changeCenterComponent(IsWidget component);

    @Event(handlers = MainPresenter.class)
    void closeElementView();

    @Event(handlers = MainPresenter.class)
    void openElementView(IsWidget component);

    // интерфейс редактора

    /**
     * Открывает представление с приложениями.
     *
     */
    @Event(handlers = AllApplicationsPresenter.class)
    void showOpenApplications();

    @Event(handlers = AllApplicationsPresenter.class)
    void showOpenApplicationsCallback(Callback<ApplicationStoreData, Throwable> callback);

    @Event(handlers = AllApplicationsPresenter.class)
    void openApplicationCallback(Callback<OpenResult, Throwable> callback);

    // приложение
    @Event(handlers = {ElementEventHandler.class})
    void newApplication(ApplicationBasicInfo appInfo);

    /**
     * Загрузить приложение.
     *
     * @param application
     */
    @Event(handlers = {MainPresenter.class, ElementEventHandler.class, LocalePresenter.class,
            PropertyEditorPresenter.class, ToolBarPresenter.class, GroupPresenter.class, ApplicationPresenter.class,
            DesignPresenter.class, ApplicationTreePresenter.class})
    void loadApplication(ApplicationElement application, Version version);

    /**
     * Восстановить состояние приложения.
     *
     * @param state
     */
    @Event(handlers = {ApplicationTreePresenter.class})
    void restoreApplicationState(TreeState state);

    /**
     * Сохранить приложение.
     *
     */
    @Event(handlers = {ElementEventHandler.class})
    void saveApplication();

    @Event(handlers = {ElementEventHandler.class})
    void saveApplicationAs(Version newVersion);

    /**
     * Получает состояние приложения
     *
     * @param callback
     */
    @Event(handlers = {ApplicationTreePresenter.class})
    void getApplicationState(Callback<TreeState, Throwable> callback);

    /**
     * Синхронизирует
     */
    @Event(handlers = ElementEventHandler.class)
    void syncServerApplication();

    /**
     * Сохранить описание приложения как XML-файл.
     */
    @Event(handlers = ElementEventHandler.class)
    void saveApplicationAsXML();

    /**
     * Загрузить приложение из XML-файла.
     */
    @Event(handlers = ElementEventHandler.class)
    void loadApplicationFromXML();

    // приложение

    /**
     * Открыть элемент на редактирование
     *
     */
    @Event(handlers = {DesignPresenter.class, PropertyFormPresenter.class, ApplicationPresenter.class,
            EventPresenter.class, EventParameterPresenter.class, PropertyReportPresenter.class, LocalePresenter.class,
            DataSourcePresenter.class, SchemaPresenter.class, TablePresenter.class, GroupPresenter.class,
            DynamicTablePresenter.class, ContextMenuItemPresenter.class})
    void openElement(AbstractElement element);

    /**
     * Открыть элемент для просмотра
     *
     * @param element
     */
    @Event(handlers = {ApplicationPresenter.class, TablePresenter.class, DynamicTablePresenter.class})
    void viewElement(AbstractElement element);

    // компоненты

    /**
     * Добавляет элемент в родителя.
     *
     * @param parent
     * @param element
     */
    @Event(handlers = ElementEventHandler.class)
    void addElement(AbstractElement parent, AbstractElement element);

    /**
     * Добавление подчиненного элемента. Абсолютно все новые элементы должны
     * добавляться через этот метод.
     *
     * @param parent   элемент-родитель
     * @param element  добавляемый элемент
     * @param callback результат добавления/создания элемента
     */
    @Event(handlers = ElementEventHandler.class)
    void addElementCallback(AbstractElement parent, AbstractElement element,
                            Callback<AbstractElement, Throwable> callback);

    /**
     * Обновление интерфейса. Добавляет элемент в родителя.
     *
     * @param parent
     * @param element
     */
    @Event(handlers = ApplicationTreePresenter.class)
    void addElementUI(AbstractElement parent, AbstractElement element);

    /**
     * Удаляет элемент из родителя.
     *
     * @param parent
     * @param element
     */
    @Event(handlers = ElementEventHandler.class)
    void removeElement(AbstractElement parent, AbstractElement element, boolean showDialog);

    /**
     * Обновление интерфейса. Удаляет элемент из родителя.
     *
     * @param parent
     * @param element
     */
    @Event(handlers = {ApplicationTreePresenter.class, DesignPresenter.class})
    void removeElementUI(AbstractElement parent, AbstractElement element);

    /**
     * Отобразить панель свойств для компонента.
     *
     * @param element
     */
    @Event(handlers = PropertyEditorPresenter.class)
    void selectComponentElement(ComponentElement element);

    /**
     * Отметить в редакторе выбранный компонент.
     *
     * @param element
     */
    @Event(handlers = DesignPresenter.class)
    void selectComponentDesignElement(ComponentElement element);

    /**
     * Изменить свойство компонента.
     *
     * @param type
     * @param locale
     * @param value
     */
    @Event(handlers = PropertyEditorPresenter.class)
    void changeComponentProperty(PropertyType type, LocaleElement locale, boolean replaceable, Object value);

    /**
     * Применить измененное свойства компонента к интерфейсу.
     *
     * @param element
     * @param name
     * @param value
     */
    @Event(handlers = DesignPresenter.class)
    void syncComponentPropertyToUI(ComponentElement element, String name, DataValue value);

    // компоненты

    // формы
    @Event(handlers = DesignPresenter.class)
    void selectGroupCell(AbstractElement element);

    @Event(handlers = DesignPresenter.class)
    void selectColumn(int column, boolean selected);

    @Event(handlers = DesignPresenter.class)
    void selectRow(int row, boolean selected);

    @Event(handlers = DesignPresenter.class)
    void clearSelection();

    @Event(handlers = PropertyFormPresenter.class)
    void setSelectedCellsArea(CellRangeElement model);

    @Event(handlers = {PropertyFormPresenter.class, DesignPresenter.class})
    void insertRow(int index);

    @Event(handlers = {PropertyFormPresenter.class, DesignPresenter.class})
    void insertColumn(int index);

    @Event(handlers = {PropertyFormPresenter.class, DesignPresenter.class})
    void deleteRow(int index);

    @Event(handlers = {PropertyFormPresenter.class, DesignPresenter.class})
    void deleteColumn(int index);

    // формы

    // данные для списков
    @Event(handlers = ElementEventHandler.class)
    void getAvailableComponents(Callback<Collection<ComponentElement>, Throwable> callback);

    @Event(handlers = ElementEventHandler.class)
    void getAvailableTables(Callback<Collection<AbstractTableElement>, Throwable> callback);

    @Event(handlers = ElementEventHandler.class)
    void getAvailableColumns(PlainTableElement table, Callback<Collection<TableColumnElement>, Throwable> callback);

    // данные для списков

    @Event(handlers = ApplicationTreePresenter.class)
    void selectTreeElement(AbstractElement element);

    @Event(handlers = LocalePresenter.class)
    void updateLocales(List<LocaleElement> locales);

    @Event(handlers = PropertyFormPresenter.class)
    void syncRowsNumber(int rows);

    @Event(handlers = PropertyFormPresenter.class)
    void syncColumnsNumber(int columns);

    @Event(handlers = PropertyEditorPresenter.class)
    void syncComponentPropertyToElement(ComponentElement element);

    @Event(handlers = ElementEventHandler.class)
    void getAvailableGroups(Callback<Collection<GroupElement>, Throwable> callback);

    @Event(handlers = ElementEventHandler.class)
    void getElementRights(Collection<AbstractElement> elements,
                          Callback<Collection<RightCollectionElement>, Throwable> callback);

    @Event(handlers = ElementEventHandler.class)
    void setElementRights(AbstractElement element, RightCollectionElement rights);

    @Event(handlers = RightEditPresenter.class)
    void editRights(Collection<? extends AbstractElement> elements, Collection<RightType> rightTypes);

    // Для вытаскивания defaultLocale и locales. Может придумать как-то по
    // другому?
    @Event(handlers = ElementEventHandler.class)
    void getApplication(Callback<ApplicationElement, Throwable> callback);

    @Event(handlers = ElementEventHandler.class)
    void getApplicationVersion(Callback<Version, Throwable> callback);

    @Event(handlers = ElementEventHandler.class)
    void getDataSources(Callback<Collection<DataSourceElement>, Throwable> callback);

    /**
     * Сравнение приложений
     */
    @Event(handlers = CompareApplicationsPresenter.class)
    void startCompareApplications();

    @Event(handlers = CompareApplicationsPresenter.class)
    void startCompareApplication(ApplicationElement leftApp, Version leftVersion);

    /**
     * Получение информации для создания нового приложения.
     */
    @Event(handlers = AppBasicInfoPresenter.class)
    void getAppInfoForNew(Callback<ApplicationBasicInfo, Throwable> callback);

    /**
     * Получение информации для "сохраняемого как" приложения.
     */
    @Event(handlers = AppBasicInfoPresenter.class)
    void getAppInfoForSaveAs(ApplicationBasicInfo info, Callback<ApplicationBasicInfo, Throwable> callback);

    @Event(handlers = AppShowIconsPresenter.class)
    void getIcons();
}
