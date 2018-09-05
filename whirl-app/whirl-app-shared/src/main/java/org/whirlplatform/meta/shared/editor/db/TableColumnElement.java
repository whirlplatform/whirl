package org.whirlplatform.meta.shared.editor.db;

import com.google.gwt.user.client.rpc.IsSerializable;
import org.whirlplatform.meta.shared.EventMetadata;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.editor.AbstractElement;
import org.whirlplatform.meta.shared.editor.ElementVisitor;
import org.whirlplatform.meta.shared.editor.PropertyValue;

import java.io.Serializable;

@SuppressWarnings("serial")
public class TableColumnElement extends AbstractElement implements Cloneable {

    public enum Order implements Serializable, IsSerializable {

		ASC, DESC

	}

    public enum ViewFormat implements Serializable, IsSerializable {

		NONE, CSS

	}

	private int index;
	private DataType type;
	private PropertyValue title = new PropertyValue(DataType.STRING);
	private String columnName;
	private PlainTableElement table;
	private Integer width;
	private Integer height;
	private Integer size;
	private Integer scale;
	private String defaultValue;
	private boolean notNull = false;
	private boolean listTitle = false;
	private boolean hidden = false;
	private boolean filter = true;
	private boolean defaultOrder;
	private Order order;
	private ViewFormat viewFormat;
	private EventMetadata event;
	private String dataFormat;
	private AbstractTableElement listTable;
	private String function;
	private String regex;
	private PropertyValue regexMessage = new PropertyValue(DataType.STRING);
	private String configColumn;
	private String color;

	public TableColumnElement() {
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

	public void setType(DataType type) {
		this.type = type;
	}

	public DataType getType() {
		return type;
	}

	public void setTitle(PropertyValue title) {
		this.title = title;
	}

	public PropertyValue getTitle() {
		return title;
	}

	public void setTable(PlainTableElement table) {
		this.table = table;
	}

	public PlainTableElement getTable() {
		return table;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getWidth() {
		return width;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Integer getHeight() {
		return height;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Integer getSize() {
		return size;
	}

	public void setScale(Integer scale) {
		this.scale = scale;
	}

	public Integer getScale() {
		return scale;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setNotNull(boolean notNull) {
		this.notNull = notNull;
	}

	public boolean isNotNull() {
		return notNull;
	}

	public void setListTitle(boolean listTitle) {
		this.listTitle = listTitle;
	}

	public boolean isListTitle() {
		return listTitle;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setFilter(boolean filter) {
		this.filter = filter;
	}

	public boolean isFilter() {
		return filter;
	}

	public void setDefaultOrder(boolean defaultOrder) {
		this.defaultOrder = defaultOrder;
	}

	public boolean isDefaultOrder() {
		return defaultOrder;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Order getOrder() {
		return order;
	}

	public void setViewFormat(ViewFormat viewFormat) {
		this.viewFormat = viewFormat;
	}

	public ViewFormat getViewFormat() {
		return viewFormat;
	}

	public void setEvent(EventMetadata event) {
		this.event = event;
	}

	public EventMetadata getEvent() {
		return event;
	}

	public void setDataFormat(String dataFormat) {
		this.dataFormat = dataFormat;
	}

	public String getDataFormat() {
		return dataFormat;
	}

	public void setListTable(AbstractTableElement listTable) {
		this.listTable = listTable;
	}

	public AbstractTableElement getListTable() {
		return listTable;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	public String getFunction() {
		return function;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	public String getRegex() {
		return regex;
	}

	public void setRegexMessage(PropertyValue regexMessage) {
		this.regexMessage = regexMessage;
	}

	public PropertyValue getRegexMessage() {
		return regexMessage;
	}
	
	public void setConfigColumn(String configColumn) {
		this.configColumn = configColumn;
	}
	
	public String getConfigColumn() {
		return configColumn;
	}

	@Deprecated
	public TableColumnElement clone() {
		TableColumnElement clone = new TableColumnElement();
		clone.setId(this.getId());
		clone.setName(this.getName());
		clone.index = this.index;
		clone.type = this.type;
		clone.title = this.title;
		clone.columnName = this.columnName;
		clone.width = this.width;
		clone.height = this.height;
		clone.size = this.size;
		clone.defaultValue = this.defaultValue;
		clone.notNull = this.notNull;
		clone.listTitle = this.listTitle;
		clone.hidden = this.hidden;
		clone.filter = this.filter;
		clone.order = this.order;
		clone.viewFormat = this.viewFormat;
		clone.dataFormat = this.dataFormat;
		clone.function = this.function;
		clone.regex = this.regex;
		clone.regexMessage = this.regexMessage;
		return clone;
	}

	@Override
	public String getName() {
		return title + " - " + columnName;
	}

	@Override
	public void setName(String name) {
		// name не используется
	}
	
	public String getColor() {
		return color;
	}
	
	public void setColor(String color) {
		this.color = color;
	}

	@Override
    public <T extends ElementVisitor.VisitContext> void accept(T ctx, ElementVisitor<T> visitor) {
		visitor.visit(ctx, this);
	}

}
