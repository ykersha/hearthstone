package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import engine.Controller;
import engine.KeyboardListener;
import model.cards.Card;
import model.cards.minions.Minion;
import model.heroes.Hero;


public class HearthstoneView extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel currentHero = new JPanel();
	private JPanel opponent = new JPanel();
	private JPanel center = new JPanel();
	private Card burned;
	private Hero c;
	private Hero o;
	private Controller controller;
	private ArrayList<JButton> handCards = new ArrayList<JButton>();
	
	public static double width = Toolkit.getDefaultToolkit().getScreenSize().getWidth()/1366;
	public static double height = Toolkit.getDefaultToolkit().getScreenSize().getHeight()/768;
	
	boolean off=false;
	
	public HearthstoneView(Controller controller) {
		
		System.out.println(Toolkit.getDefaultToolkit().getScreenSize().getWidth() +"x"+Toolkit.getDefaultToolkit().getScreenSize().getHeight() );
		
		this.controller = controller;
		this.setFocusable(true);
		this.addKeyListener(new KeyboardListener(controller));
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Hearthstone");
		makeFrameFullSize(this);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setIconImage((new ImageIcon("Images/icon.png").getImage()));
		
		currentHero.setOpaque(false);
		opponent.setOpaque(false);
		center.setOpaque(false);
		
		repaint();
		revalidate();
	
		setContentPane(new JLabel(Controller.getImage("Images/background.jpg", (int)(1366*width), (int)(768*height))));
		
		setLayout(new BorderLayout());
		setVisible(true);

	}
	
	public void makeFrameFullSize(JFrame aFrame) {
		aFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
	    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    aFrame.setSize(screenSize.width, screenSize.height);
	}

	public Hero getC() {
		return c;
	}

	public void setC(Hero c) {
		this.c = c;
	}

	public Hero getO() {
		return o;
	}

	public void setO(Hero o) {
		this.o = o;
	}

//	public static void main(String[] args) {
//		new HearthstoneView(controller);
//	}

	public void startGame() {

		this.getContentPane().removeAll();
		
		revalidate();
		repaint();

		center.add(new CenterPanel(c, o, this.controller).getCenter());
		add(center, BorderLayout.EAST);

		currentHero.setLayout(new GridLayout(0, 10));
		add(currentHero, BorderLayout.SOUTH);

		controller.getHand().clear();
		for (Card m : c.getHand()) {
			JPanel b = (new HandCard(m, controller)).getPanel();
			b.setOpaque(false);
			controller.getHand().add(b);
			currentHero.add(b);
		}

		opponent.setLayout(new GridLayout(0, 10));
		add(opponent, BorderLayout.NORTH);
		for (@SuppressWarnings("unused") Card card : o.getHand()) {
			JPanel con = new JPanel();
			con.setOpaque(false);
			ImageIcon imageIcon = Controller.getImage("Images/Card Back.png", 96, 135);
			con.add(new JLabel(imageIcon));
			opponent.add(con);
		}
			
		Controller.playAudioLoop("Sounds/Main_Title.wav");

		revalidate();
		repaint();
	}

	public void updateHand() {

		currentHero.removeAll();
		controller.getCurField().clear();
		controller.getOppField().clear();
		handCards = new ArrayList<JButton>();
		repaint();
		revalidate();

		controller.getHand().clear();
		for (Card m : c.getHand()) {
			JPanel b = (new HandCard(m, controller)).getPanel();
			b.setOpaque(false);
			controller.getHand().add(b);
			currentHero.add(b);
		}

		center.removeAll();

		center.add((new CenterPanel(c, o, this.controller)).getCenter());
		revalidate();
		repaint();
	}

	public void nextTurn() {

		center.removeAll();
		if(burned != null) {
			cardBurned(burned);
		}
		
		currentHero.removeAll();
		opponent.removeAll();
		controller.getCurField().clear();
		controller.getOppField().clear();

		handCards = new ArrayList<JButton>();
		revalidate();
		repaint();

		System.out.println(c.getName());

		center.add(new CenterPanel(c, o, this.controller).getCenter());
		add(center, BorderLayout.EAST);

		currentHero.setLayout(new GridLayout(0, 10));
		add(currentHero, BorderLayout.SOUTH);

		controller.getHand().clear();
		for (Card m : c.getHand()) {
			JPanel b = (new HandCard(m, controller)).getPanel();
			b.setOpaque(false);
			controller.getHand().add(b);
			currentHero.add(b);
		}

		opponent.setLayout(new GridLayout(0, 10));
		add(opponent, BorderLayout.NORTH);
		for (@SuppressWarnings("unused") Card card : o.getHand()) {
			JPanel con = new JPanel();
			con.setOpaque(false);
			ImageIcon imageIcon = Controller.getImage("Images/Card Back.png", 96, 135);
			con.add(new JLabel(imageIcon));
			opponent.add(con);
		}
		revalidate();
		repaint();

	}

	public void gameOver(Hero winner, Hero loser) {
		off= true;
		JFrame frame = this;
		currentHero.removeAll();
		opponent.removeAll();
		getContentPane().removeAll();
		this.getContentPane().removeAll();
		Controller.mainTitle.stop();
		
		repaint();
		revalidate();

		this.setLayout(new GridBagLayout());
		JPanel loserPanel = new JPanel();
		loserPanel.setOpaque(false);
		loserPanel.setSize(frame.getWidth(), frame.getHeight());
		BoxLayout layout = new BoxLayout(loserPanel, BoxLayout.Y_AXIS);
		loserPanel.setLayout(layout);
		JLabel loserImg = new JLabel(Controller.getImage("Images/" + loser.getName() + ".png", 200, 225));
		loserImg.setAlignmentX(Component.CENTER_ALIGNMENT);
		loserPanel.add(loserImg);
		
		
		JLabel loserLabel = new JLabel("Loser!!!");
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
		loserLabel.setFont(customFont);
		loserLabel.setHorizontalAlignment(SwingConstants.CENTER);
		loserLabel.setVerticalAlignment(SwingConstants.NORTH);
		loserLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		loserPanel.add(loserLabel);
		add(loserPanel);
		repaint();
		revalidate();

		
		
		Controller.playAudio("Sounds/HERO_07_Death.wav");
		
		Controller.playAudio("Sounds/defeat_jingle.wav");

		ActionListener delay = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.getContentPane().removeAll();
				frame.repaint();
				frame.revalidate();
				
				Controller.playAudio("Sounds/victory_fireworks.wav");
				Controller.playAudio("Sounds/victory_screen_start.wav");
				Controller.playAudio("Sounds/victory_jingle.wav");
				
				
				JPanel winPanel = new JPanel();
				winPanel.setOpaque(false);
				winPanel.setSize(frame.getWidth(), frame.getHeight());

				BoxLayout layout = new BoxLayout(winPanel, BoxLayout.Y_AXIS);
				winPanel.setLayout(layout);
				JLabel winImg = new JLabel(Controller.getImage("Images/" + winner.getName() + ".png", 200, 225));
				winImg.setAlignmentX(Component.CENTER_ALIGNMENT);
				winPanel.add(winImg);
				JLabel winLabel = new JLabel("Winner winner chicken dinner!");
				Font customFont = null;
				try {
				    customFont = Font.createFont(Font.TRUETYPE_FONT, new File("Images/belwe bold bt.ttf")).deriveFont((float)(50*HearthstoneView.width));
				    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
				    //register the font
				    ge.registerFont(customFont);
				} catch (IOException e1) {
				    e1.printStackTrace();
				} catch(FontFormatException e1) {
				    e1.printStackTrace();
				}
				
				winLabel.setFont(customFont);
				winLabel.setHorizontalAlignment(SwingConstants.CENTER);
				winLabel.setVerticalAlignment(SwingConstants.NORTH);
				winLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
				winPanel.add(winLabel);
				
				JPanel playAgainPanel = new JPanel();
				playAgainPanel.setOpaque(false);
				playAgainPanel.setLayout(new GridBagLayout());
				JButton playAgain = new JButton("Play Again?");
				playAgain.setFont(customFont.deriveFont((float)(20*HearthstoneView.width)));
				playAgain.setActionCommand("playAgain");
				playAgain.addActionListener(controller);
				playAgain.setPreferredSize(new Dimension((int)(150*HearthstoneView.width),(int)(50*HearthstoneView.height)));
				playAgain.setMaximumSize(new Dimension((int)(150*HearthstoneView.width),(int)(50*HearthstoneView.height)));
				playAgain.setBackground(new Color(77, 82, 128));
//				playAgain.setForeground(Color.BLACK);
				playAgainPanel.add(playAgain);
				playAgain.setAlignmentX(Component.CENTER_ALIGNMENT);
				winPanel.add(playAgainPanel);
				
				frame.add(winPanel);
				
				frame.repaint();
				frame.revalidate();
			}
		};

		Timer timer = new Timer(10000, delay);
		timer.setRepeats(false);
		timer.start();

	}

	public void cardBurned(Card card) {

		center.removeAll();
		center.setVisible(false);
		repaint();
		revalidate();
	
		//Controller.playAudio("Sounds/Fatigue_card_draw.wav");
		
		JPanel center1 = new JPanel();
		center1.setOpaque(false);
		center1.add(new JLabel("                                  ")); //lazy way to push image to right 

		BoxLayout xlayout = new BoxLayout(center1, BoxLayout.X_AXIS);
		center1.setLayout(xlayout);	
		JLabel image = new JLabel(Controller.getImage("Images/" + card.getName() + ".png", 240, 340));
		center1.add(image);
		JPanel labels = new JPanel();
		labels.setOpaque(false);
		BoxLayout layout = new BoxLayout(labels, BoxLayout.Y_AXIS);
		labels.setLayout(layout);
		JLabel l = new JLabel("Card Burned");
		l.setAlignmentX(Component.CENTER_ALIGNMENT);
		l.setFont(new Font("SansSerif", Font.BOLD, 30));
		JLabel l1 = new JLabel(card.getName());
		l1.setAlignmentX(Component.CENTER_ALIGNMENT);
		l1.setFont(new Font("SansSerif", Font.PLAIN, 20));

		JLabel l2 = new JLabel("" + card.getRarity());
		l2.setAlignmentX(Component.CENTER_ALIGNMENT);
		l2.setFont(new Font("SansSerif", Font.PLAIN, 20));

		JLabel l3 = new JLabel("Mana Cost: " + card.getManaCost());
		l3.setAlignmentX(Component.CENTER_ALIGNMENT);
		l3.setFont(new Font("SansSerif", Font.PLAIN, 20));

		labels.add(l);
		labels.add(l1);
		labels.add(l2);
		labels.add(l3);
		if (card instanceof Minion) {
			Minion m = (Minion) card;

			JLabel l4 = new JLabel("HP: " + m.getCurrentHP() + "/" + m.getMaxHP());
			l4.setAlignmentX(Component.CENTER_ALIGNMENT);
			l4.setFont(new Font("SansSerif", Font.PLAIN, 20));
			labels.add(l4);

			if (m.isSleeping()) {
				JLabel l5 = new JLabel("Sleeping");
				l5.setAlignmentX(Component.CENTER_ALIGNMENT);
				l5.setFont(new Font("SansSerif", Font.PLAIN, 20));
				labels.add(l5);
			}
			if (m.isDivine()) {
				JLabel l6 = new JLabel("Divine");
				l6.setAlignmentX(Component.CENTER_ALIGNMENT);
				l6.setFont(new Font("SansSerif", Font.PLAIN, 20));
				labels.add(l6);
			}
			if (m.isTaunt()) {
				JLabel l5 = new JLabel("Taunt");
				l5.setAlignmentX(Component.CENTER_ALIGNMENT);
				l5.setFont(new Font("SansSerif", Font.PLAIN, 20));
				labels.add(l5);
			}
		}

		center1.add(labels);
		
		add(center1, BorderLayout.WEST);
		System.out.println("Burned");
		burned = null;
		repaint();
		revalidate();

		HearthstoneView frame = this;
		ActionListener delay = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.remove(center1);
				center.setVisible(true);
				frame.updateHand();				
				frame.repaint();
				frame.revalidate();
				System.out.println("update burned");
			}
		};

		Timer timer = new Timer(2000, delay);
		timer.setRepeats(false);
		timer.start();
		

	}

	public ArrayList<JButton> getHandCards() {
		return handCards;
	}

	public Card getBurned() {
		return burned;
	}

	public void setBurned(Card burned) {
		this.burned = burned;
	}

}
