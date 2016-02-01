package com.wxxiaomi.teachingoffice2.engine;

import java.util.List;
import com.wxxiaomi.teachingoffice2.ConstantValue;
import com.wxxiaomi.teachingoffice2.bean.OfficeElectiveCourse;
import com.wxxiaomi.teachingoffice2.bean.OfficeUserInfo;
import com.wxxiaomi.teachingoffice2.bean.OfficeUserInfo.UserInfoColumn;
import com.wxxiaomi.teachingoffice2.bean.Score;
import com.wxxiaomi.teachingoffice2.bean.net.NetReceiverData;
import com.wxxiaomi.teachingoffice2.bean.net.NetSendData;
import com.wxxiaomi.teachingoffice2.bean.net.ResponseData;
import com.wxxiaomi.teachingoffice2.bean.page.Html_Login;
import com.wxxiaomi.teachingoffice2.bean.page.Html_Main;
import com.wxxiaomi.teachingoffice2.exception.OfficeException.LoginException;
import com.wxxiaomi.teachingoffice2.exception.OfficeException.OfficeOutTimeException;
import com.wxxiaomi.teachingoffice2.utils.CommUtils;
import com.wxxiaomi.teachingoffice2.utils.Html2ParsUtils;
import com.wxxiaomi.teachingoffice2.utils.HttpUtils;

public class OfficeEngineImpl {
	
	public ResponseData<OfficeElectiveCourse> getOfficeElectiveCourse2Bean(String url,String refererUrl){
//		Log.i("wang", "url="+url);
		ResponseData<OfficeElectiveCourse> result = new ResponseData<OfficeElectiveCourse>();
		NetSendData firstData = new NetSendData();
		firstData.setUrl(url);
		firstData.getHeaders().put("Referer", refererUrl);
		NetReceiverData sendGet = HttpUtils.sendGet(firstData);
		try {
			OfficeElectiveCourse officeElectiveCourseHtml2Bean = Html2ParsUtils.officeElectiveCourseHtml2Bean(sendGet.getContent2String());
			result.setObj(officeElectiveCourseHtml2Bean);
			result.setSuccess(true);
		} catch (OfficeOutTimeException e) {
			// 登录会话过期了
//			e.printStackTrace();
//			Log.i("wang", "e.getMessage()="+e.getMessage());
			result.setSuccess(false);
			result.setError(e.getMessage());
		}
		return result;
	}
	
	public ResponseData<Score> getScore2Bean(String url,String refererUrl){
		ResponseData<Score> result = new ResponseData<Score>();
		NetSendData firstData = new NetSendData();
		firstData.setUrl(url);
		firstData.getHeaders().put("Referer", refererUrl);
		NetReceiverData sendGet = HttpUtils.sendGet(firstData);
		
		try {
			Score officeScoreHtml2Bean = Html2ParsUtils.officeScoreHtmlToGetScore2Bean(sendGet.getContent2String());
			//上面是第一次get取得的页面实体，里面封装有参数
			//------------------------------------------------------
			NetSendData sencondData = new NetSendData();
			sencondData.setUrl(url);
			sencondData.getParmars().putAll(officeScoreHtml2Bean.getGetHistoryScorePars());
			sencondData.getHeaders().put("Referer", url);
			NetReceiverData sendPost = HttpUtils.sendPost(sencondData);
			try {
				Score officeScoreHtml2Bean2 = Html2ParsUtils.officeHistoryScoreHtml2Bean(officeScoreHtml2Bean, sendPost.getContent2String());
				result.setObj(officeScoreHtml2Bean2);
				result.setSuccess(true);
			} catch (OfficeOutTimeException e) {
//				e.printStackTrace();
				result.setSuccess(false);
				result.setError(e.getMessage());
			}
		} catch (OfficeOutTimeException e1) {
			result.setError(e1.getMessage());
			result.setSuccess(false);
		}
		
		
		
		return result;
	}
	
	public ResponseData<OfficeUserInfo> updateOfficeUserInfo(String url,String refererUrl,List<UserInfoColumn> pars,String _view){
		ResponseData<OfficeUserInfo> result = new ResponseData<OfficeUserInfo>();
		NetSendData firstData = new NetSendData();
		firstData.setUrl(url);
		firstData.getHeaders().put("Referer", url);
		firstData.getParmars().put("__VIEWSTATE",_view);
		firstData.getParmars().put("Button1",CommUtils.getGBKUrl("提 交"));
//		Log.i("wang", "url="+url+"---refererUrl="+url);
		for(UserInfoColumn column : pars){
			firstData.getParmars().put(column.getName(), column.getValue());
//			Log.i("", msg)
		}
		NetReceiverData sendGet = HttpUtils.sendPost(firstData);
		try {
			OfficeUserInfo officeUserInfoHtml2Bean = Html2ParsUtils.officeUserInfoHtml2Bean(sendGet.getContent2String());
			result.setObj(officeUserInfoHtml2Bean);
			result.setSuccess(true);
		} catch (OfficeOutTimeException e) {
			result.setError(e.getMessage());
			result.setSuccess(false);
		}
		
		return result;
	}
	
	public ResponseData<OfficeUserInfo> getOfficePersonalInfo2Bean(String url,String refererUrl){
		ResponseData<OfficeUserInfo> result = new ResponseData<OfficeUserInfo>();
		NetSendData firstData = new NetSendData();
		firstData.setUrl(url);
		firstData.getHeaders().put("Referer", refererUrl);
		NetReceiverData sendGet = HttpUtils.sendGet(firstData);
		try {
			OfficeUserInfo officeUserInfoHtml2Bean = Html2ParsUtils.officeUserInfoHtml2Bean(sendGet.getContent2String());
			result.setObj(officeUserInfoHtml2Bean);
			result.setSuccess(true);
		} catch (OfficeOutTimeException e) {
			result.setSuccess(false);
			result.setError(e.getMessage());
		}
		return result;
	}
	
//	public ResponseData<Html_Main> getOfficeMainHtml2BeanByCache(String username,String password){
//		ResponseData<Html_Main> result = new ResponseData<Html_Main>();
//		Html_Login officeLoginHtml2Bean = getOfficeLoginHtml2Bean();
//		NetSendData firstData = new NetSendData();
//		firstData.setUrl(officeLoginHtml2Bean.getLoginUrl());
//		officeLoginHtml2Bean.getLoginPars().put("txtUserName", username);
//		officeLoginHtml2Bean.getLoginPars().put("TextBox2", password);
//		firstData.setParmars(officeLoginHtml2Bean.getLoginPars());
//		firstData.getHeaders().put("Referer", officeLoginHtml2Bean.getUrl());
//		NetReceiverData sendPost = HttpUtils.sendPost(firstData);
//		Html_Main officeMainHtml2Bean;
//		try {
//			officeMainHtml2Bean = Html2ParsUtils.officeMainHtml2Bean(sendPost.getContent2String());
//			officeMainHtml2Bean.setUsername(username);
//			officeMainHtml2Bean.setPassword(password);
//			result.setObj(officeMainHtml2Bean);
//			result.getObj().setFromUrl(sendPost.getFromUrl());
//			result.setSuccess(true);
//		} catch (LoginException e) {
////			e.printStackTrace();
//			result.setSuccess(false);
//			result.setError(e.getMessage());
//		}
//		return result;
//	}
	
	public ResponseData<Html_Main> getOfficeMainHtml2BeanByOne(String username,String password){
		ResponseData<Html_Main> result = new ResponseData<Html_Main>();
		Html_Login officeLoginHtml2Bean = getOfficeLoginHtml2Bean();
		NetSendData firstData = new NetSendData();
		firstData.setUrl(officeLoginHtml2Bean.getLoginUrl());
		officeLoginHtml2Bean.getLoginPars().put("txtUserName", username);
		officeLoginHtml2Bean.getLoginPars().put("TextBox2", password);
		firstData.setParmars(officeLoginHtml2Bean.getLoginPars());
		firstData.getHeaders().put("Referer", officeLoginHtml2Bean.getUrl());
		NetReceiverData sendPost = HttpUtils.sendPost(firstData);
		Html_Main officeMainHtml2Bean;
		try {
			officeMainHtml2Bean = Html2ParsUtils.officeMainHtml2Bean(sendPost.getContent2String());
			officeMainHtml2Bean.setUsername(username);
			officeMainHtml2Bean.setPassword(password);
			result.setObj(officeMainHtml2Bean);
			result.getObj().setFromUrl(sendPost.getFromUrl());
			result.setSuccess(true);
		} catch (LoginException e) {
			result.setSuccess(false);
			result.setError(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 登录，顺便取得主页面所有参数
	 * @param loginBean
	 * @param username
	 * @param password
	 * @return
	 */
//	public ResponseData<Html_Main> getOfficeMainHtml2Bean(Html_Login loginBean,String username,String password){
////		Html_Main main = new Html_Main();
//		ResponseData<Html_Main> result = new ResponseData<Html_Main>();
//		NetSendData firstData = new NetSendData();
//		firstData.setUrl(loginBean.getLoginUrl());
//		loginBean.getLoginPars().put("txtUserName", username);
//		loginBean.getLoginPars().put("TextBox2", password);
//		firstData.setParmars(loginBean.getLoginPars());
//		firstData.getHeaders().put("Referer", loginBean.getUrl());
//		NetReceiverData sendPost = HttpUtils.sendPost(firstData);
//		Html_Main officeMainHtml2Bean = Html2ParsUtils.officeMainHtml2Bean(new String(sendPost.getContent2String()));
//		result.setObj(officeMainHtml2Bean);
//		result.setSuccess(true);
//		return result;
//	}

	/**
	 * 获取登录页面html对应的bean
	 * @param username
	 * @param password
	 * @return
	 */
	public Html_Login getOfficeLoginHtml2Bean(){
		NetSendData firstData = new NetSendData();
		firstData.setUrl(ConstantValue.Host);
		NetReceiverData sendPost = HttpUtils.sendPost(firstData);
//		Log.i("wang", "sendPost.getFromUrl="+sendPost.getFromUrl());
		
		Html_Login item = Html2ParsUtils.officeLoginHtml2Bean(new String(sendPost.getContent2String()));
		item.setTempUrl(sendPost.getFromUrl().replaceAll(item.getLoginUrl(), ""));
		item.setLoginUrl(item.getTempUrl()+item.getLoginUrl());
		ConstantValue.tempOfficeUrl = item.getTempUrl();
		if(item!=null){
			item.setUrl(sendPost.getFromUrl());
		}
		return item;
	}
}
