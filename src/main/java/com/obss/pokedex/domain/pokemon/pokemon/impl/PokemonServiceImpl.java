import com.obss.pokedex.domain.authentication.user.api.UserRetrievalService;
import com.obss.pokedex.domain.authentication.user.api.UserService;
import AbilityServiceImpl;
import GetPokemonDetailDto;
import GetPokemonDto;
import PokeApiClient;
import PokeNameDto;
import AddStatDto;
import PokemonDto;
import PokemonService;
import com.obss.pokedex.domain.pokemon.pokemonstat.impl.PokemonStatServiceImpl;
import TypeServiceImpl;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Lazy})
@Log4j2
public class PokemonServiceImpl implements PokemonService {
    private final PokeApiClient client;
    private final AbilityServiceImpl abilityService;
    private final PokemonRepository repository;
    private final TypeServiceImpl typeService;
    private final PokemonStatServiceImpl pokemonStatService;
    private final UserRetrievalService userRetrievalService;
    private final UserService userService;
    @PostConstruct
    public void init() {
        var totalCount = client.getPokemons(0, 100000).getCount();
        AtomicInteger currentCount = new AtomicInteger();

        if (repository.count() < totalCount) {
            var allPokemons = getAllPokemons();
            List<GetPokemonDetailDto> pokemonDetailsList = new ArrayList<>();
            allPokemons.forEach(pokemon -> {
                currentCount.getAndIncrement();
                log.info("Getting pokemon details: {} - Progress: {}/{}", pokemon.getName(), currentCount, totalCount);
                var pokemonDetails = client.getPokemonDetails(pokemon.getName());
                pokemonDetailsList.add(pokemonDetails);
            });
            saveBulkPokemonDetails(pokemonDetailsList);
        }
    }


    private void saveBulkPokemonDetails(List<GetPokemonDetailDto> pokemonDetailsList) {
       List<Pokemon> pokemonsWithDetails = new ArrayList<>();
        for (GetPokemonDetailDto pokemonDetails : pokemonDetailsList) {
            Pokemon pokemon = repository.findPokemonByName(pokemonDetails.getName()).orElse(new Pokemon());
            pokemon.setName(pokemonDetails.getName());
            pokemon.setBaseExperience(pokemonDetails.getBaseExperience());
            pokemon.setHeight(pokemonDetails.getHeight());
            pokemon.setWeight(pokemonDetails.getWeight());
            if(pokemonDetails.getSprites().getOther().getDreamWorld().getFrontDefault() != null){
                pokemon.setImageUrl(pokemonDetails.getSprites().getOther().getDreamWorld().getFrontDefault());
            }
            else if(pokemonDetails.getSprites().getOther().getOfficialArtwork().getFrontDefault() != null){
                pokemon.setImageUrl(pokemonDetails.getSprites().getOther().getOfficialArtwork().getFrontDefault());
            }
            else{
                pokemon.setImageUrl(pokemonDetails.getSprites().getFrontDefault());
            }
            pokemon.setAbilities(abilityService.convertToAbilities(pokemonDetails.getAbilities()));
            pokemon.setStats(pokemonStatService.convertToStats(pokemonDetails.getStats(),pokemon));
            pokemon.setTypes(typeService.convertToTypes(pokemonDetails.getTypes()));
            pokemonsWithDetails.add(pokemon);
            log.info("Creating pokemon : {}", pokemon.getName());
        }
        repository.saveAll(pokemonsWithDetails);
        log.info("Bulk pokemon details saved");
    }

    @Override
    public PokemonDto createPokemon(PokemonDto dto){
        return toDto(repository.save(toEntity(new Pokemon(),dto)));
    }

    @Override
    public PokemonDto updatePokemon(String id, PokemonDto dto) {
        return toDto(repository.save(toEntity(repository.findById(id).orElseThrow(EntityNotFoundException::new),dto)));
    }

    @Override
    public void deletePokemon(String id) {
        repository.delete(repository.findById(id).orElseThrow(EntityNotFoundException::new));
    }

    @Override
    public Page<PokemonDto> getAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::toDto);
    }

    @Override
    public PokemonDto getById(String id) {
        return toDto(repository.findById(id).orElseThrow(EntityNotFoundException::new));
    }

    @Override
    public Page<PokemonDto> filterPokemons(PokemonDto dto, String type, String ability, Pageable pageable) {
        return repository.filterPokemons(dto.getName(),dto.getBaseExperience(),dto.getHeight(),dto.getWeight(),type,ability,pageable).map(this::toDto);
    }

    @Override
    public PokemonDto addType(String id, String typeId) {
        Pokemon pokemon = repository.findById(id).orElseThrow(EntityNotFoundException::new);
        pokemon.getTypes().add(typeService.getEntityById(typeId));
        return toDto(repository.save(pokemon));
    }

    @Override
    public PokemonDto removeType(String id, String typeId) {
        Pokemon pokemon = repository.findById(id).orElseThrow(EntityNotFoundException::new);
        pokemon.getTypes().remove(typeService.getEntityById(typeId));
        return toDto(repository.save(pokemon));
    }

    @Override
    public PokemonDto addAbility(String id, String abilityId) {
        Pokemon pokemon = repository.findById(id).orElseThrow(EntityNotFoundException::new);
        pokemon.getAbilities().add(abilityService.getEntityById(abilityId));
        return toDto(repository.save(pokemon));
    }

    @Override
    public PokemonDto removeAbility(String id, String abilityId) {
        Pokemon pokemon = repository.findById(id).orElseThrow(EntityNotFoundException::new);
        pokemon.getAbilities().remove(abilityService.getEntityById(abilityId));
        return toDto(repository.save(pokemon));
    }

    @Override
    public PokemonDto addStat(String id, AddStatDto dto) {
        var pokemon = repository.findById(id).orElseThrow(EntityNotFoundException::new);
        pokemon.getStats().add(pokemonStatService.createPokemonStat(pokemon,dto));
        return toDto(repository.save(pokemon));
    }

    @Override
    public PokemonDto removeStat(String id, String statId) {
        Pokemon pokemon = repository.findById(id).orElseThrow(EntityNotFoundException::new);
        pokemon.getStats().remove(pokemonStatService.getPokemonStatByPokemonAndStat(statId));
        return toDto(repository.save(pokemon));
    }

    @Override
    public List<PokemonDto> getAllUserCatchlist(PokemonDto dto, String type, String ability) {
        var user = userRetrievalService.getCurrentUserId();
        var catchlist = userService.getUserDtoById(user).getCatchList();
        return repository.findAllByIdIn(dto.getName(), dto.getBaseExperience(), dto.getHeight(), dto.getWeight(), type, ability, catchlist).stream().map(this::toDto).toList();
    }

    @Override
    public List<PokemonDto> getAllUserWishlist(PokemonDto dto, String type, String ability) {
        var user = userRetrievalService.getCurrentUserId();
        var wishList = userService.getUserDtoById(user).getWishList();
        return repository.findAllByIdIn(dto.getName(), dto.getBaseExperience(), dto.getHeight(), dto.getWeight(), type, ability, wishList).stream().map(this::toDto).toList();
    }

    private PokemonDto toDto(Pokemon pokemon) {
        return PokemonDto.builder()
                .id(pokemon.getId())
                .name(pokemon.getName())
                .baseExperience(pokemon.getBaseExperience())
                .height(pokemon.getHeight())
                .weight(pokemon.getWeight())
                .imageUrl(pokemon.getImageUrl())
                .abilities(pokemon.getAbilities() != null ? abilityService.toDtoList(pokemon.getAbilities()) : Set.of())
                .stats(pokemon.getStats() != null ? pokemonStatService.toDtoList(pokemon.getStats()): List.of())
                .types(pokemon.getTypes() != null ? typeService.toDtoList(pokemon.getTypes()) : Set.of())
                .build();
    }
    private Pokemon toEntity(Pokemon pokemon, PokemonDto dto) {
        pokemon.setName(dto.getName());
        pokemon.setBaseExperience(dto.getBaseExperience());
        pokemon.setHeight(dto.getHeight());
        pokemon.setWeight(dto.getWeight());
        pokemon.setImageUrl(dto.getImageUrl());
        return pokemon;
    }

    private List<PokeNameDto> getAllPokemons() {
        List<PokeNameDto> allPokemons = new ArrayList<>();
        int offset = 0;
        int limit = 20;
        int totalPokemons = client.getPokemons(offset,limit).getCount();
        while (offset < 20) {
            GetPokemonDto response = client.getPokemons(offset, limit);
            allPokemons.addAll(response.getResults());
            offset += limit;
        }

        return allPokemons;
    }
}
