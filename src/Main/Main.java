package Main;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import database.Database;

public class Main {

	public static void main(String[] args)
	{
		Database database = Database.getInstance();
		database.createDatabaseIfNotExists();
		database.storeTimeOfRequest();
	
	    Document doc = null;
		try 
		{
			doc = Jsoup.connect("http://www.javatpoint.com").get();
			String keywords = doc.select("meta[name=keywords]").first().attr("content");
			System.out.println("Meta keyword : " + keywords);
			String description = doc.select("meta[name=description]").get(0).attr("content");
			System.out.println("Meta description : " + description);
			
		} catch (IOException e) {
			e.printStackTrace();
		}  
	    
//	    getImages(doc);
    
//	    getLoginInfo(doc);
	    
		if(database.isLastTimeOfRequestMoreThanOneHourInThePast() && doc != null)
		{
			try {
				doc = Jsoup.connect("https://www.spiegel.de").get();
			} catch (IOException e) {
				e.printStackTrace();
			}
	//		for (Element sentence : doc.getElementsByTag("main"))
			for (Element sentence : doc.getElementsByTag("articleTeaser"))
			{
	            System.out.print(sentence);
			}
		}
			
		if(doc != null)
		{
		    Elements links = doc.select("a[href]");
	        for (Element link : links) {  
	            System.out.println("\nlink : " + link.attr("href"));
	            System.out.println("text : " + link.text());  
	        }
		}
	}

	private static void getLoginInfo(Document doc) {
		Element loginform = doc.getElementById("registerform");
	    if(loginform != null)
	    {
		    Elements inputElements = loginform.getElementsByTag("input");  
		    for (Element inputElement : inputElements) {  
		        String key = inputElement.attr("name");  
		        String value = inputElement.attr("value");  
		        System.out.println("Param name: "+key+" \nParam value: "+value);  
		    }
	    }
	}

	private static void getImages(Document doc) {
		Elements images = doc.select("img[src~=(?i)\\.(png|jpe?g|gif)]");  
	    for (Element image : images)
	    {  
	    	System.out.println("###############################");  
	        System.out.println("src : " + image.attr("src"));  
	        System.out.println("height : " + image.attr("height"));  
	        System.out.println("width : " + image.attr("width"));  
	        System.out.println("alt : " + image.attr("alt"));  
	    }
	}
}
