/*******************************************************
 * Copyright (C) 2020-2021 jgret <thomgreimel@gmail.com>
 * 
 * This file is part of Jmpnrn.
 * 
 * Jmpnrn can not be copied and/or distributed without the express
 * permission of jgret
 *******************************************************/
package main;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.awt.image.VolatileImage;

public class Image2d {

	private VolatileImage img;
	private BufferedImage buffer;

	public Image2d (int width, int height) {
		this.img = createVolatileImage(width, height, Transparency.OPAQUE);
		this.buffer = img.getSnapshot();
		
	}

	public Image2d(BufferedImage img) {
		this.img = toVolatileImage(img);
		this.buffer = img;
	}
	
	public Graphics2D createGraphics() {
		System.out.println(img.validate(gc()) == VolatileImage.IMAGE_OK);
		Graphics2D g2 =  (Graphics2D) img.getGraphics();
		return g2;
	}
	
	public boolean backup() {
		if (img.validate(gc()) != VolatileImage.IMAGE_OK) {
			return false; 
		}
		
		this.buffer = img.getSnapshot();
		return true;
	}
	
	public void draw(Graphics2D g2, double x, double y) {
		draw(g2, x, y, img.getWidth(), img.getHeight());
	}

	public void draw(Graphics2D g2, double x, double y, double width, double height) {
		do {

			if (img.validate(gc()) != VolatileImage.IMAGE_OK) {
				restore();
			}

			g2.drawImage(img, (int) x, (int) y, (int) width, (int) height, null);
		} while (img.contentsLost());
	}

	public void draw(Graphics2D g2, double sx, double sy, double sw, double sh, double dx, double dy, double dw, double dh) {
		do {

			if (img.validate(gc()) != VolatileImage.IMAGE_OK) {
				restore();
			}

			g2.drawImage(img, (int) dx, (int) dy, (int) (dx + dw), (int) (dx + dh), (int) sx, (int) sy, (int) (sx + sw), (int) (sy + sh), null);
		} while (img.contentsLost());
	}

	public VolatileImage getImage() {
		return this.img;
	}
	
	public BufferedImage getBackup() {
		return this.buffer;
	}

	public void restore() {
		System.out.println("restore");
		img = toVolatileImage(toCompatibleImage(buffer));
	}

	//-------------------------------------------------------------------------------------------------------
	//                                         Static Functions
	//-------------------------------------------------------------------------------------------------------

	private static GameScreen screen;

	public static void makeContext(GameScreen screen) {
		Image2d.screen = screen;
	}

	public static VolatileImage createVolatileImage(int width, int height, int transparency) {
		VolatileImage image;
		do {
			image = gc().createCompatibleVolatileImage(width, height, transparency);
		} while (image.validate(gc()) == VolatileImage.IMAGE_INCOMPATIBLE);
		return image;
	}

	public static VolatileImage toVolatileImage(BufferedImage img) {
		VolatileImage vBuffer = createVolatileImage(img.getWidth(), img.getHeight(), Transparency.TRANSLUCENT);
		do {
			vBuffer.validate(gc());
			Graphics2D g2 = vBuffer.createGraphics();
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
			g2.setColor(Color.BLACK);
			g2.clearRect(0, 0, vBuffer.getWidth(), vBuffer.getHeight());
			g2.drawImage(img, 0, 0, null);
			g2.dispose();
		} while (vBuffer.contentsLost());

		return vBuffer;

	}

	public static BufferedImage toCompatibleImage(BufferedImage image) {
		BufferedImage newImage = gc().createCompatibleImage(image.getWidth(), image.getHeight(), image.getTransparency());
		Graphics2D g2d = newImage.createGraphics();
		g2d.drawImage(image, 0, 0, null);
		g2d.dispose();
		return newImage;
	}

	public static GraphicsConfiguration gc() {
		if (screen == null) {
			return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
		} else {
			return screen.getGraphicsConfiguration();
		}
	}

}
