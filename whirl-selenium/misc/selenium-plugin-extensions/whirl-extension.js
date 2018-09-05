
// Warn if we're being loaded twice as things will be very likely to not work in this case
if (Selenium.prototype.whirl_userExtensions_loaded) {
    LOG.warn("whirl-extensions.js is being loaded more than once - " +
        "check for this file being included multiple times in the Selenium core extensions.");
}

Selenium.prototype.whirl_userExtensions_loaded = true;

PageBot.prototype.getAutWindow = function() {
    var autWindow = this.browserbot.getUserWindow();
    // if the user window is the dev console, redirect to the actual app window
    if (autWindow.targetWindow != null) autWindow = autWindow.targetWindow;
    // if SmartClient isn't loaded on the target window, just bail.
    return autWindow;
};


Selenium.prototype.getAutWindow = PageBot.prototype.getAutWindow;

var WhirlHelper = {
	trackProperties: function(obj){
		for(var key in obj){
			var val = obj[key];
			  LOG.info(key + ' -> ' + val);
		   }
	},
	clickFn: function(context, autWindow, locator){
		element = autWindow.__Whirl.Selenium.getElementByLocator(locator);
		LOG.info("click on: " + element+", locator: " + locator);
	    context.browserbot.triggerMouseEvent(element, "mouseover", true, 1, 1);
	    context.browserbot.triggerMouseEvent(element, "mousedown", true, 1, 1);
	    context.browserbot.triggerMouseEvent(element, "mouseup",   true, 1, 1);
	    context.browserbot.clickElement(element);
	},
	doubleClickFn: function(context, autWindow, locator){
		element = autWindow.__Whirl.Selenium.getElementByLocator(locator);
		LOG.info("double click on: " + element+", locator: " + locator);
		
		var event = new MouseEvent('dblclick', {
		    'view': autWindow,
		    'bubbles': true,
		    'cancelable': true
		  });
		element.dispatchEvent(event);
		
//	    context.browserbot.triggerMouseEvent(element, "mouseover", true, 2, 2);
//	    context.browserbot.triggerMouseEvent(element, "mousedown", true, 2, 2);
//	    context.browserbot.triggerMouseEvent(element, "mouseup",   true, 2, 2);
//	    WhirlHelper.wait(200);
//	    context.browserbot.triggerMouseEvent(element, "mousedown", true, 2, 2);
//	    context.browserbot.triggerMouseEvent(element, "mouseup",   true, 2, 2);
	},
	wait: function(ms){
	   var start = new Date().getTime();
	   var end = start;
	   while(end < start + ms) {
	      end = new Date().getTime();
	   }
	}
}


/*
_whirlAjaxInAction = false;
_whirlAjaxSend = XMLHttpRequest.prototype.send;
XMLHttpRequest.prototype.send = function() {

     Wrap onreadystaechange callback 
    var callback = this.onreadystatechange;
    this.onreadystatechange = function() {             
         if (this.readyState == 4) {

              We are in response; do something, like logging or anything you want 
        	 _whirlAjaxInAction = true;
        	 
         } else{
        	_whirlAjaxInAction = false; 
         }
         callback.apply(this, arguments);
    }
    _whirlAjaxSend.apply(this, arguments);
}
*/


//Selenium.prototype.doWaitForElementClickable = function(locator, eventParams){
//	if(_whirlAjaxInAction){
//		this.doPause(10000); //ждём 10 секунд пока ajax прекратится.. 
//	} else{
//	    if(this.isWhirlLocator(locator)) {
//	    	var autWindow = this.getAutWindow();
//	    	var builder = autWindow.__Whirl.Selenium.getBuilderByLocator(locator);
//	    	if(builder instanceof autWindow.Whirl.EditGridBuilder){
//	    		if(autWindow.__Whirl.Selenium.gridIsLoading(builder)){
//	    			this.doPause(7000);
//	    		}
//	    	} else {
//	        	var element = autWindow.__Whirl.Selenium.getElementByLocator(locator);
//	        	var context = this;
//	        	if(element == null){
//	        		this.doPause(5000);
//	        	} 
//	    	}
//	    } 
//	}
//}

Selenium.prototype.orig_doClick = Selenium.prototype.doClick;

Selenium.prototype.doClick = function(locator, eventParams){
	
    if(this.isWhirlLocator(locator)) {
    	var autWindow = this.getAutWindow();
    	var element = autWindow.__Whirl.Selenium.getElementByLocator(locator);
    	
    	var context = this;
    	if(element != null){
    		WhirlHelper.clickFn(context, autWindow, locator); 
    	} else{
    		LOG.error("Element not found for locator " + locator);
    	}

    } else {
        this.orig_doClick(locator, eventParams);
    }
};


Selenium.prototype.doDoubleClick = function(locator, eventParams){

    if(this.isWhirlLocator(locator)) {
    	var autWindow = this.getAutWindow();
    	var element = autWindow.__Whirl.Selenium.getElementByLocator(locator);
    	var context = this;
    	if(element != null){
    		WhirlHelper.doubleClickFn(context, autWindow, locator); 
    	} else{
    		LOG.error("Element not found for locator " + locator);
    	}

    } else {
        this.orig_doClick(locator, eventParams);
    }
};
//
//function __triggerKeyboardEvent(window, el, keyCode)
//{
//    var eventObj = window.document.createEventObject ?
//    		window.document.createEventObject() : window.document.createEvent("Events");
//  
//    if(eventObj.initEvent){
//      eventObj.initEvent("keydown", true, true);
//    }
//  
//    eventObj.keyCode = keyCode;
//    eventObj.which = keyCode;
//    
//    el.dispatchEvent ? el.dispatchEvent(eventObj) : el.fireEvent("onkeydown", eventObj); 
//} 


Selenium.prototype.doHotkey = function(target,value) {

	var win = this.browserbot.getCurrentWindow(); // this way you get your site
	var doc = win.document;
	var keyboardEvent = doc.createEvent("KeyboardEvent");
	var initMethod = typeof keyboardEvent.initKeyboardEvent !== 'undefined' ? "initKeyboardEvent" : "initKeyEvent";

	keyboardEvent[initMethod](
	               "keypress", // event type : keydown, keyup, keypress
	                false, // bubbles
	                false, // cancelable
	                win, // viewArg: should be window
	                false, // ctrlKeyArg
	                false, // altKeyArg
	                false, // shiftKeyArg
	                false, // metaKeyArg
	                value, // keyCodeArg : unsigned long the virtual key code, else 0
	                value // charCodeArgs : unsigned long the Unicode character associated with the depressed key, else 0
	);
	win.console.log(keyboardEvent);
	target.dispatchEvent(keyboardEvent);
}


function triggerChangeEvent(autWindow, element){
	var document = autWindow.document;		
	if ("createEvent" in document) {
	    var evt = document.createEvent("HTMLEvents");
	    evt.initEvent("change", false, true);
	    element.dispatchEvent(evt);
	}
	else
	    element.fireEvent("onchange");
}


Selenium.prototype.orig_doType = Selenium.prototype.doType;

Selenium.prototype.doType = function(locator, value) {
	
    // Selenium doesn't actually simulate a user typing into an input box so
    // for SmartClient FormItem's manually register the change.
   	if(this.isWhirlLocator(locator)) {
    
        var autWindow = this.getAutWindow();

        var element = autWindow.__Whirl.Selenium.getElementByLocator(locator);

        if (element != null) {
        	//здесь нужно обновить значение...
        	if(element.tagName == 'INPUT' 
        	|| element.tagName == 'TEXTAREA'){
        		element.value = "";
            	bot.action.type(element, value);
            	bot.action.type(element, bot.Keyboard.Keys.ENTER);
        	}
        }
    }
};


Selenium.prototype.isWhirlLocator = function(locator) {
	return locator != null && locator.indexOf("whirl://") !== -1;
};


