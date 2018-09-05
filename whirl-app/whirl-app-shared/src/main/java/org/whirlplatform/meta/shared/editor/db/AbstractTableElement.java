package org.whirlplatform.meta.shared.editor.db;

import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.editor.AbstractElement;
import org.whirlplatform.meta.shared.editor.PropertyValue;

@SuppressWarnings("serial")
public abstract class AbstractTableElement extends AbstractElement {

	protected PropertyValue title = new PropertyValue(DataType.STRING);

	protected String code;

	protected boolean emptyRow;

	public AbstractTableElement() {

	}

	public PropertyValue getTitle() {
		return title;
	}

	public void setTitle(PropertyValue title) {
		this.title = title;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setEmptyRow(boolean emptyRow) {
		this.emptyRow = emptyRow;
	}

	public boolean isEmptyRow() {
		return emptyRow;
	}

}
