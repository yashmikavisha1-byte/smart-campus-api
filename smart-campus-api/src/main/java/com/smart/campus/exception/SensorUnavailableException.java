package com.smart.campus.exception;

public class SensorUnavailableException extends RuntimeException {

    private final String sensorId;

    public SensorUnavailableException(String sensorId) {
        super("Sensor '" + sensorId + "' is currently under MAINTENANCE and cannot accept readings.");
        this.sensorId = sensorId;
    }

    public String getSensorId() { return sensorId; }
}