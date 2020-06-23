package engine;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class HeroMouse implements MouseListener {

	JPanel t;
	JLabel image;
	Controller controller;

	public HeroMouse(JPanel t, JLabel image, Controller controller) {
		this.controller = controller;
		this.t = t;
		this.image = image;
	}

	public void mouseEntered(java.awt.event.MouseEvent evt) {

		t.setVisible(true);
		image.setVisible(false);
		Controller.playAudio("Sounds/checkbox_toggle_on.wav");

	}

	public void mouseExited(java.awt.event.MouseEvent evt) {
		java.awt.Point p = new java.awt.Point(evt.getLocationOnScreen());
		SwingUtilities.convertPointFromScreen(p, evt.getComponent());

		// if (!evt.getComponent().contains(p)) {
		t.setVisible(false);
		image.setVisible(true);
		// }
		Controller.playAudio("Sounds/checkbox_toggle_off.wav");

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (controller.isHeroPowerPressed()) {
			if (((JPanel) e.getSource()).getName().equals("oppHero")) {
				controller.setTempHeroTarget(controller.getO());
			}
			if (((JPanel) e.getSource()).getName().equals("curHero")) {
				controller.setTempHeroTarget(controller.getC());
			}
			controller.useHeroPower();
		} else {
			if (controller.getSpell() == null) {
				if (((JPanel) e.getSource()).getName().equals("oppHero")) {
					controller.attack(controller.getO());
				}
				if (((JPanel) e.getSource()).getName().equals("curHero")) {
					controller.attack(controller.getC());
				}
			} else {
				if (((JPanel) e.getSource()).getName().equals("oppHero")) {
					controller.setTempHeroTarget(controller.getO());
				}
				if (((JPanel) e.getSource()).getName().equals("curHero")) {
					controller.setTempHeroTarget(controller.getC());
				}
				controller.playSpell(controller.getSpell());
			}
		}
//		Controller.playAudio("Sounds/card_turn_over_legendary.wav");
	}

	@Override
	public void mousePressed(MouseEvent e) {
		t.setBorder(BorderFactory.createLineBorder(Color.black));
		image.setBorder(BorderFactory.createLineBorder(Color.black));

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		t.setBorder(null);
		image.setBorder(null);

	}

}
