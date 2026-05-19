package com.AppRH.AppRH.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;

public class LogoutController {
	@GetMapping("/logout")
    public String logout(Authentication authentication) {
        if (authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(null);
        }
        return "redirect:/login";
    }

}
