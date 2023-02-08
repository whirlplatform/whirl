package org.whirlplatform.component.client.check;


import com.google.gwt.event.shared.HandlerRegistration;
import com.sencha.gxt.widget.core.client.event.CheckChangedEvent;
import com.sencha.gxt.widget.core.client.event.CheckChangedEvent.CheckChangedHandler;
import com.sencha.gxt.widget.core.client.selection.AbstractStoreSelectionModel;
import org.whirlplatform.meta.shared.data.ListModelData;

public class CheckSelectionModel extends
    AbstractStoreSelectionModel<ListModelData> {

    private HandlerRegistration handlerRegistrartion;
    private CheckBoxList checkList;

    @Override
    protected void onSelectChange(ListModelData model, boolean select) {
        checkList.setChecked(model, select, false);
    }

    public void bindCheckList(CheckBoxList checkList) {
        this.checkList = checkList;
        if (handlerRegistrartion != null) {
            handlerRegistrartion.removeHandler();
            handlerRegistrartion = null;
        }
        handlerRegistrartion = checkList
            .addCheckChangedHandler(new CheckChangedHandler<ListModelData>() {

                @Override
                public void onCheckChanged(
                    CheckChangedEvent<ListModelData> event) {
                    select(event.getItems(), true);
                }
            });
        bind(checkList.getStore());
    }

}
