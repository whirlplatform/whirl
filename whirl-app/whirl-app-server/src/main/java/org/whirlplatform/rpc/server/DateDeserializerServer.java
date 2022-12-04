package org.whirlplatform.rpc.server;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import java.util.Date;
import org.whirlplatform.meta.shared.AppConstant;

@SuppressWarnings("serial")
public class DateDeserializerServer extends StdDeserializer<Date> {

    public DateDeserializerServer() {
        this(null);
    }

    public DateDeserializerServer(Class<Date> t) {
        super(t);
    }

    @Override
    public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return AppConstant.getDateFormatSerialize().parse(p.getText());
    }
}
