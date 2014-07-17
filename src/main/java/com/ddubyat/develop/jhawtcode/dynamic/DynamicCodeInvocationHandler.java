package com.ddubyat.develop.jhawtcode.dynamic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.proxy.InvocationHandler;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * DynamicCodeInvocationHandler is the invocation handler that will manage proxying executable class
 *
 * @author dwtalk
 * @version 1.0.1
 * @since 2014-07-15
 */
public class DynamicCodeInvocationHandler implements InvocationHandler {

    private static Logger log = LoggerFactory.getLogger(DynamicCodeInvocationHandler.class);

    private Class dynaCode;

    /**
     * Constructor to help create proxy
     *
     * @param dynaClass Class to be proxied
     */
    public DynamicCodeInvocationHandler(Class dynaClass) {
        this.dynaCode = dynaClass;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
        try {
            //log.debug("Invoke called on Object: {} for Method: {}", o.toString(), method.toString());
            Object dynacode = this.dynaCode.newInstance();
            return method.invoke(dynacode, args);
        } catch (Exception e) {
            return null;
        }
    }
}
