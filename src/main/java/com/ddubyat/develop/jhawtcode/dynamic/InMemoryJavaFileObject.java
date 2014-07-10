package com.ddubyat.develop.jhawtcode.dynamic;

import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import java.net.URI;

public class InMemoryJavaFileObject extends SimpleJavaFileObject implements JavaFileObject {

    private CharSequence content;

    public InMemoryJavaFileObject(String className, CharSequence content) {
        super(URI.create("string:///" + className.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
        this.content = content;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return content;
    }
}
