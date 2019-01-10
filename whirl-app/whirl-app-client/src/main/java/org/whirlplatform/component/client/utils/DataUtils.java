package org.whirlplatform.component.client.utils;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

public class DataUtils {

    public static SafeHtml notNull(String string, String def) {
        return SafeHtmlUtils.fromTrustedString(string == null ? def : string);
    }

}
