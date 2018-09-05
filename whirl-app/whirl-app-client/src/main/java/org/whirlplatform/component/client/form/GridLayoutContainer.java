package org.whirlplatform.component.client.form;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.*;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IndexedPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.GXT;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.ComponentHelper;
import com.sencha.gxt.widget.core.client.container.HasLayout;
import com.sencha.gxt.widget.core.client.container.ResizeContainer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GridLayoutContainer extends ResizeContainer {

	public class Cell {

		private int row;
		private int column;

		public Cell(int row, int column) {
			this.row = row;
			this.column = column;
		}

		public int getColumn() {
			return column;
		}

		public int getRow() {
			return row;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + column;
			result = prime * result + row;
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
			Cell other = (Cell) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (column != other.column)
				return false;
            return row == other.row;
        }

		private GridLayoutContainer getOuterType() {
			return GridLayoutContainer.this;
		}

		@Override
		public String toString() {
			return "[" + row + ":" + column + "]";
		}

	}

	private class GridFlexTable extends FlexTable {

		void doAttach() {
			onAttach();
		}

		void doDetach() {
			onDetach();
		}

		@Override
		protected void onAttach() {
			if (!isAttached()) {
				super.onAttach();
			}
		}

		@Override
		protected void onDetach() {
			if (isAttached()) {
				super.onDetach();
			}
		}

		@Override
		protected void doAttachChildren() {
			for (Widget w : widgets.keySet()) {
				ComponentHelper.doAttach(w);
			}
		}

		@Override
		protected void doDetachChildren() {
			for (Widget w : widgets.keySet()) {
				if (w.isAttached()) {
					ComponentHelper.doDetach(w);
				}
			}
		}

		@Override
		public void setWidget(int row, int column, Widget widget) {
			super.setWidget(row, column, widget);
			if (isAttached()) {
				ComponentHelper.doAttach(widget);
			}
		}
		
		@Override
		protected void adopt(Widget child) {
			assert (child.getParent() == null);
		    child.setParent(GridLayoutContainer.this);
		}
		
		@Override
		public boolean remove(Widget widget) {
			boolean result = super.remove(widget);
			if (!result) {
				// Physical detach.
				Element elem = widget.getElement();
				DOM.getParent(elem).removeChild(elem);

				// Logical detach.
				widgetMap.removeByElement(elem);
			}
			if (isAttached()) {
				ComponentHelper.doDetach(widget);
			}
			return result;
		}
	}

	private GridFlexTable table;

	private int numRows = 0;
	private int numColumns = 0;

	private Cell[][] cellIndexes;
	private Cell[][] gridIndexes;

	private Map<String, Double> fixedRowHeight = new HashMap<>();
	private Map<String, Double> fixedColumnWidth = new HashMap<>();
	private Map<String, Double> percentRowHeight = new HashMap<>();
	private Map<String, Double> percentColumnWidth = new HashMap<>();

	private Map<String, Double> currentColumnWidth = new HashMap<>();
	private Map<String, Double> currentRowHeight = new HashMap<>();

	private Map<Widget, Cell> widgets = new HashMap<Widget, Cell>();
	private Map<Cell, Widget> cells = new HashMap<Cell, Widget>();

	public GridLayoutContainer() {
		this(0, 0);
	}

	public GridLayoutContainer(int rows, int cols) {
		super();
		table = new GridFlexTable();
		table.setCellPadding(1);
		table.setCellSpacing(0);
		resizeRows(rows);
		resizeColumns(cols);
		setElement(table.getElement());
	}

	@Override
	protected void onResize(int width, int height) {
		table.setPixelSize(width, height);
		super.onResize(width, height);
	}

	public void resizeRows(int rows) {
		if (numRows == rows) {
			return;
		}
		if (rows < 0) {
			throw new IndexOutOfBoundsException(
					"Can not set number of rows to " + rows);
		}

		if (table.getRowCount() > rows) {
			for (int i = table.getRowCount(); i > rows; i--) {
				int rowToRemove = i - 1;
				for (int j = 0; j < table.getCellCount(rowToRemove); j++) {
					Widget w = table.getWidget(rowToRemove, j);
					remove(w);
				}
				table.removeRow(rowToRemove);
			}
		} else {
			for (int i = table.getRowCount(); i < rows; i++) {
				table.insertRow(i);
				// добавляем колонки
				for (int j = 0; j < numColumns; j++) {
					table.insertCell(i, j);
				}
			}
		}
		numRows = rows;
		syncIndexes();
	}

	public void resizeColumns(int columns) {
		if (numColumns == columns) {
			return;
		}
		if (columns < 0) {
			throw new IndexOutOfBoundsException(
					"Can not set number of columns to " + columns);
		}

		for (int i = 0; i < table.getRowCount(); i++) {
			if (table.getCellCount(i) == columns) {
				continue;
			}
			if (table.getCellCount(i) > columns) {
				for (int j = table.getCellCount(i); j > columns; j--) {
					Widget w = table.getWidget(i, j - 1);
					remove(w);
					table.removeCell(i, j - 1);
				}
			} else {
				for (int j = table.getCellCount(i); j < columns; j++) {
					table.insertCell(i, j);
				}
			}
		}
		numColumns = columns;
		syncIndexes();
	}

	public void resize(int rows, int columns) {
		resizeRows(rows);
		resizeColumns(columns);
	}

	public void insertRow(int beforeRow) {
		table.insertRow(beforeRow);
		for (int i = 0; i < numColumns; i++) {
//			if (beforeRow == numRows) {
				table.insertCell(beforeRow, i);
//			} else {                                               // Зачем это нужно было?
//				Cell cell = getTablePositionByGrid(beforeRow, i);
//				table.insertCell(beforeRow, i);
//			}
		}
		numRows++;
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				doLayout();
			}
		});
		syncIndexes();
	}

	public void insertColumn(int beforeColumn) {
		for (int i = 0; i < table.getRowCount(); i++) {
//			if (beforeColumn == numColumns) {
				table.insertCell(i, beforeColumn);
//			} else {                                               // Зачем это нужно было?
//				Cell cell = getTablePositionByGrid(i, beforeColumn);
//				table.insertCell(i, beforeColumn);
//			}
		}
		numColumns++;
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				doLayout();
			}
		});
		syncIndexes();
	}

	public void deleteRow(int row) {
		table.removeRow(row);
		numRows--;
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				doLayout();
			}
		});
		syncIndexes();
	}

	public void deleteColumn(int column) {
		for (int i = 0; i < table.getRowCount(); i++) {
			Cell cell = getTablePositionByGrid(i, column);
			table.removeCell(cell.getRow(), cell.getColumn());
		}
		numColumns--;
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				doLayout();
			}
		});
	}

	public void setWidget(int row, int column, Widget child,
			GridLayoutData layoutData) {
		if (child != null) {
			child.asWidget().setLayoutData(layoutData);
		}
		setWidget(row, column, child);
	}

	public void setWidget(int row, int column, Widget child) {
		if (row < 0) {
			throw new IndexOutOfBoundsException(
					"Row index can not be negative: " + row);
		}
		if (column < 0) {
			throw new IndexOutOfBoundsException(
					"Column index can not be negative: " + column);
		}
		if (row + 1 > numRows) {
			throw new IndexOutOfBoundsException(
					"Can not set widget at the row " + row
							+ ". Container's row count is " + numRows + ".");
		}
		if (column + 1 > numColumns) {
			throw new IndexOutOfBoundsException(
					"Can not set widget at the column " + column
							+ ". Container's columns count is " + numColumns
							+ ".");
		}

		child.getElement().getStyle().setPosition(Position.RELATIVE);

		setMainWidget(row, column, child);
	}

	private void setTableWidget(int row, int column, Widget child) {
		Cell cellPosition = getTablePositionByGrid(row, column);
		table.setWidget(cellPosition.getRow(), cellPosition.getColumn(), child);
		getChildren().add(child);
	}

	private void removeTableWidget(Widget child) {
		table.remove(child);
		getChildren().remove(child);
	}

	private void setMainWidget(int row, int column, Widget child) {
		setTableWidget(row, column, child);
		Cell c = new Cell(row, column);
		widgets.put(child, c);
		cells.put(c, child);
	}

	private Widget getMainWidget(int row, int column) {
		return cells.get(new Cell(row, column));
	}

	private Widget removeMainWidget(Widget child) {
		removeTableWidget(child);
		Widget result = cells.remove(widgets.remove(child));
		return result;
	}

	private Cell getWidgetPosition(Widget widget) {
		return widgets.get(widget);

	}

	@Override
	public boolean remove(Widget child) {
		if (child == null) {
			return false;
		}
		removeMainWidget(child);
		return true;
	}

	@Override
	public boolean remove(int index) {
		Widget w = getWidget(index);
		return remove(w);
	}

	public boolean remove(int row, int column) {
		Widget w = getWidget(row, column);
		return remove(w);
	}

	public Widget getWidget(int row, int column) {
		Widget result;
		result = getMainWidget(row, column);
		return result;
	}

	public Cell getGridPositionByTable(int cellRow, int cellColumn) {
		return cellIndexes[cellRow][cellColumn];
	}

	public Cell getTablePositionByGrid(int gridRow, int gridColumn) {
		return gridIndexes[gridRow][gridColumn];
	}

	public void syncIndexes() {
		cellIndexes = new Cell[numRows][numColumns];
		gridIndexes = new Cell[numRows][numColumns];
		Map<String, Map<String, Boolean>> cellOccupied = new HashMap<>();

		// пробегаемся по всем ячейкам
		for (int y = 0; y < table.getRowCount(); y++) {
			for (int x = 0; x < table.getCellCount(y); x++) {
				int colSpan = table.getFlexCellFormatter().getColSpan(y, x);
				int rowSpan = table.getFlexCellFormatter().getRowSpan(y, x);

				int cx = x;
				int cy = y;
				if (cellOccupied.get(String.valueOf(cy)) == null) {
					cellOccupied
							.put(String.valueOf(cy), new HashMap<>());
				}
				while (cellOccupied.get(String.valueOf(cy)).get(
						String.valueOf(cx)) != null) { // пропускаем уже занятые
														// ячейки в строке
					cx++;
				}

				// помечаем занятые текущим элементом ячейки
				for (int tx = cx; tx < cx + colSpan; tx++) {
					for (int ty = cy; ty < cy + rowSpan; ty++) {
						if (cellOccupied.get(String.valueOf(ty)) == null) {
							cellOccupied.put(String.valueOf(ty),
									new HashMap<>());
						}
						cellOccupied.get(String.valueOf(ty)).put(
								String.valueOf(tx), true);
					}
				}
				// System.out.println("x:" + x + " y:" + y);
				cellIndexes[y][x] = new Cell(cy, cx);
				if (gridIndexes.length > cy && gridIndexes[cy].length > cx) {
					gridIndexes[cy][cx] = new Cell(y, x);
				}
			}
		}
	}

	public void correctSpannedCells(int row, int column, int oldColSpan,
			int newColSpan) {
		if (oldColSpan < newColSpan) { // если объединение добавляется
			// удаляем следующие объединенные ячейки в строке
			int del = newColSpan - oldColSpan;
			while (del != 0) {
				if (table.isCellPresent(row, column + 1)) {
					table.removeCell(row, column + 1);
				}
				del--;
			}
		} else if (oldColSpan > newColSpan) { // если объединение уменьшается
			// добавляем следующие не объединенные ячейки в строке
			int add = oldColSpan - newColSpan;
			while (add != 0) {
				table.insertCell(row, column + 1);
				add--;
			}
		}
	}

	public void correctRowSpannedCells(int row, int column, int oldRowSpan,
			int newRowSpan) {
		if (oldRowSpan < newRowSpan) {
			table.removeCell(row, column);
		} else if (oldRowSpan > newRowSpan) {
			table.insertCell(row, column);
		} else if (!table.isCellPresent(row, column)) {
			table.insertCell(row, column);
		}
	}

	public void setSpan(int row, int column, int rowspan, int colspan) {
		setSpan(row, column, rowspan, colspan, true);
	}

	public void setSpan(int row, int column, int rowSpan, int colSpan,
			boolean layout) {
		if (rowSpan < 1) {
			throw new IndexOutOfBoundsException(
					"Row span can not be less than 1.");
		}
		Cell tablePosition = getTablePositionByGrid(row, column);
		if (tablePosition == null) {
			return;
		}
		int tableRow = tablePosition.getRow();
		int tableColumn = tablePosition.getColumn();

		int currRowSpan = getTable().getFlexCellFormatter().getRowSpan(
				tableRow, tableColumn);
		int currColSpan = getTable().getFlexCellFormatter().getColSpan(
				tableRow, tableColumn);

		// сначала восстановим ячейки
		if (currColSpan != 1) {
			correctSpannedCells(tableRow, tableColumn, currColSpan, 1);
		}

		// для начальной строки просто меняем объединение колонок
		correctSpannedCells(tableRow, tableColumn, 1, colSpan);

		if (currRowSpan != rowSpan || currRowSpan > 1) { // если объединение
															// строк изменилось
			// изменяем в каждой добавленной/удаленной строке кроме первой
			// количество ячеек
			int min;
			int max;
			if (currRowSpan == rowSpan) {
				min = 1;
				max = currRowSpan;
			} else {
				min = Math.min(currRowSpan, rowSpan);
				max = Math.max(currRowSpan, rowSpan);
			}
			for (int r = tableRow + 1; r < tableRow + 1 + max - min; r++) {
				// восстановим ячейки
				if (currColSpan != 1) {
					correctSpannedCells(r, tableColumn, currColSpan, 1);
				}
				if (currRowSpan != 1) {
					correctRowSpannedCells(r, tableColumn, currRowSpan, 1);
				}
				// меняем объединения
				// correctSpannedCells(r, tableColumn, 1, colSpan);
				// correctRowSpannedCells(r, tableColumn, 1, rowSpan);
				if (colSpan != 1) {
					correctSpannedCells(r, getTablePositionByGrid(r, column)
							.getColumn(), 1, colSpan);
				}
				if (rowSpan != 1) {
					correctRowSpannedCells(r, getTablePositionByGrid(r, column)
							.getColumn(), 1, rowSpan);
				}
			}
		}

		getTable().getFlexCellFormatter().setRowSpan(tableRow, tableColumn,
				rowSpan);
		getTable().getFlexCellFormatter().setColSpan(tableRow, tableColumn,
				colSpan);
		syncIndexes();
		if (layout) {
			doLayout();
		}
	}

	public int getRowSpan(int row, int column) {
		Cell cell = getTablePositionByGrid(row, column);
		return table.getFlexCellFormatter().getRowSpan(cell.getRow(),
				cell.getColumn());
	}

	public int getColSpan(int row, int column) {

		Cell cell = getTablePositionByGrid(row, column);
		return table.getFlexCellFormatter().getColSpan(cell.getRow(),
				cell.getColumn());

	}

	public void setRowHeight(int row, double height) {
		if (0 < height && height <= 1) {
			percentRowHeight.put(String.valueOf(row), height);
		} else if (height > 1) {
			fixedRowHeight.put(String.valueOf(row), height);
		}
	}

	public double getRowHeight(int row) {
		String rowStr = String.valueOf(row);
		if (percentRowHeight.containsKey(rowStr)) {
			return percentRowHeight.get(rowStr);
		} else if (fixedRowHeight.containsKey(rowStr)) {
			return fixedRowHeight.get(rowStr);
		} else {
			return -1;
		}
	}

	public void setColumnWidth(int column, double width) {
		if (0 < width && width <= 1) {
			percentColumnWidth.put(String.valueOf(column), width);
		} else if (width > 1) {
			fixedColumnWidth.put(String.valueOf(column), width);
		}
	}

	public double getColumnWidth(int column) {
		String columnStr = String.valueOf(column);
		if (percentColumnWidth.containsKey(columnStr)) {
			return percentColumnWidth.get(columnStr);
		} else if (fixedColumnWidth.containsKey(columnStr)) {
			return fixedColumnWidth.get(columnStr);
		} else {
			return -1;
		}
	}

	@Override
	protected void onAttach() {
		super.onAttach();
		table.doAttach();
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		table.doDetach();
	}

	@Override
	protected void doLayout() {
		if (!isAttached()) {
			return;
		}

		// width calculations
		int fixedWidth = fixedWidth();
		double restWidth;
		if (lastSize != null) {
			restWidth = lastSize.getWidth();
		} else {
			restWidth = 0;
		}
		double freeWidth = restWidth - fixedWidth;
		freeWidth = freeWidth < 0 ? 0 : freeWidth;

		List<Integer> undefinedColumnWidth = new ArrayList<Integer>();
		for (int i = 0; i < numColumns; i++) {
			double w = 0;
			boolean needSet = true;
			if (restWidth > 0) {
				if (fixedColumnWidth.containsKey(String.valueOf(i))) {
					// set fixed width
//					w = (int) Math
//							.floor(fixedColumnWidth.get(String.valueOf(i)));
					w = fixedColumnWidth.get(String.valueOf(i));
					if (w > restWidth) {
						w = restWidth;
						restWidth = 0;
					} else {
						restWidth = restWidth - w;
					}
				} else if (percentColumnWidth.containsKey(String.valueOf(i))) {
					double percent = percentColumnWidth.get(String.valueOf(i));
//					w = (int) Math.floor(percent * freeWidth);
					w = percent * freeWidth;
					if (w > restWidth) {
						w = restWidth - table.getCellSpacing();
						restWidth = 0;
					} else {
						restWidth = restWidth - w;
					}
				} else {
					undefinedColumnWidth.add(i);
					needSet = false;
				}
			}

			if (needSet) {
				if (!(w < 1 && GXT.isIE())) {
					table.getColumnFormatter().setWidth(i,
							w + Unit.PX.getType());
				}
				currentColumnWidth.put(String.valueOf(i), w);
			}
		}

		freeWidth = restWidth;
		double percentSize = 1d / undefinedColumnWidth.size();

		for (Integer i : undefinedColumnWidth) {
			double w = 0;
			if (restWidth > 0) {
//				w = (int) Math.floor(freeWidth * percentSize)
//						- table.getCellSpacing();
				w = freeWidth * percentSize - table.getCellSpacing();
				if (w > restWidth) {
					w = restWidth;
					restWidth = 0;
				} else {
					restWidth = restWidth - w;
				}
			}
			if (!(w < 1 && GXT.isIE())) {
				table.getColumnFormatter().setWidth(i, w + Unit.PX.getType());
			}
			currentColumnWidth.put(String.valueOf(i), w);
		}

		// height calculations
		int fixedHeight = fixedHeight();
		double restHeight;
		if (lastSize != null) {
			restHeight = lastSize.getHeight();
		} else {
			restHeight = 0;
		}
		double freeHeight = restHeight - fixedHeight;
		freeHeight = freeHeight < 0 ? 0 : freeHeight;

		List<Integer> undefinedRowHeight = new ArrayList<Integer>();
		for (int i = 0; i < numRows; i++) {
			double h = 0;
			boolean needSet = true;
			if (restHeight > 0) {
				if (fixedRowHeight.containsKey(String.valueOf(i))) {
					// set fixed width
//					h = (int) Math.floor(fixedRowHeight.get(String.valueOf(i)));
					h = fixedRowHeight.get(String.valueOf(i));
					if (h > restHeight) {
						h = restHeight;
						restHeight = 0;
					} else {
						restHeight = restHeight - h;
					}
				} else if (percentRowHeight.containsKey(String.valueOf(i))) {
					double percent = percentRowHeight.get(String.valueOf(i));
//					h = (int) Math.floor(percent * freeHeight);
					h = percent * freeHeight;
					if (h > restHeight) {
						h = restHeight - table.getCellSpacing();
						restHeight = 0;
					} else {
						restHeight = restHeight - h;
					}
				} else {
					undefinedRowHeight.add(i);
					needSet = false;
				}
			}

			if (needSet) {
				table.getRowFormatter().getElement(i)
						.setAttribute("height", h + Unit.PX.getType());
				currentRowHeight.put(String.valueOf(i), h);
			}
		}

		freeHeight = restHeight;
		percentSize = 1d / undefinedRowHeight.size();

		for (Integer i : undefinedRowHeight) {
			double h = 0;
			if (restHeight > 0) {
//				h = (int) Math.floor(freeHeight * percentSize)
//						- table.getCellSpacing();
				h = freeHeight * percentSize - table.getCellSpacing();
				if (h > restHeight) {
					h = restHeight;
					restHeight = 0;
				} else {
					restHeight = restHeight - h;
				}
			}
			table.getRowFormatter().getElement(i)
					.setAttribute("height", h + Unit.PX.getType());
			currentRowHeight.put(String.valueOf(i), h);
		}

		for (Widget w : table) {
			doWidgetLayout(w);
		}
	}

	private GridLayoutData getLayoutData(Widget widget) {
		return (GridLayoutData) widget.getLayoutData();
	}

	private void doWidgetLayout(Widget widget) {
		GridLayoutData data = getLayoutData(widget);
		if (data == null) {
			return;
		}
		Cell p = getWidgetPosition(widget);

		int gridRow = p.getRow();
		int gridColumn = p.getColumn();
		Cell gridPos = getTablePositionByGrid(gridRow, gridColumn);
		int row = gridPos.getRow();
		int column = gridPos.getColumn();

		double height = -1; // -1 чтобы можно было понять, изменяли значение или нет
		if (data.getHeight() > 1) {
			height = (int) data.getHeight();
		} else if (data.getHeight() >= 0) {
			int rowSpan = table.getFlexCellFormatter().getRowSpan(row, column) - 1;
			for (int i = gridRow; i <= gridRow + rowSpan; i++) {
				if (currentRowHeight.get(String.valueOf(i)) != null) {
					height = height + currentRowHeight.get(String.valueOf(i)) + 1;
				}
			}
			int margins = data.getMargins().getTop()
					+ data.getMargins().getBottom() + 2;
			height = (int) Math.floor((height - margins) * data.getHeight());
		}

		if (height != -1) {
			widget.setHeight(height + Unit.PX.getType());
		}

		double width = -1; // -1 чтобы можно было понять, изменяли значение или нет
		if (data.getWidth() > 1) {
			width = (int) data.getWidth();
		} else if (data.getWidth() >= 0) {
			int colSpan = table.getFlexCellFormatter().getColSpan(row, column) - 1;
			for (int i = gridColumn; i <= gridColumn + colSpan; i++) {
				if (currentColumnWidth.get(String.valueOf(i)) != null) {
					width = width + currentColumnWidth.get(String.valueOf(i));
				}
			}
			int margins = data.getMargins().getLeft()
					+ data.getMargins().getRight() + 2;
			width = (int) Math.floor((width - margins) * data.getWidth());
		}

		if (width != -1) {
			widget.setWidth(width + Unit.PX.getType());
		}

		if (data.getHorizontalAlignment() != null) {
			table.getCellFormatter().setHorizontalAlignment(row, column,
					data.getHorizontalAlignment());
		}
		if (data.getVerticalAlignment() != null) {
			table.getCellFormatter().setVerticalAlignment(row, column,
					data.getVerticalAlignment());
		}
		Margins margins = data.getMargins();
		if (margins != null) {
			widget.getElement().getStyle()
					.setMarginTop(margins.getTop(), Unit.PX);
			widget.getElement().getStyle()
					.setMarginRight(margins.getRight(), Unit.PX);
			widget.getElement().getStyle()
					.setMarginBottom(margins.getBottom(), Unit.PX);
			widget.getElement().getStyle()
					.setMarginLeft(margins.getLeft(), Unit.PX);
		}
	}

	protected void forceLayoutOnChildren(HasWidgets widgets) {
		for (Widget w : widgets) {
			if (w instanceof HasLayout) {
				((HasLayout) w).forceLayout();
			} else if (w instanceof HasWidgets && isWidgetVisible(w)) {
				forceLayoutOnChildren((HasWidgets) w);
			} else if (w instanceof IndexedPanel && isWidgetVisible(w)) {
				forceLayoutOnChildren((IndexedPanel) w);
			}
		}
	}

	protected void forceLayoutOnChildren(IndexedPanel widgets) {
		for (int index = 0, len = widgets.getWidgetCount(); index < len; index++) {
			Widget w = widgets.getWidget(index);
			if (w instanceof HasLayout) {
				((HasLayout) w).forceLayout();
			} else if (w instanceof HasWidgets && isWidgetVisible(w)) {
				forceLayoutOnChildren((HasWidgets) w);
			} else if (w instanceof IndexedPanel && isWidgetVisible(w)) {
				forceLayoutOnChildren((IndexedPanel) w);
			}
		}
	}

	private int fixedWidth() {
		double width = 0;
		for (Double w : fixedColumnWidth.values()) {
			if (w > 1) {
				width = width + w;
			}
		}
		return (int) Math.floor(width);
	}

	private int fixedHeight() {
		double height = 0;
		for (Double h : fixedRowHeight.values()) {
			if (h > 1) {
				height = height + h;
			}
		}
		return (int) Math.floor(height);
	}

	public int getElementRow(Element el) {
        TableElement te = table.getElement().cast();
		NodeList<TableRowElement> rows = te.getRows();
		for (int i = 0; i < rows.getLength(); i++) {
			TableRowElement re = rows.getItem(i);
			if (re == el) {
				return i;
			}
			NodeList<TableCellElement> cells = re.getCells();
			for (int j = 0; j < cells.getLength(); j++) {
				if (cells.getItem(j) == el) {
					return i;
				}
			}
		}
		return -1;
	}

	public int getCellElementColumn(Element el) {
        TableElement te = table.getElement().cast();
		NodeList<TableRowElement> rows = te.getRows();
		for (int i = 0; i < rows.getLength(); i++) {
			TableRowElement re = rows.getItem(i);
			NodeList<TableCellElement> cells = re.getCells();
			for (int j = 0; j < cells.getLength(); j++) {
				if (cells.getItem(j) == el) {
					return j;
				}
			}
		}
		return -1;
	}

	public FlexTable getTable() {
		return table;
	}

	public int getRowCount() {
		return numRows;
	}

	public int getColumnCount() {
		return numColumns;
	}

	public void clearSpans() {
		for (int r = 0; r < numRows; r++) {
			for (int c = 0; c < numColumns; c++) {
				if (getColSpan(r, c) != 1 || getRowSpan(r, c) != 1) {
					setSpan(r, c, 1, 1, false);
				}
			}
		}
		doLayout();
	}
}
