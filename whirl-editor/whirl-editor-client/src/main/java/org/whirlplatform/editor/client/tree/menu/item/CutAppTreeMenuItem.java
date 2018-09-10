package org.whirlplatform.editor.client.tree.menu.item;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.widget.core.client.menu.Item;
import org.whirlplatform.editor.client.image.ComponentBundle;
import org.whirlplatform.editor.client.tree.AppTree;
import org.whirlplatform.editor.shared.i18n.EditorMessage;
import org.whirlplatform.meta.shared.editor.AbstractElement;

/**
 * Пункт меню дерева приложения - Вырезать
 */
public class CutAppTreeMenuItem extends AbstractAppTreeMenuItem<AppTree> {
	private final static String TITLE = EditorMessage.Util.MESSAGE.context_menu_cut();
	private final static ImageResource ICON = ComponentBundle.INSTANCE.cut();

	public CutAppTreeMenuItem() {
		super();
	}

	public CutAppTreeMenuItem(final AppTree tree) {
		super(tree);
	}

	@Override
	public void updateState() {
		if (getAppTree() != null) {
			AbstractElement element = getAppTree().getSelectedElement();
			this.setEnabled(getAppTree().isCopying(element) && getAppTree().isDeleting(element));
			//TODO Реализовать функцию
			setEnabled(false);
		} else {
			setEnabled(false);
		}

	}

	@Override
	protected SelectionHandler<Item> createSelectionHandler() {
		return new SelectionHandler<Item>() {
			@Override
			public void onSelection(SelectionEvent<Item> event) {
				// TODO Auto-generated method stub

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
