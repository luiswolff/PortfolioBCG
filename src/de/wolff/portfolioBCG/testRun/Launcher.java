package de.wolff.portfolioBCG.testRun;

import processing.core.PApplet;
import de.wolff.portfolioBCG.Portfolio;

public class Launcher {

	public static void main(String[] args) {
		PApplet.main(Portfolio.class.getName(), new String[]{TestData.class.getName()});
	}

}
