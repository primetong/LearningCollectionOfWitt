package com.xinghaicom.web;

import android.util.Base64;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import javax.xml.parsers.ParserConfigurationException;
 

public class WebClient {
	private static final String TAG = "WebClient";
	private DefaultHttpClient httpClient;
	private HttpGet httpGet;
	private HttpPost httpPost;
//	private HttpPut httpPut;
//	private HttpDelete httpDelete;
	
	protected final int DEFAULT_CONNECTION_TIMEOUT = 30000;
	protected final int DEFAULT_SO_TIMEOUT = 30000;
		
	public WebClient(String initServiceMethodURI){
		
		httpClient = new DefaultHttpClient();		
		HttpParams httpParams = httpClient.getParams();
		httpParams.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, DEFAULT_CONNECTION_TIMEOUT);
		httpParams.setParameter(CoreConnectionPNames.SO_TIMEOUT, DEFAULT_SO_TIMEOUT);
		
		httpPost = new HttpPost(initServiceMethodURI);
		httpGet = new HttpGet(initServiceMethodURI);
	}
	
	public WebClient(String initServiceMethodURI,int connectionTimeout,int soTimeout){
		
		httpClient = new DefaultHttpClient();
		HttpParams httpParams = httpClient.getParams();
		httpParams.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, connectionTimeout);
		httpParams.setParameter(CoreConnectionPNames.SO_TIMEOUT, soTimeout);
		
		httpPost = new HttpPost(initServiceMethodURI);
		httpGet = new HttpGet(initServiceMethodURI);
		
	}
		
	public void setServiceURI(String serviceURI) throws URISyntaxException{		
		httpPost.setURI(new URI(serviceURI));
		httpGet.setURI(new URI(serviceURI));
	}
	
	public String execute(List<BasicNameValuePair> params) throws ParseException, ParserConfigurationException, IOException, XmlPullParserException{
		List<String> results = Execute(params);
		if(results == null || results.size() <= 0) return null;
		return results.get(0);
	}
		
	public List<String> Execute(List<BasicNameValuePair> params) throws ParserConfigurationException, ParseException, IOException, XmlPullParserException {
														
		httpPost.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));		
		HttpResponse httpResponse = httpClient.execute(httpPost);
						
		if (httpResponse.getStatusLine().getStatusCode() != 404) {
			
			List<String> stringsResult = new ArrayList<String>();
	
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);								
			XmlPullParser parser = factory.newPullParser();
			HttpEntity responseEntity = httpResponse.getEntity();
			if(responseEntity == null || responseEntity.getContentLength() < 0) return null;
			parser.setInput(new InputStreamReader(responseEntity.getContent()));
						
			int eventType = parser.getEventType();			
			while (eventType != XmlPullParser.END_DOCUMENT) {
				
				if(eventType == XmlPullParser.TEXT){
					stringsResult.add(parser.getText());
				}
												
				eventType = parser.next();
			}
			
			responseEntity.consumeContent();
			
			return stringsResult;
		}
		
		return null;
	}
		
	public JSONObject Execute(JSONObject param) throws ClientProtocolException, IOException, JSONException{
		
		if(param == null) throw new NullPointerException("参数无效！");
		
//		httpPost.setEntity(null);
		httpPost.setHeader("Content-Type", "application/json");
		httpPost.setEntity(new StringEntity(param.toString()));
		HttpResponse httpResponse = httpClient.execute(httpPost);		
		if(httpResponse == null || httpResponse.getStatusLine().getStatusCode() == 404) return null;
		
		HttpEntity responseEntity = httpResponse.getEntity();
		if(responseEntity == null) return null; 
		
		BufferedReader responseBufferedReader = new BufferedReader(new InputStreamReader(responseEntity.getContent()));
		String stringResult = null;
		StringBuffer bufferResult = new StringBuffer();
		while((stringResult = responseBufferedReader.readLine()) != null){			
			bufferResult.append(stringResult);
		}
		
		JSONObject jsonResult = new JSONObject(bufferResult.toString());
		
		responseEntity.consumeContent();
		
		return 	jsonResult;	
	}
	
	public byte[] execute(byte[] param) throws ClientProtocolException, IOException{
		
		if(param == null || param.length <= 0) throw new IllegalArgumentException("参数无效！");
		
		httpPost.setEntity(new ByteArrayEntity(param));
		HttpResponse httpResponse = httpClient.execute(httpPost);
		if(httpResponse == null || httpResponse.getStatusLine().getStatusCode() == 404) return null;
		
		HttpEntity responseEntity = httpResponse.getEntity();
		if(responseEntity == null) return null;
		
		int responseContentLength = (int) responseEntity.getContentLength();
		if(responseContentLength < 0) return null;
		
		byte[] bytesResult = new byte[responseContentLength];
		
		InputStream responseStream = responseEntity.getContent();
		int readCount = 0;
		while(readCount >= 0 && readCount < responseContentLength){
			readCount += responseStream.read(bytesResult, readCount, responseContentLength - readCount);
		}
		
		responseEntity.consumeContent();
		
		return bytesResult;		
	}
		
	public InputStream executeForStream(List<BasicNameValuePair> params) throws ClientProtocolException, IOException{
		httpPost.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));		
		HttpResponse httpResponse = httpClient.execute(httpPost);
		if(httpResponse == null || httpResponse.getStatusLine().getStatusCode() == 404) return null;

		HttpEntity responseEntity = httpResponse.getEntity();
		if(responseEntity == null) return null;			
		InputStream responseStream = responseEntity.getContent();			
		return responseStream;
	}
	
	public byte[] executeForBytes(List<BasicNameValuePair> params) throws ClientProtocolException, IOException, ParseException, ParserConfigurationException, XmlPullParserException{
		
		List<String> bytesStrings = Execute(params);
		if(bytesStrings == null || bytesStrings.size() <= 0) return null;
		
		String bytesString = bytesStrings.get(0);
		if(bytesString == null || bytesString.length() <= 0) return null;
				
		return Base64.decode(bytesString, Base64.DEFAULT);				
	}
	
	
	
	public String get() throws ClientProtocolException, IOException, XmlPullParserException{
		HttpResponse httpResponse = httpClient.execute(httpGet);
				
		if (httpResponse.getStatusLine().getStatusCode() != 404) {
			
			String stringsResult = "";
	
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);								
			XmlPullParser parser = factory.newPullParser();
			HttpEntity responseEntity = httpResponse.getEntity();
			if(responseEntity == null || responseEntity.getContentLength() < 0) return null;
			parser.setInput(new InputStreamReader(responseEntity.getContent()));
						
			int eventType = parser.getEventType();			
			while (eventType != XmlPullParser.END_DOCUMENT) {
				
				if(eventType == XmlPullParser.TEXT){
					stringsResult = parser.getText();
					break;
				}
				
				eventType = parser.next();
			}
						
			responseEntity.consumeContent();
			
			return stringsResult;
		}
		
		return null;
		
		 
	}
	
	/**
	 * get方法提交数据
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws JSONException
	 */
	public JSONObject executeByGetForJsonWithGZip() throws ClientProtocolException, IOException, JSONException{
		
		httpGet.setHeader("Accept-Encoding", "gzip");
		HttpResponse httpResponse = httpClient.execute(httpGet);
		if (httpResponse == null
				|| httpResponse.getStatusLine().getStatusCode() == 404)
			return null;

		HttpEntity responseEntity = httpResponse.getEntity();
		if (responseEntity == null)
			return null;
				
		GZIPInputStream gzipIn = new GZIPInputStream(responseEntity.getContent());
		
		BufferedReader responseBufferedReader = new BufferedReader(
				new InputStreamReader(gzipIn));
		String stringResult = null;
		StringBuffer bufferResult = new StringBuffer();
		while ((stringResult = responseBufferedReader.readLine()) != null) {
			bufferResult.append(stringResult);
		}

		JSONObject jsonResult = new JSONObject(bufferResult.toString());

		responseEntity.consumeContent();

		return jsonResult;
	}
	/**
	 *  post方法提交数据
	 * @param data
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws JSONException
	 */
	boolean bIsEnctry=false;
	public JSONObject executeByPostForJsonWithGZipAndEntrpyt(List<NameValuePair> params,int version, String username, String method) throws Exception{
		
		bIsEnctry=true;
		StringBuilder asb=new StringBuilder();
		for(int iIndex=0;iIndex< params.size();iIndex++)
		{			
			NameValuePair aNameValuePair=params.get(iIndex);
			asb.append(aNameValuePair.getName());
			asb.append("=");
			asb.append(URLEncoder.encode(aNameValuePair.getValue(), "utf-8"));
			if(iIndex<params.size()-1)
			{
				asb.append("&");
			}
		}
		String sdata=asb.toString();
		List<NameValuePair> newparams = new ArrayList<NameValuePair>(); 
		if(method.equals("Login") || method.equals("GetVerifyCode") || method.equals("GetPwd")){
			newparams.addAll(params);
			httpPost.setEntity(new UrlEncodedFormEntity(newparams,HTTP.UTF_8));
		
		}else {
			httpPost.setEntity(new StringEntity(sdata,HTTP.UTF_8));
		}
		
		return executeByPostWithGZip(); 
		 
	}
	public JSONObject executeByPostForJsonWithGZip(List<NameValuePair> params) throws ClientProtocolException, IOException, JSONException{
		bIsEnctry=false;
		httpPost.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
		return executeByPostWithGZip();
	}
	public JSONObject executeByPostWithGZip() throws ClientProtocolException, IOException, JSONException{

		httpPost.setHeader("Accept-Encoding", "*");
		HttpResponse httpResponse = httpClient.execute(httpPost);
		if (httpResponse == null
				|| httpResponse.getStatusLine().getStatusCode() == 404)
			return null;
		HttpEntity responseEntity = httpResponse.getEntity();
		if (responseEntity == null)
			return null;
//		GZIPInputStream gzipIn = new GZIPInputStream(responseEntity.getContent());
		BufferedReader responseBufferedReader = new BufferedReader(
				new InputStreamReader(responseEntity.getContent()));
		String stringResult = null;
		StringBuffer bufferResult = new StringBuffer();
		while ((stringResult = responseBufferedReader.readLine()) != null) {
			bufferResult.append(stringResult);
		}
//		if(bIsEnctry){
//			MD5 md5 = new MD5();
//			String trueContent = null;
//			try {
//				trueContent = md5.hashToDigestDes(bufferResult.toString());
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			JSONObject jsonResult = new JSONObject(trueContent);
//			responseEntity.consumeContent();
//			return jsonResult;
//		}
		JSONObject jsonResult = new JSONObject(bufferResult.toString());
		responseEntity.consumeContent();

		return jsonResult;
	}
	
	

	public InputStream getForStream() throws ClientProtocolException, IOException{		
		HttpResponse httpResponse = httpClient.execute(httpGet);
		if(httpResponse == null || httpResponse.getStatusLine().getStatusCode() == 404) return null;

		HttpEntity responseEntity = httpResponse.getEntity();
		if(responseEntity == null) return null;			
		InputStream responseStream = responseEntity.getContent();			
		return responseStream;				
	}
	
	public void release(){
		if(httpPost != null){
			httpPost.abort();
			httpPost = null;
		}
		
		if(httpGet != null){
			httpGet.abort();
			httpGet = null;
		}
		
		if(httpClient != null){
			httpClient.getConnectionManager().shutdown();
			httpClient = null;
		}
	}
}
