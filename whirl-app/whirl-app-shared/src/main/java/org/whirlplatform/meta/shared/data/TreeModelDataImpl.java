package org.whirlplatform.meta.shared.data;


public class TreeModelDataImpl extends ListModelDataImpl implements TreeModelData {

    private String parent;

    private String image;

    private String check;

    private String select;

    private String leaf;

    private String state;


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
    public String getSelect() {
        return select;
    }

    @Override
    public void setSelect(String select) {
        this.select = select;
    }

    @Override
    public String getLeaf() {
        return leaf;
    }

    @Override
    public void setLeaf(String leaf) {
        this.leaf = leaf;
    }

    @Override
    public String getState() {
        return state;
    }

    @Override
    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String getCheck() {
        return check;
    }

    @Override
    public void setCheck(String check) {
        this.check = check;
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
