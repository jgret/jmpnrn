/*******************************************************
 * Copyright (C) 2020-2021 jgret <thomgreimel@gmail.com>
 * 
 * This file is part of Jmpnrn.
 * 
 * Jmpnrn can not be copied and/or distributed without the express
 * permission of jgret
 *******************************************************/
package game.entity;

import java.awt.Graphics2D;
import java.awt.Shape;

import game.Game;
import game.graphics.Camera;
import game.graphics.Image2d;
import game.graphics.Screen;
import game.level.World;
import game.shape.Rectangle;
import game.shape.Vector2;

public abstract class GameObject extends Rectangle {

	public static final double GRAVITY = 12;
	public static final double FRICTION = 10;
	public static final double SLOPE_POINT_OFFSET = 1 / (double) Screen.TILESIZE;
	
	protected Game game = Game.instance;
	protected World worldIn;
	protected Image2d image;
	protected Vector2 vel;
	protected int priority;
	protected double friction;
	protected double gravity;
	protected boolean remove;
	protected boolean slopeCollision;
	protected boolean boxCollision;
	protected boolean staticCollision;
	protected boolean solid;
	private boolean grounded;

	public GameObject(World worldIn, Rectangle r, Image2d image) {
		super(r);
		this.game = Game.instance;
		this.worldIn = worldIn;
		this.image = image;
		this.vel = new Vector2(0, 0);
		this.gravity = GRAVITY;
		this.friction = FRICTION;
		this.boxCollision = true;
		this.slopeCollision = true;
		this.staticCollision = true;
	}

	@Override
	public void draw(Graphics2D g2, Camera cam) {
		image.draw(g2, getX() * Screen.TILESIZE - cam.getPixelOffsetX(), getY() * Screen.TILESIZE - cam.getPixelOffsetY(), getWidth() * Screen.TILESIZE, getHeight() * Screen.TILESIZE);
	}

	public void accelerate(Vector2 acc) {
		this.vel = vel.add(acc);
	}
	
	public void move(double elapsedTime) {
		this.addPosition(vel.mul(elapsedTime));
	}
	
	public void applyGravity(double elapsedTime) {
		this.vel = this.vel.addY(gravity * elapsedTime);
	}
	
	public void applyFriction(double elapsedTime) {
		if (this.vel.getX() > 0) {
			this.vel = this.vel.addX(-friction * elapsedTime);
			if (this.vel.getX() < 0) {
				this.vel.setX(0);
			}
		}
		if (this.vel.getX() < 0) {
			this.vel = this.vel.addX(friction * elapsedTime);
			if (this.vel.getX() > 0) {
				this.vel.setX(0);
			}
		}
		
	}
	
	public void update(double elapsedTime) {

	}
	
	public boolean isGrounded() {
		return grounded;
	}
	
	public abstract boolean shouldCollide(GameObject g);
	public abstract void onStaticCollision(Shape s);
	public abstract void onCollision(GameObject o);
	public abstract void onOutOfWorld(World world);
	
	public Vector2 getSlopePoint() {
		return new Vector2(this.getX() + this.getWidth() / 2, this.getBot() - SLOPE_POINT_OFFSET);
	}
	
	public void setSlopePoint(Vector2 v) {
		this.setPosition(v.getX() - this.getWidth() / 2, v.getY() + this.getBot() + SLOPE_POINT_OFFSET);
	}
	
	public Rectangle getGroundeCheckBox() {
		return new Rectangle(this.getLeft(), this.getBot(), this.getWidth(), 0.1 / Screen.TILESIZE);
	}
	
	public Vector2 getTopCollisionPoint() {
		return new Vector2(this.getX() + this.getWidth() / 2, this.getY());
	}
	
	public World getWorldIn() {
		return worldIn;
	}
	
	public void setWorldIn(World worldIn) {
		this.worldIn = worldIn;
	}
	
	public int getPriority() {
		return this.priority;
	}

	public Vector2 getVel() {
		return vel;
	}

	public void setVel(Vector2 vel) {
		this.vel = vel;
	}
	
	public void setVel(double x, double y) {
		this.vel.x = x;
		this.vel.y = y;
	}
	
	public void setVelX(double x) {
		this.vel.x = x;
	}
	
	public void setVelY(double y) {
		this.vel.y = y;
	}
	
	public double getVelX() {
		return this.vel.x;
	}
	
	public double getVelY() {
		return this.vel.y;
	}
	
	public Game getGame() {
		return game;
	}

	public Image2d getImage() {
		return image;
	}

	public boolean isRemove() {
		return remove;
	}
	
	public void setRemove(boolean remove) {
		this.remove = remove;
	}

	public boolean isSlopeCollision() {
		return slopeCollision;
	}

	public void setSlopeCollision(boolean slopeCollision) {
		this.slopeCollision = slopeCollision;
	}

	public boolean isBoxCollision() {
		return boxCollision;
	}

	public void setBoxCollision(boolean boxCollision) {
		this.boxCollision = boxCollision;
	}

	public double getFriction() {
		return friction;
	}

	public void setFriction(double friction) {
		this.friction = friction;
	}

	public double getGravity() {
		return gravity;
	}

	public void setGravity(double gravity) {
		this.gravity = gravity;
	}

	public boolean isSolid() {
		return solid;
	}

	public void setSolid(boolean solid) {
		this.solid = solid;
	}

	public boolean isStaticCollision() {
		return staticCollision;
	}

	public void setStaticCollision(boolean staticCollision) {
		this.staticCollision = staticCollision;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public void setImage(Image2d image) {
		this.image = image;
	}

	public void setGrounded(boolean grounded) {
		this.grounded = grounded;
	}
	
}
