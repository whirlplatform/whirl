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
import org.whirlplatform.meta.shared.editor.db.TableColumnElement;

public class ColumnComboBox extends ComboBox<TableColumnElement> {

    public ColumnComboBox(ColumnStore store) {
        super(store, new ElementLabelProvider<TableColumnElement>());

        setTriggerAction(TriggerAction.ALL);
        setForceSelection(true);
        addTriggerClickHandler(new TriggerClickHandler() {
            @Override
            public void onTriggerClick(TriggerClickEvent event) {
                if (!isExpanded()) {
                    getStore().getLoader().load(getText());
                }
            }
        });
        addBeforeQueryHandler(new BeforeQueryHandler<TableColumnElement>() {
            @Override
            public void onBeforeQuery(BeforeQueryEvent<TableColumnElement> event) {
                getStore().getLoader().load(event.getQuery());
            }
        });
        DropTarget target = new DropTarget(this);
        target.addDropHandler(new DndDropHandler() {
            @Override
            public void onDrop(DndDropEvent event) {
                if (event.getData() instanceof TableColumnElement) {
                    setValue((TableColumnElement) event.getData());
                }
            }
        });
        target.addDragEnterHandler(new DndDragEnterHandler() {
            @Override
            public void onDragEnter(DndDragEnterEvent event) {
                if (event.getDragSource().getData() instanceof TableColumnElement) {
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

        addSelectionHandler(new SelectionHandler<TableColumnElement>() {

            @Override
            public void onSelection(SelectionEvent<TableColumnElement> event) {
                if (event.getSelectedItem().getId().isEmpty()) {
                    ColumnComboBox.this.setValue(null);
                }
            }
        });
    }

    @Override
    public ColumnStore getStore() {
        return (ColumnStore) super.getStore();
    }

}
