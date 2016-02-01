package com.wxxiaomi.teachingoffice2.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ProtocolException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.RedirectHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import com.wxxiaomi.teachingoffice2.ConstantValue;
import com.wxxiaomi.teachingoffice2.bean.net.NetReceiverData;
import com.wxxiaomi.teachingoffice2.bean.net.NetSendData;




public class HttpUtils {
	public static NetReceiverData sendGet(NetSendData sendData){
		NetReceiverData returnData = new NetReceiverData();
		try {
			HttpClient client=new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(sendData.getUrl());
			if(sendData.getHeaders()!=null&&!sendData.getHeaders().isEmpty()){
				for(Map.Entry<String, String> entry:sendData.getHeaders().entrySet()){
					httpGet.addHeader(entry.getKey(), entry.getValue());
				}
			}
			HttpResponse httpResponse= client.execute(httpGet);
			Header[] allHeaders = httpResponse.getAllHeaders();
			if(allHeaders != null){
				for(Header header : allHeaders){
					returnData.getHeaders().put(header.getName(), header.getValue());
				}
			}
			
			if(httpResponse.getStatusLine().getStatusCode()==200)
			{
//				return EntityUtils.toString(httpResponse.getEntity());
				returnData.setContent(EntityUtils.toByteArray(httpResponse.getEntity()));
//				Log.i("wang", new String(returnData.getContent(),"gb2312"));
				returnData.setStateCode(200);
				returnData.setFromUrl(sendData.getUrl());
			}
		}catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		return returnData;
	}
	
	public static NetReceiverData sendPost(NetSendData sendData){
		NetReceiverData returnData = new NetReceiverData();
		DefaultHttpClient client=new DefaultHttpClient();
		client.setRedirectHandler(new RedirectHandler() {
			@Override
			public boolean isRedirectRequested(HttpResponse arg0, HttpContext arg1) {
				return false;
			}
			@Override
			public URI getLocationURI(HttpResponse arg0, HttpContext arg1)
					throws ProtocolException {
				return null;
			}
		});
		List<NameValuePair> list=new ArrayList<NameValuePair>();
		if(sendData.getParmars()!=null&&!sendData.getParmars().isEmpty())
		{
			for(Map.Entry<String, String> entry:sendData.getParmars().entrySet())
			{
				list.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
		}
		try {
			UrlEncodedFormEntity uefEntity=new UrlEncodedFormEntity(list, "gb2312");
			HttpPost httpPost=new HttpPost(sendData.getUrl());
			httpPost.setEntity(uefEntity);
			if(sendData.getHeaders()!=null&&!sendData.getHeaders().isEmpty()){
				for(Map.Entry<String, String> entry:sendData.getHeaders().entrySet()){
					httpPost.addHeader(entry.getKey(), entry.getValue());
				}
			}
//			httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse httpResponse= client.execute(httpPost);
			if(httpResponse.getStatusLine().getStatusCode()==200)
			{
				returnData.setFromUrl(sendData.getUrl());
				returnData.setContent(EntityUtils.toByteArray(httpResponse.getEntity()));
				returnData.setStateCode(200);
			}else if(httpResponse.getStatusLine().getStatusCode() == 302){
				NetSendData Data302 = new NetSendData();
				Data302.getHeaders().put("Referer", sendData.getUrl());
				if(sendData.getHeaders().containsKey("Cookie")){
					Data302.getHeaders().put("Cookie", sendData.getHeaders().get("Cookie"));
				}
				String temp = httpResponse.getFirstHeader("Location").getValue();
//				if(sendData.isLogin()){
//					ConstantValue.tempUrl = temp.substring(temp.indexOf("("),
//							temp.indexOf(")") + 2);
//				}
				Data302.setUrl(ConstantValue.Host + temp);
				return sendGet(Data302);
//				returnData.setLocation(temp);
//				String url = ConstantValue.Host + temp;
//				Map<String,String> heas = new HashMap<String, String>();
//				heas.put("Referer", path);
//				return sendGet(url, heas, "utf-8");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnData;
		
	}
	
	
}
