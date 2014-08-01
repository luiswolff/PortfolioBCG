package de.wolff.portfolioBCG.elements;

import java.util.Arrays;

import processing.core.PApplet;

public class Ordinate extends AbstractAxis {

	private float targetGrowth = 0;

	private float markerRange = 5;

	public Ordinate(PApplet app, float xpos, float ypos, float length,
			float markerlength) {
		super(app, xpos, ypos, length, markerlength);
	}
	
	public void update(){
		for (int i = 0; i < 20; i++) {
			markers = Arrays.copyOf(markers, i + 1);
			markerTexts = Arrays.copyOf(markerTexts, i + 1);
			float marketGrowth = targetGrowth + markerRange * (10 - i) / 10;
			markers[i] = marketGrowthPosition(marketGrowth);
			markerTexts[i] = getFormated(marketGrowth);
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
			app.text(markerTexts[i], xpos - markerlength, markers[i]);
		}
		app.textSize(16);
		app.textAlign(PApplet.CENTER, PApplet.TOP);
		
		float xtranslate = xpos - 50;
		float ytranslate = ypos + length/2;
		app.translate(xtranslate, ytranslate);
		app.rotate(PApplet.radians(270));
		app.text("Markwachstum", 0, 0);
		
		app.rotate(PApplet.radians(-270));
		app.translate(-xtranslate,-ytranslate);
	}

	public float marketGrowthPosition(float marketGrowth) {
		float balance = length / 2;
		float distance = (targetGrowth - marketGrowth) * balance / markerRange;
		return ypos + balance + distance;
	}
	
	public String getMarketGrowth(float ypos){
		float balance = length / 2;
		float dif = markerRange/balance*(ypos - this.ypos -balance);
		return getFormated(targetGrowth - dif);
	}
	
	public void setMiddle(float targetGrowth){
		this.targetGrowth = targetGrowth;
	}
	
	public void setRange(float markerRange){
		this.markerRange = markerRange;
	}
}
