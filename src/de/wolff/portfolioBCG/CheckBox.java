package de.wolff.portfolioBCG;

import processing.core.PApplet;
import processing.core.PImage;

class CheckBox {

	private PApplet app;
	private int size;
	private int xPos;
	private int yPos;
	private PImage hook;

	private boolean over;
	private boolean clicked = false;
	private boolean checked = false;

	CheckBox(PApplet app, int size, int xPos, int yPos, PImage hook) {
		this.app = app;
		this.size = size;
		this.xPos = xPos;
		this.yPos = yPos;
		this.hook = hook;
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

	void display() {
		if (over) {
			app.stroke(100);
			app.fill(150);
		} else {
			app.stroke(0);
			app.fill(50);
		}
		app.strokeWeight(10);
		app.rect(xPos, yPos, size, size);
		if (checked) {
			app.image(hook, xPos, yPos);
		}
	}
}
