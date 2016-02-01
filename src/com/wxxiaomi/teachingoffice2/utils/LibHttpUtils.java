package com.wxxiaomi.teachingoffice2.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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


import android.util.Log;

import com.wxxiaomi.teachingoffice2.bean.net.NetReceiverData;
import com.wxxiaomi.teachingoffice2.bean.net.NetSendData;


public class LibHttpUtils {
	public static NetReceiverData sendGet(NetSendData sendData){
		NetReceiverData returnData = new NetReceiverData();
		try {
			HttpClient client=new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(sendData.getUrl());
			Log.i("wang", "get目标url:"+sendData.getUrl());
			if(sendData.getHeaders()!=null&&!sendData.getHeaders().isEmpty()){
				for(Map.Entry<String, String> entry:sendData.getHeaders().entrySet()){
					httpGet.addHeader(entry.getKey(), entry.getValue());
					Log.i("wang", "get_header="+entry.getKey()+":"+entry.getValue());
					
				}
			}
			
			
//			httpGet.addHeader("Referer",sendData.getUrl());
			HttpResponse httpResponse= client.execute(httpGet);
			String cookie = "";
			if(httpResponse.getFirstHeader("Set-Cookie")!=null){
				cookie = httpResponse.getFirstHeader("Set-Cookie").getValue();
			}
//			Log.i("wang", "cookie="+(sendData.getHeaders().get("Cookie")==null?"":sendData.getHeaders().get("Cookie")+";")+cookie);
			returnData.getHeaders().put("Cookie", (sendData.getHeaders().get("Cookie")==null?"":sendData.getHeaders().get("Cookie")+";")+cookie);
//			Header[] allHeaders = httpResponse.getAllHeaders();
//			if(allHeaders != null){
//				for(Header header : allHeaders){
//					returnData.getHeaders().put(header.getName(), header.getValue());
//					Log.i("wang", "header="+header.getName()+":"+header.getValue());
//				}
//			}
			
			if(httpResponse.getStatusLine().getStatusCode()==200)
			{
//				return EntityUtils.toString(httpResponse.getEntity());
				returnData.setContent(EntityUtils.toByteArray(httpResponse.getEntity()));
//				returnData.setStateCode(200);
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
//		Log.i("wang", "post目标url:"+sendData.getUrl());
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
//				Log.i("wang", "post-pars:"+entry.getKey()+":"+entry.getValue());
			}
		}
		
		try {
			UrlEncodedFormEntity uefEntity=new UrlEncodedFormEntity(list, "utf-8");
			HttpPost httpPost=new HttpPost(sendData.getUrl());
			httpPost.setEntity(uefEntity);
			if(sendData.getHeaders()!=null&&!sendData.getHeaders().isEmpty()){
				for(Map.Entry<String, String> entry:sendData.getHeaders().entrySet()){
					httpPost.addHeader(entry.getKey(), entry.getValue());
//					Log.i("wang", "post_header="+entry.getKey()+":"+entry.getValue());
				}
			}
			
			HttpResponse httpResponse= client.execute(httpPost);
//			Header[] allHeaders = httpResponse.getAllHeaders();
//			for(Header header : allHeaders){
//				Log.i("wang", "post回来的header:"+header.getName()+":"+header.getValue());
//			}
			String cookie = httpResponse.getFirstHeader("Set-Cookie")==null?"":httpResponse.getFirstHeader("Set-Cookie").getValue();
//			Log.i("wang", "sendpost-cookie="+cookie);
//			Log.i("wang", "sendpost-sendData.getHeaders().get(Cookie)="+sendData.getHeaders().get("Cookie"));
			if(httpResponse.getStatusLine().getStatusCode()==200)
			{
				returnData.getHeaders().put("Cookie", sendData.getHeaders().get("Cookie")+";"+cookie);
				returnData.setFromUrl(sendData.getUrl());
				returnData.setContent(EntityUtils.toByteArray(httpResponse.getEntity()));
//				returnData.setStateCode(200);
			}else if(httpResponse.getStatusLine().getStatusCode() == 302){
				NetSendData Data302 = new NetSendData();
				Data302.getHeaders().put("Referer", sendData.getUrl());
//				Data302.getHeaders().put("Cookie", cookie);
//				String cookies = sendData.getHeaders().get("Cookie")+";"+cookie;
//				String[] split = cookies.split(";");
//				sendData.getHeaders().put("Cookie", sendData.getHeaders().get("Cookie")+";"+cookie);
				Data302.getHeaders().put("Cookie", sendData.getHeaders().get("Cookie")+";"+cookie);
//				Data302.getHeaders().put("Cookie", sendData.getHeaders().get("Cookie"));
//				Data302.getHeaders().put("Cookie", split[0]+"; "+split[3]);
				String temp = httpResponse.getFirstHeader("Location").getValue();
//				if(sendData.isLogin()){
//					ConstantValue.tempUrl = temp.substring(temp.indexOf("("),
//							temp.indexOf(")") + 2);
//				}
//				Data302.setUrl(ConstantValue.Host + temp);
				Data302.setUrl("http://210.38.162.2/" + temp);
//				Log.i("wang", "post-get目标url:"+Data302.getUrl());
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
	
	/**
	 * 发送Http请求到Web站点
	 * @param path Web站点请求地址
	 * @param map Http请求参数
	 * @param encode 编码格式
	 * @return Web站点响应的字符串
	 */
//	public static String sendPost(String path,Map<String, String> parmars,Map<String,String> headers,String encode,boolean isLogin)
//	{
//		DefaultHttpClient client=new DefaultHttpClient();
////		RedirectHandler redirectHandler=new RedirectHandler();
//		client.setRedirectHandler(new RedirectHandler() {
//			@Override
//			public bo.0a6n isRedirectRequested(HttpResponse arg0, HttpContext arg1) {
//				// TODO Auto-generated method stub
//				return false;
//			}
//			@Override
//			public URI getLocationURI(HttpResponse arg0, HttpContext arg1)
//					throws ProtocolException {
//				// TODO Auto-generated method stub
//				return null;
//			}
//		});
////		HttpClient client=AndroidHttpClient.newInstance("");
//		List<NameValuePair> list=new ArrayList<NameValuePair>();
//		if(parmars!=null&&!parmars.isEmpty())
//		{
//			for(Map.Entry<String, String> entry:parmars.entrySet())
//			{
//				//解析Map传递的参数，使用一个键值对对象BasicNameValuePair保存。
//				list.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
////				Log.i("wang", entry.getKey()+":"+entry.getValue());
//			}
//		}
//		
//		try {
//			//实现将请求 的参数封装封装到HttpEntity中。
//			UrlEncodedFormEntity uefEntity=new UrlEncodedFormEntity(list, "utf-8");
//			//使用HttpPost请求方式
//			HttpPost httpPost=new HttpPost(path);
//			//设置请求参数到Form中。
//			httpPost.setEntity(uefEntity);
//			//添加头部信息
//			if(headers!=null&&!headers.isEmpty()){
//				for(Map.Entry<String, String> entry:headers.entrySet()){
//					httpPost.addHeader(entry.getKey(), entry.getValue());
//				}
//			}
//			//实例化一个默认的Http客户端，使用的是AndroidHttpClient
//			
//			//执行请求，并获得响应数据
//			HttpResponse httpResponse= client.execute(httpPost);
//			//判断是否请求成功，为200时表示成功，其他均问有问题。
//			if(httpResponse.getStatusLine().getStatusCode()==200)
//			{
//				return EntityUtils.toString(httpResponse.getEntity());
//			}else if(httpResponse.getStatusLine().getStatusCode() == 302){
//				String temp = httpResponse.getFirstHeader("Location").getValue();
//				if(isLogin){
//					ConstantValue.tempUrl = temp.substring(temp.indexOf("("),
//							temp.indexOf(")") + 2);
//				}
//				String url = ConstantValue.Host + temp;
//				Map<String,String> heas = new HashMap<String, String>();
//				heas.put("Referer", path);
//				return sendGet(url, heas, "utf-8");
//			}
//			
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		} catch (ClientProtocolException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}finally{
//		}
//		return "";
//	}		
//	
//	public static String sendGet(String path,Map<String,String> headers,String encode){
//		try {
//			HttpClient client=new DefaultHttpClient();
//			HttpGet httpGet = new HttpGet(path);
//			if(headers!=null&&!headers.isEmpty()){
//				for(Map.Entry<String, String> entry:headers.entrySet()){
//					httpGet.addHeader(entry.getKey(), entry.getValue());
//				}
//			}
//			HttpResponse httpResponse= client.execute(httpGet);
//			if(httpResponse.getStatusLine().getStatusCode()==200)
//			{
//				return EntityUtils.toString(httpResponse.getEntity());
//			}
//		}catch (UnsupportedEncodingException e) {
//				e.printStackTrace();
//			} catch (ClientProtocolException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		return "";
//	}
}
