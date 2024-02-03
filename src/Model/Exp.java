package Model;

import java.util.Random;

public class Exp {
	private int x;
	private int y;
	private int width;
	private int height;
	private int x_exp;
	private int y_exp;
	Random rd;
	private int time;
	private double num;
	private Jet jet;
	private int upgradeSlot;
	private boolean shouldDrawPoint = false;

	public Exp(int x, int y, int width, int height, Jet jet) {
		super();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.jet = jet;
		
		upgradeSlot = 0;
	}

	public int getX() {
		return x;
	}

	public Exp(int x_exp, int y_exp) {
		super();
		this.x_exp = x_exp;
		this.y_exp = y_exp;
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

	public boolean shouldDrawPoint() {
		return shouldDrawPoint;
	}

	public void setShouldDrawPoint(boolean shouldDrawPoint) {
		this.shouldDrawPoint = shouldDrawPoint;
	}

	public int getX_exp() {
		return x_exp;
	}

	public void setX_exp(int x_exp) {
		this.x_exp = x_exp;
	}

	public int getY_exp() {
		return y_exp;
	}

	public void setY_exp(int y_exp) {
		this.y_exp = y_exp;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}
	
	public int getUpgradeSlot() {
		return upgradeSlot;
	}
	
	public void setUpgradeSlot(int upgradeSlot) {
		this.upgradeSlot = upgradeSlot;
	}

	public void increaseExp(int i) {
		setWidth(width + i);
		
		if (width >= 180) {
			setWidth(0);
			this.jet.setLevel(this.jet.getLevel() + 1);
			upgradeSlot++;
		}
	}
}
