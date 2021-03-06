package com.apicatalog.jsonld.api.impl;

import java.net.URI;

import javax.json.JsonArray;

import com.apicatalog.jsonld.api.JsonLdError;
import com.apicatalog.jsonld.api.JsonLdOptions;
import com.apicatalog.jsonld.document.Document;
import com.apicatalog.jsonld.lang.Version;
import com.apicatalog.jsonld.loader.DocumentLoader;
import com.apicatalog.jsonld.processor.FromRdfProcessor;
import com.apicatalog.rdf.RdfDataset;

public final class FromRdfApi implements CommonApi<FromRdfApi>, LoaderApi<FromRdfApi> {

    // required
    private final Document document;
    private final URI documentUri;
    
    // optional
    private JsonLdOptions options;
    
    public FromRdfApi(Document document) {
        this.document = document;
        this.documentUri = null;
        this.options = new JsonLdOptions();
    }

    public FromRdfApi(URI documentUri) {
        this.document = null;
        this.documentUri = documentUri;
        this.options = new JsonLdOptions();
    }
    
    @Override
    public FromRdfApi options(JsonLdOptions options) {
        
        if (options == null) {
            throw new IllegalArgumentException("Parameter 'options' is null.");
        }

        this.options = options;
        return this;
    }
    
    @Override
    public FromRdfApi mode(Version processingMode) {
        options.setProcessingMode(processingMode);
        return this;
    }

    @Override
    public FromRdfApi base(URI baseUri) {
        options.setBase(baseUri);
        return this;
    }

    @Override
    public FromRdfApi base(String baseUri) {
        return base(URI.create(baseUri));
    }

    @Override
    public FromRdfApi ordered(boolean enable) {
        options.setOrdered(enable);
        return this;
    }
    
    @Override
    public FromRdfApi ordered() {
        return ordered(true);
    }
    
    @Override
    public FromRdfApi loader(DocumentLoader loader) {
        options.setDocumentLoader(loader);
        return this;
    }
    
    /**
     * Get <code>JSON-LD</code> representation of the provided {@link RdfDataset}.
     * 
     * @return {@link JsonArray} representing <code>JSON-LD</code> document
     * @throws JsonLdError
     */
    public JsonArray get() throws JsonLdError {
        
        if (document != null) {
            return FromRdfProcessor.fromRdf(document, options);
        }
        
        if (documentUri != null) {
            return FromRdfProcessor.fromRdf(documentUri, options);
        }
        
        throw new IllegalStateException();
    }
}
