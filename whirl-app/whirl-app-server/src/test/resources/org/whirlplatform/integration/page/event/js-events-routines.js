window.JsTest = {};

window.JsTest.lockElementId = 'arquillian_whirl_js_event_lock';
window.JsTest.lockedCountAttributeName = 'data-lockcount';

//при выполнении события whirl добавляю в DOM элемент с идентификаторам arquillian_whirl_js_event_lock
window.JsTest.lock = function(){
	var lockElement = window.document.getElementById(window.JsTest.lockElementId); 
	if(!lockElement){
		lockElement = window.document.createElement('div');
		lockElement.id = window.JsTest.lockElementId;
		lockElement.setAttribute(window.JsTest.lockedCountAttributeName, 1);
		window.document.body.appendChild(lockElement);
	} else{
		var lockedCount = lockElement.getAttribute(window.JsTest.lockedCountAttributeName);
		var incLockedCount = parseInt(lockedCount) + 1;
		lockElement.setAttribute(window.JsTest.lockedCountAttributeName, incLockedCount);
	}	
}


//при завершении выполнения события(в callback-ах success либо fail  удаляю элемент с идентификатором arquillian_whirl_js_event_lock из DOM.
//затем при помощи Graphene можно будет проверить его наличие и определить что никаких событий javascript сейчас не выполняется
//PS:
//полагаю, это можно как-то проверить при помощи waitAjax, но у меня не вышло.. поэтому сделал так.
window.JsTest.unlock = function(){
	var lockElement = window.document.getElementById(window.JsTest.lockElementId); 
	if(lockElement){
		var lockedCount = lockElement.getAttribute(window.JsTest.lockedCountAttributeName);
		var iLockedCount = parseInt(lockedCount);
		if(iLockedCount > 1){
			decLockedCount = iLockedCount - 1;
			lockElement.setAttribute(window.JsTest.lockedCountAttributeName, decLockedCount);
		} else{
			window.document.body.removeChild(lockElement);
		}
	}
}


window.JsTest.executeEvent = function(whirlGlobalEventCode, paramValue, paramType) {
	 window.JsTest.lock(); //устанавливаю блокировку
	 var params = new Array();
	 
	 var data = new Whirl.DataValue(paramType); //DataValue
	 
	 if(paramType == 'DATE'){
		 value = new Date(paramValue);
	 } else if(paramType == 'LIST'){
		 var listValue = new Whirl.RowListValue();
		 for(var i = 0; i < paramValue.length; i++){
			 var val = paramValue[i];
			 var rowValue = new Whirl.RowValue(val);
			 listValue.addRowValue(rowValue);
		 }
		 value = listValue;
	 } else {
		 value = paramValue;
	 } 
	 
	 data.setValue(value);
	 var param = new Whirl.EventParameter("DATAVALUE");
	 param.setData(data);
	 
	 params.push(param);
	 // Функция выполняется при успешном вызове
	 var successFunction = function(c) { //Whirl.EventCallbackResult
	   window.JsTest.unlock();
	   console.log(c);
	 }
	 // Функция выполняется при ошибке
	 var failFunction = function(err) {
	  window.JsTest.unlock();
	  alert("Fail: \n" + err)
	 }
	 // Вызов события
	 Whirl.Events.execute(whirlGlobalEventCode, params, successFunction, failFunction);
	 console.log("JsTest.executeEvent...");
};


//получаю значения мультикомбобокса  при помощи Whirl API
window.JsTest.getMultiComboBoxValues = function(multiComboBoxCode){
	var mcb = Whirl.Builders.findBuilderByCode(multiComboBoxCode);
	var vls = mcb.getFieldValue();
	var values = vls.getValue();
	var jsonBuf = [];
	for(var k in values){
	  var value = values[k]; 
	  jsonBuf[k] = value.getId();
	}
	return JSON.stringify(jsonBuf);
}