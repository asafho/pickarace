package pickarace;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;
import org.jets3t.service.S3Service;
import org.jets3t.service.ServiceException;
import org.jets3t.service.impl.rest.httpclient.RestS3Service;
import org.jets3t.service.model.S3Object;
import org.jets3t.service.security.AWSCredentials;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class aws {

	private static String awsAccessKey="AKIAIEDJH4MYJN364IBQ";
	private static String awsSecretKey;
	private static S3Service s3Service;
	public static JSONArray contestsJsonArray = new JSONArray();
	
	public static void awsConnect()
	{
		if(awsSecretKey==null)
		{ 
			awsSecretKey=JOptionPane.showInputDialog(awsAccessKey);
		}
		AWSCredentials awsCredentials=new AWSCredentials(awsAccessKey, awsSecretKey);
		try {
			s3Service = new RestS3Service(awsCredentials);
		} catch (Exception e) {
			awsConnect();
			e.printStackTrace();
			awsSecretKey="";
		}
	}
	
	
	public static void readContestsFile() {
		
	
			if(s3Service==null){
				awsConnect();
			}

			try {
				S3Object object=s3Service.getObject("com.pickarace.app", "contests.json");
				BufferedReader bReader = new BufferedReader(new InputStreamReader(object.getDataInputStream(),"UTF8"));
				String result= "";
				String line= "";
				while ((line = bReader.readLine()) != null) {
				    result += line + "\n";
				}				
				JSONObject obj = new JSONObject(result);
				contestsJsonArray=obj.getJSONArray("events");
				
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}


	public static void uploadNewFile() {
		
	/*
		if(s3Service==null)
		{
			awsConnect();
		}
*/
		pushEventsToJsonArray();
		
		
		try{
			JSONObject newFile=new JSONObject();
			contestsJsonArray=getSortedList(contestsJsonArray);
			newFile.put("events", contestsJsonArray);
			String jsonFileContent = newFile.toString(1).replaceAll("\": \"", "\":\"");

			
			FileUtils.writeStringToFile(new File("contests.json"), jsonFileContent,"UTF-8");
			
	/*		S3Object stringObject = new S3Object("contests_backup.json", fileContent);
			stringObject.setAcl(AccessControlList.REST_CANNED_PUBLIC_READ);
			s3Service.putObject("com.pickarace.app/contests_backup.json",stringObject);
	*/	} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		
	}


	private static void pushEventsToJsonArray() {
		
		try {
		for(int i=0;i<HTMLParser.eventsList.size();i++){
			JSONObject json=new JSONObject();
			Event ev = HTMLParser.eventsList.get(i);
			json.put("id",System.currentTimeMillis()+"");
			json.put("name",ev.name);
			json.put("status","active");
			json.put("type",ev.type);
			json.put("date",ev.date);
			json.put("registration_date_normal",ev.registration_date_normal);
			json.put("registration_date_late",ev.registration_date_late);
			
			JSONObject location=new JSONObject();
			location.put("country", ev.country);
			location.put("countryCode",ev.countryCode);
			location.put("city",ev.city+"");
			json.put("location",location);
			
			JSONObject detailsObj=new JSONObject();
			detailsObj.put("row1", ev.description);
			json.put("details", detailsObj);
			
			JSONObject vendorObj=new JSONObject();
			vendorObj.put("name", ev.vendor);
			json.put("vendor", vendorObj);
			
			JSONArray subtypes=new JSONArray();
			for(int s=0;s<ev.subtypes.size();s++){
				JSONObject st=new JSONObject();
				
				st.put("distance", ev.subtypes.get(s).distance);
				st.put("price_normal", ev.subtypes.get(s).price_normal);
				st.put("price_late", ev.subtypes.get(s).price_late);
				st.put("link", ev.subtypes.get(s).link);
				subtypes.put(st);
			}
			json.put("subtype", subtypes);
			contestsJsonArray.put(json);
		}	
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static JSONArray getSortedList(JSONArray array) throws JSONException {
	    List<JSONObject> list = new ArrayList<JSONObject>();
	    for (int i = 0; i < array.length(); i++) {
	            list.add(array.getJSONObject(i));
	    }
	    Collections.sort(list, new sortByDates());
	
	    JSONArray resultArray = new JSONArray(list);
	
	    return resultArray;

	}
}
class sortByDates implements Comparator<JSONObject> {
	
	public int compare(JSONObject obj1, JSONObject obj2) {
	    try {
	   	 DateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
	     Date date1 = dateformat.parse(obj1.getString("date"));
	     Date date2 = dateformat.parse(obj2.getString("date"));
	     int res=date1.compareTo(date2);
	     return res > 0 ? 1 : res<0 ? -1 :0;
	        			
	    } catch (JSONException e) {
	        e.printStackTrace();
	    } catch (ParseException e) {
			e.printStackTrace();
		}
	    return 0;
	
	}

}
