package cs3318.raytracing.controller;

import cs3318.raytracing.model.*;
import cs3318.raytracing.utils.Point3D;
import cs3318.raytracing.utils.Vector3D;

import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Controller {

    private final Camera camera;
    private final Scene scene;
    private BufferedImage image;
    private final Renderer renderer;

    public Controller(){
        // Initialize defaults
        Point3D eye = new Point3D(0, 0, 10);
        Vector3D lookat = new Vector3D(0, 0, 0);
        Vector3D up = new Vector3D(0, 1, 0);
        Color background = new Color(0, 0, 0);
        float fov = 30;
        int width = 860;
        int height = 640;
        camera = new Camera(up, lookat, eye, width, height, fov);
        setImage(width, height);
        scene = new Scene();
        renderer = new Renderer(background);
    }

    public void setImage(int width, int height) {
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }

    public void setCamera(Vector3D up, Vector3D lookat, Point3D eye, int width, int height, float fov){
        camera.setUp(up);
        camera.setLookat(lookat);
        camera.setEye(eye);
        camera.setDimensions(width, height);
        camera.setFOV(fov);
    }

    public void setCamera(Vector3D lookat, Point3D eye){
        camera.setLookat(lookat);
        camera.setEye(eye);
    }

    public void setRenderer(Color background){
        renderer.setBackground(background);
    }

    public void renderImage() {
        long time = System.currentTimeMillis();
        for (int j = 0; j < image.getHeight(); j += 1) {
            for (int i = 0; i < image.getWidth(); i += 1) {
                Color pixelColor = renderer.renderPixel(i, j, scene, camera);
                image.setRGB(i, j, pixelColor.getRGB());
            }
        }
        time = System.currentTimeMillis() - time;
        System.err.println("Rendered in "+(time/60000)+":"+((time%60000)*0.001));
    }

    public void exportImage(String filename) {
        try {
            File outputfile = new File(filename);
            ImageIO.write(image, "PNG", outputfile);
            System.out.println("Image exported as " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setTestScene() {
        Surface testSurface = new Surface(0.2f, 0.8f, 0.2f, 0.5f, 0.9f, 0.4f, 10.0f, 0, 0, 1);
        Point3D center = new Point3D(-0.4f, 0.375f, -0.4f);
        addSphere(center, 0.375f, testSurface);

        testSurface = new Surface(0.7f,  0.3f,  0.2f,  0.5f,  0.9f,  0.4f,  6.0f, 0, 0, 1);
        center = new Point3D(-0.6f, 1.05f, -0.6f);
        addSphere(center, 0.3f, testSurface);

        testSurface = new Surface(0.2f,  0.3f,  0.8f,  0.5f,  0.9f,  0.4f,  10.0f, 0, 0, 1);
        center = new Point3D(-0.8f,  1.575f,  -0.8f);
        addSphere(center, 0.125f, testSurface);

        addLight(1.0f,  1.0f, 0.981f);
        addLight(0.9f,  0.9f, 0.9f);
        addLight(0.745f, 0.859f, 0.224f);
        Vector3D direction = new Vector3D(-1, -1, -1);
        addLight(0.6f, 0.6f, 0.6f, direction);

        Vector3D testLookat = new Vector3D(-0.5f, 0f, -0.5f);
        Point3D testEye = new Point3D(1.5f, 10.5f, -1.5f);

        setCamera(testLookat, testEye);

    }

    public void addSphere(Point3D center, float radius, Surface surface) {
        scene.addSphere(center, radius, surface);
    }

    public void addLight(float r, float g, float b){
        scene.addAmbientLight(r, g, b);
    }

    public void addLight(float r, float g, float b, Vector3D direction){
        scene.addDirectionalLight(r, g, b, direction);
    }

    public void addLight(float r, float g, float b, Point3D point){
        scene.addPointLight(r, g, b, point);
    }


}
