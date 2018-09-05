/*
* Copyright 2011 Software Freedom Conservancy
*
*  Licensed under the Apache License, Version 2.0 (the "License");
*  you may not use this file except in compliance with the License.
*  You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software
*  distributed under the License is distributed on an "AS IS" BASIS,
*  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*  See the License for the specific language governing permissions and
*  limitations under the License.
*/

function TestLoop(commandFactory) {
    this.commandFactory = commandFactory;
}

TestLoop.prototype = {
    //AJAXWaitExt & PageLoadingWaitExt, Yu-Xian Chen, SELAB, CSIE, NCKU, 2016/07/05
    new_page: false, // check if call onbeforeunload
    show_new_page: false, // check if change to new page 
    page_load_timeout: false,
    ajax_obj: [],
    ajax_load_timeout: [],

    //DOMModifiedWaitExt, Yu-Xian Chen, SELAB, CSIE, NCKU, 2016/11/23
    domModifiedTime: "",  
    dom_load_timeout: false, 
    
    //AJAXWaitExt & PageLoadingWaitExt, Yu-Xian Chen, SELAB, CSIE, NCKU, 2016/10/18    
    webdriver_ajaxWait_script: "if(!window.sideex){window.sideex={};}if(!window.sideex.origXMLHttpRequest){window.sideex.origXMLHttpRequest=window.XMLHttpRequest;window.sideex.ajax_obj=[];window.sideex.ajax_timedOut=[];}window.XMLHttpRequest=function(){var xhr=new window.sideex.origXMLHttpRequest();window.sideex.ajax_obj.push(xhr);window.sideex.ajax_timedOut.push(false);return xhr;};var done=true;if(window.sideex.ajax_obj.length!==0){for(var index in window.sideex.ajax_obj){if(window.sideex.ajax_obj[index].readyState!==4&&window.sideex.ajax_obj[index].readyState!==undefined&&window.sideex.ajax_obj[index].readyState!==0&&!window.sideex.ajax_timedOut[index]){done=false;break;}}}if(done)ajax_done=true;else ajax_done = false;",
    webdriver_ajaxClean_script: "for(var i=0;i<window.sideex.ajax_timedOut.length;i++){window.sideex.ajax_timedOut[i]=true;}",
    webdriver_ajaxWait_time: "",
    webdriver_pageWait_time: "",
    webdriver_implicitWait_time: "",

    //PageLoadingWaitExt, Yu-Xian Chen, SELAB, CSIE, NCKU, 2016/12/14
    next_command_wait: false,   

    start : function() {
        selenium.reset();
        LOG.debug("currentTest.start()");
        this.continueTest();
    },

    continueTest : function() {
        /**
         * Select the next command and continue the test.
         */
        LOG.debug("currentTest.continueTest() - acquire the next command");
        if (! this.aborted) {
            this.currentCommand = this.nextCommand();
        }
        if (! this.requiresCallBack) {
            this.continueTestAtCurrentCommand();
        } // otherwise, just finish and let the callback invoke continueTestAtCurrentCommand()
    },

    continueTestAtCurrentCommand : function() {
        LOG.debug("currentTest.continueTestAtCurrentCommand()");
        if (this.currentCommand) {
            // TODO: rename commandStarted to commandSelected, OR roll it into nextCommand
            this.commandStarted(this.currentCommand);
            this._resumeAfterDelay();
        } else {
            this._testComplete();
        }
    },

    _resumeAfterDelay : function() {
        /**
         * Pause, then execute the current command.
         */

        // Get the command delay. If a pauseInterval is set, use it once
        // and reset it.  Otherwise, use the defined command-interval.
        var delay = this.pauseInterval || this.getCommandInterval();
        this.pauseInterval = undefined;

        if (this.currentCommand.isBreakpoint || delay < 0) {
            // Pause: enable the "next/continue" button
            this.pause();
        } else {
            window.setTimeout(fnBind(this.resume, this), delay);
        }
    },

    resume: function() {
        /**
         * Select the next command and continue the test.
         */
        LOG.debug("currentTest.resume() - actually execute");
        
        //AJAXWaitExt & PageLoadingWaitExt, Yu-Xian Chen, SELAB, CSIE, NCKU, 2016/09/26
        var origResume = function() {
            try {
                selenium.browserbot.runScheduledPollers(); // webdriver playback will override this function and execute other function
                this._executeCurrentCommand();
                //ImplicitWaitExt, Yu-Xian Chen, SELAB, CSIE, NCKU, 2016/11/07
                //move to selenium-runner.js: _executeCurrentCommand function 
                // this.continueTestWhenConditionIsTrue();   
            } catch (e) {
                if (!this._handleCommandError(e)) {
                    this.testComplete();
                } else {
                    this.continueTest();
                }
            }
        }.bind(this);

        //AJAXWaitExt & PageLoadingWaitExt, Yu-Xian Chen, SELAB, CSIE, NCKU, 2016/10/18
        if(executeUsingWebDriver) {
            //if use webdriver playback need to use original resume function
            origResume();
        } else { 
            // use IDE to playback
            
            //new playing test case, set all value to default
            if (LOG.new_play) {
                this.new_page = false;
                this.show_new_page = false;
                this.page_load_timeout = false;
                this.ajax_obj = [];
                this.ajax_load_timeout = [];
                var my_win = selenium.browserbot.getCurrentWindow();
                my_win.still_last_window = false;

                if (my_win.origXMLHttpRequest) {
                    my_win.XMLHttpRequest = my_win.origXMLHttpRequest;
                    my_win.origXMLHttpRequest = "";
                }
    
                LOG.new_play = false;
            }

            // after execute last command will doing this, restitute everything changed
            // this function will be called in editor.js:943
            LOG.lastCommandRestitute = function() {
                var my_win = selenium.browserbot.getCurrentWindow();
                
                // PageLoadingWaitExt, Yu-Xian Chen, SELAB, CSIE, NCKU, 2016/11/26
                // remove onbeforeunload event
                try {
                    my_win.removeEventListener('beforeunload', setNewPageValue, false);
                } catch(e){}

    
                //  restore XMLHttpRequest
                if (my_win.origXMLHttpRequest) {
                    my_win.XMLHttpRequest = my_win.origXMLHttpRequest;
                    my_win.origXMLHttpRequest = "";
                }

                // remove DomModified event
                try {
                    my_win.document.body.removeEventListener('DOMNodeInserted', setDOMModifiedTime, false);
                    my_win.document.body.removeEventListener('DOMNodeInsertedIntoDocument', setDOMModifiedTime, false);
                    my_win.document.body.removeEventListener('DOMNodeRemoved', setDOMModifiedTime, false);
                    my_win.document.body.removeEventListener('DOMNodeRemovedFromDocument', setDOMModifiedTime, false);
                    my_win.document.body.removeEventListener('DOMSubtreeModified', setDOMModifiedTime, false);
                } catch(e){}
            };
    
            var resumeWithWait = function() {
                var my_win = selenium.browserbot.getCurrentWindow();
    
                // if make a new page, need to set all thing to default except new_page, show_new_page(next command will use)
                var setNewPageValue = function() {
                    this.new_page = true;
                    this.page_load_timeout = false;
                    this.ajax_obj = [];
                    this.ajax_load_timeout = [];
    
                    my_win.still_last_window = true;
                }.bind(this);
    
                // PageLoadingWaitExt, Yu-Xian Chen, SELAB, CSIE, NCKU, 2016/11/26
                // change pageWait implememtation: change override onbeforeunload function to use addEventListener
                my_win.addEventListener('beforeunload', setNewPageValue, false);
    
                if (my_win.XMLHttpRequest) {
                    // only override XMLHttpRequest once
                    if (!my_win.origXMLHttpRequest) {
                        // it's new page, so set the new window's XMLHttpRequest, and all of the obj in the ajax_obj
                        // are last window's instance, so clear it
                        this.ajax_obj = [];
                        this.ajax_load_timeout = [];
    
                        my_win.origXMLHttpRequest = my_win.XMLHttpRequest;
    
                        my_win.XMLHttpRequest = function() {
                            var xhr = new my_win.origXMLHttpRequest();
                            this.ajax_obj.push(xhr);
                            this.ajax_load_timeout.push(false);
                            return xhr;
                        }.bind(this);
                    }
                }

                //DOMModifiedWaitExt, Yu-Xian Chen, SELAB, CSIE, NCKU, 2016/11/23
                var setDOMModifiedTime = function() {
                    this.domModifiedTime = Date.now();
                }.bind(this);

                if (my_win.document && my_win.document.body) {
                    my_win.document.body.addEventListener('DOMNodeInserted', setDOMModifiedTime, false);
                    my_win.document.body.addEventListener('DOMNodeInsertedIntoDocument', setDOMModifiedTime, false);
                    my_win.document.body.addEventListener('DOMNodeRemoved', setDOMModifiedTime, false);
                    my_win.document.body.addEventListener('DOMNodeRemovedFromDocument', setDOMModifiedTime, false);
                    my_win.document.body.addEventListener('DOMSubtreeModified', setDOMModifiedTime, false);
                } 
    
                var count1 = 0; // just print "Wait for the new page to be fully loaded" once
                var count2 = 0; // just print "Wait for all ajax requests to be done" once
                var page_wait_record_time = 0;
                var ajax_wait_record_time = 0;
    
                // page wait: check if produce a new page, if yes, check if direct to the new page, if yes, then check document.readyState
                // if 2nd,3th step is not yet, then it has to loop check
                var waitTillPageDone = function() {
                    count1++;
                    var my_win = selenium.browserbot.getCurrentWindow();
                    var window_state = my_win.document.readyState;
    
                    // set page wait timeout bound
                    if (page_wait_record_time !== 0) {
                        if ((Date.now() - page_wait_record_time) > 30000) {
                            LOG.warn("SideeX: Page load timed out with 30000ms");
                            this.page_load_timeout = true;
                            page_wait_record_time = 0;
                        }
                    }
    
                    // check page wait
                    if ((window_state !== "complete") && (!this.page_load_timeout)) {
                        // print once
                        if (count1 === 1) {
                            LOG.info("SideeX: Wait for the new page to be fully loaded");
                        }
    
                        if (page_wait_record_time === 0) {
                            page_wait_record_time = Date.now();
                        }
    
                        setTimeout(waitTillPageDone, 500);
                    } else {
                        this.page_load_timeout = false;
                        waitTillAjaxDone();
                    }
                }.bind(this); // waitTillPageDone
    
                // ajax wait
                var waitTillAjaxDone = function() {
                    count2++;
    
                    // set ajax wait timeout bound
                    if (ajax_wait_record_time !== 0) {
                        if ((Date.now() - ajax_wait_record_time) > 30000) {
                            LOG.warn("SideeX: Ajax requests timed out with 30000ms");
    
                            for (var index in this.ajax_obj) {
                                // set all ajax obj's (readyState is 1~3) correspoding load_timeout to true
                                if (this.ajax_obj[index].readyState !== 4 &&
                                    this.ajax_obj[index].readyState !== undefined &&
                                    this.ajax_obj[index].readyState !== 0) {
    
                                    this.ajax_load_timeout[index] = true;
                                }
                            }    
                            ajax_wait_record_time = 0;
                        }
                    }
    
                    // check ajax wait
                    if (this.ajax_obj.length === 0) {
                        //DOMModifiedWaitExt, Yu-Xian Chen, SELAB, CSIE, NCKU, 2016/11/23
                        setTimeout(waitTillDOMModified, 400);
                    } else {
                        var ajax_done = true;
    
                        for (var index2 in this.ajax_obj) {
                            // if readyState is 1~3 and do not timeout, then keep waiting
                            if (this.ajax_obj[index2].readyState !== 4 &&
                                this.ajax_obj[index2].readyState !== undefined &&
                                this.ajax_obj[index2].readyState !== 0 &&
                                !this.ajax_load_timeout[index2]) {
    
                                ajax_done = false;
                                break;
                            }
                        }
    
                        if (!ajax_done) {
                            // print once
                            if (count2 === 1) {
                                LOG.info("SideeX: Wait for all ajax requests to be done");
                            }
    
                            if (ajax_wait_record_time === 0) {
                                ajax_wait_record_time = Date.now();
                            }
    
                            setTimeout(waitTillAjaxDone, 500);
                        } else {
                            //DOMModifiedWaitExt, Yu-Xian Chen, SELAB, CSIE, NCKU, 2016/11/23
                            setTimeout(waitTillDOMModified, 400);
                        }
                    }
                }.bind(this); // waitTillAjaxDone

                //DOMModifiedWaitExt, Yu-Xian Chen, SELAB, CSIE, NCKU, 2016/11/23
                var count3 = 0; // just print "Wait for the DOM tree modification" once
                var dom_wait_record_time = 0;

                var waitTillDOMModified = function() {
                    count3++;

                    // set DOM wait timeout bound
                    if (dom_wait_record_time !== 0) {
                        if ((Date.now() - dom_wait_record_time) > 10000) {
                            LOG.warn("SideeX: DOM tree modification timed out with 10000ms");
                            this.dom_load_timeout = true;
                            dom_wait_record_time = 0;
                        }
                    }

                    if (this.domModifiedTime && (Date.now() - this.domModifiedTime < 400) && (!this.dom_load_timeout)) {
                        // in last 200ms DOM has been modified, we guess that DOM tree "being" changed, so wait for it
                        // print once
                        if (count3 === 1) {
                            LOG.info("SideeX: Wait for the DOM tree modification");
                        }

                        if (dom_wait_record_time === 0) {
                            dom_wait_record_time = Date.now();
                        }
                        setTimeout(waitTillDOMModified, 400);
                    } else {
                        this.dom_load_timeout = false;
                        origResume();
                    }
                }.bind(this); // waitTillDOMModified
    
                // started wait function
                waitTillPageDone();
            }.bind(this); // resumeWithWait
    
            var check_new_page_record_time = 0;
            var checkIfNewPage = function() {
                if (this.new_page) {
                    if (this.show_new_page) {
                        this.new_page = false;
                        this.show_new_page = false;
    
                        // for the short period redirection page, get the second page(correct one)
                        setTimeout(resumeWithWait, 200);
                    } else {
                        // set check new page timeout bound
                        if (check_new_page_record_time === 0) {
                            check_new_page_record_time = Date.now();
                        } else {
                            if ((Date.now() - check_new_page_record_time) > 30000) {
                                LOG.warn("SideeX: Server response timed out with 30000ms");
                                this.show_new_page = true;
                            }
                        }
    
                        // need to check if change to new page, otherwise next command's document may be the old windows
                        var my_win = selenium.browserbot.getCurrentWindow();
                        if (!my_win.still_last_window) {
                            this.show_new_page = true;
                            my_win.still_last_window = false;
                        } 
    
                        setTimeout(checkIfNewPage, 0);
                    }
                } else {
                    resumeWithWait();
                }
            }.bind(this);
    
            // because if beforeunload event will be triggered, it may not be called immediately after last command being executed
            // so postpone 300ms to let beforeunload event be triggered and it will set this.new_page to true
            
            //PageLoadingWaitExt, Yu-Xian Chen, SELAB, CSIE, NCKU, 2016/12/14
            // and if the previous command is verifyXXX, assertXXX, storeXXX, waitForXXX except XXX=Eval, Expression
            // then this command do not need to wait 
            var needToWait = this.next_command_wait;
            var s_command = this.currentCommand.command; 
            var needToCheck = s_command.match(/^verify|^assert|^store|^waitFor/);
            if (needToCheck) {
                var needToCheck2 = s_command.match(/Eval$|Expression$/);
                if (!needToCheck2) {
                    this.next_command_wait = false;
                } else {
                    this.next_command_wait = true;
                }              
            } else {
                this.next_command_wait = true;
            }

            if (needToWait) {
                setTimeout(checkIfNewPage, 300);    
            } else {
                origResume();
            }            
        }
    },

    _testComplete : function() {
        selenium.ensureNoUnhandledPopups();
        this.testComplete();
    },

    _executeCurrentCommand : function() {
        /**
         * Execute the current command.
         *
         * @return a function which will be used to determine when
         * execution can continue, or null if we can continue immediately
         */
        var command = this.currentCommand;
        LOG.info("Executing: |" + command.command + " | " + command.target + " | " + command.value + " |");

        if (Math.random() > 0.8) {
            LOG.info("Selenium 1.0 (Core, RC, etc) is no longer under active development. Please update to WebDriver ASAP");
        }

        var handler = this.commandFactory.getCommandHandler(command.command);
        if (handler == null) {
            throw new SeleniumError("Unknown command: '" + command.command + "'");
        }

        command.target = selenium.preprocessParameter(command.target);
        command.value = selenium.preprocessParameter(command.value);
        LOG.debug("Command found, going to execute " + command.command);
        this.result = handler.execute(selenium, command);
        

        this.waitForCondition = this.result.terminationCondition;

    },

    _handleCommandError : function(e) {
        if (!e.isSeleniumError) {
            LOG.exception(e);
            var msg = "Command execution failure. Please search the user group at https://groups.google.com/forum/#!forum/selenium-users for error details from the log window.";
            msg += "  The error message is: " + extractExceptionMessage(e);
            return this.commandError(msg);
        } else {
            LOG.error(e.message);
            return this.commandError(e.message);
        }
    },

    continueTestWhenConditionIsTrue: function () {
        /**
         * Busy wait for waitForCondition() to become true, and then carry
         * on with test.  Fail the current test if there's a timeout or an
         * exception.
         */
        //LOG.debug("currentTest.continueTestWhenConditionIsTrue()");
        selenium.browserbot.runScheduledPollers();
        try {
            if (this.waitForCondition == null) {
                LOG.debug("null condition; let's continueTest()");
                LOG.debug("Command complete");
                this.commandComplete(this.result);

                //ImplicitWaitExt, Yu-Xian Chen, SELAB, CSIE, NCKU, 2016/11/07
                this.showImplicit = false;
                this.webdriver_implicitWait_time = "";

                //AJAXWaitExt & PageLoadingWaitExt, Yu-Xian Chen, SELAB, CSIE, NCKU, 2016/10/18
                if(executeUsingWebDriver) {
                    // wait 200ms for redirect page
                    setTimeout(function() {
                        this.forceCommand("storeEval", "window.document.readyState", "readyState");
                        this.force_continueTestWhenConditionIsTrue("pageWait");
                    }.bind(this), 200);
                } else {
                    this.continueTest();
                }
                
            } else if (this.waitForCondition()) {
                LOG.debug("condition satisfied; let's continueTest()");
                this.waitForCondition = null;
                LOG.debug("Command complete");
                this.commandComplete(this.result);

                //ImplicitWaitExt, Yu-Xian Chen, SELAB, CSIE, NCKU, 2016/11/07
                this.showImplicit = false;
                this.webdriver_implicitWait_time = "";

                //AJAXWaitExt & PageLoadingWaitExt, Yu-Xian Chen, SELAB, CSIE, NCKU, 2016/10/18
                if (executeUsingWebDriver) {
                    // wait 200ms for redirect page
                    setTimeout(function() {
                        this.forceCommand("storeEval", "window.document.readyState", "readyState");
                        this.force_continueTestWhenConditionIsTrue("pageWait");
                    }.bind(this), 200);
                } else {
                    this.continueTest();
                }
            } else {
                //LOG.debug("waitForCondition was false; keep waiting!");
                window.setTimeout(fnBind(this.continueTestWhenConditionIsTrue, this), 10);
            }
        } catch (e) {
            this.result = {};
            this.result.failed = true;
            this.result.failureMessage = extractExceptionMessage(e);

            //ImplicitWaitExt, Yu-Xian Chen, SELAB, CSIE, NCKU, 2016/11/07           
            if (executeUsingWebDriver) {
                var err = this.result.failureMessage;
                var match_not_found = err.match(/(not found)$/);
                var failed_locator = err.match(/(LOCATOR_DETECTION_FAILED)/);
                var elementNotFound = match_not_found && !failed_locator;
                if (elementNotFound) {
                    var timedOut = false;
                    if (!this.showImplicit) {
                        // first time enter implicit wait
                        LOG.info("Sideex: Wait until the element is found");
                        this.showImplicit = true;
                        this.webdriver_implicitWait_time = Date.now();
                    } else {
                        if (Date.now() - this.webdriver_implicitWait_time > 30000) {
                            timedOut = true;
                            LOG.warn("Sideex: Implicit wait timed out with 30000ms");
                        }
                    }
                    if (timedOut) {
                        this.showImplicit = false;
                        this.webdriver_implicitWait_time = "";
                        this.commandComplete(this.result);
                        this.continueTest();
                    } else {
                        setTimeout(function() {
                            var command = this.currentCommand;
                            this.forceCommand(command.command, command.target, command.value);
                            this.continueTestWhenConditionIsTrue();
                        }.bind(this), 500);
                    }
                } else {
                    this.showImplicit = false;
                    this.webdriver_implicitWait_time = "";
                    this.commandComplete(this.result);
                    this.continueTest();
                }
            } else {
                this.commandComplete(this.result);
                this.continueTest();
            }
        }
    },

    //AJAXWaitExt & PageLoadingWaitExt, Yu-Xian Chen, SELAB, CSIE, NCKU, 2016/10/05
    forceCommand: function(name, target, value) {
        var command = {
            command: name,
            target: target,
            value: value
            // isBreakpoint: undefined
        };

        LOG.debug("Force to execute: |" + command.command + " | " + command.target + " | " + command.value + " |");

        var handler = this.commandFactory.getCommandHandler(command.command);
        if (handler == null) {
            throw new SeleniumError("Unknown command: '" + command.command + "'");
        }

        command.target = selenium.preprocessParameter(command.target);
        command.value = selenium.preprocessParameter(command.value);
        LOG.debug("Command found, going to execute " + command.command);
        updateStats(command.command);
        this.result = handler.execute(selenium, command);
        this.waitForCondition = this.result.terminationCondition;
    },

    //AJAXWaitExt & PageLoadingWaitExt, Yu-Xian Chen, SELAB, CSIE, NCKU, 2016/10/18
    waitCheck: function(option) {
        // user command -> pageWait -> ajaxWait -> echo result -> if ajax done, execute next command
        //                                                     -> if not, do ajaxWait again                 
        if (option === "pageWait") {
            this.forceCommand("echoToGetValue", "${readyState},readyState", "");
            this.force_continueTestWhenConditionIsTrue("checkPageWait");
        } else if (option === "checkPageWait") {           
            if (LOG.sideex.readyState === "complete") {
                this.webdriver_pageWait_time = "";
                this.force_continueTestWhenConditionIsTrue("pageWaitDone");
            } else {
                var timedOut = false;
                if (!this.webdriver_pageWait_time) {
                    LOG.info("SideeX: Wait for the new page to be fully loaded");
                    // initial record time
                    this.webdriver_pageWait_time = Date.now();
                } else {
                    if ((Date.now() - this.webdriver_pageWait_time) > 30000) {
                        timedOut = true;
                    }
                }

                if (timedOut) {
                    LOG.warn("SideeX: Page load timed out with 30000ms");
                    this.webdriver_pageWait_time = "";
                    this.force_continueTestWhenConditionIsTrue("pageWaitDone");
                } else {
                    // not timed out, keep checking
                    setTimeout(function() {
                        this.forceCommand("storeEval", "readyState = window.document.readyState;", "readyState");
                        this.force_continueTestWhenConditionIsTrue("pageWait");
                    }.bind(this), 500);
                }
            }
        } else if (option === "pageWaitDone") {
            this.forceCommand("storeEval", this.webdriver_ajaxWait_script, "ajax_done");
            this.force_continueTestWhenConditionIsTrue("ajaxWait");
        } else if (option === "ajaxWait") {
            this.forceCommand("echoToGetValue", "${ajax_done},ajax", "");
            this.force_continueTestWhenConditionIsTrue("checkAjaxWait");
        } else if (option === "checkAjaxWait") {
            if (LOG.sideex.ajax_done === "true") {
                this.webdriver_ajaxWait_time = "";
                this.continueTest();
            } else {
                var timedOut = false;
                if (!this.webdriver_ajaxWait_time) {
                    LOG.info("SideeX: Wait for all ajax requests to be done");
                    // initial record time
                    this.webdriver_ajaxWait_time = Date.now();
                } else {
                    // check timed out or not
                    if ((Date.now() - this.webdriver_ajaxWait_time) > 30000) {
                        timedOut = true;
                    }
                }

                if (timedOut) {
                    LOG.warn("SideeX: Ajax requests timed out with 30000ms");
                    this.webdriver_ajaxWait_time = "";
                    this.forceCommand("runScript", this.webdriver_ajaxClean_script, "");
                    this.force_continueTestWhenConditionIsTrue("ajaxClean");
                } else {
                    // not timed out, keep checking
                    setTimeout(function(){
                        this.forceCommand("storeEval", this.webdriver_ajaxWait_script, "ajax_done");
                        this.force_continueTestWhenConditionIsTrue("ajaxWait");
                    }.bind(this), 500);
                }
            }
        } else if (option === "ajaxClean") {
            this.continueTest();
        }
    },

    //AJAXWaitExt & PageLoadingWaitExt, Yu-Xian Chen, SELAB, CSIE, NCKU, 2016/10/18
    force_continueTestWhenConditionIsTrue: function(option) {
        try {
            if (this.waitForCondition == null) {
                LOG.debug("null condition; let's continueTest()");
                LOG.debug("Command complete");
                this.commandComplete(this.result);
                this.waitCheck(option);
            } else if (this.waitForCondition()) {
                LOG.debug("condition satisfied; let's continueTest()");
                this.waitForCondition = null;
                LOG.debug("Command complete");
                this.commandComplete(this.result);
                this.waitCheck(option);
            } else {
                setTimeout(function(){
                    if (option === "pageWait") {
                        this.force_continueTestWhenConditionIsTrue("pageWait");
                    } else if (option === "checkPageWait") {
                        this.force_continueTestWhenConditionIsTrue("checkPageWait");
                    } else if (option === "pageWaitDone") {
                        this.force_continueTestWhenConditionIsTrue("pageWaitDone");
                    } else if (option === "ajaxWait") {
                        this.force_continueTestWhenConditionIsTrue("ajaxWait");
                    } else if (option === "checkAjaxWait") {
                        this.force_continueTestWhenConditionIsTrue("checkAjaxWait");
                    } else if (option === "ajaxClean") {
                        this.force_continueTestWhenConditionIsTrue("ajaxClean");
                    }
                }.bind(this), 10);
            }
        } catch (e) {
            this.result = {};
            this.result.failed = true;
            this.result.failureMessage = extractExceptionMessage(e);
            this.commandComplete(this.result);
        }
    },

    pause : function() {},
    nextCommand : function() {},
    commandStarted : function() {},
    commandComplete : function() {},
    commandError : function() {},
    testComplete : function() {},

    getCommandInterval : function() {
        return 0;
    }

}
