package com.mycompany.usercrud.resources;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class LoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig); 
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);
        String loginURI = req.getContextPath() + "/index.xhtml";
        String signupURI = req.getContextPath() + "/sign_up.xhtml";
        String resetPasswordURI = req.getContextPath() + "/forgot_password.xhtml";

        boolean loggedIn = session != null && session.getAttribute("authenticatedUser") != null;
        boolean loginRequest = req.getRequestURI().equals(loginURI);
        boolean signupRequest = req.getRequestURI().equals(signupURI);
        boolean resetPasswordRequest = req.getRequestURI().equals(resetPasswordURI);
        boolean resourceRequest = req.getRequestURI().startsWith(req.getContextPath() + "/jakarta.faces.resource/");

        
        if (loggedIn || loginRequest || resourceRequest || signupRequest || resetPasswordRequest) {
            chain.doFilter(request, response);
        } else {
            res.sendRedirect(loginURI);
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
  
}
