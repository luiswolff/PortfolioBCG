package de.wolff.portfolioBCG.elements;

import processing.core.PApplet;

public class SelectOneButtons {

	private final PApplet app;
	private Button[] buttons;
	private Button actualButton;

	public SelectOneButtons(PApplet app, String[] texts, float bWidth, float bHeigth,
			float bXPos, float bYPos, float distance) {
		this.app = app;

		int count = texts.length;
		buttons = new Button[count];
		for (int i = 0; i < count; i++) {
			buttons[i] = new Button(texts[i], bWidth, bHeigth, bXPos, bYPos + i
					* distance);
		}
		actualButton = buttons[0];
	}
	
	public void update(){
		for (Button b : buttons){
			b.update();
		}
	}

	public void display() {
		for (Button b : buttons) {
			b.display();
		}
	}

	public Button getActualButton(){
		return actualButton;
	}

	public class Button {

		private String text;
		
		private float width;
		private float height;
		private float xPos;
		private float yPos;
		private boolean over;

		public Button(String text, float width, float height, float xPos,
				float yPos) {
			this.text = text;
			this.width = width;
			this.height = height;
			this.xPos = xPos;
			this.yPos = yPos;
		}

		public boolean mouseOver() {
			if (app.mouseX < xPos)
				return false;
			if (app.mouseX > xPos + width)
				return false;
			if (app.mouseY < yPos)
				return false;
			if (app.mouseY > yPos + height)
				return false;
			return true;
		}

		public void update() {
			over = mouseOver();
			if (app.mouseButton == PApplet.LEFT && over) {
				actualButton = this;
			}
		}

		void display() {
			app.stroke(0);
			app.strokeWeight(1);
			if (over) {
				app.fill(240);
			} else if (actualButton.equals(this)) {
				app.fill(200);
			} else {
				app.fill(150);
			}
			app.rect(xPos, yPos, width, height);
			app.textAlign(PApplet.CENTER, PApplet.CENTER);
			app.fill(0);
			app.text(text, xPos + width / 2, yPos + height / 2);
		}
		
		public String getText(){
			return text;
		}
	}
}
