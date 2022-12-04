package org.whirlplatform.meta.shared;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;

@SuppressWarnings("serial")
public class FileValue implements Serializable, Cloneable {

    private String tempId;

    private String name;

    private boolean saveName = true;

    @JsonIgnore
    private transient Object inputStream;

    private long size;

    public FileValue() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSaveName() {
        return saveName;
    }

    public void setSaveName(boolean save) {
        this.saveName = save;
    }

    public String getTempId() {
        return tempId;
    }

    public void setTempId(String tempId) {
        this.tempId = tempId;
    }

    public Object getInputStream() {
        return inputStream;
    }

    public void setInputStream(Object inputStream) {
        this.inputStream = inputStream;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public FileValue clone() {
        FileValue clone = new FileValue();
        clone.tempId = tempId;
        clone.name = name;
        clone.saveName = saveName;
        clone.size = size;
        clone.inputStream = inputStream;
        return clone;
    }
}
