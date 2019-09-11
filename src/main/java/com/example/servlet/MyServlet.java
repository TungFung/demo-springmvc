package com.example.servlet;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class MyServlet implements Servlet {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {

    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    /**
     * curl -i -d '{"name":"Eric","age":28}' -H 'Content-Type: application/json' http://localhost:8080/custom
     * http://localhost:8080/custom?name=123&home=jack
     */
    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        Map<String, String[]> parameterMap = servletRequest.getParameterMap();//读取请求url的参数
        BufferedReader reader = servletRequest.getReader();//读取请求体中的数据
        String line = reader.readLine();

        log.info("servlet收到数据，URL参数:{};BODY参数:{}", JSON.toJSONString(parameterMap), line);
        PrintWriter writer = servletResponse.getWriter();
        writer.println("Got it!");
        writer.flush();
        writer.close();
    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {

    }
}
