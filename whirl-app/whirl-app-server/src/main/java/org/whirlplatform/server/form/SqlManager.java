package org.whirlplatform.server.form;

import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.google.common.collect.TreeRangeMap;
import org.whirlplatform.server.form.Sql.SqlComparator;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class SqlManager {

	private RangeMap<Integer, Sql> ranges = TreeRangeMap.create();
	private Set<Sql> allRow = new TreeSet<Sql>(new SqlComparator());

	private SortedSet<Sql> rootRow = new TreeSet<Sql>(new SqlComparator());

	private Sql beforeSql;

	public void init() {
		for (Sql r : allRow) {
			if (r.getParent() == null) {
				initSubRequests(r);
			}
		}
		for (Sql r: allRow) {
			if (r.getParent() == null) {
				rootRow.add(r);
			}
		}
	}

	private void initSubRequests(Sql parent) {
		for (Sql r : allRow) {
			if (r.getTop().getRow() >= parent.getTop().getRow()
					&& r.getBottom().getRow() <= parent.getBottom().getRow()
					&& r != parent && r.getParent() == null) {
				r.setParent(parent);
				initSubRequests(r);
			}
		}
	}

	public void addRowSql(Sql sql) {
		ranges.put(
				Range.closed(sql.getTop().getRow(), sql.getBottom().getRow()),
				sql);
		allRow.add(sql);
	}

	public boolean isRowRoot(Sql sql) {
		return sql.getParentObj() == null;
	}

	public Sql getBeforeSql() {
		return beforeSql;
	}

	public void setBeforeSql(Sql sql) {
		beforeSql = sql;
	}

	public Set<Sql> getRowRoot() {
		return rootRow;
	}

	public RowElementWrapper getFirstSqlRow() {
		if (rootRow.size() == 0) {
			return null;
		}
		Sql sql = rootRow.first();
		return sql.getTop();
	}

	public RowElementWrapper getLastSqlRow() {
		if (rootRow.size() == 0) {
			return null;
		}
		Sql sql = rootRow.last();
		return sql.getBottom();
	}

}
