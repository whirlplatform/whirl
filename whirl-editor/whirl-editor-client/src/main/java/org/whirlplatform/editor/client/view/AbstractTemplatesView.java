package org.whirlplatform.editor.client.view;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.safecss.shared.SafeStylesUtils;
import com.mvp4g.client.view.ReverseViewInterface;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.dnd.core.client.DndDragStartEvent;
import com.sencha.gxt.dnd.core.client.GridDragSource;
import com.sencha.gxt.dnd.core.client.GridDropTarget;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.CancelEditEvent;
import com.sencha.gxt.widget.core.client.event.CancelEditEvent.CancelEditHandler;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent.CompleteEditHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.event.StartEditEvent;
import com.sencha.gxt.widget.core.client.event.StartEditEvent.StartEditHandler;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.CheckBoxSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.Grid.GridCell;
import com.sencha.gxt.widget.core.client.grid.editing.ClicksToEdit;
import com.sencha.gxt.widget.core.client.grid.editing.GridInlineEditing;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;
import org.whirlplatform.component.client.utils.InfoHelper;
import org.whirlplatform.editor.client.image.ComponentBundle;
import org.whirlplatform.editor.client.image.EditorBundle;
import org.whirlplatform.editor.client.presenter.AbstractTemplatesPresenter;
import org.whirlplatform.editor.shared.i18n.EditorMessage;
import org.whirlplatform.editor.shared.templates.BaseTemplate;
import org.whirlplatform.editor.shared.visitor.CloneVisitor;
import org.whirlplatform.meta.shared.editor.AbstractElement;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractTemplatesView extends ContentPanel
        implements AbstractTemplatesPresenter.IComponentTemplatesView, ReverseViewInterface<AbstractTemplatesPresenter> {

    private static final ComponentProperties properties = GWT.create(ComponentProperties.class);
    private AbstractTemplatesPresenter presenter;
    private VerticalLayoutContainer container;
    private Grid<BaseTemplate> grid;
    private ListStore<BaseTemplate> store;
    private GridInlineEditing<BaseTemplate> editing;
    private GridDragSource<BaseTemplate> dragSource;
    private GridDropTarget<BaseTemplate> dropTarget;
    private ToolBar toolBar;
    private TextButton delete;
    private TextButton rename;

    public AbstractTemplatesView() {
        super();
        initUI();
    }

    private void initUI() {
        setHeaderVisible(true);
        setBorders(false);
        container = new VerticalLayoutContainer();
        container.setBorders(false);
        initToolBar();
        container.add(toolBar);
        createGrid();
        container.add(getGrid(), new VerticalLayoutData(1, 1));
        add(container);
        initDragSource();
        initDropTarget();
    }

    protected abstract void initDropTarget();

    private void createGrid() {
        IdentityValueProvider<BaseTemplate> identity = new IdentityValueProvider<>();
        CheckBoxSelectionModel<BaseTemplate> selectionModel = new CheckBoxSelectionModel<>(identity);
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
        ColumnConfig<BaseTemplate, String> config = new ColumnConfig<>(properties.name());
        config.setColumnStyle(SafeStylesUtils.fromTrustedString("font-weight: bold; font-size: 12px; height: 20px;"));
        List<ColumnConfig<BaseTemplate, ?>> list = new ArrayList<>();
        list.add(selectionModel.getColumn());
        list.add(config);
        ColumnModel<BaseTemplate> model = new ColumnModel<>(list);
        store = new ListStore<>(properties.key());
        grid = new Grid<>(store, model);
        getGrid().setBorders(false);
        getGrid().setHideHeaders(true);
        getGrid().setSelectionModel(selectionModel);
        getGrid().getView().setAutoExpandColumn(config);
        getGrid().getView().setStripeRows(true);
        getGrid().getSelectionModel().addSelectionHandler(event -> {
            if (event.getSelectedItem() != null && event.getSelectedItem().isRemovable()) {
                delete.setEnabled(true);
                rename.setEnabled(true);
            } else {
                delete.setEnabled(false);
                rename.setEnabled(false);
            }
        });
        getGrid().getSelectionModel().addSelectionChangedHandler(event -> {
            if (event.getSelection().size() == 0) {
                delete.setEnabled(false);
                rename.setEnabled(false);
            }
        });
        initEditing(config);
    }

    private void initEditing(ColumnConfig<BaseTemplate, String> config) {
        editing = new GridInlineEditing<>(getGrid());
        editing.setClicksToEdit(ClicksToEdit.TWO);
        editing.addEditor(config, new TextField());
        editing.addCompleteEditHandler(new CompleteEditHandler<BaseTemplate>() {

            @Override
            public void onCompleteEdit(CompleteEditEvent<BaseTemplate> event) {
                int ind = event.getEditCell().getRow();
                BaseTemplate template = store.get(ind);
                AbstractElement copy = copy(template.getElement());
                store.commitChanges();
                store.remove(template);
                presenter.renameTemplate(new BaseTemplate(copy, true), template);
                dragSource.enable();
            }
        });

        editing.addStartEditHandler(new StartEditHandler<BaseTemplate>() {

            @Override
            public void onStartEdit(StartEditEvent<BaseTemplate> event) {
                dragSource.disable();
            }
        });

        editing.addCancelEditHandler(new CancelEditHandler<BaseTemplate>() {

            @Override
            public void onCancelEdit(CancelEditEvent<BaseTemplate> event) {
                dragSource.enable();
            }
        });
    }

    private void initDragSource() {
        dragSource = new GridDragSource<BaseTemplate>(getGrid()) {

            @Override
            protected void onDragStart(DndDragStartEvent event) {
                super.onDragStart(event);
                AbstractElement source = ((List<BaseTemplate>) event.getData()).get(0).getElement();
                AbstractElement data = copy(source);
                event.setData(data);
            }
        };
    }

    private void initToolBar() {
        toolBar = new ToolBar();
        delete = new TextButton();
        delete.setEnabled(false);
        delete.setIcon(EditorBundle.INSTANCE.cross());
        delete.setToolTip(EditorMessage.Util.MESSAGE.remove());
        delete.addSelectHandler(new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                BaseTemplate template = getGrid().getSelectionModel().getSelectedItem();

                ConfirmMessageBox messageBox = new ConfirmMessageBox(EditorMessage.Util.MESSAGE.templ_delete(),
                        EditorMessage.Util.MESSAGE.templ_delete_req() + " <b> " + template.getName() + " </b> ?");
                messageBox.getButton(PredefinedButton.YES).addSelectHandler(new SelectHandler() {

                    @Override
                    public void onSelect(SelectEvent event) {
                        presenter.deleteTemplate(template, true);
                    }
                });
                messageBox.getButton(PredefinedButton.NO).addSelectHandler(new SelectHandler() {

                    @Override
                    public void onSelect(SelectEvent event) {
                        InfoHelper.info("template-toolbar", EditorMessage.Util.MESSAGE.undo(),
                                EditorMessage.Util.MESSAGE.templ_delete_cancel());
                    }
                });
                messageBox.show();
            }
        });

        rename = new TextButton();
        rename.setEnabled(false);
        rename.setIcon(ComponentBundle.INSTANCE.rename());
        rename.setToolTip(EditorMessage.Util.MESSAGE.context_menu_rename());
        rename.addSelectHandler(new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                BaseTemplate currentEl = getGrid().getSelectionModel().getSelectedItem();
                int ind = store.indexOf(currentEl);
                editing.startEditing(new GridCell(ind, 1));
            }
        });
        toolBar.add(rename);
        toolBar.add(delete);

    }

    AbstractElement copy(AbstractElement element) {
        CloneVisitor<AbstractElement> cloner = new CloneVisitor<>(element, false, true);
        return cloner.copy();
    }

    @Override
    public AbstractTemplatesPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void setPresenter(AbstractTemplatesPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void addComponents(List<BaseTemplate> components) {
        store.clear();
        store.addAll(components);
    }

    public GridDropTarget<BaseTemplate> getDropTarget() {
        return dropTarget;
    }

    public void setDropTarget(GridDropTarget<BaseTemplate> dropTarget) {
        this.dropTarget = dropTarget;
    }

    public Grid<BaseTemplate> getGrid() {
        return grid;
    }

    interface ComponentProperties extends PropertyAccess<BaseTemplate> {

        @Path("element.id")
        ModelKeyProvider<BaseTemplate> key();

        @Path("element.name")
        ValueProvider<BaseTemplate, String> name();
    }

}
