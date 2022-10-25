package org.whirlplatform.editor.client.view.toolbar;

import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.widget.core.client.event.SelectEvent;

import org.whirlplatform.editor.client.image.ComponentBundle;
import org.whirlplatform.editor.client.presenter.ToolBarPresenter;
import org.whirlplatform.editor.client.view.context.AbstractContextTextButton;
import org.whirlplatform.editor.shared.i18n.EditorMessage;

public class ToolBarShowIconsButton extends AbstractContextTextButton<ToolBarPresenter> {

    public ToolBarShowIconsButton(ToolBarPresenter context) {
        super(context);
    }

    @Override
    protected SelectEvent.SelectHandler createSelectHandler() {
        return new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                getContext().showIconsPanel();
            }
        };
    }

    @Override
    protected String createButtonTitle() {
        return null;
    }

    @Override
    protected String createToolTip() {
        return EditorMessage.Util.MESSAGE.select_icon();
    }

    @Override
    protected ImageResource selectButtonIcon() {
        return ComponentBundle.INSTANCE.iconSelection();
    }

    @Override
    public void updateState() {
    }


}
