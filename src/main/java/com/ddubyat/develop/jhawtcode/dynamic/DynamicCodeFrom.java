package com.ddubyat.develop.jhawtcode.dynamic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
//JHC-IMPORTS//
//JHC-FILE-IMPORTS//

/**
 * DynamicCodeFrom is a template class for dynamic invocation
 *
 * @author dwtalk
 * @version 1.0.1
 * @since 2014-07-15
 */
public class DynamicCodeFrom implements com.ddubyat.develop.jhawtcode.dynamic.DynamicRuntimeCode {

    private static Logger log = LoggerFactory.getLogger(DynamicCodeFrom.class);

    //JHC-GLOBALS//

    WebApplicationContext webApplicationContext;
    ApplicationContext applicationContext;
    HttpServletRequest request;
    HttpServletResponse response;

    /**
     * Method called for dynamic code
     *
     * @param applicationContext client applications context
     * @param request incoming request via spring
     * @param response outgoing response via spring
     * @return output from jhc printstream
     */
    public String doCode(ApplicationContext applicationContext, HttpServletRequest request, HttpServletResponse response) {

        log.debug("Proxy for Dynamic Code Invocation Initiated");

        //configure the vars for helper methods and shortcuts
        this.webApplicationContext = (WebApplicationContext)applicationContext;
        this.applicationContext = applicationContext;
        this.request = request;
        this.response = response;

        //setup the print stream
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintWriter jhc = new PrintWriter(baos);

        try {

            //JHC-CODE//

        } catch (Throwable t) {
            log.error("Dynamic Code doProxy Error", t);
        }

        jhc.flush();
        jhc.close();

        log.debug("Proxy for Dynamic Code Invocation Initiated");
        String outputCode = baos.toString();
        log.debug("Code generated output: {}", outputCode);

        return outputCode;
    }

    private Object getBean(String beanName) {
        return webApplicationContext.getBean(beanName);
    }

    private HttpSession getSession() {
        return request.getSession();
    }

    private Cookie getCookie(String cookieName) {
        Cookie[] requestCookies = request.getCookies();
        if(requestCookies!=null) {
            for(Cookie c : requestCookies) {
                if(c.getName().equalsIgnoreCase(cookieName)) {
                    return c;
                }
            }
        }
        return null;
    }

    //JHC-METHODS//
    //JHC-FILE-METHODS//

}