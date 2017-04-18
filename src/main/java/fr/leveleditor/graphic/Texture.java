package fr.leveleditor.graphic;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;

import fr.leveleditor.LevelEditor;
import fr.leveleditor.utils.Logger;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;

public class Texture {
	private int id;
	private int width;
	private int height;
	private float tileSize;
	
	public static Texture tiles = Texture.loadTexture("tiles.png");
	public static Texture menu = Texture.loadTexture("menubarr.png");
	
	public Texture(int id, int width, int height) {
		this.id = id;
		this.width = width;
		this.height = height;
		this.tileSize = LevelEditor.instance.getTileRes() / (float) width;
	}
	
	public static Texture loadTexture(String name) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(LevelEditor.class.getResource("/resources/" + name));
		} catch (Exception e) {
			Logger.error("Impossible to read resources files !");
		}
		
		int w = image.getWidth();
		int h = image.getHeight();
		
		int[] pixels = new int[w * h];
		image.getRGB(0, 0, w, h, pixels, 0, w);
		
		ByteBuffer buffer = BufferUtils.createByteBuffer(w * h * 4);
		
		for (int y = 0; y < w; y++) {
			for (int x = 0; x < h; x++) {
				int i = pixels[x + y * w];
				buffer.put((byte) ((i >> 16) & 0xFF));
				buffer.put((byte) ((i >> 8) & 0xFF));
				buffer.put((byte) ((i) & 0xFF));
				buffer.put((byte) ((i >> 24) & 0xFF));
			}
		}
		
		buffer.flip();
		
		int id = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, id);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, w, h, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		
		return new Texture(id, w, h);
	}
	
	public int getId() {
		return id;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public float getTileSize() {
		return tileSize;
	}
	
	public void bind() {
		glBindTexture(GL_TEXTURE_2D, id);
	}
	
	public void unbind() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}
}
