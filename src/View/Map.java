package View;

import java.awt.Color;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Controller.MoveController;
import Controller.ShootController;
import Controller.UpgradeController;
import Model.Bullet;
import Model.Exp;
import Model.HP;
import Model.Jet;
import Model.Node;
import Model.Pow;

public class Map extends JPanel implements Runnable {
	public static Bullet bullet, powBullet;
	public static Jet northJet, southJet;

	public static HP northJetHpbackground, southJetHpbackground;
	public static HP northJetHp, southJetHp;

	public static Exp northJetExpBackground, southJetExpBackground;
	public static Exp northJetExp, southJetExp;

	public static ArrayList<Exp> expNorth, expSouth;

	public static Pow northJetPow, southJetPow;

	public static boolean shooting = false;

	public static boolean moveNorthLeft, moveNorthRight, moveNorthUp, moveNorthDown;
	public static boolean moveSouthLeft, moveSouthRight, moveSouthUp, moveSouthDown;

	public static boolean endGame = false;
	public static boolean direction = true;
	public static int distanceMove = 30;
	public static int moved = 0;
	public static int[] pows = { 40, 80, 120, 160, 200 };
	public static int randomPow = pows[new Random().nextInt(pows.length)]; // random pow for AI to shoot
	public static String[] commands = { "L", "R", "U", "D" };
	public static Node root;
	public static int xNorth, yNorth, xSouth, ySouth;

	private ArrayList<Bullet> northBullets, southBullets;
	private Thread loop;

	public Map() {
		init();
		setPreferredSize(new Dimension(App.WIDTH, App.HEIGHT));
		addKeyListener(new MoveController());
		addKeyListener(new ShootController());
		addKeyListener(new UpgradeController());
		setFocusable(true);
		setDoubleBuffered(true);
		setFocusable(true);
	}

	private void init() {

		// jet
		northJet = new Jet(App.WIDTH / 2 - 10, 0, 20, 40, 2, 1, 5, 0);
		southJet = new Jet(App.WIDTH / 2 - 10, App.HEIGHT - 40, 20, 40, 2, 1, 5, 0);

		// hp
		northJetHpbackground = new HP(0, App.HEIGHT / 2 - 21, 201, 20);
		northJetHp = new HP(northJetHpbackground.getX() + 1, App.HEIGHT / 2 - 20, northJetHpbackground.getWidth() - 1,
				northJetHpbackground.getHeight() - 1);

		southJetHpbackground = new HP(App.WIDTH - 201, App.HEIGHT / 2 + 1, 200, 20);
		southJetHp = new HP(southJetHpbackground.getX() + 1, App.HEIGHT / 2 + 2, southJetHpbackground.getWidth() - 1,
				southJetHpbackground.getHeight() - 1);

		// exp_background
		northJetExpBackground = new Exp(0, App.HEIGHT / 2 - 42, 180, 20, northJet);
		northJetExp = new Exp(northJetExpBackground.getX() + 1, App.HEIGHT / 2 - 41, 0,
				northJetExpBackground.getHeight() - 1, northJet);

		southJetExpBackground = new Exp(App.WIDTH - 181, App.HEIGHT / 2 + 23, 180, 20, southJet);
		southJetExp = new Exp(southJetExpBackground.getX() - 1, App.HEIGHT / 2 + 23, 0,
				southJetExpBackground.getHeight() + 1, southJet);

		// pow
		northJetPow = new Pow(0, 20);
		southJetPow = new Pow(0, 20);

		// bullet
		bullet = new Bullet(0, 0, 0, 0);
		powBullet = new Bullet(0, 0, 0, 0);

		// setting
		moveNorthLeft = moveNorthRight = moveNorthUp = moveNorthDown = false;
		moveSouthLeft = moveSouthRight = moveSouthUp = moveSouthDown = false;
		northBullets = northJet.getBullets();
		southBullets = southJet.getBullets();

		expNorth = new ArrayList<>();
		expSouth = new ArrayList<>();

		xNorth = northJet.getX();
		yNorth = northJet.getY();

		xSouth = southJet.getX();
		ySouth = southJet.getY();

		loop = new Thread(this);
		loop.start();
	}

	public void paint(Graphics g) {
		super.paint(g);

		Graphics2D g2d = (Graphics2D) g;

		///// drawing
		try {
			BufferedImage image = ImageIO.read(new File("..\\JetFighter\\src\\\\Assets\\background.png"));
			g2d.drawImage(image, 0, 0, App.WIDTH, App.HEIGHT, null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// boundary
		g2d.setColor(Color.blue);
		g2d.drawLine(0, App.HEIGHT / 2, App.WIDTH, App.HEIGHT / 2);

		// string
		Font font1 = new Font("Arial", Font.BOLD, 15);

		g2d.setColor(Color.BLACK);
		g2d.setFont(font1);

		g2d.drawString("HP", northJetHpbackground.getWidth() + 5, northJetHpbackground.getY() + 15);
		g2d.drawString("HP", southJetHpbackground.getWidth() + 175, southJetHpbackground.getY() + 15);

		g2d.drawString("Exp", northJetExpBackground.getWidth() + 5, northJetExpBackground.getY() + 15);
		g2d.drawString("Exp", southJetExpBackground.getWidth() + 207, southJetExpBackground.getY() + 15);

		Font font2 = new Font("Arial", Font.BOLD, 10);
		g2d.setFont(font2);
		g2d.drawString("Level: " + northJet.getLevel() + "         Damage: " + northJet.getDamage() + "         Armor: "
				+ northJet.getArmor(), 370, 10);

		g2d.drawString("Level: " + southJet.getLevel() + "         Damage: " + southJet.getDamage() + "         Armor: "
				+ southJet.getArmor(), 10, App.HEIGHT - 2);

		if (northJetExp.getUpgradeSlot() > 0) {
			g2d.setColor(Color.RED);
			g2d.drawString("Upgrade", App.WIDTH - 55, App.HEIGHT / 2 - 100);
			g2d.drawString("1: Damage", App.WIDTH - 60, App.HEIGHT / 2 - 80);
			g2d.drawString("2: Armor", App.WIDTH - 60, App.HEIGHT / 2 - 60);
			if (northJetHp.getWidth() <= 190) {
				g2d.drawString("3: HP", App.WIDTH - 60, App.HEIGHT / 2 - 40);
			} else {
				g2d.setColor(Color.GRAY);
				g2d.drawString("3: HP", App.WIDTH - 60, App.HEIGHT / 2 - 40);
			}
		}

		if (southJetExp.getUpgradeSlot() > 0) {
			g2d.setColor(Color.RED);
			g2d.drawString("Upgrade", 15, App.HEIGHT - 100);
			g2d.drawString("/: Damage", 10, App.HEIGHT - 80);
			g2d.drawString("*: Armor", 10, App.HEIGHT - 60);
			if (southJetHp.getWidth() <= 190) {
				g2d.drawString("-: HP", 10, App.HEIGHT - 40);
			} else {
				g2d.setColor(Color.GRAY);
				g2d.drawString("-: HP", 10, App.HEIGHT - 40);
			}
		}

		// north hp
		g2d.setColor(Color.RED);
		g2d.drawRect(northJetHpbackground.getX(), northJetHpbackground.getY(), northJetHpbackground.getWidth(),
				northJetHpbackground.getHeight());
		g2d.setColor(Color.GREEN);
		g2d.fillRect(northJetHp.getX(), northJetHp.getY(), northJetHp.getWidth(), northJetHp.getHeight());
		g2d.setColor(Color.BLACK);

		// south hp
		g2d.setColor(Color.RED);
		g2d.drawRect(southJetHpbackground.getX(), southJetHpbackground.getY(), southJetHpbackground.getWidth(),
				southJetHpbackground.getHeight());
		g2d.setColor(Color.GREEN);
		g2d.fillRect(southJetHp.getX(), southJetHp.getY(), southJetHp.getWidth(), southJetHp.getHeight());

		// north pow
		g2d.setColor(Color.BLUE);
		g2d.fillRect(1, 1, 19, northJetPow.getPow());
		g2d.setColor(Color.YELLOW);
		g2d.drawRect(0, 0, 20, 201);
		g2d.drawLine(0, 41, 20, 41);
		g2d.drawLine(0, 81, 20, 81);
		g2d.drawLine(0, 121, 20, 121);
		g2d.drawLine(0, 161, 20, 161);

		// south pow
		g2d.setColor(Color.BLUE);
		g2d.fillRect(App.WIDTH - 21, App.HEIGHT - southJetPow.getPow(), 20, southJetPow.getPow());
		g2d.setColor(Color.YELLOW);
		g2d.drawRect(App.WIDTH - 21, App.HEIGHT - 200, 20, 200);
		g2d.drawLine(App.WIDTH - 21, App.HEIGHT - 160, App.WIDTH, App.HEIGHT - 160);
		g2d.drawLine(App.WIDTH - 21, App.HEIGHT - 120, App.WIDTH, App.HEIGHT - 120);
		g2d.drawLine(App.WIDTH - 21, App.HEIGHT - 80, App.WIDTH, App.HEIGHT - 80);
		g2d.drawLine(App.WIDTH - 21, App.HEIGHT - 40, App.WIDTH, App.HEIGHT - 40);

		// north exp
		g2d.setColor(Color.BLACK);
		g2d.drawRect(northJetExpBackground.getX(), northJetExpBackground.getY(), northJetExpBackground.getWidth(),
				northJetExpBackground.getHeight());
		g2d.setColor(Color.gray);
		g2d.fillRect(northJetExp.getX(), northJetExp.getY(), northJetExp.getWidth(), northJetExp.getHeight());

		Font font3 = new Font("Arial", Font.BOLD, 8);
		g2d.setFont(font3);
		ArrayList<Exp> expsNorth = this.expNorth;
		for (int i = 0; i < expsNorth.size(); i++) {
			Exp exp = expsNorth.get(i);
			g2d.setColor(Color.YELLOW);
			g2d.fillOval(exp.getX_exp(), exp.getY_exp(), 20, 20);
			g2d.setColor(Color.RED);
			g2d.drawString("EXP", exp.getX_exp() + 2, exp.getY_exp() + 13);
		}

		// south exp
		g2d.setColor(Color.BLACK);
		g2d.drawRect(southJetExpBackground.getX(), southJetExpBackground.getY(), southJetExpBackground.getWidth(),
				southJetExpBackground.getHeight());
		g2d.setColor(Color.gray);
		g2d.fillRect(southJetExp.getX(), southJetExp.getY(), southJetExp.getWidth() + 1, southJetExp.getHeight());

		ArrayList<Exp> expsSouth = this.expSouth;
		for (int i = 0; i < expsSouth.size(); i++) {
			Exp exp = expsSouth.get(i);
			g2d.setColor(Color.YELLOW);
			g2d.fillOval(exp.getX_exp(), exp.getY_exp(), 20, 20);
			g2d.setColor(Color.RED);
			g2d.drawString("EXP", exp.getX_exp() + 2, exp.getY_exp() + 13);
		}

		// north jet
		g2d.setColor(new Color(138, 143, 181));
		g2d.fillRect(northJet.getX(), northJet.getY(), northJet.getW(), northJet.getH());
		g2d.setColor(new Color(95, 99, 126));
		g2d.fillPolygon(new int[] { northJet.getX() - 25, northJet.getX(), northJet.getX() },
				new int[] { northJet.getY(), northJet.getY(), northJet.getY() + 15 }, 3);
		g2d.fillPolygon(
				new int[] { northJet.getX() + northJet.getW(), northJet.getX() + northJet.getW() + 25,
						northJet.getX() + northJet.getW() },
				new int[] { northJet.getY(), northJet.getY(), northJet.getY() + 15 }, 3);
		g2d.setColor(new Color(112, 107, 104));
		g2d.fillPolygon(
				new int[] { northJet.getX(), northJet.getX() + northJet.getW(), northJet.getX() + northJet.getW() / 2 },
				new int[] { northJet.getY() + northJet.getH(), northJet.getY() + northJet.getH(),
						northJet.getY() + northJet.getH() + 15 },
				3);

		// south jet
		g2d.setColor(new Color(138, 143, 181));
		g2d.fillRect(southJet.getX(), southJet.getY(), southJet.getW(), southJet.getH());
		g2d.setColor(new Color(95, 99, 126));
		g2d.fillPolygon(
				new int[] { southJet.getX() - 25, southJet.getX(), southJet.getX() }, new int[] {
						southJet.getY() + southJet.getH(), southJet.getY() + southJet.getH(), southJet.getY() + 25 },
				3);
		g2d.fillPolygon(
				new int[] { southJet.getX() + southJet.getW() + 25, southJet.getX() + southJet.getW(),
						southJet.getX() + southJet.getW() },
				new int[] { southJet.getY() + southJet.getH(), southJet.getY() + southJet.getH(),
						southJet.getY() + 25 },
				3);
		g2d.setColor(new Color(112, 107, 104));
		g2d.fillPolygon(
				new int[] { southJet.getX(), southJet.getX() + southJet.getW(), southJet.getX() + southJet.getW() / 2 },
				new int[] { southJet.getY(), southJet.getY(), southJet.getY() - 15 }, 3);

		// north bullets
		ArrayList<Bullet> northBullets = northJet.getBullets();
		for (int i = 0; i < northBullets.size(); i++) {
			Bullet bullet = (Bullet) northBullets.get(i);
			// setting bullet colors
			g2d.setColor(Color.DARK_GRAY);
			g2d.fillRect(bullet.getX(), bullet.getY(), bullet.getW(), bullet.getH());
		}

		// south bullets
		ArrayList<Bullet> southBullets = southJet.getBullets();
		for (int i = 0; i < southBullets.size(); i++) {
			Bullet bullet = (Bullet) southBullets.get(i);
			// setting bullet colors
			g2d.setColor(Color.BLACK);
			g2d.fillRect(bullet.getX(), bullet.getY(), bullet.getW(), bullet.getH());
		}
	}
	int i = 3;
	public void play() {
		root = new Node(heuristic(false, southJet.getX(), southJet.getY()), null, southJet.getX(), southJet.getY());
//		
//		long begin = System.currentTimeMillis();
//		long memoryBeforeExcute = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		
		root = getBestMove(i, Map.root);
		
//		long memoryAfterExcute = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
//		long end = System.currentTimeMillis();
//		
//		System.out.println("Depth: " + i);
//		System.out.println("Memory used: " + (memoryAfterExcute - memoryBeforeExcute) + " Bytes");
//		System.out.println("Computed time: " + (end - begin) + " ms");
		
		northJet.autoPlay();
		
//		try {
//			Thread.sleep(50000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		

		///// moving jet
		// south jet
		if (moveSouthLeft) {
			southJet.move(1);
		}

		if (moveSouthRight) {
			southJet.move(2);
		}

		if (moveSouthUp) {
			southJet.move(3);
		}

		if (moveSouthDown) {
			southJet.move(4);
		}

		///// moving bullets
		// north bullets
		ArrayList<Bullet> northBullets = northJet.getBullets();
		for (int i = 0; i < northBullets.size(); i++) {
			Bullet bullet = (Bullet) northBullets.get(i);

			bullet.move(1, 7);

			if (bullet.getX() > App.WIDTH || bullet.getX() < 0 || bullet.getY() > App.HEIGHT || bullet.getY() < 0) {
				northBullets.remove(i);
			}

		}

		// south bullets
		ArrayList<Bullet> southBullets = southJet.getBullets();
		for (int i = 0; i < southBullets.size(); i++) {
			Bullet bullet = (Bullet) southBullets.get(i);

			bullet.move(2, 7);

			if (bullet.getX() > App.WIDTH || bullet.getX() < 0 || bullet.getY() > App.HEIGHT || bullet.getY() < 0) {
				southBullets.remove(i);
			}

		}

		///// hp
		// north hp
		for (int i = 0; i < southBullets.size(); i++) {
			Bullet bullet = southBullets.get(i);
			int xBullet = bullet.getX();
			int yBullet = bullet.getY();
			int wBullet = bullet.getW();
			int hBullet = bullet.getH();

			int xNorthJet = northJet.getX();
			int yNorthJet = northJet.getY();
			int wNorthJet = northJet.getW();
			int hNorthJet = northJet.getH();

			if (// nose
			(xNorthJet + 10 <= xBullet + wBullet && xNorthJet + 10 >= xBullet && yNorthJet + 55 <= yBullet + hBullet
					&& yNorthJet + 55 >= yBullet)
					|| (xNorthJet + 10 <= xBullet + wBullet && xNorthJet + 10 >= xBullet
							&& yNorthJet + 40 <= yBullet + hBullet && yNorthJet + 40 >= yBullet)
					// left side
					|| (xNorthJet <= xBullet + wBullet && xNorthJet >= xBullet && yNorthJet + 40 <= yBullet + hBullet
							&& yNorthJet + 40 >= yBullet)
					|| (xNorthJet <= xBullet + wBullet && xNorthJet >= xBullet && yNorthJet + 30 <= yBullet + hBullet
							&& yNorthJet + 30 >= yBullet)
					|| (xNorthJet <= xBullet + wBullet && xNorthJet >= xBullet && yNorthJet + 20 <= yBullet + hBullet
							&& yNorthJet + 20 >= yBullet)
					|| (xNorthJet <= xBullet + wBullet && xNorthJet >= xBullet && yNorthJet + 10 <= yBullet + hBullet
							&& yNorthJet + 10 >= yBullet)
					||
					// right side
					(xNorthJet + 20 <= xBullet + wBullet && xNorthJet + 20 >= xBullet
							&& yNorthJet + 40 <= yBullet + hBullet && yNorthJet + 40 >= yBullet)
					|| (xNorthJet + 20 <= xBullet + wBullet && xNorthJet + 20 >= xBullet
							&& yNorthJet + 30 <= yBullet + hBullet && yNorthJet + 30 >= yBullet)
					|| (xNorthJet + 20 <= xBullet + wBullet && xNorthJet + 20 >= xBullet
							&& yNorthJet + 20 <= yBullet + hBullet && yNorthJet + 20 >= yBullet)
					|| (xNorthJet + 20 <= xBullet + wBullet && xNorthJet + 20 >= xBullet
							&& yNorthJet + 10 <= yBullet + hBullet && yNorthJet + 10 >= yBullet)
					||
					// left wing
					(xNorthJet - 12.5 <= xBullet + wBullet && xNorthJet - 12.5 >= xBullet
							&& yNorthJet + 7.5 <= yBullet + hBullet && yNorthJet + 7.5 >= yBullet)
					|| (xNorthJet - 25 <= xBullet + wBullet && xNorthJet - 25 >= xBullet
							&& yNorthJet <= yBullet + hBullet && yNorthJet >= yBullet)
					||
					// right wing
					(xNorthJet + 32.5 <= xBullet + wBullet && xNorthJet + 32.5 >= xBullet
							&& yNorthJet + 7.5 <= yBullet + hBullet && yNorthJet + 7.5 >= yBullet)
					|| (xNorthJet + 45 <= xBullet + wBullet && xNorthJet + 45 >= xBullet
							&& yNorthJet <= yBullet + hBullet && yNorthJet >= yBullet)) {

				if (bullet.getW() == 50) {
					if (southJet.getDamage() + 10 <= northJet.getArmor()) {
						northJetHp.decreaseHP(3);
					} else {
						northJetHp.decreaseHP(southJet.getDamage() + 10 - northJet.getArmor());
					}
				} else if (bullet.getW() == 60) {
					if (southJet.getDamage() + 15 <= northJet.getArmor()) {
						northJetHp.decreaseHP(3);
					} else {
						northJetHp.decreaseHP(southJet.getDamage() + 15 - northJet.getArmor());
					}
				} else if (bullet.getW() == 70) {
					if (southJet.getDamage() + 20 <= northJet.getArmor()) {
						northJetHp.decreaseHP(3);
					} else {
						northJetHp.decreaseHP(southJet.getDamage() + 20 - northJet.getArmor());
					}
				} else if (bullet.getW() == 80) {
					if (southJet.getDamage() + 25 <= northJet.getArmor()) {
						northJetHp.decreaseHP(3);
					} else {
						northJetHp.decreaseHP(southJet.getDamage() + 25 - northJet.getArmor());
					}
				} else if (bullet.getW() == 100) {
					if (southJet.getDamage() + 30 <= northJet.getArmor()) {
						northJetHp.decreaseHP(3);
					} else {
						northJetHp.decreaseHP(southJet.getDamage() + 30 - northJet.getArmor());
					}
				} else {
					if (southJet.getDamage() <= northJet.getArmor()) {
						northJetHp.decreaseHP(3);
					} else {
						northJetHp.decreaseHP(southJet.getDamage() - northJet.getArmor());
					}

					// setting pow
					southJetPow.increasePow(southJetPow.getExtraPow());
				}
				southBullets.remove(i);
			}
		}

		// south hp
		for (int i = 0; i < northBullets.size(); i++) {
			Bullet bullet = northBullets.get(i);
			int xBullet = bullet.getX();
			int yBullet = bullet.getY();
			int wBullet = bullet.getW();
			int hBullet = bullet.getH();

			int xSouthJet = southJet.getX();
			int ySouthJet = southJet.getY();
			int wSouthJet = southJet.getW();
			int hSouthJet = southJet.getH();

			if (// nose
			(xSouthJet + 10 <= xBullet + wBullet && xSouthJet + 10 >= xBullet && ySouthJet - 15 <= yBullet + hBullet
					&& ySouthJet - 15 >= yBullet)
					|| (xSouthJet + 10 <= xBullet + wBullet && xSouthJet + 10 >= xBullet
							&& ySouthJet <= yBullet + hBullet && ySouthJet >= yBullet)
					||
					// left side
					(xSouthJet <= xBullet + wBullet && xSouthJet >= xBullet && ySouthJet <= yBullet + hBullet
							&& ySouthJet >= yBullet)
					|| (xSouthJet <= xBullet + wBullet && xSouthJet >= xBullet && ySouthJet + 10 <= yBullet + hBullet
							&& ySouthJet + 10 >= yBullet)
					|| (xSouthJet <= xBullet + wBullet && xSouthJet >= xBullet && ySouthJet + 20 <= yBullet + hBullet
							&& ySouthJet + 20 >= yBullet)
					|| (xSouthJet <= xBullet + wBullet && xSouthJet >= xBullet && ySouthJet + 30 <= yBullet + hBullet
							&& ySouthJet + 30 >= yBullet)
					||
					// right side
					(xSouthJet + 20 <= xBullet + wBullet && xSouthJet + 20 >= xBullet && ySouthJet <= yBullet + hBullet
							&& ySouthJet >= yBullet)
					|| (xSouthJet + 20 <= xBullet + wBullet && xSouthJet + 20 >= xBullet
							&& ySouthJet + 10 <= yBullet + hBullet && ySouthJet + 10 >= yBullet)
					|| (xSouthJet + 20 <= xBullet + wBullet && xSouthJet + 20 >= xBullet
							&& ySouthJet + 20 <= yBullet + hBullet && ySouthJet + 20 >= yBullet)
					|| (xSouthJet + 20 <= xBullet + wBullet && xSouthJet + 20 >= xBullet
							&& ySouthJet + 30 <= yBullet + hBullet && ySouthJet + 30 >= yBullet)
					||
					// left wing
					(xSouthJet - 12.5 <= xBullet + wBullet && xSouthJet - 12.5 >= xBullet
							&& ySouthJet + 32.5 <= yBullet + hBullet && ySouthJet + 32.5 >= yBullet)
					|| (xSouthJet - 25 <= xBullet + wBullet && xSouthJet - 25 >= xBullet
							&& ySouthJet + 40 <= yBullet + hBullet && ySouthJet + 40 >= yBullet)
					||
					// right wing
					(xSouthJet + 32.5 <= xBullet + wBullet && xSouthJet + 32.5 >= xBullet
							&& ySouthJet + 32.5 <= yBullet + hBullet && ySouthJet + 32.5 >= yBullet)
					|| (xSouthJet + 45 <= xBullet + wBullet && xSouthJet + 45 >= xBullet
							&& ySouthJet + 40 <= yBullet + hBullet && ySouthJet + 40 >= yBullet)) {

				if (bullet.getW() == 50) {
					if (northJet.getDamage() + 10 <= southJet.getArmor()) {
						southJetHp.decreaseHP(3);
					} else {
						southJetHp.decreaseHP(northJet.getDamage() + 10 - southJet.getArmor());
					}
				} else if (bullet.getW() == 60) {
					if (northJet.getDamage() + 15 <= southJet.getArmor()) {
						southJetHp.decreaseHP(3);
					} else {
						southJetHp.decreaseHP(northJet.getDamage() + 15 - southJet.getArmor());
					}
				} else if (bullet.getW() == 70) {
					if (northJet.getDamage() + 20 <= southJet.getArmor()) {
						southJetHp.decreaseHP(3);
					} else {
						southJetHp.decreaseHP(northJet.getDamage() + 20 - southJet.getArmor());
					}
				} else if (bullet.getW() == 80) {
					if (northJet.getDamage() + 25 <= southJet.getArmor()) {
						southJetHp.decreaseHP(3);
					} else {
						southJetHp.decreaseHP(northJet.getDamage() + 25 - southJet.getArmor());
					}
				} else if (bullet.getW() == 100) {
					if (northJet.getDamage() + 30 <= southJet.getArmor()) {
						southJetHp.decreaseHP(3);
					} else {
						southJetHp.decreaseHP(northJet.getDamage() + 30 - southJet.getArmor());
					}
				} else {
					if (northJet.getDamage() <= southJet.getArmor()) {
						southJetHp.decreaseHP(3);
					} else {
						southJetHp.decreaseHP(northJet.getDamage() - southJet.getArmor());
					}

					// setting pow
					northJetPow.increasePow(northJetPow.getExtraPow());
				}
				northBullets.remove(i);
			}
		}

		///// exp increase

		// north exp
		for (int i = 0; i < expNorth.size(); i++) {
			Exp exp = expNorth.get(i);
			if (exp.getX_exp() >= northJet.getX() - 23 && exp.getX_exp() <= northJet.getX() + 40
					&& exp.getY_exp() >= northJet.getY() - 20 && exp.getY_exp() <= northJet.getY() + 40) {
				northJetExp.increaseExp(180 / (northJet.getLevel() + 1));
				expNorth.remove(i);
			}
		}

		// south exp
		for (int i = 0; i < expSouth.size(); i++) {
			Exp exp = expSouth.get(i);
			if (exp.getY_exp() >= southJet.getY() - 20 && exp.getY_exp() <= southJet.getY() + 40
					&& exp.getX_exp() >= southJet.getX() - 23 && exp.getX_exp() <= southJet.getX() + 40) {
				southJetExp.increaseExp(180 / (southJet.getLevel() + 1));
				expSouth.remove(i);
			}
		}

		if (endGame) {
			JOptionPane.showMessageDialog(null, "Trò Chơi Kết Thúc !!!");
		}
	}

	public static int heuristic(boolean isMax, int x, int y) {
		int result = 10;
		if (!isMax) {
			// hp
			if (northJetHp.getWidth() > southJetHp.getWidth()) {
				result += (northJetHp.getWidth() - southJetHp.getWidth()) / 5 * 2;
			} else if (northJetHp.getWidth() < southJetHp.getWidth()) {
				result -= (southJetHp.getWidth() - northJetHp.getWidth()) / 5 * 2;
			} else {
				result += 0;
			}

			// exp
			if (northJet.getLevel() > southJet.getLevel()) {
				result += (northJet.getLevel() - southJet.getLevel()) * 5;
			} else if (northJet.getLevel() < southJet.getLevel()) {
				result -= (southJet.getLevel() - northJet.getLevel()) * 5;
			} else {
				result += 0;
			}

			// pow
			if (northJetPow.getPow() > southJetPow.getPow()) {
				result += (northJetPow.getPow() - southJetPow.getPow()) / 20 * 3;
			} else if (northJetPow.getPow() < southJetPow.getPow()) {
				result -= (southJetPow.getPow() - northJetPow.getPow()) / 20 * 3;
			} else {
				result += 0;
			}

			// shoot turn
			if (x + 10 > southJet.getX() - 15 && x + 10 < southJet.getX() + 35) {
				result += 5;
			}

			// move
			if (y > App.HEIGHT / 4) {
				result += 10;
			}
		} else {
			// hp
			if (southJetHp.getWidth() > northJetHp.getWidth()) {
				result += (southJetHp.getWidth() - northJetHp.getWidth()) / 5 * 2;
			} else if (southJetHp.getWidth() < northJetHp.getWidth()) {
				result -= (northJetHp.getWidth() - southJetHp.getWidth()) / 5 * 2;
			} else {
				result += 0;
			}

			// exp
			if (southJet.getLevel() > northJet.getLevel()) {
				result += (southJet.getLevel() - northJet.getLevel()) * 5;
			} else if (southJet.getLevel() < northJet.getLevel()) {
				result -= (northJet.getLevel() - southJet.getLevel()) * 5;
			} else {
				result += 0;
			}

			// pow
			if (southJetPow.getPow() > northJetPow.getPow()) {
				result += (southJetPow.getPow() - northJetPow.getPow()) / 20 * 3;
			} else if (southJetPow.getPow() < northJetPow.getPow()) {
				result -= (northJetPow.getPow() - southJetPow.getPow()) / 20 * 3;
			} else {
				result += 0;
			}

			// shoot turn
			if (x + 10 > northJet.getX() - 15 && x + 10 < northJet.getX() + 35) {
				result += 5;
			}

			// move
			if (y < App.HEIGHT / 4 * 3 - 40) {
				result += 10;
			}
		}

		return result;
	}

	public static int minimax(int depth, Node state, boolean isMax) {
		if (depth == 0) {
			return heuristic(isMax, state.getX(), state.getY());
		}

		if (isMax) {
			int bestValue = Integer.MIN_VALUE;
			xNorth = state.getParent().getX();
			yNorth = state.getParent().getY();

			for (Node node : generateChildren(state, isMax).getChildrens()) {
				int value = minimax(depth - 1, node, !isMax);
				bestValue = Math.max(value, bestValue);
			}

			return bestValue;
		} else {
			int bestValue = Integer.MAX_VALUE;
			xSouth = state.getParent().getX();
			ySouth = state.getParent().getY();

			for (Node node : generateChildren(state, isMax).getChildrens()) {
				int value = minimax(depth - 1, node, !isMax);
				bestValue = Math.min(value, bestValue);
			}

			return bestValue;
		}
	}
	
	public static int alphabeta(int depth, Node state, boolean isMax, int alpha, int beta) {
		if (depth == 0) {
			return heuristic(isMax, state.getX(), state.getY());
		}

		if (isMax) {
			int bestValue = Integer.MIN_VALUE;
			for (Node node : generateChildren(state, isMax).getChildrens()) {
				int value = alphabeta(depth - 1, node, !isMax, alpha, beta);
				if (value > bestValue) {
					bestValue = value;
				}

				alpha = Math.max(alpha, bestValue);
				if (beta <= alpha) break;
			}
			
			return bestValue;
		} else {
			int bestValue = Integer.MAX_VALUE;
			for (Node node : generateChildren(state, isMax).getChildrens()) {
				int value = alphabeta(depth - 1, node, !isMax, alpha, beta);
				if (value < bestValue) {
					bestValue = value;
				}
				
				beta = Math.min(beta, bestValue);
				if (beta <= alpha) break;
			}
			
			return bestValue;
		}
	}

	public static Node generateChildren(Node node, boolean isMax) {
		int move = 40;
		int x, y;

		if (isMax) {
			x = Map.xNorth;
			y = Map.yNorth;
		} else {
			x = Map.xSouth;
			y = Map.ySouth;
		}

		for (String command : commands) {
			// make move
			if (command.equalsIgnoreCase("L")) {
				if (x < move) {
					continue;
				} else {
					x -= move;
				}
			} else if (command.equalsIgnoreCase("R")) {
				if (x > App.WIDTH - move - northJet.getW()) {
					continue;
				} else {
					x += move;
				}
			} else if (command.equalsIgnoreCase("U")) {
				if (y < move) {
					continue;
				} else {
					y -= move;
				}
			} else if (command.equalsIgnoreCase("D")) {
				if (y > (App.HEIGHT / 2) - move - (northJet.getH() + 15)) {
					continue;
				} else {
					y += move;
				}
			}

			// add child for parent
			node.addChild(new Node(heuristic(!isMax, x, y), node, x, y));

			// undo move
			if (isMax) {
				x = Map.xNorth;
				y = Map.yNorth;
			} else {
				x = Map.xSouth;
				y = Map.ySouth;
			}
		}

		return node;
	}
	
	public static Node getBestMove(int depth, Node state) {
		Node result = null;
		int maxValue = Integer.MIN_VALUE;
		for (Node node : generateChildren(state, shooting).getChildrens()) {
			int bestValue = minimax(depth - 1, node, !shooting);
//			int bestValue = alphabeta(depth - 1, node, !shooting, Integer.MIN_VALUE, Integer.MAX_VALUE);
			if (bestValue > maxValue) {
				maxValue = bestValue;
				result = node;
			}
		}
		return result;
	}

	@Override
	public void run() {
		while (true) {
			repaint();
			play();
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}