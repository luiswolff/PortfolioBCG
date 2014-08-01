package de.wolff.portfolioBCG.elements;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import processing.core.PApplet;

public abstract class AbstractAxis {
	
	private NumberFormat numbers = new DecimalFormat("#0.00");
	
	protected PApplet app;
	protected float xpos;
	protected float ypos;
	protected float length;
	protected float markerlength;
	
	protected float[] markers = new float[0];
	
	protected String[] markerTexts = new String[0];
	
	public AbstractAxis(PApplet app, float xpos, float ypos, float length, float markerlength) {
		super();
		this.app = app;
		this.xpos = xpos;
		this.ypos = ypos;
		this.length = length;
		this.markerlength = markerlength;
	}
	
	protected String getFormated(float number){
		return numbers.format(number).replace(".", ",");
	}
}
