package pickarace;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.JTextPane;
import java.awt.Component;


public class editor {
	
	private JFrame frame;
	private JTextField eventName;
	private JTextField country;
	private JTextField countryCode;
	private JTextField city;
	private JTextField date;
	private JTextField regDate;
	private JTextField regDateLate;
	private JTextField distance1;
	private JTextField link1;
	private JTextField price_reg1;
	private JTextField price_late1;
	private JTextField price_late2;
	private JTextField price_reg2;
	private JTextField link2;
	private JTextField distance2;
	private JTextField price_late3;
	private JTextField price_reg3;
	private JTextField link3;
	private JTextField distance3;
	private JTextField price_late4;
	private JTextField price_reg4;
	private JTextField link4;
	private JTextField distance4;
	private JTextField price_late5;
	private JTextField price_reg5;
	private JTextField link5;
	private JTextField distance5;
	private JTextField price_late6;
	private JTextField price_reg6;
	private JTextField link6;
	private JTextField distance6;
	private JTextField price_late7;
	private JTextField price_reg7;
	private JTextField link7;
	private JTextField distance7;
	private JTextField price_late8;
	private JTextField price_reg8;
	private JTextField link8;
	private JTextField distance8;
	private JComboBox type;
	private JComboBox vendor;
	private JTextPane details;
	private JLabel label_6;
	private JCheckBox subtype1;
	private JCheckBox subtype2;
	private JCheckBox subtype3;
	private JCheckBox subtype4;
	private JCheckBox subtype5;
	private JCheckBox subtype6;
	private JCheckBox subtype7;
	private JCheckBox subtype8;
	private JButton btnUpload;
	private JButton btnNewButton;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				//aws.readContestsFile();
				resetFrame();
			}
		});
	}

	protected static void resetFrame() {
		editor window = new editor();
		window.frame.setVisible(true);
		
	}

	/**
	 * Create the application.
	 */
	public editor() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 800, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
			
		eventName = new JTextField();
		eventName.setBounds(278, 22, 379, 28);
		frame.getContentPane().add(eventName);
		eventName.setColumns(10);
		
		JLabel label = new JLabel("���� ������������");
		label.setBounds(696, 28, 83, 16);
		frame.getContentPane().add(label);
		
		type = new JComboBox();
		type.setModel(new DefaultComboBoxModel(new String[] {"running", "swimming", "biking", "triathlon"}));
		type.setBounds(496, 56, 163, 27);
		frame.getContentPane().add(type);
		
		JLabel label_1 = new JLabel("������");
		label_1.setBounds(698, 60, 81, 16);
		frame.getContentPane().add(label_1);
		
		country = new JTextField();
		country.setText("����������");
		country.setToolTipText("����������");
		country.setBounds(523, 95, 134, 28);
		frame.getContentPane().add(country);
		country.setColumns(10);
		
		JLabel label_2 = new JLabel("����������");
		label_2.setBounds(696, 101, 83, 16);
		frame.getContentPane().add(label_2);
		
		countryCode = new JTextField();
		countryCode.setText("IL");
		countryCode.setToolTipText("������ ����������");
		countryCode.setBounds(394, 95, 105, 28);
		frame.getContentPane().add(countryCode);
		countryCode.setColumns(10);
		
		city = new JTextField();
		city.setToolTipText("������");
		city.setBounds(237, 94, 134, 28);
		frame.getContentPane().add(city);
		city.setColumns(10);
		
		JLabel label_3 = new JLabel("���������� ������������");
		label_3.setBounds(696, 139, 83, 16);
		frame.getContentPane().add(label_3);
		
		date = new JTextField();
		date.setBounds(523, 133, 134, 28);
		frame.getContentPane().add(date);
		date.setColumns(10);
		
		JLabel label_4 = new JLabel("���������� ����������");
		label_4.setBounds(681, 178, 98, 16);
		frame.getContentPane().add(label_4);
		
		regDate = new JTextField();
		regDate.setToolTipText("���������� ����������");
		regDate.setBounds(523, 173, 134, 28);
		frame.getContentPane().add(regDate);
		regDate.setColumns(10);
		
		regDateLate = new JTextField();
		regDateLate.setToolTipText("���������� ����������");
		regDateLate.setBounds(365, 172, 134, 28);
		frame.getContentPane().add(regDateLate);
		regDateLate.setColumns(10);
		
		JButton btnSave = new JButton("Save");
		btnSave.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				
				JSONObject newEvent = createNewObject();
				if(newEvent!=null){
					aws.contestsJsonArray.put(newEvent);
					frame.dispose();
					resetFrame();
				}
				
			}
		});
		btnSave.setBounds(513, 649, 117, 29);
		frame.getContentPane().add(btnSave);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				
				frame.dispose();
				resetFrame();
			}
		});
		btnCancel.setBounds(365, 649, 117, 29);
		frame.getContentPane().add(btnCancel);
		
		distance1 = new JTextField();
		distance1.setText("��������");
		distance1.setBounds(660, 392, 134, 28);
		frame.getContentPane().add(distance1);
		distance1.setColumns(10);
		
		link1 = new JTextField();
		link1.setText("��������");
		link1.setBounds(660, 418, 134, 28);
		frame.getContentPane().add(link1);
		link1.setColumns(10);
		
		price_reg1 = new JTextField();
		price_reg1.setText("��������");
		price_reg1.setBounds(660, 445, 134, 28);
		frame.getContentPane().add(price_reg1);
		price_reg1.setColumns(10);
		
		price_late1 = new JTextField();
		price_late1.setText("�������� ����������");
		price_late1.setBounds(660, 470, 134, 28);
		frame.getContentPane().add(price_late1);
		price_late1.setColumns(10);
		
		vendor = new JComboBox();
		vendor.setModel(new DefaultComboBoxModel(new String[] {"shvoong", "realtiming"}));
		vendor.setBounds(496, 212, 163, 27);
		frame.getContentPane().add(vendor);
		
		JLabel label_5 = new JLabel("������");
		label_5.setBounds(696, 216, 83, 16);
		frame.getContentPane().add(label_5);
		
		subtype1 = new JCheckBox("");
		subtype1.setBounds(710, 368, 40, 23);
		frame.getContentPane().add(subtype1);
		
		subtype2 = new JCheckBox("");
		subtype2.setBounds(564, 368, 40, 23);
		frame.getContentPane().add(subtype2);
		
		price_late2 = new JTextField();
		price_late2.setText("�������� ����������");
		price_late2.setColumns(10);
		price_late2.setBounds(514, 470, 134, 28);
		frame.getContentPane().add(price_late2);
		
		price_reg2 = new JTextField();
		price_reg2.setText("��������");
		price_reg2.setColumns(10);
		price_reg2.setBounds(514, 445, 134, 28);
		frame.getContentPane().add(price_reg2);
		
		link2 = new JTextField();
		link2.setText("��������");
		link2.setColumns(10);
		link2.setBounds(514, 418, 134, 28);
		frame.getContentPane().add(link2);
		
		distance2 = new JTextField();
		distance2.setText("��������");
		distance2.setColumns(10);
		distance2.setBounds(514, 392, 134, 28);
		frame.getContentPane().add(distance2);
		
		subtype3 = new JCheckBox("");
		subtype3.setBounds(418, 368, 40, 23);
		frame.getContentPane().add(subtype3);
		
		price_late3 = new JTextField();
		price_late3.setText("�������� ����������");
		price_late3.setColumns(10);
		price_late3.setBounds(368, 470, 134, 28);
		frame.getContentPane().add(price_late3);
		
		price_reg3 = new JTextField();
		price_reg3.setText("��������");
		price_reg3.setColumns(10);
		price_reg3.setBounds(368, 445, 134, 28);
		frame.getContentPane().add(price_reg3);
		
		link3 = new JTextField();
		link3.setText("��������");
		link3.setColumns(10);
		link3.setBounds(368, 418, 134, 28);
		frame.getContentPane().add(link3);
		
		distance3 = new JTextField();
		distance3.setText("��������");
		distance3.setColumns(10);
		distance3.setBounds(368, 392, 134, 28);
		frame.getContentPane().add(distance3);
		
		subtype4 = new JCheckBox("");
		subtype4.setBounds(272, 368, 40, 23);
		frame.getContentPane().add(subtype4);
		
		price_late4 = new JTextField();
		price_late4.setText("�������� ����������");
		price_late4.setColumns(10);
		price_late4.setBounds(222, 470, 134, 28);
		frame.getContentPane().add(price_late4);
		
		price_reg4 = new JTextField();
		price_reg4.setText("��������");
		price_reg4.setColumns(10);
		price_reg4.setBounds(222, 445, 134, 28);
		frame.getContentPane().add(price_reg4);
		
		link4 = new JTextField();
		link4.setText("��������");
		link4.setColumns(10);
		link4.setBounds(222, 418, 134, 28);
		frame.getContentPane().add(link4);
		
		distance4 = new JTextField();
		distance4.setText("��������");
		distance4.setColumns(10);
		distance4.setBounds(222, 392, 134, 28);
		frame.getContentPane().add(distance4);
		
		subtype5 = new JCheckBox("");
		subtype5.setBounds(710, 510, 40, 23);
		frame.getContentPane().add(subtype5);
		
		price_late5 = new JTextField();
		price_late5.setText("�������� ����������");
		price_late5.setColumns(10);
		price_late5.setBounds(660, 612, 134, 28);
		frame.getContentPane().add(price_late5);
		
		price_reg5 = new JTextField();
		price_reg5.setText("��������");
		price_reg5.setColumns(10);
		price_reg5.setBounds(660, 587, 134, 28);
		frame.getContentPane().add(price_reg5);
		
		link5 = new JTextField();
		link5.setText("��������");
		link5.setColumns(10);
		link5.setBounds(660, 560, 134, 28);
		frame.getContentPane().add(link5);
		
		distance5 = new JTextField();
		distance5.setText("��������");
		distance5.setColumns(10);
		distance5.setBounds(660, 534, 134, 28);
		frame.getContentPane().add(distance5);
		
		subtype6 = new JCheckBox("");
		subtype6.setBounds(561, 510, 40, 23);
		frame.getContentPane().add(subtype6);
		
		price_late6 = new JTextField();
		price_late6.setText("�������� ����������");
		price_late6.setColumns(10);
		price_late6.setBounds(511, 612, 134, 28);
		frame.getContentPane().add(price_late6);
		
		price_reg6 = new JTextField();
		price_reg6.setText("��������");
		price_reg6.setColumns(10);
		price_reg6.setBounds(511, 587, 134, 28);
		frame.getContentPane().add(price_reg6);
		
		link6 = new JTextField();
		link6.setText("��������");
		link6.setColumns(10);
		link6.setBounds(511, 560, 134, 28);
		frame.getContentPane().add(link6);
		
		distance6 = new JTextField();
		distance6.setText("��������");
		distance6.setColumns(10);
		distance6.setBounds(511, 534, 134, 28);
		frame.getContentPane().add(distance6);
		
		subtype7 = new JCheckBox("");
		subtype7.setBounds(413, 510, 40, 23);
		frame.getContentPane().add(subtype7);
		
		price_late7 = new JTextField();
		price_late7.setText("�������� ����������");
		price_late7.setColumns(10);
		price_late7.setBounds(363, 612, 134, 28);
		frame.getContentPane().add(price_late7);
		
		price_reg7 = new JTextField();
		price_reg7.setText("��������");
		price_reg7.setColumns(10);
		price_reg7.setBounds(363, 587, 134, 28);
		frame.getContentPane().add(price_reg7);
		
		link7 = new JTextField();
		link7.setText("��������");
		link7.setColumns(10);
		link7.setBounds(363, 560, 134, 28);
		frame.getContentPane().add(link7);
		
		distance7 = new JTextField();
		distance7.setText("��������");
		distance7.setColumns(10);
		distance7.setBounds(363, 534, 134, 28);
		frame.getContentPane().add(distance7);
		
		subtype8 = new JCheckBox("");
		subtype8.setBounds(272, 510, 40, 23);
		frame.getContentPane().add(subtype8);
		
		price_late8 = new JTextField();
		price_late8.setText("�������� ����������");
		price_late8.setColumns(10);
		price_late8.setBounds(222, 612, 134, 28);
		frame.getContentPane().add(price_late8);
		
		price_reg8 = new JTextField();
		price_reg8.setText("��������");
		price_reg8.setColumns(10);
		price_reg8.setBounds(222, 587, 134, 28);
		frame.getContentPane().add(price_reg8);
		
		link8 = new JTextField();
		link8.setText("��������");
		link8.setColumns(10);
		link8.setBounds(222, 560, 134, 28);
		frame.getContentPane().add(link8);
		
		distance8 = new JTextField();
		distance8.setText("��������");
		distance8.setColumns(10);
		distance8.setBounds(222, 534, 134, 28);
		frame.getContentPane().add(distance8);
		
		details = new JTextPane();
		details.setAlignmentX(Component.RIGHT_ALIGNMENT);
		details.setBounds(249, 245, 408, 111);
		frame.getContentPane().add(details);
		
		label_6 = new JLabel("����������");
		label_6.setBounds(696, 245, 61, 16);
		frame.getContentPane().add(label_6);
		
		btnUpload = new JButton("upload new File");
		btnUpload.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				
				aws.uploadNewFile();
			}
		});
		btnUpload.setBounds(6, 649, 163, 29);
		frame.getContentPane().add(btnUpload);
		
		btnNewButton = new JButton("Get Data");
		btnNewButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				HTMLParser.getShvoong();
				//HTMLParser.getRealTiming();
			}
		});
		btnNewButton.setBounds(6, 608, 163, 29);
		frame.getContentPane().add(btnNewButton);
	}

	protected JSONObject createNewObject() {
		JSONObject enwEventObject=new JSONObject();
		try {
			enwEventObject.put("id","\""+System.currentTimeMillis()+"\"");
			enwEventObject.put("name",eventName.getText());
			enwEventObject.put("status","active");
			enwEventObject.put("type",type.getSelectedItem().toString().toLowerCase());
			enwEventObject.put("date",date.getText());
			enwEventObject.put("registration_date_normal",regDate.getText());
			enwEventObject.put("registration_date_late",regDateLate.getText());
			
			JSONObject location=new JSONObject();
			location.put("country", country.getText());
			location.put("countryCode",countryCode.getText());
			location.put("city",city.getText());
			enwEventObject.put("location",location);
			
			JSONObject detailsObj=new JSONObject();
			detailsObj.put("row1", details.getText());
			enwEventObject.put("details", detailsObj);
			
			JSONObject vendorObj=new JSONObject();
			vendorObj.put("name", vendor.getSelectedItem().toString().toLowerCase());
			enwEventObject.put("vendor", vendorObj);
			
			JSONArray subtypes=new JSONArray();
			if(subtype1.isSelected()){
				subtypes.put(getsubTypeObject(subtype1,distance1,link1,price_reg1,price_late1));
			}
			if(subtype2.isSelected()){
				subtypes.put(getsubTypeObject(subtype2,distance2,link2,price_reg2,price_late2));
			}
			if(subtype3.isSelected()){
				subtypes.put(getsubTypeObject(subtype3,distance3,link3,price_reg3,price_late3));
			}
			if(subtype4.isSelected()){
				subtypes.put(getsubTypeObject(subtype4,distance4,link4,price_reg4,price_late4));
			}
			if(subtype5.isSelected()){
				subtypes.put(getsubTypeObject(subtype5,distance5,link5,price_reg5,price_late5));
			}
			if(subtype6.isSelected()){
				subtypes.put(getsubTypeObject(subtype6,distance6,link6,price_reg6,price_late6));
			}
			if(subtype7.isSelected()){
				subtypes.put(getsubTypeObject(subtype7,distance7,link7,price_reg7,price_late7));
			}
			if(subtype8.isSelected()){
				subtypes.put(getsubTypeObject(subtype8,distance8,link8,price_reg8,price_late8));
			}
			enwEventObject.put("subtype", subtypes);
		
			
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return enwEventObject;
	}

	private JSONObject getsubTypeObject(JCheckBox subtype,JTextField distance, JTextField link, JTextField price_reg,JTextField price_late) {
		JSONObject subType=new JSONObject();
		try{
			subType.put("distance", distance.getText());
			subType.put("price_normal", price_reg.getText());
			subType.put("price_late", price_late.getText());
			subType.put("link", link.getText());
		}catch(Exception e){}
		
		return subType;
	}
}
