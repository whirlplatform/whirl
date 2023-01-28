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
import java.util.Collection;
import org.whirlplatform.component.client.utils.InfoHelper;
import org.whirlplatform.editor.client.EditorEventBus;
import org.whirlplatform.editor.client.view.DynamicTableView;
import org.whirlplatform.editor.shared.i18n.EditorMessage;
import org.whirlplatform.meta.shared.editor.AbstractElement;
import org.whirlplatform.meta.shared.editor.ApplicationElement;
import org.whirlplatform.meta.shared.editor.LocaleElement;
import org.whirlplatform.meta.shared.editor.PropertyValue;
import org.whirlplatform.meta.shared.editor.db.DynamicTableElement;

@Presenter(view = DynamicTableView.class)
public class DynamicTablePresenter
        extends BasePresenter<DynamicTablePresenter.IDynaimcTableView, EditorEventBus>
        implements ElementPresenter {

    private DynamicTableElement table;
    private TextButton saveButton;
    private TextButton closeButton;

    @Override
    public void bind() {
        ContentPanel panel = (ContentPanel) view;
        saveButton = new TextButton(EditorMessage.Util.MESSAGE.apply(), new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                String getMetadataFunction = view.getMetadataFunction();
                String getDataFunction = view.getDataFunction();
                String insertFunction = view.getInsertFunction();
                String updateFunction = view.getUpdateFunction();
                String deleteFunction = view.getDeleteFunction();
                String errorText = "";

                // Собираем текст ошибки (если есть)
                if (getMetadataFunction == null || getMetadataFunction.isEmpty()) {
                    errorText += EditorMessage.Util.MESSAGE.error_dynamic_table_empty_metadata()
                        + "<br/>";
                }
                if (getDataFunction == null || getDataFunction.isEmpty()) {
                    errorText +=
                            EditorMessage.Util.MESSAGE.error_dynamic_table_empty_data() + "<br/>";
                }
                if (getDataFunction != null && !getDataFunction.isEmpty()
                        && (!getDataFunction.contains(":data_config")
                        || !getDataFunction.contains(":data_count"))) {
                    errorText +=
                            EditorMessage.Util.MESSAGE.error_dynamic_table_get_data() + "<br/>";
                }
                if (insertFunction != null && !insertFunction.isEmpty()
                        && !insertFunction.contains(":insert_config")) {
                    errorText += EditorMessage.Util.MESSAGE.error_dynamic_table_insert() + "<br/>";
                }
                if (updateFunction != null && !updateFunction.isEmpty()
                        && !updateFunction.contains(":update_config")) {
                    errorText += EditorMessage.Util.MESSAGE.error_dynamic_table_update() + "<br/>";
                }
                if (deleteFunction != null && !deleteFunction.isEmpty()
                        && !deleteFunction.contains(":delete_config")) {
                    errorText += EditorMessage.Util.MESSAGE.error_dynamic_table_delete() + "<br/>";
                }

                // Если есть ошибка, выводим пользователю, ничего не делаем
                if (!errorText.isEmpty()) {
                    InfoHelper.error("save-table", EditorMessage.Util.MESSAGE.error(), errorText);
                    return;
                }
                table.setTitle(view.getTableTitle());
                table.setCode(view.getCode());
                table.setEmptyRow(view.getEmptyRow());
                table.setMetadataFunction(getMetadataFunction);
                table.setDataFunction(getDataFunction);
                table.setInsertFunction(insertFunction);
                table.setUpdateFunction(updateFunction);
                table.setDeleteFunction(deleteFunction);

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

    @Override
    public void onOpenElement(AbstractElement element) {
        if (element instanceof DynamicTableElement) {
            showElement((DynamicTableElement) element, false);
        }
    }

    public void onViewElement(AbstractElement element) {
        if (element instanceof DynamicTableElement) {
            showElement((DynamicTableElement) element, true);
        }
    }

    private void showElement(DynamicTableElement element, boolean readOnly) {
        table = element;

        view.clearFields();

        eventBus.getApplication(new Callback<ApplicationElement, Throwable>() {
            @Override
            public void onSuccess(ApplicationElement result) {
                view.setLocales(result.getLocales(), result.getDefaultLocale());
            }

            @Override
            public void onFailure(Throwable reason) {
            }
        });
        view.setHeaderText(EditorMessage.Util.MESSAGE.editing_table() + table.getName());
        view.setTableTitle(table.getTitle());
        view.setCode(table.getCode());
        view.setEmptyRow(table.isEmptyRow());
        view.setMetadataFunction(table.getMetadataFunction());
        view.setDataFunction(table.getDataFunction());
        view.setInsertFunction(table.getInsertFunction());
        view.setUpdateFunction(table.getUpdateFunction());
        view.setDeleteFunction(table.getDeleteFunction());
        eventBus.openElementView(view);
        view.setEnableAll(!readOnly);
        saveButton.setEnabled(!readOnly);
    }

    public interface IDynaimcTableView
            extends ReverseViewInterface<DynamicTablePresenter>, IsWidget {

        PropertyValue getTableTitle();

        void setTableTitle(PropertyValue title);

        String getCode();

        void setCode(String code);

        String getMetadataFunction();

        void setMetadataFunction(String metadataFunction);

        String getDataFunction();

        void setDataFunction(String dataFunction);

        String getInsertFunction();

        void setInsertFunction(String insertFunction);

        String getUpdateFunction();

        void setUpdateFunction(String updateFunction);

        String getDeleteFunction();

        void setDeleteFunction(String detleteFunction);

        boolean getEmptyRow();

        void setEmptyRow(boolean emptyRow);

        void setLocales(Collection<LocaleElement> locales, LocaleElement defaultLocale);

        void clearFields();

        void setHeaderText(String headerText);

        void setEnableAll(boolean enabled);
    }
}
