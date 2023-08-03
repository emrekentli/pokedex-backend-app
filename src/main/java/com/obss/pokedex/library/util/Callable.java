package com.obss.pokedex.library.util;

@FunctionalInterface
public interface Callable<T, R> {
    T call(R r);
}
