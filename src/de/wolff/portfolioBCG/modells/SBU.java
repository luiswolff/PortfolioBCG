package de.wolff.portfolioBCG.modells;

import java.util.HashMap;
import java.util.List;

public class SBU {
	
	private String name;
	
	private float ownMarketShare;
	
	private float rivalMarketShare;
	
	private float marketGrowth;

	public SBU(String name, float marketGrowth, Manufacture own, List<Brand> allBrands) {
		super();
		this.name = name;
		this.marketGrowth = marketGrowth;
		
		HashMap<Manufacture, Float> rivalsTurnover = new HashMap<>();
		
		for (Brand brand : allBrands){
			Manufacture m = brand.getManufacture();
			if (own.equals(m)){
				ownMarketShare = ownMarketShare + brand.getTurnover();
			} else if (rivalsTurnover.containsKey(m)){
				float knownTurnover = rivalsTurnover.get(m);
				rivalsTurnover.put(m, knownTurnover + brand.getTurnover());
			} else {
				rivalsTurnover.put(m, brand.getTurnover());
			}
		}
		
		rivalMarketShare = 0;
		for (float rivalTurnover : rivalsTurnover.values()){
			rivalMarketShare = Math.max(rivalMarketShare, rivalTurnover);
		}
	}

	public String getName() {
		return name;
	}

	public float getOwnMarketShare() {
		return ownMarketShare;
	}

	public float getRivalMarketShare() {
		return rivalMarketShare;
	}

	public float getMarketGrowth() {
		return marketGrowth;
	}
}
