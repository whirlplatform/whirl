package org.whirlplatform.editor.client.util;

import com.google.gwt.core.client.Callback;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.loader.DataProxy;
import com.sencha.gxt.data.shared.loader.LoadEvent;
import com.sencha.gxt.data.shared.loader.LoadHandler;
import com.sencha.gxt.data.shared.loader.Loader;
import org.whirlplatform.editor.client.ApplicationDataProvider;
import org.whirlplatform.editor.shared.i18n.EditorMessage;
import org.whirlplatform.meta.shared.editor.db.AbstractTableElement;
import org.whirlplatform.meta.shared.editor.db.PlainTableElement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class TableStore extends ListStore<AbstractTableElement> {

	private static final Comparator<AbstractTableElement> comparator = new Comparator<AbstractTableElement>() {

		@Override
		public int compare(AbstractTableElement o1, AbstractTableElement o2) {
			if (o1.getId().isEmpty()) {
				return -1;
			} else if (o2.getId().isEmpty()) {
				return 1;
			}
			return o1.getName().compareTo(o2.getName());
		}

	};

	private class TableProxy implements DataProxy<String, List<AbstractTableElement>> {
		private ApplicationDataProvider provider;

		private TableProxy(ApplicationDataProvider provider) {
			this.provider = provider;
			addSortInfo(new StoreSortInfo<AbstractTableElement>(comparator, SortDir.ASC));
		}

		@Override
		public void load(final String loadConfig,
				final Callback<List<AbstractTableElement>, Throwable> callback) {
			provider.getAvailableTables(new Callback<Collection<AbstractTableElement>, Throwable>() {
				@Override
				public void onFailure(Throwable reason) {
					callback.onFailure(reason);
				}

				@Override
				public void onSuccess(Collection<AbstractTableElement> result) {
					List<AbstractTableElement> list = new ArrayList<AbstractTableElement>();
					if (loadConfig == null || loadConfig.isEmpty()) {
						list.addAll(result);
					} else {
						for (AbstractTableElement element : result) {
							String query = loadConfig.toLowerCase();
							boolean isName = element.getName() != null
									&& element.getName().toLowerCase()
											.contains(query);
							boolean isTable = false;
							if (element instanceof PlainTableElement) {
								isTable = (((PlainTableElement) element)
										.getTableName() != null && ((PlainTableElement) element)
										.getTableName().toLowerCase()
										.contains(query));
							}
							if (isName || isTable) {
								list.add(element);
							}
						}
					}
					callback.onSuccess(list);
				}
			});
		}
	}

	private DataProxy<String, List<AbstractTableElement>> proxy;
	private Loader<String, List<AbstractTableElement>> loader;

	public TableStore(ApplicationDataProvider provider) {
		super(new ElementKeyProvider<AbstractTableElement>());

		proxy = new TableProxy(provider);
		loader = new Loader<String, List<AbstractTableElement>>(proxy);

		loader.addLoadHandler(new LoadHandler<String, List<AbstractTableElement>>() {
			@Override
			public void onLoad(LoadEvent<String, List<AbstractTableElement>> event) {
				TableStore.this.replaceAll(event.getLoadResult());
				AbstractTableElement empty = new PlainTableElement();
				empty.setId("");
				empty.setName(EditorMessage.Util.MESSAGE.property_no_data());
				TableStore.this.add(empty);
			}
		});
	}

	public Loader<String, List<AbstractTableElement>> getLoader() {
		return loader;
	}

}