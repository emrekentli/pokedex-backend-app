import Pokemon;
import Stat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PokemonStatRepository extends JpaRepository<PokemonStat, String> {
    Optional<PokemonStat> findPokemonStatByStat_NameAndPokemon_Name(String name, String pokemonName);

    Optional<PokemonStat> findPokemonStatByPokemonAndStat(Pokemon pokemon, Stat stat);

    boolean existsByPokemonAndStat(Pokemon pokemon, Stat entityById);
}
