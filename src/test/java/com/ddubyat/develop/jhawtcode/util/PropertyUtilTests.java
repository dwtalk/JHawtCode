package com.ddubyat.develop.jhawtcode.util;

import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * PropertyUtil tests
 *
 * @author dwtalk
 * @version 1.0.0
 * @since 2014-07-16
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ComponentScan(basePackages = {"com.ddubyat.develop.jhawtcode"})
@ContextConfiguration("classpath:test-config.xml")
public class PropertyUtilTests {

    @Autowired
    PropertyUtil propertyUtil;

    @Test
    public void testSetProp() {
        propertyUtil.setProp("key", "value");
        Assert.assertEquals("value", System.getProperty("key"));
    }

    @Test
    public void testEnabled() {
        Assert.assertTrue(propertyUtil.canHawtTheCode());
    }

}
