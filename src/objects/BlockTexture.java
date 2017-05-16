package objects;

import models.RawModel;
import models.TexturedModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import render.Loader;
import shaders.StaticShader;
import textures.ModelTexture;
import toolbox.Maths;
import java.util.HashMap;
import java.util.Map;

public class BlockTexture {

    static private RawModel rawModel;
    static private RawModel rawModelSingle;
    static private Map<Integer, TexturedModel> textures = new HashMap<>();
    static private Map<Integer, Integer> types = new HashMap<>();
    static public final int FOUR_SQUARE = 2;
    static public final int ONE_SQUARE = 1;

    public static void addTexture(String filename, Loader loader, int ID, int type) {
        if (rawModel == null) {
            rawModel = loader.load(vertices, textureCoords, indices);
        }
        if (rawModelSingle == null) {
            rawModelSingle = loader.load(vertices, textureCoordsSingle, indices);
        }
        ModelTexture modelTexture = new ModelTexture(loader.loadTexture(filename));
        if (type == FOUR_SQUARE) {
            textures.put(ID, new TexturedModel(rawModel, modelTexture));
        } else {
            textures.put(ID, new TexturedModel(rawModelSingle, modelTexture));
        }
        types.put(ID, type);
    }

    public static void render(Block[][][] block, StaticShader shader) {
        int chunkSize = block.length;
        int limit = chunkSize - 1;
        GL30.glBindVertexArray(rawModelSingle.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL11.glCullFace(GL11.GL_BACK);
        int perviousTextureType = ONE_SQUARE;
        int previousID = 0;
        for (int x = 0; x < chunkSize; x++) {
            for (int y = 0; y < chunkSize; y++) {
                for (int z = 0; z < chunkSize; z++) {
                    int ID = block[x][y][z].getID(); // Get texture ID
                    if (ID == 0) continue; // Break if air
                    // Change texture if needed
                    if (types.get(ID) != perviousTextureType) {
                        perviousTextureType = types.get(ID);
                        GL20.glDisableVertexAttribArray(0);
                        GL20.glDisableVertexAttribArray(1);
                        if (types.get(ID) == ONE_SQUARE) {
                            GL30.glBindVertexArray(rawModelSingle.getVaoID());
                        } else {
                            GL30.glBindVertexArray(rawModel.getVaoID());
                        }
                        GL20.glEnableVertexAttribArray(0);
                        GL20.glEnableVertexAttribArray(1);
                    }
                    if (previousID != ID) {
                        GL13.glActiveTexture(GL13.GL_TEXTURE0);
                        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textures.get(ID).getTexture().getTextureID());
                        previousID = ID;
                    }

                    if (x != 0 && x != limit && y != 0 && y != limit && z != 0 && z != limit) {
                        if (block[x - 1][y][z].getID() != 0 && block[x + 1][y][z].getID() != 0 &&
                                block[x][y - 1][z].getID() != 0 && block[x][y + 1][z].getID() != 0 &&
                                block[x][y][z - 1].getID() != 0 && block[x][y][z + 1].getID() != 0) {
                            continue;
                        }
                    }

                    Matrix4f transformationMatrix = Maths.createTransformationMatrix(new Vector3f(x * 2, y * 2, z * 2),
                                                                                     0, 0, 0, 1);
                    shader.loadTransformationMatrix(transformationMatrix);
                    shader.loadBrightness(block[x][y][z].getLight());
                    GL11.glDrawElements(GL11.GL_TRIANGLES, 36, GL11.GL_UNSIGNED_INT, 0);
                }
            }
        }
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
    }


    static private float[] vertices = {
            -1,1,-1, // 0
            -1,-1,-1, // 1
            1,-1,-1, // 2
            1,1,-1, // 3

            -1,1,1, // 4
            -1,-1,1, // 5
            1,-1,1,
            1,1,1,

            1,1,-1,
            1,-1,-1,
            1,-1,1,
            1,1,1,

            -1,1,-1, // 0
            -1,-1,-1,
            -1,-1,1,
            -1,1,1,

            -1,1,1,
            -1,1,-1,
            1,1,-1,
            1,1,1,

            -1,-1,1, // 20
            -1,-1,-1,
            1,-1,-1,
            1,-1,1 // 23
    };

    static private float[] textureCoords = {
            0.5f, 0, // Back side
            0.5f, 0.5f,
            0, 0.5f,
            0, 0,
            0.5f, 0, // Front side
            0.5f, 0.5f,
            0, 0.5f,
            0, 0,
            0.5f, 0, // Right side
            0.5f, 0.5f,
            0, 0.5f,
            0, 0,
            0.5f, 0, // Left side
            0.5f, 0.5f,
            0, 0.5f,
            0, 0,
            1, 0, // Top side
            1, 0.5f,
            0.5f, 0.5f,
            0.5f, 0,
            0.5f, 0.5f, // Bottom side
            0.5f, 1,
            0, 1,
            0, 0.5f
    };

    static private float[] textureCoordsSingle = {
            1, 0, // Back side
            1, 1,
            0, 1,
            0, 0,
            1, 0, // Back side
            1, 1,
            0, 1,
            0, 0,
            1, 0, // Back side
            1, 1,
            0, 1,
            0, 0,
            1, 0, // Back side
            1, 1,
            0, 1,
            0, 0,
            1, 0, // Back side
            1, 1,
            0, 1,
            0, 0,
            1, 0, // Back side
            1, 1,
            0, 1,
            0, 0
    };

    static private int[] indices = {
            0,3,1, // Back Counter
            3,2,1,
            4,5,7, // Front Normal
            7,5,6,
            8,11,9, // Right Counter
            11,10,9,
            12,13,15, // Left Normal
            15,13,14,
            16,19,17, // Top Counter
            19,18,17,
            20,21,23, // Bottom Normal
            23,21,22
    };

}
