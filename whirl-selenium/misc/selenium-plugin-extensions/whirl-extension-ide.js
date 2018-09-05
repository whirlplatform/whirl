


var WhirlHelper = {
	DOUBLE_CLICK_TIMEOUT: 500,
	doubleClickInProgress: false,
	prevClickLocator: null,
		
	trace: function(recorderContext, target, value){
		if(recorderContext != null)	recorderContext.record('TRACE:', target, value);
	},
	
	getWindow: function(context){
	    var autWindow = context.window;
	    if (autWindow.wrappedJSObject) {
	        autWindow = autWindow.wrappedJSObject;
	    }
	    return autWindow;
	},
	findwhirlLocator: function(element, autWindow, recorderContext) {
	    //the element selenium passes is a "safe" XPCNativeWrappers wrapper of the real element. XPCNativeWrappers are used to protect
	    //the chrome code working with content objects and there's no way to access the real "underlying" element object.
	    //example of an element passed here is [object XPCNativeWrapper [object HTMLInputElement]]

	    //see https://developer.mozilla.org/en/wrappedJSObject
	    //https://developer.mozilla.org/en/XPCNativeWrapper

	    if(WhirlHelper.haswhirlContext(autWindow)) {
	        try {
	            var whirlLocator = autWindow.__whirl.Selenium.getLocatorByElement(element);
	        	//WhirlHelper.trace(recorderContext, whirlLocator, 'whirl locator');

	            return whirlLocator;
	          
	        } catch(ex) {
	            WhirlHelper.trace(recorderContext, 'caught error ' + ex + ' for element ' + e + ' with id' + e.id, 'ERROR');
	            return null;
	        }
	    } else {
	        return null;
	    }
	},
	/**
	 * Вобщем-то предполагается что этот плагин используется для приложения.
	 * Но если это будет не так, то такая проверка предотвратит ошибку.
	 * @param autWindow
	 * @returns
	 */
	hasWhirlContext: function (autWindow) {
	    return autWindow != null && autWindow.__Whirl != null;
	},
	

	convertToLiveElement: function (element) {
	    var id = element.id,
	        nullID;
	    var autWindow = WhirlHelper.getWindow(this);
	    //The sc classes are loaded in wrappedJSObject window, and not the window reference held by Locators.
	    //see https://developer.mozilla.org/en/wrappedJSObject
	    var e = autWindow.document.getElementById(id);
	    
	    // reset ID to null - this way if we *don't* get a SmartClient locator
	    // normal page locator strategy will work
	    if (nullID) {
	        element.id = null;
	    }
	    return e;
	},
	
	handleClick: function(recorderContext, element, whirlLocator, isFirstClick, isSecondClick){
		
        if(WhirlHelper.doubleClickInProgress && isSecondClick){
//            recorderContext.record("waitForElementClickable", whirlLocator, '');
            recorderContext.record("doubleClick", whirlLocator, '');
        }else
        if(!WhirlHelper.doubleClickInProgress && isFirstClick){
//        	recorderContext.record("waitForElementClickable", whirlLocator, '');
        	recorderContext.record("click", whirlLocator, '');
        }
	},

	doMouseDown: function(event){
		var recorderContext = this;
		
	    if (event.button == 0) {
	    	var autWindow = WhirlHelper.getWindow(this);
	        if(WhirlHelper.hasWhirlContext(autWindow)) {
	        	var element = this.clickedElement;
	            var whirlLocator = WhirlHelper.findwhirlLocator(element, autWindow, this);
	            var clickTime = (new Date).getTime();

	            var isFirstClick = false;
	            var isSecondClick = false;
	            WhirlHelper.doubleClickInProgress = false;
	            if(WhirlHelper.lastClickTime != null){
	            	if(clickTime - WhirlHelper.lastClickTime < WhirlHelper.DOUBLE_CLICK_TIMEOUT
	            		&& WhirlHelper.prevClickLocator == whirlLocator ){
	            		isSecondClick = true;
	            		isFirstClick = false;
	            		WhirlHelper.doubleClickInProgress = true;
	            	} else{
	            		isSecondClick = false;
	            		isFirstClick = true;
	            	}
	            } else{
	            	isFirstClick = true;
	            	isSecondClick = false;
	            }
	            
	            autWindow.setTimeout(function(){WhirlHelper.handleClick(recorderContext, element, whirlLocator, isFirstClick ,isSecondClick);}, WhirlHelper.DOUBLE_CLICK_TIMEOUT);
	           
	           	WhirlHelper.lastClickTime = clickTime;
	           	WhirlHelper.prevClickLocator = whirlLocator;
	        }
	    }
	}
};


LocatorBuilders.add('whirl-locator', WhirlHelper.findwhirlLocator);
LocatorBuilders.order = ['whirl-locator', 'id', 'link', 'name', 'dom:name', 'xpath:link', 'xpath:img', 'xpath:attributes', 'xpath:href', 'dom:index', 'xpath:position'];

Recorder.removeEventHandler('mouseDownLocator');
Recorder.addEventHandler('mouseDownLocator', 'mousedown', WhirlHelper.doMouseDown, { capture: true });

//override the default type locator to pick up typing within SC form items.
Recorder.removeEventHandler('type');
Recorder.addEventHandler('type', 'change', function(event) {
        
        var recorderContext = null; // this; //null отключит трассировку для этого метода и вложенных. this - включит.

    	var tagName = event.target.tagName.toLowerCase();
        var type = event.target.type;

        //WhirlHelper.trace(recorderContext, 'type ' + type, '');

        if (('input' == tagName && ('text' == type || 'password' == type || 'file' == type)) ||
                'textarea' == tagName) {
    
            // === start sc specific code ===
       	   var autWindow = WhirlHelper.getWindow(this);

            if(WhirlHelper.haswhirlContext(autWindow)) {
                var element = event.target;
              
                var whirlLocator = WhirlHelper.findwhirlLocator(element, autWindow, this);
                WhirlHelper.trace(recorderContext, 'whirl locator ' + whirlLocator, '');
                if(whirlLocator != null) {
                    this.record("type", whirlLocator, event.target.value);
                }
            }
        } else {
        	WhirlHelper.trace(recorderContext, 'type ' + type + ' not supported', 'error');
        }
    }, { capture: true });
