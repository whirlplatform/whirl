package org.whirlplatform.component.client.grid;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.safecss.shared.SafeStylesBuilder;
import com.google.gwt.safecss.shared.SafeStylesUtils;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.text.shared.SimpleSafeHtmlRenderer;
import com.sencha.gxt.cell.core.client.NumberCell;
import com.sencha.gxt.cell.core.client.form.CheckBoxCell;
import com.sencha.gxt.core.client.util.Util;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.editing.GridEditing;
import org.whirlplatform.component.client.AbstractFieldBuilder;
import org.whirlplatform.component.client.data.RowModelDataValueProvider;
import org.whirlplatform.component.client.selenium.Locator;
import org.whirlplatform.component.client.selenium.LocatorAware;
import org.whirlplatform.component.client.state.ColumnConfigStore;
import org.whirlplatform.component.client.utils.FileLinkHelper;
import org.whirlplatform.component.client.utils.StringMetrics;
import org.whirlplatform.meta.shared.AppConstant;
import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.FieldMetadata;
import org.whirlplatform.meta.shared.FileValue;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.data.ListModelData;
import org.whirlplatform.meta.shared.data.RowModelData;

import java.util.*;

public class ColumnModelHelper implements LocatorAware {

	private ClassMetadata metadata;

	private ColumnModel<RowModelData> columnModel;

	private GridEditing<RowModelData> editing;

	private ColumnConfig<RowModelData, ?> firstColumn;

	private Store<RowModelData> store;

	private boolean showCell;

	private ColumnConfigStore columnConfigStore;

	private Map<ColumnConfig<RowModelData, ?>, AbstractFieldBuilder> builders = new HashMap<>();

	public ColumnModelHelper(ClassMetadata metadata, Store<RowModelData> store, boolean showCell,
			ColumnConfigStore columnConfigStore) {
		this.metadata = metadata;
		this.store = store;
		this.showCell = showCell;
		this.columnConfigStore = columnConfigStore;
	}

	// Вынести в отдельный метод установку стиля
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ColumnModel<RowModelData> build() {
		List<ColumnConfig<RowModelData, ?>> configs = new ArrayList<ColumnConfig<RowModelData, ?>>();

		if (firstColumn != null) {
			configs.add(firstColumn);
		}

		for (final FieldMetadata field : metadata.getFields()) {
			if (!field.isView()) {
				continue;
			}

			int width = field.getWidth();
			if (width <= 0) {
				if (field.getLength() > 0) {
					width = StringMetrics.getWidth(field.getLength(), "13px");
				} else {
					width = StringMetrics.getWidth(field.getLabel(), "13px");
				}
			}

			final ColumnConfig<RowModelData, Object> columnConfig = new ColumnConfig<RowModelData, Object>(
					new RowModelDataValueProvider<Object>(field.getName()), width,
					SafeHtmlUtils.fromTrustedString(field.getLabel()));

			columnConfig.setHidden(field.isHidden());

			if (!field.isEdit() && field.getViewFormat() == null) {
				SafeStylesBuilder style = new SafeStylesBuilder();
				style.trustedBackgroundColor("#F0F0F0");
				columnConfig.setColumnStyle(style.toSafeStyles());
			}

			// TODO вообще нужна своя реализация Cell
			if (DataType.BOOLEAN == field.getType()) {
				CheckBoxCell cell;
				if (field.getViewFormat() != null) {
					cell = new CheckBoxCell() {

						@Override
						public void render(com.google.gwt.cell.client.Cell.Context context, Boolean value,
								SafeHtmlBuilder sb) {
							SafeStylesBuilder style = new SafeStylesBuilder();
							style.appendTrustedString(getStyle((String) context.getKey(), field.getName()));
							columnConfig.setColumnTextStyle(style.toSafeStyles());
							super.render(context, value, sb);
						}
					};
				} else {
					cell = new CheckBoxCell();
				}
				if (!field.isEdit()) {
					cell.disable(null);
				}
				columnConfig.setCell((Cell) cell);
			} else if (DataType.FILE == field.getType()) {
				AbstractCell<FileValue> cell = new AbstractCell<FileValue>(BrowserEvents.CLICK) {

					@Override
					public void render(com.google.gwt.cell.client.Cell.Context context, FileValue value,
							SafeHtmlBuilder sb) {
						if (field.getViewFormat() != null) {
							columnConfig.setColumnTextStyle(SafeStylesUtils
									.fromTrustedString(getStyle((String) context.getKey(), field.getName())));

						}
						if (value == null || Util.isEmptyString(value.getName())) {
							return;
						}
						String link = new String("<span title=\"" + value.getName()
								+ "\" style=\"cursor: pointer; text-decoration: underline; color: #1936A1;\">"
								+ value.getName() + "</span>");
						sb.appendHtmlConstant(link);
					}

					@Override
					public void onBrowserEvent(com.google.gwt.cell.client.Cell.Context context, Element parent,
							FileValue value, NativeEvent event, ValueUpdater<FileValue> valueUpdater) {
						if (Util.isEmptyString(value.getName())) {
							return;
						}
						if (NativeEvent.BUTTON_LEFT == event.getButton()) {
							RowModelData model = store.findModelWithKey((String) context.getKey());
							if (model != null) {
								String url = FileLinkHelper.getTableFileLinkById(metadata.getClassId(), field.getName(),
										model.getId());

								com.google.gwt.user.client.Window.open(url, "_blank", null);
							}
						}
						super.onBrowserEvent(context, parent, value, event, valueUpdater);
					}
				};
				columnConfig.setCell((Cell) cell);
			} else if (DataType.DATE == field.getType()) {
				DateCell cell;
				final DateTimeFormat format;
				if (field.getDataFormat() == null || field.getDataFormat().isEmpty()) {
					format = AppConstant.getDateFormatLong();
				} else {
					format = DateTimeFormat.getFormat(field.getDataFormat());
				}
				if (!Util.isEmptyString(field.getViewFormat())) {
					cell = new DateCell(format) {
						@Override
						public void render(com.google.gwt.cell.client.Cell.Context context, Date value,
								SafeHtmlBuilder sb) {
							columnConfig.setColumnTextStyle(SafeStylesUtils
									.fromTrustedString(getStyle((String) context.getKey(), field.getName())));

							if (value == null) {
								sb.appendHtmlConstant("&nbsp");
							} else {
								sb.appendHtmlConstant(addQTip(SimpleSafeHtmlRenderer.getInstance()
										.render(format.format(value, null)).asString()));
							}
						}
					};
				} else {
					cell = new DateCell(format) {
						@Override
						public void render(com.google.gwt.cell.client.Cell.Context context, Date value,
								SafeHtmlBuilder sb) {
							if (value != null) {
								sb.appendHtmlConstant(addQTip(SimpleSafeHtmlRenderer.getInstance()
										.render(format.format(value, null)).asString()));
							}
						}
					};
				}
				columnConfig.setCell((Cell) cell);
			} else {
				// Можно просто ListModelData наследовать от Comparable
				if (DataType.LIST == field.getType()) {
					columnConfig.setComparator(new Comparator<Object>() {
						@Override
						public int compare(Object o1, Object o2) {
							if (o1 instanceof ListModelData && o2 instanceof ListModelData) {
								String s1 = ((ListModelData) o1).getLabel();
								String s2 = ((ListModelData) o2).getLabel();
								if (s1 == null) {
									return -1;
								} else if (s2 == null) {
									return 1;
								}
								return s1.compareToIgnoreCase(s2);
							} else {
								// По идее такого не может быть. Может
								// выкидывать ошибку?
								return 0;
							}
						}
					});
					AbstractCell<Object> cell = new AbstractCell<Object>() {

						@Override
						public void render(com.google.gwt.cell.client.Cell.Context context, Object value,
								SafeHtmlBuilder sb) {
							if (value != null && value.toString() != null) {
								sb.appendHtmlConstant(addQTip(SafeHtmlUtils.htmlEscape(value.toString())));
							}
						}
					};
					columnConfig.setCell(cell);
				} else if (DataType.NUMBER == field.getType()) {
					NumberCell cell;
					final NumberFormat format;
					if (field.getDataFormat() != null && !field.getDataFormat().isEmpty()) {
						format = NumberFormat.getFormat(field.getDataFormat());
					} else {
						format = NumberFormat.getFormat("#0.#");
					}
					if (!Util.isEmptyString(field.getViewFormat())) {
						cell = new NumberCell<Number>(format) {
							@Override
							public void render(Cell.Context context, Number value, SafeHtmlBuilder sb) {
								columnConfig.setColumnTextStyle(SafeStylesUtils
										.fromTrustedString(getStyle((String) context.getKey(), field.getName())));

								if (value != null) {
									sb.appendHtmlConstant(addQTip(SimpleSafeHtmlRenderer.getInstance()
											.render(format.format(value)).asString()));
								}
							}
						};
					} else {
						cell = new NumberCell<Number>(format) {
							@Override
							public void render(Cell.Context context, Number value, SafeHtmlBuilder sb) {
								if (value != null) {
									sb.appendHtmlConstant(addQTip(SimpleSafeHtmlRenderer.getInstance()
											.render(format.format(value)).asString()));
								}
                            }
                        };
					}
					columnConfig.setCell(cell);
				}

				if (!Util.isEmptyString(field.getViewFormat())) {
					AbstractCell<Object> cell = new AbstractCell<Object>() {
						@Override
						public void render(com.google.gwt.cell.client.Cell.Context context, Object value,
								SafeHtmlBuilder sb) {

							columnConfig.setColumnTextStyle(SafeStylesUtils
									.fromTrustedString(getStyle((String) context.getKey(), field.getName())));

							String displayValue;
							if (value == null || Util.isEmptyString((displayValue = value.toString()))) {
								sb.appendHtmlConstant("&nbsp");
							} else {
								sb.appendHtmlConstant(addQTip(SafeHtmlUtils.htmlEscape(displayValue)));
							}
						}
					};
                    columnConfig.setCell(cell);
				} else if (DataType.STRING == field.getType()) {

					AbstractCell<Object> cell = new AbstractCell<Object>() {

						@Override
						public void render(com.google.gwt.cell.client.Cell.Context context, Object value,
								SafeHtmlBuilder sb) {
							if (value != null && value instanceof String) {
								sb.appendHtmlConstant(addQTip(SafeHtmlUtils.htmlEscape((String) value)));
							}
						}
					};
					columnConfig.setCell(cell);
				}
			}
			configs.add(columnConfig);
		}

		// Восстановление пользовательских настроек
		if (columnConfigStore != null) {
			for (ColumnConfig<RowModelData, ?> c : configs) {
				Integer w = columnConfigStore.getWidth(c.getPath());
				if (w != null) {
					c.setWidth(w);
				}
				Boolean hid = columnConfigStore.isHidden(c.getPath());
				if (hid != null) {
					c.setHidden(hid);
				}
			}
			final List<String> cols = columnConfigStore.getPositions();
			if (cols != null) {
				Collections.sort(configs, new Comparator<ColumnConfig>() {
					@Override
					public int compare(ColumnConfig o1, ColumnConfig o2) {
						int ind1 = cols.indexOf(o1.getPath());
						int ind2 = cols.indexOf(o2.getPath());
						if (ind1 == -1 || ind2 == -1) {
							return 0;
						} else {
							return ind1 - ind2;
						}
					}
				});
			}
		}
		columnModel = new ColumnModel<RowModelData>(configs);
		return columnModel;
	}

	public void setFirstColumn(ColumnConfig<RowModelData, ?> column) {
		this.firstColumn = column;
	}

	public void setClassMetadata(ClassMetadata metadata) {
		this.metadata = metadata;
	}

	public void setEditing(GridEditing<RowModelData> editing) {
		this.editing = editing;
		rebuildEditing();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void rebuildEditing() {
		if (!metadata.isUpdatable()) {
			return;
		}
		for (ColumnConfig<RowModelData, ?> config : columnModel.getColumns()) {
			if (config == firstColumn) {
				continue;
			}
			FieldMetadata field = metadata.getField(config.getPath());
			if (!field.isEdit()) {
				continue;
			}

			AbstractFieldBuilder builder = FieldMetadataHelper.build(field);
			if (builder == null) {
				continue;
			}
			Component component = builder.create();
			builders.put(config, builder);
			if (component instanceof IsField<?>) {
				editing.addEditor((ColumnConfig) config, (IsField<?>) component);
			}
		}
	}

	private String getStyle(String key, String fieldName) {
		RowModelData model = store.findModelWithKey(key);
		String style = model.getStyle(fieldName);
		if (style == null) {
			style = "";
		} else if (!Util.isEmptyString(style) && !style.endsWith(";")) {
			style = style + ";";
		}
		return style;
	}

	private String addQTip(String value) {
		if (showCell) {
			return "<span qtip=\"" + value + "\">" + value + "</span>";
		} else {
			return value;
		}
	}

	/*
	 * Selenium
	 */

	public static class LocatorParams {

		public static String TYPE_CELL_EDITING = "CellEditing";
	}

	@Override
	public Locator getLocatorByElement(Element element) {
		Locator result = null;
		for (AbstractFieldBuilder builder : builders.values()) {
			Locator part = builder.getLocatorByElement(element);
			if (part != null) {
				result = new Locator(LocatorParams.TYPE_CELL_EDITING);
				result.setPart(part);
				break;
			}
		}
		return result;
	}

	@Override
	public void fillLocatorDefaults(Locator locator, Element element) {
	}

	@Override
	public Element getElementByLocator(Locator locator) {
		Element result = null;
		if (isLocatorAcceptable(locator)) {
			Locator builderLocator = locator.getPart();
			for (AbstractFieldBuilder builder : builders.values()) {
				result = builder.getElementByLocator(builderLocator);
				if (result != null) {
					break;
				}
			}
		}
		return result;
	}

	private boolean isLocatorAcceptable(Locator locator) {
		return (locator != null && locator.typeEquals(LocatorParams.TYPE_CELL_EDITING) && locator.getPart() != null);
	}
}
