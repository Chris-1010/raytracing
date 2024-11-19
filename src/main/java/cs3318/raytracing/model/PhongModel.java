package cs3318.raytracing.model;

public class PhongModel {
    /*
    ka =  ambient reflection coefficient
    kd = diffuse reflection coefficient
    ks = specular reflection coefficient
    kt = transmission coefficient
    kr = reflectance coefficient
    ns phong exponent
     */
    public float ambientReflectionCoefficient, diffuseReflectionCoefficient, specularReflectionCoefficient,
    transmissionCoefficient, reflectanceCoefficient;
    public float exponent;
    public float index;

    PhongModel (float ambientReflectionCoefficient, float diffuseReflectionCoefficient, float specularReflectionCoefficient,
                float transmissionCoefficient, float reflectanceCoefficient, float exponent, float index){
        this.ambientReflectionCoefficient = ambientReflectionCoefficient;
        this.diffuseReflectionCoefficient = diffuseReflectionCoefficient;
        this.specularReflectionCoefficient = specularReflectionCoefficient;
        this.transmissionCoefficient = transmissionCoefficient;
        this.reflectanceCoefficient = reflectanceCoefficient;
        this.exponent = exponent;
        this.index = index;
    }
}
