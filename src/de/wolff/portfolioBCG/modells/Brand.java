package de.wolff.portfolioBCG.modells;

public class Brand {
	
	private Manufacture manufacture;
	
	private String name;
	
	private float turnover;

	public Brand(Manufacture manufacture, String name, float turnover) {
		super();
		this.manufacture = manufacture;
		this.name = name;
		this.turnover = turnover;
	}

	public Manufacture getManufacture() {
		return manufacture;
	}

	public String getName() {
		return name;
	}

	public float getTurnover() {
		return turnover;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((manufacture == null) ? 0 : manufacture.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + Float.floatToIntBits(turnover);
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
		Brand other = (Brand) obj;
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
		if (Float.floatToIntBits(turnover) != Float
				.floatToIntBits(other.turnover))
			return false;
		return true;
	}
}
