package org.whirlplatform.editor.client.tree;

import org.whirlplatform.meta.shared.editor.AbstractElement;

/**
 * Basic methods and propertries of the application tree components.
 */
public interface AppTreeBase {
    /*
     * Properties
     */
    boolean isRenaming(AbstractElement element);

    boolean isAdding(AbstractElement element);

    boolean isCopying(AbstractElement element);

    boolean isPasting(AbstractElement parent, AbstractElement element);

    boolean isEditing(AbstractElement element);

    boolean isDeleting(AbstractElement element);

    boolean isReference(AbstractElement element);

    boolean hasRights(AbstractElement element);

    boolean canDragDrop(AbstractElement dropTarget, Object dropData);

    /*
     * Methods
     */
    boolean doAddElement(AbstractElement parent, AbstractElement element);

    boolean doAddElementUI(AbstractElement parent, AbstractElement element);

    boolean doRemoveElement(AbstractElement parent, AbstractElement element);

    boolean doRemoveElementUI(AbstractElement parent, AbstractElement element);

    boolean doEditElement(AbstractElement element);

    boolean doEditElementRights(AbstractElement element);

    boolean doDragDrop(AbstractElement parent, Object dropData);
}
