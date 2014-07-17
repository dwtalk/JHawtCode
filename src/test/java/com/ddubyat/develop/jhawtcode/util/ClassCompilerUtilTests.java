package com.ddubyat.develop.jhawtcode.util;

import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;


/**
 * ClassCompilerUtilTests tests the compile ability of the code with a simple test class in a new class loader
 *
 * @author dwtalk
 * @version 1.0.1
 * @since 2014-07-16
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ComponentScan(basePackages = {"com.ddubyat.develop.jhawtcode"})
@ContextConfiguration("classpath:test-config.xml")
public class ClassCompilerUtilTests {

    @Autowired
    ClassCompilerUtil classCompilerUtil;

    @Test
    public void testCompileJava() throws Exception {
        // Create a new class loader with the directory
        ClassLoader ucl = new URLClassLoader(new URL[]{(new File("")).toURI().toURL()});
        Class testClass = classCompilerUtil.compileNewClass("TestCode", "", "public class TestCode { public void doCode() { return; } }", ucl);
        Assert.assertNotNull(testClass);
        Assert.assertEquals(testClass.getName(), "TestCode");
    }
}
