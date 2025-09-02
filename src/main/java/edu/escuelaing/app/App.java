package edu.escuelaing.app;

import edu.escuelaing.app.framework.*;

public class App {

    public static void main(String[] args) {
        // Define carpeta de archivos estáticos (index.html, styles.css, images/...)
        SimpleHttpServer.staticfiles("src/main/resources/public");

        // Ejemplo de endpoint manual: /pi
        WebApp.get("/pi", req -> String.valueOf(Math.PI));

        // Ejecutar el scanner automático a partir del paquete de App (escanea subpaquetes también)
        // MicroSpringBoot.run() registrará las rutas anotadas y al final iniciará el servidor
        MicroSpringBoot.run(App.class, args);
    }

    // (opcional - si necesitas usarlo en handlers manuales)
    private static String escapeHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }
}
