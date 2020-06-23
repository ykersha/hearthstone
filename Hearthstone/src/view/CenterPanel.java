package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JPanel;

import engine.Controller;
import model.heroes.Hero;

public class CenterPanel {
	
	private JPanel center = new JPanel();
	private Controller controller; 
	 JButton endTurn;
	JPanel field;
	JPanel heroes;
	
	public CenterPanel(Hero c, Hero o, Controller controller) {
	
		this.setController(controller);
		field = (new FieldPanel(c,o,controller).getField());
		heroes = new JPanel(new GridLayout(2, 2));
		heroes.setOpaque(false);
		center.setOpaque(false);
		center.add(field, BorderLayout.WEST);
		center.add(heroes, BorderLayout.EAST);
		
		
		
//		JPanel status = new JPanel();
//		BoxLayout layout =  new BoxLayout(status, BoxLayout.Y_AXIS);
//		status.setLayout(layout);
//		status.add(new JLabel("Selected Card:"));
//		JLabel l = new JLabel("");
//		if(controller.getSpell() != null)
//			l.setText(controller.getSpell().getName());
//		else if(controller.getAttacker() != null)
//			l.setText(controller.getAttacker().getName());
//		status.add(l);
//		JButton deselect = new JButton("Deselect");
//		deselect.addActionListener(controller);
//		status.add(deselect);	
		
		
		JPanel endTurnPanel = new JPanel();
		endTurnPanel.setOpaque(false);
		endTurnPanel.setLayout(new GridBagLayout());
		endTurn = new JButton("End Turn");
		
		Font customFont = null;
		try {
		    customFont = Font.createFont(Font.TRUETYPE_FONT, new File("Images/belwe bold bt.ttf")).deriveFont((float)(15*HearthstoneView.width));
		    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		    //register the font
		    ge.registerFont(customFont);
		} catch (IOException e1) {
		    e1.printStackTrace();
		} catch(FontFormatException e1) {
		    e1.printStackTrace();
		}
		
		endTurn.setFont(customFont);
		
		endTurn.setActionCommand("endTurn");
		endTurn.addActionListener(controller);
		endTurn.setPreferredSize(new Dimension((int)(150*HearthstoneView.width),(int)(50*HearthstoneView.height)));
		endTurn.setMaximumSize(new Dimension((int)(150*HearthstoneView.width),(int)(50*HearthstoneView.height)));
		endTurn.setBackground(new Color(77, 82, 128));
		endTurn.setForeground(Color.BLACK);
		endTurnPanel.add(endTurn);
		
//		endTurnPanel.add(status, BorderLayout.NORTH);
		
		heroes.add(endTurnPanel, BorderLayout.SOUTH);

		JPanel oppHero = (new HeroPanel(o, controller).getHeroPanel());
		heroes.add(oppHero);

		JButton heroPower = new JButton();
		heroPower.setActionCommand("useHeroPower");
	//	heroPower.setToolTipText("Use Hero Power");
		heroPower.addActionListener(controller);
		heroPower.setIcon(Controller.getImage("Images/Hero Power " + c.getName() + ".png", 123, 171));
		heroPower.setContentAreaFilled(false);
		heroes.add(heroPower);
		
		JPanel curHero = (new HeroPanel(c,controller).getHeroPanel());
		heroes.add(curHero);
		
		

	}
	
	public JPanel getCenter() {
		return center;
	}

	public Controller getController() {
		return controller;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}


}
