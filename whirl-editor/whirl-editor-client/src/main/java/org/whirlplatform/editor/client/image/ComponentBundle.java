package org.whirlplatform.editor.client.image;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface ComponentBundle extends ClientBundle {

    ComponentBundle INSTANCE = GWT.create(ComponentBundle.class);

    @Source("database.png")
    ImageResource helpDB();

    @Source("help.png")
    ImageResource helpJS();

    @Source("icon_selection.png")
    ImageResource iconSelection();

    @Source("textfield.png")
    ImageResource textField();

    @Source("html.png")
    ImageResource html();

    @Source("button.png")
    ImageResource button();

    @Source("datefield.png")
    ImageResource dateField();

    @Source("radio.png")
    ImageResource radio();

    @Source("checkbox.png")
    ImageResource checkBox();

    @Source("combobox.png")
    ImageResource comboBox();

    @Source("treepanel.png")
    ImageResource treePanel();

    @Source("reportparam.png")
    ImageResource reportParam();

    @Source("window.png")
    ImageResource window();

    @Source("captcha.png")
    ImageResource captcha();

    @Source("taskbar.png")
    ImageResource taskBar();

    @Source("uploadfield.png")
    ImageResource uploadField();

    @Source("frame.png")
    ImageResource frame();

    @Source("loginpanel.png")
    ImageResource loginPanel();

    @Source("loginpanel.png")
    ImageResource framedLoginPanel();

    @Source("form.png")
    ImageResource form();

    @Source("multicombobox.png")
    ImageResource multiCombobox();

    @Source("simpleimage.png")
    ImageResource simpleImage();

    @Source("editgrid.png")
    ImageResource editGrid();

    @Source("label.png")
    ImageResource label();

    @Source("checkgroup.png")
    ImageResource checkGroup();

    @Source("radiogroup.png")
    ImageResource radioGroup();

    @Source("htmleditor.png")
    ImageResource htmlEditor();

    @Source("numberfield.png")
    ImageResource numberField();

    @Source("textarea.png")
    ImageResource textArea();

    @Source("simplehtmleditor.png")
    ImageResource simpleHtmlEditor();

    @Source("tabitem.png")
    ImageResource tabItem();

    @Source("tabpanel.png")
    ImageResource tabPanel();

    @Source("bordercontainer.png")
    ImageResource borderContainer();

    @Source("horizontalcontainer.png")
    ImageResource horizontalContainer();

    @Source("verticalcontainer.png")
    ImageResource verticalContainer();

    @Source("centercontainer.png")
    ImageResource centerContainer();

    @Source("contentpanel.png")
    ImageResource contentPanel();

    @Source("simplecontainer.png")
    ImageResource simpleContainer();

    @Source("keyboard.png")
    ImageResource hotKey();

    @Source("view_tree.png")
    ImageResource menuTree();

    @Source("tree-menu.png")
    ImageResource horizontalMenu();

    @Source("alarm.png")
    ImageResource timer();

    @Source("treecombobox.png")
    ImageResource treeComboBox();

    @Source("menu-item.png")
    ImageResource menuItem();

    @Source("hboxcontainer.png")
    ImageResource hBoxContainer();

    @Source("vboxcontainer.png")
    ImageResource vBoxContainer();

    @Source("field-set.png")
    ImageResource fieldSet();

    @Source("hmenu.png")
    ImageResource hMenu();

    @Source("tree-menu-item.png")
    ImageResource treeMenuItem();

    @Source("hint-icon.png")
    ImageResource hintIcon();

    @Source("user-small.png")
    ImageResource userSmall();

    @Source("context.png")
    ImageResource context();

    @Source("plus.png")
    ImageResource plus();

    @Source("cross.png")
    ImageResource cross();

    @Source("access.png")
    ImageResource access();

    @Source("rename.png")
    ImageResource rename();

    @Source("copy.png")
    ImageResource copy();

    @Source("cut.png")
    ImageResource cut();

    @Source("paste.png")
    ImageResource paste();

    @Source("compare.png")
    ImageResource compare();

    @Source("load.png")
    ImageResource load();

    @Source("run.png")
    ImageResource run();

    @Source("merge.png")
    ImageResource merge();

    @Source("commit.png")
    ImageResource commit();

    @Source("package.png")
    ImageResource pack();

    @Source("new_app.png")
    ImageResource newApp();

    @Source("help_api.png")
    ImageResource helpApi();

    @Source("hint.png")
    ImageResource hint();

    @Source("save_as.png")
    ImageResource saveAs();

    @Source("save.png")
    ImageResource save();

    @Source("save_xml.png")
    ImageResource saveXml();

    @Source("open.png")
    ImageResource open();

    @Source("open_xml.png")
    ImageResource openXml();

    @Source("expand.png")
    ImageResource expand();

    @Source("collapse.png")
    ImageResource collapse();

    @Source("ok.png")
    ImageResource ok();

    @Source("cancel.png")
    ImageResource cancel();

    @Source("password-field.png")
    ImageResource passwordField();
}
