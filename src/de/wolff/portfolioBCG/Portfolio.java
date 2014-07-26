package de.wolff.portfolioBCG;

import processing.core.PApplet;
import processing.core.PImage;

public class Portfolio extends PApplet {

	private MarketData data = null;

	private SelectOneButtons sob;

	private CheckBox cb;

	private Scrollbar sb;

	public void setup() {
		loadData();
		frame.setTitle("Portfolio " + data.getCompany().getName());
		size(800, 600);

		textAlign(CENTER);

		// Initialize SelectOneButtons
		String[] detailgrad = { "Segment", "Marke" };
		sob = new SelectOneButtons(this, detailgrad, 200, 60, 580, 15, 80);

		// Initialize CheckBox
		PImage hook = loadImage("control/icon-check.png");
		cb = new CheckBox(this, 20, 580, 400, hook);

		// Initialize Scrollbar
		sb = new Scrollbar(this, 15, 545, 520, 20, 32, 16);
	}

	@Override
	public void draw() {
		sob.update();
		cb.update();
		sb.update();
		
		background(255);
		
		sob.display();
		textAlign(LEFT, CENTER);
		textSize(16);
		text("Zeige Vorperiode", 610, 410);
		
		cb.display();

		sb.display();

		stroke(0);
		strokeWeight(1);

		fill(250);
		rect(15, 15, 520, 520);

		fill(0);

		textSize(16);
		textAlign(CENTER, TOP);
		text("Relativer Marktanteil (log10)", 275, 500);
		
		textAlign(LEFT);
		text("Display " + sob.getActualButton().getText(), 300, 300);
		text("Zeige Vorperiode: " + (cb.isCheck() ? "Ja" : "Nein"), 300, 330 );
		text("Zeige Periode " + sb.getActualArea(), 300, 360);

		textAlign(CENTER, TOP);
		translate(20, 275);
		rotate(radians(270));
		text("Markwachstum", 0, 0);

	}
	
	private void loadData() {
		try {
			Class<?> data = getClass().getClassLoader().loadClass(args[0]);
			Object obj = data.newInstance();
			if (obj instanceof MarketData)
				this.data = (MarketData) obj;
			else {
				System.err.println("No valid Data");
				System.exit(0);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(0);
		} catch (InstantiationException e) {
			e.printStackTrace();
			System.exit(0);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

}
