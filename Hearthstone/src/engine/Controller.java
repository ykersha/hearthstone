package engine;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.GrayFilter;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import exceptions.CannotAttackException;
import exceptions.FullFieldException;
import exceptions.FullHandException;
import exceptions.HeroPowerAlreadyUsedException;
import exceptions.InvalidTargetException;
import exceptions.NotEnoughManaException;
import exceptions.NotSummonedException;
import exceptions.NotYourTurnException;
import exceptions.TauntBypassException;
import model.cards.minions.Minion;
import model.cards.spells.AOESpell;
import model.cards.spells.FieldSpell;
import model.cards.spells.HeroTargetSpell;
import model.cards.spells.LeechingSpell;
import model.cards.spells.MinionTargetSpell;
import model.cards.spells.Spell;
import model.heroes.Hero;
import model.heroes.Hunter;
import model.heroes.Mage;
import model.heroes.Paladin;
import model.heroes.Priest;
import model.heroes.Warlock;
import view.ChooseHero;
import view.HearthstoneView;

public class Controller implements GameListener, ActionListener {

	private HearthstoneView view;
	private Game game;
	private Hero c;
	private Hero o;
	int click = 0;
	Minion attacker;
	private ArrayList<JPanel> curField = new ArrayList<JPanel>();
	private ArrayList<JPanel> oppField = new ArrayList<JPanel>();
	private ArrayList<JPanel> hand = new ArrayList<JPanel>();
	private Spell spell;
	private Minion tempMinionTarget;
	private Hero tempHeroTarget;
	private boolean isHeroPowerPressed = false;
	ChooseHero startup;

	public static Clip mainTitle;

	public Controller() {

		view = new HearthstoneView(this);
		startup = new ChooseHero(this);
		view.add(startup.getView(), BorderLayout.CENTER);

		view.repaint();
		view.revalidate();

	}

	public void swapHeroes() {
		Hero tmp = c;
		this.c = o;
		o = tmp;
	}

	public static void main(String[] args) {
		new Controller();
	}

	@Override
	public void onGameOver() {
		Hero winner, loser;
		if (o.getCurrentHP() <= 0) {
			loser = o;
			winner = c;
		} else {
			loser = c;
			winner = o;
		}
		view.getContentPane().removeAll();
		view.gameOver(winner, loser);
		// playAudio("Sounds/game_end_reward.wav");
	}

	public void attack(Hero target) {

		if (attacker != null) {
			try {
				c.attackWithMinion(attacker, target);
				playAudio("Sounds/FX_Minion_AttackImpactLarge.wav");
			} catch (CannotAttackException e) {
				playAudio("Sounds/priest/cantDothat.wav");
				JOptionPane.showMessageDialog(null, e.getMessage());
				attacker = null;
				e.printStackTrace();

			} catch (NotYourTurnException e) {
				playAudio("Sounds/priest/cantPlay.wav");
				JOptionPane.showMessageDialog(null, e.getMessage());
				attacker = null;
				e.printStackTrace();

			} catch (TauntBypassException e) {
				playAudio("Sounds/priest/taunt.wav");
				JOptionPane.showMessageDialog(null, e.getMessage());
				attacker = null;
				e.printStackTrace();

			} catch (NotSummonedException e) {
				playAudio("Sounds/priest/cantDothat.wav");
				JOptionPane.showMessageDialog(null, e.getMessage());
				attacker = null;
				e.printStackTrace();

			} catch (InvalidTargetException e) {
				playAudio("Sounds/priest/InvalidTarget.wav");
				JOptionPane.showMessageDialog(null, e.getMessage());
				attacker = null;
				e.printStackTrace();

			}

			curField.clear();
			oppField.clear();
			attacker = null;
			view.updateHand();

		}

	}

	public void attack(JPanel source) {

		if (source.getName().equals("current")) {
			int attackerIndex = curField.indexOf(source);
			Minion selected = c.getField().get(attackerIndex);
			if (selected == this.attacker) {
				attacker = null;
			}else {
				attacker = selected;
			}
			System.out.println(curField.indexOf(source));
			view.updateHand();
		}

		if (source.getName().equals("opponent") && attacker == null)
			return;

		if (source.getName().equals("opponent") && attacker != null) {
			int index = oppField.indexOf(source);
			Minion target = o.getField().get(index);

			try {
				c.attackWithMinion(attacker, target);
				playAudio("Sounds/FX_Minion_AttackImpact.wav");
			} catch (CannotAttackException e) {
				playAudio("Sounds/priest/cantDothat.wav");
				JOptionPane.showMessageDialog(null, e.getMessage());
				e.printStackTrace();
				attacker = null;
			} catch (NotYourTurnException e) {
				playAudio("Sounds/priest/cantDothat.wav");
				JOptionPane.showMessageDialog(null, e.getMessage());
				e.printStackTrace();
				attacker = null;

			} catch (TauntBypassException e) {
				playAudio("Sounds/priest/taunt.wav");
				JOptionPane.showMessageDialog(null, e.getMessage());
				e.printStackTrace();
				attacker = null;

			} catch (InvalidTargetException e) {
				playAudio("Sounds/priest/InvalidTarget.wav");
				JOptionPane.showMessageDialog(null, e.getMessage());
				e.printStackTrace();
				attacker = null;

			} catch (NotSummonedException e) {
				playAudio("Sounds/priest/cantPlay.wav");
				JOptionPane.showMessageDialog(null, e.getMessage());
				e.printStackTrace();
				attacker = null;

			}
			curField.clear();
			oppField.clear();
			attacker = null;
			view.updateHand();
		}

	}

	public void playMinion(Minion card) {
		try {
			c.playMinion((Minion) card);
			playAudio("Sounds/play_card_from_hand_1.wav");
		} catch (NotYourTurnException e) {
			playAudio("Sounds/priest/cantDothat.wav");
			System.out.println(game.getCurrentHero().getName());
			JOptionPane.showMessageDialog(null, e.getMessage());

			e.printStackTrace();
		} catch (NotEnoughManaException e) {
			playAudio("Sounds/priest/noMana.wav");
			JOptionPane.showMessageDialog(null, e.getMessage());

			e.printStackTrace();
		} catch (FullFieldException e) {
			playAudio("Sounds/priest/cantDothat.wav");
			JOptionPane.showMessageDialog(null, e.getMessage());

			e.printStackTrace();

		}
		spell = null;
		attacker = null;
		tempHeroTarget = null;
		tempMinionTarget = null;
		view.updateHand();

	}

	public void playSpell(Spell card) {

		spell = card;
		if (card instanceof FieldSpell) {
			try {
				c.castSpell((FieldSpell) spell);
				playAudio("Sounds/FX_MinionSummon_Cast.wav");
			} catch (NotYourTurnException e) {
				playAudio("Sounds/priest/cantPlay.wav");
				JOptionPane.showMessageDialog(null, e.getMessage());
				e.printStackTrace();

			} catch (NotEnoughManaException e) {
				playAudio("Sounds/priest/noMana.wav");
				JOptionPane.showMessageDialog(null, e.getMessage());
				e.printStackTrace();

			}
			spell = null;
		} else if (card instanceof AOESpell) {
			try {
				c.castSpell(((AOESpell) card), o.getField());
				playAudio("Sounds/FX_MinionSummon_Cast.wav");
			} catch (NotYourTurnException e) {
				playAudio("Sounds/priest/cantPlay.wav");
				JOptionPane.showMessageDialog(null, e.getMessage());
				e.printStackTrace();

			} catch (NotEnoughManaException e) {
				playAudio("Sounds/priest/noMana.wav");
				JOptionPane.showMessageDialog(null, e.getMessage());
				e.printStackTrace();

			}
			spell = null;
		} else if (card instanceof MinionTargetSpell && !(card instanceof HeroTargetSpell)) {
			if (tempMinionTarget == null)
				JOptionPane.showMessageDialog(null, "Please choose a target Minion.");
			else {
				try {
					try {
						c.castSpell(((MinionTargetSpell) card), tempMinionTarget);
						playAudio("Sounds/FX_MinionSummon_Cast.wav");
					} catch (InvalidTargetException e) {
						playAudio("Sounds/priest/InvalidTarget.wav");
						JOptionPane.showMessageDialog(null, e.getMessage());
						e.printStackTrace();
					}
					
				} catch (NotYourTurnException e) {
					playAudio("Sounds/priest/cantPlay.wav");
					JOptionPane.showMessageDialog(null, e.getMessage());
					e.printStackTrace();

				} catch (NotEnoughManaException e) {
					playAudio("Sounds/priest/noMana.wav");
					JOptionPane.showMessageDialog(null, e.getMessage());
					e.printStackTrace();

				}
				tempMinionTarget = null;
				spell = null;
			}
		} else if (card instanceof LeechingSpell) {
			if (tempMinionTarget == null)
				JOptionPane.showMessageDialog(null, "Please choose a target Minion.");
			else {
				try {
					c.castSpell(((LeechingSpell) card), tempMinionTarget);
					playAudio("Sounds/FX_MinionSummon_Cast.wav");
				} catch (NotYourTurnException e) {
					playAudio("Sounds/priest/cantPlay.wav");
					JOptionPane.showMessageDialog(null, e.getMessage());
					e.printStackTrace();

				} catch (NotEnoughManaException e) {
					playAudio("Sounds/priest/noMana.wav");
					JOptionPane.showMessageDialog(null, e.getMessage());
					e.printStackTrace();

				}
				tempMinionTarget = null;
				spell = null;
			}

		} else if (card instanceof HeroTargetSpell && !(card instanceof MinionTargetSpell)) {
			if (tempHeroTarget == null)
				JOptionPane.showMessageDialog(null, "Please choose a target Hero.");
			else {
				try {
					c.castSpell(((HeroTargetSpell) card), tempHeroTarget);
					playAudio("Sounds/FX_MinionSummon_Cast.wav");
				} catch (NotYourTurnException e) {
					playAudio("Sounds/priest/cantPlay.wav");
					JOptionPane.showMessageDialog(null, e.getMessage());
					e.printStackTrace();

				} catch (NotEnoughManaException e) {
					playAudio("Sounds/priest/noMana.wav");
					JOptionPane.showMessageDialog(null, e.getMessage());
					e.printStackTrace();

				}
				tempHeroTarget = null;
				spell = null;
			}
		} else if (card instanceof HeroTargetSpell && card instanceof MinionTargetSpell) {
			if (tempHeroTarget == null && tempMinionTarget == null)
				JOptionPane.showMessageDialog(null, "Please choose a target.");
			else if (tempHeroTarget != null) {
				try {
					c.castSpell(((HeroTargetSpell) card), tempHeroTarget);
					playAudio("Sounds/FX_MinionSummon_Cast.wav");
				} catch (NotYourTurnException e) {
					playAudio("Sounds/priest/cantPlay.wav");
					JOptionPane.showMessageDialog(null, e.getMessage());
					e.printStackTrace();

				} catch (NotEnoughManaException e) {
					playAudio("Sounds/priest/noMana.wav");
					JOptionPane.showMessageDialog(null, e.getMessage());
					e.printStackTrace();

				}
				tempHeroTarget = null;
				spell = null;
			} else if (tempMinionTarget != null) {
				try {
					c.castSpell(((MinionTargetSpell) card), tempMinionTarget);
					playAudio("Sounds/FX_MinionSummon_Cast.wav");
				} catch (NotYourTurnException e) {
					playAudio("Sounds/priest/cantPlay.wav");
					JOptionPane.showMessageDialog(null, e.getMessage());
					e.printStackTrace();

				} catch (NotEnoughManaException e) {
					playAudio("Sounds/priest/noMana.wav");
					JOptionPane.showMessageDialog(null, e.getMessage());
					e.printStackTrace();

				} catch (InvalidTargetException e) {
					playAudio("Sounds/priest/InvalidTarget.wav");
					JOptionPane.showMessageDialog(null, e.getMessage());
					e.printStackTrace();

				}
				tempMinionTarget = null;
				spell = null;
			}

		}
		curField.clear();
		oppField.clear();
		view.updateHand();

	}

	public void useHeroPower() {
		if (c instanceof Warlock) {

			try {
				((Warlock) c).useHeroPower();
				playAudio("Sounds/card_turn_over_legendary.wav");
			} catch (NotEnoughManaException e) {
				playAudio("Sounds/priest/noMana.wav");
				JOptionPane.showMessageDialog(null, e.getMessage());
				e.printStackTrace();

			} catch (HeroPowerAlreadyUsedException e) {
				playAudio("Sounds/priest/HeroPowerUsed.wav");
				JOptionPane.showMessageDialog(null, e.getMessage());
				e.printStackTrace();

			} catch (NotYourTurnException e) {
				playAudio("Sounds/priest/cantPlay.wav");
					JOptionPane.showMessageDialog(null, e.getMessage());
				e.printStackTrace();

			} catch (FullHandException e) {
				playAudio("Sounds/priest/fullHand.wav");
					JOptionPane.showMessageDialog(null, e.getMessage());
				e.printStackTrace();

			} catch (FullFieldException e) {
				playAudio("Sounds/priest/cantPlay.wav");
					JOptionPane.showMessageDialog(null, e.getMessage());
				e.printStackTrace();

			} catch (CloneNotSupportedException e) {
				playAudio("Sounds/priest/cantPlay.wav");
					JOptionPane.showMessageDialog(null, e.getMessage());
				e.printStackTrace();

			}

		}

		if (c instanceof Hunter) {
			try {
				((Hunter) c).useHeroPower();
				playAudio("Sounds/card_turn_over_legendary.wav");
			} catch (NotEnoughManaException e) {
				playAudio("Sounds/priest/noMana.wav");
				JOptionPane.showMessageDialog(null, e.getMessage());
				e.printStackTrace();

			} catch (HeroPowerAlreadyUsedException e) {
				playAudio("Sounds/priest/HeroPowerUsed.wav");
				JOptionPane.showMessageDialog(null, e.getMessage());
				e.printStackTrace();

			} catch (NotYourTurnException e) {
				playAudio("Sounds/priest/cantPlay.wav");
				JOptionPane.showMessageDialog(null, e.getMessage());
				e.printStackTrace();

			} catch (FullHandException e) {
				playAudio("Sounds/priest/fullHand.wav");
				JOptionPane.showMessageDialog(null, e.getMessage());
				e.printStackTrace();

			} catch (FullFieldException e) {
				playAudio("Sounds/priest/cantPlay.wav");
				JOptionPane.showMessageDialog(null, e.getMessage());
				e.printStackTrace();

			} catch (CloneNotSupportedException e) {
				playAudio("Sounds/priest/cantPlay.wav");
				JOptionPane.showMessageDialog(null, e.getMessage());
				e.printStackTrace();

			}
		}
		if (c instanceof Paladin) {
			try {
				((Paladin) c).useHeroPower();
				playAudio("Sounds/card_turn_over_legendary.wav");
			} catch (NotEnoughManaException e) {
				playAudio("Sounds/priest/noMana.wav");
				JOptionPane.showMessageDialog(null, e.getMessage());
				e.printStackTrace();

			} catch (HeroPowerAlreadyUsedException e) {
				playAudio("Sounds/priest/HeroPowerUsed.wav");
				JOptionPane.showMessageDialog(null, e.getMessage());
				e.printStackTrace();

			} catch (NotYourTurnException e) {
				playAudio("Sounds/priest/cantPlay.wav");
				JOptionPane.showMessageDialog(null, e.getMessage());
				e.printStackTrace();

			} catch (FullHandException e) {
				playAudio("Sounds/priest/fullHand.wav");
				JOptionPane.showMessageDialog(null, e.getMessage());
				e.printStackTrace();

			} catch (FullFieldException e) {
				playAudio("Sounds/priest/cantPlay.wav");
				JOptionPane.showMessageDialog(null, e.getMessage());
				e.printStackTrace();

			} catch (CloneNotSupportedException e) {
				playAudio("Sounds/priest/cantPlay.wav");
				JOptionPane.showMessageDialog(null, e.getMessage());
				e.printStackTrace();

			}
		}

		if (c instanceof Mage) {
			if (tempHeroTarget == null && tempMinionTarget == null) {
				isHeroPowerPressed = true;
				JOptionPane.showMessageDialog(null, "Choose a target!");
			} else if (isHeroPowerPressed && tempHeroTarget != null) {
				try {
					((Mage) c).useHeroPower(tempHeroTarget);
					playAudio("Sounds/card_turn_over_legendary.wav");
					isHeroPowerPressed = false;
					tempHeroTarget = null;
				} catch (NotEnoughManaException e) {
					playAudio("Sounds/priest/noMana.wav");
					JOptionPane.showMessageDialog(null, e.getMessage());
					e.printStackTrace();

				} catch (HeroPowerAlreadyUsedException e) {
					playAudio("Sounds/priest/HeroPowerUsed.wav");
					JOptionPane.showMessageDialog(null, e.getMessage());
					e.printStackTrace();

				} catch (NotYourTurnException e) {
					playAudio("Sounds/priest/cantPlay.wav");
					JOptionPane.showMessageDialog(null, e.getMessage());
					e.printStackTrace();

				} catch (FullHandException e) {
					playAudio("Sounds/priest/fullHand.wav");
					JOptionPane.showMessageDialog(null, e.getMessage());
					e.printStackTrace();

				} catch (FullFieldException e) {
					playAudio("Sounds/priest/cantPlay.wav");
					JOptionPane.showMessageDialog(null, e.getMessage());
					e.printStackTrace();

				} catch (CloneNotSupportedException e) {
					playAudio("Sounds/priest/cantPlay.wav");
					JOptionPane.showMessageDialog(null, e.getMessage());
					e.printStackTrace();

				}
			} else if (isHeroPowerPressed && tempMinionTarget != null) {
				try {
					((Mage) c).useHeroPower(tempMinionTarget);
					playAudio("Sounds/card_turn_over_legendary.wav");
					isHeroPowerPressed = false;
					tempMinionTarget = null;
				} catch (NotEnoughManaException e) {
					playAudio("Sounds/priest/noMana.wav");
					JOptionPane.showMessageDialog(null, e.getMessage());
					e.printStackTrace();

				} catch (HeroPowerAlreadyUsedException e) {
					playAudio("Sounds/priest/HeroPowerUsed.wav");
					JOptionPane.showMessageDialog(null, e.getMessage());
					e.printStackTrace();

				} catch (NotYourTurnException e) {
					playAudio("Sounds/priest/cantPlay.wav");
					JOptionPane.showMessageDialog(null, e.getMessage());
					e.printStackTrace();

				} catch (FullHandException e) {
					playAudio("Sounds/priest/fullHand.wav");
					JOptionPane.showMessageDialog(null, e.getMessage());
					e.printStackTrace();

				} catch (FullFieldException e) {
					playAudio("Sounds/priest/cantPlay.wav");
					JOptionPane.showMessageDialog(null, e.getMessage());
					e.printStackTrace();

				} catch (CloneNotSupportedException e) {
					playAudio("Sounds/priest/cantPlay.wav");
					JOptionPane.showMessageDialog(null, e.getMessage());
					e.printStackTrace();

				}
			}
		}
		if (c instanceof Priest) {
			if (tempHeroTarget == null && tempMinionTarget == null) {
				isHeroPowerPressed = true;
				JOptionPane.showMessageDialog(null, "Choose a target!");
			} else if (isHeroPowerPressed && tempHeroTarget != null) {
				try {
					((Priest) c).useHeroPower(tempHeroTarget);
					playAudio("Sounds/card_turn_over_legendary.wav");
					isHeroPowerPressed = false;
					tempHeroTarget = null;
				} catch (NotEnoughManaException e) {
					playAudio("Sounds/priest/noMana.wav");
					JOptionPane.showMessageDialog(null, e.getMessage());
					e.printStackTrace();

				} catch (HeroPowerAlreadyUsedException e) {
					playAudio("Sounds/priest/HeroPowerUsed.wav");
					JOptionPane.showMessageDialog(null, e.getMessage());
					e.printStackTrace();

				} catch (NotYourTurnException e) {
					playAudio("Sounds/priest/cantPlay.wav");
					JOptionPane.showMessageDialog(null, e.getMessage());
					e.printStackTrace();

				} catch (FullHandException e) {
					playAudio("Sounds/priest/fullHand.wav");
					JOptionPane.showMessageDialog(null, e.getMessage());
					e.printStackTrace();

				} catch (FullFieldException e) {
					playAudio("Sounds/priest/cantPlay.wav");
					JOptionPane.showMessageDialog(null, e.getMessage());
					e.printStackTrace();

				} catch (CloneNotSupportedException e) {
					playAudio("Sounds/priest/cantPlay.wav");
					JOptionPane.showMessageDialog(null, e.getMessage());
					e.printStackTrace();

				}
			} else if (isHeroPowerPressed && tempMinionTarget != null) {
				try {
					((Priest) c).useHeroPower(tempMinionTarget);
					playAudio("Sounds/card_turn_over_legendary.wav");
					isHeroPowerPressed = false;
					tempMinionTarget = null;
				} catch (NotEnoughManaException e) {
					playAudio("Sounds/priest/noMana.wav");
					JOptionPane.showMessageDialog(null, e.getMessage());
					e.printStackTrace();

				} catch (HeroPowerAlreadyUsedException e) {
					playAudio("Sounds/priest/HeroPowerUsed.wav");
					JOptionPane.showMessageDialog(null, e.getMessage());
					e.printStackTrace();

				} catch (NotYourTurnException e) {
					playAudio("Sounds/priest/cantPlay.wav");
					JOptionPane.showMessageDialog(null, e.getMessage());
					e.printStackTrace();

				} catch (FullHandException e) {
					playAudio("Sounds/priest/fullHand.wav");
					JOptionPane.showMessageDialog(null, e.getMessage());
					e.printStackTrace();

				} catch (FullFieldException e) {
					playAudio("Sounds/priest/cantPlay.wav");
					JOptionPane.showMessageDialog(null, e.getMessage());
					e.printStackTrace();

				} catch (CloneNotSupportedException e) {
					playAudio("Sounds/priest/cantPlay.wav");
					JOptionPane.showMessageDialog(null, e.getMessage());
					e.printStackTrace();

				}
			}
		}
		view.updateHand();

	}

	public void actionPerformed(ActionEvent e) {
		click++;

		if (click == 1) {
			startup.setLabelText("Choose Player Two");
			JButton b = (JButton) e.getSource();
			String s = b.getActionCommand();

			if (s.equals("mage")) {
				try {
					c = new Mage();
					playAudio("Sounds/VO_ANNOUNCER_JAINA_06.wav");
				} catch (IOException e1) {
					playAudio("Sounds/priest/cantPlay.wav");
//					JOptionPane.showMessageDialog(null, e1.getMessage());
					e1.printStackTrace();

				} catch (CloneNotSupportedException e1) {
					playAudio("Sounds/priest/cantPlay.wav");
//					JOptionPane.showMessageDialog(null, e1.getMessage());
					e1.printStackTrace();

				}
			}
			if (s.equals("hunter")) {
				try {
					c = new Hunter();
					playAudio("Sounds/VO_ANNOUNCER_REXXAR_08.wav");
				} catch (IOException e1) {
					playAudio("Sounds/priest/cantPlay.wav");
//					JOptionPane.showMessageDialog(null, e1.getMessage());
					e1.printStackTrace();

				} catch (CloneNotSupportedException e1) {
					playAudio("Sounds/priest/cantPlay.wav");
//					JOptionPane.showMessageDialog(null, e1.getMessage());
					e1.printStackTrace();

				}
			}
			if (s.equals("paladin")) {
				try {
					c = new Paladin();
					playAudio("Sounds/VO_ANNOUNCER_UTHER_10.wav");
				} catch (IOException e1) {
					playAudio("Sounds/priest/cantPlay.wav");
//					JOptionPane.showMessageDialog(null, e1.getMessage());
					e1.printStackTrace();

				} catch (CloneNotSupportedException e1) {
					playAudio("Sounds/priest/cantPlay.wav");
//					JOptionPane.showMessageDialog(null, e1.getMessage());
					e1.printStackTrace();

				}
			}
			if (s.equals("warlock")) {
				try {
					c = new Warlock();
					playAudio("Sounds/VO_ANNOUNCER_GUL'DAN_13.wav");
				} catch (IOException e1) {
					playAudio("Sounds/priest/cantPlay.wav");
//					JOptionPane.showMessageDialog(null, e1.getMessage());
					e1.printStackTrace();

				} catch (CloneNotSupportedException e1) {
					playAudio("Sounds/priest/cantPlay.wav");
//					JOptionPane.showMessageDialog(null, e1.getMessage());
					e1.printStackTrace();

				}
			}
			if (s.equals("priest")) {
				try {
					c = new Priest();
					playAudio("Sounds/VO_ANNOUNCER_ANDUIN_12.wav");
				} catch (IOException e1) {
					playAudio("Sounds/priest/cantPlay.wav");
//					JOptionPane.showMessageDialog(null, e1.getMessage());
					e1.printStackTrace();

				} catch (CloneNotSupportedException e1) {
					playAudio("Sounds/priest/cantPlay.wav");
//					JOptionPane.showMessageDialog(null, e1.getMessage());
					e1.printStackTrace();

				}

			}
		}

		if (click == 2) {
			JButton b1 = (JButton) e.getSource();
			String s1 = b1.getActionCommand();

			if (s1.equals("mage")) {
				try {
					o = new Mage();
					playAudio("Sounds/VO_ANNOUNCER_JAINA_06.wav");
				} catch (IOException e1) {
					playAudio("Sounds/priest/cantPlay.wav");
//					JOptionPane.showMessageDialog(null, e1.getMessage());
					e1.printStackTrace();

				} catch (CloneNotSupportedException e1) {
					playAudio("Sounds/priest/cantPlay.wav");
// 					JOptionPane.showMessageDialog(null, e1.getMessage());
					e1.printStackTrace();

				}
			}
			if (s1.equals("hunter")) {
				try {
					o = new Hunter();
					playAudio("Sounds/VO_ANNOUNCER_REXXAR_08.wav");
				} catch (IOException e1) {
					playAudio("Sounds/priest/cantPlay.wav");
//					JOptionPane.showMessageDialog(null, e1.getMessage());
					e1.printStackTrace();

				} catch (CloneNotSupportedException e1) {
					playAudio("Sounds/priest/cantPlay.wav");
//					JOptionPane.showMessageDialog(null, e1.getMessage());
					e1.printStackTrace();

				}
			}
			if (s1.equals("paladin")) {
				try {
					o = new Paladin();
					playAudio("Sounds/VO_ANNOUNCER_UTHER_10.wav");
				} catch (IOException e1) {
					playAudio("Sounds/priest/cantPlay.wav");
//					JOptionPane.showMessageDialog(null, e1.getMessage());
					e1.printStackTrace();

				} catch (CloneNotSupportedException e1) {
					playAudio("Sounds/priest/cantPlay.wav");
//					JOptionPane.showMessageDialog(null, e1.getMessage());
					e1.printStackTrace();

				}
			}
			if (s1.equals("warlock")) {
				try {
					o = new Warlock();
					playAudio("Sounds/VO_ANNOUNCER_GUL'DAN_13.wav");
				} catch (IOException e1) {
					playAudio("Sounds/priest/cantPlay.wav");
//					JOptionPane.showMessageDialog(null, e1.getMessage());
					e1.printStackTrace();

				} catch (CloneNotSupportedException e1) {
					playAudio("Sounds/priest/cantPlay.wav");
//					JOptionPane.showMessageDialog(null, e1.getMessage());
					e1.printStackTrace();

				}
			}
			if (s1.equals("priest")) {
				try {
					o = new Priest();
					playAudio("Sounds/VO_ANNOUNCER_ANDUIN_12.wav");
				} catch (IOException e1) {
					playAudio("Sounds/priest/cantPlay.wav");
//					JOptionPane.showMessageDialog(null, e1.getMessage());
					e1.printStackTrace();

				} catch (CloneNotSupportedException e1) {
					playAudio("Sounds/priest/cantPlay.wav");
//					JOptionPane.showMessageDialog(null, e1.getMessage());
					e1.printStackTrace();

				}

			}

		}

		if (click == 2) {
			try {
				game = new Game(c, o);
				view.setC(game.getCurrentHero());
				view.setO(game.getOpponent());
				c = game.getCurrentHero();
				o = game.getOpponent();
				game.setListener(this);
				view.startGame();
			} catch (FullHandException e1) {
				playAudio("Sounds/priest/cantPlay.wav");
//				JOptionPane.showMessageDialog(null, e1.getMessage());
				e1.printStackTrace();

			} catch (CloneNotSupportedException e1) {
				playAudio("Sounds/priest/cantPlay.wav");
//				JOptionPane.showMessageDialog(null, e1.getMessage());
				e1.printStackTrace();

			}
		}

		if (e.getActionCommand().equals("endTurn")) {

			this.swapHeroes();
			attacker = null;
			spell = null;
			isHeroPowerPressed = false;
			tempHeroTarget = null;
			tempMinionTarget = null;
			try {
				game.endTurn();
			} catch (FullHandException e1) {

				view.setBurned(e1.getBurned());
				playAudio("Sounds/CardBurn2.wav");
//					JOptionPane.showMessageDialog(null, e1.getMessage());
				e1.printStackTrace();

			} catch (CloneNotSupportedException e1) {
				playAudio("Sounds/priest/cantPlay.wav");
//					JOptionPane.showMessageDialog(null, e1.getMessage());
				e1.printStackTrace();

			}
			view.setC(game.getCurrentHero());
			view.setO(game.getOpponent());
			view.nextTurn();
			playAudio("Sounds/FX_EndTurnFlip.wav");
			System.out.println("switch");

		}
		if (e.getActionCommand().equals("useHeroPower")) {
			this.tempHeroTarget = null;
			this.tempMinionTarget = null;
			this.useHeroPower();
		}
		
		if(e.getActionCommand().equals("playAgain")) {
			view.setVisible(false);
			view.dispose();
			new Controller();
			
		}

	}

	public static ImageIcon getImage(String s, int w, int h) {
		ImageIcon imageIcon = new ImageIcon(s); // load the image to a imageIcon
		Image image = imageIcon.getImage(); // transform it
		Image newimg = image.getScaledInstance((int) (w * HearthstoneView.width), (int) (h * HearthstoneView.height),
				java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
		// 200, 225 were set
		return new ImageIcon(newimg);
	}

	public static ImageIcon getGrayImage(String s, int w, int h) {
		ImageIcon icon = new ImageIcon(s);
		Image normalImage = icon.getImage();
		Image grayImage = GrayFilter.createDisabledImage(normalImage);
		Image newimg = grayImage.getScaledInstance((int) (w * HearthstoneView.width),
				(int) (h * HearthstoneView.height), java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
		// 200, 225 were set
		return new ImageIcon(newimg);
	}

	public static void playAudio(String s) {
		AudioInputStream audioIn;
		try {
			audioIn = AudioSystem.getAudioInputStream(new File(s).getAbsoluteFile());
			Clip clip = AudioSystem.getClip();
			clip.open(audioIn);
			clip.start();

		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void playAudioLoop(String s) {
		AudioInputStream audioIn;
		try {
			audioIn = AudioSystem.getAudioInputStream(new File(s).getAbsoluteFile());
			Clip clip = AudioSystem.getClip();
			clip.open(audioIn);
			clip.start();
			mainTitle = clip;
			clip.loop(Clip.LOOP_CONTINUOUSLY);

		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public ArrayList<JPanel> getCurField() {
		return curField;
	}

	public ArrayList<JPanel> getOppField() {
		return oppField;
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

	public Spell getSpell() {
		return spell;
	}

	public void setSpell(Spell spell) {
		this.spell = spell;
	}

	public Minion getTempMinionTarget() {
		return tempMinionTarget;
	}

	public void setTempMinionTarget(Minion spellMinionTarget) {
		this.tempMinionTarget = spellMinionTarget;
	}

	public Hero getTempHeroTarget() {
		return tempHeroTarget;
	}

	public void setTempHeroTarget(Hero spellHeroTarget) {
		this.tempHeroTarget = spellHeroTarget;
	}

	public Minion getAttacker() {
		return attacker;
	}

	public void setAttacker(Minion attacker) {
		this.attacker = attacker;
	}

	public ArrayList<JPanel> getHand() {
		return hand;
	}

	public boolean isHeroPowerPressed() {
		return isHeroPowerPressed;
	}

	public void setHeroPowerPressed(boolean isHeroPowerPressed) {
		this.isHeroPowerPressed = isHeroPowerPressed;
	}

	public HearthstoneView getView() {
		return view;
	}

	public void setView(HearthstoneView view) {
		this.view = view;
	}

}