package org.whirlplatform.editor.client.util;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.dnd.core.client.DndDragEnterEvent;
import com.sencha.gxt.dnd.core.client.DndDragEnterEvent.DndDragEnterHandler;
import com.sencha.gxt.dnd.core.client.DndDragLeaveEvent;
import com.sencha.gxt.dnd.core.client.DndDragLeaveEvent.DndDragLeaveHandler;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.dnd.core.client.DndDropEvent.DndDropHandler;
import com.sencha.gxt.dnd.core.client.DropTarget;
import com.sencha.gxt.widget.core.client.event.BeforeQueryEvent;
import com.sencha.gxt.widget.core.client.event.BeforeQueryEvent.BeforeQueryHandler;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.editor.ComponentElement;

public class ComponentComboBox extends ComboBox<ComponentElement> {

    public ComponentComboBox(ComponentStore store) {
        //        super(store, new ElementLabelProvider<ComponentElement>());
        super(store, new LabelProvider<ComponentElement>() {
            @Override
            public String getLabel(ComponentElement item) {
                return item.getName() + " - " + item.getProperty(PropertyType.Code);
            }
        });

        setTriggerAction(TriggerAction.ALL);
        setForceSelection(true);
        addBeforeQueryHandler(new BeforeQueryHandler<ComponentElement>() {
            @Override
            public void onBeforeQuery(BeforeQueryEvent<ComponentElement> event) {
                getStore().getLoader().load(event.getQuery());
            }
        });
        DropTarget target = new DropTarget(this);
        target.addDropHandler(new DndDropHandler() {
            @Override
            public void onDrop(DndDropEvent event) {
                if (event.getData() instanceof ComponentElement) {
                    setValue((ComponentElement) event.getData());
                }
            }
        });
        target.addDragEnterHandler(new DndDragEnterHandler() {
            @Override
            public void onDragEnter(DndDragEnterEvent event) {
                if (event.getDragSource().getData() instanceof ComponentElement) {
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

        addSelectionHandler(new SelectionHandler<ComponentElement>() {

            @Override
            public void onSelection(SelectionEvent<ComponentElement> event) {
                if (event.getSelectedItem().getId().isEmpty()) {
                    ComponentComboBox.this.setValue(null);
                }
            }
        });
    }

    @Override
    public ComponentStore getStore() {
        return (ComponentStore) super.getStore();
    }

}
