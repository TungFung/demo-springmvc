package com.example.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import java.io.IOException;

public class MyDispatchFilter implements Filter {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("myDispatchFilter--begin");
        chain.doFilter(request, response);
        log.info("myDispatchFilter--end");
    }

    @Override
    public void destroy() {

    }
}
