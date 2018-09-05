package org.whirlplatform.editor.client.view.design;

import org.whirlplatform.meta.shared.editor.FormElement;

public class FormIndexData {

	private FormElement form;
	private Integer index;

	public FormIndexData(FormElement form, int index) {
		this.form = form;
		this.index = index;
	}

	public FormElement getForm() {
		return form;
	}

	public Integer getInsert() {
		return index;
	}
}
