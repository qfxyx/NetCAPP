package com.example.jinanuniversity.error;

public class IEasyException extends Exception {
 
	private static final long serialVersionUID = 1L;
	private String mExtra;
	
	public IEasyException(String message){
		super(message);
	}

	public IEasyException(String message, String extra){
		super(message);
		mExtra = extra;
	}
	/**
	 * @return the mExtra
	 */
	public String getmExtra() {
		return mExtra;
	}

 
	
	 

}
