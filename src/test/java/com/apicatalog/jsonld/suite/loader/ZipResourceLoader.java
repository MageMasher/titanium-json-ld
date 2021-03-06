package com.apicatalog.jsonld.suite.loader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.apicatalog.jsonld.api.JsonLdError;
import com.apicatalog.jsonld.api.JsonLdErrorCode;
import com.apicatalog.jsonld.document.Document;
import com.apicatalog.jsonld.document.DocumentParser;
import com.apicatalog.jsonld.http.media.MediaType;
import com.apicatalog.jsonld.loader.DocumentLoader;
import com.apicatalog.jsonld.loader.DocumentLoaderOptions;

public class ZipResourceLoader implements DocumentLoader {

    @Override
    public Document loadDocument(URI url, DocumentLoaderOptions options) throws JsonLdError {
        
        if (!"zip".equals(url.getScheme())) {
            throw new JsonLdError(JsonLdErrorCode.LOADING_DOCUMENT_FAILED);
        }
        
        URL zipFileUrl = getClass().getResource("/" + url.getAuthority());

        if (zipFileUrl == null) {
            throw new JsonLdError(JsonLdErrorCode.LOADING_DOCUMENT_FAILED);
        }
            
        File zipFile = null;

        try {
            zipFile = new File(zipFileUrl.toURI());

        } catch (URISyntaxException e) {
            throw new JsonLdError(JsonLdErrorCode.LOADING_DOCUMENT_FAILED, e);
        }

        try (ZipFile zip = new ZipFile(zipFile)) {
            
            ZipEntry zipEntry = zip.getEntry(url.getPath().substring(1));

            if (zipEntry == null) {
                return null;
            }

            MediaType type = null; 
                    
            if (zipEntry.getName().endsWith(".nq")) {
                type = MediaType.N_QUADS;
                
            } else if (zipEntry.getName().endsWith(".json")) {
                type = MediaType.JSON;
                
            } else if (zipEntry.getName().endsWith(".jsonld")) {
                type = MediaType.JSON_LD;
            }
            
            if (type == null) {
                return null;
            }

            try (InputStream is = zip.getInputStream(zipEntry)) {

                final Document document = DocumentParser.parse(type, is);
                document.setDocumentUrl(url);
                
                return document;
            }
            
        } catch (IOException e) {
            throw new JsonLdError(JsonLdErrorCode.LOADING_DOCUMENT_FAILED, e);
        }
    }    
    
    public byte[] fetchBytes(URI url) throws JsonLdError {
        
        if (!"zip".equals(url.getScheme())) {
            throw new JsonLdError(JsonLdErrorCode.LOADING_DOCUMENT_FAILED);
        }
        
        URL zipFileUrl = getClass().getResource("/" + url.getAuthority());

        if (zipFileUrl == null) {
            throw new JsonLdError(JsonLdErrorCode.LOADING_DOCUMENT_FAILED);
        }
            
        File zipFile = null;

        try {
            zipFile = new File(zipFileUrl.toURI());

        } catch (URISyntaxException e) {
            throw new JsonLdError(JsonLdErrorCode.LOADING_DOCUMENT_FAILED, e);
        }

        try (ZipFile zip = new ZipFile(zipFile)) {
            
            ZipEntry zipEntry = zip.getEntry(url.getPath().substring(1));

            if (zipEntry == null) {
                return null;
            }
            
            try (InputStream is = zip.getInputStream(zipEntry)) {

                return readAsByteArray(is);
            }

            
        } catch (IOException e) {
            throw new JsonLdError(JsonLdErrorCode.LOADING_DOCUMENT_FAILED, e);
        }
    }
    
    private static final byte[] readAsByteArray(InputStream is) throws IOException {
        
        final ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();

        byte[] buffer = new byte[16384];
        int readed;
        
        while ((readed = is.read(buffer, 0, buffer.length)) != -1) {
            byteArrayStream.write(buffer, 0, readed);
        }

        return byteArrayStream.toByteArray();
    }
}
