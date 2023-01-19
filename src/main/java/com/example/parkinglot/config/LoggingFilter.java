package com.example.parkinglot.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class LoggingFilter implements Filter {


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        long startTime = System.currentTimeMillis();
        log.info("Incoming Request : "+ request.getRequestURI() + " : "+ request.getMethod());
        filterChain.doFilter(request,response);
        log.info("Outgoing Response : "+ response + " : "+ response.getStatus());
        log.info("Time taken : " + (System.currentTimeMillis() - startTime));
    }
}