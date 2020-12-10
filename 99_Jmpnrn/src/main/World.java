/*******************************************************
 * Copyright (C) 2020-2021 jgret <thomgreimel@gmail.com>
 * 
 * This file is part of Jmpnrn.
 * 
 * Jmpnrn can not be copied and/or distributed without the express
 * permission of jgret
 *******************************************************/
package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class World implements Drawable {

	private Game game;
	private ArrayList<Rectangle> staticRects;
	private ArrayList<GameObject> actors;
	private Image2d worldImageBuffer;
	private Tileset tileset;
	private int tilesize;
	private int width;
	private int height;

	public World(Game game) {
		this.game = game;
		this.staticRects = new ArrayList<>();
		this.actors = new ArrayList<>();
	}

	public void load(String filename) {

		int[][] background;
		int[][] forground;

		try {
			File file = new File("res/world/" + filename + ".tmx");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);
			doc.getDocumentElement().normalize();

			Element map = doc.getDocumentElement();

			tilesize = Integer.parseInt(map.getAttribute("tilewidth"));
			width = Integer.parseInt(map.getAttribute("width"));
			height = Integer.parseInt(map.getAttribute("height"));

			background = new int[width][height];
			forground = new int[width][height];

			//read tilesets
			NodeList tilesets = doc.getElementsByTagName("tileset");
			for (int i = 0; i < tilesets.getLength(); i++) {
				Element tileset = (Element) tilesets.item(i);
				Element src = (Element) tileset.getElementsByTagName("image").item(0);
				String stringsrc = src.getAttribute("source");
				String[] parts = stringsrc.split("/");
				String fname = parts[parts.length - 1];
				this.tileset = new Tileset(fname, 16);
			}

			//read layers
			NodeList layers = doc.getElementsByTagName("layer");
			for (int i = 0; i < layers.getLength(); i++) {
				Element layer = (Element) layers.item(i);
				String name = layer.getAttribute("name");
				Element data = (Element) layer.getElementsByTagName("data").item(0);

				switch (name) {

				case ("background"): {
					background = toIntArray(data.getTextContent().trim());
					break;
				}

				case ("forground"): {
					forground = toIntArray(data.getTextContent().trim());
					break;
				}

				}

			}

			NodeList objectgroups = doc.getElementsByTagName("objectgroup");
			for (int i = 0; i < objectgroups.getLength(); i++) {
				Element objgrp = (Element) objectgroups.item(i);
				NodeList objs = objgrp.getElementsByTagName("object");
				for (int j = 0; j < objs.getLength(); j++) {
					Element obj = (Element) objs.item(j);
					double rx = Double.parseDouble(obj.getAttribute("x"));
					double ry = Double.parseDouble(obj.getAttribute("y"));
					double rwidth = Double.parseDouble(obj.getAttribute("width"));
					double rheight = Double.parseDouble(obj.getAttribute("height"));
					
					rx = (rx / (double) tilesize);
					ry = (ry / (double) tilesize);
					rwidth = (rwidth / (double) tilesize);
					rheight = (rheight / (double) tilesize);
					
					Rectangle rect = new Rectangle(rx, ry, rwidth, rheight);
					staticRects.add(rect);
				}
			}

			this.worldImageBuffer = new Image2d(width * tilesize,  height * tilesize);
			Graphics2D g2 = this.worldImageBuffer.createGraphics();

			for (int row = 0; row < height; row++) {
				for (int coll = 0; coll < width; coll++) {

					int n = background[row][coll];
					if (n > 0) {
						n--;
						g2.drawImage(tileset.get(n).getImage(), coll * tilesize, row * tilesize, tilesize, tilesize, null);
					}

					n = forground[row][coll];
					if (n > 0) {
						n--;
						g2.drawImage(tileset.get(n).getImage(), coll * tilesize, row * tilesize, tilesize, tilesize, null);
					}

				}
			
			}
			
			g2.dispose();
			
			worldImageBuffer.backup();
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public Rectangle getBounds() {
		return new Rectangle(0, 0, width, height);
	}

	public void spawn(GameObject g, Vector2 pos) {
		g.setPos(pos);
		this.actors.add(g);
	}
	
	public void init() {
		
	}
	
	public void update(double elapsedTime) {

		ArrayList<GameObject> trash = new ArrayList<GameObject>();
		
		for (GameObject g : actors) {
			if (!g.isRemove()) {
				g.update(elapsedTime);
			} else {
				trash.add(g);
			}
		}
		
		for (GameObject g : trash) {
			actors.remove(g);
		}
		
		for (GameObject gactor : actors) {
			
			for (GameObject gopponent : actors) {
				
				if (gactor == gopponent) {
					continue;
				}

				if (gactor.intersects(gopponent)) {
					
					GameObject master, slave;
					
					//set the master based on priority
					//the master first tries to push the opponent around
					if (gactor.getPriority() > gopponent.getPriority()) {
						master = gactor;
						slave = gopponent;
					} else {
						master = gopponent;
						slave = gactor;
					}
					
					Vector2 prevSlavePos = slave.getPos();
					
					int result = slave.pushout(master);
					
					Rectangle rect;
					if ((rect = collidesWithSomething(slave)) != null) {

						GameObject temp = master;
						master = slave;
						slave = temp;
						
						int dir = slave.pushout(master);
						
						if (dir == GameObject.PUSHOUT_DOWN) {
							slave.vel.y = 0;
						} else 
						
						if (dir == GameObject.PUSHOUT_UP) {
							slave.vel.y = 0;
						} else
						
						if (dir == GameObject.PUSHOUT_RIGHT || dir == GameObject.PUSHOUT_LEFT) {
							slave.vel.x = 0;
						}
						
					}
					
				}
			
			}
			
			for (Rectangle rect : staticRects) {
				
				if (gactor.intersects(rect)) {
					
					int dir = gactor.pushout(rect);
					
					if (dir == GameObject.PUSHOUT_DOWN || dir == GameObject.PUSHOUT_UP) {
						gactor.vel.y = 0;
					}
					
					if (dir == GameObject.PUSHOUT_RIGHT || dir == GameObject.PUSHOUT_LEFT) {
						gactor.vel.x = 0;
					}
					
					if (dir != GameObject.PUSHOUT_DOWN) {
						gactor.grounded = true;
					}
					
				}
				
				
			}
    		
    	}
		
	}
	
	public Rectangle collidesWithSomething(GameObject actor) {
		
		for (GameObject gopponent : actors) {

			if (actor == gopponent) {
				continue;
			}
			
			if (actor.intersects(gopponent)) {
				return gopponent;
			}
			
		}
		
		for (Rectangle r : staticRects) {
			
			if (actor.intersects(r)) {
				return r;
			}
			
		}
		
		return null;
	}
	
	@Override
	public void draw(Graphics2D g2, Camera cam, int scale) {
		
		double wscale = (double) scale / (double) tilesize;
		
		System.out.println(wscale + " " + scale + "/" + tilesize);
		
		worldImageBuffer.draw(g2, cam.getX(scale / wscale), cam.getY(scale / wscale), cam.getWidth(), cam.getHeight(), 0, 0, cam.getWidth() * wscale, cam.getHeight() * wscale);

		g2.setColor(Color.GREEN);
		
		for (Rectangle r : staticRects) {
			r.draw(g2, cam, scale);
		}
		
		for (GameObject g : actors) {
			g.draw(g2, cam, scale);
		}
	
	}

	private int[][] toIntArray(String sdata) {
		int[][] data;
		int width, height;

		String[] rows = sdata.split("\n");
		height = rows.length;
		width = rows[0].split(",").length;
		data = new int[height][width];

		for (int i = 0; i < rows.length; i++) {
			String[] row_values = rows[i].split(",");
			for (int j = 0; j < row_values.length; j++) {
				data[i][j] = Integer.parseInt(row_values[j]);
			}
		}

		return data;
	}
	
	public Game getGame() {
		return this.game;
	}

}