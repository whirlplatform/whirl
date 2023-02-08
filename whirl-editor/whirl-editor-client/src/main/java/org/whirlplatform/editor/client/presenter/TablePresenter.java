package org.whirlplatform.editor.client.presenter;

import com.google.gwt.core.client.Callback;
import com.google.gwt.user.client.ui.IsWidget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;
import com.mvp4g.client.view.ReverseViewInterface;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import org.whirlplatform.component.client.utils.InfoHelper;
import org.whirlplatform.editor.client.EditorEventBus;
import org.whirlplatform.editor.client.meta.NewTableColumnElement;
import org.whirlplatform.editor.client.view.TableView;
import org.whirlplatform.editor.shared.i18n.EditorMessage;
import org.whirlplatform.meta.shared.component.RandomUUID;
import org.whirlplatform.meta.shared.editor.AbstractElement;
import org.whirlplatform.meta.shared.editor.ApplicationElement;
import org.whirlplatform.meta.shared.editor.LocaleElement;
import org.whirlplatform.meta.shared.editor.PropertyValue;
import org.whirlplatform.meta.shared.editor.RightType;
import org.whirlplatform.meta.shared.editor.db.PlainTableElement;
import org.whirlplatform.meta.shared.editor.db.TableColumnElement;
import org.whirlplatform.meta.shared.editor.db.ViewElement;

@Presenter(view = TableView.class)
public class TablePresenter extends BasePresenter<TablePresenter.ITableView, EditorEventBus>
    implements ElementPresenter {

    private PlainTableElement table;
    private TextButton saveButton;
    private TextButton closeButton;

    public TablePresenter() {
        super();
    }

    @Override
    public void bind() {
        ContentPanel panel = ((ContentPanel) view.asWidget());
        saveButton = new TextButton(EditorMessage.Util.MESSAGE.apply(), new SelectHandler() {
            @Override
            public void onSelect(SelectEvent e) {
                // TODO эти действия должны происходить в
                // ElementEventHandler
                table.setTitle(view.getTableTitle());

                table.setTableName(view.getTableName());
                for (PlainTableElement t : table.getClones()) {
                    t.setTableName(view.getTableName());
                }
                table.setIdColumn(view.getIdColumn());
                table.setDeleteColumn(view.getDeleteColumn());
                table.changeColumns(view.getColumns());
                table.setCode(view.getCode());
                table.setEmptyRow(view.isEmptyRow());
                if (view.getViewName() == null || view.getViewName().isEmpty()) {
                    table.setView(null);
                } else {
                    // TODO представления надо тоже создавать отдельно
                    ViewElement viewElement = table.getView();
                    if (viewElement == null) {
                        viewElement = new ViewElement();
                        viewElement.setId(RandomUUID.uuid());
                        table.setView(viewElement);
                    }
                    viewElement.setName(
                        EditorMessage.Util.MESSAGE.save() + " - " + view.getViewName());
                    viewElement.setViewName(view.getViewName());
                    //                    viewElement.setSource(view.getViewSource());
                }

                eventBus.syncServerApplication();

                InfoHelper.display(EditorMessage.Util.MESSAGE.success(),
                    EditorMessage.Util.MESSAGE.info_table_saved());
            }
        });
        panel.addButton(saveButton);
        closeButton = new TextButton(EditorMessage.Util.MESSAGE.close(), new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                eventBus.closeElementView();
            }
        });
        panel.addButton(closeButton);
    }

    public void onOpenElement(AbstractElement element) {
        showElement(element, false);
    }

    public void newColumn() {
        eventBus.addElementCallback(table, new NewTableColumnElement(),
            new Callback<AbstractElement, Throwable>() {

                @Override
                public void onFailure(Throwable reason) {
                    InfoHelper.throwInfo("add-event", reason);
                }

                @Override
                public void onSuccess(AbstractElement result) {
                    ((TableColumnElement) result).setIndex(view.getColumns().size());
                    view.addColumn((TableColumnElement) result);
                }
            });
    }

    public void onEditRightsElement() {
        eventBus.editRights(Collections.unmodifiableCollection(table.getColumns()),
            Collections.unmodifiableCollection(
                Arrays.asList(/* RightType.ADD, RightType.DELETE, */RightType.EDIT,
                    RightType.VIEW)));
    }

    public void onSynchronizeCloneFields() {
        PlainTableElement clonedTable = table.getClonedTable();
        boolean exists;
        for (TableColumnElement clonedColumn : clonedTable.getColumns()) {
            exists = false;
            for (TableColumnElement column : view.getColumns()) {
                if (clonedColumn.getName().equalsIgnoreCase(column.getName())) {
                    exists = true;
                }
            }
            if (exists) {
                continue;
            }
            TableColumnElement newColumn = clonedColumn.clone();
            newColumn.setId(RandomUUID.uuid());
            view.addColumn(newColumn);
        }
    }

    public void onViewElement(AbstractElement element) {
        showElement(element, true);
    }

    private void showElement(AbstractElement element, boolean readOnly) {
        if (!(element instanceof PlainTableElement)) {
            return;
        }
        table = (PlainTableElement) element;
        view.clearUI();
        eventBus.getApplication(new Callback<ApplicationElement, Throwable>() {
            @Override
            public void onSuccess(ApplicationElement result) {
                view.setLocales(result.getLocales(), result.getDefaultLocale());
            }

            @Override
            public void onFailure(Throwable reason) {
            }
        });
        view.setHeaderText(
            EditorMessage.Util.MESSAGE.editing_table() + table.getTableName() + " - "
                + table.getName());
        view.setTableTitle(table.getTitle());
        view.setCode(table.getCode());
        view.setEmptyRow(table.isEmptyRow());
        if (table.isClone()) {
            view.setClone(true);
            // view.setTableName(table.getClonedTable().getTableName());
        }
        view.setTableName(table.getTableName());
        if (table.getView() != null) {
            view.setViewName(table.getView().getViewName());
            //            view.setViewSource(table.getView().getSource());
        }
        view.setIdColumn(table.getIdColumn());
        view.setDeleteColumn(table.getDeleteColumn());
        for (TableColumnElement c : table.getColumns()) {
            view.addColumn(c);
        }
        eventBus.openElementView(view);
        view.setEnableAll(!readOnly);
        saveButton.setEnabled(!readOnly);
    }

    public interface ITableView extends ReverseViewInterface<TablePresenter>, IsWidget {

        void setHeaderText(String text);

        PropertyValue getTableTitle();

        void setTableTitle(PropertyValue title);

        String getTableName();

        void setTableName(String tableName);

        TableColumnElement getIdColumn();

        void setIdColumn(TableColumnElement idColumn);

        TableColumnElement getDeleteColumn();

        void setDeleteColumn(TableColumnElement deleteColumn);

        void addColumn(TableColumnElement column);

        Collection<TableColumnElement> getColumns();

        String getViewName();

        void setViewName(String viewName);

        //        String getViewSource();

        //        void setViewSource(String viewSource);

        void clearUI();

        void setLocales(Collection<LocaleElement> locales, LocaleElement defaultLocale);

        void setClone(boolean clone);

        String getCode();

        void setCode(String code);

        boolean isEmptyRow();

        void setEmptyRow(boolean emptyRow);

        void setEnableAll(boolean enable);

    }
}
