package com.obss.pokedex.library.rest;

public class Response<T> {

    private T data;
    private MetaResponse meta;

    public Response() {
    }

    public Response(MetaResponse meta) {
        this.meta = meta;
    }

    public Response(T data) {
        this.data = data;
    }

    public MetaResponse getMeta() {
        return meta;
    }

    public T getData() {
        return data;
    }
}
