package cs3318.raytracing.model;

import java.util.ArrayList;
import java.util.List;

import cs3318.raytracing.utils.*;

public class Scene {
    final static int CHUNKSIZE = 100;  // Max array size
    List<Renderable> objectList;
    List<Light> lightList;

    public Scene (){
        objectList = new ArrayList<>(CHUNKSIZE);
        lightList = new ArrayList<>(CHUNKSIZE);
    }

    public List<Light> getLights() {
        return lightList;
    }

    public List<Renderable> getObjects() {
        return objectList;
    }

    public void addSphere(Point3D center, float radius, Surface surface){
        Sphere sphere = new Sphere(center, radius, surface);
        objectList.add(sphere);
    }

    public void addAmbientLight(float r, float g, float b) {
        AmbientLight ambientLight = new AmbientLight(r, g, b);
        lightList.add(ambientLight);
    }

    public void addDirectionalLight(float r, float g, float b, Vector3D direction) {
        DirectionalLight directionalLight = new DirectionalLight(r, g, b, direction);
        lightList.add(directionalLight);
    }

    public void addPointLight(float r, float g, float b, Point3D point) {
        PointLight pointLight = new PointLight(r, g, b, point);
        lightList.add(pointLight);
    }
}
