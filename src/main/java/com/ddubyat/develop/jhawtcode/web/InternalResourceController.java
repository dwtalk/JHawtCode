package com.ddubyat.develop.jhawtcode.web;

import com.ddubyat.develop.jhawtcode.util.PropertyUtil;
import com.ddubyat.develop.jhawtcode.util.ResourceUtil;
import jodd.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.UUID;
import java.security.*;
import java.net.NetworkInterface;

@Controller
public class InternalResourceController {

    String systemUUID = "";

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
    public byte[] requestJS(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("Content-Encoding", "gzip");
        response.setHeader("Server", "jhawtconsole");
        response.setContentType("application/javascript");
        response.setHeader("Cache-Control", "no-cache,no-store,must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", -1);

        if(!propertyUtil.canHawtTheCode()) {
            return (new String("")).getBytes();
        }

        if(systemUUID.equalsIgnoreCase("")) {
            try {
                //generate from hardware
                Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
                while (nis.hasMoreElements()) {
                    NetworkInterface ni = nis.nextElement();
                    systemUUID += Arrays.toString(ni.getHardwareAddress());
                }
            } catch (Exception e) {
                //generate a uuid randomly for failures
                systemUUID = UUID.randomUUID().toString();
            }
            //md5 the uuid
            try {
                byte[] bytesOfMessage = systemUUID.getBytes("UTF-8");
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] thedigest = md.digest(bytesOfMessage);
                systemUUID = new BigInteger(1, thedigest).toString(16);
            } catch (Exception e) {
                systemUUID = "1234567890";
            }
        }

        String appname = "noappname";
        if(request.getSession() != null && request.getSession().getServletContext() != null && request.getSession().getServletContext().getServletContextName() != null) {
            appname = request.getServletContext().getServletContextName();
        }

        String username = "nouser";
        if(System.getProperty("user.name") != null && StringUtil.isNotEmpty(System.getProperty("user.name"))) {
            username = System.getProperty("user.name");
        }

        String license = "demo";
        if(System.getProperty("jhawtcode.license") != null && StringUtil.isNotEmpty(System.getProperty("jhawtcode.license"))) {
            license = System.getProperty("jhawtcode.license");
        }

        String javascript = resourceUtil.readLocalResource(new String[]{"classpath:/js/jhawtcode.js"});
        javascript = javascript.replace("|systemUUID|", systemUUID);
        javascript = javascript.replace("|username|", username);
        javascript = javascript.replace("|license|", license);
        javascript = javascript.replace("|appname|", appname);

        try {
            return resourceUtil.gzipCompress(javascript);
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
