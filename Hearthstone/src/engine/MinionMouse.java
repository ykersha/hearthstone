package engine;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class MinionMouse implements MouseListener {
	JPanel image;
	JPanel back;
	Controller controller;

	public MinionMouse(JPanel image, JPanel back, Controller controller) {
		this.back = back;
		this.image = image;
		this.controller = controller;

	}

	public void mouseEntered(java.awt.event.MouseEvent evt) {

		image.setVisible(false);
		back.setVisible(true);

	}

	public void mouseExited(java.awt.event.MouseEvent evt) {
		//java.awt.Point p = new java.awt.Point(evt.getLocationOnScreen());
		//SwingUtilities.convertPointFromScreen(p, evt.getComponent());

		// if (!evt.getComponent().contains(p)) {
		back.setVisible(false);
		image.setVisible(true);
		// }
		

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if (controller.isHeroPowerPressed()) {
			JPanel p = (JPanel) (e.getSource());
			if (p.getName().equals("current")) {
				int attackerIndex = controller.getCurField().indexOf(p);
				controller.setTempMinionTarget(controller.getC().getField().get(attackerIndex));
			} else if (p.getName().equals("opponent")) {
				int attackerIndex = controller.getOppField().indexOf(p);
				controller.setTempMinionTarget(controller.getO().getField().get(attackerIndex));
			}
			controller.useHeroPower();
		} else {
			if (controller.getSpell() == null)
				controller.attack((JPanel) (e.getSource()));
			else {
				JPanel p = (JPanel) (e.getSource());
				if (p.getName().equals("current")) {
					int attackerIndex = controller.getCurField().indexOf(p);
					controller.setTempMinionTarget(controller.getC().getField().get(attackerIndex));
				} else if (p.getName().equals("opponent")) {
					int attackerIndex = controller.getOppField().indexOf(p);
					controller.setTempMinionTarget(controller.getO().getField().get(attackerIndex));
				}
				System.out.println(
						controller.getSpell().getName() + " --- " + controller.getTempMinionTarget().getName());
				controller.playSpell(controller.getSpell());
			}
		}
		
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		back.setBorder(BorderFactory.createLineBorder(Color.black));
		image.setBorder(BorderFactory.createLineBorder(Color.black));
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		back.setBorder(null);
		image.setBorder(null);

	}

}
