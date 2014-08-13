package de.wolff.portfolioBCG;

import java.util.List;

import de.wolff.portfolioBCG.modells.Manufacture;
import de.wolff.portfolioBCG.modells.Period;

/**
 * Market data to be analyzed with an portfolio.
 * 
 * @author Luis
 *
 */
public interface MarketData {
	
	/**
	 * All Periods, which can be analyzed
	 * 
	 * @return List of period to analyze.
	 */
	public List<Period> getPeriods();
	
	/**
	 * Number of Periods to analyze.
	 * 
	 * @return Number of Periods to analyze
	 */
	public int getPeriodsCount();
	
	/**
	 * The company, which has to be analyzed.
	 * 
	 * @return The company, which has to be analyzed.
	 */
	public Manufacture getCompany();
	
	/**
	 * Target growth, which says, whether or not be invested in business units.
	 * 
	 * @return the target growth.
	 */
	public float getTargetGrowth();

}
