package main.states;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import main.App;
import main.gui.WordBox;

public class MenuState extends AppState {
	
	private WordBox wordBox;
	
	
	public MenuState() {
		wordBox = new WordBox("<Press any key to start>", Color.WHITE);
	}
	

	@Override
	public void render(Graphics2D g) {
		g.setColor(SONIC_SILVER);
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
