package org.whirlplatform.component.client.window;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.util.Util;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.Header;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.Containable;
import org.whirlplatform.component.client.selenium.Locator;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataValue;

import java.util.Map;

/**
 * Построитель окон
 */
public class WindowBuilder extends ComponentBuilder implements Containable {

	private ComponentBuilder topComponent;

	private Window window;

	private static final int WINDOW_MIN_WIDTH = 100;
	private static final int WINDOW_MIN_HEIGHT = 100;

	private boolean modal;

	public WindowBuilder(Map<String, DataValue> builderProperties) {
		super(builderProperties);
	}

	public WindowBuilder() {
		super();
	}

	/**
	 * Получить тип окна
	 * 
	 * @return ComponentType
	 */
	@Override
	public ComponentType getType() {
		return ComponentType.WindowType;
	}

	/**
	 * Создание компонента - окно
	 * 
	 * @return Component
	 */
	protected Component init(Map<String, DataValue> builderProperties) {
		window = new Window();
		window.setMaximizable(false);
		window.setMinimizable(false);
		window.setShadow(false);
		window.setButtonAlign(BoxLayoutPack.CENTER);
		window.setMinWidth(WINDOW_MIN_WIDTH);
		window.setMinHeight(WINDOW_MIN_HEIGHT);
		window.setOnEsc(false);
		window.setHeading(getTitle());

		WindowManager.get().add(this);
		return window;
	}

	/**
	 * Установка атрибута для окна
	 * 
	 * @param name
	 *            - String, название атрибута
	 * @param value
	 *            - String, значение атрибута
	 * @return boolean
	 */
	@Override
	public boolean setProperty(String name, DataValue value) {
		if (PropertyType.Closable.getCode().equalsIgnoreCase(name) && value != null) {
			setClosable(Boolean.TRUE.equals(value.getBoolean()));
			return true;
		} else if (name.equalsIgnoreCase(PropertyType.Minimizable.getCode()) && value != null) {
			setMinimizable(Boolean.TRUE.equals(value.getBoolean()));
			return true;
		} else if (name.equalsIgnoreCase(PropertyType.Maximizable.getCode()) && value != null) {
			setMaximizable(Boolean.TRUE.equals(value.getBoolean()));
			return true;
		} else if (name.equalsIgnoreCase(PropertyType.Resizable.getCode()) && value != null) {
			setResizable(Boolean.TRUE.equals(value.getBoolean()));
			return true;
		} else if (name.equalsIgnoreCase(PropertyType.Modal.getCode()) && value != null) {
			setModal(Boolean.TRUE.equals(value.getBoolean()));
			return true;
		} else if (name.equalsIgnoreCase(PropertyType.Title.getCode()) && value != null
				&& !Util.isEmptyString(value.getString())) {
			setTitle(value.getString());
		}
		return super.setProperty(name, value);
	}

	@Override
	public void setTitle(String title) {
		super.setTitle(title);
		window.setHeading(title);
	}

	public void setModal(boolean modal) {
		this.modal = modal;
		window.setModal(modal);
	}

	public boolean isModal() {
		return window.isModal();
	}

	public void setResizable(boolean resizable) {
		window.setResizable(resizable);
	}

	public boolean isResizable() {
		return window.isResizable();
	}

	public void setMinimizable(boolean minimizable) {
		window.setMinimizable(minimizable);
	}

	public boolean isMinimizable() {
		return window.isMinimizable();
	}

	public void setMaximizable(boolean maximizable) {
		window.setMaximizable(maximizable);
	}

	public boolean isMaximizable() {
		return window.isMaximizable();
	}

	public void setClosable(boolean closable) {
		window.setClosable(closable);
	}

	public boolean isClosable() {
		return window.isClosable();
	}

	/**
	 * Установить позицию окна в страннице
	 * 
	 * @param left
	 *            - int
	 * @param top
	 *            - int
	 */
	public void setPosition(int left, int top) {
		window.setPosition(left, top);
	}

	/**
	 * Установить позицию страницы
	 * 
	 * @param x
	 *            - int
	 * @param y
	 *            - int
	 */
	public void setPagePosition(int x, int y) {
		window.setPagePosition(x, y);
	}

	/**
	 * Добавление потомка окна
	 * 
	 * @param child
	 *            - ComponentBuilder
	 */
	@Override
	public void addChild(ComponentBuilder child) {
		window.setWidget(child.getComponent());
		topComponent = child;
		child.setParentBuilder(this);
	}

	/**
	 * Удаление потомка окна
	 * 
	 * @param child
	 *            - ComponentBuilder
	 */
	@Override
	public void removeChild(ComponentBuilder child) {
		if (window.remove(child.getComponent())) {
			topComponent = null;
			child.setParentBuilder(null);
		}
	}

	/**
	 * Очистка окна
	 */
	@Override
	public void clearContainer() {
		if (topComponent != null) {
			removeChild(topComponent);
		}
	}

	/**
	 * Перерисовка окна вывода
	 */
	@Override
	public void forceLayout() {
		Window win = (Window) componentInstance;
		win.forceLayout();
	}

	/**
	 * Получение сущности окна
	 * 
	 * @return (C) window
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected <C> C getRealComponent() {
		return (C) window;
	}

	/**
	 * Получение массива потомков у окна
	 * 
	 * @return ComponentBuilder[]
	 */
	@Override
	public ComponentBuilder[] getChildren() {
		if (topComponent != null) {
			ComponentBuilder[] result = { topComponent };
			return result;
		} else {
			return new ComponentBuilder[0];
		}
	}

	/**
	 * Получить количество потомков
	 * 
	 * @return int
	 */
	@Override
	public int getChildrenCount() {
		if (topComponent == null) {
			return 0;
		}
		return 1;
	}

	/**
	 * Скрыть окно
	 */
	public void hide() {
		window.hide();
	}

	/**
	 * Отобразить окно
	 */
	public void show() {
		// Нужно ли делать окно модальным
		WindowManager.get().add(this); // т.к. при вызове window.hide() билдер удаляется из списка, регистрирую его
										// заново, что необходимо для работы локаторов
		boolean parentModal = isParentModal();
		window.setModal(this.modal || parentModal);
		WindowManager.get().showWindow(window); // излишне выполняется операция syncTaskBars(). но это не критично.
	}

	/**
	 * Используется родитель активного окна (WindowManager.get().getActive())
	 */
	private boolean isParentModal() {
		boolean parentModal = false;
		Widget tmp = com.sencha.gxt.widget.core.client.WindowManager.get().getActive();
		if (tmp instanceof Window && ((Window) tmp).isModal()) {
			parentModal = true;
		}
		return parentModal;
	}

	/**
	 * Максимизировать окно
	 */
	public void maximize() {
		window.maximize();
	}

	/**
	 * Минимизировать окно
	 */
	public void minimize() {
		window.minimize();
	}

	public void center() {
		window.center();
	}

	@Override
	public void setParentBuilder(ComponentBuilder parentBuilder) {
		super.setParentBuilder(parentBuilder);
	}

	public static class LocatorParams {
		public static String TYPE_HEADER = "Header";

		public static String TYPE_TOOL_CLOSE = "Close";
		public static String TYPE_TOOL_MAXIMIZE = "Maximize";
		public static String TYPE_TOOL_MINIMIZE = "Minimize";
		public static String TYPE_TOOL_RESTORE = "Restore";
	}

	@Override
	public Locator getLocatorByElement(Element element) {
		String s = "WindowBuilder__getLocatorByElement(Element element)";
		if (s != null)
			s = "";

		Locator locator = super.getLocatorByElement(element);
		if (locator == null) {
			locator = new Locator(ComponentType.WindowType);
			fillLocatorDefaults(locator, element);
		}
		ToolBarHelper toolBarHelper = new ToolBarHelper(window.getHeader());
		Locator part = toolBarHelper.getPartByElement(element);
		if (part != null) {
			locator.setPart(part);
		}
		return locator;
	}

	@Override
	public Element getElementByLocator(Locator locator) {
		if (!fitsLocator(locator) || !locator.typeEquals(getType().getType())) {
			return null;
		}
		Locator part = locator.getPart();
		if (part == null) {
			return super.getElementByLocator(locator);
		}
		ToolBarHelper helper = new ToolBarHelper(window.getHeader());
		if (part.typeEquals(LocatorParams.TYPE_HEADER) && window.getHeader() != null) {
			Locator subPart = part.getPart();
			if (subPart != null && subPart.getType() != null) {
				if (LocatorParams.TYPE_TOOL_MINIMIZE.equals(subPart.getType())) {
					Widget w = helper.getMinimizeWidget();
					if (w != null) {
						return w.getElement();
					}
				}
				if (LocatorParams.TYPE_TOOL_MAXIMIZE.equals(subPart.getType())) {
					Widget w = helper.getMaximizeWidget();
					if (w != null) {
						return w.getElement();
					}
				}
				if (LocatorParams.TYPE_TOOL_RESTORE.equals(subPart.getType())) {
					Widget w = helper.getRestoreWidget();
					if (w != null) {
						return w.getElement();
					}
				}
				if (LocatorParams.TYPE_TOOL_CLOSE.equals(subPart.getType())) {
					Widget w = helper.getCloseWidget();
					if (w != null) {
						return w.getElement();
					}
				}
			}
			return window.getHeader().getElement(); // если toolbarButton не определена - возвращаю просто header окна
		}
		return null;
	}

	/**
	 * Помощь при работе с элементами Header-а окна.
	 */
	private class ToolBarHelper {
		private Header header;

		public ToolBarHelper(Header header) {
			this.header = header;
		}

		public Locator getPartByElement(Element element) {
			if (header != null) {
				boolean isHeaderElem = header.getElement().isOrHasChild(element);
				if (isHeaderElem) {
					Locator headerPart = new Locator(LocatorParams.TYPE_HEADER);
					for (Widget tool : header.getTools()) {
						if (tool instanceof ToolButton) {
							if (tool.getElement().isOrHasChild(element)) {
								if (tool.getElement().hasClassName(ToolButton.CLOSE.getStyle())) {
									headerPart.setPart(new Locator(LocatorParams.TYPE_TOOL_CLOSE));
								} else if (tool.getElement().hasClassName(ToolButton.RESTORE.getStyle())) {
									headerPart.setPart(new Locator(LocatorParams.TYPE_TOOL_RESTORE));
								} else if (tool.getElement().hasClassName(ToolButton.MAXIMIZE.getStyle())) {
									headerPart.setPart(new Locator(LocatorParams.TYPE_TOOL_MAXIMIZE));
								} else if (tool.getElement().hasClassName(ToolButton.MINIMIZE.getStyle())) {
									headerPart.setPart(new Locator(LocatorParams.TYPE_TOOL_MINIMIZE));
								}
								break;
							}
						}
					}
					return headerPart;
				}
			}
			return null;
		}

		public Widget getCloseWidget() {
			return findWidgetByClassName(ToolButton.CLOSE.getStyle());
		}

		public Widget getRestoreWidget() {
			return findWidgetByClassName(ToolButton.RESTORE.getStyle());
		}

		public Widget getMaximizeWidget() {
			return findWidgetByClassName(ToolButton.MAXIMIZE.getStyle());
		}

		public Widget getMinimizeWidget() {
			return findWidgetByClassName(ToolButton.MINIMIZE.getStyle());
		}

		private Widget findWidgetByClassName(String className) {
			for (Widget toolWidget : header.getTools()) {
				if (toolWidget instanceof ToolButton) {
					ToolButton tool = (ToolButton) toolWidget;
					Element toolElement = tool.getElement();
					if (toolElement.hasClassName(className)) {
						return toolWidget;
					}
				}
			}
			return null;
		}
	}

}