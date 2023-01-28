package org.whirlplatform.editor.client.tree.part;

import java.util.Arrays;
import java.util.Collections;
import org.whirlplatform.editor.client.meta.NewTableElement;
import org.whirlplatform.editor.client.tree.AppTree;
import org.whirlplatform.editor.client.tree.AppTreePresenter;
import org.whirlplatform.editor.client.tree.dummy.DummyTableClones;
import org.whirlplatform.meta.shared.editor.AbstractElement;
import org.whirlplatform.meta.shared.editor.RightType;
import org.whirlplatform.meta.shared.editor.db.PlainTableElement;
import org.whirlplatform.meta.shared.editor.db.TableColumnElement;

public class AppTreePlainTablePart extends AbstractAppTreePart<PlainTableElement> {

    private TableFolders folders;

    public AppTreePlainTablePart(AppTree appTree, AppTreePresenter treePresenter,
                                 PlainTableElement table) {
        super(appTree, treePresenter, table);
        folders = new TableFolders(table);
    }

    @Override
    public void init() {
        if (handledElement instanceof PlainTableElement) {
            addChildElement(handledElement, folders.clones);
            for (PlainTableElement t : handledElement.getClones()) {
                // view.addChildElement(folders.clones, t);
                doAddElementUI(handledElement, t);
            }
        }
    }

    @Override
    public boolean isRenaming(AbstractElement element) {
        // return !isReference()
        // && (element == table
        // || (element instanceof TableColumnElement && table
        // .getColumns().contains(element)) || (element instanceof
        // TableAbstractElement && table
        // .getClones().contains(element)));
        boolean result = false;
        if (!isReference()) {
            result = element == handledElement;
            if (!result && handledElement instanceof PlainTableElement) {
                result = element instanceof TableColumnElement
                        && handledElement.getColumns().contains(element)
                        || element instanceof PlainTableElement
                        && handledElement.getClones().contains(element);
            }
        }

        return result;
    }

    @Override
    public boolean isAdding(AbstractElement element) {
        return !isReference() && element == folders.clones
            && handledElement instanceof PlainTableElement;
    }

    @Override
    public boolean isCopying(AbstractElement element) {
        return element instanceof PlainTableElement;
    }

    @Override
    public boolean isPasting(AbstractElement element, AbstractElement copy) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isEditing(AbstractElement element) {
        return !isReference() && (element == handledElement
                || (element instanceof PlainTableElement
                && handledElement.getClones().contains(element)));
    }

    @Override
    public boolean isDeleting(AbstractElement element) {
        return !isReference() && (element == handledElement
                || (element instanceof PlainTableElement
                && handledElement.getClones().contains(element)));
    }

    @Override
    public boolean hasRights(AbstractElement element) {
        return element == handledElement
                || (element instanceof PlainTableElement
                && handledElement.getClones().contains(element));
    }

    @Override
    public boolean doAddElement(AbstractElement parent, AbstractElement element) {
        if (parent == folders.clones && handledElement instanceof PlainTableElement) {
            treePresenter.riseAddElement(handledElement, new NewTableElement());
            return true;
        }
        return false;
    }

    @Override
    public boolean doAddElementUI(AbstractElement parent, AbstractElement element) {
        if (parent == handledElement && element instanceof PlainTableElement
                && handledElement.getClones().contains(element)) {
            removeElement(element);
            addChildElement(folders.clones, element);
            // TableTreePartHandler tableHandler = new
            // TableTreePartHandler(view,
            // treeHandler, (TableElement) element);
            // tableHandler.setReference(isReference());
            // treeHandler.putHandler(element, tableHandler);
            return true;
        }
        return false;
    }

    @Override
    public boolean doRemoveElement(AbstractElement parent, AbstractElement element) {
        if (element instanceof PlainTableElement && handledElement.getClones().contains(element)) {
            treePresenter.riseRemoveElement(handledElement, element, true);
            return true;
        }
        return false;
    }

    @Override
    public boolean doRemoveElementUI(AbstractElement parent, AbstractElement element) {
        if (parent == handledElement && appTree.hasChild(folders.clones, element, false)) {
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
        if (element == handledElement || (element instanceof PlainTableElement
                && handledElement.getClones().contains(element))) {
            treePresenter.riseEditRights(Collections.singleton(element),
                    Collections.unmodifiableCollection(
                            Arrays.asList(RightType.ADD, RightType.DELETE, RightType.EDIT,
                                    RightType.VIEW, RightType.RESTRICT)));
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

    @Override
    public boolean isReference(AbstractElement element) {
        return isReference()
                && (handledElement == element || (element instanceof PlainTableElement
                && handledElement.getClones().contains(element)));
    }

    class TableFolders {
        private DummyTableClones clones;

        private TableFolders(PlainTableElement table) {
            clones = new DummyTableClones(table.getId());
        }
    }
}
