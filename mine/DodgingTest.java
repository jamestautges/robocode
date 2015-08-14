package mine;

import robocode.AdvancedRobot;
import robocode.HitWallEvent;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

import mine.controllers.AntiGravMovement;
import mine.controllers.IterativePredictiveTargeting;
import mine.models.EnemyRobot;

public class DodgingTest extends AdvancedRobot {

	private final double scanFactor = 2.0;

	private EnemyRobot enemy = new EnemyRobot(this);

	private AntiGravMovement movementSystem;
	private IterativePredictiveTargeting targetingSystem;

	public void run() {
		movementSystem = new AntiGravMovement(this, enemy);
		targetingSystem = new IterativePredictiveTargeting(this, enemy);

		setAdjustRadarForGunTurn(true);
		setAdjustRadarForRobotTurn(true);
		setAdjustGunForRobotTurn(true);
		turnRadarRightRadians(Double.POSITIVE_INFINITY);

		while (true) {
			scan();
			turnRadarRightRadians(Double.POSITIVE_INFINITY);
		}
	}

	public void onScannedRobot(ScannedRobotEvent e) {
		enemy.update(e);

		// tracking
		turnMultiplierLock();

		// movement
		movementSystem.update();
		movementSystem.run();

		// targeting section
		targetingSystem.update();
		targetingSystem.run();

		execute();
	}

	public void onHitWall(HitWallEvent e) {
		out.println("Hit Wall");
	}

	private void turnMultiplierLock() {
		// tracking section
		double radarTurn = getHeadingRadians() + enemy.getBearing()
				- getRadarHeadingRadians();
		setTurnRadarRightRadians(scanFactor
				* Utils.normalRelativeAngle(radarTurn));
	}

}
