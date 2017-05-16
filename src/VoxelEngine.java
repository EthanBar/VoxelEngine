import audio.AudioMaster;
import audio.Source;
import entity.Camera;
import objects.Block;
import objects.BlockTexture;
import objects.Block_Side;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import render.Loader;
import org.lwjgl.opengl.Display;
import render.DisplayMain;
import render.Renderer;
import shaders.StaticShader;
import worldgen.World;

import java.util.ArrayList;

public class VoxelEngine {

    private static boolean AUDIO = false;

    public static void main(String[] args) {

        DisplayMain.create();

        Loader loader = new Loader();
        Block_Side.setLoader(loader);
        StaticShader shader = new StaticShader();
        Renderer renderer = new Renderer(shader);

        BlockTexture.addTexture("blocks/dirt/map", loader, 1, BlockTexture.FOUR_SQUARE);
        BlockTexture.addTexture("blocks/planks_oak/single", loader, 2, BlockTexture.ONE_SQUARE);
//        BlockTexture.addTexture("natev2", loader, 2, BlockTexture.ONE_SQUARE);
//        BlockTexture.addTexture("natev3", loader, 3, BlockTexture.ONE_SQUARE);
//        BlockTexture.addTexture("natev3", loader, 3, BlockTexture.ONE_SQUARE);

        int chunkSize = 32;
        Block[][][] blocks = new Block[chunkSize][chunkSize][chunkSize];
        for (int x = 0; x < chunkSize; x++) {
            for (int y = 0; y < chunkSize; y++) {
                for (int z = 0; z < chunkSize; z++) {
                    blocks[x][y][z] = new Block(x * 2, y * 2, z * 2, y);
                }
            }
        }
        blocks[10][7][8].setID(0);
//        Light light = new Light(new Vector3f(8, 8, 8), new Vector3f(1, 1, 1));
        Camera camera = new Camera();

        if (AUDIO) {
            AudioMaster.init();
            AudioMaster.setListenerData();
            int buffer = AudioMaster.loadSound("audio/moogcity.wav");
            Source source = new Source();
            source.play(buffer);
        }

        World world = new World();
        // Enable wireframe
//        GL11.glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        GL11.glEnable(GL11.GL_CULL_FACE);
        while(!Display.isCloseRequested()) {
            if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) break;

            // Tick math
            camera.move();

            // Render
            renderer.prepare();
            shader.start();
//            shader.loadLight(light);
            shader.loadViewMatrix(camera);
            BlockTexture.render(blocks, shader);
            shader.stop();
            DisplayMain.update();
        }

        shader.clean();
        loader.clean();
        if (AUDIO) AudioMaster.clean();
        DisplayMain.close();
    }

    /* Notes
        Block_Side block = new Block_Side("hi", true);
        Entity entity = new Entity(texturedModel, new Vector3f(0, 1, -1), 0, 0, 0, 1);
        Entity entity2 = new Entity(dirt.get(), new Vector3f(0, 0, 0), 0, 0, 0, 1);
        RawModel model = OBJLoader.loadObjModel("blocks/dirt/block1", loader);
        ModelTexture dirtBottom = new ModelTexture(loader.loadTexture("blocks/dirt/bottom"));
        renderer.render(entity2, shader);
        renderer.render(block.entity, shader);

            static private float normals[] = {   // x     y     z
            -1, -1,  1,
            -1,  1,  1,
            -1, -1, -1,
            -1,  1, -1,

             1, -1, -1,
             1,  1, -1,
             1, -1,  1,
             1,  1,  1,

             1,  1, -1,
            -1,  1, -1,
             1,  1,  1,
            -1,  1,  1,

             1, -1,  1,
            -1, -1,  1,
             1, -1, -1,
            -1, -1, -1,

            1, -1,  1,
            -1, -1,  1,
            1, -1, -1,
            -1, -1, -1,

            1, -1,  1,
            -1, -1,  1,
            1, -1, -1,
            -1, -1, -1

    };

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
            0, 0.5f,
    };



    static private int[] indices = {
            0,1,3,
            3,1,2,
            4,5,7,
            7,5,6,
            8,9,11,
            11,9,10,
            12,13,15,
            15,13,14,
            16,17,19,
            19,17,18,
            20,21,23,
            23,21,22
    };
     */

}
