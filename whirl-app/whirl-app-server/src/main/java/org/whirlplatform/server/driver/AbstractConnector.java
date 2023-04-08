package org.whirlplatform.server.driver;

import com.google.gwt.event.dom.client.ClickEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeSet;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;
import org.whirlplatform.meta.shared.AppConstant;
import org.whirlplatform.meta.shared.component.ComponentModel;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.data.DataValueImpl;
import org.whirlplatform.meta.shared.data.RowListValue;
import org.whirlplatform.meta.shared.data.RowListValueImpl;
import org.whirlplatform.meta.shared.data.RowValueImpl;
import org.whirlplatform.meta.shared.editor.ApplicationElement;
import org.whirlplatform.meta.shared.editor.ComponentElement;
import org.whirlplatform.meta.shared.editor.ContextMenuItemElement;
import org.whirlplatform.meta.shared.editor.EventElement;
import org.whirlplatform.meta.shared.editor.LocaleElement;
import org.whirlplatform.meta.shared.editor.PropertyValue;
import org.whirlplatform.meta.shared.editor.condition.ConditionSolver;
import org.whirlplatform.meta.shared.editor.db.AbstractTableElement;
import org.whirlplatform.rpc.shared.CustomException;
import org.whirlplatform.server.db.ConnectException;
import org.whirlplatform.server.db.ConnectionProvider;
import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.driver.multibase.condition.EventConditionSolver;
import org.whirlplatform.server.global.SrvConstant;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;
import org.whirlplatform.server.login.ApplicationUser;

public abstract class AbstractConnector implements Connector {

    private static final List<ComponentType> COMPONENTS_TO_DISABLE = Arrays.asList(
        ComponentType.ButtonType,
        ComponentType.HtmlType,
        ComponentType.LabelType,
        ComponentType.ImageType,
        ComponentType.ContextMenuItemType
    );
    private static Logger _log = LoggerFactory.getLogger(AbstractConnector.class);
    protected ConnectionProvider connectionProvider;

    public AbstractConnector(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    protected ConnectionWrapper aliasConnection(String alias, ApplicationUser user) {
        try {
            return connectionProvider.getConnection(alias, user);
        } catch (ConnectException e) {
            _log.error(e);
            throw new CustomException(e.getMessage());
        }
    }

    public void addCheckedEvents(Collection<EventElement> events, ComponentModel model,
                                 List<DataValue> params,
                                 ApplicationUser user) {
        for (EventElement event : events) {
            if (isEventAvailable(event, params, user)) {
                model.addEvent(event.getHandlerType(),
                    EventElement.eventElementToMetadata(event, user.getLocaleElement()));
            } else if (COMPONENTS_TO_DISABLE.contains(model.getType())
                && event.getHandlerType().equalsIgnoreCase(ClickEvent.getType().toString())) {
                model.setValue(PropertyType.Enabled.name(),
                    new DataValueImpl(DataType.BOOLEAN, Boolean.FALSE));
                model.removeReplaceableProperty(PropertyType.Enabled.name());
            }
        }
    }

    protected boolean isEventAvailable(EventElement event, List<DataValue> params,
                                       ApplicationUser user) {
        ApplicationElement application = user.getApplication();
        boolean result = false;
            try (ConnectionWrapper connection = aliasConnection(
                event.getDataSource() != null ? event.getDataSource().getAlias() :
                    SrvConstant.DEFAULT_CONNECTION,
                user)) {
                ConditionSolver solver =
                    new EventConditionSolver(event, application, appendInitialParams(user, params), user, connection);
                return solver.allowed();
            } catch (SQLException e) {
                _log.error(e);
            }
        return false;
    }

    protected ComponentModel componentElementsToModels(ComponentElement comp,
                                                       List<DataValue> params,
                                                       LocaleElement locale, ApplicationUser user) {
        ComponentModel model = new ComponentModel(comp.getType());
        model.setId(comp.getId());
        model.setValue("Name", new DataValueImpl(DataType.STRING, comp.getName()));

        for (ContextMenuItemElement el : comp.getContextMenuItems()) {
            model.addContextMenuItem(itemElementsToModels(el, params, locale, user));
        }

        for (Entry<PropertyType, PropertyValue> e : comp.getProperties().entrySet()) {
            String c = e.getKey().getCode();
            PropertyValue p = e.getValue();
            DataValue d = p.getValue(locale);

            model.setValue(c, d);
            if (p.isReplaceable()) {
                model.addReplaceableProperty(c);
            }
        }
        addCheckedEvents(comp.getEvents(), model, params, user);

        for (ComponentElement child : comp.getChildren()) {
            if (!ComponentType.FormBuilderType.equals(comp.getType())) {
                model.addChild(componentElementsToModels(child, params, locale, user));
            }
        }
        return model;
    }

    private ComponentModel itemElementsToModels(ContextMenuItemElement el, List<DataValue> params,
                                                LocaleElement locale, ApplicationUser user) {
        ComponentModel item = new ComponentModel(ComponentType.ContextMenuItemType);
        item.setValue(PropertyType.Title.getCode(), el.getLabel().getValue(locale));
        item.setValue(PropertyType.ImageUrl.getCode(),
            new DataValueImpl(DataType.STRING, el.getImageUrl()));
        addCheckedEvents(el.getEvents(), item, params, user);
        for (ContextMenuItemElement childEl : el.getChildren()) {
            item.addChild(itemElementsToModels(childEl, params, locale, user));
        }
        return item;
    }

    /**
     * Рекурсивное шифрование whereSql в компонентах
     *
     * @param model
     */
    protected void encode(ComponentModel model, ApplicationUser user) {
        if (model == null) {
            return;
        }
        DataValue whereSql = model.getValue(PropertyType.WhereSql.getCode());
        DataValue labelExpression = model.getValue(PropertyType.LabelExpression.getCode());
        if (whereSql != null && !StringUtils.isEmpty(whereSql.getString())) {
            model.setValue(PropertyType.WhereSql.getCode(),
                new DataValueImpl(DataType.STRING,
                    user.getEncryptor().encrypt(whereSql.getString())));
        }
        if (labelExpression != null && !StringUtils.isEmpty(labelExpression.getString())) {
            model.setValue(PropertyType.LabelExpression.getCode(),
                new DataValueImpl(DataType.STRING,
                    user.getEncryptor().encrypt(labelExpression.getString())));
        }
        if (model.getChildren() == null || model.getChildren().isEmpty()) {
            return;
        }
        for (ComponentModel m : model.getChildren()) {
            encode(m, user);
        }
    }

    //    public String encrypt(String value, ApplicationUser user) {
    //        return user.getEncryptor().encrypt(value);
    //    }

    @Override
    public AbstractTableElement findTableElement(String tableId, ApplicationUser user) {
        return user.getApplication().findTableElementById(tableId);
    }

    private List<DataValue> initialParams(ApplicationUser user) {
        List<DataValue> result = new ArrayList<>();

        DataValue data;
        if (!user.isGuest()) {
            data = new DataValueImpl(DataType.STRING);
            data.setCode(AppConstant.WHIRL_USER);
            data.setValue(user.getId());
            result.add(data);

            data = new DataValueImpl(DataType.BOOLEAN);
            data.setCode(AppConstant.WHIRL_USER_GUEST);
            data.setValue(false);
            result.add(data);
        } else {
            data = new DataValueImpl(DataType.BOOLEAN);
            data.setCode(AppConstant.WHIRL_USER_GUEST);
            data.setValue(true);
            result.add(data);
        }

        data = new DataValueImpl(DataType.STRING);
        data.setCode(AppConstant.WHIRL_IP);
        data.setValue(user.getIp());
        result.add(data);

        data = new DataValueImpl(DataType.STRING);
        data.setCode(AppConstant.WHIRL_APPLICATION);
        data.setValue(user.getApplication().getCode());
        result.add(data);

        RowListValue list = new RowListValueImpl();
        list.setCode(AppConstant.WHIRL_USER_GROUPS);
        list.setRowList(user.getGroups().stream().map(RowValueImpl::new).collect(Collectors.toList()));
        result.add(list);

        return result;
    }

    @Override
    public List<DataValue> appendInitialParams(ApplicationUser user, List<DataValue> params) {
        List<DataValue> paramMap = initialParams(user);
        if (params != null) {
            paramMap.addAll(params);
        }
        return paramMap.stream().collect(Collectors.collectingAndThen(
            Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(DataValue::getCode))), ArrayList::new));
    }

}