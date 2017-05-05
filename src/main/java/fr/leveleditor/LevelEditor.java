package fr.leveleditor;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.opengl.GL11.*;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;

import fr.leveleditor.graphic.Texture;
import fr.leveleditor.input.CursorManager;
import fr.leveleditor.input.MouseManager;
import fr.leveleditor.level.Level;
import fr.leveleditor.level.Tile;
import fr.leveleditor.menu.Menu;
import fr.leveleditor.menu.MenuCallback;
import fr.leveleditor.menu.MenuItem;
import fr.leveleditor.menu.NewLevel;
import fr.leveleditor.utils.Logger;
import fr.leveleditor.utils.Window;

public class LevelEditor {
	public static LevelEditor instance;
	
	private float scale = 60;
	private int maxTicks = 30;
	private int maxFPS = 60;
	private int tileRes = 32;
	
	private static int width = 1280;
	private int height = 720;
	private String title = "Editeur de niveau";
	
	private boolean running = false;
	private Level level;
	private Menu menu;
	
	private Tile selectedTile;
	
	private int[] pos1 = {0, 0};
	private int[] pos2 = {0, 0};
	private boolean isSelecting = false;
	
	private int[] overTile = {0, 0};
	private int activeLayer = 0;
	private int tool = 0;
	
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
		selectedTile = Tile.GRASS;
		
	    try { 
	    	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    } 
	    catch(Exception e){ 
	    	Logger.error("Impossible to load Look and Feel");
	    	e.printStackTrace();
	    }
	   
		
		menu = new Menu("Barr Menu", 32);
		
		menu.add(new MenuItem(32, Texture.menu, 0, 0, new MenuCallback() {
			public void callback() {
				new NewLevel();
			}
		}));
		
		menu.add(new MenuItem(32, Texture.menu, 1, 0, new MenuCallback() {
			public void callback() {
				JFileChooser chooser = new JFileChooser(new File("."));
				
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Fichiers YAML", "yml");
				chooser.addChoosableFileFilter(filter);
				chooser.setAcceptAllFileFilterUsed(false);
				
				chooser.showOpenDialog(null);
				
				if(chooser.getSelectedFile() != null) {
					level = Level.loadFromPath(chooser.getSelectedFile().getAbsolutePath());
				}
			}
		}));
		
		menu.add(new MenuItem(32, Texture.menu, 2, 0, new MenuCallback() {
			public void callback() {
				level.save();
				Logger.info("Level saved !");
				
				JOptionPane.showMessageDialog(null, "Le niveau \"" + level.getName() +"\" a bien été sauvegardé !", "Niveau sauvegardé", JOptionPane.INFORMATION_MESSAGE);
			}
		}));
		
		//Separateur
		menu.add(new MenuItem(0, Texture.menu, 0, 0, null));
		
		menu.add(new MenuItem(32, Texture.menu, 0, 1, new MenuCallback() {
			public void callback() {
				activeLayer = 0;
			}
		}));
		
		menu.add(new MenuItem(32, Texture.menu, 1, 1, new MenuCallback() {
			public void callback() {
				activeLayer = 1;
			}
		}));
		
		menu.add(new MenuItem(32, Texture.menu, 2, 1, new MenuCallback() {
			public void callback() {
				activeLayer = 2;
			}
		}));
		
		menu.add(new MenuItem(32, Texture.menu, 3, 1, new MenuCallback() {
			public void callback() {
				activeLayer = 3;
			}
		}));
		
		//Separateur
		menu.add(new MenuItem(0, Texture.menu, 0, 0, null));
		
		menu.add(new MenuItem(32, Texture.menu, 0, 2, new MenuCallback() {
			public void callback() {
				tool = 0;
			}
		}));
		
		menu.add(new MenuItem(32, Texture.menu, 1, 2, new MenuCallback() {
			public void callback() {
				tool = 1;
			}
		}));
		
		menu.add(new MenuItem(32, Texture.menu, 2, 2, new MenuCallback() {
			public void callback() {
				tool = 2;
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
		
		glfwSetScrollCallback(Window.list.get(0).getId(), new GLFWScrollCallback() {
			public void invoke(long window, double xoffset, double yoffset) {
				scale += 2 * yoffset;
			}
		});
		
		int x = (int) CursorManager.posX / (int) scale;
		int y = (int) (CursorManager.posY - 32) / (int) scale;
		int layer = activeLayer < 3 ? activeLayer : 2;
		if(MouseManager.isDown(GLFW_MOUSE_BUTTON_LEFT, 0) && CursorManager.posY > 32) {
			if(x >= 0 && x < level.getSizeX() && y >= 0 && y < level.getSizeY()) {
				if(tool == 0) {
					int[] t1 = pos1.clone();
					int[] t2 = pos2.clone();

					if(t2[0] < t1[0]) {
						t1[0] = pos2[0];
						t2[0] = pos1[0];
					}
					
					if(t2[1] < t1[1]) {
						t1[1] = pos2[1];
						t2[1] = pos1[1];
					}

					for(int i = 0; i < t2[0] - t1[0] + 1; i++) {
						for(int j = 0; j < t2[1] - t1[1] + 1; j++) {
							if(x + i >= 0 && y + j - (t2[1] - t1[1]) >= 0) level.getTiles()[layer][x + i][y + j - (t2[1] - t1[1])] = Tile.fromCoord(t1[0] + i, t1[1] + j);							
						}
					}
				}
				else if(tool == 1) level.remplissageDiffusion(layer, x, y, level.getTiles()[layer][x][y], selectedTile);
				else if(tool == 2) level.getTiles()[layer][x][y] = Tile.NULL;
			}
		}
		else if(MouseManager.isDown(GLFW_MOUSE_BUTTON_RIGHT, 0) && CursorManager.posY > 32) {
			if(x >= 0 && x < level.getSizeX() && y >= 0 && y < level.getSizeY()) {
				selectedTile = level.getTiles()[layer][x][y];
			}
			
			if(tool == 2) tool = 0;
		}
		
		if(MouseManager.isDown(GLFW_MOUSE_BUTTON_LEFT, 1)) {
			if(!isSelecting) {
				pos1[0] = CursorManager.posX / tileRes;
				pos1[1] = CursorManager.posY / tileRes;
				selectedTile = Tile.fromCoord(pos1[0], pos1[1]);
			}
			isSelecting = true;
			
			pos2[0] = CursorManager.posX / tileRes;
			pos2[1] = CursorManager.posY / tileRes;
			
			if(tool == 2) tool = 0;
		}
		
		
		if(!MouseManager.isDown(GLFW_MOUSE_BUTTON_LEFT, 1)) {
			isSelecting = false;
			if(tool == 1 || tool == 2) pos2 = pos1.clone();
		}
		
		if(CursorManager.window == 0) {
			overTile[0] = (int) (CursorManager.posX / scale);
			overTile[1] = (int) ((CursorManager.posY - 32) / scale);
		}

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
					glVertex2f(0, 32);
					glVertex2f(level.getSizeX() * scale, 32);
					glVertex2f(level.getSizeX() * scale, 32 + level.getSizeY() * scale);
					glVertex2f(0, 32 + level.getSizeY() * scale);
					glVertex2f(0, 32);
				glEnd();
				
				int sizeX = Math.abs(pos2[0] - pos1[0]) + 1;
				int sizeY = Math.abs(pos2[1] - pos1[1]) + 1;
				glBegin(GL_LINE_LOOP);
					glColor4f(0, 0, 0, 1);
					glVertex2f(overTile[0] * scale, 32 + (overTile[1] + 1) * scale + 1);
					glVertex2f((overTile[0] + sizeX) * scale, 32 + (overTile[1] + 1) * scale + 1);
					glVertex2f((overTile[0] + sizeX) * scale, 32 + (overTile[1] + 1 - sizeY) * scale);
					glVertex2f(overTile[0] * scale, 32 + (overTile[1] + 1 - sizeY) * scale);
					glVertex2f(overTile[0] * scale, 32 + (overTile[1] + 1) * scale + 1);
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
				
				int[] t1 = pos1.clone();
				int[] t2 = pos2.clone();
				
				if(t2[0] < t1[0]) {
					t1[0] = pos2[0];
					t2[0] = pos1[0];
				}
				
				if(t2[1] < t1[1]) {
					t1[1] = pos2[1];
					t2[1] = pos1[1];
				}
				
				glBegin(GL_LINE_LOOP);
					glColor4f(1, 0, 0, 1);
					glVertex2f(t1[0] * tileRes, t1[1] * tileRes + 1);
					glVertex2f((t2[0] + 1) * tileRes, t1[1] * tileRes + 1);
					glVertex2f((t2[0] + 1) * tileRes, (t2[1] + 1) * tileRes);
					glVertex2f(t1[0] * tileRes + 1, (t2[1] + 1) * tileRes);
					glVertex2f(t1[0] * tileRes + 1, t1[1] * tileRes + 1);
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
	
	public void setLevel(Level level) {
		this.level = level;
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
	
	public int getActiveLayer() {
		return activeLayer;
	}
	
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		LevelEditor.width = width;
	}
	
	public int getTool() {
		return tool;
	}

	public static void main(String[] args) {
		new LevelEditor();
	}

}
