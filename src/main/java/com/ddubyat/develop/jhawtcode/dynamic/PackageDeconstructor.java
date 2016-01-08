package com.ddubyat.develop.jhawtcode.dynamic;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;

import javax.tools.JavaFileObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jodd.util.StringUtil;

/**
 * PackageDeconstructor retrieves classes from classloader
 *
 * @author dwtalk
 * @version 1.0.4
 * @since 2014-07-15
 */
class PackageDeconstructor {
    private ClassLoader classLoader;
    private static final String CLASS_EXTENSION = ".class";
    private static Logger log = LoggerFactory.getLogger(PackageDeconstructor.class);

    /**
     * Construct the package deconstructor with the container classloader
     *
     * @param classLoader classloader of container
     */
    public PackageDeconstructor(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    /**
     * Utility method to get list of classes from a package name
     *
     * @param packageName the class we seek
     * @return List of class
     * @throws IOException
     */
    public List<JavaFileObject> find(String packageName) throws IOException {
        String javaPackageName = packageName.replaceAll("\\.", "/");
        List<JavaFileObject> result = new ArrayList<>();

        if(classLoader != null) {
            Enumeration<URL> urlEnumeration = classLoader.getResources(javaPackageName);
            log.trace("Getting resources for: {}", javaPackageName);
            while (urlEnumeration.hasMoreElements()) {
                URL packageFolderURL = urlEnumeration.nextElement();
                result.addAll(classList(packageName, packageFolderURL));
            }
        }
        return result;
    }

    /**
     * Generate a list of files from a given package and location
     *
     * @param packageName jar name or directory name
     * @param packageFolderURL location of jar
     * @return Collection of files
     */
    private Collection<JavaFileObject> classList(String packageName, URL packageFolderURL) {
        List<JavaFileObject> result = new ArrayList<>();

        if(StringUtil.isEmpty(packageName)) {
            return result;
        }

        if ((new File(packageFolderURL.getFile())).isDirectory()) {
            log.trace("Processing directory {} for package {}", packageFolderURL.getFile().toString(), packageName);

            for (File childFile : (new File(packageFolderURL.getFile())).listFiles()) {
                if (childFile.isFile()) {
                    if (childFile.getName().endsWith(CLASS_EXTENSION)) {
                        String binaryName = packageName + "." + childFile.getName();
                        log.trace("Class File Found: {}", binaryName);
                        binaryName = binaryName.replaceAll(CLASS_EXTENSION + "$", "");
                        result.add(new CustomJavaFileObject(binaryName, childFile.toURI()));
                    }
                }
            }
            return result;
        } else {
            log.trace("Processing jar {}", packageFolderURL.toString());

            try {
                String jarUri = packageFolderURL.toExternalForm().split("!")[0];
                log.trace("Jar file to search: {}", jarUri);
                JarURLConnection jarConn = (JarURLConnection) packageFolderURL.openConnection();
                String rootEntryName = jarConn.getEntryName();
                log.trace("Jar root: {}", rootEntryName);
                int rootEnd = rootEntryName.length()+1;
                Enumeration<JarEntry> entryEnum = jarConn.getJarFile().entries();
                while (entryEnum.hasMoreElements()) {
                    JarEntry jarEntry = entryEnum.nextElement();
                    String name = jarEntry.getName();
                    if (name.startsWith(rootEntryName) && name.indexOf('/', rootEnd) == -1 && name.endsWith(CLASS_EXTENSION)) {
                        URI uri = URI.create(jarUri + "!/" + name);
                        String binaryName = name.replaceAll("/", ".");
                        log.trace("Class File Found: {}", binaryName);
                        binaryName = binaryName.replaceAll(CLASS_EXTENSION + "$", "");
                        result.add(new CustomJavaFileObject(binaryName, uri));
                    }
                }
            } catch (Exception e) {
                log.trace("Jar open errors", e);
                throw new RuntimeException("Unable to open jar: " + packageFolderURL, e);
            }
            return result;
        }
    }
}
