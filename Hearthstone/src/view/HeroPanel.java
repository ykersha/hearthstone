package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import engine.Controller;
import engine.HeroMouse;
import model.cards.spells.HeroTargetSpell;
import model.heroes.Hero;

public class HeroPanel {

	JPanel oppHero;
	JLabel oppImage;
	Controller controller;

	public HeroPanel(Hero o, Controller controller) {
		this.controller = controller;
		oppHero = new JPanel(null);

		Font customFont = null;
		try {
			customFont = Font.createFont(Font.TRUETYPE_FONT, new File("Images/belwe bold bt.ttf"))
					.deriveFont((float) (50 * HearthstoneView.width));
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			// register the font
			ge.registerFont(customFont);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FontFormatException e) {
			e.printStackTrace();
		}

		oppHero.setPreferredSize(
				new Dimension((int) (146 * HearthstoneView.width), (int) (171 * HearthstoneView.height)));
		oppHero.setSize(new Dimension((int) (146 * HearthstoneView.width), (int) (171 * HearthstoneView.height)));

		ImageIcon oppnewimg = Controller.getImage("Images/" + o.getName() + ".png", 146, 171);
		oppImage = new JLabel(oppnewimg);

		OutlineLabel currentHP = new OutlineLabel("" + o.getCurrentHP());
		currentHP.setFont(customFont.deriveFont((float) (19 * HearthstoneView.width)));
		currentHP.setAlignmentX(Component.CENTER_ALIGNMENT);
		currentHP.setForeground(Color.WHITE);
		oppHero.add(currentHP);
		if (o.getCurrentHP() >= 10)
			currentHP.setBounds((int) (122 * HearthstoneView.width), (int) (145 * HearthstoneView.height),
					(int) (30 * HearthstoneView.width), (int) (25 * HearthstoneView.height));
		else {
			currentHP.setFont(customFont.deriveFont((float) (22 * HearthstoneView.width)));
			currentHP.setBounds((int) (126 * HearthstoneView.width), (int) (145 * HearthstoneView.height),
					(int) (30 * HearthstoneView.width), (int) (25 * HearthstoneView.height));
		}
		ImageIcon hpImage = Controller.getImage("Images/blood.png", 50, 50);
		JLabel hp = new JLabel(hpImage);
		oppHero.add(hp);
		hp.setBounds((int) (102 * HearthstoneView.width), (int) (124 * HearthstoneView.height),
				(int) (60 * HearthstoneView.width), (int) (60 * HearthstoneView.height));

		if (o.equals(controller.getC())) {
			OutlineLabel currentMana = new OutlineLabel("" + o.getCurrentManaCrystals());
			currentMana.setFont(customFont.deriveFont((float) (22 * HearthstoneView.width)));
			currentMana.setForeground(Color.WHITE);
			currentMana.setAlignmentX(Component.CENTER_ALIGNMENT);
			oppHero.add(currentMana);
			if (o.getCurrentManaCrystals() == 10)
				currentMana.setBounds((int) (3 * HearthstoneView.width), (int) (140 * HearthstoneView.height),
						(int) (30 * HearthstoneView.width), (int) (30 * HearthstoneView.height));
			else
				currentMana.setBounds((int) (9 * HearthstoneView.width), (int) (140 * HearthstoneView.height),
						(int) (30 * HearthstoneView.width), (int) (30 * HearthstoneView.height));

			ImageIcon crystalIm = Controller.getImage("Images/mana.png", 30, 30);
			JLabel crystal = new JLabel(crystalIm);
			oppHero.add(crystal);
			crystal.setBounds(0, (int) (141 * HearthstoneView.height), (int) (30 * HearthstoneView.width),
					(int) (30 * HearthstoneView.height));
		}

		oppHero.add(oppImage);
		oppImage.setBounds(0, 0, (int) (146 * HearthstoneView.width), (int) (171 * HearthstoneView.height));

		JPanel labels = new JPanel();

		labels.setPreferredSize(
				new Dimension((int) (145 * HearthstoneView.width), (int) (168 * HearthstoneView.height)));
		labels.setSize(new Dimension((int) (145 * HearthstoneView.width), (int) (168 * HearthstoneView.height)));
		BoxLayout layout = new BoxLayout(labels, BoxLayout.Y_AXIS);
		labels.setLayout(layout);

		JLabel l1 = new JLabel(o.getName());
		JLabel l2 = new JLabel(o.getCurrentManaCrystals() + "/" + o.getTotalManaCrystals() + " mana crystals");
		JLabel l3 = new JLabel("HP: " + o.getCurrentHP());
		JLabel l4 = new JLabel("Cards in deck: " + o.getDeck().size());
		l1.setAlignmentX(Component.CENTER_ALIGNMENT);
		l2.setAlignmentX(Component.CENTER_ALIGNMENT);
		l3.setAlignmentX(Component.CENTER_ALIGNMENT);
		l4.setAlignmentX(Component.CENTER_ALIGNMENT);

		labels.add(new JLabel(" "));
		labels.add(l1);
		labels.add(l2);
		labels.add(l3);
		labels.add(l4);
		labels.add(new JLabel(" "));
		labels.setVisible(false);

		oppHero.add(labels);
		oppHero.setOpaque(false);

		if (o.equals(controller.getO())) {
			oppHero.setName("oppHero");
		}

		if (o.equals(controller.getC())) {
			oppHero.setName("curHero");
		}

		if (controller.getAttacker() != null || controller.getSpell() instanceof HeroTargetSpell || controller.isHeroPowerPressed()) {
			JPanel attack = new JPanel();
			attack.setBackground(new Color(77, 82, 128));
			attack.add(new JLabel("Target"));
			JPanel att = new JPanel();
			att.setOpaque(false);
			att.add(attack);
			labels.add(att);
		}
//			oppHero.setOpaque(false);

		labels.setBackground(new Color(255, 255, 255, 75));

		oppHero.addMouseListener(new HeroMouse(labels, oppImage, controller));
	}

	public JPanel getHeroPanel() {
		return oppHero;
	}

	public void setOppHero(JPanel oppHero) {
		this.oppHero = oppHero;
	}

}
