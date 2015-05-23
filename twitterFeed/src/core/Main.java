package core;
import java.io.IOException;
import java.util.List;

import analysis.SignalNode;
import analysis.SignalsDecider;
import twitter4j.JSONException;
import twitter4j.TwitterException;
import gate.GateAnalysis;
import gate.util.GateException;


public class Main {
	
	public Main(){
		
	}
	public static void main(String[] args) throws JSONException{
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
	}
}
