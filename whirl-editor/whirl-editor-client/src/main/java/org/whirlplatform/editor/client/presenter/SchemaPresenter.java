package org.whirlplatform.editor.client.presenter;

import com.google.gwt.core.client.Callback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import java.util.Collection;
import java.util.HashSet;
import org.whirlplatform.component.client.utils.InfoHelper;
import org.whirlplatform.editor.client.EditorEventBus;
import org.whirlplatform.editor.client.presenter.SchemaPresenter.ISchemaView;
import org.whirlplatform.editor.client.view.SchemaView;
import org.whirlplatform.editor.shared.EditorDataService;
import org.whirlplatform.editor.shared.i18n.EditorMessage;
import org.whirlplatform.meta.shared.data.RowModelData;
import org.whirlplatform.meta.shared.editor.AbstractElement;
import org.whirlplatform.meta.shared.editor.db.AbstractTableElement;
import org.whirlplatform.meta.shared.editor.db.PlainTableElement;
import org.whirlplatform.meta.shared.editor.db.SchemaElement;

@Presenter(view = SchemaView.class)
public class SchemaPresenter extends BasePresenter<ISchemaView, EditorEventBus>
        implements ElementPresenter {

    private SchemaElement schema;

    public SchemaPresenter() {
        super();
    }

    @Override
    public void bind() {
        ContentPanel panel = ((ContentPanel) view.asWidget());
        panel.addButton(new TextButton(EditorMessage.Util.MESSAGE.apply(), new SelectHandler() {
            @Override
            public void onSelect(SelectEvent e) {
                schema.setSchemaName(view.getSchema());
                eventBus.syncServerApplication();
                view.clearValues();
                view.setSchemaName(schema.getSchemaName());
            }
        }));
        panel.addButton(new TextButton(EditorMessage.Util.MESSAGE.close(), new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                eventBus.closeElementView();
            }
        }));

        view.addRefreshImportHandler(new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                view.startProcessing();
                EditorDataService.Util.getDataService()
                        .getTableImportList(schema.getDataSource(), schema,
                                new AsyncCallback<Collection<RowModelData>>() {
                                    @Override
                                    public void onSuccess(Collection<RowModelData> result) {
                                        view.setImports(result);
                                        view.stopProcessing();
                                    }

                                    @Override
                                    public void onFailure(Throwable caught) {
                                        InfoHelper.throwInfo("get-table-import-list", caught);
                                        view.stopProcessing();
                                    }
                                });
            }
        });
        view.addImportHandler(new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                importTableElements(view.getCheckedImports());
            }
        });
    }

    public void onOpenElement(AbstractElement element) {
        if (!(element instanceof SchemaElement)) {
            return;
        }
        schema = (SchemaElement) element;
        view.setHeaderText(EditorMessage.Util.MESSAGE.editing_schema() + schema.getName());
        view.clearValues();
        view.setSchemaName(schema.getSchemaName());

        eventBus.openElementView(view);
    }

    public void importTableElements(final Collection<RowModelData> models) {
        final AsyncCallback<Collection<PlainTableElement>> callback =
                new AsyncCallback<Collection<PlainTableElement>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        InfoHelper.throwInfo("import-tables", caught);
                        view.stopProcessing();
                    }

                    @Override
                    public void onSuccess(Collection<PlainTableElement> result) {
                        for (PlainTableElement t : result) {
                            eventBus.addElement(schema, t);
                        }
                        InfoHelper.display(EditorMessage.Util.MESSAGE.success(),
                                EditorMessage.Util.MESSAGE.info_tables_imported());
                        view.stopProcessing();
                    }
                };

        // Проверка на наличие таблиц в приложении
        eventBus.getAvailableTables(new Callback<Collection<AbstractTableElement>, Throwable>() {
            @Override
            public void onFailure(Throwable reason) {
                InfoHelper.throwInfo("get-available-tables", reason);
            }

            @Override
            public void onSuccess(Collection<AbstractTableElement> result) {
                Collection<RowModelData> uniqueModels = new HashSet<RowModelData>();
                uniqueModels.addAll(models);
                StringBuilder tables = new StringBuilder();
                for (RowModelData model : models) {
                    for (AbstractTableElement table : result) {
                        if (table.getId().equals(model.getId())) {
                            tables.append(table.getName() + ", ");
                            uniqueModels.remove(model);
                        }
                    }
                }
                if (tables.length() > 0) {
                    String warnMsg = EditorMessage.Util.MESSAGE.warn_this_tables_already_imported()
                            + tables.substring(0, tables.length() - 2);
                    InfoHelper.warning("get-available-tables",
                            EditorMessage.Util.MESSAGE.warn_tables_already_imported(), warnMsg);
                }
                if (uniqueModels.size() > 0) {
                    view.startProcessing();
                    EditorDataService.Util.getDataService()
                            .importTables(schema.getDataSource(), schema, uniqueModels,
                                    callback);
                }
            }
        });
    }

    public interface ISchemaView extends IsWidget {

        void setHeaderText(String text);

        void setSchemaName(String schema);

        String getSchema();

        void setImports(Collection<RowModelData> imports);

        Collection<RowModelData> getCheckedImports();

        void addRefreshImportHandler(SelectHandler handler);

        void addImportHandler(SelectHandler handler);

        void clearValues();

        void startProcessing();

        void stopProcessing();

    }

}
