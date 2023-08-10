package com.obss.pokedex.library.rest;

public enum ResponseCode {
    SUCCESS("200"),
    WARNING("1000"),
    ERROR("1001");

    private final String value;

    public String getValue() {
        return value;
    }

    ResponseCode(String value) {
        this.value = value;
    }
}