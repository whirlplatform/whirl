package org.whirlplatform.editor.client.tree.visitor;

import com.google.gwt.resources.client.ImageResource;
import org.whirlplatform.editor.client.image.EditorBundle;
import org.whirlplatform.editor.client.tree.AppTreeIconProvider.AppTreeElementIconVisitContext;
import org.whirlplatform.editor.client.tree.dummy.*;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.editor.*;
import org.whirlplatform.meta.shared.editor.db.*;

public class AppTreeElementIconSetter implements TreeElementVisitor<AppTreeElementIconVisitContext> {
    private final EditorBundle icon;

    public AppTreeElementIconSetter() {
        icon = EditorBundle.INSTANCE;
    }

    @Override
    public void visit(AppTreeElementIconVisitContext ctx, AbstractElement element) {
        ctx.setIcon(icon.brick());
    }

    @Override
    public void visit(AppTreeElementIconVisitContext ctx, ApplicationElement element) {
        ctx.setIcon((element.isAvailable()) ? icon.application() : icon.prohibition());
    }

    @Override
    public void visit(AppTreeElementIconVisitContext ctx, CellElement element) {
        ctx.setIcon(icon.brick());
    }

    @Override
    public void visit(AppTreeElementIconVisitContext ctx, CellRangeElement element) {
        ctx.setIcon(icon.brick());
    }

    @Override
    public void visit(AppTreeElementIconVisitContext ctx, ColumnElement element) {
        ctx.setIcon(icon.brick());
    }

    @Override
    public void visit(AppTreeElementIconVisitContext ctx, ComponentElement element) {
        ctx.setIcon(getComponentElementIcon(element));
    }

    @Override
    public void visit(AppTreeElementIconVisitContext ctx, EventElement element) {
        ctx.setIcon(icon.arrowCircle());
    }

    @Override
    public void visit(AppTreeElementIconVisitContext ctx, EventParameterElement element) {
        int num = element.getIndex() + 1;
        if (num <= 15) {
            ctx.setIcon((ImageResource) icon.getResource("counter" + num));
        } else {
            ctx.setIcon(icon.counterMany());
        }
    }

    @Override
    public void visit(AppTreeElementIconVisitContext ctx, FileElement element) {
        ctx.setIcon(icon.brick());
    }

    @Override
    public void visit(AppTreeElementIconVisitContext ctx, FormElement element) {
        ctx.setIcon(icon.applicationForm());
    }

    @Override
    public void visit(AppTreeElementIconVisitContext ctx, GroupElement element) {
        ctx.setIcon(icon.users());
    }

    @Override
    public void visit(AppTreeElementIconVisitContext ctx, ReportElement element) {
        ctx.setIcon(icon.report());
    }

    @Override
    public void visit(AppTreeElementIconVisitContext ctx, RequestElement element) {
        ctx.setIcon(icon.brick());
    }

    @Override
    public void visit(AppTreeElementIconVisitContext ctx, RightCollectionElement element) {
        ctx.setIcon(icon.brick());
    }

    @Override
    public void visit(AppTreeElementIconVisitContext ctx, RowElement element) {
        ctx.setIcon(icon.brick());
    }

    @Override
    public void visit(AppTreeElementIconVisitContext ctx, DataSourceElement element) {
        ctx.setIcon(icon.database());
    }

    @Override
    public void visit(AppTreeElementIconVisitContext ctx, SchemaElement element) {
        ctx.setIcon(icon.folder());
    }

    @Override
    public void visit(AppTreeElementIconVisitContext ctx, TableColumnElement element) {
        ctx.setIcon(icon.brick());
    }

    @Override
    public void visit(AppTreeElementIconVisitContext ctx, AbstractTableElement element) {
        ctx.setIcon(icon.table());
    }

    @Override
    public void visit(AppTreeElementIconVisitContext ctx, ViewElement element) {
        ctx.setIcon(icon.brick());
    }

    @Override
    public void visit(AppTreeElementIconVisitContext ctx, DatabaseTableElement element) {
        ctx.setIcon(icon.table());
    }

    @Override
    public void visit(AppTreeElementIconVisitContext ctx, PlainTableElement element) {
        ctx.setIcon(icon.table());
    }

    @Override
    public void visit(AppTreeElementIconVisitContext ctx, DynamicTableElement element) {
        ctx.setIcon(icon.table());
    }

    @Override
    public void visit(AppTreeElementIconVisitContext ctx, ContextMenuItemElement element) {
        ctx.setIcon(icon.contextMenuItem());
    }

    @Override
    public void visit(AppTreeElementIconVisitContext ctx, AbstractDummyElement element) {
        ctx.setIcon(icon.brick());
    }

    @Override
    public void visit(AppTreeElementIconVisitContext ctx, DummyAppLocales element) {
        ctx.setIcon(icon.locales());
    }

    @Override
    public void visit(AppTreeElementIconVisitContext ctx, DummyAppComponents element) {
        ctx.setIcon(icon.node());
    }

    @Override
    public void visit(AppTreeElementIconVisitContext ctx, DummyAppFreeComponents element) {
        ctx.setIcon(icon.category());
    }

    @Override
    public void visit(AppTreeElementIconVisitContext ctx, DummyAppEvents element) {
        ctx.setIcon(icon.gear());
    }

    @Override
    public void visit(AppTreeElementIconVisitContext ctx, DummyAppDataSources element) {
        ctx.setIcon(icon.databases());
    }

    @Override
    public void visit(AppTreeElementIconVisitContext ctx, DummyAppGroups element) {
        ctx.setIcon(icon.users());
    }

    @Override
    public void visit(AppTreeElementIconVisitContext ctx, DummyAppReferences element) {
        ctx.setIcon(icon.applications_stack());
    }

    @Override
    public void visit(AppTreeElementIconVisitContext ctx, DummyComponentEvents element) {
        ctx.setIcon(icon.gear());
    }

    @Override
    public void visit(AppTreeElementIconVisitContext ctx, DummyMenuItems element) {
        ctx.setIcon(icon.contextMenu());
    }

    @Override
    public void visit(AppTreeElementIconVisitContext ctx, DummyMenuItemEvents element) {
        ctx.setIcon(icon.gear());
    }

    @Override
    public void visit(AppTreeElementIconVisitContext ctx, DummySchemas element) {
        ctx.setIcon(icon.folders_stack());
    }

    @Override
    public void visit(AppTreeElementIconVisitContext ctx, DummyEventParameters element) {
        ctx.setIcon(icon.puzzle());
    }

    @Override
    public void visit(AppTreeElementIconVisitContext ctx, DummyEventSubEvents element) {
        ctx.setIcon(icon.nodeBlue());
    }

    @Override
    public void visit(AppTreeElementIconVisitContext ctx, DummyPlainTables element) {
        ctx.setIcon(icon.tables_stacks());
    }

    @Override
    public void visit(AppTreeElementIconVisitContext ctx, DummyDynamicTables element) {
        ctx.setIcon(icon.tables_stacks());
    }

    @Override
    public void visit(AppTreeElementIconVisitContext ctx, DummyTableClones element) {
        ctx.setIcon(icon.folder());
    }


    private ImageResource getComponentElementIcon(final ComponentElement model) {
        ComponentType modelType = model.getType();
        switch (modelType) {
            case BorderContainerType:
                return icon.borderContainerSmall();
            case HorizontalContainerType:
                return icon.applicationHorizontal();
            case VerticalContainerType:
                return icon.applicationVertical();
            case CenterContainerType:
                return icon.centerContainerSmall();
            case SimpleContainerType:
                return icon.simpleContainerSmall();
            case VBoxContainerType:
                return icon.vBoxContainerSmall();
            case HBoxContainerType:
                return icon.vBoxContainerSmall();
            case ContentPanelType:
                return icon.contentPanelSmall();
            case TabPanelType:
                return icon.tabPanelSmall();
            case TabItemType:
                return icon.tabItemSmall();
            case FormBuilderType:
                return icon.applicationForm();
            case ButtonType:
                return icon.buttonSmall();
            case HtmlType:
                return icon.htmlSmall();
            case SimpleHtmlEditorType:
                return icon.simpleHtmlEditorSmall();
            case TextFieldType:
                return icon.textFieldSmall();
            case TextAreaType:
                return icon.textAreaSmall();
            case NumberFieldType:
                return icon.numberFieldSmall();
            case ImageType:
                return icon.simpleImageSmall();
            case EditGridType:
                return icon.table();
            case DateFieldType:
                return icon.dateFieldSmall();
            case LabelType:
                return icon.labelSmall();
            case RadioGroupType:
                return icon.radioGroupSmall();
            case CheckGroupType:
                return icon.checkGroupSmall();
            case HtmlEditorType:
                return icon.htmlEditorSmall();
            case ComboBoxType:
                return icon.comboBoxSmall();
            case MultiComboBoxType:
                return icon.multiComboBoxSmall();
            case TreePanelType:
                return icon.treePanelSmall();
            case LoginPanelType:
                return icon.applicationFormKey();
            case FramedLoginPanelType:
                return icon.applicationFormKey();
            case FrameType:
                return icon.application();
            case UploadFieldType:
                return icon.uploadFieldSmall();
            case RadioType:
                return icon.radioSmall();
            case CheckBoxType:
                return icon.checkBoxSmall();
            case WindowType:
                return icon.windowSmall();
            case TaskBarType:
                return icon.taskBarSmall();
            case CaptchaType:
                return icon.captchaSmall();
            case ReportType:
                return icon.report();
            case TreeMenuType:
                return icon.treeMenuSmall();
            case HorizontalMenuType:
                return icon.horizontalMenuSmall();
            case HotKeyType:
                return icon.hotKeySmall();
            case TreeComboBoxType:
                return icon.treeComboBoxSmall();
            case TimerType:
                return icon.timerSmall();
            case HorizontalMenuItemType:
                return icon.menuItemSmall();
            case FieldSetType:
                return icon.fieldSetSmall();
            case TreeMenuItemType:
                return icon.treeMenuItemSmall();
            case ContextMenuItemType:
                return icon.contextMenuItem();
            case PasswordFieldType:
                return icon.passwordFieldSmall();
            default:
                return icon.brick();
        }
    }

}
