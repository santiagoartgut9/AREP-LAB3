package edu.escuelaing.app.framework;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dispatcher {
    private final Map<String, Method> routes = new HashMap<>();
    private final Map<String, Object> instances = new HashMap<>();

    public Dispatcher(List<Class<?>> controllers) {
        try {
            for (Class<?> controller : controllers) {
                Object instance = controller.getDeclaredConstructor().newInstance();
                instances.put(controller.getName(), instance);

                for (Method method : controller.getDeclaredMethods()) {
                    if (method.isAnnotationPresent(GetMapping.class)) {
                        String path = method.getAnnotation(GetMapping.class).value();
                        routes.put(path, method);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String dispatch(String path) {
        try {
            Method method = routes.get(path);
            if (method == null) return "404 Not Found";
            Object instance = instances.get(method.getDeclaringClass().getName());
            return (String) method.invoke(instance);
        } catch (Exception e) {
            return "500 Internal Server Error: " + e.getMessage();
        }
    }
}
