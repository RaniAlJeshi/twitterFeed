package analysis;

public class SignalNode extends Signal{

	private String nextNodeID;
	private String preNodeID;
	
	public SignalNode(){
		
	}
	
	public void setNextNode(String signal){
		nextNodeID = signal; 
	}
	
	public String getNextNode(){
		return nextNodeID; 
	}
	
	public void setPreNode(String signal){
		preNodeID = signal; 
	}
	
	public String getPreNode(){
		return preNodeID; 
	}
	
	public String toString(){
		return "ID: " + this.getTweetID() + " CurPair/Cur: " + this.getCurrency() + " Text: " + this.getTextBody() +" Polarity: "+this.getPolarity() +
		" Date Of Tweet: "+this.getDateOfTweet()+ " Date of Excution:"+ this.getTimeOfExcute()+" Date of Dexcute: "+this.getTimeOfDexcute()+
		" Signal Value: "+this.getSignalValue()+" Start Number: "+this.getStartNumber()+" Stop Number: "+ this.getStopNumber() +"\r\n"; 
	}
}
