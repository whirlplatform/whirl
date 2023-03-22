package org.whirlplatform.meta.shared.data;


public class TreeModelDataImpl extends ListModelDataImpl implements TreeModelData {

    private String parent;

    private String image;

    private boolean isCheck;

    private boolean isSelect;

    private boolean isExpand;

    //private boolean isLeaf;


    public TreeModelDataImpl() {
    }

    @Override
    public String getParent() {
        return parent;
    }

    @Override
    public void setParent(String parent) {
        this.parent = parent;
    }

    @Override
    public String getImage() {
        return image;
    }

    @Override
    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public boolean isIsCheck() {
        return isCheck;
    }

    @Override
    public void setIsCheck(boolean check) {
        isCheck = check;
    }

    @Override
    public boolean isIsSelect() {
        return isSelect;
    }

    @Override
    public void setIsSelect(boolean select) {
        isSelect = select;
    }

    //    @Override
    //    public boolean isIsLeaf() {
    //        return isLeaf;
    //    }
    //
    //    @Override
    //    public void setIsLeaf(boolean newLeaf) {
    //        isLeaf = newLeaf;
    //    }

    @Override
    public boolean isIsExpand() {
        return isExpand;
    }

    @Override
    public void setIsExpand(boolean expand) {
        isExpand = expand;
    }
}
