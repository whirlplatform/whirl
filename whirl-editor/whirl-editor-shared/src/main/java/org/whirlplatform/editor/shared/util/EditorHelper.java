package org.whirlplatform.editor.shared.util;

import org.whirlplatform.meta.shared.AppConstant;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.component.RandomUUID;
import org.whirlplatform.meta.shared.editor.ComponentElement;
import org.whirlplatform.meta.shared.editor.FormElement;
import org.whirlplatform.meta.shared.editor.LocaleElement;
import org.whirlplatform.meta.shared.editor.PropertyValue;
import org.whirlplatform.meta.shared.editor.ReportElement;

public class EditorHelper {

    public static ComponentElement newComponentElement(ComponentType type,
                                                       LocaleElement defaultLocale) {
        ComponentElement result;
        if (ComponentType.FormBuilderType == type) {
            result = new FormElement();
        } else if (ComponentType.ReportType == type) {
            result = new ReportElement();
        } else {
            result = new ComponentElement(type);
        }
        result.setId(RandomUUID.uuid());
        String typeStr = result.getType().getType();
        result.setName(typeStr.substring(0, typeStr.lastIndexOf("Builder")));
        return setDefaultProps(result, defaultLocale);

    }

    public static boolean hasNestedElement(ComponentElement parent, ComponentElement nested) {
        for (ComponentElement e : parent.getChildren()) {
            if (e == nested) {
                return true;
            }
            if (hasNestedElement(e, nested)) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("incomplete-switch")
    private static ComponentElement setDefaultProps(ComponentElement comp,
                                                    LocaleElement defaultLocale) {
        switch (comp.getType()) {
            case ButtonType:
                setProperty(comp, defaultLocale, PropertyType.Html, "Button");
                break;
            case LabelType:
                setProperty(comp, defaultLocale, PropertyType.Html, "Label");
                break;
            case VerticalContainerType:
                setProperty(comp, defaultLocale, PropertyType.Width, 1);
                setProperty(comp, defaultLocale, PropertyType.Height, 1);
                setProperty(comp, defaultLocale, PropertyType.LayoutDataWidth, 1);
                setProperty(comp, defaultLocale, PropertyType.LayoutDataHeight, 1);
                break;
            case HorizontalContainerType:
                setProperty(comp, defaultLocale, PropertyType.Width, 1);
                setProperty(comp, defaultLocale, PropertyType.Height, 1);
                setProperty(comp, defaultLocale, PropertyType.LayoutDataWidth, 1);
                setProperty(comp, defaultLocale, PropertyType.LayoutDataHeight, 1);
                break;
            case BorderContainerType:
                setProperty(comp, defaultLocale, PropertyType.Width, 1);
                setProperty(comp, defaultLocale, PropertyType.Height, 1);
                setProperty(comp, defaultLocale, PropertyType.LayoutDataWidth, 1);
                setProperty(comp, defaultLocale, PropertyType.LayoutDataHeight, 1);
                break;
            case SimpleContainerType:
                setProperty(comp, defaultLocale, PropertyType.Width, 1);
                setProperty(comp, defaultLocale, PropertyType.Height, 1);
                setProperty(comp, defaultLocale, PropertyType.LayoutDataWidth, 1);
                setProperty(comp, defaultLocale, PropertyType.LayoutDataHeight, 1);
                break;
            case CenterContainerType:
                setProperty(comp, defaultLocale, PropertyType.Width, 1);
                setProperty(comp, defaultLocale, PropertyType.Height, 1);
                setProperty(comp, defaultLocale, PropertyType.LayoutDataWidth, 1);
                setProperty(comp, defaultLocale, PropertyType.LayoutDataHeight, 1);
                break;
            case ContentPanelType:
                setProperty(comp, defaultLocale, PropertyType.Width, 1);
                setProperty(comp, defaultLocale, PropertyType.Height, 1);
                setProperty(comp, defaultLocale, PropertyType.LayoutDataWidth, 1);
                setProperty(comp, defaultLocale, PropertyType.LayoutDataHeight, 1);
                break;
            case RadioGroupType:
                setProperty(comp, defaultLocale, PropertyType.Width, 100);
                setProperty(comp, defaultLocale, PropertyType.Height, 150);
                break;
            case CheckGroupType:
                setProperty(comp, defaultLocale, PropertyType.Width, 100);
                setProperty(comp, defaultLocale, PropertyType.Height, 150);
                break;
            case ImageType:
                setProperty(comp, defaultLocale, PropertyType.Width, 50);
                setProperty(comp, defaultLocale, PropertyType.Height, 50);
                break;
            case TreePanelType:
                setProperty(comp, defaultLocale, PropertyType.Width, 100);
                setProperty(comp, defaultLocale, PropertyType.Height, 150);
                break;
            case TreeMenuType:
                setProperty(comp, defaultLocale, PropertyType.Width, 100);
                setProperty(comp, defaultLocale, PropertyType.Height, 150);
                break;
            case TabItemType:
                setProperty(comp, defaultLocale, PropertyType.Title, "Item");
                break;
            case HtmlType:
                setProperty(comp, defaultLocale, PropertyType.Html, "Html");
                break;
            case HorizontalMenuItemType:
                setProperty(comp, defaultLocale, PropertyType.Title, "Item");
                break;
            case ReportType:
                setProperty(comp, defaultLocale, PropertyType.ShowReportParams, true);
                setProperty(comp, defaultLocale, PropertyType.ReportFormat,
                    AppConstant.REPORT_FORMAT_HTML);
                break;
            case TabPanelType:
                setProperty(comp, defaultLocale, PropertyType.Width, 1);
                setProperty(comp, defaultLocale, PropertyType.Height, 1);
                setProperty(comp, defaultLocale, PropertyType.LayoutDataWidth, 1);
                setProperty(comp, defaultLocale, PropertyType.LayoutDataHeight, 1);
                break;
            case FormBuilderType:
                setProperty(comp, defaultLocale, PropertyType.Rows, 3);
                setProperty(comp, defaultLocale, PropertyType.Columns, 3);
                setProperty(comp, defaultLocale, PropertyType.Width, 1);
                setProperty(comp, defaultLocale, PropertyType.Height, 1);
                setProperty(comp, defaultLocale, PropertyType.LayoutDataWidth, 1);
                setProperty(comp, defaultLocale, PropertyType.LayoutDataHeight, 1);
                break;
            case EditGridType:
                setProperty(comp, defaultLocale, PropertyType.ShowDataButtons, true);
                setProperty(comp, defaultLocale, PropertyType.ShowExportButtons, true);
                setProperty(comp, defaultLocale, PropertyType.ShowFindButtons, true);
                setProperty(comp, defaultLocale, PropertyType.ShowMethodButtons, true);
                setProperty(comp, defaultLocale, PropertyType.ShowRefreshButtons, true);
                setProperty(comp, defaultLocale, PropertyType.ShowPagingToolbar, true);
                setProperty(comp, defaultLocale, PropertyType.DefaultRowsPerPage, 20);
                setProperty(comp, defaultLocale, PropertyType.Width, 1);
                setProperty(comp, defaultLocale, PropertyType.Height, 1);
                break;
            case FieldSetType:
                setProperty(comp, defaultLocale, PropertyType.Collapsible, true);
                break;
            case ComboBoxType:
                setProperty(comp, defaultLocale, PropertyType.MinChars, 2);
                break;
            case MultiComboBoxType:
                setProperty(comp, defaultLocale, PropertyType.MinChars, 2);
                break;
            case TreeComboBoxType:
                setProperty(comp, defaultLocale, PropertyType.MinChars, 2);
                break;
            default:
                throw new IllegalArgumentException("Unsupported type");
        }
        setProperty(comp, defaultLocale, PropertyType.Enabled, true);

        setProperty(comp, defaultLocale, PropertyType.LayoutDataMarginTop, 3);
        setProperty(comp, defaultLocale, PropertyType.LayoutDataMarginRight, 3);
        setProperty(comp, defaultLocale, PropertyType.LayoutDataMarginBottom, 3);
        setProperty(comp, defaultLocale, PropertyType.LayoutDataMarginLeft, 3);

        return comp;
    }

    private static void setProperty(ComponentElement element, LocaleElement locale,
                                    PropertyType type, Object value) {
        element.setProperty(type, new PropertyValue(type.getType(), locale, value));
    }

    public interface ComponentCommand {

        void execute();

    }

}
