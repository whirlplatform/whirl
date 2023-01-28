package org.whirlplatform.editor.client.tree.part;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import org.whirlplatform.editor.client.meta.NewDynamicTableElement;
import org.whirlplatform.editor.client.meta.NewTableElement;
import org.whirlplatform.editor.client.tree.AppTree;
import org.whirlplatform.editor.client.tree.AppTreePresenter;
import org.whirlplatform.editor.client.tree.dummy.DummyDynamicTables;
import org.whirlplatform.editor.client.tree.dummy.DummyPlainTables;
import org.whirlplatform.meta.shared.editor.AbstractElement;
import org.whirlplatform.meta.shared.editor.RightType;
import org.whirlplatform.meta.shared.editor.db.AbstractTableElement;
import org.whirlplatform.meta.shared.editor.db.DynamicTableElement;
import org.whirlplatform.meta.shared.editor.db.PlainTableElement;
import org.whirlplatform.meta.shared.editor.db.SchemaElement;

public class AppTreeSchemaPart extends AbstractAppTreePart<SchemaElement> {

    private SchemaFolders folders;

    public AppTreeSchemaPart(AppTree appTree, AppTreePresenter treePresenter,
                             SchemaElement schema) {
        super(appTree, treePresenter, schema);
        this.folders = new SchemaFolders(schema);
    }

    @Override
    public void init() {
        addChildElement(handledElement, folders.plainTables);
        addChildElement(handledElement, folders.dynamicTables);

        for (AbstractTableElement t : handledElement.getTables()) {
            doAddElementUI(handledElement, t);
        }
    }

    @Override
    public boolean isRenaming(AbstractElement element) {
        return element == handledElement;
    }

    @Override
    public boolean isAdding(AbstractElement element) {
        return element == folders.plainTables || element == folders.dynamicTables;
    }

    @Override
    public boolean isCopying(AbstractElement element) {
        return false;
    }

    @Override
    public boolean isPasting(AbstractElement parent, AbstractElement element) {
        return parent == folders.plainTables && element instanceof PlainTableElement;
    }

    @Override
    public boolean isEditing(AbstractElement element) {
        return element == handledElement;
    }

    @Override
    public boolean isDeleting(AbstractElement element) {
        return element instanceof AbstractTableElement
            && handledElement.getTables().contains(element);
    }

    @Override
    public boolean hasRights(AbstractElement element) {
        return element == handledElement || element == folders.plainTables
            || element == folders.dynamicTables;
    }

    @Override
    public boolean doAddElement(AbstractElement parent, AbstractElement element) {
        if (parent == folders.plainTables && element == null) {
            treePresenter.riseAddElement(handledElement, new NewTableElement());
            return true;
        } else if (parent == folders.plainTables && element instanceof PlainTableElement) {
            treePresenter.riseAddElement(handledElement, element);
            return true;
        } else if (parent == folders.dynamicTables && element == null) {
            treePresenter.riseAddElement(handledElement, new NewDynamicTableElement());
            return true;
        }
        return false;
    }

    @Override
    public boolean doAddElementUI(AbstractElement parent, AbstractElement element) {
        if (parent == handledElement && element instanceof PlainTableElement
                && handledElement.getTables().contains(element)) {
            removeElement(element);
            addChildElement(folders.plainTables, element);
            putTreePart(element,
                    new AppTreePlainTablePart(appTree, treePresenter, (PlainTableElement) element));
            return true;
        } else if (parent == handledElement && element instanceof DynamicTableElement
                && handledElement.getTables().contains(element)) {
            removeElement(element);
            addChildElement(folders.dynamicTables, element);
            putTreePart(element, new AppTreeDynamicTablePart(appTree, treePresenter,
                    (DynamicTableElement) element));
            return true;
        }
        return false;
    }

    @Override
    public boolean doRemoveElement(AbstractElement parent, AbstractElement element) {
        if (element instanceof AbstractTableElement
                && handledElement.getTables().contains(element)) {
            treePresenter.riseRemoveElement(handledElement, element, true);
            return true;
        }
        return false;
    }

    @Override
    public boolean doRemoveElementUI(AbstractElement parent, AbstractElement element) {
        if (parent == handledElement && appTree.hasChild(folders.plainTables, element, false)) {
            removeElement(element);
            return true;
        }
        return false;
    }

    @Override
    public boolean doEditElement(AbstractElement element) {
        return false;
    }

    @Override
    public boolean doEditElementRights(AbstractElement element) {
        // TODO: Возможно потом потребуется разделить
        if (element == handledElement || element == folders.plainTables) {
            Collection<AbstractTableElement> tables = new HashSet<AbstractTableElement>();
            for (AbstractTableElement t : handledElement.getTables()) {
                tables.add(t);
                if (t instanceof PlainTableElement) {
                    tables.addAll(((PlainTableElement) t).getClones());
                }
            }
            if (!tables.isEmpty()) {
                treePresenter.riseEditRights(tables, Collections.unmodifiableCollection(Arrays
                        .asList(RightType.ADD, RightType.DELETE, RightType.EDIT, RightType.VIEW,
                                RightType.RESTRICT)));
            }
        }
        return false;
    }

    @Override
    public boolean canDragDrop(AbstractElement dropTarget, Object dropData) {
        return false;
    }

    @Override
    public boolean doDragDrop(AbstractElement parent, Object dropData) {
        return false;
    }

    @Override
    public void clear() {
    }

    class SchemaFolders {
        private DummyPlainTables plainTables;
        private DummyDynamicTables dynamicTables;

        private SchemaFolders(SchemaElement schema) {
            final String shemaId = schema.getId();
            plainTables = new DummyPlainTables(shemaId);
            dynamicTables = new DummyDynamicTables(shemaId);
        }
    }
}
