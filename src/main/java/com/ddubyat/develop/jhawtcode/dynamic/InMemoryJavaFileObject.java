package com.ddubyat.develop.jhawtcode.dynamic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import java.net.URI;

/**
 * JavaClassObject contains its own code in a string versus a file
 *
 * @author dwtalk
 * @version 1.0.1
 * @since 2014-07-15
 */
public class InMemoryJavaFileObject extends SimpleJavaFileObject implements JavaFileObject {

    private static Logger log = LoggerFactory.getLogger(InMemoryJavaFileObject.class);

    private CharSequence content;

    /**
     * Constructor to initialize uri of object from string
     *
     * @param className file name
     * @param content code of file
     */
    public InMemoryJavaFileObject(String className, CharSequence content) {
        super(URI.create("string:///" + className.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
        this.content = content;
        log.debug("Creating class {} with code content", className.replace('.', '/'));
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return content;
    }
}
