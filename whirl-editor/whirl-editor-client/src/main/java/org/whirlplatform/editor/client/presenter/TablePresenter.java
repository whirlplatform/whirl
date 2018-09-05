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
import org.whirlplatform.component.client.utils.InfoHelper;
import org.whirlplatform.editor.client.EditorEventBus;
import org.whirlplatform.editor.client.meta.NewTableColumnElement;
import org.whirlplatform.editor.client.view.TableView;
import org.whirlplatform.editor.shared.i18n.EditorMessage;
import org.whirlplatform.meta.shared.component.RandomUUID;
import org.whirlplatform.meta.shared.editor.*;
import org.whirlplatform.meta.shared.editor.db.PlainTableElement;
import org.whirlplatform.meta.shared.editor.db.TableColumnElement;
import org.whirlplatform.meta.shared.editor.db.ViewElement;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

@Presenter(view = TableView.class)
public class TablePresenter extends BasePresenter<TablePresenter.ITableView, EditorEventBus>
        implements ElementPresenter {

    public interface ITableView extends ReverseViewInterface<TablePresenter>, IsWidget {

        void setHeaderText(String text);

        void setTableTitle(PropertyValue title);

        PropertyValue getTableTitle();

        void setTableName(String tableName);

        String getTableName();

        void setIdColumn(TableColumnElement idColumn);

        TableColumnElement getIdColumn();

        void setDeleteColumn(TableColumnElement deleteColumn);

        TableColumnElement getDeleteColumn();

        void addColumn(TableColumnElement column);

        Collection<TableColumnElement> getColumns();

        void setViewName(String viewName);

        String getViewName();

        void setViewSource(String viewSource);

        String getViewSource();

        void setListName(String listName);

        String getListName();

        void setListSource(String listSource);

        String getListSource();

        void clearUI();

        void setLocales(Collection<LocaleElement> locales, LocaleElement defaultLocale);

        void setClone(boolean clone);

        void setCode(String code);

        String getCode();

        void setEmptyRow(boolean emptyRow);

        boolean isEmptyRow();

        void setEnableAll(boolean enable);

        void setSimple(boolean simple);

        boolean isSimple();
    }

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
                table.setSimple(view.isSimple());
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
                    viewElement.setName(EditorMessage.Util.MESSAGE.save() + " - " + view.getViewName());
                    viewElement.setViewName(view.getViewName());
                    viewElement.setSource(view.getViewSource());
                }

                if (view.getListName() == null || view.getListName().isEmpty()) {
                    table.setList(null);
                } else {
                    // TODO представления надо тоже создавать отдельно
                    ViewElement listElement = table.getList();
                    if (listElement == null) {
                        listElement = new ViewElement();
                        listElement.setId(RandomUUID.uuid());
                        table.setList(listElement);
                    }
                    listElement.setName(EditorMessage.Util.MESSAGE.editing_table_view() + " - " + view.getListName());
                    listElement.setViewName(view.getListName());
                    listElement.setSource(view.getListSource());
                }

                eventBus.syncServerApplication();

                InfoHelper.display(EditorMessage.Util.MESSAGE.success(), EditorMessage.Util.MESSAGE.info_table_saved());
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
        eventBus.addElementCallback(table, new NewTableColumnElement(), new Callback<AbstractElement, Throwable>() {

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
        eventBus.editRights(Collections.unmodifiableCollection(table.getColumns()), Collections.unmodifiableCollection(
                Arrays.asList(/* RightType.ADD, RightType.DELETE, */RightType.EDIT, RightType.VIEW)));
    }

    public void onSynchronizeCloneFields() {
        PlainTableElement pTable = table.getClonedTable();
        boolean exists;
        for (TableColumnElement pColumn : pTable.getColumns()) {
            exists = false;
            for (TableColumnElement column : view.getColumns()) {
                if (pColumn.getName().equalsIgnoreCase(column.getName())) {
                    exists = true;
                }
            }
            if (exists) {
                continue;
            }
            TableColumnElement newColumn = pColumn.clone();
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
        view.setHeaderText(EditorMessage.Util.MESSAGE.editing_table() + table.getTableName() + " - " + table.getName());
        view.setTableTitle(table.getTitle());
        view.setCode(table.getCode());
        view.setEmptyRow(table.isEmptyRow());
        view.setSimple(table.isSimple());
        if (table.isClone()) {
            view.setClone(true);
            // view.setTableName(table.getClonedTable().getTableName());
        }
        view.setTableName(table.getTableName());
        if (table.getView() != null) {
            view.setViewName(table.getView().getViewName());
            view.setViewSource(table.getView().getSource());
        }
        if (table.getList() != null) {
            view.setListName(table.getList().getViewName());
            view.setListSource(table.getList().getSource());
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
}
