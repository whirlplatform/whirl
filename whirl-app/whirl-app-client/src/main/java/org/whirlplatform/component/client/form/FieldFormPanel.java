package org.whirlplatform.component.client.form;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Command;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.Field;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.IsField;
import org.whirlplatform.component.client.AbstractFieldBuilder;
import org.whirlplatform.component.client.ListParameter;
import org.whirlplatform.component.client.base.NumberFieldBuilder;
import org.whirlplatform.component.client.base.TextFieldBuilder;
import org.whirlplatform.component.client.base.UploadFieldBuilder;
import org.whirlplatform.component.client.check.CheckBoxBuilder;
import org.whirlplatform.component.client.combo.ComboBoxBuilder;
import org.whirlplatform.component.client.date.DateFieldBuilder;
import org.whirlplatform.component.client.event.ChangeEvent;
import org.whirlplatform.component.client.grid.FieldMetadataHelper;
import org.whirlplatform.component.client.selenium.Locator;
import org.whirlplatform.component.client.selenium.LocatorAware;
import org.whirlplatform.component.client.utils.StringMetrics;
import org.whirlplatform.meta.shared.FieldMetadata;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.data.DataValueImpl;
import org.whirlplatform.meta.shared.data.RowListValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class FieldFormPanel extends VerticalLayoutContainer implements LocatorAware {

    private Map<FieldMetadata, AbstractFieldBuilder> builders = new HashMap<FieldMetadata, AbstractFieldBuilder>();
    private Map<FieldMetadata, UploadFieldBuilder> uploadBuilders = new HashMap<FieldMetadata, UploadFieldBuilder>();
    private Map<FieldMetadata, IsField<?>> components = new HashMap<FieldMetadata, IsField<?>>();

    private Set<FieldMetadata> changed = new HashSet<FieldMetadata>();

    private boolean wasChanged;

    private boolean viewOnly = false;

    public FieldFormPanel(List<FieldMetadata> fields, boolean viewOnly) {
        super();
        this.viewOnly = viewOnly;
        setAdjustForScroll(true);
        setScrollMode(ScrollMode.AUTOY);
        initFields(fields);
    }

    public FieldFormPanel(List<FieldMetadata> fields) {
        this(fields, false);
    }

    public void setUploadCommand(Command command) {
        for (UploadFieldBuilder builder : uploadBuilders.values()) {
            builder.prepair(command);
        }
    }

    public boolean checkUpload() {
        boolean result = true;
        for (Entry<FieldMetadata, UploadFieldBuilder> entry : uploadBuilders
                .entrySet()) {
            if (changed.contains(entry.getKey()) && !entry.getValue().isReady()) {
                result = false;
            }
        }
        return result;
    }

    private void initFields(List<FieldMetadata> fields) {
        int labelWidth = 100;
        List<FieldLabel> labels = new ArrayList<FieldLabel>();
        for (FieldMetadata m : fields) {
            if (!m.isView()) {
                continue;
            }
            AbstractFieldBuilder builder = FieldMetadataHelper.build(m, viewOnly);
            if (builder == null) {
                continue;
            }
            if (builder instanceof UploadFieldBuilder) {
                putFieldBuilder(m, (UploadFieldBuilder) builder);
            }
            builders.put(m, builder);

            Map<String, DataValue> properties = FieldMetadataHelper.prepareProperties(m, viewOnly);
            builder.setProperties(properties, false);

            Component comp = builder.create();
            components.put(m, (IsField<?>) comp);

            initChangeHandler(m, builder);

            FieldLabel label = new FieldLabel(comp);
            label.setHTML(m.getLabel());
            if (m.getHeight() != 0) {
                label.setHeight(m.getHeight());
            }
            labels.add(label);

            labelWidth = Math.max(labelWidth, StringMetrics.getWidth(m.getLabel()));

            VerticalLayoutData layout = new VerticalLayoutData(1, -1);
            layout.setMargins(new Margins(5, 5, 0, 5));
            add(label, layout);
        }

        for (FieldLabel l : labels) {
            l.setWidth(labelWidth);
        }
    }

    private void putFieldBuilder(FieldMetadata meta, UploadFieldBuilder builder) {
        uploadBuilders.put(meta, builder);
    }

    private void initChangeHandler(final FieldMetadata metadata, AbstractFieldBuilder builder) {
        builder.addChangeHandler(new ChangeEvent.ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                changed.add(metadata);
            }
        });
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void setValue(FieldMetadata metadata, DataValue value) {
        if (!components.containsKey(metadata) || value == null) {
            return;
        }

        if (metadata.getType() == DataType.FILE) {
            UploadFieldBuilder builder = uploadBuilders.get(metadata);
            builder.setFileValue(value.getFileValue());
            return;
        }

        // if (metadata.getType() == DataType.MULTILIST
        // && builders.get(metadata) instanceof MultiComboBoxBuilder) {
        // ((MultiComboBoxBuilder) builders.get(metadata)).setSelection(value
        // .<List<ListModelData>> getValue());
        // }

        if (builders.get(metadata) instanceof ListParameter) {
            ((ListParameter) builders.get(metadata))
                    .setFieldValue((RowListValue) value);
        }

        IsField field = components.get(metadata);
        field.reset();
        // if (field instanceof Field) {
        // DomEvent.fireNativeEvent(Document.get().createBlurEvent(),
        // ((HasHandlers) field));
        // }
        if (field instanceof Field) {
            field.finishEditing();
            // ((Field) field).getElement().focus();
            // ((Field) field).getElement().blur();
        }
    
        field.setValue(value.getObject());
    }

    @SuppressWarnings("rawtypes")
    public DataValue getValue(FieldMetadata metadata) {
        if (!components.containsKey(metadata)) {
            return null;
        }

        if (builders.get(metadata) instanceof ListParameter) {
            RowListValue value = ((ListParameter) builders.get(metadata))
                    .getFieldValue();
            return value;
        }

        DataValue value = new DataValueImpl(metadata.getType());
        if (metadata.getType() == DataType.FILE) {
            UploadFieldBuilder builder = uploadBuilders.get(metadata);
            value.setValue(builder.getFileValue());
            // } else if (metadata.getType() == DataType.MULTILIST
            // && builders.get(metadata) instanceof MultiComboBoxBuilder) {
            // value.setValue(((MultiComboBoxBuilder)
            // builders.get(metadata)).getSelection());
        } else {
            IsField field = components.get(metadata);
            value.setValue(field.getValue());
        }
        return value;
    }

    public boolean isChanged(FieldMetadata metadata) {
        return changed.contains(metadata);
    }

    public Set<FieldMetadata> getChanged() {
        return changed;
    }

    public boolean isWasChanged() {
        return wasChanged;
    }

    public void setWasChanged(boolean wasChanged) {
        this.wasChanged = wasChanged;
        if (!wasChanged) {
            changed.clear();
        }
    }

    public boolean isValid() {
        boolean valid = true;
        for (AbstractFieldBuilder b : builders.values()) {
            if (!b.isValid(true)) {
                valid = false;
            }
        }
        return valid;
    }

    public void clearFields() {
        for (IsField<?> field : components.values()) {
            field.clear();
        }
    }

    // Selenium

    @Override
    public Locator getLocatorByElement(Element element) {
        Locator result = null;
        for (AbstractFieldBuilder builder : builders.values()) {
            if (builder instanceof NumberFieldBuilder) {
                result = builder.getLocatorByElement(element);
            }
            if (builder instanceof TextFieldBuilder) {
                result = builder.getLocatorByElement(element);
            }
            if (builder instanceof CheckBoxBuilder) {
                result = builder.getLocatorByElement(element);
            }
            if (builder instanceof DateFieldBuilder) {
                result = builder.getLocatorByElement(element);
            }
            if (builder instanceof ComboBoxBuilder) {
                result = builder.getLocatorByElement(element);
            }
            if (result != null) {
                break;
            }
        }
        return result;
    }

    @Override
    public void fillLocatorDefaults(Locator locator, Element element) {
    }

    @Override
    public Element getElementByLocator(Locator locator) {
        Element result = null;
        for (AbstractFieldBuilder builder : builders.values()) {
            if (builder instanceof NumberFieldBuilder) {
                result = builder.getElementByLocator(locator);
            }
            if (builder instanceof TextFieldBuilder) {
                result = builder.getElementByLocator(locator);
            }
            if (builder instanceof CheckBoxBuilder) {
                result = builder.getElementByLocator(locator);
            }
            if (builder instanceof DateFieldBuilder) {
                result = builder.getElementByLocator(locator);
            }
            if (builder instanceof ComboBoxBuilder) {
                result = builder.getElementByLocator(locator);
            }
            if (result != null) {
                break;
            }
        }
        return result;
    }
}
