package org.whirlplatform.meta.shared.component;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import org.whirlplatform.meta.shared.data.DataType;

public enum PropertyType implements Serializable {

    //@formatter:off
    Active("Active", DataType.BOOLEAN, false),
    DomId("DomId", DataType.STRING, false),
    Align("Align", DataType.STRING, true, true),
    BackgroundColor("BackgroundColor", DataType.STRING, true, true),
    BodyStyle("BodyStyle", DataType.STRING, true, true),
    Border("Border", DataType.BOOLEAN, true),
    BorderColor("BorderColor", DataType.STRING, true, true),
    Color("Color", DataType.STRING, true, true),
    Columns("Columns", DataType.NUMBER, true),
    ContextMenu("ContextMenu", DataType.LIST, true),
    Enabled("Enabled", DataType.BOOLEAN, true),
    FontFamily("FontFamily", DataType.STRING, true, true),
    FontSize("FontSize", DataType.STRING, true, true),
    FontStyle("FontStyle", DataType.STRING, true, true),
    FontWeight("FontWeight", DataType.STRING, true, true),
    GridColor("GridColor", DataType.STRING, true, true),
    Height("Height", DataType.NUMBER, true),
    Hidden("Hidden", DataType.BOOLEAN, true),
    Html("Html", DataType.STRING, true),
    ImageUrl("ImageUrl", DataType.STRING, true),
    Orientation("Orientation", DataType.STRING, true, true),
    Rows("Rows", DataType.NUMBER, true),
    ScrollMode("ScrollMode", DataType.STRING, true),
    AdjustForScroll("AdjustForScroll", DataType.BOOLEAN, true),
    Style("Style", DataType.STRING, true, true),
    StyleName("StyleName", DataType.STRING, true, true),
    Title("Title", DataType.STRING, true),
    ToolTip("ToolTip", DataType.STRING, false),
    TextDecoration("TextDecoration", DataType.STRING, true, true),
    Width("Width", DataType.NUMBER, true),
    Closable("Closable", DataType.BOOLEAN, false),
    Minimizable("Minimizable", DataType.BOOLEAN, false),
    Maximizable("Maximizable", DataType.BOOLEAN, false),
    Resizable("Resizable", DataType.BOOLEAN, false),
    Modal("Modal", DataType.BOOLEAN, false),
    DebugId("DebugId", DataType.STRING, false, true),
    TabIndex("TabIndex", DataType.NUMBER, false),
    Mask("Mask", DataType.STRING, false),
    Parameters("Parameters", DataType.STRING, false, true),
    BoxLayoutPack("BoxLayoutPack", DataType.STRING, true, true),
    PaddingTop("PaddingTop", DataType.NUMBER, true),
    PaddingRight("PaddingRight", DataType.NUMBER, true),
    PaddingBottom("PaddingBottom", DataType.NUMBER, true),
    PaddingLeft("PaddingLeft", DataType.NUMBER, true),
    ScrollOffset("ScrollOffset", DataType.NUMBER, true),
    HBoxAlign("HBoxAlign", DataType.STRING, true),
    VBoxAlign("VBoxAlign", DataType.STRING, true),

    AllowDecimals("AllowDecimals", DataType.BOOLEAN, false),
    AllowNegative("AllowNegative", DataType.BOOLEAN, false),
    Alt("Alt", DataType.BOOLEAN, false),
    BoxLabel("BoxLabel", DataType.STRING, true),
    Checkable("Checkable", DataType.BOOLEAN, false),
    CheckExpression("CheckExpression", DataType.STRING, false, true),
    SelectColumn("SelectColumn", DataType.STRING, false),
    CheckStyle("CheckStyle", DataType.STRING, false, true),
    GroupName("GroupName", DataType.STRING, true, true),
    Ctrl("Ctrl", DataType.BOOLEAN, false),
    DataSource("DataSource", DataType.LIST, false),
    Format("Format", DataType.STRING, false, true),
    DisplayValue("DisplayValue", DataType.STRING, false),
    StringValue("Value", DataType.STRING, false, false, Arrays.asList(
        ComponentType.TextFieldType,
        ComponentType.TextAreaType,
        ComponentType.MultiComboBoxType,
        ComponentType.TreeComboBoxType,
        ComponentType.ComboBoxType)),
    NumberValue("Value", DataType.NUMBER, false, false, Arrays.asList(
        ComponentType.NumberFieldType)),
    DateValue("Value", DataType.DATE, false, false, Arrays.asList(
        ComponentType.DateFieldType)),
    BooleanValue("Value", DataType.BOOLEAN, false, false, Arrays.asList(
        ComponentType.RadioType,
        ComponentType.CheckBoxType)),
    Delay("Delay", DataType.NUMBER, false),
    Editable("Editable", DataType.BOOLEAN, false), Cleanable("Cleanable", DataType.BOOLEAN, false),
    FrameEditing("FrameEditing", DataType.BOOLEAN, false),
    Grid("Grid", DataType.BOOLEAN, false),
    HideButtonGroups("HideButtonGroups", DataType.BOOLEAN, false),
    HideColumnHeader("HideColumnHeader", DataType.BOOLEAN, false),
    HideTrigger("HideTrigger", DataType.BOOLEAN, false),
    Key("Key", DataType.STRING, false, true),
    KeyValidate("KeyValidate", DataType.BOOLEAN, false),
    IsLeafColumn("IsLeafColumn", DataType.STRING, false, true),
    SkipInitialLoad("SkipInitialLoad", DataType.BOOLEAN, false, true),
    LoadAll("LoadAll", DataType.BOOLEAN, false),
    MaxLength("MaxLength", DataType.NUMBER, false),
    MaxRowSelected("MaxRowSelected", DataType.NUMBER, false),
    MaxRowMessage("MaxRowMessage", DataType.STRING, false),
    NumberMaxValue("MaxValue", DataType.NUMBER, false, false,
        Arrays.asList(ComponentType.NumberFieldType)),
    DateMaxValue("MaxValue", DataType.DATE, false, false,
        Arrays.asList(ComponentType.DateFieldType)),
    MinLength("MinLength", DataType.NUMBER, false),
    NumberMinValue("MinValue", DataType.NUMBER, false, false,
        Arrays.asList(ComponentType.NumberFieldType)),
    DateMinValue("MinValue", DataType.DATE, false, false,
        Arrays.asList(ComponentType.DateFieldType)),
    Code("Code", DataType.STRING, false, true),

    LabelExpression("LabelExpression", DataType.STRING, false, true),
    ParentExpression("ParentExpression", DataType.STRING, false, true),
    Period("Period", DataType.NUMBER, false),
    ReadOnly("ReadOnly", DataType.BOOLEAN, false),
    RegEx("RegEx", DataType.STRING, false),
    RegExMessage("RegExMessage", DataType.STRING, false),
    Reloadable("Reloadable", DataType.BOOLEAN, false),
    ReloadStructure("ReloadStructure", DataType.BOOLEAN, false),
    Required("Required", DataType.BOOLEAN, false),
    SaveFileName("SaveFileName", DataType.BOOLEAN, false, true),
    StateScope("StateScope", DataType.STRING, false),
    SaveState("SaveState", DataType.BOOLEAN, false),
    RestoreState("RestoreState", DataType.BOOLEAN, false),
    Shift("Shift", DataType.BOOLEAN, false),

    ShowDataButtons("ShowDataButtons", DataType.BOOLEAN, false),
    ShowExportButtons("ShowExportButtons", DataType.BOOLEAN, false),
    ShowFindButtons("ShowFindButtons", DataType.BOOLEAN, false),
    ShowLoadMask("ShowLoadMask", DataType.BOOLEAN, false),
    ShowMethodButtons("ShowMethodButtons", DataType.BOOLEAN, false),
    ShowToolbar("ShowToolbar", DataType.BOOLEAN, false),
    ShowPagingToolbar("ShowPagingToolbar", DataType.BOOLEAN, false),
    ShowRefreshButtons("ShowRefreshButtons", DataType.BOOLEAN, false),
    SingleSelection("SingleSelection", DataType.BOOLEAN, false),
    StateExpression("StateExpression", DataType.STRING, false, true),
    Template("Template", DataType.STRING, false),
    Url("Url", DataType.STRING, true),
    WhereSql("WhereSql", DataType.STRING, false, true),
    EventColumn("EventColumn", DataType.STRING, false, true),
    ImageExpression("ImageExpression", DataType.STRING, false, true),
    Collapsible("Collapsible", DataType.BOOLEAN, false),
    ReportDataType("ReportDataType", DataType.STRING, false),
    ReportDataFormat("ReportDataFormat", DataType.STRING, false),
    DefaultRowsPerPage("DefaultRowsPerPage", DataType.NUMBER, false),
    CellToolTip("CellToolTip", DataType.BOOLEAN, false),
    UseSearchParameters("UseSearchParameters", DataType.BOOLEAN, false),
    IncludeMask("IncludeMask", DataType.BOOLEAN, false),
    MinChars("MinChars", DataType.NUMBER, false),
    SearchField("SearchField", DataType.BOOLEAN, false),
    MoveColumns("MoveColumns", DataType.BOOLEAN, false),

    // Свойства отчета
    FormId("FormId", DataType.STRING, false, true),
    Print("Print", DataType.BOOLEAN, false),
    PageNum("PageNum", DataType.BOOLEAN, false),
    ShowReportParams("ShowReportParams", DataType.BOOLEAN, false),
    ReportFormat("ReportFormat", DataType.STRING, false),
    HorizontalDPI("HorizontalDPI", DataType.STRING, false),
    VerticalDPI("VerticalDPI", DataType.STRING, false),

    LayoutDataSize("LayoutData:Size", DataType.NUMBER, true),
    LayoutDataSplit("LayoutData:Split", DataType.BOOLEAN, true),
    LayoutDataCollapsible("LayoutData:Collapsible", DataType.BOOLEAN, true),
    LayoutDataWidth("LayoutData:Width", DataType.NUMBER, true),
    LayoutDataHeight("LayoutData:Height", DataType.NUMBER, true),
    LayoutDataLocation("LayoutData:Location", DataType.STRING, true, true),
    LayoutDataIndex("LayoutData:Index", DataType.NUMBER, true),
    LayoutDataHorizontalAlign("LayoutData:HorizontalAlign", DataType.STRING, true, true),
    LayoutDataVerticalAlign("LayoutData:VerticalAlign", DataType.STRING, true, true),
    LayoutDataMarginTop("LayoutData:MarginTop", DataType.NUMBER, true, true),
    LayoutDataMarginRight("LayoutData:MarginRight", DataType.NUMBER, true, true),
    LayoutDataMarginBottom("LayoutData:MarginBottom", DataType.NUMBER, true, true),
    LayoutDataMarginLeft("LayoutData:MarginLeft", DataType.NUMBER, true, true),
    LayoutDataFormRow("LayoutData:FormRow", DataType.NUMBER, true),
    LayoutDataFormColumn("LayoutData:FormColumn", DataType.NUMBER, true),
    LayoutDataMaxSize("LayoutData:MaxSize", DataType.NUMBER, true),
    LayoutDataMinSize("LayoutData:MinSize", DataType.NUMBER, true),
    LayoutDataFlex("LayoutData:Flex", DataType.NUMBER, true);
    //@formatter:onØ

    /**
     * Код свойства компонента
     */
    private String code;

    /**
     * Тип данных
     */
    private DataType dataType;

    /**
     * Влияет на визуальное отображение. Используется только в редакторе приложений для перестроения
     * интерфейса.
     */
    private Boolean isUI; // TODO это свойстов должно обявляться отдельно в
    // редакторе приложений

    /**
     * Указывает на наобходимость создания простого не мультиязычного поля редактирования.
     * Используется только в редакторе приложений.
     */
    private boolean simple = false; // TODO это свойстов должно обявляться
    // отдельно в редакторе приложений

    /**
     * Указывает в каких компонентах это свойство используется (нужно для свойств с одинаковым
     * кодом)
     */
    private List<ComponentType> componentTypes;

    PropertyType(String code, DataType dataType, Boolean isUI) {
        this(code, dataType, isUI, false, null);
    }

    PropertyType(String code, DataType dataType, Boolean isUI,
                 boolean simple) {
        this(code, dataType, isUI, simple, null);
    }

    PropertyType(String code, DataType dataType, Boolean isUI,
                 boolean simple, List<ComponentType> componentTypes) {
        this.code = code;
        this.dataType = dataType;
        this.isUI = isUI;
        this.simple = simple;
        this.componentTypes = componentTypes;
    }

    //TODO убрать упоминание по типам компонентов, свойства не должны содержать информацию по компонентам
    public static PropertyType parse(String type, ComponentType componentType) {
        for (PropertyType t : PropertyType.values()) {
            if (t.getCode().equals(type)
                && (t.getComponentTypes() == null
                || t.getComponentTypes().contains(componentType))) {
                return t;
            }
        }
        return null;
    }

    public boolean isUI() {
        return isUI;
    }

    public String getCode() {
        return code;
    }

    public DataType getType() {
        return dataType;
    }

    public boolean isSimple() {
        return simple;
    }

    public List<ComponentType> getComponentTypes() {
        return componentTypes;
    }

    @Override
    public String toString() {
        return code;
    }

}
