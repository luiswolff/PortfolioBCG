package de.wolff.portfolioBCG;

import java.lang.Float;

import processing.core.PApplet;

class Scrollbar {

	private final PApplet app;
	private Rail rail;
	private Slider slider;
	private boolean over;
	private float[] positions;
	private float[] borders;
	private Float lowerBorder;
	private Float upperBorder;
	private int actualArea;

	public Scrollbar(PApplet app, float xpos, float ypos, int swidth, int sheight,
			int loose, int positions) {
		this.app = app;
		
		rail = new Rail(swidth, sheight, xpos, ypos);
		float sposMax = xpos + swidth;
		slider = new Slider(sheight, sposMax, ypos, loose, xpos, sposMax);

		actualArea = positions;

		this.positions = new float[positions];
		for (int i = 0; i < positions; i = i + 1) {
			if (i == 0) {
				this.positions[i] = xpos;
			} else if (i == positions - 1) {
				this.positions[i] = sposMax;
			} else {
				this.positions[i] = xpos + swidth * i / (positions - 1);
			}
		}

		borders = new float[positions - 1];
		for (int i = 0; i + 1 < positions; i = i + 1) {
			float pos = this.positions[i];
			borders[i] = pos + (this.positions[i + 1] - pos) / 2;
		}

		lowerBorder = borders[positions - 2];
		upperBorder = null;

	}
	
	public boolean mouseOver() {
		return rail.mouseOver();
	}
	
	public void update(){
		if(mouseOver()){
			over = true;
		} else {
			over = false;
		}
		
		slider.update();
		
		if (upperBorder != null && slider.getPos() > upperBorder) {
			actualArea = actualArea + 1;
			lowerBorder = borders[actualArea - 2];
			if (actualArea <= borders.length)
				upperBorder = borders[actualArea - 1];
			else
				upperBorder = null;
		} else if (lowerBorder != null && slider.getPos() < lowerBorder) {
			actualArea = actualArea - 1;
			upperBorder = borders[actualArea - 1];
			if (actualArea > 1)
				lowerBorder = borders[actualArea - 2];
			else
				lowerBorder = null;
		}
	}
	
	public void display(){
		
		rail.display();
		
		for (float border : borders) {
			rail.displayLineAt(border);
		}
		slider.display();
	}
	
	public int getActualArea(){
		return actualArea;
	}
	
	class Slider{
		
		private float size;
		private float xpos, ypos, newPos;
		private float posMin, posMax;
		private int loose;
		private boolean locked = false;
		
		public Slider(float size, float xpos, float ypos, int loose, float posMin, float posMax) {
			super();
			this.size = size;
			this.xpos = xpos;
			this.ypos = ypos;
			this.posMin = posMin;
			this.posMax = posMax;
			this.newPos = xpos;
			this.loose = loose;
		}
		
		public void update(){
			if (app.mousePressed && over) {
				locked = true;
			}
			if (!app.mousePressed) {
				locked = false;
			}
			if (locked) {
				newPos = constrain(app.mouseX, posMin, posMax);
			} else {
				newPos = orient(newPos);
			}
			if (PApplet.abs(newPos - xpos) > 1) {
				xpos = xpos + (newPos - xpos) / loose;
			}
		}
		
		public void display(){
			app.noStroke();
			if (over || locked) {
				app.fill(0, 0, 0);
			} else {
				app.fill(102, 102, 102);
			}
			app.beginShape();
			app.vertex(xpos - size/2, ypos - 1);
			app.vertex(xpos + size/2, ypos - 1);
			app.vertex(xpos + size/2, ypos + size);
			app.vertex(xpos, ypos + size + 5);
			app.vertex(xpos - size/2, ypos + size);
			app.endShape(PApplet.CLOSE);
		}
		
		public float getPos(){
			return xpos;
		}
		
		private float constrain(float val, float minv, float maxv) {
			return PApplet.min(PApplet.max(val, minv), maxv);
		}

		private float orient(float cursor) {
			float newspos = Float.MAX_VALUE;
			for (float position : positions) {
				if (PApplet.abs(position - cursor) < PApplet.abs(newspos - cursor))
					newspos = position;
			}
			return newspos;
		}
		
	}
	
	class Rail {
		
		private float width, height;
		private float xpos, ypos;
		
		public Rail(float width, float height, float xpos, float ypos) {
			super();
			this.width = width;
			this.height = height;
			this.xpos = xpos;
			this.ypos = ypos;
		}
		
		public void display(){
			app.stroke(0);
			app.strokeWeight(1);
			app.fill(204);
			app.rect(xpos, ypos, width, height);
		}
		
		public void displayLineAt(float position){
			app.stroke(10);
			app.strokeWeight(1);
			app.line(position, ypos, position, ypos + height); 
		}
		
		public boolean mouseOver(){
			if (app.mouseX < xpos)
				return false;
			if (app.mouseX > xpos + width)
				return false;
			if(app.mouseY < ypos)
				return false;
			if(app.mouseY > ypos + height)
				return false;
			return true;
		}
		
	}
}
