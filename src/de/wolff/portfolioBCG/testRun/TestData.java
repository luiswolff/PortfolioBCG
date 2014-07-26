package de.wolff.portfolioBCG.testRun;

import java.util.List;

import de.wolff.portfolioBCG.MarketData;
import de.wolff.portfolioBCG.modells.Manufacture;
import de.wolff.portfolioBCG.modells.Period;

public class TestData implements MarketData {

	@Override
	public List<Period> getPeriods() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Manufacture getCompany() {
		return new Manufacture("Daimler AG", 300000);
	}

	@Override
	public float getTargetGrowth() {
		return 0;
	}
}
