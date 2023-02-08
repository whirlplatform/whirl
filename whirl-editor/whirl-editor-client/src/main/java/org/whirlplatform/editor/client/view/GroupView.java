package org.whirlplatform.editor.client.view;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
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
import org.whirlplatform.editor.client.meta.NewGroupElement;
import org.whirlplatform.editor.client.presenter.GroupPresenter;
import org.whirlplatform.editor.client.presenter.GroupPresenter.IGroupView;
import org.whirlplatform.editor.shared.i18n.EditorMessage;
import org.whirlplatform.meta.shared.editor.AbstractElement;
import org.whirlplatform.meta.shared.editor.GroupElement;

public class GroupView extends ContentPanel implements IGroupView {

    private static final GroupProperties properties = GWT
        .create(GroupProperties.class);
    private GroupPresenter presenter;
    private Grid<GroupElement> grid;
    private ListStore<GroupElement> store;
    private GridInlineEditing<GroupElement> editing;
    private VerticalLayoutContainer container;

    public GroupView() {
        super();
        initUI();
    }

    private void initUI() {
        setHeaderVisible(true);
        setHeading(EditorMessage.Util.MESSAGE.editing_group());
        container = new VerticalLayoutContainer();
        createGrid();
        setWidget(container);
    }

    private void createGrid() {
        ColumnConfig<GroupElement, String> nameColumn = new ColumnConfig<GroupElement, String>(
            properties.name(), 400, EditorMessage.Util.MESSAGE.group_name());
        ColumnConfig<GroupElement, String> groupColumn = new ColumnConfig<GroupElement, String>(
            properties.groupName(), 200,
            EditorMessage.Util.MESSAGE.group_group());

        List<ColumnConfig<GroupElement, ?>> columns =
            new ArrayList<ColumnConfig<GroupElement, ?>>();
        columns.add(nameColumn);
        columns.add(groupColumn);

        ColumnModel<GroupElement> cm = new ColumnModel<GroupElement>(columns);
        store = new ListStore<GroupElement>(properties.key());

        grid = new Grid<GroupElement>(store, cm);
        grid.setWidth(300);
        grid.setHeight(300);
        grid.setBorders(false);
        grid.getView().setStripeRows(true);
        grid.getView().setColumnLines(true);
        grid.getView().setShowDirtyCells(false);
        grid.getSelectionModel().setSelectionMode(SelectionMode.MULTI);

        editing = new GridInlineEditing<GroupElement>(grid);
        editing.setClicksToEdit(ClicksToEdit.TWO);

        TextField nameField = new TextField();
        nameField.setAllowBlank(false);

        TextField groupField = new TextField();
        groupField.setAllowBlank(false);

        editing.addEditor(nameColumn, nameField);
        editing.addEditor(groupColumn, groupField);
        editing.addCancelEditHandler(new CancelEditHandler<GroupElement>() {

            @Override
            public void onCancelEdit(CancelEditEvent<GroupElement> event) {
                store.rejectChanges();
            }
        });
        editing.addCompleteEditHandler(new CompleteEditHandler<GroupElement>() {

            @Override
            public void onCompleteEdit(CompleteEditEvent<GroupElement> event) {
                store.commitChanges();
            }
        });

        TextButton add = new TextButton(EditorMessage.Util.MESSAGE.group_add());
        add.addSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                presenter.getEventBus().addElementCallback(null,
                    new NewGroupElement(),
                    new Callback<AbstractElement, Throwable>() {

                        @Override
                        public void onSuccess(AbstractElement result) {
                            store.add(0, (GroupElement) result);
                            editing.startEditing(new GridCell(0, 0));
                        }

                        @Override
                        public void onFailure(Throwable reason) {
                        }
                    });
            }

        });
        TextButton remove = new TextButton(
            EditorMessage.Util.MESSAGE.group_remove());
        remove.addSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                List<GroupElement> selectedElements = grid.getSelectionModel().getSelectedItems();
                if (selectedElements != null && !selectedElements.isEmpty()) {
                    for (GroupElement element : selectedElements) {
                        store.remove(element);
                    }
                    store.commitChanges();
                }
            }

        });
        ToolBar toolBar = new ToolBar();
        toolBar.add(add);
        toolBar.add(remove);
        container.add(toolBar, new VerticalLayoutData(1, -1));
        container.add(grid, new VerticalLayoutData(1, 1));
    }

    @Override
    public GroupPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void setPresenter(GroupPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void addGroup(GroupElement group) {
        store.add(group);
    }

    @Override
    public Collection<GroupElement> getGroups() {
        store.commitChanges();
        return store.getAll();
    }

    @Override
    public void clearUI() {
        store.clear();
    }

    interface GroupProperties extends PropertyAccess<GroupElement> {
        @Path("id")
        ModelKeyProvider<GroupElement> key();

        ValueProvider<GroupElement, String> name();

        ValueProvider<GroupElement, String> groupName();
    }

}
