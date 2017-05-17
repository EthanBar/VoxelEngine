package render;

import models.RawModel;
import models.TexturedModel;
import objects.Block;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import shaders.StaticShader;
import textures.ModelTexture;
import toolbox.Maths;
import toolbox.Settings;

import java.util.HashMap;
import java.util.Map;

public class BlockRenderer {

    static private RawModel rawModel;
    static private RawModel rawModelSingle;
    static private Map<Integer, TexturedModel> textures = new HashMap<>();
    static private Map<Integer, Integer> types = new HashMap<>();
    static public final int FOUR_SQUARE = 2;
    static public final int ONE_SQUARE = 1;

    static private float px, py, pz;

    public static void setX(float x) {
        BlockRenderer.px = x;
    }

    public static void setY(float y) {
        BlockRenderer.py = y;
    }

    public static void setZ(float z) {
        BlockRenderer.pz = z;
    }

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

    private static int perviousTextureType = FOUR_SQUARE;
    private static int render = Settings.RENDER_DISTANCE;
    private static int previousID = 0;
    private static int chunkSize = 16;
    private static int limit = 15;

    public static void start() {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
    }

    public static void prepare() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(0.737f, 0.823f, 0.956f, 1);
//        GL30.glBindVertexArray(rawModel.getVaoID());
//        GL20.glEnableVertexAttribArray(0);
//        GL20.glEnableVertexAttribArray(1);
    }

    public static void render(Block[][][] block, StaticShader shader, int[] offset) {
//        GL20.glEnableVertexAttribArray(0);
//        GL20.glEnableVertexAttribArray(1);
        for (int x = 0; x < chunkSize; x++) {
            for (int y = 0; y < block[x].length; y++) {
                for (int z = 0; z < chunkSize; z++) {
                    int ID = block[x][y][z].getID(); // Get texture ID
                    if (ID == 0) continue; // Break if air

                    // Break if out of render distance
                    float distance = (float)Math.sqrt(Math.pow(px - (x + offset[0]), 2) + Math.pow(py - (y + offset[1]), 2) + Math.pow(pz - (z + offset[2]), 2));
                    if (distance > render) continue;

                    // Break if underground (temp speed up)
                    if (y != block[x].length - 1) {
                        if (block[x][y + 1][z].getID() != 0) continue;
                    }

                    // Break if surrounded
                    if (x != 0 && x != limit && y != 0 && y != limit && z != 0 && z != limit) {
                        if (block[x - 1][y][z].getID() != 0 && block[x + 1][y][z].getID() != 0 &&
                                block[x][y - 1][z].getID() != 0 && block[x][y + 1][z].getID() != 0 &&
                                block[x][y][z - 1].getID() != 0 && block[x][y][z + 1].getID() != 0) {
                            continue;
                        }
                    }

                    // Change texture if needed
                    if (previousID != ID) {
                        // Change model type if needed
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
                        GL13.glActiveTexture(GL13.GL_TEXTURE0);
                        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textures.get(ID).getTexture().getTextureID());
                        previousID = ID;
                    }

                    Matrix4f transformationMatrix = Maths.createTransformationMatrix(new Vector3f(x + offset[0], y + offset[1], z + offset[2]),
                                                                                     0, 0, 0, 0.5f);
                    shader.loadTransformationMatrix(transformationMatrix);
                    shader.loadBrightness(block[x][y][z].getLight());
                    GL11.glDrawElements(GL11.GL_TRIANGLES, 36, GL11.GL_UNSIGNED_INT, 0);
                }
            }
        }
    }

    public static void clean() {
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
