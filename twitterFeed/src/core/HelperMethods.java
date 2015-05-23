package core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class HelperMethods {
	
		public static String getPropValue(String p) {
			File file = new File("Resources/config.properties");
			Properties prop = new Properties();
			InputStream inputStream = null;
			try {
				inputStream = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				System.out.println("File Not found " + e.getMessage());
				e.printStackTrace();
			}
	 		try {
				prop.load(inputStream);
			} catch (IOException e) {
				System.out.println("File Cannot be accessed  " + e.getMessage());
				e.printStackTrace();
			}
	 		return prop.getProperty(p);
		}
}
