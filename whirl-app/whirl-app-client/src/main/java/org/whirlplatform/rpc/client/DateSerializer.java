package org.whirlplatform.rpc.client;

import com.github.nmorel.gwtjackson.client.JsonSerializationContext;
import com.github.nmorel.gwtjackson.client.JsonSerializer;
import com.github.nmorel.gwtjackson.client.JsonSerializerParameters;
import com.github.nmorel.gwtjackson.client.stream.JsonWriter;
import org.whirlplatform.meta.shared.AppConstant;

import java.util.Date;

public class DateSerializer extends JsonSerializer<Date> {

    @Override
    protected void doSerialize(JsonWriter writer, Date value, JsonSerializationContext ctx,
                               JsonSerializerParameters params) {
        if (value == null) {
            writer.value((String) null); // По идее null он не должен писать вообще
        } else {
            writer.value(AppConstant.getDateFormatSerialize().format(value));
        }
    }
}
