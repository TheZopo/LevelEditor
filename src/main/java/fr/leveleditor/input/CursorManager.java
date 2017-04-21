package fr.leveleditor.input;

import org.lwjgl.glfw.GLFWCursorPosCallback;

import fr.leveleditor.utils.Window;

public class CursorManager extends GLFWCursorPosCallback {
	public static int posX;
	public static int posY;
	public static int window;
	
	public void invoke(long window, double posX, double posY) {
		CursorManager.posX = (int) posX;
		CursorManager.posY = (int) posY;
		CursorManager.window = Window.indexFromId(window);
	}
	
	public static boolean isInRect(int posX, int posY, int sizeX, int sizeY) {
		return CursorManager.posX >= posX && CursorManager.posX < posX + sizeX && CursorManager.posY >= posY && CursorManager.posY < posY + sizeY;
	}
}
