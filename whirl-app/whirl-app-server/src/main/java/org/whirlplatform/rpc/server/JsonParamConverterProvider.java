package org.whirlplatform.rpc.server;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.rpc.shared.ListHolder;
import org.whirlplatform.server.config.RestApplication;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.*;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.Map;

/**
 * Для сериализации объектов в параметрах запроса отмеченных аннотацией
 * {@link javax.ws.rs.FormParam}.<br/>
 * Регистрируется в {@link RestApplication}.
 *
 * @author lebedev_sv
 */
@Provider
public class JsonParamConverterProvider implements ParamConverterProvider {

    @Context
    private Providers providers;

    @Context
    private ParamConverterProvider defaultProvider;

    @Override
    public <T> ParamConverter<T> getConverter(final Class<T> rawType, final Type genericType,
                                              final Annotation[] annotations) {
        ParamConverter<T> defaultConverter = defaultProvider.getConverter(rawType, genericType, annotations);
        if (defaultConverter != null && !ClassMetadata.class.equals(rawType)) {
            return defaultConverter;
        }

        // Check whether we can convert the given type with Jackson.
        final MessageBodyReader<T> mbr = providers.getMessageBodyReader(rawType, genericType, annotations,
                MediaType.APPLICATION_JSON_TYPE);
        if (mbr == null || !mbr.isReadable(rawType, genericType, annotations, MediaType.APPLICATION_JSON_TYPE)) {
            return null;
        }

        // Obtain custom ObjectMapper for special handling.
        final ContextResolver<ObjectMapper> contextResolver = providers.getContextResolver(ObjectMapper.class,
                MediaType.APPLICATION_JSON_TYPE);

        final ObjectMapper mapper = contextResolver != null ? contextResolver.getContext(rawType) : new ObjectMapper();
        final JavaType mapType;
        if (Map.class.isAssignableFrom(rawType) && genericType instanceof ParameterizedType) {
            Type[] t = ((ParameterizedType) genericType).getActualTypeArguments();
            mapType = mapper.getTypeFactory().constructMapType((Class<? extends Map>) rawType, (Class) t[0],
                    (Class) t[1]);

        } else if (ListHolder.class.isAssignableFrom(rawType) && genericType instanceof ParameterizedType) {
            Type[] t = ((ParameterizedType) genericType).getActualTypeArguments();
            mapType = mapper.getTypeFactory().constructParametricType(rawType, (Class) t[0]);
        } else {
            mapType = null;
        }

        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Date.class, new DateSerializerServer());
        simpleModule.addDeserializer(Date.class, new DateDeserializerServer());
        mapper.registerModule(simpleModule);

        // Create ParamConverter.
        return new ParamConverter<T>() {

            @Override
            public T fromString(final String value) {
                try {
                    if (mapType == null) {
                        return mapper.reader(rawType).readValue(value);
                    } else {
                        return mapper.reader(mapType).readValue(value);
                    }
                } catch (IOException e) {
                    throw new ProcessingException(e);
                }
            }

            @Override
            public String toString(final T value) {
                try {
                    return mapper.writer().writeValueAsString(value);
                } catch (IOException e) {
                    throw new ProcessingException(e);
                }
            }
        };
    }
}
