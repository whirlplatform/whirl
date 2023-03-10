package org.whirlplatform.meta.shared.component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ComponentProperties {

    //@formatter:off
    private static List<PropertyType> generalProperties = Arrays
        .asList(PropertyType.DomId, PropertyType.Code, PropertyType.Enabled,
            PropertyType.Hidden,
            PropertyType.Width, PropertyType.Height, PropertyType.Border,
            PropertyType.BorderColor,
            PropertyType.Title, PropertyType.ToolTip, PropertyType.Style,
            PropertyType.Reloadable,
            PropertyType.TabIndex, PropertyType.LayoutDataSize,
            PropertyType.LayoutDataWidth,
            PropertyType.LayoutDataHeight, PropertyType.LayoutDataSplit,
            PropertyType.LayoutDataCollapsible,
            PropertyType.LayoutDataLocation, PropertyType.LayoutDataIndex,
            PropertyType.LayoutDataHorizontalAlign, PropertyType.LayoutDataVerticalAlign,
            PropertyType.LayoutDataMarginTop, PropertyType.LayoutDataMarginRight,
            PropertyType.LayoutDataMarginBottom, PropertyType.LayoutDataMarginLeft,
            PropertyType.LayoutDataFormRow, PropertyType.LayoutDataFormColumn,
            PropertyType.Closable,
            PropertyType.LayoutDataMinSize, PropertyType.LayoutDataMaxSize,
            PropertyType.LayoutDataFlex);
    //@formatter:on

    @SuppressWarnings("serial")
    private static Map<String, Set<PropertyType>> binding =
        new HashMap<String, Set<PropertyType>>() {
            {
                put(ComponentType.BorderContainerType.getType(), new HashSet<>());
                put(ComponentType.HorizontalContainerType.getType(),
                    new HashSet<PropertyType>() {
                        {
                            add(PropertyType.ScrollMode);
                            add(PropertyType.AdjustForScroll);
                        }
                    });
                put(ComponentType.VerticalContainerType.getType(), new HashSet<PropertyType>() {
                    {
                        add(PropertyType.ScrollMode);
                        add(PropertyType.AdjustForScroll);
                    }
                });
                put(ComponentType.HBoxContainerType.getType(), new HashSet<PropertyType>() {
                    {
                        add(PropertyType.BoxLayoutPack);
                        add(PropertyType.PaddingTop);
                        add(PropertyType.PaddingRight);
                        add(PropertyType.PaddingBottom);
                        add(PropertyType.PaddingLeft);
                        add(PropertyType.ScrollOffset);
                        add(PropertyType.HBoxAlign);
                    }
                });
                put(ComponentType.VBoxContainerType.getType(), new HashSet<PropertyType>() {
                    {
                        add(PropertyType.BoxLayoutPack);
                        add(PropertyType.PaddingTop);
                        add(PropertyType.PaddingRight);
                        add(PropertyType.PaddingBottom);
                        add(PropertyType.PaddingLeft);
                        add(PropertyType.ScrollOffset);
                        add(PropertyType.VBoxAlign);
                    }
                });
                put(ComponentType.CenterContainerType.getType(), new HashSet<>());
                put(ComponentType.SimpleContainerType.getType(), new HashSet<>());
                put(ComponentType.ContentPanelType.getType(), new HashSet<PropertyType>() {
                    {
                        add(PropertyType.Title);
                        add(PropertyType.Html);
                        add(PropertyType.BodyStyle);
                    }
                });
                put(ComponentType.TabPanelType.getType(), new HashSet<>());
                put(ComponentType.TabItemType.getType(), new HashSet<PropertyType>() {
                    {
                        add(PropertyType.Active);
                        add(PropertyType.Html);
                    }
                });
                put(ComponentType.FormBuilderType.getType(), new HashSet<PropertyType>() {
                    {
                        add(PropertyType.Columns);
                        add(PropertyType.Grid);
                        add(PropertyType.GridColor);
                        add(PropertyType.Rows);
                        add(PropertyType.Parameters);
                    }
                });
                put(ComponentType.ButtonType.getType(), new HashSet<PropertyType>() {
                    {
                        add(PropertyType.Html);
                    }
                });
                put(ComponentType.HtmlType.getType(), new HashSet<PropertyType>() {
                    {
                        add(PropertyType.TextDecoration);
                        add(PropertyType.Html);
                        add(PropertyType.FontFamily);
                        add(PropertyType.FontSize);
                        add(PropertyType.FontStyle);
                        add(PropertyType.FontWeight);
                        add(PropertyType.Color);
                        add(PropertyType.ReportDataType);
                        add(PropertyType.ReportDataFormat);
                    }
                });
                put(ComponentType.SimpleHtmlEditorType.getType(), new HashSet<PropertyType>() {
                    {
                        add(PropertyType.StringValue);
                        add(PropertyType.Required);
                        add(PropertyType.ReadOnly);
                    }
                });
                put(ComponentType.TextFieldType.getType(), new HashSet<PropertyType>() {
                    {
                        add(PropertyType.RegEx);
                        add(PropertyType.RegExMessage);
                        add(PropertyType.Required);
                        add(PropertyType.StringValue);
                        add(PropertyType.MinLength);
                        add(PropertyType.MaxLength);
                        add(PropertyType.ReadOnly);
                        add(PropertyType.Mask);
                        add(PropertyType.Cleanable);
                        add(PropertyType.IncludeMask);
                    }
                });
                put(ComponentType.TextAreaType.getType(), new HashSet<PropertyType>() {
                    {
                        add(PropertyType.Required);
                        add(PropertyType.MaxLength);
                        add(PropertyType.MinLength);
                        add(PropertyType.StringValue);
                        add(PropertyType.ReadOnly);
                        add(PropertyType.Cleanable);
                    }
                });
                put(ComponentType.NumberFieldType.getType(), new HashSet<PropertyType>() {
                    {
                        add(PropertyType.Required);
                        add(PropertyType.NumberMaxValue);
                        add(PropertyType.NumberMinValue);
                        add(PropertyType.AllowDecimals);
                        add(PropertyType.AllowNegative);
                        add(PropertyType.MinLength);
                        add(PropertyType.MaxLength);
                        add(PropertyType.NumberValue);
                        add(PropertyType.KeyValidate);
                        add(PropertyType.FontSize);
                        add(PropertyType.Color);
                        add(PropertyType.ReadOnly);
                        add(PropertyType.Cleanable);
                        add(PropertyType.Format);
                    }
                });
                put(ComponentType.ImageType.getType(), new HashSet<PropertyType>() {
                    {
                        add(PropertyType.Url);
                    }
                });
                put(ComponentType.EditGridType.getType(), new HashSet<PropertyType>() {
                    {
                        add(PropertyType.DataSource);
                        add(PropertyType.MaxRowSelected);
                        add(PropertyType.MaxRowMessage);
                        add(PropertyType.ShowLoadMask);
                        add(PropertyType.SkipInitialLoad);
                        add(PropertyType.LoadAll);
                        add(PropertyType.SingleSelection);
                        add(PropertyType.ReloadStructure);
                        add(PropertyType.HideColumnHeader);
                        add(PropertyType.ShowPagingToolbar);
                        add(PropertyType.ShowDataButtons);
                        add(PropertyType.ShowFindButtons);
                        add(PropertyType.ShowExportButtons);
                        add(PropertyType.ShowMethodButtons);
                        add(PropertyType.ShowRefreshButtons);
                        add(PropertyType.HideButtonGroups);
                        add(PropertyType.Checkable);
                        add(PropertyType.WhereSql);
                        add(PropertyType.StateScope);
                        add(PropertyType.SaveState);
                        add(PropertyType.RestoreState);
                        add(PropertyType.Parameters);
                        add(PropertyType.SkipInitialLoad);
                        add(PropertyType.DefaultRowsPerPage);
                        add(PropertyType.CellToolTip);
                        add(PropertyType.MoveColumns);
                    }
                });
                put(ComponentType.DateFieldType.getType(), new HashSet<PropertyType>() {
                    {
                        add(PropertyType.DateMinValue);
                        add(PropertyType.DateMaxValue);
                        add(PropertyType.Required);
                        add(PropertyType.DateValue);
                        add(PropertyType.Format);
                        add(PropertyType.Editable);
                        add(PropertyType.HideTrigger);
                        add(PropertyType.ReadOnly);
                        add(PropertyType.Mask);
                        add(PropertyType.Cleanable);
                    }
                });
                put(ComponentType.LabelType.getType(), new HashSet<PropertyType>() {
                    {
                        add(PropertyType.Html);
                        add(PropertyType.FontFamily);
                        add(PropertyType.FontSize);
                        add(PropertyType.FontStyle);
                        add(PropertyType.FontWeight);
                        add(PropertyType.Color);
                        add(PropertyType.BackgroundColor);
                        add(PropertyType.TextDecoration);
                        add(PropertyType.ReportDataType);
                        add(PropertyType.ReportDataFormat);
                    }
                });
                put(ComponentType.RadioGroupType.getType(), new HashSet<PropertyType>() {
                    {
                        add(PropertyType.DataSource);
                        add(PropertyType.LabelExpression);
                        add(PropertyType.WhereSql);
                        add(PropertyType.Orientation);
                        add(PropertyType.StringValue);
                        add(PropertyType.Parameters);
                    }
                });
                put(ComponentType.CheckGroupType.getType(), new HashSet<PropertyType>() {
                    {
                        add(PropertyType.DataSource);
                        add(PropertyType.LabelExpression);
                        add(PropertyType.CheckExpression);
                        add(PropertyType.StringValue);
                        add(PropertyType.WhereSql);
                        add(PropertyType.StateScope);
                        add(PropertyType.SaveState);
                        add(PropertyType.RestoreState);
                        add(PropertyType.Parameters);
                    }
                });
                put(ComponentType.HtmlEditorType.getType(), new HashSet<PropertyType>());
                put(ComponentType.ComboBoxType.getType(), new HashSet<PropertyType>() {
                    {
                        add(PropertyType.LabelExpression);
                        add(PropertyType.HideTrigger);
                        add(PropertyType.StringValue);
                        add(PropertyType.DisplayValue);
                        add(PropertyType.DataSource);
                        add(PropertyType.Editable);
                        add(PropertyType.Required);
                        add(PropertyType.WhereSql);
                        add(PropertyType.ReadOnly);
                        add(PropertyType.StateScope);
                        add(PropertyType.SaveState);
                        add(PropertyType.RestoreState);
                        add(PropertyType.Parameters);
                        add(PropertyType.Cleanable);
                        add(PropertyType.LoadAll);
                        add(PropertyType.UseSearchParameters);
                        add(PropertyType.MinChars);
                        add(PropertyType.ReloadStructure);
                    }
                });
                put(ComponentType.MultiComboBoxType.getType(), new HashSet<PropertyType>() {
                    {
                        add(PropertyType.LabelExpression);
                        add(PropertyType.HideTrigger);
                        add(PropertyType.DataSource);
                        add(PropertyType.Editable);
                        add(PropertyType.Required);
                        add(PropertyType.WhereSql);
                        add(PropertyType.ReadOnly);
                        add(PropertyType.StateScope);
                        add(PropertyType.SaveState);
                        add(PropertyType.RestoreState);
                        add(PropertyType.StringValue);
                        add(PropertyType.DisplayValue);
                        add(PropertyType.Parameters);
                        add(PropertyType.Cleanable);
                        add(PropertyType.LoadAll);
                        add(PropertyType.SingleSelection);
                        add(PropertyType.UseSearchParameters);
                        add(PropertyType.MinChars);
                        add(PropertyType.ReloadStructure);
                    }
                });
                put(ComponentType.TreeComboBoxType.getType(), new HashSet<PropertyType>() {
                    {
                        add(PropertyType.LabelExpression);
                        add(PropertyType.DataSource);
                        add(PropertyType.StringValue);
                        add(PropertyType.DisplayValue);
                        add(PropertyType.IsLeafColumn);
                        add(PropertyType.StateExpression);
                        add(PropertyType.ParentExpression);
                        add(PropertyType.StateScope);
                        add(PropertyType.SaveState);
                        add(PropertyType.RestoreState);
                        add(PropertyType.WhereSql);
                        add(PropertyType.CheckStyle);
                        add(PropertyType.Parameters);
                        add(PropertyType.Required);
                        add(PropertyType.Cleanable);
                        add(PropertyType.SingleSelection);
                        add(PropertyType.UseSearchParameters);
                        add(PropertyType.MinChars);
                    }
                });
                put(ComponentType.TreePanelType.getType(), new HashSet<PropertyType>() {
                    {
                        add(PropertyType.LabelExpression);
                        add(PropertyType.SelectColumn);
                        add(PropertyType.DataSource);
                        add(PropertyType.IsLeafColumn);
                        add(PropertyType.ParentExpression);
                        add(PropertyType.CheckExpression);
                        add(PropertyType.StateExpression);
                        add(PropertyType.Checkable);
                        add(PropertyType.StateScope);
                        add(PropertyType.SaveState);
                        add(PropertyType.RestoreState);
                        add(PropertyType.Required);
                        add(PropertyType.SingleSelection);
                        add(PropertyType.WhereSql);
                        add(PropertyType.CheckStyle);
                        add(PropertyType.ImageExpression);
                        add(PropertyType.Parameters);
                        add(PropertyType.SearchField);
                        add(PropertyType.MinChars);
                    }
                });
                put(ComponentType.LoginPanelType.getType(), new HashSet<>());
                put(ComponentType.FramedLoginPanelType.getType(), new HashSet<>());
                put(ComponentType.FrameType.getType(), new HashSet<PropertyType>() {
                    {
                        add(PropertyType.Template);
                        add(PropertyType.Url);
                    }
                });
                put(ComponentType.UploadFieldType.getType(), new HashSet<PropertyType>() {
                    {
                        add(PropertyType.SaveFileName);
                        add(PropertyType.Required);
                        add(PropertyType.ReadOnly);
                    }
                });
                put(ComponentType.RadioType.getType(), new HashSet<PropertyType>() {
                    {
                        add(PropertyType.BoxLabel);
                        add(PropertyType.BooleanValue);
                        add(PropertyType.Required);
                        add(PropertyType.ReadOnly);
                        add(PropertyType.GroupName);
                    }
                });
                put(ComponentType.CheckBoxType.getType(), new HashSet<PropertyType>() {
                    {
                        add(PropertyType.BoxLabel);
                        add(PropertyType.BooleanValue);
                        add(PropertyType.Required);
                        add(PropertyType.ReadOnly);
                    }
                });
                put(ComponentType.WindowType.getType(), new HashSet<PropertyType>() {
                    {
                        add(PropertyType.Closable);
                        add(PropertyType.Minimizable);
                        add(PropertyType.Maximizable);
                        add(PropertyType.Resizable);
                        add(PropertyType.Modal);
                    }
                });
                put(ComponentType.TaskBarType.getType(), new HashSet<PropertyType>());
                put(ComponentType.TreeMenuType.getType(), new HashSet<PropertyType>() {
                    {

                    }
                });
                put(ComponentType.HorizontalMenuType.getType(), new HashSet<PropertyType>() {
                    {
                    }
                });
                put(ComponentType.HorizontalMenuItemType.getType(),
                    new HashSet<PropertyType>() {
                        {
                            add(PropertyType.Html);
                            add(PropertyType.ImageUrl);
                        }
                    });
                put(ComponentType.TimerType.getType(), new HashSet<PropertyType>() {
                    {
                        add(PropertyType.Delay);
                        add(PropertyType.Period);
                    }
                });
                put(ComponentType.ReportType.getType(), new HashSet<PropertyType>() {
                    {
                        add(PropertyType.ShowReportParams);
                        add(PropertyType.ReportFormat);
                    }
                });
                put(ComponentType.HotKeyType.getType(), new HashSet<PropertyType>() {
                    {
                        add(PropertyType.Key);
                        add(PropertyType.Alt);
                        add(PropertyType.Ctrl);
                        add(PropertyType.Shift);
                    }
                });
                put(ComponentType.FieldSetType.getType(), new HashSet<PropertyType>() {
                    {
                        add(PropertyType.Collapsible);
                    }
                });

            }
        };

    public static Set<PropertyType> getProperties(ComponentType type) {
        Set<PropertyType> properties = new HashSet<PropertyType>();
        properties.addAll(generalProperties);
        if (binding.containsKey(type.getType()) && binding.get(type.getType()) != null) {
            properties.addAll(binding.get(type.getType()));
        }
        return properties;
    }

    public static Set<String> getReplaceableProperties(ComponentType type) {
        Set<String> result = new HashSet<String>();
        for (PropertyType pt : getProperties(type)) {
            result.add(pt.getCode());
        }
        return result;
    }

}
