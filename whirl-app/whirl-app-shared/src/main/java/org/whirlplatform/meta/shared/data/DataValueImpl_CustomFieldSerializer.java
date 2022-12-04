package org.whirlplatform.meta.shared.data;

import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.i18n.shared.DefaultDateTimeFormatInfo;
import com.google.gwt.user.client.rpc.CustomFieldSerializer;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

public class DataValueImpl_CustomFieldSerializer extends CustomFieldSerializer<DataValueImpl> {

    // с сервера получаем время со смещением
    private static final DateTimeFormat dateFormatter;

    static {
        DefaultDateTimeFormatInfo dateFormatInfo = new DefaultDateTimeFormatInfo();
        dateFormatter = new DateTimeFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", dateFormatInfo) {
        };
    }

    private static DateTimeFormat formatterSerialize() {
        return dateFormatter;
    }

    public static void serialize(SerializationStreamWriter streamWriter, DataValueImpl instance)
            throws SerializationException {
        streamWriter.writeObject(instance.getType());
        if (instance.getType() == DataType.DATE) {
            if (instance.getDate() == null) {
                streamWriter.writeString(null);
            } else {
                streamWriter.writeString(formatterSerialize().format(instance.getDate()));
            }
        } else {
            streamWriter.writeObject(instance.getObject());
        }
        streamWriter.writeString(instance.getCode());
    }

    private static DateTimeFormat formatterDeserialize() {
        return dateFormatter;
    }

    public static void deserialize(SerializationStreamReader streamReader, DataValueImpl instance)
            throws SerializationException {
        instance.setType((DataType) streamReader.readObject());
        if (instance.getType() == DataType.DATE) {
            String dateString = streamReader.readString();
            if (dateString != null) {
                instance.setValue(formatterDeserialize().parse(dateString));
            }
        } else {
            instance.setValue(streamReader.readObject());
        }
        instance.setCode(streamReader.readString());
    }

    @Override
    public void serializeInstance(SerializationStreamWriter streamWriter, DataValueImpl instance)
            throws SerializationException {
        serialize(streamWriter, instance);
    }

    @Override
    public void deserializeInstance(SerializationStreamReader streamReader, DataValueImpl instance)
            throws SerializationException {
        deserialize(streamReader, instance);
    }
}
