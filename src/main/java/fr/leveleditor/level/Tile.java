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
	
	MOUNTAIN_CORNER_TL(6, 1),
	MOUNTAIN_TOP(7, 1),
	MOUNTAIN_CORNER_TR(8, 1),
	MOUNTAIN_LEFT(6, 2),
	MOUNTAIN(7, 2),
	MOUNTAIN_RIGHT(8, 2),
	MOUNTAIN_CORNER_BL(6, 3),
	MOUNTAIN_BOTTOM(7, 3),
	MOUNTAIN_CORNER_BR(8, 3),
	
	MOUNTAIN_EDGE_TL(6, 4),
	MOUNTAIN_EDGE_TR(7, 4),
	MOUNTAIN_EDGE_BL(6, 5),
	MOUNTAIN_EDGE_BR(7, 5),
	
	TREE_BOT_L(9, 4),
	TREE_BOT_R(10, 4),
	TREE_MID_L_1(9, 3),
	TREE_MID_R_1(10, 3),
	TREE_MID_L_2(9, 2),
	TREE_MID_R_2(10, 2),
	TREE_TOP_L(9, 1),
	TREE_TOP_R(10, 1),
	
	TREE_MID_WBACK_L_1(11, 3),
	TREE_MID_WBACK_R_1(12, 3),
	TREE_MID_WBACK_L_2(11, 2),
	TREE_MID_WBACK_R_2(12, 2),
	TREE_TOP_WBACK_L(11, 1),
	TREE_TOP_WBACK_R(12, 1),
	
	PARASOL_BOT_L(0, 8),
	PARASOL_BOT_M(1, 8),
	PARASOL_BOT_R(2, 8),
	PARASOL_MID_L(0, 7),
	PARASOL_MID_M(1, 7),
	PARASOL_MID_R(2, 7),
	PARASOL_TOP_L(0, 6),
	PARASOL_TOP_M(1, 6),
	PARASOL_TOP_R(2, 6),
	
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
		glVertex2f(x * scale, 32 + y * scale);
		glTexCoord2f(texX + size, texY);
		glVertex2f((x + 1) * scale, 32 + y * scale);
		glTexCoord2f(texX, texY + size);
		glVertex2f(x * scale, 32 + (y + 1) * scale);
		
		glTexCoord2f(texX, texY + size);
		glVertex2f(x * scale, 32 + (y + 1) * scale);
		glTexCoord2f(texX + size, texY);
		glVertex2f((x + 1) * scale, 32 + y * scale);
		glTexCoord2f(texX + size, texY + size);
		glVertex2f((x + 1) * scale, 32 + (y + 1) * scale);
	}
	
	public int getX() {
		return (int) (texX / Texture.tiles.getTileSize());
	}
	
	public int getY() {
		return (int) (texY / Texture.tiles.getTileSize());
	}
}