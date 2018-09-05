package org.whirlplatform.component.client;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.AttachEvent.Handler;
import com.google.gwt.event.shared.*;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.util.Format;
import com.sencha.gxt.core.client.util.Util;
import com.sencha.gxt.core.shared.FastMap;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.menu.Menu;
import org.whirlplatform.component.client.base.ContextMenuItemBuilder;
import org.whirlplatform.component.client.event.*;
import org.whirlplatform.component.client.form.GridLayoutData;
import org.whirlplatform.component.client.selenium.Locator;
import org.whirlplatform.component.client.selenium.LocatorAware;
import org.whirlplatform.component.client.selenium.WrapperAware;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataValue;

import java.util.*;

/**
 * Абстрактный класс - построитель компонента
 * <p>
 * Базовый класс всех построителей компонентов, все компоненты должны его
 * расширять.
 */
public abstract class ComponentBuilder implements HasHandlers, AttachEvent.HasAttachHandlers, CreateEvent.HasCreateHandlers, DetachEvent.HasDetachHandlers,
        FocusEvent.HasFocusHandlers, LocatorAware, WrapperAware, BlurEvent.HasBlurHandlers, ShowEvent.HasShowHandlers, HideEvent.HasHideHandlers, HasLayoutData,
        CloseProvider, TitleProvider, HasCode {

    public static final String CBUILDER = "CBUILDER";
    public static final String DEFAULT_CODE = "code";
    private String id;

    protected Component componentInstance;
    private boolean created = false;

    protected ComponentBuilder parentBuilder;

    protected Map<String, DataValue> builderProperties = new FastMap<DataValue>();
    protected Set<String> replaceableProperties = new HashSet<String>();

    public boolean hidden = false;

    private String code = DEFAULT_CODE;

    private boolean refreshable = true;

    protected String contextMenuId;

    private boolean closable = true;
    private String title;

    private HandlerManager handlerManager;

    protected List<ContextMenuItemBuilder> contextMenuItems = new ArrayList<ContextMenuItemBuilder>();

    public ComponentBuilder(Map<String, DataValue> builderProperties) {
        super();
        createClear(builderProperties);
    }

    public ComponentBuilder() {
        super();
        createClear();
    }

    /**
     * Установка ID компонета
     *
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Получение ID компонента
     *
     * @return String
     */
    public String getId() {
        return id;
    }

    /**
     * Получение типа компонента
     *
     * @return ComponentType
     */
    public abstract ComponentType getType();

    /**
     * Установка атрибутов
     *
     * @param properties   - Map< String, String >, Название и значение атрибута
     * @param forComponent - boolean
     */
    public void setProperties(Map<String, DataValue> properties, boolean forComponent) {
        builderProperties.putAll(properties);
        if (forComponent) {
            for (String key : properties.keySet()) {
                setProperty(key, properties.get(key));
            }
        }
    }

    /**
     * Получение всех атрибутов
     *
     * @return Map<        String       ,               String        >, Название и значение атрибута
     */
    public Map<String, DataValue> getProperties() {
        return Collections.unmodifiableMap(builderProperties);
    }

    /**
     * Создает экземпляр компонента.
     *
     * @return Component
     */
    protected abstract Component init(Map<String, DataValue> builderProperties);

    private Component createClear() {
        return createClear(Collections.emptyMap());
    }

    private Component createClear(Map<String, DataValue> builderProperties) {
        if (!created) {
            componentInstance = init(builderProperties);
            componentInstance.setData(CBUILDER, this);

            initHandlers();

            fireEvent(new CreateEvent());

            created = true;
        }
        return componentInstance;
    }

    /**
     * Основной метод создания компонента.<br>
     * <p>
     * Метод создает компонент по переданной карте параметров.
     *
     * @return Созданный компонент
     */
    public Component create() {
        // инициализация
        setInitProperties(builderProperties);

        buildContextMenu();

        return componentInstance;
    }

    /**
     * Установка атрибутов
     *
     * @param properties - Map< String, String >, Название и значение атрибутов
     */
    protected void setInitProperties(Map<String, DataValue> properties) {
        // установка атрибутов
        setProperties(properties, true);
    }

    protected void buildContextMenu() {
        if (!contextMenuItems.isEmpty()) {
            Menu menu = new Menu();
            for (ContextMenuItemBuilder item : contextMenuItems) {
                menu.add(item.create());
            }
            componentInstance.setContextMenu(menu);
        }
    }

    /**
     * Получение сущности компонента
     *
     * @return < C > C
     */
    protected abstract <C> C getRealComponent();

    /**
     * Установка атрибута
     *
     * @param name  - String, название атрибута
     * @param value - String, значение атрибута
     * @return boolean
     */
    public boolean setProperty(String name, DataValue value) {
        builderProperties.put(name, value);

        if (name.equalsIgnoreCase(PropertyType.DomId.getCode()) && value != null) {
            setDomId(value.getString());
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.Hidden.getCode()) && value != null) {
            setHidden(Boolean.TRUE.equals(value.getBoolean()));
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.Code.getCode()) && value != null) {
            setCode(value.getString());
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.Width.getCode())) {
            if (value != null && value.getInteger() != null && value.getInteger() != 0) {
                setWidth(value.getInteger());
            }
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.Height.getCode())) {
            if (value != null && value.getInteger() != null && value.getInteger() != 0) {
                setHeight(value.getInteger());
            }
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.Border.getCode()) && value != null) {
            componentInstance.setBorders(Boolean.TRUE.equals(value.getBoolean()));
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.ToolTip.getCode()) && value != null) {
            if (Util.isEmptyString(value.getString())) {
                componentInstance.setTitle("");
            } else {
                componentInstance.setTitle(value.getString());
            }
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.BorderColor.getCode()) && value != null) {
            componentInstance.getElement().getStyle().setBorderColor(value.getString());
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.Style.getCode())) {
            if (value != null && !Util.isEmptyString(value.getString())) {
                componentInstance.getElement().applyStyles(Format.camelize(value.getString()));
            }
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.TabIndex.getCode())) {
            if (value != null && value.getInteger() != null) {
                componentInstance.setTabIndex(value.getInteger());
            }
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.Reloadable.getCode())) {
            if (value != null && value.getBoolean() != null) {
                refreshable = value.getBoolean();
            }
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.ContextMenu.getCode()) && value != null) {
            contextMenuId = value.getString();
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.Closable.getCode())) {
            if (value != null && value.getBoolean() != null) {
                setClosable(value.getBoolean());
            }
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.Title.getCode()) && value != null) {
            setTitle(value.getString());
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.StyleName.getCode())) {
            if (value != null && !Util.isEmptyString(value.getString())) {
                setStyleName(value.getString());
            }
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.DebugId.getCode()) && value != null) {
            componentInstance.ensureDebugId(value.getString());
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.Enabled.getCode())) {
            if (value != null && value.getBoolean() != null) {
                setEnabled(Boolean.TRUE.equals(value.getBoolean()));
                return true;
            }
        }
        return false;
    }

    public void setDomId(String domId) {
        componentInstance.setId(domId);
    }

    public String getDomId() {
        return componentInstance != null ? componentInstance.getElement().getId() : null;
    }

    public void setWidth(int value) {
        componentInstance.setWidth(String.valueOf(value));
    }

    public void setHeight(int value) {
        componentInstance.setHeight(String.valueOf(value));
    }

    /**
     * Инициализация оброботчиков компонента
     */
    protected void initHandlers() {
        componentInstance.addAttachHandler(new Handler() {

            @Override
            public void onAttachOrDetach(com.google.gwt.event.logical.shared.AttachEvent event) {
                if (event.isAttached()) {
                    fireEvent(new AttachEvent());
                } else {
                    fireEvent(new DetachEvent());
                }
            }

        });
        componentInstance.addShowHandler(new com.sencha.gxt.widget.core.client.event.ShowEvent.ShowHandler() {

            @Override
            public void onShow(com.sencha.gxt.widget.core.client.event.ShowEvent event) {
                fireEvent(new ShowEvent());
            }

        });
        componentInstance.addHideHandler(new com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler() {

            @Override
            public void onHide(com.sencha.gxt.widget.core.client.event.HideEvent event) {
                fireEvent(new HideEvent());
            }

        });
        componentInstance.addFocusHandler(new com.sencha.gxt.widget.core.client.event.FocusEvent.FocusHandler() {

            @Override
            public void onFocus(com.sencha.gxt.widget.core.client.event.FocusEvent event) {
                fireEvent(new FocusEvent());
            }

        });
        componentInstance.addBlurHandler(new com.sencha.gxt.widget.core.client.event.BlurEvent.BlurHandler() {

            @Override
            public void onBlur(com.sencha.gxt.widget.core.client.event.BlurEvent event) {
                fireEvent(new BlurEvent());
            }

        });
    }

    /**
     * Получение информации о создании компонента
     *
     * @return boolean
     */
    public boolean isCreated() {
        return created;
    }

    /**
     * Получение информации о закрываемости компонента
     *
     * @return boolean
     */
    @Override
    public boolean isClosable() {
        return closable;
    }

    /**
     * Установка свойства закрываемости компонента
     *
     * @param closable - boolean
     */
    @Override
    public void setClosable(boolean closable) {
        this.closable = closable;
    }

    /**
     * Установка заголовка
     */
    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Получение заголовка
     *
     * @return String
     */
    @Override
    public String getTitle() {
        return title;
    }

    /**
     * Полчение компонента
     *
     * @return Component
     */
    public Component getComponent() {
        return componentInstance;
    }

    /**
     * Получение кода компонента
     *
     * @return String
     */
    @Override
    public String getCode() {
        return code;
    }

    /**
     * Установка кода компонента
     */
    @Override
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Получение свойства скрытости компонента
     *
     * @return boolean
     */
    public boolean isHidden() {
        return hidden;
    }

    /**
     * Установка свойства скрытости компонента
     *
     * @param hidden - boolean
     */
    public void setHidden(boolean hidden) {
        this.hidden = hidden;
        if (componentInstance != null) {
            if (hidden) {
                componentInstance.hide();
            } else {
                componentInstance.show();
            }
        }
    }

    /**
     * Получение родительского компонента
     *
     * @return ComponentBuilder, компонент
     */
    public ComponentBuilder getParentBuilder() {
        return parentBuilder;
    }

    /**
     * Установка родительского компонента
     *
     * @param parentBuilder - ComponentBuilder, компонент
     */
    public void setParentBuilder(ComponentBuilder parentBuilder) {
        this.parentBuilder = parentBuilder;
    }

    /**
     * Отделиться от родительского контейнера
     */
    public void removeFromParent() {
        if (parentBuilder != null && parentBuilder instanceof Containable) {
            ((Containable) parentBuilder).removeChild(this);
        }
    }

    /**
     * Получить информацию о свойстве перезагружаемости компонента
     *
     * @return boolean
     */
    public boolean isReloadable() {
        return refreshable;
    }

    /**
     * Добавляет имя CSS
     *
     * @param name - String
     */
    public void addStyleName(String name) {
        componentInstance.addStyleName(name);
    }

    /**
     * Удаляет имя CSS стиля
     *
     * @param name - String
     */
    public void removeStyleName(String name) {
        componentInstance.removeStyleName(name);
    }

    /**
     * Устанавливает фокус на компоненте.
     */
    public void focus() {
        if (componentInstance == null) {
            return;
        }
        componentInstance.focus();
    }

    /**
     * Установка активности компонента
     *
     * @param enabled - boolean
     */
    public void setEnabled(boolean enabled) {
        ComponentBuilder parent = parentBuilder;
        while (parent != null) {
            if (!parent.isEnabled()) {
                return;
            }
            parent = parent.getParentBuilder();
        }
        componentInstance.setEnabled(enabled);
    }

    /**
     * Получение информации об активности компонента
     *
     * @return boolean
     */
    public boolean isEnabled() {
        return componentInstance.isEnabled();
    }

    /**
     * Получение обработчика
     *
     * @return HandlerManager
     */
    protected HandlerManager ensureHandler() {
        if (handlerManager == null) {
            handlerManager = new HandlerManager(this);
        }
        return handlerManager;
    }

    /**
     * Добавление обработчика
     *
     * @param handler - H
     * @param type    - GwtEvent.Type < H >
     * @return < H extends EventHandler >
     */
    public final <H extends EventHandler> HandlerRegistration addHandler(final H handler, GwtEvent.Type<H> type) {
        return ensureHandler().addHandler(type, handler);
    }

    @Override
    public HandlerRegistration addAttachHandler(AttachEvent.AttachHandler handler) {
        return addHandler(handler, AttachEvent.getType());
    }

    @Override
    public HandlerRegistration addCreateHandler(CreateEvent.CreateHandler handler) {
        return addHandler(handler, CreateEvent.getType());
    }

    @Override
    public HandlerRegistration addDetachHandler(DetachEvent.DetachHandler handler) {
        return addHandler(handler, DetachEvent.getType());
    }

    @Override
    public HandlerRegistration addFocusHandler(FocusEvent.FocusHandler handler) {
        return addHandler(handler, FocusEvent.getType());
    }

    @Override
    public HandlerRegistration addBlurHandler(BlurEvent.BlurHandler handler) {
        return addHandler(handler, BlurEvent.getType());
    }

    @Override
    public HandlerRegistration addShowHandler(ShowEvent.ShowHandler handler) {
        return addHandler(handler, ShowEvent.getType());
    }

    @Override
    public HandlerRegistration addHideHandler(HideEvent.HideHandler handler) {
        return addHandler(handler, HideEvent.getType());
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        ensureHandler().fireEvent(event);
    }

    @Override
    public BorderLayoutData getBorderLayoutData() {
        return ContainerHelper.getBorderLayoutData(builderProperties);
    }

    @Override
    public HorizontalLayoutData getHorizontalLayoutData() {
        return ContainerHelper.getHorizontalLayoutData(builderProperties);
    }

    @Override
    public VerticalLayoutData getVerticalLayoutData() {
        return ContainerHelper.getVerticalLayoutData(builderProperties);
    }

    @Override
    public BoxLayoutData getBoxLayoutData() {
        return ContainerHelper.getBoxLayoutData(builderProperties);
    }

    @Override
    public boolean isNorth() {
        DataValue northData = builderProperties.get(PropertyType.LayoutDataLocation.getCode());
        String north = null;
        if (northData != null) {
            north = northData.getString();
        }
        return "North".equalsIgnoreCase(north);
    }

    @Override
    public boolean isWest() {
        DataValue westData = builderProperties.get(PropertyType.LayoutDataLocation.getCode());
        String west = null;
        if (westData != null) {
            west = westData.getString();
        }
        return "West".equalsIgnoreCase(west);
    }

    @Override
    public boolean isSouth() {
        DataValue southData = builderProperties.get(PropertyType.LayoutDataLocation.getCode());
        String south = null;
        if (southData != null) {
            south = southData.getString();
        }
        return "South".equalsIgnoreCase(south);
    }

    @Override
    public boolean isEast() {
        DataValue eastData = builderProperties.get(PropertyType.LayoutDataLocation.getCode());
        String east = null;
        if (eastData != null) {
            east = eastData.getString();
        }
        return "East".equalsIgnoreCase(east);
    }

    @Override
    public boolean isCenter() {
        DataValue centerData = builderProperties.get(PropertyType.LayoutDataLocation.getCode());
        String center = null;
        if (centerData != null) {
            center = centerData.getString();
        }
        return "Center".equalsIgnoreCase(center);
    }

    @Override
    public GridLayoutData getGridLayoutData() {
        return ContainerHelper.getGridLayoutData(builderProperties);
    }

    @Override
    public int getRowPosition() {
        int res = 0;
        DataValue layoutData = builderProperties.get(PropertyType.LayoutDataFormRow.getCode());
        if (layoutData != null && layoutData.getInteger() != null) {
            res = layoutData.getInteger();
        }
        return res;
    }

    @Override
    public int getColumnPosition() {
        int res = 0;
        DataValue layoutData = builderProperties.get(PropertyType.LayoutDataFormColumn.getCode());
        if (layoutData != null && layoutData.getInteger() != null) {
            res = layoutData.getInteger();
        }
        return res;
    }

    @Override
    public int getIndexPosition() {
        int res = 0;
        DataValue layoutData = builderProperties.get(PropertyType.LayoutDataIndex.getCode());
        if (layoutData != null && layoutData.getInteger() != null) {
            res = layoutData.getInteger();
        }
        return res;
    }

    /**
     * Установка стиля компонета
     *
     * @param styleName - String, название стиля
     */
    public void setStyleName(String styleName) {
        componentInstance.setStyleName(styleName);
    }

    public void setReplaceableProperties(Collection<String> properties) {
        replaceableProperties.clear();
        replaceableProperties.addAll(properties);
    }

    public Collection<String> getReplaceableProperties() {
        return Collections.unmodifiableSet(replaceableProperties);
    }

    public void mask() {
        componentInstance.mask();
    }

    public void mask(String message) {
        componentInstance.mask(message);
    }

    public void unmask() {
        componentInstance.unmask();
    }

    public void addContextMenuItem(ContextMenuItemBuilder item) {
        contextMenuItems.add(item);
    }

    public void removeContextMenuItem(ContextMenuItemBuilder item) {
        contextMenuItems.remove(item);
    }

    public List<ContextMenuItemBuilder> getContextMenuItems() {
        return Collections.unmodifiableList(contextMenuItems);
    }

    @Override
    public Widget getWrapper() {
        return getRealComponent();
    }

    public static class LocatorParams {
        public static String PARAMETER_ID = "id";
        public static String PARAMETER_CODE = "code";
    }

    @Override
    public void fillLocatorDefaults(Locator locator, Element element) {
        if (!Util.isEmptyString(id)) {
            locator.setParameter(LocatorParams.PARAMETER_ID, id);
        } else if (!Util.isEmptyString(code)) {
            locator.setParameter(LocatorParams.PARAMETER_CODE, code);
        }
    }

    @Override
    public Locator getLocatorByElement(Element element) {
        if (getWrapper().getElement().isOrHasChild(element)) {
            Locator result = new Locator(getType());
            fillLocatorDefaults(result, element);
            return result;
        }
        return null;
    }

    /**
     * По-умолчанию возвращает обёрточный элемент по любому локатору. Вообще
     * говоря, если этот метод вызван, то локатор уже содержит идентификатор
     * билдера. Иначе сюда не попадём.
     */
    public Element getElementByLocator(Locator locator) {
        if (locator.getType().equals(getType().getType())) {
            if (fitsLocator(locator)) {
                return getWrapper().getElement();
            }
        }
        return null;
    }

    protected boolean fitsLocator(Locator locator) {
        if (checkLocatorParameter(LocatorParams.PARAMETER_ID, locator)) {
            return true;
        }
        return checkLocatorParameter(LocatorParams.PARAMETER_CODE, locator);
    }

    private boolean checkLocatorParameter(String parameter, Locator locator) {
        if (locator.hasParameter(LocatorParams.PARAMETER_ID)) {
            final String value = locator.getParameter(parameter);
            if (LocatorParams.PARAMETER_ID.equals(parameter) && !Util.isEmptyString(id) && id.equals(value)) {
                return true;
            }
        }
        if (locator.hasParameter(LocatorParams.PARAMETER_CODE)) {
            final String value = locator.getParameter(parameter);
            return LocatorParams.PARAMETER_CODE.equals(parameter) && !Util.isEmptyString(code) && code.equals(value);
        }
        return false;
    }
}