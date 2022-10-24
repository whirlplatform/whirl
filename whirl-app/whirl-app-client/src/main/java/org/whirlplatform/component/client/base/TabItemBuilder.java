package org.whirlplatform.component.client.base;

import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.TabItemConfig;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import jsinterop.annotations.JsConstructor;
import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsOptional;
import jsinterop.annotations.JsType;
import org.whirlplatform.component.client.Closable;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.Containable;
import org.whirlplatform.component.client.TitleProvider;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.data.DataValue;

import java.util.Collections;
import java.util.Map;

/**
 * Вкладка
 */
@JsType(namespace = "Whirl", name = "TabItem")
public class TabItemBuilder extends ComponentBuilder implements Containable,
		Closable {

	private ComponentBuilder topComponent;

	private TabItemConfig itemConfig;

	private SimpleContainer container;

	@JsConstructor
	public TabItemBuilder(@JsOptional Map<String, DataValue> builderProperties) {
		super(builderProperties);
	}

	@JsIgnore
	public TabItemBuilder() {
		this(Collections.emptyMap());
	}

	@JsIgnore
	@Override
	public ComponentType getType() {
		return ComponentType.TabItemType;
	}

	protected Component init(Map<String, DataValue> builderProperties) {
		itemConfig = new TabItemConfig("");
		container = new SimpleContainer();
		return container;
	}

	/**
	 * Делает данную вкладку активной в панели.
	 */
	public void activate() {
		TabPanelBuilder tabPanelBuilder = (TabPanelBuilder) parentBuilder;
		TabPanel panel = (TabPanel) tabPanelBuilder.getComponent();
		panel.setActiveWidget(componentInstance);
	}

	/**
	 * Устанавливает для элемента разрешение быть закрываемым
	 * (по умолчанию false).
	 *
	 * @param closable - boolean, true для закрываемого
	 */
	public void setClosable(boolean closable) {
		itemConfig.setClosable(closable);
	}

	/**
	 * Возвращает состояние закрытия элемента.
	 *
	 * @return true, если закрыт
	 */
	@Override
	public boolean isClosable() {
		return itemConfig.isClosable();
	}

	/**
	 * Закрывает элемент.
	 */
	@Override
	public void close() {
		TabPanelBuilder panel = (TabPanelBuilder) parentBuilder;
		panel.removeChild(this);
	}

	/**
	 * Устанавливает заголовок.
	 *
	 * @param title - String, заголовок
	 */
	@Override
	public void setTitle(String title) {
		itemConfig.setHTML(title == null ? "" : title);
	}

	/**
	 * Возвращает заголовок.
	 *
	 * @return String - содержимое заголовка в формате html
	 */
	@Override
	public String getTitle() {
		return itemConfig.getHTML();
	}

	protected TabItemConfig getTabItemConfig() {
		return itemConfig;
	}
	
	@Override
	public void addChild(ComponentBuilder child) {
		container.setWidget(child.getComponent());
		if (getTitle() == null || getTitle().isEmpty()
				&& child instanceof TitleProvider) {
            setTitle(child.getTitle());
		}
		topComponent = child;
		child.setParentBuilder(this);
	}

	@Override
	public void removeChild(ComponentBuilder child) {
		if (container.remove(child.getComponent())) {
			topComponent = null;
			child.setParentBuilder(null);
		}
	}

	/**
	 * Очищает контейнер.
	 */
	@Override
	public void clearContainer() {
		if (topComponent != null) {
			removeChild(topComponent);
		}
	}

	/**
	 * Пересчитывает расположение компонентов в данном контейнере.
	 */
	@Override
	public void forceLayout() {
		container.forceLayout();
	}

	/**
	 * Обновляет элемент.
	 */
	public void refresh() {
		if (parentBuilder != null && parentBuilder instanceof TabPanelBuilder) {
			TabPanelBuilder panel = (TabPanelBuilder) parentBuilder;
			panel.update(this);
		}
	}

	@Override
	protected <C> C getRealComponent() {
		return (C) container;
	}

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
	 * Проверяет активность компонента.
	 *
	 * @return true, компонент активен
	 */
	public boolean isActive() {
        return parentBuilder != null
                && ((TabPanelBuilder) parentBuilder).getActive() == this;
    }

	@Override
	public int getChildrenCount() {
		if (topComponent == null) {
			return 0;
		}
		return 1;
	}

	/**
	 * Возвращает код компонента.
	 *
	 * @return код компонента
	 */
	@Override
	public String getCode() {
		return super.getCode();
	}

	/**
	 * Проверяет, находится ли компонент в скрытом состоянии.
	 *
	 * @return true, если компонент скрыт
	 */
	public boolean isHidden() {
		return super.isHidden();
	}

	/**
	 * Устанавливает скрытое состояние компонента.
	 *
	 * @param hidden true - для скрытия компонента, false - для отображения компонента
	 */
	public void setHidden(boolean hidden) {
		super.setHidden(hidden);
	}

	/**
	 * Устанавливает фокус на компоненте.
	 */
	@Override
	public void focus() {
		super.focus();
	}

	 /**
	 * Проверяет, включен ли компонент.
	 *
	 * @return true если компонент включен
	 */
	@Override
	public boolean isEnabled() {
		return super.isEnabled();
	}

	/**
	 * Устанавливает включенное состояние компонента.
	 *
	 * @param enabled true - для включения компонента,
	 *                false - для отключения компонента
	 */
	@Override
	public void setEnabled(boolean enabled) {
	super.setEnabled(enabled);
	}

}
