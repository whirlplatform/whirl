package org.whirlplatform.editor.client.view;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.mvp4g.client.view.ReverseViewInterface;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.data.shared.StringLabelProvider;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.Resizable;
import com.sencha.gxt.widget.core.client.Resizable.Dir;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.event.ResizeEndEvent.ResizeEndHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import com.sencha.gxt.widget.core.client.toolbar.SeparatorToolItem;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;
import java.util.Arrays;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.form.FormBuilder;
import org.whirlplatform.editor.client.component.ColorMenu;
import org.whirlplatform.editor.client.image.EditorBundle;
import org.whirlplatform.editor.client.presenter.DesignPresenter;
import org.whirlplatform.editor.client.presenter.DesignPresenter.IDesignView;
import org.whirlplatform.editor.shared.i18n.EditorMessage;

public class DesignView extends FlowLayoutContainer implements IDesignView,
        ReverseViewInterface<DesignPresenter> {

    private DesignPresenter presenter;
    private SimpleContainer container;
    private Resizable resizable;
    private ToolBar toolBar;
    private FlowLayoutContainer outer;
    private FlowLayoutContainer inner;

    private TextButton delete;
    private TextButton addRow;
    private TextButton addColumn;
    private TextButton deleteRow;
    private TextButton deleteColumn;
    private TextButton union;
    private TextButton divide;
    private TextButton borderTop;
    private TextButton borderRight;
    private TextButton borderBottom;
    private TextButton borderLeft;
    private SimpleComboBox<Integer> borderWidth;
    private TextButton borderColor;
    private ColorMenu borderColorMenu;
    private TextButton backgroundColor;
    private ColorMenu backgroundColorMenu;
    private TextButton clearBackgroundColor;
    private SeparatorToolItem separator;
    private SeparatorToolItem separator2;
    private TextButton showHidden;

    private int containerWidth;
    private int containerHeight;
    private double percentWidth;
    private double percentHeight;

    public DesignView() {
        super();
        initUI();
    }

    @Override
    public DesignPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void setPresenter(DesignPresenter presenter) {
        this.presenter = presenter;
    }

    private void initUI() {
        setBorders(true);
        getElement().getStyle().setBackgroundColor("#FFFFFF");

        initToolbar();

        outer = new FlowLayoutContainer();
        outer.setScrollMode(ScrollMode.ALWAYS);

        inner = new FlowLayoutContainer();

        container = new SimpleContainer();
        container.setBorders(true);
        container.setLayoutData(new MarginData(8));
        setAllowTextSelection(false);
        inner.add(container);
        outer.add(inner);
        add(outer);

        resizable = new Resizable(container, Dir.E, Dir.SE, Dir.S);

        resizable.setMinHeight(3);
        resizable.setMinWidth(3);
        resizable.setMaxHeight(5000);
        resizable.setMaxWidth(25000);
    }

    private void initToolbar() {
        toolBar = new ToolBar();
        toolBar.setHeight(26);
        delete = new TextButton();
        delete.setTitle(EditorMessage.Util.MESSAGE.design_remove());
        delete.setIcon(EditorBundle.INSTANCE.cross());
        delete.addSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                presenter.onDeleteSelectedComponent();
            }
        });
        addRow = new TextButton();
        addRow.setTitle(EditorMessage.Util.MESSAGE.design_row_add());
        addRow.setIcon(EditorBundle.INSTANCE.insertRow());
        addRow.addSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                presenter.onAddRow();
            }
        });
        addColumn = new TextButton();
        addColumn.setTitle(EditorMessage.Util.MESSAGE.design_column_add());
        addColumn.setIcon(EditorBundle.INSTANCE.insertColumn());
        addColumn.addSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                presenter.onAddColumn();
            }
        });
        deleteRow = new TextButton();
        deleteRow.setTitle(EditorMessage.Util.MESSAGE.design_row_remove());
        deleteRow.setIcon(EditorBundle.INSTANCE.deleteRow());
        deleteRow.addSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                presenter.onDeleteRow();
            }
        });
        deleteColumn = new TextButton();
        deleteColumn
                .setTitle(EditorMessage.Util.MESSAGE.design_column_remove());
        deleteColumn.setIcon(EditorBundle.INSTANCE.deleteColumn());
        deleteColumn.addSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                presenter.onDeleteColumn();
            }
        });
        union = new TextButton();
        union.setTitle(EditorMessage.Util.MESSAGE.design_union());
        union.setIcon(EditorBundle.INSTANCE.union());
        union.addSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                presenter.onUnion();
            }
        });
        divide = new TextButton();
        divide.setTitle(EditorMessage.Util.MESSAGE.design_split());
        divide.setIcon(EditorBundle.INSTANCE.split());
        divide.addSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                presenter.onDivide();
            }
        });
        borderTop = new TextButton();
        borderTop.setTitle(EditorMessage.Util.MESSAGE.design_border_top());
        borderTop.setIcon(EditorBundle.INSTANCE.borderTop());
        borderTop.addSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                presenter.onAddBorderTop(borderWidth.getValue(),
                        borderColorMenu.getColor());
            }
        });
        borderRight = new TextButton();
        borderRight.setTitle(EditorMessage.Util.MESSAGE.design_border_right());
        borderRight.setIcon(EditorBundle.INSTANCE.borderRight());
        borderRight.addSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                presenter.onAddBorderRight(borderWidth.getValue(),
                        borderColorMenu.getColor());
            }
        });
        borderBottom = new TextButton();
        borderBottom
                .setTitle(EditorMessage.Util.MESSAGE.design_border_bottom());
        borderBottom.setIcon(EditorBundle.INSTANCE.borderBottom());
        borderBottom.addSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                presenter.onAddBorderBottom(borderWidth.getValue(),
                        borderColorMenu.getColor());
            }
        });
        borderLeft = new TextButton();
        borderLeft.setTitle(EditorMessage.Util.MESSAGE.design_border_left());
        borderLeft.setIcon(EditorBundle.INSTANCE.borderLeft());
        borderLeft.addSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                presenter.onAddBorderLeft(borderWidth.getValue(),
                        borderColorMenu.getColor());
            }
        });

        borderWidth = new SimpleComboBox<Integer>(
                new StringLabelProvider<Integer>());
        borderWidth.setAllowBlank(false);
        borderWidth.setForceSelection(true);
        borderWidth.setEditable(false);
        borderWidth.setTriggerAction(TriggerAction.ALL);
        borderWidth.setWidth(50);
        borderWidth.setValue(1);
        borderWidth.add(Arrays.asList(0, 1, 2, 3, 4, 5));
        borderWidth.setTitle(EditorMessage.Util.MESSAGE.design_border_width());
        borderWidth.addSelectionHandler(new SelectionHandler<Integer>() {
            @Override
            public void onSelection(SelectionEvent<Integer> event) {
                borderWidth.setValue(event.getSelectedItem());
            }
        });

        borderColor = new TextButton();
        borderColor.setTitle(EditorMessage.Util.MESSAGE.design_border_color());
        borderColor.setIcon(EditorBundle.INSTANCE.color());
        borderColorMenu = new ColorMenu();
        borderColor.setMenu(borderColorMenu);
        borderColorMenu.getPalette().addValueChangeHandler(
                new ValueChangeHandler<String>() {

                    @Override
                    public void onValueChange(ValueChangeEvent<String> event) {
                        borderColorMenu.hide();
                    }
                });

        backgroundColor = new TextButton();
        backgroundColor.setTitle(EditorMessage.Util.MESSAGE
                .design_background_color());
        backgroundColor.setIcon(EditorBundle.INSTANCE.color());
        backgroundColorMenu = new ColorMenu();
        backgroundColor.setMenu(backgroundColorMenu);
        backgroundColorMenu.getPalette().addValueChangeHandler(
                new ValueChangeHandler<String>() {

                    @Override
                    public void onValueChange(ValueChangeEvent<String> event) {
                        backgroundColorMenu.hide();
                        backgroundColorMenu.getPalette().setValue("");
                        presenter.onSetColor(event.getValue());
                    }
                });

        clearBackgroundColor = new TextButton();
        clearBackgroundColor.setTitle(EditorMessage.Util.MESSAGE.design_clear_color());
        clearBackgroundColor.setIcon(EditorBundle.INSTANCE.clearColor());
        clearBackgroundColor.addSelectHandler(new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                presenter.onSetColor(null);
            }
        });
        separator = new SeparatorToolItem();
        separator2 = new SeparatorToolItem();

        showHidden = new TextButton();
        showHidden.setTitle(EditorMessage.Util.MESSAGE.design_show_hidden());
        showHidden.setIcon(EditorBundle.INSTANCE.bricks());
        showHidden.addSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                presenter.onChangeShowHidden();
            }
        });

        toolBar.add(delete);
        toolBar.add(addRow);
        toolBar.add(addColumn);
        toolBar.add(deleteRow);
        toolBar.add(deleteColumn);
        toolBar.add(union);
        toolBar.add(divide);
        toolBar.add(backgroundColor);
        toolBar.add(clearBackgroundColor);
        toolBar.add(separator);
        toolBar.add(borderTop);
        toolBar.add(borderRight);
        toolBar.add(borderBottom);
        toolBar.add(borderLeft);
        toolBar.add(borderWidth);
        toolBar.add(borderColor);
        toolBar.add(separator2);
        toolBar.add(showHidden);

        add(toolBar);
    }

    @Override
    public void addResizeEndHandler(ResizeEndHandler handler) {
        resizable.addResizeEndHandler(handler);
    }

    @Override
    public void setRootComponent(ComponentBuilder builder, double width,
                                 double height) {
        container.clear();

        if (builder instanceof FormBuilder) {
            prepareFormToolBar();
        } else {
            prepareComponentToolBar();
        }

        container.add(builder.getComponent());
        setContainerSize(width, height);
    }

    private void prepareFormToolBar() {
        delete.setVisible(true);
        addRow.setVisible(true);
        addColumn.setVisible(true);
        deleteRow.setVisible(true);
        deleteColumn.setVisible(true);
        union.setVisible(true);
        divide.setVisible(true);
        backgroundColor.setVisible(true);
        clearBackgroundColor.setVisible(true);
        borderTop.setVisible(true);
        borderRight.setVisible(true);
        borderBottom.setVisible(true);
        borderLeft.setVisible(true);
        borderWidth.setVisible(true);
        borderColor.setVisible(true);
        separator.setVisible(true);
        separator2.setVisible(true);
        showHidden.setVisible(true);
    }

    private void prepareComponentToolBar() {
        delete.setVisible(true);
        addRow.setVisible(false);
        addColumn.setVisible(false);
        deleteRow.setVisible(false);
        deleteColumn.setVisible(false);
        union.setVisible(false);
        divide.setVisible(false);
        backgroundColor.setVisible(false);
        clearBackgroundColor.setVisible(false);
        borderTop.setVisible(false);
        borderRight.setVisible(false);
        borderBottom.setVisible(false);
        borderLeft.setVisible(false);
        borderWidth.setVisible(false);
        borderColor.setVisible(false);
        separator.setVisible(false);
    }

    // Чтобы Selector.SelectionListener прослушивал только контейнер, а не весь
    // view
    @Override
    public Component getContainer() {
//        return inner;
        return container;
    }

    @Override
    protected void onResize(int width, int height) {
        super.onResize(width, height);
        outer.setHeight(height - 27); // Просто подобрал число. Не понятно как
        // получить относительную высоту
        inner.setHeight(height - 52);

        containerWidth = width - 32;
        containerHeight = height - 60;
        setContainerSize(0, 0);
    }

    private void setContainerSize(double width, double height) {
        if (width > 1) {
            container.setWidth(new Double(width).intValue());
            percentWidth = 0;
        } else if (width > 0) {
            percentWidth = width;
            container.setWidth(new Double(containerWidth * width).intValue());
        } else if (percentWidth != 0) {
            container.setWidth(new Double(containerWidth * percentWidth)
                    .intValue());
        }
        if (height > 1) {
            container.setHeight(new Double(height).intValue());
            percentHeight = 0;
        } else if (height > 0) {
            percentHeight = height;
            container
                    .setHeight(new Double(containerHeight * height).intValue());
        } else if (percentHeight != 0) {
            container.setHeight(new Double(containerHeight * percentHeight)
                    .intValue());
        }
    }

    @Override
    public void clearSizes() {
        percentWidth = 0;
        percentHeight = 0;
    }

    @Override
    public void setShowHidden(boolean showHidden) {
        if (showHidden) {
            this.showHidden.setTitle(EditorMessage.Util.MESSAGE.design_hide_hidden());
            this.showHidden.setIcon(EditorBundle.INSTANCE.bricksHidden());
        } else {
            this.showHidden.setTitle(EditorMessage.Util.MESSAGE.design_show_hidden());
            this.showHidden.setIcon(EditorBundle.INSTANCE.bricks());
        }
    }
}
