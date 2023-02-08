package org.whirlplatform.editor.client.dnd;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.dom.client.TableElement;
import com.google.gwt.dom.client.TableRowElement;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.Rectangle;
import com.sencha.gxt.dnd.core.client.DndDragEnterEvent;
import com.sencha.gxt.dnd.core.client.DndDragLeaveEvent;
import com.sencha.gxt.dnd.core.client.DndDragMoveEvent;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.dnd.core.client.DropTarget;
import org.whirlplatform.component.client.form.GridLayoutContainer;
import org.whirlplatform.component.client.form.GridLayoutContainer.Cell;
import org.whirlplatform.editor.client.component.surface.Surface;

public class GridLayoutDropTarget extends DropTarget {

    private GridLayoutContainer gridLayout;

    private Element over;
    private int row = -1;
    private int column = -1;

    public GridLayoutDropTarget(GridLayoutContainer container) {
        super(container);
        this.gridLayout = container;
    }

    @Override
    protected void onDragDrop(DndDropEvent event) {
        // super.onDragDrop(event);
        // Widget widget = (Widget) event.getData();
        //
        // TableElement table = gridLayout.getElement().cast();
        // NodeList<TableRowElement> rows = table.getRows();
        // boolean stop = false;
        // for (int i = 0; i < rows.getLength(); i++) {
        // TableRowElement rowEl = rows.getItem(i);
        // NodeList<TableCellElement> cells = rowEl.getCells();
        // for (int j = 0; j < cells.getLength(); j++) {
        // TableCellElement cellEl = cells.getItem(j);
        // if (cellEl == over) {
        // row = i;
        // column = j;
        // stop = true;
        // break;
        // }
        // }
        // if (stop) {
        // break;
        // }
        // }
        //
        // if (row != -1 && column != -1) {
        // if (gridLayout.getWidgetIndex(widget) > -1) {
        // widget.removeFromParent();
        // }
        // gridLayout.setWidget(row, column, widget);
        // }
        //
        // Scheduler.get().scheduleFinally(new ScheduledCommand() {
        // @Override
        // public void execute() {
        // gridLayout.forceLayout();
        // }
        // });

        hideSurface();

        over = null;
        row = -1;
        column = -1;
    }

    @Override
    protected void onDragLeave(DndDragLeaveEvent event) {
        super.onDragLeave(event);
        hideSurface();
    }

    private void hideSurface() {
        Surface surface = Surface.get();
        surface.setVisible(false);
    }

    @Override
    protected void onDragEnter(DndDragEnterEvent event) {
        super.onDragEnter(event);
        event.setCancelled(true);
        event.getStatusProxy().setStatus(true);
    }

    @Override
    protected void onDragMove(DndDragMoveEvent event) {
        XElement target = event.getDragMoveEvent().getNativeEvent()
            .getEventTarget().cast();
        if (!gridLayout.getElement().isOrHasChild(target)) {
            event.setCancelled(true);
            event.getStatusProxy().setStatus(false);
        } else {
            event.setCancelled(false);
            event.getStatusProxy().setStatus(true);
        }
    }

    @Override
    protected void showFeedback(DndDragMoveEvent event) {
        event.getStatusProxy().setStatus(true);
        NativeEvent e = event.getDragMoveEvent().getNativeEvent().cast();
        XElement el = e.getEventTarget().cast();
        if (gridLayout.getElement() == el) {
            return;
        }
        TableCellElement cellEl = el.findParent(TableCellElement.TAG_TD, -1)
            .cast();
        if (cellEl != null) {
            Element tr = cellEl.getParentElement();
            Element tbody = tr.getParentElement();
            Element table = tbody.getParentElement();
            if (table != gridLayout.getElement()) {
                return;
            }
            over = cellEl;
            showSurface(cellEl);

            TableElement t = gridLayout.getElement().cast();
            NodeList<TableRowElement> rows = t.getRows();
            boolean stop = false;
            for (int i = 0; i < rows.getLength(); i++) {
                TableRowElement rowEl = rows.getItem(i);
                NodeList<TableCellElement> cells = rowEl.getCells();
                for (int j = 0; j < cells.getLength(); j++) {
                    cellEl = cells.getItem(j);
                    if (cellEl == over) {
                        Cell cell = gridLayout.getGridPositionByTable(i, j);
                        row = cell.getRow();
                        column = cell.getColumn();
                        stop = true;
                        break;
                    }
                }
                if (stop) {
                    break;
                }
            }
        }
    }

    private void showSurface(Element cell) {
        Rectangle rect = cell.<XElement>cast().getBounds();
        Surface surface = Surface.get();
        surface.getElement().makePositionable(true);
        surface.getElement().setBounds(rect);
        surface.show(cell);
    }

    public GridLayoutContainer.Cell getCell() {
        return gridLayout.new Cell(row, column);
    }

}
