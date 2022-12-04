package org.whirlplatform.editor.client.tree.part;

import org.whirlplatform.editor.client.tree.AppTree;
import org.whirlplatform.editor.client.tree.AppTreePresenter;
import org.whirlplatform.meta.shared.editor.AbstractElement;

/**
 * Base class for all application tree parts
 */
public abstract class AbstractAppTreePart<T extends AbstractElement> implements AppTreePart<T> {
    protected AppTree appTree;
    protected AppTreePresenter treePresenter;
    protected T handledElement;
    private boolean reference;

    public AbstractAppTreePart(final AppTree view, final AppTreePresenter treePresenter,
                               final T handlerElement) {
        reference = false;
        this.appTree = view;
        this.treePresenter = treePresenter;
        this.handledElement = handlerElement;
    }

    @Override
    public boolean isReference(AbstractElement element) {
        return reference;
    }

    @Override
    public T getHandledElement() {
        return handledElement;
    }

    public void clear() {
        appTree = null;
        treePresenter = null;
        handledElement = null;
    }

    protected boolean isReference() {
        return reference;
    }

    protected void setReference(boolean reference) {
        this.reference = reference;
    }

    protected void addRootElement(AbstractElement element) {
        appTree.getStore().add(element);
    }

    protected void addChildElement(AbstractElement parent, AbstractElement child) {
        if (appTree.getStore().findModel(child) != null) {
            appTree.getStore().remove(child);
        }
        appTree.getStore().add(parent, child);
    }

    protected void removeElement(AbstractElement element) {
        appTree.getStore().remove(element);
    }

    protected void putTreePart(AbstractElement element,
                               AppTreePart<? extends AbstractElement> treePart) {
        appTree.putTreePart(element, treePart);
    }
}
