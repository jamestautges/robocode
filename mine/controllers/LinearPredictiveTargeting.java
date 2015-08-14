package mine.controllers;

import javafx.geometry.Point2D;

import mine.models.EnemyRobot;
import robocode.AdvancedRobot;
import robocode.util.Utils;

public class LinearPredictiveTargeting {
	
	private AdvancedRobot me;
	private EnemyRobot enemy;

	private long fireTime = 0;
	private double power = 2.0;
	
	private double turn;
	private boolean fire = true;

	public LinearPredictiveTargeting(AdvancedRobot me, EnemyRobot enemy) {
		this.me = me;
		this.enemy = enemy;
	}

	public void update() {
		double bulletSpeed = 20 - 3*power;
		Point2D them = enemy.getPosition();
		Point2D us = new Point2D(me.getX(), me.getY());
		Point2D deltaThem = new Point2D(enemy.getVelocity() * Math.sin(enemy.getHeading()), 
				enemy.getVelocity() * Math.cos(enemy.getHeading()));
		int deltaTime = 0;
		
		fire = true;
		while(deltaTime * bulletSpeed < us.distance(them)) {
			them = them.add(deltaThem);
			
			// if the robot will hit a wall on its current path, don't fire
			if(them.getX() > me.getBattleFieldWidth() || them.getX() < 0 || them.getY() > me.getBattleFieldHeight() || them.getY() < 0) {
				fire = false;
				break;
			}
			deltaTime++;
		}
		
		double bearing = Math.atan2(them.getX() - us.getX(), them.getY() - us.getY());
		turn = Utils.normalRelativeAngle(bearing - me.getGunHeadingRadians());
	}

	public void run() {
		if (me.getTime() == fireTime && me.getGunTurnRemaining() == 0) {
			me.setFire(power);
		}
		
		if (fire) {
			fireTime = me.getTime() + 1;
		} else {
			fireTime = me.getTime() - 1;
		}

		me.setTurnGunRightRadians(turn);
	}

}
