package com.smart.campus.storage;

import com.smart.campus.model.Room;
import com.smart.campus.model.Sensor;
import com.smart.campus.model.SensorReading;
import java.util.*;

public class DataStore {
    private static final Map<String, Room> rooms = new LinkedHashMap<>();
    private static final Map<String, Sensor> sensors = new LinkedHashMap<>();
    private static final Map<String, List<SensorReading>> readings = new LinkedHashMap<>();

    public static Map<String, Room> getRooms() { return rooms; }
    public static Room getRoom(String id) { return rooms.get(id); }
    public static void putRoom(Room r) { rooms.put(r.getId(), r); }
    public static Room removeRoom(String id) { return rooms.remove(id); }
    public static boolean roomExists(String id) { return rooms.containsKey(id); }

    public static Map<String, Sensor> getSensors() { return sensors; }
    public static Sensor getSensor(String id) { return sensors.get(id); }
    public static void putSensor(Sensor s) { sensors.put(s.getId(), s); }
    public static Sensor removeSensor(String id) { return sensors.remove(id); }
    public static boolean sensorExists(String id) { return sensors.containsKey(id); }

    public static List<SensorReading> getReadings(String sensorId) {
        return readings.computeIfAbsent(sensorId, k -> new ArrayList<>());
    }
    public static void addReading(String sensorId, SensorReading reading) {
        getReadings(sensorId).add(reading);
    }
}