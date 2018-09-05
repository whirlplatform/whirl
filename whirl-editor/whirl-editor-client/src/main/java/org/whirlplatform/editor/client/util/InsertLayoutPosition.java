package org.whirlplatform.editor.client.util;

public class InsertLayoutPosition implements LayoutPosition {

	private int index;

	public InsertLayoutPosition(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}
