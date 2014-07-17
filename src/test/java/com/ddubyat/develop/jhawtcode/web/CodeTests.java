package com.ddubyat.develop.jhawtcode.web;

import jodd.util.ClassLoaderUtil;
import jodd.util.StringUtil;
import org.hamcrest.core.IsNot;
import org.hamcrest.core.IsNull;
import org.hamcrest.text.IsEmptyString;
import org.hamcrest.text.IsEqualIgnoringWhiteSpace;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.net.MalformedURLException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * ConsoleTests is a test for css, js, and JHawtCode
 *
 * @author dwtalk
 * @version 1.0.1
 * @since 2014-07-16
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("classpath:test-config.xml")
@ComponentScan(basePackages = {"com.ddubyat.develop.jhawtcode"})
@ImportResource("classpath*:jhawtcode-applicationContext.xml")
public class CodeTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private static String fullClassPath = "";

    @Before
    public void setup() throws MalformedURLException {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        setFullClassPath();
    }

    private void setFullClassPath() throws MalformedURLException {
        if(StringUtil.isEmpty(fullClassPath)) {
            File[] allClassPath = ClassLoaderUtil.getDefaultClasspath(Thread.currentThread().getContextClassLoader());
            for(File f : allClassPath) {
                fullClassPath += "!" + f.toURI().toURL().toString();
            }
        }
    }

    @Test
    public void runCodeSimple() throws Exception {
        ResultActions actions = this.mockMvc.perform(post("/jhawtcode/dynacode").param("code", "jhc.println(true);").param("replacementCP", fullClassPath));
        //actions.andDo(print());
        actions.andExpect(status().isOk());
        //not the best content test, but works for now until gzip deflate
        actions.andExpect(content().string(new IsNot(new IsNull())));
        actions.andExpect(content().string(new IsNot(new IsEmptyString())));
        actions.andExpect(content().string(new IsEqualIgnoringWhiteSpace("true")));
    }

    @Test
    public void runCodeSimpleWithImport() throws Exception {
        ResultActions actions = this.mockMvc.perform(post("/jhawtcode/dynacode").param("code", "jhc.println(Math.pow(3,3));").param("replacementCP", fullClassPath).param("imports", "import java.lang.Math;"));
        //actions.andDo(print());
        actions.andExpect(status().isOk());
        //not the best content test, but works for now until gzip deflate
        actions.andExpect(content().string(new IsNot(new IsNull())));
        actions.andExpect(content().string(new IsNot(new IsEmptyString())));
        actions.andExpect(content().string(new IsEqualIgnoringWhiteSpace("27.0")));
    }

    @Test
    public void runCodeSimpleWithImportAndMethod() throws Exception {
        ResultActions actions = this.mockMvc.perform(post("/jhawtcode/dynacode").param("code", "jhc.println(Math.pow(3,addem(2,1)));").param("replacementCP", fullClassPath).param("imports", "import java.lang.Math;").param("methods", "public int addem(int a, int b) { return a+b; }"));
        //actions.andDo(print());
        actions.andExpect(status().isOk());
        //not the best content test, but works for now until gzip deflate
        actions.andExpect(content().string(new IsNot(new IsNull())));
        actions.andExpect(content().string(new IsNot(new IsEmptyString())));
        actions.andExpect(content().string(new IsEqualIgnoringWhiteSpace("27.0")));
    }

    @Test
    public void runCodeSimpleWithImportAndMethodAndGlobal() throws Exception {
        ResultActions actions = this.mockMvc.perform(post("/jhawtcode/dynacode").param("code", "jhc.println(Math.pow(3,addem(1,1)));").param("replacementCP", fullClassPath).param("imports", "import java.lang.Math;").param("methods", "public int addem(int a, int b) { return a+b+MYVAR; }").param("globals", "private int MYVAR=1;"));
        //actions.andDo(print());
        actions.andExpect(status().isOk());
        //not the best content test, but works for now until gzip deflate
        actions.andExpect(content().string(new IsNot(new IsNull())));
        actions.andExpect(content().string(new IsNot(new IsEmptyString())));
        actions.andExpect(content().string(new IsEqualIgnoringWhiteSpace("27.0")));
    }

}
