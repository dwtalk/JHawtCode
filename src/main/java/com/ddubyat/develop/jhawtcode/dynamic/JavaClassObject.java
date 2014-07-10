package com.ddubyat.develop.jhawtcode.dynamic;

import javax.tools.SimpleJavaFileObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

public class JavaClassObject extends SimpleJavaFileObject {

    protected final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

    public JavaClassObject(String name, Kind kind) {
        super(URI.create("string:///" + name.replace('.', '/') + kind.extension), kind);
    }
    public byte[] getBytes() {
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public OutputStream openOutputStream() throws IOException {
        return byteArrayOutputStream;
    }
}