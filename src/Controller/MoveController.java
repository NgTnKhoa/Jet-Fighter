package Controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import View.Map;

public class MoveController implements KeyListener {
	public static KeyEvent e;
	public static int leftCount, rightCount;
	public static boolean isLeft, isRight;

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// south jet move
		if (e.getKeyCode() == e.VK_DOWN) {
			Map.moveSouthDown = true;
		}

		if (e.getKeyCode() == e.VK_LEFT) {
			Map.moveSouthLeft = true;
			isLeft = true;
			leftCount++;
		}

		if (e.getKeyCode() == e.VK_UP) {
			Map.moveSouthUp = true;
		}

		if (e.getKeyCode() == e.VK_RIGHT) {
			Map.moveSouthRight = true;
			isRight = true;
			rightCount++;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// south jet move
		if (e.getKeyCode() == e.VK_DOWN) {
			Map.moveSouthDown = false;
		}

		if (e.getKeyCode() == e.VK_LEFT) {
			Map.moveSouthLeft = false;
			isLeft = false;
		}

		if (e.getKeyCode() == e.VK_UP) {
			Map.moveSouthUp = false;
		}

		if (e.getKeyCode() == e.VK_RIGHT) {
			Map.moveSouthRight = false;
			isRight = false;
		}
	}
}
