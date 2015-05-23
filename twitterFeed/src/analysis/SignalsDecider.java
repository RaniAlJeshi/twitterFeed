package analysis;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import core.HelperMethods;
import twitter4j.JSONException;
import twitter4j.JSONObject;
import twitter4j.JSONTokener;


public class SignalsDecider {
	int strategy1 = 1; //Strategy one ....
	int strategy2 = 2; //Strategy two ....
	
	public SignalsDecider(){
		
	}

	public static SignalNode decide(SignalNode node1, SignalNode node2){
		
		return null; 
	}
	
	public static List<SignalNode> sortSignals(List<SignalNode> signals){
		
		
		
		for(SignalNode s : signals){
			/*
			 * TODO Sort the signals based on different strategies. 
			 * 
			 * if(tempSignal == null){
						signal.setPreNode(null);	
						tempSignal = signal;
					}
					else{
						signal.setPreNode(tempSignal.getTweetID());
						tempSignal.setNextNode(signal.getTweetID());
						tempSignal = signal; 
					}
			 */
			System.out.println(s); 
			System.out.println("PreNode to " + s.getTweetID()+ " is" + (s.getPreNode() != null ? s.getPreNode() : "NULL"));
			System.out.println("NextNode to " + s.getTweetID()+ " is" + (s.getNextNode() != null ? s.getNextNode() : "NULL"));
		}
		return signals;  
	}
	
	public static List<SignalNode> addSignalPrices(List<SignalNode> signals) throws IOException, JSONException{
		
		List<String> currList= new ArrayList<String>(); 
		String cur;
		char c = '/';
        for(SignalNode sn : signals){
			cur = sn.getCurrency();
			if(cur.length() == 6 )
				currList.add(cur);
			else if(cur.indexOf("/") == -1)
				currList.add(cur.substring(0,2) + cur.substring(4, 6));
			
			/*
			 * else{
				TODO I need to modefy Gate so It would attache the FX symbol to the signal so I can request from Yahoo having base currency in.
				//String baseCurr = HelperMethods.getPropValue("base_currency"); 
			*/
				
		}
		String  currString = currList.get(0); 
		for(int k = 1; k <  currList.size() - 1; k++){
			currString += "," + currList.get(k) ;
		}
		String yahooAppID = HelperMethods.getPropValue("w8IdI43c");
		String baseUrl = HelperMethods.getPropValue("baseUrlYQL") + yahooAppID ;
		//String yahooAppKey = HelperMethods.getPropValue("yahoo_consumer_key");
		//String yahooAppSecret = HelperMethods.getPropValue("yahoo_consumer_secret");
		String query = "&q=select * from yahoo.finance.exchange where pair in (" + currString + ")&env=store://datatables.org/alltableswithkeys";
		String fullUrlStr = null;
		URL fullUrl = null;
		InputStream is = null;
		
		
		fullUrlStr = URLEncoder.encode(baseUrl + query + "&format=json", "UTF-8");
	
		fullUrl = new URL(fullUrlStr);
		is = fullUrl.openStream();
		JSONTokener token;
		token = new JSONTokener(is);
		JSONObject result = new JSONObject(token);
		System.out.println(result.toString());
		is.close();
	//System.out.println(quotes.toString());
		
		return signals;  
	}
}
