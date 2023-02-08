package org.whirlplatform.editor.client.util;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.dnd.core.client.DndDragEnterEvent;
import com.sencha.gxt.dnd.core.client.DndDragEnterEvent.DndDragEnterHandler;
import com.sencha.gxt.dnd.core.client.DndDragLeaveEvent;
import com.sencha.gxt.dnd.core.client.DndDragLeaveEvent.DndDragLeaveHandler;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.dnd.core.client.DndDropEvent.DndDropHandler;
import com.sencha.gxt.dnd.core.client.DropTarget;
import com.sencha.gxt.widget.core.client.event.BeforeQueryEvent;
import com.sencha.gxt.widget.core.client.event.BeforeQueryEvent.BeforeQueryHandler;
import com.sencha.gxt.widget.core.client.event.TriggerClickEvent;
import com.sencha.gxt.widget.core.client.event.TriggerClickEvent.TriggerClickHandler;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import org.whirlplatform.meta.shared.editor.db.AbstractTableElement;

public class TableComboBox extends ComboBox<AbstractTableElement> {

    public TableComboBox(TableStore store) {
        super(store, new ElementLabelProvider<AbstractTableElement>());

        setTriggerAction(TriggerAction.ALL);
        setForceSelection(true);
        addTriggerClickHandler(new TriggerClickHandler() {
            @Override
            public void onTriggerClick(TriggerClickEvent event) {
                if (!isExpanded()) {
                    //                    getStore().getLoader().load(getText());
                }
            }
        });
        addBeforeQueryHandler(new BeforeQueryHandler<AbstractTableElement>() {
            @Override
            public void onBeforeQuery(BeforeQueryEvent<AbstractTableElement> event) {
                getStore().getLoader().load(event.getQuery());
            }
        });
        DropTarget target = new DropTarget(this);
        target.addDropHandler(new DndDropHandler() {
            @Override
            public void onDrop(DndDropEvent event) {
                if (event.getData() instanceof AbstractTableElement) {
                    setValue((AbstractTableElement) event.getData());
                }
            }
        });
        target.addDragEnterHandler(new DndDragEnterHandler() {
            @Override
            public void onDragEnter(DndDragEnterEvent event) {
                if (event.getDragSource().getData() instanceof AbstractTableElement) {
                    event.getStatusProxy().setStatus(true);
                }
            }
        });
        target.addDragLeaveHandler(new DndDragLeaveHandler() {
            @Override
            public void onDragLeave(DndDragLeaveEvent event) {
                event.getStatusProxy().setStatus(false);
            }
        });

        addSelectionHandler(new SelectionHandler<AbstractTableElement>() {

            @Override
            public void onSelection(SelectionEvent<AbstractTableElement> event) {
                if (event.getSelectedItem().getId().isEmpty()) {
                    TableComboBox.this.setValue(null, false, true);
                    TableComboBox.this.setText("");
                }
            }
        });
    }

    @Override
    public TableStore getStore() {
        return (TableStore) super.getStore();
    }
}
