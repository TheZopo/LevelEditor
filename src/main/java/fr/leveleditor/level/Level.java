package fr.leveleditor.level;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;

import fr.leveleditor.LevelEditor;
import fr.leveleditor.graphic.Texture;
import fr.leveleditor.utils.Logger;

import static org.lwjgl.opengl.GL11.*;

public class Level {
	private Tile[][][] tiles;
	
	private String name;
	private int sizeX;
	private int sizeY;
	
	private String path;
	
	public Level() {
		this.name = "Unknow";
		this.sizeX = 0;
		this.sizeY = 0;
		
		try {
			this.path = Level.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath() + "/" + name + ".yml";
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	public Level(String name, int sizeX, int sizeY) {
		this.name = name;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		
		this.tiles = new Tile[3][sizeX][sizeY];
		
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < sizeX; j++) {
				for(int k = 0; k < sizeY; k++) {
					tiles[i][j][k] = Tile.NULL;
				}
			}
		}
		
		try {
			this.path = LevelEditor.class.getProtectionDomain().getCodeSource().getLocation().toURI().resolve(".").getPath() + name + ".yml";
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	public static Level load(String name) {
		Level level = null;
		try {
			File f = new File(LevelEditor.class.getProtectionDomain().getCodeSource().getLocation().toURI().resolve(".").getPath() + name + ".yml");
			
			if(f.exists()) {
				YamlReader reader = new YamlReader(new FileReader(f));
				level = reader.read(Level.class);
				level.setPath(f.getAbsolutePath());
			}
			else {
				level = new Level("current", 20, 20);
				level.save();
			}
		} catch (FileNotFoundException | NullPointerException | URISyntaxException e) {
			Logger.error("Level \"" + name + "\" does not exists !");
		} catch (YamlException e) {
			Logger.error("Level \"" + name + "\" is invalid !");
			e.printStackTrace();
		}
		
		return level;
	}
	
	public static Level loadFromPath(String path) {
		Level level = null;
		try {
			File f = new File(path);
			
			if(f.exists()) {
				YamlReader reader = new YamlReader(new FileReader(f));
				level = reader.read(Level.class);
				level.setPath(path);
			}
		} catch (FileNotFoundException | NullPointerException e) {
			Logger.error("Level does not exists !");
		} catch (YamlException e) {
			Logger.error("Level is invalid !");
			e.printStackTrace();
		}
		
		return level;
	}
	
	public void save() {
		try {
			File f = new File(path);
			f.createNewFile();
			
			YamlWriter writer = new YamlWriter(new FileWriter(f));
			writer.write(this);
			writer.close();
		} catch (IOException e) {
			Logger.error("Impossible to acces to the save path !");
			e.printStackTrace();
		}
	}
	
	public void update() {
		
	}
	
	public void render() {
		final float layer = LevelEditor.instance.getActiveLayer();
		Texture.tiles.bind();
		glBegin(GL_TRIANGLES);
			for(int i = 0; i < 3; i++) {
				if(i < layer && layer < 3) glColor3f(0.7f, 0.7f, 0.7f);
				else if(i > layer) glColor4f(1, 1, 1, 0.25f);
				for(int j = 0; j < sizeX; j++) {
					for(int k = 0; k < sizeY; k++) {
						tiles[i][j][k].render(j, k);
					}
				}
				glColor3f(1, 1, 1);
			}
		glEnd();
		Texture.tiles.unbind();
	}
	
	public void remplissageDiffusion(int layer, int x, int y, Tile tilecible, Tile tilerep) {
		if(tiles[layer][x][y] == tilecible && tilecible != tilerep) {
			tiles[layer][x][y] = tilerep;
			if(x > 0) remplissageDiffusion(layer, x - 1, y, tilecible, tilerep);
			if(x < sizeX - 1) remplissageDiffusion(layer, x + 1, y, tilecible, tilerep);
			if(y > 0) remplissageDiffusion(layer, x, y - 1, tilecible, tilerep);
			if(y < sizeY - 1) remplissageDiffusion(layer, x, y + 1, tilecible, tilerep);
		}
	}

	public Tile[][][] getTiles() {
		return tiles;
	}

	public void setTiles(Tile[][][] tiles) {
		this.tiles = tiles;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSizeX() {
		return sizeX;
	}

	public void setSizeX(int sizeX) {
		this.sizeX = sizeX;
	}

	public int getSizeY() {
		return sizeY;
	}

	public void setSizeY(int sizeY) {
		this.sizeY = sizeY;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
}
