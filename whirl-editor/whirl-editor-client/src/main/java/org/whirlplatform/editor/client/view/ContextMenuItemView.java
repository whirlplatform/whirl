package org.whirlplatform.editor.client.view;

import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;
import java.util.Collection;
import org.whirlplatform.editor.client.component.PropertyValueField;
import org.whirlplatform.editor.client.presenter.ContextMenuItemPresenter;
import org.whirlplatform.editor.client.presenter.ContextMenuItemPresenter.IContextMenuItemView;
import org.whirlplatform.editor.shared.i18n.EditorMessage;
import org.whirlplatform.meta.shared.editor.LocaleElement;
import org.whirlplatform.meta.shared.editor.PropertyValue;

public class ContextMenuItemView extends ContentPanel implements IContextMenuItemView {

    private ContextMenuItemPresenter presenter;
    private VerticalLayoutContainer container;

    private PropertyValueField labelField;
    private FieldLabel labelLabel;
    private TextField imageUrlField;
    private FieldLabel imageUrlLabel;

    @Override
    public void initUI() {
        container = new VerticalLayoutContainer();
        container.setAdjustForScroll(true);
        container.setScrollMode(ScrollMode.AUTO);
        setWidget(container);
        initFields();
    }

    private void initFields() {
        labelField = new PropertyValueField();
        labelLabel = new FieldLabel(labelField, EditorMessage.Util.MESSAGE.title());
        container.add(labelLabel, new VerticalLayoutData(1, -1, new Margins(10,
            10, 0, 10)));

        imageUrlField = new TextField();
        imageUrlLabel =
            new FieldLabel(imageUrlField, EditorMessage.Util.MESSAGE.context_menu_item_image());
        container.add(imageUrlLabel, new VerticalLayoutData(1, -1, new Margins(10,
            10, 0, 10)));
    }

    @Override
    public ContextMenuItemPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void setPresenter(ContextMenuItemPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public PropertyValue getLabel() {
        return labelField.getPropertyValue();
    }

    @Override
    public void setLabel(PropertyValue label) {
        labelField.setPropertyValue(label);
    }

    @Override
    public String getImageUrl() {
        return imageUrlField.getValue();
    }

    @Override
    public void setImageUrl(String imageUrl) {
        imageUrlField.setValue(imageUrl);
    }

    @Override
    public void clearValues() {
        labelField.clear();
        imageUrlField.clear();
    }

    @Override
    public void setLocales(Collection<LocaleElement> locales,
                           LocaleElement defaultLocale) {
        labelField.setLocales(defaultLocale, locales);
    }

    @Override
    public void setHeaderText(String text) {
        setHeading(text);
    }
}
