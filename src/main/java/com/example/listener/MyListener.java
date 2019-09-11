package com.example.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class MyListener implements ServletContextListener {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.info("Web容器初始化");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        log.info("Web容器销毁");
    }
}
