package com.ddubyat.develop.jhawtcode.web;

import org.hamcrest.core.IsNot;
import org.hamcrest.core.IsNull;
import org.hamcrest.text.IsEmptyString;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.web.context.WebApplicationContext;


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
public class ConsoleTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void getCSS() throws Exception {
        ResultActions actions = this.mockMvc.perform(get("/jhawtcode/jhc.css").accept(MediaType.TEXT_PLAIN));
        //actions.andDo(print());
        actions.andExpect(status().isOk());
        actions.andExpect(content().contentType(MediaType.TEXT_PLAIN));
        //not the best content test, but works for now until gzip deflate
        actions.andExpect(content().string(new IsNot(new IsNull())));
        actions.andExpect(content().string(new IsNot(new IsEmptyString())));
    }

    @Test
    public void getJS() throws Exception {
        ResultActions actions = this.mockMvc.perform(get("/jhawtcode/jhc.js").accept(MediaType.ALL));
        //actions.andDo(print());
        actions.andExpect(status().isOk());
        actions.andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM));
        //not the best content test, but works for now until gzip deflate
        actions.andExpect(content().string(new IsNot(new IsNull())));
        actions.andExpect(content().string(new IsNot(new IsEmptyString())));
    }

}
