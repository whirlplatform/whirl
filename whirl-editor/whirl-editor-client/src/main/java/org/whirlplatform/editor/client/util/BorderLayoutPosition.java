package org.whirlplatform.editor.client.util;

public class BorderLayoutPosition implements LayoutPosition {

    private String location;

    public BorderLayoutPosition(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
