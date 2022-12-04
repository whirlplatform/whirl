package org.whirlplatform.editor.shared;

import java.io.Serializable;
import org.whirlplatform.meta.shared.Version;
import org.whirlplatform.meta.shared.editor.ApplicationElement;

@SuppressWarnings("serial")
public class SaveResult implements Serializable {

    private ApplicationElement application;
    private Version version;
    private TreeState state;

    public SaveResult() {
    }

    public SaveResult(ApplicationElement application, Version version, TreeState state) {
        this.application = application;
        this.version = version;
        this.state = state;
    }

    public ApplicationElement getApplication() {
        return application;
    }

    public void setApplication(ApplicationElement application) {
        this.application = application;
    }

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public TreeState getState() {
        return state;
    }

    public void setState(TreeState state) {
        this.state = state;
    }
}
