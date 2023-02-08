package org.whirlplatform.editor.client.image;

import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.core.shared.FastMap;
import java.util.Map;
import org.whirlplatform.meta.shared.component.ComponentType;

public class ComponentIcon {

    @SuppressWarnings("serial")
    private static Map<String, ImageResource> binding = new FastMap<ImageResource>() {
        {
            put(ComponentType.TextFieldType.getType(),
                ComponentBundle.INSTANCE.textField());
            put(ComponentType.HtmlType.getType(),
                ComponentBundle.INSTANCE.html());
            put(ComponentType.ButtonType.getType(),
                ComponentBundle.INSTANCE.button());
            put(ComponentType.DateFieldType.getType(),
                ComponentBundle.INSTANCE.dateField());
            put(ComponentType.RadioType.getType(),
                ComponentBundle.INSTANCE.radio());
            put(ComponentType.CheckBoxType.getType(),
                ComponentBundle.INSTANCE.checkBox());
            put(ComponentType.ComboBoxType.getType(),
                ComponentBundle.INSTANCE.comboBox());
            put(ComponentType.TreePanelType.getType(),
                ComponentBundle.INSTANCE.treePanel());
            put(ComponentType.ReportType.getType(),
                ComponentBundle.INSTANCE.reportParam());
            put(ComponentType.WindowType.getType(),
                ComponentBundle.INSTANCE.window());
            put(ComponentType.CaptchaType.getType(),
                ComponentBundle.INSTANCE.captcha());
            put(ComponentType.TaskBarType.getType(),
                ComponentBundle.INSTANCE.taskBar());
            put(ComponentType.UploadFieldType.getType(),
                ComponentBundle.INSTANCE.uploadField());
            put(ComponentType.FrameType.getType(),
                ComponentBundle.INSTANCE.frame());
            put(ComponentType.LoginPanelType.getType(),
                ComponentBundle.INSTANCE.loginPanel());
            put(ComponentType.FormBuilderType.getType(),
                ComponentBundle.INSTANCE.form());
            put(ComponentType.FramedLoginPanelType.getType(),
                ComponentBundle.INSTANCE.framedLoginPanel());
            put(ComponentType.MultiComboBoxType.getType(),
                ComponentBundle.INSTANCE.multiCombobox());
            put(ComponentType.ImageType.getType(),
                ComponentBundle.INSTANCE.simpleImage());
            put(ComponentType.EditGridType.getType(),
                ComponentBundle.INSTANCE.editGrid());
            put(ComponentType.LabelType.getType(),
                ComponentBundle.INSTANCE.label());
            put(ComponentType.CheckGroupType.getType(),
                ComponentBundle.INSTANCE.checkGroup());
            put(ComponentType.RadioGroupType.getType(),
                ComponentBundle.INSTANCE.radioGroup());
            put(ComponentType.HtmlEditorType.getType(),
                ComponentBundle.INSTANCE.htmlEditor());
            put(ComponentType.NumberFieldType.getType(),
                ComponentBundle.INSTANCE.numberField());
            put(ComponentType.TextAreaType.getType(),
                ComponentBundle.INSTANCE.textArea());
            put(ComponentType.SimpleHtmlEditorType.getType(),
                ComponentBundle.INSTANCE.simpleHtmlEditor());
            put(ComponentType.TabItemType.getType(),
                ComponentBundle.INSTANCE.tabItem());
            put(ComponentType.TabPanelType.getType(),
                ComponentBundle.INSTANCE.tabPanel());
            put(ComponentType.BorderContainerType.getType(),
                ComponentBundle.INSTANCE.borderContainer());
            put(ComponentType.HorizontalContainerType.getType(),
                ComponentBundle.INSTANCE.horizontalContainer());
            put(ComponentType.VerticalContainerType.getType(),
                ComponentBundle.INSTANCE.verticalContainer());
            put(ComponentType.CenterContainerType.getType(),
                ComponentBundle.INSTANCE.centerContainer());
            put(ComponentType.ContentPanelType.getType(),
                ComponentBundle.INSTANCE.contentPanel());
            put(ComponentType.SimpleContainerType.getType(),
                ComponentBundle.INSTANCE.simpleContainer());
            put(ComponentType.TreeMenuType.getType(),
                ComponentBundle.INSTANCE.menuTree());
            put(ComponentType.HotKeyType.getType(),
                ComponentBundle.INSTANCE.hotKey());
            put(ComponentType.HorizontalMenuType.getType(),
                ComponentBundle.INSTANCE.horizontalMenu());
            put(ComponentType.TimerType.getType(),
                ComponentBundle.INSTANCE.timer());
            put(ComponentType.TreeComboBoxType.getType(),
                ComponentBundle.INSTANCE.treeComboBox());
            put(ComponentType.HorizontalMenuItemType.getType(),
                ComponentBundle.INSTANCE.menuItem());
            put(ComponentType.HBoxContainerType.getType(),
                ComponentBundle.INSTANCE.hBoxContainer());
            put(ComponentType.VBoxContainerType.getType(),
                ComponentBundle.INSTANCE.vBoxContainer());
            put(ComponentType.FieldSetType.getType(),
                ComponentBundle.INSTANCE.fieldSet());
            put(ComponentType.HorizontalMenuType.getType(),
                ComponentBundle.INSTANCE.hMenu());
            put(ComponentType.TreeMenuItemType.getType(),
                ComponentBundle.INSTANCE.treeMenuItem());
            put(ComponentType.PasswordFieldType.getType(),
                ComponentBundle.INSTANCE.passwordField());
        }
    };

    public ComponentIcon() {
    }

    public static ImageResource getIcon(String type) {
        return binding.get(type);
    }

}
