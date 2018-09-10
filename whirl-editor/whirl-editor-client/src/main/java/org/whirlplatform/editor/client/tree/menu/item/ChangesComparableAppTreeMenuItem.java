package org.whirlplatform.editor.client.tree.menu.item;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.widget.core.client.menu.Item;
import org.whirlplatform.editor.client.image.ComponentBundle;
import org.whirlplatform.editor.client.presenter.compare.ElementChangeState;
import org.whirlplatform.editor.client.tree.ComparableAppTree;
import org.whirlplatform.editor.shared.i18n.EditorMessage;
import org.whirlplatform.meta.shared.editor.AbstractElement;

/**
 * Пункт меню сравниваемого дерева приложения - Изменения
 */
public class ChangesComparableAppTreeMenuItem extends AbstractAppTreeMenuItem<ComparableAppTree> {
	private final static String TITLE = EditorMessage.Util.MESSAGE.context_menu_changes();
	private final static ImageResource ICON = ComponentBundle.INSTANCE.compare();

	public ChangesComparableAppTreeMenuItem() {
		super();
	}

	public ChangesComparableAppTreeMenuItem(final ComparableAppTree tree) {
		super(tree);
	}

	@Override
	public void updateState() {
		boolean enabled = false;
		if (getAppTree() != null) {
			AbstractElement element = getAppTree().getSelectedElement();
			if (element != null) {
				enabled = (getAppTree().getChangeState(element) != ElementChangeState.NONE);
			}
		}
		setEnabled(enabled);
	}

	@Override
	protected SelectionHandler<Item> createSelectionHandler() {
		return new SelectionHandler<Item>() {
			@Override
			public void onSelection(SelectionEvent<Item> event) {
				AbstractElement element = getAppTree().getSelectedElement();
				if (element != null) {
					getAppTree().doOpenElement(element);
				}
			}
		};
	}

	@Override
	protected String getItemTitle() {
		return TITLE;
	}

	@Override
	protected ImageResource getItemIcon() {
		return ICON;
	}
}
