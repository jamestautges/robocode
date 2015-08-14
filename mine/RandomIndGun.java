package mine;

import java.util.Random;
import robocode.AdvancedRobot;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;
import static robocode.util.Utils.normalRelativeAngleDegrees;

public class RandomIndGun extends AdvancedRobot {
	private Random random = new Random();
	
	private int direction = 1;
	private double turn = 2;
	
	private String trackName;
	private int count = 0;
	
	//double[][] seen = {{0, 0}, {0, 0}};
	
	private final int waitTime = 15;
	
	public void run() {
		trackName = null;
		turnGunRight(90);
		
		while(true) {
			if(trackName != null) {
				count++;
			}
			out.println(count);
			
			if(count > waitTime) {
				setTurnGunRight(90 + getGunBearing());
				trackName = null;
			}
			
			setTurnRight(10);
			execute();
		}
	}
	
	public void onScannedRobot(ScannedRobotEvent e) {
		if (trackName == null) { // If we don't have a target, well, now we do!
			count = 0;
			trackName = e.getName();
			out.println("Tracking " + trackName);
			
			setTurnGunRight(e.getBearing() - getGunBearing());
		} else if (e.getName().equals(trackName)) {
			/*seen[0] = seen[1];
			seen[1] = getRelativePos(e.getDistance(), e.getBearing());*/
			// This is our target. Reset count (see the run method)
			count = 0;

			if (getGunHeat() == 0) {
				setFire(3);
			}

			if (getTime() % 20 == 0) {
				turn = random.nextDouble()*6 - 3;
			}
			
			setTurnRight(turn);
			setTurnGunRight(normalRelativeAngleDegrees(e.getBearing() - getGunBearing()));

			setAhead(5 * direction);
		} else if (count > waitTime) {
			setTurnGunRight(normalRelativeAngleDegrees(90 + getGunBearing()));
			trackName = null;
			count = 0;
		} else {
			count++;
		}
		
		execute();
		out.println("Distance: " + e.getDistance());
		out.println("Heading of robot: " + e.getHeading());
		out.println("Time: " + getTime());
		out.println("Tracking: " + trackName);
		out.println("Count: " + count);
		out.println();
		scan();
	}
	
	public void onHitWall(HitWallEvent e) {
		direction = -direction;
	}
	
	public void onHitRobot(HitRobotEvent e) {
		direction = -direction;
	}
	
	public void onRobotDeath(RobotDeathEvent e) {
		if (trackName != null && e.getName().equals(trackName)) {
			trackName = null;
			count = 0;
		}
	}
	
	private double getGunBearing() {
		return getGunHeading() - getHeading();
	}
	
	private double[] getRelativePos(double distance, double angle) {
		double[] pos = new double[2];
		double headingAngle = angle + getHeading();
		
		pos[0] = distance * Math.cos(headingAngle) + getX();
		pos[1] = distance * Math.sin(headingAngle) + getY();
		
		return pos;
	}
}
