package org.whirlplatform.editor.client.tree.dummy;

import org.whirlplatform.editor.client.tree.visitor.TreeElementVisitor;
import org.whirlplatform.editor.shared.i18n.EditorMessage;
import org.whirlplatform.meta.shared.editor.ElementVisitor;
import org.whirlplatform.meta.shared.editor.ElementVisitor.VisitContext;

/**
 * Заголовок для раздела Клоны таблиц
 * 
 * @author bedritckiy_mr
 */
@SuppressWarnings("serial")
public class DummyTableClones extends AbstractDummyElement {
	private static final String ID_PREFIX = "dummy-table-clones-";
	private static final String TITLE = EditorMessage.Util.MESSAGE.dummy_table_clones();

	public DummyTableClones() {
		super();
	}

	public DummyTableClones(final String id) {
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
