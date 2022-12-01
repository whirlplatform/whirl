package org.whirlplatform.editor.client.tree.part;

import java.util.Arrays;
import java.util.Collections;
import org.whirlplatform.editor.client.tree.AppTree;
import org.whirlplatform.editor.client.tree.AppTreePresenter;
import org.whirlplatform.meta.shared.editor.AbstractElement;
import org.whirlplatform.meta.shared.editor.RightType;
import org.whirlplatform.meta.shared.editor.db.DynamicTableElement;

public class AppTreeDynamicTablePart extends AbstractAppTreePart<DynamicTableElement>
        implements AppTreePart<DynamicTableElement> {

    public AppTreeDynamicTablePart(AppTree appTree, AppTreePresenter treePresenter,
                                   DynamicTableElement table) {
        super(appTree, treePresenter, table);
    }

    @Override
    public void init() {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isRenaming(AbstractElement element) {
        return !isReference() && handledElement == element;
    }

    @Override
    public boolean isAdding(AbstractElement element) {
        // TODO Auto-generated method stub
        return false;
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
        return !isReference() && handledElement == element;
    }

    @Override
    public boolean isDeleting(AbstractElement element) {
        return !isReference() && handledElement == element;
    }

    @Override
    public boolean hasRights(AbstractElement element) {
        return handledElement == element;
    }

    @Override
    public boolean doAddElement(AbstractElement parent, AbstractElement element) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean doAddElementUI(AbstractElement parent, AbstractElement element) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean doRemoveElement(AbstractElement parent, AbstractElement element) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean doRemoveElementUI(AbstractElement parent, AbstractElement element) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean doEditElement(AbstractElement element) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean doEditElementRights(AbstractElement element) {
        if (element == handledElement) {
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
        return isReference() && handledElement == element;
    }
}
