package mine.models;

import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;

import javafx.geometry.Point2D;

public class EnemyRobot {

	private double bearing;
	private double absoluteBearing;
	private double distance;
	private double energy;
	private double heading;
	private String name;
	private double velocity;
	private boolean isSentry;

	private Point2D position;

	private double deltaHeading;
	private double deltaVelocity;
	private double deltaEnergy;

	private AdvancedRobot me;

	public EnemyRobot(AdvancedRobot r) {
		setBearing(0);
		setAbsoluteBearing(0);
		setDistance(0);
		setEnergy(0);
		setHeading(0);
		setName(null);
		setVelocity(0);
		setSentry(false);
		position = new Point2D(0, 0);

		deltaHeading = 0;
		deltaVelocity = 0;

		me = r;
	}

	public void update(ScannedRobotEvent e) {
		this.bearing = e.getBearingRadians();
		this.absoluteBearing = bearing + me.getHeadingRadians();
		this.distance = e.getDistance();
		setEnergy(e.getEnergy());
		setHeading(e.getHeadingRadians());
		this.name = e.getName();
		setVelocity(e.getVelocity());
		this.isSentry = e.isSentryRobot();

		position = new Point2D(
				me.getX() + distance * Math.sin(absoluteBearing), me.getY()
						+ distance * Math.cos(absoluteBearing));
	}

	public double getBearing() {
		return bearing;
	}

	public void setBearing(double bearing) {
		this.bearing = bearing;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public double getEnergy() {
		return energy;
	}

	public void setEnergy(double energy) {
		deltaEnergy = energy - this.energy;
		this.energy = energy;
	}

	public double getHeading() {
		return heading;
	}

	public void setHeading(double heading) {
		deltaHeading = heading - this.heading;
		this.heading = heading;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getVelocity() {
		return velocity;
	}

	public void setVelocity(double velocity) {
		deltaVelocity = velocity - this.velocity;
		this.velocity = velocity;
	}

	public boolean isSentry() {
		return isSentry;
	}

	public void setSentry(boolean isSentry) {
		this.isSentry = isSentry;
	}

	public double getAbsoluteBearing() {
		return absoluteBearing;
	}

	public void setAbsoluteBearing(double absoluteBearing) {
		this.absoluteBearing = absoluteBearing;
	}

	public double getX() {
		return position.getX();
	}

	public double getY() {
		return position.getY();
	}

	public Point2D getPosition() {
		return position;
	}
	
	public void setPosition(Point2D position) {
		this.position = position;
	}

	public double getDeltaHeading() {
		return deltaHeading;
	}

	public double getDeltaVelocity() {
		return deltaVelocity;
	}
	
	public double getDeltaEnergy() {
		return deltaEnergy;
	}

	public Motion getNextCircularMotion(Motion previous) {
		// circular tracking
		return new Motion(previous.getHeading() + deltaHeading,
				previous.getVelocity());
	}
	
	public Motion getNextLinearMotion(Motion previous) {
		// linear tracking
		return new Motion(previous.getHeading(), previous.getVelocity());
	}

}
