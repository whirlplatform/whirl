package org.whirlplatform.editor.client.view;

import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;
import org.whirlplatform.editor.client.component.PropertyValueField;
import org.whirlplatform.editor.client.presenter.DynamicTablePresenter;
import org.whirlplatform.editor.client.presenter.DynamicTablePresenter.IDynaimcTableView;
import org.whirlplatform.editor.shared.i18n.EditorMessage;
import org.whirlplatform.meta.shared.editor.LocaleElement;
import org.whirlplatform.meta.shared.editor.PropertyValue;

import java.util.Collection;

public class DynamicTableView extends ContentPanel implements IDynaimcTableView {

	private DynamicTablePresenter presenter;
	
	private VerticalLayoutContainer container;
	
	private FieldLabel labelTableTitle;
	private PropertyValueField tableTitle;
	
	private FieldLabel labelCode;
	private TextField code;
	
	private FieldLabel labelEmptyRow;
	private CheckBox emptyRow;
	
	private FieldLabel labelMetadataFunction;
	private TextField metadataFunction;
	
	private FieldLabel labelDataFunction;
	private TextField dataFunction;
	
	private FieldLabel labelInsertFunction;
	private TextField insertFunction;
	
	private FieldLabel labelUpdateFunction;
	private TextField updateFunction;
	
	private FieldLabel labelDeleteFunction;
	private TextField deleteFunction;
	
	@Override
	public void setPresenter(DynamicTablePresenter presenter) {
		this.presenter = presenter;
		initUI();
	}

	@Override
	public DynamicTablePresenter getPresenter() {
		return presenter;
	}

	public void initUI() {
		setHeaderVisible(true);
		if (container == null) {
			container = new VerticalLayoutContainer();
			initFields();
			setWidget(container);
		}
	}
	
	public void initFields() {
		tableTitle = new PropertyValueField();
		labelTableTitle = new FieldLabel(tableTitle,
				EditorMessage.Util.MESSAGE.title());
		container.add(labelTableTitle, new VerticalLayoutData(1, -1,
				new Margins(10, 10, 0, 10)));
		
		code = new TextField();
		labelCode = new FieldLabel(code,
				EditorMessage.Util.MESSAGE.table_code());
		container.add(labelCode, new VerticalLayoutData(1, -1, new Margins(10,
				10, 0, 10)));
		
		emptyRow = new CheckBox();
		emptyRow.setBoxLabel("");
		labelEmptyRow = new FieldLabel(emptyRow,
				EditorMessage.Util.MESSAGE.table_empty_row());
		container.add(labelEmptyRow, new VerticalLayoutData(-1, -1, new Margins(
				10, 10, 0, 10)));
		
		metadataFunction = new TextField();
		labelMetadataFunction = new FieldLabel(metadataFunction, EditorMessage.Util.MESSAGE.table_metadata_function());
		container.add(labelMetadataFunction, new VerticalLayoutData(1, -1, new Margins(10,
				10, 0, 10)));
		
		dataFunction = new TextField();
		labelDataFunction = new FieldLabel(dataFunction, EditorMessage.Util.MESSAGE.table_data_function());
		container.add(labelDataFunction, new VerticalLayoutData(1, -1, new Margins(10,
				10, 0, 10)));
		
		insertFunction = new TextField();
		labelInsertFunction = new FieldLabel(insertFunction, EditorMessage.Util.MESSAGE.table_insert_function());
		container.add(labelInsertFunction, new VerticalLayoutData(1, -1, new Margins(10,
				10, 0, 10)));
		
		updateFunction = new TextField();
		labelUpdateFunction = new FieldLabel(updateFunction, EditorMessage.Util.MESSAGE.table_update_function());
		container.add(labelUpdateFunction, new VerticalLayoutData(1, -1, new Margins(10,
				10, 0, 10)));
		
		deleteFunction = new TextField();
		labelDeleteFunction = new FieldLabel(deleteFunction, EditorMessage.Util.MESSAGE.table_delete_function());
		container.add(labelDeleteFunction, new VerticalLayoutData(1, -1, new Margins(10,
				10, 0, 10)));
	}

	@Override
	public void setTableTitle(PropertyValue title) {
		this.tableTitle.setPropertyValue(title);
	}

	@Override
	public PropertyValue getTableTitle() {
		return tableTitle.getPropertyValue();
	}

	@Override
	public void setCode(String code) {
		this.code.setValue(code);
	}

	@Override
	public String getCode() {
		return code.getValue();
	}

	@Override
	public void setMetadataFunction(String metadataFunction) {
		this.metadataFunction.setValue(metadataFunction);
	}

	@Override
	public String getMetadataFunction() {
		return metadataFunction.getValue();
	}

	@Override
	public void setDataFunction(String dataFunction) {
		this.dataFunction.setValue(dataFunction);
	}

	@Override
	public String getDataFunction() {
		return dataFunction.getValue();
	}

	@Override
	public void setInsertFunction(String insertFunction) {
		this.insertFunction.setValue(insertFunction);
	}

	@Override
	public String getInsertFunction() {
		return insertFunction.getValue();
	}

	@Override
	public void setUpdateFunction(String updateFunction) {
		this.updateFunction.setValue(updateFunction);
	}

	@Override
	public String getUpdateFunction() {
		return updateFunction.getValue();
	}

	@Override
	public void setDeleteFunction(String detleteFunction) {
		this.deleteFunction.setValue(detleteFunction);
	}

	@Override
	public String getDeleteFunction() {
		return deleteFunction.getValue();
	}

	@Override
	public void setEmptyRow(boolean emptyRow) {
		this.emptyRow.setValue(emptyRow);
	}

	@Override
	public boolean getEmptyRow() {
		return emptyRow.getValue();
	}

	@Override
	public void setLocales(Collection<LocaleElement> locales,
			LocaleElement defaultLocale) {
		tableTitle.setLocales(defaultLocale, locales);
	}

	@Override
	public void clearFields() {
		tableTitle.clear();
		code.clear();
		emptyRow.clear();
		metadataFunction.clear();
		dataFunction.clear();
		insertFunction.clear();
		updateFunction.clear();
		deleteFunction.clear();
	}
	
	@Override
	public void setHeaderText(String headerText) {
		setHeading(headerText);
	}
	
	@Override
	public void setEnableAll(boolean enabled) {
		labelTableTitle.setEnabled(enabled);
		labelCode.setEnabled(enabled);
		labelEmptyRow.setEnabled(enabled);
		labelMetadataFunction.setEnabled(enabled);
		labelDataFunction.setEnabled(enabled);
		labelInsertFunction.setEnabled(enabled);
		labelUpdateFunction.setEnabled(enabled);
		labelDeleteFunction.setEnabled(enabled);
	}
}
