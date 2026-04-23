package com.smart.campus.api;

import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/api/v1")
public class SmartCampusApp extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(com.smart.campus.resource.DiscoveryResource.class);
        classes.add(com.smart.campus.resource.RoomResource.class);
        classes.add(com.smart.campus.resource.SensorResource.class);
        classes.add(com.smart.campus.mapper.RoomNotEmptyExceptionMapper.class);
        classes.add(com.smart.campus.mapper.LinkedResourceNotFoundExceptionMapper.class);
        classes.add(com.smart.campus.mapper.SensorUnavailableExceptionMapper.class);
        classes.add(com.smart.campus.mapper.GlobalExceptionMapper.class);
        classes.add(com.smart.campus.filter.LoggingFilter.class);
        return classes;
    }
}