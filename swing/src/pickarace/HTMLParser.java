package pickarace;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


class Event{
	String id;
	String name;
	String description;
	String location;
	String date;
	String countryCode="IL";
	String country="ישראל";
	String city;
	String status="active";
	String vendor="shvoong";
	String type;
	String registration_date_late;
	String registration_date_normal;
	ArrayList<subType> subtypes = new ArrayList<subType>();
}

class subType{
	String distance;
	String link;
	String price_late;
	String price_normal;
}


public class HTMLParser {

	private static PrintWriter writer;
	private static String realTiming = "http://www.realtiming.co.il";
	private static String shvoong = "http://www.shvoong.co.il";
	private static String realTimingLink = null;
	private static String realTimingRegisterLink = null;
	public static ArrayList<Event> eventsList = new ArrayList<Event>();
	private static int sleeptime = 1000;
	
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		getShvoong(3);
		getRealTiming();
	}


		
	public static void getRealTiming() {
		
		Document doc = null;
		
		try {
			
			doc = Jsoup.connect(realTiming + "/events").get();
			
			//events-items
			 Elements els = doc.getElementsByClass("event_list");
			    for(Element e:els){
			    	Element assigns = doc.select("table").get(0);
			    	
			    	 Elements rows = assigns.getElementsByTag("tr");
					 for(Element row : rows) {
						 String sw  = row.toString();
						 try{
							 
							 Element Event = row.getElementsByTag("td").get(0);
							 String eventDate = Event.getElementsByTag("td").get(0).text();
							 
							 String eventLink = row.attr("onclick").toString().split("'")[1];
							 realTimingLink = realTiming + eventLink;
							 parseRealtimeEvent(realTimingLink,eventDate);
						 }
						 catch(ArrayIndexOutOfBoundsException ddd){}
						 catch(IndexOutOfBoundsException dd){}
					 }
			    }
			    
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}

public static String getEventType(String eventType){
	
	if(eventType.equals("ריצה"))
	{
		return "running";
	}
	else if(eventType.contains("ריצה ו")){
		return "triathlon";
	}
	else if(eventType.equals("אופניים")){
		return "biking";
	}
	else if(eventType.equals("שחיה")){
		return "swiming";
	}
	else if(eventType.equals("טריאתלון")){
		return "triathlon";
	}
	else{
		return  "running";
	}
	
}
public static void parseRealtimeEvent(String eventURL, String eventDate){
	
	Event event=new Event();
	Document doc = null;
	
	try {
		doc = Jsoup.connect(eventURL).get();
		Elements els = doc.select("meta");
		for( Element row : els) {
			if(row.hasAttr("name") && row.attr("name").toString().equals("description"))
				event.name = row.attr("content").toString();
		} 
		
		Elements paragraphs = doc.select("p");
		String eventDesc = "";
		for(Element p : paragraphs){
			String test = p.text().trim().replaceAll(" ", "");
			if( test.length() > 0)
				eventDesc += p.text() + "\n";
		}
		event.description = eventDesc;
		
		String[] eventPlace = doc.select("div.event_place").text().split("מקום: ");
		String[] eventType = doc.select("div.event_cat_name").text().split("ענף: ");
		String eventCity = eventPlace[1].trim();
	
		
		
		event.city = eventCity;
		event.location = eventPlace[1].trim();
		event.type = getEventType(eventType[1].trim());
		event.vendor="realtiming";
		
		Element contests = doc.select("table.heat").get(0);
		
		 Elements rows = contests.getElementsByTag("tr");
		 for(Element row : rows) {
			 
			 try{
				 subType subType = new subType();
				 String eventLink = row.attr("onclick").toString().split("'")[1];
				 realTimingRegisterLink = realTiming + eventLink;
				 String contentName = row.getElementsByTag("td").get(0).text();
				 //String contentDistance = row.getElementsByTag("td").get(1).text();
				 String priceEarly = row.getElementsByTag("td").get(2).text();
				 String priceLate = row.getElementsByTag("td").get(3).text();
				 
				 subType.distance=contentName;
		         subType.link=realTimingRegisterLink;
		         subType.price_normal=priceEarly;
		         subType.price_late=priceLate;
		         event.subtypes.add(subType);
			 }
			 catch(IndexOutOfBoundsException e){}
		 }
		 eventsList.add(event);
		 //event.toString();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}
	
	
	
	public static void parseShvoongEvent(String eventURL, String eventType) throws FileNotFoundException, UnsupportedEncodingException
	{
			
		Event event=new Event();
		
		event.type = getEventType(eventType); 
		event.vendor="shvoong";
		String registrationLink = null;
		Document doc = null;
		try {
			
			
			for(int i=1;i<3;i++){
				Thread.sleep(sleeptime);
				try{
					doc = Jsoup.connect(eventURL).timeout(3000).get();
					continue;
				}catch(SocketTimeoutException r){
					System.out.println("Got Http Socket timeout Exception... trying again");
				}
			}
					
			
			 event.name = doc.getElementsByClass("sub-category-title").text();
			 event.description = doc.getElementsByClass("entry-content").text(); 
			 event.location=doc.getElementsByClass("_location").text();
			 event.date=doc.getElementsByClass("_start").text();
			 
			 
			 if(event.name.length()<=0){
				 Elements metaTags = doc.getElementsByTag("meta");
				  for(Element metatag:metaTags){
			        	String metatagValue = metatag.attr("property");
			        	if(metatagValue.equals("og:title")){
			        		event.name = metatag.attr("content");
			        	}
			        	if(metatagValue.equals("og:description")){
			        		event.description = metatag.attr("content");
			        	}
				  }
			 }
			 
			 

			 Elements ddd = doc.select("article");
			 for(Element eee:ddd){
			        Elements links1 = eee.getElementsByTag("a");
			        for(Element e1:links1){
			        	String registerLink = e1.attr("href");
			        	String target = e1.attr("target");
			        	if(target.equals("_blank"))
			        		registrationLink = registerLink;
			        }
			 }
			 
			 if(registrationLink != null && registrationLink.equals(""))
				 registrationLink = eventURL;
			 
			 try{
				 Elements doesTableExist = doc.select("table");
				 
				 if(doesTableExist.size() == 0)
				 {
					 System.out.println("INFO: No additional data exist for this event.");
					 return;
				 }
					 
				 
					 Element assigns = doc.select("table").get(0); 
					 Elements rows = assigns.getElementsByTag("tr");
					 int count = 1;
					 for(Element row : rows) {
						 if(count == 1){ //skip first row
							 count++;
							 continue;
						 }
							 subType subType = new subType();
							 
							 if(registrationLink != null){
								 subType.link = registrationLink;
							 }
							 else{
								 subType.link = eventURL;
							 }
								 String eventtype = row.getElementsByTag("td").get(0).text();
								 String distance = row.getElementsByTag("td").get(1).text();
								 String starttime = row.getElementsByTag("td").get(2).text();
								 String type = row.getElementsByTag("td").get(3).text();
								 String cost = row.getElementsByTag("td").get(4).text();
								 
								 String delimiter = "/";;
								 
								 if(cost.contains("-"))
									 delimiter = "-";
								 
								 String[] costStrArray = cost.split(delimiter);
								 String costEarly = "";
								 String costLate = "";
								 
								 
								 // The way they write the price is not consistent, sometimes with - sometimes with /
								 // so the array spliting does not come up correctly. the array must be at length 2 order to parse the correct ammounts.
								 if(costStrArray.length >= 2)
								 {
								 
									 
									 for( String costs:costStrArray){
										 String[] cost_late = null;
										 
										 if(costs.contains(" ש\"ח"))
										 {
											 cost_late = costs.split(" ש\"ח");
											 costLate = cost_late[0];
											 
										 }
										 else if(costs.contains("-")){
											 cost_late = costs.split("-");
											 costLate = cost_late[0];
											 costEarly = cost_late[1];
										 }
										 else{
											 costEarly = costs;
											 costLate  = costs;
										 }
									 }
					 			}
								 else{
									 if(cost.length() != 0)
										 System.out.println("Could not parse correctly the prices!! check it. the cost looks like this:" + cost);
									 costEarly = costStrArray[0];
									 costLate  =  costStrArray[0];
								 }
								 
						         System.out.println(" EventType: " + eventtype);
						         System.out.println(" distance: " + distance);
						         System.out.println(" starttime: " + starttime);
						         System.out.println(" type: " + type);
						         System.out.println(" costEarly: " + costEarly);
						         System.out.println(" costLate: " + costLate);
						         
						         
						         subType.distance=distance;
						         subType.price_normal=costEarly;
						         subType.price_late=costLate;
						         event.subtypes.add(subType);
					 } 
				 }catch(IndexOutOfBoundsException e){
					System.out.println("Event didn't have any data: " + e.getMessage());
				 }
			 eventsList.add(event);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public static void getShvoong(int attemp) {
		Document doc = null;
	
		try {
			
			for(int i=1;i<=20;i++){
				
				String shvoongLink = null;
				if(i == 1)
					shvoongLink = shvoong + "/events";
				else
					shvoongLink = shvoong + "/events/page/" + i + "/";
				
				
				try {
					Thread.sleep(sleeptime);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				System.out.println("**********");
				doc = Jsoup.connect(shvoongLink).timeout(3000).get();
				System.out.println("**********");
				
				
				 Elements imageElements = doc.select("article");
				 String EventHref = "";
				 String EventText = "";
				 String eventType = "";
				 for(Element e:imageElements){
				        Element links = e.getElementsByTag("a").first();
				        EventHref = links.attr("href");
				        EventText = links.attr("title");
				        System.out.println("Event: " + EventText);
				        
				        Elements icons = e.getElementsByTag("li");
				        int count = 1;
				        for (Element icon : icons) {
				        	if(count == 3){
				        		Elements imageData = icon.select("img");
				        		eventType = imageData.attr("title").trim();
				        	}
				        	count++;
				        }
				        
				        System.out.println("Trying to open: "  + EventHref);
				        parseShvoongEvent(EventHref,eventType);
				        
				        	//parseShvoongEvent("http://www.shvoong.co.il/events/%d7%98%d7%a8%d7%99%d7%90%d7%aa%d7%9c%d7%95%d7%9f-%d7%a2%d7%9e%d7%a7-%d7%94%d7%9e%d7%a2%d7%99%d7%99%d7%a0%d7%95%d7%aa-2/",eventType);
				    }
			}

		} catch (IOException e) {/*

			if(attemp!=0){
				try {
					Thread.sleep(3);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				getShvoong(attemp--);
			}*/
			e.printStackTrace();
		}
		
	}

}
