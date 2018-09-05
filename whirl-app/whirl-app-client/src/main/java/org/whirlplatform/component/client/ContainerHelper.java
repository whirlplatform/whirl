package org.whirlplatform.component.client;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment.VerticalAlignmentConstant;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.core.client.util.Util;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import com.sencha.gxt.widget.core.client.container.HasHeight;
import com.sencha.gxt.widget.core.client.container.HasWidth;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import org.whirlplatform.component.client.base.ContextMenuItemBuilder;
import org.whirlplatform.component.client.event.EventManager;
import org.whirlplatform.component.client.form.GridLayoutData;
import org.whirlplatform.meta.shared.component.ComponentModel;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataValue;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ContainerHelper {

    private static final int LAYOUT_DATA_MIN_SIZE = 50;
    private static final int LAYOUT_DATA_MAX_SIZE = 5000;

    public static BorderLayoutData getBorderLayoutData(
            Map<String, DataValue> builderProperties) {
        BorderLayoutData data = new BorderLayoutData();

        DataValue sizeData = builderProperties.get(PropertyType.LayoutDataSize.getCode());
        if (sizeData != null) {
            Double size = sizeData.getDouble();
            if (size != null) data.setSize(size);
        }
        DataValue collData = builderProperties.get(PropertyType.LayoutDataCollapsible.getCode());
        if (collData != null) {
            Boolean collaps = collData.getBoolean();
            if (collaps != null) data.setCollapsible(collaps);
        }
        DataValue splitData = builderProperties.get(PropertyType.LayoutDataSplit.getCode());
        if (splitData != null) {
            Boolean split = splitData.getBoolean();
            if (split != null) data.setSplit(split);
        }

        data.setMinSize(LAYOUT_DATA_MIN_SIZE);
        data.setMaxSize(LAYOUT_DATA_MAX_SIZE);

        data.setMargins(getMargins(builderProperties));
        return data;
    }

    private static HorizontalAlignmentConstant parseHorizontalAlign(String value) {
        if ("Center".equalsIgnoreCase(value)) {
            return HasHorizontalAlignment.ALIGN_CENTER;
        } else if ("Right".equalsIgnoreCase(value)) {
            return HasHorizontalAlignment.ALIGN_RIGHT;
        } else {
            return HasHorizontalAlignment.ALIGN_LEFT;
        }
    }

    private static VerticalAlignmentConstant parseVerticalAlign(String value) {
        if ("Top".equalsIgnoreCase(value)) {
            return HasVerticalAlignment.ALIGN_TOP;
        } else if ("Bottom".equalsIgnoreCase(value)) {
            return HasVerticalAlignment.ALIGN_BOTTOM;
        } else {
            return HasVerticalAlignment.ALIGN_MIDDLE;
        }
    }

    public static GridLayoutData getGridLayoutData(
            Map<String, DataValue> properties) {
        GridLayoutData data = new GridLayoutData();

        setWidth(data, properties);
        setHeight(data, properties);


        DataValue horizontalData = properties.get("LayoutData:HorizontalAlign");
        if (horizontalData != null) {
            String horizontal = horizontalData.getString();
            if (!Util.isEmptyString(horizontal)) {
                data.setHorizontalAlignment(parseHorizontalAlign(horizontal));
            }
        }

        DataValue verticalData = properties.get("LayoutData:VerticalAlign");
        if (verticalData != null) {
            String vertical = verticalData.getString();
            if (!Util.isEmptyString(vertical)) {
                data.setVerticalAlignment(parseVerticalAlign(vertical));
            }
        }
        data.setMargins(getMargins(properties));
        return data;
    }

    public static HorizontalLayoutData getHorizontalLayoutData(
            Map<String, DataValue> properties) {
        HorizontalLayoutData data = new HorizontalLayoutData();

        setWidth(data, properties);
        setHeight(data, properties);

        data.setMargins(getMargins(properties));
        return data;
    }

    public static VerticalLayoutData getVerticalLayoutData(
            Map<String, DataValue> properties) {
        VerticalLayoutData data = new VerticalLayoutData();

        setWidth(data, properties);
        setHeight(data, properties);

        data.setMargins(getMargins(properties));
        return data;
    }

    public static BoxLayoutData getBoxLayoutData(Map<String, DataValue> properties) {
        BoxLayoutData data = new BoxLayoutData();

        DataValue minSizeData = properties.get(PropertyType.LayoutDataMinSize);
        if (minSizeData != null) {
            Double msval = minSizeData.getDouble();
            if (msval != null) data.setMinSize(msval.intValue());
        }
        DataValue maxSizeData = properties.get(PropertyType.LayoutDataMaxSize);
        if (maxSizeData != null) {
            Double msval = maxSizeData.getDouble();
            if (msval != null) data.setMaxSize(msval.intValue());
        }
        DataValue flexData = properties.get(PropertyType.LayoutDataFlex);
        if (flexData != null) {
            Double flval = flexData.getDouble();
            if (flval != null) data.setFlex(flval.intValue());
        }

        data.setMargins(getMargins(properties));
        return data;
    }

    private static void setWidth(HasWidth component,
                                 Map<String, DataValue> properties) {
        DataValue widthData = properties.get("LayoutData:Width");
        if (widthData != null) {
            Double width = widthData.getDouble();
            if (width != null) component.setWidth(width);
        }
    }

    private static void setHeight(HasHeight component,
                                  Map<String, DataValue> properties) {
        DataValue heightData = properties.get("LayoutData:Height");
        if (heightData != null) {
            Double height = heightData.getDouble();
            if (height != null) component.setHeight(height);
        }
    }

    private static Margins getMargins(Map<String, DataValue> properties) {
        Margins margins = new Margins();

        DataValue marginTop = properties.get(PropertyType.LayoutDataMarginTop
                .getCode());
        if (marginTop != null) {
            Double top = marginTop.getDouble();
            if (top != null) {
                margins.setTop(top.intValue());
            }
        }
        DataValue marginRight = properties.get(PropertyType.LayoutDataMarginRight
                .getCode());
        if (marginRight != null) {
            Double right = marginRight.getDouble();
            if (right != null) {
                margins.setRight(right.intValue());
            }
        }
        DataValue marginBottom = properties
                .get(PropertyType.LayoutDataMarginBottom.getCode());
        if (marginBottom != null) {
            Double bot = marginBottom.getDouble();
            if (bot != null) {
                margins.setBottom(bot.intValue());
            }
        }
        DataValue marginLeft = properties.get(PropertyType.LayoutDataMarginLeft
                .getCode());
        if (marginLeft != null) {
            Double left = marginLeft.getDouble();
            if (left != null) {
                margins.setLeft(left.intValue());
            }
        }


        return margins;
    }

    public static void buildComponent(Collection<ComponentModel> children,
                                      Containable container) {
        if (children == null || container == null) {
            throw new IllegalArgumentException("Paramaters can not be null");
        }

        for (ComponentModel child : children) {
            buildComponent(child, container);
        }
    }

    public static void buildComponent(Collection<? extends ComponentModel> children,
                                      Containable container, List<DataValue> parameters) {
        if (children == null || container == null) {
            throw new IllegalArgumentException("Paramaters can not be null");
        }

        for (ComponentModel child : children) {
            buildComponent(child, container, parameters);
        }
        //TODO не должно быть для всех HorizontalMenu
        container.forceLayout();
    }

    public static ComponentBuilder buildComponent(ComponentModel model,
                                                  Containable container, List<DataValue> parameters) {
        String id = model.getId();

        ComponentBuilder builder = ComponentTypeUtil.findBuilder(model.getType(), model.getValues());

        if (builder != null) {
            builder.setId(id);
            Object ow = builder.getWrapper();
            if (ow instanceof Widget) {
                Widget w = (Widget) ow;
                w.ensureDebugId(id);
            }
            builder.setProperties(model
                    .getValues(), false);
            builder.setReplaceableProperties(model.getReplaceableProperties());

            // Контекстное меню
            for (ComponentModel m : model.getContextMenuItems()) {
                builder.addContextMenuItem(buildContextMenuItem(m));
            }

            if (builder instanceof HasCreateParameters) {
                ((HasCreateParameters) builder).create(parameters);
            } else {
                builder.create();
            }

            EventManager.Util.get().addEvents(builder, model);
            if (container != null) {
                container.addChild(builder);
            }

            if (builder instanceof Containable && model.hasChildren()) {
                buildComponent(model.getChildren(), (Containable) builder, parameters);
            }

        }
        return builder;
    }

    private static ContextMenuItemBuilder buildContextMenuItem(ComponentModel item) {
        ContextMenuItemBuilder builder = new ContextMenuItemBuilder();
        builder.setProperties(item.getValues(), false);
        EventManager.Util.get().addEvents(builder, item);

        for (ComponentModel child : item.getChildren()) {
            builder.addContextMenuItem(buildContextMenuItem(child));
        }

        return builder;
    }

    public static ComponentBuilder buildComponent(ComponentModel model,
                                                  Containable container) {
        return buildComponent(model, container,
                Collections.emptyList());
    }

}
