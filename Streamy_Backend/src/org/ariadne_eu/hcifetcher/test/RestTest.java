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

public class RestTest {
	public static void main(String[] args) {

		// testExport();
		testImport();
		
		//exportRssFeed();
		// testAddLikePost();

	}

	private static void exportRssFeed() {
		try {
			// String url =
			// "http://ariadneproject.appspot.com/api/entity?f=export";
			String url = "http://hcifetcher.appspot.com/api/entity?f=export&ent=RssFeed";
			// String url = "http://localhost:8080/api/entity?f=export";
			String json = RestUtils.getInstance().getUrlAsString(new URL(url), Charset.forName("UTF-8"));

			IOUtilsv2.writeStringToFileInEncodingUTF8(json, "backup/export-rssfeed.json");

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void testExport() {
		try {
			// String url =
			// "http://ariadneproject.appspot.com/api/entity?f=export";
			String url = "http://hcifetcher.appspot.com/api/entity?f=export";
			// String url = "http://localhost:8080/api/entity?f=export";
			String json = RestUtils.getInstance().getUrlAsString(new URL(url), Charset.forName("UTF-8"));

			IOUtilsv2.writeStringToFileInEncodingUTF8(json, "backup/export.json");

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void testAddLikePost() {
		try {

			// String url = "http://localhost:8888/api/entries/addLike";
			String url = "http://ariadneproject.appspot.com/api/entries/addLike";

			HttpClient client = new HttpClient();
			String test = "{userid:\"thepostid\"," + "likeid:\"thelikeid\"}";

			PostMethod method = new PostMethod(url);
			method.addParameter("like", test);
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

	private static void testImport() {
		try {
			// String url = "http://localhost:8080/api/entity/import";
			String url = "http://hcifetcher.appspot.com/api/entity/import";

			HttpClient client = new HttpClient();
			String jsonStr = IOUtilsv2.readStringFromFile(new File("backup/export.json"), "UTF-8");
			PostMethod method = new UTF8PostMethod(url);
			method.addParameter("import", jsonStr);

			/**
			 * CHANGE THE SECRET
			 */
			method.addParameter("secret", "MyS3cr3t");

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
