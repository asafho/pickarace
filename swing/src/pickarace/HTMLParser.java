package pickarace;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
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
	
	public String toString() {
		StringBuilder result = new StringBuilder();
		String newLine = System.getProperty("line.separator");
		result.append( this.getClass().getName() );
		  result.append( " Object {" );
		  result.append(newLine);
		//determine fields declared in this class only (no fields of superclass)
		  Field[] fields = this.getClass().getDeclaredFields();

		  //print field names paired with their values
		  for ( Field field : fields  ) {
		    result.append("  ");
		    try {
		      result.append( field.getName() );
		      result.append(": ");
		      //requires access to private field:
		      
		      if(field.getName().equals("subtypes")){
		    	  field.get(this).toString();
		      }
		      else{
		    	  result.append( field.get(this) );
		      }
		    } catch ( IllegalAccessException ex ) {
		      System.out.println(ex);
		    }
		    result.append(newLine);
		  }
		  result.append("}");
		  System.out.println(result.toString());
		  return result.toString();
	}
}

class subType{
	String distance;
	String link;
	String price_late;
	String price_normal;
	
	public String toString() {
		StringBuilder result = new StringBuilder();
		String newLine = System.getProperty("line.separator");
		result.append( this.getClass().getName() );
		  result.append( " Object {" );
		  result.append(newLine);
		//determine fields declared in this class only (no fields of superclass)
		  Field[] fields = this.getClass().getDeclaredFields();

		  //print field names paired with their values
		  for ( Field field : fields  ) {
		    result.append("  ");
		    try {
		      result.append( field.getName() );
		      result.append(": ");
		      //requires access to private field:
		    	  result.append( field.get(this) );
		    } catch ( IllegalAccessException ex ) {
		      System.out.println(ex);
		    }
		    result.append(newLine);
		  }
		  result.append("}");
		  System.out.println(result.toString());
		  return result.toString();
	}
}


public class HTMLParser {

	private static PrintWriter writer;
	private static String realTiming = "http://www.realtiming.co.il";
	private static String realTimingLink = null;
	private static String realTimingRegisterLink = null;
	public static ArrayList<Event> eventsList = new ArrayList<Event>();
	
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		getRealTiming();
		//getShvoong();
	}


		
	private static void getRealTiming() throws FileNotFoundException, UnsupportedEncodingException {
		
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
		event.type = eventType[1].trim();
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
	
	
	
	public static void parseShvoongEvent(String eventURL) throws FileNotFoundException, UnsupportedEncodingException
	{
			
		Event event=new Event();
		event.id=System.currentTimeMillis()+"";
		event.vendor="shvoong";
		Document doc = null;
		try {
			doc = Jsoup.connect(eventURL).get();
			
			 Elements els = doc.select("span._summary");
			 for(Element e:els){
			        System.out.println("	name:" + e.text());
			        event.name=e.text();
			        
			 }
			 els = doc.select("span._description");
			 for(Element e:els){
			        System.out.println("	Description:" + e.text());
			        event.description=e.text();
			 }
			 els = doc.select("span._location");
			 for(Element e:els){
			        System.out.println("	Location:" + e.text());
			        event.location=e.text();
			 }
			 els = doc.select("span._start");
			 for(Element e:els){
			        System.out.println("	Date:" + e.text());
			        event.date=e.text();
			 }
			 Element assigns = doc.select("table").get(0);
			 Elements rows = assigns.getElementsByTag("tr");
			 for(Element row : rows) {
				 try{
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
				         subType.link=distance;
				         subType.price_normal=costEarly;
				         subType.price_late=costLate;
				         event.subtypes.add(subType);
				 }
				 catch(IndexOutOfBoundsException e){
					 e.printStackTrace();
				 }
			 }
			 eventsList.add(event);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void getShvoong() throws FileNotFoundException, UnsupportedEncodingException {
		Document doc = null;
		writer = new PrintWriter("/tmp/events.txt", "UTF-8");
		
		try {
			
			doc = Jsoup.connect("http://www.shvoong.co.il/events/").get();
			
			//events-items
			 Elements els = doc.getElementsByClass("events-items");
			    for(Element e:els){
			        Elements links = e.getElementsByTag("a");
			        for (Element link : links) {
			          String EventHref = link.attr("href");
			          String EventText = link.text();
			          System.out.println("Event: " + EventText);
			          parseShvoongEvent(EventHref);
			        }
			    }
			    
			    writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			writer.close();
		}
		
	}

}
