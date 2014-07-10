package com.ddubyat.develop.jhawtcode.util;

import com.ddubyat.develop.jhawtcode.dynamic.DynamicClassLoaderJavaFileManager;
import com.ddubyat.develop.jhawtcode.dynamic.InMemoryJavaFileObject;
import org.springframework.stereotype.Service;
import jodd.util.ClassLoaderUtil;

import javax.tools.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Service
public class ClassCompilerUtil {

    public Class compileNewClass(String fileName, String classPath, String source, ClassLoader loader) throws Exception {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();

        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);
        JavaFileManager javaFileManager = new DynamicClassLoaderJavaFileManager(loader, fileManager);

        String fullClassPath = System.getProperty("java.class.path");
        File[] allClassPath = ClassLoaderUtil.getDefaultClasspath(loader);
        for(File f : allClassPath) {
            fullClassPath += ":" + f.getAbsolutePath();
        }

        List<String> optionList = new ArrayList<>();
        optionList.addAll(Arrays.asList("-classpath", fullClassPath));

        List<JavaFileObject> sourceJavaFiles = new ArrayList<>();
        sourceJavaFiles.add(new InMemoryJavaFileObject(fileName, source));

        JavaCompiler.CompilationTask ct = compiler.getTask(null, javaFileManager, diagnostics, optionList, null, sourceJavaFiles);
        ct.call();
        javaFileManager.close();
        fileManager.close();

        String compileErrors = "Compiler Exception" + System.lineSeparator();
        Boolean hasErrors = Boolean.FALSE;

        if(diagnostics.getDiagnostics().size() > 0) {
            for (Diagnostic diagnostic : diagnostics.getDiagnostics()) {
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
                throw new Exception(compileErrors);
            }
        }

        return javaFileManager.getClassLoader(null).loadClass(classPath.replaceAll("/",".") + fileName);
    }
}
