/*******************************************************
 * Copyright (C) 2020-2021 jgret <thomgreimel@gmail.com>
 * 
 * This file is part of Jmpnrn.
 * 
 * Jmpnrn can not be copied and/or distributed without the express
 * permission of jgret
 *******************************************************/
package game.entity.projectile;

import java.awt.Shape;

import game.entity.Entity;
import game.entity.GameObject;
import game.graphics.Image2d;
import game.level.World;
import game.shape.Rectangle;
import game.shape.Vector2;

public abstract class Projectile extends GameObject{
	
	protected GameObject owner;
	
	public Projectile(Entity owner, Rectangle r, Image2d image, double vel, Vector2 dir) {
		super(owner.getWorldIn(), r, image);
		this.owner = owner;
		this.setVel(dir.unitvectl(vel));
	}
	
	@Override
	public void onCollision(GameObject o) {
		if (o instanceof Entity) {
			this.hitEntity((Entity) o);
		}
	}

	@Override
	public void onOutOfWorld(World world) {
		this.remove = true;
	}
	
	public abstract void hitEntity(Entity e);
	

}