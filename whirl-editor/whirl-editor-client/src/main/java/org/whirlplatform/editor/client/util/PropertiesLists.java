package org.whirlplatform.editor.client.util;

import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer.HBoxLayoutAlign;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer.VBoxLayoutAlign;
import com.sencha.gxt.widget.core.client.tree.Tree.CheckCascade;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.whirlplatform.component.client.state.StateScope;
import org.whirlplatform.meta.shared.AppConstant;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.component.ReportDataType;

public class PropertiesLists {

    @SuppressWarnings("serial")
    private static Map<PropertyType, List<String>> properties =
        new HashMap<PropertyType, List<String>>() {
            {
                put(PropertyType.ScrollMode, new ArrayList<String>() {
                    {
                        add(ScrollMode.NONE.toString());
                        add(ScrollMode.ALWAYS.toString());
                        add(ScrollMode.AUTO.toString());
                        add(ScrollMode.AUTOX.toString());
                        add(ScrollMode.AUTOY.toString());
                    }
                });
                put(PropertyType.HBoxAlign, new ArrayList<String>() {
                    {
                        add(HBoxLayoutAlign.MIDDLE.toString());
                        add(HBoxLayoutAlign.TOP.toString());
                        add(HBoxLayoutAlign.BOTTOM.toString());
                        add(HBoxLayoutAlign.STRETCH.toString());
                        add(HBoxLayoutAlign.STRETCHMAX.toString());
                    }
                });
                put(PropertyType.VBoxAlign, new ArrayList<String>() {
                    {
                        add(VBoxLayoutAlign.CENTER.toString());
                        add(VBoxLayoutAlign.LEFT.toString());
                        add(VBoxLayoutAlign.RIGHT.toString());
                        add(VBoxLayoutAlign.STRETCH.toString());
                        add(VBoxLayoutAlign.STRETCHMAX.toString());
                    }
                });
                put(PropertyType.LayoutDataHorizontalAlign,
                    new ArrayList<String>() {
                        {
                            add("Center");
                            add("Right");
                            add("Left");
                        }
                    });
                put(PropertyType.LayoutDataVerticalAlign, new ArrayList<String>() {
                    {
                        add("Middle");
                        add("Top");
                        add("Bottom");
                    }
                });
                put(PropertyType.LayoutDataLocation, new ArrayList<String>() {
                    {
                        add("Center");
                        add("North");
                        add("East");
                        add("South");
                        add("West");
                    }
                });
                put(PropertyType.ReportFormat, new ArrayList<String>() {
                    {
                        add(AppConstant.REPORT_FORMAT_HTML);
                        add(AppConstant.REPORT_FORMAT_XLS);
                        add(AppConstant.REPORT_FORMAT_XLSX);
                        add(AppConstant.REPORT_FORMAT_CSV);
                    }
                });
                put(PropertyType.CheckStyle, new ArrayList<String>() {
                    {
                        add(CheckCascade.NONE.name());
                        add(CheckCascade.CHILDREN.name());
                        add(CheckCascade.PARENTS.name());
                        add(CheckCascade.TRI.name());
                    }
                });
                put(PropertyType.StateScope, new ArrayList<String>() {
                    {
                        add(StateScope.MEMORY.name());
                        add(StateScope.SESSION.name());
                        add(StateScope.LOCAL.name());
                    }
                });
                put(PropertyType.ReportDataType, new ArrayList<String>() {
                    {
                        add(ReportDataType.STRING.name());
                        add(ReportDataType.NUMBER.name());
                        add(ReportDataType.DATE.name());
                    }
                });
                put(PropertyType.DefaultRowsPerPage, new ArrayList<String>() {
                    {
                        // Где-то отдельно хранить этот список?
                        add("10");
                        add("15");
                        add("20");
                        add("30");
                        add("40");
                        add("50");
                        add("100");
                        add("500");
                    }
                });
            }
        };

    public static List<String> getList(PropertyType type) {
        return properties.get(type);
    }

    public static boolean containsList(PropertyType type) {
        return properties.containsKey(type);
    }
}
