package Controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import Model.Exp;
import Model.Jet;
import View.App;
import View.Map;

public class ShootController implements KeyListener {

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (Map.shooting) {
			if (e.getKeyCode() == e.VK_END) {
				Map.southJet.fire(2);
				Map.shooting = false;
			}

			if (e.getKeyCode() == e.VK_ENTER) {
				Map.southJet.skill(2);
				Map.shooting = false;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}
