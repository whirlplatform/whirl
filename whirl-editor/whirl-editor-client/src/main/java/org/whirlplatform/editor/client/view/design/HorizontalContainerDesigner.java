package org.whirlplatform.editor.client.view.design;

import com.sencha.gxt.dnd.core.client.DND.Feedback;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.widget.core.client.container.InsertResizeContainer;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.editor.client.dnd.HorizontalLayoutDropTarget;
import org.whirlplatform.meta.shared.editor.ComponentElement;
import org.whirlplatform.meta.shared.editor.LocaleElement;

public class HorizontalContainerDesigner extends IndexContainerDesigner {

    public HorizontalContainerDesigner(LocaleElement defaultLocale,
                                       ComponentElement element) {
        super(defaultLocale, element);
    }

    @Override
    protected void initRootDropTarget(final ComponentElement element,
                                      final ComponentBuilder builder) {
        HorizontalLayoutDropTarget target = new HorizontalLayoutDropTarget(
                (InsertResizeContainer) builder.getComponent()) {
            protected void onDragDrop(DndDropEvent event) {
                HorizontalContainerDesigner.super.onRootDrop(element, builder,
                        event.getData(), getIndex());
                super.onDragDrop(event);
            }

        };
        target.setFeedback(Feedback.BOTH);
    }

}
