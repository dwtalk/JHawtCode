package com.ddubyat.develop.jhawtcode.dynamic;

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

public class DynamicCodeFrom implements com.ddubyat.develop.jhawtcode.dynamic.DynamicRuntimeCode {

    //JHC-GLOBALS//
    WebApplicationContext webApplicationContext;
    ApplicationContext applicationContext;
    HttpServletRequest request;
    HttpServletResponse response;

    public String doCode(ApplicationContext applicationContext, HttpServletRequest request, HttpServletResponse response) {

        //configure the vars for helper methods and shortcuts
        this.webApplicationContext = (WebApplicationContext)applicationContext;
        this.applicationContext = applicationContext;
        this.request = request;
        this.response = response;

        //setup the print stream
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintWriter jhc = new PrintWriter(baos);

        //JHC-CODE//

        jhc.flush();
        jhc.close();
        return baos.toString();
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