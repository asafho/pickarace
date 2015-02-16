package pickarace;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;

import javax.swing.JOptionPane;

import org.jets3t.service.S3Service;
import org.jets3t.service.S3ServiceException;
import org.jets3t.service.ServiceException;
import org.jets3t.service.acl.AccessControlList;
import org.jets3t.service.impl.rest.httpclient.RestS3Service;
import org.jets3t.service.model.S3Object;
import org.jets3t.service.security.AWSCredentials;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class aws {

	private static String awsAccessKey="AKIAJUJTYGFHBOPKA37Q";
	private static String awsSecretKey="";
	private static S3Service s3Service;
	public static JSONArray contestsJsonArray;
	
	
	public static void awsConnect()
	{
		String tmp=JOptionPane.showInputDialog(awsAccessKey,"insert correct secret key");
		try {
			AWSCredentials awsCredentials=new AWSCredentials(awsAccessKey, awsSecretKey);
			s3Service = new RestS3Service(awsCredentials);
			awsSecretKey=tmp;
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
				S3Object object=s3Service.getObject("pickarace", "contests_backup.json");
				BufferedReader bReader = new BufferedReader(new InputStreamReader(object.getDataInputStream()));
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
		if(s3Service==null)
		{
			awsConnect();
		}

		try{
			JSONObject newFile=new JSONObject();
			newFile.put("events", contestsJsonArray);
			String fileContent = newFile.toString(1).replaceAll("\": \"", "\":\"");
			S3Object stringObject = new S3Object("contests_backup.json", fileContent);
			stringObject.setAcl(AccessControlList.REST_CANNED_PUBLIC_READ);
			s3Service.putObject("pickarace/contests_backup.json",stringObject);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (S3ServiceException e) {
			e.printStackTrace();
		}
		
	}	
}
