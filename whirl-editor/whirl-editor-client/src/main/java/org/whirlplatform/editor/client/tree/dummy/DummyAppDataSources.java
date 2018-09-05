package org.whirlplatform.editor.client.tree.dummy;

import org.whirlplatform.editor.client.tree.visitor.TreeElementVisitor;
import org.whirlplatform.editor.shared.i18n.EditorMessage;
import org.whirlplatform.meta.shared.editor.ElementVisitor;
import org.whirlplatform.meta.shared.editor.ElementVisitor.VisitContext;

/**
 * Заголовок для раздела Источники данных
 *
 * @author bedritckiy_mr
 */
@SuppressWarnings("serial")
public class DummyAppDataSources extends AbstractDummyElement implements OrderedDummy {
    private static final String ID_PREFIX = "dummy-application-datasources-";
    private static final String TITLE = EditorMessage.Util.MESSAGE.dummy_application_datasources();
    private static final int INDEX = 30;

    public DummyAppDataSources() {
        super();
    }

    public DummyAppDataSources(final String id) {
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
