package org.whirlplatform.meta.shared.data;

public interface TreeModelData extends ListModelData {

        String getParent();
        void setParent(String parentId);

        boolean isCheck();
        void setCheck(boolean check);

        boolean isSelect();
        void setSelect(boolean select);

        boolean isLeaf();
        void setLeaf(boolean leaf);

        boolean isExpand();
        void setExpand(boolean state);

        String getImage();
        void setImage(String image);

}
