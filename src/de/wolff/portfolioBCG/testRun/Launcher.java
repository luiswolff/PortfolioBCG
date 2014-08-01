package de.wolff.portfolioBCG.testRun;

import de.wolff.portfolioBCG.Portfolio;

public class Launcher {

	public static void main(String[] args) {
		Portfolio.analyseData(TestData.class.getName(), args);
	}
}
