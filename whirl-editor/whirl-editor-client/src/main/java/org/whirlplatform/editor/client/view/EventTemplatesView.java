package org.whirlplatform.editor.client.view;

import com.sencha.gxt.dnd.core.client.DndDragMoveEvent;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.dnd.core.client.GridDropTarget;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.box.PromptMessageBox;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import org.whirlplatform.component.client.utils.InfoHelper;
import org.whirlplatform.editor.shared.i18n.EditorMessage;
import org.whirlplatform.editor.shared.templates.BaseTemplate;
import org.whirlplatform.meta.shared.editor.AbstractElement;
import org.whirlplatform.meta.shared.editor.EventElement;

public class EventTemplatesView extends AbstractTemplatesView {

    EventTemplatesView() {
        super();
        setHeading(EditorMessage.Util.MESSAGE.templ_events());
    }

    protected void initDropTarget() {
        setDropTarget(new GridDropTarget<BaseTemplate>(getGrid()) {
            @Override
            protected void onDragDrop(DndDropEvent event) {
                AbstractElement data = copy((AbstractElement) event.getData());
                final BaseTemplate template = new BaseTemplate(data, true);
                final PromptMessageBox messageBox =
                        new PromptMessageBox(EditorMessage.Util.MESSAGE.templ_save(),
                                EditorMessage.Util.MESSAGE.templ_enter_name());
                messageBox.setToolTip(EditorMessage.Util.MESSAGE.templ_name_message());
                messageBox.getButton(PredefinedButton.OK).addSelectHandler(new SelectHandler() {

                    @Override
                    public void onSelect(SelectEvent event) {
                        String newName = messageBox.getValue();
                        if (newName == null || newName.isEmpty()) {
                            newName = template.getName();
                        }
                        template.setName(newName);
                        getPresenter().saveTemplate(template, true);
                    }
                });
                messageBox.getButton(PredefinedButton.CANCEL).addSelectHandler(new SelectHandler() {

                    @Override
                    public void onSelect(SelectEvent event) {
                        InfoHelper.info("templace-save-cancel", EditorMessage.Util.MESSAGE.undo(),
                                EditorMessage.Util.MESSAGE.templ_save_cancel());
                    }
                });
                messageBox.show();
            }

            @Override
            protected void onDragMove(DndDragMoveEvent event) {
                event.getStatusProxy().setStatus(
                        event.getData() != null && event.getData() instanceof EventElement);
            }
        });
    }
}
