package main.listeners;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import main.App;

public class MouseEventListener implements MouseListener, MouseMotionListener, MouseWheelListener {
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent event) {
		App.instance().getState().onMouseEvent(event);
	}

	
	@Override
	public void mouseDragged(MouseEvent event) {
		App.instance().getState().onMouseEvent(event);
	}
	@Override
	public void mouseMoved(MouseEvent event) {
		App.instance().getState().onMouseEvent(event);
	}
	
	
	@Override
	public void mouseClicked(MouseEvent event) {
		App.instance().getState().onMouseEvent(event);
	}
	@Override
	public void mousePressed(MouseEvent event) {
		App.instance().getState().onMouseEvent(event);
	}
	@Override
	public void mouseReleased(MouseEvent event) {
		App.instance().getState().onMouseEvent(event);
	}
	@Override
	public void mouseEntered(MouseEvent event) {
		App.instance().getState().onMouseEvent(event);
	}
	@Override
	public void mouseExited(MouseEvent event) {
		App.instance().getState().onMouseEvent(event);
	}
}
