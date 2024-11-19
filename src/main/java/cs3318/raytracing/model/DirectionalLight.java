package cs3318.raytracing.model;

import cs3318.raytracing.utils.Point3D;
import cs3318.raytracing.utils.Vector3D;

public class DirectionalLight extends Light{

    public Vector3D lightVector;

    public DirectionalLight(float r, float g, float b, Vector3D direction) {
        super(r, g, b);
        lightVector = direction;
        lightVector.normalize();
    }

    public Vector3D calculateLightVector(Point3D p) {
        return new Vector3D(-lightVector.x, -lightVector.y, -lightVector.z);
    }

}


