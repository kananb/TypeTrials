package main.states;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import main.App;
import main.gui.WordBox;

public class MenuState extends AppState {
	
	private WordBox wordBox;
	
	
	public MenuState() {
		wordBox = new WordBox(App.instance().getWidth() / 2, App.instance().getHeight() / 2, 40, "<Press any key to start>");
		wordBox.setCursorPos(-1);
	}
	

	@Override
	public void render(Graphics2D g) {
		g.setColor(DEFAULT_BG_COLOR);
		g.fillRect(0, 0, App.instance().getWidth(), App.instance().getHeight());
		
		wordBox.render(g);
	}

	
	@Override
	public void onMouseEvent(MouseEvent event) {
		// do nothing for now
	}

	@Override
	public void onKeyEvent(KeyEvent event) {
		App.instance().setState(new TrialState());
	}

}
