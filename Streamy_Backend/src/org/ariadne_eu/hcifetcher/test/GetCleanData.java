package org.ariadne_eu.hcifetcher.test;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.UTF8PostMethod;
import org.ariadne.util.IOUtilsv2;
import org.ariadne_eu.hcifetcher.rest.RestUtils;

public class GetCleanData {
	
	public static void main(String[] args) {
		
		//export();
		importData();
	}

	
	private static void export() {
		try {
			
			File backup = new File("backup");
			if(!backup.exists() || !backup.isDirectory()){
				backup.mkdirs();
			}
			
			String url = "http://hcifetcher.appspot.com/api/entity?f=export";
			String json = RestUtils.getInstance().getUrlAsString(new URL(url), Charset.forName("UTF-8"));
			IOUtilsv2.writeStringToFileInEncodingUTF8(json, "backup/export.json");
			System.out.println("Export to export.json successful");
			
			url = "http://hcifetcher.appspot.com/api/entity?f=export&ent=RssFeed";
			json = RestUtils.getInstance().getUrlAsString(new URL(url), Charset.forName("UTF-8"));
			IOUtilsv2.writeStringToFileInEncodingUTF8(json, "backup/export-feeds.json");
			
			System.out.println("Export to export-feeds.json successful");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void importData() {
		try {
			/**
			 * Change url to your application
			 */
			String url = "http://streamy11.appspot.com/api/entity/import";
			/**
			 * CHANGE THE SECRET
			 */
			String secret = "StReAmY";
			
			HttpClient client = new HttpClient();
			/*String jsonStr = IOUtilsv2.readStringFromFile(new File("backup/export-feeds.json"), "UTF-8");
			PostMethod method = new UTF8PostMethod(url);
			method.addParameter("import", jsonStr);
			method.addParameter("secret", secret);
			int returnCode = client.executeMethod(method);
			System.out.println(method.getResponseBodyAsString());*/
			System.out.println("import export.json");
			String jsonStr = IOUtilsv2.readStringFromFile(new File("backup/export.json"), "UTF-8");
			System.out.println("jsonFile - chars: "+jsonStr.length());
			PostMethod method = new UTF8PostMethod(url);
			method.addParameter("import", jsonStr);
			method.addParameter("secret", secret);
			int returnCode = client.executeMethod(method);
			System.out.println(method.getResponseBodyAsString());
			
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
