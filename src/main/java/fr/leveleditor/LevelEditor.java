package fr.leveleditor;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;

import fr.leveleditor.graphic.Texture;
import fr.leveleditor.input.CursorManager;
import fr.leveleditor.input.MouseManager;
import fr.leveleditor.level.Level;
import fr.leveleditor.menu.Menu;
import fr.leveleditor.menu.MenuCallback;
import fr.leveleditor.menu.MenuItem;
import fr.leveleditor.utils.Logger;
import fr.leveleditor.utils.Window;

public class LevelEditor {
	public static LevelEditor instance;
	
	private float scale = 60;
	private int maxTicks = 60;
	private int maxFPS = 120;
	private int tileRes = 32;
	
	private static int width = 600;
	private int height = 600;
	private String title = "Editeur de niveau";
	
	private boolean running = false;
	private Level level;
	private Menu menu;
	
	private int[] selectedTile = {0, 0};
	private int[] overTile = {0, 0};
	private int activeLayer = 0;
	
	public LevelEditor() {
		instance = this;
		
		GLFWErrorCallback.createPrint(System.err).set();
		
		Logger.info("Initialize GLFW");
		if(!glfwInit()) Logger.error("GLFW can't inizialise. ABORT MISSION");
		
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
		glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE);
		
		
		long id = glfwCreateWindow(width, height, title, 0, 0);
		Window.list.add(new Window(id, width, height));
		id = glfwCreateWindow(width / 2, height / 2, "TileSet", 0, id);
		Window.list.add(new Window(id, width / 2, height / 2));
		
		for(Window window : Window.list) {
			glfwMakeContextCurrent(window.getId());
			
			glfwShowWindow(window.getId());
			Logger.info("Disabling VSync");
			glfwSwapInterval(0);
			
			Logger.info("Creating OpenGL Capabilities");
			GLCapabilities capabilities = GL.createCapabilities();
			window.setCapabilities(capabilities);
			
			glClearColor(1, 1, 1, 1);
			
			Logger.info("Configuring OpenGL settings");
			glMatrixMode(GL_PROJECTION);
			glLoadIdentity();
			glOrtho(0.0f, window.getWidth(), window.getHeight(), 0.0f, 0.0f, 1.0f);
			glEnableClientState(GL_VERTEX_ARRAY);
			
			glEnable(GL_TEXTURE_2D);		
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			
			Logger.info("Registering inputs");
			glfwSetCursorPosCallback(window.getId(), new CursorManager());
			glfwSetMouseButtonCallback(window.getId(), new MouseManager());			
		}
		
		level = Level.load("current");
		menu = new Menu("Barr Menu", 32);
		menu.add(new MenuItem(32, Texture.menu, 0, 0, new MenuCallback() {
			public void callback() {
				activeLayer = 0;
			}
		}));
		
		menu.add(new MenuItem(32, Texture.menu, 1, 0, new MenuCallback() {
			public void callback() {
				activeLayer = 1;
			}
		}));
		
		menu.add(new MenuItem(32, Texture.menu, 2, 0, new MenuCallback() {
			public void callback() {
				activeLayer = 2;
			}
		}));
		
		menu.add(new MenuItem(32, Texture.menu, 3, 0, new MenuCallback() {
			public void callback() {
				activeLayer = 3;
			}
		}));
		
		
		running = true;
		mainLoop();
	}
	
	public void update() {
		glfwPollEvents();
		
		if(glfwWindowShouldClose(Window.list.get(0).getId()) || glfwWindowShouldClose(Window.list.get(1).getId())) stop();
		
		
		glfwSetWindowSizeCallback(Window.list.get(0).getId(), new GLFWWindowSizeCallback(){
			public void invoke(long window, int width, int height) {
				setWidth(width);
				glfwMakeContextCurrent(window);
				glViewport(0, 0, width, height);
				
				glMatrixMode(GL_PROJECTION);
				glLoadIdentity();
				glOrtho(0.0f, width, height, 0.0f, 0.0f, 1.0f);
			}
		});
		
		glfwSetWindowSizeCallback(Window.list.get(1).getId(), new GLFWWindowSizeCallback(){
			public void invoke(long window, int width, int height) {
				glfwMakeContextCurrent(window);
				
				glViewport(0, 0, width, height);
				glMatrixMode(GL_PROJECTION);
				glLoadIdentity();
				glOrtho(0.0f, width, height, 0.0f, 0.0f, 1.0f);
			}
		});
		
		overTile[0] = (int) (CursorManager.posX / scale);
		overTile[1] = (int) ((CursorManager.posY - 32) / scale);
		
		menu.update();
		level.update();
	}
	
	public void render() {
		for(Window window : Window.list) {
			glfwMakeContextCurrent(window.getId());
			GL.setCapabilities(window.getCapabilities());
			
			glClear(GL_COLOR_BUFFER_BIT);
			
			if(Window.list.indexOf(window) == 0) {
				level.render();
				
				glBegin(GL_LINE_LOOP);
					glColor4f(0, 0, 1, 1);
					glVertex2f(0, 0);
					glVertex2f(level.getSizeX() * scale, 0);
					glVertex2f(level.getSizeX() * scale, level.getSizeY() * scale);
					glVertex2f(0, level.getSizeY() * scale);
					glVertex2f(0, 0);
				glEnd();
				
				glBegin(GL_LINE_LOOP);
					glColor4f(0, 0, 0, 1);
					glVertex2f(overTile[0] * scale, 32 + overTile[1] * scale + 1);
					glVertex2f((overTile[0] + 1) * scale, 32 + overTile[1] * scale + 1);
					glVertex2f((overTile[0] + 1) * scale, 32 + (overTile[1] + 1) * scale);
					glVertex2f(overTile[0] * scale, 32 + (overTile[1] + 1) * scale);
					glVertex2f(overTile[0] * scale, 32 + overTile[1] * scale + 1);
					glColor4f(1, 1, 1, 1);
				glEnd();
				
				menu.render();
			}
			else {
				Texture.tiles.bind();
				glBegin(GL_QUADS);
					glTexCoord2f(0, 0);
					glVertex2f(0, 0);
					glTexCoord2f(1, 0);
					glVertex2f(Texture.tiles.getWidth(), 0);
					glTexCoord2f(1, 1);
					glVertex2f(Texture.tiles.getWidth(), Texture.tiles.getHeight());
					glTexCoord2f(0, 1);
					glVertex2f(0, Texture.tiles.getHeight());
				glEnd();
				Texture.tiles.unbind();
				
				glBegin(GL_LINE_LOOP);
					glColor4f(1, 0, 0, 1);
					glVertex2f(selectedTile[0] * tileRes, selectedTile[1] * tileRes + 1);
					glVertex2f((selectedTile[0] + 1) * tileRes, selectedTile[1] * tileRes + 1);
					glVertex2f((selectedTile[0] + 1) * tileRes, (selectedTile[1] + 1) * tileRes);
					glVertex2f(selectedTile[0] * tileRes + 1, (selectedTile[1] + 1) * tileRes);
					glVertex2f(selectedTile[0] * tileRes + 1, selectedTile[1] * tileRes + 1);
					glColor4f(1, 1, 1, 1);
				glEnd();
			}
			
			glfwSwapBuffers(window.getId());
		}
	}
	
	public void mainLoop() {
		long initialTime = System.nanoTime();
		final double timeU = 1000000000 / maxTicks;
		final double timeF = maxFPS == -1 ? 0 : 1000000000 / maxFPS;
		double deltaU = 0, deltaF = 0;
		
		while(running) {
	        long currentTime = System.nanoTime();
	        deltaU += (currentTime - initialTime) / timeU;
	        deltaF += (currentTime - initialTime) / timeF;
	        initialTime = currentTime;

	        if (deltaF >= 1) {
	            render();
	            deltaF--;
	        }
	        
	        if (deltaU >= 1) {
	            update();
	            deltaU--;
	        }
		}
	}
	
	public void stop() {
		running = false;
		
		level.save();
		
		for(Window window : Window.list) {
			glfwFreeCallbacks(window.getId());
			glfwDestroyWindow(window.getId());			
		}
		
		glfwTerminate();
		System.exit(0);
	}
	
	public Level getLevel() {
		return level;
	}
	
	public float getScale() {
		return scale;
	}
	
	public void setScale(float scale) {
		this.scale = scale;
	}
	
	public int getMaxTicks() {
		return maxTicks;
	}
	
	public int getTileRes() {
		return tileRes;
	}
	
	public void setSelectedTile(int[] selectedTile) {
		this.selectedTile = selectedTile;
	}
	
	public int[] getSelectedTile() {
		return selectedTile;
	}
	
	public int getActiveLayer() {
		return activeLayer;
	}
	
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		LevelEditor.width = width;
	}

	public static void main(String[] args) {
		new LevelEditor();
	}

}
