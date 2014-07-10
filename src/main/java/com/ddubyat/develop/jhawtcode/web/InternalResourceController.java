package com.ddubyat.develop.jhawtcode.web;

import com.ddubyat.develop.jhawtcode.util.PropertyUtil;
import com.ddubyat.develop.jhawtcode.util.ResourceUtil;
import jodd.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class InternalResourceController {

    @Autowired
    private ResourceUtil resourceUtil;

    @Autowired
    private PropertyUtil propertyUtil;

    @ResponseBody
    @RequestMapping(value = "/jhawtcode/jhc.css", method = {RequestMethod.GET})
    public byte[] requestCSS(HttpServletResponse response) throws IOException {
        response.setHeader("Content-Encoding", "gzip");
        response.setHeader("Server", "jhawtconsole");
        response.setContentType("text/css");
        response.setHeader("Cache-Control", "no-cache,no-store,must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", -1);

        if(!propertyUtil.canHawtTheCode()) {
            return (new String("")).getBytes();
        }

        String heightOriginal = "height: 200px;";
        String heightOverride = heightOriginal;
        String sysHeightProp = System.getProperty("jhawtcode.console.height");
        if(sysHeightProp != null && StringUtil.isNotEmpty(sysHeightProp) && isNumeric(sysHeightProp)) {
            heightOverride = heightOriginal.replaceAll("200", sysHeightProp);
        }

        try {
            return resourceUtil.gzipCompress(resourceUtil.readLocalResource("classpath:/css/jhawtcode.css").replaceAll(heightOriginal, heightOverride));
        } catch (IOException ioe) {
            return ioe.getLocalizedMessage().getBytes("UTF-8");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/jhawtcode/jhc.js", method = {RequestMethod.GET})
    public byte[] requestJS(HttpServletResponse response) throws IOException {
        response.setHeader("Content-Encoding", "gzip");
        response.setHeader("Server", "jhawtconsole");
        response.setContentType("application/javascript");
        response.setHeader("Cache-Control", "no-cache,no-store,must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", -1);

        if(!propertyUtil.canHawtTheCode()) {
            return (new String("")).getBytes();
        }

        try {
            return resourceUtil.gzipCompress(resourceUtil.readLocalResource(new String[]{"classpath:/js/jhawtcode.js"}));
        } catch (IOException ioe) {
            return ioe.getLocalizedMessage().getBytes("UTF-8");
        }

    }

    private static boolean isNumeric(String str) {
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }


}
