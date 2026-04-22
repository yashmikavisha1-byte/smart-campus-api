package com.smart.campus.resource;

import com.smart.campus.exception.RoomNotEmptyException;
import com.smart.campus.model.Room;
import com.smart.campus.storage.DataStore;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {

    // GET /api/v1/rooms - get all rooms
    @GET
    public Response getAllRooms() {
        List<Room> roomList = new ArrayList<>(DataStore.getRooms().values());
        return Response.ok(roomList).build();
    }

    // POST /api/v1/rooms - create a room
    @POST
    public Response createRoom(Room room) {
        if (room == null || room.getId() == null || room.getId().isBlank()) {
            return Response.status(400)
                    .entity(error(400, "Room id is required."))
                    .build();
        }
        if (DataStore.roomExists(room.getId())) {
            return Response.status(409)
                    .entity(error(409, "Room '" + room.getId() + "' already exists."))
                    .build();
        }
        DataStore.putRoom(room);
        return Response.status(201).entity(room).build();
    }

    // GET /api/v1/rooms/{roomId} - get one room
    @GET
    @Path("/{roomId}")
    public Response getRoom(@PathParam("roomId") String roomId) {
        Room room = DataStore.getRoom(roomId);
        if (room == null) {
            return Response.status(404)
                    .entity(error(404, "Room '" + roomId + "' not found."))
                    .build();
        }
        return Response.ok(room).build();
    }

    // DELETE /api/v1/rooms/{roomId} - delete room
    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Room room = DataStore.getRoom(roomId);
        if (room == null) {
            return Response.status(404)
                    .entity(error(404, "Room '" + roomId + "' not found."))
                    .build();
        }
        if (!room.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException(roomId);
        }
        DataStore.removeRoom(roomId);
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", "Room '" + roomId + "' successfully deleted.");
        return Response.ok(body).build();
    }

    private Map<String, Object> error(int status, String message) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("status", status);
        map.put("message", message);
        return map;
    }
}