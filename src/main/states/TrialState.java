package main.states;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import main.App;
import main.gui.EntryWordBox;

public class TrialState extends AppState {
	
	private EntryWordBox entryWordBox;
	private long start, end;
	
	public TrialState() {
		int appWidth = App.instance().getWidth(), appHeight = App.instance().getHeight();
		int width = appWidth * 3 / 4, height = 120, x = appWidth / 2 - width / 2, y = appHeight / 3 - height / 2;
		entryWordBox = new EntryWordBox(new Rectangle(x, y, width, height), 40);
		entryWordBox.setCursorPos(entryWordBox.getText().length() / 2);
		
		start = System.nanoTime();
	}
	
	
	@Override
	public void render(Graphics2D g) {
		g.setColor(DEFAULT_BG_COLOR);
		g.fillRect(0, 0, App.instance().getWidth(), App.instance().getHeight());
		
		entryWordBox.render(g);
	}

	@Override
	public void onMouseEvent(MouseEvent event) {

	}

	@Override
	public void onKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.VK_SHIFT) return;
		else if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
			App.instance().setState(new MenuState());
		}
		
		if (event.getKeyCode() == KeyEvent.VK_BACK_SPACE)
			entryWordBox.untype();
		else {
			entryWordBox.type((event.isShiftDown()) ? Character.toUpperCase(event.getKeyChar()) : event.getKeyChar());
			final int WORD_QUOTA = 40;
			if (entryWordBox.getNumWordsTyped() - entryWordBox.getNumMissedChars() >= WORD_QUOTA) {
				end = System.nanoTime();
				double secDuration = (end - start) / 1e9;
				System.out.printf("Time: %.2fs\nWPM: %.2f\n", secDuration, WORD_QUOTA / secDuration * 60);
				App.instance().setState(new MenuState());
			}
		}
	}
}
