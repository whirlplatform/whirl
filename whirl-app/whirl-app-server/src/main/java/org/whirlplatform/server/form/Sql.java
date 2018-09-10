package org.whirlplatform.server.form;

import org.whirlplatform.server.global.SrvConstant;

import java.util.Collection;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * 
 */
public class Sql {

	public static class SqlComparator implements Comparator<Sql> {

		// Верхние запросы больше, вложенные - меньше.
		// Если строки запросов совпадают, сравнение по содержимому.
		@Override
		public int compare(Sql o1, Sql o2) {
			int top1 = o1.getTop().getRow();
			int top2 = o2.getTop().getRow();
			if (top1 < top2) {
				return -1;
			} else if (top1 > top2) {
				return 2;
			} else {
				int bot1 = o1.getBottom().getRow();
				int bot2 = o2.getBottom().getRow();
				if (bot1 < bot2) {
					return -1;
				} else if (bot1 > bot2) {
					return 2;
				} else {
					return o1.getSql().compareToIgnoreCase(o2.getSql());
				}
			}
		}

	}

	private String obj;

	private String dataSourceAlias = SrvConstant.DEFAULT_CONNECTION;
	
	private String sql;

	private String textNoData;

	private String parentObj;

	private Sql parent;

	private RowElementWrapper top;

	private RowElementWrapper bottom;

	private SortedSet<Sql> children = new TreeSet<Sql>(new SqlComparator());

	public String getObj() {
		return obj;
	}

	public void setObj(String obj) {
		this.obj = obj;
	}
	
	public String getDataSourceAlias() {
		return dataSourceAlias;
	}
	
	public void setDataSourceAlias(String dataSourceAlias) {
		this.dataSourceAlias = dataSourceAlias;
	}
	
	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getTextNoData() {
		return textNoData;
	}

	public void setTextNoData(String textNoData) {
		this.textNoData = textNoData;
	}

	public String getParentObj() {
		return parentObj;
	}

	public void setParentObj(String parentObj) {
		this.parentObj = parentObj;
	}

	public RowElementWrapper getTop() {
		return top;
	}

	public void setTop(RowElementWrapper top) {
		this.top = top;
	}

	public RowElementWrapper getBottom() {
		return bottom;
	}

	public void setBottom(RowElementWrapper bottom) {
		this.bottom = bottom;
	}

	public void addChild(Sql child) {
		children.add(child);
	}

	public Collection<Sql> getChildren() {
		return children;
	}

	public boolean hasChildren() {
		return !children.isEmpty();
	}

	public Sql getParent() {
		return parent;
	}

	public void setParent(Sql parent) {
		this.parent = parent;
		if (parent != null) {
			parent.addChild(this);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((obj == null) ? 0 : obj.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Sql other = (Sql) obj;
		if (this.obj == null) {
            return other.obj == null;
        } else return this.obj.equals(other.obj);
    }

}
