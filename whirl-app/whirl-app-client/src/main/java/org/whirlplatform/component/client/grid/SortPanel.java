package org.whirlplatform.component.client.grid;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerRegistration;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.dom.DomQuery;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.Util;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.event.StoreAddEvent;
import com.sencha.gxt.data.shared.event.StoreAddEvent.StoreAddHandler;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.event.ShowEvent;
import com.sencha.gxt.widget.core.client.event.ShowEvent.ShowHandler;
import com.sencha.gxt.widget.core.client.form.DualListField;
import com.sencha.gxt.widget.core.client.form.DualListField.Mode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.whirlplatform.component.client.event.SortEvent;
import org.whirlplatform.component.client.selenium.Locator;
import org.whirlplatform.component.client.selenium.LocatorAware;
import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.FieldMetadata;
import org.whirlplatform.meta.shared.SortType;
import org.whirlplatform.meta.shared.SortValue;
import org.whirlplatform.meta.shared.i18n.AppMessage;

public class SortPanel extends Window implements SortEvent.HasSortHandlers, LocatorAware {

    public static final int MIN_WIDTH = 400;
    public static final int MIN_HEIGHT = 250;
    private final ClassMetadata metadata;
    private final TextButton save;
    private final TextButton close;
    private boolean init = false;
    private DualListField<SortValue, SortValue> dualList;
    private ListStore<SortValue> fromStore;
    private ListStore<SortValue> toStore;

    public SortPanel(final ClassMetadata metadata) {
        super();
        this.metadata = metadata;

        addShowHandler(new ShowHandler() {

            @Override
            public void onShow(ShowEvent event) {
                expandWindow();
            }

        });

        setMaximizable(true);
        setButtonAlign(BoxLayoutPack.CENTER);

        save = new TextButton(AppMessage.Util.MESSAGE.save());
        save.addSelectHandler(new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                fireEvent(new SortEvent());
            }
        });
        addButton(save);
        close = new TextButton(AppMessage.Util.MESSAGE.close());
        close.addSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                hide();
            }

        });
        addButton(close);
        initDualList();
        setModal(true); // Т.к. если окно не модальное, всё разблокируется
    }

    private void initDualList() {
        ModelKeyProvider<SortValue> keyProvider = new ModelKeyProvider<SortValue>() {

            @Override
            public String getKey(SortValue item) {
                return item.getField().getId();
            }

        };
        fromStore = new ListStore<SortValue>(keyProvider);
        toStore = new ListStore<SortValue>(keyProvider);

        ValueProvider<SortValue, SortValue> valueProvider =
            new ValueProvider<SortValue, SortValue>() {

                @Override
                public SortValue getValue(SortValue object) {
                    return object;
                }

                @Override
                public void setValue(SortValue object, SortValue value) {
                }

                @Override
                public String getPath() {
                    return null;
                }

            };

        dualList = new DualListField<SortValue, SortValue>(fromStore, toStore, valueProvider,
            new SortRowCell(toStore));

        dualList.setEnableDnd(true);
        dualList.setMode(Mode.INSERT);
    }

    private void expandWindow() {
        int maxItems = Math.max(fromStore.size(), toStore.size());
        int wndHeight = maxItems * 25 + 30;
        if (wndHeight < MIN_HEIGHT) {
            wndHeight = MIN_HEIGHT;
        }

        List<SortValue> all = new ArrayList<SortValue>();
        all.addAll(fromStore.getAll());
        all.addAll(toStore.getAll());
        int labelLength = 0;
        int maxLabelLength = 0;
        for (SortValue sm : all) {
            labelLength = sm.getField().getRawLabel().length();
            if (maxLabelLength < labelLength) {
                maxLabelLength = labelLength;
            }
        }
        int wndWidth = maxLabelLength * 20 + 50;
        if (wndWidth < MIN_WIDTH) {
            wndWidth = MIN_WIDTH;
        }
        setWidth(wndWidth);
        setHeight(wndHeight);
        center();
    }

    public void show(List<SortValue> values) {
        super.show();

        // заполняем/обновляем списки
        fillSorts(values);

        if (init) {
            return;
        }

        initStoreListener();

        setWidget(dualList);
        expandWindow();
        init = true;
        doLayout();
    }

    private void initStoreListener() {
        toStore.addStoreAddHandler(new StoreAddHandler<SortValue>() {

            @Override
            public void onAdd(StoreAddEvent<SortValue> event) {
                List<SortValue> list = event.getItems();
                for (SortValue el : list) {
                    if (el.getOrder() == null) {
                        el.setOrder(SortType.ASC);
                        event.getSource().update(el);
                    }
                }
                expandWindow();
            }

        });

        fromStore.addStoreAddHandler(new StoreAddHandler<SortValue>() {

            @Override
            public void onAdd(StoreAddEvent<SortValue> event) {
                List<SortValue> list = event.getItems();
                for (SortValue el : list) {
                    el.setOrder(null);
                    event.getSource().update(el);
                }
                expandWindow();
            }

        });
    }

    public void fillSorts(List<SortValue> data) {
        if (data == null) {
            return;
        }
        HashSet<FieldMetadata> sorts = new HashSet<FieldMetadata>();
        for (SortValue m : data) {
            if (toStore.findModelWithKey(toStore.getKeyProvider().getKey(m)) != null) {
                toStore.findModelWithKey(toStore.getKeyProvider().getKey(m))
                    .setOrder(m.getOrder()); // Восстановление
                // сортировки
                sorts.add(m.getField());
                continue;
            }
            toStore.add(m);
            fromStore.remove(m);
            sorts.add(m.getField());
        }

        for (FieldMetadata f : metadata.getFields()) {
            if (!f.isView()) {
                continue;
            }
            if (!sorts.contains(f)) {
                SortValue model = new SortValue();
                model.setField(f);
                if (fromStore.findModelWithKey(fromStore.getKeyProvider().getKey(model)) == null) {
                    fromStore.add(model);
                }
            }
        }
    }

    public List<SortValue> getSort() {
        return toStore.getAll();
    }

    @Override
    public HandlerRegistration addSortHandler(SortEvent.SortHandler handler) {
        return addHandler(handler, SortEvent.getType());
    }

    @Override
    public Locator getLocatorByElement(Element element) {
        Locator result = new Locator(LocatorParams.TYPE_SORT_PANEL);
        if (save.getElement().isOrHasChild(element)) {
            result.setPart(new Locator(LocatorParams.TYPE_SAVE_BUTTON));
            return result;
        } else if (close.getElement().isOrHasChild(element)) {
            result.setPart(new Locator(LocatorParams.TYPE_CLOSE_BUTTON));
            return result;
        }
        if (dualList.getElement().isOrHasChild(element)) {
            if (dualList.getFromView().getElement().isOrHasChild(element)) {
                result.setPart(getFromViewLocator(element));
                return result;
            } else if (dualList.getToView().getElement().isOrHasChild(element)) {
                result.setPart(getToViewLocator(element));
                return result;
            } else {
                Locator buttonsLocator = searchLocatorInDualListButtons(element);
                if (buttonsLocator != null) {
                    result.setPart(buttonsLocator);
                }
            }
        }
        return result;
    }

    private Locator getFromViewLocator(Element element) {
        Locator fromPanelPart = new Locator(LocatorParams.TYPE_FROM_PANEL);
        List<Element> elements = dualList.getFromView().getElements();
        int index = -1;
        for (int i = 0; i < elements.size(); i++) {
            if (elements.get(i).isOrHasChild(element)) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            SortValue value = dualList.getFromStore().get(index);
            if (value != null) {
                Locator sortValPart = new Locator(LocatorParams.TYPE_SORT_VAL);
                sortValPart.setParameter(LocatorParams.PARAM_MDID, value.getField().getId());
                sortValPart.setParameter(LocatorParams.PARAM_LABEL, value.getField().getRawLabel());
                sortValPart.setParameter(LocatorParams.PARAM_INDEX, String.valueOf(index));
                fromPanelPart.setPart(sortValPart);
            }
        }
        return fromPanelPart;
    }

    private Locator getToViewLocator(Element element) {
        Locator toPanelPart = new Locator(LocatorParams.TYPE_TO_PANEL);
        List<Element> elements = dualList.getToView().getElements();
        int index = -1;
        Element foundCellElement = null;
        for (int i = 0; i < elements.size(); i++) {
            if (elements.get(i).isOrHasChild(element)) {
                foundCellElement = elements.get(i);
                index = i;
                break;
            }
        }
        if (index != -1) {
            SortValue value = dualList.getToStore().get(index);
            if (value != null) {
                Locator sortValPart = new Locator(LocatorParams.TYPE_SORT_VAL);
                sortValPart.setParameter(LocatorParams.PARAM_MDID, value.getField().getId());
                sortValPart.setParameter(LocatorParams.PARAM_LABEL, value.getField().getRawLabel());
                sortValPart.setParameter(LocatorParams.PARAM_INDEX, String.valueOf(index));
                Element img = DomQuery.selectNode("img", foundCellElement);
                if (img != null && img.isOrHasChild(element)) {
                    sortValPart.setPart(new Locator(LocatorParams.TYPE_IMG));
                }
                toPanelPart.setPart(sortValPart);
            }
        }
        return toPanelPart;
    }

    private Locator searchLocatorInDualListButtons(Element element) {
        if (dualList.getRightButton().getElement().isOrHasChild(element)) {
            return new Locator(LocatorParams.TYPE_RIGHT_ICON);
        } else if (dualList.getLeftButton().getElement().isOrHasChild(element)) {
            return new Locator(LocatorParams.TYPE_LEFT_ICON);
        } else if (dualList.getAllRightButton().getElement().isOrHasChild(element)) {
            return new Locator(LocatorParams.TYPE_ALL_RIGHT_ICON);
        } else if (dualList.getAllLeftButton().getElement().isOrHasChild(element)) {
            return new Locator(LocatorParams.TYPE_ALL_LEFT_ICON);
        } else if (dualList.getUpButton().getElement().isOrHasChild(element)) {
            return new Locator(LocatorParams.TYPE_UP_ICON);
        } else if (dualList.getDownButton().getElement().isOrHasChild(element)) {
            return new Locator(LocatorParams.TYPE_DOWN_ICON);
        }
        return null;
    }

    @Override
    public void fillLocatorDefaults(Locator locator, Element element) {
        // TODO Auto-generated method stub
    }

    @Override
    public Element getElementByLocator(Locator locator) {
        Locator part = locator.getPart();
        if (part == null) {
            return this.getElement();
        }
        if (LocatorParams.TYPE_SAVE_BUTTON.equals(part.getType())) {
            return getTextButtonElement(save);
        } else if (LocatorParams.TYPE_CLOSE_BUTTON.equals(part.getType())) {
            return getTextButtonElement(close);
        } else if (LocatorParams.TYPE_FROM_PANEL.equals(part.getType())) {
            return getFromViewElement(part);
        } else if (LocatorParams.TYPE_TO_PANEL.equals(part.getType())) {
            return getToViewElement(part);
        } else {
            return searchElementInDualListButtons(part);
        }
    }

    private Element getFromViewElement(Locator part) {
        Locator sortValPart = part.getPart();
        ListView<SortValue, SortValue> view = dualList.getFromView();
        if (sortValPart == null) {
            return view.getElement();
        }
        ListStore<SortValue> store = dualList.getFromStore();
        String type = LocatorParams.PARAM_MDID;
        Element byId = getElementByParam(sortValPart.getParameter(type), type, false, store, view);
        if (byId != null) {
            return byId;
        }
        type = LocatorParams.PARAM_LABEL;
        Element byLabel =
            getElementByParam(sortValPart.getParameter(type), type, false, store, view);
        if (byLabel != null) {
            return byLabel;
        }
        type = LocatorParams.PARAM_INDEX;
        Element byIndex =
            getElementByParam(sortValPart.getParameter(type), type, false, store, view);
        return byIndex;
    }

    private Element getToViewElement(Locator part) {
        Locator sortValPart = part.getPart();
        ListView<SortValue, SortValue> view = dualList.getToView();
        if (sortValPart == null) {
            return view.getElement();
        }
        Locator imgPart = sortValPart.getPart();
        boolean isImg = (imgPart != null && LocatorParams.TYPE_IMG.equals(imgPart.getType()));
        ListStore<SortValue> store = dualList.getToStore();
        String type = LocatorParams.PARAM_MDID;
        Element byId = getElementByParam(sortValPart.getParameter(type), type, isImg, store, view);
        if (byId != null) {
            return byId;
        }
        type = LocatorParams.PARAM_LABEL;
        Element byLabel =
            getElementByParam(sortValPart.getParameter(type), type, isImg, store, view);
        if (byLabel != null) {
            return byLabel;
        }
        type = LocatorParams.PARAM_INDEX;
        Element byIndex =
            getElementByParam(sortValPart.getParameter(type), type, isImg, store, view);
        return byIndex;
    }

    private Element getElementByParam(String param, String paramType, boolean isImg,
                                      ListStore<SortValue> store,
                                      ListView<SortValue, SortValue> view) {
        if (!Util.isEmptyString(param)) {
            int sortValIndex = -1;
            if (LocatorParams.PARAM_MDID.equals(paramType)) {
                for (SortValue sortValue : store.getAll()) {
                    if (sortValue.getField().getId().equals(param)) {
                        sortValIndex = store.indexOf(sortValue);
                        break;
                    }
                }
            }
            if (LocatorParams.PARAM_LABEL.equals(paramType)) {
                for (SortValue sortValue : store.getAll()) {
                    if (sortValue.getField().getRawLabel().equals(param)) {
                        sortValIndex = store.indexOf(sortValue);
                        break;
                    }
                }
            }
            if (LocatorParams.PARAM_INDEX.equals(paramType)) {
                int index = Integer.valueOf(param);
                sortValIndex = index;
            }
            if (sortValIndex != -1) {
                if (isImg) {
                    return getImgElement(view.getElement(sortValIndex));
                } else {
                    return view.getElement(sortValIndex);
                }

            }
        }
        return null;
    }

    private Element searchElementInDualListButtons(Locator locator) {
        String type = locator.getType();
        if (Util.isEmptyString(type)) {
            return null;
        }
        if (LocatorParams.TYPE_UP_ICON.equals(type)) {
            return dualList.getUpButton().getElement();
        } else if (LocatorParams.TYPE_ALL_LEFT_ICON.equals(type)) {
            return dualList.getAllLeftButton().getElement();
        } else if (LocatorParams.TYPE_LEFT_ICON.equals(type)) {
            return dualList.getLeftButton().getElement();
        } else if (LocatorParams.TYPE_RIGHT_ICON.equals(type)) {
            return dualList.getRightButton().getElement();
        } else if (LocatorParams.TYPE_ALL_RIGHT_ICON.equals(type)) {
            return dualList.getAllRightButton().getElement();
        } else if (LocatorParams.TYPE_DOWN_ICON.equals(type)) {
            return dualList.getDownButton().getElement();
        }
        return null;
    }

    private XElement getTextButtonElement(final TextButton button) {
        return (button != null) ? button.getCell().getFocusElement(button.getElement()) : null;
    }

    /**
     * Пытаемся извлечь изображение, если не удалось возвращаем родительский элемент.
     *
     * @param element - элемент у которого пытаемся получить изображение.
     * @return
     */
    private Element getImgElement(Element element) {
        if (element == null) {
            return null;
        }
        Element img = DomQuery.selectNode("img", element);
        if (img != null) {
            return img;
        } else {
            return element;
        }
    }

    static class LocatorParams {
        public static final String TYPE_SORT_PANEL = "SortPanel";
        public static final String TYPE_FROM_PANEL = "FromPanel";
        public static final String TYPE_TO_PANEL = "ToPanel";

        public static final String TYPE_SAVE_BUTTON = "SaveButton";
        public static final String TYPE_CLOSE_BUTTON = "CloseButton";

        public static final String TYPE_UP_ICON = "Up";
        public static final String TYPE_DOWN_ICON = "Down";
        public static final String TYPE_ALL_RIGHT_ICON = "AllRight";
        public static final String TYPE_ALL_LEFT_ICON = "AllLeft";
        public static final String TYPE_RIGHT_ICON = "Right";
        public static final String TYPE_LEFT_ICON = "Left";

        public static final String TYPE_SORT_VAL = "SortVal";
        public static final String TYPE_IMG = "Img";

        public static final String PARAM_MDID = "mdid";
        public static final String PARAM_LABEL = "label";
        public static final String PARAM_INDEX = "index";
    }

}
