package org.whirlplatform.component.client.grid;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.widget.core.client.button.ButtonGroup;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import com.sencha.gxt.widget.core.client.toolbar.SeparatorToolItem;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;
import java.util.Iterator;
import org.whirlplatform.component.client.ParameterHelper;
import org.whirlplatform.component.client.resource.ApplicationBundle;
import org.whirlplatform.component.client.selenium.Locator;
import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.i18n.AppMessage;

public class EditGridToolBar extends ToolBar {

    private final ClassMetadata metadata;

    private final boolean showData;
    private final boolean showSearch;
    private final boolean showExport;
    private final boolean showRefresh;

    private final boolean hideGroups;

    private final LoadConfigProvider configProvider;
    private final ParameterHelper paramHelper;

    private TextButton viewBtn;
    private TextButton insertBtn;
    private TextButton copyBtn;
    private TextButton updateBtn;
    private TextButton deleteBtn;

    private TextButton searchBtn;
    private TextButton sortBtn;
    private TextButton methodBtn;
    private TextButton refreshBtn;

    private TextButton expCsvBtn;
    private TextButton expExcelBtn;
    private TextButton impCsvBtn;
    private TextButton impExcelBtn;

    private Menu methodMenu;

    public EditGridToolBar(ClassMetadata metadata, boolean hideGroups, boolean showData,
                           boolean showSearch,
                           boolean showExport, boolean showMethod, boolean showRefresh,
                           LoadConfigProvider configProvider,
                           ParameterHelper paramHelper) {
        this(metadata, hideGroups, showData, showSearch, showExport, showMethod, showRefresh,
            configProvider,
            paramHelper, null);

    }

    public EditGridToolBar(ClassMetadata metadata, boolean hideGroups, boolean showData,
                           boolean showSearch,
                           boolean showExport, boolean showMethod, boolean showRefresh,
                           LoadConfigProvider configProvider,
                           ParameterHelper paramHelper, String baseComponentId) {
        super();
        this.metadata = metadata;
        this.configProvider = configProvider;
        this.hideGroups = hideGroups;
        this.showData = showData;
        this.showSearch = showSearch;
        this.showExport = showExport;
        this.showRefresh = showRefresh;
        this.paramHelper = paramHelper;
        init();
    }

    private void init() {
        FlexTable dataTable = new FlexTable();
        initData(dataTable);

        FlexTable searchTable = new FlexTable();
        initSearch(searchTable);
        initRefresh(searchTable);

        FlexTable exportTable = new FlexTable();
        initExport(exportTable);
    }

    private void initData(FlexTable dataTable) {
        if (showData && (metadata.isViewable() || metadata.isInsertable() || metadata.isUpdatable()
            || metadata.isDeletable())) {
            ButtonGroup dataGroup = null;
            // FlexTable dataTable = null;
            if (!hideGroups) {
                dataGroup = new ButtonGroup();
                // dataGroup.setHeadingText(Whirl.MESSAGE.changeData());
                dataTable = new FlexTable();
            }

            // view
            if (metadata.isViewable() && !metadata.isUpdatable()) {
                viewBtn = createButton(AppMessage.Util.MESSAGE.view(),
                    ApplicationBundle.INSTANCE.view());
                if (!hideGroups) {
                    viewBtn.setText(AppMessage.Util.MESSAGE.view());
                    addToGroup(dataTable, viewBtn);
                } else {
                    add(viewBtn);
                }
            }

            // insert
            if (metadata.isInsertable()) {
                insertBtn = createButton(AppMessage.Util.MESSAGE.insert(),
                    ApplicationBundle.INSTANCE.add());
                copyBtn = createButton(AppMessage.Util.MESSAGE.copy(),
                    ApplicationBundle.INSTANCE.copy());
                if (!hideGroups) {
                    insertBtn.setText(AppMessage.Util.MESSAGE.insert());
                    copyBtn.setText(AppMessage.Util.MESSAGE.copy());
                    addToGroup(dataTable, insertBtn);
                    addToGroup(dataTable, copyBtn);
                } else {
                    add(insertBtn);
                    add(copyBtn);
                }
            }

            // update
            if (metadata.isUpdatable()) {
                updateBtn = createButton(AppMessage.Util.MESSAGE.update(),
                    ApplicationBundle.INSTANCE.edit());
                if (!hideGroups) {
                    updateBtn.setText(AppMessage.Util.MESSAGE.update());
                    addToGroup(dataTable, updateBtn);
                } else {
                    add(updateBtn);
                }
            }

            // delete
            if (metadata.isDeletable()) {
                deleteBtn = createButton(AppMessage.Util.MESSAGE.delete(),
                    ApplicationBundle.INSTANCE.delete());
                if (!hideGroups) {
                    deleteBtn.setText(AppMessage.Util.MESSAGE.delete());
                    addToGroup(dataTable, deleteBtn);
                } else {
                    add(deleteBtn);
                }
            }

            if (hideGroups) {
                add(new SeparatorToolItem());
            } else {
                dataGroup.setWidget(dataTable);
                add(dataGroup);
            }
        }
    }

    private void initSearch(FlexTable groupTable) {
        if (showSearch) {
            // search
            searchBtn = createButton(AppMessage.Util.MESSAGE.filter(),
                ApplicationBundle.INSTANCE.search());

            // sort
            sortBtn =
                createButton(AppMessage.Util.MESSAGE.sort(), ApplicationBundle.INSTANCE.sort());

            // refresh
            // refreshBtn = createButton(AppMessage.Util.MESSAGE.refresh(),
            // ApplicationBundle.INSTANCE.refresh());

            if (!hideGroups) {
                searchBtn.setText(AppMessage.Util.MESSAGE.filter());
                sortBtn.setText(AppMessage.Util.MESSAGE.sort());
                // refreshBtn.setText(AppMessage.Util.MESSAGE.refresh());

                ButtonGroup group = new ButtonGroup();
                // FlexTable groupTable = new FlexTable();
                addToGroup(groupTable, searchBtn);
                addToGroup(groupTable, sortBtn);
                // addToGroup(groupTable, refreshBtn);
                group.setWidget(groupTable);

                if (methodBtn != null) {
                    methodBtn.setText(AppMessage.Util.MESSAGE.slaves());
                    addToGroup(groupTable, methodBtn);
                }

                add(group);
            } else {
                add(searchBtn);
                add(sortBtn);
                // add(refreshBtn);

                if (methodBtn != null) {
                    add(methodBtn);
                }
                add(new SeparatorToolItem());
            }
        }
    }

    private void initExport(FlexTable groupTable) {
        if (showExport) {
            GridExportImport exportImport = new GridExportImport(configProvider, paramHelper);

            expCsvBtn = exportImport.create(metadata, GridExportImport.ExpImpType.EXPORT_CSV,
                AppMessage.Util.MESSAGE.expimp_exportCSV(),
                false);
            expExcelBtn = exportImport.create(metadata, GridExportImport.ExpImpType.EXPORT_XLS,
                AppMessage.Util.MESSAGE.expimp_exportXLS(), false);
            impCsvBtn = exportImport.create(metadata, GridExportImport.ExpImpType.IMPORT_CSV,
                AppMessage.Util.MESSAGE.expimp_importCSV(),
                false);
            impExcelBtn = exportImport.create(metadata, GridExportImport.ExpImpType.IMPORT_XLS,
                AppMessage.Util.MESSAGE.expimp_importXLS(), false);

            if (!hideGroups) {
                ButtonGroup group = new ButtonGroup();

                addToGroup(groupTable, expCsvBtn);
                addToGroup(groupTable, expExcelBtn);
                if (metadata.isInsertable()) {
                    addToGroup(groupTable, impCsvBtn);
                    addToGroup(groupTable, impExcelBtn);
                }

                group.setWidget(groupTable);
                add(group);
            } else {
                add(expCsvBtn);
                add(expExcelBtn);
                add(new SeparatorToolItem());
            }
        }
    }

    private void initRefresh(FlexTable groupTable) {
        if (showRefresh) {
            // refresh
            refreshBtn = createButton(AppMessage.Util.MESSAGE.refresh(),
                ApplicationBundle.INSTANCE.refresh());
            if (!hideGroups) {
                refreshBtn.setText(AppMessage.Util.MESSAGE.refresh());
                addToGroup(groupTable, refreshBtn);
            } else {
                add(refreshBtn);
            }
        }
    }

    private TextButton createButton(String title, ImageResource icon) {
        TextButton button = new TextButton();
        button.setTitle(title);
        button.setIcon(icon);
        return button;
    }

    private void addToGroup(FlexTable table, Widget w) {
        if (!table.isCellPresent(0, 0) || table.getWidget(0, 0) == null) {
            table.setWidget(0, 0, w);
        } else if (!table.isCellPresent(1, 0) || table.getWidget(1, 0) == null) {
            table.setWidget(1, 0, w);
        } else if (!table.isCellPresent(0, 1) || table.getWidget(0, 1) == null) {
            table.setWidget(0, 1, w);
        } else if (!table.isCellPresent(1, 1) || table.getWidget(1, 1) == null) {
            table.setWidget(1, 1, w);
        }
    }

    public HandlerRegistration addViewHandler(SelectHandler handler) {
        if (viewBtn != null) {
            return viewBtn.addSelectHandler(handler);
        }
        return null;
    }

    public HandlerRegistration addInsertHandler(SelectHandler handler) {
        if (insertBtn != null) {
            return insertBtn.addSelectHandler(handler);
        }
        return null;
    }

    public HandlerRegistration addUpdateHandler(SelectHandler handler) {
        if (updateBtn != null) {
            return updateBtn.addSelectHandler(handler);
        }
        return null;
    }

    public HandlerRegistration addDeleteHandler(SelectHandler handler) {
        if (deleteBtn != null) {
            return deleteBtn.addSelectHandler(handler);
        }
        return null;
    }

    public HandlerRegistration addCopyHandler(SelectHandler handler) {
        if (copyBtn != null) {
            return copyBtn.addSelectHandler(handler);
        }
        return null;
    }

    public HandlerRegistration addSearchHandler(SelectHandler handler) {
        if (searchBtn != null) {
            return searchBtn.addSelectHandler(handler);
        }
        return null;
    }

    public HandlerRegistration addSortHandler(SelectHandler handler) {
        if (sortBtn != null) {
            return sortBtn.addSelectHandler(handler);
        }
        return null;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public HandlerRegistration addMethodHandler(SelectionHandler handler) {
        if (methodMenu != null) {
            Iterator<MenuItem> iter = (Iterator) methodMenu.iterator();
            while (iter.hasNext()) {
                MenuItem item = iter.next();
                item.addSelectionHandler(handler);
            }
        }
        return null;
    }

    public HandlerRegistration addRefreshHandler(SelectHandler handler) {
        if (refreshBtn != null) {
            return refreshBtn.addSelectHandler(handler);
        }
        return null;
    }

    public HandlerRegistration addExoprtImportHandler(SelectHandler handler) {
        if (expCsvBtn != null) {
            expCsvBtn.addSelectHandler(handler);
        }
        if (expExcelBtn != null) {
            expExcelBtn.addSelectHandler(handler);
        }
        if (impCsvBtn != null) {
            impCsvBtn.addSelectHandler(handler);
        }
        if (impExcelBtn != null) {
            impExcelBtn.addSelectHandler(handler);
        }
        return null;
    }

    public Element getElementByLocator(Locator locator) {
        Locator part = locator.getPart();
        if (part == null) {
            return getElement();
        }
        if (part.typeEquals(LocatorParams.TYPE_VIEW_BUTTON) && viewBtn != null) {
            return getTextButtonElement(viewBtn);
        } else if (part.typeEquals(LocatorParams.TYPE_ADD_BUTTON) && insertBtn != null) {
            return getTextButtonElement(insertBtn);
        } else if (part.typeEquals(LocatorParams.TYPE_COPY_BUTTON) && copyBtn != null) {
            return getTextButtonElement(copyBtn);
        } else if (part.typeEquals(LocatorParams.TYPE_EDIT_BUTTON) && updateBtn != null) {
            return getTextButtonElement(updateBtn);
        } else if (part.typeEquals(LocatorParams.TYPE_DELETE_BUTTON) && deleteBtn != null) {
            return getTextButtonElement(deleteBtn);
        } else if (part.typeEquals(LocatorParams.TYPE_SEARCH_BUTTON) && searchBtn != null) {
            return getTextButtonElement(searchBtn);
        } else if (part.typeEquals(LocatorParams.TYPE_SORT_BUTTON) && sortBtn != null) {
            return getTextButtonElement(sortBtn);
        } else if (part.typeEquals(LocatorParams.TYPE_REFRESH_BUTTON) && refreshBtn != null) {
            return getTextButtonElement(refreshBtn);
        }
        return null;
    }

    public Locator getLocatorByElement(Element element) {
        Locator result = null;
        if (this.getElement().isOrHasChild(element)) {
            result = new Locator(LocatorParams.TYPE_TOOL_BAR);
            Locator part = null;
            if (isButtonElement(viewBtn, element)) {
                part = new Locator(LocatorParams.TYPE_VIEW_BUTTON);
            } else if (isButtonElement(insertBtn, element)) {
                part = new Locator(LocatorParams.TYPE_ADD_BUTTON);
            } else if (isButtonElement(copyBtn, element)) {
                part = new Locator(LocatorParams.TYPE_COPY_BUTTON);
            } else if (isButtonElement(updateBtn, element)) {
                part = new Locator(LocatorParams.TYPE_EDIT_BUTTON);
            } else if (isButtonElement(deleteBtn, element)) {
                part = new Locator(LocatorParams.TYPE_DELETE_BUTTON);
            } else if (isButtonElement(searchBtn, element)) {
                part = new Locator(LocatorParams.TYPE_SEARCH_BUTTON);
            } else if (isButtonElement(sortBtn, element)) {
                part = new Locator(LocatorParams.TYPE_SORT_BUTTON);
            } else if (isButtonElement(refreshBtn, element)) {
                part = new Locator(LocatorParams.TYPE_REFRESH_BUTTON);
            }
            result.setPart(part);
        }
        return result;
    }

    private boolean isButtonElement(TextButton button, Element element) {
        return (button != null && button.getElement().isOrHasChild(element));
    }

    private XElement getTextButtonElement(final TextButton button) {
        return (button != null) ? button.getCell().getFocusElement(button.getElement()) : null;
    }

    /*
     * Selenium
     */
    static class LocatorParams {
        public static String TYPE_TOOL_BAR = "ToolBar";

        public static String TYPE_VIEW_BUTTON = "ViewButton";
        public static String TYPE_ADD_BUTTON = "AddButton";
        public static String TYPE_COPY_BUTTON = "CopyButton";
        public static String TYPE_EDIT_BUTTON = "EditButton";
        public static String TYPE_DELETE_BUTTON = "DeleteButton";
        public static String TYPE_SEARCH_BUTTON = "SearchButton";
        public static String TYPE_SORT_BUTTON = "SortButton";
        public static String TYPE_REFRESH_BUTTON = "RefreshButton";
    }

    public class ExpImpButton extends TextButton {

        private GridExportImport.ExpImpType expImpType;

        public GridExportImport.ExpImpType getExpImpType() {
            return expImpType;
        }

        public void setExpImpType(GridExportImport.ExpImpType expImpType) {
            this.expImpType = expImpType;
        }

    }
}
