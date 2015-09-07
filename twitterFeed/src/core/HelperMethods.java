package core;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Properties;

import twitter4j.JSONObject;


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
		public static String readUrl(String urlString) throws Exception {
		    BufferedReader reader = null;
		    try {
		        URL url = new URL(urlString);
		        reader = new BufferedReader(new InputStreamReader(url.openStream()));
		        StringBuffer buffer = new StringBuffer();
		        int read;
		        char[] chars = new char[1024];
		        while ((read = reader.read(chars)) != -1)
		            buffer.append(chars, 0, read); 

		        return buffer.toString();
		    } finally {
		        if (reader != null)
		            reader.close();
		    }
		}
		
		public static void analyseJSON(JSONObject json){
			
		
		}
}
