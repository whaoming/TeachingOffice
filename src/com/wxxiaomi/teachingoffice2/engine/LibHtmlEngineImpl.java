package com.wxxiaomi.teachingoffice2.engine;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.util.Log;

import com.wxxiaomi.teachingoffice2.ConstantValue;
import com.wxxiaomi.teachingoffice2.bean.BookBorrowedState;
import com.wxxiaomi.teachingoffice2.bean.BookInfo;
import com.wxxiaomi.teachingoffice2.bean.BookInfo.CollectState;
import com.wxxiaomi.teachingoffice2.bean.LibUserInfo;
import com.wxxiaomi.teachingoffice2.bean.net.NetReceiverData;
import com.wxxiaomi.teachingoffice2.bean.page.Html_Lib_BookInfoDetail;
import com.wxxiaomi.teachingoffice2.bean.page.Html_Lib_BorrowHistory;
import com.wxxiaomi.teachingoffice2.bean.page.Html_Lib_BorrowedState;
import com.wxxiaomi.teachingoffice2.bean.page.Html_Lib_Main;
import com.wxxiaomi.teachingoffice2.bean.page.Html_Lib_Search_Result;
import com.wxxiaomi.teachingoffice2.bean.page.Html_Lib_Search_Main;
import com.wxxiaomi.teachingoffice2.bean.page.Html_lib_Login;
import com.wxxiaomi.teachingoffice2.exception.OfficeException.OfficeOutTimeException;
import com.wxxiaomi.teachingoffice2.utils.CommUtils;

public class LibHtmlEngineImpl {
	
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
	
	public Html_Lib_Search_Result LibSearchResultHtml2Bean(String html,int page){
		Html_Lib_Search_Result result = new Html_Lib_Search_Result();
		Document doc = Jsoup.parse(html);
		Element table = doc.select("table.tb").first();
		if(table != null){
			Element tbody = table.select("tbody").first();
			if(tbody.children().size()!=0){
				for(int i = 0;i<tbody.children().size();i++){
					Element tr = tbody.child(i);
					BookInfo column = new BookInfo(tr.child(1).child(0).child(0).text()
							, tr.child(2).text(), tr.child(3).text(), tr.child(4).text()
							, tr.child(5).text(), tr.child(6).text(), tr.child(7).text());
					column.setUrl(ConstantValue.LIBMAIN+tr.child(1).child(0).child(0).attr("href"));
					result.getColumns().add(column);
				}
				if(page == 0){//说明是加载第一页结果
					String pageUrl = ConstantValue.LIBMAIN+ConstantValue.LIBASEARCHRESULTAJAX+"?"+CommUtils.getEncodeUrl(doc.getElementById("ctl00_ContentPlaceHolder1_thissearchhf").attr("value"));
					result.setPageUrl(pageUrl);
					result.setPageCount(doc.getElementById("ctl00_ContentPlaceHolder1_gplblfl1").text());
//					result.setCurrentPage(doc.getElementById("ctl00_ContentPlaceHolder1_dplblfl1").text());
					result.setCurrentPage(1+"");
				}
			}
		}else{
			result.setMsg("无数据");
		}
//		String value = ;
		/**
		 * 下一页的地址，就要图书馆服务器地址+ajax地址+参数+page
		 */
	
		return result;
	}
	
	/**
	 * 把图书馆搜索页面转化成bean
	 * @param html
	 * @return
	 */
	public Html_Lib_Search_Main LibSearchMainHtml2Bean(String html){
		Html_Lib_Search_Main result = new Html_Lib_Search_Main();
		Document doc = Jsoup.parse(html);
		result.getSearchPars().put("__VIEWSTATE", doc.getElementById("__VIEWSTATE").attr("value"));
		result.getSearchPars().put("__EVENTVALIDATION", doc.getElementById("__EVENTVALIDATION").attr("value"));
		result.getSearchPars().put("ctl00$ContentPlaceHolder1$searchbtn", CommUtils.getGBKUrl(doc.getElementsByAttributeValue("name", "ctl00$ContentPlaceHolder1$searchbtn").first().attr("value")));
		result.setSearchUrl(ConstantValue.LIBSEARCHURL);
		return result;
	}
	
	/**
	 * 获取借书历史的bean
	 * @param html
	 * @return
	 * @throws OfficeOutTimeException
	 */
	public Html_Lib_BorrowHistory getBorrowHistory(String html) throws OfficeOutTimeException{
		Html_Lib_BorrowHistory result = new Html_Lib_BorrowHistory();
		Document doc = Jsoup.parse(html);
		Element table = doc.select("table.tb").first();
		if(table == null){
			throw new OfficeOutTimeException("登录过期");
		}else{
			Element tbody = table.select("tbody").first();
			for(int i=0;i<tbody.children().size();i++){
				Element tr = tbody.child(i);
				BookBorrowedState column = new BookBorrowedState(tr.child(0).text()
						, tr.child(1).text(), ConstantValue.LibHost + tr.child(2).child(0).attr("href")
						,tr.child(2).child(0).text(), tr.child(3).text()
						, tr.child(4).text());
				result.getColumns().add(column);
			}
			result.setPageCount(doc.getElementById("ctl00_cpRight_Pagination2_gplbl2").text());
			Element next = doc.select(":containsOwn(下一页)").first();
			if(next != null){
				result.setNextPageUrl(next.attr("href"));
			}else{
				result.setNextPageUrl("");
			}
			
		}
		return result;
	}
	
	/**
	 * 获取目前借书情况的bean
	 * @param html
	 * @return
	 * @throws OfficeOutTimeException
	 */
	public Html_Lib_BorrowedState getBorrowStateBean(String html) throws OfficeOutTimeException{
		Html_Lib_BorrowedState result = new Html_Lib_BorrowedState();
		Document doc = Jsoup.parse(html);
		Element div = doc.getElementById("borrowedcontent");
		if(div == null){
			throw new OfficeOutTimeException("登录过期");
		}else{
			Element tbody = div.select("tbody").first();
			for(int i=0;i<tbody.children().size();i++){
				Element tr = tbody.child(i);
//				BookBorrowedState column = new BookBorrowedState(tr.child(1).text()
//						, tr.child(2).child(0).text(), tr.child(3).text()
////						, tr.child(4).text(), tr.child(5).text(), tr.child(6).text(),0);
//				BookBorrowedState column = new BookBorrowedState(tr.child(0).text()
//						, tr.child(1).text(), ConstantValue.LibHost + tr.child(2).child(0).attr("href")
//						,tr.child(2).child(0).text(), tr.child(3).text()
//						, tr.child(4).text());
				BookBorrowedState column = new BookBorrowedState(tr.child(1).text()
						,  tr.child(2).child(0).text()
						,tr.child(3).text()
						, tr.child(4).text()
						, tr.child(5).text()
						,tr.child(6).text()
						,ConstantValue.LibHost + tr.child(2).child(0).attr("href")
						);
				result.getColumns().add(column);
			}
		}
		return result;
	}

	/**
	 * 判断是否登录成功，如果成功顺便把参数封装到bean中
	 * @param html
	 * @return
	 */
	public  Html_Lib_Main isLoginSuccess(String html) throws OfficeOutTimeException{
//		ResponseData<Html_Lib_Main> result = new ResponseData<Html_Lib_Main>();
		Html_Lib_Main main = new Html_Lib_Main();
		Document doc = Jsoup.parse(html);
		Element userInfoContent = doc.getElementById("userInfoContent");
		if(userInfoContent == null){
			//验证失败
			throw new OfficeOutTimeException(doc.getElementById("ctl00_ContentPlaceHolder1_lblErr_Lib").child(0).text());
			//<span id="ctl00_ContentPlaceHolder1_lblErr_Lib"><font color="#ff0000">验证码错误</font>&nbsp;&nbsp;</span>
//			result.setError(doc.getElementById("ctl00_ContentPlaceHolder1_lblErr_Lib").child(0).text());
		}else{
			main.setBorrowHistoryUrl(ConstantValue.LibHost + doc.select(":containsOwn(我的借书历史)").first().attr("href"));
			
			//获取参数,记得设置cookie
			LibUserInfo userInfo = new LibUserInfo(userInfoContent.child(0).child(1).text(), userInfoContent.child(1).child(1).text()
					, userInfoContent.child(2).child(1).text(), userInfoContent.child(3).child(1).text()
					, userInfoContent.child(4).child(1).text(), userInfoContent.child(5).child(1).text()
					, userInfoContent.child(6).child(1).text(), userInfoContent.child(7).child(1).text()
					, userInfoContent.child(8).child(1).text(), userInfoContent.child(9).child(1).text());
			main.setUserInfo(userInfo);
			main.setBookBorrowedUrl(ConstantValue.LibHost + doc.select(":containsOwn(当前借阅情况和续借)").first().attr("href"));
		}
		return main;
	}

	public  Html_lib_Login loginHtml2Bean(NetReceiverData data){
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
