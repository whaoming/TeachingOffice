package com.wxxiaomi.teachingoffice2.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.wxxiaomi.teachingoffice2.ConstantValue;
import com.wxxiaomi.teachingoffice2.bean.OfficeElectiveCourse;
import com.wxxiaomi.teachingoffice2.bean.OfficeElectiveCourse.ElectiveCourseColumn;
import com.wxxiaomi.teachingoffice2.bean.OfficeUserInfo;
import com.wxxiaomi.teachingoffice2.bean.OfficeUserInfo.UserInfoColumn;
import com.wxxiaomi.teachingoffice2.bean.Score;
import com.wxxiaomi.teachingoffice2.bean.Score.ScoreColumn;
import com.wxxiaomi.teachingoffice2.bean.page.Html_Login;
import com.wxxiaomi.teachingoffice2.bean.page.Html_Main;
import com.wxxiaomi.teachingoffice2.exception.OfficeException.LoginException;
import com.wxxiaomi.teachingoffice2.exception.OfficeException.OfficeOutTimeException;

public class Html2ParsUtils {

	public static OfficeElectiveCourse officeElectiveCourseHtml2Bean(String html)
			throws OfficeOutTimeException {
		OfficeElectiveCourse result = new OfficeElectiveCourse();
		Document doc = Jsoup.parse(html);
		Element tag = doc.getElementById("DBGrid");
		if (tag == null) {
			throw new OfficeOutTimeException("登录过期");
		} else {
			Elements trs = doc.getElementById("DBGrid").select("tr");
			for (int i = 1; i < trs.size(); i++) {
				Element tr = trs.get(i);
				ElectiveCourseColumn column = new ElectiveCourseColumn(tr
						.child(0).text(), tr.child(1).text(), tr.child(2)
						.child(0).text(), tr.child(3).text(), tr.child(4)
						.text(), tr.child(5).child(0).text(), tr.child(6)
						.text(), tr.child(7).text(), tr.child(8).child(0)
						.text(), tr.child(9).text(), tr.child(10).text(), tr
						.child(11).text(), tr.child(12).text(), tr.child(13)
						.text(), tr.child(14).text());
				result.getColumns().add(column);
			}
		}
		return result;
	}

	public static Score officeHistoryScoreHtml2Bean(Score score, String html)
			throws OfficeOutTimeException {
		Document doc = Jsoup.parse(html);
		Element loginTag = doc.getElementById("Datagrid1");
		if (loginTag == null) {
			throw new OfficeOutTimeException("登录过期");
		} else {
			Elements trs = doc.getElementById("Datagrid1").select("tr");
			if (trs != null) {
				for (int i = 1; i < trs.size(); i++) {
					Element element = trs.get(i);
					ScoreColumn column = new ScoreColumn(element.child(0)
							.text(), element.child(1).text(), element.child(2)
							.text(), element.child(3).text(), element.child(4)
							.text(), element.child(5).text(), element.child(6)
							.text(), element.child(7).text(), element.child(8)
							.text(), element.child(9).text(), element.child(10)
							.text(), element.child(11).text(), element
							.child(12).text(), element.child(13).text(),
							element.child(12).text());
					score.getColumns().add(column);
				}
			}
		}
		return score;
	}

	public static Score officeScoreHtmlToGetScore2Bean(String html) throws OfficeOutTimeException {
		Score result = new Score();
		Document doc = Jsoup.parse(html);
		/**
		 * __EVENTTARGET __EVENTARGUMENT __VIEWSTATE hidLanguage ddlXN ddlXQ
		 * ddl_kcxz btn_zcj
		 */
		Element first = doc.getElementsByAttributeValue("name", "__EVENTTARGET")
		.first();
		if(first == null){
			throw new OfficeOutTimeException("登录会话过时");
		}else{
			result.getGetHistoryScorePars().put(
					"__EVENTTARGET",
					doc.getElementsByAttributeValue("name", "__EVENTTARGET")
							.first().attr("value"));
			result.getGetHistoryScorePars().put(
					"__EVENTARGUMENT",
					doc.getElementsByAttributeValue("name", "__EVENTARGUMENT")
							.first().attr("value"));
			result.getGetHistoryScorePars().put("__VIEWSTATE",
					(doc.select("[name=__VIEWSTATE]").first().attr("value")));
			result.getGetHistoryScorePars().put("hidLanguage", "");
			result.getGetHistoryScorePars().put("ddlXN", "");
			result.getGetHistoryScorePars().put("ddlXQ", "");
			result.getGetHistoryScorePars().put("ddl_kcxz", "");
			result.getGetHistoryScorePars()
					.put("btn_zcj",
							CommUtils.getGBKUrl(doc.getElementById("btn_zcj").attr(
									"value")));
			// result.set__EVENTTARGET(doc.getElementsByAttributeValue("name",
			// "__EVENTTARGET").first().attr("value"));
			// result.set__EVENTARGUMENT(doc.getElementsByAttributeValue("name",
			// "__EVENTARGUMENT").first().attr("value"));
			// result.set__VIEWSTATE(doc.getElementsByAttributeValue("name",
			// "__VIEWSTATE").first().attr("value"));
			// result.setHidLanguage("");
			// result.setDdl_kcxz("");
			// result.setDdlXN("");
			// result.setDdlXQ("");
			// result.setBtn_zcj(CommUtils.getGBKUrl(doc.getElementById("btn_zcj").attr("value")));
		}
		
		return result;
	}

	public static OfficeUserInfo officeUserInfoHtml2Bean(String html)
			throws OfficeOutTimeException {
		OfficeUserInfo result = new OfficeUserInfo();
		Document doc = Jsoup.parse(html);
		if (doc.getElementById("lbxsgrxx_xh") == null) {
			throw new OfficeOutTimeException("登录会话过时");
		} else {
			Elements keys = doc.select(".trbg1");
			for (Element keyEle : keys) {
				if (keyEle.childNodeSize() != 0) {
					UserInfoColumn column = new UserInfoColumn();
					String key = keyEle.child(0).text();
					column.setKey(key);
					Element nextElementSibling = keyEle.nextElementSibling();
					if (nextElementSibling.childNodeSize() != 0) {
						String tagName = nextElementSibling.child(0).tagName();
						String value = "";
						if ("span".equals(tagName)) {
							value = nextElementSibling.child(0).text();
						} else if ("input".equals(tagName)) {
							value = nextElementSibling.child(0).attr("value");
							column.setEdit(true);
							column.setName(nextElementSibling.child(0).attr(
									"name"));
						}
						column.setValue(value);
						result.getColumns().add(column);
					}
				}
			}
			result.set__VIEWSTATE(doc
					.getElementsByAttributeValue("name", "__VIEWSTATE").first()
					.attr("value"));
		}
		return result;
	}

	public static Html_Main officeMainHtml2Bean(String html)
			throws LoginException {
		Html_Main main = new Html_Main();
		Document doc = Jsoup.parse(html);
		if (doc.getElementById("xhxm") == null) {
			throw new LoginException("登录失败");
		} else {
			// 设置获取课表的url
			main.setClassFormUrl(ConstantValue.tempOfficeUrl
					+ CommUtils.getGBKUrl(doc.select(":containsOwn(专业推荐课表查询)")
							.first().attr("href")));

			// 设置获取个人信息的url
			main.setPersonalInfoUrl(ConstantValue.tempOfficeUrl
					+ CommUtils.getGBKUrl(doc.select(":containsOwn(个人信息)")
							.first().attr("href")));

			// 设置获取选课情况的url
			main.setElectiveCourseUrl(ConstantValue.tempOfficeUrl
					+ CommUtils.getGBKUrl(doc.select(":containsOwn(学生选课情况查询)")
							.first().attr("href")));

			// 设置获取成绩的url
			main.setScoreUrl(ConstantValue.tempOfficeUrl
					+ CommUtils.getGBKUrl(doc.select(":containsOwn(成绩查询)")
							.first().attr("href")));

			// 获取main首页的信息
			main.setNumberAndName(doc.getElementById("xhxm").text());
		}

		return main;
	}

	public static Html_Login officeLoginHtml2Bean(String html) {
		Html_Login result = new Html_Login();
		Document doc = Jsoup.parse(html);
		result.setLoginUrl(doc.select("[name=form1]").first().attr("action"));

		String __VIEWSTATE = doc.select("[name=__VIEWSTATE]").first()
				.attr("value");
		result.getLoginPars().put("__VIEWSTATE", __VIEWSTATE);
		result.getLoginPars().put("RadioButtonList1", "%D1%A7%C9%FA");
		result.getLoginPars().put("Button1", "");
		result.getLoginPars().put("lbLanguage", "");
		result.getLoginPars().put("hidPdrs", "");
		result.getLoginPars().put("hidsc", "");
		result.getLoginPars().put("txtSecretCode", "");
		return result;
	}
}
