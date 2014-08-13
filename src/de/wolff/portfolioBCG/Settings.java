package de.wolff.portfolioBCG;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Class, which holds the configuration properties of the application
 * 
 * @author Luis
 * 
 */
public class Settings {

	/**
	 * Properties holding the configuration.
	 */
	private Properties probs = new Properties();

	/**
	 * Creates Settings with an File leading to the properties file.
	 * 
	 * @param file
	 *            File to properties
	 * @throws FileNotFoundException
	 * @throws IOException
	 * 
	 * @see Settings#Settings(FileInputStream)
	 */
	public Settings(File file) throws FileNotFoundException, IOException {
		this(new FileInputStream(file));
	}

	/**
	 * Creates Settings with an String representing the Path to the properties
	 * file.
	 * 
	 * @param file
	 *            String representing the Path to the properties file.
	 * @throws FileNotFoundException
	 * @throws IOException
	 * 
	 * @see Settings#Settings(FileInputStream)
	 */
	public Settings(String file) throws FileNotFoundException, IOException {
		this(new FileInputStream(file));
	}

	/**
	 * Creating Properties with an file input stream of an properties file.
	 * 
	 * @param fis
	 * @throws IOException
	 * 
	 * @see Settings#Settings(BufferedInputStream)
	 */
	public Settings(FileInputStream fis) throws IOException {
		this(new BufferedInputStream(fis));
	}

	/**
	 * Loads configuration from properties file.
	 * 
	 * @param bis
	 * @throws IOException
	 */
	public Settings(BufferedInputStream bis) throws IOException {
		probs.load(bis);
		bis.close();
	}

	/**
	 * Returns the properties value as String.
	 * 
	 * @param key
	 * @return
	 */
	public String value(String key) {
		return probs.getProperty(key);
	}

	/**
	 * Returns the properties value as Integer
	 * 
	 * @param key
	 * @return
	 */
	public int valueAsInt(String key) {
		String prob = value(key);
		return Integer.parseInt(prob);
	}

	/**
	 * Returns the properties value as Float
	 * 
	 * @param key
	 * @return
	 */
	public float valueAsFloat(String key) {
		String prob = value(key);
		return Float.parseFloat(prob);
	}

	/**
	 * Return the properties value as RGB-Color
	 * 
	 * @param key
	 * @return
	 */
	public Color valueAsColor(String key) {
		String prob = value(key);
		return new Color(prob);
	}

	/**
	 * Returns a formated String using the properties value and args.
	 * 
	 * @param key
	 * @param args
	 * @return
	 */
	public String valueFormated(String key, Object... args) {
		String prob = value(key);
		return String.format(prob, args);
	}

	/**
	 * Class to create RGB-Color instances.
	 * 
	 * @author Luis
	 * 
	 */
	public static class Color {

		/**
		 * Value of red.
		 */
		public final float r;

		/**
		 * Value of green.
		 */
		public final float g;

		/**
		 * Value of blue.
		 */
		public final float b;

		/**
		 * Creating an new RGB-Color instance from an Properties value. If the
		 * prop contain three float values, the instance has an real RGB-color
		 * shade. If only one, it becomes an grey-shade. Otherwise, it is black.
		 * 
		 * @param prop
		 */
		public Color(String prop) {
			String[] values = prop.split(",");
			if (values.length == 1) {
				r = Float.parseFloat(values[0]);
				g = Float.parseFloat(values[0]);
				b = Float.parseFloat(values[0]);
			} else if (values.length == 3) {
				r = Float.parseFloat(values[0]);
				g = Float.parseFloat(values[1]);
				b = Float.parseFloat(values[2]);
			} else {
				r = 0;
				g = 0;
				b = 0;
			}
		}

	}

}
