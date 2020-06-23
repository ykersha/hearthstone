package view;


import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import engine.Controller;
import engine.HandMouse;
import model.cards.Card;
import model.cards.minions.Minion;

public class HandCard {

	Card card;
	JPanel panel;
	Controller controller;

	public HandCard(Card card, Controller controller) {
		this.card = card;
		this.controller = controller;

		panel = new JPanel();
		panel.setPreferredSize(new Dimension((int)(120*HearthstoneView.width),(int)(177*HearthstoneView.height)));
		panel.setSize(new Dimension((int)(120*HearthstoneView.width),(int)(177*HearthstoneView.height)));

		String s = card.getName();
		if (s.equals("Shadow Word: Death"))
			s = "Shadow Word Death";

		JLabel image = new JLabel(Controller.getImage("Images/" + s + ".png", 120, 171));
		panel.add(image);

		JPanel labels = new JPanel();
		labels.setPreferredSize(new Dimension((int)(120*HearthstoneView.width),(int)(171*HearthstoneView.height)));
		labels.setSize(new Dimension((int)(120*HearthstoneView.width),(int)(171*HearthstoneView.height)));

		BoxLayout layout = new BoxLayout(labels, BoxLayout.Y_AXIS);
		labels.setLayout(layout);

		JLabel l1 = new JLabel(card.getName());
		l1.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		if (l1.getText().equals("Colossus of the Moon")) {
			l1.setText("Colossus of ");
			JLabel l1c = new JLabel("the Moon");
			l1c.setAlignmentX(Component.CENTER_ALIGNMENT);
			labels.add(l1);
			labels.add(l1c);
		} else
			labels.add(l1);

		JLabel l2 = new JLabel("" + card.getRarity());
		l2.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel l3 = new JLabel("Mana Cost: " + card.getManaCost());
		l3.setAlignmentX(Component.CENTER_ALIGNMENT);

		
		labels.add(l2);
		labels.add(l3);
		if (card instanceof Minion) {
			Minion m = (Minion) card;
			
			JLabel l4 = new JLabel("HP: " + m.getCurrentHP() + "/" + m.getMaxHP());
			l4.setAlignmentX(Component.CENTER_ALIGNMENT);
			labels.add(l4);
			
			JLabel l7 = new JLabel("Attack: " + m.getAttack());
			l7.setAlignmentX(Component.CENTER_ALIGNMENT);
			labels.add(l7);
			

			if (!m.isSleeping()) {
				JLabel l5 = new JLabel("Charge");
				l5.setAlignmentX(Component.CENTER_ALIGNMENT);
				labels.add(l5);
			}
			if (m.isDivine()) {
				JLabel l6 = new JLabel("Divine");
				l6.setAlignmentX(Component.CENTER_ALIGNMENT);
				labels.add(l6);
			}
			if (m.isTaunt()) {
				JLabel l5 = new JLabel("Taunt");
				l5.setAlignmentX(Component.CENTER_ALIGNMENT);
				labels.add(l5);
			}
		}

		if(card == controller.getSpell())
		{
			image.setBorder(BorderFactory.createEtchedBorder());
			labels.setBorder(BorderFactory.createLoweredBevelBorder());
		}
		labels.setVisible(false);
		panel.add(labels);
		
		labels.setBackground(new Color(255, 255, 255, 75));

		panel.addMouseListener(new HandMouse(image, labels, controller));
	}

	public JPanel getPanel() {
		return panel;
	}

	public void setPanel(JPanel panel) {
		this.panel = panel;
	}

}
