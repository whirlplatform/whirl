package org.whirlplatform.editor.client.view;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.safecss.shared.SafeStyles;
import com.google.gwt.safecss.shared.SafeStylesUtils;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.cell.core.client.form.CheckBoxCell;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.*;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.data.shared.event.StoreDataChangeEvent;
import com.sencha.gxt.data.shared.event.StoreDataChangeEvent.StoreDataChangeHandler;
import com.sencha.gxt.data.shared.event.StoreRecordChangeEvent;
import com.sencha.gxt.data.shared.event.StoreRecordChangeEvent.StoreRecordChangeHandler;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.ColumnWidthChangeEvent;
import com.sencha.gxt.widget.core.client.event.ColumnWidthChangeEvent.ColumnWidthChangeHandler;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.*;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor.IntegerPropertyEditor;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.editing.ClicksToEdit;
import com.sencha.gxt.widget.core.client.grid.editing.GridEditing;
import com.sencha.gxt.widget.core.client.grid.editing.GridInlineEditing;
import org.geomajas.codemirror.client.Config;
import org.geomajas.codemirror.client.widget.CodeMirrorPanel;
import org.whirlplatform.editor.client.component.PropertyValueField;
import org.whirlplatform.editor.client.component.PropertyValueFieldConverter;
import org.whirlplatform.editor.client.presenter.TablePresenter;
import org.whirlplatform.editor.client.presenter.TablePresenter.ITableView;
import org.whirlplatform.editor.client.util.ColumnStore.TableColumnProperties;
import org.whirlplatform.editor.client.util.ElementKeyProvider;
import org.whirlplatform.editor.client.util.ElementLabelProvider;
import org.whirlplatform.editor.client.util.TableComboBox;
import org.whirlplatform.editor.client.util.TableStore;
import org.whirlplatform.editor.shared.i18n.EditorMessage;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.editor.LocaleElement;
import org.whirlplatform.meta.shared.editor.PropertyValue;
import org.whirlplatform.meta.shared.editor.db.AbstractTableElement;
import org.whirlplatform.meta.shared.editor.db.TableColumnElement;
import org.whirlplatform.meta.shared.editor.db.TableColumnElement.Order;
import org.whirlplatform.meta.shared.editor.db.TableColumnElement.ViewFormat;

import java.util.*;

public class TableView extends ContentPanel implements ITableView {

	private TablePresenter presenter;

	private VerticalLayoutContainer container;

	private FieldLabel labelTitle;
	private PropertyValueField fieldTitle;

	private FieldLabel labelTableName;
	private TextField fieldTableName;

	private FieldLabel labelCode;
	private TextField fieldCode;

	private FieldLabel labelEmptyRow;
	private CheckBox fieldEmptyRow;

	private FieldLabel labelIdColumn;
	private ComboBox<TableColumnElement> fieldIdColumn;
	private TextButton idColumnClearButton;

	private FieldLabel labelDeleteColumn;
	private ComboBox<TableColumnElement> fieldDeleteColumn;
	private TextButton deleteColumnClearButton;

	private FieldLabel labelViewName;
	private TextField fieldViewName;
	private CodeMirrorPanel fieldViewSource;

	private FieldLabel labelListName;
	private TextField fieldListName;
	private CodeMirrorPanel fieldListSource;

	private FieldLabel labelSimple;
	private CheckBox fieldSimple;

	private ListStore<TableColumnElement> storeColumns;
	private Grid<TableColumnElement> gridColumns;
	private GridEditing<TableColumnElement> editingColumns;

	private TextButton synchronizeButton;
	private TextButton rightsButton;
	private TextButton viewEditButton;
	private TextButton listEditButton;
	private TextButton addButton;
	private TextButton deleteButton;

	private PropertyValueField columnTitleField;
	private PropertyValueField columnRegexMessageField;

	private TextButton windowButtonPressed;

	// Если true - кнопка "Сгенерировать" на окне редактирования view будет
	// недоступна
	private boolean changed;

	private boolean clone;

	private Set<ColumnConfig<?, ?>> checkBoxCols = new HashSet<>();

	public TableView() {
		super();
	}

	@Override
	public void setHeaderText(String text) {
		setHeading(text);
	}

	private void initUI() {
		setHeaderVisible(true);
		container = new VerticalLayoutContainer();
		container.setAdjustForScroll(true);
		container.setScrollMode(ScrollMode.AUTO);
		setWidget(container);
		initStore();
		initFields();
	}

	public void clearUI() {
		changed = false;
		container.clear();
		initFields();
	}

	private void initStore() {
		storeColumns = new ListStore<TableColumnElement>(new ElementKeyProvider<TableColumnElement>());
		storeColumns.addStoreDataChangeHandler(new StoreDataChangeHandler<TableColumnElement>() {

			@Override
			public void onDataChange(StoreDataChangeEvent<TableColumnElement> event) {
				changed = true;
			}
		});
		storeColumns.addStoreRecordChangeHandler(new StoreRecordChangeHandler<TableColumnElement>() {

			@Override
			public void onRecordChange(StoreRecordChangeEvent<TableColumnElement> event) {
				changed = true;
			}
		});

		// Сортировка по индексу по умолчанию
		ValueProvider<TableColumnElement, Integer> provider = new ValueProvider<TableColumnElement, Integer>() {
			@Override
			public Integer getValue(TableColumnElement object) {
				return object.getIndex();
			}

			@Override
			public void setValue(TableColumnElement object, Integer value) {
			}

			@Override
			public String getPath() {
				return null;
			}
		};
		storeColumns.addSortInfo(new StoreSortInfo<TableColumnElement>(provider, SortDir.ASC));
	}

	private void initFields() {
		initStore();

		fieldTitle = new PropertyValueField();

		labelTitle = new FieldLabel(fieldTitle, EditorMessage.Util.MESSAGE.title());
		container.add(labelTitle, new VerticalLayoutData(1, -1, new Margins(10, 10, 0, 10)));

		fieldTableName = new TextField();
		labelTableName = new FieldLabel(fieldTableName, EditorMessage.Util.MESSAGE.table_name());
		container.add(labelTableName, new VerticalLayoutData(1, -1, new Margins(10, 10, 0, 10)));

		fieldCode = new TextField();
		labelCode = new FieldLabel(fieldCode, EditorMessage.Util.MESSAGE.table_code());
		container.add(labelCode, new VerticalLayoutData(1, -1, new Margins(10, 10, 0, 10)));

		fieldEmptyRow = new CheckBox();
		fieldEmptyRow.setBoxLabel("");
		labelEmptyRow = new FieldLabel(fieldEmptyRow, EditorMessage.Util.MESSAGE.table_empty_row());
		container.add(labelEmptyRow, new VerticalLayoutData(-1, -1, new Margins(10, 10, 0, 10)));

		fieldSimple = new CheckBox();
		fieldSimple.setBoxLabel("");
		labelSimple = new FieldLabel(fieldSimple, EditorMessage.Util.MESSAGE.table_no_subrequest());
		container.add(labelSimple, new VerticalLayoutData(-1, -1, new Margins(10, 10, 0, 10)));

		initIdColumnField();

		initDeleteColumnField();

		initViewEditField();

		initListEditField();

		initColumns();
	}

	private void initIdColumnField() {
		fieldIdColumn = new ComboBox<TableColumnElement>(storeColumns, new ElementLabelProvider<TableColumnElement>());
		fieldIdColumn.setTriggerAction(TriggerAction.ALL);
		fieldIdColumn.setEditable(false);

		HorizontalLayoutContainer idContainer = new HorizontalLayoutContainer();
		idColumnClearButton = new TextButton(EditorMessage.Util.MESSAGE.table_clear());
		idColumnClearButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				fieldIdColumn.clear();
			}
		});
		labelIdColumn = new FieldLabel(idContainer, EditorMessage.Util.MESSAGE.table_primary_key());

		idContainer.add(fieldIdColumn, new HorizontalLayoutData(1, -1, new Margins(0, 5, 0, 0)));
		idContainer.add(idColumnClearButton);
		container.add(labelIdColumn, new VerticalLayoutData(1, -1, new Margins(10, 10, 0, 10)));
	}

	private void initDeleteColumnField() {
		fieldDeleteColumn = new ComboBox<TableColumnElement>(storeColumns,
				new ElementLabelProvider<TableColumnElement>());
		fieldDeleteColumn.setTriggerAction(TriggerAction.ALL);
		fieldDeleteColumn.setEditable(false);

		HorizontalLayoutContainer deleteContainer = new HorizontalLayoutContainer();
		deleteColumnClearButton = new TextButton(EditorMessage.Util.MESSAGE.table_clear());
		deleteColumnClearButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				fieldDeleteColumn.clear();
			}
		});
		deleteContainer.add(fieldDeleteColumn, new HorizontalLayoutData(1, -1, new Margins(0, 5, 0, 0)));
		deleteContainer.add(deleteColumnClearButton);

		labelDeleteColumn = new FieldLabel(deleteContainer, EditorMessage.Util.MESSAGE.table_delete_column());
		container.add(labelDeleteColumn, new VerticalLayoutData(1, -1, new Margins(10, 10, 0, 10)));
	}

	private void initViewEditField() {
		fieldViewName = new TextField();
		fieldViewName.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				changed = true;
			}
		});
		labelViewName = new FieldLabel(fieldViewName, EditorMessage.Util.MESSAGE.table_view_name());
		HorizontalLayoutContainer vContainer = new HorizontalLayoutContainer();
		viewEditButton = new TextButton(EditorMessage.Util.MESSAGE.table_edit());
		viewEditButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				showViewEditWindow(fieldViewSource, false);
			}
		});
		vContainer.add(labelViewName, new HorizontalLayoutData(1, -1, new Margins(0, 5, 0, 0)));
		vContainer.add(viewEditButton);
		container.add(vContainer, new VerticalLayoutData(1, 40, new Margins(10, 10, 0, 10)));

		fieldViewSource = new CodeMirrorPanel();
	}

	private void initListEditField() {
		fieldListName = new TextField();
		fieldListName.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				changed = true;
			}
		});
		SimpleContainer s = new SimpleContainer();
		s.add(fieldListName);
		labelListName = new FieldLabel(s, EditorMessage.Util.MESSAGE.table_list_name());

		HorizontalLayoutContainer lContainer = new HorizontalLayoutContainer();
		listEditButton = new TextButton(EditorMessage.Util.MESSAGE.table_edit());
		listEditButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				showViewEditWindow(fieldListSource, true);
			}
		});
		lContainer.add(labelListName, new HorizontalLayoutData(1, -1, new Margins(0, 5, 0, 0)));
		lContainer.add(listEditButton);

		container.add(lContainer, new VerticalLayoutData(1, 40, new Margins(10, 10, 0, 10)));

		fieldListSource = new CodeMirrorPanel();
	}

	private void initColumns() {
		TableColumnProperties properties = GWT.create(TableColumnProperties.class);

		HorizontalLayoutContainer toolbar = new HorizontalLayoutContainer();
		addButton = new TextButton(EditorMessage.Util.MESSAGE.context_menu_add());
		addButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				getPresenter().newColumn();
			}
		});
		deleteButton = new TextButton(EditorMessage.Util.MESSAGE.context_menu_remove());
		deleteButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				gridColumns.getStore().remove(gridColumns.getSelectionModel().getSelectedItem());
			}

		});
		rightsButton = new TextButton(EditorMessage.Util.MESSAGE.context_menu_rights());
		rightsButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				presenter.onEditRightsElement();
			}
		});
		synchronizeButton = new TextButton(EditorMessage.Util.MESSAGE.table_synchronize());
		synchronizeButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				presenter.onSynchronizeCloneFields();
			}
		});
		synchronizeButton.setVisible(false);

		toolbar.add(addButton, new HorizontalLayoutData(100, -1));
		toolbar.add(deleteButton, new HorizontalLayoutData(100, -1, new Margins(0, 0, 0, 5)));
		toolbar.add(rightsButton, new HorizontalLayoutData(100, -1, new Margins(0, 0, 0, 5)));
		toolbar.add(synchronizeButton, new HorizontalLayoutData(100, -1, new Margins(0, 0, 0, 5)));
		container.add(toolbar, new VerticalLayoutData(1, 35, new Margins(15, 5, 0, 5)));

		List<ColumnConfig<TableColumnElement, ?>> list = new ArrayList<ColumnConfig<TableColumnElement, ?>>();
		ColumnConfig<TableColumnElement, Integer> configIndex = new ColumnConfig<TableColumnElement, Integer>(
				properties.index(), 50, EditorMessage.Util.MESSAGE.table_index());
		list.add(configIndex);

		// Если не заменять Cell, вместо строкового значения отображается id
		// объекта
		Cell<PropertyValue> titleCell = new AbstractCell<PropertyValue>(BrowserEvents.CLICK) {
			@Override
			public void render(com.google.gwt.cell.client.Cell.Context context, PropertyValue value,
					SafeHtmlBuilder sb) {
				if (value != null) {
					String strValue = value.getValue(value.getDefaultLocale()).getString();
					if (strValue != null) {
						sb.appendHtmlConstant(strValue);
					}
				} else {
					sb.appendHtmlConstant("");
				}
			}
		};

		ColumnConfig<TableColumnElement, PropertyValue> configTitle = new ColumnConfig<TableColumnElement, PropertyValue>(
				properties.title(), 100, EditorMessage.Util.MESSAGE.title());
		configTitle.setCell(titleCell);
		list.add(configTitle);

		ColumnConfig<TableColumnElement, String> configColumnName = new ColumnConfig<TableColumnElement, String>(
				properties.columnName(), 100, EditorMessage.Util.MESSAGE.table_column_name());
		list.add(configColumnName);

		ColumnConfig<TableColumnElement, DataType> configType = new ColumnConfig<TableColumnElement, DataType>(
				properties.type(), 70, EditorMessage.Util.MESSAGE.table_column_datatype());
		list.add(configType);

		ColumnConfig<TableColumnElement, AbstractTableElement> configListTable = new ColumnConfig<TableColumnElement, AbstractTableElement>(
				properties.listTable(), 100, EditorMessage.Util.MESSAGE.table_column_list_table());
		list.add(configListTable);

		ColumnConfig<TableColumnElement, String> configFunction = new ColumnConfig<TableColumnElement, String>(
				properties.function(), 100, EditorMessage.Util.MESSAGE.table_column_function());
		list.add(configFunction);

		ColumnConfig<TableColumnElement, Integer> configSize = new ColumnConfig<TableColumnElement, Integer>(
				properties.size(), 60, EditorMessage.Util.MESSAGE.table_column_size());
		list.add(configSize);

		ColumnConfig<TableColumnElement, Integer> configScale = new ColumnConfig<TableColumnElement, Integer>(
				properties.scale(), 90, EditorMessage.Util.MESSAGE.table_column_scale());
		list.add(configScale);

		ColumnConfig<TableColumnElement, Integer> configWidth = new ColumnConfig<TableColumnElement, Integer>(
				properties.width(), 60, EditorMessage.Util.MESSAGE.table_column_width());
		list.add(configWidth);

		ColumnConfig<TableColumnElement, Integer> configHeight = new ColumnConfig<TableColumnElement, Integer>(
				properties.height(), 60, EditorMessage.Util.MESSAGE.table_column_height());
		list.add(configHeight);

		ColumnConfig<TableColumnElement, String> configDefaultValue = new ColumnConfig<TableColumnElement, String>(
				properties.defaultValue(), 60, EditorMessage.Util.MESSAGE.table_column_value());
		list.add(configDefaultValue);

		ColumnConfig<TableColumnElement, Boolean> configNotNull = new ColumnConfig<TableColumnElement, Boolean>(
				properties.notNull(), 70, EditorMessage.Util.MESSAGE.table_column_not_null());
		configNotNull.setCell(new CheckBoxCell());
		setCheckBoxColCenterAlign(configNotNull);
		checkBoxCols.add(configNotNull);
		list.add(configNotNull);

		ColumnConfig<TableColumnElement, Boolean> configHidden = new ColumnConfig<TableColumnElement, Boolean>(
				properties.hidden(), 70, EditorMessage.Util.MESSAGE.table_column_hidden());
		configHidden.setCell(new CheckBoxCell());
		setCheckBoxColCenterAlign(configHidden);
		checkBoxCols.add(configHidden);
		list.add(configHidden);

		ColumnConfig<TableColumnElement, Boolean> configFilter = new ColumnConfig<TableColumnElement, Boolean>(
				properties.filter(), 70, EditorMessage.Util.MESSAGE.table_column_filter());
		configFilter.setCell(new CheckBoxCell());
		setCheckBoxColCenterAlign(configFilter);
		checkBoxCols.add(configFilter);
		list.add(configFilter);

		ColumnConfig<TableColumnElement, Boolean> configListTitle = new ColumnConfig<TableColumnElement, Boolean>(
				properties.listTitle(), 70, EditorMessage.Util.MESSAGE.table_column_list_title());
		configListTitle.setCell(new CheckBoxCell());
		setCheckBoxColCenterAlign(configListTitle);
		checkBoxCols.add(configListTitle);
		list.add(configListTitle);

		ColumnConfig<TableColumnElement, String> configDataFormat = new ColumnConfig<TableColumnElement, String>(
				properties.dataFormat(), 70, EditorMessage.Util.MESSAGE.table_column_data_format());
		list.add(configDataFormat);

		ColumnConfig<TableColumnElement, String> configRegex = new ColumnConfig<TableColumnElement, String>(
				properties.regex(), 100, EditorMessage.Util.MESSAGE.table_column_regex());
		list.add(configRegex);

		ColumnConfig<TableColumnElement, PropertyValue> configRegexMessage = new ColumnConfig<TableColumnElement, PropertyValue>(
				properties.regexMessage(), 130, EditorMessage.Util.MESSAGE.table_column_regex_message());
		list.add(configRegexMessage);

		ColumnConfig<TableColumnElement, Boolean> defaultOrder = new ColumnConfig<TableColumnElement, Boolean>(
				properties.defaultOrder(), 130, EditorMessage.Util.MESSAGE.table_column_default_order());
		defaultOrder.setCell(new CheckBoxCell());
		setCheckBoxColCenterAlign(defaultOrder);
		checkBoxCols.add(defaultOrder);
		list.add(defaultOrder);

		ColumnConfig<TableColumnElement, Order> order = new ColumnConfig<TableColumnElement, Order>(properties.order(),
				130, EditorMessage.Util.MESSAGE.table_column_order());
		list.add(order);

		ColumnConfig<TableColumnElement, ViewFormat> configViewFormat = new ColumnConfig<TableColumnElement, ViewFormat>(
				properties.viewFormat(), 130, EditorMessage.Util.MESSAGE.table_column_view_format());
		list.add(configViewFormat);

		ColumnConfig<TableColumnElement, String> configColumn = new ColumnConfig<TableColumnElement, String>(
				properties.configColumn(), 130, EditorMessage.Util.MESSAGE.table_column_config_column());
		list.add(configColumn);

		ColumnConfig<TableColumnElement, String> colorConfig = new ColumnConfig<>(properties.color(), 100,
				EditorMessage.Util.MESSAGE.design_background_color());
		list.add(colorConfig);

		ColumnModel<TableColumnElement> model = new ColumnModel<TableColumnElement>(list);

		gridColumns = new Grid<TableColumnElement>(storeColumns, model);
		gridColumns.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		gridColumns.setBorders(true);
		container.add(gridColumns, new VerticalLayoutData(1, 1, new Margins(5, 5, 0, 5)));

		model.addColumnWidthChangeHandler(new ColumnWidthChangeHandler() {

			@Override
			public void onColumnWidthChange(ColumnWidthChangeEvent event) {
				ColumnConfig<?, ?> conf = event.getColumnConfig();
				if (checkBoxCols.contains(conf)) {
					setCheckBoxColCenterAlign(conf);
					gridColumns.getView().refresh(true);
				}
			}
		});

		// Переопределен для редактирования параметров с возможностью смены
		// локали
		// editingColumns = new GridRowEditing<TableColumnElement>(gridColumns);
		editingColumns = new GridInlineEditing<TableColumnElement>(gridColumns);
		((GridInlineEditing<TableColumnElement>) editingColumns).setClicksToEdit(ClicksToEdit.TWO);

		editingColumns.addEditor(configIndex, new NumberField<Integer>(new IntegerPropertyEditor()));

		columnTitleField = new PropertyValueField();
		editingColumns.addEditor(configTitle, new PropertyValueFieldConverter(columnTitleField), columnTitleField);
		editingColumns.addEditor(configColumnName, new TextField());
		editingColumns.addEditor(configType, initDataTypeField());
		editingColumns.addEditor(configListTable, initListTableField());
		editingColumns.addEditor(configFunction, new TextField());
		editingColumns.addEditor(configSize, new NumberField<Integer>(new IntegerPropertyEditor()));
		editingColumns.addEditor(configScale, new NumberField<Integer>(new IntegerPropertyEditor()));
		editingColumns.addEditor(configWidth, new NumberField<Integer>(new IntegerPropertyEditor()));
		editingColumns.addEditor(configHeight, new NumberField<Integer>(new IntegerPropertyEditor()));
		editingColumns.addEditor(configDefaultValue, new TextField());
		editingColumns.addEditor(configDataFormat, new TextField());
		editingColumns.addEditor(configRegex, new TextField());
		editingColumns.addEditor(order, initOrderField());
		columnRegexMessageField = new PropertyValueField();
		editingColumns.addEditor(configRegexMessage, new PropertyValueFieldConverter(columnRegexMessageField),
				columnRegexMessageField);
		Converter<ViewFormat, ViewFormat> converter = new Converter<ViewFormat, ViewFormat>() {
			@Override
			public ViewFormat convertFieldValue(ViewFormat object) {
				return object == ViewFormat.NONE ? null : object;
			}

			@Override
			public ViewFormat convertModelValue(ViewFormat object) {
				return object;
			}
		};
		editingColumns.addEditor(configViewFormat, converter, initViewFormatField());
		editingColumns.addEditor(configColumn, new TextField());
		editingColumns.addEditor(colorConfig, new TextField());
	}

	private ComboBox<DataType> initDataTypeField() {
		ListStore<DataType> store = new ListStore<DataType>(new ModelKeyProvider<DataType>() {

			@Override
			public String getKey(DataType item) {
				return item.name();
			}
		});
		store.add(DataType.STRING);
		store.add(DataType.NUMBER);
		store.add(DataType.DATE);
		store.add(DataType.BOOLEAN);
		store.add(DataType.LIST);
		store.add(DataType.FILE);
		ComboBox<DataType> result = new ComboBox<DataType>(store, new LabelProvider<DataType>() {

			@Override
			public String getLabel(DataType item) {
				return item.name();
			}
		});

		result.setTriggerAction(TriggerAction.ALL);
		result.setEditable(false);
		result.setValue(DataType.STRING);
		return result;
	}

	public TableComboBox initListTableField() {
		TableComboBox combo = new TableComboBox(new TableStore(getPresenter().getEventBus()));
		return combo;
	}

	private ComboBox<Order> initOrderField() {
		ListStore<Order> store = new ListStore<Order>(new ModelKeyProvider<Order>() {

			@Override
			public String getKey(Order item) {
				return item.name();
			}
		});
		store.add(Order.ASC);
		store.add(Order.DESC);
		ComboBox<Order> result = new ComboBox<Order>(store, new LabelProvider<Order>() {

			@Override
			public String getLabel(Order item) {
				return item.name();
			}
		});

		result.setTriggerAction(TriggerAction.ALL);
		result.setEditable(false);
		result.setValue(Order.ASC);
		return result;
	}

	private ComboBox<ViewFormat> initViewFormatField() {
		ListStore<ViewFormat> store = new ListStore<ViewFormat>(new ModelKeyProvider<ViewFormat>() {

			@Override
			public String getKey(ViewFormat item) {
				return item.name();
			}
		});
		store.add(ViewFormat.NONE);
		store.add(ViewFormat.CSS);
		ComboBox<ViewFormat> result = new ComboBox<ViewFormat>(store, new LabelProvider<ViewFormat>() {

			@Override
			public String getLabel(ViewFormat item) {
				return item.name();
			}
		});

		result.setTriggerAction(TriggerAction.ALL);
		result.setEditable(false);
		return result;
	}

	@Override
	public void setPresenter(TablePresenter presenter) {
		this.presenter = presenter;
		initUI();
	}

	@Override
	public TablePresenter getPresenter() {
		return presenter;
	}

	@Override
	public void setTableTitle(PropertyValue title) {
		fieldTitle.setPropertyValue(title);
	}

	@Override
	public PropertyValue getTableTitle() {
		return fieldTitle.getPropertyValue();
	}

	@Override
	public void setTableName(String tableName) {
		fieldTableName.setValue(tableName);
	}

	@Override
	public String getTableName() {
		return fieldTableName.getValue();
	}

	@Override
	public void setIdColumn(TableColumnElement idColumn) {
		fieldIdColumn.setValue(idColumn);
	}

	@Override
	public TableColumnElement getIdColumn() {
		return fieldIdColumn.getValue();
	}

	@Override
	public void setDeleteColumn(TableColumnElement deleteColumn) {
		fieldDeleteColumn.setValue(deleteColumn);
	}

	@Override
	public TableColumnElement getDeleteColumn() {
		return fieldDeleteColumn.getValue();
	}

	@Override
	public void addColumn(TableColumnElement column) {
		storeColumns.add(storeColumns.size(), column);
	}

	@Override
	public Collection<TableColumnElement> getColumns() {
		gridColumns.getStore().commitChanges();
		return storeColumns.getAll();
	}

	@Override
	public void setViewName(String viewName) {
		fieldViewName.setValue(viewName);
	}

	@Override
	public String getViewName() {
		return fieldViewName.getValue();
	}

	@Override
	public void setViewSource(String viewSource) {
		fieldViewSource.setValue(viewSource);
	}

	@Override
	public String getViewSource() {
		return fieldViewSource.getValue();
	}

	@Override
	public void setListName(String listName) {
		fieldListName.setValue(listName);
	}

	@Override
	public String getListName() {
		return fieldListName.getValue();
	}

	@Override
	public void setListSource(String listSource) {
		fieldListSource.setValue(listSource);
	}

	@Override
	public String getListSource() {
		return fieldListSource.getValue();
	}

	@Override
	public void setLocales(Collection<LocaleElement> locales, LocaleElement defaultLocale) {
		fieldTitle.setLocales(defaultLocale, locales);
		columnTitleField.setLocales(defaultLocale, locales);
		columnRegexMessageField.setLocales(defaultLocale, locales);
	}

	public void setClone(boolean clone) {
		this.clone = clone;
		if (clone) {
			labelTableName.setEnabled(false);
			synchronizeButton.setVisible(true);
		} else {
			labelTableName.setEnabled(true);
			synchronizeButton.setVisible(false);
		}
	}

	@Override
	public void setCode(String code) {
		fieldCode.setValue(code);
	}

	@Override
	public String getCode() {
		return fieldCode.getValue();
	}

	@Override
	public void setEmptyRow(boolean emptyRow) {
		fieldEmptyRow.setValue(emptyRow);
	}

	@Override
	public boolean isEmptyRow() {
		return fieldEmptyRow.getValue();
	}

	public void showViewEditWindow(final CodeMirrorPanel area, final boolean isLView) {
		final String oldValue = area.getValue();

		final Window w = new Window();
		w.getHeader().setText(EditorMessage.Util.MESSAGE.table_view_edit());
		w.setModal(true);
		w.setWidget(area);
		w.setPixelSize(600, 400);

		final TextButton save = new TextButton(EditorMessage.Util.MESSAGE.apply());
		save.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				windowButtonPressed = save;
				w.hide();
			}
		});

		TextButton close = new TextButton(EditorMessage.Util.MESSAGE.close());
		close.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				w.hide();
			}
		});
		w.addButton(save);
		w.addButton(close);

		w.addHideHandler(new HideHandler() {

			@Override
			public void onHide(HideEvent event) {
				if (windowButtonPressed != save) {
					area.setValue(oldValue);
				}
				windowButtonPressed = null;
			}
		});
		w.show();
		area.showEditor(Config.forSql());
	}

	@Override
	public void setEnableAll(boolean enable) {
		labelTitle.setEnabled(enable);
		if (!clone) {
			labelTableName.setEnabled(enable);
		}
		labelCode.setEnabled(enable);
		labelEmptyRow.setEnabled(enable);
		labelIdColumn.setEnabled(enable);
		labelDeleteColumn.setEnabled(enable);
		labelViewName.setEnabled(enable);
		labelListName.setEnabled(enable);
		labelSimple.setEnabled(enable);
		if (enable) {
			gridColumns.getView().getBody().unmask();
		} else {
			gridColumns.getView().getBody().mask(null);
		}
		viewEditButton.setEnabled(enable);
		listEditButton.setEnabled(enable);
		addButton.setEnabled(enable);
		deleteButton.setEnabled(enable);
		synchronizeButton.setEnabled(enable);

		// rightsButton.setEnabled(false); // Редактирование прав отключать не
		// нужно
	}

	@Override
	public void setSimple(boolean simple) {
		fieldSimple.setValue(simple);
	}

	@Override
	public boolean isSimple() {
		return fieldSimple.getValue();
	}

	private void setCheckBoxColCenterAlign(ColumnConfig<?, ?> config) {
		int padding = (config.getWidth() - 13) / 2;
		String style = "padding: 3px 0px 0px " + padding + "px;";
		SafeStyles safeStyle = SafeStylesUtils.fromTrustedString(style);
		config.setColumnTextStyle(safeStyle);
	}
}
