package com.ddubyat.develop.jhawtcode.dynamic;

import org.springframework.context.ApplicationContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * DynamicRuntimeCode is an interface for dynamic invocation
 *
 * @author dwtalk
 * @version 1.0
 * @since 2014-07-15
 */
public interface DynamicRuntimeCode {

    /**
     * Method that contains basic elements to interact with a spring app
     *
     * @param applicationContext Spring Context
     * @param request Spring Controller Request
     * @param response Spring Controller Response
     * @return Code Output from execution
     */
    public String doCode(ApplicationContext applicationContext, HttpServletRequest request, HttpServletResponse response);

}
