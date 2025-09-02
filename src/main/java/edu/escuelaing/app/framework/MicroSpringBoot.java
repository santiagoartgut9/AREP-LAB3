package edu.escuelaing.app.framework;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * MicroSpringBoot: escanea recursivamente el classpath a partir del paquete base
 * del mainClass, registra controladores anotados con @RestController y métodos
 * con @GetMapping en WebApp, y finalmente inicia SimpleHttpServer.
 */
public class MicroSpringBoot {

    public static void run(Class<?> mainClass, String[] args) {
        String basePackage = mainClass.getPackageName();            // e.g. "edu.escuelaing.app"
        String path = basePackage.replace('.', '/');                // e.g. "edu/escuelaing/app"
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Enumeration<URL> resources = classLoader.getResources(path);
            List<String> classNames = new ArrayList<>();

            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                String protocol = resource.getProtocol();

                if ("file".equals(protocol)) {
                    // Directorio en filesystem (IDE / target/classes)
                    File directory = new File(resource.toURI());
                    findClassesInDirectory(directory, basePackage, classNames);
                } else if ("jar".equals(protocol)) {
                    // Dentro de un JAR
                    JarURLConnection jarConn = (JarURLConnection) resource.openConnection();
                    JarFile jarFile = jarConn.getJarFile();
                    Enumeration<JarEntry> entries = jarFile.entries();
                    while (entries.hasMoreElements()) {
                        JarEntry entry = entries.nextElement();
                        String name = entry.getName();
                        if (name.endsWith(".class") && name.startsWith(path)) {
                            String className = name.replace('/', '.').replace(".class", "");
                            classNames.add(className);
                        }
                    }
                }
            }

            // Convertir nombres a clases y filtrar controladores
            List<Class<?>> controllers = new ArrayList<>();
            for (String cn : classNames) {
                try {
                    Class<?> clazz = Class.forName(cn);
                    if (clazz.isAnnotationPresent(RestController.class)) {
                        controllers.add(clazz);
                        System.out.println("Controlador encontrado: " + clazz.getName());
                    }
                } catch (Throwable t) {
                    // ignorar clases que no se puedan cargar
                }
            }

            // Registrar controladores encontrados en WebApp
            for (Class<?> clazz : controllers) {
                Object instance = clazz.getDeclaredConstructor().newInstance();
                for (Method m : clazz.getDeclaredMethods()) {
                    if (m.isAnnotationPresent(GetMapping.class)) {
                        String route = m.getAnnotation(GetMapping.class).value();
                        WebApp.get(route, req -> {
                            try {
                                Object[] params = buildMethodArgs(m, req);
                                Object r = m.invoke(instance, params);
                                return r != null ? r.toString() : "";
                            } catch (Exception e) {
                                e.printStackTrace();
                                return "<h1>500 Internal Server Error</h1><pre>" + e.getMessage() + "</pre>";
                            }
                        });
                        System.out.println("Registrada ruta GET " + route + " -> " + clazz.getName() + "#" + m.getName());
                    }
                }
            }

            // Iniciar servidor al final (SimpleHttpServer leerá las rutas ya registradas)
            SimpleHttpServer.start();

        } catch (URISyntaxException ure) {
            ure.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Recorre recursivamente un directorio y agrega clases encontradas
    private static void findClassesInDirectory(File directory, String packageName, List<String> classNames) {
        if (!directory.exists()) return;
        File[] files = directory.listFiles();
        if (files == null) return;
        for (File f : files) {
            if (f.isDirectory()) {
                findClassesInDirectory(f, packageName + "." + f.getName(), classNames);
            } else if (f.getName().endsWith(".class")) {
                String className = packageName + "." + f.getName().substring(0, f.getName().length() - 6);
                classNames.add(className);
            }
        }
    }

    // Construye argumentos para invocar el método a partir de @RequestParam
    private static Object[] buildMethodArgs(Method m, Request req) {
        Parameter[] params = m.getParameters();
        Object[] args = new Object[params.length];
        for (int i = 0; i < params.length; i++) {
            Parameter p = params[i];
            if (p.isAnnotationPresent(RequestParam.class)) {
                RequestParam rp = p.getAnnotation(RequestParam.class);
                String value = req.getParameter(rp.value());
                if (value == null || value.isEmpty()) {
                    value = rp.defaultValue();
                }
                args[i] = value;
            } else {
                args[i] = null;
            }
        }
        return args;
    }
}
