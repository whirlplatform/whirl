package org.whirlplatform.meta.shared;

import java.io.Serializable;
import lombok.Data;
import org.whirlplatform.meta.shared.editor.ApplicationElement;
import org.whirlplatform.meta.shared.version.VersionUtil;

@SuppressWarnings("serial")
@Data
public class ApplicationStoreData implements Serializable {
    private String id;
    private String code;
    private String name;
    private Version version;
    private long modified;

    public ApplicationStoreData() {
    }

    public ApplicationStoreData(final String id, final String name) {
        this.id = id;
        this.name = name;
    }

    public ApplicationStoreData(final ApplicationElement application, final Version version) {
        this.id = application.getId();
        this.name = application.getName();
        this.code = application.getCode();
        this.version = VersionUtil.createVersion(version);
    }

    public ApplicationStoreData(final ApplicationStoreData data) {
        this.id = new String(data.id);
        this.name = new String(data.name);
        this.code = new String(data.code);
        this.version = VersionUtil.createVersion(data.version);
        this.modified = data.modified;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Version getVersion() {
        return version;
    }

    public void setVersion(final Version version) {
        this.version = version;
    }

    public long getModified() {
        return modified;
    }

    public void setModified(long modified) {
        this.modified = modified;
    }
}
