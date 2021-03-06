/*******************************************************
 * Copyright (C) 2020-2021 jgret <thomgreimel@gmail.com>
 * 
 * This file is part of Jmpnrn.
 * 
 * Jmpnrn can not be copied and/or distributed without the express
 * permission of jgret
 *******************************************************/
package game.shape;

import java.awt.geom.Point2D;

/**
 * The Vector Class: used for movement, force
 */

public class Vector2 extends Point2D.Double {

	public static final Vector2 UP = new Vector2(0, -1);
	public static final Vector2 DOWN = new Vector2(0, 1);
	public static final Vector2 LEFT = new Vector2(-1, 0);
	public static final Vector2 RIGHT = new Vector2(1, 0);
	
	public static final Vector2 X = new Vector2(1, 0);
	public static final Vector2 Y = new Vector2(0, 1);
	

	public Vector2() {
		this.x = 0.0;
		this.y = 0.0;
	}

	public Vector2(Vector2 v1) {
		this.x = v1.x;
		this.y = v1.y;
		
	}

	/**
	 * Creates new Vector
	 * 
	 * @param x coordinate
	 * @param y coordinate
	 */

	public Vector2(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Creates new Vector from <b>v1</B> to <b>v2</b> <br>
	 * <br>
	 * AB = B - A
	 * 
	 * @param v1
	 * @param v2
	 */

	public Vector2(Vector2 v1, Vector2 v2) {
		this.x = v2.x - v1.x;
		this.y = v2.y - v1.y;
	}

	/**
	 * length of the Vector
	 * 
	 * @return the length of the Vector
	 */

	public double length() {
		return Math.sqrt(x * x + y * y);
	}
	
	
	/**
	 * distance to a point
	 * 
	 * @param point
	 * @return the distance to the point
	 */
	
	public double distance(Vector2 point) {
		double dx = this.x - point.x;
		double dy = this.y - point.y;
		
		return Math.sqrt(dx * dx + dy * dy);
	}
	

	/**
	 * unit Vector
	 * 
	 * @return unit Vector
	 */

	public Vector2 unitvect() {

		Vector2 vector = new Vector2(this.x, this.y);
		double length = vector.length();
		if (length > 0) {
			vector.x /= length;
			vector.y /= length;
		}

		return vector;

	}

	/**
	 * Creates a unit Vector with the lenght given
	 * 
	 * @param length of the new Vector
	 * @return the unit Vector with the length
	 */

	public Vector2 unitvectl(double length) {

		Vector2 vector = new Vector2(this.x, this.y);
		vector = vector.unitvect();
		vector.x *= length;
		vector.y *= length;

		return vector;
	}

	/**
	 * Rotates the Vector by 180�
	 * 
	 * @return the new Vector
	 */

	public Vector2 negate() {

		return new Vector2(-this.x, -this.y);
	}

	/**
	 * negates the x value
	 * 
	 * @return the new Vector
	 */

	public Vector2 negateX() {

		return new Vector2(-this.x, this.y);
	}

	/**
	 * negates the y value
	 * 
	 * @return the new Vector
	 */

	public Vector2 negateY() {

		return new Vector2(this.x, -this.y);

	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	/**
	 * Sets the Vector to x and y
	 * 
	 * @param x
	 * @param y
	 */

	public void set(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void setV(Vector2 vec) {
		this.x = vec.x;
		this.y = vec.y;
	}

	/**
	 * Multiplies x and y by n
	 * 
	 * @param n
	 * @return the new Vector
	 */

	public Vector2 mul(double n) {
		return new Vector2(this.x * n, this.y * n);
	}

	/**
	 * Multiplies <b>this.x * v.x</b> and <b>this.y * v.y</b>
	 * 
	 * @param v
	 * @return the new Vector
	 */

	public Vector2 mul(Vector2 v) {
		return new Vector2(this.x * v.x, this.y * v.y);
	}

	/**
	 * Subtracts n from x and y
	 * 
	 * @param n
	 * @return the new Vector
	 */

	public Vector2 sub(double n) {
		return new Vector2(this.x - n, this.y - n);
	}

	/**
	 * Subtracts <b>this.x - v.x</b> and <b>this.y - v.y</b>
	 * 
	 * @param v
	 * @return the new Vector
	 */

	public Vector2 sub(Vector2 v) {
		return new Vector2(this.x - v.x, this.y - v.y);
	}

	/**
	 * Divides x and y by n
	 * 
	 * @param n
	 * @return the new Vector
	 */

	public Vector2 div(double n) {
		return new Vector2(this.x / n, this.y / n);
	}

	/**
	 * Adds x to this.x
	 * 
	 * @param x
	 * @return the new Vector
	 */

	public Vector2 addX(double x) {
		return new Vector2(this.x + x, this.y);
	}

	/**
	 * Adds y to this.y
	 * 
	 * @param y
	 * @return the new Vector
	 */

	public Vector2 addY(double y) {
		return new Vector2(this.x, this.y + y);
	}

	/**
	 * Adds the Vector to the Vector
	 * 
	 * @param v
	 * @return the new Vector 
	 */

	public Vector2 add(Vector2 v) {
		return new Vector2(this.x + v.x, this.y + v.y);
	}

	/**
	 * Tests if the Vectors are the same
	 * 
	 * @param a
	 * @return this.x == a.x && this.y == a.y;
	 */

	@Override
	public boolean equals(Object o) {
		if (o instanceof Vector2) {
			Vector2 v = (Vector2) o;
			return this.x == v.x && this.y == v.y;
		}
		return false;
	}
	
	public double scalar(Vector2 b) {
		return this.x * b.x + this.y * b.y;
	}
	
	public boolean isZero() {
		return x == 0 && y == 0;
	}
	
	public boolean isInfinit() {
		return java.lang.Double.isInfinite(x) || java.lang.Double.isInfinite(y);
	}
	
	public boolean isNaN() {
		return java.lang.Double.isNaN(x) || java.lang.Double.isNaN(y);
	}	
	
	public boolean isFinite() {
		return !isZero() && !isInfinit() && !isNaN();
	}

	public double angle(Vector2 v) {
		return Math.acos(this.scalar(v) / (this.length() * v.length()));
	}
	
	public Vector2 swap() {
		return new Vector2(y, x);
	}
	
	public double angleX() {
		
		double phi = Math.atan2(y, x);
		
		if (phi < 0) {
			phi += Math.PI * 2;
		}

		return phi;
	}
	
	public String toString() {
		return String.format("%.4g | %.4g", x, y);
	}

	public Vector2 clone() {
		return new Vector2(this);
	}
	
	public void printExact() {
		System.out.println(x + " | " + y);
	}
	
	public void round(int digits) {
		int d = (int) Math.pow(10, digits);
		this.x = (double) ((int)(x * d)) / d;
		this.y = (double) ((int)(y * d)) / d;
	}

}
