package com.dapeng.service.exception;

public class UserAccountException extends Exception {

	public UserAccountException(){
		super();
	}

	public UserAccountException(String message){
		super(message);
	}

	public UserAccountException(String message, Throwable throwable){
		super(message, throwable);
	}

}
