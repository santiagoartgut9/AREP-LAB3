package edu.escuelaing.app.framework;

public class WebApp {

    @FunctionalInterface
    public interface RequestHandler {
        String handle(Request req);
    }

    @FunctionalInterface
    public interface SimpleHandler {
        String handle();
    }

    public static void get(String path, RequestHandler handler) {
        SimpleHttpServer.addRoute(path, handler);
    }

    public static void get(String path, SimpleHandler handler) {
        SimpleHttpServer.addRoute(path, req -> handler.handle());
    }
}
