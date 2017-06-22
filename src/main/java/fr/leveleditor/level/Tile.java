package fr.leveleditor.level;

import static org.lwjgl.opengl.GL11.*;

import fr.leveleditor.LevelEditor;
import fr.leveleditor.graphic.Texture;

public enum Tile {
	SOL_CORNER_L(3,0),
	SOL(4, 0),
	SOL_CORNER_R(5,0),
	SOL_CORNER_LD(3,1),
	SOL_D(4,1),
	SOL_D2(0,1),
	SOL_CORNER_RD(5,1),
	
	SOL_GRASS_CORNER_L(1,1),
	SOL_GRASS_CORNER_R(1,2),
	
	SOL_GRASS_1(0,0),
	SOL_GRASS_2(1,0),
	SOL_GRASS_3(2,0),
	
	BACK_TL(4,4),
	BACK_TM(5,4),
	BACK_TR(6,4),
	BACK_ML(4,5),
	BACK_MM(5,5),
	BACK_MR(6,5),
	BACK_DL(4,6),
	BACK_DM(5,6),
	BACK_DR(6,6),
	
	BACK_CORNER_TL(2,4),
	BACK_CORNER_TR(3,4),
	BACK_CORNER_DL(2,5),
	BACK_CORNER_DR(3,5),
	
	BACK_GRASS1(2,6),
	BACK_GRASS2(3,6),
	
	BOX(1,4),
	
	WATER_T(1,5),
	WATER_B(1,6),
	
	LADDER_T(0,4),
	LADDER_M(0,5),
	LADDER_B(0,6),
	
	SIGN(7,0),
	SIGN_ARROW(7,1),
	
	FENCE_L(7,2),
	FENCE_M(8,2),
	FENCE_R(9,2),
	
	FLOWER_YELLOW1(7,5),
	FLOWER_YELLOW2(8,5),

	FLOWER_ORANGE1(7,6),
	FLOWER_ORANGE2(8,6),
	
	ROCK1(9,5),
	ROCK2(9,6),
	
	CHEST(7,4),
	
	GRASS(4,3),
	
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