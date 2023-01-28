package org.whirlplatform.editor.client.tree.part;

import org.whirlplatform.editor.client.tree.AppTree;
import org.whirlplatform.editor.client.tree.AppTreePresenter;
import org.whirlplatform.meta.shared.editor.AbstractElement;
import org.whirlplatform.meta.shared.editor.ApplicationElement;
import org.whirlplatform.meta.shared.editor.db.AbstractTableElement;
import org.whirlplatform.meta.shared.editor.db.DynamicTableElement;
import org.whirlplatform.meta.shared.editor.db.PlainTableElement;

public class AppTreeReferencePart extends AbstractAppTreePart<ApplicationElement> {

    AppTreeReferencePart(AppTree appTree, AppTreePresenter treePresenter,
                         ApplicationElement application) {
        super(appTree, treePresenter, application);
        setReference(true);
    }

    @Override
    public void init() {
        if (handledElement.isAvailable()) {
            for (AbstractTableElement t : handledElement.getApplicationTables()) {
                doAddElementUI(handledElement, t);
            }
        }
    }

    @Override
    public boolean isRenaming(AbstractElement element) {
        return false;
    }

    @Override
    public boolean isAdding(AbstractElement element) {
        return false;
    }

    @Override
    public boolean isCopying(AbstractElement element) {
        return false;
    }

    @Override
    public boolean isPasting(AbstractElement element, AbstractElement copy) {
        return false;
    }

    @Override
    public boolean isEditing(AbstractElement element) {
        return false;
    }

    @Override
    public boolean isDeleting(AbstractElement element) {
        return element == handledElement;
    }

    @Override
    public boolean hasRights(AbstractElement element) {
        return false;
    }

    @Override
    public boolean doAddElement(AbstractElement parent, AbstractElement element) {
        return false;
    }

    @Override
    public boolean doAddElementUI(AbstractElement parent, AbstractElement element) {
        if (parent == handledElement && element instanceof PlainTableElement
                && handledElement.getApplicationTables().contains(element)) {
            removeElement(element);
            addChildElement(handledElement, element);
            AppTreePlainTablePart tableHandler = new AppTreePlainTablePart(appTree, treePresenter,
                    (PlainTableElement) element);
            tableHandler.setReference(true);
            putTreePart(element, tableHandler);
            return true;
        } else if (parent == handledElement && element instanceof DynamicTableElement
                && handledElement.getApplicationTables().contains(element)) {
            removeElement(element);
            addChildElement(handledElement, element);
            AppTreeDynamicTablePart dynamicTableHandler =
                    new AppTreeDynamicTablePart(appTree, treePresenter,
                            (DynamicTableElement) element);
            dynamicTableHandler.setReference(true);
            putTreePart(element, dynamicTableHandler);
            return true;
        }
        return false;
    }

    @Override
    public boolean doRemoveElement(AbstractElement parent, AbstractElement element) {
        return false;
    }

    @Override
    public boolean doRemoveElementUI(AbstractElement parent, AbstractElement element) {
        return false;
    }

    @Override
    public boolean doEditElement(AbstractElement element) {
        return false;
    }

    @Override
    public boolean doEditElementRights(AbstractElement element) {
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
    public ApplicationElement getHandledElement() {
        return null;
    }

    @Override
    public void clear() {
    }

    @Override
    public boolean isReference(AbstractElement element) {
        return handledElement == element;
    }
}
