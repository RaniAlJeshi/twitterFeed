package analysis;

import java.util.Date;

public class Signal{
	private String currency; 
	private Boolean polarity; 
	private String textBody;
	private Date dateOfTweet;
	private Date timeOfExcute; 
	private Date timeOfDexcute; 
	private Integer signalValue;
	private String tweetID; 
	private double buyNumber; 
	private double stopNumber; 
	private double bidNumber; 
	
	public Signal(){
		
	}
	
	public Signal(String currency, Boolean polarity, String textBody,
			Date dateOfTweet, Date timeOfExcute, Date timeOfDexcute,
			Integer signalValue, String tweetID) {
		super();
		this.currency = currency;
		this.polarity = polarity;
		this.textBody = textBody;
		this.dateOfTweet = dateOfTweet;
		this.timeOfExcute = timeOfExcute;
		this.timeOfDexcute = timeOfDexcute;
		this.signalValue = signalValue;
		this.tweetID = tweetID;
	}

	public String getTweetID() {
		return tweetID;
	}
	public void setTweetID(String tweetID) {
		this.tweetID = tweetID;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currencyPair) {
		this.currency = currencyPair;
	}
	public Boolean getPolarity() {
		return polarity;
	}
	public void setPolarity(Boolean polarity) {
		this.polarity = polarity;
	}
	public String getTextBody() {
		return textBody;
	}
	public void setTextBody(String textBody) {
		this.textBody = textBody;
	}
	public Date getDateOfTweet() {
		return dateOfTweet;
	}
	public void setDateOfTweet(Date dateOfTweet) {
		this.dateOfTweet = dateOfTweet;
	}
	public Date getTimeOfExcute() {
		return timeOfExcute;
	}
	public void setTimeOfExcute(Date timeOfExcute) {
		this.timeOfExcute = timeOfExcute;
	}
	public Date getTimeOfDexcute() {
		return timeOfDexcute;
	}
	public void setTimeOfDexcute(Date timeOfDexcute) {
		this.timeOfDexcute = timeOfDexcute;
	}
	public Integer getSignalValue() {
		return signalValue;
	}
	public void setSignalValue(Integer signalValue) {
		this.signalValue = signalValue;
	} 
	public double getStartNumber() {
		return buyNumber;
	}

	public void setStartNumber(double startNumber) {
		this.buyNumber = startNumber;
	}

	public double getStopNumber() {
		return stopNumber;
	}

	public void setStopNumber(double stopNumber) {
		this.stopNumber = stopNumber;
	}

	public double getBidNumber() {
		return bidNumber;
	}

	public void setBidNumber(double bidNumber) {
		this.bidNumber = bidNumber;
	}
	
	
}