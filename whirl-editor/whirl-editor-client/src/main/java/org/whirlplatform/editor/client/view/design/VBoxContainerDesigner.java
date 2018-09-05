package org.whirlplatform.editor.client.view.design;

import com.sencha.gxt.dnd.core.client.DND.Feedback;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.widget.core.client.container.InsertResizeContainer;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.editor.client.dnd.VerticalLayoutDropTarget;
import org.whirlplatform.meta.shared.editor.ComponentElement;
import org.whirlplatform.meta.shared.editor.LocaleElement;

public class VBoxContainerDesigner extends ComponentDesigner {

	public VBoxContainerDesigner(LocaleElement defaultLocale,
			ComponentElement element) {
		super(defaultLocale, element);
	}

	@Override
	protected void initRootDropTarget(final ComponentElement element,
			final ComponentBuilder builder) {
		VerticalLayoutDropTarget target = new VerticalLayoutDropTarget(
				(InsertResizeContainer) builder.getComponent()) {
			protected void onDragDrop(DndDropEvent event) {
				VBoxContainerDesigner.super.onRootDrop(element, builder, event.getData(), getIndex());
				super.onDragDrop(event);
            }
        };
		target.setFeedback(Feedback.BOTH);
	}
	
}
