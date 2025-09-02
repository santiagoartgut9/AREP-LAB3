package edu.escuelaing.app.framework;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.util.*;

public class SimpleHttpServer {

    private static final int PORT = 35000;
    private static String STATIC_ROOT = "src/main/resources/public";
    private static Map<String, WebApp.RequestHandler> routes = new HashMap<>();

    public static void setStaticRoot(String root) {
        STATIC_ROOT = root;
    }

    public static void staticfiles(String root) {
        setStaticRoot(root);
    }

    public static void addRoute(String path, WebApp.RequestHandler handler) {
        routes.put(path, handler);
    }

    public static void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Servidor escuchando en http://localhost:" + PORT);
        while (true) {
            try (Socket clientSocket = serverSocket.accept()) {
                handleRequest(clientSocket);
            } catch (Exception e) {
                System.err.println("Error manejando petición: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private static void handleRequest(Socket clientSocket) throws IOException {
        InputStream input = clientSocket.getInputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(input));
        OutputStream out = clientSocket.getOutputStream();

        String requestLine = in.readLine();
        if (requestLine == null || requestLine.isEmpty()) {
            clientSocket.close();
            return;
        }

        String[] parts = requestLine.split(" ");
        if (parts.length < 2) {
            writeResponse(out, 400, "Bad Request", "text/html", "<h1>400 Bad Request</h1>".getBytes());
            return;
        }

        String method = parts[0];
        String fullPath = parts[1];

        // Consumir headers
        String header;
        while ((header = in.readLine()) != null && !header.isEmpty()) {}

        String path = fullPath;
        Map<String,String> params = new HashMap<>();
        int qIdx = fullPath.indexOf('?');
        if (qIdx >= 0) {
            path = fullPath.substring(0, qIdx);
            String qs = fullPath.substring(qIdx + 1);
            for (String pair : qs.split("&")) {
                if (pair.isEmpty()) continue;
                String[] kv = pair.split("=", 2);
                String k = URLDecoder.decode(kv[0], "UTF-8");
                String v = kv.length > 1 ? URLDecoder.decode(kv[1], "UTF-8") : "";
                params.put(k, v);
            }
        }

        System.out.println("Solicitud: " + method + " " + fullPath);

        Request req = new Request(method, path, params);

        if (routes.containsKey(path)) {
            try {
                String responseBody = routes.get(path).handle(req);
                byte[] bodyBytes = responseBody.getBytes("UTF-8");
                writeResponse(out, 200, "OK", "text/html; charset=UTF-8", bodyBytes);
            } catch (Exception e) {
                String err = "<h1>500 Internal Server Error</h1><pre>" + e.getMessage() + "</pre>";
                writeResponse(out, 500, "Internal Server Error", "text/html", err.getBytes());
            }
        } else {
            serveStaticFile(path, out);
        }
        out.flush();
    }

    private static void serveStaticFile(String path, OutputStream out) throws IOException {
        if (path.equals("/")) path = "/index.html";
        File file = new File(STATIC_ROOT + path);

        if (file.exists() && file.isFile()) {
            String mimeType = Files.probeContentType(file.toPath());
            if (mimeType == null) mimeType = "application/octet-stream";
            byte[] fileBytes = Files.readAllBytes(file.toPath());
            writeResponse(out, 200, "OK", mimeType, fileBytes);
        } else {
            String body = "<h1>404 Not Found</h1>";
            writeResponse(out, 404, "Not Found", "text/html", body.getBytes());
        }
    }

    private static void writeResponse(OutputStream out, int statusCode, String statusText, String contentType, byte[] body) throws IOException {
        PrintWriter pw = new PrintWriter(out);
        pw.print("HTTP/1.1 " + statusCode + " " + statusText + "\r\n");
        pw.print("Content-Type: " + contentType + "\r\n");
        pw.print("Content-Length: " + body.length + "\r\n");
        pw.print("\r\n");
        pw.flush();
        out.write(body);
    }
}
