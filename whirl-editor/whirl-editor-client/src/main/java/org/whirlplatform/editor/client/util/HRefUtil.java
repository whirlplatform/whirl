package org.whirlplatform.editor.client.util;

import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.user.client.Window;
import org.whirlplatform.meta.shared.ApplicationStoreData;
import org.whirlplatform.meta.shared.Version;

public class HRefUtil {

    public static String createApplicationHRef(final String appcode, final Version version) {
        UrlBuilder builder = Window.Location.createUrlBuilder();
        builder.setProtocol("https");
        builder.removeParameter("gwt.codesvr");
        final String editorPath = Window.Location.getPath();
        int position = editorPath.indexOf("-editor");
        if (position > 0) {
            final String whirlPath = editorPath.substring(0, position);
            builder.setPath(whirlPath + "/app");
        }
        builder.setParameter("application", appcode);
        if (version != null) {
            final String versionType = (version.isBranch()) ? "branch" : "version";
            builder.setParameter(versionType, version.toString());
        }
        return builder.buildString();
    }

    public static String createApplicationHRef(final ApplicationStoreData data) {
        return createApplicationHRef(data.getCode(), data.getVersion());
    }

    public static void openNewApplicationTab(final ApplicationStoreData data) {
        final String url = createApplicationHRef(data);
        Window.open(url, "_blank", "");
    }

    public static void openNewApplicationTab(final String appcode, final Version version) {
        final String url = createApplicationHRef(appcode, version);
        Window.open(url, "_blank", "");
    }
}
