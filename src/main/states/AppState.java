package main.states;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public abstract class AppState {
	
	protected static final Color DEFAULT_BG_COLOR = new Color(60, 60, 60);
	
	
	// public abstract void update();
	public abstract void render(Graphics2D g);
	
	public abstract void onMouseEvent(MouseEvent event);
	public abstract void onKeyEvent(KeyEvent event);
}
