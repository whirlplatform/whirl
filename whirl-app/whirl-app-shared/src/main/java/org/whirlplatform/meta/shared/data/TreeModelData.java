package org.whirlplatform.meta.shared.data;

import com.google.gwt.event.logical.shared.ValueChangeHandler;

public interface TreeModelData extends ListModelData {

        String getParent();
        void setParent(String parentId);

        boolean isCheck();
        void setIsCheck(boolean isCheck);

        boolean isSelect();
        void setIsSelect(boolean isSelect);

        boolean isLeaf();
        void setIsLeaf(boolean isLeaf);

        boolean isExpand();
        void setIsExpand(boolean isState);

        String getImage();
        void setImage(String image);

}
