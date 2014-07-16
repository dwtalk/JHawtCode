package com.ddubyat.develop.jhawtcode.dynamic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.tools.SimpleJavaFileObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

/**
 * JavaClassObject contains its own code versus a file
 *
 * @author dwtalk
 * @version 1.0
 * @since 2014-07-15
 */
public class JavaClassObject extends SimpleJavaFileObject {

    private static Logger log = LoggerFactory.getLogger(JavaClassObject.class);

    protected final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

    /**
     * Constructor to initialize uri of object from long class name
     *
     * @param name file name
     * @param kind file type
     */
    public JavaClassObject(String name, Kind kind) {
        super(URI.create("string:///" + name.replace('.', '/') + kind.extension), kind);
        log.debug("Creating JavaClassObject of name: {} and kind: {}", name, kind.extension);
    }

    /**
     * Return contents of java code from stream instead of file
     *
     * @return byte array
     */
    public byte[] getBytes() {
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public OutputStream openOutputStream() throws IOException {
        return byteArrayOutputStream;
    }
}