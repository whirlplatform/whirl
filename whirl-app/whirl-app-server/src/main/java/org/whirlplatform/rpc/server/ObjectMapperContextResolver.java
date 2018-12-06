package org.whirlplatform.rpc.server;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import org.whirlplatform.meta.shared.AppConstant;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import java.text.SimpleDateFormat;

@Provider
public class ObjectMapperContextResolver implements ContextResolver<ObjectMapper> {

    private ObjectMapper mapper = new ObjectMapper();

    public ObjectMapperContextResolver() {
        SerializationConfig serConfig = mapper.getSerializationConfig();
        serConfig.with(new SimpleDateFormat(AppConstant.DATE_FORMAT_SERIALIZE));
        DeserializationConfig deserializationConfig = mapper.getDeserializationConfig();
        deserializationConfig.with(new SimpleDateFormat(AppConstant.DATE_FORMAT_SERIALIZE));
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return mapper;
    }
}
