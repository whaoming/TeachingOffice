package com.wxxiaomi.teachingoffice2.bean.page;

import java.util.ArrayList;
import java.util.List;

import com.wxxiaomi.teachingoffice2.bean.BookBorrowedState;

public class Html_Lib_BorrowHistory {

	private int pageCount;
	List<BookBorrowedState> columns = new ArrayList<BookBorrowedState>();
	private String nextPageUrl;
	

	public String getNextPageUrl() {
		return nextPageUrl;
	}

	public void setNextPageUrl(String nextPageUrl) {
		this.nextPageUrl = nextPageUrl;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public List<BookBorrowedState> getColumns() {
		return columns;
	}

	public void setColumns(List<BookBorrowedState> columns) {
		this.columns = columns;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(String pageCount) {
		this.pageCount = Integer.valueOf(pageCount);
	}
	
	
}
