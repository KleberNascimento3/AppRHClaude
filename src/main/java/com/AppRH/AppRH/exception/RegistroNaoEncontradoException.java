package com.AppRH.AppRH.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND)
public class RegistroNaoEncontradoException extends RuntimeException{
	
	private static final long serialVersionUID = -597608312624905962L;

	public RegistroNaoEncontradoException() {
		super();
	}
	
	public RegistroNaoEncontradoException(String message) {
		super(message);
	}
	
	public RegistroNaoEncontradoException(Throwable cause) {
		super(cause);
	}
	
	public RegistroNaoEncontradoException(String message, Throwable cause) {
		super(message,cause);
	}
	
}

