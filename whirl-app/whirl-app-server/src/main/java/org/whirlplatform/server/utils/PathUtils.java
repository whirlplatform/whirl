package org.whirlplatform.server.utils;

import org.apache.empire.commons.StringUtils;

import java.io.File;

public class PathUtils {

    public static String getApplicationFilePath(String base,
                                                String platformContext, String applicationCode, String type,
                                                String file) {
        String result;
        if (StringUtils.isEmpty(base)) {
            result = File.separator;
        } else {
            result = base
                    + (base.endsWith(File.separator) ? "" : File.separator);
        }
        result = result + platformContext + File.separator + "applications"
                + File.separator + applicationCode + File.separator + type;

        if (!StringUtils.isEmpty(file)) {
            result = result + File.separator + file;
        } else {
            result = result + File.separator;
        }
        return result;
    }
}
