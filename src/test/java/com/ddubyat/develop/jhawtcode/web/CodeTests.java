package com.ddubyat.develop.jhawtcode.web;

import jodd.util.ClassLoaderUtil;
import org.hamcrest.core.IsNot;
import org.hamcrest.core.IsNull;
import org.hamcrest.text.IsEmptyString;
import org.hamcrest.text.IsEqualIgnoringCase;
import org.hamcrest.text.IsEqualIgnoringWhiteSpace;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void runCodeSimple() throws Exception {
        String fullCP = "";
        File[] allClassPath = ClassLoaderUtil.getDefaultClasspath(Thread.currentThread().getContextClassLoader());
        for(File f : allClassPath) {
            fullCP += "!" + f.toURI().toURL().toString();
        }

        ResultActions actions = this.mockMvc.perform(post("/jhawtcode/dynacode").param("code", "jhc.println(true);").param("replacementCP", fullCP));
        actions.andDo(print());
        actions.andExpect(status().isOk());
        //not the best content test, but works for now until gzip deflate
        actions.andExpect(content().string(new IsNot(new IsNull())));
        actions.andExpect(content().string(new IsNot(new IsEmptyString())));
        actions.andExpect(content().string(new IsEqualIgnoringWhiteSpace("true")));
    }

}
