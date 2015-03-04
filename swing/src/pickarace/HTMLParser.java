package pickarace;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.crypto.Data;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;



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
	String vendor;
	String type;
	String registration_date_late;
	String registration_date_normal;
	ArrayList<subType> subtypes = new ArrayList<subType>();
	
	public void printEvent()
	{
		System.out.println("id: " + id);
		System.out.println("name: " + name);
		System.out.println("description: " + description);
		System.out.println("location: " + location);
		System.out.println("date: " + date);
		System.out.println("countryCode: " + countryCode);
		System.out.println("country: " + country);
		System.out.println("city: " + city);
		System.out.println("vendor: " + vendor);
		System.out.println("type: " + type);
		System.out.println("registration_date_late: " + registration_date_late);
		System.out.println("registration_date_normal: " + registration_date_normal);
	}
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
			 
			 //get the city name from google maps coordinates
			 Elements iframes = doc.getElementsByTag("iframe");
			 for(Element iframe:iframes){
				 String iframe_src = iframe.attr("src");
				 if(iframe_src.contains("https://maps.google.com") || iframe_src.contains("www.google.co.il/maps")){

					 System.out.println("parsing: " + iframe_src);
					 int ssl_index = iframe_src.indexOf("sll=");
					 int sspn_index = iframe_src.indexOf("&sspn=");
					 
					 
					int	 ll_index = iframe_src.indexOf("&ll=");
					int	 spn_index = iframe_src.indexOf("&spn=");
					 String location_cordinates;
					if(ll_index != -1 && spn_index != -1)
					{
						 location_cordinates = iframe_src.substring(ll_index+4, spn_index);
					}
					else{
						 location_cordinates = iframe_src.substring(ssl_index+4, sspn_index);
					}
					
					
					 try {
						event.city = getCity(location_cordinates);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				 }
			 
			 }
			 
			 
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
			 event.printEvent();
			 
			 
			 
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
	}
	
	public static String getCity(String cordinates) throws IOException, ParseException{
		
		String googleURL = "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + cordinates + "&sensor=false";
		InputStream inputStream = null;
        String json = "";

        try {           
            HttpClient client = new DefaultHttpClient();  
            HttpPost post = new HttpPost(googleURL);
            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            inputStream = entity.getContent();
        } catch(Exception e) {
        }

        try {           
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,"utf-8"),8);
            StringBuilder sbuild = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sbuild.append(line);
            }
            inputStream.close();
            json = sbuild.toString();               
        } catch(Exception e) {
        }


        //now parse
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(json);
        JSONObject jb = (JSONObject) obj;
        
        

        //now read
        JSONArray jsonObject1 = (JSONArray) jb.get("results");
        if(jsonObject1.size() == 0)
        {
        	System.out.println("***  Could not find City as the JSON response was empty!!");
        	return "";
        }
        	
        
        JSONObject jsonObject2 = (JSONObject)jsonObject1.get(0);
        JSONArray jsonObject3 = (JSONArray) jsonObject2.get("address_components");
        JSONObject jsonObject4 = (JSONObject)jsonObject3.get(1);
       
        String locationName = (String) jsonObject4.get("long_name");
		return locationName;
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
				        
				        System.out.println("Parsing: "  + EventHref);
				        parseShvoongEvent(EventHref,eventType);
				       // parseShvoongEvent("http://www.shvoong.co.il/events/%d7%9e%d7%a8%d7%95%d7%a5-%d7%9b%d7%a4%d7%a8-%d7%a1%d7%91%d7%90-3/",eventType);
				       

				        
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
