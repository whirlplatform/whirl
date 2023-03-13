package org.whirlplatform.meta.shared.data;


public class TreeModelDataImpl extends ListModelDataImpl implements TreeModelData {

    private String parent;

    private String image;

    private boolean isCheck;

    private boolean isSelect;

    private boolean islLeaf;

    private boolean isState;


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
        return islLeaf;
    }

    @Override
    public void setIsLeaf(boolean isLeaf) {
        this.islLeaf = isLeaf;
    }

    @Override
    public boolean isState() {
        return isState;
    }

    @Override
    public void setState(boolean isState) {
    this.isState = isState;
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
