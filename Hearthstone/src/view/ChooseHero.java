package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import engine.Controller;

public class ChooseHero {

	private Controller control;
	private JPanel view;
	private JLabel label;
	
	public ChooseHero(Controller control) {
		this.control = control;
		
		JPanel center = new JPanel();
		center.setOpaque(false);
		BoxLayout layout = new BoxLayout(center, BoxLayout.Y_AXIS);
		center.setLayout(layout);
		
		
		view = new JPanel();
		view.setOpaque(false);
		JPanel chooseHero = new JPanel();
		
		label = new JLabel("Choose Player One");
		//label.setFont(new Font("SansSerif", Font.BOLD, (int)(40*HearthstoneView.width)));
		

		Font customFont = null;
		try {
		    customFont = Font.createFont(Font.TRUETYPE_FONT, new File("Images/belwe bold bt.ttf")).deriveFont((float)(50*HearthstoneView.width));
		    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		    //register the font
		    ge.registerFont(customFont);
		} catch (IOException e) {
		    e.printStackTrace();
		} catch(FontFormatException e) {
		    e.printStackTrace();
		}

		//use the font
		label.setFont(customFont);
		
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		center.add(label);

		JPanel m = new JPanel();
		m.setOpaque(false);
		m.setLayout(new GridLayout(2,1));
		JButton mage = new JButton();
		mage.setContentAreaFilled(false);
		mage.setActionCommand("mage");
		mage.setIcon(getImage("Images/Jaina Proudmoore.png"));
		mage.addActionListener(control);
		m.add(mage);
		JLabel mlabel = new JLabel("Mage");
		mlabel.setFont(customFont.deriveFont((float)(25*HearthstoneView.width)));
		mlabel.setHorizontalAlignment(SwingConstants.CENTER);
		mlabel.setVerticalAlignment(SwingConstants.NORTH);
		m.add(mlabel);
		chooseHero.add(m);

		JButton hunter = new JButton();
		hunter.setContentAreaFilled(false);
		JPanel h = new JPanel();
		h.setOpaque(false);
		h.setLayout(new GridLayout(2,1));
		hunter.setActionCommand("hunter");
		hunter.setIcon(getImage("Images/Rexxar.png"));
		hunter.addActionListener(control);
		h.add(hunter);
		JLabel hlabel = new JLabel("Hunter");
		hlabel.setFont(customFont.deriveFont((float)(25*HearthstoneView.width)));
		hlabel.setHorizontalAlignment(SwingConstants.CENTER);
		hlabel.setVerticalAlignment(SwingConstants.NORTH);
		h.add(hlabel);
		chooseHero.add(h);

		JButton paladin = new JButton();
		paladin.setContentAreaFilled(false);
		JPanel p = new JPanel();
		p.setOpaque(false);
		p.setLayout(new GridLayout(2,1));
		paladin.setActionCommand("paladin");
		paladin.setIcon(getImage("Images/Uther Lightbringer.png"));
		paladin.addActionListener(control);
		p.add(paladin);
		JLabel plabel = new JLabel("Paladin");
		plabel.setFont(customFont.deriveFont((float)(25*HearthstoneView.width)));
		plabel.setHorizontalAlignment(SwingConstants.CENTER);
		plabel.setVerticalAlignment(SwingConstants.NORTH);
		p.add(plabel);
		chooseHero.add(p);

		JButton warlock = new JButton();
		warlock.setContentAreaFilled(false);
		JPanel w = new JPanel();
		w.setOpaque(false);
		w.setLayout(new GridLayout(2,1));
		warlock.setActionCommand("warlock");
		warlock.setIcon(getImage("Images/Gul'dan.png"));
		warlock.addActionListener(control);
		w.add(warlock);
		JLabel wlabel = new JLabel("Warlock");
		wlabel.setFont(customFont.deriveFont((float)(25*HearthstoneView.width)));
		wlabel.setHorizontalAlignment(SwingConstants.CENTER);
		wlabel.setVerticalAlignment(SwingConstants.NORTH);
		w.add(wlabel);
		chooseHero.add(w);

		JButton priest = new JButton();
		priest.setContentAreaFilled(false);
		JPanel pr = new JPanel();
		pr.setOpaque(false);
		pr.setLayout(new GridLayout(2,1));
		priest.setActionCommand("priest");
		priest.setIcon(getImage("Images/Anduin Wrynn.png"));
		priest.addActionListener(control);
		pr.add(priest);
		JLabel prlabel = new JLabel("Priest");
		prlabel.setFont(customFont.deriveFont((float)(25*HearthstoneView.width)));
		prlabel.setHorizontalAlignment(SwingConstants.CENTER);
		prlabel.setVerticalAlignment(SwingConstants.NORTH);
		pr.add(prlabel);
		chooseHero.add(pr);

		chooseHero.setOpaque(false);
		center.add(chooseHero);
		
		Controller.playAudio("Sounds/VO_INNKEEPER2_INTRO_02.wav");
		

		JLabel x=new JLabel(Controller.getImage("Images/hearthstone-logo.png", 600,250));
//		x.setPreferredSize();
		view.add(x,BorderLayout.NORTH);
		view.add(center, BorderLayout.CENTER);
	
		
		view.repaint();
		view.revalidate();

	}

	public static ImageIcon getImage(String s) {
		return Controller.getImage(s,200,225);
	}

	public Controller getControl() {
		return control;
	}

	public void setControl(Controller control) {
		this.control = control;
	}

	public JPanel getView() {
		return view;
	}

	public JLabel getLabel() {
		return label;
	}

	public void setLabelText(String s) {
		this.label.setText(s);
	}

	

}
