package cs3318.raytracing.model;

/*
ka =  ambient reflection coefficient
kd = diffuse reflection coefficient
ks = specular reflection coefficient
kt = transmission coefficient
kr = reflectance coefficient
ns phong exponent
 */
public class Surface {
    public float rIntrinsic, gIntrinsic, bIntrinsic;    // surface's intrinsic color
    public PhongModel phong;
//    private static final float TINY = 0.001f;
    private static final float I255 = 0.00392156f;  // 1/255

    public Surface(float rval, float gval, float bval,
                   float ambientReflectionCoefficient, float diffuseReflectionCoefficient, float specularReflectionCoefficient, float exponent,
                   float reflectanceCoefficient, float transmissionCoefficient, float index) {
        rIntrinsic = rval; gIntrinsic = gval; bIntrinsic = bval;
        phong = new PhongModel(ambientReflectionCoefficient, diffuseReflectionCoefficient, specularReflectionCoefficient,
                transmissionCoefficient,reflectanceCoefficient*I255,
                exponent, index);
    }

}

//public Surface(float rval, float gval, float bval,
//               float ambientReflectionCoefficient, float diffuseReflectionCoefficient, float specularReflectionCoefficient, float exponent,
//               float reflectanceCoefficient, float transmissionCoefficient, float index) {
//    rIntrinsic = rval; gIntrinsic = gval; bIntrinsic = bval;
//    phong = new PhongModel(ambientReflectionCoefficient, diffuseReflectionCoefficient, specularReflectionCoefficient,
//            transmissionCoefficient,reflectanceCoefficient*I255,
//            exponent, index);
