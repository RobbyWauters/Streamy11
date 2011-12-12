package org.ariadne_eu.hcifetcher.test;

import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONtest {
	
	static String test = "{result:\"Like theblogid from user myuserID succefully added.\"," +
			"myresult:\"test2\"}";
	public static void main(String[] args) {
		try {
			JSONObject jsonObject = new JSONObject(test);
			ByteArrayOutputStream sos = new ByteArrayOutputStream();
			XMLEncoder e = new XMLEncoder(sos);
				e.writeObject(jsonObject);
				e.close();
			System.out.println(sos.toString());
			System.out.println(jsonObject.get("myresult"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
