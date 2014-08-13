package de.wolff.portfolioBCG.elements;

import processing.core.PApplet;

public class Abscissa extends AbstractAxis {
	
	private int logBase = 5;
	
	private String title;

	public Abscissa(PApplet app, float xpos, float ypos, float length,
			float markerlength, int markerCount, int logBase, String title) {
		super(app, xpos, ypos, length, markerlength, markerCount);
		this.logBase = logBase;
		this.title = title;
		
		makeMarkers();
	}
	
	private void makeMarkers(){
		markers = new float[markerCount];
		labels = new String[markerCount];
		
		int i = 0;
		int j = ((markerCount - 1) / 2);
		int k = 0;
		while (k < markerCount){
			float n = 1 + (logBase - 1) * i / ((markerCount - 1) / 2);
			float m = 1 + (logBase - 1) * j / ((markerCount - 1) / 2);
			markers[k] = xPosOf(n, m);
			labels[k] = getFormated(n / m);
			if ( i < j)
				i++;
			else
				j--;
			k++;
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
			app.text(labels[i], markers[i], ypos + markerlength);
		}
		
		app.textSize(16);
		app.text(String.format(title, logBase), xpos + length/2, ypos + 20);
	}

	public float xPosOf(float own, float rival) {
		float balance = length / 2;
		float logMultiplier = PApplet.log(own / rival) / PApplet.log(logBase);
		return xpos + balance - balance * logMultiplier;
	}
	
	public String relationOf(float xpos){
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

}
