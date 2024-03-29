package org.whirlplatform.component.client.grid;

import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import java.util.Map;
import jsinterop.annotations.JsIgnore;
import org.whirlplatform.component.client.combo.ComboBoxBuilder;
import org.whirlplatform.component.client.state.StateScope;
import org.whirlplatform.meta.shared.ClassLoadConfig;
import org.whirlplatform.meta.shared.FieldMetadata;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.data.ListModelData;

class TableComboBoxBuilder extends ComboBoxBuilder {

    private final FieldMetadata tableField;

    public TableComboBoxBuilder(FieldMetadata tableField,
                                Map<String, DataValue> builderProperties) {
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
     * Проверяет, находится ли компонент в скрытом состоянии.
     *
     * @return true, если компонент скрыт
     */
    public boolean isHidden() {
        return super.isHidden();
    }

    /**
     * Устанавливает скрытое состояние компонента.
     *
     * @param hidden true - для скрытия компонента, false - для отображения компонента
     */
    public void setHidden(boolean hidden) {
        super.setHidden(hidden);
    }

    /**
     * Фокусирует компонент.
     */
    public void focus() {
        if (componentInstance == null) {
            return;
        }
        componentInstance.focus();
    }

    /**
     * Проверяет, включен ли компонент.
     *
     * @return true, если компонент включен
     */
    public boolean isEnabled() {
        return super.isEnabled();
    }

    /**
     * Устанавливает включенное состояние компонента.
     *
     * @param enabled true - для включения компонента, false - для отключения компонента
     */
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
    }

    /**
     * Получает маску поля.
     *
     * @return маска поля
     */
    public String getFieldMask() {
        return super.getFieldMask();
    }

    /**
     * Устанавливает маску поля.
     *
     * @param mask новая маска поля
     */
    @Override
    public void setFieldMask(String mask) {
        super.setFieldMask(mask);
    }

    /**
     * Устанавливает статус недействительности для поля с заданным текстом.
     *
     * @param msg сообщение
     */
    @Override
    public void markInvalid(String msg) {
        super.markInvalid(msg);
    }

    /**
     * Очищает статус недействительности для поля.
     */
    @Override
    public void clearInvalid() {
        super.clearInvalid();
    }

    /**
     * Очищает значение поля.
     */
    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void clear() {
        super.clear();
    }

    /**
     * Проверяет, является ли поле валидным.
     *
     * @param invalidate true для признания поля валидным
     * @return true если поле валидно
     */
    public boolean isValid(boolean invalidate) {
        return super.isValid(invalidate);
    }

    /**
     * Проверяет, обязательно ли поле для заполнения.
     *
     * @return true, если обязательно
     */
    @Override
    public boolean isRequired() {
        return super.isRequired();
    }

    /**
     * Устанавливает обязательность для заполнения поля.
     *
     * @param required true, если поле обязательно для заполнения
     */
    @Override
    public void setRequired(boolean required) {
        super.setRequired(required);
    }

    /**
     * Устанавливает значение только для чтения.
     *
     * @param readOnly true, если поле доступно только для чтения
     */
    @Override
    public void setReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly);
    }

    @JsIgnore
    @Override
    public Component create() {
        return super.create();
    }

    /**
     * Проверяет доступность для редактирования.
     *
     * @return true, если доступен для редактирования
     */
    @Override
    public boolean isEditable() {
        return super.isEditable();
    }

    /**
     * Устанавливает доступность для редактирования.
     *
     * @param editable true, доступ для редактирования
     */
    @Override
    public void setEditable(boolean editable) {
        super.setEditable(editable);
    }

    @JsIgnore
    @Override
    public ListModelData getValue() {
        return super.getValue();
    }

    @JsIgnore
    @Override
    public void setValue(ListModelData value) {
        super.setValue(value);
    }

    /**
     * Возвращает текст объекта.
     *
     * @return новый текст объекта
     */
    @Override
    public String getText() {
        return super.getText();
    }

    @JsIgnore
    @Override
    public boolean isSaveState() {
        return super.isSaveState();
    }

    @JsIgnore
    @Override
    public void setSaveState(boolean save) {
        super.setSaveState(save);
    }

    @JsIgnore
    public void setRestoreState(boolean restore) {
        super.setRestoreState(restore);
    }

    @JsIgnore
    @Override
    public StateScope getStateScope() {
        return super.getStateScope();
    }

    @JsIgnore
    @Override
    public void setStateScope(StateScope scope) {
        super.setStateScope(scope);
    }

    @JsIgnore
    @Override
    public void saveState() {
        super.saveState();
    }


}