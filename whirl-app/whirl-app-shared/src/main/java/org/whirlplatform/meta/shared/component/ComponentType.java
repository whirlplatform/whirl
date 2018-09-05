package org.whirlplatform.meta.shared.component;

import java.io.Serializable;
import java.util.Set;

/**
 * Билдеры компонентов
 */
public enum ComponentType implements Serializable {

    // @formatter:off
    BorderContainerType("BorderContainerBuilder", true),
    HorizontalContainerType("HorizontalContainerBuilder", true),
    VerticalContainerType("VerticalContainerBuilder", true),
    CenterContainerType("CenterContainerBuilder", true),
    SimpleContainerType("SimpleContainerBuilder", true),
    VBoxContainerType("VBoxContainerBuilder", true),
    HBoxContainerType("HBoxContainerBuilder", true),
    ContentPanelType("ContentPanelBuilder", true),
    TabPanelType("TabPanelBuilder", true),
    TabItemType("TabItemBuilder", true),
    FormBuilderType("FormBuilder", true),
    ButtonType("ButtonBuilder"),
    HtmlType("HTMLBuilder"),
    GXTHtmlEditorType("GXTHtmlEditorBuilder"),
    TextFieldType("TextFieldBuilder"),
    TextAreaType("TextAreaBuilder"),
    PasswordFieldType("PasswordFieldBuilder"),
    NumberFieldType("NumberFieldBuilder"),
    ImageType("ImageBuilder"),
    EditGridType("EditGridBuilder"),
    DateFieldType("DateFieldBuilder"),
    LabelType("LabelBuilder"),
    RadioGroupType("RadioGroupBuilder"),
    CheckGroupType("CheckGroupBuilder"),
    HtmlEditorType("HtmlEditorBuilder"),
    ComboBoxType("ComboBoxBuilder"),
    MultiComboBoxType("MultiComboBoxBuilder"),
    TreePanelType("TreeBuilder"),
    LoginPanelType("LoginPanelBuilder"),
    FramedLoginPanelType("FramedLoginPanelBuilder"),
    FrameType("FrameBuilder"),
    UploadFieldType("UploadFieldBuilder"),
    RadioType("RadioBuilder"),
    CheckBoxType("CheckBoxBuilder"),
    WindowType("WindowBuilder", true),
    TaskBarType("TaskBarBuilder"),
    CaptchaType("CaptchaBuilder"),
    ReportType("ReportBuilder"),
    TreeMenuType("TreeMenuBuilder", true),
    TreeMenuItemType("TreeMenuItemBuilder", true),
    HorizontalMenuType("HorizontalMenuBuilder", true),
    HorizontalMenuItemType("HorizontalMenuItemBuilder", true),
    HotKeyType("HotKeyBuilder"),
    TreeComboBoxType("TreeComboBoxBuilder"),
    TimerType("TimerBuilder"),
    FieldSetType("FieldSetBuilder", true),
    ContextMenuItemType("ContextMenuItemBuilder");
    // @formatter:on

    private String type;
    private boolean container;

    ComponentType(String type, boolean container) {
        this.type = type;
        this.container = container;
    }

    ComponentType(String type) {
        this(type, false);
    }

    public String getType() {
        return type;
    }

    public boolean isContainer() {
        return container;
    }

    @Override
    public String toString() {
        return type;
    }

    public static ComponentType parse(String type) {
        for (ComponentType t : ComponentType.values()) {
            if (t.getType().equals(type)) {
                return t;
            }
        }
        return null;
    }

    public Set<PropertyType> getProperties() {
        return ComponentProperties.getProperties(this);
    }

}
