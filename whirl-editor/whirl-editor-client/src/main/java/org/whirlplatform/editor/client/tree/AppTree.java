package org.whirlplatform.editor.client.tree;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.tree.Tree.TreeNode;
import org.whirlplatform.editor.client.tree.part.AppTreePart;
import org.whirlplatform.editor.shared.TreeState;
import org.whirlplatform.meta.shared.Version;
import org.whirlplatform.meta.shared.editor.AbstractElement;
import org.whirlplatform.meta.shared.editor.ApplicationElement;

/**
 *
 */
public interface AppTree extends AppTreeBase {
	/*
	 * Additional element properties
	 */
	boolean isPasting(AbstractElement parent);

	boolean hasChild(AbstractElement parent, AbstractElement child, boolean deep);

	/*
	 * Element selectors
	 */
	AbstractElement getSelectedElement();

	TreeNode<AbstractElement> getSelectedNode();

	void setExpanded(AbstractElement element, boolean expanded);

	void selectElement(AbstractElement element);

	/*
	 * Additional element methods
	 */
	boolean doCopyElement(AbstractElement element);

	boolean doPasteElement(AbstractElement parent);

	void doOpenElement(AbstractElement element);

	boolean renameSelectedElement();

	void refresh(AbstractElement element);

	/*
	 * Tree parts management
	 */
	void putTreePart(AbstractElement element, AppTreePart<? extends AbstractElement> treePart);

	/*
	 * Tree state
	 */
	void setState(TreeState state);

	TreeState getState();

	/*
	 * Application management
	 */
	void loadApplication(ApplicationElement application, Version version, AppTreePresenter handler);

	ApplicationElement getApplication();

	Version getVersion();

	void setContextMenu(Menu contextMenu);

	/*
	 * Other
	 */
	Widget asWidget();

	void setCell(Cell<String> cell);

	TreeStore<AbstractElement> getStore();

	void clearAll();
}
