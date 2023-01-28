package org.whirlplatform.meta.shared.data;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeInfo(use = Id.MINIMAL_CLASS, defaultImpl = RowValueImpl.class)
@JsonAutoDetect(
    fieldVisibility = Visibility.ANY,
    getterVisibility = Visibility.NONE,
    isGetterVisibility = Visibility.NONE,
    setterVisibility = Visibility.NONE)
public interface RowValue extends Serializable, Cloneable {

    String getId();

    boolean isSelected();

    void setSelected(boolean selected);

    boolean isChecked();

    void setChecked(boolean checked);

    boolean isExpanded();

    void setExpanded(boolean expanded);

    String getLabel();

    void setLabel(String label);

    RowValue clone();
}
