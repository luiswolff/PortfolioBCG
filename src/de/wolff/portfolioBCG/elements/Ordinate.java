package de.wolff.portfolioBCG.elements;

import processing.core.PApplet;

public class Ordinate extends AbstractAxis {

	private float target = 0;

	private float range = 5;
	
	private String title;

	public Ordinate(PApplet app, float xpos, float ypos, float length,
			float markerlength, int markerCount, float target, float range, String title) {
		super(app, xpos, ypos, length, markerlength, markerCount);
		this.target = target;
		this.range = range;
		this.title = title;
		
		makeMarkers();
	}
	
	private void makeMarkers(){
		markers = new float[markerCount];
		labels = new String[markerCount];
		
		int n = (markerCount - 1) / 2;
		for (int i = 0; i < markerCount; i++) {
			float growth = target + range * (n - i) / n;
			markers[i] = yPosOf(growth);
			labels[i] = getFormated(growth);
		}
	}

	public void display() {
		app.fill(0);
		app.textSize(9);
		app.textAlign(PApplet.RIGHT, PApplet.CENTER);
		app.line(xpos, ypos, xpos, ypos + length);

		for (int i = 0; i < markers.length; i++) {
			float markerHalf = markerlength / 2;
			app.line(xpos + markerHalf, markers[i], xpos - markerHalf,
					markers[i]);
			app.text(labels[i], xpos - markerlength, markers[i]);
		}
		app.textSize(16);
		app.textAlign(PApplet.CENTER, PApplet.TOP);
		
		float xtranslate = xpos - 50;
		float ytranslate = ypos + length/2;
		app.translate(xtranslate, ytranslate);
		app.rotate(PApplet.radians(270));
		app.text(String.format(title, target - range, target + range), 0, 0);
		
		app.rotate(PApplet.radians(-270));
		app.translate(-xtranslate,-ytranslate);
	}

	public float yPosOf(float growth) {
		float balance = length / 2;
		float distance = (target - growth) * balance / range;
		return ypos + balance + distance;
	}
	
	public String growthOf(float ypos){
		float balance = length / 2;
		float dif = range/balance*(ypos - this.ypos -balance);
		return getFormated(target - dif);
	}
}
