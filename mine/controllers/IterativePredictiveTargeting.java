package mine.controllers;

import javafx.geometry.Point2D;
import mine.models.EnemyRobot;
import mine.models.Motion;
import robocode.AdvancedRobot;
import robocode.util.Utils;

public class IterativePredictiveTargeting {

	private AdvancedRobot me;
	private EnemyRobot enemy;

	private long fireTime = 0;
	private double power = 2.0;

	private double turn;

	public IterativePredictiveTargeting(AdvancedRobot me, EnemyRobot enemy) {
		this.me = me;
		this.enemy = enemy;
	}

	public void update() {
		double bulletSpeed = 20 - 3 * power;
		Point2D them = enemy.getPosition();
		Point2D us = new Point2D(me.getX(), me.getY());
		Motion themMotion = new Motion(enemy.getHeading(), enemy.getVelocity());
		int deltaTime = 0;

		while (deltaTime * bulletSpeed < us.distance(them)) {
			them = them.add(new Point2D(themMotion.getVelocity()
					* Math.sin(themMotion.getHeading()), themMotion
					.getVelocity() * Math.cos(themMotion.getHeading())));

			themMotion = enemy.getNextMotion(themMotion);

			deltaTime++;
		}

		double bearing = Math.atan2(them.getX() - us.getX(),
				them.getY() - us.getY());
		turn = Utils.normalRelativeAngle(bearing - me.getGunHeadingRadians());
	}

	public void run() {
		if (me.getTime() == fireTime && me.getGunTurnRemaining() == 0) {
			me.setFire(power);
		}

		fireTime = me.getTime() + 1;

		me.setTurnGunRightRadians(turn);
	}

}
