package org.whirlplatform.editor.client.presenter;

import com.google.gwt.user.client.ui.IsWidget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;
import com.mvp4g.client.view.ReverseViewInterface;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import org.whirlplatform.editor.client.EditorEventBus;
import org.whirlplatform.editor.client.view.EventParameterView;
import org.whirlplatform.editor.shared.i18n.EditorMessage;
import org.whirlplatform.meta.shared.data.*;
import org.whirlplatform.meta.shared.editor.AbstractElement;
import org.whirlplatform.meta.shared.editor.ComponentElement;
import org.whirlplatform.meta.shared.editor.EventParameterElement;

import java.util.Date;

@Presenter(view = EventParameterView.class)
public class EventParameterPresenter extends BasePresenter<EventParameterPresenter.IEventParameterView, EditorEventBus>
        implements ElementPresenter {

    public interface IEventParameterView extends ReverseViewInterface<EventParameterPresenter>, IsWidget {

        void setHeaderText(String text);

        void setType(ParameterType type);

        ParameterType getType();

        void setCode(String name);

        String getCode();

        void setComponent(ComponentElement element);

        ComponentElement getComponent();

        void setComponentCode(String componentName);

        String getCompnentCode();

        void setStorageCode(String storageName);

        String getStorageCode();

        void setDataType(DataType dataType);

        DataType getDataType();

        void setString(String value);

        String getString();

        void setNumber(Double value);

        Double getNumber();

        void setDate(Date value);

        Date getDate();

        void setBoolean(Boolean value);

        Boolean getBoolean();

        void setList(String id, String label);

        String getListId();

        String getListLabel();

        void clearFields();

        void setSelectedDataType(DataType type);

        void initUI();
    }

    private EventParameterElement parameter;

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

    public void onOpenElement(AbstractElement element) {
        if (!(element instanceof EventParameterElement)) {
            return;
        }
        parameter = (EventParameterElement) element;
        view.setHeaderText(EditorMessage.Util.MESSAGE.editing_event_parameter() + parameter.getName());

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
        }
        eventBus.openElementView(view);
    }
}
