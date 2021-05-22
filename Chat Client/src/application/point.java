package application;

import java.io.Serializable;

public class point implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double x;
	private double y;
	
	point(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	double getX() {
		return x;
	}
	
	double getY() {
		return y;
	}
	
}
