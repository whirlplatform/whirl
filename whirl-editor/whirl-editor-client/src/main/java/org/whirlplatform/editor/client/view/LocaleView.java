package org.whirlplatform.editor.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.editor.client.EditorError;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.CancelEditEvent;
import com.sencha.gxt.widget.core.client.event.CancelEditEvent.CancelEditHandler;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent.CompleteEditHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.validator.AbstractValidator;
import com.sencha.gxt.widget.core.client.form.validator.MaxLengthValidator;
import com.sencha.gxt.widget.core.client.form.validator.MinLengthValidator;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.Grid.GridCell;
import com.sencha.gxt.widget.core.client.grid.editing.ClicksToEdit;
import com.sencha.gxt.widget.core.client.grid.editing.GridInlineEditing;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.whirlplatform.editor.client.presenter.LocalePresenter;
import org.whirlplatform.editor.client.presenter.LocalePresenter.ILocaleView;
import org.whirlplatform.editor.shared.i18n.EditorMessage;
import org.whirlplatform.meta.shared.editor.LocaleElement;

public class LocaleView extends ContentPanel implements ILocaleView {

    private static final LocaleProperties properties = GWT
            .create(LocaleProperties.class);
    private LocaleElement newLocale = null;
    private LocalePresenter presenter;
    private Grid<LocaleElement> grid;
    private ListStore<LocaleElement> store;
    private GridInlineEditing<LocaleElement> editing;
    private VerticalLayoutContainer container;

    public LocaleView() {
        super();
        initUI();
    }

    private void initUI() {
        setHeaderVisible(true);
        setHeading(EditorMessage.Util.MESSAGE.editing_locale());
        container = new VerticalLayoutContainer();
        createGrid();
        createButtons();
        setWidget(container);
    }

    private void createGrid() {
        ColumnConfig<LocaleElement, String> cc1 = new ColumnConfig<LocaleElement, String>(
                properties.language(), 100,
                EditorMessage.Util.MESSAGE.locale_lang());
        ColumnConfig<LocaleElement, String> cc2 = new ColumnConfig<LocaleElement, String>(
                properties.country(), 100,
                EditorMessage.Util.MESSAGE.locale_country());

        List<ColumnConfig<LocaleElement, ?>> l = new ArrayList<ColumnConfig<LocaleElement, ?>>();
        l.add(cc1);
        l.add(cc2);

        ColumnModel<LocaleElement> cm = new ColumnModel<LocaleElement>(l);
        store = new ListStore<LocaleElement>(properties.key());

        grid = new Grid<LocaleElement>(store, cm);
        grid.setWidth(300);
        grid.setHeight(300);
        grid.setBorders(false);
        grid.getView().setStripeRows(true);
        grid.getView().setColumnLines(true);
        grid.getView().setShowDirtyCells(false);

        editing = new GridInlineEditing<LocaleElement>(grid);
        editing.setClicksToEdit(ClicksToEdit.TWO);

        TextField editor1 = new TextField();
        editor1.setAllowBlank(false);
        editor1.addValidator(new MaxLengthValidator(2));
        editor1.addValidator(new MinLengthValidator(2));
        editor1.addValidator(new CustomValidator());

        TextField editor2 = new TextField();

        editing.addEditor(cc1, editor1);
        editing.addEditor(cc2, editor2);

        editing.addCompleteEditHandler(new CompleteEditHandler<LocaleElement>() {

            @Override
            public void onCompleteEdit(CompleteEditEvent<LocaleElement> event) {
                if (newLocale != null) {
                    Store<LocaleElement>.Record record = store.getRecord(newLocale);
                    LocaleElement locale = new LocaleElement();
                    locale.setLanguage(record.getValue(properties.language()));
                    locale.setCountry(record.getValue(properties.country()));
                    addLocale(locale);
                    store.remove(newLocale);
                    newLocale = null;
                }
                store.commitChanges();
            }

        });
        editing.addCancelEditHandler(new CancelEditHandler<LocaleElement>() {

            @Override
            public void onCancelEdit(CancelEditEvent<LocaleElement> event) {
                if (newLocale != null) {
                    store.remove(newLocale);
                    newLocale = null;
                    store.commitChanges();
                } else {
                    store.rejectChanges();
                }
            }
        });

        TextButton add = new TextButton(EditorMessage.Util.MESSAGE.locale_add());
        add.addSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                editing.cancelEditing();

                // TODO создание элементов перенести в ElementEventHandler
                newLocale = new LocaleElement();
                addLocale(newLocale);

                editing.startEditing(new GridCell(0, 0));
            }

        });
        TextButton remove = new TextButton(
                EditorMessage.Util.MESSAGE.locale_remove());
        remove.addSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                List<LocaleElement> l = grid.getSelectionModel()
                        .getSelectedItems();
                for (LocaleElement le : l) {
                    removeLocale(le);
                }
            }

        });
        ToolBar toolBar = new ToolBar();
        toolBar.add(add);
        toolBar.add(remove);
        container.add(toolBar, new VerticalLayoutData(1, -1));
        container.add(grid, new VerticalLayoutData(1, 1));
    }

    private void createButtons() {
        addButton(new TextButton(EditorMessage.Util.MESSAGE.apply(),
                new SelectHandler() {

                    @Override
                    public void onSelect(SelectEvent event) {
                        store.commitChanges();
                        editing.cancelEditing();
                        presenter.getEventBus().updateLocales(store.getAll());
                        presenter.getEventBus().closeElementView();
                    }

                }));
        addButton(new TextButton(EditorMessage.Util.MESSAGE.close(),
                new SelectHandler() {

                    @Override
                    public void onSelect(SelectEvent event) {
                        editing.cancelEditing();
                        presenter.getEventBus().closeElementView();
                    }

                }));
    }

    private void addLocale(LocaleElement element) {
        store.add(0, element);
    }

    private void removeLocale(LocaleElement element) {
        store.remove(element);
    }

    @Override
    public LocalePresenter getPresenter() {
        return presenter;
    }

    @Override
    public void setPresenter(LocalePresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setLocales(Collection<LocaleElement> locales) {
        store.clear();
        store.addAll(locales);
    }

    interface LocaleProperties extends PropertyAccess<LocaleElement> {
        @Path("language")
        ModelKeyProvider<LocaleElement> key();

        ValueProvider<LocaleElement, String> language();

        ValueProvider<LocaleElement, String> country();
    }

    public class CustomValidator extends AbstractValidator<String> {

        @Override
        public List<EditorError> validate(Editor<String> editor, String value) {
            List<EditorError> errors = null;

            boolean duplicate = false;
            int currentRow = editing.getActiveCell().getRow();

            for (LocaleElement le : store.getAll()) {
                if (le.getLanguage() == null) {
                    continue;
                }
                if (le.getLanguage().equalsIgnoreCase(value)) {
                    int row = store.indexOf(le);
                    if (currentRow != row) {
                        duplicate = true;
                    }
                }
            }
            if (duplicate) {
                String message = EditorMessage.Util.MESSAGE.locale_err_msg();
                errors = createError(editor, message, value);
            }
            return errors;
        }

    }
}
