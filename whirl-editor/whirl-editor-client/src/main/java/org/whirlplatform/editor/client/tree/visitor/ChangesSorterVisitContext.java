package org.whirlplatform.editor.client.tree.visitor;

import com.sencha.gxt.data.shared.TreeStore;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.whirlplatform.editor.client.tree.dummy.DummyAppGroups;
import org.whirlplatform.editor.client.tree.dummy.DummyAppLocales;
import org.whirlplatform.meta.shared.editor.AbstractElement;
import org.whirlplatform.meta.shared.editor.ApplicationElement;
import org.whirlplatform.meta.shared.editor.CellElement;
import org.whirlplatform.meta.shared.editor.ColumnElement;
import org.whirlplatform.meta.shared.editor.ElementVisitor.VisitContext;
import org.whirlplatform.meta.shared.editor.EventElement;
import org.whirlplatform.meta.shared.editor.FormElement;
import org.whirlplatform.meta.shared.editor.RequestElement;
import org.whirlplatform.meta.shared.editor.RightCollectionElement;
import org.whirlplatform.meta.shared.editor.RowElement;
import org.whirlplatform.meta.shared.editor.db.AbstractTableElement;
import org.whirlplatform.meta.shared.editor.db.PlainTableElement;
import org.whirlplatform.meta.shared.editor.db.TableColumnElement;

/**
 *
 */
public class ChangesSorterVisitContext implements VisitContext {
    private final Map<String, String> parents;
    private TreeStore<AbstractElement> treeStore;
    private ApplicationElement root;
    private String dummyLocalesId;
    private String dummyGroupsId;
    // visit result
    private String changeOwnerId;

    public ChangesSorterVisitContext() {
        parents = new HashMap<>();
    }

    public ChangesSorterVisitContext(final TreeStore<AbstractElement> treeStore) {
        this();
        this.treeStore = treeStore;
    }

    public void init(final TreeStore<AbstractElement> treeStore) {
        setTreeStore(treeStore);
        init();
    }

    public void init() {
        root = (treeStore != null) ? (ApplicationElement) treeStore.getRootItems().get(0) : null;
        if (root != null) {
            parents.clear();
            this.<EventElement>collectRightCollections(root.getAllEventRights());
            this.<AbstractTableElement>collectRightCollections(root.getAllTableRights());
            this.<AbstractTableElement>collectRightCollections(root.getAllTableColumnRights());
            for (AbstractElement element : treeStore.getAll()) {
                if (element instanceof PlainTableElement) {
                    PlainTableElement table = (PlainTableElement) element;
                    collectPlainTableViews(table);
                    collectPlainTableColumns(table);
                } else if (element instanceof FormElement) {
                    FormElement form = (FormElement) element;
                    collectFormCells(form);
                    collectFormColumns(form);
                    collectFormRequests(form);
                    collectFormRows(form);
                } else if (element instanceof DummyAppGroups) {
                    dummyGroupsId = element.getId();
                } else if (element instanceof DummyAppLocales) {
                    dummyLocalesId = element.getId();
                }
            }
        }
    }

    public void setTreeStore(TreeStore<AbstractElement> treeStore) {
        this.treeStore = treeStore;
    }

    @SuppressWarnings("unchecked")
    private <T extends AbstractElement> void collectRightCollections(
            Collection<RightCollectionElement> source) {
        for (RightCollectionElement rightsCollection : source) {
            T parent = (T) rightsCollection.getElement();
            this.collectElementIdToMap(parents, parent, rightsCollection);
        }
    }

    private void collectPlainTableViews(final PlainTableElement table) {
        this.collectElementIdToMap(parents, table, table.getView());
    }

    private void collectPlainTableColumns(final PlainTableElement table) {
        for (TableColumnElement tableColumn : table.getColumns()) {
            this.collectElementIdToMap(parents, table, tableColumn);
        }
    }

    private void collectFormCells(final FormElement form) {
        for (CellElement cell : form.getCells().values()) {
            this.collectElementIdToMap(parents, form, cell);
        }
    }

    private void collectFormColumns(final FormElement form) {
        for (ColumnElement column : form.getColumnsWidth()) {
            this.collectElementIdToMap(parents, form, column);
        }
    }

    private void collectFormRequests(final FormElement form) {
        for (RequestElement request : form.getRowRequests()) {
            this.collectElementIdToMap(parents, form, request);
        }
    }

    private void collectFormRows(final FormElement form) {
        for (RowElement row : form.getRowsHeight()) {
            this.collectElementIdToMap(parents, form, row);
        }
    }

    private <T extends AbstractElement, E extends AbstractElement> void collectElementIdToMap(
            Map<String, String> map,
            T parent, E element) {
        if (element != null) {
            if (map.containsKey(element.getId())) {
                duplicatedIdWarning(element);
            } else {
                map.put(element.getId(), parent.getId());
            }
        }
    }

    private void duplicatedIdWarning(final AbstractElement element) {
        StringBuilder sb = new StringBuilder("*** Warning! *** Duplicated ID of ");
        sb.append(element.getClass().getSimpleName());
        System.out.println(sb.toString());
    }

    //    private void parentWarning(final AbstractElement element) {
    //        StringBuilder sb = new StringBuilder("*** Warning! *** Parent was not located for ");
    //        sb.append(element.getClass().getSimpleName());
    //        System.out.println(sb.toString());
    //    }

    public <E extends AbstractElement> String getParentId(final E element) {
        final String id = element.getId();
        if (!parents.containsKey(id)) {
        //parentWarning(element);
            return null;
        }
        return parents.get(id);
    }

    public String getRootId() {
        return (root != null) ? root.getId() : null;
    }

    public String getDummyLocalesId() {
        return dummyLocalesId;
    }

    public String getDummyGroupsId() {
        return dummyGroupsId;
    }

    public String getChangeOwnerId() {
        return changeOwnerId;
    }

    public void setChangeOwnerId(final String id) {
        this.changeOwnerId = id;
    }
}