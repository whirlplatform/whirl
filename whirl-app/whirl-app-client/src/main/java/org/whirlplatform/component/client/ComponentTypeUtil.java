package org.whirlplatform.component.client;

import com.sencha.gxt.widget.core.client.box.MessageBox;
import java.util.Collections;
import java.util.Map;
import org.whirlplatform.component.client.base.BorderContainerBuilder;
import org.whirlplatform.component.client.base.ButtonBuilder;
import org.whirlplatform.component.client.base.CaptchaBuilder;
import org.whirlplatform.component.client.base.CenterContainerBuilder;
import org.whirlplatform.component.client.base.ContentPanelBuilder;
import org.whirlplatform.component.client.base.ContextMenuItemBuilder;
import org.whirlplatform.component.client.base.FieldSetBuilder;
import org.whirlplatform.component.client.base.FrameBuilder;
import org.whirlplatform.component.client.base.FramedLoginPanelBuilder;
import org.whirlplatform.component.client.base.HBoxContainerBuilder;
import org.whirlplatform.component.client.base.HorizontalContainerBuilder;
import org.whirlplatform.component.client.base.HtmlBuilder;
import org.whirlplatform.component.client.base.HtmlEditorBuilder;
import org.whirlplatform.component.client.base.ImageBuilder;
import org.whirlplatform.component.client.base.LabelBuilder;
import org.whirlplatform.component.client.base.LoginPanelBuilder;
import org.whirlplatform.component.client.base.NumberFieldBuilder;
import org.whirlplatform.component.client.base.PasswordFieldBuilder;
import org.whirlplatform.component.client.base.SimpleContainerBuilder;
import org.whirlplatform.component.client.base.SimpleHtmlEditorBuilder;
import org.whirlplatform.component.client.base.TabItemBuilder;
import org.whirlplatform.component.client.base.TabPanelBuilder;
import org.whirlplatform.component.client.base.TextAreaBuilder;
import org.whirlplatform.component.client.base.TextFieldBuilder;
import org.whirlplatform.component.client.base.TimerBuilder;
import org.whirlplatform.component.client.base.UploadFieldBuilder;
import org.whirlplatform.component.client.base.VBoxContainerBuilder;
import org.whirlplatform.component.client.base.VerticalContainerBuilder;
import org.whirlplatform.component.client.check.CheckBoxBuilder;
import org.whirlplatform.component.client.check.CheckGroupBuilder;
import org.whirlplatform.component.client.check.RadioBuilder;
import org.whirlplatform.component.client.check.RadioGroupBuilder;
import org.whirlplatform.component.client.combo.ComboBoxBuilder;
import org.whirlplatform.component.client.combo.MultiComboBoxBuilder;
import org.whirlplatform.component.client.combo.TreeComboBoxBuilder;
import org.whirlplatform.component.client.date.DateFieldBuilder;
import org.whirlplatform.component.client.form.FormBuilder;
import org.whirlplatform.component.client.grid.EditGridBuilder;
import org.whirlplatform.component.client.hotkey.HotKeyBuilder;
import org.whirlplatform.component.client.report.ReportBuilder;
import org.whirlplatform.component.client.tree.HorizontalMenuBuilder;
import org.whirlplatform.component.client.tree.HorizontalMenuItemBuilder;
import org.whirlplatform.component.client.tree.TreeBuilder;
import org.whirlplatform.component.client.tree.TreeMenuBuilder;
import org.whirlplatform.component.client.window.TaskBarBuilder;
import org.whirlplatform.component.client.window.WindowBuilder;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.i18n.AppMessage;

public class ComponentTypeUtil {

    public static ComponentBuilder findBuilder(final ComponentType type) {
        return findBuilder(type, Collections.emptyMap());
    }

    public static ComponentBuilder findBuilder(final String type,
                                               Map<String, DataValue> builderProperties) {
        if (type == null) {
            return null;
        }
        return findBuilder(ComponentType.parse(type), builderProperties);
    }

    public static ComponentBuilder findBuilder(final ComponentType type,
                                               Map<String, DataValue> builderProperties) {
        ComponentBuilder builder = null;
        if (type != null) {
            if (ComponentType.BorderContainerType == type) {
                builder = new BorderContainerBuilder(builderProperties);
            } else if (ComponentType.HorizontalContainerType == type) {
                builder = new HorizontalContainerBuilder(builderProperties);
            } else if (ComponentType.VerticalContainerType == type) {
                builder = new VerticalContainerBuilder(builderProperties);
            } else if (ComponentType.HBoxContainerType == type) {
                builder = new HBoxContainerBuilder(builderProperties);
            } else if (ComponentType.VBoxContainerType == type) {
                builder = new VBoxContainerBuilder(builderProperties);
            } else if (ComponentType.CenterContainerType == type) {
                builder = new CenterContainerBuilder(builderProperties);
            } else if (ComponentType.ContentPanelType == type) {
                builder = new ContentPanelBuilder(builderProperties);
            } else if (ComponentType.TabPanelType == type) {
                builder = new TabPanelBuilder(builderProperties);
            } else if (ComponentType.TabItemType == type) {
                builder = new TabItemBuilder(builderProperties);
            } else if (ComponentType.ButtonType == type) {
                builder = new ButtonBuilder(builderProperties);
            } else if (ComponentType.HtmlType == type) {
                builder = new HtmlBuilder(builderProperties);
            } else if (ComponentType.SimpleHtmlEditorType == type) {
                builder = new SimpleHtmlEditorBuilder(builderProperties);
            } else if (ComponentType.TextFieldType == type) {
                builder = new TextFieldBuilder(builderProperties);
            } else if (ComponentType.PasswordFieldType == type) {
                builder = new PasswordFieldBuilder(builderProperties);
            } else if (ComponentType.TextAreaType == type) {
                builder = new TextAreaBuilder(builderProperties);
            } else if (ComponentType.NumberFieldType == type) {
                builder = new NumberFieldBuilder(builderProperties);
            } else if (ComponentType.ImageType == type) {
                builder = new ImageBuilder(builderProperties);
            } else if (ComponentType.SimpleContainerType == type) {
                builder = new SimpleContainerBuilder(builderProperties);
            } else if (ComponentType.EditGridType == type) {
                builder = new EditGridBuilder(builderProperties);
            } else if (ComponentType.DateFieldType == type) {
                builder = new DateFieldBuilder(builderProperties);
            } else if (ComponentType.LabelType == type) {
                builder = new LabelBuilder(builderProperties);
            } else if (ComponentType.RadioType == type) {
                builder = new RadioBuilder(builderProperties);
            } else if (ComponentType.RadioGroupType == type) {
                builder = new RadioGroupBuilder(builderProperties);
            } else if (ComponentType.CheckBoxType == type) {
                builder = new CheckBoxBuilder(builderProperties);
            } else if (ComponentType.CheckGroupType == type) {
                builder = new CheckGroupBuilder(builderProperties);
            } else if (ComponentType.HtmlEditorType == type) {
                builder = new HtmlEditorBuilder(builderProperties);
            } else if (ComponentType.ComboBoxType == type) {
                builder = new ComboBoxBuilder(builderProperties);
            } else if (ComponentType.FormBuilderType == type) {
                builder = new FormBuilder(builderProperties);
            } else if (ComponentType.MultiComboBoxType == type) {
                builder = new MultiComboBoxBuilder(builderProperties);
            } else if (ComponentType.TreePanelType == type) {
                builder = new TreeBuilder(builderProperties);
            } else if (ComponentType.LoginPanelType == type) {
                builder = new LoginPanelBuilder(builderProperties);
            } else if (ComponentType.FramedLoginPanelType == type) {
                builder = new FramedLoginPanelBuilder(builderProperties);
            } else if (ComponentType.FrameType == type) {
                builder = new FrameBuilder(builderProperties);
            } else if (ComponentType.UploadFieldType == type) {
                builder = new UploadFieldBuilder(builderProperties);
            } else if (ComponentType.RadioType == type) {
                builder = new RadioBuilder(builderProperties);
            } else if (ComponentType.WindowType == type) {
                builder = new WindowBuilder(builderProperties);
            } else if (ComponentType.TaskBarType == type) {
                builder = new TaskBarBuilder(builderProperties);
            } else if (ComponentType.CaptchaType == type) {
                builder = new CaptchaBuilder(builderProperties);
            } else if (ComponentType.ReportType == type) {
                builder = new ReportBuilder(builderProperties);
            } else if (ComponentType.TreeMenuType == type) {
                builder = new TreeMenuBuilder(builderProperties);
            } else if (ComponentType.HorizontalMenuType == type) {
                builder = new HorizontalMenuBuilder(builderProperties);
            } else if (ComponentType.HotKeyType == type) {
                builder = new HotKeyBuilder(builderProperties);
            } else if (ComponentType.TreeComboBoxType == type) {
                builder = new TreeComboBoxBuilder(builderProperties);
            } else if (ComponentType.TimerType == type) {
                builder = new TimerBuilder(builderProperties);
            } else if (ComponentType.HorizontalMenuItemType == type) {
                builder = new HorizontalMenuItemBuilder(builderProperties);
            } else if (ComponentType.FieldSetType == type) {
                builder = new FieldSetBuilder(builderProperties);
            } else if (ComponentType.ContextMenuItemType == type) {
                builder = new ContextMenuItemBuilder(builderProperties);
            }
            return builder;
        }
        MessageBox message = new MessageBox(AppMessage.Util.MESSAGE.error(),
            AppMessage.Util.MESSAGE.alert_noCompBuilder());
        message.setIcon(MessageBox.ICONS.warning());
        return null;
    }
}
