package main.listeners;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import main.App;

public class KeyEventListener implements KeyListener {
	
	@Override
	public void keyTyped(KeyEvent event) {
	}
	@Override
	public void keyPressed(KeyEvent event) {
		App.instance().getState().onKeyEvent(event);
	}
	@Override
	public void keyReleased(KeyEvent event) {
	}
}
