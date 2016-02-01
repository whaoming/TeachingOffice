package com.wxxiaomi.teachingoffice2.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommUtils {
	
	public static String utf8Togb2312(String str){

        StringBuffer sb = new StringBuffer();

        for ( int i=0; i<str.length(); i++) {

            char c = str.charAt(i);

            switch (c) {

               case '+' :

                   sb.append( ' ' );

               break ;

               case '%' :

                   try {

                        sb.append(( char )Integer.parseInt (

                        str.substring(i+1,i+3),16));

                   }

                   catch (NumberFormatException e) {

                       throw new IllegalArgumentException();

                  }

                  i += 2;

                  break ;

               default :

                  sb.append(c);

                  break ;

             }

        }

        String result = sb.toString();

        String res= null ;

        try {

             byte [] inputBytes = result.getBytes( "8859_1" );

            res= new String(inputBytes, "UTF-8" );

        }

        catch (Exception e){}

        return res;

  } 

	public static String getGBKUrl(String url){
		String p = "[\u4e00-\u9fa5]{2,4}";
		Pattern pa = Pattern.compile(p);
		Matcher m = pa.matcher(url);
		String temp = "";
		while (m.find()) {
			temp = temp + m.group();
		}
		try {
			url = url.replaceAll(temp, URLEncoder.encode(temp, "gb2312"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return url;
	}
	
	public static String getEncodeUrl(String url){
		try {
			return URLEncoder.encode(url, "gb2312");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return url;
	}
}
