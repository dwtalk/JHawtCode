package com.ddubyat.develop.jhawtcode.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.zip.GZIPOutputStream;

@Service
public class ResourceUtil {

    @Autowired
    private ApplicationContext applicationContext;

    public String readLocalResource(String[] resourcePaths) throws IOException {
        List<String> resources = new ArrayList<>(Arrays.asList(resourcePaths));
        String combinedResources = "";
        for(String resource: resources) {
            combinedResources += readLocalResource(resource) + System.lineSeparator();
        }
        return combinedResources;
    }

    public String readLocalResource(String resourcePath) throws IOException {
        Resource resFile = applicationContext.getResource(resourcePath);
        String responseFile = null;
        if(resFile != null) {
            try (Scanner fileScanner = new Scanner(resFile.getInputStream(), StandardCharsets.UTF_8.name())) {
                responseFile = fileScanner.useDelimiter("\\A").next();
            }
        }
        return responseFile;
    }

    public static byte[] gzipCompress(String uncompressedData) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(byteStream);
        gzip.write(uncompressedData.getBytes("UTF-8"));
        gzip.close();
        return byteStream.toByteArray();
    }

}
