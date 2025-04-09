package cs3318.raytracing.server.controller;

import cs3318.raytracing.api.RayTraceAPI;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/raytracer")
public class RayTraceController {
    private final RayTraceAPI rayTracer;
    private final ConcurrentLinkedQueue<CameraUpdate> updateQueue;
    private final AtomicBoolean isRendering;
    private static final long RENDER_THROTTLE = 100; // ms
    private long lastRenderTime = 0;

    private static class CameraUpdate {
        float eyeX, eyeY, eyeZ;
        float lookAtX, lookAtY, lookAtZ;
        boolean quickRender;
    }

    public RayTraceController() {
        this.rayTracer = new RayTraceAPI();
        this.updateQueue = new ConcurrentLinkedQueue<>();
        this.isRendering = new AtomicBoolean(false);
        initializeScene();
        startRenderLoop();
    }

    private void initializeScene() {
        rayTracer.setMaxBounces(1);
        rayTracer.setCameraDimensions(500, 500);
        rayTracer.setBackgroundColor(20,20,20);

        rayTracer.setCamera(0,10.5f,-40,0,16,5);

        rayTracer.addAmbientLight(1,1,1);
        rayTracer.addDirectionalLight(0.8f,0.8f,0.8f,0,-22,15);
        rayTracer.addPointLight(0.8f,0.8f,0.8f,0,22,490);
        rayTracer.addPointLight(0.8f,0.8f,0.8f,0,10,0);

        rayTracer.setCurrentSurface("emerald");
        rayTracer.addSphere(-5,3,10,3);
        rayTracer.addSphere(-5,8,10,2);

        rayTracer.setCurrentSurface("gold");
        rayTracer.addSphere(5,3,10,3);
        rayTracer.addSphere(5,8,10,2);

        rayTracer.setCurrentSurface("mirror");
        rayTracer.addSphere(0,3,15,3);
        rayTracer.addSphere(0,8,15,2);

        rayTracer.setCurrentSurface("blue rubber");
        rayTracer.addSphere(0,3,-11,3);
        rayTracer.addSphere(0,7.8f,-11,2);

        rayTracer.setCurrentSurface("gold");
        rayTracer.addPlane(0,1, 0, 0, 0, 0);

        rayTracer.setCurrentSurface("transparent water");
        rayTracer.addPlane(0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 500.0f);

        rayTracer.renderImage();
        rayTracer.saveImage("test_render.png");
    }

    private void startRenderLoop() {
        Thread renderThread = new Thread(() -> {
            while (true) {
                try {
                    if (!updateQueue.isEmpty() && !isRendering.get()) {
                        CameraUpdate update = updateQueue.poll();
                        if (update != null) {
                            isRendering.set(true);
                            rayTracer.setCamera(
                                    update.eyeX, update.eyeY, update.eyeZ,
                                    update.lookAtX, update.lookAtY, update.lookAtZ
                            );
                            rayTracer.renderImage();
                            isRendering.set(false);
                        }
                    }
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        renderThread.setDaemon(true);
        renderThread.start();
    }

    @PostMapping("/camera")
    public ResponseEntity<String> updateCamera(
            @RequestParam("eyeX") float eyeX,
            @RequestParam("eyeY") float eyeY,
            @RequestParam("eyeZ") float eyeZ,
            @RequestParam("lookAtX") float lookAtX,
            @RequestParam("lookAtY") float lookAtY,
            @RequestParam("lookAtZ") float lookAtZ,
            @RequestParam(value = "quickRender", defaultValue = "true") boolean quickRender) {

        System.out.println("Received camera update request:");
        System.out.println("Eye position: " + eyeX + ", " + eyeY + ", " + eyeZ);
        System.out.println("Look at point: " + lookAtX + ", " + lookAtY + ", " + lookAtZ);

        try {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastRenderTime < RENDER_THROTTLE) {
                return ResponseEntity.ok("Throttled");
            }

            CameraUpdate update = new CameraUpdate();
            update.eyeX = eyeX;
            update.eyeY = eyeY;
            update.eyeZ = eyeZ;
            update.lookAtX = lookAtX;
            update.lookAtY = lookAtY;
            update.lookAtZ = lookAtZ;
            update.quickRender = quickRender;

            updateQueue.offer(update);
            lastRenderTime = currentTime;

            return ResponseEntity.ok("Update queued");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    @GetMapping(value = "/render", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<Resource> getCurrentRender() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedImage image = rayTracer.getRenderedImage();
        ImageIO.write(image, "png", baos);

        ByteArrayResource resource = new ByteArrayResource(baos.toByteArray());
        return ResponseEntity.ok()
                .contentLength(resource.contentLength())
                .body(resource);
    }

    @GetMapping("/status")
    public ResponseEntity<Boolean> getRenderStatus() {
        return ResponseEntity.ok(isRendering.get());
    }
}