package de.wolff.portfolioBCG.modells;

public class Manufacture {
	
	private String name;
	
	private float turnover;
	
	public Manufacture(String name, float turnover) {
		super();
		this.name = name;
		this.turnover = turnover;
	}

	public String getName(){
		return name;
	}
	
	public float getTurnover(){
		return turnover;
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
		Manufacture other = (Manufacture) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
