package fr.leveleditor.input;

import org.lwjgl.glfw.GLFWMouseButtonCallback;

import fr.leveleditor.LevelEditor;
import fr.leveleditor.level.Tile;
import fr.leveleditor.utils.Window;

import static org.lwjgl.glfw.GLFW.*;

public class MouseManager extends GLFWMouseButtonCallback {
	private static boolean[] buttons = new boolean[12];
	
	public void invoke(long window, int button, int action, int mods) {
		buttons[button] = action != GLFW_RELEASE;
		
		if(button == GLFW_MOUSE_BUTTON_LEFT && action != GLFW_RELEASE) {
			
			if(Window.indexFromId(window) == 0) {
				LevelEditor.instance.getLevel().getTiles()[LevelEditor.instance.getActiveLayer()][(int) CursorManager.posX / (int) LevelEditor.instance.getScale()][(int) CursorManager.posY / (int) LevelEditor.instance.getScale()] = Tile.fromCoord(LevelEditor.instance.getSelectedTile()[0], LevelEditor.instance.getSelectedTile()[1]);
			}
			else if(Window.indexFromId(window) == 1) {
				LevelEditor.instance.setSelectedTile(new int[]{CursorManager.posX / LevelEditor.instance.getTileRes(), CursorManager.posY / LevelEditor.instance.getTileRes()});
			}
		}
		else if(button == GLFW_MOUSE_BUTTON_RIGHT && action != GLFW_RELEASE) {
			LevelEditor.instance.updateActiveLayer();
		}
	}
	
	public static boolean isDown(int buttonCode) {
		return buttons[buttonCode];
	}
}
