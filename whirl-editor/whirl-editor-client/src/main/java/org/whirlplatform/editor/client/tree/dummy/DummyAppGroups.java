package org.whirlplatform.editor.client.tree.dummy;

import org.whirlplatform.editor.client.tree.visitor.TreeElementVisitor;
import org.whirlplatform.editor.shared.i18n.EditorMessage;
import org.whirlplatform.meta.shared.editor.ElementVisitor;
import org.whirlplatform.meta.shared.editor.ElementVisitor.VisitContext;

/**
 * Заголовок для раздела Группы пользователей
 */
@SuppressWarnings("serial")
public class DummyAppGroups extends AbstractDummyElement
        implements ChildrenlessDummy, OrderedDummy {
    private static final String ID_PREFIX = "dummy-application-groups-";
    private static final String TITLE = EditorMessage.Util.MESSAGE.dummy_application_groups();
    private static final int INDEX = 20;

    public DummyAppGroups() {
        super();
    }

    public DummyAppGroups(final String id) {
        super(id);
    }

    @Override
    public <T extends VisitContext> void accept(T ctx, TreeElementVisitor<T> visitor) {
        visitor.visit(ctx, this);
    }

    @Override
    public <T extends VisitContext> void accept(T ctx, ElementVisitor<T> visitor) {
        visitor.visit(ctx, this);
    }

    @Override
    public int getIndex() {
        return INDEX;
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
