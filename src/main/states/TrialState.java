package main.states;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import main.App;
import main.gui.RandomWordBox;

public class TrialState extends AppState {
	
	private RandomWordBox randomWordBox;
	private long start, end;
	
	public TrialState() {
		randomWordBox = new RandomWordBox(41);
		start = System.nanoTime();
	}
	
	
	@Override
	public void render(Graphics2D g) {
		g.setColor(SONIC_SILVER);
		g.fillRect(0, 0, App.instance().getWidth(), App.instance().getHeight());
		
		randomWordBox.render(g);
	}

	@Override
	public void onMouseEvent(MouseEvent event) {

	}

	@Override
	public void onKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.VK_SHIFT) return;
		
		if (event.getKeyCode() == KeyEvent.VK_BACK_SPACE)
			randomWordBox.untype();
		else {
			randomWordBox.type((event.isShiftDown()) ? Character.toUpperCase(event.getKeyChar()) : event.getKeyChar());
			final int WORD_QUOTA = 40;
			if (randomWordBox.getNumWordsTyped() - randomWordBox.getNumMissedChars() >= WORD_QUOTA) {
				end = System.nanoTime();
				double secDuration = (end - start) / 1e9;
				System.out.printf("Time: %.2fs\nWPM: %.2f\n", secDuration, WORD_QUOTA / secDuration * 60);
				App.instance().setState(new MenuState());
			}
		}
	}
}
