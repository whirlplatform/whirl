package org.whirlplatform.meta.shared;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, creatorVisibility = Visibility.ANY,
    getterVisibility = Visibility.NONE, isGetterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public class ClassMetadata implements Serializable {

    private String classId;

    private String title;

    private List<FieldMetadata> all = new ArrayList<FieldMetadata>();

    private Map<String, FieldMetadata> map = new HashMap<>();

    private FieldMetadata idField;

    private boolean viewable = false;
    private boolean insertable = false;
    private boolean updatable = false;
    private boolean deletable = false;

    // protected ModelType modelType;

    private boolean loadAll;

    @SuppressWarnings("unused")
    private ClassMetadata() {
    }

    public ClassMetadata(String classId) {
        this.classId = classId;
    }

    public String getClassId() {
        return classId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<FieldMetadata> getFields() {
        return Collections.unmodifiableList(all);
    }

    public FieldMetadata getField(String field) {
        return map.get(FieldMetadata.getNativeFieldName(field));
    }

    public void addField(FieldMetadata field) {
        if (field == null) {
            throw new IllegalArgumentException("Field can not be null");
        }
        all.add(field);
        map.put(field.getName(), field);
    }

    public boolean isViewable() {
        return viewable;
    }

    public void setViewable(boolean viewable) {
        this.viewable = viewable;
    }

    public boolean isInsertable() {
        return insertable;
    }

    public void setInsertable(boolean insertable) {
        this.insertable = insertable;
    }

    public boolean isUpdatable() {
        return updatable;
    }

    public void setUpdatable(boolean updatable) {
        this.updatable = updatable;
    }

    public boolean isDeletable() {
        return deletable;
    }

    public void setDeletable(boolean deletable) {
        this.deletable = deletable;
    }

    public boolean isLoadAll() {
        return loadAll;
    }

    public void setLoadAll(boolean loadAll) {
        this.loadAll = loadAll;
    }

    public boolean hasField(String field) {
        return map.containsKey(FieldMetadata.getNativeFieldName(field));
    }

    public FieldMetadata getIdField() {
        return idField;
    }

    public void setIdField(FieldMetadata idField) {
        this.idField = idField;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((classId == null) ? 0 : classId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ClassMetadata other = (ClassMetadata) obj;
        if (classId == null) {
            return other.classId == null;
        } else {
            return classId.equals(other.classId);
        }
    }

}
