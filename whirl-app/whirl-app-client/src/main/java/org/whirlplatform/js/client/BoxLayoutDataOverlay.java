package org.whirlplatform.js.client;

import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportConstructor;
import org.timepedia.exporter.client.ExportOverlay;
import org.timepedia.exporter.client.ExportPackage;


/**
 * Экземпляры класса BoxLayoutData используются для определения размерностей и отступов компонентов  
 */
@Export("BoxLayoutData")
@ExportPackage("Whirl")
public class BoxLayoutDataOverlay implements ExportOverlay<BoxLayoutData> {

	@ExportConstructor
	public static BoxLayoutData constructor(int minSize, int maxSize) {
		return constructor(minSize, maxSize, 0, 0, 0, 0, 0);
	}

	
	/**
	 * 
	 * @param margins представляет значение для marginTop, marginRight, marginBottom, marginLeft 
	 */
	@ExportConstructor
	public static BoxLayoutData constructor(int minSize, int maxSize,
			double flex, int margins) {
		return constructor(minSize, maxSize, flex, margins, margins, margins,
				margins);
	}

	
	/**
	 * @param minSize минимальная ширина элемента
	 * @param maxSize максимальная ширина элемента
	 * @param flex коэффициент ширины компонента, вычисляемый пропорционально значений flex соседних компонентов. 
	 *           например, если flex компонента = 2 и сумма flex других соседних компонентов = 8, то компонент будет занимать 20% ширины контейнера.
	 *           или, flex1 = 1, flex2 = 2, flex3 = 3 - то компонент с flex = 1 будет занимать 1/6 ширины контейнера.  
	 * @param marginTop отступ внутренних компонентов от верхней границы контейнера
	 * @param marginRight отступ внутренних компонентов от правой границы контейнера
	 * @param marginBottom отступ внутренних компонентов от нижней границы контейнера
	 * @param marginLeft отступ внутренних компонентов от левой границы контейнера
	 */
	@ExportConstructor
	public static BoxLayoutData constructor(int minSize, int maxSize,
			double flex, int marginTop, int marginRight, int marginBottom,
			int marginLeft) {
		BoxLayoutData data = new BoxLayoutData(new Margins(marginTop,
				marginRight, marginBottom, marginLeft));
		data.setMinSize(minSize < 0 ? 0 : minSize);
		data.setMaxSize(maxSize < 0 ? Integer.MAX_VALUE : maxSize);
		data.setFlex(flex < 0 ? 0 : flex);
		return data;
	}
}
