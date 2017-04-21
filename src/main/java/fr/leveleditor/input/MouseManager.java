package fr.leveleditor.input;

import org.lwjgl.glfw.GLFWMouseButtonCallback;

import fr.leveleditor.utils.Window;

import static org.lwjgl.glfw.GLFW.*;

public class MouseManager extends GLFWMouseButtonCallback {
	private static boolean[][] buttons = new boolean[Window.list.size()][12];
	
	public void invoke(long window, int button, int action, int mods) {
		buttons[Window.indexFromId(window)][button] = action != GLFW_RELEASE;
	}
	
	public static boolean isDown(int buttonCode, long window) {
		return buttons[Window.indexFromId(window)][buttonCode];
	}

	public static boolean isDown(int buttonCode, int index) {
		return buttons[index][buttonCode];
	}
	
}
