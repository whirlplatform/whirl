package org.whirlplatform.editor.client.tree.menu;

import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.event.ShowEvent;
import com.sencha.gxt.widget.core.client.event.ShowEvent.ShowHandler;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.SeparatorMenuItem;
import org.whirlplatform.editor.client.tree.AppTree;
import org.whirlplatform.editor.client.tree.menu.item.AppTreeMenuItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Базовая реализация Меню для дерева приложения.
 * 
 * Перед открытием атоматически обновляет состояние всех входящих в его состав
 * AppTreeMenuItem. Логика измененения состояния пунктов меню скрыта внутри их
 * реализации. Состояние пунктов меню не являющимися AppTreeMenuItem не
 * затрагивается. Добавление и удаление AppTreeMenuItem производится стандартным
 * способом через вызов add или remove.
 * 
 * @see AppTreeMenuItem
 * @author bedritckiy_mr
 */
public class AbstractAppTreeMenu<T extends AppTree> extends Menu implements AppTreeMenu<T> {
	private T appTree;
	private final List<AppTreeMenuItem<T>> appTreeMenuItems;

	public AbstractAppTreeMenu() {
		appTreeMenuItems = new ArrayList<>();
		addShowHandler(new ShowHandler() {
			@Override
			public void onShow(ShowEvent event) {
				updateMenuItemsState();
			}
		});
	}

	public AbstractAppTreeMenu(final T appTree) {
		this();
		this.appTree = appTree;
	}

	/**
	 * Устанавливает ссылку на дерево приложения как для самого меню, так и для
	 * всех его пунктов типа AppTreeMenuItem.
	 * 
	 * @param appTree
	 *            - Дерево приложения
	 * 
	 * @see AppTreeMenuItem
	 */
	@Override
	public void setAppTree(final T appTree) {
		this.appTree = appTree;
		for (AppTreeMenuItem<T> item : appTreeMenuItems) {
			item.setAppTree(appTree);
		}
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void add(Widget child) {
		if (child instanceof AppTreeMenuItem) {
			AppTreeMenuItem item = (AppTreeMenuItem) child;
			if (appTree != null) {
				item.setAppTree(appTree);
			}
			appTreeMenuItems.add(item);
		}
		super.add(child);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public boolean remove(Widget child) {
		if (child instanceof AppTreeMenuItem) {
			AppTreeMenuItem item = (AppTreeMenuItem) child;
            appTreeMenuItems.remove(item);
		}
		return super.remove(child);
	}

	/**
	 * Добавляет разделитель
	 */
	public void addSeparator() {
		add(new SeparatorMenuItem());
	}

	/**
	 * Обновляет состояние пунктов меню
	 */
	protected void updateMenuItemsState() {
		for (AppTreeMenuItem<T> item : appTreeMenuItems) {
			item.updateState();
		}
	}

	@Override
	public Menu asMenu() {
		return this;
	}
}
