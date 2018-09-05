package org.whirlplatform.editor.client.dnd;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.dnd.core.client.DndDragMoveEvent;
import com.sencha.gxt.dnd.core.client.DropTarget;
import com.sencha.gxt.widget.core.client.menu.MenuBar;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.editor.ComponentElement;

public class HorizontalMenuDropTarget extends DropTarget {

    private MenuBar menuBar;
    // private boolean before;
    private int insertIndex;

    public HorizontalMenuDropTarget(MenuBar menuBar) {
        super(menuBar);
        this.menuBar = menuBar;
    }

    @Override
    protected void showFeedback(DndDragMoveEvent event) {
        event.getStatusProxy().setStatus(true);

        int x = event.getDragMoveEvent().getNativeEvent().getClientX();
        int y = event.getDragMoveEvent().getNativeEvent().getClientY();

        insertIndex = menuBar.getWidgetCount();

        XElement el = menuBar.getElement().cast();
        NodeList<Element> trElements = el.getElementsByTagName("tr");
        for (int i = 0; i < trElements.getLength(); i++) {
            Element tr = trElements.getItem(i);
            NodeList<Element> tdElements = tr.getElementsByTagName("td");

            for (int j = 0; j < tdElements.getLength(); j++) {
                Element li = tdElements.getItem(j);
                if (li.<XElement>cast().getBounds().contains(x, y)) {
                    insertIndex = j;
                }
            }
        }
    }

    @Override
    protected void onDragMove(DndDragMoveEvent event) {
        ComponentType type = null;
        if (event.getData() instanceof ComponentType) {
            type = (ComponentType) event.getData();
        } else if (event.getData() instanceof ComponentElement) {
            type = ((ComponentElement) event.getData()).getType();
        }

        if (type != null && type.equals(ComponentType.HorizontalMenuItemType)) {
            event.setCancelled(false);
            event.getStatusProxy().setStatus(true);
        } else {
            event.setCancelled(true);
            event.getStatusProxy().setStatus(false);
        }
    }

    // @Override
    // protected void onDragDrop(DndDropEvent event) {
    // hideInsert();
    // insertIndex = -1;
    // }

    // @Override
    // protected void onDragLeave(DndDragLeaveEvent event) {
    // hideInsert();
    // }

    public int getIndex() {
        return insertIndex;
    }

    // private void showInsert(Element el) {
    // HorizontalInsert insert = HorizontalInsert.get();
    // insert.show(el);
    //
    // Rectangle rect = el.<XElement> cast().getBounds();
    //
    // int x;
    // if (before) {
    // x = rect.getX() - 4;
    // } else {
    // x = rect.getX() + rect.getWidth();
    // }
    //
    // insert.getElement().makePositionable(true);
    // insert.getElement().setBounds(x, rect.getY(), 6, rect.getHeight());
    // }

    // private void hideInsert() {
    // HorizontalInsert insert = HorizontalInsert.get();
    // insert.setVisible(false);
    // }

}
