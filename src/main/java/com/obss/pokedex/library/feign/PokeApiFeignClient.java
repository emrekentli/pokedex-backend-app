package com.obss.pokedex.library.feign;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "POKEAPI",url = "https://pokeapi.co/api/v2/")
public interface PokeApiFeignClient extends PokeApiFeignCallableApi {
}
