package de.wolff.portfolioBCG.elements;

import java.util.Arrays;

import processing.core.PApplet;

public class Abscissa extends AbstractAxis {
	
	private int logBase = 5;

	public Abscissa(PApplet app, float xpos, float ypos, float length,
			float markerlength) {
		super(app, xpos, ypos, length, markerlength);
	}
	
	public void update(){
		float own = 1;
		float rival = logBase;
		int i = 0;
		while (rival > 0) {
			markers = Arrays.copyOf(markers, i + 1);
			markerTexts = Arrays.copyOf(markerTexts, i + 1);
			markers[i] = marketSharePosition(own, rival);
			markerTexts[i] = getFormated(own / rival);
			if (own < rival)
				own++;
			else
				rival--;
			i++;
		}
	}

	public void display() {
		app.fill(0);
		app.textSize(9);
		app.textAlign(PApplet.CENTER, PApplet.TOP);
		
		app.line(xpos, ypos, xpos + length, ypos);
		float markerHalf = markerlength / 2;
		for (int i = 0; i < markers.length ;i++){
			app.line(markers[i], ypos + markerHalf, markers[i], ypos - markerHalf);
			app.text(markerTexts[i], markers[i], ypos + markerlength);
		}
		
		app.textSize(16);
		app.text("Relativer Marktanteil (log" + logBase +")", xpos + length/2, ypos + 20);
	}

	public float marketSharePosition(float own, float rival) {
		float balance = length / 2;
		float logMultiplier = PApplet.log(own / rival) / PApplet.log(logBase);
		return xpos + balance - balance * logMultiplier;
	}
	
	public String getRelation(float xpos){
		float balance = length / 2;
		float difFactor = PApplet.pow(logBase, (xpos - this.xpos - balance) / - balance);
		float own, rival;
		if (xpos > this.xpos + balance){
			own = 1;
			rival = PApplet.pow(difFactor, -1);
		}  else if (xpos < this.xpos + balance){
			rival = 1;
			own = difFactor;
		} else {
			own = 1;
			rival = 1;
		}
		return getFormated(own) + " / " + getFormated(rival);
	}
	
	public void setLogBase(int logBase){
		this.logBase = logBase;
	}

}
