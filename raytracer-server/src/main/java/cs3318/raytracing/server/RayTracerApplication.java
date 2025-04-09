// raytracer-server/src/main/java/cs3318/raytracing/server/RayTracerApplication.java
package cs3318.raytracing.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "cs3318.raytracing")
public class RayTracerApplication {
    public static void main(String[] args) {
        SpringApplication.run(RayTracerApplication.class, args);
    }
}