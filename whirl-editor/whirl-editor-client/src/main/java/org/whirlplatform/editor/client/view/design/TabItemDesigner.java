package org.whirlplatform.editor.client.view.design;

import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.meta.shared.editor.ComponentElement;
import org.whirlplatform.meta.shared.editor.LocaleElement;

public class TabItemDesigner extends ComponentDesigner {

	public TabItemDesigner(LocaleElement defaultLocale, ComponentElement element) {
		super(defaultLocale, element);
	}
	
	@Override
	protected void onRootDrop(ComponentElement parentElement,
			ComponentBuilder parentBuilder, Object dropData, Object locationData) {
		super.onRootDrop(parentElement, parentBuilder, dropData, locationData);
		
	}

}
