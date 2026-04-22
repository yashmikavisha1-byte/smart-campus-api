package com.smart.campus.mapper;

import com.smart.campus.exception.LinkedResourceNotFoundException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.LinkedHashMap;
import java.util.Map;

@Provider
public class LinkedResourceNotFoundExceptionMapper
        implements ExceptionMapper<LinkedResourceNotFoundException> {

    @Override
    public Response toResponse(LinkedResourceNotFoundException ex) {
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("status", 422);
        error.put("error", "Unprocessable Entity");
        error.put("message", ex.getMessage());
        error.put("hint", "Ensure the referenced " + ex.getResourceType()
                + " exists before creating this resource.");

        return Response.status(422)
                .type(MediaType.APPLICATION_JSON)
                .entity(error)
                .build();
    }
}