package org.whirlplatform.rpc.client;

import com.github.nmorel.gwtjackson.client.AbstractConfiguration;
import java.util.Date;

public class SerializeConfiguration extends AbstractConfiguration {

    @Override
    protected void configure() {
        type(Date.class).serializer(DateSerializer.class).deserializer(DateDeserializer.class);
    }
}
