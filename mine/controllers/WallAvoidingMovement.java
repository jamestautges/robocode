package mine.controllers;

import javafx.geometry.Rectangle2D;
import javafx.geometry.Point2D;

import robocode.AdvancedRobot;
import robocode.util.Utils;

import mine.models.EnemyRobot;

public class WallAvoidingMovement {
	
	private AdvancedRobot me;
	private EnemyRobot enemy;
	
	private Rectangle2D battleField;
	
	private double heading = 0;
	private double ahead = Double.POSITIVE_INFINITY;

	public WallAvoidingMovement(AdvancedRobot me, EnemyRobot enemy, Rectangle2D battleField) {
		this.me = me;
		this.enemy = enemy;
		this.battleField = battleField;
	}
	
	public void update() {
		Point2D stick = new Point2D(120 * Math.sin(me.getHeading()), 120 * Math.sin(me.getHeading()));
		Point2D ball = new Point2D(me.getX(), me.getY()).add(stick);
		
		double newHeading = me.getHeadingRadians();
		
		if (ball.getY() > battleField.getMaxY()) {
			System.out.println("Hit upper wall");
			if (ball.getX() >= me.getX()) {
				newHeading = Math.acos((battleField.getMaxY() - ball.getY()) / 120);
			} else {
				newHeading = -Math.acos((battleField.getMaxY() - ball.getY()) / 120);
			}
		} else if (ball.getY() < battleField.getMinY()) {
			System.out.println("Hit bottom wall");
			if (ball.getX() >= me.getX()) {
				newHeading = -Math.acos((ball.getY() - battleField.getMinY()) / 120) + Math.PI;
			} else {
				newHeading = Math.acos((ball.getY() - battleField.getMinY()) / 120) + Math.PI;
			}
		} else if (ball.getX() < battleField.getMinX()) {
			System.out.println("Hit left wall");
			if (ball.getY() >= me.getY()) {
				newHeading = Math.acos((battleField.getMinX() - ball.getX()) / 120) - Math.PI / 2;
			} else {
				newHeading = -Math.acos((battleField.getMinX() - ball.getX()) / 120) - Math.PI / 2;
			}
		} else if (ball.getX() > battleField.getMaxX()) {
			System.out.println("Hit right wall");
			if (ball.getY() >= me.getY()) {
				newHeading = -Math.acos((ball.getX() - battleField.getMaxX()) / 120) + Math.PI / 2;
			} else {
				newHeading = Math.acos((ball.getX() - battleField.getMaxX()) / 120) + Math.PI / 2;
			}
		}
		
		heading = Utils.normalRelativeAngle(newHeading);
	}
	
	public void run() {
		me.setTurnRightRadians(heading - me.getHeadingRadians());
		me.setAhead(ahead);
	}

}
