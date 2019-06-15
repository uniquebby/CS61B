/**
 * a class to run the simulation.
 * @author yangbinbin
 */
public class NBody{
	/**
	 * retrun the radius of universe in file.
	 */
	public static double readRadius(String file) {
		In in = new In(file);
		in.readInt();
		double radius = in.readDouble();
		return radius;
	}	

	/**
	 * return an array of Bodys corresponding to the bodies in the file given.
	 */
	public static Body[] readBodies(String file) {
		In in = new In(file);
		int numOfBody = in.readInt();
		in.readDouble();
		Body[] bodys = new Body[numOfBody];
		for (int i = 0; i < numOfBody; ++i) {
			double xxPos = in.readDouble();
			double yyPos = in.readDouble();
			double xxVel = in.readDouble();
			double yyVel = in.readDouble();
			double mass = in.readDouble();
			String imgFileName = in.readString();
			bodys[i] = new Body(xxPos, yyPos, xxVel, yyVel, mass, imgFileName);
		}
		return bodys;
	}

	public static void main(String[] args) {
		/* collecting all needed input */
		double T = Double.valueOf(args[0]);
		double dt = Double.valueOf(args[1]);
		String filename = args[2];
		double radius = readRadius(filename);
		Body[] bodys = readBodies(filename);

		/* Drawing the background */
		StdDraw.enableDoubleBuffering();
		StdDraw.setScale(-radius, radius);
		StdDraw.clear();
		StdDraw.picture(0, 0, "images/starfield.jpg");

		/* Draw bodys */
		for (int i = 0; i < bodys.length; ++i) {
			bodys[i].draw();
		}
		StdDraw.show();
		StdDraw.pause(1000);

		/* creating an animation */
		StdDraw.enableDoubleBuffering();
		double time = 0.0;
		double[] xForces = new double[bodys.length];
		double[] yForces = new double[bodys.length];
		while (time < T) {
			/* calculate the net x and y forces of each body. */
			for (int i = 0; i < bodys.length; ++i) {
				xForces[i] = bodys[i].calcNetForceExertedByX(bodys);
				yForces[i] = bodys[i].calcNetForceExertedByY(bodys);
			}
			/* update each body. */
			for (int j = 0; j < bodys.length; ++j) {
				bodys[j].update(dt, xForces[j], yForces[j]);
			}
			/* draw the background image. */
			StdDraw.picture(0, 0, "images/starfield.jpg");
			/* draw all of the bodys */
			for (int i = 0; i < bodys.length; ++i) {
				bodys[i].draw();
			}
			StdDraw.show();
			StdDraw.pause(10);
			/* increase time */
			time += dt;
		}
	}

}


















