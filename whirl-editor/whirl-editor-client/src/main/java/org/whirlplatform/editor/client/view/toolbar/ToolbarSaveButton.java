package org.whirlplatform.editor.client.view.toolbar;

import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import org.whirlplatform.editor.client.image.ComponentBundle;
import org.whirlplatform.editor.client.presenter.ToolBarPresenter;
import org.whirlplatform.editor.client.view.context.AbstractContextTextButton;
import org.whirlplatform.editor.shared.i18n.EditorMessage;

public class ToolbarSaveButton extends AbstractContextTextButton<ToolBarPresenter> {

    public ToolbarSaveButton(final ToolBarPresenter context) {
        super(context);
    }

    @Override
    public void updateState() {
        setEnabled(getContext().isApplicationPresent());
    }

    @Override
    protected SelectHandler createSelectHandler() {
        return new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                getContext().saveApplication();
            }
        };
    }

    @Override
    protected String createButtonTitle() {
        return null;
    }

    @Override
    protected ImageResource selectButtonIcon() {
        return ComponentBundle.INSTANCE.save();
    }

    @Override
    protected String createToolTip() {
        return EditorMessage.Util.MESSAGE.save();
    }
}
