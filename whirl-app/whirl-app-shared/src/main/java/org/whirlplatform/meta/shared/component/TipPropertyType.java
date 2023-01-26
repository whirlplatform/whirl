package org.whirlplatform.meta.shared.component;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class TipPropertyType implements Serializable {
    private String title;
    private String body;

    public TipPropertyType(String title, String body){
        this.title = title;
        this.body = body;
    }

    public TipPropertyType() {
    }

    static Map<String, String> mapHTMLFiles = new HashMap<String, String>();

    public Map<String, String> getMapHTMLFiles() {
        return mapHTMLFiles;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }
}
