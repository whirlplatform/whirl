package org.whirlplatform.meta.shared;

import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.data.RowModelData;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class DataModifyConfig implements Serializable {
	public enum DataModifyType {
		INSERT, UPDATE, DELETE
	}
	
	private DataModifyType type;
	private List<RowModelData> models;
	private Map<String, DataValue> params;
	
	public DataModifyConfig() {
	}
	
	public DataModifyConfig(DataModifyType type, List<RowModelData> models, Map<String, DataValue> params) {
		this.type = type;
		this.models = models;
		this.params = params;
	}
	
	public DataModifyType getType() {
		return type;
	}
	
	public List<RowModelData> getModels() {
		return models;
	}
	
	public Map<String, DataValue> getParams() {
		return params;
	}
}