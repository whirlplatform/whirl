package org.whirlplatform.editor.client.presenter;

import com.google.gwt.user.client.ui.IsWidget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;
import com.mvp4g.client.view.ReverseViewInterface;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import java.util.Date;
import org.whirlplatform.editor.client.EditorEventBus;
import org.whirlplatform.editor.client.view.EventParameterView;
import org.whirlplatform.editor.shared.i18n.EditorMessage;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.data.DataValueImpl;
import org.whirlplatform.meta.shared.data.ListModelData;
import org.whirlplatform.meta.shared.data.ListModelDataImpl;
import org.whirlplatform.meta.shared.data.ParameterType;
import org.whirlplatform.meta.shared.editor.AbstractElement;
import org.whirlplatform.meta.shared.editor.ComponentElement;
import org.whirlplatform.meta.shared.editor.EventParameterElement;

@Presenter(view = EventParameterView.class)
public class EventParameterPresenter
        extends BasePresenter<EventParameterPresenter.IEventParameterView, EditorEventBus>
        implements ElementPresenter {

    private EventParameterElement parameter;

    public void onOpenElement(AbstractElement element) {
        if (!(element instanceof EventParameterElement)) {
            return;
        }
        parameter = (EventParameterElement) element;
        view.setHeaderText(
                EditorMessage.Util.MESSAGE.editing_event_parameter() + parameter.getName());

        view.clearFields();
        view.setType(parameter.getType());
        view.setCode(parameter.getCode());
        switch (parameter.getType()) {
            case COMPONENT:
                view.setComponent(parameter.getComponent());
                break;
            case COMPONENTCODE:
                view.setComponentCode(parameter.getComponentCode());
                break;
            case STORAGE:
                view.setStorageCode(parameter.getStorageCode());
                break;
            case DATAVALUE:
                DataValue value = parameter.getValue();
                switch (value.getType()) {
                    case STRING:
                        view.setString(value.getString());
                        break;
                    case NUMBER:
                        view.setNumber(value.getDouble());
                        break;

                    case DATE:
                        view.setDate(value.getDate());
                        break;
                    case BOOLEAN:
                        view.setBoolean(value.getBoolean());
                        break;
                    case LIST:
                        ListModelData v = value.getListModelData();
                        view.setList(v.getId(), v.getLabel());
                        break;
                    case FILE:
                        break;
                    default:
                        break;
                }
                view.setSelectedDataType(value.getType());
                break;
            default:
                throw new IllegalArgumentException("Unsupported type");
        }
        eventBus.openElementView(view);
    }

    @Override
    public void bind() {
        view.initUI();
        ContentPanel panel = ((ContentPanel) view.asWidget());
        panel.addButton(new TextButton(EditorMessage.Util.MESSAGE.apply(), new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                parameter.setType(view.getType());
                parameter.setCode(view.getCode());
                parameter.setComponent(view.getComponent());
                parameter.setComponentCode(view.getCompnentCode());
                parameter.setStorageCode(view.getStorageCode());
                parameter.setValue(dataValueFromView());
                eventBus.syncServerApplication();
                eventBus.closeElementView();
            }
        }));
        panel.addButton(new TextButton(EditorMessage.Util.MESSAGE.close(), new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                eventBus.closeElementView();
            }
        }));
    }

    public DataValueImpl dataValueFromView() {
        DataType type = view.getDataType();
        if (type == null) {
            return null;
        }
        DataValueImpl value = new DataValueImpl(type);
        switch (type) {
            case STRING:
                value.setValue(view.getString());
                break;
            case NUMBER:
                value.setValue(view.getNumber());
                break;
            case DATE:
                value.setValue(view.getDate());
                break;
            case BOOLEAN:
                value.setValue(view.getBoolean());
                break;
            case LIST:
                ListModelData data = new ListModelDataImpl();
                data.setId(view.getListId());
                data.setLabel(view.getListLabel());
                value.setValue(data);
                break;
            case FILE:
                break;
            default:
                break;
        }
        return value;
    }

    public interface IEventParameterView
            extends ReverseViewInterface<EventParameterPresenter>, IsWidget {

        void setHeaderText(String text);

        ParameterType getType();

        void setType(ParameterType type);

        String getCode();

        void setCode(String name);

        ComponentElement getComponent();

        void setComponent(ComponentElement element);

        void setComponentCode(String componentName);

        String getCompnentCode();

        String getStorageCode();

        void setStorageCode(String storageName);

        DataType getDataType();

        void setDataType(DataType dataType);

        String getString();

        void setString(String value);

        Double getNumber();

        void setNumber(Double value);

        Date getDate();

        void setDate(Date value);

        Boolean getBoolean();

        void setBoolean(Boolean value);

        void setList(String id, String label);

        String getListId();

        String getListLabel();

        void clearFields();

        void setSelectedDataType(DataType type);

        void initUI();
    }
}
