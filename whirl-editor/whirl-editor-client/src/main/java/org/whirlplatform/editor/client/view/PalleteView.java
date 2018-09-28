package org.whirlplatform.editor.client.view;

import com.sencha.gxt.cell.core.client.ButtonCell.ButtonScale;
import com.sencha.gxt.cell.core.client.ButtonCell.IconAlign;
import com.sencha.gxt.core.client.dom.DefaultScrollSupport;
import com.sencha.gxt.core.client.dom.ScrollSupport;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.CssFloatLayoutContainer;
import org.whirlplatform.editor.client.image.ComponentIcon;
import org.whirlplatform.editor.client.presenter.PalletePresenter.IPalleteView;
import org.whirlplatform.meta.shared.component.ComponentType;

public class PalleteView extends CssFloatLayoutContainer implements IPalleteView {

    public PalleteView() {
        super();
        initUI();
    }

    private void initUI() {
        ScrollSupport scroll = new DefaultScrollSupport(getElement());
        scroll.setScrollMode(ScrollMode.AUTOY);
        setScrollSupport(scroll);
        setBorders(false);
    }

    public Component addComponentType(ComponentType type) {
        String text = getComponentName(type.getType());
        TextButton button = new TextButton(text);
        button.setScale(ButtonScale.MEDIUM);
        button.setWidth(115);
        button.setIcon(ComponentIcon.getIcon(type.getType()));
        button.setIconAlign(IconAlign.TOP);
        // VerticalLayoutData data = new VerticalLayoutData();
        // MarginData data = new MarginData();
        CssFloatData data = new CssFloatData();
        data.setMargins(new Margins(2));
        add(button, data);
        return button;
    }

    private String getComponentName(String type) {
        int i = type.lastIndexOf("Builder");
        if (i != -1) {
            return type.substring(0, i);
        }
        return type;
    }
}
