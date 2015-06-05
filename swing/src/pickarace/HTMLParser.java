package pickarace;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;

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
	public void printSubType()
	{
		System.out.println("distance: " + distance);
		System.out.println("link: " + link);
		System.out.println("price_late: " + price_late);
		System.out.println("price_normal: " + price_normal);
	}
}


public class HTMLParser {

	private static String realTiming = "http://www.realtiming.co.il";
	private static String znook = "https://www.znook.co.il";
	private static String sportweb = "http://www.sportweb.co.il/%D7%90%D7%99%D7%A8%D7%95%D7%A2%D7%99%D7%9D";
	private static String shvoong = "http://www.shvoong.co.il";
	private static String realTimingLink = null;
	private static String realTimingRegisterLink = null;
	public static ArrayList<Event> eventsList = new ArrayList<Event>();
	private static int sleeptime = 1000;
	static SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	
public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		getSportWeb();
		getRealTiming();
		getShvoong();
		aws.uploadNewFile();
	}
		
public static void getSportWeb() {
		String starttime = format.format(Calendar.getInstance().getTime());
		
		Document doc = null;
		
		try {
			doc = Jsoup.connect(sportweb).get();
			
			//events-items
			 Elements els = doc.getElementsByClass("tevents_nav");
			    for(Element e:els){
			    	 Elements events = e.getElementsByTag("li");
			    	 for(Element e1:events){
			    		 	Event event = new Event();
			    		 	subType subType = new subType();
			    		 	Elements eventlink = e1.getElementsByTag("a");
			    		 	subType.link = eventlink.attr("href");
			    		 	subType.distance      = eventlink.attr("title");
					        subType.price_normal = "";
					        subType.price_late   = "";
			    		 	
			    		 	
					        event.name = eventlink.attr("title");
					        String year = "2015";
					        if(eventlink.attr("title").contains("2016")){
					        	year = "2016";
					        }
					        String numberOnly= eventlink.select("span").text().replaceAll("[^0-9]", "");
					        
					        
					        Element mye  = eventlink.select("span").first();
					        
					        String datestring = mye.select("small").text();
					        SimpleDateFormat fromUser = new SimpleDateFormat("dd-MM-yyyy");
					        String reformattedStr = "";
					        try {
								reformattedStr = fromUser.format(fromUser.parse(numberOnly + "-" + getMonth(datestring) + "-" + year));
							} catch (java.text.ParseException e2) {
								// TODO Auto-generated catch block
								e2.printStackTrace();
							}
					        event.date = reformattedStr;
					        
					        event.vendor = "sportweb";
					        event.type = "running";
					        event.description = eventlink.attr("title");
					        event.subtypes.add(subType);
					        eventsList.add(event);
			    	 }
			    }	    
		} catch (IOException e) {
			System.out.println("Exception in sportweb parsing");
			e.printStackTrace();
		}
	
}
	

public static String getMonth(String mon){
	
	if(mon.equals("ינואר")){
		return "1";
	}
	if(mon.equals("פברואר")){
		return "2";
	}
	if(mon.equals("מרץ")){
		return "3";
	}
	if(mon.equals("אפריל")){
		return "4";
	}
	if(mon.equals("מאי")){
		return "5";
	}
	if(mon.equals("יוני")){
		return "6";
	}
	if(mon.equals("יולי")){
		return "7";
	}
	if(mon.equals("אוגוסט")){
		return "8";
	}
	if(mon.equals("ספטמבר")){
		return "9";
	}
	if(mon.equals("אוקטובר")){
		return "10";
	}
	if(mon.equals("נובמבר")){
		return "11";
	}
	if(mon.equals("דצמבר")){
		return "12";
	}
	
	return "";
}

public static void getRealTiming() {
		
		String starttime = format.format(Calendar.getInstance().getTime());
		
		Document doc = null;
		
		try {
			
			doc = Jsoup.connect(realTiming + "/events").get();
			
			//events-items
			 Elements els = doc.getElementsByClass("event_list");
			    for(Element e:els){
			    	Element assigns = doc.select("table").get(0);
			    	
			    	 Elements rows = assigns.getElementsByTag("tr");
					 for(Element row : rows) {
						 try{
							 if(row.hasAttr("onclick")){
								 Element Event = row.getElementsByTag("td").get(0);
								 String eventDate = Event.getElementsByTag("td").get(0).text();
								 
								 String eventLink = row.attr("onclick").toString().split("'")[1];
								 realTimingLink = realTiming + eventLink;
								 
								 parseRealtimeEvent(realTimingLink,eventDate);
							 }
						 }
						 catch(ArrayIndexOutOfBoundsException ddd){
							 System.out.println("Exception 9");
								ddd.printStackTrace();
						 }
						 catch(IndexOutOfBoundsException dd){
							 System.out.println("Missing event data for: " + realTimingLink);
						 }
					 }
			    }
			    
		} catch (IOException e) {
			System.out.println("Exception 11");
			e.printStackTrace();

		}
}

public static void getShvoong() {

	
	String starttime = format.format(Calendar.getInstance().getTime());
	
	int eventID = 0;
	Document doc = null;
	int maxPaginate = 0;
	maxPaginate = getMaxPagination();
	
	try {
		
		for(int i=1;i<= maxPaginate;i++){
			System.out.println("Page: " + i);
			String shvoongLink = null;
			if(i == 1)
				shvoongLink = shvoong + "/events";
			else
				shvoongLink = shvoong + "/events/page/" + i + "/" + "?cbg_tz=0";
			
			for(int i1=1;i1<5;i1++){
				
				try{
					Thread.sleep(sleeptime);
					//doc = Jsoup.connect(shvoongLink).timeout(3000).get();
					Connection conn  = Jsoup.connect(shvoongLink).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.81 Safari/537.36").timeout(10000);
				       
					 Connection.Response resp = conn.execute();
					
					 if (resp.statusCode() == 200) {
				            doc = conn.get();
				            PrintWriter writer = new PrintWriter("the-file-name.txt", "UTF-8");
				            writer.println(doc);
				            writer.close();
				            Elements imageElements = doc.select("article");
				            if(imageElements.size() != 0){
				            	break;   
				            }
				            else{
				            	continue;
				            }
				        }
					continue;
				}catch(SocketTimeoutException r){
					System.out.println("Got SocketTimeoutException Exception... trying again");
				} catch (InterruptedException e1) {
					System.out.println("Got InterruptedException Exception... trying again");
					e1.printStackTrace();
				}
				catch (IOException e) {
					continue;
				}
			}
			
			
			 Elements imageElements = doc.select("article");
			 String EventHref = "";
			 String EventText = "";
			 String eventType = "";
			 
			 for(Element e:imageElements){
			        Element links = e.getElementsByTag("a").first();
			        EventHref = links.attr("href");
			        EventText = links.attr("title");
			        //System.out.println("Event: " + EventText);
			        //System.out.println("Event ID: " + eventID++);
			        eventType = "";
			        
			        Elements icons = e.getElementsByTag("li");
			       
			        for (Element icon : icons) {
			        		if(icon.hasAttr("title")){
			        			String ff = icon.attr("title");
			        		}
			        		Elements imageData = icon.select("img");
			        		
			        		if(imageData.attr("title").trim().contains("משחה") || imageData.attr("title").trim().contains("שחיה") || imageData.attr("title").trim().contains("שחייה") || imageData.attr("title").trim().equals("ריצה") || imageData.attr("title").trim().contains("אופניים") || imageData.attr("title").trim().contains("טריאתלון"))
			        		{
			        			eventType = imageData.attr("title").trim();
			        			break;
			        		}	       
			        }
			        
			        if(!eventType.equals("")){
			        	//parseShvoongEvent("http://www.shvoong.co.il/events/%D7%98%D7%A8%D7%99%D7%90%D7%AA%D7%9C%D7%95%D7%9F-%D7%A8%D7%90%D7%A9%D7%95%D7%9F-%D7%9C%D7%A6%D7%99%D7%95%D7%9F-2/?cbg_tz=-120","טריאתלון");
			        	parseShvoongEvent(EventHref,eventType);
			        }
			        else{
			        	//System.out.println("No event Type for " + EventHref);
			        }
			      }
		}

	} catch (IOException e) {
		System.out.println("Got Exception!");
		e.printStackTrace();
	}	
}

public static void parseRealtimeEvent(String eventURL, String eventDate){
	
	Event event=new Event();
	Document doc = null;
	boolean foundSubTypes = false;
	
	try {
		doc = Jsoup.connect(eventURL).get();
		
		//System.out.println("Parsing event: " + eventURL);
		
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
		String eventCity = "";
		
		if(eventPlace.length >= 2){
			eventCity = eventPlace[1].trim();
		}
		else{
			eventCity = "אין מידע";
		}
	
		 Element contests = doc.select("table.heat").get(0);

		 Elements rows = contests.getElementsByTag("tr");
		 for(Element row : rows) {
			 
			 try{
				 subType subType = new subType();
				 if(row.hasAttr("onclick")){
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
			         subType.printSubType();
			         event.subtypes.add(subType);
			         foundSubTypes = true;
				 }
			 }
			 catch(IndexOutOfBoundsException e){
				 System.out.println("Exception 1");
				 e.printStackTrace();
			 }
		 }
		 
		 if(foundSubTypes){
			 event.city = eventCity;
			 event.location = eventPlace[1].trim();
			 event.type = getEventType(eventType[1].trim());
			 event.vendor="realtiming";
			 String[] tmp=eventDate.split("-");
			 event.date = tmp[2]+"-"+tmp[1]+"-"+tmp[0];
			 eventsList.add(event);
		 }
		 
		 //event.printEvent();
		 //event.toString();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		System.out.println("Exception 2");
		e.printStackTrace();
	}
	
}
		
public static String getHTTPObj(String url, int retry)
{
	
	try{
	Document doc;
	URL obj = new URL(url);
	HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	
	
	for(int i = 0;i< retry;i++){
		// optional default is GET
		con.setRequestMethod("GET");
	
		//add request header
		String USER_AGENT = "Mozilla/5.0";
		con.setRequestProperty("User-Agent", USER_AGENT);
	
		int responseCode = con.getResponseCode();
		//System.out.println("\nSending 'GET' request to URL : " + url);
		//System.out.println("Response Code : " + responseCode);
		
		if(responseCode != 200)
			getHTTPObj(url,retry--);
		else
			break;
	}

	BufferedReader in = new BufferedReader(
	        new InputStreamReader(con.getInputStream()));
	String inputLine;
	StringBuffer response = new StringBuffer();

	while ((inputLine = in.readLine()) != null) {
		response.append(inputLine);
	}
	in.close();
	return response.toString();
	}
	catch(IOException e){}
	
	return null;	
}

public static void parseShvoongEvent(String eventURL, String eventType) throws FileNotFoundException, UnsupportedEncodingException
{		
		Event event=new Event();
		event.city = "אין מידע";
		event.type = getEventType(eventType); 
		event.vendor="shvoong";
		String registrationLink = null;
		Document doc = null;
		try {
			for(int i=1;i<3;i++){
				Thread.sleep(sleeptime);
				//doc = Jsoup.connect(eventURL).timeout(3000).get();
				if(eventURL != null && !eventURL.equals("")){
					try{
						doc = Jsoup.parse(getHTTPObj(eventURL,i));
					}catch(IllegalArgumentException r){
						
						System.out.println("eventURL: " + eventURL);
						doc = Jsoup.connect(eventURL).timeout(3000).get();
					}
					break;
				}
			}
					
			
			 event.name = doc.getElementsByClass("sub-category-title").text();
			 event.description = doc.getElementsByClass("entry-content").text(); 
			 event.location=doc.getElementsByClass("_location").text();
			 event.date=doc.getElementsByClass("_start").text();
			 if(event.date!=null){
				 try{
					 event.date=event.date.substring(0,event.date.indexOf(" "));
				 }
				 catch(Exception e){}
		     }
			 //get the city name from google maps coordinates
			 Elements iframes = doc.getElementsByTag("iframe");
			 for(Element iframe:iframes){
				 String iframe_src = iframe.attr("src");
				 if(iframe_src.contains("https://maps.google.com") || iframe_src.contains("www.google.co.il/maps")){

					// System.out.println("parsing: " + iframe_src);
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
							 if(event.city.equals("null") || event.city.equals(""))
								 event.city = "אין מידע";
					} catch (ParseException e) {
						event.city = "אין מידע";
						System.out.println("Exception 3");
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
					 //System.out.println("INFO: No additional data exist for this event.");
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
								 if(cost.contains(" ERO"))
									 delimiter = " ERO";
								 
								 
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
									 costEarly = costStrArray[0];
									 costLate  =  costStrArray[0];
								 }

						         subType.distance=distance;
						         subType.price_normal=costEarly;
						         subType.price_late=costLate;
						         //subType.printSubType();
						         event.subtypes.add(subType); 
					 } 
				 }catch(IndexOutOfBoundsException e){
					System.out.println("Event didn't have any data: " + e.getMessage());
				 }
			 eventsList.add(event);
			 event.printEvent();
			 
			 
			 
		} catch (IOException e) {
			System.out.println("Exception 4");
			e.printStackTrace();
		} catch (InterruptedException e1) {
			System.out.println("Exception 5");
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
        	System.out.println("Exception 6");
    		e.printStackTrace();
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
        	System.out.println("Exception 7");
    		e.printStackTrace();
        }


        //now parse
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(json);
        JSONObject jb = (JSONObject) obj;
        
        

        //now read
        JSONArray jsonObject1 = (JSONArray) jb.get("results");
        if(jsonObject1.size() == 0)
        	return "";

        	
        
        JSONObject jsonObject2 = (JSONObject)jsonObject1.get(0);
        JSONArray jsonObject3 = (JSONArray) jsonObject2.get("address_components");
        JSONObject jsonObject4 = (JSONObject)jsonObject3.get(1);
       
        String locationName = (String) jsonObject4.get("long_name");
		return locationName;
	}
	
public static int getMaxPagination()
	{
		int[] pages = new int[20];
		Document doc = null;
		int i=0;
	    int largest = Integer.MIN_VALUE;
		try {
			
				String shvoongLink = shvoong + "/events";
				//doc = Jsoup.connect(shvoongLink).timeout(10000).get();
				Connection conn  = Jsoup.connect(shvoongLink).userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21").timeout(10000);
			       
				 Connection.Response resp = conn.execute();
				
				 if (resp.statusCode() == 200) {
			            doc = conn.get();
			                  
			        }
				 
				Elements paginations = doc.select("div.pagination");
				 for(Element paginate:paginations){
					 //page-numbers
					 Elements links = paginate.getElementsByTag("a");
					 for(Element link:links){
						if ( ! link.getElementsByAttributeValue("class", "page-numbers").text().equals("")){
							pages[i] = Integer.parseInt(link.getElementsByAttributeValue("class", "page-numbers").text());
							i++;
						}
					 }
				 }

		} catch (IOException e) {
			System.out.println("got exception ");
			e.printStackTrace();
		}
		

		
		 for(int j = 0;j<=pages.length;j++) {	
			 if(pages[j] != 0){
		 		if(pages[j] > largest)
		 			largest = pages[j];
			 }
			 else
			 {
				 break;
			 }
		 }
		 
		return largest;
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
		else if(eventType.contains("שחייה ו")){
			return "swimming";
		}
		else if(eventType.equals("טריאתלון")){
			return "triathlon";
		}
		else{
			return  "running";
		}
		
	}

}
