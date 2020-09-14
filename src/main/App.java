package main;

import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

import main.gui.Display;
import main.listeners.KeyEventListener;
import main.listeners.MouseEventListener;
import main.states.MenuState;
import main.states.AppState;

public class App {
	
	private static App instance = null;
	
	
	private Display display;
	private MouseEventListener mouseListener;
	private KeyEventListener keyListener;
	
	private BufferStrategy bs;
	private Graphics2D g;
	private AppState state;

	private boolean running;
	

	private App() {
		display = new Display(1280, 720, "Type Trial");
		mouseListener = new MouseEventListener();
		keyListener = new KeyEventListener();
		display.getCanvas().addMouseListener(mouseListener);
		display.getCanvas().addMouseMotionListener(mouseListener);
		display.getCanvas().addMouseWheelListener(mouseListener);
		display.getCanvas().addKeyListener(keyListener);
		
		if (display.getCanvas().getBufferStrategy() == null) display.getCanvas().createBufferStrategy(3);
		bs = display.getCanvas().getBufferStrategy();
		g = (Graphics2D)bs.getDrawGraphics();
		
		state = new MenuState();
	}
	public static App instance() {
		if (instance == null) instance = new App();
		return instance;
	}
	
	public void run() {
		running = true;
		
		while (running) {
			g.clearRect(0, 0, display.getWidth(), display.getHeight());
			state.render(g);
			bs.show();
		}
		
		g.dispose();
	}
	
	
	public AppState getState() {
		return state;
	}
	public void setState(AppState state) {
		this.state = state;
	}
	
	
	public int getWidth() {
		return display.getWidth();
	}
	public int getHeight() {
		return display.getHeight();
	}
}
