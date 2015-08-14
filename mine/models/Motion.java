package mine.models;

public class Motion {
	
	private double heading;
	private double velocity;

	public Motion(double heading, double velocity) {
		this.heading = heading;
		this.velocity = velocity;
	}
	
	public boolean equals(Motion other) {
		return other.getHeading() == heading && other.getVelocity() == velocity;
	}

	public double getHeading() {
		return heading;
	}

	public void setHeading(double heading) {
		this.heading = heading;
	}

	public double getVelocity() {
		return velocity;
	}

	public void setVelocity(double velocity) {
		this.velocity = velocity;
	}

}
