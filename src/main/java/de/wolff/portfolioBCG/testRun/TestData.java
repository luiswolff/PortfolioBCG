package de.wolff.portfolioBCG.testRun;

import java.util.ArrayList;
import java.util.List;

import de.wolff.portfolioBCG.MarketData;
import de.wolff.portfolioBCG.modells.Manufacture;
import de.wolff.portfolioBCG.modells.Period;
import de.wolff.portfolioBCG.modells.PeriodBrand;
import de.wolff.portfolioBCG.modells.PeriodSBU;

public class TestData implements MarketData {

	private String[] datas = {
			"Mercedes C-Klasse ; Mittelklasse ; 5 ; Daimler AG ; 10000 ; 2013",
			"Mercedes CLA-Klasse ; Mittelklasse ; 4 ; Daimler AG ; 15000 ; 2012",
			"Mercedes CLA-Klasse ; Mittelklasse ; 5; Daimler AG  ;16000 ; 2013",
			"BMW 3ER ; Mittelklasse ; 5 ; BMW Group ; 33000 ; 2013",
			"VW Passat ; Mittelklasse ; 5 ; Volkswagen Gruppe ; 9000 ; 2013",
			"Mercedes C-Klasse ; Mittelklasse ; 4 ; Daimler AG ; 20000 ; 2012",
			"BMW 3ER ; Mittelklasse ; 4 ; BMW Group ; 8000 ; 2012",
			"VW Passat ; Mittelklasse ; 4 ; Volkswagen Gruppe ; 9500 ; 2012",
			"Smart FORTWO ; Minis ; 8 ; Daimler AG ;  7000 ; 2013",
			"VW UP ; Minis ; 10 ; Volkswagen Gruppe ; 5000 ; 2013",
			"Mercedes A-Klasse; Kompaktklasse ; 3 ; Daimler AG; 6000 ; 2013",
			"Mini ; Kompaktklasse ; 3 ; BMW Group; 8000 ; 2013",
			"Mercedes E-Klasse ; Obere Mittelklasse ; 9 ; Daimler AG ; 30000 ; 2013",
			"BMW 5ER ; Obere Mittelklasse ; 9; BMW Group; 13000; 2013",
			"Smart FORTOW ; Minis ; 7 ; Daimler AG ; 6110 ; 2012",
			"VW UP ; Minis ; 7 ; Volkswagen Gruppe ; 3950 ; 2012",
			"Mercedes G-Klasse ; Geländewagen ; 2 ; Daimler AG ; 30000 ; 2012",
			"BMW X5 ; Geländewagen ; 2 ; BMW Group ; 28000 ; 2012",
			"Mercedes R-Klasse ; SUV ; 4 ; Daimler AG ; 25000; 2012",
			"BMW X1 ; SUV ; 4 ; BMW Group ; 27000 ; 2012",
			"Mercedes CLA-Klasse ; Mittelklasse ; 5; Daimler AG  ;16000 ; 2011",
			"BMW 3ER ; Mittelklasse ; 5 ; BMW Group ; 33000 ; 2011" };

	//private String[] segmentLevel1 = { "Mittelklasse : Passenger Car",
	//		"Minis : Passenger Car", "Kompaktklasse : Passenger Car",
	//		"Obere Mittelklasse : Passenger Car", "Geländewagen : Off-Roader",
	//		"SUV : Off-Roader" };
	
	//private HashMap<String, String> segmentChanger = new HashMap<>();

	private List<Period> periods = new ArrayList<>(4);

	private Manufacture we = new Manufacture("Daimler AG");

	private float target = 6;

	public TestData() {
		/*
		for (String segment : segmentLevel1){
			String[] elements = segment.split(":");
			segmentChanger.put(elements[0].trim(), elements[1].trim());
		}
		 */
		for (String data : datas) {

			String[] elements = data.split(";");
			String brand = elements[0].trim();
			String businessUnit = elements[1].trim();
			float growth = Float.parseFloat(elements[2]);
			String manufacture = elements[3].trim();
			float turnover = Float.parseFloat(elements[4]);
			String period = elements[5].trim();

			Period p = getEntry(periods, new Period(period));

			Manufacture m = new Manufacture(manufacture);
			
			//businessUnit = segmentChanger.get(businessUnit);

			PeriodSBU sbu = new PeriodSBU(businessUnit, we);
			sbu = getEntry(p.getSBUs(), sbu);
			sbu.setMarketGrowth(growth);

			PeriodBrand b = new PeriodBrand(brand, m, turnover);

			if (we.equals(m))
				p.setCompanyTurnover(p.getCompanyTurnover() + b.getTurnover());
			sbu.addBrand(b);
		}
	}

	private <T extends Object> T getEntry(List<T> list, T obj) {
		int index = list.indexOf(obj);
		if (index != -1) {
			return list.get(index);
		} else {
			list.add(obj);
			return obj;
		}
	}

	@Override
	public List<Period> getPeriods() {
		return periods;
	}

	@Override
	public int getPeriodsCount() {
		return periods.size();
	}

	@Override
	public Manufacture getCompany() {
		return we;
	}

	@Override
	public float getTargetGrowth() {
		return target;
	}

	public static void main(String[] args) {
		TestData testData = new TestData();
		for (Period p : testData.periods) {
			System.out.println("------------------");
			System.out.println("------------------");
			System.out.println(p.getName());
			for (PeriodSBU s : p.getSBUs()) {
				System.out.println("------------------");
				System.out.println(s.getName());
				System.out.println("Marktwachstum: " + s.getMarketGrowth());
				System.out.println("Unser Umsatz: " + s.getOwnMarketShare());
				System.out.println("Umsatz von stärkern Rivalen: "
						+ s.getRivalMarketShare());
			}
		}
	}
}
