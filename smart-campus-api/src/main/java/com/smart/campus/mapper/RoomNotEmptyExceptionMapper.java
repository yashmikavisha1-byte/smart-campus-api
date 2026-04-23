package com.smart.campus.mapper;

import com.smart.campus.exception.RoomNotEmptyException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.LinkedHashMap;
import java.util.Map;

@Provider
public class RoomNotEmptyExceptionMapper
        implements ExceptionMapper<RoomNotEmptyException> {

    @Override
    public Response toResponse(RoomNotEmptyException ex) {
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("status", 409);
        error.put("error", "Conflict");
        error.put("message", ex.getMessage());
        error.put("hint", "Remove all sensors from the room before deleting it.");

        return Response.status(Response.Status.CONFLICT)
                .type(MediaType.APPLICATION_JSON)
                .entity(error)
                .build();
    }
}