package main.states;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import main.App;
import main.gui.EntryWordBox;
import main.gui.WordBox;

public class TrialState extends AppState {
	
	private EntryWordBox entryWordBox;
	private WordBox altWordBox1, altWordBox2;
	private long start, end;
	
	public TrialState() {
		entryWordBox = new EntryWordBox(App.instance().getWidth() / 2, App.instance().getHeight() / 5 * 2, 30, 55);
		
		altWordBox1 = new WordBox(entryWordBox.getX(), App.instance().getHeight() / 3 * 2, 20, 55);
		altWordBox2 = new WordBox(entryWordBox.getX(), altWordBox1.getY() + altWordBox1.getHeight(), 20, 55);
		altWordBox1.setText(entryWordBox.getText());
		altWordBox2.setText(entryWordBox.getText());
		altWordBox1.setCursorPos(altWordBox1.getText().length() / 2);
		altWordBox2.setCursorPos(altWordBox2.getText().length() / 2);
		
		start = System.nanoTime();
	}
	
	
	@Override
	public void render(Graphics2D g) {
		g.setColor(DEFAULT_BG_COLOR);
		g.fillRect(0, 0, App.instance().getWidth(), App.instance().getHeight());
		
		entryWordBox.render(g);
		altWordBox1.render(g);
		altWordBox2.render(g);
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
		
		altWordBox1.setText(entryWordBox.getText());
		altWordBox1.setDisplayColors(entryWordBox.getDisplayColors());
	}
}
