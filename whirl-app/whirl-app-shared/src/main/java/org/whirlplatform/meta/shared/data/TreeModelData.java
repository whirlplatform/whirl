package org.whirlplatform.meta.shared.data;

public interface TreeModelData extends ListModelData {
    String getParent();

    void setParent(String parentId);

    String getImage();

    void setImage(String image);

    boolean isIsCheck();

    void setIsCheck(boolean check);

    boolean isIsSelect();

    void setIsSelect(boolean select);

    // boolean isIsLeaf();
    // void setIsLeaf(boolean leaf);

    boolean isIsExpand();

    void setIsExpand(boolean state);


}
