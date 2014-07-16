package com.ddubyat.develop.jhawtcode.web;

import com.ddubyat.develop.jhawtcode.dynamic.DynamicCodeInvocationHandler;
import com.ddubyat.develop.jhawtcode.dynamic.DynamicRuntimeCode;
import com.ddubyat.develop.jhawtcode.util.ClassCompilerUtil;
import com.ddubyat.develop.jhawtcode.util.PropertyUtil;
import com.ddubyat.develop.jhawtcode.util.ResourceUtil;
import jodd.util.ClassLoaderUtil;
import jodd.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.cglib.proxy.Proxy;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * InternalResourceController is a Spring Controller that will serve up css and js for JHawtCode
 *
 * @author dwtalk
 * @version 1.0
 * @since 2014-07-15
 */
@Controller
public class DynamicCodeController {

    private static Logger log = LoggerFactory.getLogger(DynamicCodeController.class);

    @Autowired
    private ClassCompilerUtil classCompilerUtil;

    @Autowired
    private ResourceUtil resourceUtil;

    @Autowired
    private PropertyUtil propertyUtil;

    @Autowired
    private ApplicationContext applicationContext;

    private static String templateClassName = "DynamicCodeFrom";
    private static String templateClassPathPrefix = "com/ddubyat/develop/jhawtcode/dynamic/";
    private static String templateClassNamePostfix = templateClassName + ".java";

    /**
     * Method to give version of application
     *
     * @return version of application
     */
    @ResponseBody
    @RequestMapping(value = "/jhawtcode", method = {RequestMethod.GET})
    public String version() {
        if(!propertyUtil.canHawtTheCode()) {
            log.debug("Cannot give version since not enabled");
            return (new String(""));
        }

        return "1.0.0";
    }

    /**
     * Controller method that will accept java code for compilation and execution
     *
     * @param request Spring Controller Parameter for HttpRequest
     * @param response Spring Controller Parameter for HttpResponse
     * @param code Java code to be executed
     * @param imports Java imports to add to code
     * @param globals Java global variables to add to code
     * @param methods Java methods to add to code
     * @return Output from code jhc printstream or messaging
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @ResponseBody
    @RequestMapping(value = "/jhawtcode/dynacode", method = {RequestMethod.POST})
    public String runCode(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = true, value = "code") String code, @RequestParam(required = false, value = "imports") String imports, @RequestParam(required = false, value = "globals") String globals, @RequestParam(required = false, value = "methods") String methods) throws IOException, ClassNotFoundException {
        if(!propertyUtil.canHawtTheCode()) {
            log.debug("Cannot run code since not enabled");
            return (new String(""));
        }

        log.debug("Template classto be read: classpath: " + templateClassPathPrefix + templateClassNamePostfix);
        String templateClass = resourceUtil.readLocalResource("classpath:" + templateClassPathPrefix + templateClassNamePostfix);
        templateClass = templateClass.replace(templateClassName, templateClassName + "Console");
        templateClass = templateClass.replace("//JHC-CODE//", code + System.lineSeparator());
        templateClass = templateClass.replace("//JHC-IMPORTS//", imports + System.lineSeparator());
        templateClass = templateClass.replace("//JHC-GLOBALS//", globals + System.lineSeparator());
        templateClass = templateClass.replace("//JHC-METHODS//", methods + System.lineSeparator());

        String appendCodeFile = System.getProperty("jhawtcode.appendCodeFile");
        log.debug("appendCodeFile: {}", appendCodeFile);

        if(appendCodeFile!=null && StringUtil.isNotEmpty(appendCodeFile)) {
            try {
                File codeFile = ResourceUtils.getFile(appendCodeFile);
                String codeContents = new Scanner(codeFile).useDelimiter("\\Z").next();
                String importContents = "";
                Pattern importPattern = Pattern.compile(".*(import\\s[a-zA-Z0-9_\\.*]+;).*");
                Matcher importMatcher = importPattern.matcher(codeContents);

                while (importMatcher.find()) {
                    importContents += importMatcher.group() + System.lineSeparator();
                }
                codeContents = importMatcher.replaceAll(System.lineSeparator());

                templateClass = templateClass.replace("//JHC-FILE-IMPORTS//", importContents + System.lineSeparator());
                templateClass = templateClass.replace("//JHC-FILE-METHODS//", codeContents + System.lineSeparator());

                log.trace("AppendFile Imports: {}", importContents);
                log.trace("AppendFile Methods: {}", codeContents);
            } catch (Exception e) {
                log.debug("AppendFile not read");
                return "Cannot load code append file from disk";
            }
        }

        String compilationError = "";
        URLClassLoader ucl = (URLClassLoader) Thread.currentThread().getContextClassLoader();
        log.debug("Using classloader {}", ucl.toString());
        Class newDynamicClass = null;

        try {
            newDynamicClass = classCompilerUtil.compileNewClass(templateClassName + "Console", templateClassPathPrefix, templateClass, ucl);
        } catch (Exception e) {
            compilationError = e.getLocalizedMessage();
            log.debug("Compilation exception", e);
        }

        if(newDynamicClass != null) {
            InvocationHandler handler = new DynamicCodeInvocationHandler(newDynamicClass);
            DynamicRuntimeCode proxy = (DynamicRuntimeCode) Proxy.newProxyInstance(DynamicRuntimeCode.class.getClassLoader(), new Class[]{DynamicRuntimeCode.class}, handler);
            try {
                log.debug("Invoking Dynamic Class");
                String runtimeResponse = proxy.doCode(applicationContext, request, response);
                log.debug("Class call completed");
                if(runtimeResponse == null) {
                    runtimeResponse = "null";
                    log.debug("null response from code");
                }
                log.debug("Returning response: {}", runtimeResponse);
                return runtimeResponse;
            } catch (Throwable t) {
                log.debug("Runtime throwable", t);
                return "Runtime Exception: " + t.getLocalizedMessage();
            }
        }

        return compilationError;
    }

    /**
     * Set a system property controller endpoint
     *
     * @param propkey Property key to set
     * @param value Property value to  set
     * @return Set value
     */
    @ResponseBody
    @RequestMapping(value = "/jhawtcode/dynaprop", method = {RequestMethod.POST})
    public String setProp(@RequestParam(required = true, value = "propkey") String propkey, @RequestParam(required = true, value = "value") String value) {
        if(!propertyUtil.canHawtTheCode()) {
            log.trace("Cannot load property due to not being enabled");
            return (new String(""));
        }

        return propertyUtil.setProp(propkey, value);
    }

    /**
     * Controller mapping to load local jar file
     *
     * @param url Url of the jar file to be loaded
     * @return exception message or success message
     * @throws MalformedURLException
     */
    @ResponseBody
    @RequestMapping(value = "/jhawtcode/dynajar", method = {RequestMethod.POST})
    public String loadJar(@RequestParam(required = true, value = "url") String url) throws MalformedURLException {
        if(!propertyUtil.canHawtTheCode()) {
            log.trace("Cannot load jar due to not being enabled");
            return (new String(""));
        }

        URLClassLoader ucl = (URLClassLoader) Thread.currentThread().getContextClassLoader();
        try {
            log.debug("Attempting to add {} to class loader {}", url, ucl.toString());
            ClassLoaderUtil.addUrlToClassPath(new URL(url), ucl);
        } catch (Exception e) {
            log.debug("Unable to add to classpath", e);
            return e.getLocalizedMessage();
        }
        return "Jar File Loaded into Classpath";
    }

    /*
    @ResponseBody
    @RequestMapping(value = "/jhawtcode/createJar", method = {RequestMethod.POST})
    public String createJar(@RequestParam(required = true, value = "name") String name, @RequestParam("file") MultipartFile file) {
        if(!propertyUtil.canHawtTheCode()) {
            return (new String(""));
        }

        if(!file.isEmpty()) {
            try {
                File jarFile = new File(System.getProperty("java.io.tmpdir") + "/" + name);
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(jarFile));
                stream.write(bytes);
                stream.close();
                return "File Uploaded\n" + loadJar("file:" + jarFile.getAbsolutePath());
            } catch (Exception e) {
                return "Upload Failed: " + e.getLocalizedMessage();
            }
        } else {
            return "Upload Failed: Empty";
        }
    }
    */

}
