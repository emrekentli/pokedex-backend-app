public interface PokeApiClient {
    GetPokemonDto getPokemons(int offset, int limit);

    GetPokemonDto getAbilities(int offset, int limit);

    GetPokemonDto getTypes(int offset, int limit);

    GetPokemonDto getStats(int offset, int limit);

    GetPokemonDetailDto getPokemonDetails(String bulbasaur);
}
