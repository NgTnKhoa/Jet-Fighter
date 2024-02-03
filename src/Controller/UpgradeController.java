package Controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import View.Map;

public class UpgradeController implements KeyListener {

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (Map.southJetExp.getUpgradeSlot() > 0) {
			if (e.getKeyCode() == e.VK_DIVIDE) {
				Map.southJet.setDamage(Map.southJet.getDamage() + 5);
				Map.southJetExp.setUpgradeSlot(Map.southJetExp.getUpgradeSlot() - 1);
			}

			if (e.getKeyCode() == e.VK_MULTIPLY) {
				Map.southJet.setArmor(Map.southJet.getArmor() + 5);
				Map.southJetExp.setUpgradeSlot(Map.southJetExp.getUpgradeSlot() - 1);
			}

			if (e.getKeyCode() == e.VK_SUBTRACT) {
				if (Map.southJetHp.getWidth() <= 190) {
					Map.southJetHp.setWidth(Map.southJetHp.getWidth() + 10);
					Map.southJetExp.setUpgradeSlot(Map.southJetExp.getUpgradeSlot() - 1);
				}
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {

	}
}
