package org.whirlplatform.component.client.check;


import com.google.gwt.event.shared.HandlerRegistration;
import com.sencha.gxt.widget.core.client.event.CheckChangedEvent;
import com.sencha.gxt.widget.core.client.event.CheckChangedEvent.CheckChangedHandler;
import com.sencha.gxt.widget.core.client.selection.AbstractStoreSelectionModel;
import org.whirlplatform.meta.shared.data.RowModelData;

public class CheckSelectionModel extends
		AbstractStoreSelectionModel<RowModelData> {

	private HandlerRegistration handlerRegistrartion;
	private CheckBoxList checkList;

	@Override
	protected void onSelectChange(RowModelData model, boolean select) {
		checkList.setChecked(model, select, false);
	}

	public void bindCheckList(CheckBoxList checkList) {
		this.checkList = checkList;
		if (handlerRegistrartion != null) {
			handlerRegistrartion.removeHandler();
			handlerRegistrartion = null;
		}
		handlerRegistrartion = checkList
				.addCheckChangedHandler(new CheckChangedHandler<RowModelData>() {

					@Override
					public void onCheckChanged(
							CheckChangedEvent<RowModelData> event) {
						select(event.getItems(), true);
					}
				});
		bind(checkList.getStore());
	}

}
