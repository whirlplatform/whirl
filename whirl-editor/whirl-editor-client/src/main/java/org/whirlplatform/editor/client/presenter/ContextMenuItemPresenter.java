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
import java.util.Collection;
import org.whirlplatform.editor.client.EditorEventBus;
import org.whirlplatform.editor.client.view.ContextMenuItemView;
import org.whirlplatform.editor.shared.i18n.EditorMessage;
import org.whirlplatform.meta.shared.editor.AbstractElement;
import org.whirlplatform.meta.shared.editor.ApplicationElement;
import org.whirlplatform.meta.shared.editor.ContextMenuItemElement;
import org.whirlplatform.meta.shared.editor.LocaleElement;
import org.whirlplatform.meta.shared.editor.PropertyValue;

@Presenter(view = ContextMenuItemView.class)
public class ContextMenuItemPresenter extends
        BasePresenter<ContextMenuItemPresenter.IContextMenuItemView, EditorEventBus>
        implements ElementPresenter {

    private ContextMenuItemElement item;

    @Override
    public void onOpenElement(AbstractElement element) {
        if (!(element instanceof ContextMenuItemElement)) {
            return;
        }
        item = (ContextMenuItemElement) element;
        view.setHeaderText(
                EditorMessage.Util.MESSAGE.editing_context_menu() + " " + item.getName());
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
        view.setLabel(item.getLabel());
        view.setImageUrl(item.getImageUrl());
        eventBus.openElementView(view);
    }

    @Override
    public void bind() {
        view.initUI();
        ContentPanel panel = ((ContentPanel) view.asWidget());
        panel.addButton(new TextButton(EditorMessage.Util.MESSAGE.apply(), new SelectHandler() {
            @Override
            public void onSelect(SelectEvent e) {
                item.setLabel(view.getLabel());
                item.setImageUrl(view.getImageUrl());

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

    public interface IContextMenuItemView
            extends ReverseViewInterface<ContextMenuItemPresenter>, IsWidget {
        PropertyValue getLabel();

        void setLabel(PropertyValue label);

        String getImageUrl();

        void setImageUrl(String imageUrl);

        void initUI();

        void clearValues();

        void setLocales(Collection<LocaleElement> locales, LocaleElement defaultLocale);

        void setHeaderText(String text);
    }
}
