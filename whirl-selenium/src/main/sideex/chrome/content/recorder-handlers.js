/*
 * Copyright 2005 Shinya Kasatani
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * type
 */
Recorder.inputTypes = ["text", "password", "file", "datetime", "datetime-local", "date", "month", "time", "week", "number", "range", "email", "url", "search", "tel", "color"];
Recorder.addEventHandler('type', 'change', function(event) {
    //SuggestionDropDownExt, Chen-Chieh Ping, SELAB, CSIE, NCKU, 2016/11/10
    if (event.target.tagName && !preventType) {
        var tagName = event.target.tagName.toLowerCase();
        var type = event.target.type;
        if ('input' == tagName && Recorder.inputTypes.indexOf(type) >= 0) {
            if (event.target.value.length > 0) {
                // TODO figure out if we need sendKeys or type and record it
                this.record("type", this.findLocators(event.target), event.target.value);

                //SuggestionDropDownExt, Chen-Chieh Ping, SELAB, CSIE, NCKU, 2016/11/10
                preventType = true;

                //FormSubmitByEnterKeyExt, Chen-Chieh Ping, SELAB, CSIE, NCKU, 2016/10/07
                if (enterTarget != null) {
                    //FormSubmitByEnterKeyExt & UnnamedWinIFrameExt, Jie-Lin You, SELAB, CSIE, NCKU, 2016/10/18
                    var tempTarget = event.target.parentElement;
                    var formChk = tempTarget.tagName.toLowerCase();
                    while (formChk != 'form' && formChk != 'body') {
                        tempTarget = tempTarget.parentElement;
                        formChk = tempTarget.tagName.toLowerCase();
                    }
                    if (formChk == 'form' && (tempTarget.hasAttribute("id") || tempTarget.hasAttribute("name")) && (!tempTarget.hasAttribute("onsubmit"))) {
                        if (tempTarget.hasAttribute("id")) this.record("submit", "id=" + tempTarget.id, "");
                        else if (tempTarget.hasAttribute("name")) this.record("submit", "name=" + tempTarget.name, "");
                    } else
                        this.record("sendKeys", this.findLocators(enterTarget), "${KEY_ENTER}");
                    enterTarget = null;
                }
            } else {
                //use type to clear
                this.record("type", this.findLocators(event.target), event.target.value);
            }
        } else if ('textarea' == tagName) {
            //use type for file uploads
            this.record("type", this.findLocators(event.target), event.target.value);
        }
    }
});

/*
 * select / addSelection / removeSelection
 */
Recorder.addEventHandler('selectFocus', 'focus', function(event) {
    if (event.target.nodeName) {
        var tagName = event.target.nodeName.toLowerCase();
        if ('select' == tagName && event.target.multiple) {
            this.log.debug('remembering selections');
            var options = event.target.options;
            for (var i = 0; i < options.length; i++) {
                if (options[i]._wasSelected == null) {
                    // is the focus was gained by mousedown event, _wasSelected would be already set
                    options[i]._wasSelected = options[i].selected;
                }
            }
        }
    }
}, { capture: true });


Recorder.addEventHandler('selectMousedown', 'mousedown', function(event) {
    //DragAndDropExt, Shuo-Heng Shih, SELAB, CSIE, NCKU, 2016/07/22
    var self = this;
    if (event.clientX < this.window.document.documentElement.clientWidth && event.clientY < this.window.document.documentElement.clientHeight){
        this.mousedown =event;
        this.mouseup = setTimeout(function(){
            delete self.mousedown;
        }.bind(this),200);

        this.selectMouseup = setTimeout(function() {
            self.selectMousedown = event;
        }.bind(this), 200);
    }
    this.mouseoverQ = [];

    if (event.target.nodeName) {
        var tagName = event.target.nodeName.toLowerCase();
        if ('option' == tagName) {
            var parent = event.target.parentNode;
            if (parent.multiple) {
                this.log.debug('remembering selections');
                var options = parent.options;
                for (var i = 0; i < options.length; i++) {
                    options[i]._wasSelected = options[i].selected;
                }
            }
        }
    }
}, { capture: true });


//DragAndDropExt, Shuo-Heng Shih, SELAB, CSIE, NCKU, 2016/11/01
Recorder.addEventHandler('selectMouseup', 'mouseup', function(event) {
    clearTimeout(this.selectMouseup);
    if (this.selectMousedown) {

        var x = event.clientX - this.selectMousedown.clientX;
        var y = event.clientY - this.selectMousedown.clientY;

        function getSelectionText(self) {
            var text = "";
            var activeEl = self.window.document.activeElement;
            var activeElTagName = activeEl ? activeEl.tagName.toLowerCase() : null;
            if (activeElTagName == "textarea" || activeElTagName == "input") {
                text = activeEl.value.slice(activeEl.selectionStart, activeEl.selectionEnd);
            } else if (self.window.getSelection) {
                text = self.window.getSelection().toString();
            }
            return text.trim();
        }

        if (this.selectMousedown && event.button === 0 && (x + y) && (event.clientX < this.window.document.documentElement.clientWidth && event.clientY < this.window.document.documentElement.clientHeight) && getSelectionText(this) === '') {
            var sourceRelateX = this.selectMousedown.pageX - this.selectMousedown.target.getBoundingClientRect().left - this.window.scrollX;
            var sourceRelateY = this.selectMousedown.pageY - this.selectMousedown.target.getBoundingClientRect().top - this.window.scrollY;
            var targetRelateX, targetRelateY;
            if (!!this.mouseoverQ.length && this.mouseoverQ[1].relatedTarget == this.mouseoverQ[0].target && this.mouseoverQ[0].target == event.target) {
                targetRelateX = event.pageX - this.mouseoverQ[1].target.getBoundingClientRect().left - this.window.scrollX;
                targetRelateY = event.pageY - this.mouseoverQ[1].target.getBoundingClientRect().top - this.window.scrollY;
                this.record("mouseDownAt", this.findLocators(this.selectMousedown.target), sourceRelateX + ',' + sourceRelateY);
                this.record("mouseMoveAt", this.findLocators(this.mouseoverQ[1].target), targetRelateX + ',' + targetRelateY);
                this.record("mouseUpAt", this.findLocators(this.mouseoverQ[1].target), targetRelateX + ',' + targetRelateY);
            } else {
                targetRelateX = event.pageX - event.target.getBoundingClientRect().left - this.window.scrollX;
                targetRelateY = event.pageY - event.target.getBoundingClientRect().top - this.window.scrollY;
                this.record("mouseDownAt", this.findLocators(this.selectMousedown.target), sourceRelateX + ',' + sourceRelateY);
                this.record("mouseMoveAt", this.findLocators(event.target), targetRelateX + ',' + targetRelateY);
                this.record("mouseUpAt", this.findLocators(event.target), targetRelateX + ',' + targetRelateY);
            }
        }

    } else {

        delete this.clickLocator;
        delete this.mouseup;
        var x = event.clientX - this.mousedown.clientX;
        var y = event.clientY - this.mousedown.clientY;

        if (this.mousedown && this.mousedown.target !== event.target && !(x + y)) {
            this.record("mouseDown", this.findLocators(this.mousedown.target), '');
            this.record("mouseUp", this.findLocators(event.target), '');
        } else if (this.mousedown && this.mousedown.target === event.target) {
            var self = this;
            var target = this.findLocators(this.mousedown.target);
            setTimeout(function() {
                if (!self.clickLocator)
                    self.record("click", target, '');
            }.bind(this), 100);
        }

    }
    delete this.mousedown;
    delete this.selectMousedown;
    delete this.mouseoverQ;

}, {
    capture: true

});



//DragAndDropExt, Shuo-Heng Shih, SELAB, CSIE, NCKU, 2016/07/19
Recorder.addEventHandler('dragstartLocator', 'dragstart', function(event) {
    var self = this;
    this.dropLocator = setTimeout(function() {
        self.dragstartLocator = event;
    }.bind(this), 200);
}, {
    capture: true
});

//DragAndDropExt, Shuo-Heng Shih, SELAB, CSIE, NCKU, 2016/10/17
Recorder.addEventHandler('dropLocator', 'drop', function(event) {
    clearTimeout(this.dropLocator);
    if (this.dragstartLocator && event.button == 0 && this.dragstartLocator.target !== event.target) {
        this.record("dragAndDropToObject", this.findLocators(this.dragstartLocator.target), this.findLocator(event.target));
    }
    delete this.dragstartLocator;
    delete this.selectMousedown;
}, {
    capture: true
});

Recorder.prototype.getOptionLocator = function(option) {
    var label = option.text.replace(/^ *(.*?) *$/, "$1");
    if (label.match(/\xA0/)) { // if the text contains &nbsp;
        return "label=regexp:" + label.replace(/[\(\)\[\]\\\^\$\*\+\?\.\|\{\}]/g, function(str) {
                return '\\' + str })
            .replace(/\s+/g, function(str) {
                if (str.match(/\xA0/)) {
                    if (str.length > 1) {
                        return "\\s+";
                    } else {
                        return "\\s";
                    }
                } else {
                    return str;
                }
            });
    } else {
        return "label=" + label;
    }
};

Recorder.addEventHandler('select', 'change', function(event) {
    if (event.target.tagName) {
        var tagName = event.target.tagName.toLowerCase();
        if ('select' == tagName) {
            if (!event.target.multiple) {
                var option = event.target.options[event.target.selectedIndex];
                this.log.debug('selectedIndex=' + event.target.selectedIndex);
                this.record("select", this.findLocators(event.target), this.getOptionLocator(option));
            } else {
                this.log.debug('change selection on select-multiple');
                var options = event.target.options;
                for (var i = 0; i < options.length; i++) {
                    this.log.debug('option=' + i + ', ' + options[i].selected);
                    if (options[i]._wasSelected == null) {
                        this.log.warn('_wasSelected was not recorded');
                    }
                    if (options[i]._wasSelected != options[i].selected) {
                        var value = this.getOptionLocator(options[i]);
                        if (options[i].selected) {
                            this.record("addSelection", this.findLocators(event.target), value);
                        } else {
                            this.record("removeSelection", this.findLocators(event.target), value);
                        }
                        options[i]._wasSelected = options[i].selected;
                    }
                }
            }
        }
    }
});

//ClickAtMouseDownUpExt, Jie-Lin You, SELAB, CSIE, NCKU, 2016/11/28
var preventClickTwice = false;
Recorder.addEventHandler('clickLocator', 'click', function(event) {
    this.clickLocator = event;
    //SuggestionDropDownExt, Chen-Chieh Ping, SELAB, CSIE, NCKU, 2016/11/24
    //FormSubmitByEnterKeyExt, Chen-Chieh Ping, SELAB, CSIE, NCKU, 2016/10/07
    if (event.button == 0 && !preventClick && event.isTrusted) {
        //SuggestionDropDownExt, Chen-Chieh Ping, SELAB, CSIE, NCKU, 2016/11/10
        if (focusTarget != null && focusTarget.value != focusValue && !preventType) {
            this.record("type", this.findLocators(focusTarget), focusTarget.value);
            preventType = true;
        }
		//ClickAtMouseDownUpExt, Jie-Lin You, SELAB, CSIE, NCKU, 2016/11/28
		if (!preventClickTwice){
			//ClickAtMouseDownUpExt, Jie-Lin You, SELAB, CSIE, NCKU, 2016/11/18
			var clickable = this.isClickableElement(event.target);
			if (clickable) {
				// prepend any required mouseovers. These are defined as
				// handlers that set the "mouseoverLocator" attribute of the
				// interacted element to the locator that is to be used for the
				// mouseover command. For example:
				//
				// Recorder.addEventHandler('mouseoverLocator', 'mouseover', function(event) {
				//     var target = event.target;
				//     if (target.id == 'mmlink0') {
				//         this.mouseoverLocator = 'img' + target._itemRef;
				//     }
				//     else if (target.id.match(/^mmlink\d+$/)) {
				//         this.mouseoverLocator = 'lnk' + target._itemRef;
				//     }
				// }, { alwaysRecord: true, capture: true });
				//
				if (this.mouseoverLocator) {
					this.record('mouseOver', this.mouseoverLocator, '');
                    this.mouseoutLocator = this.mouseoverLocator;
					delete this.mouseoverLocator;
				}
				//InfluentialMouseoverExt, Shuo-Heng Shih, SELAB, CSIE, NCKU, 2016/08/02
				delete this.nodeInsertedLocator;

				//UnnamedWinIFrameExt, Jie-Lin You, SELAB, CSIE, NCKU, 2016/07/05
				var flag = 0;
				if (event.target.hasAttribute("href")) {
					var link = event.target.getAttribute("href");
					if (event.target.hasAttribute("target")) {
						var target = event.target.getAttribute("target");
						if (link != '#' && link != '') {
							if (target == "_blank" || target.indexOf('_') != 0) {
								this.record("click", this.findLocators(event.target), '');
								this.record("waitForPopUp", '', '30000');
								flag = 1;
							}
						}
					}
				}
				if (flag == 0) {
					this.record("click", this.findLocators(event.target), '');
				}
				//ClickAtMouseDownUpExt, Jie-Lin You, SELAB, CSIE, NCKU, 2016/11/28
				preventClickTwice = true;
			} else {
				//ClickAtMouseDownUpExt, Jie-Lin You, SELAB, CSIE, NCKU, 2016/11/15
				//Calculate the corresponding client X,Y of element
				var top = event.pageY,
					left = event.pageX;
				var element = event.target;
				do {
					top -= element.offsetTop;
					left -= element.offsetLeft;
					element = element.offsetParent;
				} while (element);
				var target = event.target;
				this.record("clickAt", this.findLocators(target), left + ',' + top);
				//ClickAtMouseDownUpExt, Jie-Lin You, SELAB, CSIE, NCKU, 2016/11/28
				preventClickTwice = true;
				/*this.callIfMeaningfulEvent(function() {
						this.record("click", this.findLocators(target), '');
					});*/
			}
		}
		//ClickAtMouseDownUpExt, Jie-Lin You, SELAB, CSIE, NCKU, 2016/11/28
		setTimeout(function() { preventClickTwice = false; }, 30);
    }
}, { capture: true });

//InfluentialScrollingExt, Shuo-Heng Shih, SELAB, CSIE, NCKU, 2016/08/02
var prevTimeOut = null;
Recorder.addEventHandler('scrollDetector', 'scroll', function(event) {
    if (pageLoaded === true) {
        var self = this;
        this.scrollDetector = event.target;
        clearTimeout(prevTimeOut);
        prevTimeOut = setTimeout(function() {
            delete self.scrollDetector;
        }.bind(self), 500);
    }
}, {
    capture: true
});

//InfluentialMouseoverExt, Shuo-Heng Shih, SELAB, CSIE, NCKU, 2016/10/17
var nowNode = 0;
Recorder.addEventHandler('mouseoverLocator', 'mouseover', function(event) {
    if(this.window.document.documentElement)
        nowNode = this.window.document.documentElement.getElementsByTagName('*').length;
    var self = this;
    if (pageLoaded === true) {
        var clickable = this.findClickableElement(event.target);
        if (clickable) {
            this.nodeInsertedLocator = event.target;
            setTimeout(function() { delete self.nodeInsertedLocator; }.bind(self), 500);

            this.nodeAttrChange = this.findLocators(event.target);
            this.nodeAttrChangeTimeout = setTimeout(function(){delete self.nodeAttrChange;}.bind(self),10);
        }
        //drop target overlapping
        if (this.mouseoverQ) //mouse keep down
        {
            if (this.mouseoverQ.length >= 3)
                this.mouseoverQ.shift();
            this.mouseoverQ.push(event);
        }
    }
}, {
    capture: true
});

//InfluentialMouseoverExt, Shuo-Heng Shih, SELAB, CSIE, NCKU, 2016/11/08
Recorder.addEventHandler('mouseoutLocator', 'mouseout', function(event) {
    if (this.mouseoutLocator !== null && event.target === this.mouseoutLocator) {
        this.record("mouseOut", this.findLocators(event.target), '');
    }
    delete this.mouseoutLocator;
}, {
    capture: true
});

//InfluentialMouseoverExt & InfluentialScrollingExt, Shuo-Heng Shih, SELAB, CSIE, NCKU, 2016/11/08
Recorder.addEventHandler('nodeInsertedLocator', 'DOMNodeInserted', function(event) {
    if (pageLoaded === true && this.window.document.documentElement.getElementsByTagName('*').length > nowNode) {
        var self = this;
        if (this.scrollDetector) {
            this.record("runScript", "window.scrollTo(0," + this.window.scrollY + ")", '');
            pageLoaded = false;
            setTimeout(function() { pageLoaded = true; }.bind(self), 550);
            delete this.scrollDetector;
            delete this.nodeInsertedLocator;
        }
        if (this.nodeInsertedLocator) {
            this.record("mouseOver", this.findLocators(this.nodeInsertedLocator), '');
            this.mouseoutLocator = this.nodeInsertedLocator;
            delete this.nodeInsertedLocator;
            delete this.mouseoverLocator;
        }
        }
}, {
    capture: true
});

Recorder.addEventHandler('nodeRemovedLocator', 'DOMNodeRemoved', function(event) {
    if (pageLoaded === true && this.window.document.documentElement.getElementsByTagName('*').length < nowNode) {
        var self = this;
        if (this.scrollDetector) {
            this.record("runScript", "window.scrollTo(0," + this.window.scrollY + ")", '');
            pageLoaded = false;
            setTimeout(function() { pageLoaded = true; }.bind(self), 550);
            delete this.scrollDetector;
            delete this.nodeInsertedLocator;
        }
        if (this.nodeInsertedLocator) {
            this.record("mouseOver", this.findLocators(this.nodeInsertedLocator), '');
            this.mouseoutLocator = this.nodeInsertedLocator;
            delete this.nodeInsertedLocator;
            delete this.mouseoverLocator;
        }
    }
}, {
    capture: true
});

Recorder.addEventHandler('nodeAttrChange', 'DOMAttrModified', function(event) {
    var self = this;
    if(this.nodeAttrChange){
        clearTimeout(this.nodeAttrChangeTimeout);
        this.mouseoverLocator = this.nodeAttrChange;
        delete this.nodeAttrChange;
    }
}, {
    capture: true
});

Recorder.prototype.findClickableElement = function(e) {
    if (!e.tagName) return null;
    var tagName = e.tagName.toLowerCase();
    var type = e.type;
    if (e.hasAttribute("onclick") || e.hasAttribute("href") || tagName == "button" ||
        (tagName == "input" &&
            (type == "submit" || type == "button" || type == "image" || type == "radio" || type == "checkbox" || type == "reset"))) {
        return e;
    } else {
        if (e.parentNode != null) {
            return this.findClickableElement(e.parentNode);
        } else {
            return null;
        }
    }
};

//ClickAtMouseDownUpExt, Jie-Lin You, SELAB, CSIE, NCKU, 2016/11/18
Recorder.prototype.isClickableElement = function(e) {
    if (!e.tagName) return false;
    var tagName = e.tagName.toLowerCase();
    var type = e.type;
    if (e.hasAttribute("onclick") || e.hasAttribute("href") || tagName == "button" ||
        (tagName == "input" &&
            (type == "submit" || type == "button" || type == "image" || type == "radio" || type == "checkbox" || type == "reset"))) {
        return true;
    } else return false;
};

//InfluentialMouseoverExt & InfluentialScrollingExt, Shuo-Heng Shih, SELAB, CSIE, NCKU, 2016/08/02
var readyTimeOut = null;
var pageLoaded = true;
Recorder.addEventHandler('checkPageLoaded', 'readystatechange', function(event) {
    var self = this;
    if (this.window.document.readyState === 'loading') {
        pageLoaded = false;
    } else {
        pageLoaded = false;
        clearTimeout(readyTimeOut);
        readyTimeOut = setTimeout(function() { pageLoaded = true; }.bind(self), 1500); //setReady after complete 1.5s
    }
}, {
    capture: true,
    alwaysRecord: true
});

//DoubleClickExt, Chen-Chieh Ping, SELAB, CSIE, NCKU, 2016/11/23
Recorder.addEventHandler('doubleClicktest', 'dblclick', function(event) {
    this.record('doubleClick', this.findLocators(event.target), '');
}, { capture: true });

//FormSubmitByEnterKeyExt & SuggestionDropDownExt, Chen-Chieh Ping, SELAB, CSIE, NCKU, 2016/11/10
var focusTarget = null;
var focusValue = null;
var tempValue = null;
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
        }
    }
}, { capture: true });


//FormSubmitByEnterKeyExt & SuggestionDropDownExt, Chen-Chieh Ping, SELAB, CSIE, NCKU, 2016/11/10
Recorder.addEventHandler('inputBlur', 'blur', function(event) {
    if (event.target.nodeName) {
        var tagName = event.target.nodeName.toLowerCase();
        var type = event.target.type;
        if (tagName == 'input' && Recorder.inputTypes.indexOf(type) >= 0) {
            focusTarget = null;
            focusValue = null;
            tempValue = null;
        }
    }
}, { capture: true });

//FormSubmitByEnterKeyExt, Chen-Chieh Ping, SELAB, CSIE, NCKU, 2016/10/08
var preventClick = false;
var preventType = false;
var enterTarget = null;
var enterValue = null;
var tabCheck = null;
Recorder.addEventHandler('keyDownTest', 'keydown', function(event) {
    if (event.target.tagName) {
        var key = event.keyCode;
        var tagName = event.target.tagName.toLowerCase();
        var type = event.target.type;
        if (tagName == 'input' && Recorder.inputTypes.indexOf(type) >= 0) {
            if (key == 13) {
                enterTarget = event.target;
                enterValue = enterTarget.value;
                //FormSubmitByEnterKeyExt & UnnamedWinIFrameExt, Jie-Lin You, SELAB, CSIE, NCKU, 2016/10/17
                var tempTarget = event.target.parentElement;
                var formChk = tempTarget.tagName.toLowerCase();
                if (tempValue == enterTarget.value && tabCheck == enterTarget) {
                    this.record("sendKeys", this.findLocators(enterTarget), "${KEY_ENTER}");
                    enterTarget = null;
                    preventType = true;
                } else if (focusValue == enterTarget.value) {
                    while (formChk != 'form' && formChk != 'body') {
                        tempTarget = tempTarget.parentElement;
                        formChk = tempTarget.tagName.toLowerCase();
                    }
                    if (formChk == 'form' && (tempTarget.hasAttribute("id") || tempTarget.hasAttribute("name")) && (!tempTarget.hasAttribute("onsubmit"))) {
                        if (tempTarget.hasAttribute("id")) this.record("submit", "id=" + tempTarget.id, "");
                        else if (tempTarget.hasAttribute("name")) this.record("submit", "name=" + tempTarget.name, "");
                    } else
                        this.record("sendKeys", this.findLocators(enterTarget), "${KEY_ENTER}");
                    enterTarget = null;
                }
                preventClick = true;
                setTimeout(function() {
                    preventClick = false;
                }, 500);
                setTimeout(function() {
                    if (enterValue != event.target.value) enterTarget = null;
                }, 50);
            }

            //SuggestionDropDownExt, Chen-Chieh Ping, SELAB, CSIE, NCKU, 2016/11/10
            var tempbool = false;
            if ((key == 38 || key == 40) && event.target.value != '') {
                if (focusTarget != null && focusTarget.value != tempValue) {
                    tempbool = true;
                    tempValue = focusTarget.value;
                }
                this.callIfMeaningfulEvent(function() {
                    if (tempbool) {
                        this.record("type", this.findLocators(focusTarget), tempValue);
                    }

                    setTimeout(function() {
                        tempValue = focusTarget.value;
                    }, 50);

                    if (key == 38) this.record("sendKeys", this.findLocators(event.target), "${KEY_UP}");
                    else this.record("sendKeys", this.findLocators(event.target), "${KEY_DOWN}");

                    tabCheck = event.target;
                });
            }
            if (key == 9) {
                if (tabCheck == event.target) {
                    this.callIfMeaningfulEvent(function() {
                        this.record("sendKeys", this.findLocators(event.target), "${KEY_TAB}");

                        preventType = true;
                    });
                }
            }
        }
    }
}, { capture: true });

// remember clicked element to be used in CommandBuilders
Recorder.addEventHandler('rememberClickedElement', 'mousedown', function(event) {
    this.clickedElement = event.target;
    this.clickedElementLocators = this.findLocators(event.target);
}, { alwaysRecord: true, capture: true });

Recorder.addEventHandler('attrModified', 'DOMAttrModified', function(event) {
    this.log.debug('attrModified');
    this.domModified();
}, { capture: true });

Recorder.addEventHandler('nodeInserted', 'DOMNodeInserted', function(event) {
    this.log.debug('nodeInserted');
    this.domModified();
}, { capture: true });

Recorder.addEventHandler('nodeRemoved', 'DOMNodeRemoved', function(event) {
    this.log.debug('nodeRemoved');
    this.domModified();
}, { capture: true });

Recorder.prototype.domModified = function() {
    if (this.delayedRecorder) {
        this.delayedRecorder.apply(this);
        this.delayedRecorder = null;
        if (this.domModifiedTimeout) {
            clearTimeout(this.domModifiedTimeout);
        }
    }
};

Recorder.prototype.callIfMeaningfulEvent = function(handler) {
    this.log.debug("callIfMeaningfulEvent");
    this.delayedRecorder = handler;
    var self = this;
    this.domModifiedTimeout = setTimeout(function() {
        self.log.debug("clear event");
        self.delayedRecorder = null;
        self.domModifiedTimeout = null;
    }, 250);
};

//EditContentExt, Lin Yun Wen, SELAB, CSIE, NCKU, 2016/11/17
var getEle;
var checkFocus = 0;
Recorder.addEventHandler('getFocus', 'focus', function(event) {
    var editable = event.target.contentEditable;
    if (editable == 'true') {
        getEle = event.target;
        contentTest = getEle.innerHTML;
        checkFocus = 1;
    }

}, { capture: true });

//EditContentExt, Lin Yun Wen, SELAB, CSIE, NCKU, 2016/11/17
Recorder.addEventHandler('inputText', 'blur', function(event) {
    if (checkFocus == 1) {
        if (event.target == getEle) {
            if (getEle.innerHTML != contentTest) {
                this.record("editContent", this.findLocators(event.target), getEle.innerHTML);
            }
            checkFocus = 0;
        }
    }
}, { capture: true });
