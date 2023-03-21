package org.whirlplatform.meta.shared.data;


public class TreeModelDataImpl extends ListModelDataImpl implements TreeModelData {

    private String parent;

    private String image;

    private boolean check;

    private boolean select;

    private boolean leaf;

    private boolean expand;


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
        return check;
    }

    @Override
    public void setCheck(boolean check) {
        this.check = check;
    }

    @Override
    public boolean isSelect() {
        return select;
    }

    @Override
    public void setSelect(boolean select) {
        this.select = select;
    }

    @Override
    public boolean isLeaf() {
        return leaf;
    }

    @Override
    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

    @Override
    public boolean isExpand() {
        return expand;
    }

    @Override
    public void setExpand(boolean state) {
        this.expand = state;
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
