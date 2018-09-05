package org.whirlplatform.meta.shared.data;

@SuppressWarnings("serial")
public class EventParameterImpl implements EventParameter {

    protected ParameterType type;
    protected String componentId;
    protected String componentCode;
    private String storageCode;
    private String code;
    private DataValue data;

    public EventParameterImpl() {
    }

    public EventParameterImpl(ParameterType type) {
        this.type = type;
    }

    public ParameterType getType() {
        return type;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentCode(String componentCode) {
        this.componentCode = componentCode;
    }

    public String getComponentCode() {
        return componentCode;
    }

    @Override
    public String getStorageCode() {
        return storageCode;
    }

    @Override
    public void setStorageCode(String storageCode) {
        this.storageCode = storageCode;
    }

    public void setData(DataValue data) {
        this.data = data;
    }

    public DataValue getData() {
        return data;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((code == null) ? 0 : code.hashCode());
        result = prime * result + ((componentCode == null) ? 0 : componentCode.hashCode());
        result = prime * result + ((componentId == null) ? 0 : componentId.hashCode());
        result = prime * result + ((data == null) ? 0 : data.hashCode());
        result = prime * result + ((storageCode == null) ? 0 : storageCode.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof EventParameterImpl)) {
            return false;
        }
        EventParameterImpl other = (EventParameterImpl) obj;
        if (code == null) {
            if (other.code != null) {
                return false;
            }
        } else if (!code.equals(other.code)) {
            return false;
        }
        if (componentCode == null) {
            if (other.componentCode != null) {
                return false;
            }
        } else if (!componentCode.equals(other.componentCode)) {
            return false;
        }
        if (componentId == null) {
            if (other.componentId != null) {
                return false;
            }
        } else if (!componentId.equals(other.componentId)) {
            return false;
        }
        if (data == null) {
            if (other.data != null) {
                return false;
            }
        } else if (!data.equals(other.data)) {
            return false;
        }
        if (storageCode == null) {
            if (other.storageCode != null) {
                return false;
            }
        } else if (!storageCode.equals(other.storageCode)) {
            return false;
        }
        return type == other.type;
    }
}
