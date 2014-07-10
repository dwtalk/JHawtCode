package com.ddubyat.develop.jhawtcode.dynamic;

import javax.tools.JavaFileObject;
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

class PackageDeconstructor {
    private ClassLoader classLoader;
    private static final String CLASS_FILE_EXTENSION = ".class";

    public PackageDeconstructor(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public List<JavaFileObject> find(String packageName) throws IOException {
        String javaPackageName = packageName.replaceAll("\\.", "/");
        List<JavaFileObject> result = new ArrayList<>();
        Enumeration<URL> urlEnumeration = classLoader.getResources(javaPackageName);
        while (urlEnumeration.hasMoreElements()) {
            URL packageFolderURL = urlEnumeration.nextElement();
            result.addAll(classList(packageName, packageFolderURL));
        }
        return result;
    }

    private Collection<JavaFileObject> classList(String packageName, URL packageFolderURL) {
        if ((new File(packageFolderURL.getFile())).isDirectory()) {
            return processDir(packageName, (new File(packageFolderURL.getFile())));
        } else {
            return processJar(packageFolderURL);
        }
    }

    private List<JavaFileObject> processJar(URL packageFolderURL) {
        List<JavaFileObject> result = new ArrayList<>();
        try {
            String jarUri = packageFolderURL.toExternalForm().split("!")[0];
            JarURLConnection jarConn = (JarURLConnection) packageFolderURL.openConnection();
            String rootEntryName = jarConn.getEntryName();
            int rootEnd = rootEntryName.length()+1;
            Enumeration<JarEntry> entryEnum = jarConn.getJarFile().entries();
            while (entryEnum.hasMoreElements()) {
                JarEntry jarEntry = entryEnum.nextElement();
                String name = jarEntry.getName();
                if (name.startsWith(rootEntryName) && name.indexOf('/', rootEnd) == -1 && name.endsWith(CLASS_FILE_EXTENSION)) {
                    URI uri = URI.create(jarUri + "!/" + name);
                    String binaryName = name.replaceAll("/", ".");
                    binaryName = binaryName.replaceAll(CLASS_FILE_EXTENSION + "$", "");
                    result.add(new CustomJavaFileObject(binaryName, uri));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Unable to open jar: " + packageFolderURL, e);
        }
        return result;
    }

    private List<JavaFileObject> processDir(String packageName, File directory) {
        List<JavaFileObject> result = new ArrayList<>();
        for (File childFile : directory.listFiles()) {
            if (childFile.isFile()) {
                if (childFile.getName().endsWith(CLASS_FILE_EXTENSION)) {
                    String binaryName = packageName + "." + childFile.getName();
                    binaryName = binaryName.replaceAll(CLASS_FILE_EXTENSION + "$", "");
                    result.add(new CustomJavaFileObject(binaryName, childFile.toURI()));
                }
            }
        }
        return result;
    }
}
