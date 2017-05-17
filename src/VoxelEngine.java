import audio.AudioMaster;
import audio.Source;
import entity.Camera;
import render.BlockRenderer;
import objects.Block_Side;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import render.Loader;
import org.lwjgl.opengl.Display;
import render.DisplayMain;
import render.Renderer;
import shaders.StaticShader;
import toolbox.Settings;
import worldgen.World;

public class VoxelEngine {

    private static boolean AUDIO = false;

    public static void main(String[] args) {

        DisplayMain.create();

        Loader loader = new Loader();
        Block_Side.setLoader(loader);
        StaticShader shader = new StaticShader();
        Renderer renderer = new Renderer(shader);

        BlockRenderer.addTexture("blocks/dirt/map", loader, 1, BlockRenderer.FOUR_SQUARE);
        BlockRenderer.addTexture("blocks/planks_oak/single", loader, 2, BlockRenderer.ONE_SQUARE);
//        BlockRenderer.addTexture("natev2", loader, 2, BlockRenderer.ONE_SQUARE);
//        BlockRenderer.addTexture("natev3", loader, 3, BlockRenderer.ONE_SQUARE);
//        BlockRenderer.addTexture("natev3", loader, 3, BlockRenderer.ONE_SQUARE);

//        int chunkSize = 64;
//        Block[][][] blocks = new Block[chunkSize][chunkSize][chunkSize];
//        for (int x = 0; x < chunkSize; x++) {
//            for (int y = 0; y < chunkSize; y++) {
//                for (int z = 0; z < chunkSize; z++) {
//                    blocks[x][y][z] = new Block(x * 2, y * 2, z * 2, y);
//                }
//            }
//        }
//        blocks[10][7][8].setID(0);
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
        int frameNumber = 0;
        int renderChunks = (int)Math.ceil(Settings.RENDER_DISTANCE / 16) * 2;
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);


        BlockRenderer.start();
        while(!Display.isCloseRequested()) {
            // Set up
            if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) break;
            Vector3f pos = camera.getPosition();
            BlockRenderer.setX(pos.x);
            BlockRenderer.setY(pos.y);
            BlockRenderer.setZ(pos.z);
            frameNumber++;
            double time = System.currentTimeMillis();

            // Tick math
            camera.move();
            int cx = (int)Math.floor(pos.x / 16);
            int cy = (int)Math.floor(pos.z / 16);


            // Render
            BlockRenderer.prepare();
            shader.start();
            shader.loadRenderDistance(Settings.RENDER_DISTANCE);
            shader.loadViewMatrix(camera);
            for (int x = cx - renderChunks / 2 - 1; x < cx + renderChunks / 2 + 1; x++) {
                for (int y = cy - renderChunks / 2 - 1; y < cy + renderChunks / 2 + 1; y++) {
                    if (world.checkEmpty(x, y)) BlockRenderer.render(world.getBlocks(x, y), shader, new int[]{x * 16, 0, y * 16});
                }
            }

            if ( Mouse.isButtonDown(0)) {
                if (world.checkEmpty(cx, cy)) {
                    int posx = ((int)(pos.x)) % 16;
                    int posy = (int)pos.y;
                    int posz = ((int)(pos.z)) % 16;
                    if (posx < 0) posx += 15;
                    if (posy < 0) posy += 15;
                    if (posz < 0) posz += 15;
//                    System.out.println((int)pos.x);
                    world.getChunk(cx, cy).updateBlock(posx, posy, posz, 2);
                }
            }
            shader.stop();
            DisplayMain.update();
//            if (frameNumber % 60 == 0) System.out.println("Milliseconds per frame: " +
//                                                                  Double.toString((double)System.currentTimeMillis() - time));
        }

        BlockRenderer.clean();
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
    */
}
