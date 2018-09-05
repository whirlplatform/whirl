package org.whirlplatform.component.client;

import org.whirlplatform.meta.shared.data.RowListValue;


/**
 * Реализация этого интерфейса говорит о том, что собираемый параметр список
 * значений.
 *
 * @author semenov_pa
 */
public interface ListParameter<T extends RowListValue> {

    T getFieldValue();

    void setFieldValue(T value);

}
