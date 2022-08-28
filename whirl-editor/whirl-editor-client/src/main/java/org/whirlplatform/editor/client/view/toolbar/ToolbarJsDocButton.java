package org.whirlplatform.editor.client.view.toolbar;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Window;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import org.whirlplatform.editor.client.image.ComponentBundle;
import org.whirlplatform.editor.client.presenter.ToolBarPresenter;
import org.whirlplatform.editor.client.view.context.AbstractContextTextButton;
import org.whirlplatform.editor.shared.i18n.EditorMessage;

public class ToolbarJsDocButton extends AbstractContextTextButton<ToolBarPresenter> {

    private static final String API_DOC = "api/js/jsdoc";

    public ToolbarJsDocButton(final ToolBarPresenter context) {
        super(context);
    }

    @Override
    protected SelectEvent.SelectHandler createSelectHandler() {
        return event -> Window.open( API_DOC, "_blank", "");
    }

    @Override
    protected String createButtonTitle() {
        return null;
    }

    @Override
    protected String createToolTip() {
        return EditorMessage.Util.MESSAGE.help_js_api();
    }

    @Override
    protected ImageResource selectButtonIcon() {
        return ComponentBundle.INSTANCE.helpApi();
    }

    @Override
    public void updateState() {

    }
}
