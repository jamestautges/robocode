package mine.controllers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javafx.geometry.Point2D;
import mine.models.EnemyRobot;
import mine.models.Motion;
import mine.models.VirtualBullet;
import robocode.AdvancedRobot;

public class AntiGravDodging extends AntiGravMovement {

	private EnemyRobot enemySelf;

	private List<VirtualBullet> virtualBullets = new ArrayList<VirtualBullet>();

	private final double bulletForceFactor = 1000.0;
	private final double perpendicularBulletForceFactor = 100.0;

	private double bulletDir = 1;

	public AntiGravDodging(AdvancedRobot me, EnemyRobot enemy) {
		super(me, enemy);

		enemySelf = new EnemyRobot(me);
	}

	public void update() {
		// update representation of me as the enemy
		enemySelf.setHeading(me.getHeadingRadians());
		enemySelf.setVelocity(me.getVelocity());
		enemySelf.setEnergy(me.getEnergy());

		// add any bullets that have just been fired
		updateBullets();

		getForceToCenter();

		Point2D force = new Point2D(0, 0);

		// enemy force
		force = addEnemyForce(force);

		// wall force
		force = addWallForce(force);

		// corner force
		force = addCornerForce(force);

		// perpendicular force
		force = addPerpendicularForce(force);

		// bullet force
		force = addBulletForce(force);

		System.out.println(force.getX() + ", " + force.getY());

		// generate movement based on cumulative force
		generateMovement(force);
	}

	private void updateBullets() {
		Iterator<VirtualBullet> i = virtualBullets.iterator();
		while (i.hasNext()) {
			VirtualBullet bullet = i.next();
			Point2D pos1 = bullet.getPosition();
			bullet.increment();
			Point2D pos2 = bullet.getPosition();
			if (!battleField.contains(bullet.getPosition()) || pos2.distance(me.getX(), me.getY()) > pos1.distance(me.getX(), me.getY())) {
				i.remove();
			}
		}
		
		double power = enemy.getDeltaEnergy();
		if (power <= -0.1 && power >= -3) {
			System.out.println("shot");
			double circularHeading = getCircularTargeting(power);
			double linearHeading = getLinearTargeting(power);
			double headOnHeading = getHeadOnTargeting(power);

			virtualBullets.add(new VirtualBullet(enemy.getPosition(),
					circularHeading, power));
			virtualBullets.add(new VirtualBullet(enemy.getPosition(),
					linearHeading, power));
			virtualBullets.add(new VirtualBullet(enemy.getPosition(),
					headOnHeading, power));
		}
	}

	private Point2D addBulletForce(Point2D force) {
		if (me.getX() < 60 || me.getBattleFieldWidth() - me.getX() < 60
				|| me.getY() < 60 || me.getBattleFieldHeight() - me.getY() < 60) {

			Point2D relCenter = new Point2D(
					(battleField.getMinX() + battleField.getMaxX()) / 2
							- me.getX(),
					(battleField.getMinY() + battleField.getMaxY()) / 2
							- me.getY());
			Point2D relEnemy = new Point2D(enemy.getX() - me.getX(),
					enemy.getY() - me.getY());

			if (relEnemy.crossProduct(relCenter).getZ() <= 0) {
				bulletDir = 1;
			} else {
				bulletDir = -1;
			}
		}

		for (VirtualBullet bullet : virtualBullets) {
			force = force.subtract(
					bulletForceFactor
							* (bullet.getX() - me.getX())
							/ Math.pow(
									bullet.getPosition().distance(me.getX(),
											me.getY()), 3),
					bulletForceFactor
							* (bullet.getY() - me.getY())
							/ Math.pow(
									bullet.getPosition().distance(me.getX(),
											me.getY()), 3));

			force = force.subtract(
					-bulletDir
							* perpendicularBulletForceFactor
							* (bullet.getY() - me.getY())
							/ Math.pow(
									bullet.getPosition().distance(me.getX(),
											me.getY()), 2),
					bulletDir
							* perpendicularBulletForceFactor
							* (bullet.getX() - me.getX())
							/ Math.pow(
									bullet.getPosition().distance(me.getX(),
											me.getY()), 2));
		}
		System.out.println();

		return force;
	}

	private double getCircularTargeting(double power) {
		double bulletSpeed = 20 - 3 * power;
		Point2D them = enemy.getPosition();
		Point2D us = new Point2D(me.getX(), me.getY());
		Motion usMotion = new Motion(me.getHeading(), me.getVelocity());
		int deltaTime = 0;

		while (deltaTime * bulletSpeed < them.distance(us)) {
			us = them.add(new Point2D(usMotion.getVelocity()
					* Math.sin(usMotion.getHeading()), usMotion.getVelocity()
					* Math.cos(usMotion.getHeading())));

			usMotion = enemySelf.getNextCircularMotion(usMotion);

			deltaTime++;
		}

		return Math.atan2(me.getX() - enemy.getX(), me.getY() - enemy.getY());
	}

	private double getLinearTargeting(double power) {
		double bulletSpeed = 20 - 3 * power;
		Point2D them = enemy.getPosition();
		Point2D us = new Point2D(me.getX(), me.getY());
		Motion usMotion = new Motion(me.getHeading(), me.getVelocity());
		int deltaTime = 0;

		while (deltaTime * bulletSpeed < them.distance(us)) {
			us = them.add(new Point2D(usMotion.getVelocity()
					* Math.sin(usMotion.getHeading()), usMotion.getVelocity()
					* Math.cos(usMotion.getHeading())));

			usMotion = enemySelf.getNextLinearMotion(usMotion);

			deltaTime++;
		}

		return Math.atan2(me.getX() - enemy.getX(), me.getY() - enemy.getY());
	}

	private double getHeadOnTargeting(double power) {
		return Math.atan2(me.getX() - enemy.getX(), me.getY() - enemy.getY());
	}

}
