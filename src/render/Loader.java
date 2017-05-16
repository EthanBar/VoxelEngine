package render;

import models.RawModel;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import toolbox.GameIO;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class Loader {

    private List<Integer> vaos = new ArrayList<>();
    private List<Integer> vbos = new ArrayList<>();
    private List<Integer> textures = new ArrayList<>();

    public RawModel load2(float[] positions, float[] textureCoords, int[] indices, float[] offset) {
        int vaoID = create();
        bindIndices(indices);
        storeData(0, 3, positions, false);
        storeData(1, 2, textureCoords, false);
        storeData(2, 3, offset, true);
        unbind();
        return new RawModel(vaoID, indices.length);
    }

//    public RawModel load(float[] positions, float[] textureCoords, int[] indices, float[] normals) {
//        int vaoID = create();
//        bindIndices(indices);
//        storeData(0, 3, positions);
//        storeData(1, 2, textureCoords);
//        storeData(2, 3, normals);
//        unbind();
//        return new RawModel(vaoID, indices.length);
//    }

    public RawModel load(float[] positions, float[] textureCoords, int[] indices) {
        int vaoID = create();
        bindIndices(indices);
        storeData(0, 3, positions, false);
        storeData(1, 2, textureCoords, false);
        unbind();
        return new RawModel(vaoID, indices.length);
    }

    public RawModel load(float[] positions, int[] indices) {
        int vaoID = create();
        bindIndices(indices);
        storeData(0, 3, positions, false);
        unbind();
        return new RawModel(vaoID, indices.length);
    }


    public int loadTexture(String filename) {
        Texture texture;
        try {
            texture = TextureLoader.getTexture("PNG", new FileInputStream(GameIO.getCWD() + "/res/" + filename + ".png"), GL11.GL_NEAREST);
//            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
//            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_NEAREST);
//            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
//            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -1f);
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
        int textureID = texture.getTextureID();
        textures.add(textureID);
        return textureID;
    }

    public void clean() {
        for (int vao: vaos) {
            GL30.glDeleteVertexArrays(vao);
        }
        for (int vbo: vbos) {
            GL15.glDeleteBuffers(vbo);
        }
        for (int texture: textures) {
            GL11.glDeleteTextures(texture);
        }
    }

    private int create() {
        int vaoID = GL30.glGenVertexArrays();
        vaos.add(vaoID);
        GL30.glBindVertexArray(vaoID);
        return vaoID;
    }

    private void storeData(int attributeNumber, int coordinateSize, float[] data, boolean instance) {
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
            FloatBuffer buffer = storeFloatBuffer(data);
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
            GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,0);
        if (instance) GL33.glVertexAttribDivisor(2, 1);
    }

    private void unbind() {
        GL30.glBindVertexArray(0);
    }

    private void bindIndices(int[] indices) {
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
        IntBuffer buffer = storeIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }

    private IntBuffer storeIntBuffer(int data[]) {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    private FloatBuffer storeFloatBuffer(float data[]) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

}
