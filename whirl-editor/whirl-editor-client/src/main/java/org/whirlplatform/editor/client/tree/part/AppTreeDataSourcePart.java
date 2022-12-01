package org.whirlplatform.editor.client.tree.part;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import org.whirlplatform.editor.client.meta.NewSchemaElement;
import org.whirlplatform.editor.client.tree.AppTree;
import org.whirlplatform.editor.client.tree.AppTreePresenter;
import org.whirlplatform.editor.client.tree.dummy.DummySchemas;
import org.whirlplatform.meta.shared.editor.AbstractElement;
import org.whirlplatform.meta.shared.editor.RightType;
import org.whirlplatform.meta.shared.editor.db.AbstractTableElement;
import org.whirlplatform.meta.shared.editor.db.DataSourceElement;
import org.whirlplatform.meta.shared.editor.db.PlainTableElement;
import org.whirlplatform.meta.shared.editor.db.SchemaElement;

public class AppTreeDataSourcePart extends AbstractAppTreePart<DataSourceElement> {

    private DataSourceFolders folders;

    AppTreeDataSourcePart(AppTree appTree, AppTreePresenter treePresenter,
                          DataSourceElement datasource) {
        super(appTree, treePresenter, datasource);
        this.folders = new DataSourceFolders(datasource);
    }

    @Override
    public void init() {
        addChildElement(handledElement, folders.schemas);

        for (SchemaElement s : handledElement.getSchemas()) {
            doAddElementUI(handledElement, s);
        }
    }

    @Override
    public boolean isRenaming(AbstractElement element) {
        return element == handledElement;
    }

    @Override
    public boolean isAdding(AbstractElement element) {
        return element == folders.schemas;
    }

    @Override
    public boolean isCopying(AbstractElement element) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isPasting(AbstractElement element, AbstractElement copy) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isEditing(AbstractElement element) {
        return element == handledElement;
    }

    @Override
    public boolean isDeleting(AbstractElement element) {
        return element instanceof SchemaElement && handledElement.getSchemas().contains(element);
    }

    @Override
    public boolean hasRights(AbstractElement element) {
        return element == handledElement || element == folders.schemas;
    }

    @Override
    public boolean doAddElement(AbstractElement parent, AbstractElement element) {
        if (parent == folders.schemas) {
            treePresenter.riseAddElement(handledElement, new NewSchemaElement());
            return true;
        }
        return false;
    }

    @Override
    public boolean doAddElementUI(AbstractElement parent, AbstractElement element) {
        if (parent == handledElement && element instanceof SchemaElement &&
                handledElement.getSchemas().contains(element)) {
            removeElement(element);
            addChildElement(folders.schemas, element);
            putTreePart(element,
                    new AppTreeSchemaPart(appTree, treePresenter, (SchemaElement) element));
            return true;
        }
        return false;
    }

    @Override
    public boolean doRemoveElement(AbstractElement parent, AbstractElement element) {
        if (element instanceof SchemaElement && handledElement.getSchemas().contains(element)) {
            treePresenter.riseRemoveElement(handledElement, element, true);
            return true;
        }
        return false;
    }

    @Override
    public boolean doRemoveElementUI(AbstractElement parent, AbstractElement element) {
        if (parent == handledElement && appTree.hasChild(folders.schemas, element, false)) {
            removeElement(element);
            return true;
        }
        return false;
    }

    @Override
    public boolean doEditElement(AbstractElement element) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean doEditElementRights(AbstractElement element) {
        // TODO: Возможно потом потребуется разделить
        if (element == handledElement || element == folders.schemas) {
            Collection<AbstractTableElement> tables = new HashSet<AbstractTableElement>();
            for (SchemaElement s : handledElement.getSchemas()) {
                for (AbstractTableElement t : s.getTables()) {
                    tables.add(t);
                    if (t instanceof PlainTableElement) {
                        tables.addAll(((PlainTableElement) t).getClones());
                    }
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
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean doDragDrop(AbstractElement parent, Object dropData) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void clear() {
    }

    class DataSourceFolders {
        private DummySchemas schemas;

        private DataSourceFolders(DataSourceElement datasource) {
            schemas = new DummySchemas(datasource.getId());
        }
    }
}
