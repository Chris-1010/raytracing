package cs3318.raytracing.model;

import cs3318.raytracing.utils.*;

public class Camera {

    public Point3D eye;
    private Vector3D lookat;
    private Vector3D up;
    public Vector3D Du, Dv, Vp;
    private float fov;
    private int width, height;

    public Camera (){}

    public Camera(Vector3D up, Vector3D lookat, Point3D eye, int width, int height, float fov){
        this.eye = eye;
        this.lookat = lookat;
        this.up = up;
        this.width = width;
        this.height = height;
        this.fov = fov;
        computeVectors();
    }

    public void setEye(Point3D eye) {
        this.eye = eye;
        computeVectors();
    }

    public void setLookat(Vector3D lookat){
        this.lookat = lookat;
        computeVectors();
    }

    public void setDimensions(int width, int height){
        this.width = width;
        this.height = height;
        computeVectors();
    }

    public void setFOV(float fov){
        this.fov = fov;
        computeVectors();
    }

    public void setUp(Vector3D up){
        this.up = up;
        computeVectors();
    }

    private void computeVectors() {
        // Compute viewing matrix that maps a
        // screen coordinate to a ray direction
        Vector3D look = new Vector3D(lookat.x - eye.x, lookat.y - eye.y, lookat.z - eye.z);
        Du = Vector3D.normalize(look.cross(up));
        Dv = Vector3D.normalize(look.cross(Du));
        float fl = (float)(width / (2*Math.tan((0.5*fov)*Math.PI/180)));
        Vp = Vector3D.normalize(look);
        Vp.x = Vp.x*fl - 0.5f*(width*Du.x + height*Dv.x);
        Vp.y = Vp.y*fl - 0.5f*(width*Du.y + height*Dv.y);
        Vp.z = Vp.z*fl - 0.5f*(width*Du.z + height*Dv.z);
    }

    public Vector3D calculateDirection(int i, int j) {
        return new Vector3D(
                i*Du.x + j*Dv.x + Vp.x,
                i*Du.y + j*Dv.y + Vp.y,
                i*Du.z + j*Dv.z + Vp.z);
    }

}
