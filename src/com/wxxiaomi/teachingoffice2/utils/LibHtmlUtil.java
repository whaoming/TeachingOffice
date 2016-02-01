package com.wxxiaomi.teachingoffice2.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.wxxiaomi.teachingoffice2.ConstantValue;
import com.wxxiaomi.teachingoffice2.bean.LibUserInfo;
import com.wxxiaomi.teachingoffice2.bean.net.NetReceiverData;
import com.wxxiaomi.teachingoffice2.bean.net.ResponseData;
import com.wxxiaomi.teachingoffice2.bean.page.Html_Lib_Main;
import com.wxxiaomi.teachingoffice2.bean.page.Html_lib_Login;

public class LibHtmlUtil {
	
	public static ResponseData<Html_Lib_Main> isLoginSuccess(String html){
		ResponseData<Html_Lib_Main> result = new ResponseData<Html_Lib_Main>();
		Html_Lib_Main obj = new Html_Lib_Main();
		Document doc = Jsoup.parse(html);
		Element userInfoContent = doc.getElementById("userInfoContent");
		if(userInfoContent == null){
			//验证失败
			result.setSuccess(false);
			//<span id="ctl00_ContentPlaceHolder1_lblErr_Lib"><font color="#ff0000">验证码错误</font>&nbsp;&nbsp;</span>
			result.setError(doc.getElementById("ctl00_ContentPlaceHolder1_lblErr_Lib").child(0).text());
		}else{
			result.setObj(obj);
			result.setSuccess(true);
			//获取参数,记得设置cookie
			LibUserInfo userInfo = new LibUserInfo(userInfoContent.child(0).child(1).text(), userInfoContent.child(1).child(1).text()
					, userInfoContent.child(2).child(1).text(), userInfoContent.child(3).child(1).text()
					, userInfoContent.child(4).child(1).text(), userInfoContent.child(5).child(1).text()
					, userInfoContent.child(6).child(1).text(), userInfoContent.child(7).child(1).text()
					, userInfoContent.child(8).child(1).text(), userInfoContent.child(9).child(1).text());
			result.getObj().setUserInfo(userInfo);
			result.getObj().setBookBorrowedUrl(ConstantValue.LibHost + doc.select(":containsOwn(当前借阅情况和续借)").first().attr("href"));
		}
		return result;
	}

	public static Html_lib_Login loginHtml2Bean(NetReceiverData data){
		Html_lib_Login result = new Html_lib_Login();
		Document doc = Jsoup.parse(data.getContent2String());
		result.setLoginUrl("http://210.38.162.2/OPAC/login.aspx?ReturnUrl=/opac/user/userinfo.aspx");
		result.setCodePicUrl("http://210.38.162.2/OPAC/"+doc.getElementById("ccodeimg").attr("src")+"?rd="+Math.random());
		String __VIEWSTATE = doc.getElementById("__VIEWSTATE").attr("value");
		String ctl00_ContentPlaceHolder1_txtlogintype = doc.getElementById("ctl00_ContentPlaceHolder1_txtlogintype").attr("value");
		String ctl00_ContentPlaceHolder1_btnLogin_Lib="%E7%99%BB%E5%BD%95";
		String __EVENTVALIDATION = doc.getElementById("__EVENTVALIDATION").attr("value")==""?"":doc.getElementById("__EVENTVALIDATION").attr("value");
		result.getLoginPars().put("__VIEWSTATE", __VIEWSTATE);
		result.getLoginPars().put("__EVENTTARGET", "");
		result.getLoginPars().put("__EVENTARGUMENT", "");
		result.getLoginPars().put("ctl00$ContentPlaceHolder1$txtlogintype", ctl00_ContentPlaceHolder1_txtlogintype);
		result.getLoginPars().put("ctl00$ContentPlaceHolder1$btnLogin_Lib", ctl00_ContentPlaceHolder1_btnLogin_Lib);
		result.getLoginPars().put("__EVENTVALIDATION", __EVENTVALIDATION);
		return result;
	}
}
