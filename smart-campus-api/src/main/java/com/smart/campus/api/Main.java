package com.smart.campus.api;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.jackson.JacksonFeature;
import java.io.IOException;
import java.net.URI;
import java.util.logging.Logger;

public class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    public static final String BASE_URI = "http://localhost:8080/";

    public static HttpServer startServer() {
        final ResourceConfig config = new ResourceConfig()
                .packages("com.smart.campus")
                .register(JacksonFeature.class);
        return GrizzlyHttpServerFactory.createHttpServer(
                URI.create(BASE_URI), config);
    }

    public static void main(String[] args) throws IOException {
        final HttpServer server = startServer();
        LOGGER.info("Smart Campus API started!");
        LOGGER.info("Base URL: " + BASE_URI);
        System.out.println("Press ENTER to stop server...");
        System.in.read();
        server.shutdownNow();
    }
}