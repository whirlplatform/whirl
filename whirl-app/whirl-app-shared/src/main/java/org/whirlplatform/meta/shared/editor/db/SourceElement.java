package org.whirlplatform.meta.shared.editor.db;

import org.whirlplatform.meta.shared.editor.AbstractElement;

@SuppressWarnings("serial")
public abstract class SourceElement extends AbstractElement {

	private String source;

	public SourceElement() {

	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSource() {
		return source;
	}

}
