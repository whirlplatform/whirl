package org.whirlplatform.component.client;

public interface Containable {

    void addChild(ComponentBuilder child);

    void removeChild(ComponentBuilder child);

    void clearContainer();

    void forceLayout();

    ComponentBuilder[] getChildren();

    int getChildrenCount();

}
