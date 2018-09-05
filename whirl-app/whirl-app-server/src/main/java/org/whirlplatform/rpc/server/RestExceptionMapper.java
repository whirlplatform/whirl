package org.whirlplatform.rpc.server;

import org.whirlplatform.rpc.shared.CustomException;
import org.whirlplatform.rpc.shared.ExceptionData;
import org.whirlplatform.server.config.RestApplication;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Для автоматического преобразования исключений {@link CustomException} в корректный json ответ клиенту.<br/>
 * На клиент отправляется сериализованный {@link ExceptionData} из исключения.<br/>
 * Регистрируется в {@link RestApplication}.
 *
 * @author lebedev_sv
 */
@Provider
public class RestExceptionMapper implements ExceptionMapper<CustomException> {

    private static final Logger log = LoggerFactory.getLogger(RestExceptionMapper.class);

    @Override
    public Response toResponse(CustomException exception) {
        log.error(exception.getMessage(), exception);
        return Response.status(400).entity(exception.getData()).type(MediaType.APPLICATION_JSON_TYPE).build();
    }
}
