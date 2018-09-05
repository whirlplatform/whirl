package org.whirlplatform.editor.client.image;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundleWithLookup;
import com.google.gwt.resources.client.ImageResource;

public interface EditorBundle extends ClientBundleWithLookup {

    EditorBundle INSTANCE = GWT.create(EditorBundle.class);

	@Source("application.png")
	ImageResource application();

	@Source("category.png")
	ImageResource category();

	@Source("node.png")
	ImageResource node();

	@Source("gear.png")
	ImageResource gear();

	@Source("brick.png")
	ImageResource brick();

	@Source("bricks.png")
	ImageResource bricks();

	@Source("application_form.png")
	ImageResource applicationForm();

	@Source("application_tile_horizontal.png")
	ImageResource applicationHorizontal();

	@Source("application_tile_vertical.png")
	ImageResource applicationVertical();

	@Source("table.png")
	ImageResource table();

	@Source("arrow-join.png")
	ImageResource union();

	@Source("arrow-split.png")
	ImageResource split();

	@Source("border-top.png")
	ImageResource borderTop();

	@Source("border-right.png")
	ImageResource borderRight();

	@Source("border-bottom.png")
	ImageResource borderBottom();

	@Source("border-left.png")
	ImageResource borderLeft();

	@Source("cross.png")
	ImageResource cross();

	@Source("color.png")
	ImageResource color();

	@Source("green-toggle.png")
	ImageResource greenToggle();

	@Source("red-toggle.png")
	ImageResource redToggle();

	@Source("insert-row.png")
	ImageResource insertRow();

	@Source("insert-column.png")
	ImageResource insertColumn();

	@Source("delete-row.png")
	ImageResource deleteRow();

	@Source("delete-column.png")
	ImageResource deleteColumn();

	@Source("node-select-all.png")
	ImageResource nodeBlue();

	@Source("puzzle.png")
	ImageResource puzzle();

	@Source("arrow-circle-double.png")
	ImageResource arrowCircle();

	@Source("notification-counter-01.png")
	ImageResource counter1();

	@Source("notification-counter-02.png")
	ImageResource counter2();

	@Source("notification-counter-03.png")
	ImageResource counter3();

	@Source("notification-counter-04.png")
	ImageResource counter4();

	@Source("notification-counter-05.png")
	ImageResource counter5();

	@Source("notification-counter-06.png")
	ImageResource counter6();

	@Source("notification-counter-07.png")
	ImageResource counter7();

	@Source("notification-counter-08.png")
	ImageResource counter8();

	@Source("notification-counter-09.png")
	ImageResource counter9();

	@Source("notification-counter-10.png")
	ImageResource counter10();

	@Source("notification-counter-11.png")
	ImageResource counter11();

	@Source("notification-counter-12.png")
	ImageResource counter12();

	@Source("notification-counter-13.png")
	ImageResource counter13();

	@Source("notification-counter-14.png")
	ImageResource counter14();

	@Source("notification-counter-15.png")
	ImageResource counter15();

	@Source("notification-counter-15-plus.png")
	ImageResource counterMany();

	@Source("status.png")
	ImageResource status();

	@Source("status-busy.png")
	ImageResource statusBusy();

	@Source("locales.png")
	ImageResource locales();

	@Source("databases.png")
	ImageResource databases();

	@Source("database.png")
	ImageResource database();

	@Source("folders-stack.png")
	ImageResource folders_stack();

	@Source("folder.png")
	ImageResource folder();

	@Source("tables-stacks.png")
	ImageResource tables_stacks();

	@Source("key.png")
	ImageResource key();

	@Source("users.png")
	ImageResource users();

	@Source("applications-stack.png")
	ImageResource applications_stack();

	@Source("prohibition.png")
	ImageResource prohibition();

	@Source("alarm-small.png")
	ImageResource timerSmall();

	@Source("keyboard-small.png")
	ImageResource hotKeySmall();

	@Source("minus.png")
	ImageResource minus();

	@Source("plus.png")
	ImageResource plus();

	@Source("clear-color.png")
	ImageResource clearColor();

	@Source("bordercontainer-small.png")
	ImageResource borderContainerSmall();

	@Source("centercontainer-small.png")
	ImageResource centerContainerSmall();

	@Source("simplecontainer-small.png")
	ImageResource simpleContainerSmall();

	@Source("vboxcontainer-small.png")
	ImageResource vBoxContainerSmall();

	@Source("hboxcontainer-small.png")
	ImageResource hBoxContainerSmall();

	@Source("contentpanel-small.png")
	ImageResource contentPanelSmall();

	@Source("tab-content.png")
	ImageResource tabPanelSmall();

	@Source("tab.png")
	ImageResource tabItemSmall();

	@Source("button-small.png")
	ImageResource buttonSmall();

	@Source("html-small.png")
	ImageResource htmlSmall();

	@Source("gxthtmleditor-small.png")
	ImageResource gxtHtmlEditorSmall();

	@Source("text-field.png")
	ImageResource textFieldSmall();

	@Source("text-area.png")
	ImageResource textAreaSmall();

	@Source("text-field-number.png")
	ImageResource numberFieldSmall();

	@Source("image.png")
	ImageResource simpleImageSmall();

	@Source("datefield-small.png")
	ImageResource dateFieldSmall();

	@Source("label-small.png")
	ImageResource labelSmall();

	@Source("radio-buttons-list.png")
	ImageResource radioGroupSmall();

	@Source("check-boxes-list.png")
	ImageResource checkGroupSmall();

	@Source("htmleditor-small.png")
	ImageResource htmlEditorSmall();

	@Source("combobox-small.png")
	ImageResource comboBoxSmall();

	@Source("multicombobox-small.png")
	ImageResource multiComboBoxSmall();

	@Source("application_side_tree.png")
	ImageResource treePanelSmall();

	@Source("application_form_key.png")
	ImageResource applicationFormKey();

	@Source("uploadfield-small.png")
	ImageResource uploadFieldSmall();

	@Source("radio-button.png")
	ImageResource radioSmall();

	@Source("check-box.png")
	ImageResource checkBoxSmall();

	@Source("window-small.png")
	ImageResource windowSmall();

	@Source("taskbar-small.png")
	ImageResource taskBarSmall();

	@Source("captcha-small.png")
	ImageResource captchaSmall();

	@Source("report.png")
	ImageResource report();

	@Source("view_tree-small.png")
	ImageResource treeMenuSmall();

	@Source("tree-menu-small.png")
	ImageResource horizontalMenuSmall();

	@Source("treecombobox-small.png")
	ImageResource treeComboBoxSmall();

	@Source("menu-item-small.png")
	ImageResource menuItemSmall();
	
	@Source("field-set-small.png")
	ImageResource fieldSetSmall();
	
	@Source("tree-menu-item-small.png")
	ImageResource treeMenuItemSmall();
	
	@Source("bricks-hidden.png")
	ImageResource bricksHidden();
	
	@Source("ui-menu.png")
	ImageResource contextMenu();
	
	@Source("ui-menu-item.png")
	ImageResource contextMenuItem();
	
	@Source("templates.png")
	ImageResource templates();
	
	@Source("template.png")
	ImageResource template();
	
	@Source("palette.png")
	ImageResource palette();
	
	@Source("gear_blue.png")
	ImageResource gear_blue();
	
	@Source("events.png")
	ImageResource events();
	
	@Source("chart-small.png")
	ImageResource chartSmall();
}
