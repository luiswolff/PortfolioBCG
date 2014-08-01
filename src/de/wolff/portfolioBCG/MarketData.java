package de.wolff.portfolioBCG;

import java.util.List;

import de.wolff.portfolioBCG.modells.Manufacture;
import de.wolff.portfolioBCG.modells.Period;

public interface MarketData {
	
	public List<Period> getPeriods();
	
	public int getPeriodsCount();
	
	public Manufacture getCompany();
	
	public float getTargetGrowth();

}
