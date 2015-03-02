import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class HTMLParser {

	private static PrintWriter writer;
	private static String realTiming = "http://www.realtiming.co.il";
	private static String realTimingLink = null;
	
	
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		getRealTiming();
		//getShvoong();
	}


		
	private static void getRealTiming() throws FileNotFoundException, UnsupportedEncodingException {
		
		Document doc = null;
		writer = new PrintWriter("/tmp/events_realtiming.txt", "UTF-8");
		
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
							 String eventLink = row.attr("onclick").toString().split("'")[1];
							 realTimingLink = realTiming + eventLink;
							 parseRealtimeEvent(realTimingLink);
							 
							 Element Event = row.getElementsByTag("td").get(0);
							 String eventDate = Event.getElementsByTag("td").get(0).text();
							 String eventType = row.getElementsByTag("td").get(2).text();
							 System.out.println(" Date: " + eventDate);
							 System.out.println(" Type: " + eventType);
							
						 }
						 catch(ArrayIndexOutOfBoundsException ddd)
						 {
							 
						 }
						 catch(IndexOutOfBoundsException dd){}
					 }
			    }
			    
			    writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			writer.close();
		}
	}


public static void parseRealtimeEvent(String eventURL){
	Document doc = null;
	try {
		doc = Jsoup.connect(eventURL).get();
		Elements els = doc.select("meta");
		for( Element row : els) {
			if(row.hasAttr("name") && row.attr("name").toString().equals("description"))
			{
				String dd = row.attr("content").toString();
				writer.println("	Name:");
				System.out.println("Event Name: " + dd);
			}
		} 
		
		Elements masthead = doc.select("div.event_text");
		System.out.println("");
		
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}
	
	
	
	public static void parseShvoongEvent(String eventURL) throws FileNotFoundException, UnsupportedEncodingException
	{
		Document doc = null;
		try {
			doc = Jsoup.connect(eventURL).get();
			
			 Elements els = doc.select("span._summary");
			 for(Element e:els){
			        writer.println("	name:" + e.text());
			 }
			 els = doc.select("span._description");
			 for(Element e:els){
			        writer.println("	Description:" + e.text());
			 }
			 els = doc.select("span._location");
			 for(Element e:els){
			        writer.println("	Location:" + e.text());
			 }
			 els = doc.select("span._start");
			 for(Element e:els){
			        writer.println("	Date:" + e.text());
			 }
			 Element assigns = doc.select("table").get(0);
			 Elements rows = assigns.getElementsByTag("tr");
			 for(Element row : rows) {
				 try{
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
						 
				         writer.println(" EventType: " + eventtype);
				         writer.println(" distance: " + distance);
				         writer.println(" starttime: " + starttime);
				         writer.println(" type: " + type);
				         writer.println(" costEarly: " + costEarly);
				         writer.println(" costLate: " + costLate);
				 }
				 catch(IndexOutOfBoundsException e){
				 }
			 }
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void getShvoong() throws FileNotFoundException, UnsupportedEncodingException {
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
			          writer.println("Event: " + EventText);
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
