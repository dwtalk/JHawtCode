package com.ddubyat.develop.jhawtcode.dynamic;

import org.springframework.cglib.proxy.InvocationHandler;

import java.lang.reflect.Method;

/**
 * Created by dtalk on 1/28/14.
 */
public class DynamicCodeInvocationHandler implements InvocationHandler {

    private Class dynaCode;

    public DynamicCodeInvocationHandler(Class dynaClass) {
        this.dynaCode = dynaClass;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
        try {
            Object dynacode = this.dynaCode.newInstance();
            return method.invoke(dynacode, args);
        } catch (Exception e) {
            return null;
        }
    }
}
