package org.whirlplatform.editor.client.tree.dummy;

import org.whirlplatform.editor.client.tree.visitor.TreeElementVisitor;
import org.whirlplatform.editor.shared.i18n.EditorMessage;
import org.whirlplatform.meta.shared.editor.ElementVisitor;
import org.whirlplatform.meta.shared.editor.ElementVisitor.VisitContext;

/**
 * Заголовок для раздела Контекстное меню
 * 
 * @author bedritckiy_mr
 */
@SuppressWarnings("serial")
public class DummyMenuItems extends AbstractDummyElement {
	private static final String ID_PREFIX = "dummy-component-menu_items-";
	private static final String TITLE = EditorMessage.Util.MESSAGE.dummy_context_menu();

	public DummyMenuItems() {
		super();
	}

	public DummyMenuItems(final String id) {
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
