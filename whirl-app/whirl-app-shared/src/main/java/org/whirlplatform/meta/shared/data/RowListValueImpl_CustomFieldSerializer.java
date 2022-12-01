package org.whirlplatform.meta.shared.data;

import com.google.gwt.user.client.rpc.CustomFieldSerializer;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import java.util.List;

public class RowListValueImpl_CustomFieldSerializer
        extends CustomFieldSerializer<RowListValueImpl> {

    public static void serialize(SerializationStreamWriter streamWriter, RowListValueImpl instance)
            throws SerializationException {
        streamWriter.writeString(instance.getCode());
        streamWriter.writeObject(instance.getRowList());
        streamWriter.writeBoolean(instance.isCheckable());
    }

    @SuppressWarnings("unchecked")
    public static void deserialize(SerializationStreamReader streamReader,
                                   RowListValueImpl instance)
            throws SerializationException {
        instance.setCode(streamReader.readString());
        instance.setRowList((List<RowValue>) streamReader.readObject());
        instance.setCheckable(streamReader.readBoolean());
    }

    @Override
    public void serializeInstance(SerializationStreamWriter streamWriter, RowListValueImpl instance)
            throws SerializationException {
        serialize(streamWriter, instance);
    }

    @Override
    public void deserializeInstance(SerializationStreamReader streamReader,
                                    RowListValueImpl instance)
            throws SerializationException {
        deserialize(streamReader, instance);
    }
}
