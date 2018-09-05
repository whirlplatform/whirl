package org.whirlplatform.editor.shared;

import org.whirlplatform.meta.shared.Version;
import org.whirlplatform.meta.shared.editor.ApplicationElement;

import java.io.Serializable;

/**
 * @author bedritckiy_mr
 */
public class OpenResult implements Serializable {
    private static final long serialVersionUID = 4600942828011905184L;

    private ApplicationElement application;
    private Version version;

    public OpenResult() {
    }

    public OpenResult(ApplicationElement application, Version version) {
        this.application = application;
        this.version = version;
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
}
