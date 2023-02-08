package org.whirlplatform.meta.shared.data;

public interface ListModelData extends RowModelData {

    static org.whirlplatform.meta.shared.data.ListModelData fromRowModelData(
        org.whirlplatform.meta.shared.data.RowModelData model) {
        ListModelData result = new ListModelDataImpl();
        result.setId(model.getId());
        result.setDeletable(model.isDeletable());
        result.setEditable(model.isEditable());
        for (String property : model.getPropertyNames()) {
            result.setValue(property, model.getValue(property));
            if (model.getStyle(property) != null) {
                result.setStyle(property, model.getStyle(property));
            }
        }
        return result;
    }

    String getLabel();

    void setLabel(String label);

    @Override
    ListModelData clone();

}
