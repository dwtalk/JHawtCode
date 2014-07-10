package com.ddubyat.develop.jhawtcode.dynamic;

import org.springframework.context.ApplicationContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface DynamicRuntimeCode {

    public String doCode(ApplicationContext applicationContext, HttpServletRequest request, HttpServletResponse response);

}
