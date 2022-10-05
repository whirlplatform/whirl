package org.whirlplatform.component.client.grid;

import com.sencha.gxt.widget.core.client.Component;
import org.whirlplatform.component.client.combo.TreeComboBoxBuilder;
import org.whirlplatform.component.client.state.StateScope;
import org.whirlplatform.meta.shared.ClassLoadConfig;
import org.whirlplatform.meta.shared.FieldMetadata;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.data.ListModelData;

import java.util.Map;


class TableTreeComboBoxBuilder extends TreeComboBoxBuilder {

    private FieldMetadata tableField;


    public TableTreeComboBoxBuilder(FieldMetadata tableField, Map<String, DataValue> builderProperties) {
        super(builderProperties);
        this.tableField = tableField;
    }

    @Override
    protected ClassLoadConfig getLoadConfig(boolean dontUseQuery) {
        ClassLoadConfig config = super.getLoadConfig(dontUseQuery);
        config.setTableField(tableField);
        return config;
    }

    /**
     * Checks if component is in hidden state.
     *
     * @return true if component is hidden
     */
    public boolean isHidden() {
        return super.isHidden();
    }

    /**
     * Sets component's hidden state.
     *
     * @param hidden true - to hide component, false - to show component
     */
    public void setHidden(boolean hidden) {
        super.setHidden(hidden);
    }

    /**
     * Focuses component.
     */
    public void focus() {
        if (componentInstance == null) {
            return;
        }
        componentInstance.focus();
    }

    /**
     * Checks if component is enabled.
     *
     * @return true if component is enabled
     */
    public boolean isEnabled() {
        return super.isEnabled();
    }

    /**
     * Sets component's enabled state.
     *
     * @param enabled true - to enable component, false - to disable component
     */
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
    }

    @Override
    public Component create() {
        Component c = super.create();
        return c;
    }

    @Override
    public boolean isValid(boolean invalidate) {
        return super.isValid(invalidate);
    }

    @Override
    public void clear() {
        super.clear();
    }

    @Override
    public boolean isEditable() {
        return super.isEditable();
    }

    @Override
    public void setEditable(boolean editable) {
        super.setEditable(editable);
    }

    @Override
    public boolean isSaveState() {
        return super.isSaveState();
    }

    @Override
    public void setSaveState(boolean save) {
        super.setSaveState(save);
    }

    public void setRestoreState(boolean restore) {
        super.setRestoreState(restore);
    }

    @Override
    public StateScope getStateScope() {
        return super.getStateScope();
    }

    @Override
    public void setStateScope(StateScope scope) {
        super.setStateScope(scope);
    }

    @Override
    public void saveState() {
        super.saveState();
    }

    /**
     * Gets the field mask.
     *
     * @return the field mask
     */
    public String getFieldMask() {
        return super.getFieldMask();
    }

    /**
     * Sets the field mask.
     *
     * @param mask the new field mask
     */
    public void setFieldMask(String mask) {
        super.setFieldMask(mask);
    }

    /**
     * Sets the invalid status for the field with given text.
     *
     * @param msg message
     */
    @Override
    public void markInvalid(String msg) {
        super.markInvalid(msg);
    }

    /**
     * Clears the invalid status for the field.
     */
    @Override
    public void clearInvalid() {
        super.clearInvalid();
    }

    /**
     * Checks if is required.
     *
     * @return true, if is required
     */
    @Override
    public boolean isRequired() {
        return super.isRequired();
    }

    /**
     * Sets the required to fill.
     *
     * @param required true, if the field is required to be filled
     */
    @Override
    public void setRequired(boolean required) {
        super.setRequired(required);
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly);
    }

    @Override
    public void setValue(ListModelData value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListModelData getValue() {
        throw new UnsupportedOperationException();
    }

    public String getText() {
        return comboBox.getText();
    }


}
