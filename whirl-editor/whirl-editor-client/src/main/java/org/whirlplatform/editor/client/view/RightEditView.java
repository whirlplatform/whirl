package org.whirlplatform.editor.client.view;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.ui.Label;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.StringLabelProvider;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.geomajas.codemirror.client.Config;
import org.geomajas.codemirror.client.widget.CodeMirrorPanel;
import org.whirlplatform.component.client.form.GridLayoutContainer;
import org.whirlplatform.component.client.form.GridLayoutDecorator;
import org.whirlplatform.editor.client.component.CheckState;
import org.whirlplatform.editor.client.component.ThreeStateCheck;
import org.whirlplatform.editor.client.image.EditorBundle;
import org.whirlplatform.editor.client.presenter.RightEditPresenter.IRightEditView;
import org.whirlplatform.editor.shared.i18n.EditorMessage;
import org.whirlplatform.meta.shared.editor.AbstractCondition;
import org.whirlplatform.meta.shared.editor.AbstractElement;
import org.whirlplatform.meta.shared.editor.BooleanCondition;
import org.whirlplatform.meta.shared.editor.GroupElement;
import org.whirlplatform.meta.shared.editor.RightCollectionElement;
import org.whirlplatform.meta.shared.editor.RightElement;
import org.whirlplatform.meta.shared.editor.RightType;
import org.whirlplatform.meta.shared.editor.SQLCondition;

public class RightEditView extends GridLayoutContainer implements
        IRightEditView {

    private GroupElement applicationGroup = new GroupElement();
    private Table<AbstractElement, GroupElement, Map<RightType, RightChangeComponent>> mappings =
            HashBasedTable
                    .create();
    private GridLayoutDecorator decorator = new GridLayoutDecorator(this);

    public RightEditView() {
        super();
        applicationGroup.setName(EditorMessage.Util.MESSAGE.right_edit_application());

        decorator.setGridColor("#99B9E9");
        decorator.setGridEnabled(true);
        getElement().getStyle().setBackgroundColor("#FFFFFF");

        initHeader();
    }

    public void initHeader() {
        resize(1, 2);

        Label rightLabel = new Label(EditorMessage.Util.MESSAGE.right_edit_right());
        setWidget(0, 1, rightLabel);
        setColumnWidth(1, 50);

        setRowHeight(0, 30);
    }

    public void initialize(Collection<? extends AbstractElement> elements,
                           Collection<GroupElement> groups, Collection<RightType> types) {
        mappings.clear();
        resizeRows(1);
        resizeColumns(2);

        int colSize = 4 + groups.size();
        resizeColumns(colSize);

        // заголовок по группам
        List<GroupElement> allGroups = new ArrayList<GroupElement>();
        allGroups.add(applicationGroup);
        allGroups.addAll(groups);

        int groupCol = 2;
        Iterator<GroupElement> groupIter = allGroups.iterator();
        while (groupIter.hasNext()) {
            GroupElement g = groupIter.next();
            Label groupHeader = new Label(g.getName());
            setWidget(0, groupCol, groupHeader);
            groupCol++;
        }

        int rowSize = 1 + elements.size() * types.size();
        resizeRows(rowSize);

        int elemRow = 1;
        Iterator<? extends AbstractElement> elemIter = elements.iterator();
        while (elemIter.hasNext()) {
            AbstractElement e = elemIter.next();
            int currentTopRow = elemRow;

            Iterator<RightType> typesIter = types.iterator();
            while (typesIter.hasNext()) {
                RightType t = typesIter.next();

                setWidget(elemRow, 1, new Label(t.name()));

                int grIndex = 0;
                Iterator<GroupElement> grIter = allGroups.iterator();
                while (grIter.hasNext()) {
                    GroupElement g = grIter.next();
                    Map<RightType, RightChangeComponent> map = mappings.get(e,
                            g);
                    if (map == null) {
                        map = new HashMap<RightType, RightChangeComponent>();
                        mappings.put(e, g, map);
                    }

                    RightChangeComponent changeComponent = new RightChangeComponent(
                            t);
                    setWidget(elemRow, grIndex + 2, changeComponent);
                    map.put(t, changeComponent);

                    setColumnWidth(grIndex + 2, 80);

                    grIndex++;
                }

                elemRow++;
            }
            setWidget(currentTopRow, 0, new Label(e.getName()));
            setSpan(currentTopRow, 0, types.size(), 1);
        }
        decorator.setPaddingInCells(3);
    }

    public Map<AbstractElement, RightCollectionElement> getRights() {
        Map<AbstractElement, RightCollectionElement> result =
                new HashMap<AbstractElement, RightCollectionElement>();
        for (AbstractElement e : mappings.rowKeySet()) {
//            boolean hasRights = false;
            RightCollectionElement collection = new RightCollectionElement(e);
            for (GroupElement g : mappings.row(e).keySet()) {
                Map<RightType, RightChangeComponent> map = mappings.get(e, g);
                for (RightChangeComponent c : map.values()) {
                    RightElement right = c.getValue();
                    if (right != null) {
//                        hasRights = true;
                        if (g == applicationGroup) {
                            collection.addApplicationRight(right);
                        } else if (g instanceof GroupElement) {
                            collection.addGroupRight(g, right);
                        }
                    }
                }
            }
//            if (hasRights) {
            result.put(e, collection);
//            }
        }
        return result;
    }

    @Override
    public void setRight(AbstractElement element, RightCollectionElement right) {
        for (RightElement r : right.getApplicationRights()) {
            Map<RightType, RightChangeComponent> map = mappings.get(element,
                    applicationGroup);
            if (map != null && map.containsKey(r.getType())) {
                RightChangeComponent component = map.get(r.getType());
                component.setValue(r);
            }
        }

        for (GroupElement g : mappings.columnKeySet()) {
            if (g == applicationGroup || !(g instanceof GroupElement)) {
                continue;
            }
            for (RightElement r : right.getGroupRights(g)) {
                Map<RightType, RightChangeComponent> map = mappings.get(
                        element, g);
                if (map != null && map.containsKey(r.getType())) {
                    RightChangeComponent component = map.get(r.getType());
                    component.setValue(r);
                }
            }
        }
    }

    private class RightChangeComponent extends HorizontalLayoutContainer
            implements TakesValue<RightElement> {

        private RightType rightType;

        private ThreeStateCheck state;
        private TextButton conditionButton;
        private Window conditionWindow;
        private SimpleComboBox<String> conditionType;
        private CodeMirrorPanel conditionText;

        private String conditionValue;

        public RightChangeComponent(RightType rightType) {
            super();
            this.rightType = rightType;
            init();
            state.setValue(null);
        }

        private void init() {
            setHeight(22);
            state = new ThreeStateCheck();
            state.addValueChangeHandler(new ValueChangeHandler<CheckState>() {

                @Override
                public void onValueChange(ValueChangeEvent<CheckState> event) {
                    stateChanged(event.getValue());
                }
            });
            add(state, new HorizontalLayoutData(-1, -1, new Margins(2, 0, 0, 0)));
            conditionButton = new TextButton();
            conditionButton.setIcon(EditorBundle.INSTANCE.key());
            conditionButton.addSelectHandler(new SelectHandler() {
                @Override
                public void onSelect(SelectEvent event) {
                    openConditionWindow();
                }
            });
            conditionButton.setEnabled(false);
            add(conditionButton, new HorizontalLayoutData(-1, -1));

//            conditionWindow = new Window();
//            conditionWindow.setModal(true);
//            conditionWindow.setHeadingHtml(EditorMessage.Util.MESSAGE.right_edit_edit_condition());
//            conditionWindow.setWidth(600);
//            conditionWindow.setHeight(500);
//
//            VerticalLayoutContainer conditionPanel = new VerticalLayoutContainer();
//            conditionWindow.setWidget(conditionPanel);

            conditionType = new SimpleComboBox<String>(
                    new StringLabelProvider<String>());
            conditionType.add("SQL");
            conditionType.setValue("SQL");
            conditionType.setEnabled(false);
//            FieldLabel typeLabel = new FieldLabel(conditionType,
//                    EditorMessage.Util.MESSAGE.right_edit_condition_type());
//            conditionPanel.add(typeLabel, new VerticalLayoutData(1, -1,
//                    new Margins(5, 5, 0, 5)));
//
//            conditionText = new CodeMirrorPanel();
//            FieldLabel conditionLabel = new FieldLabel(conditionText,
//                    EditorMessage.Util.MESSAGE.right_edit_condition_value());
//            conditionPanel.add(conditionLabel, new VerticalLayoutData(1, 1,
//                    new Margins(5, 5, 5, 5)));
//
//            TextButton conditionSave = new TextButton(EditorMessage.Util.MESSAGE.save());
//            conditionSave.addSelectHandler(new SelectHandler() {
//                @Override
//                public void onSelect(SelectEvent event) {
//                    conditionValue = conditionText.getValue() != null
//                            && conditionText.getValue().isEmpty() ? null
//                            : conditionText.getValue();
//                    conditionWindow.hide();
//                }
//            });
//            TextButton conditionClose = new TextButton(EditorMessage.Util.MESSAGE.close());
//            conditionClose.addSelectHandler(new SelectHandler() {
//                @Override
//                public void onSelect(SelectEvent event) {
//                    conditionWindow.hide();
//                }
//            });
//            conditionWindow.setButtonAlign(BoxLayoutPack.END);
//            conditionWindow.addButton(conditionSave);
//            conditionWindow.addButton(conditionClose);
        }

        private void openConditionWindow() {
            if (conditionWindow == null) {
                conditionWindow = new Window();
                conditionWindow.setModal(true);
                conditionWindow.setHeading(EditorMessage.Util.MESSAGE.right_edit_edit_condition());
                conditionWindow.setWidth(600);
                conditionWindow.setHeight(500);

                VerticalLayoutContainer conditionPanel = new VerticalLayoutContainer();
                conditionWindow.setWidget(conditionPanel);

                FieldLabel typeLabel = new FieldLabel(conditionType,
                        EditorMessage.Util.MESSAGE.right_edit_condition_type());
                conditionPanel.add(typeLabel, new VerticalLayoutData(1, -1,
                        new Margins(5, 5, 0, 5)));

                conditionText = new CodeMirrorPanel();
                FieldLabel conditionLabel = new FieldLabel(conditionText,
                        EditorMessage.Util.MESSAGE.right_edit_condition_value());
                conditionPanel.add(conditionLabel, new VerticalLayoutData(1, 1,
                        new Margins(5, 5, 5, 5)));


                TextButton conditionSave = new TextButton(EditorMessage.Util.MESSAGE.apply());
                conditionSave.addSelectHandler(new SelectHandler() {
                    @Override
                    public void onSelect(SelectEvent event) {
                        conditionValue = conditionText.getValue() != null
                                && conditionText.getValue().isEmpty() ? null
                                : conditionText.getValue();
                        conditionWindow.hide();
                    }
                });
                TextButton conditionClose = new TextButton(EditorMessage.Util.MESSAGE.close());
                conditionClose.addSelectHandler(new SelectHandler() {
                    @Override
                    public void onSelect(SelectEvent event) {
                        conditionWindow.hide();
                    }
                });
                conditionWindow.setButtonAlign(BoxLayoutPack.END);
                conditionWindow.addButton(conditionSave);
                conditionWindow.addButton(conditionClose);
            }
            conditionText
                    .setValue(conditionValue == null ? "" : conditionValue);
            conditionText.showEditor(Config.forSql());
            conditionWindow.show();
        }

        private void stateChanged(CheckState st) {
            CheckState s = st == null ? CheckState.UNCHECKED : st;
            switch (s) {
                case CHECKED:
                case UNCHECKED:
                    conditionButton.setEnabled(false);
                    break;
                default:
                    conditionButton.setEnabled(true);
                    break;
            }
        }

        @Override
        public RightElement getValue() {
            if (state.getValue() == null
                    || state.getValue() == CheckState.UNCHECKED) {
                RightElement right = new RightElement(rightType);
                BooleanCondition c = new BooleanCondition();
                c.setValue(false);
                right.setCondition(c);
//                return null;
                return right;
            } else if (state.getValue() == CheckState.CHECKED) {
                RightElement right = new RightElement(rightType);
                BooleanCondition c = new BooleanCondition();
                c.setValue(true);
                right.setCondition(c);
                return right;
            } else if ("SQL".equals(conditionType.getValue())) {
                RightElement right = new RightElement(rightType);
                SQLCondition c = new SQLCondition();
                c.setValue(conditionValue);
                right.setCondition(c);
                return right;
            }
            return null;
        }

        @Override
        public void setValue(RightElement value) {
            if (value == null) {
                state.setValue(CheckState.UNCHECKED);
            } else {
                AbstractCondition<?> c = value.getCondition();
                CheckState s = CheckState.UNCHECKED;
                if (c instanceof BooleanCondition) {
                    if (((BooleanCondition) c).getValue()) {
                        s = CheckState.CHECKED;
                    }
                } else if (c instanceof SQLCondition) {
                    s = CheckState.PARTIAL;
                    conditionValue = ((SQLCondition) c).getValue();
                }
                state.setValue(s, true);
            }
        }
    }
}
