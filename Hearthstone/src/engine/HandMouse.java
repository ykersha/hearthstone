package engine;


import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.cards.Card;
import model.cards.minions.Minion;
import model.cards.spells.Spell;

public class HandMouse implements MouseListener {

	JLabel image;
	JPanel details;
	Controller controller;

	public HandMouse(JLabel image, JPanel details, Controller controller) {
		this.image = image;
		this.details = details;
		this.controller = controller;
	}

	public void mouseEntered(java.awt.event.MouseEvent evt) {
		image.setVisible(false);
		details.setVisible(true);

		Controller.playAudio("Sounds/card_mouse_over.wav");
	}

	public void mouseExited(java.awt.event.MouseEvent evt) {
		details.setVisible(false);
		image.setVisible(true);
		Controller.playAudio("Sounds/card_mouse_away.wav");

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		JPanel p = (JPanel) (e.getSource());
		int index = controller.getHand().indexOf(p);
		Card card = controller.getC().getHand().get(index);
		if (card instanceof Minion) {
			controller.playMinion((Minion) card);
		} else if (card instanceof Spell) {
			if (controller.getSpell() == card) {
				controller.setSpell(null);
				controller.getView().updateHand();
			} else
				controller.playSpell((Spell) card);
		}

	}

	@Override
	public void mousePressed(MouseEvent e) {
		details.setBorder(BorderFactory.createLineBorder(Color.black));
		image.setBorder(BorderFactory.createLineBorder(Color.black));

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		details.setBorder(null);
		image.setBorder(null);

	}

}
