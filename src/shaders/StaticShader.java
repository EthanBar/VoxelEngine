package shaders;

import entity.Camera;
import entity.Light;
import org.lwjgl.util.vector.Matrix4f;
import toolbox.GameIO;
import toolbox.Maths;

public class StaticShader extends ShaderProgram {

    private static final String VERTEX_FILE = GameIO.getCWD() + "/src/shaders/vertex.glsl";
    private static final String FRAG_FILE = GameIO.getCWD() + "/src/shaders/fragment.glsl";

    private int location_transformation;
    private int location_projection;
    private int location_view;
    private int location_brightness;
    private int location_lightColor;
    private int location_lightLocation;

    public StaticShader() {
        super(VERTEX_FILE, FRAG_FILE);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureSampler");
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformation = super.getUniformLocation("transformationMatrix");
        location_projection = super.getUniformLocation("projectionMatrix");
        location_view = super.getUniformLocation("viewMatrix");
        location_brightness = super.getUniformLocation("brightness");
//        location_lightLocation = super.getUniformLocation("lightPosition");
//        location_lightColor = super.getUniformLocation("lightColor");
    }

    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrix(location_transformation, matrix);
    }

    public void loadProjectionMatrix(Matrix4f matrix) {
        super.loadMatrix(location_projection, matrix);
    }

    public void loadViewMatrix(Camera camera) {
        Matrix4f viewMatrix = Maths.createViewMatrix(camera);
        super.loadMatrix(location_view, viewMatrix);
    }

    public void loadBrightness(int brightness) {
        super.loadInt(location_brightness, brightness);
    }

    public void loadLight(Light light) {
        super.loadVector(location_lightLocation, light.getPosition());
        super.loadVector(location_lightColor, light.getColor());
    }
}
