package com.smart.campus.resource;

import com.smart.campus.exception.SensorUnavailableException;
import com.smart.campus.model.Sensor;
import com.smart.campus.model.SensorReading;
import com.smart.campus.storage.DataStore;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {

    private final String sensorId;

    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    // GET /api/v1/sensors/{sensorId}/readings
    @GET
    public Response getReadings() {
        List<SensorReading> history = DataStore.getReadings(sensorId);
        return Response.ok(history).build();
    }

    // POST /api/v1/sensors/{sensorId}/readings
    @POST
    public Response addReading(SensorReading reading) {
        Sensor sensor = DataStore.getSensor(sensorId);

        if ("MAINTENANCE".equalsIgnoreCase(sensor.getStatus())) {
            throw new SensorUnavailableException(sensorId);
        }

        if (reading.getId() == null || reading.getId().isBlank()) {
            reading.setId(UUID.randomUUID().toString());
        }
        if (reading.getTimestamp() == 0) {
            reading.setTimestamp(System.currentTimeMillis());
        }

        DataStore.addReading(sensorId, reading);
        sensor.setCurrentValue(reading.getValue());

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", "Reading recorded successfully.");
        body.put("reading", reading);
        body.put("sensorCurrentValue", sensor.getCurrentValue());

        return Response.status(201).entity(body).build();
    }
}