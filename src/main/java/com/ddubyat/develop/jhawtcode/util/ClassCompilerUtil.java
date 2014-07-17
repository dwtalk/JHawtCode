package com.ddubyat.develop.jhawtcode.util;

import com.ddubyat.develop.jhawtcode.dynamic.DynamicClassLoaderJavaFileManager;
import com.ddubyat.develop.jhawtcode.dynamic.InMemoryJavaFileObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import jodd.util.ClassLoaderUtil;

import javax.tools.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * ClassCompilerUtil compiles a file from string contents then load it into the current classloader using the extended classpath available to the container
 *
 * @author dwtalk
 * @version 1.0.1
 * @since 2014-07-15
 */
@Service
public class ClassCompilerUtil {

    private static Logger log = LoggerFactory.getLogger(ClassCompilerUtil.class);

    /**
     * Utility function to compile a class in memory on server
     *
     * @param fileName the filename of the class to compile
     * @param classPath the classpath of the file
     * @param source the string source code of the class
     * @param loader the web application url class loader
     * @return A compiled class or null
     * @throws Exception
     */
    public Class compileNewClass(String fileName, String classPath, String source, ClassLoader loader) throws Exception {
        log.debug("Compiling class {} for classpath {}", fileName, classPath);

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();

        log.debug("In Memory File Manager Setup");
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);
        JavaFileManager javaFileManager = new DynamicClassLoaderJavaFileManager(loader, fileManager);

        String fullClassPath = System.getProperty("java.class.path");
        File[] allClassPath = ClassLoaderUtil.getDefaultClasspath(loader);
        for(File f : allClassPath) {
            fullClassPath += ":" + f.getAbsolutePath();
        }

        log.trace("Full classpath of the container is: {}", fullClassPath);

        List<String> optionList = new ArrayList<>();
        optionList.addAll(Arrays.asList("-classpath", fullClassPath));

        log.debug("Java Source File Created in Memory");
        List<JavaFileObject> sourceJavaFiles = new ArrayList<>();
        sourceJavaFiles.add(new InMemoryJavaFileObject(fileName, source));

        log.debug("Compilation Beginning");
        JavaCompiler.CompilationTask ct = compiler.getTask(null, javaFileManager, diagnostics, optionList, null, sourceJavaFiles);

        try {
            ct.call();
        } catch (Throwable t) {
            log.debug("Could not compile", t);
            return null;
        }

        javaFileManager.close();
        fileManager.close();
        log.debug("Compilation Completed");

        String compileErrors = "Compiler Exception" + System.lineSeparator();
        Boolean hasErrors = Boolean.FALSE;

        if(diagnostics.getDiagnostics().size() > 0) {
            log.debug("Diagnostics Generated during compilation");
            for (Diagnostic diagnostic : diagnostics.getDiagnostics()) {
                log.debug("Diagnostic on line: {} column: {} with message {}", diagnostic.getLineNumber(), diagnostic.getColumnNumber(), diagnostic.getMessage(Locale.ENGLISH));
                if(diagnostic.getKind().equals(Diagnostic.Kind.ERROR)) {
                    compileErrors += "  Error on line: " + diagnostic.getLineNumber() + " column: " + diagnostic.getColumnNumber() +
                            System.lineSeparator() + "   " + diagnostic.getMessage(Locale.ENGLISH);
                    System.out.println(compileErrors);
                    hasErrors = Boolean.TRUE;
                } else {
                    System.out.println("Compiler Diagnostic Message: " + diagnostic.getLineNumber() + " " + diagnostic.getMessage(Locale.ENGLISH));
                }
            }
            if(hasErrors.booleanValue()) {
                log.debug("Compilation Failed due to Errors");
                throw new Exception(compileErrors);
            }
        }

        return javaFileManager.getClassLoader(null).loadClass(classPath.replaceAll("/",".") + fileName);
    }
}
