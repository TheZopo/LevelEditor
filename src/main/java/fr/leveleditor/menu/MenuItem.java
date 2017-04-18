package fr.leveleditor.menu;

import fr.leveleditor.graphic.Texture;
import fr.leveleditor.input.CursorManager;
import fr.leveleditor.input.MouseManager;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.glfw.GLFW;

public class MenuItem {
	private int posX;
	private int height;
	private int width;
	
	private Texture texture;
	private float texX;
	private float texY;
	
	private MenuCallback callback;
	
	public MenuItem(int width, Texture texture, int texX, int texY, MenuCallback callback) {
		this.width = width;
		this.callback = callback;
		
		this.texture = texture;
		this.texX = texX * texture.getTileSize();	
		this.texY = texY * texture.getTileSize();	
		
		this.posX = 0;
		this.height = 32;
	}
	
	public void render() {
		texture.bind();
		glBegin(GL_QUADS);
			glTexCoord2f(texX, texY);
			glVertex2i(posX, 0);
			glTexCoord2f(texX + texture.getTileSize(), texY);
			glVertex2i(posX + width, 0);
			glTexCoord2f(texX + texture.getTileSize(), texY + texture.getTileSize());
			glVertex2i(posX + width, height);
			glTexCoord2f(texX, texY + texture.getTileSize());
			glVertex2i(posX, height);
		glEnd();
		texture.unbind();
	}
	
	public void update() {
		if(CursorManager.isInRect(posX, 0, width, height) && MouseManager.isDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) callback.callback();
	}

	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}
}
