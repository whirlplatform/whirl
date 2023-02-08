/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.codemirror.client;

import com.google.common.base.Preconditions;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.TextAreaElement;
import com.google.gwt.user.client.ui.TextArea;

/**
 * This class wraps the CodeMirror Javascript object.
 *
 * @author Kristof Heirwegh
 * @since 3.1.1
 */
public class CodeMirrorWrapper {

    private static final NullAutoCompletionHandler NULL_AUTO_COMPLETION_HANDLER =
        new NullAutoCompletionHandler();

    private JavaScriptObject codeMirrorJs;
    private String mode;
    private AutoCompletionHandler autoCompletionHandler = NULL_AUTO_COMPLETION_HANDLER;

    /**
     * Create a new GWT object wrapping the given TextAreaElement.
     *
     * @param tae
     * @param config
     */
    public CodeMirrorWrapper(TextAreaElement tae, Config config) {
        codeMirrorJs = fromTextAreaJs(this, tae, config.asJSOject());
    }

    /**
     * Convenience method creating a new GWT object wrapping the TextAreaElement with given id.
     *
     * @param id
     * @param config
     * @return CodeMirrorWrapper
     */
    public static CodeMirrorWrapper fromTextArea(String id, Config config) {
        return fromTextArea(TextAreaElement.as(Document.get().getElementById(id)), config);
    }

    /**
     * Convenience method creating a new GWT object wrapping the given TextAreaElement.
     *
     * @param tae
     * @param config
     * @return CodeMirrorWrapper
     */
    public static CodeMirrorWrapper fromTextArea(TextAreaElement tae, Config config) {
        return new CodeMirrorWrapper(tae, config);
    }

    /**
     * Convenience method creating a new GWT object wrapping the given TextArea.
     *
     * @param ta
     * @param config
     * @return CodeMirrorWrapper
     */
    public static CodeMirrorWrapper fromTextArea(TextArea ta, Config config) {
        return fromTextArea(ta.getElement().<TextAreaElement>cast(), config);
    }

    // ---------------------------------------------------------------
    // CHECKSTYLE:OFF
    @SuppressWarnings("checkstyle:linelength")
    private static native JavaScriptObject fromTextAreaJs(CodeMirrorWrapper wrapper,
                                                          TextAreaElement tae,
                                                          JavaScriptObject config) /*-{
        var options = {
            extraKeys: {
                "Ctrl-Space": function (editor) {
                    $wnd.CodeMirror.showHint(editor, function (editor, callback) {
                        var result = [];
                        var cursor = editor.doc.getCursor();
                        var index = editor.indexFromPos(cursor);
                        $entry(wrapper.@org.geomajas.codemirror.client.CodeMirrorWrapper::getCompletions(Ljava/lang/String;IIILcom/google/gwt/core/client/JavaScriptObject;Lcom/google/gwt/core/client/JavaScriptObject;)(editor.getValue(), cursor.line, cursor.ch, index, result, callback));
                    }, {async: true});
                }
            }
        };
        for (var property in config) {
            if (config.hasOwnProperty(property)) {
                options[property] = config[property];
            }
        }
        var cm = $wnd.CodeMirror.fromTextArea(tae, options);
        return cm;
    }-*/;
    // CHECKSTYLE:ON

    private static native Object callFunction(JavaScriptObject jso, String name) /*-{
        return jso[name].apply(jso);
    }-*/;

    private static native Object callFunction(JavaScriptObject jso, String name, Object arg1) /*-{
        return jso[name].apply(jso, [arg1]);
    }-*/;

    private static native Object callFunction(JavaScriptObject jso, String name, Object arg1,
                                              Object arg2) /*-{
        return jso[name].apply(jso, [arg1, arg2]);
    }-*/;

    private static native boolean callBooleanFunction(JavaScriptObject jso, String name) /*-{
        return jso[name].apply(jso);
    }-*/;

    private static native String callStringFunction(JavaScriptObject jso, String name) /*-{
        return jso[name].apply(jso);
    }-*/;

    private static native int callIntFunction(JavaScriptObject jso, String name) /*-{
        return jso[name].apply(jso);
    }-*/;

    /**
     * Get the contents (= data) of this editor.
     *
     * @return data
     */
    public String getContent() {
        return callStringFunction(codeMirrorJs, "getValue");
    }

    /**
     * Set the contents (= dta) of this editor.
     *
     * @param content
     */
    public void setContent(String content) {
        callFunction(codeMirrorJs, "setValue", content);
    }

    /**
     * Call undo action on underlying editor.
     */
    public void undo() {
        callFunction(codeMirrorJs, "undo");
    }

    /**
     * Call redo action on underlying editor.
     */
    public void redo() {
        callFunction(codeMirrorJs, "redo");
    }

    /**
     * Call historySize function on underlying editor.
     *
     * @return historySize
     */
    public int getHistorySize() {
        return callIntFunction(codeMirrorJs, "historySize");
    }

    /**
     * Call clearHistory function on underlying editor.
     */
    public void clearHistory() {
        callFunction(codeMirrorJs, "clearHistory");
    }

    // ---------------------------------------------------------------

    /**
     * Call clearHistory function on underlying editor.
     */
    public void focus() {
        callFunction(codeMirrorJs, "focus");
    }

    /**
     * Call isClean (= !dirty) function on underlying editor.
     *
     * @return isClean
     */
    public boolean isClean() {
        return callBooleanFunction(codeMirrorJs, "isClean");
    }

    /**
     * Call markClean function on underlying editor.
     */
    public void markClean() {
        callFunction(codeMirrorJs, "markClean");
    }

    /**
     * Set an editor option.
     * <p>Take a look at the {@link Config} objects static strings for possible values.
     *
     * @param key
     * @param value
     */
    public void setOption(String key, Object value) {
        callFunction(codeMirrorJs, "setOption", key, value);
    }

    /**
     * Get an editor option. Take a look at the {@link Config} objects static strings for possible
     * values.
     *
     * @param key
     */
    public Object getOption(String key) {
        return callFunction(codeMirrorJs, "getOption", key);
    }

    /**
     * Returns the internal Codemirror JavascriptObject.
     *
     * @return Codemirror JavascriptObject
     */
    public JavaScriptObject asJSOject() {
        return codeMirrorJs;
    }

    public void refresh() {
        callFunction(codeMirrorJs, "refresh");
    }


    // ---------------------------------------------------------------

    /**
     * Called to retrive completions.
     *
     * @param editorText     The current editor text.
     * @param line           The line number that the caret is at (zero based).
     * @param ch             The character number that the caret is at (zero based).
     * @param completionList A JavaScriptObject that is an array and should be populated with lists
     *                       of completions. This can be done by calling
     *                       {@link #addElement(JavaScriptObject, JavaScriptObject)}.
     * @param callback
     */
    private void getCompletions(final String editorText, final int line, final int ch,
                                final int index, final JavaScriptObject completionList,
                                final JavaScriptObject callback) {
        autoCompletionHandler.getCompletions(editorText, new EditorPosition(line, ch), index,
            new AutoCompletionCallback() {
                public void completionsReady(AutoCompletionResult result) {
                    for (AutoCompletionChoice choice : result.getChoices()) {
                        addElement(completionList, createAutoCompletionResult(choice));
                    }
                    int fromLineNumber = result.getFromPosition().getLineNumber();
                    int fromColumnNumber = result.getFromPosition().getColumnNumber();
                    doAutoCompleteCallback(callback, completionList, fromLineNumber,
                        fromColumnNumber);
                }
            });
    }

    /**
     * Sets the {@link AutoCompletionHandler}.
     *
     * @param autoCompletionHandler The handler.  Not {@code null}.
     * @throws java.lang.NullPointerException if {@code autoCompletionHandler} is {@code null}.
     */
    public void setAutoCompletionHandler(AutoCompletionHandler autoCompletionHandler) {
        this.autoCompletionHandler = Preconditions.checkNotNull(autoCompletionHandler);
    }

    /**
     * Clears the previously set {@link AutoCompletionHandler}.
     */
    public void clearAutoCompletionHandler() {
        setAutoCompletionHandler(NULL_AUTO_COMPLETION_HANDLER);
    }

    /**
     * Adds a JavaScriptObject to a JavaScript array.
     *
     * @param jsListObject The JavaScriptObject that represents the array.
     * @param elementToAdd The JavaScriptObject that represents the element to be added.
     */
    private native void addElement(JavaScriptObject jsListObject, JavaScriptObject elementToAdd)/*-{
        jsListObject.push(elementToAdd);
    }-*/;

    private JavaScriptObject createAutoCompletionResult(AutoCompletionChoice choice) {
        JavaScriptObject from = choice.getReplaceTextFrom().toJavaScriptObject();
        JavaScriptObject to = choice.getReplaceTextTo().toJavaScriptObject();
        return createAutoCompletionResult(choice.getText(), choice.getDisplayText(),
            choice.getCssClassName(), from, to);
    }

    /**
     * Creates a JavaScriptObject that has the appropriate properties to describe the
     * auto-completion result.
     *
     * @param completeText        The text to insert.
     * @param completeDisplayText The text to display.
     * @param completeClassName   The CSS class name of the item in the list.
     * @return The JavaScriptObject that specified the given properties.
     */
    private native JavaScriptObject createAutoCompletionResult(String completeText,
                                                               String completeDisplayText,
                                                               String completeClassName,
                                                               JavaScriptObject completeFrom,
                                                               JavaScriptObject completeTo)/*-{
        return {
            text: completeText,
            displayText: completeDisplayText,
            className: completeClassName,
            from: completeFrom,
            to: completeTo
        }
    }-*/;

    /**
     * Calls the auto-complete callback with the specified argument.
     *
     * @param callbackFunction The actual function to call.
     * @param argument         The argument to pass to the function.
     * @param completeLine     The line of the completion (zero based index).
     * @param completeCh       The character index on the line of the completion (zero based
     *                         index).
     */
    private native void doAutoCompleteCallback(JavaScriptObject callbackFunction,
                                               JavaScriptObject argument, int completeLine,
                                               int completeCh)/*-{
        callbackFunction({
            list: argument,
            from: {line: completeLine, ch: completeCh}
        });
    }-*/;

}
