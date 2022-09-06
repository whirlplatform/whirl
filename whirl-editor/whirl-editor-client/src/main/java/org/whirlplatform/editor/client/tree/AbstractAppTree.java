package org.whirlplatform.editor.client.tree;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.tree.Tree;
import com.sencha.gxt.widget.core.client.tree.TreeSelectionModel;
import org.whirlplatform.editor.client.tree.dummy.AbstractDummyElement;
import org.whirlplatform.editor.client.tree.dummy.ChildrenlessDummy;
import org.whirlplatform.editor.client.tree.part.AppTreeApplicationPart;
import org.whirlplatform.editor.client.tree.part.AppTreePart;
import org.whirlplatform.editor.client.util.ApplicationTreeComparator;
import org.whirlplatform.editor.client.util.ElementKeyProvider;
import org.whirlplatform.editor.shared.TreeState;
import org.whirlplatform.editor.shared.i18n.EditorMessage;
import org.whirlplatform.editor.shared.visitor.CloneVisitor;
import org.whirlplatform.meta.shared.Version;
import org.whirlplatform.meta.shared.editor.*;
import org.whirlplatform.meta.shared.editor.db.PlainTableElement;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Base class for the application tree implementations
 *
 */
public abstract class AbstractAppTree extends Tree<AbstractElement, String> implements AppTree {
	public static final String COPY = EditorMessage.Util.MESSAGE.copy();

	private Map<AbstractElement, AppTreePart<? extends AbstractElement>> partHandlers;
	private AbstractElement copy;
	private ApplicationElement application;
	private AppTreeApplicationPart applicationPart;
	private Version version;

	public AbstractAppTree() {
		super(createElementStore(), createElementValueProvider());
		this.setIconProvider(new AppTreeIconProvider());
		this.getElement().getStyle().setBackgroundColor("#FFFFFF");
		this.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		partHandlers = new ConcurrentHashMap<>();
	}

	public abstract void doOpenElement(AbstractElement element);

	@Override
	public void loadApplication(final ApplicationElement app, final Version version, final AppTreePresenter presenter) {
		if (app != null && presenter != null) {
			clearAll();
			this.version = version;
			this.application = app;
			this.applicationPart = new AppTreeApplicationPart(this, presenter, app);
			putTreePart(app, applicationPart);
			this.setExpanded(getApplication(), true, false);
		}
	}

	@Override
	public Version getVersion() {
		return version;
	}

	@Override
	public void clearAll() {
		getStore().clear();
		partHandlers.clear();
		copy = null;
		application = null;
		applicationPart = null;
		version = null;
	}

	@Override
	public ApplicationElement getApplication() {
		return application;
	}

	@Override
	public AbstractElement getSelectedElement() {
		return getSelectionModel().getSelectedItem();
	}

	@Override
	public TreeNode<AbstractElement> getSelectedNode() {
		return findNode(getSelectionModel().getSelectedItem());
	}

	@Override
	public boolean doAddElement(AbstractElement parent, AbstractElement element) {
		for (AppTreePart<? extends AbstractElement> h : partHandlers.values()) {
			if (h.doAddElement(parent, element)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean doAddElementUI(AbstractElement parent, AbstractElement element) {
		for (AppTreePart<? extends AbstractElement> h : partHandlers.values()) {
			if (h.doAddElementUI(parent, element)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean doCopyElement(AbstractElement element) {
		boolean result;
		if (element instanceof FormElement || element instanceof ReportElement || element instanceof ComponentElement
				|| element instanceof PlainTableElement || element instanceof EventElement
				|| element instanceof EventElement) {
			CloneVisitor<AbstractElement> cloner = new CloneVisitor<>(element, false, true);
			copy = cloner.copy();
			copy.setName(createNameForCopy(copy));
			result = true;
		} else {
			copy = null;
			result = false;
		}
		return result;
	}

	private String createNameForCopy(final AbstractElement element) {
		return element.getName() + " " + COPY;
	}

	@Override
	public boolean doPasteElement(AbstractElement parent) {
		boolean result = doPasteElement(parent, copy);
		copy = null;
		return result;
	}

	private boolean doPasteElement(AbstractElement parent, AbstractElement element) {
		boolean result = false;
		if (element != null) {
			for (AppTreePart<? extends AbstractElement> h : partHandlers.values()) {
				if (h.doAddElement(parent, element)) {
					result = true;
					break;
				}
			}
		}
		return result;
	}

	@Override
	public boolean doRemoveElement(AbstractElement parent, AbstractElement element) {
		for (AppTreePart<? extends AbstractElement> h : partHandlers.values()) {
			if (h.doRemoveElement(parent, element)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean doRemoveElementUI(AbstractElement parent, AbstractElement element) {
		boolean removed = false;
		List<AbstractElement> children = getStore().getAllChildren(element);
		for (AppTreePart<? extends AbstractElement> h : partHandlers.values()) {
			if (h.doRemoveElementUI(parent, element)) {
				removed = true;
				break;
			}
		}
		if (removed) {
			for (AbstractElement child : children) {
				partHandlers.remove(child);
			}
			partHandlers.remove(element);
		}
		return removed;
	}

	@Override
	public boolean doEditElementRights(AbstractElement element) {
		for (AppTreePart<? extends AbstractElement> h : partHandlers.values()) {
			if (h.doEditElementRights(element)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean doEditElement(AbstractElement element) {
		for (AppTreePart<? extends AbstractElement> h : partHandlers.values()) {
			if (h.doEditElement(element)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean doDragDrop(AbstractElement dropTarget, Object dropData) {
		for (AppTreePart<? extends AbstractElement> h : partHandlers.values()) {
			if (h.doDragDrop(dropTarget, dropData)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean hasChild(AbstractElement parent, AbstractElement child, boolean deep) {
		if (deep) {
			return getStore().getAllChildren(parent).contains(child);
		} else {
			return getStore().getChildren(parent).contains(child);
		}
	}

	@Override
	public void selectElement(AbstractElement element) {
		TreeSelectionModel<AbstractElement> sm = getSelectionModel();
		sm.deselectAll();
		sm.select(element, true);
	}

	@Override
	public TreeState getState() {
		TreeState state = new TreeState();
		if (!(getSelectionModel().getSelectedItem() instanceof AbstractDummyElement)) {
			state.setSelected(getSelectionModel().getSelectedItem());
		}
		for (AbstractElement element : getStore().getAll()) {
			if (!(element instanceof AbstractDummyElement) && isExpanded(element)) {
				state.addExpanded(element);
			}
		}
		return state;
	}

	@Override
	public void setState(TreeState state) {
		for (AbstractElement element : state.getExpanded()) {
			setExpanded(element, true);
		}
		getSelectionModel().select(state.getSelected(), false);
	}

	@Override
	public void putTreePart(AbstractElement element, AppTreePart<? extends AbstractElement> treePart) {
		AppTreePart<? extends AbstractElement> elementHandler = partHandlers.get(element);
		if (elementHandler != null) {
			elementHandler.clear();
		}
		partHandlers.put(element, treePart);
		treePart.init();
	}

	/*
	 * element properties
	 */
	@Override
	public boolean canDragDrop(AbstractElement dropTarget, Object dropData) {
		for (AppTreePart<? extends AbstractElement> h : partHandlers.values()) {
			if (h.canDragDrop(dropTarget, dropData)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isReference(AbstractElement element) {
		for (AppTreePart<? extends AbstractElement> h : partHandlers.values()) {
			if (h.isReference(element)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isRenaming(AbstractElement element) {
		if (element != null) {
			for (AppTreePart<? extends AbstractElement> h : partHandlers.values()) {
				if (h.isRenaming(element)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean isAdding(AbstractElement element) {
		if (element != null) {
			for (AppTreePart<? extends AbstractElement> h : partHandlers.values()) {
				if (h.isAdding(element)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean isCopying(AbstractElement element) {
		if (element != null) {
			for (AppTreePart<? extends AbstractElement> h : partHandlers.values()) {
				if (h.isCopying(element)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean isPasting(AbstractElement parent) {
		return this.isPasting(parent, copy);
	}

	@Override
	public boolean isPasting(AbstractElement parent, AbstractElement element) {
		if (element != null) {
			for (AppTreePart<? extends AbstractElement> h : partHandlers.values()) {
				if (h.isPasting(parent, element)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean isEditing(AbstractElement element) {
		for (AppTreePart<? extends AbstractElement> h : partHandlers.values()) {
			if (h.isEditing(element)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isDeleting(AbstractElement element) {
		for (AppTreePart<? extends AbstractElement> h : partHandlers.values()) {
			if (h.isDeleting(element)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean hasRights(AbstractElement element) {
		for (AppTreePart<? extends AbstractElement> h : partHandlers.values()) {
			if (h.hasRights(element)) {
				return true;
			}
		}
		return false;
	}

	// Для исправления автоматического скролла при нажатии:
	// https://code.google.com/p/google-web-toolkit/issues/detail?id=1467
	@Override
	public void onBrowserEvent(Event event) {
		if (DOM.eventGetType(event) == Event.ONMOUSEDOWN) {
			Element e = getElement();
			int s = e.getScrollTop();
			super.onBrowserEvent(event);
			if (e.getScrollTop() != s) {
				e.setScrollTop(s);
			}
			return;
		}
		super.onBrowserEvent(event);
	}

	@Override
	protected void onDoubleClick(Event event) {
		doOpenElement(getSelectedElement());
    }

    // for constructor
	private static TreeStore<AbstractElement> createElementStore() {
		TreeStore<AbstractElement> store = new TreeStore<AbstractElement>(new ElementKeyProvider<AbstractElement>()) {
			// Чтобы пустые контейнеры и свободные компоненты отображались как
			// контейнеры без элементов
			@Override
			public boolean hasChildren(AbstractElement item) {
				if (item instanceof ComponentElement && ((ComponentElement) item).getType().isContainer()) {
					return true;
				} else if (item instanceof ChildrenlessDummy) {
					return false;
				} else if (item instanceof AbstractDummyElement) {
					return true;
				}
				return super.hasChildren(item);
			}
		};
		store.addSortInfo(new StoreSortInfo<AbstractElement>(new ApplicationTreeComparator(), SortDir.ASC));
		return store;
	}

	// for constructor
	private static ValueProvider<AbstractElement, String> createElementValueProvider() {
		return new ValueProvider<AbstractElement, String>() {
			@Override
			public String getValue(AbstractElement element) {
				return element.getName();
			}

			@Override
			public void setValue(AbstractElement element, String value) {
				element.setName(value);
			}

			@Override
			public String getPath() {
				return "name";
			}
		};
	}
}
