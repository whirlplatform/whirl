package org.whirlplatform.editor.client.presenter;

import com.google.gwt.core.client.Callback;
import com.google.gwt.user.client.ui.IsWidget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;
import com.mvp4g.client.view.ReverseViewInterface;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import org.whirlplatform.editor.client.EditorEventBus;
import org.whirlplatform.editor.client.view.EventView;
import org.whirlplatform.editor.shared.i18n.EditorMessage;
import org.whirlplatform.meta.shared.EventType;
import org.whirlplatform.meta.shared.editor.*;
import org.whirlplatform.meta.shared.editor.db.DataSourceElement;

import java.util.Collection;

@Presenter(view = EventView.class)
public class EventPresenter extends BasePresenter<EventPresenter.IEventView, EditorEventBus>
        implements ElementPresenter {

    public interface IEventView extends ReverseViewInterface<EventPresenter>, IsWidget {

        void setHeaderText(String text);

        void setCode(String code);

        String getCode();

        void setType(EventType type);

        EventType getType();

        void setHandlerType(String handlerType);

        String getHandlerType();

        void setNamed(Boolean named);

        Boolean isNamed();

        void setConfirm(Boolean confirm);

        Boolean isConfirm();

        void setConfirmText(PropertyValue text);

        PropertyValue getConfirmText();

        void setWait(Boolean wait);

        Boolean isWait();

        void setWaitText(PropertyValue text);

        PropertyValue getWaitText();

        void setSchema(String schema);

        String getSchema();

        void setFunction(String function);

        String getFunction();

        void setSource(String source);

        String getSource();

        void setComponent(ComponentElement component);

        ComponentElement getComponent();

        void setTargetComponent(ComponentElement component);

        ComponentElement getTargetComponent();

        void setDataSource(DataSourceElement element);

        DataSourceElement getDataSource();

        Boolean isCreateNewComponent();

        void setCreateNewComponent(Boolean value);

        void clearValues();

        void initUI();

        void setLocales(Collection<LocaleElement> locales, LocaleElement defaultLocale);
    }

    private EventElement event;

    @Override
    public void bind() {
        view.initUI();
        ContentPanel panel = ((ContentPanel) view.asWidget());
        panel.addButton(new TextButton(EditorMessage.Util.MESSAGE.apply(), new SelectHandler() { // "Сохранить"
            @Override
            public void onSelect(SelectEvent e) {
                event.setType(view.getType());
                event.setCode(view.getCode());
                event.setHandlerType(view.getHandlerType());
                event.setNamed(view.isNamed());
                event.setConfirm(view.isConfirm());
                event.setConfirmText(view.getConfirmText());
                event.setWait(view.isWait());
                event.setWaitText(view.getWaitText());
                event.setSchema(view.getSchema());
                event.setFunction(view.getFunction());
                event.setSource(view.getSource());
                event.setComponent(view.getComponent());
                event.setTargetComponent(view.getTargetComponent());
                event.setDataSource(view.getDataSource());
                event.setCreateNew(view.isCreateNewComponent());
                eventBus.syncServerApplication();
                eventBus.closeElementView();
            }
        }));
        panel.addButton(new TextButton(EditorMessage.Util.MESSAGE.close(), new SelectHandler() { // "Закрыть"
            @Override
            public void onSelect(SelectEvent event) {
                eventBus.closeElementView();
            }
        }));
    }

    public void onOpenElement(AbstractElement element) {
        if (!(element instanceof EventElement)) {
            return;
        }
        event = (EventElement) element;
        view.setHeaderText(EditorMessage.Util.MESSAGE.editing_event() + event.getName());
        view.clearValues();
        eventBus.getApplication(new Callback<ApplicationElement, Throwable>() {
            @Override
            public void onSuccess(ApplicationElement result) {
                view.setLocales(result.getLocales(), result.getDefaultLocale());
            }

            @Override
            public void onFailure(Throwable reason) {
            }
        });
        view.setCode(event.getCode());
        view.setType(event.getType());
        view.setHandlerType(event.getHandlerType());
        view.setNamed(event.isNamed());
        view.setConfirm(event.isConfirm());
        view.setConfirmText(event.getConfirmText());
        view.setWait(event.isWait());
        view.setWaitText(event.getWaitText());
        view.setSchema(event.getSchema());
        view.setFunction(event.getFunction());
        view.setSource(event.getSource());
        view.setComponent(event.getComponent());
        view.setTargetComponent(event.getTargetComponent());
        view.setDataSource(event.getDataSource());
        view.setCreateNewComponent(event.isCreateNew());
        eventBus.openElementView(view);
    }
}
