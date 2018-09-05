package org.whirlplatform.component.client.state;

import com.sencha.gxt.widget.core.client.event.ColumnMoveEvent;
import com.sencha.gxt.widget.core.client.event.ColumnMoveEvent.ColumnMoveHandler;
import com.sencha.gxt.widget.core.client.event.ColumnWidthChangeEvent;
import com.sencha.gxt.widget.core.client.event.ColumnWidthChangeEvent.ColumnWidthChangeHandler;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnHiddenChangeEvent;
import com.sencha.gxt.widget.core.client.grid.ColumnHiddenChangeEvent.ColumnHiddenChangeHandler;
import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.storage.client.StorageHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ColumnConfigStore implements ColumnHiddenChangeHandler, ColumnWidthChangeHandler,
        ColumnMoveHandler {

    String compId;

    AbstractMetadataStateStore<Serializable> store;

    // Может просто установить LOCAL?
    public ColumnConfigStore(StateScope scope, ClassMetadata metadata,
                             String compId) {
        store = new AbstractMetadataStateStore<Serializable>(scope, metadata) {
            {
                switch (scope) {
                    case MEMORY:
                        storage = StorageHelper.memory();
                        break;
                    case SESSION:
                        storage = StorageHelper.session();
                        break;
                    case LOCAL:
                        storage = StorageHelper.local();
                        break;
                    default:
                }
            }
        };
    }

    private String getKey(String type) {
        return compId + "/" + type;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void onColumnMove(ColumnMoveEvent event) {
        ArrayList<String> positions = new ArrayList<String>();
        for (ColumnConfig conf : event.getSource().getColumns()) {
            positions.add(conf.getPath());
        }
        store.save(getKey("positions"), positions);
    }

    @Override
    public void onColumnWidthChange(ColumnWidthChangeEvent event) {
        ColumnConfig<?, ?> conf = event.getColumnConfig();
        store.save(getKey("width/" + conf.getPath()), conf.getWidth());
    }

    @Override
    public void onColumnHiddenChange(ColumnHiddenChangeEvent event) {
        ColumnConfig<?, ?> conf = event.getColumnConfig();
        store.save(getKey("hidden/" + conf.getPath()), conf.isHidden());
    }

    public List<String> getPositions() {
        return (List) store.restore(getKey("positions"));
    }

    public Integer getWidth(String column) {
        return (Integer) store.restore(getKey("width/" + column));
    }

    public Boolean isHidden(String column) {
        return (Boolean) store.restore(getKey("hidden/" + column));
    }
}
