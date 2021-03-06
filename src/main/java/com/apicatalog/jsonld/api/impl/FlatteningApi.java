package com.apicatalog.jsonld.api.impl;

import java.net.URI;

import javax.json.JsonStructure;

import com.apicatalog.jsonld.api.JsonLdError;
import com.apicatalog.jsonld.api.JsonLdOptions;
import com.apicatalog.jsonld.document.Document;
import com.apicatalog.jsonld.document.JsonDocument;
import com.apicatalog.jsonld.lang.Version;
import com.apicatalog.jsonld.loader.DocumentLoader;
import com.apicatalog.jsonld.processor.FlatteningProcessor;
import com.apicatalog.jsonld.uri.UriUtils;

public final class FlatteningApi implements CommonApi<FlatteningApi>, LoaderApi<FlatteningApi>, ContextApi<FlatteningApi> {

    // required
    private final URI documentUri;
    private final Document document;
    
    // optional
    private Document context;
    private URI contextUri;
    private JsonLdOptions options;
    
    public FlatteningApi(URI documentUri) {
        this.document = null;
        this.documentUri = documentUri;
        this.context = null;
        this.contextUri = null;
        this.options = new JsonLdOptions();
    }

    public FlatteningApi(Document document) {
        this.document = document;
        this.documentUri = null;
        this.context = null;
        this.contextUri = null;
        this.options = new JsonLdOptions();
    }

    @Override
    public FlatteningApi options(JsonLdOptions options) {
        
        if (options == null) {
            throw new IllegalArgumentException("Parameter 'options' is null.");
        }

        this.options = options;
        return this;
    }

    @Override
    public FlatteningApi mode(Version processingMode) {
        options.setProcessingMode(processingMode);
        return this;
    }

    @Override
    public FlatteningApi base(URI baseUri) {
        options.setBase(baseUri);
        return this;
    }

    @Override
    public FlatteningApi base(String baseLocation) {
        
        if (baseLocation != null && !UriUtils.isNotURI(baseLocation)) {
            throw new IllegalArgumentException("Base location must be valid URI or null but is [" + baseLocation + ".");
        }
        
        return base(baseLocation != null ? UriUtils.create(baseLocation) : null);
    }

    public FlatteningApi compactArrays(boolean enable) {
        options.setCompactArrays(enable);
        return this;
    }

    public FlatteningApi compactArrays() {
        return compactArrays(true);
    }

    @Override
    public FlatteningApi loader(DocumentLoader loader) {
        options.setDocumentLoader(loader);
        return this;
    }

    @Override
    public FlatteningApi ordered(boolean enable) {
        options.setOrdered(enable);
        return this;
    }

    @Override
    public FlatteningApi ordered() {
        return ordered(true);
    }

    @Override
    public FlatteningApi context(URI contextUri) {
        this.contextUri = contextUri;
        return this;
    }
    
    @Override
    public FlatteningApi context(final String contextLocation) {
        
        if (contextLocation != null && !UriUtils.isNotURI(contextLocation)) {
            throw new IllegalArgumentException("Context location must be valid URI or null but is [" + contextLocation + ".");
        }
        
        return context(contextLocation != null ? UriUtils.create(contextLocation) : null);
    }

    @Override
    public FlatteningApi context(JsonStructure context) {
        this.context = context != null ?  JsonDocument.of(context) : null;
        return this;
    }

    @Override
    public FlatteningApi context(Document context) {
        this.context = context;
        return this;
    }

    /**
     * Get the result of flattening.
     * 
     * @return {@link JsonStructure} representing flattened document
     * @throws JsonLdError
     */
    public JsonStructure get() throws JsonLdError {

        if (document != null && context != null) {
            return FlatteningProcessor.flatten(document, context, options);
        }

        if (document != null && contextUri != null) {
            return FlatteningProcessor.flatten(document, contextUri, options);
        }

        if (document != null) {
            return FlatteningProcessor.flatten(document, (Document)null, options);
        }

        if (documentUri != null && context != null) {
            return FlatteningProcessor.flatten(documentUri, context, options);
        }

        if (documentUri != null && contextUri != null) {
            return FlatteningProcessor.flatten(documentUri, contextUri, options);
        }

        if (documentUri != null) {
            return FlatteningProcessor.flatten(documentUri, (Document)null, options);
        }

        throw new IllegalStateException();
    }
}
