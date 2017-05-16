package render;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.*;

public class DisplayMain {

    private static final int WIDTH = 1440;
    private static final int HEIGHT = 900;
    private static final int FPS = 60;

    public static void create(){

        ContextAttribs attribs = new ContextAttribs(3, 2)
                .withForwardCompatible(true).withProfileCore(true);

        try {
            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
            Mouse.setGrabbed(true);
            Mouse.setClipMouseCoordinatesToWindow(true);
            Mouse.isClipMouseCoordinatesToWindow();
            Display.setResizable(true);
            DisplayMode[] modes = Display.getAvailableDisplayModes();
            Display.setFullscreen(true);
            Display.setTitle("Hello, OpenGL");
            Display.setVSyncEnabled(true);
            Display.create(new PixelFormat(), attribs);
        } catch (LWJGLException e) {
            e.printStackTrace();
        }

        GL11.glViewport(0, 0, WIDTH, HEIGHT);
    }

    public static void update(){
        Display.sync(FPS);
        Display.update();
    }

    public static void close(){
        Display.destroy();
    }

}
