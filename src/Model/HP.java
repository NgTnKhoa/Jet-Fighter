package Model;

import javax.swing.JOptionPane;

public class HP {
	int x;
	int y;
	int width;
	int height;

	public HP(int x, int y, int width, int height) {
		super();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	// decrease hp
	public void decreaseHP(int i) {
		setWidth(width - i);
		if (width <= 0) {
			JOptionPane.showMessageDialog(null, "Trò Chơi Kết Thúc !!!");
		}
	}

}
