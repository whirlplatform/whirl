package org.whirlplatform.editor.client.presenter;

import com.google.gwt.core.client.Callback;
import com.google.gwt.user.client.ui.IsWidget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;
import com.mvp4g.client.view.ReverseViewInterface;
import org.whirlplatform.editor.client.EditorEventBus;
import org.whirlplatform.editor.client.view.PropertyFormView;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.component.RandomUUID;
import org.whirlplatform.meta.shared.editor.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Presenter(view = PropertyFormView.class)
public class PropertyFormPresenter extends BasePresenter<PropertyFormPresenter.IPropertyFormView, EditorEventBus> {

    private FormElement currentElement;
    private CellRangeElement selectedCellsArea;
    private LocaleElement defaultLoclale;

    public interface IPropertyFormView extends ReverseViewInterface<PropertyFormPresenter>, IsWidget {

        List<RowElement> getRowsHeight();

        void setRowsHeight(ComponentType componentType, List<RowElement> rowsHeight);

        List<ColumnElement> getColumnsWidth();

        void setColumnsWidth(ComponentType componentType, List<ColumnElement> columnsWidth);

        List<RequestElement> getRequests();

        void setRequests(ComponentType componentType, List<RequestElement> requests);

        void initUI();

        void setLocales(Collection<LocaleElement> locales, LocaleElement defaultLocale);

    }

    public PropertyFormPresenter() {
        super();
    }

    @Override
    public void bind() {
        view.initUI();
    }

    public void onOpenElement(AbstractElement element) {
        if (element instanceof FormElement) {
            currentElement = (FormElement) element;
            eventBus.getApplication(new Callback<ApplicationElement, Throwable>() {
                @Override
                public void onSuccess(ApplicationElement result) {
                    defaultLoclale = result.getDefaultLocale();
                    view.setLocales(result.getLocales(), result.getDefaultLocale());
                }

                @Override
                public void onFailure(Throwable reason) {
                }
            });
            view.setRowsHeight(currentElement.getType(), currentElement.getRowsHeight());
            view.setColumnsWidth(currentElement.getType(), currentElement.getColumnsWidth());
            view.setRequests(currentElement.getType(), currentElement.getRowRequests());
            eventBus.changeSecondRightComponent(view);
        } else {
            eventBus.changeSecondRightComponent(null);
        }
    }

    public void onSetSelectedCellsArea(CellRangeElement model) {
        selectedCellsArea = model;
    }

    /**
     * Добавление строки в store грида
     *
     * @param index Индекс строки
     */
    public void onInsertRow(int index) {
        List<RowElement> rowsHeight = new ArrayList<RowElement>();
        rowsHeight.addAll(view.getRowsHeight());
        RowElement model = new RowElement();
        model.setId(RandomUUID.uuid());
        model.setRow(index);
        for (RowElement m : rowsHeight) {
            int i = m.getRow();
            if (i >= index) {
                m.setRow(i + 1);
            }
        }
        rowsHeight.add(model);
        view.setRowsHeight(currentElement.getType(), rowsHeight);
    }

    /**
     * Добавление колонки в store грида
     *
     * @param index Индекс колонки
     */
    public void onInsertColumn(int index) {
        List<ColumnElement> columnsWidth = new ArrayList<ColumnElement>();
        columnsWidth.addAll(view.getColumnsWidth());
        ColumnElement model = new ColumnElement();
        model.setId(RandomUUID.uuid());
        model.setColumn(index);
        for (ColumnElement m : columnsWidth) {
            int i = m.getColumn();
            if (i >= index) {
                m.setColumn(i + 1);
            }
        }
        columnsWidth.add(model);
        view.setColumnsWidth(currentElement.getType(), columnsWidth);
    }

    /**
     * Добавление новой строки в конец списка
     */
    public void addNewRow() {
        List<RowElement> rowsHeight = new ArrayList<RowElement>();
        rowsHeight.addAll(view.getRowsHeight());
        final LocaleElement locale = currentElement.getProperty(PropertyType.Rows).getDefaultLocale();
        Double rowsProp = currentElement.getProperty(PropertyType.Rows).getValue(locale).getDouble();
        int rows = rowsProp == null ? 0 : rowsProp.intValue();
        if (rows > rowsHeight.size()) {
            RowElement model = new RowElement();
            model.setId(RandomUUID.uuid());
            model.setRow(rowsHeight.size());
            rowsHeight.add(model);
            view.setRowsHeight(currentElement.getType(), rowsHeight);
        }
    }

    /**
     * Добавление нового столбца в конец списка
     */
    public void addNewColumn() {
        List<ColumnElement> columnsWidth = new ArrayList<ColumnElement>();
        columnsWidth.addAll(view.getColumnsWidth());
        final LocaleElement locale = currentElement.getProperty(PropertyType.Columns).getDefaultLocale();
        Double columnsProp = currentElement.getProperty(PropertyType.Columns).getValue(locale).getDouble();
        int columns = (columnsProp == null) ? 0 : columnsProp.intValue();
        if (columns > columnsWidth.size()) {
            ColumnElement model = new ColumnElement();
            model.setId(RandomUUID.uuid());
            model.setColumn(columnsWidth.size());
            columnsWidth.add(model);
            view.setColumnsWidth(currentElement.getType(), columnsWidth);
        }
    }

    /**
     * Удаление строки из store грида
     *
     * @param index Индекс строки
     */
    public void onDeleteRow(int index) {
        List<RowElement> rowsHeight = currentElement.getRowsHeight();
        RowElement model = null;
        for (RowElement m : rowsHeight) {
            if (m.getRow() == index) {
                model = m;
                break;
            }
        }
        if (model != null) {
            rowsHeight.remove(model);
            for (RowElement m : rowsHeight) {
                int i = m.getRow();
                if (i > 0 && i > index) {
                    m.setRow(i - 1);
                }
            }
            view.setRowsHeight(currentElement.getType(), rowsHeight);
        }
    }

    /**
     * Удаление колонки из store грида
     *
     * @param index Индекс колонки
     */
    public void onDeleteColumn(int index) {
        List<ColumnElement> columnsWidth = currentElement.getColumnsWidth();
        ColumnElement model = null;
        for (ColumnElement m : columnsWidth) {
            if (m.getColumn() == index) {
                model = m;
                break;
            }
        }
        if (model != null) {
            columnsWidth.remove(model);
            for (ColumnElement m : columnsWidth) {
                int i = m.getColumn();
                if (i > 0 && i > index) {
                    m.setColumn(i - 1);
                }
            }
            view.setColumnsWidth(currentElement.getType(), columnsWidth);
        }
    }

    public FormElement getElement() {
        return currentElement;
    }

    public CellRangeElement getSelectedCellsArea() {
        return selectedCellsArea;
    }

    public void onSyncRowsNumber(int rows) {
        int currRows = currentElement.getRowsHeight().size();

        if (currRows < rows) {
            for (int i = currRows; i < rows; i++) {
                addNewRow();
            }
        } else if (currRows > rows) {
            for (int i = currRows; i > rows; i--) {
                onDeleteRow(i - 1);
            }
        }
    }

    public void onSyncColumnsNumber(int columns) {
        int currCols = currentElement.getColumnsWidth().size();

        if (currCols < columns) {
            for (int i = currCols; i < columns; i++) {
                addNewColumn();
            }
        } else if (currCols > columns) {
            for (int i = currCols; i > columns; i--) {
                onDeleteColumn(i - 1);
            }
        }
    }

    public LocaleElement getDefaultLocale() {
        return defaultLoclale;
    }
}
