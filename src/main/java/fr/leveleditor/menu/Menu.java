package fr.leveleditor.menu;

import java.util.ArrayList;

import fr.leveleditor.LevelEditor;

import static org.lwjgl.opengl.GL11.*;

public class Menu {
	private String name;
	private ArrayList<MenuItem> items;
	
	public Menu(String name, int height) {
		this.name = name;
		this.items = new ArrayList<MenuItem>();
	}
	
	public void render() {
		glBegin(GL_QUADS);
			glColor3f(0.9f, 0.9f, 0.9f);
			glVertex2i(0, 0);
			glVertex2i(LevelEditor.instance.getWidth(), 0);
			glVertex2i(LevelEditor.instance.getWidth(), 32);
			glVertex2i(0, 32);
			glColor3f(1, 1 ,1);
		glEnd();
		
		final int layer = 3 + LevelEditor.instance.getActiveLayer();
		glBegin(GL_LINE_LOOP);
			glColor3f(0, 0, 0);
			glVertex2i(20 + layer * 10 + layer * 32, 1);
			glVertex2i(20 + layer * 10 + layer * 32 + 32, 1);
			glVertex2i(20 + layer * 10 + layer * 32 + 32, 32);
			glVertex2i(20 + layer * 10 + layer * 32, 32);
			glVertex2i(20 + layer * 10 + layer * 32, 1);
			glColor3f(1, 1, 1);
		glEnd();
		
		final int tool = 3 + 4 + LevelEditor.instance.getTool();
		glBegin(GL_LINE_LOOP);
		glColor3f(0, 0, 0);
		glVertex2i(30 + tool * 10 + tool * 32, 1);
		glVertex2i(30 + tool * 10 + tool * 32 + 32, 1);
		glVertex2i(30 + tool * 10 + tool * 32 + 32, 32);
		glVertex2i(30 + tool * 10 + tool * 32, 32);
		glVertex2i(30 + tool * 10 + tool * 32, 1);
		glColor3f(1, 1, 1);
		glEnd();

		for(MenuItem item : items) item.render();
	}
	
	public void update() {		
		for(MenuItem item : items) item.update();
	}
	
	public void add(MenuItem item) {
		int posX = 10;
		for(MenuItem itm : items) posX += itm.getWidth() + 10;
		
		item.setPosX(posX);
		items.add(item);
	}
	
	public void remove(MenuItem item) {
		items.remove(item);
	}
	
	public String getName() {
		return name;
	}
}
