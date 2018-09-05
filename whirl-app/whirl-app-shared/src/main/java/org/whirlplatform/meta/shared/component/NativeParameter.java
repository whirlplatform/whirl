package org.whirlplatform.meta.shared.component;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

/**
 * Все билдеры реализующие этот интерфейс создают компоненты с которых в
 * дальнейшем возможно собирать параметры.
 * 
 * @author semenov_pa
 * 
 */
@JsonTypeInfo(use = Id.MINIMAL_CLASS)
public interface NativeParameter<T> {

	T getValue();

	void setValue(T value);

}
