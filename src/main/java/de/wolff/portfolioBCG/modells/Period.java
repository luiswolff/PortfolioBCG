package de.wolff.portfolioBCG.modells;

import java.util.ArrayList;
import java.util.List;

public class Period {
	
	private String name;
	
	private float companyTurnover = 0;
	
	private ArrayList<PeriodSBU> sbus = new ArrayList<>();
	
	public Period(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public List<PeriodSBU> getSBUs(){
		return sbus;
	}
	
	public void setCompanyTurnover(float companyTurnover){
		this.companyTurnover = companyTurnover;
	}
	
	public float getCompanyTurnover() {
		return companyTurnover;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Period other = (Period) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	@Override
	public String toString(){
		return name;
	}

}
