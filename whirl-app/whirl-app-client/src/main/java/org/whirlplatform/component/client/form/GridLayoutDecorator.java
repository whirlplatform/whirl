package org.whirlplatform.component.client.form;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.user.client.ui.FlexTable;
import com.sencha.gxt.core.client.util.Util;

public class GridLayoutDecorator {

    private final GridLayoutContainer container;
    private final FlexTable table;

    public GridLayoutDecorator(GridLayoutContainer container) {
        this.container = container;
        table = container.getTable();
    }

    public void setGridEnabled(boolean enabled) {
        if (enabled) {
            table.setBorderWidth(1);
        } else {
            table.setBorderWidth(0);
        }
    }

    public void setGridColor(String color) {
        table.getElement().getStyle().setBorderColor(color);
    }

    public void setCellBorderTop(int row, int column, int thickness, String color) {
        GridLayoutContainer.Cell c = container.getTablePositionByGrid(row,
            column);
        if (c == null) {
            return;
        }
        if (table.getRowCount() > c.getRow()
            && table.getCellCount(c.getRow()) > c.getColumn()) {
            TableCellElement cell = table.getCellFormatter()
                .getElement(c.getRow(), c.getColumn()).cast();
            if (thickness > 0) {
                if (color == null) {
                    color = "#000000";
                }
                cell.getStyle()
                    .setProperty("borderTop", thickness + "px solid "
                        + (color.startsWith("#") ? color : "#" + color));
            } else {
                cell.getStyle().setProperty("borderTop", "0px none");
            }
        }
    }

    public void setCellBorderRight(int row, int column, int thickness, String color) {
        GridLayoutContainer.Cell c = container.getTablePositionByGrid(row,
            column);
        if (c == null) {
            return;
        }
        if (table.getRowCount() > c.getRow() && table.getCellCount(c.getRow()) > c.getColumn()) {
            TableCellElement cell = table.getCellFormatter()
                .getElement(c.getRow(), c.getColumn()).cast();
            if (thickness > 0) {
                if (color == null) {
                    color = "#000000";
                }
                cell.getStyle().setProperty("borderRight",
                    thickness + "px solid "
                        + (color.startsWith("#") ? color : "#" + color));
            } else {
                cell.getStyle().setProperty("borderRight", "0px none");
            }
        }
    }

    public void setCellBorderBottom(int row, int column, int thickness, String color) {
        GridLayoutContainer.Cell c = container.getTablePositionByGrid(row,
            column);
        if (c == null) {
            return;
        }
        if (table.getRowCount() > c.getRow() && table.getCellCount(c.getRow()) > c.getColumn()) {
            TableCellElement cell = table.getCellFormatter()
                .getElement(c.getRow(), c.getColumn()).cast();
            if (thickness > 0) {
                if (color == null) {
                    color = "#000000";
                }
                cell.getStyle().setProperty("borderBottom",
                    thickness + "px solid "
                        + (color.startsWith("#") ? color : "#" + color));
            } else {
                cell.getStyle().setProperty("borderBottom", "0px none");
            }
        }
    }

    public void setCellBorderLeft(int row, int column, int thickness, String color) {
        GridLayoutContainer.Cell c = container.getTablePositionByGrid(row,
            column);
        if (c == null) {
            return;
        }
        if (table.getRowCount() > c.getRow() && table.getCellCount(c.getRow()) > c.getColumn()) {
            TableCellElement cell = table.getCellFormatter()
                .getElement(c.getRow(), c.getColumn()).cast();
            if (thickness > 0) {
                if (color == null) {
                    color = "#000000";
                }
                cell.getStyle().setProperty("borderLeft",
                    thickness + "px solid "
                        + (color.startsWith("#") ? color : "#" + color));
            } else {
                cell.getStyle().setProperty("borderLeft", "0px none");
            }
        }
    }

    public void setCellColor(int row, int column, String color) {
        GridLayoutContainer.Cell c = container.getTablePositionByGrid(row,
            column);
        if (c == null) {
            return;
        }
        if (!Util.isEmptyString(color) && table.getRowCount() > c.getRow()
            && table.getCellCount(c.getRow()) > c.getColumn()) {
            TableCellElement cell = table.getCellFormatter()
                .getElement(c.getRow(), c.getColumn()).cast();
            cell.getStyle().setBackgroundColor(
                color.startsWith("#") ? color : "#" + color);
        }
    }

    public void setCellSpacing(int spacing) {
        table.setCellSpacing(spacing);
    }

    public void setPaddingInCells(int padding) {
        for (int i = 0; i < table.getRowCount(); i++) {
            for (int j = 0; j < table.getCellCount(i); j++) {
                table.getCellFormatter().getElement(i, j).getStyle()
                    .setPadding(padding, Unit.PX);
            }
        }
    }
}
