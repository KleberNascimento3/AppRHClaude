package com.AppRH.AppRH.exception;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

public class TratamentoErro403 implements AccessDeniedHandler {	
	
		    @Override
	    public void handle(HttpServletRequest req, HttpServletResponse res, AccessDeniedException ex) throws IOException, ServletException {
	        res.setStatus(403);
	        res.setContentType("application/json");
	        res.getWriter().write("sua mensagem personalizada");
	    }

}
