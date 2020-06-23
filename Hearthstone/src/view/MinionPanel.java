package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import engine.Controller;
import engine.MinionMouse;
import model.cards.minions.Minion;
import model.cards.spells.MinionTargetSpell;

public class MinionPanel {

	JPanel panel = new JPanel(null);
	Minion minion;
	Controller controller;
	String position;

	public MinionPanel(Minion m, Controller c, String p) {
		minion = m;
		controller = c;

		JPanel imageP = new JPanel(null);

		imageP.setOpaque(false);

		imageP.setSize(new Dimension((int) (120 * HearthstoneView.width), (int) (171 * HearthstoneView.height)));
		imageP.setPreferredSize(
				new Dimension((int) (120 * HearthstoneView.width), (int) (171 * HearthstoneView.height)));

		panel.setName(p);

		panel.setSize(new Dimension((int) (120 * HearthstoneView.width), (int) (171 * HearthstoneView.height)));
		panel.setPreferredSize(
				new Dimension((int) (120 * HearthstoneView.width), (int) (171 * HearthstoneView.height)));

		Font customFont = null;
		try {
			customFont = Font.createFont(Font.TRUETYPE_FONT, new File("Images/belwe bold bt.ttf"))
					.deriveFont((float) (25 * HearthstoneView.width));
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			// register the font
			ge.registerFont(customFont);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FontFormatException e) {
			e.printStackTrace();
		}

		//// Drop of Blood showing the current HP
		if (!m.isSleeping() || p.equals("opponent")) {
			OutlineLabel currentHP = new OutlineLabel("" + m.getCurrentHP());
			currentHP.setForeground(Color.BLACK);
			// currentHP.setFont(new Font("SansSerif", Font.BOLD, 22));
			currentHP.setForeground(Color.white);
			currentHP.setAlignmentX(Component.CENTER_ALIGNMENT);

			// use the font
			currentHP.setFont(customFont);

			imageP.add(currentHP);
			if (m.getCurrentHP() >= 10)
				currentHP.setBounds((int) (91 * HearthstoneView.width), (int) (136 * HearthstoneView.height),
						(int) (35 * HearthstoneView.width), (int) (32 * HearthstoneView.height));
			else
				currentHP.setBounds((int) (97 * HearthstoneView.width), (int) (136 * HearthstoneView.height),
						(int) (25 * HearthstoneView.width), (int) (32 * HearthstoneView.height));

			ImageIcon hpImage = Controller.getImage("Images/blood.png", 50, 50);
			JLabel hp = new JLabel(hpImage);
			imageP.add(hp);
			hp.setBounds((int) (93 * HearthstoneView.width), (int) (133 * HearthstoneView.height),
					(int) (23 * HearthstoneView.width), (int) (38 * HearthstoneView.height));

		}

		// adding the attack
		if (!m.isSleeping() || p.equals("opponent")) {
			OutlineLabel attack = new OutlineLabel("" + m.getAttack());
			attack.setForeground(Color.BLACK);
			// currentHP.setFont(new Font("SansSerif", Font.BOLD, 22));
			attack.setForeground(Color.white);
			attack.setAlignmentX(Component.CENTER_ALIGNMENT);

			// use the font
			attack.setFont(customFont);

			imageP.add(attack);
			if (m.getAttack() >= 10)
				attack.setBounds((int) (6 * HearthstoneView.width), (int) (138 * HearthstoneView.height),
						(int) (35 * HearthstoneView.width), (int) (32 * HearthstoneView.height));
			else
				attack.setBounds((int) (13 * HearthstoneView.width), (int) (138 * HearthstoneView.height),
						(int) (25 * HearthstoneView.width), (int) (32 * HearthstoneView.height));

			ImageIcon attackImage = Controller.getImage("Images/attack.png", 42, 42);
			JLabel att = new JLabel(attackImage);
			imageP.add(att);
			att.setBounds((int) (-2 * HearthstoneView.width), (int) (133 * HearthstoneView.height),
					(int) (40 * HearthstoneView.width), (int) (40 * HearthstoneView.height));

		}

		JLabel imageColor = new JLabel(Controller.getImage("Images/" + m.getName() + ".png", 120, 170));
		JLabel imageGray = new JLabel(Controller.getGrayImage("Images/" + m.getName() + ".png", 120, 170));

		JLabel image;

		if (m.isSleeping() && p.equals("current"))
			image = imageGray;
		else
			image = imageColor;

		if (m.isDivine()) {
			JLabel div = new JLabel(Controller.getImage("Images/shield40.png", 120, 170));
			panel.add(div);
			div.setOpaque(false);
			div.setBounds(0, 0, (int) (120 * HearthstoneView.width), (int) (170 * HearthstoneView.height));
		}

		panel.add(imageP);
		imageP.add(image);
		image.setBounds(0, 0, (int) (120 * HearthstoneView.width), (int) (170 * HearthstoneView.height));

//		panel.setBorder(BorderFactory.createLineBorder(Color.black));	
//		JTextArea text = new JTextArea();
//		text.setEditable(false);
//		text.setLineWrap(true);
//		text.setWrapStyleWord(true);
//		text.setOpaque(false);
//		text.setFont(new Font("SansSerif", Font.ITALIC, 13));
//		
//		String s = minion.getName() +"\nMana Cost: "+minion.getManaCost()+"\nAttack: "+
//				minion.getAttack() + "\nHP: " +minion.getMaxHP() + "\n"+ minion.getRarity();
//		JPanel labels = new JPanel();
//		BoxLayout layout =  new BoxLayout(labels, BoxLayout.Y_AXIS);
//		labels.setLayout(layout);
//		labels.add(new JLabel(minion.getName()));
//		labels.add(new JLabel("Mana Cost: "+minion.getManaCost() ));
//		labels.add(new JLabel("Attack: " + minion.getAttack()));
//		labels.add(new JLabel("HP: " + minion.getCurrentHP() + "/" + minion.getMaxHP()));
//		labels.add(new JLabel(""+minion.getRarity()));
//		
//		if(minion.isDivine())
//			labels.add(new JLabel("Divine"));
//		if(!minion.isSleeping())
//			labels.add(new JLabel("Charge"));
//		if(minion.isTaunt())
//			labels.add(new JLabel("Taunt"));;

		JPanel labels = new JPanel();

		BoxLayout layout = new BoxLayout(labels, BoxLayout.Y_AXIS);
		labels.setLayout(layout);

		JLabel l1 = new JLabel(m.getName());
		l1.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel l2 = new JLabel("" + m.getRarity());
		l2.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel l3 = new JLabel("Mana Cost: " + m.getManaCost());
		l3.setAlignmentX(Component.CENTER_ALIGNMENT);

		if (l1.getText().equals("Colossus of the Moon")) {
			l1.setText("Colossus of ");
			JLabel l1c = new JLabel("the Moon");
			l1c.setAlignmentX(Component.CENTER_ALIGNMENT);
			labels.add(l1);
			labels.add(l1c);
		} else
			labels.add(l1);

		labels.add(l2);
		labels.add(l3);

		JLabel l4 = new JLabel("HP: " + m.getCurrentHP() + "/" + m.getMaxHP());
		l4.setAlignmentX(Component.CENTER_ALIGNMENT);
		labels.add(l4);

		JLabel l7 = new JLabel("Attack: " + m.getAttack());
		l7.setAlignmentX(Component.CENTER_ALIGNMENT);
		labels.add(l7);

		if (m.isSleeping()) {
			JLabel l5 = new JLabel("Sleeping");
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

		labels.add(new JLabel("  "));
		JPanel otherFace = new JPanel();
		otherFace.setPreferredSize(
				new Dimension((int) (120 * HearthstoneView.width), (int) (170 * HearthstoneView.height)));
		otherFace.setSize(new Dimension((int) (120 * HearthstoneView.width), (int) (170 * HearthstoneView.height)));
		BoxLayout layout2 = new BoxLayout(otherFace, BoxLayout.Y_AXIS);
		otherFace.setLayout(layout2);

		labels.setOpaque(false);
		otherFace.add(labels);

		JPanel attack = new JPanel();
		attack.setBackground(new Color(77, 82, 128));
		attack.setVisible(false);
		JLabel attLabel = new JLabel();
		attack.add(attLabel);

		if (p.equals("current")) {
			attLabel.setText("Attack");
			attack.setVisible(true);
		}
		if ((p.equals("opponent") && controller.getAttacker() != null) || controller.isHeroPowerPressed()
				|| controller.getSpell() instanceof MinionTargetSpell) {
			attLabel.setText("Target");
			attack.setVisible(true);
		}

		JPanel att = new JPanel();
		att.setOpaque(false);
		att.add(attack);
		otherFace.add(att);

		if (m.isSleeping() && p.equals("current"))
			att.setVisible(false);

		otherFace.setVisible(false);

		if (m == controller.getAttacker()) {
			image.setBorder(BorderFactory.createEtchedBorder());
			otherFace.setBorder(BorderFactory.createLoweredBevelBorder());
		}

		panel.setOpaque(false);
		otherFace.setBackground(new Color(255, 255, 255, 75));

		panel.add(otherFace, BorderLayout.WEST);

		panel.addMouseListener(new MinionMouse(imageP, otherFace, controller));

	}

	public JPanel getPanel() {
		return panel;
	}

	public void setPanel(JPanel panel) {
		this.panel = panel;
	}

	public Controller getController() {
		return controller;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}

}
