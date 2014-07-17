package com.ddubyat.develop.jhawtcode.util;

import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;

/**
 * ResourceUtil tests
 *
 * @author dwtalk
 * @version 1.0.1
 * @since 2014-07-16
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ComponentScan(basePackages = {"com.ddubyat.develop.jhawtcode"})
@ContextConfiguration("classpath:test-config.xml")
public class ResourceUtilTests {

    @Autowired
    ResourceUtil resourceUtil;

    @Test
    public void testReadLocalResource() throws IOException {
        String testFile = resourceUtil.readLocalResource("classpath:/css/jhawtcode.css");
        Assert.assertNotNull(testFile);
        Assert.assertTrue(testFile.contains("#jhc {"));
    }

    @Test
    public void testReadLocalResources() throws IOException {
        String[] res = new String[]{"classpath:/css/jhawtcode.css","classpath:/js/jhawtcode.js"};
        String testFiles = resourceUtil.readLocalResource(res);
        Assert.assertNotNull(testFiles);
        Assert.assertTrue(testFiles.contains("#jhc {"));
        Assert.assertTrue(testFiles.contains("jQuery.fn"));
    }

    @Test
    public void testCompress() throws IOException {
        byte[] tb = resourceUtil.gzipCompress("a");
         Assert.assertEquals(Arrays.toString(tb), "[31, -117, 8, 0, 0, 0, 0, 0, 0, 0, 75, 4, 0, 67, -66, -73, -24, 1, 0, 0, 0]");
    }

}
