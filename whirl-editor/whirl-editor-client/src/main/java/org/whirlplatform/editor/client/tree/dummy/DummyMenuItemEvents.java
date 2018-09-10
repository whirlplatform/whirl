package org.whirlplatform.editor.client.tree.dummy;

import org.whirlplatform.editor.client.tree.visitor.TreeElementVisitor;
import org.whirlplatform.editor.shared.i18n.EditorMessage;
import org.whirlplatform.meta.shared.editor.ElementVisitor;
import org.whirlplatform.meta.shared.editor.ElementVisitor.VisitContext;

/**
 *  Заголовок для раздела События контекстного меню
 */
@SuppressWarnings("serial")
public class DummyMenuItemEvents extends AbstractDummyElement {
	private static final String ID_PREFIX = "dummy-menu_item-events-";
	private static final String TITLE = EditorMessage.Util.MESSAGE.dummy_component_events();

	public DummyMenuItemEvents() {
		super();
	}

	public DummyMenuItemEvents(final String id) {
		super(id);
	}

	@Override
	public <T extends VisitContext> void accept(T ctx, ElementVisitor<T> visitor) {
		visitor.visit(ctx, this);
	}

	@Override
	public <T extends VisitContext> void accept(T ctx, TreeElementVisitor<T> visitor) {
		visitor.visit(ctx, this);
	}

	@Override
	public String getIdPrefix() {
		return ID_PREFIX;
	}

	@Override
	public String getTitle() {
		return TITLE;
	}
}
