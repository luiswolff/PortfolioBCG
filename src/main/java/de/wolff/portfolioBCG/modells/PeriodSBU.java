package de.wolff.portfolioBCG.modells;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PeriodSBU {

	private String name;

	private Manufacture ourCompany;
	
	private Manufacture rival = null;
	
	private ArrayList<PeriodBrand> ownBrands = new ArrayList<>();
	
	private HashMap<Manufacture, Float> rivalsTurnover = new HashMap<>();
	
	private HashMap<Manufacture, ArrayList<PeriodBrand>> rivalBrands = new HashMap<>();
	
	private float ownMarketShare = 0;

	private float marketGrowth = 0;

	public PeriodSBU(String name, Manufacture ourCompany) {
		super();
		this.name = name;
		this.ourCompany = ourCompany;
	}

	public String getName() {
		return name;
	}
	
	public void addBrand(PeriodBrand b){
		Manufacture m = b.getManufacture();
		if (ourCompany.equals(m)){
			ownMarketShare += b.getTurnover();
			ownBrands.add(b);
		} else {
			//Actualizes manufacture turnover
			float turnover = rivalsTurnover.containsKey(m) ? rivalsTurnover.get(m) : 0;
			turnover += b.getTurnover();
			rivalsTurnover.put(m, turnover);
			//Actualizes manufacture brands
			ArrayList<PeriodBrand> brands = rivalBrands.get(m);
			if (brands == null){
				brands = new ArrayList<>();
				rivalBrands.put(m, brands);
			}
			brands.add(b);
			//Actualizes biggest rival
			float biggestRival = rivalsTurnover.containsKey(rival) ? rivalsTurnover.get(rival) : 0;
			rival = (turnover > biggestRival) ? m : rival;
		}
	}

	public float getMarketGrowth() {
		return marketGrowth;
	}
	
	public void setMarketGrowth(float marketGrowth){
		this.marketGrowth =  marketGrowth;
	}

	public float getOwnMarketShare() {
		return ownMarketShare;
	}

	public float getRivalMarketShare() {
		return rivalsTurnover.get(rival);
	}
	
	public List<PeriodBrand> getOwnBrands(){
		return ownBrands;
	}
	
	public List<PeriodBrand> getRivalBrands(){
		return rivalBrands.get(rival);
	}
	
	public Manufacture getRival(){
		return rival;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((ourCompany == null) ? 0 : ourCompany.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PeriodSBU other = (PeriodSBU) obj;;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (ourCompany == null) {
			if (other.ourCompany != null)
				return false;
		} else if (!ourCompany.equals(other.ourCompany))
			return false;
		return true;
	}
	
	@Override
	public String toString(){
		return name;
	}
}
