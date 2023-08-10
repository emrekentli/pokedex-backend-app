import com.obss.pokedex.domain.pokemon.pokeapi.api.GetPokemonDto;
import com.obss.pokedex.domain.pokemon.pokeapi.api.PokeApiClient;
import com.obss.pokedex.domain.pokemon.pokeapi.api.PokeNameDto;
import StatDto;
import StatService;
import com.obss.pokedex.library.util.PageUtil;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatServiceImpl implements StatService {
    private final StatRepository repository;
    private final PokeApiClient client;

    @PostConstruct
    public void init() {
        var totalCount = client.getStats(0, 20).getCount();
        if (repository.count() < totalCount) {
            var allTypes = getAllTypes();
            repository.saveAll(allTypes.stream().map(this::toEntity).toList());
        }
    }

    private Stat toEntity(PokeNameDto pokeNameDto) {
        var ability = new Stat();
        ability.setName(pokeNameDto.getName());
        return ability;
    }

    public List<PokeNameDto> getAllTypes() {
        List<PokeNameDto> allTypes = new ArrayList<>();
        int offset = 0;
        int limit = 20;
        int totalPokemons = client.getTypes(0,20).getCount();
        while (offset < totalPokemons) {
            GetPokemonDto response = client.getStats(offset, limit);
            allTypes.addAll(response.getResults());
            offset += limit;
        }

        return allTypes;
    }


    public Stat findStatByName(String name) {
        return repository.findByName(name).orElseThrow(EntityNotFoundException::new);
    }

    public StatDto toDto(Stat stat) {
        return StatDto.builder()
                .id(stat.getId())
                .created(stat.getCreated())
                .modified(stat.getModified())
                .name(stat.getName())
                .build();
    }

    public Stat getEntityById(String statId) {
        return repository.findById(statId).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public StatDto createStat(StatDto dto) {
        var stat = new Stat();
        return toDto(repository.save(toEntity(stat,dto)));
    }

    @Override
    public StatDto updateStat(String id, StatDto dto) {
        var stat = getEntityById(id);
        return toDto(repository.save(toEntity(stat,dto)));
    }

    @Override
    public void deleteStat(String id) {
        repository.delete(getEntityById(id));
    }

    @Override
    public Page<StatDto> getAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::toDto);
    }

    @Override
    public StatDto getById(String id) {
        return toDto(getEntityById(id));
    }

    @Override
    public Page<StatDto> filterStats(StatDto dto, Pageable pageable) {
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues().withIgnoreCase().withStringMatcher(ExampleMatcher.StringMatcher.STARTING);
        return PageUtil.pageToDto(repository.findAll(Example.of(toEntity(new Stat(), dto),matcher), pageable), this::toDto);
    }

    private Stat  toEntity( Stat stat,StatDto dto) {
        stat.setName(dto.getName());
        return stat;
    }
}
