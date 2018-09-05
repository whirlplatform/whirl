package org.whirlplatform.editor.client.util;

import com.google.gwt.core.shared.GWT;

public class Random {

    public static long nextLong() {
        if (GWT.isScript()) {
            return com.google.gwt.user.client.Random.nextInt();
        } else {
            java.util.Random random = new java.util.Random();
            return random.nextLong();
        }
    }

}
