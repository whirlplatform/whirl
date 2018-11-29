package org.whirlplatform.editor.client.util;

import com.google.gwt.core.client.Callback;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.loader.DataProxy;
import com.sencha.gxt.data.shared.loader.LoadEvent;
import com.sencha.gxt.data.shared.loader.LoadHandler;
import com.sencha.gxt.data.shared.loader.Loader;
import org.whirlplatform.editor.client.ApplicationDataProvider;
import org.whirlplatform.editor.shared.i18n.EditorMessage;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.editor.PropertyValue;
import org.whirlplatform.meta.shared.editor.db.AbstractTableElement;
import org.whirlplatform.meta.shared.editor.db.PlainTableElement;
import org.whirlplatform.meta.shared.editor.db.TableColumnElement;
import org.whirlplatform.meta.shared.editor.db.TableColumnElement.Order;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class ColumnStore extends ListStore<TableColumnElement> {

	private static final Comparator<TableColumnElement> comparator = new Comparator<TableColumnElement>() {

		@Override
		public int compare(TableColumnElement o1, TableColumnElement o2) {
			if (o1.getId().isEmpty()) {
				return -1;
			} else if (o2.getId().isEmpty()) {
				return 1;
			}
			return o1.getName().compareTo(o2.getName());
		}

	};

    public interface TableColumnProperties extends
			PropertyAccess<TableColumnElement> {

		ModelKeyProvider<TableColumnElement> id();

		ValueProvider<TableColumnElement, Integer> index();

		ValueProvider<TableColumnElement, DataType> type();

		ValueProvider<TableColumnElement, PropertyValue> title();

		ValueProvider<TableColumnElement, String> columnName();

		ValueProvider<TableColumnElement, Integer> width();

		ValueProvider<TableColumnElement, Integer> height();

		ValueProvider<TableColumnElement, Integer> size();
		
		ValueProvider<TableColumnElement, Integer> scale();

		ValueProvider<TableColumnElement, String> defaultValue();

		ValueProvider<TableColumnElement, Boolean> notNull();
	
		ValueProvider<TableColumnElement, Boolean> hidden();
		
		ValueProvider<TableColumnElement, Boolean> filter();
		
		ValueProvider<TableColumnElement, AbstractTableElement> listTable();
	
		ValueProvider<TableColumnElement, String> labelColumn();
		
		ValueProvider<TableColumnElement, String> function();
		
		ValueProvider<TableColumnElement, String> regex();
		
		ValueProvider<TableColumnElement, PropertyValue> regexMessage();
		
		ValueProvider<TableColumnElement, Boolean> defaultOrder();
		
		ValueProvider<TableColumnElement, Order> order();
		
		ValueProvider<TableColumnElement, String> dataFormat();
		
		ValueProvider<TableColumnElement, String> configColumn();
		
		ValueProvider<TableColumnElement, String> color();
	}

	private class ColumnProxy implements
			DataProxy<String, List<TableColumnElement>> {
		private ApplicationDataProvider provider;
		private PlainTableElement table;

		private ColumnProxy(ApplicationDataProvider provider, PlainTableElement table) {
			this.provider = provider;
			this.table = table;
			addSortInfo(new StoreSortInfo<TableColumnElement>(comparator,
					SortDir.ASC));
		}

		@Override
		public void load(final String loadConfig,
				final Callback<List<TableColumnElement>, Throwable> callback) {
			provider.getAvailableColumns(table,
					new Callback<Collection<TableColumnElement>, Throwable>() {
						@Override
						public void onFailure(Throwable reason) {
							callback.onFailure(reason);
						}

						@Override
						public void onSuccess(
								Collection<TableColumnElement> result) {
							List<TableColumnElement> list = new ArrayList<TableColumnElement>();
							if (loadConfig == null || loadConfig.isEmpty()) {
								list.addAll(result);
							} else {
								for (TableColumnElement element : result) {
									String query = loadConfig.toLowerCase();
									boolean isName = element.getName() != null
											&& element.getName().toLowerCase()
													.contains(query);
									boolean isColumn = (element.getColumnName() != null && element
											.getColumnName().toLowerCase()
											.contains(query));
									if (isName || isColumn) {
										list.add(element);
									}
								}
							}
							callback.onSuccess(list);
						}
					});
		}
	}

	private DataProxy<String, List<TableColumnElement>> proxy;
	private Loader<String, List<TableColumnElement>> loader;

	public ColumnStore(ApplicationDataProvider provider, PlainTableElement table) {
		super(new ElementKeyProvider<TableColumnElement>());

		proxy = new ColumnProxy(provider, table);
		loader = new Loader<String, List<TableColumnElement>>(proxy);

		loader.addLoadHandler(new LoadHandler<String, List<TableColumnElement>>() {
			@Override
			public void onLoad(LoadEvent<String, List<TableColumnElement>> event) {
				ColumnStore.this.replaceAll(event.getLoadResult());
				TableColumnElement empty = new TableColumnElement();
				empty.setId("");
				empty.setName(EditorMessage.Util.MESSAGE.property_no_data());
				ColumnStore.this.add(empty);
			}
		});
	}

	public Loader<String, List<TableColumnElement>> getLoader() {
		return loader;
	}

}
