package com.ddubyat.develop.jhawtcode.util;

import jodd.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * PropertyUtil is used to set system properties and check system property for ability to execute dynamic code
 *
 * @author dwtalk
 * @version 1.0
 * @since 2014-07-15
 */
@Service
public class PropertyUtil {

    private static Logger log = LoggerFactory.getLogger(PropertyUtil.class);

    /**
     * Service method to set a system property during runtime
     * @param propertyKey The system property key to set
     * @param propertyValue The system property value to set
     * @return The value of the set system property
     */
    public String setProp(String propertyKey, String propertyValue) {
        log.debug("System Property {} set to {}", propertyKey, propertyValue);
        return System.setProperty(propertyKey.trim(), propertyValue.trim());
    }

    /**
     * Internal check to see if dynamic code execution is enabled
     * @return true if enabled or false if disabled
     */
    public Boolean canHawtTheCode() {
        String enableProp = System.getProperty("jhawtcode.enabled");
        log.trace("Check for jhawtenabled: {}", enableProp);
        if(enableProp != null && StringUtil.isNotEmpty(enableProp) && enableProp.equalsIgnoreCase("ICERTIFYTHISISNOTPROD")) {
            return true;
        }
        return false;
    }

}
