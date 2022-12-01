package org.whirlplatform.editor.client.presenter;

import com.google.gwt.user.client.ui.IsWidget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;
import com.mvp4g.client.view.ReverseViewInterface;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import org.whirlplatform.editor.client.EditorEventBus;
import org.whirlplatform.editor.client.meta.NewPropertyElement;
import org.whirlplatform.editor.client.view.PropertyEditorView;
import org.whirlplatform.meta.shared.Version;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataValueImpl;
import org.whirlplatform.meta.shared.editor.ApplicationElement;
import org.whirlplatform.meta.shared.editor.ComponentElement;
import org.whirlplatform.meta.shared.editor.LocaleElement;
import org.whirlplatform.meta.shared.editor.PropertyValue;

@Presenter(view = PropertyEditorView.class)
public class PropertyEditorPresenter
        extends BasePresenter<PropertyEditorPresenter.IPropertyEditorView, EditorEventBus> {

    private ApplicationElement application;
    private ComponentElement currentElement;
    public PropertyEditorPresenter() {
        super();
    }

    /**
     * Все доступные свойства компонента, отсортированные по полям isUI и code
     *
     * @param type      Тип компонента
     * @param initProps Свойства, с установленными значениями
     * @return
     */
    private Map<PropertyType, PropertyValue> prepareProperties(ComponentType type,
                                                               Map<PropertyType, PropertyValue> initProps) {
        Map<PropertyType, PropertyValue> result = new TreeMap<PropertyType, PropertyValue>(
                new Comparator<PropertyType>() {
                    @Override
                    public int compare(PropertyType o1, PropertyType o2) {
                        int res = String.valueOf(!o1.isUI()).compareTo(String.valueOf(!o2.isUI()));
                        if (res == 0) {
                            res = o1.getCode().compareTo(o2.getCode());
                        }
                        return res;
                    }
                });
        for (PropertyType propertyType : type.getProperties()) {
            PropertyValue propertyValue = initProps.get(propertyType);
            result.put(propertyType, propertyValue);
        }
        return result;
    }

    public void onSelectComponentElement(final ComponentElement element) {
        view.finishEditing();
        PropertyEditorPresenter.this.currentElement = element;
        view.setComponentProperties(currentElement.getType(),
                prepareProperties(currentElement.getType(), currentElement.getProperties()));
        eventBus.changeSecondLeftComponent(view);
    }

    // TODO параметризовать свойства
    public void onChangeComponentProperty(PropertyType type,
                                          LocaleElement locale, boolean replaceable, Object value) {
        eventBus.addElement(currentElement,
                new NewPropertyElement(type, locale, replaceable, value));
        eventBus.syncComponentPropertyToUI(currentElement, type.getCode(),
                value == null ? null : new DataValueImpl(type.getType(), value));
    }

    public void onSyncComponentPropertyToElement(ComponentElement element) {
        if (currentElement != element) {
            return;
        }

        // TODO устанавливать значения по одному
        view.setComponentProperties(element.getType(),
                prepareProperties(element.getType(), currentElement.getProperties()));
    }

    public void onLoadApplication(ApplicationElement application, Version version) {
        this.application = application;
    }

    public ApplicationElement getApplication() {
        return application;
    }

    public interface IPropertyEditorView
            extends IsWidget, ReverseViewInterface<PropertyEditorPresenter> {

        Map<PropertyType, PropertyValue> getProperties();

        void setComponentProperties(ComponentType type,
                                    Map<PropertyType, PropertyValue> properties);

        void finishEditing();
    }
}
