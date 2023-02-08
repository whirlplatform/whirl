package org.whirlplatform.rpc.server;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.util.Date;
import org.whirlplatform.meta.shared.AppConstant;

@SuppressWarnings("serial")
public class DateSerializerServer extends StdSerializer<Date> {

    public DateSerializerServer() {
        this(null);
    }

    public DateSerializerServer(Class<Date> t) {
        super(t);
    }

    @Override
    public void serialize(Date value, JsonGenerator gen, SerializerProvider provider)
        throws IOException {
        gen.writeString(AppConstant.getDateFormatSerialize().format(value));
    }
}
