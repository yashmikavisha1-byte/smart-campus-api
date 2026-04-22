package com.smart.campus.resource;

import com.smart.campus.exception.LinkedResourceNotFoundException;
import com.smart.campus.model.Room;
import com.smart.campus.model.Sensor;
import com.smart.campus.storage.DataStore;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;
import java.util.stream.Collectors;

@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    // GET /api/v1/sensors - get all sensors with optional ?type= filter
    @GET
    public Response getSensors(@QueryParam("type") String type) {
        Collection<Sensor> all = DataStore.getSensors().values();
        List<Sensor> result;
        if (type != null && !type.isBlank()) {
            result = all.stream()
                    .filter(s -> s.getType().equalsIgnoreCase(type))
                    .collect(Collectors.toList());
        } else {
            result = new ArrayList<>(all);
        }
        return Response.ok(result).build();
    }

    // POST /api/v1/sensors - create a sensor
    @POST
    public Response createSensor(Sensor sensor) {
        if (sensor == null || sensor.getId() == null || sensor.getId().isBlank()) {
            return Response.status(400)
                    .entity(error(400, "Sensor id is required."))
                    .build();
        }
        if (DataStore.sensorExists(sensor.getId())) {
            return Response.status(409)
                    .entity(error(409, "Sensor '" + sensor.getId() + "' already exists."))
                    .build();
        }
        // Validate roomId exists
        if (sensor.getRoomId() == null || !DataStore.roomExists(sensor.getRoomId())) {
            throw new LinkedResourceNotFoundException("Room", sensor.getRoomId());
        }
        // Default status to ACTIVE
        if (sensor.getStatus() == null || sensor.getStatus().isBlank()) {
            sensor.setStatus("ACTIVE");
        }
        // Save sensor
        DataStore.putSensor(sensor);
        // Link sensor to room
        Room room = DataStore.getRoom(sensor.getRoomId());
        room.getSensorIds().add(sensor.getId());
        return Response.status(201).entity(sensor).build();
    }

    // GET /api/v1/sensors/{sensorId} - get one sensor
    @GET
    @Path("/{sensorId}")
    public Response getSensor(@PathParam("sensorId") String sensorId) {
        Sensor sensor = DataStore.getSensor(sensorId);
        if (sensor == null) {
            return Response.status(404)
                    .entity(error(404, "Sensor '" + sensorId + "' not found."))
                    .build();
        }
        return Response.ok(sensor).build();
    }

    // DELETE /api/v1/sensors/{sensorId} - delete a sensor
    @DELETE
    @Path("/{sensorId}")
    public Response deleteSensor(@PathParam("sensorId") String sensorId) {
        Sensor sensor = DataStore.getSensor(sensorId);
        if (sensor == null) {
            return Response.status(404)
                    .entity(error(404, "Sensor '" + sensorId + "' not found."))
                    .build();
        }
        // Unlink from room
        Room room = DataStore.getRoom(sensor.getRoomId());
        if (room != null) {
            room.getSensorIds().remove(sensorId);
        }
        DataStore.removeSensor(sensorId);
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", "Sensor '" + sensorId + "' deleted.");
        return Response.ok(body).build();
    }

    // Sub-resource locator for readings - Part 4
    @Path("/{sensorId}/readings")
    public SensorReadingResource getReadingResource(
            @PathParam("sensorId") String sensorId) {
        Sensor sensor = DataStore.getSensor(sensorId);
        if (sensor == null) {
            throw new NotFoundException("Sensor '" + sensorId + "' not found.");
        }
        return new SensorReadingResource(sensorId);
    }

    private Map<String, Object> error(int status, String message) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("status", status);
        map.put("message", message);
        return map;
    }
}