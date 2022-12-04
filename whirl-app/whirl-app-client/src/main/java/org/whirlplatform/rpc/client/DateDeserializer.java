package org.whirlplatform.rpc.client;

import com.github.nmorel.gwtjackson.client.JsonDeserializationContext;
import com.github.nmorel.gwtjackson.client.JsonDeserializer;
import com.github.nmorel.gwtjackson.client.JsonDeserializerParameters;
import com.github.nmorel.gwtjackson.client.exception.JsonDeserializationException;
import com.github.nmorel.gwtjackson.client.stream.JsonReader;
import java.util.Date;
import org.whirlplatform.meta.shared.AppConstant;

public class DateDeserializer extends JsonDeserializer<Date> {

    @Override
    protected Date doDeserialize(JsonReader reader, JsonDeserializationContext ctx,
                                 JsonDeserializerParameters params) {
        String strDate = reader.nextString();
        if (strDate == null || strDate.isEmpty()) {
            return null;
        } else {
            try {
                return AppConstant.getDateFormatSerialize().parse(strDate);
            } catch (Exception e) {
                throw new JsonDeserializationException(e);
            }
        }
    }
}
