package com.wxxiaomi.teachingoffice2.engine;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.wxxiaomi.teachingoffice2.ConstantValue;
import com.wxxiaomi.teachingoffice2.bean.BookInfo;
import com.wxxiaomi.teachingoffice2.bean.BookInfo.CollectState;
import com.wxxiaomi.teachingoffice2.bean.net.NetReceiverData;
import com.wxxiaomi.teachingoffice2.bean.net.NetSendData;
import com.wxxiaomi.teachingoffice2.bean.net.ResponseData;
import com.wxxiaomi.teachingoffice2.bean.page.Html_Lib_BookInfoDetail;
import com.wxxiaomi.teachingoffice2.bean.page.Html_Lib_BorrowHistory;
import com.wxxiaomi.teachingoffice2.bean.page.Html_Lib_BorrowedState;
import com.wxxiaomi.teachingoffice2.bean.page.Html_Lib_Main;
import com.wxxiaomi.teachingoffice2.bean.page.Html_Lib_Search_Main;
import com.wxxiaomi.teachingoffice2.bean.page.Html_Lib_Search_Result;
import com.wxxiaomi.teachingoffice2.bean.page.Html_lib_Login;
import com.wxxiaomi.teachingoffice2.exception.OfficeException.OfficeOutTimeException;
import com.wxxiaomi.teachingoffice2.utils.CommUtils;
import com.wxxiaomi.teachingoffice2.utils.LibHttpUtils;

public class LibraryEngineImpl extends LibHtmlEngineImpl{
	
	public Html_Lib_BookInfoDetail BookInfo2Bean(String html){
		Html_Lib_BookInfoDetail result = new Html_Lib_BookInfoDetail();
		BookInfo info = new BookInfo();
		Document doc = Jsoup.parse(html);
		Element span = doc.getElementById("ctl00_ContentPlaceHolder1_bookcardinfolbl");
		if(span != null){
			info.setDetail(span.text());
			Element tbody = doc.select("table.tb tbody").first();
			if(tbody != null){
				for(Element tr : tbody.children()){
					CollectState location = new CollectState(tr.child(0).child(0).text()
							, tr.child(1).text(), tr.child(2).text(), tr.child(3).text()
							, tr.child(4).text(), tr.child(5).text(), tr.child(6).text()
							);
					info.getCollecLocations().add(location);
				}	
			}
			result.setBookInfo(info);
		}else{
			Log.i("wang", "span == null");
		}
		
		return result;
	}
	
	public ResponseData<Html_Lib_BookInfoDetail> getBookInfo(String url){
		ResponseData<Html_Lib_BookInfoDetail> result = new ResponseData<Html_Lib_BookInfoDetail>();
		NetSendData sendData = new NetSendData();
		sendData.setUrl(url);
		NetReceiverData data = LibHttpUtils.sendGet(sendData);
		Html_Lib_BookInfoDetail bookInfo2Bean = BookInfo2Bean(new String(data.getContent()));
		result.setObj(bookInfo2Bean);
		result.setSuccess(true);
		return result;
	}
//	public ResponseData<Html_Lib_BookInfoDetail> getBookInfo(BookInfo bookInfo){
//		ResponseData<Html_Lib_BookInfoDetail> result = new ResponseData<Html_Lib_BookInfoDetail>();
//		NetSendData sendData = new NetSendData();
//		sendData.setUrl(bookInfo.getUrl());
//		NetReceiverData data = LibHttpUtils.sendGet(sendData);
//		Html_Lib_BookInfoDetail bookInfo2Bean = BookInfo2Bean(new String(data.getContent()),bookInfo);
//		result.setObj(bookInfo2Bean);
//		result.setSuccess(true);
//		return result;
//	}
	
	public ResponseData<Html_Lib_Search_Result> getNextPage(String nexPageUrl,int currentPage, String refererUrl){
		ResponseData<Html_Lib_Search_Result> result = new ResponseData<Html_Lib_Search_Result>();
		String url = nexPageUrl+CommUtils.getEncodeUrl("&page="+(currentPage+1))+"=";
//		Log.i("wang", "url="+url);
		NetSendData sendData = new NetSendData();
		sendData.setUrl(url);
		sendData.getHeaders().put("Referer", refererUrl);
		NetReceiverData sendGet = LibHttpUtils.sendGet(sendData);
//		Log.i("wang", new String(sendGet.getContent()));
		Html_Lib_Search_Result libSearchResultHtml2Bean = LibSearchResultHtml2Bean(new String(sendGet.getContent()),currentPage);
//		info.getColumns().addAll(libSearchResultHtml2Bean.getColumns());
//		info.setCurrentPage(info.getCurrentPage()+1+"");
		result.setSuccess(true);
		result.setObj(libSearchResultHtml2Bean);
		return result;
	}
	
	public ResponseData<Html_Lib_Search_Result> getSearchResult(Html_Lib_Search_Main main,String content){
		ResponseData<Html_Lib_Search_Result> result = new ResponseData<Html_Lib_Search_Result>();
		NetSendData sendData = new NetSendData();
		sendData.setUrl(main.getSearchUrl());
		main.getSearchPars().put("ctl00$ContentPlaceHolder1$keywordstb", content);
		main.getSearchPars().put("ctl00$ContentPlaceHolder1$splb", "TITLEFORWARD");
		main.getSearchPars().put("ctl00$ContentPlaceHolder1$deptddl", "ALL");
		main.getSearchPars().put("ctl00$ContentPlaceHolder1$depthf", "ALL");
		sendData.setParmars(main.getSearchPars());
		NetReceiverData sendPost = LibHttpUtils.sendPost(sendData);
		result.setObj(LibSearchResultHtml2Bean(new String(sendPost.getContent()),0));
		result.getObj().setUrl(sendPost.getFromUrl());
//		Log.i("wang", "sendPost.getFromUrl()="+sendPost.getFromUrl());
		result.setSuccess(true);
		return result;
	}
	
	public ResponseData<Html_Lib_Search_Main> getLibSearchMain(){
		ResponseData<Html_Lib_Search_Main> result = new ResponseData<Html_Lib_Search_Main>();
		NetSendData sendData = new NetSendData();
		sendData.setUrl(ConstantValue.LIBSEARCHURL);
		NetReceiverData sendGet = LibHttpUtils.sendGet(sendData);
//		Log.i("wang", "sendGet.getHeaders().get(Cookie)="+sendGet.getHeaders().get("Cookie"));
		result.setObj(LibSearchMainHtml2Bean(new String(sendGet.getContent())));
		result.setSuccess(true);
		return result;
	}
	
	public ResponseData<Html_Lib_BorrowHistory> getBorrowHistory(String cookie,String url){
		ResponseData<Html_Lib_BorrowHistory> result = new ResponseData<Html_Lib_BorrowHistory>();
		NetSendData sendData = new NetSendData();
		sendData.setUrl(url);
		sendData.getHeaders().put("Cookie", cookie);
		NetReceiverData receiverData = LibHttpUtils.sendGet(sendData);
		try {
			Html_Lib_BorrowHistory borrowHistory = getBorrowHistory(new String(receiverData.getContent()));
			result.setObj(borrowHistory);
			result.setSuccess(true);
		} catch (OfficeOutTimeException e) {
			result.setSuccess(false);
			result.setError(e.getMessage());
		}
		return result;
	}
	
	public ResponseData<Html_Lib_BorrowedState> getBorrowedState(String cookie,String url,String refererUrl){
//		Log.i("wang", refererUrl+url+cookie);
		ResponseData<Html_Lib_BorrowedState> result = new ResponseData<Html_Lib_BorrowedState>();
		NetSendData sendData = new NetSendData();
		sendData.setUrl(url);
		sendData.getHeaders().put("Cookie", cookie);
		sendData.getHeaders().put("Referer", refererUrl);
		NetReceiverData receiverData = LibHttpUtils.sendGet(sendData);
		try {
			Html_Lib_BorrowedState borrowStateBean = getBorrowStateBean(new String(receiverData.getContent()));
			result.setObj(borrowStateBean);
			result.setSuccess(true);
		} catch (OfficeOutTimeException e) {
			result.setError(e.getMessage());
			result.setSuccess(false);
		}
		return result;
	}
	
	public ResponseData<Html_Lib_Main> Login(Html_lib_Login login,String username,String password,String code){
		ResponseData<Html_Lib_Main> result = new ResponseData<Html_Lib_Main>();
		NetSendData loginToSendData = new NetSendData();
		loginToSendData.setUrl(login.getLoginUrl());
		loginToSendData.getHeaders().put("Referer", login.getLoginUrl());
		Map<String, String> libLoginParmars = login.getLoginPars();
		libLoginParmars.put("ctl00$ContentPlaceHolder1$txtUsername_Lib", username);
		libLoginParmars.put("ctl00$ContentPlaceHolder1$txtPas_Lib", password);
		libLoginParmars.put("ctl00$ContentPlaceHolder1$txtCode", code);
		loginToSendData.setParmars(libLoginParmars);
		loginToSendData.getHeaders().put("Cookie", login.getCookie());
//		Log.i("wang", "login.getCookie()="+login.getCookie());
		NetReceiverData receiverData = LibHttpUtils.sendPost(loginToSendData);
		try {
			Html_Lib_Main loginSuccess = isLoginSuccess(new String(receiverData.getContent()));
			loginSuccess.setCookie(receiverData.getHeaders().get("Cookie"));
			loginSuccess.setUsername(username);
			loginSuccess.setPassword(password);
			result.setObj(loginSuccess);
			result.setSuccess(true);
			ConstantValue.isLibLogin = true;
//			Log.i("wang", "loginSuccess.getCookie()="+loginSuccess.getCookie());
		} catch (OfficeOutTimeException e) {
//			e.printStackTrace();
			result.setError(e.getMessage());
			result.setSuccess(false);
		}
		return result;
	}
	
	public ResponseData<Html_lib_Login> getLibLoginPageAndCodePic(){
		ResponseData<Html_lib_Login> result = new ResponseData<Html_lib_Login>();
		NetSendData send = new NetSendData();
		send.setUrl(ConstantValue.LIBURL);
		NetReceiverData data = LibHttpUtils.sendGet(send);
		Html_lib_Login loginHtml2Bean = loginHtml2Bean(data);
		String temlCookie = data.getHeaders().get("Cookie");
		loginHtml2Bean.setCookie(temlCookie);
		Log.i("wang", "temlCookie="+temlCookie);
		NetSendData getCode = new NetSendData();
		getCode.setUrl(loginHtml2Bean.getCodePicUrl());
		getCode.getHeaders().put("Cookie", temlCookie);
		NetReceiverData receiverData = LibHttpUtils.sendGet(getCode);
		InputStream is = null;
		try {
			is = new ByteArrayInputStream(receiverData.getContent());
			loginHtml2Bean.setPicCode(BitmapFactory.decodeStream(is));
			is.close();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		result.setObj(loginHtml2Bean);
		result.setSuccess(true);
		return result;
	}
	
	public ResponseData<Bitmap> getLoginCodePicBitmap(Html_lib_Login d_login){
		 ResponseData<Bitmap> bitmap = new ResponseData<Bitmap>();
		NetSendData getCode = new NetSendData();
		getCode.setUrl(d_login.getCodePicUrl());
		getCode.getHeaders().put("Cookie", d_login.getCookie());
		NetReceiverData receiverData = LibHttpUtils.sendGet(getCode);
		InputStream is = null;
		try {
			is = new ByteArrayInputStream(receiverData.getContent());
			bitmap.setObj(BitmapFactory.decodeStream(is));
			is.close();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		bitmap.setSuccess(true);
		return bitmap;
	}

	public ResponseData<Html_lib_Login> getLibLoginPage2Bean(){
		ResponseData<Html_lib_Login> result = new ResponseData<Html_lib_Login>();
		NetSendData send = new NetSendData();
		send.setUrl(ConstantValue.LIBURL);
		NetReceiverData data = LibHttpUtils.sendGet(send);
		result.setObj(loginHtml2Bean(data));
		result.getObj().setCookie(data.getHeaders().get("Cookie"));
		result.setSuccess(true);
		return result;
	}
}
