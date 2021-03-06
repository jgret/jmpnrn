/*******************************************************
 * Copyright (C) 2020-2021 jgret <thomgreimel@gmail.com>
 * 
 * This file is part of Jmpnrn.
 * 
 * Jmpnrn can not be copied and/or distributed without the express
 * permission of jgret
 *******************************************************/
package game.graphics;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferStrategy;
import java.awt.image.VolatileImage;
import java.io.IOException;

import game.Engine;
import game.io.FileIO;

public class Screen extends Canvas implements WindowListener, ComponentListener {

	public static final Font FONT_SMALL = new Font("Monospaced Bold", Font.PLAIN, 10);
	public static final Font FONT_MEDIUM = new Font("Monospaced Bold", Font.PLAIN, 20);
	public static final Font FONT_LARGE = new Font("Monospaced Bold", Font.PLAIN, 30);
	
	public static final Font PIXEL_FONT_SMALL = createFont("font/pixel_font_2.ttf").deriveFont(15f);
	public static final Font PIXEL_FONT_MEDIUM = createFont("font/pixel_font_2.ttf").deriveFont(20f);
	public static final Font PIXEL_FONT_LARGE = createFont("font/pixel_font_2.ttf").deriveFont(40f);
	
	public static final int TILESIZE = 64;
	private static final long serialVersionUID = 1L;
	private Engine game;
	private Camera cam;
	private Frame frame;

	private BufferStrategy strategy;
	private VolatileImage vBuffer;

	public Screen(Engine game, int width, int height, int scale) {
		this.setPreferredSize(new Dimension(width, height));
		this.game = game;
		this.cam = new Camera(this, 0, 0, width, height);
		this.setFocusable(true);
		this.addKeyListener(game.getInput());
		frame = new Frame("GameScreen");
		frame.addWindowListener(this);
		frame.addComponentListener(this);
		frame.setIgnoreRepaint(true);
		frame.add(this);
		frame.pack();
		createBufferStrategy(2);
		strategy = getBufferStrategy();
		createBackbuffer();
		frame.setLocationRelativeTo(null);
	}
	
	public Engine getGame() {
		return game;
	}

	public void createBackbuffer() {
		this.vBuffer = this.createVolatileImage(getWidth(), getHeight());
	}

	public void checkVBuffer() {
		if (vBuffer.validate(getGraphicsConfiguration()) == VolatileImage.IMAGE_INCOMPATIBLE) {
			createBackbuffer();
		}
	}

	public void render() {
		checkVBuffer();
		cam.setDimensions(getWidth(), getHeight());
		
		Graphics2D g3 = (Graphics2D) vBuffer.getGraphics();
		g3.clearRect(0, 0, getWidth(), getHeight());
		game.render(g3, cam);
		g3.dispose();
		
		Graphics2D g2 = (Graphics2D) strategy.getDrawGraphics();
		g2.clearRect(0, 0, getWidth(), getHeight());
		g2.drawImage(vBuffer, 0, 0, getWidth(), getHeight(), null);
		g2.dispose();
		strategy.show();
	}
	
	public Camera getCam() {
		return cam;
	}
	
	public void enterFullscreen() {
		frame.setVisible(false);
		frame.dispose();
		frame.setUndecorated(true);
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		frame.setVisible(true);
	}

	public void leaveFullscreen() {
		frame.setVisible(false);
		frame.dispose();
		frame.setUndecorated(false);
		this.setPreferredSize(new Dimension(1280, 720));
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public boolean isFullscreen() {
		return frame.isUndecorated();
	}

	public void setTitle(String title) {
		frame.setTitle(title);
	}

	@Override
	public void componentResized(ComponentEvent e) {
		createBackbuffer();
	}

	@Override
	public void componentMoved(ComponentEvent e) {

	}

	@Override
	public void componentShown(ComponentEvent e) {

	}

	@Override
	public void componentHidden(ComponentEvent e) {

	}

	@Override
	public void windowOpened(WindowEvent e) {

	}

	@Override
	public void windowClosing(WindowEvent e) {
		System.exit(0);
	}

	@Override
	public void windowClosed(WindowEvent e) {

	}

	@Override
	public void windowIconified(WindowEvent e) {

	}

	@Override
	public void windowDeiconified(WindowEvent e) {

	}

	@Override
	public void windowActivated(WindowEvent e) {

	}

	@Override
	public void windowDeactivated(WindowEvent e) {

	}
	
	@Override
	public void setVisible(boolean b) {
		frame.setVisible(b);
	}
	
	public static Font createFont(String file) {
		try {
			Font font = Font.createFont(Font.TRUETYPE_FONT, FileIO.getResourceAsStream(file));
			GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
			return font;
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
