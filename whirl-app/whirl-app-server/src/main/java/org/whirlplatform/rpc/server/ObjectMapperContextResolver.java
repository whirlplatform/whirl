package org.whirlplatform.rpc.server;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.text.SimpleDateFormat;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import org.whirlplatform.meta.shared.AppConstant;

@Provider
public class ObjectMapperContextResolver implements ContextResolver<ObjectMapper> {

    private ObjectMapper mapper = new ObjectMapper();

    public ObjectMapperContextResolver() {
        SerializationConfig serConfig = mapper.getSerializationConfig();
        serConfig.with(new SimpleDateFormat(AppConstant.DATE_FORMAT_SERIALIZE));
        DeserializationConfig deserializationConfig = mapper.getDeserializationConfig();
        deserializationConfig.with(new SimpleDateFormat(AppConstant.DATE_FORMAT_SERIALIZE));

        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false);
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return mapper;
    }
}
