package gate;

import gate.creole.ResourceInstantiationException;
import gate.creole.SerialAnalyserController;
import gate.util.GateException;
import gate.util.persistence.PersistenceManager;
import gate.Document;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import analysis.SignalNode;
import analysis.SignalsDecider;
import twitter4j.JSONException;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitterFeed.twitterFeed;
/*
 * This class to get the tweets form twitter with lastID and analyze them one by one before getting the next patch of 
 * tweets. 
 */
public class GateAnalysis {

	public  long lastTweetID;
	private List<SignalNode> signals = new ArrayList<SignalNode>(); 
	SignalNode tempSignal; 
	Corpus cor;
	File gappFile = new File("Resources/Golder.gapp");
	Boolean gateInit = false; 
	
	//TODO Change to properties file golder.properties.
	static final int shortExcution = 2;
	static final int mediumExcution = 12;
	static final int longExcution = 24;
	
	public  long getLastTweetID() {
		return lastTweetID;
	}

	public  void setLastTweetID(long lastTweetID) {
		this.lastTweetID = lastTweetID;
	}
	
	
	/**
	 * Getting tweets from tweeter. 
	 * If lastTweetID exist it will pull tweets from that ID otherwise it will 
	 * pull recent 20 tweets in my time line.  
	 * @return list<status> tweets. 
	 */
	private  List<Status> getTweets()
	{
		
		List<Status> tweets = null; 
		
		//Twitter Start
		twitterFeed twitterStream = new twitterFeed();
		
		Twitter twitter = new TwitterFactory().getInstance();
		
		try {
			tweets = twitter.getHomeTimeline();
			lastTweetID = tweets.get(tweets.size()-1).getId(); 
			//twitterFeed feed = new twitterFeed(); 
			twitterStream.getStreamS(getLastTweetID());
			
		} catch (TwitterException e) {
			e.printStackTrace();
			System.out.println("Twitter Exception Error: " + e.getMessage());
			//TODO Get the twitter exception related to time then add a time off before fetchin next patch.  
		}
		
		return tweets; 
	}
	/**
	 * 
	 * This will take the tweets strip unnessacery information and create a doc 
	 * with ready to process tweets.  
	 * 
	 * @param list
	 * @return gate Document doc
	 */
	private  Document createDoc(List<Status> list)
	{
		StringBuffer sb = new StringBuffer();
		Document doc = null; 
		for(int i = 0; i< list.size(); i++)
		{
			Status tweet = list.get(i);
			sb.append("ID: "+tweet.getId() + " - User: " +tweet.getUser().getScreenName() + " - Text: " + tweet.getText() +".\r\n");
		}
		//Getting last Id in the stream for next stream. 
		setLastTweetID(list.get(list.size()-1).getId()); 
		
		try {
			doc = Factory.newDocument(sb.toString());
			//System.out.println(sb.toString());			
			
		} catch (ResourceInstantiationException e) {
			System.out.println("Document Couldn't Start Error: " + e.getMessage());
			e.printStackTrace();
		}
		return doc;
	}
	
	/**
	 * Initialize gate for first time. 
	 * @throws GateException
	 */
	private  void initGate() throws GateException
	{
		Gate.setUserConfigFile(new File("user-gate.xml")); 
        Gate.setSiteConfigFile(new File("gate.xml"));
        GateCore gateCore = new GateCore();
        gateCore.makeVisible(); 
        
        cor = Factory.newCorpus("Cor1");
        gateInit = true; 
        
	}
	
	//TODO Refactor;
	private  List<Status> excludeRepititve(List<Status> t){
		
		Iterator<Status> t1 = t.iterator(); 
		Iterator<Status> t2 = t.iterator();
		while(t1.hasNext())
		{
			Status s1 = (Status) t1.next();
			while(t2.hasNext()){
				Status s2 = (Status) t2.next();
				if(s1.getId()!= s2.getId() && s1.getText().equals(s2.getText()))
				{	
					t.remove(t.indexOf(s1));
					t.get(t.indexOf(s2));
				}
			}
		}
		return t;
		
	}
	@SuppressWarnings("deprecation")
	private  List<SignalNode> getSignals(AnnotationSet signalsAnnot, List<Status> tweets, Document doc){
		
		AnnotationSet tweetsIDs = doc.getAnnotations().get("tweetID");
		AnnotationSet currencyPair = doc.getAnnotations().get("CurrencyPair");
		AnnotationSet currency = doc.getAnnotations().get("Currency");
		AnnotationSet positiveSignal = doc.getAnnotations().get("PositiveSignal");
		AnnotationSet negativeSignal = doc.getAnnotations().get("NegativeSignal");
		AnnotationSet neutralSignal = doc.getAnnotations().get("NeutralSignal");
		String signalText; 
		for(Annotation annot : signalsAnnot){
			SignalNode signal = new SignalNode();
			Status currentTweet ; 
			AnnotationSet tweetSpecificID = gate.Utils.getContainedAnnotations(tweetsIDs, annot);
			String sentencID = "000"; 
			try{
				sentencID = gate.Utils.stringFor(doc, tweetSpecificID);
			}catch(NullPointerException e){
				System.out.println("Null Point Exception in reading TweetID \r\n" + e.getMessage() + "\r\n The Value is: " + tweetSpecificID.toString());
			}
			for(int e =0; e < tweets.size() - 1; e++){
				// TODO Add more fields 
				currentTweet = tweets.get(e);
				if(currentTweet.getId() == Long.parseLong(sentencID))
				{
					signal.setTweetID(sentencID+"");
					signal.setDateOfTweet(currentTweet.getCreatedAt());
					if(gate.Utils.getContainedAnnotations(positiveSignal, annot).size() != 0)
					{
						AnnotationSet s = gate.Utils.getContainedAnnotations(positiveSignal, annot);
						signalText = gate.Utils.stringFor(doc, s);
						signal.setPolarity(true);
					}
					else if (gate.Utils.getContainedAnnotations(negativeSignal, annot).size() != 0)
					{
						AnnotationSet s = gate.Utils.getContainedAnnotations(negativeSignal, annot);
						signalText = gate.Utils.stringFor(doc, s);
						signal.setPolarity(false);
					}
					else
					{
						AnnotationSet s = gate.Utils.getContainedAnnotations(neutralSignal, annot);
						signalText = gate.Utils.stringFor(doc, s);
						signal.setPolarity(null);
					}
					signal.setTextBody(signalText);// or NegativeSignal ..polarity. 
					if(gate.Utils.getContainedAnnotations(currencyPair, annot).size() != 0)
					{
						AnnotationSet c = gate.Utils.getContainedAnnotations(currencyPair, annot);
						signalText = gate.Utils.stringFor(doc, c);
						signal.setCurrency(signalText);
					}
					else
					{
						AnnotationSet c = gate.Utils.getContainedAnnotations(currency, annot);
						signalText = gate.Utils.stringFor(doc, c);
						signal.setCurrency(signalText);
					}
					
					signal.setSignalValue(currentTweet.getRetweetCount());
					signal.setDateOfTweet(currentTweet.getCreatedAt());
					//TODO fix the Logic with factual dates and times.
					//TODO Fix the date time deprecated methods. 
				    Date date = new Date();
				    Date date2 = new Date();
					signal.setTimeOfExcute(date);
					date2.setHours(date2.getHours() + shortExcution); 
					signal.setTimeOfDexcute(date2);
					signals.add(signal); 
						
				}
				 
			}
		}
		return signals;
	}
	
	/**
	 * This will analys the patch of tweets from lastTweetedId or
	 * recent 20 tweets. 
	 * 
	 * @throws GateException
	 * @throws TwitterException
	 * @throws IOException
	 * @throws JSONException 
	 */
	public  List<SignalNode> NLP_Process() throws GateException, TwitterException, IOException, JSONException
	{
		if(gateInit == false)
			initGate();
		
		List<Status> tweetsRaw = getTweets(); 
		Document d = createDoc(tweetsRaw);
		cor.add(d);
		SerialAnalyserController pipeLine = (SerialAnalyserController)PersistenceManager.loadObjectFromFile(gappFile);
		pipeLine.setName("ReadyMade");
		pipeLine.setCorpus(cor);
		try {
			Gate.setExecutable(pipeLine);
			Gate.getExecutable().execute();
		} catch (GateException e) {
			System.out.println("Gate Excution Error: " + e.getMessage());
			e.printStackTrace();
		} catch(IndexOutOfBoundsException e){
			System.out.println("Gate Out of Bound Excution Error: " + e.getMessage());
			e.printStackTrace();
		}
		tweetsRaw = excludeRepititve(tweetsRaw);
		AnnotationSet sentenceSignal = d.getAnnotations().get("SentenceSignal");
		
		
		//Before return Add the prices from Yahoo Finance in one call for this patch of signals. 
		List<SignalNode> signalsToDecider = getSignals(sentenceSignal, tweetsRaw, d); 
		if (signalsToDecider.size() > 0)
			return SignalsDecider.addSignalPrices(signalsToDecider);
		else
		{
			System.out.println("No singnals in this patch...."); 
			return null;
		}
	}
}