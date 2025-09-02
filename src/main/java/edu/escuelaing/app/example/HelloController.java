package edu.escuelaing.app.example;

import edu.escuelaing.app.framework.*;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Greetings from MicroSpringBoot!";
    }
}
