package com.ddubyat.develop.jhawtcode.util;

import jodd.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class PropertyUtil {

    @Autowired
    private ApplicationContext applicationContext;

    public String setProp(String propertyKey, String propertyValue) {
        return System.setProperty(propertyKey.trim(), propertyValue.trim());
    }

    public Boolean canHawtTheCode() {
        String enableProp = System.getProperty("jhawtcode.enabled");
        if(enableProp != null && StringUtil.isNotEmpty(enableProp) && enableProp.equalsIgnoreCase("ICERTIFYTHISISNOTPROD")) {
            return true;
        }
        return false;
    }

}
