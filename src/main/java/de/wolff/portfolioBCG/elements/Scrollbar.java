package de.wolff.portfolioBCG.elements;

import processing.core.PApplet;

public class Scrollbar {

	private final PApplet app;
	private Rail rail;
	private Slider slider;
	private boolean over;
	private float[] positions = new float[0];
	private float[] borders = new float[0];
	private Float lowerBorder;
	private Float upperBorder;
	private int actualArea;
	private String[] labels = new String[0];

	public Scrollbar(PApplet app, float xpos, float ypos, int swidth,
			int sheight, int loose, Object[] labels) {
		this.app = app;
		rail = new Rail(swidth, sheight, xpos, ypos);
		slider = new Slider(loose);
		int length = labels.length;
		this.labels = new String[length];
		for (int i = 0; i < length; i++){
			this.labels[i] = labels[i].toString();
		}
		setPositions(length);
	}

	public boolean mouseOver() {
		return rail.mouseOver();
	}

	public void update() {
		over = mouseOver();
		slider.update();

		if (upperBorder != null && slider.xpos > upperBorder) {
			actualArea = actualArea + 1;
			lowerBorder = borders[actualArea - 2];
			if (actualArea <= borders.length)
				upperBorder = borders[actualArea - 1];
			else
				upperBorder = null;
		} else if (lowerBorder != null && slider.xpos < lowerBorder) {
			actualArea = actualArea - 1;
			upperBorder = borders[actualArea - 1];
			if (actualArea > 1)
				lowerBorder = borders[actualArea - 2];
			else
				lowerBorder = null;
		}
	}

	public void display() {

		rail.display();

		for (float border : borders) {
			rail.displayLineAt(border);
		}
		app.fill(0);
		app.textAlign(PApplet.CENTER, PApplet.TOP);
		app.textSize(12);
		for (int i = 0; i < labels.length; i++) {
			float pos = positions[i];
			app.text(labels[i], pos, rail.ypos + rail.height + 3);
		}
		slider.display();
	}

	public int getActualArea() {
		return actualArea;
	}

	public float getSliderPos() {
		return slider.xpos;
	}

	private void setPositions(int posCount) {
		actualArea = posCount;
		
		positions = new float[posCount];
		for (int i = 0; i < posCount; i = i + 1) {
			positions[i] = rail.xpos
					+ (rail.width / posCount * i + rail.width / posCount
							* (i + 1)) / 2;
		}
		setBorders(positions);		
		slider.actualizesPos();
	}
	
	private void setBorders(float[] positions){
		int posCount = positions.length;
		if (posCount <= 1){
			borders = new float[0];
			lowerBorder = null;
			upperBorder = null;
		} else {
			borders = new float[posCount - 1];
			for (int i = 0; i + 1 < posCount; i = i + 1) {
				float pos = positions[i];
				borders[i] = pos + (positions[i + 1] - pos) / 2;
			}
			lowerBorder = borders[posCount - 2];
			upperBorder = null;
		}
	}

	public class Slider {

		private float size = rail.height;
		private float ypos = rail.ypos;
		private float posMin = rail.xpos;
		private float posMax = rail.xpos + rail.width;
		private float xpos = posMax;
		private float newPos = xpos;
		private int loose = 32;
		private boolean locked = false;
		
		public Slider(int loose){
			this.loose = loose;
		}

		public void update() {
			if (app.mousePressed && over) {
				locked = true;
			}
			if (!app.mousePressed) {
				locked = false;
			}
			if (locked) {
				newPos = constrain();
			} else {
				newPos = orient();
			}
			if (PApplet.abs(newPos - xpos) > 1) {
				xpos = xpos + (newPos - xpos) / loose;
			}
		}

		public void display() {
			app.noStroke();
			if (over || locked) {
				app.fill(0, 0, 0);
			} else {
				app.fill(102, 102, 102);
			}
			app.beginShape();
			app.vertex(xpos - size / 2, ypos - 1);
			app.vertex(xpos + size / 2, ypos - 1);
			app.vertex(xpos + size / 2, ypos + size);
			app.vertex(xpos, ypos + size + 5);
			app.vertex(xpos - size / 2, ypos + size);
			app.endShape(PApplet.CLOSE);
		}

		private void actualizesPos() {
			int posCount = positions.length;
			slider.posMin = posCount > 1 ? positions[0] : rail.xpos;
			slider.posMax = posCount > 1 ? positions[posCount - 1] : rail.xpos
					+ rail.width;
			slider.xpos = slider.posMax;
			slider.newPos = slider.posMax;
		}

		private float constrain() {
			return PApplet.min(PApplet.max(app.mouseX, posMin), posMax);
		}

		private float orient() {
			if (positions.length == 0) {
				return constrain();
			}
			float newspos = Float.MAX_VALUE;
			for (float position : positions) {
				float distPos = PApplet.abs(position - this.newPos);
				float distNewPos = PApplet.abs(newspos - this.newPos);
				if (distPos < distNewPos)
					newspos = position;
			}
			return newspos;
		}
	}

	public class Rail {

		private float width, height;
		private float xpos, ypos;

		public Rail(float width, float height, float xpos, float ypos) {
			super();
			this.width = width;
			this.height = height;
			this.xpos = xpos;
			this.ypos = ypos;
		}

		public void display() {
			app.stroke((over ? 50 : 0));
			app.strokeWeight((over ? 2 : 1));
			app.fill((over ? 204 : 244));
			app.rect(xpos, ypos, width, height);
		}

		public void displayLineAt(float position) {
			app.stroke(200);
			app.strokeWeight(1);
			app.line(position, ypos, position, ypos + height);
		}

		public boolean mouseOver() {
			if (app.mouseX < xpos)
				return false;
			if (app.mouseX > xpos + width)
				return false;
			if (app.mouseY < ypos)
				return false;
			if (app.mouseY > ypos + height)
				return false;
			return true;
		}

	}
}
