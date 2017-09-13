package de.wolff.portfolioBCG.modells;

public class PeriodBrand{
	
	private String name;
	
	private Manufacture manufacture;
	
	private Float turnover;

	public PeriodBrand(String name, Manufacture manufacture, Float turnover) {
		super();
		this.name = name;
		this.manufacture = manufacture;
		this.turnover = turnover;
	}

	public String getName() {
		return name;
	}
	
	public Manufacture getManufacture(){
		return manufacture;
	}
	
	public Float getTurnover(){
		return turnover;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((manufacture == null) ? 0 : manufacture.hashCode());
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
		PeriodBrand other = (PeriodBrand) obj;
		if (manufacture == null) {
			if (other.manufacture != null)
				return false;
		} else if (!manufacture.equals(other.manufacture))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return name;
	}
	
	
}
