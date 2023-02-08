package org.whirlplatform.component.client.grid;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.sencha.gxt.core.client.dom.DomQuery;
import com.sencha.gxt.data.shared.ListStore;
import org.whirlplatform.component.client.resource.ApplicationBundle;
import org.whirlplatform.meta.shared.SortType;
import org.whirlplatform.meta.shared.SortValue;

public class SortRowCell extends AbstractCell<SortValue> {

    private final ListStore<SortValue> store;

    public SortRowCell(ListStore<SortValue> store) {
        super("click");
        this.store = store;
    }

    @Override
    public void render(com.google.gwt.cell.client.Cell.Context context,
                       SortValue value, SafeHtmlBuilder sb) {
        sb.appendHtmlConstant("<div>");
        sb.appendHtmlConstant("<span>")
            .appendEscaped(value.getField().getRawLabel())
            .appendHtmlConstant("</span>");
        String imgSrc = null;
        if (value.getOrder() == SortType.ASC) {
            imgSrc = ApplicationBundle.INSTANCE.arrow_up().getSafeUri()
                .asString();
        }
        if (value.getOrder() == SortType.DESC) {
            imgSrc = ApplicationBundle.INSTANCE.arrow_down().getSafeUri()
                .asString();
        }
        if (imgSrc != null) {
            sb.append(SafeHtmlUtils.fromTrustedString("<img src=\"" + imgSrc
                + "\" style=\"vertical-align: bottom;\">"));
        }
        sb.appendHtmlConstant("</div>");
    }

    @Override
    public void onBrowserEvent(Context context, Element parent,
                               SortValue value, NativeEvent event,
                               ValueUpdater<SortValue> valueUpdater) {
        Element img = DomQuery.selectNode("img", parent);
        Element target = event.getEventTarget().cast();
        if (target.equals(img)) {
            super.onBrowserEvent(context, parent, value, event, valueUpdater);
            if (value.getOrder() == SortType.ASC) {
                value.setOrder(SortType.DESC);
            } else {
                value.setOrder(SortType.ASC);
            }
            store.update(value);
        } else {
            event.stopPropagation();
        }
    }
}
