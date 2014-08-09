package de.wolff.portfolioBCG.elements;

import processing.core.PApplet;
import processing.core.PImage;

public class CheckBox {

	private PApplet app;
	private int size;
	private int xPos;
	private int yPos;
	private PImage hook;

	private boolean over;
	private boolean clicked = false;
	private boolean checked = false;
	
	private String title;

	public CheckBox(PApplet app, int xPos, int yPos, int size, PImage hook, String title) {
		this.app = app;
		this.size = size;
		this.xPos = xPos;
		this.yPos = yPos;
		this.hook = hook;
		this.title = title;
	}

	public boolean mouseOver() {
		if (app.mouseX < xPos)
			return false;
		if (app.mouseX > xPos + size)
			return false;
		if (app.mouseY < yPos)
			return false;
		if (app.mouseY > yPos + size)
			return false;
		return true;
	}

	public void update() {
		over = mouseOver();
		if (clicked && app.mouseButton == 0) {
			clicked = false;
		} else if (!clicked && app.mouseButton == PApplet.LEFT && over) {
			checked = checked ? false : true;
			clicked = true;
		}
	}

	public boolean isCheck() {
		return checked;
	}

	public void display() {
		if (over) {
			app.stroke(100);
			app.fill(150);
		} else {
			app.stroke(0);
			app.fill(50);
		}
		app.strokeWeight(10);
		app.rect(xPos, yPos, size, size);
		app.imageMode(PApplet.CORNER);
		if (checked) {
			app.image(hook, xPos, yPos);
		}
		
		app.textAlign(PApplet.LEFT, PApplet.CENTER);
		app.textSize(16);
		app.text(String.format(title, checked), xPos + 30, yPos + 10);
	}
}
