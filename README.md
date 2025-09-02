# 🖥️ AREP - MicroSpringBoot & HTTP Server en Java

## 📌 Project Statement
Desarrollo de un **servidor web tipo Apache** en Java, acompañado de un **mini framework IoC** inspirado en Spring Boot.  
El servidor es capaz de servir archivos estáticos (HTML, CSS, JS, imágenes PNG/JPG) y exponer **servicios REST** a partir de **POJOs anotados**.

Este proyecto explora capacidades reflexivas de Java para construir un framework ligero que soporte:
- Carga dinámica de componentes (`@RestController`).
- Definición de endpoints REST (`@GetMapping`).
- Manejo de parámetros (`@RequestParam`).
- Exploración automática del classpath para registrar controladores.
- Ejecución de aplicaciones web sin necesidad de frameworks externos.

---

## 🎯 Objetivo
- Comprender los principios de la arquitectura cliente-servidor.
- Construir un servidor HTTP básico en Java.
- Implementar un mini-framework IoC usando reflexión.
- Soportar anotaciones tipo Spring para definir servicios REST.
- Servir archivos estáticos junto con contenido dinámico.

---

## 📦 Alcance y Features

### 🔹 Framework IoC
- **@RestController** → marca clases como controladores REST.
- **@GetMapping("/path")** → define rutas GET.
- **@RequestParam** → extrae parámetros de la URL con valores por defecto.

### 🔹 Servidor HTTP
- Maneja **peticiones GET**.
- Sirve archivos estáticos desde `src/main/resources/public`.
- Maneja errores básicos: `404 Not Found`, `500 Internal Server Error`.

### 🔹 Escaneo Automático
- `MicroSpringBoot.run(App.class, args)` explora el classpath y registra automáticamente los controladores anotados.

---

## 📂 Project Structure



```text
.
│   .gitignore
│   pom.xml
│   README.md
│
├───src
│   ├───main
│   │   ├───java
│   │   │   └───edu
│   │   │       └───escuelaing
│   │   │           └───app
│   │   │               │   App.java
│   │   │               │
│   │   │               ├───example
│   │   │               │       GreetingController.java
│   │   │               │       HelloController.java
│   │   │               │
│   │   │               └───framework
│   │   │                       Dispatcher.java
│   │   │                       GetMapping.java
│   │   │                       MicroSpringBoot.java
│   │   │                       Request.java
│   │   │                       RequestParam.java
│   │   │                       RestController.java
│   │   │                       SimpleHttpServer.java
│   │   │                       WebApp.java
│   │   │
│   │   └───resources
│   │       └───public
│   │           │   index.html
│   │           │   styles.css
│   │           │   app.js
│   │           └───images/
│   │                   LOGO.png
│   │                   LOGO2.jpg
│   │                   header-logoescuela.jpg
│   │                   ...
│   └───test
│       └───java
│           └───edu
│               └───escuelaing
│                   └───app
│                           AppTest.java

```

⚙️ Requirements

Java 17+

Maven 3.8+

Git
---
🚀 Execution

Clonar el repositorio:

```bash
git clone https://github.com/santiagoartgut9/AREP-LAB3.git
cd AREP-LAB3

```
Compilar y empaquetar con Maven:
```bash
mvn clean install

```
Ejecutar el servidor:
```bash
mvn exec:java

```

Abrir en el navegador:
```bash
mvn exec:java](http://localhost:35000/


```
---

🌐 Available Endpoints

🔹 GET /hello

```bash
http://localhost:35000/hello


```
---
🔹 GET /greeting?name=Pedro

```bash

http://localhost:35000/greeting?name=Pedro
```
---
🔹 GET /pi (definido manualmente en App.java)
```bash

http://localhost:35000/pi
```
---
🔹 Static Files

```bash
[
http://localhost:35000/pi](http://localhost:35000/index.html
http://localhost:35000/styles.css
http://localhost:35000/app.js
http://localhost:35000/images/LOGO.png
)
```
---
📦 Packaging and Deployment

Generar .jar ejecutable:
```bash
mvn package
```
Ejecutar con:
```bash
java -cp target/AREP-LAB3-1.0.0.jar edu.escuelaing.app.App
```
---

🛠️ Technologies

Java 17 – Lenguaje principal.

Maven – Gestión de dependencias y build.

JUnit – Testing framework.
---
Reflexión en Java – Motor del IoC framework.

Outcome

Este proyecto implementa un mini framework tipo Spring Boot que permite:

Crear aplicaciones web en Java con POJOs anotados.

Servir archivos estáticos y recursos dinámicos.

Entender fundamentos de arquitectura HTTP y cliente-servidor.

Aplicar reflexión y anotaciones para construir frameworks IoC.
---
✒️ Author

Proyecto desarrollado por: Santiago Arteaga




