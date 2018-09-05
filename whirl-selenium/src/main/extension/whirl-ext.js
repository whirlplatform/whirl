

/**
 * 
 */
var getWrappedJsWindow = function(win) {
	// see https://developer.mozilla.org/en/wrappedJSObject
	// https://developer.mozilla.org/en/XPCNativeWrapper
	if (win.wrappedJSObject) {
		return win.wrappedJSObject;
	}
	return win;
}

var isWhirlLocator = function(locator) {
	return locator != null && locator.indexOf("whirl:") !== -1;
};

PageBot.prototype.orig_findElement = PageBot.prototype.findElement; 

PageBot.prototype.findElement = function(locator){
	if(isWhirlLocator(locator)){ //обрабатываем только локаторы с префиксом whirl://, не все.
		return getWrappedJsWindow(this.getCurrentWindow()).__Whirl.Selenium.getElementByLocator(locator);
	} else{
		if(orig_findElement){  //если какой-то обработчик назначен, то он будет вызван. Иначе findElement вернёт null. Т.к. нечем обрабатывать локатор.
			return orig_findElement(locator);
		} else{
			return null;
		}
	}
}


PageBot.prototype.locateElementByWhirl = function(locator, document) {
	return findElement(locator, document);
}

/**
 * is_fuzzy_match - необходим для того чтобы локатор работал для возвращаемого
 * пути который определяет не тот элемент который ему передали а псевдо-элемент
 * по которому должна идти логика работы компонента.
 */
PageBot.prototype.locateElementByWhirl.is_fuzzy_match = function(node, target) {
	return true;
}


//PageBot.prototype.findElement.is_fuzzy_match = function(node, target) {
//	return true;
//}

//Change SideeX method version to original one.
Selenium.prototype.doType = function(locator, value) {
    /**
     * Sets the value of an input field, as though you typed it in.
     *
     * <p>Can also be used to set the value of combo boxes, check boxes, etc. In these cases,
     * value should be the value of the option selected, not the visible text.</p>
     *
     * @param locator an <a href="#locators">element locator</a>
     * @param value the value to type
     */
    if (this.browserbot.controlKeyDown || this.browserbot.altKeyDown || this.browserbot.metaKeyDown) {
        throw new SeleniumError("type not supported immediately after call to controlKeyDown() or altKeyDown() or metaKeyDown()");
    }

    var element = this.browserbot.findElement(locator);

    //SuggestionDropDownExt, Chen-Chieh Ping, SELAB, CSIE, NCKU, 2016/11/10
    //core.events.setValue(element, value);
    core.events.setValue(element, ""); //очистить значение поля.
    bot.action.type(element, value); //сымитировать ввод пользователя. (но от этого не срабатывает onchange, которое необходимо GXT компоненту)
	bot.action.type(element, bot.Keyboard.Keys.ENTER); //от этого сработает событие onchange на текстовом поле. Что и требуется.
    //core.events.setValue(element, value);

};