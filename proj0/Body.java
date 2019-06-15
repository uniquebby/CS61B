/** On behalf of the heavenly bodies 
 * @author yangbinbin
 */
public class Body {
	/**
	 * the gravitational constant.
	 */
	public double G = 6.67E-11;

	/** 
	 * current x position of body.
	 */
	public double xxPos;
	/** 
	 * current y position of body.
	 */
	public double yyPos;
	/** 
	 * current x velocity of body.
	 */
	public double xxVel;
	/**
	 * current y velocity of body.
	 */
	public double yyVel;
	/**
	 * mass of body.
	 */
	public double mass;
	/**
	 * the name of the file that corresponds the image that depicts the body.
	 */
	public String imgFileName;

	/**
	 * constructor that take in 6 parameters.
	 */
	public Body(double xP, double yP, double xV,
					double yV, double m, String img) {
		this.xxPos = xP;
		this.yyPos = yP;
		this.xxVel = xV;
		this.yyVel = yV;
		this.mass = m;
		this.imgFileName = img;
	}

	/**
	 * constructor that take in a Body object.
	 */
	public Body(Body b) {
		this.xxPos = b.xxPos;
		this.yyPos = b.yyPos;
		this.xxVel = b.xxVel;
		this.yyVel = b.yyVel;
		this.mass = b.mass;
		this.imgFileName = b.imgFileName;
	}

	/**
	 * calculates the distance between two Bodys.
	 */
	public double calcDistance(Body b) {
		double dx = b.xxPos - this.xxPos;	
		double dy = b.yyPos - this.yyPos;	
		return Math.sqrt(dx * dx + dy * dy);
	}

	/**
	 * calculates the force exerted on this body by the given body.
	 */
	public double calcForceExertedBy(Body b) {
		double r = this.calcDistance(b);
		return G * b.mass * this.mass / (r * r);
	}

	/**
	 * calculates the net X force exerted by all bodies in given body array.
	 */
	public double calcNetForceExertedByX(Body[] allBodys) {
		double allForceByX = 0;
		int i = 0;
		double r = 0;
		while (i < allBodys.length) {
			if (this.equals(allBodys[i])) {
				++i;
				continue;
			}
			r = this.calcDistance(allBodys[i]);
			allForceByX += this.calcForceExertedBy(allBodys[i]) *  
								(allBodys[i].xxPos - this.xxPos) / r;
			++i;
		}
		return allForceByX;
	}

	/**
	 * calculates the net Y force exerted by all bodies in given body array.
	 */
	public double calcNetForceExertedByY(Body[] allBodys) {
		double allForceByY = 0;
		int i = 0;
		double r = 0;
		while (i < allBodys.length) {
			if (this.equals(allBodys[i])) {
				++i;
				continue;
			}
			r = this.calcDistance(allBodys[i]);
			allForceByY += this.calcForceExertedBy(allBodys[i]) *  
								(allBodys[i].yyPos - this.yyPos) / r;
			++i;
		}
		return allForceByY;
	}

	/**
	 * update the body's velocity and position in a small period dt.
	 */
	public void update(double dt, double allForceByX, double allForceByY) {
		double accelerationByX = allForceByX / this.mass;
		double accelerationByY = allForceByY / this.mass;
		this.xxVel += dt * accelerationByX;
		this.yyVel += dt * accelerationByY;
		this.xxPos += dt * xxVel;
		this.yyPos += dt * yyVel;
	}

	/**
	 * draw the body itself at its appropriate position.
	 */
	public void draw() {
		String imgToDraw = "images/" + this.imgFileName;
		StdDraw.picture(this.xxPos, this.yyPos, imgToDraw);
	}

}

















