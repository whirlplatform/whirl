package org.whirlplatform.editor.shared.metadata;

import java.io.Serializable;
import org.whirlplatform.meta.shared.Version;
import org.whirlplatform.meta.shared.editor.ApplicationElement;
import org.whirlplatform.meta.shared.editor.LocaleElement;
import org.whirlplatform.meta.shared.version.VersionUtil;

public class ApplicationBasicInfo implements Serializable {
    private static final long serialVersionUID = -5896849717828598945L;

    private String name;
    private String title;
    private String code;
    private String url;
    private LocaleElement locale;
    private Version version;

    public ApplicationBasicInfo() {
    }

    public ApplicationBasicInfo(ApplicationElement application, Version version) {
        setCode(application.getCode());
        setName(application.getName());
        setTitle(application.getTitle().toString());
        setVersion(version);
        setLocale(application.getDefaultLocale());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LocaleElement getLocale() {
        return locale;
    }

    public void setLocale(LocaleElement locale) {
        this.locale = locale;
    }

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        setVersion(version.toString());
    }

    public void setVersion(String value) {
        this.version = VersionUtil.createVersion(value);
    }
}
