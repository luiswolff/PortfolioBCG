package de.wolff.portfolioBCG;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertiesFile extends Properties {
	
	public PropertiesFile(File file) throws FileNotFoundException, IOException{
		this(new FileInputStream(file));
	}
	
	public PropertiesFile(String file) throws FileNotFoundException, IOException{
		this(new FileInputStream(file));
	}
	
	public PropertiesFile(FileInputStream fis) throws IOException{
		this(new BufferedInputStream(fis));
	}
	
	public PropertiesFile(BufferedInputStream bis) throws IOException{
		load(bis);
		bis.close();
	}

}
