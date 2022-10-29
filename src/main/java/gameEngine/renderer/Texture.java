package gameEngine.renderer;

import org.lwjgl.BufferUtils;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;

public class Texture {
    private int textureID;
    private int width;
    private int height;

    public void create(BufferedImage image){
        if (image == null) assert false : "Error: (Texture) BufferedImage is null";

        init();

        Raster raster = image.getRaster();
        DataBufferByte dataBufferByte = (DataBufferByte) raster.getDataBuffer();
        byte[] data = dataBufferByte.getData();

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(data.length);
        byteBuffer.order(ByteOrder.nativeOrder());
        byteBuffer.put(data, 0, data.length);
        byteBuffer.flip();

        int channelAmount = image.getColorModel().hasAlpha() ? 4 : 3;

        uploadToGPU(byteBuffer, image.getWidth(), image.getHeight(), channelAmount);
    }

    public Texture create(String filepath){
        init();

        //Convert image to ByteBuffer
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);
        stbi_set_flip_vertically_on_load(true);
        ByteBuffer image = stbi_load(filepath, width, height, channels, 0);

        uploadToGPU(image, width.get(0), height.get(0), channels.get(0));

        //Free storage used by stbi
        stbi_image_free(image);

        return this;
    }

    private void init(){
        //Generate texture on GPU
        textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);

        //Set texture parameters
        //Wrap image for x and y
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        //When stretching/shrinking image, pixelate
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
    }

    private void uploadToGPU(ByteBuffer image, int width, int height, int channelAmount){
        if (image != null) {
            this.width = width;
            this.height = height;

            if (channelAmount == 3) {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height,
                        0, GL_RGB, GL_UNSIGNED_BYTE, image);
            } else if (channelAmount == 4) {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height,
                        0, GL_RGBA, GL_UNSIGNED_BYTE, image);
            } else {
                assert false : "Error: (Texture) Unknown number of channels '" + channelAmount + "'";
            }
        } else {
            assert false : "Error: (Texture) Could not load image";
        }
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, textureID);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getId(){return textureID;}
}