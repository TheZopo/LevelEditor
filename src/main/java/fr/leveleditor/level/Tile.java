package fr.leveleditor.level;

import static org.lwjgl.opengl.GL11.*;

import fr.leveleditor.LevelEditor;
import fr.leveleditor.graphic.Texture;

public enum Tile {
	GRASS(0, 0),
	GRASS2(1, 0),
	FLOWER(2, 0),
	ROCK(3, 0),
	WATER_ROCK(4, 0),
	WATER_ROCK2(5, 0),
	
	SAND_CORNER_TL(0, 1),
	SAND_TOP(1, 1),
	SAND_CORNER_TR(2, 1),
	SAND_LEFT(0, 2),
	SAND(1, 2),
	SAND_RIGHT(2, 2),
	SAND_CORNER_BL(0, 3),
	SAND_BOTTOM(1, 3),
	SAND_CORNER_BR(2, 3),
	
	SAND_EDGE_TL(0, 4),
	SAND_EDGE_TR(1, 4),
	SAND_EDGE_BL(0, 5),
	SAND_EDGE_BR(1, 5),
	
	WATER_CORNER_TL(3, 1),
	WATER_TOP(4, 1),
	WATER_CORNER_TR(5, 1),
	WATER_LEFT(3, 2),
	WATER(4, 2),
	WATER_RIGHT(5, 2),
	WATER_CORNER_BL(3, 3),
	WATER_BOTTOM(4, 3),
	WATER_CORNER_BR(5, 3),
	
	WATER_EDGE_TL(3, 4),
	WATER_EDGE_TR(4, 4),
	WATER_EDGE_BL(3, 5),
	WATER_EDGE_BR(4, 5),
	
	NULL();
	
	private float texX;
	private float texY;
	
	Tile(int texX, int texY) {
		this.texX = texX * Texture.tiles.getTileSize();
		this.texY = texY * Texture.tiles.getTileSize();		
	}
	
	Tile() {
		this.texX = 0;
		this.texY = 0;
	}
	
	public static Tile fromCoord(int x, int y) {
		for(Tile tile : Tile.values()) {
			if(tile.texX ==  x * Texture.tiles.getTileSize() && tile.texY ==  y * Texture.tiles.getTileSize()) return tile;
		}
		return NULL;
	}
	
	public void render(int x, int y) {
		if(this == NULL) return;
		final float scale =  LevelEditor.instance.getScale();
		final float size = Texture.tiles.getTileSize();
		
		glTexCoord2f(texX, texY);
		glVertex2f(x * scale, y * scale);
		glTexCoord2f(texX + size, texY);
		glVertex2f((x + 1) * scale, y * scale);
		glTexCoord2f(texX, texY + size);
		glVertex2f(x * scale, (y + 1) * scale);
		
		glTexCoord2f(texX, texY + size);
		glVertex2f(x * scale, (y + 1) * scale);
		glTexCoord2f(texX + size, texY);
		glVertex2f((x + 1) * scale, y * scale);
		glTexCoord2f(texX + size, texY + size);
		glVertex2f((x + 1) * scale, (y + 1) * scale);
	}
}