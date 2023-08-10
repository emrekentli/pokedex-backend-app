import GetPokemonDetailDto;
import GetPokemonDto;
import PokeApiClient;
import com.obss.pokedex.library.feign.PokeApiFeignCallableApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PokeApiClientImpl implements PokeApiClient {
    private final PokeApiFeignCallableApi callableApi;

    @Override
    public GetPokemonDto getPokemons(int offset, int limit) {
        return callableApi.getPokemons(offset, limit);
    }

    @Override
    public GetPokemonDto getAbilities(int offset, int limit) {
        return callableApi.getAbilities(offset, limit);
    }

    @Override
    public GetPokemonDto getTypes(int offset, int limit) {
        return callableApi.getTypes(offset, limit);
    }

    @Override
    public GetPokemonDto getStats(int offset, int limit) {
        return callableApi.getStats(offset, limit);
    }

    @Override
    public GetPokemonDetailDto getPokemonDetails(String pokemonName) {
        return callableApi.getPokemon(pokemonName);
    }
}
