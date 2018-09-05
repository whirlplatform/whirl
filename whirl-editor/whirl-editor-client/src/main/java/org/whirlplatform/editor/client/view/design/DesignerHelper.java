package org.whirlplatform.editor.client.view.design;

import com.sencha.gxt.core.client.dom.XElement;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.base.ImageBuilder;
import org.whirlplatform.component.client.base.TimerBuilder;
import org.whirlplatform.component.client.hotkey.HotKeyBuilder;
import org.whirlplatform.editor.client.image.ComponentBundle;
import org.whirlplatform.editor.client.image.EditorBundle;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.editor.ComponentElement;

public class DesignerHelper {

	static void setConstantComponentProperties(ComponentElement element, ComponentBuilder builder) {
		if (ComponentType.ImageType.equals(element.getType())) {
			((ImageBuilder) builder).setDefaultImage(ComponentBundle.INSTANCE.simpleImage().getSafeUri().asString());
		} else if (ComponentType.TimerType.equals(element.getType())) {
			((TimerBuilder) builder).setIcon(EditorBundle.INSTANCE.timerSmall());
		} else if (ComponentType.HotKeyType.equals(element.getType())) {
			((HotKeyBuilder) builder).setIcon(EditorBundle.INSTANCE.hotKeySmall());
		}
	}

	static void setConstantComponentStyles(ComponentElement element, ComponentBuilder builder) {
		// Куда-то вынести из этого класса?
		// Окаймление пустых контейнеров и радио/чек группы
		if ((element.getType().isContainer() || ComponentType.RadioGroupType.equals(element.getType())
				|| ComponentType.CheckGroupType.equals(element.getType()))
				&& !ComponentType.TabPanelType.equals(element.getType())) {
			// TabPanel странно себя ведет при установке setWidth <= 0
			// (ставит ширину равной 1000000px)
			XElement childEl = builder.getComponent().getElement();
			if (element.getChildren() == null || element.getChildren().size() == 0) {
				builder.getComponent().setHeight(30);
				builder.getComponent().setWidth(-1);
			}
			childEl.applyStyles("border: 1px dotted red; position: relative;");
		} else if (ComponentType.ReportType.equals(element.getType())
				|| ComponentType.EditGridType.equals(element.getType())) {
			XElement childEl = builder.getComponent().getElement();
			builder.getComponent().setHeight(30);
			builder.getComponent().setWidth(-1);
			childEl.applyStyles("border: 1px dotted red; position: relative;");
		}
	}

}
