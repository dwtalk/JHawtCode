package com.ddubyat.develop.jhawtcode.dynamic;

import java.io.IOException;
import java.net.URLClassLoader;
import java.util.Collections;
import java.util.Set;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DynamicClassLoaderJavaFileManager is a java file manager implementation to allow in class loader class compilation
 *
 * @author dwtalk
 * @version 1.0.1
 * @since 2014-07-15
 */
public class DynamicClassLoaderJavaFileManager extends ForwardingJavaFileManager<JavaFileManager> implements JavaFileManager {
    private final ClassLoader classLoader;
    private final StandardJavaFileManager standardFileManager;
    private final PackageDeconstructor packageDeconstructor;
    private JavaClassObject javaClassObject;

    private static Logger log = LoggerFactory.getLogger(DynamicClassLoaderJavaFileManager.class);

    /**
     * Constructor for file manager using classloader
     *
     * @param classLoader the application classloader
     * @param standardFileManager the in mem file manager
     */
    public DynamicClassLoaderJavaFileManager(ClassLoader classLoader, StandardJavaFileManager standardFileManager) {
        super(standardFileManager);
        this.classLoader = classLoader;
        this.standardFileManager = standardFileManager;
        packageDeconstructor = new PackageDeconstructor(classLoader);
    }

    @Override
    public ClassLoader getClassLoader(Location location) {
        log.trace("Getting classloader for location");
        if(location==null) {
            URLClassLoader ucl = (URLClassLoader) classLoader;
            return new URLClassLoader(ucl.getURLs(), classLoader) {
                @Override
                protected Class<?> findClass(String name) throws ClassNotFoundException {
                    byte[] b = javaClassObject.getBytes();
                    return super.defineClass(name, javaClassObject.getBytes(), 0, b.length);
                }
            };
        }
        return classLoader;
    }

    @Override
    public String inferBinaryName(Location location, JavaFileObject file) {
        if (file instanceof InMemoryJavaFileObject) {
            return file.getName();
        } else if (file instanceof CustomJavaFileObject) {
            return ((CustomJavaFileObject) file).binaryName();
        } else {
            return standardFileManager.inferBinaryName(location, file);
        }
    }

    @Override
    public Iterable<JavaFileObject> list(Location location, String packageName, Set<JavaFileObject.Kind> kinds, boolean recurse) throws IOException {
        if (location == StandardLocation.PLATFORM_CLASS_PATH) {
            return standardFileManager.list(location, packageName, kinds, recurse);
        } else if (location == StandardLocation.CLASS_PATH && kinds.contains(JavaFileObject.Kind.CLASS)) {
            if (packageName.startsWith("java")) {
                return standardFileManager.list(location, packageName, kinds, recurse);
            } else {
                return packageDeconstructor.find(packageName);
            }
        }
        return Collections.emptyList();
    }

    @Override
    public JavaFileObject getJavaFileForInput(Location location, String className, JavaFileObject.Kind kind) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) throws IOException {
        javaClassObject = new JavaClassObject(className, kind);
        return javaClassObject;
    }

    @Override
    public FileObject getFileForInput(Location location, String packageName, String relativeName) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public FileObject getFileForOutput(Location location, String packageName, String relativeName, FileObject sibling) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasLocation(Location location) {
        return location == StandardLocation.CLASS_PATH || location == StandardLocation.PLATFORM_CLASS_PATH;
    }

    @Override
    public void flush() throws IOException {}

    @Override
    public void close() throws IOException {}

    @Override
    public int isSupportedOption(String option) {
        return -1;
    }
}
