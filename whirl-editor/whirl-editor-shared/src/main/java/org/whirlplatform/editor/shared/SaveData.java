package org.whirlplatform.editor.shared;

import org.whirlplatform.meta.shared.Version;
import org.whirlplatform.meta.shared.editor.ApplicationElement;

import java.io.Serializable;

@SuppressWarnings("serial")
public class SaveData implements Serializable {

    private ApplicationElement application;
    private Version version;
    private TreeState state;

    public SaveData() {
    }

    public void setApplication(ApplicationElement application) {
        this.application = application;
    }

    public ApplicationElement getApplication() {
        return application;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public Version getVersion() {
        return version;
    }

    public void setState(TreeState state) {
        this.state = state;
    }

    public TreeState getState() {
        return state;
    }

}
