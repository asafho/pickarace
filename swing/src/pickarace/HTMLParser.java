package pickarace;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.ArrayList;

import javax.swing.plaf.SliderUI;

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
	
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		getShvoong(3);
		//getRealTiming();
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
		Document doc = null;
		try {
			Thread.sleep(100);
			doc = Jsoup.connect(eventURL).timeout(3000).get();
					
			
			 event.name = doc.getElementsByClass("sub-category-title").text();
			 event.description = doc.getElementsByClass("entry-content").text(); 
			 event.location=doc.getElementsByClass("_location").text();
			 event.date=doc.getElementsByClass("_start").text();
			 //<a target="_blank" href="http://events.shvoong.co.il/kfarsaba/" class="register btn blue gradient">פרטים והרשמה</a>
			 
			 
			 String links = doc.getElementsByTag("a").text();

			 
			 doc.getElementsByClass("register btn blue gradient").text();
			 try{
					 Element assigns = doc.select("table").get(0);
					 Elements rows = assigns.getElementsByTag("tr");
					 int count = 1;
					 for(Element row : rows) {
						 if(count == 1){ //skip first row
							 count++;
							 continue;
						 }
							 subType subType = new subType();
								 String eventtype = row.getElementsByTag("td").get(0).text();
								 String distance = row.getElementsByTag("td").get(1).text();
								 String starttime = row.getElementsByTag("td").get(2).text();
								 String type = row.getElementsByTag("td").get(3).text();
								 String cost = row.getElementsByTag("td").get(4).text();
								 String[] a = cost.split("/");
								 String costEarly = "";
								 String costLate = "";
								 for( String costs:a){
									 String[] cost_late = null;
									 if(costs.contains(" ש\"ח"))
									 {
										 cost_late = costs.split(" ש\"ח");
										 costLate = cost_late[0];
										 costLate = cost_late[1];
										 
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
								 
						         System.out.println(" EventType: " + eventtype);
						         System.out.println(" distance: " + distance);
						         System.out.println(" starttime: " + starttime);
						         System.out.println(" type: " + type);
						         System.out.println(" costEarly: " + costEarly);
						         System.out.println(" costLate: " + costLate);
						         
						         
						         subType.distance=distance;
						         subType.link="";
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
					shvoongLink = shvoong + "/page/" + i + "/";
				
				doc = Jsoup.connect(shvoongLink).get();
				
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
				    }
			}

		} catch (IOException e) {

			if(attemp!=0){
				try {
					Thread.sleep(3);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				getShvoong(attemp--);
			}
			e.printStackTrace();
		}
		
	}

}
