package view;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import engine.Controller;
import model.cards.Card;
import model.cards.minions.Minion;
import model.heroes.Hero;

public class FieldPanel {

	Hero current;
	Hero opponent;
	JPanel field = new JPanel(new GridLayout(2, 1));

	public FieldPanel(Hero c, Hero o, Controller controller) {
		current = c;
		opponent = o;
		JPanel curField = new JPanel();
		JPanel oppField = new JPanel();
		field.setOpaque(false);
		curField.setOpaque(false);
		oppField.setOpaque(false);
		
		oppField.setPreferredSize( new Dimension((int)(960*HearthstoneView.width),(int)(182*HearthstoneView.height)));
		oppField.setSize( new Dimension((int)(960*HearthstoneView.width),(int)(182*HearthstoneView.height)));
		curField.setPreferredSize( new Dimension((int)(960*HearthstoneView.width),(int)(185*HearthstoneView.height)));
		curField.setSize( new Dimension((int)(960*HearthstoneView.width),(int)(185*HearthstoneView.height)));
		
		
//		oppField.setBackground(new Color(245,245,220));
//		curField.setBackground(new Color(245,245,220));
//		field.setBackground(new Color(245,245,220));
		
		field.add(oppField);
		field.add(curField);
		
		for (Card card : o.getField()) {
			if (card instanceof Minion) {
				JPanel minion = (new MinionPanel((Minion) card, controller, "opponent")).getPanel();
				oppField.add(minion);
				controller.getOppField().add(minion);
			} else {
				JPanel fim = new JPanel();
				ImageIcon imageIcon = Controller.getImage("Images/" + card.getName() + ".png", 120, 170);
				fim.add(new JLabel(imageIcon));
				oppField.add(fim);
			}
		}
		for (Card card : c.getField()) {
			if (card instanceof Minion) {
				JPanel minion = (new MinionPanel((Minion) card, controller, "current")).getPanel();
				curField.add(minion);
				controller.getCurField().add(minion);
			} else {
				JPanel fim = new JPanel();
				ImageIcon imageIcon = Controller.getImage("Images/" + card.getName() + ".png", 120, 171);
				fim.add(new JLabel(imageIcon));
				curField.add(fim);
			}
		}

	}

	public JPanel getField() {
		return field;
	}

}
