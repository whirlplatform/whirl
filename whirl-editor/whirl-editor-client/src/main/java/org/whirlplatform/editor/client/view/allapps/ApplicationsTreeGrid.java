package org.whirlplatform.editor.client.view.allapps;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.IconProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.treegrid.TreeGrid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.whirlplatform.editor.client.image.EditorBundle;
import org.whirlplatform.editor.shared.i18n.EditorMessage;
import org.whirlplatform.meta.shared.ApplicationStoreData;
import org.whirlplatform.meta.shared.Version;

/**
 *
 */
public class ApplicationsTreeGrid extends TreeGrid<ApplicationStoreData> {
    /**
     * The treegrid dummy folders constants
     */
    private static final String ROOT_PREFIX = "@/";
    private static final String TAG_PREFIX = "@/tag-";
    private static final String TAG_NAME = "tag";
    private static final String BRANCH_PREFIX = "@/branch-";
    private static final String BRANCH_NAME = "branch";
    private static final String DATE_FORMAT = "dd.MM.yyyy HH:mm:ss";
    // Tree grid columns titles
    private static final String NAME_CAPTION = EditorMessage.Util.MESSAGE.code();
    private static final String TITLE_CAPTION = EditorMessage.Util.MESSAGE.title();
    private static final String MODIFIED_CAPTION = EditorMessage.Util.MESSAGE.modified();
    // Tree grid columns widths
    private static final int NAME_COLUMN_WIDTH = 140;
    private static final int TITLE_COLUMN_WIDTH = 250;
    private static final int REVISION_COLUMN_WIDTH = 75;
    private static final int DATE_COLUMN_WIDTH = 125;
    private static final int ADDRESS_COLUMN_WIDTH = 400;
    private static final int MODIFIED_COLUMN_WIDTH = 125;
    private static ColumnConfig<ApplicationStoreData, String> treeColumn =
            new ColumnConfig<>(createNameValueProvider(),
                    NAME_COLUMN_WIDTH, NAME_CAPTION);
    private final Map<String, Set<ApplicationStoreData>> appsMap;

    public ApplicationsTreeGrid() {
        super(createTreeStore(), createColumnModel(), treeColumn);
        setIconProvider(createIconProvider());
        getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        appsMap = new HashMap<>();
        treeColumn.setCell(new AbstractCell<String>() {
            @Override
            public void render(com.google.gwt.cell.client.Cell.Context context, String value,
                               SafeHtmlBuilder sb) {
                ApplicationStoreData data =
                        getTreeStore().findModelWithKey(context.getKey().toString());
                if ((data instanceof DummyRootFolder) || (data instanceof ApplicationStoreData)) {
                    sb.appendHtmlConstant("<b>").appendEscaped(value).appendHtmlConstant("</b>");
                } else {
                    sb.appendEscaped(value);
                }
            }
        });
        setAutoLoad(true);
    }

    /**
     * Рассчитывает ширину формы
     */
    public static int estimatedWidth() {
        return MODIFIED_COLUMN_WIDTH + NAME_COLUMN_WIDTH + TITLE_COLUMN_WIDTH
                + REVISION_COLUMN_WIDTH + DATE_COLUMN_WIDTH + ADDRESS_COLUMN_WIDTH;
    }

    /*
     * Methods for the tree grid construction
     */
    private static TreeStore<ApplicationStoreData> createTreeStore() {
        TreeStore<ApplicationStoreData> result =
                new TreeStore<>(new ModelKeyProvider<ApplicationStoreData>() {
                    @Override
                    public String getKey(ApplicationStoreData item) {
                        return (item == null) ? null : item.getId();
                    }
                });
        result.addSortInfo(
                new StoreSortInfo<ApplicationStoreData>(new Comparator<ApplicationStoreData>() {
                    @Override
                    public int compare(ApplicationStoreData o1, ApplicationStoreData o2) {
                        int compareResult = (o1.getCode() != null)
                            ? o1.getCode().compareToIgnoreCase(o2.getCode()) : -1;
                        if (compareResult == 0) {
                            if (o1.getVersion() != null && o2.getVersion() != null) {
                                compareResult = Version.compare(o1.getVersion(), o2.getVersion());
                            }
                        }
                        return compareResult;
                    }
                }, SortDir.ASC));
        return result;
    }

    private static ColumnModel<ApplicationStoreData> createColumnModel() {
        ColumnConfig<ApplicationStoreData, String> modifiedColumn =
                new ColumnConfig<>(createModifiedValueProvider(),
                        MODIFIED_COLUMN_WIDTH, MODIFIED_CAPTION);
        ColumnConfig<ApplicationStoreData, String> titleColumn =
                new ColumnConfig<>(createTitleValueProvider(),
                        TITLE_COLUMN_WIDTH, TITLE_CAPTION);

        List<ColumnConfig<ApplicationStoreData, ?>> columnsList = new ArrayList<>();
        columnsList.add(treeColumn);
        columnsList.add(modifiedColumn);
        columnsList.add(titleColumn);
        return new ColumnModel<ApplicationStoreData>(columnsList);
    }

    private static ReadOnlyValueProvider createModifiedValueProvider() {
        return new ReadOnlyValueProvider("modified") {
            @Override
            public String getValue(ApplicationStoreData object) {
                if (object == null || object.getModified() == 0) {
                    return "";
                }
                return DateTimeFormat.getFormat(DATE_FORMAT).format(new Date(object.getModified()));
            }
        };
    }

    private static ReadOnlyValueProvider createNameValueProvider() {
        return new ReadOnlyValueProvider("name") {
            @Override
            public String getValue(ApplicationStoreData object) {
                return (object == null) ? "" : object.getName();
            }
        };
    }

    private static ReadOnlyValueProvider createTitleValueProvider() {
        return new ReadOnlyValueProvider("title") {
            @Override
            public String getValue(ApplicationStoreData object) {
                if (object == null) {
                    return "";
                } else if (object instanceof DummyRootFolder) {
                    return ((DummyRootFolder) object).getTitle();
                }
                return "";
            }
        };
    }

    /**
     * Get selected application store data
     *
     * @return selected item or null if it is not a ApplicationStoreData object dummy folder
     */
    public ApplicationStoreData getSelectedLeaf() {
        ApplicationStoreData selected = getSelectionModel().getSelectedItem();
        if (selected == null) {
            return null;
        }
        if (treeStore.hasChildren(selected) || !(selected instanceof ApplicationStoreData)) {
            return null;
        }
        return selected;
    }

    /**
     * Возвращает выбранную dummy папку
     */
    public ApplicationStoreData getSelectedFolder() {
        ApplicationStoreData selected = getSelectionModel().getSelectedItem();
        if (selected == null || !treeStore.hasChildren(selected)) {
            return null;
        }
        return selected;
    }

    public void expandSelectedFolder() {
        ApplicationStoreData selected = getSelectedFolder();
        if (selected != null && treeStore.hasChildren(selected)) {
            setExpanded(selected, true, true);
        }
    }

    public void collapseSelectedFolder() {
        ApplicationStoreData selected = getSelectedFolder();
        if (selected != null && treeStore.hasChildren(selected)) {
            setExpanded(selected, false, true);
        }
    }

    /**
     * Clears the tree grid content
     */
    public void clear() {
        treeStore.clear();
        appsMap.clear();
    }

    /**
     * Initialises the tree grid content
     *
     * @param data - the collection of ApplicationStoreData items
     */
    public void loadData(Collection<ApplicationStoreData> data) {
        clear();
        for (ApplicationStoreData d : data) {
            DummyRootFolder rootFolder = new DummyRootFolder(d.getCode(), d.getCode(), d.getName());
            ApplicationStoreData root = treeStore.findModelWithKey(rootFolder.getId());
            if (root == null) {
                root = rootFolder;
                root.setCode(d.getCode());
                root.setVersion(d.getVersion());
                treeStore.add(rootFolder);
                appsMap.put(d.getCode(), new HashSet<ApplicationStoreData>());
            }
            ApplicationStoreData rootSubFolder;
            if (d.getVersion() == null) {
                rootSubFolder = root;
            } else {
                if (!d.getVersion().isBranch()) {
                    DummyTagFolder tagFolder = new DummyTagFolder(d.getCode());
                    rootSubFolder = treeStore.findModelWithKey(tagFolder.getId());
                    if (rootSubFolder == null) {
                        rootSubFolder = tagFolder;
                        rootSubFolder.setCode(d.getCode());
                        rootSubFolder.setVersion(d.getVersion());
                        treeStore.add(root, rootSubFolder);
                    }
                } else {
                    DummyBranchFolder branchFolder = new DummyBranchFolder(d.getCode());
                    rootSubFolder = treeStore.findModelWithKey(branchFolder.getId());
                    if (rootSubFolder == null) {
                        rootSubFolder = branchFolder;
                        rootSubFolder.setCode(d.getCode());
                        rootSubFolder.setVersion(d.getVersion());
                        treeStore.add(root, rootSubFolder);
                    }
                }
                if (d instanceof ApplicationStoreData) {
                    appsMap.get(d.getCode()).add(d);
                }
            }
            d.setName(d.getVersion() == null ? d.getCode() : d.getVersion().toString());
            treeStore.add(rootSubFolder, d);
        }
    }

    /**
     * Determines if the application exists
     *
     * @param code    - application code
     * @param version - application version
     * @return true if the application exists
     */
    public boolean exists(final String code, final Version version) {
        if (appsMap.containsKey(code)) {
            for (ApplicationStoreData data : appsMap.get(code)) {
                if (version.equals(data.getVersion())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Collects ApplicationStoreData for all stored applications
     *
     * @return The collection of the ApplicationStoreData objects
     */
    public Collection<ApplicationStoreData> getAllApplicatonsData() {
        List<ApplicationStoreData> result = new ArrayList<>();
        for (Set<ApplicationStoreData> set : appsMap.values()) {
            for (ApplicationStoreData item : set) {
                result.add(item);
            }
        }
        return Collections.unmodifiableCollection(result);
    }

    /**
     * Collects ApplicationStoreData for the applications containing given code
     *
     * @param code
     * @return The collection of the ApplicationStoreData objects
     */
    public Collection<ApplicationStoreData> getAppicationData(final String code) {
        List<ApplicationStoreData> result = new ArrayList<>();
        if (appsMap.containsKey(code)) {
            for (ApplicationStoreData item : appsMap.get(code)) {
                result.add(item);
            }
        }
        return Collections.unmodifiableCollection(result);
    }

    private IconProvider<ApplicationStoreData> createIconProvider() {
        return new IconProvider<ApplicationStoreData>() {
            @Override
            public ImageResource getIcon(ApplicationStoreData model) {
                if (model instanceof DummyRootFolder) {
                    return EditorBundle.INSTANCE.applications_stack();
                } else if (model instanceof DummyBranchFolder) {
                    return null;
                } else if (model instanceof DummyTagFolder) {
                    return null;
                }
                return EditorBundle.INSTANCE.application();
            }
        };
    }

    /**
     * Wraps ValueProvider and implements common methods
     */
    public abstract static class ReadOnlyValueProvider
            implements ValueProvider<ApplicationStoreData, String> {
        private final String path;

        ReadOnlyValueProvider(final String path) {
            this.path = path;
        }

        @Override
        public void setValue(ApplicationStoreData object, String value) {
        }

        @Override
        public String getPath() {
            return path;
        }
    }

    /**
     * The basic dummy folder
     */
    @SuppressWarnings("serial")
    public abstract class DummyFolder extends ApplicationStoreData {

        public DummyFolder() {
            super();
        }

        public DummyFolder(final String id, final String name) {
            super();
            StringBuilder sb = new StringBuilder(prefix());
            sb.append(id);
            super.setId(sb.toString());
            super.setName(name);
        }

        @Override
        public void setId(final String id) {
            super.setId(prefix() + id);
        }

        protected abstract String prefix();
    }

    /**
     * The root dummy folder
     */
    @SuppressWarnings("serial")
    public class DummyRootFolder extends DummyFolder {
        private String title;

        public DummyRootFolder() {
        }

        public DummyRootFolder(final String id, final String name, final String title) {
            super(id, name);
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        @Override
        protected String prefix() {
            return ROOT_PREFIX;
        }
    }

    /**
     * The branch dummy sub folder
     */
    @SuppressWarnings("serial")
    public class DummyBranchFolder extends DummyFolder {
        public DummyBranchFolder(final String id) {
            super(id, BRANCH_NAME);
        }

        @Override
        protected String prefix() {
            return BRANCH_PREFIX;
        }
    }

    /**
     * The tag (version) dummy sub folder
     */
    @SuppressWarnings("serial")
    public class DummyTagFolder extends DummyFolder {

        public DummyTagFolder(final String id) {
            super(id, TAG_NAME);
        }

        @Override
        protected String prefix() {
            return TAG_PREFIX;
        }
    }
}
