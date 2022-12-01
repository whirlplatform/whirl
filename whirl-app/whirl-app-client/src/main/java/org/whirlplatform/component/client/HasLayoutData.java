package org.whirlplatform.component.client;

import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import org.whirlplatform.component.client.form.GridLayoutData;

public interface HasLayoutData {

    BorderLayoutData getBorderLayoutData();

    GridLayoutData getGridLayoutData();

    HorizontalLayoutData getHorizontalLayoutData();

    VerticalLayoutData getVerticalLayoutData();

    BoxLayoutData getBoxLayoutData();

    boolean isNorth();

    boolean isWest();

    boolean isSouth();

    boolean isEast();

    boolean isCenter();

    int getRowPosition();

    int getColumnPosition();

    int getIndexPosition();
}
