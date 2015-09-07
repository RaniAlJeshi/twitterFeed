package core;
import java.io.IOException;
import java.util.List;

import analysis.SignalNode;
import analysis.SignalsDecider;
import twitter4j.JSONException;
import twitter4j.JSONObject;
import twitter4j.JSONTokener;
import twitter4j.TwitterException;
import gate.GateAnalysis;
import gate.util.GateException;


public class Main {
	
	public Main(){
		
	}
	/*public static void main(String[] args) throws JSONException{
		GateAnalysis ga = new GateAnalysis();
		try {
			List<SignalNode> signalNodes = ga.NLP_Process();
			signalNodes = SignalsDecider.sortSignals(signalNodes);
		} catch (GateException e){
			System.out.println("Gate Excution Error in Main: " + e.getMessage());
			e.printStackTrace();
		} catch( TwitterException e){
			System.out.println("Twitter Excution Error in Main: " + e.getMessage());
			e.printStackTrace();
		} catch(IOException e) {
			System.out.println("IOExcution Error in Main: " + e.getMessage());
			e.printStackTrace();
		}
	}*/
	public static void main(String[] args){
		//String yahooAppID = HelperMethods.getPropValue("w8IdI43c");
		//String baseUrl = HelperMethods.getPropValue("baseUrlYQL") + yahooAppID ;
		//String yahooAppKey = HelperMethods.getPropValue("yahoo_consumer_key");
		//String yahooAppSecret = HelperMethods.getPropValue("yahoo_consumer_secret");
		//String query = "&q=select * from yahoo.finance.xchange where pair in %28" + currString + "%29&env=store://datatables.org/alltableswithkeys";
		//String fullUrlStr = "http://query.yahooapis.com/v1/public/yql?appid=null&q=select%20*%20from%20yahoo.finance.xchange%20where%20pair%20in%20%28%22EURUSD%22%29&env=store://datatables.org/alltableswithkeys&format=json";
		
		
		//fullUrlStr = baseUrl  + query + "&format=json";
	
		try {
			
			//String resultUrl = HelperMethods.readUrl(fullUrlStr);
			//is = fullUrl.openStream();
			JSONTokener token;
			token = new JSONTokener("{\"query\":{\"count\":1,\"created\":\"2015-09-07T20:39:53Z\",\"lang\":\"en-US\",\"results\":{\"rate\":{\"id\":\"EURUSD\",\"Name\":\"EUR/USD\",\"Rate\":\"1.1169\",\"Date\":\"9/7/2015\",\"Time\":\"9:39pm\",\"Ask\":\"1.1170\",\"Bid\":\"1.1169\"}}}}");
			JSONObject result = new JSONObject(token);
			HelperMethods.analyseJSON(result);
			System.out.println(result.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
