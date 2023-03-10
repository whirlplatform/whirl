package org.whirlplatform.meta.shared.data;

public interface TreeModelData {

        String getParent();
        void setParent(String parentId);

        String getSelect();
        void setSelect(String select);

        String getLeaf();
        void setLeaf(String leaf);

        String getState();
        void setState(String state);

        String getCheck();
        void setCheck(String check);

        String getImage();
        void setImage(String image);

}
