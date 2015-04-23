package gate;

import java.io.File;

import gate.util.GateException;
import gate.creole.annic.apache.lucene.document.*;
import gate.gui.MainFrame;
import gate.Corpus;
import gate.Gate;

public class GateCore 
{
	/*
	 * 1- Get tweets in a document
	 * 2- populate corpus
	 * 3- run pipeline to corpus
	 * 4- get info from annotation
	 * 5- populate DB
	 */
	//Solo constructor
	Document doc; 
	Corpus cor; 
	
	public GateCore() throws GateException
	{
		gate.Gate.init();
		
	}

	
	
	//Passing the tweets in a document
	public void applyPipe()
	{
		try 
		{
			Document doc = getDoc();
		} 
		catch (Exception e) 
		{
			System.out.println("Error 1001 " + e.getMessage()); 
		} 
		
	}
	
	
	
	//Getters and Setters
	public void setDoc(Document doc)
	{
		this.doc = doc;
	
	}
	
	public Document getDoc()
	{
		return doc; 
	}

	public Corpus getCor() 
	{
		return cor;
	}

	public void setCor(Corpus cor)
	{
		this.cor = cor;
	}
	
	public void makeVisible()
	{
		MainFrame.getInstance().setVisible(true);
	}


}
