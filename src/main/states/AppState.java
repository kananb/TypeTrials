package main.states;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public abstract class AppState {
	
	protected static final Color LAVENDER_BLUE = new Color(0xBBBDF6);
	protected static final Color BLUE_BELL = new Color(0x9893DA);
	protected static final Color RYTHM_BLUE = new Color(0x797A9E);
	protected static final Color SONIC_SILVER = new Color(0x72727E);
	protected static final Color DIM_GRAY = new Color(0x625F63);
	
	
	// public abstract void update();
	public abstract void render(Graphics2D g);
	
	public abstract void onMouseEvent(MouseEvent event);
	public abstract void onKeyEvent(KeyEvent event);
}
