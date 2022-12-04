package org.whirlplatform.component.client.grid;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.WhiteSpace;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.cell.core.client.form.TriggerFieldCell.TriggerFieldAppearance;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.Util;
import com.sencha.gxt.data.shared.StringLabelProvider;
import com.sencha.gxt.theme.base.client.field.TriggerFieldDefaultAppearance;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.Status;
import com.sencha.gxt.widget.core.client.button.CellButtonBase;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.button.ToggleButton;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.NumberField;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import com.sencha.gxt.widget.core.client.menu.CheckMenuItem;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import com.sencha.gxt.widget.core.client.tips.ToolTipConfig;
import com.sencha.gxt.widget.core.client.toolbar.FillToolItem;
import com.sencha.gxt.widget.core.client.toolbar.SeparatorToolItem;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;
import java.util.ArrayList;
import java.util.List;
import org.whirlplatform.component.client.event.ChangeEvent;
import org.whirlplatform.component.client.resource.ApplicationBundle;
import org.whirlplatform.component.client.selenium.Locator;
import org.whirlplatform.component.client.selenium.LocatorAware;
import org.whirlplatform.meta.shared.PageConfig;
import org.whirlplatform.meta.shared.i18n.AppMessage;

/**
 * Панель страниц для {@link EditGridBuilder}
 */
class GridPagingToolBar extends ToolBar implements LocatorAware {

    private static final int PAGING_COUNT = 5; /*
     * Максимальное количество
     * отображаемых кнопок
     * пагинатора. Например, для
     * значения 3 пагинатор будет
     * выглядеть примерно так: <<
     * ...[15] [16] [17] ... >> для
     * значения 5 - вот так: <<
     * ...[2] [3] [4] [5] [6] ... >>
     */
    private static final int CHAR_PIXELS = 8;
    private boolean buttonPressed;
    private boolean comboPressed;
    private HBoxLayoutContainer buttonsContainer;
    private TextButton first;
    private TextButton prev;
    private TextButton next;
    private TextButton last;
    private TextButton fastPrev;
    private TextButton fastNext;
    private SeparatorToolItem pgSeparator;
    private SimpleComboBox<Integer> selector;
    private List<PageButton> pageButtons = new ArrayList<PageButton>(PAGING_COUNT);
    private NumberField<Integer> numField;
    private Status numLabel;
    private Status filter;
    private int oldValue;
    private ChangeEvent changeEvent = new ChangeEvent();
    /**
     * Текущая страница
     */
    private int page;
    /**
     * Строк на таблицу
     */
    private int rows;
    /**
     * Количество страниц
     */
    private int pageCount;
    /**
     * Общее количество строк
     */
    private int rowCount;
    private int currPagin;

    /**
     * Конструктор {@link GridPagingToolBar}
     *
     * @param classList   - идентификатор таблицы
     * @param gridBuilder - {@link EditGridBuilder}
     */
    public GridPagingToolBar() {

        setEnableOverflow(true);
        setDeferHeight(true);

        initButtons();

        initFields();
        initSelector();
        initStatus();

        initListeners();
    }

    private void setButtonsVisible(boolean val) {
        setVisible(fastPrev, val);
        setVisible(first, val);
        setVisible(prev, val);
        setVisible(next, val);
        setVisible(last, val);

        setVisible(fastNext, val);
        for (int i = 0; i < pageButtons.size(); i++) {
            setVisible(pageButtons.get(i), val);
        }
        setVisible(pgSeparator, val);
    }

    /**
     * Храню флаги видимости в component.data. Сам этот метод не делает компонент видимым. Действие
     * происходит позже. Внутри класса GridPagingToolbar.
     */
    private void setVisible(Component c, Boolean v) {
        c.setData("c-visible", v);
    }

    /**
     * Проверка: показывать или скрывать компонент.
     */
    private Boolean isVisible(Component c) {
        if (c == null) {
            return Boolean.FALSE;
        }
        Object data = c.getData("c-visible");
        return (data != null) ? (Boolean) data : Boolean.FALSE;
    }

    /**
     * Создание компонентов линейки пагинатора без привязки их к контейнеру.
     */
    private void createPagButtons() {

        buttonsContainer = new HBoxLayoutContainer();
        buttonsContainer.setHBoxLayoutAlign(HBoxLayoutAlign.MIDDLE);
        this.add(buttonsContainer);

        first = new TextButton();
        first.setIcon(ApplicationBundle.INSTANCE.first());
        first.addSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                page = 1;
                ((TextButton) event.getSource()).focus();
                changePage();
            }
        });

        prev = new TextButton();
        prev.setIcon(ApplicationBundle.INSTANCE.previous());
        prev.addSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                page--;
                if (page < 1)
                    page = 1;
                ((TextButton) event.getSource()).focus();
                changePage();
            }
        });

        fastPrev = new TextButton();
        fastPrev.setText("...");
        fastPrev.addSelectHandler(new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                page -= PAGING_COUNT;
                if (page < 1)
                    page = 1;
                ((TextButton) event.getSource()).focus();
                changePage();
            }
        });

        for (int i = 0; i < PAGING_COUNT; i++) {
            PageButton btn = new PageButton(i + 1);
            btn.setWidth(25);
            pageButtons.add(btn);

            btn.addSelectHandler(new SelectHandler() {

                @Override
                public void onSelect(SelectEvent event) {
                    PageButton button = (PageButton) event.getSource();
                    page = button.getNumber();
                    button.focus();
                    changePage();
                }
            });
        }

        fastNext = new TextButton();
        fastNext.setText("...");
        fastNext.addSelectHandler(new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                page += PAGING_COUNT;
                if (page > pageCount)
                    page = pageCount;

                ((TextButton) event.getSource()).focus();
                changePage();
            }
        });

        next = new TextButton();
        next.setIcon(ApplicationBundle.INSTANCE.next());
        next.addSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                page++;
                if (page > pageCount)
                    page = pageCount;
                ((TextButton) event.getSource()).focus();
                changePage();
            }
        });

        last = new TextButton();
        last.setIcon(ApplicationBundle.INSTANCE.last());

        last.addSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                page = pageCount;
                ((TextButton) event.getSource()).focus();
                changePage();
            }
        });

        pgSeparator = new SeparatorToolItem();
        setButtonsVisible(true);

    }

    /**
     * Физическое отделение от контейнера.
     */
    private void clearPagButtons() {
        buttonsContainer.remove(first);
        buttonsContainer.remove(prev);
        buttonsContainer.remove(fastPrev);

        for (PageButton pb : pageButtons) {
            buttonsContainer.remove(pb);
        }

        buttonsContainer.remove(fastNext);
        buttonsContainer.remove(next);
        buttonsContainer.remove(last);
        buttonsContainer.remove(pgSeparator);
    }

    private void addIfVisible(Component c) {
        if (isVisible(c)) {
            buttonsContainer.add(c);
        }
    }

    /**
     * Управление видимостью посредством условного добавления компонентов к контейнеру. Вместо
     * стандартного c.setVisible(), которое некорректно работает если компонент в текущий момент
     * невидим.
     */
    private void arrangePagButtons() {
        addIfVisible(first);
        addIfVisible(prev);
        addIfVisible(fastPrev);

        for (PageButton pb : pageButtons) {
            addIfVisible(pb);
        }

        addIfVisible(fastNext);
        addIfVisible(next);
        addIfVisible(last);
        addIfVisible(pgSeparator);

        // приходится перерисовывать элементы,
        // т.к. в некоторых случаях наблюдается наезжание элементов.
        buttonsContainer.forceLayout();

    }

    /**
     * Инициализировать кнопки
     */
    private void initButtons() {
        createPagButtons();
        arrangePagButtons();
    }

    private void changePage() {
        buttonPressed = true;
        fireEvent(changeEvent);
    }

    private void initFields() {
        numField = new NumberField<Integer>(new NumberPropertyEditor.IntegerPropertyEditor());
        numField.setWidth(30);
        numField.setAllowDecimals(false);
        add(numField);
        add(new SeparatorToolItem());

        numLabel = new Status();
        numLabel.setWidth(180);
        numLabel.setText("");
        add(numLabel);
    }

    private void initSelector() {
        selector = new SimpleComboBox<Integer>(new StringLabelProvider<Integer>());

        selector.add(10);
        selector.add(15);
        selector.add(20);
        selector.add(30);
        selector.add(40);
        selector.add(50);
        selector.add(100);
        selector.add(500);

        selector.setWidth(55);
        selector.setTriggerAction(TriggerAction.ALL);
        selector.setSelectOnFocus(true);
        selector.setEditable(false);

        SelectionHandler<Integer> handler = new SelectionHandler<Integer>() {

            @Override
            public void onSelection(SelectionEvent event) {
                setCurrentComboBox((Integer) event.getSelectedItem());

            }
        };
        selector.addSelectionHandler(handler);

        add(selector);
    }

    private void setCurrentComboBox(Integer selected) {
        comboPressed = true;
        page = 1;
        rows = selected;
        fireEvent(changeEvent);
    }

    private void initStatus() {
        add(new FillToolItem());
        filter = new Status();
        XElement filterEl = filter.getElement();
        filterEl.getStyle().setWhiteSpace(WhiteSpace.NOWRAP);
        filter.getAppearance().getHtmlElement(filterEl).getStyle().setColor("#FF0000");
        add(filter);
    }

    /**
     * Установить статус фильтра
     *
     * @param text - текст статуса
     */
    public void setFilter(String text) {
        if (text != null) {
            filter.setToolTipConfig(new ToolTipConfig(text));

            int statusWidth = (int) (getOffsetWidth() * 0.9);
            int statusLen = statusWidth / CHAR_PIXELS;

            if (statusLen < text.length()) {
                if (statusLen <= 0)
                    statusLen = text.length() / 3;

                text = text.substring(0, statusLen) + "...";
            }

            filter.setText(text);
            filter.getElement().getStyle().setColor("#2071CF");
        } else {
            filter.setText("");
            filter.removeToolTip();
        }
    }

    /**
     * Очистить статус
     */
    public void clearFilter() {
        filter.setText("");
        filter.removeToolTip();
    }

    public int getPage() {
        if (comboPressed) {
            comboPressed = false;
            // return -1;
        }
        return page;
    }

    public int getRows() {
        if (buttonPressed) {
            buttonPressed = false;
            // return -1;
        }
        return rows;
    }

    public void setRows(int rowCount) {
        this.rowCount = rowCount;
    }

    // /** Инициализировать прослушиватели */
    private void initListeners() {
        ValueChangeHandler<Integer> numFieldHandler = new ValueChangeHandler<Integer>() {

            @Override
            public void onValueChange(ValueChangeEvent<Integer> e) {
                NumberField nf = (NumberField) e.getSource();
                int newValue = e.getValue();
                if (newValue <= pageCount && newValue >= 1) {
                    page = newValue;
                    fireEvent(changeEvent);
                } else {
                    nf.setValue(oldValue);
                }
            }

        };

        numField.addValueChangeHandler(numFieldHandler);
    }

    /**
     * Пресчитать параметры пагинатора
     */
    public void recalcParams() {
        if (rows != 0) {
            pageCount = rowCount / rows;
            if (pageCount < ((float) rowCount / (float) rows))
                pageCount++;

            // Если внешним фильтром поменяли кол-во записей,
            // и номер текущей страницы оказался больше чем номер последней,
            // устанавливаем последнюю страницу и перезагружаем грид.
            if (page > pageCount && pageCount > 0) {
                page = pageCount;
                changePage();
                return;
            } else if (pageCount == 0) {
                page = 1;
            }

            int fromRow = (((page - 1) * rows) + 1);
            int toRow = (page == pageCount) ? rowCount : page * rows;
            if (fromRow < 0) {
                fromRow = 0;
            }
            if (rowCount == 0) {
                numField.setEnabled(false);
                numLabel.setText("нет записей ");
            } else {
                numField.setEnabled(true);
                numLabel.setText(AppMessage.Util.MESSAGE.rowsFromTo(fromRow, toRow, rowCount));
            }

            setVisible(fastNext, true);
            setVisible(fastPrev, true);
            if (pageCount < PAGING_COUNT) {
                for (int i = 0; i < PAGING_COUNT; i++) {
                    setVisible(pageButtons.get(i), i < pageCount);
                }
                setVisible(fastNext, false);
            } else /* if (pageCount >= PAGING_COUNT) */ {
                for (int i = 0; i < PAGING_COUNT; i++) {
                    setVisible(pageButtons.get(i), true);
                }
                if (pageCount > PAGING_COUNT) {
                    setVisible(fastNext, true);
                }
            }

            if ((page - 1) < Math.round((float) PAGING_COUNT / 2) || pageCount < PAGING_COUNT) {
                currPagin = page - 1;
                setVisible(fastPrev, false);
            } else if ((pageCount - page) < Math.round((float) PAGING_COUNT / 2)) {
                currPagin = PAGING_COUNT - (pageCount - page) - 1;
                setVisible(fastNext, false);

            } else {
                currPagin = Math.round((float) PAGING_COUNT / 2) - 1;
            }

            for (int i = 0; i < PAGING_COUNT; i++) {
                pageButtons.get(i).setNumber(page + (i - currPagin));
            }

            for (int i = 0; i < PAGING_COUNT; i++) {
                if (currPagin == i) {
                    pageButtons.get(i).setValue(true);
                } else {
                    pageButtons.get(i).setValue(false);
                }
                this.doLayout();
            }

            next.setEnabled(page < pageCount);
            last.setEnabled(page < pageCount);
            prev.setEnabled(page > 1);
            first.setEnabled(page > 1);
            numField.disableEvents();
            numField.setValue(page);
            numField.enableEvents();
        }

        clearPagButtons();
        arrangePagButtons();
    }

    public void setConfig(PageConfig config, boolean setRowsOnly) {
        this.rowCount = config.getRows();
        if (!setRowsOnly) {
            this.page = config.getPage();
            setRowsPerPage(config.getRowsPerPage());
        }
        setButtonsVisible(true);
    }

    public void setRowsPerPage(int rows) {
        this.rows = rows;
        selector.setValue(this.rows);
    }

    @Override
    protected void addWidgetToMenu(Menu menu, Widget w) {
        if (w == selector) {
            final String dataKey = "intValue";
            MenuItem parent = new MenuItem("Строк на страницу");
            Menu m = new Menu();
            for (Integer i : selector.getStore().getAll()) {
                CheckMenuItem item = new CheckMenuItem(String.valueOf(i));
                item.setData(dataKey, i);
                item.setChecked(selector.getValue() == i, true);
                m.add(item);
            }

            m.addSelectionHandler(new SelectionHandler<Item>() {
                @Override
                public void onSelection(SelectionEvent<Item> event) {
                    Integer item = event.getSelectedItem().<Integer>getData(dataKey);
                    selector.setValue(item);
                    setCurrentComboBox(item);
                }
            });
            parent.setSubMenu(m);
            menu.add(parent);
        } else if (w == numLabel) {
            String text = numLabel.getText();
            if (text != null && !text.isEmpty()) {
                MenuItem item = new MenuItem(text);
                item.setEnabled(false);
                menu.add(item);
            }
        } else if (w == filter) {
            String text = filter.getText();
            if (text != null && !text.isEmpty()) {
                MenuItem item = new MenuItem(text);
                item.setEnabled(false);
                menu.add(item);
            }
        } else {
            super.addWidgetToMenu(menu, w);
        }
    }

    @Override
    public Locator getLocatorByElement(Element element) {
        Locator paginatorLocator = new Locator(LocatorParams.TYPE_PAGINATOR);
        Locator specificPart = getSpecificLocator(element);
        paginatorLocator.setPart(specificPart);
        return paginatorLocator;
    }

    private Locator getSpecificLocator(Element element) {
        Locator pageButtonLocator = findLocatorFromButtons(element);
        if (pageButtonLocator != null) {
            return pageButtonLocator;
        }
        if (numField.getElement().isOrHasChild(element)) {
            return new Locator(LocatorParams.TYPE_NUM_FIELD);
        } else if (first.getElement().isOrHasChild(element)) {
            return new Locator(LocatorParams.TYPE_FIRST_BUTTON);
        } else if (last.getElement().isOrHasChild(element)) {
            return new Locator(LocatorParams.TYPE_LAST_BUTTON);
        } else if (prev.getElement().isOrHasChild(element)) {
            return new Locator(LocatorParams.TYPE_PREV_BUTTON);
        } else if (next.getElement().isOrHasChild(element)) {
            return new Locator(LocatorParams.TYPE_NEXT_BUTTON);
        } else if (fastNext.getElement().isOrHasChild(element)) {
            return new Locator(LocatorParams.TYPE_FAST_NEXT_BUTTON);
        } else if (fastPrev.getElement().isOrHasChild(element)) {
            return new Locator(LocatorParams.TYPE_FAST_PREV_BUTTON);
        } else if (numLabel.getElement().isOrHasChild(element)) {
            return new Locator(LocatorParams.TYPE_NUM_STATUS);
        } else if (filter.getElement().isOrHasChild(element)) {
            return new Locator(LocatorParams.TYPE_FILTER_STATUS);
        }
        boolean isSelectorChild = selector.getElement().isOrHasChild(element);
        boolean isSelectorItem = false;
        if (!isSelectorChild) {
            isSelectorItem = isSelectorItem(element);
        }
        if (isSelectorChild || isSelectorItem) {
            return getSelectorLocator(element, isSelectorChild, isSelectorItem);
        }
        return null;
    }

    private Locator getSelectorLocator(Element element, boolean isSelectorChild,
                                       boolean isSelectorItem) {
        if (isSelectorChild) {
            if (selector.getCell().getInputElement(selector.getElement()).isOrHasChild(element)) {
                Locator selectorlocator = new Locator(LocatorParams.TYPE_SELECTOR);
                Locator inputPart = new Locator(LocatorParams.TYPE_INPUT);
                selectorlocator.setPart(inputPart);
                return selectorlocator;
            } else if (selector.getCell().getAppearance()
                    .triggerIsOrHasChild(selector.getElement(), element)) {
                Locator selectorlocator = new Locator(LocatorParams.TYPE_SELECTOR);
                Locator inputPart = new Locator(LocatorParams.TYPE_TRIGGER);
                selectorlocator.setPart(inputPart);
                return selectorlocator;
            }
        } else if (isSelectorItem) {
            Locator selectorlocator = new Locator(LocatorParams.TYPE_SELECTOR);
            for (Element item : selector.getListView().getElements()) {
                if (item.isOrHasChild(element)) {
                    Locator itemLocator = new Locator(LocatorParams.TYPE_ITEM);
                    int index = selector.getListView().indexOf(item);
                    String val = String.valueOf(selector.getStore().get(index));
                    itemLocator.setParameter(LocatorParams.PARAM_INDEX, String.valueOf(index));
                    itemLocator.setParameter(LocatorParams.PARAM_VALUE, val);
                    selectorlocator.setPart(itemLocator);
                    break;
                }
            }
            return selectorlocator;
        }
        return null;
    }

    protected boolean isSelectorItem(Element element) {
        for (Element item : selector.getListView().getElements()) {
            if (item.isOrHasChild(element)) {
                return true;
            }
        }
        return false;
    }

    private Locator findLocatorFromButtons(Element element) {
        for (PageButton button : pageButtons) {
            if (button.getElement().isOrHasChild(element)) {
                Locator pageButtonLocator = new Locator(LocatorParams.TYPE_PAGE_BUTTON);
                pageButtonLocator.setParameter(LocatorParams.PARAM_NUM,
                        String.valueOf(button.getNumber()));
                return pageButtonLocator;
            }
        }
        return null;
    }

    @Override
    public void fillLocatorDefaults(Locator locator, Element element) {
        // TODO Auto-generated method stub

    }

    @Override
    public Element getElementByLocator(Locator locator) {
        Locator specificPart = locator.getPart();
        if (specificPart == null) {
            return this.getElement();
        }
        Element pageButtonElement = findElementFromPageButtons(specificPart);
        if (pageButtonElement != null) {
            return pageButtonElement;
        }
        if (LocatorParams.TYPE_FIRST_BUTTON.equals(specificPart.getType())) {
            return getButtonElement(first);
        } else if (LocatorParams.TYPE_PREV_BUTTON.equals(specificPart.getType())) {
            return getButtonElement(prev);
        } else if (LocatorParams.TYPE_NEXT_BUTTON.equals(specificPart.getType())) {
            return getButtonElement(next);
        } else if (LocatorParams.TYPE_LAST_BUTTON.equals(specificPart.getType())) {
            return getButtonElement(last);
        } else if (LocatorParams.TYPE_FAST_NEXT_BUTTON.equals(specificPart.getType())) {
            return getButtonElement(fastNext);
        } else if (LocatorParams.TYPE_FAST_PREV_BUTTON.equals(specificPart.getType())) {
            return getButtonElement(fastPrev);
        } else if (LocatorParams.TYPE_SELECTOR.equals(specificPart.getType())) {
            return getSelectorElement(specificPart);
        } else if (LocatorParams.TYPE_FILTER_STATUS.equals(specificPart.getType())) {
            return filter.getElement();
        } else if (LocatorParams.TYPE_NUM_STATUS.equals(specificPart.getType())) {
            return numLabel.getElement();
        } else if (LocatorParams.TYPE_NUM_FIELD.equals(specificPart.getType())) {
            return numField.getCell().getInputElement(numField.getElement());
        }
        return null;
    }

    private Element findElementFromPageButtons(Locator specificPart) {
        for (PageButton pageButton : pageButtons) {
            int buttonNum = pageButton.getNumber();
            String numParam = specificPart.getParameter(LocatorParams.PARAM_NUM);
            if (!Util.isEmptyString(numParam)) {
                int locatorNum = Integer.valueOf(numParam);
                if (locatorNum == buttonNum) {
                    return getButtonElement(pageButton);
                }
            }
        }
        return null;
    }

    private Element getSelectorElement(Locator locator) {
        Locator cpecificPart = locator.getPart();
        if (cpecificPart == null) {
            return null;
        }
        if (LocatorParams.TYPE_INPUT.equals(cpecificPart.getType())) {
            return selector.getCell().getInputElement(selector.getElement());
        } else if (LocatorParams.TYPE_TRIGGER.equals(cpecificPart.getType())) {
            TriggerFieldAppearance appearance = selector.getCell().getAppearance();
            if (appearance instanceof TriggerFieldDefaultAppearance) {
                TriggerFieldDefaultAppearance defApp = (TriggerFieldDefaultAppearance) appearance;
                return selector.getElement().selectNode("." + defApp.getStyle().trigger());
            }
        } else if (LocatorParams.TYPE_ITEM.equals(cpecificPart.getType()) &&
                selector.isExpanded()) {
            String indexParam = cpecificPart.getParameter(LocatorParams.PARAM_INDEX);
            if (!Util.isEmptyString(indexParam)) {
                int index = Integer.valueOf(indexParam);
                return selector.getListView().getElement(index);
            }
            String valueParam = cpecificPart.getParameter(LocatorParams.PARAM_VALUE);
            if (!Util.isEmptyString(valueParam)) {
                Integer val = Integer.valueOf(valueParam);
                for (Integer item : selector.getStore().getAll()) {
                    if (item.equals(val)) {
                        int indexInView = selector.getStore().indexOf(item);
                        return selector.getListView().getElement(indexInView);
                    }
                }
            }
        }
        return null;
    }

    private Element getButtonElement(CellButtonBase<?> button) {
        return button == null ? null : button.getCell().getFocusElement(button.getElement());
    }

    static class LocatorParams {
        public static String TYPE_PAGINATOR = "Paginator";
        public static String TYPE_NUM_FIELD = "NumField";
        public static String TYPE_FIRST_BUTTON = "FirstButton";
        public static String TYPE_LAST_BUTTON = "LastButton";
        public static String TYPE_PREV_BUTTON = "PrevButton";
        public static String TYPE_NEXT_BUTTON = "NextButton";
        public static String TYPE_FAST_NEXT_BUTTON = "FastNextButton";
        public static String TYPE_FAST_PREV_BUTTON = "FastPrevButton";
        public static String TYPE_SELECTOR = "Selector";
        public static String TYPE_INPUT = "Input";
        public static String TYPE_TRIGGER = "Trigger";
        public static String TYPE_ITEM = "Item";
        public static String TYPE_PAGE_BUTTON = "PageButton";
        public static String TYPE_NUM_STATUS = "NumStatus";
        public static String TYPE_FILTER_STATUS = "FilterStatus";

        public static String PARAM_INDEX = "index";
        public static String PARAM_VALUE = "value";
        public static String PARAM_NUM = "num";
    }

    class PageButton extends ToggleButton {

        private int num;

        public PageButton(int num) {
            super();
            setText(String.valueOf(num));
            this.num = num;
        }

        public int getNumber() {
            return num;
        }

        public void setNumber(int num) {
            setText(String.valueOf(num));
            this.num = num;
        }
    }
}
