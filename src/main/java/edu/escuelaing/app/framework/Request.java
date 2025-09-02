package edu.escuelaing.app.framework;

import java.util.Map;
import java.util.HashMap;

public class Request {
    private final String method;
    private final String path;
    private final Map<String,String> params;

    public Request(String method, String path, Map<String,String> params) {
        this.method = method;
        this.path = path;
        this.params = params != null ? params : new HashMap<>();
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getParameter(String name) {
        return params.get(name);
    }

    public Map<String,String> getParameters() {
        return params;
    }
}
