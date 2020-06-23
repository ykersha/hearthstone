package engine;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardListener implements KeyListener {

	private Controller controller;
	
	public KeyboardListener(Controller controller) {
		this.controller = controller;
	}
	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() ==   KeyEvent.VK_ESCAPE) {
			controller.setAttacker(null);
			controller.setSpell(null);
			controller.setTempHeroTarget(null);
			controller.setTempMinionTarget(null);
			controller.setHeroPowerPressed(false);
			controller.getView().updateHand();
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
