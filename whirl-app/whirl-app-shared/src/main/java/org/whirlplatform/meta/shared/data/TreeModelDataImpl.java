package org.whirlplatform.meta.shared.data;


public class TreeModelDataImpl extends ListModelDataImpl implements TreeModelData {

    private String parent;

    private String image;

    private boolean isCheck;

    private boolean isSelect;

    private boolean isLeaf;

    private boolean isExpand;


    public TreeModelDataImpl() {
    }

    @Override
    public String getParent() {
        return parent;
    }

    @Override
    public void setParent(String parentId) {
        this.parent = parentId;
    }

    @Override
    public boolean isCheck() {
        return isCheck;
    }


    @Override
    public void setIsCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

    @Override
    public boolean isSelect() {
        return isSelect;
    }

    @Override
    public void setIsSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }

    @Override
    public boolean isLeaf() {
        return isLeaf;
    }

    @Override
    public void setIsLeaf(boolean isLeaf) {
        this.isLeaf = isLeaf;
    }

    @Override
    public boolean isExpand() {
        return isExpand;
    }

    @Override
    public void setIsExpand(boolean isExpand) {
    this.isExpand = isExpand;
    }


    @Override
    public String getImage() {
        return image;
    }

    @Override
    public void setImage(String image) {
        this.image = image;
    }

}
