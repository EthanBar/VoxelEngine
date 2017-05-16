package objects;

import entity.Entity;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;
import render.Loader;
import textures.ModelTexture;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Block_Side {

    float[] vertices = {
            -1, 1, 0,
            -1, -1, 0,
            1, -1, 0,
            1, 1, 0f
    };

    int[] indices = {
            0,1,3,
            3,1,2
    };

    static private float[] textureCoords = {
//            0.5f, 0, // Back side
//            0.5f, 0.5f,
//            0, 0.5f,
//            0, 0,
//            0.5f, 0, // Front side
//            0.5f, 0.5f,
//            0, 0.5f,
//            0, 0,
//            0.5f, 0, // Right side
//            0.5f, 0.5f,
//            0, 0.5f,
//            0, 0,
//            0.5f, 0, // Left side
//            0.5f, 0.5f,
//            0, 0.5f,
//            0, 0,
            1, 0, // Top side
            1, 0.5f,
            0.5f, 0.5f,
            0.5f, 0
//            0.5f, 0.5f, // Bottom side
//            0.5f, 1,
//            0, 1,
//            0, 0.5f,
    };

    public Entity entity;

    private static Loader loader;
    static Map<String, TexturedModel> textures = new HashMap<>();

    public Block_Side(String filename, boolean sigular) {
        RawModel bmodel = loader.load(vertices, textureCoords, indices);
        ModelTexture texture = new ModelTexture(loader.loadTexture("blocks/dirt/map"));
        TexturedModel model = new TexturedModel(bmodel, texture);
        entity = new Entity(model, new Vector3f(0, 0, 0), 90, 0, 0, 1);
//        ModelTexture texture = new ModelTexture(loader.loadTexture(filename));
//        TexturedModel texturedModel = new TexturedModel(model, texture);
    }

    static public void setLoader(Loader load) {
        loader = load;
    }



}
