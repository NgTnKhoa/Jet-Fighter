package Model;

import java.util.ArrayList;
import java.util.Random;

import Controller.MoveController;
import Controller.ShootController;
import Controller.UpgradeController;
import View.App;
import View.Map;

public class Jet {

	private int x, y;
	private int w, h;
	private int speed;
	private int level;
	private int damage;
	private int armor;
	private ArrayList<Bullet> bullets;

	public Jet(int x, int y, int w, int h, int speed, int level, int damage, int armor) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.speed = speed;
		this.level = level;
		this.damage = damage;
		this.armor = armor;

		bullets = new ArrayList<Bullet>();
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getW() {
		return w;
	}

	public int getH() {
		return h;
	}

	public ArrayList<Bullet> getBullets() {
		return bullets;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public int getArmor() {
		return armor;
	}

	public void setArmor(int armor) {
		this.armor = armor;
	}

	public int getRandom(int[] value) {
		int randomNum = value[new Random().nextInt(value.length)];
		return randomNum;
	}

	public void move(int direction) {
		// direction 1:left, 2:right, 3:up, 4:down
		if (direction == 1) {
			if (x > 0) {
				x -= speed;
			}
		} else if (direction == 2) {
			if (x < App.WIDTH - w) {
				x += speed;
			}
		} else if (direction == 3) {
			if (y > App.HEIGHT / 2 + 15) {
				y -= speed;
			}
		} else {
			if (y < App.HEIGHT - h) {
				y += speed;
			}
		}
	}

	public void autoMoveWhenNotShootTurn() {
		if (Map.shooting) {
			int move = new Random().nextInt(2) + 1;

			if (Map.moved >= Map.distanceMove) {
				if (move == 1) {
					Map.direction = true;
				}

				if (move == 2) {
					Map.direction = false;
				}
				Map.moved = 0;
			}

			if (x >= 0 && Map.direction) {
				if (290 - x >= 290) {
					Map.direction = false;
				}
				x -= speed;
				Map.moved += speed;
			}

			if (x <= App.WIDTH - w && !Map.direction) {
				if (x - 290 >= 290) {
					Map.direction = true;
				}
				x += speed;
				Map.moved += speed;
			}

			if (y > 0) {
				y -= speed;
			}
		}
	}

	public void autoMoveWhenShootTurn() {
		if (!Map.shooting) {
			if (x > 0) {
				if (x >= Map.root.getX()) {
					x -= speed;
				}
			}

			if (x < App.WIDTH - w) {
				if (x < Map.root.getX()) {
					x += speed;
				}
			}

			if (y < App.HEIGHT / 2 - h - 15 - 100) {
				y += speed;
			}
		}
	}

	public void autoFindExp() {
		if (Map.expNorth.size() > 0) {
			for (Exp exp : Map.expNorth) {
				if (x > exp.getX_exp()) {
					x -= speed;
				}

				if (x < exp.getX_exp()) {
					x += speed;
				}

				if (y > exp.getY_exp()) {
					y -= speed;
				}

				if (y < exp.getY_exp()) {
					y += speed;
				}
			}
		}
	}

	public void autoUpgrade() {
		if (Map.northJetExp.getUpgradeSlot() > 0) {
			// upgrade when over 50% hp
			if (Map.northJetHp.getWidth() >= 100) {
				// random 1 in 3
				int rd = new Random().nextInt(3) + 1;

				if (Map.northJetHp.getWidth() > 190) {
					while (rd == 3) {
						rd = new Random().nextInt(3) + 1;
					}
				}

				if (rd == 1) {
					Map.northJet.setDamage(Map.northJet.getDamage() + 5);
					Map.northJetExp.setUpgradeSlot(Map.northJetExp.getUpgradeSlot() - 1);
					System.out.println("Damage upgraded");
				} else if (rd == 2) {
					Map.northJet.setArmor(Map.northJet.getArmor() + 5);
					Map.northJetExp.setUpgradeSlot(Map.northJetExp.getUpgradeSlot() - 1);
					System.out.println("Armor upgraded");
				} else {
					Map.northJetHp.setWidth(Map.northJetHp.getWidth() + 10);
					Map.northJetExp.setUpgradeSlot(Map.northJetExp.getUpgradeSlot() - 1);
					System.out.println("Healed");
				}
				// upgrade when low hp
			} else if (Map.northJetHp.getWidth() < 100) {
				if (Map.northJetHp.getWidth() < Map.southJetHp.getWidth()) {
					Map.northJetHp.setWidth(Map.northJetHp.getWidth() + 10);
					Map.northJetExp.setUpgradeSlot(Map.northJetExp.getUpgradeSlot() - 1);
					System.out.println("Healed");
				} else {
					int rd = new Random().nextInt(3) + 1;
					if (rd == 1) {
						Map.northJet.setDamage(Map.northJet.getDamage() + 5);
						Map.northJetExp.setUpgradeSlot(Map.northJetExp.getUpgradeSlot() - 1);
						System.out.println("Damage upgraded");
					} else if (rd == 2) {
						Map.northJet.setArmor(Map.northJet.getArmor() + 5);
						Map.northJetExp.setUpgradeSlot(Map.northJetExp.getUpgradeSlot() - 1);
						System.out.println("Armor upgraded");
					} else {
						Map.northJetHp.setWidth(Map.northJetHp.getWidth() + 10);
						Map.northJetExp.setUpgradeSlot(Map.northJetExp.getUpgradeSlot() - 1);
						System.out.println("Healed");
					}
				}
			}

		}
	}

	public void autoShoot() {
		if (Map.southJet.getX() <= 20) {
			// shoot left side
			if (x <= 45) {
				if (Map.northJetPow.getPow() == Map.randomPow) {
					skill(1);
					Map.shooting = true;
				} else {
					Map.northJet.fire(1);
					Map.shooting = true;
				}
			}
		} else if (Map.southJet.getX() >= App.WIDTH - Map.southJet.getW() - 20) {
			// shoot right side
			if (x >= App.WIDTH - Map.southJet.getW() - 45) {
				if (Map.northJetPow.getPow() == Map.randomPow) {
					skill(1);
					Map.shooting = true;
				} else {
					Map.northJet.fire(1);
					Map.shooting = true;
				}
			}
		} else {
			if (Math.abs(x - Map.root.getX()) < 5) {
				if (Map.northJetPow.getPow() == Map.randomPow) {
					skill(1);
					Map.shooting = true;
				} else {
					Map.northJet.fire(1);
					Map.shooting = true;
				}
			}
		}
	}

//	public int getDisCal() {
//		if (MoveController.leftCount > MoveController.rightCount) {
//			return -30;
//		} else if (MoveController.leftCount < MoveController.rightCount) {
//			return 30;
//		} else {
//			return 0;
//		}
//	}

	public void autoPlay() {
		Map.root = Map.getBestMove(2, Map.root);
		
		autoUpgrade();

		if (Map.shooting) {
			autoMoveWhenNotShootTurn();
		} else {
			if (Map.expNorth.size() > 0) {
				autoFindExp();
			} else {
				autoMoveWhenShootTurn();
				autoShoot();
			}
		}
	}

	public void fire(int jet) {
		// jet 1:north, 2:south
		if (jet == 1) {
			// north exp
			Random random = new Random();
			Exp exp = new Exp(0, 0);
			int x = Map.northJet.getX();
			int y = Map.northJet.getY();

			// set location north exp
			if (x >= 0 && x <= App.WIDTH / 3) {
				exp.setX_exp(random.nextInt(App.WIDTH / 3 + 20, App.WIDTH));
				exp.setY_exp(random.nextInt(20, App.HEIGHT / 2 - 62));
			} else if (x <= App.WIDTH && x >= (App.WIDTH / 3) * 2) {
				exp.setX_exp(random.nextInt(0, (App.WIDTH / 3) * 2 - 80));
				exp.setY_exp(random.nextInt(20, App.HEIGHT / 2 - 62));
			} else {
				int number1 = random.nextInt(0, App.WIDTH / 3 + 20);

				int number2 = random.nextInt((App.WIDTH / 3) * 2 - 20, App.WIDTH - 20);

				int randomNumber = random.nextBoolean() ? number2 : number1;

				exp.setX_exp(randomNumber);
				exp.setY_exp(random.nextInt(20, App.HEIGHT / 2 - 62));
			}
			Map.expNorth.add(exp);

			// setting the bullet
			Map.bullet.setX(x + w / 2 - 10);
			Map.bullet.setY(y + h + 10);
			Map.bullet.setW(20);
			Map.bullet.setH(20);

			// adding the bullet to the array list
			bullets.add(new Bullet(Map.bullet.getX(), Map.bullet.getY(), Map.bullet.getW(), Map.bullet.getH()));
		} else {
			// south exp
			Exp exp2 = new Exp(0, 0);
			Random random = new Random();
			int x = Map.southJet.getX();
			int y = Map.southJet.getY();

			// Set location south exp
			if (x >= 0 && x <= App.WIDTH / 3) {
				exp2.setX_exp(random.nextInt(App.WIDTH / 3 + 60, App.WIDTH));
				exp2.setY_exp(random.nextInt(App.HEIGHT / 2 + 43, App.HEIGHT - 20));

			} else if (x <= App.WIDTH && x >= (App.WIDTH / 3) * 2) {
				exp2.setX_exp(random.nextInt(0, (App.WIDTH / 3) * 2 - 80));
				exp2.setY_exp(random.nextInt(App.HEIGHT / 2 + 43, App.HEIGHT - 20));

			} else {
				int number1 = random.nextInt(0, App.WIDTH / 3 + 20);

				int number2 = random.nextInt((App.WIDTH / 3) * 2 - 20, App.WIDTH - 20);

				int randomNumber = random.nextBoolean() ? number2 : number1;

				exp2.setX_exp(randomNumber);
				exp2.setY_exp(random.nextInt(App.HEIGHT / 2 + 43, App.HEIGHT - 20));
			}
			Map.expSouth.add(exp2);

			// setting the bullet
			Map.bullet.setX(x + w / 2 - 10);
			Map.bullet.setY(y - h + 15);
			Map.bullet.setW(20);
			Map.bullet.setH(20);

			// adding the bullet to the array list
			bullets.add(new Bullet(Map.bullet.getX(), Map.bullet.getY(), Map.bullet.getW(), Map.bullet.getH()));
		}

	}

	public void skill(int jet) {
		// jet 1:north, 2:south
		if (jet == 1) {
			if (Map.northJetPow.getPow() >= 40) {
				if (Map.northJetPow.getPow() >= 40 && Map.northJetPow.getPow() < 80) {
					// setting the bullet
					Map.bullet.setX(x + w / 2 - 25);
					Map.bullet.setY(y + h + 10);
					Map.bullet.setW(50);
					Map.bullet.setH(50);

					// adding the bullet to the array list
					bullets.add(new Bullet(Map.bullet.getX(), Map.bullet.getY(), Map.bullet.getW(), Map.bullet.getH()));
					Map.northJetPow.setPow(Map.northJetPow.getPow() - 40);
				} else if (Map.northJetPow.getPow() >= 80 && Map.northJetPow.getPow() < 120) {
					// setting the bullet
					Map.bullet.setX(x + w / 2 - 30);
					Map.bullet.setY(y + h + 10);
					Map.bullet.setW(60);
					Map.bullet.setH(60);

					// adding the bullet to the array list
					bullets.add(new Bullet(Map.bullet.getX(), Map.bullet.getY(), Map.bullet.getW(), Map.bullet.getH()));

					Map.northJetPow.setPow(Map.northJetPow.getPow() - 80);
				} else if (Map.northJetPow.getPow() >= 120 && Map.northJetPow.getPow() < 160) {
					// setting the bullet
					Map.bullet.setX(x + w / 2 - 35);
					Map.bullet.setY(y + h + 10);
					Map.bullet.setW(70);
					Map.bullet.setH(70);

					// adding the bullet to the array list
					bullets.add(new Bullet(Map.bullet.getX(), Map.bullet.getY(), Map.bullet.getW(), Map.bullet.getH()));

					Map.northJetPow.setPow(Map.northJetPow.getPow() - 120);
				} else if (Map.northJetPow.getPow() >= 160 && Map.northJetPow.getPow() < 200) {
					// setting the bullet
					Map.bullet.setX(x + w / 2 - 40);
					Map.bullet.setY(y + h + 10);
					Map.bullet.setW(80);
					Map.bullet.setH(80);

					// adding the bullet to the array list
					bullets.add(new Bullet(Map.bullet.getX(), Map.bullet.getY(), Map.bullet.getW(), Map.bullet.getH()));

					Map.northJetPow.setPow(Map.northJetPow.getPow() - 160);
				} else if (Map.northJetPow.getPow() == 200) {
					// setting the bullet
					Map.bullet.setX(x + w / 2 - 50);
					Map.bullet.setY(y + h + 10);
					Map.bullet.setW(100);
					Map.bullet.setH(100);

					// adding the bullet to the array list
					bullets.add(new Bullet(Map.bullet.getX(), Map.bullet.getY(), Map.bullet.getW(), Map.bullet.getH()));

					Map.northJetPow.setPow(0);
				}
			}
			Map.randomPow = Map.pows[new Random().nextInt(Map.pows.length)];
		} else {
			if (Map.southJetPow.getPow() >= 40) {
				if (Map.southJetPow.getPow() >= 40 && Map.southJetPow.getPow() < 80) {
					// setting the bullet
					Map.bullet.setX(x + w / 2 - 25);
					Map.bullet.setY(y - h - 15);
					Map.bullet.setW(50);
					Map.bullet.setH(50);

					// adding the bullet to the array list
					bullets.add(new Bullet(Map.bullet.getX(), Map.bullet.getY(), Map.bullet.getW(), Map.bullet.getH()));
					Map.southJetPow.setPow(Map.southJetPow.getPow() - 40);
				} else if (Map.southJetPow.getPow() >= 80 && Map.southJetPow.getPow() < 120) {
					// setting the bullet
					Map.bullet.setX(x + w / 2 - 30);
					Map.bullet.setY(y - h - 25);
					Map.bullet.setW(60);
					Map.bullet.setH(60);

					// adding the bullet to the array list
					bullets.add(new Bullet(Map.bullet.getX(), Map.bullet.getY(), Map.bullet.getW(), Map.bullet.getH()));

					Map.southJetPow.setPow(Map.southJetPow.getPow() - 80);
				} else if (Map.southJetPow.getPow() >= 120 && Map.southJetPow.getPow() < 160) {
					// setting the bullet
					Map.bullet.setX(x + w / 2 - 35);
					Map.bullet.setY(y - h - 35);
					Map.bullet.setW(70);
					Map.bullet.setH(70);

					// adding the bullet to the array list
					bullets.add(new Bullet(Map.bullet.getX(), Map.bullet.getY(), Map.bullet.getW(), Map.bullet.getH()));

					Map.southJetPow.setPow(Map.southJetPow.getPow() - 120);
				} else if (Map.southJetPow.getPow() >= 160 && Map.southJetPow.getPow() < 200) {
					// setting the bullet
					Map.bullet.setX(x + w / 2 - 40);
					Map.bullet.setY(y - h - 45);
					Map.bullet.setW(80);
					Map.bullet.setH(80);

					// adding the bullet to the array list
					bullets.add(new Bullet(Map.bullet.getX(), Map.bullet.getY(), Map.bullet.getW(), Map.bullet.getH()));
					Map.southJetPow.setPow(Map.southJetPow.getPow() - 160);
				} else {
					// setting the bullet
					Map.bullet.setX(x + w / 2 - 50);
					Map.bullet.setY(y - h - 65);
					Map.bullet.setW(100);
					Map.bullet.setH(100);

					// adding the bullet to the array list
					bullets.add(new Bullet(Map.bullet.getX(), Map.bullet.getY(), Map.bullet.getW(), Map.bullet.getH()));
					Map.southJetPow.setPow(0);
				}
			}
		}
	}
}