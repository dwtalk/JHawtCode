package com.ddubyat.develop.jhawtcode.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

/**
 * ResourceUtil is used to read and manipulate local resources
 *
 * @author dwtalk
 * @version 1.0.0
 * @since 2014-07-15
 */
@Service
public class ResourceUtil {

    private static Logger log = LoggerFactory.getLogger(ResourceUtil.class);

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * Reads local resource files into a string from classpath
     * @param resourcePaths file location
     * @return String of files combined contents
     * @throws IOException
     */
    public String readLocalResource(String[] resourcePaths) throws IOException {
        log.trace("Reading resource files {}", Arrays.toString(resourcePaths));
        List<String> resources = new ArrayList<>(Arrays.asList(resourcePaths));
        String combinedResources = "";
        for(String resource: resources) {
            combinedResources += readLocalResource(resource) + System.lineSeparator();
        }
        return combinedResources;
    }

    /**
     * Reads a local resource file into a string from classpath
     * @param resourcePath file location
     * @return String of file contents
     * @throws IOException
     */
    public String readLocalResource(String resourcePath) throws IOException {
        log.trace("Reading resource file {}", resourcePath);
        Resource resFile = applicationContext.getResource(resourcePath);
        String responseFile = null;
        if(resFile != null) {
            try (Scanner fileScanner = new Scanner(resFile.getInputStream(), StandardCharsets.UTF_8.name())) {
                responseFile = fileScanner.useDelimiter("\\A").next();
            }
        }
        return responseFile;
    }

    /**
     * Compresses string data into a byte array
     * @param uncompressedData string data to be compressed
     * @return byte array of compressed string data
     * @throws IOException
     */
    public byte[] gzipCompress(String uncompressedData) throws IOException {
        log.trace("GZip Compressing Resource");
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(byteStream);
        gzip.write(uncompressedData.getBytes("UTF-8"));
        gzip.close();
        return byteStream.toByteArray();
    }

}
