package de.wolff.portfolioBCG.modells;

import java.util.Iterator;
import java.util.LinkedList;

public class Period {
	
	private String name;
	
	private LinkedList<SBU> sbus = new LinkedList<>();
	
	public Period(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public Iterator<SBU> getSBUs(){
		return sbus.iterator();
	}
	
	public void addSBU(SBU sbu){
		sbus.add(sbu);
	}

}
