package fr.leveleditor.utils;

import java.util.ArrayList;

import org.lwjgl.opengl.GLCapabilities;

public class Window {
	public static ArrayList<Window> list = new ArrayList<Window>();
	
	private long id;
	private GLCapabilities capabilities;
	private int width;
	private int height;
	
	public Window(long id, int width, int height) {
		this.id = id;
		this.width = width;
		this.height = height;
	}
	
	public static Window fromId(long id) {
		for(Window w : list) if(w.getId() == id) return w;
		return null;
	}
	
	public static int indexFromId(long id) {
		return list.indexOf(fromId(id));
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public GLCapabilities getCapabilities() {
		return capabilities;
	}

	public void setCapabilities(GLCapabilities capabilities) {
		this.capabilities = capabilities;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
}
