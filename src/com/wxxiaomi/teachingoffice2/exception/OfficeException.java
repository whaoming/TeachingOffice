package com.wxxiaomi.teachingoffice2.exception;


public class OfficeException{

	public static class LoginException  extends Exception {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public LoginException(String msg){
			super(msg);
		}
	}
	
	public static class OfficeOutTimeException  extends Exception {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		
		public OfficeOutTimeException(String msg){
			super(msg);
		}

	}
}
