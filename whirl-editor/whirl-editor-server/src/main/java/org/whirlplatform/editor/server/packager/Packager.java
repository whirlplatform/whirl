package org.whirlplatform.editor.server.packager;

import java.util.zip.ZipOutputStream;
import org.whirlplatform.meta.shared.Version;
import org.whirlplatform.meta.shared.editor.ApplicationElement;

public interface Packager {

    String SESSION_KEY = "pacakge";

    void make(ApplicationElement application, Version version, ZipOutputStream ouput);

}
