package org.whirlplatform.editor.client.util;

import java.util.Comparator;
import org.whirlplatform.editor.client.tree.dummy.OrderedDummy;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.editor.AbstractElement;
import org.whirlplatform.meta.shared.editor.ApplicationElement;
import org.whirlplatform.meta.shared.editor.ComponentElement;
import org.whirlplatform.meta.shared.editor.ContextMenuItemElement;
import org.whirlplatform.meta.shared.editor.EventParameterElement;
import org.whirlplatform.meta.shared.editor.db.AbstractTableElement;

public class ApplicationTreeComparator implements Comparator<AbstractElement> {

    public ApplicationTreeComparator() {
    }

    @Override
    public int compare(AbstractElement first, AbstractElement second) {
        if (first instanceof ComponentElement && second instanceof ComponentElement) {
            return compareComponents((ComponentElement) first, (ComponentElement) second);
        } else if (first instanceof EventParameterElement
                && second instanceof EventParameterElement) {
            return ((EventParameterElement) first).getIndex()
                    - ((EventParameterElement) second).getIndex();
        } else if (first instanceof AbstractTableElement
                && second instanceof AbstractTableElement) {
            return (first != null && first.getName() != null)
                ? first.getName().compareTo(second.getName()) : -1;
        } else if (first instanceof ApplicationElement && second instanceof ApplicationElement) {
            return (first != null && ((ApplicationElement) first).getCode() != null)
                    ? ((ApplicationElement) first).getCode()
                    .compareTo(((ApplicationElement) second).getCode()) : -1;
        } else if (first instanceof ComponentElement && !(second instanceof ComponentElement)) {
            return -1;
        } else if (second instanceof ComponentElement && !(first instanceof ComponentElement)) {
            return 1;
        } else if (first instanceof OrderedDummy && second instanceof OrderedDummy) {
            return compareOrderedDummies((OrderedDummy) first, (OrderedDummy) second);
        } else if (first instanceof ContextMenuItemElement
                && second instanceof ContextMenuItemElement) {
            return ((ContextMenuItemElement) first).getIndex()
                - ((ContextMenuItemElement) second).getIndex();
        } else if (first instanceof ContextMenuItemElement
            && !(second instanceof ContextMenuItemElement)) {
            return -1;
        } else if (second instanceof ContextMenuItemElement
                && !(first instanceof ContextMenuItemElement)) {
            return 1;
        }
        return (first != null && first.getName() != null)
            ? first.getName().compareTo(second.getName()) : -1;
    }

    private int compareOrderedDummies(final OrderedDummy first, final OrderedDummy second) {
        return first.getIndex() - second.getIndex();
    }

    private int compareComponents(ComponentElement first, ComponentElement second) {
        int res = 0;
        if (first.getParent() != null && second.getParent() != null
                && first.getParent() == second.getParent()) {
            if (first.getParent().getType() == ComponentType.VerticalContainerType
                    || first.getParent().getType() == ComponentType.HorizontalContainerType
                    || first.getParent().getType() == ComponentType.TabPanelType) {
                int index1 =
                        parseIndex(String.valueOf(first.getProperty(PropertyType.LayoutDataIndex)
                                .getValue(first.getProperty(PropertyType.LayoutDataIndex)
                                        .getDefaultLocale()).getInteger()));
                int index2 =
                        parseIndex(String.valueOf(second.getProperty(PropertyType.LayoutDataIndex)
                                .getValue(second.getProperty(PropertyType.LayoutDataIndex)
                                        .getDefaultLocale()).getInteger()));
                res = index1 - index2;
            } else if (first.getParent().getType() == ComponentType.BorderContainerType) {
                int index1 = parseLocation(
                        String.valueOf(first.getProperty(PropertyType.LayoutDataLocation)
                                .getValue(first.getProperty(PropertyType.LayoutDataLocation)
                                        .getDefaultLocale()).getInteger()));
                int index2 = parseLocation(
                        String.valueOf(second.getProperty(PropertyType.LayoutDataLocation)
                                .getValue(second.getProperty(PropertyType.LayoutDataLocation)
                                        .getDefaultLocale()).getInteger()));
                res = index1 - index2;
            } else if (first.getParent().getType() == ComponentType.FormBuilderType) {
                int cols = first.getParent().getProperty(PropertyType.Columns)
                        .getValue(first.getParent().getProperty(PropertyType.Columns)
                                .getDefaultLocale()).getInteger();
                int row1 = first.getProperty(PropertyType.LayoutDataFormRow)
                        .getValue(first.getProperty(PropertyType.LayoutDataFormRow)
                                .getDefaultLocale()).getInteger();
                int col1 = first.getProperty(PropertyType.LayoutDataFormColumn)
                        .getValue(first.getProperty(PropertyType.LayoutDataFormColumn)
                                .getDefaultLocale()).getInteger();
                int row2 = second.getProperty(PropertyType.LayoutDataFormRow)
                        .getValue(second.getProperty(PropertyType.LayoutDataFormRow)
                                .getDefaultLocale()).getInteger();
                int col2 = second.getProperty(PropertyType.LayoutDataFormColumn)
                        .getValue(second.getProperty(PropertyType.LayoutDataFormColumn)
                                .getDefaultLocale()).getInteger();
                int index1 = parseForm(row1, col1, cols);
                int index2 = parseForm(row2, col2, cols);
                res = index1 - index2;
            }
        } else if (first.getParent() == null && second.getParent() == null) {
            res = first.getName().compareTo(second.getName());
        }
        return res;
    }

    private int parseIndex(String value) {
        try {
            Double res = Double.parseDouble(value);
            return res.intValue();
        } catch (Exception e) {
            return 3000;
        }
    }

    private int parseLocation(String value) {
        if ("center".equalsIgnoreCase(value)) {
            return 0;
        } else if ("north".equalsIgnoreCase(value)) {
            return 1;
        } else if ("east".equalsIgnoreCase(value)) {
            return 2;
        } else if ("south".equalsIgnoreCase(value)) {
            return 3;
        } else if ("west".equalsIgnoreCase(value)) {
            return 4;
        }
        return 3000;
    }

    private int parseForm(int row, int col, int cols) {
        try {
            return cols * row + col;
        } catch (Exception e) {
            return 3000;
        }
    }
}
