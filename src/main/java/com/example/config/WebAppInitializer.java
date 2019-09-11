package com.example.config;

import com.example.filter.MyDispatchFilter;
import com.example.filter.MyFilter;
import com.example.listener.MyListener;
import com.example.servlet.MyServlet;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.*;

/**
 * AbstractAnnotationConfigDispatcherServletInitializer会初始化DispatcherServlet和ContextLoaderListener
 *
 * 加载Servlet是通过javax.servlet.ServletContext的servletContext.addServlet(servletName, dispatcherServlet)方法加载,
 * 加载其他Servlet也是一样的方式。这里继承了这个Initializer是为了实现配置的几个protected方法。
 */
public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{RootConfig.class};
    }

    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{WebConfig.class};
    }

    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        //默认的onStartup方法会初始化Dispatcher
        super.onStartup(servletContext);

        //添加自定义的Filter
        FilterRegistration.Dynamic dispatchFilter = servletContext.addFilter("myDispatchFilter", MyDispatchFilter.class);
        dispatchFilter.addMappingForServletNames(null, false, "dispatcher");//给DispatcherServlet添加

        FilterRegistration.Dynamic filter = servletContext.addFilter("myFilter", MyFilter.class);
        filter.addMappingForUrlPatterns(null, false, "/custom/*");//给指定URL添加

        //添加自定义的Servlet
        ServletRegistration.Dynamic servlet = servletContext.addServlet("myServlet", MyServlet.class);
        servlet.addMapping("/custom");//这是比/更具体的请求，请求会直接到这里而不会经过DispatcherServlet

        //添加自定义的Listener
        servletContext.addListener(MyListener.class);

    }

    /**
     * 配置文件上传解析器的一些设置
     */
    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
        //上传文件存储磁盘路径 -- src/main/resources/uploads
        //上传文件的最大容量 -- 2M
        //整个multipart请求的最大容量 --4M
        //在上传过程中，当文件大小达到一定阈值后将会写入到临时文件路径中
        registration.setMultipartConfig(new MultipartConfigElement("D://uploads", 2097152, 4194304, 0));
    }
}
