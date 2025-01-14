package com.example;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import java.util.Set;
import java.util.HashSet;

@ApplicationPath("/api")
public class RestApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();
        resources.add(com.example.filters.JwtFilter.class);
        resources.add(com.example.resources.BookingResource.class);
        resources.add(com.example.resources.AuthResource.class);
        return resources;
    }
}
