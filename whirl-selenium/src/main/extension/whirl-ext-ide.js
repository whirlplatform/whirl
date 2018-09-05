/**
 * 
 */

var whirlFindLocator = function(element) {
	var win = getWrappedJsWindow(this.window);
	if (whirlHasContext(win)) {
		try {
			var locator = win.__Whirl.Selenium
					.getLocatorByElement(element);
			return locator;
		} catch (ex) {
			return null;
		}
	} else {
		return null;
	}
};

/**
 * Вобщем-то предполагается что этот плагин используется для нашего
 * приложения. Но если это будет не так, то такая проверка предотвратит ошибку.
 * 
 * @param autWindow
 * @returns
 */
var whirlHasContext = function(win) {
	return win != null && win.__Whirl != null;
};

var getWrappedJsWindow = function(win) {
	// see https://developer.mozilla.org/en/wrappedJSObject
	// https://developer.mozilla.org/en/XPCNativeWrapper
	if (win.wrappedJSObject) {
		return win.wrappedJSObject;
	}
	return win;
}

LocatorBuilders.add('whirl', whirlFindLocator);

LocatorBuilders.order = [ 'whirl', 'id', 'link', 'name', 'dom:name',
		'xpath:link', 'xpath:img', 'xpath:attributes', 'xpath:href',
		'dom:index', 'xpath:position' ];

/*Фикс логической ошибки selenium ide (Sideex@nckuselab.org: recorder-handlers.js -> поискать по тексту переменную preventType
 *она не сбрасывается при фокусе на textarea, а сбрасывается только при фокусе на input. 
 *Добавляю обработчик, который сбрасывает её безусловно при фокусе на любом элементе. */
Recorder.removeEventHandler('inputFocus'); //если плагин селениума будет не 0.8.0 версии, то синхронизировать этот код.
Recorder.addEventHandler('inputFocus', 'focus', function(event) {
    if (event.target.nodeName) {
        var tagName = event.target.nodeName.toLowerCase();
        var type = event.target.type;
        if (tagName == 'input' && Recorder.inputTypes.indexOf(type) >= 0) {
            focusTarget = event.target;
            focusValue = focusTarget.value;
            tempValue = focusValue;
            preventType = false;
        } else {
            focusTarget = null;
            focusValue = null;
            tempValue = null;
            preventType = false;
        }
    }
}, { capture: true });
